package lv.celokopa.config.root;


import com.allanditzel.springframework.security.web.csrf.CsrfTokenResponseHeaderBindingFilter;
import javax.sql.DataSource;
import lv.celokopa.app.security.AjaxAuthenticationFailureHandler;
import lv.celokopa.app.security.AjaxAuthenticationSuccessHandler;
import lv.celokopa.app.security.SecurityUserDetailsService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;

/**
 *
 * The Spring Security configuration for the application - its a form login config with authentication via session cookie (once logged in),
 * with fallback to HTTP Basic for non-browser clients.
 *
 * The CSRF token is put on the reply as a header via a filter, as there is no server-side rendering on this app.
 *
 */
@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger LOGGER = Logger.getLogger(AppSecurityConfig.class);

    @Autowired
    private SecurityUserDetailsService userDetailsService;



    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        CsrfTokenResponseHeaderBindingFilter csrfTokenFilter = new CsrfTokenResponseHeaderBindingFilter();
        http.addFilterAfter(csrfTokenFilter, CsrfFilter.class);

        http
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/").permitAll()
            .antMatchers(HttpMethod.GET, "/login").permitAll()
            .antMatchers(HttpMethod.GET, "/restore-password").permitAll()
            .antMatchers(HttpMethod.GET, "/search-results").permitAll()
            .antMatchers(HttpMethod.GET, "/ride/*").permitAll()
            .antMatchers(HttpMethod.GET, "/api/drive/*").permitAll()
            .antMatchers(HttpMethod.GET, "/register").permitAll()
            .antMatchers(HttpMethod.GET, "/api/activate").permitAll()
            .antMatchers(HttpMethod.GET, "/activated").permitAll()
            .antMatchers(HttpMethod.GET, "/not-activated").permitAll()
            .antMatchers(HttpMethod.POST, "/api/user").permitAll()
            .antMatchers(HttpMethod.POST, "/api/drive/find").permitAll()
            .antMatchers(HttpMethod.POST, "/api/password/restore").permitAll()
            .antMatchers(HttpMethod.GET, "/api/autocomplete").permitAll()
            .antMatchers(HttpMethod.GET, "/facebookCallback").permitAll()
            .antMatchers(HttpMethod.GET, "/draugiemCallback").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .defaultSuccessUrl("/")
            .loginProcessingUrl("/api/authenticate")
            .usernameParameter("username")
            .passwordParameter("password")
            .successHandler(new AjaxAuthenticationSuccessHandler(new SavedRequestAwareAuthenticationSuccessHandler()))
            .failureHandler(new AjaxAuthenticationFailureHandler())
            .loginPage("/login")
            .and()
            .httpBasic()
            .and()
            .logout()
            .logoutUrl("/api/logout")
            .logoutSuccessUrl("/")
            .permitAll();

        http.exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
            if (authException != null) {
                response.sendRedirect("/");
            }
        });

        if ("true".equals(System.getProperty("httpsOnly"))) {
            LOGGER.info("launching the application in HTTPS-only mode");
            http.requiresChannel().anyRequest().requiresSecure();
        }
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

}

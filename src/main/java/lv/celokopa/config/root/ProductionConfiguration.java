package lv.celokopa.config.root;

import lv.celokopa.app.init.TestDataInitializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alex on 16.23.10.
 */
@Configuration
@Profile("production")
@EnableTransactionManagement
public class ProductionConfiguration extends WebMvcConfigurerAdapter {

    @Value("${draugiem.app.key}")
    String draugiemAppKey;

    @Bean(initMethod = "init")
    public TestDataInitializer initTestData() {
        return new TestDataInitializer();
    }

    @Bean(name = "datasource")
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/driveby?autoReconnect=true&amp;useUnicode=true&amp;createDatabaseIfNotExist=true&amp;characterEncoding=utf-8");
        dataSource.setUsername("admin");
        dataSource.setPassword("123");
        return dataSource;
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DriverManagerDataSource dataSource) {

        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan(new String[]{"lv.celokopa.app.model"});
        entityManagerFactoryBean.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Map<String, Object> jpaProperties = new HashMap<String, Object>();
        jpaProperties.put("hibernate.hbm2ddl.auto", "create");
        jpaProperties.put("hibernate.show_sql", "false");
        jpaProperties.put("hibernate.format_sql", "true");
        jpaProperties.put("hibernate.use_sql_comments", "true");
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        jpaProperties.put("hibernate.connection.CharSet", "utf8");
        jpaProperties.put("hibernate.connection.characterEncoding", "utf8");
        jpaProperties.put("hibernate.connection.useUnicode", "true");
        entityManagerFactoryBean.setJpaPropertyMap(jpaProperties);

        return entityManagerFactoryBean;
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver=new CommonsMultipartResolver();
        resolver.setDefaultEncoding("utf-8");
        return resolver;
    }

    @Bean
    @Qualifier("pictureFilePath")
    public String pictureFilePath(){
        return "/root/foto";
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.setOrder(-1).addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    @Bean(name = "draugiemAppKey")
    public String draugiemAppKey(){
        return draugiemAppKey;
    }

}


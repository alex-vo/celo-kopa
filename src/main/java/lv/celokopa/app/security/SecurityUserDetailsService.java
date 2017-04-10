package lv.celokopa.app.security;


import lv.celokopa.app.dao.UserRepository;
import lv.celokopa.app.model.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * UserDetails service that reads the user credentials from the database, using a JPA repository.
 *
 */
@Service
public class SecurityUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = Logger.getLogger(SecurityUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(usernameOrEmail);
        if(user == null){
            user = userRepository.findUserByEmail(usernameOrEmail);
        }

        if (user == null || !user.getIsActive()) {
            String message = "Username/email not found " + usernameOrEmail;
            LOGGER.info(message);
            throw new UsernameNotFoundException(message);
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        LOGGER.info("Found user in database: " + user);

//        return new org.springframework.security.core.userdetails.User(username, user.getPasswordDigest(), authorities);
        return CustomUser.createWithLanguage(usernameOrEmail, user.getPasswordDigest(),
                authorities, user.getPreferredLanguage());
    }
}

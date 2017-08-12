package lv.celokopa.test;


import lv.celokopa.app.dto.NewUserDTO;
import lv.celokopa.app.model.User;
import lv.celokopa.app.services.UserService;
import lv.celokopa.config.root.RootContextConfig;
import lv.celokopa.config.root.TestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes={TestConfiguration.class, RootContextConfig.class})
public class UserServiceTest {

    public static final String USERNAME = "test123";

    @Autowired
    private UserService userService;

    @PersistenceContext
    private EntityManager em;

    @Test
    public void testFindUserByUsername() {
        User user = findUserByUsername(USERNAME);
        assertNotNull("User is mandatory",user);
        assertTrue("Unexpected user " + user.getUsername(), user.getUsername().equals(USERNAME));
    }

    @Test
    public void testUserNotFound() {
        User user = findUserByUsername("doesnotexist");
        assertNull("User must be null", user);
    }

    @Test
    public void testCreateValidUser() {
        NewUserDTO newUserDTO = new NewUserDTO();
        newUserDTO.setUsername("test456");
        newUserDTO.setEmail("test@gmail.com");
        newUserDTO.setPlainTextPassword("Password3");
        userService.createUser(newUserDTO, null);
        User user = findUserByUsername("test456");

        assertTrue("username not expected " + user.getUsername(), "test456".equals(user.getUsername()) );
        assertTrue("email not expected " + user.getEmail(), "test@gmail.com".equals(user.getEmail()) );

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        assertTrue("password not expected " + user.getPasswordDigest(), passwordEncoder.matches("Password3",user.getPasswordDigest()) );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlankUser() {
        NewUserDTO newUserDTO = new NewUserDTO();
        newUserDTO.setUsername("");
        newUserDTO.setEmail("test@gmail.com");
        newUserDTO.setPlainTextPassword("Password3");
        userService.createUser(newUserDTO, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUsernameLength() {
        NewUserDTO newUserDTO = new NewUserDTO();
        newUserDTO.setUsername("test");
        newUserDTO.setEmail("test@gmail.com");
        newUserDTO.setPlainTextPassword("Password3");
        userService.createUser(newUserDTO, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUsernameAvailable() {
        NewUserDTO newUserDTO = new NewUserDTO();
        newUserDTO.setUsername("test123");
        newUserDTO.setEmail("test@gmail.com");
        newUserDTO.setPlainTextPassword("Password3");
        userService.createUser(newUserDTO, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlankEmail() {
        NewUserDTO newUserDTO = new NewUserDTO();
        newUserDTO.setUsername("test001");
        newUserDTO.setEmail(null);
        newUserDTO.setPlainTextPassword("Password3");
        userService.createUser(newUserDTO, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEmail() {
        NewUserDTO newUserDTO = new NewUserDTO();
        newUserDTO.setUsername("test001");
        newUserDTO.setEmail("test");
        newUserDTO.setPlainTextPassword("Password3");
        userService.createUser(newUserDTO, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlankPassword() {
        NewUserDTO newUserDTO = new NewUserDTO();
        newUserDTO.setUsername("test002");
        newUserDTO.setEmail("test@gmail.com");
        newUserDTO.setPlainTextPassword("");
        userService.createUser(newUserDTO, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPasswordPolicy() {
        NewUserDTO newUserDTO = new NewUserDTO();
        newUserDTO.setUsername("test003");
        newUserDTO.setEmail("test@gmail.com");
        newUserDTO.setPlainTextPassword("Password");
        userService.createUser(newUserDTO, null);
    }


    private User findUserByUsername(String username) {
        List<User> users = em.createQuery("select u from User u where username = :username")
                .setParameter("username", username).getResultList();

        return users.size() == 1 ? users.get(0) : null;
    }


}

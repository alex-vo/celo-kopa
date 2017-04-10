package lv.celokopa.app.dao;


import lv.celokopa.app.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 *
 * Repository class for the User entity
 *
 */
@Repository
public class UserRepository {

    @PersistenceContext
    private EntityManager em;

    public User findUserByUsername(String username) {

        List<User> users = em.createNamedQuery(User.FIND_BY_USERNAME, User.class)
                .setParameter("username", username)
                .getResultList();

        return users.size() == 1 ? users.get(0) : null;
    }

    public User findUserByEmail(String email) {

        List<User> users = em.createNamedQuery(User.FIND_BY_EMAIL, User.class)
                .setParameter("email", email)
                .getResultList();

        return users.size() == 1 ? users.get(0) : null;
    }

    public void save(User user) {
        em.merge(user);
    }

    public boolean isUsernameAvailable(String username) {

        List<User> users = em.createNamedQuery(User.FIND_BY_USERNAME, User.class)
                .setParameter("username", username)
                .getResultList();

        return users.isEmpty();
    }
}

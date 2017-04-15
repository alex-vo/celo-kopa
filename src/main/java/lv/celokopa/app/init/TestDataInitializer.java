package lv.celokopa.app.init;


import lv.celokopa.app.model.Drive;
import lv.celokopa.app.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;
import java.util.Date;

/**
 *
 * This is a initializing bean that inserts some test data in the database. It is only active in
 * the development profile, to see the data login with user123 / PAssword2 and do a search starting on
 * 1st of January 2015.
 *
 */
@Component
public class TestDataInitializer {

    @Autowired
    private EntityManagerFactory entityManagerFactory;


    public void init() throws Exception {

        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        User user = new User("test123", "Ivan", "Demidov", new Date(80, 3, 3), "About him", "Honda Accord",
                             "AA-7777", "$2a$10$x9vXeDsSC2109FZfIJz.pOZ4dJ056xBpbesuMJg3jZ.ThQkV119tS",
                             "test@email.com", true, null, "/resources/static/test123.jpg", "lv_LV", null, null, null);
        User user1 = new User("alexvo", "Leha", "Voroncov", new Date(80, 3, 3), "About him", "Honda Accord",
                              "AA-7777", "$2a$04$6vP8bXbRBC5MXjBC3ZRM6e5ouMFP9oLJ3fCjR/9HEB33/I4GjKKaK",
                              "avoroncovs@gmail.com", true, null, "/resources/static/test123.jpg", "lv_LV", null, null, null);
        session.persist(user);
        session.persist(user1);

        session.persist(new Drive(user, "A drive to Dpils", "Rīga", "Daugavpils", "Brivibas 20", "Skolas iela 25",
                                  "28899747", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()+1000*60*60*3), "I go Dpils", 20.,
                                  "BA-8283", 3, 4, "lv_LV"));
        session.persist(new Drive(user, "A drive to Liepaja", "Rīga", "Liepāja", "Kalku 10", "Rigas iela 25",
                                  "28899747", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()+1000*60*60*3), "I go Liepaja", 10.,
                "EH-1409", 3, 4, "lv_LV"));
        session.persist(new Drive(user, "A drive to Ventspils", "Rīga", "Ventspils", "Centrala stacija", "Raina iela 15",
                                  "28899747", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()+1000*60*60*3), "I go Ventspils", 15.,
                "HD-2658", 3, 4, "ru_RU"));
        session.persist(new Drive(user, "Поездка в вентак", "Rīga", "Ventspils", "Маскачка", "Парвента",
                                  "28899747", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()+1000*60*60*3), "Весёлое путешествие", 20.,
                "AA-1234", 2, 5, "ru_RU"));

//        session.persist(new Locality("Rīga", "", ""));
//        session.persist(new Locality("Ventspils", "", ""));
//        session.persist(new Locality("Jelgava", "", ""));
//        session.persist(new Locality("Liepāja", "", ""));
//        session.persist(new Locality("Vanagi", "", ""));
//        session.persist(new Locality("Valmiera", "", ""));
//        session.persist(new Locality("Valmiermuiža", "", ""));
//        session.persist(new Locality("Vabole", "", ""));
//        session.persist(new Locality("Vadakste", "", ""));


        transaction.commit();
    }
}

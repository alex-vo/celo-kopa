package lv.celokopa.app.dao;

import lv.celokopa.app.model.Drive;
import lv.celokopa.app.model.User;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 15.30.12.
 */
@Repository
public class DriveRepository {
    private static final Logger LOGGER = Logger.getLogger(DriveRepository.class);

    @PersistenceContext
    EntityManager em;

    @Transactional
    public Drive save(Drive drive) {
        return em.merge(drive);
    }

    @Transactional
    public void delete(Long deletedDriveId) {
        Drive delete = em.find(Drive.class, deletedDriveId);
        em.remove(delete);
    }

    public List<Drive> findDrives(String username){
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Drive> searchQuery = cb.createQuery(Drive.class);
        Root<Drive> searchRoot = searchQuery.from(Drive.class);
        searchQuery.select(searchRoot);

        List<Predicate> predicates = new ArrayList<Predicate>();
        Join<Drive, User> user = searchRoot.join("user", JoinType.INNER);

        predicates.add(cb.equal(user.<String>get("username"), username));

        searchQuery.where(predicates.toArray(new Predicate[]{}));
        TypedQuery<Drive> filterQuery = em.createQuery(searchQuery);
        return filterQuery.getResultList();
    }

    public List<Drive> findDrives(String from, String to){
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Drive> searchQuery = cb.createQuery(Drive.class);
        Root<Drive> searchRoot = searchQuery.from(Drive.class);
        searchQuery.select(searchRoot);

        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(cb.equal(cb.upper(searchRoot.get("driveFrom")), from.toUpperCase()));
        predicates.add(cb.equal(cb.upper(searchRoot.get("driveTo")), to.toUpperCase()));

        searchQuery.where(predicates.toArray(new Predicate[]{}));
        TypedQuery<Drive> filterQuery = em.createQuery(searchQuery);
        return filterQuery.getResultList();
    }

    public Drive findDriveById(Long id) {
        return em.find(Drive.class, id);
    }
}

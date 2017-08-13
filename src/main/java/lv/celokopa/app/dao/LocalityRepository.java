package lv.celokopa.app.dao;

import lv.celokopa.app.model.Locality;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

/**
 * Created by alex on 16.10.5.
 */
@Repository
public class LocalityRepository {
    private static final Logger LOGGER = Logger.getLogger(LocalityRepository.class);

    @PersistenceContext
    EntityManager em;

    public List<Locality> findLocalities(String predicate){
        LOGGER.info("findLocalities() invoked");
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Locality> searchQuery = cb.createQuery(Locality.class);
        Root<Locality> searchRoot = searchQuery.from(Locality.class);
        searchQuery.select(searchRoot);

        //TODO: prevent possible sql injections
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.like(cb.upper(searchRoot.get("title")), predicate.toUpperCase() + "%"));

        searchQuery.where(predicates.toArray(new Predicate[]{}));

        TypedQuery<Locality> filterQuery = em.createQuery(searchQuery);
        List<Locality> result = filterQuery.setMaxResults(10).getResultList();
        LOGGER.info("findLocalities() finished returning " + result.size() + " localities");
        return result;
    }
}

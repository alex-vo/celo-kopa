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
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Locality> searchQuery = cb.createQuery(Locality.class);
        Root<Locality> searchRoot = searchQuery.from(Locality.class);
        searchQuery.select(searchRoot);

        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(cb.like(cb.upper(searchRoot.<String>get("title")), predicate.toUpperCase() + "%"));

        searchQuery.where(predicates.toArray(new Predicate[]{}));
//        searchQuery.orderBy(cb.desc(searchRoot.get("")));
//        searchQuery.groupBy(searchRoot.<String>get("title"));
        TypedQuery<Locality> filterQuery = em.createQuery(searchQuery);
        return filterQuery.getResultList();

//        String hql = "from Locality l " +
//                "where upper(l.title) like '%" + predicate.toUpperCase() + "%' " +
////                "group by l.title " +
//                "ORDER BY LOCATE('" + predicate.toUpperCase() + "', upper(l.title))";
//        Query query = em.createQuery(hql);
//        ProjectionList projList = Projections.projectionList();
//        projList.add(Projections.property("id.state"));
//        projList.add(Projections.property("id.uspsCity"));
//
//
//        List<Locality> results = query.getResultList();
//        return results;

//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Locality> criteria = cb.createQuery(Locality.class);
//        Root<Locality> searchRoot = criteria.from(Locality.class);
//        criteria.select(searchRoot);
//
//        List<Predicate> predicates = new ArrayList<Predicate>();
//        predicates.add(cb.like(cb.upper(searchRoot.<String>get("title")), predicate.toUpperCase() + "%"));
//
//        criteria.where(predicates.toArray(new Predicate[]{}));
//        criteria.addO


//        Session s = (Session) em.getDelegate();
//        List result = (List<Locality>) s.createSQLQuery(
////        TypedQuery<Locality> result = em.createQuery(
//                "select * from LOCALITY "+
////                        "where upper(title) like :likePredicate " +
//                        "group by title "
////                        "ORDER BY LOCATE(:predicate, upper(title))"
//                ).addEntity(Locality.class)
////                .setParameter("likePredicate", "%" + predicate.toUpperCase() + "%")
//                .list();
////                .setParameter("predicate", predicate);
//        return result;

//        Query q = em.createNativeQuery("SELECT l.*\n" +
//                "FROM LOCALITY l\n" +
//                "WHERE upper(l.title) LIKE CONVERT(? USING binary)\n" +
//                "order by LOCATE(CONVERT(? USING binary) , upper(l.title))", Locality.class);
//        q.setParameter(1, "%" + predicate.toUpperCase() + "%");
//        q.setParameter(2, predicate.toUpperCase());

//        Criteria c = s.createCriteria(Locality.class);
//        c.add(Restrictions.like("title", "CONVERT('%" + predicate + "%' USING binary)").ignoreCase());
//
//        ProjectionList projList = Projections.projectionList();
//        projList.add(Projections.groupProperty("title"), "title");
//
//        c.setProjection(projList);
//        c.addOrder(CustomizedOrderBy.sqlFormula("LOCATE(CONVERT('" + predicate.toUpperCase() + "' USING binary) , upper(title))"));
//        c.setResultTransformer(Transformers.aliasToBean(Locality.class));
//        TypedQuery<Locality> filterQuery = em.createQuery(c);
//        return filterQuery.getResultList();

//        List<Locality> result = (List<Locality>) q.getResultList();
//        return result;
    }
}

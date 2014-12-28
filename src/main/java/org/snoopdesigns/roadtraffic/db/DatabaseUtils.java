package org.snoopdesigns.roadtraffic.db;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class DatabaseUtils {

    private EntityManager em;
    private List<LearningRules> learningRuleses = new ArrayList<LearningRules>();

    public DatabaseUtils(EntityManager entityManager) {
        this.em = entityManager;
        deleteAllStatistics();
        deleteAllPaths();
        //deleteAllRules();
    }

    public List<RoadPath> getAllPaths() {
        try {
            return em.createQuery("SELECT t FROM RoadPath t").getResultList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public List<PathStatistics> getAllStatistics() {
        try {
            return em.createQuery("SELECT t FROM PathStatistics t").getResultList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public void addNewPath(RoadPath path) {
        em.getTransaction().begin();
        em.persist(path);
        em.getTransaction().commit();
    }

    public RoadPath getPathById(Integer id) {
        return em.find(RoadPath.class, id);
    }

    public void updatePathSpeed(Integer pathId, Integer speed) {
        em.getTransaction().begin();
        RoadPath path = em.find(RoadPath.class, pathId);
        path.setPathSpeed(speed);
        em.merge(path);
        em.getTransaction().commit();
    }

    private void deleteAllPaths() {
        for(RoadPath path : getAllPaths()) {
            em.getTransaction().begin();
            em.remove(path);
            em.getTransaction().commit();
        }
    }

    public void deleteAllStatistics() {
        for(PathStatistics st : getAllStatistics()) {
            em.getTransaction().begin();
            em.remove(st);
            em.getTransaction().commit();
        }
    }

    public void addNewPathStatistics(PathStatistics statistics) {
        em.getTransaction().begin();
        em.persist(statistics);
        em.getTransaction().commit();
    }

    public List<PathStatistics> getStatisticsByPath(RoadPath path) {
        try {
            return em.createQuery("SELECT t FROM PathStatistics t where t.pathId=" + path.getId()).getResultList();
        } catch(Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public void addNewRule(LearningRules rule) {
        /*em.getTransaction().begin();
        em.persist(rule);
        em.getTransaction().commit();*/
        learningRuleses.add(rule);
    }

    public List<LearningRules> getAllRules() {
        /*try {
            return em.createQuery("SELECT t FROM LearningRules t").getResultList();
        } catch (Exception e) {
            return Collections.emptyList();
        }*/
        return learningRuleses;
    }

    public void deleteAllRules() {
        /*for(LearningRules rule : getAllRules()) {
            em.getTransaction().begin();
            em.remove(rule);
            em.getTransaction().commit();
        }*/
        learningRuleses.clear();
    }

    public void destroy() {
        em.flush();
        em.close();
    }

}

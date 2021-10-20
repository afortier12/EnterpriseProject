package ec.project.ml;

import java.util.List;

import javax.persistence.EntityManager;

public class EvaluationsRepository {
	
	private EntityManager entityManager;
    public EvaluationsRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    public Evaluations save(Evaluations evaluation) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(evaluation);
            entityManager.getTransaction().commit();
            return evaluation;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public int createEvaluation(Evaluations evaluation) {
    	entityManager.persist(evaluation);
        if (entityManager.contains(evaluation)) {
        	entityManager.flush();
        	return 1;
        } else {
        	return 0;
        }
    }

    
    public List<Evaluations> findAll() {
        return entityManager.createQuery("from Evaluations").getResultList();
    }


    public Evaluations findByName(String name) {
        List<Evaluations> users = entityManager.createNamedQuery("Model.findByName", Evaluations.class)
                .setParameter("name", name)
                .getResultList();
        if (users.size() == 0) return null;
        else return users.get(0);
    }

}

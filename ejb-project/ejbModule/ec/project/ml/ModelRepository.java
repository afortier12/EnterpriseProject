package ec.project.ml;

import javax.persistence.EntityManager;

import ec.project.ml.Model;
import ec.project.users.User;

import java.util.List;
import java.util.Optional;

public class ModelRepository {
    private EntityManager entityManager;
    public ModelRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    public Model save(Model model) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(model);
            entityManager.getTransaction().commit();
            return model;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    public Model createModel(Model model) {
        entityManager.persist(model);
        if (entityManager.contains(model)) {
        	return model;
        } else {
        	return null;
        }
    }
    
    public int removeModel(Model model) {
        entityManager.remove(model);
        if (!entityManager.contains(model)) {
        	entityManager.flush();
        	return 1;
        } else {
        	return 0;
        }
    }
    

    public Model get() {
        Model model = entityManager.createQuery("select m from Model m ",  Model.class).getSingleResult();
        return model != null ? model : null;
    }
    
    public Model getByName(String name) {
    	try {
	        Model model = entityManager.createQuery("select m from Model m where m.name = :name ",  Model.class).setParameter("name", name).getSingleResult();
	        return model != null ? model : null;
    	} catch (Exception e) {
    		return null;
    	}
    }
    
    
}
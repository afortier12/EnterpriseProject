package ec.project.data;

import java.util.List;

import javax.persistence.EntityManager;

import ec.project.data.Data;

public class DataRepository {
    private EntityManager entityManager;
    public DataRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    public Data save(Data model) {
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
    
    public int addInstances(Data data) {
        entityManager.persist(data);
        if (entityManager.contains(data)) {
        	entityManager.flush();
        	return 1;
        } else {
        	return 0;
        }
    }
    
    public int removeInstances(Data data) {
        entityManager.remove(data);
        if (!entityManager.contains(data)) {
        	entityManager.flush();
        	return 1;
        } else {
        	return 0;
        }
    }

    public Data getByName(String name) {
    	try {
	        Data data = entityManager.createQuery("select m from Data m where m.name = :name ",  Data.class).setParameter("name", name).getSingleResult();
	        return data != null ? data : null;
    	} catch (Exception e) {
    		return null;
    	}
    }


    public List<Data> getAllData() {
        List<Data> data = entityManager.createNamedQuery("Data.getAllData", Data.class)
                .getResultList();
        if (data.size() == 0) return null;
        else return data;

    }
}
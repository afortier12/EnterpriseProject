package ec.project.bean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ec.project.data.DataRepository;
import ec.project.data.Model;
import ec.project.data.ModelRepository;
import ec.project.data.Data;
import weka.classifiers.functions.LinearRegression;
import weka.core.Instance;
import weka.core.Instances;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class DataBean implements DataBeanI  {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    
    @Autowired
    private DataRepository dataRep;
    
    @Override
    public List<Data> findAll() {
    	List<Data> data = entityManager.createQuery("select m from Data m ",  Data.class).getResultList();
        return data != null ? data : null;
    }
    
  
      
}

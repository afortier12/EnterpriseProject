package ec.project.bean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ec.project.data.Model;
import ec.project.data.ModelRepository;
import weka.classifiers.functions.LinearRegression;
import weka.core.Instance;
import weka.core.Instances;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class ModelBean implements ModelBeanI  {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private ModelRepository repository;
    
    @Override
    public List<Model> findAll() {
        List<Model> model = entityManager.createQuery("select m from Model m ",  Model.class).getResultList();
        return model != null ? model : null;
    }

    @Override
	public double getPrediction(Instances predicationDataSet, Model model) {
		
		//predict
		Instance predicationDataInstance = predicationDataSet.lastInstance();
		
		double value = 0;
		
		try {		
			byte[] model_bytes = model.getModel();
			
			ObjectInputStream objectIn = null;
			if (model_bytes != null)
				objectIn = new ObjectInputStream(new ByteArrayInputStream(model_bytes));
			
			LinearRegression cls = (LinearRegression) objectIn.readObject();

			 // label instances
		    for (int i = 0; i < predicationDataSet.numInstances(); i++) {
		      double clsLabel = cls.classifyInstance(predicationDataSet.instance(i));
		      System.out.println(predicationDataSet.instance(i));
		      predicationDataSet.instance(i).setClassValue(clsLabel);
		    }
		    //System.out.println(predicationDataSet.toString());
			value = cls.classifyInstance(predicationDataInstance);
			System.out.println(value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}


    
    
}

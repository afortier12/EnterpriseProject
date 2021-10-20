package ec.project.beans;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.logging.Logger;

import ec.project.ml.Data;
import ec.project.ml.DataRepository;
import ec.project.ml.Evaluations;
import ec.project.ml.Model;
import ec.project.ml.ModelRepository;
import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.experiment.InstanceQuery;

/**
 * Session Bean implementation class ModelEJBStateless
 */
@Stateless
@LocalBean
public class ModelEJBStateless implements ModelEJBStatelessRemote, ModelEJBStatelessLocal {
	
	
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/test";
	static final String USER = "root";
	static final String PASS = "adam";
	static final String TABLE = "tool_data";
	
    /**
     * Default constructor. 
     */
    public ModelEJBStateless() {
        // TODO Auto-generated constructor stub
    }
    
    
    private static final Logger LOGGER = Logger.getLogger(Evaluations.class);
    
	@PersistenceContext(unitName="primary")
    private EntityManager entityManager;
    private ModelRepository modelRep;


    public Classifier loadModel(String name) {
		modelRep = new ModelRepository(entityManager);
		Model model = (Model) modelRep.getByName(name);
		
		try {
			if (model != null) {
				byte[] data_bytes = model.getModel();
				
				ObjectInputStream objectIn = null;
				if (data_bytes != null) {
					objectIn = new ObjectInputStream(new ByteArrayInputStream(data_bytes));
					Classifier cls = (Classifier) objectIn.readObject();
					return cls;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
    }
	
	

}

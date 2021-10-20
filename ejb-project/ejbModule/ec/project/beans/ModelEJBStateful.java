

package ec.project.beans;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.logging.Logger;

import ec.project.ml.Data;
import ec.project.ml.DataRepository;
import ec.project.ml.Evaluations;
import ec.project.ml.Model;
import ec.project.ml.ModelRepository;
import weka.attributeSelection.PrincipalComponents;
import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.DatabaseSaver;

/**
 * Session Bean implementation class ModelEJBStateful
 */
@Stateful
@LocalBean
public class ModelEJBStateful implements ModelEJBStatefulRemote, ModelEJBStatefulLocal {
	
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/test";
	static final String USER = "root";
	static final String PASS = "adam";

    /**
     * Default constructor. 
     */
    public ModelEJBStateful() {
        // TODO Auto-generated constructor stub
    }
    
    private static final Logger LOGGER = Logger.getLogger(Evaluations.class);
    
	@PersistenceContext(unitName="primary")
    private EntityManager entityManager;
    private ModelRepository modelrep;
    private DataRepository dataRep;
    
	public void saveData(Instances data, String name) {

		dataRep = new DataRepository(entityManager);
		Data check = dataRep.getByName(name);
		if (check != null) {
			dataRep.removeInstances(check);
		}
		Data instanceData = new Data();
		
		ByteArrayOutputStream stream = null;
		ObjectOutputStream out = null;
		try {		
			instanceData.setName(name);
			
			stream = new ByteArrayOutputStream();
			out = new ObjectOutputStream(stream);   
			out.writeObject(data);
			out.flush();
			byte[] dataBytes = stream.toByteArray();
			instanceData.setData(dataBytes);
			
			dataRep.addInstances(instanceData);
			
			stream = null;
			out=null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public Instances split(Instances data) {
		data.randomize(new java.util.Random(0));
		int trainSize = (int) Math.round(data.numInstances() * 0.8);
		int testSize = data.numInstances() - trainSize;
		Instances train = new Instances(data, 0, trainSize);
		Instances test = new Instances(data, trainSize, testSize);
		
		saveData(train, "train");
		saveData(test, "test");
		saveData(data, "processed");
		
		
		return train;
	
	}
	
	@Override
	public void saveModel(Classifier cls, String name, PrincipalComponents pca) {
		
		Model model = new Model();
		
		modelrep = new ModelRepository(entityManager);
		Model check = modelrep.getByName(name);
		if (check != null) {
			modelrep.removeModel(check);
		}
		
		ByteArrayOutputStream stream = null;
		ObjectOutputStream out = null;
		try {
			stream = new ByteArrayOutputStream();
			out = new ObjectOutputStream(stream);   
			out.writeObject(cls);
			out.flush();
			byte[] objBytes = stream.toByteArray();
			model.setModel(objBytes);
			out=null;
			stream=null;
			
			stream = new ByteArrayOutputStream();
			out = new ObjectOutputStream(stream); 
			out.writeObject(pca);
			out.flush();
			byte[] pcaBytes = stream.toByteArray();
			model.setPca(pcaBytes);		
			
			model.setName(name);
			
			modelrep.createModel(model);
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stream.close();
			} catch (IOException ex) {
				// ignore close exception
			}
		}

	}

}

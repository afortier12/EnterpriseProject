package ec.project.beans;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.sql.Types;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.sql.JDBCType;

import org.jboss.logging.Logger;

import ec.project.ml.Data;
import ec.project.ml.DataRepository;
import ec.project.ml.Evaluations;
import ec.project.ml.EvaluationsRepository;
import ec.project.ml.ModelRepository;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.PrincipalComponents;
import weka.attributeSelection.Ranker;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ArffLoader;
import weka.core.converters.DatabaseSaver;
import weka.experiment.InstanceQuery;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.Remove;

/**
 * Session Bean implementation class ModelEJBSingleton
 */
@Singleton
@LocalBean
public class ModelEJBSingleton implements ModelEJBSingletonRemote, ModelEJBSingletonLocal {

	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/test";
	static final String USER = "root";
	static final String PASS = "adam";
    /**
     * Default constructor. 
     */
    public ModelEJBSingleton() {
        // TODO Auto-generated constructor stub
    }
    
    
    private static final Logger LOGGER = Logger.getLogger(Evaluations.class);
    
	@PersistenceContext(unitName="primary")
    private EntityManager entityManager;
    private DataRepository dataRep;
   
    

	@Override
	public Classifier buildModel(Instances train, Classifier cls) {
		
		//train the classifier
		train.setClassIndex(train.numAttributes() - 1);
		
		// create copy
		Instances labeled = new Instances(train);
		System.out.println("Number of training datasets:" + train.numInstances());

		//train the classifier
		try {
			cls.buildClassifier(train);
		
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return cls;
		
	}
	
	@Override
	public Instances Load(String name) {
		
		dataRep = new DataRepository(entityManager);
		Data data = (Data) dataRep.getByName(name);
		
		try {
			if (data != null) {
				byte[] data_bytes = data.getData();
				
				ObjectInputStream objectIn = null;
				if (data_bytes != null) {
					objectIn = new ObjectInputStream(new ByteArrayInputStream(data_bytes));
					Instances instanceData = (Instances) objectIn.readObject();
					return instanceData;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;

		
	}
	
	@Override
	public Instances preProcess(Instances loadedData) {
		NumericToNominal convert= new NumericToNominal();
        String[] nominalOptions= new String[2];
        nominalOptions[0]="-R";
        nominalOptions[1]=Integer.toString(loadedData.numAttributes() - 3) + "-" + Integer.toString(loadedData.numAttributes());  //range of variables to make numeric
        
        Instances data = null;
        

        try {
			convert.setOptions(nominalOptions);
		
			convert.setInputFormat(loadedData);
	
	        data=Filter.useFilter(loadedData, convert);
	       
			
			data.setClassIndex(data.numAttributes()-3);
			
			
			// remove instances where M1_CURRENT_FEEDRATE = 50 or X1 ActualPosition = 198 or 
			// M1_CURRENT_PROGRAM_NUMBER is not 0
			for (int i = data.numInstances() - 1; i >= 0; i--) {
			    Instance inst = data.get(i);
			    //|| inst.attribute(inst.numAttributes()-8).equals(50) ||
	    		//!inst.attribute(inst.numAttributes()-10).equals(0)
			    Integer x1_pos = (int) inst.value(0);
			    Integer feedrate = (int) inst.value(inst.numAttributes()-8);
			    Integer progNumber = (int) inst.value(inst.numAttributes()-10);
			    if (x1_pos.equals(new Integer(198)) || feedrate.equals(new Integer(50))) {
			        data.delete(i);
			    }
			}
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
	}
	
	
	@Override
	public PrincipalComponents getPCA(Instances data) {

		PrincipalComponents pca = new PrincipalComponents();
		try {
			List<String> optionsList = new ArrayList<>();
			optionsList.clear();
			optionsList.add("-R");
			optionsList.add("0.95");
			optionsList.add("-A");
			optionsList.add("10");
			String[] options = new String[optionsList.size()];
			optionsList.toArray(options);
			pca.setOptions(options);
			pca.buildEvaluator(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return pca;

		//pca.buildEvaluator(data);
		
	}
	
	@Override
	public Instances process(Instances data, PrincipalComponents pca) {

		try {
			data = pca.transformedData(data);
			AttributeSelection attSelect = new AttributeSelection();
			Ranker search = new Ranker();
			List<String> optionsList = new ArrayList<>();
			optionsList.clear();
			optionsList.add("-T");
			optionsList.add("-1.7976931348623157E308");
			optionsList.add("-N");
			optionsList.add("10");
			String[] options = new String[optionsList.size()];
			optionsList.toArray(options);
			search.setOptions(options);
			attSelect.setEvaluator(pca);
			attSelect.setSearch(search);
			attSelect.SelectAttributes(data);
			
			Remove rm = new Remove();
			rm.setAttributeIndices("first-11, last");
			rm.setInvertSelection(true);
			rm.setInputFormat(data);
			data=Filter.useFilter(data, rm);
		
			int[] indices = attSelect.selectedAttributes();
			System.out.println(
			"selected attribute indices (starting with 0):\\n\"\r\n" + 
			Utils.arrayToString(indices));
			System.out.println("Results:" +attSelect.toResultsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return data;
	}
	

}

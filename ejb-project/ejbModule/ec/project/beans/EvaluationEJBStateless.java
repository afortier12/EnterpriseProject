package ec.project.beans;

import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.logging.Logger;

import ec.project.ml.Evaluations;
import ec.project.ml.ModelRepository;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

@Singleton
@LocalBean
public class EvaluationEJBStateless implements EvaluationEJBStatelessRemote, EvaluationEJBStatelessLocal {

	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/test";
	static final String USER = "root";
	static final String PASS = "adam";
    /**
     * Default constructor. 
     */
    public EvaluationEJBStateless() {
        // TODO Auto-generated constructor stub
    }
    
    
    private static final Logger LOGGER = Logger.getLogger(Evaluations.class);
    
	@PersistenceContext(unitName="primary")
    private EntityManager entityManager;
    private ModelRepository modelrep;
    

	@Override
	public Evaluation getEvaluation(Instances train, Instances test, Classifier cls) {
		Evaluation eval = null;    
		
		//set class attribute index
		test.setClassIndex(test.numAttributes() - 1);  
		
		// create copy
		Instances labeled = new Instances(test);
		System.out.println("Number of testing datasets:" + test.numInstances());


		try {
			// model testing
			eval = new Evaluation(train);
			eval.evaluateModel(cls, test);
		
			System.out.println("Model Evaluation: " + cls.getClass().getName());
			System.out.println(eval.toSummaryString());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return eval;
	}
}

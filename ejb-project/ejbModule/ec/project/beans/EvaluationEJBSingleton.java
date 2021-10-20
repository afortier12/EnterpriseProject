package ec.project.beans;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.logging.Logger;

import ec.project.ml.Evaluations;
import ec.project.ml.EvaluationsRepository;
import ec.project.ml.Model;
import ec.project.ml.ModelRepository;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

/**
 * Session Bean implementation class ModelEJBSingleton
 */
@Singleton
@LocalBean
public class EvaluationEJBSingleton implements EvaluationEJBSingletonRemote, EvaluationEJBSingletonLocal {

    /**
     * Default constructor. 
     */
    public EvaluationEJBSingleton() {
        // TODO Auto-generated constructor stub
    }
    
    
    private static final Logger LOGGER = Logger.getLogger(Evaluations.class);
    
	@PersistenceContext(unitName="primary")
    private EntityManager entityManager;
    private EvaluationsRepository evalRep;
    
    

	@Override
	public void saveEvaluation(Evaluation eval, int trainSize,  int testSize, String name) {
		// TODO Auto-generated method stub
		evalRep = new EvaluationsRepository(entityManager);
		try {
			Evaluations saveEval = new Evaluations(trainSize, testSize, name, eval.correct(), eval.incorrect(),
				eval.kappa(), eval.meanAbsoluteError(), eval.rootMeanSquaredError(), eval.relativeAbsoluteError(),
				eval.rootRelativeSquaredError()) ;
			
			evalRep.createEvaluation(saveEval);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public Evaluation generateEvaluation(Classifier cls, Instances train, Instances test) {
		//set class attribute index
		test.setClassIndex(test.numAttributes() - 1);
		
		// create copy
		Instances labeled = new Instances(test);
		System.out.println("Number of testing datasets:" + test.numInstances());


		try {
			// model testing
			Evaluation eval = new Evaluation(train);
			eval.evaluateModel(cls, test);
		
			System.out.println("Model Evaluation: " + cls.getClass().getName());
			System.out.println(eval.toSummaryString());
			
			return eval;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}

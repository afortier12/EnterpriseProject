package ec.project.beans;

import javax.ejb.Local;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

@Local
public interface EvaluationEJBSingletonLocal {
	
	public void saveEvaluation(Evaluation eval, int train,  int test, String name);
	
	public Evaluation generateEvaluation(Classifier cls, Instances train, Instances test);

}

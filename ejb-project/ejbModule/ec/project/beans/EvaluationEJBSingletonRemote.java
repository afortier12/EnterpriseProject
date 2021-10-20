package ec.project.beans;

import javax.ejb.Remote;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

@Remote
public interface EvaluationEJBSingletonRemote {
	
	public void saveEvaluation(Evaluation eval, int train,  int test, String name);
	
	public Evaluation generateEvaluation(Classifier cls, Instances train, Instances test);

}

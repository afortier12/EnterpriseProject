package ec.project.beans;

import javax.ejb.Remote;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

@Remote
public interface EvaluationEJBStatelessRemote {
	
	public Evaluation getEvaluation(Instances train, Instances test, Classifier cls);

}

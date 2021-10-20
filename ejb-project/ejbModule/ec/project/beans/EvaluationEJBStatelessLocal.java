package ec.project.beans;

import javax.ejb.Local;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

@Local
public interface EvaluationEJBStatelessLocal {
	
	public Evaluation getEvaluation(Instances train, Instances test, Classifier cls);

}

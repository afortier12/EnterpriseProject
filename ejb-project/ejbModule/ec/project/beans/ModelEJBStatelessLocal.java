package ec.project.beans;

import javax.ejb.Local;

import weka.classifiers.Classifier;
import weka.core.Instances;

@Local
public interface ModelEJBStatelessLocal {

	public Classifier loadModel(String name);
}

package ec.project.beans;

import javax.ejb.Remote;

import weka.classifiers.Classifier;
import weka.core.Instances;

@Remote
public interface ModelEJBStatelessRemote {

	public Classifier loadModel(String name);
	
}

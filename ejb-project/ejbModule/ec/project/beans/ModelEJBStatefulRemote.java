package ec.project.beans;

import java.io.File;

import javax.ejb.Remote;

import weka.attributeSelection.PrincipalComponents;
import weka.classifiers.Classifier;
import weka.core.Instances;

@Remote
public interface ModelEJBStatefulRemote {

	void saveData(Instances data, String name);
	
	Instances split(Instances data);

	void saveModel(Classifier cls, String name, PrincipalComponents pca);
}

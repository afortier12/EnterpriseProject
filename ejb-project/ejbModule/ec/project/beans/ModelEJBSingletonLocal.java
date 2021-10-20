package ec.project.beans;

import java.io.File;

import javax.ejb.Local;

import weka.attributeSelection.PrincipalComponents;
import weka.classifiers.Classifier;
import weka.core.Instances;

@Local
public interface ModelEJBSingletonLocal {
	
	Classifier buildModel(Instances train, Classifier cls);
	
	Instances Load(String name);
	
	Instances preProcess(Instances loadedData);
	
	PrincipalComponents getPCA(Instances data);
	
	Instances process(Instances data, PrincipalComponents pca);
		

}

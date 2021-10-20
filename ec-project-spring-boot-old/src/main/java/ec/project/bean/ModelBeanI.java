package ec.project.bean;


import java.util.List;

import ec.project.data.Model;
import weka.core.Instances;
public interface ModelBeanI {

	List<Model> findAll();
	
	double getPrediction(Instances predicationDataSet, Model model);
}


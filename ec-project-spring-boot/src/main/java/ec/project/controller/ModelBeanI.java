package ec.project.controller;


import java.util.List;

import weka.core.Instances;
public interface ModelBeanI {

	List<Model> findAll();
	
	Integer getPrediction(Instances predicationDataSet, Model model);
}


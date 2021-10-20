package ec.project.controller;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.PrincipalComponents;
import weka.attributeSelection.Ranker;
import weka.classifiers.Classifier;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.meta.AttributeSelectedClassifier;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.Remove;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class ModelBean implements ModelBeanI  {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private ModelRepository repository;
    
    @Override
    public List<Model> findAll() {
        List<Model> model = entityManager.createQuery("select m from Model m ",  Model.class).getResultList();
        return model != null ? model : null;
    }

    @Override
	public Integer getPrediction(Instances predictionDataSet, Model model) {
	
		double value = 0;
		
		predictionDataSet = preProcess(predictionDataSet);
		
		try {		
			byte[] model_bytes = model.getModel();
			
			ObjectInputStream objectIn = null;
			if (model_bytes != null)
				objectIn = new ObjectInputStream(new ByteArrayInputStream(model_bytes));
			
			Classifier cls = (Classifier) objectIn.readObject();
			
			byte[] pca_bytes = model.getPca();
			
			objectIn = null;
			if (pca_bytes != null)
				objectIn = new ObjectInputStream(new ByteArrayInputStream(pca_bytes));
			
			PrincipalComponents pca = (PrincipalComponents) objectIn.readObject();
			
			predictionDataSet.setClassIndex(predictionDataSet.numAttributes()-3);
			
			pca.buildEvaluator(predictionDataSet);
			predictionDataSet = pca.transformedData(predictionDataSet);
			
			Ranker search = new Ranker();
			List<String> optionsList = new ArrayList<>();
			optionsList.clear();
			optionsList.add("-T");
			optionsList.add("-1.7976931348623157E308");
			optionsList.add("-N");
			optionsList.add("10");
			String[] options = new String[optionsList.size()];
			optionsList.toArray(options);
			search.setOptions(options);
			
			AttributeSelection attSelect = new AttributeSelection();
			attSelect.setEvaluator(pca);
			attSelect.setSearch(search);
			attSelect.SelectAttributes(predictionDataSet);
			
			Remove rm = new Remove();
			rm.setAttributeIndices("first-11, last");
			rm.setInvertSelection(true);
			rm.setInputFormat(predictionDataSet);
			
			predictionDataSet=Filter.useFilter(predictionDataSet, rm);
			
			double maxValue = -1;
			double maxIndex = -1;
			
			for (int i = 0; i < predictionDataSet.numInstances(); i++) {
				Instance dataInstance = predictionDataSet.instance(i);
				
				
				/*double [] distributions = cls.distributionForInstance(predicationDataSet.get(i));
				
				for (int j=0; j <distributions.length; j++) {
					if (distributions[i]>0.8) {
						maxIndex =j;
						maxValue = distributions[j];
					}
					System.out.println( "class_"+j+" : "+distributions[j]+" ~ "+Math.round(distributions[j]));
				}*/
				
				double clsIndex = cls.classifyInstance(dataInstance);
				dataInstance.setClassValue(clsIndex);
			    
			    //String outputClass = dataInstance.stringValue(dataInstance.numAttributes()-1);
			    
			    System.out.println("classified as class_" +  clsIndex +" ("+100*maxValue+"%)");
			    //System.out.println("classified as: " +outputClass);
			    
			}

			return Integer.valueOf((int) Math.round(maxValue));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
    

	private Instances preProcess(Instances loadedData) {
		NumericToNominal convert= new NumericToNominal();
        String[] nominalOptions= new String[2];
        nominalOptions[0]="-R";
        nominalOptions[1]=Integer.toString(loadedData.numAttributes() - 3) + "-" + Integer.toString(loadedData.numAttributes());  //range of variables to make numeric
        
        Instances data = null;
        try {
			convert.setOptions(nominalOptions);
			convert.setInputFormat(loadedData);
	        data=Filter.useFilter(loadedData, convert);
	       
			data.setClassIndex(data.numAttributes()-3);
			
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
	}

    
}

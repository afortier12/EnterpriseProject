package ec.project.data;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.Remove;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.PrincipalComponents;
import weka.attributeSelection.Ranker;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;

public class DataProcess {

	public static void main(String[] args) throws Exception {
		File dir = new File("../data");
		ArffLoader loader = new ArffLoader();
		loader.setSource(new File(dir+"/complete.arff"));
		Instances loadedData = loader.getDataSet();
		
		/*NumericToNominal convert= new NumericToNominal();
        String[] nominalOptions= new String[2];
        nominalOptions[0]="-R";
        nominalOptions[1]=Integer.toString(loadedData.numAttributes() - 3) + "-" + Integer.toString(loadedData.numAttributes());  //range of variables to make numeric
        
        

        convert.setOptions(nominalOptions);
        convert.setInputFormat(loadedData);

        Instances data=Filter.useFilter(loadedData, convert);
       
		
		data.setClassIndex(data.numAttributes()-3);
		
		
		// remove instances where M1_CURRENT_FEEDRATE = 50 or X1 ActualPosition = 198 or 
		// M1_CURRENT_PROGRAM_NUMBER is not 0
		for (int i = data.numInstances() - 1; i >= 0; i--) {
		    Instance inst = data.get(i);
		    //|| inst.attribute(inst.numAttributes()-8).equals(50) ||
    		//!inst.attribute(inst.numAttributes()-10).equals(0)
		    Integer x1_pos = (int) inst.value(0);
		    Integer feedrate = (int) inst.value(inst.numAttributes()-8);
		    Integer progNumber = (int) inst.value(inst.numAttributes()-10);
		    if (x1_pos.equals(new Integer(198)) || feedrate.equals(new Integer(50))) {
		        data.delete(i);
		    }
		}
	
		
		//Normalize filter = new Normalize();
		//filter.setInputFormat(data);
		//data = Filter.useFilter(data, filter);
		PrincipalComponents pca = new PrincipalComponents();
		List<String> optionsList = new ArrayList<>();
		optionsList.clear();
		optionsList.add("-R");
		optionsList.add("0.95");
		optionsList.add("-A");
		optionsList.add("5");
		optionsList.add("-O");
		String[] options = new String[optionsList.size()];
		optionsList.toArray(options);
		pca.setOptions(options);
		pca.buildEvaluator(data);
		data = pca.transformedData(data);
		//pca.buildEvaluator(data);
		
		AttributeSelection attSelect = new AttributeSelection();
		Ranker search = new Ranker();
		optionsList.clear();
		optionsList.add("-T");
		optionsList.add("-1.7976931348623157E308");
		optionsList.add("-N");
		optionsList.add("-1");
		options = new String[optionsList.size()];
		optionsList.toArray(options);
		search.setOptions(options);
		attSelect.setEvaluator(pca);
		attSelect.setSearch(search);
		attSelect.SelectAttributes(data);
		
		int[] indices = attSelect.selectedAttributes();
		System.out.println(
		"selected attribute indices (starting with 0):\\n\"\r\n" + 
		Utils.arrayToString(indices));
		System.out.println("Results:" +attSelect.toResultsString());*/
		
		Instances data = preProcess(loadedData);
		PrincipalComponents pca = getPCA(data);
		data = process(data, pca);
		
		
		 // save ARFF
	    ArffSaver saver = new ArffSaver();
	    saver.setInstances(data);
	    saver.setFile(new File(dir+"/complete_pca.arff"));
	    saver.writeBatch();	
		System.out.println("Done");
		
	}
	
	public static Instances preProcess(Instances loadedData) {
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
			
			
			// remove instances where M1_CURRENT_FEEDRATE = 50 or X1 ActualPosition = 198 or 
			// M1_CURRENT_PROGRAM_NUMBER is not 0
			for (int i = data.numInstances() - 1; i >= 0; i--) {
			    Instance inst = data.get(i);
			    //|| inst.attribute(inst.numAttributes()-8).equals(50) ||
	    		//!inst.attribute(inst.numAttributes()-10).equals(0)
			    Integer x1_pos = (int) inst.value(0);
			    Integer feedrate = (int) inst.value(inst.numAttributes()-8);
			    Integer progNumber = (int) inst.value(inst.numAttributes()-10);
			    if (x1_pos.equals(new Integer(198)) || feedrate.equals(new Integer(50))) {
			        data.delete(i);
			    }
			}
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
	}
	
	public static PrincipalComponents getPCA(Instances data) {

		PrincipalComponents pca = new PrincipalComponents();
		try {
			List<String> optionsList = new ArrayList<>();
			optionsList.clear();
			optionsList.add("-R");
			optionsList.add("0.95");
			optionsList.add("-A");
			optionsList.add("5");
			String[] options = new String[optionsList.size()];
			optionsList.toArray(options);
			pca.setOptions(options);
			pca.buildEvaluator(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return pca;

		//pca.buildEvaluator(data);
		
	}
	
	public static Instances process(Instances data, PrincipalComponents pca) {

		try {
			data = pca.transformedData(data);
			AttributeSelection attSelect = new AttributeSelection();
			Ranker search = new Ranker();
			List<String> optionsList = new ArrayList<>();
			optionsList.clear();
			optionsList.add("-T");
			optionsList.add("-1.7976931348623157E308");
			optionsList.add("-N");
			optionsList.add("-1");
			String[] options = new String[optionsList.size()];
			optionsList.toArray(options);
			search.setOptions(options);
			attSelect.setEvaluator(pca);
			attSelect.setSearch(search);
			attSelect.SelectAttributes(data);
		
			int[] indices = attSelect.selectedAttributes();
			System.out.println(
			"selected attribute indices (starting with 0):\\n\"\r\n" + 
			Utils.arrayToString(indices));
			System.out.println("Results:" +attSelect.toResultsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return data;
	}
}

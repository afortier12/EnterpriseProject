package ec.project.data;

import java.io.File;

import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.PrincipalComponents;
import weka.attributeSelection.Ranker;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;

public class DataSplit {

	public static void main(String[] args) throws Exception {
		File dir = new File("../data");
		ArffLoader loader = new ArffLoader();
		loader.setSource(new File(dir+"/complete_pca.arff"));
		Instances data = loader.getDataSet();
		
		data.randomize(new java.util.Random(0));
		int trainSize = (int) Math.round(data.numInstances() * 0.8);
		int testSize = data.numInstances() - trainSize;
		Instances train = new Instances(data, 0, trainSize);
		Instances test = new Instances(data, trainSize, testSize);
		
		
		ArffSaver saver = new ArffSaver();
	    saver.setInstances(train);
	    saver.setFile(new File(dir+"/train.arff"));
	    saver.writeBatch();	
		System.out.println("Training data saved");
		
		 // save ARFF
	    saver = new ArffSaver();
	    saver.setInstances(test);
	    saver.setFile(new File(dir+"/test.arff"));
	    saver.writeBatch();	
		System.out.println("Testing data saved");
		
	}
}

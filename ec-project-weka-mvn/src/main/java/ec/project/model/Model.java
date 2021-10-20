package ec.project.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.REPTree;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.ArffLoader;

public class Model {

	
	public static Classifier buildModel(Instances train, Classifier cls) {
	
		//train the classifier
		train.setClassIndex(train.numAttributes() - 1);
		
		// create copy
		Instances labeled = new Instances(train);
		System.out.println("Number of training datasets:" + train.numInstances());

		//train the classifier
		try {
			cls.buildClassifier(train);
		
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return cls;
		
	}

	
	public static void evaluateModel(Instances train, Instances test, Classifier cls) {
		
		//set class attribute index
		test.setClassIndex(test.numAttributes() - 1);
		
		// create copy
		Instances labeled = new Instances(test);
		System.out.println("Number of testing datasets:" + test.numInstances());


		try {
			// model testing
			Evaluation eval = new Evaluation(train);
			eval.evaluateModel(cls, test);
		
			System.out.println("Model Evaluation: " + cls.getClass().getName());
			System.out.println(eval.toSummaryString());
			/*
			// label instances
			for (int i = 0; i < test.numInstances(); i++) {
				double clsLabel = cls.classifyInstance(test.instance(i));
				System.out.println(clsLabel + " -> " + test.classAttribute().value((int) clsLabel));
				labeled.instance(i).setClassValue(clsLabel);
			}
	
			//predict
			Instance predicationDataInstance = test.lastInstance();
			double value = cls.classifyInstance(predicationDataInstance);
			System.out.println(value);	*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		
		File dir = new File("../data");
		ArffLoader loader = new ArffLoader();
		loader.setSource(new File(dir+"/train.arff"));
		Instances trainingData = loader.getDataSet();
		
		loader.setSource(new File(dir+"/test.arff"));
		Instances testingData = loader.getDataSet();
		
		//SVM Model
		Classifier cls = new SMO();
		cls = buildModel(trainingData, cls);
		evaluateModel(trainingData, testingData, cls);
		
		//kNN Model
		cls = new IBk(3);
		cls = buildModel(trainingData, cls);
		evaluateModel(trainingData, testingData, cls);
		
		//Decision Tree
		cls = new REPTree();
		cls = buildModel(trainingData, cls);
		evaluateModel(trainingData, testingData, cls);
	
	

	}

}

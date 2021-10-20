package ec.project;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

public class DataImport {

	
	public DataImport() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void loadFromCSV(File dir) {
		File[] listFiles = dir.listFiles();
		for(int i=0; i<listFiles.length; i++ ) {
			String filename = listFiles[i].getName();
			String extension = "";
			int index = filename.lastIndexOf(".");
			if (index > 0) {
				extension = filename.substring(index+1);
			}
			
			if (extension.equalsIgnoreCase("csv")) {
				// load CSV
				CSVLoader loader = new CSVLoader();
				try {
					loader.setSource(listFiles[i]);
					Instances data = loader.getDataSet();
					data.setClassIndex(data.numAttributes()-1);
			   
				    // save ARFF
				    ArffSaver saver = new ArffSaver();
				    saver.setInstances(data);
				    saver.setFile(new File(dir+"/"+filename.substring(0,index)+".arff"));
				    saver.writeBatch();	
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}
	
	public Instances combineDataSets(File dir) {
		
		File[] listFiles = dir.listFiles();
		ArrayList<String> arffFiles = new ArrayList<String>();
		
		for(int i=0; i<listFiles.length; i++ ) {
			String filename = listFiles[i].getName();
			String extension = "";
			int index = filename.lastIndexOf(".");
			if (index > 0) {
				extension = filename.substring(index+1);
			}
			
			if (extension.equalsIgnoreCase("arff") && filename.contains("experiment")) {
				arffFiles.add(filename);
			}
			
		}
		
		try {
			ArffLoader loader = new ArffLoader();
			loader.setSource(new File(dir+"/"+arffFiles.get(0)));
			Instances completeData = loader.getDataSet();
			
			ArffLoader trainLoader = new ArffLoader();
			trainLoader.setSource(new File(dir +"/train.arff"));
			Instances trainData = trainLoader.getDataSet();
			FastVector toolConditionValues = new FastVector();
			FastVector finalizedValues = new FastVector();
			FastVector visualValues = new FastVector();
			
			completeData.insertAttributeAt(new Attribute("id"), completeData.numAttributes());
			completeData.insertAttributeAt(new Attribute("feedrate"), completeData.numAttributes());
			completeData.insertAttributeAt(new Attribute("clampPressure"), completeData.numAttributes());
			completeData.insertAttributeAt(new Attribute("toolCondition"), completeData.numAttributes());
			completeData.insertAttributeAt(new Attribute("machiningFinalized"), completeData.numAttributes());
			completeData.insertAttributeAt(new Attribute("passedVisual"), completeData.numAttributes());
			
			for (int j=0; j<completeData.size(); j++) {
				completeData.instance(j).setValue(completeData.numAttributes() - 6, 0);
				completeData.instance(j).setValue(completeData.numAttributes() - 5, trainData.get(0).value(2));
				completeData.instance(j).setValue(completeData.numAttributes() - 4, trainData.get(0).value(3));
				completeData.instance(j).setValue(completeData.numAttributes() - 3, trainData.get(0).value(4));
				completeData.instance(j).setValue(completeData.numAttributes() - 2, trainData.get(0).value(5));
				completeData.instance(j).setValue(completeData.numAttributes() - 1, trainData.get(0).value(6));
			}
			


			
			for (int i=1; i<arffFiles.size(); i++) {
				loader = new ArffLoader();
				loader.setSource(new File(dir+"/"+arffFiles.get(i)));
				Instances data = loader.getDataSet();
				
				data.insertAttributeAt(new Attribute("id"), data.numAttributes());
				data.insertAttributeAt(new Attribute("feedrate"), data.numAttributes());
				data.insertAttributeAt(new Attribute("clampPressure"), data.numAttributes());
				data.insertAttributeAt(new Attribute("toolCondition"), data.numAttributes());
				data.insertAttributeAt(new Attribute("machiningFinalized"), data.numAttributes());
				data.insertAttributeAt(new Attribute("passedVisual"), data.numAttributes());
				
				for (int j=0; j<data.size(); j++) {
					data.instance(j).setValue(data.numAttributes() - 6, i);
					data.instance(j).setValue(data.numAttributes() - 5, trainData.get(i).value(2));
					data.instance(j).setValue(data.numAttributes() - 4, trainData.get(i).value(3));
					data.instance(j).setValue(data.numAttributes() - 3, trainData.get(i).value(4));
					data.instance(j).setValue(data.numAttributes() - 2, trainData.get(i).value(5));
					data.instance(j).setValue(data.numAttributes() - 1, trainData.get(i).value(6));
				}
				
				
				for (int j=0; j<data.size(); j++) {
					completeData.add(data.get(j));
				}
			}
			
			 Attribute stage = completeData.attribute(completeData.numAttributes() - 7);
			 for (int n=0; n<stage.numValues(); n++) {
				 completeData.renameAttributeValue(stage, stage.value(n), String.valueOf(n));
			 }
			return completeData;
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		return null;

	}
}

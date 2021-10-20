package ec.project;

import java.io.File;

import weka.core.Instances;
import weka.core.converters.ArffSaver;


public class DataSaver {
	
	public DataSaver() {
		super();
	}

	public void Save(Instances data, File dir, String filename) {
		
		try {
			ArffSaver saver = new ArffSaver();
		    saver.setInstances(data);
		    saver.setFile(new File(dir+"/"+filename+".arff"));
		    saver.writeBatch();	
			System.out.println("Data saved");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}

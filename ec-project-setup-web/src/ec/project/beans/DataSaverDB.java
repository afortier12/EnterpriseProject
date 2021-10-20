package ec.project.beans;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.io.IOUtils;

import ec.project.data.Data;
import ec.project.data.DataRepository;
import weka.core.Instances;

@Singleton
@LocalBean
public class DataSaverDB {
	
	@PersistenceContext(unitName="primary")
	private EntityManager entityManager;
	
	private DataRepository dataRep;
		
	public void saveInstances(Instances dataset) {
		
		checkSavedInstances();
		
		dataRep = new DataRepository(entityManager);
		Data instanceData = new Data();
		
		ByteArrayOutputStream stream = null;
		ObjectOutputStream out = null;
		try {		
			instanceData.setName("complete");
			
			stream = new ByteArrayOutputStream();
			out = new ObjectOutputStream(stream);   
			out.writeObject(dataset);
			out.flush();
			byte[] dataBytes = stream.toByteArray();
			instanceData.setData(dataBytes);
			
			dataRep.addInstances(instanceData);
			
			stream = null;
			out=null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	private void checkSavedInstances() {
		
		dataRep = new DataRepository(entityManager);
		Data data = dataRep.getByName("complete");
		if (data != null) {
			dataRep.removeInstances(data);
		}
	}
	
	

}

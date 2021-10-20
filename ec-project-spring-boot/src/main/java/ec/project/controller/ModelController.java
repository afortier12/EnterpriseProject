package ec.project.controller;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import ec.project.controller.ModelBean;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;


@CrossOrigin(origins="http://localhost:8080")
@RestController
public class ModelController {
	
	
    @Autowired
    private ModelRepository repository;
    
    @Autowired
    private DataRepository dataRep;

    
    
	@RequestMapping(value="/predict", method = RequestMethod.POST)
	public Integer getPredict(@RequestParam int id, @RequestParam int start) {
		
		List<Model> models = repository.findAll();
		List<Data> dataInstances = dataRep.findAll();
		
		ModelBean modelBean = new ModelBean();
        
        try {
        	Data data = null;
			Instances instanceData = null;
			
			for (int i=0; i<dataInstances.size(); i++) {
				String name = dataInstances.get(i).getName();
				if (name.equalsIgnoreCase("complete")) {
					data = dataInstances.get(i);
				}
			}
			if (data != null) {
				byte[] data_bytes = data.getData();
				
				ObjectInputStream objectIn = null;
				if (data_bytes != null) {
					objectIn = new ObjectInputStream(new ByteArrayInputStream(data_bytes));
					instanceData = (Instances) objectIn.readObject();
				}
				
				
				if (start >=instanceData.numInstances()) {
					return 2;
				}
				

				for (int i = instanceData.numInstances() - 1; i >= 0; i--) {
				    Instance inst = instanceData.get(i);
				    //|| inst.attribute(inst.numAttributes()-8).equals(50) ||
		    		//!inst.attribute(inst.numAttributes()-10).equals(0)
				    Integer instId = (int) inst.value(inst.numAttributes()-6);
				    if (!instId.equals(new Integer(id))) {
				    	instanceData.delete(i);
				    }
				}
				
				int end =0;
				if ((start+100) > instanceData.numInstances()) {
					end = instanceData.numInstances();
				} else {
					end = start + 100;
				}
				
				for (int i = instanceData.numInstances() - 1; i >= 0; i--) {
					if ((i<start)||(i>=end)) {
						instanceData.delete(i);
					}
				}

	        return modelBean.getPrediction(instanceData, models.get(0));
		}
     
        } catch (NumberFormatException nfe) {
        	System.out.println(nfe.getMessage());
        	return 0;
        } catch (Exception ex) {
        	System.out.println(ex.getMessage());
        	return 0;
        } 
	
		return 2;
	}

}

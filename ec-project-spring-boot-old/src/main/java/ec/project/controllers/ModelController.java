package ec.project.controllers;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ec.project.bean.DataBean;
import ec.project.bean.ModelBean;
import ec.project.data.Data;
import ec.project.data.DataRepository;
import ec.project.data.Model;
import ec.project.data.ModelRepository;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;



@Controller
public class ModelController {
	
    @Autowired
    private ModelRepository repository;

	@RequestMapping(value="/predict", method = RequestMethod.GET)
	public ModelAndView getRank(@RequestParam int id, @RequestParam int start) {
		ModelAndView modelView = new ModelAndView("predict");
		
		try {
	        
			List<Model> model = repository.findAll();
			DataBean databean = new DataBean();
			List<Data> dataList = databean.findAll();
			ModelBean modelbean = new ModelBean();
			
			Data data = null;
			Instances instanceData = null;
			
			for (int i=0; i<dataList.size(); i++) {
				if (dataList.get(i).getName() == "complete") {
					data = dataList.get(i);
				}
			}
			if (data != null) {
				byte[] data_bytes = data.getData();
				
				ObjectInputStream objectIn = null;
				if (data_bytes != null) {
					objectIn = new ObjectInputStream(new ByteArrayInputStream(data_bytes));
					instanceData = (Instances) objectIn.readObject();
				}
				

				for (int i = instanceData.numInstances() - 1; i >= 0; i--) {
				    Instance inst = instanceData.get(i);
				    //|| inst.attribute(inst.numAttributes()-8).equals(50) ||
		    		//!inst.attribute(inst.numAttributes()-10).equals(0)
				    Integer instId = (int) inst.value(inst.numAttributes()-3);
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
				
		        modelView.addObject("predict", modelbean.getPrediction(instanceData, model.get(0)));
			}
			
    
     
        } catch (NumberFormatException nfe) {
        	System.out.println(nfe.getMessage());
        } catch (Exception ex) {
        	System.out.println(ex.getMessage());
        } 
	
		return modelView;
	}

}

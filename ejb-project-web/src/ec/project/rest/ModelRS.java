package ec.project.rest;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import ec.project.WebSocketServer;
import weka.attributeSelection.PrincipalComponents;
import weka.classifiers.Classifier;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.REPTree;
import weka.core.Instances;

@Path("/")
@RequestScoped
public class ModelRS {  
	@EJB 
	protected ec.project.beans.ModelEJBStatefulLocal saver;
	
	@EJB 
	protected ec.project.beans.ModelEJBSingletonLocal modeller;
    
    @GET
    @Path("/load")
    public void load(@QueryParam("method") @DefaultValue("") String method) {

		
		WebSocketServer.broadcastMessage("Loading data from database...");
		Instances data = modeller.Load("complete");
		
		WebSocketServer.broadcastMessage("Preprocessing data...");
		data = modeller.preProcess(data);
		
		WebSocketServer.broadcastMessage("Performing Principal Components Analysis...");
		PrincipalComponents pca = modeller.getPCA(data);
		
		WebSocketServer.broadcastMessage("Ranking attributes...");
		data = modeller.process(data, pca);
		
		WebSocketServer.broadcastMessage("Splitting data into training and testing datasets...");
		data = saver.split(data);
		
		Classifier cls = null;
		
		switch (method) {
		case "Support Vector Machine (SVM)":
			cls = new SMO();
			break;
		case "K-Nearest Neighbours (kNN)":
			cls = new IBk(3);
			break;
		case "Decision Tree":
			cls = new REPTree();
			break;
		}
		
		WebSocketServer.broadcastMessage("Creating model...");
		cls = modeller.buildModel(data, cls);
		try {
			WebSocketServer.broadcastMessage("Saving model...");
			saver.saveModel(cls, method, pca);
		} catch (Exception e) {
			
		}
		
		WebSocketServer.broadcastMessage("Model saved to database!");
		WebSocketServer.broadcastMessage("evaluate");
    } 
     
    
}

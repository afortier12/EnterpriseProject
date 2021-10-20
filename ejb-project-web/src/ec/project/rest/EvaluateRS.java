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
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.REPTree;
import weka.core.Instances;

@Path("/")
@RequestScoped
public class EvaluateRS {  
	@EJB 
	protected ec.project.beans.EvaluationEJBSingletonLocal evaluator;
	
	@EJB 
	protected ec.project.beans.ModelEJBSingletonLocal modeller;
	
	@EJB 
	protected ec.project.beans.ModelEJBStatelessLocal classifer;
    
    @GET
    @Path("/evaluate")
    @Produces("text/plain")
    public String evaluate(@QueryParam("method") @DefaultValue("") String method) {

		
		String name = "";
		
		WebSocketServer.broadcastMessage("Loading training data from database...");
		Instances train = modeller.Load("train");
		
		WebSocketServer.broadcastMessage("Loading testing data from database...");
		Instances test = modeller.Load("test");
		
		WebSocketServer.broadcastMessage("Loading model from database...");
		Classifier cls = classifer.loadModel(method);
		
		WebSocketServer.broadcastMessage("Evaluating model...");
		Evaluation eval  = evaluator.generateEvaluation(cls, train, test);
		
		WebSocketServer.broadcastMessage("Saving evaluation...");
		evaluator.saveEvaluation(eval, train.numInstances(), test.numInstances(), method);
		
		return eval.toSummaryString();
    } 
     
    
}

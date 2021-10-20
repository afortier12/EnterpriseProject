package ec.project;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@WebListener
public class ImportServletContextListener implements ServletContextListener {

	private ExecutorService executor;
	private ServletContext ctx;
	
	public void contextInitialized(ServletContextEvent event) {
	    //Executor
	    executor = Executors.newCachedThreadPool();
	    ctx = event.getServletContext();
	    ctx.setAttribute("executor", executor);
	}

	public void contextDestroyed(ServletContextEvent event) {
	    try {
	        executor.shutdown();
	        while(!executor.awaitTermination(10, TimeUnit.SECONDS)){
	            System.out.println("Waiting for task to shutdown");
	        };
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }
	}
}

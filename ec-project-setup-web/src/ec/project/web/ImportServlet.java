package ec.project.web;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import ec.project.DataImport;
import ec.project.DataSaver;
import ec.project.beans.DataSaverDB;
import weka.core.Instances;

@WebServlet("/DataImport")
public class ImportServlet extends HttpServlet{
	
	   private boolean isMultipart;
	   private String filePath;
	   private int maxFileSize = 100 * 1024;
	   private int maxMemSize = 4 * 1024;
	   private List<File> fileList;
	   private static final String dir = "data";
	   
		@EJB
		DataSaverDB dbsaver;
	   
	   

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		fileList = new ArrayList<>();
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);

        String uploadPath = getServletContext().getRealPath("")
                + File.separator + dir;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
		
		if(!ServletFileUpload.isMultipartContent(request)){
			throw new ServletException("Content type is not multipart/form-data");
		}
   		
   		try {
			List<FileItem> fileItemsList = upload.parseRequest(request);
			Iterator<FileItem> fileItemsIterator = fileItemsList.iterator();
			while(fileItemsIterator.hasNext()){
				FileItem fileItem = fileItemsIterator.next();
				System.out.println("FieldName="+fileItem.getFieldName());
				System.out.println("FileName="+fileItem.getName());
				System.out.println("ContentType="+fileItem.getContentType());
				System.out.println("Size in bytes="+fileItem.getSize());
	   				
				File file = new File(uploadPath + File.separator + fileItem.getName());
				if (file.exists()) file.delete();
				fileItem.write(file);
				fileList.add(file);
 
			}
   		} catch (Exception e) {
			throw new ServletException("Failed to write file", e);
			
		}
   			
   			
		fileList.clear();
		File dir = new File(uploadPath);
		
		ExecutorService serv = (ExecutorService) this.getServletContext().getAttribute("executor");
		serv.execute(new importAsync(dir));

   		
		RequestDispatcher dispatcher = request.getRequestDispatcher("import.html");
		if (dispatcher != null){
			dispatcher.forward(request, response);
		} 
   	}
	
	private class importAsync implements Runnable{ 
		   
		private File dir;
		private WebSocketServer websocket;
		
	    public importAsync(File dir){
	        this.dir = dir;
	    }

	    @Override
	    public void run(){
        	DataImport dataImport = new DataImport(); 
        	DataSaverDB dataSaver = new DataSaverDB();
  
    		WebSocketServer.broadcastMessage("Loading csv files...");
   			dataImport.loadFromCSV(dir);
   			WebSocketServer.broadcastMessage("Combining csv files...");
   			Instances data = dataImport.combineDataSets(dir);
   			WebSocketServer.broadcastMessage("Saving data to the database...");
   			
   			
   			dbsaver.saveInstances(data);
   			//dataSaver.Save(data,dir,"complete");
   			WebSocketServer.broadcastMessage("Data imported successfully!");
	           
	    }
	}

}





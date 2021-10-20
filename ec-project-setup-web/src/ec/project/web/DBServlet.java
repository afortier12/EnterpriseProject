package ec.project.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ec.project.SetupDB;

@WebServlet("/SetupDB")
public class DBServlet extends HttpServlet {
	
	private SetupDB setupDB;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
             throws ServletException, IOException {
	     response.setContentType("text/html;charset=UTF-8");
	     PrintWriter out = response.getWriter();
 
	     try {
	         setupDB = new SetupDB();
	         
	         if (setupDB.createTables() == -1) {
	
	         	out.println("Database setup succeeded!<br/>");
	       
	         } else {
	         	out.println("Database setup failed <br>");
	         }
	         	
	         
	     } catch (Exception ex) {
	         throw new ServletException(ex);
	     } finally {
	         out.close();
	     }
	}
}
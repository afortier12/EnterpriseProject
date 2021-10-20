package ec.project.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ErrorServlet")
public class ErrorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,  HttpServletResponse response) throws IOException {
    	response.setContentType("text/html;charset=UTF-8");
	    PrintWriter out = response.getWriter();
    
		Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		String servletName = (String) request.getAttribute("javax.servlet.error.servlet_name");
		   
		out.write("<!DOCTYPE html>");
		out.write("<html lang=\"en\">");
		out.write("<head><meta charset=\"UTF-8\">");
		out.write("<title>Error page</title>");
		out.write("</head>");
		if (servletName == null) {
			servletName = "Unknown";
		}
		String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
		  
		if (requestUri == null) {
			requestUri = "Unknown";
		}
	     
		if (throwable == null && statusCode == null) {
			out.println("<h2>Error information is missing</h2>");
			out.println("<p><a href=\"javascript:history.back()\">Go Back</a></p>");
		} else if (statusCode != null) {
			out.println("The status code : " + statusCode);
		} else {
			out.println("<h2>Error information</h2>");
			out.println("Servlet Name : " + servletName + "</br></br>");
			out.println("Exception Type : " + throwable.getClass( ).getName( ) + "</br></br>");
			out.println("The request URI: " + requestUri + "<br><br>");
			out.println("The exception message: " + throwable.getMessage( ));
		}
			out.println("</body>");
			out.println("</html>");
		}
}
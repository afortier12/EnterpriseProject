package ec.project;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
  
  @EJB 
  public ec.project.beans.SecurityLocal guard;
  
  @EJB 
  public ec.project.beans.UsersEJBSingletonLocal user;

  protected void doPost(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    String username = request.getParameter("username");
    String password = request.getParameter("password");

    int role = guard.validate(username, password);
    if (role >= 0) {
      HttpSession session = request.getSession();
      session.setAttribute("user", username);
      session.setMaxInactiveInterval(30*60);      
      Cookie loginCookie = new Cookie("user", username);
      response.addCookie(loginCookie);
      String encodedURL = "";
      if (role == 2) {
	      encodedURL = response.encodeRedirectURL("operator.jsp");
	      response.sendRedirect(encodedURL);
      } else {
    	  encodedURL = response.encodeRedirectURL("index.jsp");
	      response.sendRedirect(encodedURL);
      }
    }else{
    	RequestDispatcher dispatcher = request.getRequestDispatcher("loginfailure.jsp");
		if (dispatcher != null){
			dispatcher.forward(request, response);
		}
    }
  }
}
package ec.project;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ec.project.users.User;

@WebServlet("/AddUser")
public class UserEJBStatefulServlet extends HttpServlet {

	private static final long serialVersionUID = 2L;
	
	@EJB 
	public ec.project.beans.SecurityLocal guard;
  
	@EJB 
	public ec.project.beans.UsersEJBStatefulLocal user;

	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserEJBStatefulServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String role = request.getParameter("role");
		
		List<String> encryptedPw = guard.generateHash(password);
		int result = -1;
		try {
			result = user.AddUser(username, encryptedPw.get(0), encryptedPw.get(1), Integer.parseInt(role));
		} catch (Exception e) {
			request.setAttribute("error", e.getMessage());
		}
		
		if (result >= 0) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("addsuccess.jsp");
			if (dispatcher != null){
				dispatcher.forward(request, response);
			} 
		} else {
			RequestDispatcher dispatcher = request.getRequestDispatcher("addfailure.jsp");
			if (dispatcher != null){
				dispatcher.forward(request, response);
			}
		}
	}
}

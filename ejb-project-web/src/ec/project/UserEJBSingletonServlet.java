package ec.project;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ec.project.users.User;

@WebServlet("/Users")
public class UserEJBSingletonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	  
	@EJB 
	public ec.project.beans.UsersEJBSingletonLocal user;
	

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		List<String> displayList = new ArrayList<>();
		List<User> users = user.getUsers();
		if (users != null) {
			for (User user: users) {
				String role = "unknown";
				displayList.add(user.getName());
				switch (user.getRole()){
					case 1:
						role="administrator";
						break;
					case 2:
						role="operator";
						break;
				}
				displayList.add(role);
			}
		}
		request.setAttribute("users",displayList);

		RequestDispatcher dispatcher = request.getRequestDispatcher("userlist.jsp");
		if (dispatcher != null){
			dispatcher.forward(request, response);
		} 

	}

}

package ec.project;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/UploadServlet")
public class ModelEJBSingletonServlet extends HttpServlet {

	private static final long serialVersionUID = 2L;
    

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ModelEJBSingletonServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
		
	}
}

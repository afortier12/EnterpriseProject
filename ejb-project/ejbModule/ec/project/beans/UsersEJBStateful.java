package ec.project.beans;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.logging.Logger;

import ec.project.users.User;
import ec.project.users.UserRepository;

@Singleton
@LocalBean
public class UsersEJBStateful implements UsersEJBStatefulRemote, UsersEJBStatefulLocal {

	 private static final Logger LOGGER = Logger.getLogger(User.class);
	    
		@PersistenceContext(unitName="primary")
	    private EntityManager entityManager;

	    private UserRepository userrep;
		    
		public UsersEJBStateful() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public int AddUser(String name, String password, String salt, int role) {
			// TODO Auto-generated method stub
			userrep = new UserRepository(entityManager);
	        LOGGER.info("get users" );
			
			User user = new User(name, password, salt, role);
			if (userrep.createUser(user) == null) {
				return -1;
			};
			return 0;
		}

		
}

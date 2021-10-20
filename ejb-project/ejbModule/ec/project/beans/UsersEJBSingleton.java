package ec.project.beans;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.logging.Logger;

import ec.project.users.User;
import ec.project.users.UserRepository;

/**
 * Session Bean implementation class Users
 */
@Singleton
@LocalBean
public class UsersEJBSingleton implements UsersEJBSingletonRemote, UsersEJBSingletonLocal {

	 private static final Logger LOGGER = Logger.getLogger(User.class);
	    
		@PersistenceContext(unitName="primary")
	    private EntityManager entityManager;

	    private UserRepository userrep;
		    
		public UsersEJBSingleton() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public List<User> getUsers() {
			userrep = new UserRepository(entityManager);
	        LOGGER.info("get users" );
	        List<User> users = userrep.getAllUsers();
	        
	        return users;
		}
}

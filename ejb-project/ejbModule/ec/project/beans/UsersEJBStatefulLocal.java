package ec.project.beans;

import javax.ejb.Local;

@Local
public interface UsersEJBStatefulLocal {
	
	int AddUser(String name, String password, String salt, int role);

}

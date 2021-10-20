package ec.project.beans;

import javax.ejb.Remote;

@Remote
public interface UsersEJBStatefulRemote {

	int AddUser(String name, String password, String salt, int role);
	
}

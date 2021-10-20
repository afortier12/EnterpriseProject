package ec.project.beans;

import java.util.List;

import javax.ejb.Remote;

import ec.project.users.User;

@Remote
public interface UsersEJBSingletonRemote {

	List<User> getUsers();
	
}

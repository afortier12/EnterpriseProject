package ec.project.beans;

import java.util.List;

import javax.ejb.Local;

import ec.project.users.User;

@Local
public interface UsersEJBSingletonLocal {

	List<User> getUsers();

}

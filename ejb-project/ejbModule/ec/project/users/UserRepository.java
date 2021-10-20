package ec.project.users;

import java.util.List;

import javax.persistence.EntityManager;

public class UserRepository {

	private EntityManager entityManager;
    public UserRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    public User save(User user) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(user);
            entityManager.getTransaction().commit();
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public User createUser(User user) {
        entityManager.persist(user);
        if (entityManager.contains(user)) {
        	return user;
        } else {
        	return null;
        }
    }
    
    public List<User> findAll() {
        return entityManager.createQuery("from User").getResultList();
    }
    public User findById(Integer id) {
        User user = entityManager.find(User.class, id);
        return user != null ? user : null;
    }
    public User findByID(int id) {
        User user = entityManager.createQuery("select u from User u where u.id = :userid",  User.class).setParameter("userid", id).getSingleResult();
        return user != null ? user : null;
    }
    public User findByName(String name) {
    	 User user = entityManager.createQuery("select u from User u where u.name = :name",  User.class).setParameter("name", name).getSingleResult();
         return user != null ? user : null;
    }
    public User findByNamePW(String name, List<String> hashSalt) {
        List<User> users = entityManager.createNamedQuery("User.findByNamePW", User.class)
                .setParameter("name", name)
                .setParameter("password", hashSalt.get(0))
                .setParameter("salt", hashSalt.get(1))
                .getResultList();
        if (users.size() == 0) return null;
        else return users.get(0);

    }
    
    public List<User> getAllUsers() {
        List<User> users = entityManager.createNamedQuery("User.getAllUsers", User.class)
                .getResultList();
        if (users.size() == 0) return null;
        else return users;

    }
}

package ec.project.users;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="tool_users")
@NamedQueries({
    @NamedQuery(name = "User.findByNamePW", query = "SELECT a FROM User a WHERE a.name = :name and a.password = :password and a.salt = :salt"),
    @NamedQuery(name = "User.getAllUsers", query = "SELECT a FROM User a")
})
public class User {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String password;
    private String salt;
    private int role;
    
    public User() { }
    public User(String name) {
       this.name = name;
    }
    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
    public User(String name, String password, String salt, Integer role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.salt = salt;
        this.role = role;
    }
    
    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) {  this.name = name; }
    public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	@Override
    public String toString() {
        return "User {" + "id=" + id + ", name='" + name + '\'' + '}';
    }
	
}

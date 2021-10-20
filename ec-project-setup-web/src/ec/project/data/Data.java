package ec.project.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "tool_data")
@NamedQueries({
    @NamedQuery(name = "Data.getAllData", query = "SELECT a FROM Data a")
})
public class Data {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String name;
    @Lob
    private byte[] data;
    
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
	public byte[] getDatal() {
		return data;
	}

	public void setData(byte[] object) {
		this.data = object;
	}
	
	public String toString() {
		return this.getName();
	}
}
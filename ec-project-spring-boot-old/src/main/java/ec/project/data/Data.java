package ec.project.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "tool_data")
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
	public byte[] getData() {
		return data;
	}

	public void setData(byte[] object) {
		this.data = object;
	}
	
	public String toString() {
		return this.getName();
	}
	public void setId(Long id) {
		this.id = id;
	}
}
package ec.project.ml;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="tool_model")
public class Model {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String name;
    @Lob
    private byte[] model;
    @Lob
    private byte[] pca;
    
    public byte[] getPca() {
		return pca;
	}
	public void setPca(byte[] pca) {
		this.pca = pca;
	}
	public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
	public byte[] getModel() {
		return model;
	}

	public void setModel(byte[] object) {
		this.model = object;
	}
	
	public String toString() {
		return this.getName();
	}
}
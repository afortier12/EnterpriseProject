package ec.project.beans;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;


@Local
public interface SecurityLocal {
    public Integer validate(String user, String password);
    
    public List<String> generateHash(String password);
    
    public String generateHashFromSalt(String password, String salt);
}
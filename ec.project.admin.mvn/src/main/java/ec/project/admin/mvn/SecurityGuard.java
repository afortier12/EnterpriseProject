package ec.project.admin.mvn;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


public class SecurityGuard {
    
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 512;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    
    private Map<String, String> DB = new HashMap<String, String>();
	private static final String SALT = "CP630 final project";
    
    /**
     * Default constructor. 
     */
    public SecurityGuard() {
    }

    
    public List<String> generateHash(String password) {
    	
    	List<String> hashSalt = new ArrayList<>();
    	
        char[] chars = password.toCharArray();      
        try {
        	byte[] salt = getSalt();
	        PBEKeySpec spec = new PBEKeySpec(chars, salt, ITERATIONS, KEY_LENGTH);
	        SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
	        byte[] hash = skf.generateSecret(spec).getEncoded();
	        
	        hashSalt.add(Base64.getEncoder().encodeToString(hash));
	        hashSalt.add(Base64.getEncoder().encodeToString(salt));

        } catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return hashSalt;
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }
}
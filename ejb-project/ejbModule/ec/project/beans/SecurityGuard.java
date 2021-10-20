package ec.project.beans;

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
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.logging.Logger;

import ec.project.users.User;
import ec.project.users.UserRepository;

@Singleton
@LocalBean
public class SecurityGuard implements SecurityLocal {
	private static final Logger LOGGER = Logger.getLogger(SecurityGuard.class);

	private static final int ITERATIONS = 65536;
	private static final int KEY_LENGTH = 512;
	private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

	@PersistenceContext(unitName = "primary")
	private EntityManager entityManager;

	private Map<String, String> DB = new HashMap<String, String>();
	private static final String SALT = "CP630 final project";

	private UserRepository userrep;

	/**
	 * Default constructor.
	 */
	public SecurityGuard() {
	}

	@Override
    public Integer validate(String username, String password) {

    	userrep = new UserRepository(entityManager);
    	User user = userrep.findByName(username);
    	
    	if (user !=null) {
    		String salt = user.getSalt();
    		String pwEncrypted = generateHashFromSalt(password, salt);
		    if (pwEncrypted.length() == 0) return -1;
		    userrep = new UserRepository(entityManager);
		    LOGGER.info("check user: " + username );
		    List<String> hashSalt = new ArrayList<>();
		    hashSalt.add(pwEncrypted);
		    hashSalt.add(salt);
		    user = userrep.findByNamePW(username, hashSalt);
		    if (user!=null) {
		      LOGGER.info(username + ": valid" );
		      return user != null ? user.getRole() : -1;
		    }else {
		      LOGGER.info(username + ": invalid" );
		    return -1;
		    }
    	} else {
    		LOGGER.info(username + ": invalid" );
   		    return -1;
    	}

    }

	@Override
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

	@Override
	public String generateHashFromSalt(String password, String salt) {

		char[] chars = password.toCharArray();
		try {
			byte[] bSalt = Base64.getDecoder().decode(salt);
			PBEKeySpec spec = new PBEKeySpec(chars, bSalt, ITERATIONS, KEY_LENGTH);
			SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
			byte[] hash = skf.generateSecret(spec).getEncoded();

			return Base64.getEncoder().encodeToString(hash);

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static byte[] getSalt() throws NoSuchAlgorithmException {
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return salt;
	}
}
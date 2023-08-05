package classes.entities;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Hashing Machine is a class defining static methods for hashing passwords.
 */

public class HashingMachine {
	
    /**
     * Declares the hashing string.
     */
	private static final String SALT = "70197a4d3a5cd29b62d4239007b1c5c3c0009d42d190308fd855fc459b107f40a03bd427cb6d87de18911f21ae9fdfc24dadb0163741559719669c7668d7d587";

    /**
     * Class Constructor.
     */
    public HashingMachine() {};

    /**
     * Returns a hashed password from an input plaintext password.
     * 
     * @param password                  plaintext password being hashed
     * 
     * @return                          hashed password
     */
    public static String hashPassword(String password){
        // reference link to code this 
        String generatedPassword = null;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(SALT.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
}

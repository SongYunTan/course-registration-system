package classes.entities;

import java.util.*;
import java.io.*;

/**
 * Admin is a concrete class extending User.
 * <p>
 * Admin contains methods to read and edit attributes stored in adminFlatFile.csv flat file.
 * </p>
 * <p>
 * An Admin object encapsulates the information needed for some AdminManager operations. This state information includes:
 * <ul>
 * <li>Unique Username
 * <li>Password in hashed format
 * </ul>
 * </p>
 */

public class Admin extends User {
    //attributes
    private String username;
    private String hashedPassword;

    //static attributes
    private static String adminFile = "flatFiles/adminFlatFile.csv";

    /** 
     * Class Constructor for an empty Admin.
     */
    public Admin() {
        this.username = "admin not found";
    }

    /**
     * Class Constructor specifying Admin attributes.
     */
    public Admin(String username, String hashedPassword) {
        this.username = username;
        this.hashedPassword = hashedPassword;
    }

    /**
     * Returns a Hashmap that can be used to determine if username exists in the adminFlatFile.csv flat file.
     * 
     * @return              Hashmap<String, String> of username:hashedpassword key:value pairs in the student .csv flat file
     */
    public static HashMap<String, String> getUserPassword(){
        return User.getUserPassword(Admin.adminFile);
    }

    /**
     * Creates an Admin object according to a username.
     * <p>
     * Parses through adminFlatFile.csv flat file to find matching username in order to instantiate Admin object.
     * </p>
     * 
     * @param username              username of Admin being instantiated
     * 
     * @return                      an Admin object with a username corresponding to input username
     * 
     */
    public static Admin createByUsername(String username) {
        Admin admin;
        try {
            BufferedReader br = new BufferedReader(new FileReader(Admin.adminFile));
            String line = "";
            //iterate through flatfile rows
            while ((line = br.readLine()) != null) {
                //save flat file row
                String[] row = line.split(User.csvSplitBy);
                //check if username corresponds
                if (row[0].toLowerCase().equals(username.toLowerCase())) {
                    // instantiate attributes
                    admin = new Admin(row[0], row[1]);
                    br.close();
                    return admin;
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        admin = new Admin();
        return admin;
    }

    /**
     * Edits the adminFlatFile.csv flat file to change a desired item delineated by desired row and column. 
     * <p>
     * Desired row is determined by the Admin's unique username attribute.
     * </p>
     * <p>
     * Writes to a temporary file that is subsequently renamed to replace the original adminFlatFile.csv flat file.
     * </p>
     * 
     * @param newValue          the desired changed value
     * @param index             identifier used to delineate which column should be edited
     * 
     * @return                  <code>true</code> if the edit function was successful
     */ 
    public boolean editField(String newValue, int index) {
        return super.editField(this.username, newValue, index, Admin.adminFile);
    }

    /**
     * Returns the username of the admin object
     * 
     * @return                  the username of the administrator
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Sets the username of the admin object and modifies the flat file accordingly
     * 
     * @return                  <code>true</code> if the operation was successful
     */
    public boolean setUsername(String newUsername) {
        this.username = newUsername;
        return this.editField(newUsername, 0);
    }

    /**
     * Sets the password of the admin object and modifies the flat file accordingly
     * 
     * @return                  <code>true</code> if the operation was successful
     */
    public boolean setPassword(String newPassword) {
        this.hashedPassword = HashingMachine.hashPassword(newPassword);
        return this.editField(newPassword, 1);
    }
}

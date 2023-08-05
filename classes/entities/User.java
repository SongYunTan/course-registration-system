package classes.entities;

import java.util.*;
import java.io.*;

/**
 * User is an abstract class implemented by Student and Admin.
 * <p>
 * User defines attributes and methods for interacting with .csv flat files that subclasses utilize:
 * <ul>
 * <li>The character that separates values in flat files
 * <li>HashMap of username:hashedPassword pairs
 * <li>Edit function for .csv files
 * <ul>
 * </p>
 */

public abstract class User {
    /**
     * Declares the comma "," as the character used to separate values in working flat files
     */
    protected static final String csvSplitBy = ",";

    /**
     * A reader object for reading a flat file
     */
    private static BufferedReader br = null;

    /**
     * Returns a Hashmap that can be used to determine if username exists in the .csv flat file specified.
     * <p>
     * Used by subclasses on their specific flat files.
     * </p>
     * 
     * @param file          .csv file with username in the first column and hashed passwords in the second column
     * 
     * @return              Hashmap<String, String> of username:hashedPassword key:value pairs
     */
    public static HashMap<String, String> getUserPassword(String file){
        HashMap<String, String> usernamePassword = new HashMap<String, String>();
        
        try {
            BufferedReader br = new BufferedReader(new FileReader(file)); //file containing passwords
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] nameList = line.split(User.csvSplitBy);
                // column 1 is username, column 2 is password
                usernamePassword.put(nameList[0].toLowerCase(), nameList[1]);
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return usernamePassword;
    }

    /**
     * Edits a .csv flat file to change a desired item delineated by desired row and column. 
     * <p>
     * Writes to a temporary file that is subsequently renamed to replace the original file.
     * Used by subclasses on their specific flat files.
     * </p>
     * 
     * @param username          unique identifier used to delineate which row should be edited
     * @param newValue          the desired changed value
     * @param index             identifier used to delineate which column should be edited
     * @param file              address of the .csv flat file
     * 
     * @return                  Boolean representing whether the edit function was successful
     */
    public boolean editField(String username, String newValue, int option, String file) {
    	//initialization
    	File tempFile = new File("flatFiles/temp.csv");
        File oldFile = new File(file);
        String line = "";  
		String csvSplitBy = ",";
        

        try {
        	br = new BufferedReader(new FileReader(file));
            FileWriter fw = new FileWriter(tempFile);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);   
             
            while ((line = br.readLine()) != null) {       	
                String[] row = line.split(csvSplitBy);   
                if (row[0].toLowerCase().equals(username.toLowerCase())) {
                    if (option == 1) {
                        newValue = HashingMachine.hashPassword(newValue);
                    } 
                    row[option] = newValue;
                }
                String myContent = String.join(",", row);
                myContent += "\n";
                pw.write(myContent);                        
            }
                
            pw.flush();
            pw.close();
            br.close();
  
            File dump = new File(file);
            dump.delete();
            tempFile.renameTo(new File(file));
            
        } catch (IOException e) {
            e.printStackTrace();
            return false;
     	} finally {
        	if (br != null) {
            	try {
                    br.close();
                    return true;
            	} catch (IOException e) {
                    e.printStackTrace();
                    return false;
             	}
         	}
		}
    return false;
    }
}

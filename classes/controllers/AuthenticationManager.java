package classes.controllers;

import java.util.*;
import java.time.*;
import java.time.format.*;
import classes.entities.*;

/**
 * AuthenticationManager manages user login
 */
public class AuthenticationManager {
    /**
     * Empty Class Constructor
     */
    public AuthenticationManager() {}

    /**
     * Returns <code>true</code> if username is found
     * 
     * @param choice            1 for admin, 2 for student
     * @param username          username being searched
     * 
     * @return                  <code>true</code> if username is found
     */
    public static boolean checkUsername(int choice, String username){
        // Login Form will call this method with a username
        // Check against flat file to see if username exists 
        // Choice is decide which flat file to check against
        // 1 for admin and 2 for student 
    	HashMap<String, String> usernamePassword;

        if (choice == 1){
            usernamePassword = classes.entities.Admin.getUserPassword();
        }
        else{
            usernamePassword = classes.entities.Student.getUserPassword();
        }

        if (usernamePassword.containsKey(username.toLowerCase())){
            return true;
        }

        return false;
    }

    /**
     * Returns <code>true</code> if username and password are found and correspond
     * 
     * @param choice            1 for admin, 2 for student
     * @param username          username being searched
     * @param password          plain password being searched
     * 
     * @return                  <code>true</code> if username and password are found and correspond
     */
    public static boolean checkPassword(int choice, String username, String password){
        // Login Form will call this method with a username and password
        // Check against flat file to see if password is correct
        // Choice is decide which flat file to check against
        // 1 for admin and 2 for student 

        // call password hasher
    	HashMap<String, String> usernamePassword;

        if (choice == 1){
            usernamePassword = classes.entities.Admin.getUserPassword();
        }
        else{
            usernamePassword = classes.entities.Student.getUserPassword();
        }

        // hash password
        String hashedPassword = HashingMachine.hashPassword(password);

        if (usernamePassword.get(username.toLowerCase()).equals(hashedPassword)){
            return true;
        }

        else return false;
        
    }
    
    /**
     * Returns <code>true</code> if current time is within student access period
     * 
     * @param student           the student whose access period is being checked
     * 
     * @return                  <code>true</code> if the current time is within student access period
     */
    public static boolean checkAccessPeriod(String username){
        // Login Form will call this method with a username
        // Check against Access Period flat file to see if user has access now

        Student student = Student.createByUsername(username);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.ENGLISH);

        LocalDateTime startDate = student.getStartDate();
        LocalDateTime endDate = student.getEndDate();
        
        LocalDateTime nowDate = LocalDateTime.now(); 

        if (nowDate.isAfter(startDate) && nowDate.isBefore(endDate)){
            return true;
        }

        else{
            System.out.println("You are not allowed access during this period!");
            System.out.println("Your access period is from " + startDate.format(formatter) + " to " + endDate.format(formatter));
            return false;
        }
    }
}

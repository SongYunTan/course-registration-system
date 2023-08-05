package classes.interfaces;

import java.util.*;
import java.io.*;
import classes.controllers.*;

/**
 * LoginForm is an interface that all users will interact with when using this STARS system.
 */
public class LoginForm {

    /**
     * Defines ADMIN as 1
     */
    final static int ADMIN = 1;

    /**
     * Defines STUDENT as 2
     */
    final static int STUDENT = 2;

    /**
     * Defines the maximum number of retries for login
     */
    final static int MAX_RETRIES = 10;

    /**
     * Instantiates a scanner for reading user input
     */
    static Scanner scan = new Scanner(System.in);

    /**
     * Instantiates a console for reading user password input as a hidden field
     */
    static Console console = System.console();

    /**
     * Main function for interacting with the STARS system.
     * Performs user authentication before instantiating either the admin or student interface for the user to interact with.
     */
    public static void main(String[] args) throws IOException {

        // Welcome message
        System.out.println("Welcome to My Student Automated Registration System (MySTARS)");

        // separate admin and student flow
        System.out.println("For admin system, choose 1. Else, choose 2: ");

        String ch = scan.nextLine();
        int choice = 0;
        if (ch != null && !ch.equals("") && !ch.equals(" ")){
            choice = ch.charAt(0) - '0';
        }

        while (choice != 1 && choice != 2){
            System.out.println("Invalid choice. Please select again: ");
            ch = scan.nextLine();
            if (ch != null && !ch.equals("") && !ch.equals(" ")){
                choice = ch.charAt(0) - '0';
            }
        }

        // check username 
        String username = checkUsername(choice);
        if (username == null){
            System.out.println("Max number of retries exceeded.");
            System.out.println("Exiting MySTARS Planner...\n");
            System.exit(0);
        }
        
        if (!checkPassword(choice, username)){
            System.out.println("Max number of retries exceeded.");
            System.out.println("Exiting MySTARS Planner...\n");
            System.exit(0);
        }
        
        if (choice == 1){

            // call admin menu
            try{
                classes.interfaces.AdminMenu.start(username);
            } catch (IOException e){
                System.out.println("Failed to load admin menu! Try again later!");
            }
            System.out.println("Logging into admin system..");
        }
        
        else{

            if(AuthenticationManager.checkAccessPeriod(username)){
                // call student menu
                try{
                    classes.interfaces.StudentMenu.start(username);
                } catch (IOException e){
                    System.out.println("Failed to load admin menu! Try again later!");
                }
                System.out.println("Logging into student system..");
            }
            else{
                System.out.println("Login Failed.");
                System.out.println("Exiting MySTARS Planner...\n");
                System.exit(0);
            }
        }
    }

    /**
     * Checks if username is valid
     * 
     * @param choice                1 for admin, 2 for student
     * 
     * @return                      username if valid, null if invalid
     */
    // check userName in either admin flat file or student flat file
    protected static String checkUsername(int choice){

        for (int i = 0; i < MAX_RETRIES; i++) {
            System.out.println("Please Enter Username: ");
            String username = scan.nextLine();

            if (AuthenticationManager.checkUsername(choice, username)){
            	return username;
            }

            System.out.println("Invalid Username!! Please try again.");

        } 

        return null;
    }
    
    /**
     * Checks if password is valid
     * 
     * @param choice                1 for admin, 2 for student
     * @param username              username that password is being validated for
     * 
     * @return                      <code>true</code> if valid password
     */
    protected static boolean checkPassword(int choice, String username){

        for (int i = 0; i < MAX_RETRIES; i++) {
            System.out.println("Please Enter Password: ");
            char[] password = console.readPassword();

            if (AuthenticationManager.checkPassword(choice, username, new String(password))){
            	return true;
            }

            System.out.println("Invalid Password!! Please try again.");

        } 

        return false;
    }

    
 
}
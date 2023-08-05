package classes.interfaces;

import java.io.*;
import java.util.*;
import classes.controllers.*;
import classes.entities.*;

/**
 * StudentMenu is an interface class for students to interact with the STARS system.
 * Students are able to perform 8 different functions through interacting with the StudentMenu interface.
 * <ol>
 * <li>Enroll themselves into a course
 * <li>Drop themselves from a course
 * <li>Display a list of courses they are enrolled in
 * <li>Check the vacancies available for a course and their respective indexes
 * <li>Change their index number in a course they are currently registered in
 * <li>Swop their index number with another student within the same course
 * <li>Print out their timetable
 * <li>Change their password
 * </ol>
 */

public class StudentMenu {
    /**
     * Class Constructor specifying the student that is using the student menu
     * @param stud
     */
    public StudentMenu(Student stud) {}

    /**
     * Main function of the student menu interface that students will interact with
     * 
     * @param currentUser                   student object that the student menu operates upon
     * 
     * @throws IOException
     */
    public static void start(String username) throws IOException {
        Scanner sc = new Scanner(System.in);
        int userChoice = 0;
        StudentManager sm = new StudentManager(username);
        while(userChoice != 9) {
            userChoice = selectFunction();
            switch (userChoice) {
                case 1 -> {
                    //print list of courses and their indexes
                    Map<String,ArrayList<String>> list = IndexManager.getCourseList();
                    
                    int cnt = 1;
                    System.out.format("      %-10s %-32s\n", "CourseCode", "Index");
                    for (String course: list.keySet()){
                        String key = course.toString();
                        String value = String.join(", ", list.get(key));
                        System.out.format("%-2d    %-10s %-32s\n", cnt, key, value);
                        cnt++;
                    } 
                    System.out.println();

                    // receive a valid user input for course code
                    System.out.println("Please enter the Course Code you wish to add: ");
                    String chosenCourse = sc.nextLine();

                    int quit = 0;
                    while (!chosenCourse.matches("\\w{2}\\d{4}")) {
                        System.out.println("Invalid format. Course code should follow this example format: CZ2002");
                        System.out.println("Enter a new course code. Enter an integer to quit: ");
                        chosenCourse = sc.nextLine();
                        quit = 0;
                        if (chosenCourse != null && chosenCourse.matches("[-+]?\\d*\\.?\\d+")) {
                            quit = 1;
                            break;
                        }
                    }
                    if (quit == 1) break;

                    // receive a valid input for course index
                    System.out.println("Please enter the Course Index you wish to add: ");
                    String chosenIndex = sc.nextLine();

                    int addCourseOutcome = sm.addCourse(chosenCourse, chosenIndex, 0);
                    
                    if (addCourseOutcome == -1)
                    	System.out.println("Unable to add course, check if you inputted the correct Course Code and corresponding Course Indexes\n");                    	
                    else if (addCourseOutcome == 0)
                    	System.out.println("You are already in this Course, unable to add course!\n");
                    else if (addCourseOutcome == 1)
                    	System.out.println("Successfully added course!\n");
                    else if (addCourseOutcome == 2) {
                    	System.out.println("The current index for the course is already full. Please type '1' if you wish to be added to the waitlist; otherwise, please type'0'");
                        int waitlistChoice = 0;
                        try {
                            waitlistChoice = sc.nextInt();
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid Input!");
                        }
                
                        if (waitlistChoice == 1){
                        	int addWaitlistOutcome = sm.addWaitlist(chosenCourse, chosenIndex);
                        	if (addWaitlistOutcome == 0)
                        		System.out.println("The course is already in your waitlist\n");
                        	else if (addWaitlistOutcome == 1)
                        		System.out.println ("Successfully added the course to your waitlist!\n");
                        	else 
                        		System.out.println("You currently hold " + addWaitlistOutcome + " Academic Units, unable to add course as it will exceed the recommended maximum of 21 Academic Units\n");            
                        }
                        else {System.out.println("Exit the current session\n");}
                    }
                    else if (addCourseOutcome == 3) {
                        System.out.println("Your chosen index will clash with another course, unable to add course.");
                    }
                    else 
                    	System.out.println("You currently hold " + addCourseOutcome + " Academic Units, unable to add course as it will exceed the recommended maximum of 21 Academic Units\n");
                        
                    break;
                }

                case 2 -> {
                    // drop a course
                    System.out.println("These are the courses you are enrolled in, along with their indexes:");
                    // print all courses the student is enrolled in
                    sm.displayStudentCourses();
                    System.out.println();

                    // receive a valid course code
                    System.out.println("Please enter the course you wish to drop: ");
                    String chosenCourse = sc.nextLine();

                    int quit = 0;
                    while (!chosenCourse.matches("\\w{2}\\d{4}")) {
                        System.out.println("Invalid format. Course code should follow this example format: CZ2002");
                        System.out.println("Enter a new course code. Enter an integer to quit: ");
                        chosenCourse = sc.nextLine();
                        quit = 0;
                        if (chosenCourse != null && chosenCourse.matches("[-+]?\\d*\\.?\\d+")) {
                            quit = 1;
                            break;
                        }
                    }
                    if (quit == 1) break;

                    // receive a valid input for course index
                    System.out.println("Please enter the Course Index you wish to drop: ");
                    String chosenIndex = sc.nextLine();

                    // call for remove course in Student Manager
                    if (sm.dropCourse(chosenCourse, chosenIndex)) System.out.println(chosenCourse + " successfully dropped!\n");
                    else System.out.println("Unable to drop course, check that you have inputted the correct Course Code and its corresponding Course Index\n");
                    break;
                }

                case 3 -> {
                    // display registered courses
                    System.out.println("These are the courses you are enrolled in, along with their indexes:");
                    // print all courses the student is enrolled in
                    sm.displayStudentCourses();
                    System.out.println();
                    break;
                }

                case 4 -> {
                    //print list of courses and their indexes
                    Map<String,ArrayList<String>> list = IndexManager.getCourseList();
                    
                    int cnt = 1;
                    System.out.format("      %-10s %-32s\n", "CourseCode", "Index");
                    for (String course: list.keySet()){
                        String key = course.toString();
                        String value = String.join(", ", list.get(key));
                        System.out.format("%-2d    %-10s %-32s\n", cnt, key, value);
                        cnt++;
                    } 

                    // receive a valid input for the course code
                    System.out.println("Please enter the course that you wish to check for vacancies");
                    String chosenCourse = sc.nextLine();

                    int quit = 0;
                    while (!chosenCourse.matches("\\w{2}\\d{4}")) {
                        System.out.println("Invalid format. Course code should follow this example format: CZ2002");
                        System.out.println("Enter a new course code. Enter an integer to quit: ");
                        chosenCourse = sc.nextLine();
                        quit = 0;
                        if (chosenCourse != null && chosenCourse.matches("[-+]?\\d*\\.?\\d+")) {
                            quit = 1;
                            break;
                        }
                    }

                    if (quit == 1) break;

                    // print out the vacancy of the course
                    if (sm.displayIndexVacancy(chosenCourse, "", 2)) System.out.println("Unable to display course vacancies, check that you have inputted the correct Course Code\n");;
                    break;
                }

                case 5 -> { // change index number in a registered course
                    // display registered courses
                    System.out.println("These are the courses you are enrolled in, along with their indexes:");
                    // print all courses the student is enrolled in
                    sm.displayStudentCourses();
                    System.out.println();

                    System.out.println("Please enter the course you wish to have its index swapped");
                    String chosenCourse = sc.nextLine();

                    int quit = 0;
                    while (!chosenCourse.matches("\\w{2}\\d{4}")) {
                        System.out.println("Invalid format. Course code should follow this example format: CZ2002");
                        System.out.println("Enter a new course code. Enter an integer to quit: ");
                        chosenCourse = sc.nextLine();
                        quit = 0;
                        if (chosenCourse != null && chosenCourse.matches("[-+]?\\d*\\.?\\d+")) {
                            quit = 1;
                            break;
                        }
                    }
                    if (quit == 1) break;
                    System.out.println("Please enter the index number of that course that you are currently enrolled in");
                    String originalIndex = sc.nextLine();
                    System.out.println("Please enter the course index you wish to swap to");
                    String targetIndex = sc.nextLine();

                    // check target index for vacancies and mutate both target index and cur index
                    if (sm.changeIndex(chosenCourse, originalIndex, targetIndex)) {
                        System.out.println("Successfully changed index in course " + chosenCourse + " to " + targetIndex + "\n");
                    } else {
                        System.out.println("Error in swapping courses, check that the course/index exists and that the target index you want to swap to has spare vacancies\n");
                    }
                    break;
                }
                case 6 -> {
                    // display registered courses
                    System.out.println("These are the courses you are enrolled in, along with their indexes:");
                    // print all courses the student is enrolled in
                    sm.displayStudentCourses();
                    System.out.println();

                    System.out.println("Please note that the swopping of index requires a mutual agreement, both of you have to Login to your account here in order to complete the swop.\n");
                    String quitOption = "1";

                    while (quitOption.equals("1")) {
                        //get course code
                        System.out.println("Please input the course code of your currently enrolled course that you wish to swop: ");
                        String courseCodeToSwap = sc.nextLine();
                        while (!sm.checkIfExistingCourse(courseCodeToSwap)) {
                            System.out.println("You are not enrolled in this course!");
                            System.out.println("Press 1 to continue. Enter anything else to quit."); 
                            quitOption = sc.nextLine();
                            if (!quitOption.equals("1")) break;
                            System.out.println("Please input the course code of your currently enrolled course that you wish to swop: ");
                            courseCodeToSwap = sc.nextLine();
                        }
                        if (!quitOption.equals("1")) break;
                        
                        //get index
                        System.out.println("Please input the index of your currently enrolled course which you wish to swop: ");
                        String ownIndex = sc.nextLine();
                        while (!sm.checkIfExistingIndex(courseCodeToSwap, ownIndex)) {
                            System.out.println("You are not enrolled in this index!");
                            System.out.println("Press 1 to continue. Enter anything else to quit."); 
                            quitOption = sc.nextLine();
                            if (!quitOption.equals("1")) break;
                            System.out.println("Please input the index of your currently enrolled course which you wish to swop: ");
                            ownIndex = sc.nextLine();
                        }
                        if (!quitOption.equals("1")) break;

                        //get peer username           
                        System.out.println("Please input your peer's username");
                        String peerUsername = sc.nextLine();
                        while (!AuthenticationManager.checkUsername(2, peerUsername)) {
                            System.out.println("Peer does not exist on this system. Please double check their username.");
                            System.out.println("Press 1 to continue. Enter anything else to quit."); 
                            quitOption = sc.nextLine();
                            if (!quitOption.equals("1")) break; 
                            System.out.println("Please input your peer's username");
                            peerUsername = sc.nextLine();  
                        }
                        if (!quitOption.equals("1")) break;
                        
                        //get peer password
                        System.out.println("Please input your peer's password");
                        Console cs = System.console();
                        char[] peerPassword = cs.readPassword();
                        while (!AuthenticationManager.checkPassword(2, peerUsername, new String(peerPassword))){
                            System.out.println("Peer password incorrect. Please double check their password.");
                            System.out.println("Press 1 to continue. Enter anything else to quit."); 
                            quitOption = sc.nextLine();
                            if (!quitOption.equals("1")) break;
                            System.out.println("Please input your peer's password");
                            peerPassword = cs.readPassword();
                        }
                        if (!quitOption.equals("1")) break;
                        
                        StudentManager peerSM = new StudentManager(peerUsername);
                        //get peer index
                        System.out.println("Please input your peer's enrolled index to be swopped with");
                        String peerIndex = sc.nextLine();
                        while (!peerSM.checkIfExistingIndex(courseCodeToSwap, peerIndex)) {
                            System.out.println("Peer is not enrolled in this index. Please double check their index.");
                            System.out.println("Press 1 to continue. Enter anything else to quit."); 
                            quitOption = sc.nextLine();
                            if (!quitOption.equals("1")) break;
                            System.out.println("Please input your peer's enrolled index to be swopped with");
                            peerIndex = sc.nextLine();
                        }
                        if (!quitOption.equals("1")) break;

                        int swapOutcome = sm.swopIndex(courseCodeToSwap, ownIndex, peerUsername, peerIndex); //execute swop index function
                        if (swapOutcome == -1)
                            System.out.println("The index you have input is invalid, you are not enrolled in this index.");
                        else if (swapOutcome == 0)
                            System.out.println("The index of your peer is invalid, he/she is not enrolled in this index.");
                        else if (swapOutcome == 1)
                            System.out.println("Swapped successfully.");
                        else if (swapOutcome == 2)
                            System.out.println("The two index are not within the same course");
                        else if (swapOutcome == 3)
                            System.out.println("Unsuccessful swap due to a TimeTable clash");
                        
                        // display registered courses
                        System.out.println("These are the courses you are enrolled in, along with their indexes:");
                        // print all courses the student is enrolled in
                        sm.displayStudentCourses();
                        System.out.println();
                        
                        break;
                    }
                    break;
                }

                case 7 -> {
                    System.out.println("This is your timetable: ");
                    System.out.printf("%-8s %-4s %-7s %-12s %-5s %-12s %-12s\n","Course", "AU", "Index", "Class Type", "Day", "Time", "Venue");
                    
                    // obtain course info
                    String course, au, index, classType, day, time, venue; 
                    ArrayList<ArrayList<ArrayList<String>>> timetable = sm.getTimeTableArray();
                    ArrayList<String> row = new ArrayList<String>();

                    for(int i=0;i<timetable.size();i++) {
                        for(int l=0;l<timetable.get(i).size();l++) {
                            row = timetable.get(i).get(l);
                            course = row.get(0);
                            au = row.get(1);
                            index = row.get(2);
                            classType = row.get(3);
                            day = row.get(4);
                            time = row.get(5);
                            venue = row.get(6);

                            System.out.printf("%-8s %-4s %-7s %-12s %-5s %-12s %-12s\n", 
                                course, au, index, classType, day, time, venue);
                        }
                    }
                }

                case 8 -> {
                    // change password
                    Console console = System.console();
                    System.out.println("Please enter your old password:");
                    char[] password = console.readPassword();
                    String strPassword = String.valueOf(password);
                    if (AuthenticationManager.checkPassword(2, username, strPassword)) {
                        System.out.println("Please enter a new password:");
                        char[] newPassword1 = console.readPassword();
                        String newStrPassword1 = String.valueOf(newPassword1);
                        System.out.println("Please re-enter the new password:");
                        char[] newPassword2 = console.readPassword();
                        String newStrPassword2 = String.valueOf(newPassword2);
                        if (newStrPassword1.equals(newStrPassword2)) {
                            sm.changePassword(newStrPassword2);
                            System.out.println("Password changed!");
                        }
                        else {
                            System.out.println("Passwords do not match!");
                        }
                    }
                    else {
                        System.out.println("Password invalid!");
                    }       
                    break;
                }
            }
	        System.out.println();
        }
        System.out.println("Exiting MySTARS Planner...\n");
        System.exit(0);
    }

    /**
     * Guard for user input when selecting which operation they would like to perform
     * 
     * @return                  user choice, from 1-9
     */
    public static int selectFunction() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your choice:\n1: Add a course\n2: Drop a course\n" +
                "3: Display registered courses\n4: Check vacancies in a course\n5: Change index number in a registered course\n" +
                "6: Swop Index with another student\n7: View timetable\n8: Change password\n9: Logout");
        
        int userInput = -1;
        try {
            userInput = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {

        }
        if (userInput < 1 || userInput > 9) {
            while(userInput < 1 || userInput > 9) {
                System.out.println("Invalid choice. Please select again: \n");
                try {
                    userInput = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {

                }
            }
        }
        return userInput;
    }
}
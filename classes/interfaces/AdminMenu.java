package classes.interfaces;

import java.time.*;
import java.time.format.*;
import java.util.*;
import java.io.Console;
import java.io.IOException;
import classes.controllers.*;
import classes.entities.*;

/**
 * AdminMenu is an interface class for system administrators to interact with the STARS system.
 * Administrators are able to perform 17 different functions through interacting with the AdminMenu interface class.
 * <ol>
 * <li>Print out a list of all students
 * <li>Editing student access period
 * <li>Adding a student into the system
 * <li>Print out a list of students enrolled in a course
 * <li>Print out a list of students enrolled in a course index
 * <li>Print out a list of all courses and their respective indexes
 * <li>Printing out course information
 * <li>Printing out course index information
 * <li>Adding a course and corresponding indexes into the system
 * <li>Adding an index to a preexisting course
 * <li>Changing the code of a preexisting course
 * <li>Changing the school of a preexisting course
 * <li>Changing the number of vacancies of a preexisting course index
 * <li>Changing the index of a preexisting course index
 * <li>Changing the sessions of a preexisting course index
 * <li>Checking how many vacancies are available for a preexisting course / course index
 * <li>Change password
 * </ol>
 */

public class AdminMenu {
    /**
     * Empty Class Constructor
     */
    public AdminMenu() {}

    /**
     * Date Time Formatter for date time objects used in this class
     */
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.ENGLISH);

    /**
     * Main function of the admin menu interface that system administrators will interact with.
     * 
     * @param ad                admin object that the admin menu operates on
     *
     * @throws IOException
     */
    public static void start(String username) throws IOException {
        Scanner sc = new Scanner(System.in);
        int userChoice = 0;

        AdminManager am = new AdminManager(username);
        while (userChoice != 18) {
            userChoice = selectFunction();
            int index;
            switch (userChoice) {
                //student-related functions
                case 1 -> {
                    //print list of all students
                    System.out.println("Printing list of students: ");
                    HashMap<String, String> studentList = StudentManager.getStudents();
                    int count =1;
                    System.out.format("      %-16s %-32s\n", "Username", "Name");
                    for (String user: studentList.keySet()){
                        String key = user.toString();
                        String value = studentList.get(user).toString();  
                        System.out.format("%-2d    %-16s %-32s\n", count, key, value);
                        count++;
                    } 
                    break;
                }
                
                case 2 -> {
                    // add a student into the system
                    System.out.println("Enter student username: ");
                    String usrnm = sc.nextLine();
                    int quit =0;
                    while (am.validStudent(usrnm)){
                        System.out.println("Username already exists.");
                        System.out.println("Enter another username. Enter a number to cancel: ");
                        usrnm = sc.nextLine();
                        quit = 0;
                        if (usrnm != null && usrnm.matches("[-+]?\\d*\\.?\\d+")){
                        	quit = 1;
                            break; 
                        }  
                    }
                    if (quit == 1) break;
                    while (usrnm.equals("") || usrnm.equals(" ") || Character.isDigit(usrnm.charAt(0)) || usrnm.contains(" ")){
                        System.out.println("Invalid Username");
                        System.out.println("Enter another username. Enter a number to cancel: ");
                        usrnm = sc.nextLine();
                        quit = 0;
                        if (usrnm != null && usrnm.matches("[-+]?\\d*\\.?\\d+")){
                        	quit = 1;
                            break; 
                        }  
                    }
                    if (quit == 1) break;
                    System.out.println("Enter student name: ");
                    String name = sc.nextLine();
                    System.out.println("Enter student matric number: ");
                    String matricNumber = sc.nextLine();
                    System.out.println("Enter student gender: ");
                    String gender = sc.nextLine();
                    while (! (gender.equals("M") || gender.equals("F"))){
                        System.out.println("Invalid Gender");
                        System.out.println("Enter another gender. Enter a number to cancel: ");
                        gender = sc.nextLine();
                        quit = 0;
                        if (gender != null && gender.matches("[-+]?\\d*\\.?\\d+")){
                        	quit = 1;
                            break; 
                        }  
                    }
                    if (quit == 1) break;
                    System.out.println("Enter student nationality: ");
                    String nationality = sc.nextLine();
                    am.addStudentSystem(usrnm, name, matricNumber, String.valueOf(gender), nationality);
                    System.out.println("\nAdded " + username + " into system!");
                    System.out.println("Printing list of students: ");
                    HashMap<String, String> studentList = StudentManager.getStudents();
                    int count =1;
                    System.out.format("      %-16s %-32s\n", "Username", "Name");
                    for (String user: studentList.keySet()){
                        String key = user.toString();
                        String value = studentList.get(user).toString();  
                        System.out.format("%-2d    %-16s %-32s\n", count, key, value);
                        count++;
                    } 
                    break;
                }

                case 3 -> {
                    // edit access period
                    System.out.println("Enter student username: ");
                    String usrnm = sc.nextLine();
                    // invalid username
                    if (!am.validStudent(usrnm)){
                        System.out.println("Username does not exist. Please try again later.");
                        break;
                    }
                    System.out.println("Enter start date in DD/MM/YYYY format: ");
                    String start = sc.nextLine();
                    System.out.println("Enter start time in 24hr format (e.g. 23:59): ");
                    String startTime = sc.nextLine();
                    System.out.println("Enter end date in DD/MM/YYYY format: ");
                    String end = sc.nextLine();
                    System.out.println("Enter end time in 24hr format (e.g. 23:59): ");
                    String endTime = sc.nextLine();

                    LocalDateTime startDate;
                    LocalDateTime endDate;

                    try {
                        startDate = LocalDateTime.parse(start + " " + startTime, formatter);
                        endDate = LocalDateTime.parse(end + " " + endTime, formatter);
                        am.editAccessPeriod(usrnm, startDate, endDate);
                        System.out.println("Successfully edited access date of student " + usrnm);
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date format. Please try again later.");
                        //e.printStackTrace();
                    }

                    
                    break;
                }

                case 4 -> {
                    // print student list by course
                    System.out.println("Enter course code: ");
                    String courseCode = sc.nextLine();
                    if (!AdminManager.checkCourse(courseCode)) {
                    	System.out.println("Course does not exist. Please try again later.");
                        break;
                    }
                    am.viewCourseInfo(courseCode);
                    break;
                }

                case 5 -> {
                    // print student list by index 
                    System.out.println("Enter course code: ");
                    String courseCode = sc.nextLine();
                    if (!AdminManager.checkCourse(courseCode)) {
                    	System.out.println("Course does not exist. Please try again later.");
                        break;
                    }
                    System.out.println("Enter index number: ");
                    try{
                        index = Integer.parseInt(sc.nextLine());
                        if (!AdminManager.checkIndex(courseCode, String.valueOf(index))){
                            System.out.println("Index does not exist. Please try again later.");
                            break;
                        }
                        am.viewIndexInfo(courseCode, index);
                    } catch (NumberFormatException e){
                        System.out.println("Invalid index. ");
                    }
                    break;
                }

                case 6 -> {
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
                    break;
                }

                case 7 -> {
                    // print course info
                    System.out.println("Enter course code: ");
                    String courseCode = sc.nextLine();
                    boolean result = AdminManager.checkCourse(courseCode);
                    if(result==false)  {
                        System.out.println("Course does not exist. Please try again later.");
                        break;
                    }
                    am.printCourseInfo(courseCode);
                    break;

                }

                case 8 -> {
                    // print index info
                    System.out.println("Enter course code: ");
                    String courseCode = sc.nextLine();
                    boolean result = AdminManager.checkCourse(courseCode);
                    if(result==false)  {
                        System.out.println("Course does not exist. Please try again later.");
                        break;
                    }
                    System.out.println("Enter index number: ");
                    try{
                        index = Integer.parseInt(sc.nextLine());
                        result = AdminManager.checkIndex(courseCode, String.valueOf(index));
                        if(result==false)  {
                            System.out.println("Index does not exist. Please try again.");
                            break;
                        }
                        am.printIndexInfo(courseCode, index);
                    } catch (NumberFormatException e){
                        System.out.println("Invalid index. ");
                    }
                    break;
                }

                case 9 -> {
                    // add a course and corresponding indexes into the system
                    System.out.println("Enter course code: ");
                    String courseCode = sc.nextLine();
                    boolean result = AdminManager.checkCourse(courseCode);
                    if(result==true)  {
                        System.out.println("Course already exists. Please try again later.");
                        break;
                    }
                    int quit = 0;
                    while (!courseCode.matches("\\w{2}\\d{4}")) {
                    	System.out.println("Invalid format. Course code should follow this example format: CZ2002");
                    	System.out.println("Enter a new course code. Enter an integer to quit: ");
                    	courseCode = sc.nextLine();
                        quit = 0;
                        if (courseCode != null && courseCode.matches("[-+]?\\d*\\.?\\d+")) {
                        	quit = 1;
                        	break;
                        }
                    }
                    if (quit == 1) break;
                    System.out.println("Enter number of indexes: ");
                    int no = Integer.parseInt(sc.nextLine());
                    int[] indexNumbers = new int[no];
                    for(int i=0; i<no; i++) {
                        System.out.println("Enter index number: ");
                        try{
                            index = Integer.parseInt(sc.nextLine());
                            indexNumbers[i] = index;
                        } catch(NumberFormatException e){
                            System.out.println("Index not captured!");
                            i--;
                        }
                    }
                    am.addCourse(courseCode, indexNumbers);
                    System.out.println("Course has been added successfully.\n");
                    
                    Map<String,ArrayList<String>> list = IndexManager.getCourseList();
                    
                    int cnt = 1;
                    System.out.format("      %-10s %-32s\n", "CourseCode", "Index");
                    for (String course: list.keySet()){
                        String key = course.toString();
                        String value = String.join(", ", list.get(key));
                        System.out.format("%-2d    %-10s %-32s\n", cnt, key, value);
                        cnt++;
                    } 
                    break;
                }

                case 10 -> {
                    // add index
                    System.out.println("Enter course code: ");
                    String courseCode = sc.nextLine();
                    boolean result = AdminManager.checkCourse(courseCode);
                    if(result==false)  {
                        System.out.println("Course does not exist. Please add course first and try again later.");
                        break;
                    }
                    System.out.println("Enter index to add: ");
                    String indexToAdd = sc.nextLine();
                    result = am.addIndex(courseCode, indexToAdd);
                    if(result==false) System.out.println("Index already exists. Please try again.");
                    else {
                    	System.out.println("Index has been added successfully!");
                    	am.printCourseInfo(courseCode);
                    }
                    break;
                }

                case 11 -> {
                    //change courseCode
                    System.out.println("Enter old course code: ");
                    String courseCode = sc.nextLine();
                    boolean result = AdminManager.checkCourse(courseCode);
                    if(result==false)  {
                        System.out.println("Course does not exist. Please try again later.");
                        break;
                    }
                    System.out.println("Enter new course code: ");
                    String newCourseCode = sc.nextLine();
                    int quit = 0;
                    while (!newCourseCode.matches("\\w{2}\\d{4}")) {
                    	System.out.println("Invalid format. Course code should follow this example format: CZ2002");
                    	System.out.println("Enter a new course code. Enter an integer to quit: ");
                    	newCourseCode = sc.nextLine();
                        quit = 0;
                        if (newCourseCode != null && newCourseCode.matches("[-+]?\\d*\\.?\\d+")) {
                        	quit = 1;
                        	break;
                        }
                    }
                    if (quit == 1) break;
                    if (AdminManager.checkCourse(newCourseCode)) {
                    	System.out.println("Course code already exists. Please try again later.");
                    	break;
                    }
                    result = am.changeCourseCode(courseCode, newCourseCode);
                    if(result==false) System.out.println("Course has not been successfully changed. Either the course does not exist or the new course code already exists.");
                    else {
                    	System.out.println("Course code has been changed successfully!");
                    	am.printCourseInfo(newCourseCode);
                    }
                    break;       
                }

                case 12 -> {
                    // change school
                    System.out.println("Enter course code: ");
                    String courseCode = sc.nextLine();
                    boolean result = AdminManager.checkCourse(courseCode);
                    if(result==false)  {
                        System.out.println("Course does not exist. Please try again.");
                        break;
                    }
                    System.out.println("Enter new school: ");
                    String school = sc.nextLine();
                    result = am.changeSchool(courseCode, school);
                    if(result==false) System.out.println("Course does not exist. Please try again.");
                    else {
                    	System.out.println("School has been changed successfully!");
                    	am.printCourseInfo(courseCode);
                    }
                    break;
                }

                case 13 -> {
                    // change index number
                    System.out.println("Enter course code: ");
                    String courseCode = sc.nextLine();
                    boolean result = AdminManager.checkCourse(courseCode);
                    if(result==false)  {
                        System.out.println("Course does not exist. Please try again later.");
                        break;
                    }
                    System.out.println("Enter index to change: ");
                    String indexToChange = sc.nextLine();
                    result = AdminManager.checkIndex(courseCode, indexToChange);
                    if(result==false)  {
                        System.out.println("Index does not exist. Please try again later.");
                        break;
                    }
                    System.out.println("Enter new index: ");
                    String newIndex = sc.nextLine();
                    result = AdminManager.checkIndex(courseCode, newIndex);
                    if(result==true)  {
                        System.out.println("Index already exists. Please try again later.");
                        break;
                    }
                    result = am.changeIndex(courseCode, indexToChange, newIndex);
                    if(result==false)  {
                        System.out.println("Index change unsuccesful.");
                        break;
                    }
                    
                    else {
                    	System.out.println("Index changed.");
                    	am.printCourseInfo(courseCode);
                    }
                    break;
                }

                case 14 -> {
                    // edit index vacancy
                    System.out.println("Enter course code: ");
                    String courseCode = sc.nextLine();
                    System.out.println("Enter index: ");
                    String ind = sc.nextLine();
                    if (!AdminManager.checkIndex(courseCode, ind)){
                        System.out.println("Index does not exist. Please try again.");
                        break;
                    };
                    System.out.println("Enter new vacancy for index: ");
                    int newVacancy = Integer.parseInt(sc.nextLine());
                    while (newVacancy < 0) {
                    	System.out.println("Vacancy cannot be negative. Please re-enter: ");
                    	newVacancy = Integer.parseInt(sc.nextLine());
                    }
                    boolean result = am.changeIndexVacancy(courseCode, ind, newVacancy);
                    if(result==false) System.out.println("Index does not exist. Please try again.");
                    else {
                    	System.out.println("Index vacancy changed successfully!");
                    	am.printCourseInfo(courseCode);
                    }
                    break;
                }

                case 15 -> {
                    // edit session
                    System.out.println("Enter course code: ");
                    String courseCode = sc.nextLine();
                    System.out.println("Enter index: ");
                    String ind = sc.nextLine();
                    if (!AdminManager.checkIndex(courseCode, String.valueOf(ind))){
                        System.out.println("Index does not exist. Please try again later.");
                        break;
                    };
                    System.out.println("Enter number of sessions: ");
                    int sess = 0;
                    try {
                    	sess = Integer.parseInt(sc.nextLine());
                    } catch(NumberFormatException e) {
                    	System.out.println("Invalid number of sessions. Please try again later.");
                    }
                     
                    int result;
                    ArrayList<Integer> sessionIDs = new ArrayList<Integer>();
                    String location, newDay, newStartTime, newEndTime, classType;
                    for (int i = 1; i <= sess; i++){
                        System.out.println("Enter the location of session " + i + ": ");
                        location = sc.nextLine();
                        System.out.println("Enter the day of session " + i + ": ");
                        newDay = sc.nextLine();
                        System.out.println("Enter the start time of session " + i + ": ");
                        newStartTime = sc.nextLine();
                        System.out.println("Enter the end time of session " + i + ": ");
                        newEndTime = sc.nextLine();
                        System.out.println("Enter the class type of session " + i + ": ");
                        classType = sc.nextLine();
                        result = classes.entities.Lesson.addLesson(location, newDay, newStartTime, newEndTime, classType);
                        if (result == -1){
                            System.out.println("Timing and location clashed with existing session. Session " + i + " not added.");
                        } else{
                            System.out.println("Session has been added successfully");
                            sessionIDs.add(result);
                        }
                    }
                    boolean changeResult = am.changeSession(courseCode, ind, sessionIDs.stream().mapToInt(Integer::intValue).toArray());
                    if(changeResult==false) System.out.println("Index does not exist. Please try again.");
                    else {
                    	System.out.println("Lessons added for index " + ind);
                    	am.printCourseInfo(courseCode);
                    }
                    break;
                }
                
                case 16 -> {
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
                    /*
                    // check a course's vacancies
                    System.out.println("These are the courses available, along with their indexes:");
                    am.displayFile();
                    System.out.println();
                    */
                    // receive a valid input for the course code
                    System.out.println("Please enter the course that you wish to check for vacancies");

                    String chosenCourse = sc.nextLine();
                    if (!AdminManager.checkCourse(chosenCourse)) {
                    	System.out.println("Course does not exist. Please try again later.");
                        break;
                    }

                    // print out the vacancy of the course
                    if (!am.displayIndexVacancy(chosenCourse, "", 2)) 
                    	System.out.println("Unable to display course vacancies, check that you have inputted the correct Course Code\n");;
                    break;
                	
                }          

                case 17 -> {
                    // change password
                    Console console = System.console();
                    System.out.println("Please enter your old password:");
                    char[] password = console.readPassword();
                    String strPassword = String.valueOf(password);
                    if (AuthenticationManager.checkPassword(1, username, strPassword)) {
                        System.out.println("Please enter a new password:");
                        char[] newPassword1 = console.readPassword();
                        String newStrPassword1 = String.valueOf(newPassword1);
                        System.out.println("Please re-enter the new password:");
                        char[] newPassword2 = console.readPassword();
                        String newStrPassword2 = String.valueOf(newPassword2);
                        if (newStrPassword1.equals(newStrPassword2)) {
                            am.changePassword(newStrPassword2);
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
            System.out.println("");
        }
        System.out.println("Exiting MySTARS Planner...\n");
        System.exit(0);
    }

    /**
     * Guard for user input when selecting which operation they would like to perform.
     * 
     * @return                  user choice, from 1-18
     */
    public static int selectFunction() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your choice:\n1:  Print List of all Students\n2:  Add Student into System\n" +
                "3:  Edit Student Access Period\n4:  Print Student List by Course\n"+
                "5:  Print Student List by Index Number\n" +
                "6:  Print List of Courses\n" +
                "7:  Print Course Info\n8:  Print Index Info\n" +
                "9:  Add Course into System\n10: Add Index to existing course\n11: Edit Course Code\n"+
                "12: Edit School\n13: Edit Existing Index\n" + 
                "14: Edit Index Vacancy\n15: Edit Session\n16: Check Available Vacancies\n" +
                "17: Change Password\n18: Log Out");
        int userInput = -1;
        try{
            userInput = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e){
            // System.out.println("Invalid choice. Please select again: ");
        }
        if (userInput < 1 || userInput > 18) {
            while(userInput < 1 || userInput > 18) {
                System.out.println("Invalid choice. Please select again: ");
                try{
                    userInput = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e){
                    // System.out.println("Invalid choice. Please select again: ");
                }
            }
        }
        return userInput;
    }
}

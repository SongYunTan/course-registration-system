package classes.controllers;


import java.io.IOException;
import java.time.*;
import java.time.format.*;
import java.util.*;
import classes.entities.*;

/**
 * AdminManager contains methods for administrators to perform administrator management of the STARS system
 * <p>
 * These functions include:
 * <ul>
 * <li>Adding a student to the system
 * <li>Changing a student's time access to the system
 * <li>Adding a course to the system
 * <li>Adding an index to an existing course
 * <li>Changing a course code
 * <li>Changing a course school
 * <li>Changing a course index
 * <li>Changing the number of vacancies for a course index
 * <li>Changing the session details of a course index
 * <li>Displaying students by course
 * <li>Displaying students by course index
 * <li>Displaying a list of courses
 * <li>Displaying a list of course indexes
 * <li>Displaying the number of vacancies for a course index
 * </ul>
 * </p>
 */
public class AdminManager {
    /**
     * The administrator that is currently using the system
     */
    public Admin currAdmin;

    /**
     * The default password for a new student added to the system
     */
    private static final String PASSWORD = "password";

    /**
     * The formatter used for date objects
     */
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.ENGLISH);

    /**
     * Class Constructor specifying the administrator that is currently using the system
     */
    public AdminManager(String username) {
        currAdmin = Admin.createByUsername(username);
    }

    // private static String adminFile = "admin.csv";
    // private static String studentFile = "studentFlatFile.csv";
    // private static String courseFile = "indexFlatFile.csv";

    /**
     * Checks if username exists inside the system
     * 
     * @param username          the username being checked
     * 
     * @return                  <code>true</code> if username exists in the system
     */
    public boolean validStudent(String username){
        return Student.getUserPassword().containsKey(username.toLowerCase());
    }

    /**
     * Modifies the access period of a specified student
     * 
     * @param username          the username of the student being modified
     * @param startDate         the new start date of access of the student
     * @param endDate           the new end date of access of the student
     */
    public void editAccessPeriod(String username, LocalDateTime startDate, LocalDateTime endDate) {
        // accesses the access period flat file and edits the start and end dates
        Student stud = Student.createByUsername(username);
        stud.setAccessPeriod(startDate, endDate);
    }

    /**
     * Adds a new student into the system with certain default parameters
     * 
     * @param username          the username of the new student
     * @param name              the name of the new student in all caps e.g. SAMUEL ANDREW TAN
     * @param matricNumber      the matriculation number of the new student
     * @param gender            the gender of the new student
     * @param nationality       the nationality of the new student
     */
    public void addStudentSystem(String username, String name, String matricNumber, String gender, String nationality) {
        // create a Student object and pass it into the Student flat file
        // default password is password
        // default startAccess is 01/01/2000 at 00:00
        // default endAccess is 31/12/3000 at 00:00
        classes.entities.Student.addNewStudent(username, PASSWORD,
                LocalDateTime.parse("01/01/2000 00:00", formatter),
                LocalDateTime.parse("31/12/3000 00:00", formatter), name, matricNumber, gender, nationality, "", 0, "");
    }

    /**
     * Checks if a course code already exists
     * 
     * @return                  <code>true</code> if the course code already exists
     */
    public static boolean checkCourse(String courseCode) {
        return Index.checkCourse(courseCode);
    }

    /**
     * Checks if a course index already exists
     * 
     * @return                  <code>true</code> if the course index already exists
     */
    public static boolean checkIndex(String courseCode, String index) {
        return Index.checkIndex(courseCode, index);
    }
 
    /**
     * Adds a course into the system
     * 
     * @param courseCode        the course code of the new course e.g. CZ2002
     * @param indexNumbers      the index numbers of the new course e.g. 201
     * 
     * @return                  <code>true</code> if the operation was sucessful
     */
    public boolean addCourse(String courseCode, int[] indexNumbers) {
        for (int i = 0; i < indexNumbers.length; i++) {
            boolean result = IndexManager.addCourse(courseCode, String.valueOf(indexNumbers[i]));
            if(result==false) return false;
        }
        return true;
    }

    /**
     * Adds an index to a pre existing course
     * 
     * @param courseCode        the course code of the new course e.g. CZ2002
     * @param index             the new index being added to the course
     * 
     * @return                  <code>true</code> if the operation was sucessful
     */
    public boolean addIndex(String courseCode, String index) {
        boolean result = IndexManager.addIndex(courseCode, index);
        if(result==false) return false;
        return true;
    }

    /**
     * Modifies the course code of a prexisting course
     * 
     * @param courseCode        the old course code of the course being changed
     * @param newCourseCode     the new course code
     * 
     * @return                  <code>true</code> if the operation was successful
     */
    public boolean changeCourseCode(String courseCode, String newCourseCode) throws IOException {
        // old course code MUST exist -> if old course code does not exist, this function fails and returns false
        if(!AdminManager.checkCourse(courseCode)) return false;
        // new course code MUST NOT exist -> if it exists, cannot change old course code to one that already exists
        if(AdminManager.checkCourse(newCourseCode)) return false;
        
        // change the course code for all students in the student flat file too
        StudentManager.changeCourseCode(courseCode, newCourseCode);

        // change the course code of the course in the index flat file 
        boolean result = IndexManager.changeCourseCode(courseCode, newCourseCode);
        if(result==false) return false;
        else return true;
    }

    /**
     * Modifies the school of a pre-existing course
     * 
     * @param courseCode        the course code of the course being changed
     * @param school            the new school
     * 
     * @return                  <code>true</code> if the operation was successful
     */
    public boolean changeSchool(String courseCode, String school) {
        boolean result = IndexManager.changeSchool(courseCode, school);
        if(result==false) return false;
        else return true;
    }

    /**
     * Modifies the index number of a pre-existing course index
     * 
     * @param courseCode        the course code of the course being changed
     * @param index             the old index being changed
     * @param newIndex          the new index
     * 
     * @return                  <code>true</code> if the operation was successful
     */
    public boolean changeIndex(String courseCode, String index, String newIndex) throws IOException {
        // old course code and index MUST exist -> if old course code or index does not exist, this function fails and returns false
        if(!AdminManager.checkIndex(courseCode, index)) return false;
        // new index MUST NOT exist -> if it exists, cannot change index to one that already exists
        if(AdminManager.checkIndex(courseCode, newIndex)) return false;

        // change all current students' index
        StudentManager.changeIndexForAdmin(courseCode, index, newIndex);

        boolean result = IndexManager.changeIndex(courseCode, index, newIndex);

        if(result==false) return false;
        else return true;
    }

    /**
     * Modifies the number of vacancies of a course index
     * 
     * @param courseCode        the course code of the course being changed
     * @param index             the course index of the course being changed
     * @param vacancy           the new number of vacancies for the course index
     * 
     * @return                  <code>true</code> if the operation was sucessful
     */
    public boolean changeIndexVacancy(String courseCode, String index, int vacancy) {
        boolean result = IndexManager.changeIndexVacancy(courseCode, index, vacancy);
        
        return result;
    }

    /**
     * Modifies the lesson scheudle of a course index
     * 
     * @param courseCode        the course code of the course being changed
     * @param index             the course index of the course being changed
     * @param sessionIDs        the new list of session ids
     * 
     * @return                  <code>true</code> if the operation was sucessful
     */
    public boolean changeSession(String courseCode, String index, int[] sessionIDs) {
        // for the number of sessions, need to create new lesson row in lesson file
        // return the lesson ids
        boolean result = IndexManager.changeSessions(courseCode, index, sessionIDs);
        return result;
    }

    /**
     * Calls Index to display a list of all students enrolled in selected course in "Name: , Gender: , Nationality: ," format
     * 
     * @param courseCode        the course code of the course being searched
     * 
     * @throws IOexception
     */
    public void viewCourseInfo(String courseCode) throws IOException {
        // printing all information of a particular course 
        Index.displayByCourse(courseCode);
    }

    /**
     * Calls Index to display a list of all students enrolled in selected course index in "Name: , Gender: , Nationality: ," format
     * 
     * @param courseCode        the course code of the course being searched
     * @param index             the course index of the course being searched
     */
    public void viewIndexInfo(String courseCode, int index) {
        // printing all information of a particular course 
        Index.displayByIndex(courseCode, index);
    }

    /**
     * Calls Index to display information about a selected course in "Course Code: , Course Vacancy: , Index: , indexVacancy: , Sessions: , Acad Units: , School: " format
     * 
     * @param courseCode        the course code of the course being searched
     * 
     * @throws IOException
     */
    public void printCourseInfo(String courseCode) throws IOException {
        // printing all information of a particular course 
        Index.displayCourseInfo(courseCode);
    }

    /**
     * Calls Index to display information about a selected course index in "Course Code: , Course Vacancy: , Index: , indexVacancy: , Sessions: , Acad Units: , School: " format
     * 
     * @param courseCode        the course code of the course being searched
     * @param index             the course index of the course being searched
     */
    public void printIndexInfo(String courseCode, int index) {
        // printing all information of a particular course 
        Index.displayIndexInfo(courseCode, index);
    }
    /**
     * Calls Index to display information about all courses and indexes
     * 
     * @throws IOException
     */
    public void displayFile() throws IOException {
        Index.displayIndex();
    }

    /**
     * Calls Index to display information about a selected course and/or index in "courseCode/index : vacancies" format
     * 
     * @param chosenCourse      the course code of the course being searched
     * @param chosenIndex       the course index of the course being searched
     * @param choice            2 to display for ALL indexes a course has, 1 to display for selected index
     * 
     * @throws IOException 
     */
    public boolean displayIndexVacancy(String chosenCourse, String chosenIndex, int choice) throws IOException {
        return Index.displayVacancy(chosenCourse, chosenIndex, choice) != null;
    } 
    /**
     * Changes the password of the administrator
     * 
     * @param plainPassword     the new password of the administrator in plain text format
     * 
     * @return                  <code>true</code> if the modification was successful
     */
    public boolean changePassword(String plainPassword) {
        return currAdmin.setPassword(plainPassword);
    }

    /**
     * Changes the username of the administrator
     * 
     * @param newUsername       the new username of the administrator
     * 
     * @return                  <code>true</code> if the modification was successful
     */
    public boolean changeUsername(String newUsername) {
        return currAdmin.setUsername(newUsername);
    }
}
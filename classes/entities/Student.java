package classes.entities;

import java.util.*;
import java.io.*;
import java.time.*;
import java.time.format.*;

/**
 * Student is a concrete class extending User.
 * <p>
 * Student contains methods to read, edit, and write to studentFlatFile.csv flat file.
 * </p>
 * <p>
 * A Student object encapsulates the information needed for StudentManager operations. This state information includes:
 * <ul>
 * <li>Unique Username
 * <li>Password in hashed format
 * <li>Start date of access to STARS
 * <li>End date of access to STARS
 * <li>Name
 * <li>Unique Matriculation Number
 * <li>Gender
 * <li>Nationality
 * <li>List of Courses and Indexes currently enrolled in
 * <li>Total AU of courses currently enrolled in
 * <li>Courses that are in the wait list of the student
 * </ul>
 * </p>
 */

public class Student extends User{
    //attributes
    /**
     * The unique username of the student
     */
    private String username;
    /**
     * The student's username in hashed format
     */
    private String hashedPassword;
    /**
     * The student's start date of access to STARS
     */
    private LocalDateTime startAccess;
    /**
     * The student;s end date of access to STARS
     */
    private LocalDateTime endAccess;
    /**
     * The student's name (both first and last names)
     */
    private String name;
    /**
     * The student's unqiue matriculation number
     */
    private String matricNumber;
    /**
     * The student's gender
     */
    private String gender;
    /**
     * The student's nationality
     */
    private String nationality;
    /**
     * The student's enrolled list of courses in ';' delineated format
     */
    private String enrolled;
    /**
     * The student's total number of academic units
     */
    private int totalAU;
    /**
     * The courses currently in the student's wait list
     */
    private String waitlist;
    
    //static attributes
    /**
     * The address of the student flat file
     */
    private static String studentFile = "flatFiles/studentFlatFile.csv";

    /**
     * The format of localdatetime objects used by student
     */
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.ENGLISH);
    
    /** 
     * Class Constructor for an empty Student.
     */
    public Student() { 
        this.username = "student not found";
    }
    
    /**
     * Class Constructor specifying Student attributes.
     */
    public Student(String username, String hashedPassword, LocalDateTime startAccess, LocalDateTime endAccess,
    String name, String matricNumber, String gender, String nationality, String enrolled, int totalAU, String waitlist) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.startAccess = startAccess;
        this.endAccess = endAccess;
        this.name = name;
        this.matricNumber = matricNumber;
        this.gender = gender;
        this.nationality = nationality;
        this.enrolled = enrolled;
        this.totalAU = totalAU;
        this.waitlist = waitlist;
    }

    /**
     * Creates a Student object according to a username.
     * <p>
     * Parses through studentFlatFile.csv flat file to find matching username in order to instantiate Student object.
     * </p>
     * 
     * @param username              username of Student being instantiated
     * 
     * @return                      a Student object with a username corresponding to input username
     */
    public static Student createByUsername(String username) {
        Student student;
        try {
            BufferedReader br = new BufferedReader(new FileReader(Student.studentFile));
            String line = "";
            //iterate through flat file rows
            while ((line = br.readLine()) != null) {
                //save flat file row
                String[] row = line.split(User.csvSplitBy);
                //check if username corresponds
                if (row[0].toLowerCase().equals(username.toLowerCase())) {
                    // instantiate attributes
                    student = new Student(row[0], row[1], LocalDateTime.parse(row[2], formatter), LocalDateTime.parse(row[3], formatter), 
                    row[4], row[5], row[6], row[7], row[8], Integer.parseInt(row[9]), row[10]);
                    br.close();
                    return student;
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        student = new Student();
        return student;
    }

    /**
     * Creates a Student object according to a name.
     * <p>
     * Parses through studentFlatFile.csv flat file to find matching name in order to instantiate Student object.
     * </p>
     * 
     * @param name                  name of Student being instantiated
     * 
     * @return                      a Student object with a name corresponding to input name
     */
    public static Student createByName(String name) {
        Student student;
        try {
            BufferedReader br = new BufferedReader(new FileReader(Student.studentFile));
            String line = "";
            //iterate through flat file rows
            while ((line = br.readLine()) != null) {
                //save flat file row
                String[] row = line.split(User.csvSplitBy);
                //check if username corresponds
                if (row[4].toLowerCase().equals(name.toLowerCase())) {
                    // instantiate attributes
                    student = new Student(row[0], row[1], LocalDateTime.parse(row[2], formatter), LocalDateTime.parse(row[3], formatter), 
                    row[4], row[5], row[6], row[7], row[8], Integer.parseInt(row[9]), row[10]);
                    br.close();
                    return student;
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        student = new Student();
        return student;
    }

    //getters
    /**
     * Returns Student's unique username.
     * 
     * @return      this Student's name
     */
    public String getUsername(){
        return this.username;
    }
    
    /**
     * Returns Student's start date of access to STARS system.
     * 
     * @return      this Student's starting access date
     */
    public LocalDateTime getStartDate(){
        return this.startAccess;
    }

    /**
     * Returns Student's end date of access to STARS system.
     * 
     * @return       this Student's ending access date
     */
    public LocalDateTime getEndDate(){
        return this.endAccess;
    }

    /**
     * Returns Student's name.
     * 
     * @return       this Student's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns Student's matriculation number.
     * 
     * @return       this Student's matriculation number
     */
    public String getMatricNumber() {
        return this.matricNumber;
    }
    
    /**
     * Returns Student's currently enrolled courses in ";" delimited format.
     * 
     * @return       this Student's currently enrolled list of courses
     */
    public String getStudentCourses() {
        return this.enrolled;
    }

    /**
     * Returns Student's gender
     * 
     * @return       this Student's gender
     */
    public String getGender() {
        return this.gender;
    }

    /**
     * Returns Student's nationality.
     * 
     * @return       this Student's nationality
     */
    public String getNationality() {
        return this.nationality;
    }

    /**
     * Returns Student's total number of Academic Units based on the courses he/she is currently enrolled in.
     * 
     * @return       this Student's total number of AUs
     */
    public int getTotalAU() { 
        return this.totalAU;
    }

    /**
     * Returns Student's current wait-listed courses in the format course/index (e.g. CZ2002/202), in ";" delineated format.
     * 
     * @return       this Student's wait-listed courses
     */
    public String getWaitlist() { 
        return this.waitlist;
    }

    /**
     * Edits the studentFlatFile.csv flat file to change a desired item delineated by desired row and column. 
     * <p>
     * Desired row is determined by the Student's unique username attribute.
     * </p>
     * <p>
     * Writes to a temporary file that is subsequently renamed to replace the original studentFlatFile.csv flat file.
     * </p>
     * 
     * @param newValue          the desired changed value
     * @param index             identifier used to delineate which column should be edited
     * 
     * @return                  Boolean representing whether the edit function was successful
     */ 
    public boolean editField(String newValue, int index) {
        return super.editField(this.username, newValue, index, Student.studentFile);
    }

    /**
     * Sets this student's start date of access and end date of access. 
     *  
     * @param startDate         the new start date of student access
     * @param endDate           the new end date of student access
     * 
     */ 
    public void setAccessPeriod(LocalDateTime startDate, LocalDateTime endDate){
        this.editField(startDate.format(Student.formatter), 2);
        this.editField(endDate.format(Student.formatter), 3);
        this.startAccess = startDate;
        this.endAccess = endDate;
    }

    /**
     * Sets this student's password
     * 
     * @param plainPassword         the new password in plain text format
     * 
     * @return                      <code>true</code> if the operation was successful
     */
    public boolean setPassword(String plainPassword) {
        boolean result = this.editField(plainPassword, 1);
        this.hashedPassword = HashingMachine.hashPassword(plainPassword);
        return result;
    }

    /**
     * Sets this student's enrolled
     * 
     * @param newEnrolled           the new list of enrolled courses in ";" delineated format
     * 
     * @return                      <code>true</code> if the operation was successful
     */
    public boolean setEnrolled(String newEnrolled) {
        boolean result = this.editField(newEnrolled, 8);
        this.enrolled = newEnrolled;
        return result;
    }

    /**
     * Sets this student's total AU
     * 
     * @param setAU                 the new total AU value
     * 
     * @return                      <code>true</code> if the operation was successful
     */
    public boolean setAU(int newAU) {
        boolean result = this.editField(Integer.toString(newAU), 9);
        this.totalAU = newAU;
        return result;
    }

    /**
     * Sets this student's waitlist status
     * 
     * @param newWaitlist           the new waitlist in ";" delineated format
     * 
     * @return                      <code>true</code> if the operation was successful
     */
    public boolean setWaitlist(String newWaitlist) {
        boolean result = this.editField(newWaitlist, 10);
        this.waitlist = newWaitlist;
        return result;
    }

    /**
     * Returns a Hashmap of student usernames and student names
     * 
     * @return              Hashmap<String, String> of username:Name key:value pairs
     */
    public static HashMap<String, String> getStudentList(){
        HashMap<String, String> studentList = new HashMap<String, String>();
        
        try {
            BufferedReader br = new BufferedReader(new FileReader(studentFile)); //file containing passwords
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] nameList = line.split(User.csvSplitBy);
                // column 1 is username, column 5 is name
		if (nameList[0].equals("username"))
	 	    continue;
                studentList.put(nameList[0], nameList[4]);
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return studentList;
    }

    /**
     * Returns a Hashmap that can be used to determine if Username exists in the studentFlatFile.csv flat file.
     * 
     * @return                  Hashmap<String, String> of username:hashedpassword key:value pairs in the student .csv flat file
     */
    public static HashMap<String, String> getUserPassword(){
        return User.getUserPassword(Student.studentFile);
    }

    /**
     * Adds a Student object to studentFlatFile.csv
     * <p>
     * Parameters must contain all 11 constructors for a new Student.
     * </p>
     * <p
     * Example: Student.addnewStudent(JYIP123, hashashash, 01:02:2020, 01:02:2020, Justin Yip, 1234567, M, Singaporean, 1107;2002;2006, 10, 2003);
     * </p>
     * 
     * @param username          unique identifier for each student, used to access student data
     * @param hashedPassword    unique hash derived from plain text password
     * @param startAccess       start date of user access
     * @param endAccess         end date of user access
     * @param name              name of student
     * @param matricNumber      matriculation number of student
     * @param gender            gender of student
     * @param nationality       nationality of student
     * @param coursesEnrolled   all courses that a student is currently enrolled in, in ";" delimited format
     * @param totalAU           the total number of Academic Units that the student currently holds
     * @param waitlist          the current courses that are in the student's wait list
     * 
     * @return                  <code>true</code> if instantiating the Student object was successful
     */
    public static boolean addNewStudent(String username, String plainPassword, LocalDateTime startAccess, LocalDateTime endAccess,
    String name, String matricNumber, String gender, String nationality, String coursesEnrolled, int totalAU, String waitlist) {
        
        //check studentFlatFile.csv to see if username is already taken. If taken, return false.
        try {
            BufferedReader br = new BufferedReader(new FileReader(Student.studentFile)); //file containing passwords
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] row = line.split(User.csvSplitBy);
                if (row[0] == username) {
                    br.close();
                    return false;
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // hash password
        String hashedPassword = HashingMachine.hashPassword(plainPassword);

        // convert from DateTime to String using SimpleDateFormat
        String start = startAccess.format(Student.formatter);
        String end = endAccess.format(Student.formatter);


    	try {
	        FileWriter pw = new FileWriter(Student.studentFile, true); 
	        pw.append(username + "," + hashedPassword + "," + start + "," + end + "," + name + 
                    "," + matricNumber + "," + gender + "," + nationality + "," + coursesEnrolled + "," + totalAU + "," + waitlist); 
	        pw.append("\n");
            // System.out.println("Student has been added successfully");
	        pw.flush();
	        pw.close();
            return true;
    	} catch (IOException e) {
            return false;
    	}
    }

    /*
    public static ArrayList<Student> findByCourse(String course) {
        ArrayList<Student> studArr = new ArrayList<Student>();
        try {
            BufferedReader bur = new BufferedReader(new FileReader(Student.studentFile));
            String line = "";
            //iterate through flat file rows
            while ((line = bur.readLine()) != null) {
                //save flat file row
                String[] row = line.split(User.csvSplitBy);
                //check if course corresponds
                String[] courseArr = row[10].split(";");
                
                for (int i = 0; i < courseArr.length; i++) {
                    String[] courseInd = courseArr[i].split("/");
                    
                    if (courseInd[0].equals(course)) {
                        // instantiate attributes
                        Student student = new Student(row[0], row[1], LocalDateTime.parse(row[2], formatter), LocalDateTime.parse(row[3], formatter), 
                        row[4], row[5], row[6], row[7], row[8], Integer.parseInt(row[9]), row[10]);
                        studArr.add(student);
                    }
                }
            }
            bur.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return studArr;
    }
    */
}

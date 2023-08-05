package classes.controllers;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import classes.entities.*;

/**
 * StudentManager contains methods for students and administrators to modify student data in the STARS system.
 * <p>
 * These functions include:
 * <ul>
 * <li>
 * <li>
 * <li>
 * <li>
 * <li>
 * <li>
 * <li>
 * <li>
 */

public class StudentManager {
    /**
     * The student that is currently using the system/being modified
     */
    public Student currStudent;
    
    /**
     * Class Constructor specifying the student that is currently using the system/being modified
     */
    public StudentManager(String username) {
        currStudent = Student.createByUsername(username);
        
        if (currStudent == null || currStudent.getUsername().equals("student not found")){
            currStudent = Student.createByName(username);
        }
    }

    //INFORMATION GETTERS
    /**
     * Calls Student to get an array of all UserName: StudentName pairs
     * 
     * @return              an array of all UserName: StudentName pairs
     */
    public static HashMap<String, String> getStudents() {
        return Student.getStudentList();
    }

    /**
     * Gets a timetable array of all the student's enrolled courses
     * 
     * @return              a timetable array of all the student's enrolled courses
     */
    public ArrayList<ArrayList<ArrayList<String>>> getTimeTableArray() {
        // get courses that student has, so we know what index he/she is in
        String studentCourses = currStudent.getStudentCourses(); // "CZ2002/201;CZ2001/204"
        String waitlistCourses = currStudent.getWaitlist(); // "CZ2003/203;CZ2005/205"
        if (waitlistCourses.equals("")) studentCourses += ";" + waitlistCourses; // add wait list courses to timetable

        String[] courseArr = studentCourses.split(";"); // looks like this: ["CZ2002/201", "CZ2001/204"]

        // create the multi-dimensional array to store each index and its attributes
        ArrayList<ArrayList<ArrayList<String>>> timeTableArray = new ArrayList<ArrayList<ArrayList<String>>>();
        
        for (int i=0; i<courseArr.length; i++) {
            String course = courseArr[i].split("/")[0];
            String index = courseArr[i].split("/")[1];
            ArrayList<ArrayList<String>> indexDetails = IndexManager.getTimetableDetails(course, index);
            timeTableArray.add(indexDetails);
        }

        return timeTableArray;
    }

    /**
     * Checks if a student is already enrolled in a specified course
     * 
     * @param courseCode            the course code being checked
     * 
     * @return                      <code>true</code> if the student is already enrolled in the specified course
     */
    // checks if student is already enrolled in that course
    public boolean checkIfExistingCourse(String courseCode) {
        String courses = currStudent.getStudentCourses();
        
        String[] courseArr = courses.split(";"); // CZ2002/201 -> CZ2003/201
        for (int j=0; j<courseArr.length; j++) {
            String pair = courseArr[j];
            if (!pair.equals("")) {
                String[] pairArr = pair.split("/"); // ["CZ2002", "201"] -> ["CZ2003", "201"]
                if (pairArr[0].equals(courseCode)) {
                    return true;
                }
            }
            
        }
        return false;
    }

    /**
     * Checks if a student is already enrolled in a specified course index
     * 
     * @param courseCode            the course code being checked
     * @param courseIndex           the course index being checked
     * 
     * @return                      <code>true</code> if the student is already enrolled in the specified course
     */
    // checks if student is already enrolled in that index
    public boolean checkIfExistingIndex(String courseCode, String courseIndex) {
        String courses = currStudent.getStudentCourses();
        String[] courseArr = courses.split(";"); // CZ2002/201 -> CZ2003/201
        for (int j=0; j<courseArr.length; j++) {
            String pair = courseArr[j];
            String[] pairArr = pair.split("/"); // ["CZ2002", "201"] -> ["CZ2003", "201"]
            if (pairArr[0].equals(courseCode) && pairArr[1].equals(courseIndex)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the 21 AU limit will be exceeded upon being enrolled in a selected course
     * 
     * @param chosenCourse              the course being checked
     * @param chosenIndex               the course index being checked
     * 
     * @return                          1 if the 21 AU limit is not exceeded,
     *                                  the current total AU of the student if the AU limit is exceeded
     */
    public int checkAULimit(String chosenCourse, String chosenIndex) {
    	//before adding a course or adding a course to the waitlist, check first if it will exceed the AU Limit if the course will be added successfully
        int currTotalAU = currStudent.getTotalAU();
        Index idx = new Index(chosenCourse, chosenIndex);
        if ((currTotalAU + idx.getAcadUnits()) > 21) {return currTotalAU ;}
        else {return 1;}	
    }

    /**
     * Checks if there will be a timetable clash upon being enrolled in a selected course
     * 
     * @param newCourse                 the course being checked
     * @param newIndex                  the course index being checked
     * 
     * @return                          <code>true</code> if there is no timetable clash
     */
    public boolean checkTimeClash(String newCourse, String newIndex) {  // course and index to add
        String studentCourses = currStudent.getStudentCourses();
        String[] courseArr = studentCourses.split(";"); // looks like this: ["CZ2002/201", "CZ2001/204"]

        // cater to students that have no existing courses, simply return true since there will be no clash
        if (courseArr.length == 1 && courseArr[0].equals("")) return true;
        
        // get DayTime for new course and index 
        // {{Mon,1330,1430}, {Wed,1330,1430}}
        ArrayList<ArrayList<String>> newDayTime = IndexManager.getDayTime(newCourse, newIndex);

        ArrayList<ArrayList<String>> currAllDay = new ArrayList<ArrayList<String>>();
        
        for (int i=0; i<courseArr.length; i++) {
            String course = courseArr[i].split("/")[0];
            String index = courseArr[i].split("/")[1];
            // {{Mon,1330,1430}, {Wed,1330,1430}} for current index enrolled
            currAllDay.addAll(IndexManager.getDayTime(course, index));
        }
        
        // compare each lesson timing of new index with old lesson timing
        for(int j=0;j<newDayTime.size();j++) {
                for(int k=0;k<currAllDay.size();k++) {
                    String newDay = newDayTime.get(j).get(0);
                    String currDay = currAllDay.get(k).get(0);
                    if(newDay.equals(currDay)) {
                        int newStart = Integer.parseInt(newDayTime.get(j).get(1));
                        int newEnd = Integer.parseInt(newDayTime.get(j).get(2));
                        int currStart = Integer.parseInt(currAllDay.get(k).get(1));
                        int currEnd = Integer.parseInt(currAllDay.get(k).get(2));
                        if(newStart>=currStart && newStart<currEnd) {
                            return false;
                        } else if(newEnd>currStart && newEnd<=currEnd) {
                            return false;
                        }
                    } 
                }
            }
        return true;
    }


    //INFORMATION WRITERS
    /**
     * Enrolls a student into a course
     * 
     * @param chosenCourse              the course the student is enrolling in
     * @param chosenIndex               the course index that the student is enrolling in
     * 
     * @return                          -1 if there is input error,
     *                                  0 if the student is already in the course,
     *                                  1 if the student is enrolled successfully,
     *                                  2 if the index is currently full and the student will be asked if they want to be waitlisted,
     *                                  3 if the chosen index lesson time would cause a clash with the currently enrolled courses,
     *                                  current AU amount if the addition of the course would cause the AU limit to be exceeded
     * 
     * @throws IOException
     */

    public int addCourse(String chosenCourse, String chosenIndex, int fromWM) throws IOException {
    	//return -1 if there is input error (e.g. wrong course/course Index) 
    	//return 0 if the student is already in the course 
    	//return 1 if added successfully 
    	//return 2 if the index is currently full (studentMenu will proceed to ask the student if he/she wishes to be added to the waitlist) 
    	//return 3 if the chosen index lesson time clashes with current indexes
        //return current AU amount if the addition of the course will exceed the AU Limit 

        // check if student is already in that course
        if (checkIfExistingCourse(chosenCourse)) return 0;
        if (!Index.checkIndex(chosenCourse, chosenIndex)) return -1;
        Index idx = new Index(chosenCourse, chosenIndex);
        
        // check if course and index is already full
        if (idx.getIndexVacancy() !=0) {//case when it is not full yet:
        	
        	//check if the addition of the course will exceed the AU Limit             	        	          
        	if (checkAULimit(chosenCourse, chosenIndex)==1) {

	            if(checkTimeClash(chosenCourse, chosenIndex)== true) {
                    String studentCourses = currStudent.getStudentCourses();
                  
                    // necessary coupling with Student class i.e. StudentManager must know that courses are in index 8      
                    // add course if it does not exceed AU Limit or clash with current indexes
                    boolean success = IndexManager.addEnrolled(chosenCourse, chosenIndex, currStudent.getName());

                    //check for invalid input
                    if (success==false) return -1;
                    
                    // add the number of Academic Units that the student holds and make that change to the Student object and the flat file
                    Index toBeRemoved = new Index(chosenCourse, chosenIndex);
                    int finalStudentAU = currStudent.getTotalAU() + toBeRemoved.getAcadUnits();
                    currStudent.setAU(finalStudentAU);

                    // send in the new student's course array into the student flat file via Student class
                    if (studentCourses.equals("")) {
                        studentCourses = chosenCourse+"/"+chosenIndex;
                    }
                    else {
                        // append all courses that student takes into the new course string
                        studentCourses += ";"+chosenCourse+"/"+chosenIndex;
                    }
                    if ((!currStudent.setEnrolled(studentCourses))) return -1;
                    return 1;      
                }
                else return 3;
        	}
        	else {return checkAULimit(chosenCourse, chosenIndex);}
        }
       else {return 2;}
    }
        
    /**
     * Add the student to the waitlist of the chosen course 
     * 
     * @param chosenCourse              the course that the student is being waitlisted for
     * @param chosenIndex               the course index that the student is being waitlisted for
     * 
     * @return                          0 if the student is already enrolled in the waitlist
     *                                  1 if the student is enrolled in the waitlist successfully
     *                                  current AU amount if addition of the course would cause the student to exceed the AU limit
     * 
     * @throws IOException
     */
    public int addWaitlist(String chosenCourse, String chosenIndex) throws IOException {
    	//return 0 if the the course already exists in the student's waitlist 
    	//return 1 if added to waitlist successfully 
    	//return current AU Amount if the addition of the course will potentially exceed the AU Limit 
    	//no need to check for valid input as it will have been checked already in addEnrolled 

        //add to waitlist if the student choose to do so (known from studentMenu)
        // add course if it does not exceed AU Limit
    	if (checkAULimit(chosenCourse, chosenIndex)==1) {
    		
    		//check for whether the course already exists in the student's waitlist 
    		if (!IndexManager.addWaitlist(chosenCourse, chosenIndex, currStudent.getName())) return 0;
    		
    		String waitlistCourses = currStudent.getWaitlist();
    	    System.out.println("Courses in waitlist before addition\n" + waitlistCourses);
    	    // append all courses that student takes into the new course string
            if (waitlistCourses.equals("")) {
                waitlistCourses = chosenCourse+"/"+chosenIndex;
            }
            else {
                waitlistCourses += ";"+chosenCourse+"/"+chosenIndex;
            }
            
    	    System.out.println("Courses in waitlist after addition\n" + waitlistCourses);    
    	       		    		
            // add student into indexFlatFile waitlist
            IndexManager.addWaitlist(chosenCourse, chosenIndex, currStudent.getName());

            // send in the new student's course array into the student flat file via Student class
            // necessary coupling with Student class i.e. StudentManager must know that waitlist  are in index 10
            currStudent.setWaitlist(waitlistCourses);
            
            WaitlistManager.subscribe(currStudent.getUsername(), "waitlist", chosenCourse, chosenIndex);
            
            return 1;
    	}
    	else {return checkAULimit(chosenCourse, chosenIndex);}      
    }

    /**
     * Removes a waitlisted course from a student's list of waitlisted courses
     * 
     * @param course                        the course being removed
     * @param index                         the course index being removed
     */
    public boolean removeStudentWaitlist(String course, String index){
        String waitlist = currStudent.getWaitlist();
        String[] waitlistArr = waitlist.split(";"); // waitlistArr holds all waitlisted courses for the particular student (with username)
        String updatedWaitlist = ""; //to be sent into the flat file

        // append all waitlist that are NOT deleted waitlist into the new waitlist string
        for (int i=0; i<waitlistArr.length; i++) {
            if (!(waitlistArr[i].equals(course + "/" + index))) {
                if (updatedWaitlist.equals("")) {
                    updatedWaitlist += waitlistArr[i];
                }
                else {
                    updatedWaitlist += ";"+waitlistArr[i];
                }
            }
        }

        // send the new student's waitlist array into the flat file via Student class
        if (!currStudent.setWaitlist(updatedWaitlist)) return false; // necessary coupling with Student class i.e. StudentManager must know that courses are in index 8
        
        return true;
    }

    /**
     * Removes a course from the list of student's enrolled courses. This updates the AU total of a student and creates a vacancy for a waitlisted student to fill the slot.
     * 
     * @param chosenCourse              the course the student is being unenrolled from
     * @param chosenIndex               the course index the student is being unenrolled from
     * 
     * @return                          <code>true</code> if the operation was successful
     * 
     * @throws NumberFormatException
     * @throws IOException
     */
    public boolean dropCourse(String chosenCourse, String chosenIndex) throws NumberFormatException, IOException {
        // remove the student from the index flat file (in that removed index)  
        if (!IndexManager.removeEnrolled(chosenCourse, chosenIndex, currStudent.getName())) return false;
        
        // reduce the number of Academic Units that the student holds and make that change to the Student object and the flat file
        Index toBeRemoved = new Index(chosenCourse, chosenIndex);
        int finalStudentAU = currStudent.getTotalAU() - toBeRemoved.getAcadUnits();
        currStudent.setAU(finalStudentAU);

        String studentCourses = currStudent.getStudentCourses();
        String[] courseArr = studentCourses.split(";"); // courseArr holds all enrolled courses for the particular student (with username)
        String updatedCourses = ""; //to be sent into the flat file 

        // append all courses that are NOT chosenCourse into the new course string
        for (int i=0; i<courseArr.length; i++) {
            if (!(courseArr[i].equals(chosenCourse + "/" + chosenIndex))) {
                if (updatedCourses.equals("")) {
                    updatedCourses += courseArr[i];
                }
                else {
                    updatedCourses += ";"+courseArr[i];
                }
            }
        }
        // send the new student's course array into the flat file via Student class 
        if (!currStudent.setEnrolled(updatedCourses)) return false; // necessary coupling with Student class i.e. StudentManager must know that courses are in index 8
        
        studentCourses = currStudent.getStudentCourses();

        // if removing this course increases the index vacancy from 0 to 1, call WaitlistManager to enroll the next student in the waitlist
        int indexVacancy = Integer.parseInt(Index.displayVacancy(chosenCourse, chosenIndex, 1));
        if (indexVacancy == 1) {
            // new slot open, time to add the student on the wait list in
            WaitlistManager.enrollStudent(chosenCourse, chosenIndex);
        }
        return true;
    }

    /**
     * Change the index of a course that a student is enrolled in to another index of the same course
     * 
     * @param chosenCourse              the course being modified
     * @param originalIndex             the original index that the student is currently enrolled in and will be unenrolled from
     * @param targetIndex               the new index that the student is being enrolled in
     * 
     * @return                          <code>true</code> if the operation is successful
     * 
     * @throws IOException
     */
    public boolean changeIndex(String chosenCourse, String originalIndex, String targetIndex) throws IOException {
        // check first if the student wants to swap away from an index he is NOT in (wrong original index)
        if (!checkIfExistingIndex(chosenCourse, originalIndex)) return false;

        // initialize both the student's current index and target index to be swapped to
        Index orig = new Index(chosenCourse, originalIndex);
        Index target = new Index(chosenCourse, targetIndex);

        // check if target index has enough vacancy for the student to change to
        int vacancies = target.getIndexVacancy();

        if (vacancies > 0) { // enough vacancy, change the index of the student
            // remove original course index from student
            this.dropCourse(chosenCourse, originalIndex);

            // add target course index to student
            this.addCourse(chosenCourse, targetIndex, 0);

            return true;
        }

        return false;
    }

    /**
     * Changes the course code that students are enrolled in. This is primarily called from the admin manager when the administrator renames a course.
     * 
     * @param courseCode                        the old course code
     * @param newCourseCode                     the new course code
     * @return                                  <code>true</code> if the operation was successful
     * @throws IOException
     */
    public static boolean changeCourseCode(String courseCode, String newCourseCode) throws IOException {
        // if this fn processes, AdminManager has already verified that the old course code and new course code are valid course codes
        String students = Index.getByCourse(courseCode);
        String[] studentArr = students.split(";");
        for (int i=0; i<studentArr.length; i++) {
            Student stud = Student.createByName(studentArr[i]);
            String courses = stud.getStudentCourses();
            if (courses==null){
                continue;
            }
            String[] courseArr = courses.split(";"); // CZ2002/201 -> CZ2003/201
            for (int j=0; j<courseArr.length; j++) {
                String pair = courseArr[j];
                String[] pairArr = pair.split("/"); // ["CZ2002", "201"] -> ["CZ2003", "201"]
                if (pairArr[0].equals(courseCode)) {
                    pairArr[0] = newCourseCode;
                }
                courseArr[j] = String.join("/", pairArr);
            }
            courses = String.join(";", courseArr);
            stud.setEnrolled(courses);
        }

        return true;
    }

    /**
     * Changes the course index that students are enrolled in. This is primarily called from the admin manager when the adniminstrator modifies a course index.
     * @param oldCourse                         the course being modified
     * @param oldIndex                          the old index being modified
     * @param newIndex                          the new index
     * @return                                  <code>true</code> if the operation was successful
     */
    // called from AdminManager to change index for all students
    public static boolean changeIndexForAdmin(String oldCourse, String oldIndex, String newIndex) {
        try {
            String students = Index.getByIndex(oldCourse, oldIndex);
            String[] studentArr = students.split(";");
            for (int j=0; j<studentArr.length; j++) {
                Student stud = Student.createByName(studentArr[j]);
                String courses = stud.getStudentCourses();
                if (courses==null) {
                    continue;
                }
                String[] courseArr = courses.split(";");
                for(int i=0; i<courseArr.length; i++) {
                    String pair = courseArr[i];
                    String[] pairArr = pair.split("/");
                    if(pairArr[0].equals(oldCourse) && pairArr[1].equals(oldIndex)) {
                        pairArr[1] = newIndex;
                    }
                    courseArr[i] = String.join("/", pairArr);
                }
                courses = String.join(";", courseArr);
                stud.setEnrolled(courses);  // call edit field to change index
            }
        }
        catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * Swaps the index of a course with another student. Both students must be in the same course and enrolled in different indexes.
     * If the swap would case a timetable clash, prevent the swap
     * 
     * @param chosenCourse                  the course code being swapped
     * @param ownIndex                      the index of the student initiating the swap
     * @param peerUserName                  the username of the student being swapped with
     * @param peerIndex                     the index of the student being swapped with
     * 
     * @return                              -1 if the student has input an index/course that they are not enrolled in,
     *                                      0 if the other student is not enrolled in their input course/index,
     *                                      1 if the index is swapped successfully,
     *                                      2 if the indexes are not within the same course
     *                                      3 if the swap was unsuccesful for time table reasons
     * 
     * @throws IOException
     */
    public int swopIndex(String chosenCourse, String ownIndex, String peerUserName,  String peerIndex) throws IOException {
        //return -1 if the student has input an index/course that he/she is not enrolled in 
        //return 0 if the student's peer has input an index/course that he/she is not enrolled in
        //return 1 if Index is swapped successfully
        //return 2 if the Indexes are not within the same course
        //return 3 if the swap was unsuccessful

        //Create a student manager for the student's peer (i.e."peerManager")
        
        StudentManager peerManager = new StudentManager(peerUserName);

        // check first if the input index are enrolled by the student/peer
        if (!this.checkIfExistingIndex(chosenCourse, ownIndex)) return -1;
        if (!peerManager.checkIfExistingIndex(chosenCourse, peerIndex)) return 0;

        //check if the input index are within the same course
        Index ownIdx = new Index(chosenCourse, ownIndex);
        Index peerIdx = new Index(chosenCourse, peerIndex);
        if (!ownIdx.getCourse().equals(peerIdx.getCourse())) return 2;

        // drop the students' courses first
        this.dropCourseNoRemoveWaitlist(chosenCourse, ownIndex);
        peerManager.dropCourseNoRemoveWaitlist(chosenCourse, peerIndex);

        int self = this.addCourse(chosenCourse, peerIndex, 0);
        int peer = peerManager.addCourse(chosenCourse, ownIndex, 0);

        // check if can add the target swap course. If can't, add back original course
        if (peer != 1 && self != 1) {
            // both failed to add their peer's courses, add back own courses
            this.addCourse(chosenCourse, ownIndex, 0);
            peerManager.addCourse(chosenCourse, peerIndex, 0);
            return 3;
        } else if (peer == 1 && self != 1) {
            peerManager.dropCourseNoRemoveWaitlist(chosenCourse, ownIndex);
            this.addCourse(chosenCourse, ownIndex, 0);
            peerManager.addCourse(chosenCourse, peerIndex, 0);
            return 3;
        } else if (peer != 1 && self == 1) {
            this.dropCourseNoRemoveWaitlist(chosenCourse, peerIndex);
            this.addCourse(chosenCourse, ownIndex, 0);
            peerManager.addCourse(chosenCourse, peerIndex, 0);
            return 3;
        }

        return 1;
    }

    /**
     * Unenrolls a student from a course without pushing in new students from the waitlist. Used primarily for swapping indexes.
     * 
     * @param chosenCourse                      the course the student is being unenrolled from
     * @param chosenIndex                       the course index the student is being unenrolled from
     * 
     * @return                                  <code>true</code> if the operation was successful
     * 
     * @throws NumberFormatException
     * @throws IOException
     */
    public boolean dropCourseNoRemoveWaitlist(String chosenCourse, String chosenIndex) throws NumberFormatException, IOException {
        // remove the student from the index flat file (in that removed index)
        if (!IndexManager.removeEnrolled(chosenCourse, chosenIndex, currStudent.getName())) return false;

        // reduce the number of Academic Units that the student holds and make that change to the Student object and the flat file
        Index toBeRemoved = new Index(chosenCourse, chosenIndex);
        int finalStudentAU = currStudent.getTotalAU() - toBeRemoved.getAcadUnits();
        currStudent.setAU(finalStudentAU);

        String studentCourses = currStudent.getStudentCourses();
        String[] courseArr = studentCourses.split(";"); // courseArr holds all enrolled courses for the particular student (with username)
        String updatedCourses = ""; //to be sent into the flat file

        // append all courses that are NOT chosenCourse into the new course string
        for (int i=0; i<courseArr.length; i++) {
            if (!(courseArr[i].equals(chosenCourse + "/" + chosenIndex))) {
                if (updatedCourses.equals("")) {
                    updatedCourses += courseArr[i];
                }
                else {
                    updatedCourses += ";"+courseArr[i];
                }
            }
        }
        // send the new student's course array into the flat file via Student class
        if (!currStudent.setEnrolled(updatedCourses)) return false; // necessary coupling with Student class i.e. StudentManager must know that courses are in index 8

        studentCourses = currStudent.getStudentCourses();

        return true;
    }

    /**
     * Change the password of the student
     * 
     * @return              <code>true</code> if the operation was successful
     */
    public boolean changePassword(String plainPassword) {
        return currStudent.setPassword(plainPassword);
    }
    
    /**
     * Returns the username of a student given name
     * 
     * @return              the username of a student
     */
    public String getUsername() {
        return currStudent.getUsername();
    }

    
    //DISPLAY METHODS
    /**
     * Displays a list of courses student is enrolled in
     * 
     * @throws IOException
     */
    public void displayStudentCourses() throws IOException {
        String studentCourses = currStudent.getStudentCourses();
        String[] courseArr = studentCourses.split(";");
        // print out each course in courseArr
        for (int i=0; i<courseArr.length; i++) {
            System.out.println(i + ": " + courseArr[i]);
        }
    }

    /**
     * Displays a list of all available courses along with their indexes
     * 
     * @throws IOException
     */
    public void displayFile() throws IOException {
        Index.displayIndex();
    }

    /**
     *  Displays information about a selected course and/or index in "courseCode/index : vacancies" format
	 * 
	 * @param chosenCourse			the selected course
	 * @param chosenIndex			the selected index
	 * @param choice				selected option, 2 will display for ALL indexes that a course has, 1 will only display for selected indexDisplays the number of vaca
     * 
     * @return                      <code>true</code> if information is successfully found and displayed
     * @throws IOException
     */
    public boolean displayIndexVacancy(String chosenCourse, String chosenIndex, int choice) throws IOException {
        return Index.displayVacancy(chosenCourse, chosenIndex, choice).equals("0");
    }
}

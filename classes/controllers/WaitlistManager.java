package classes.controllers;

import java.io.IOException;

import classes.entities.*;

/** 
 * WaitlistManager is a concrete class extending NotificationPublisher.
 * <p>
 * WaitlistManager contains static methods to perform waitlist related modifications
 * </p>
 */

public class WaitlistManager extends NotificationPublisher {
    /**
     * Empty Class Constructor
     */
    public WaitlistManager() {
    }
 
    /**
     * Enrolls a student in their wait-listed course and notifies them.
     * 
     * @param courseCode            the code of the course with the vacancy
     * @param courseIndex           the index of the course vacancy
     * 
     * @return                      1 if the operation is successful, -1 if unsuccessful
     */
    public static int enrollStudent(String courseCode, String courseIndex) throws IOException {
        // calls StudentManager.addCourse to push the 1st student in the wait list into the index he or she was waiting for
        

        // get the wait list from that course and index
        Index freedIdx = new Index(courseCode, courseIndex);
        String[] waitList = freedIdx.getWaitlist();

        for (int i=0; i<waitList.length; i++) {
            // get the first student on the wait list and add him/her to the course index
            String frontStud = waitList[i];
            
            if (waitList[i].equals("") || waitList[i].equals(" ")){
                return 1;
            }

            // try to add the course to the student FIRST (in case he/she is already overloaded)
            // Student stud = Student.createByName(frontStud);
            StudentManager sm = new StudentManager(frontStud);

            // check if enrolling frontStud in the wait listed course will burst AUs
            if (sm.checkAULimit(courseCode, courseIndex) == 1 && frontStud != null) {
                sm.addCourse(courseCode, courseIndex, 1); // adding the student passes IF addCourseStatus == 1
                
                // remove student from waiting list in student file 
                sm.removeStudentWaitlist(courseCode, courseIndex);

                // remove the first student from the wait list in that index
                IndexManager.removeWaitlistStudent(courseCode, courseIndex, i);

                // send notification upon successful enroll
                WaitlistManager.notify(sm.getUsername(), courseCode, courseIndex);

                // unsubscribe the student from the notification list
                NotificationPublisher.unsubscribe(sm.getUsername(), "waitlist", courseCode, courseIndex);
                return 1;
            }
        }

        return -1;
    }

    /**
     * Notifies a student of their newly accepted course.
     * 
     * @param username              unique username of the student
     * @param index                 the course:index the student was accepted into
     */
    public static void notify(String username, String courseCode, String index) {
    	String message = "You have been accepted on the waitlist for " + courseCode + "/" + index + "!";
    	NotificationPublisher.notify(username, "waitlist", message);
    }
}
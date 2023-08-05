package classes.controllers;

import classes.entities.*;
import java.util.*;

/**
 * IndexManager is a concrete class for interacting with Index, StudentManager and AdminManager.
 * <p>
 * IndexManager contains static methods that calls the Index class to change the indexFlatFile.csv.
 * </p>
 */

public class IndexManager { 
    /**
     * Empty Class Constructor
     */
    public IndexManager() {}

    /**
     * Calls Index to add a new course
     * 
     * @param course			the unique identifier of the course being added
	 * @param index				an identifier unique within each course being added	
	 * 
	 * @return 					<code>true</code> if the operation was successful
     */
    public static boolean addCourse(String course, String index) {
        Index.addCourse(course, index);
        return true;
    }

    /**
     * Calls Index to add a new index number to an existing course
     * 
     * @param course			the unique identifier of the existing course
	 * @param index				an identifier unique within each course being added	
	 * 
	 * @return 					<code>true</code> if the operation was successful
     */
    public static boolean addIndex(String course, String index) {
        if (!Index.checkCourse(course)) {return false;}  // course does not exist, cannot create index
        if (Index.checkIndex(course, index)) {return false;} // index already exists, cannot create again
        
        Index.addCourse(course, index);

        //update course vacancies appropriately
        Index idx = new Index(course);
        idx.setCourseVacancy(idx.getCourseVacancy());
        idx.setSchool(idx.getSchool());
        idx.setAcadUnits(idx.getAcadUnits());
        return true;
    }

    /**
     * Calls Index to return a hashmap of student courses and indexes
     * 
     * @return                  map of course:indexarray key:value pairs
     */
    public static Map<String, ArrayList<String>> getCourseList() {
    	return Index.getCourseList();
    }

    /**
     * Calls Index to change the name of a course
     * 
     * @param course            old name of the course being modified
     * @param newCourse         new name of the course
     * 
     * @return                  <code>true</code> if the operation was successful
     */
    public static boolean changeCourseCode(String course, String newCourse) {
        if (!Index.checkCourse(course)) {return false;} // course does not exist, cannot change course code
        
        Index idx = new Index(course);
        idx.setCourse(newCourse);
		return true;
    }

    /**
     * Calls Index to change the school of a course
     * 
     * @param course            name of the course being modified
     * @param school            new school of the course
     * 
     * @return                  <code>true</code> if the operation was successful
     */
    public static boolean changeSchool(String course, String school) {
        if (!Index.checkCourse(course)) {return false;} // course does not exist, cannot change school

        Index idx = new Index(course);
        idx.setSchool(school);
        return true;
    }

    /**
     * Calls Index to change the name of an index of a course
     * 
     * @param course            name of the course being modified
     * @param index             old name of the index being modified
     * @param newIndex          new name of the index
     * 
     * @return                  <code>true</code> if the operation was successful  
     */
    public static boolean changeIndex(String course, String index, String newIndex) {
        if (!Index.checkIndex(course, index)) {return false;} // index does not exist, cannot change index
        
        Index idx = new Index(course, index);
        idx.setIndex(newIndex);
        return true;
    }

    /**
     * Calls Index to change the number of vacancies available to an index
     * 
     * @param course            name of the course being modified
     * @param index             index of the course being modified
     * @param indexVacancy      new number of vacancies of the index
     * 
     * @return                  <code>true</code> if the operation was successful
     */
    public static boolean changeIndexVacancy(String course, String index, int indexVacancy) {
        if (!Index.checkIndex(course, index)) {return false;} // index does not exist, cannot change index vacancy

        Index idx = new Index(course, index);
        int valueToAdd = indexVacancy-idx.getIndexVacancy();
		int newCourseVacancy = idx.getCourseVacancy() + valueToAdd;

        // change index and course vacancies accordingly
        idx.setIndexVacancy(indexVacancy);
        idx.setCourseVacancy(newCourseVacancy);
        return true;
    }

    /**
     * Calls Index to add a enroll a student to an index
     * 
     * @param course            name of the course being modified
     * @param index             index of the course being modified
     * @param studentName       name of the student being added to the index, in all caps e.g. SAMUEL ANDREW TAN
     * 
     * @return                  <code>true</code> if the operation was successful
     */
    public static boolean addEnrolled(String course, String index, String studentName) {
        //check if index exists
        if (!Index.checkIndex(course, index)) {return false;}

        Index idx = new Index(course, index);
        
        //check if index has vacancies
        if (idx.getIndexVacancy() == 0) {return false;}
        
        //add student to enrolled array
        String[] studentArr = idx.getEnrolledStudents();
        LinkedList<String> newStudentLL = new LinkedList<String>();
        for(int i=0;i<studentArr.length;i++) {
            newStudentLL.add(studentArr[i]);
            if(studentArr[i].equals(studentName)) return false; // student alr exists, cannot add again
        }
        newStudentLL.add(studentName);
        String[] newStudentArr = newStudentLL.toArray(studentArr);
        String newStudentList = String.join(";",newStudentArr);

        // change index and course vacancies accordingly
        int newCourseVacancy = idx.getCourseVacancy() - 1;
        int newIndexVacancy = idx.getIndexVacancy() - 1;
        idx.setEnrolled(newStudentArr, newStudentList);
        idx.setCourseVacancy(newCourseVacancy);
        idx.setIndexVacancy(newIndexVacancy);
        
        return true;
    }
    
    /**
     * Calls Index to remove a student from an index
     * 
     * @param course            name of the course being modified
     * @param index             index of the course being modified
     * @param studentName       name of the student being removed from the index, in all caps e.g. SAMUEL ANDREW TAN
     * 
     * @return                  <code>true</code> if the operation was successful
     */
    public static boolean removeEnrolled(String course, String index, String studentName) {
        if (!Index.checkIndex(course, index)) {return false;}

        Index idx = new Index(course, index);
        boolean studentExists = false;
		//remove student
        String[] studentArr = idx.getEnrolledStudents();
        ArrayList<String> newStudentArrList = new ArrayList<String>();
        for(int i=0;i<studentArr.length;i++) {
            if(!studentArr[i].equals(studentName))
                newStudentArrList.add(studentArr[i]);
            else studentExists = true;
        }
        String[] newStudentArr = new String[newStudentArrList.size()];
        for (int i=0; i<newStudentArrList.size(); i++) {
            newStudentArr[i] = newStudentArrList.get(i);
        }
        String newStudentList = String.join(";",newStudentArr);

        int newCourseVacancy = idx.getCourseVacancy() + 1;
        int newIndexVacancy = idx.getIndexVacancy() + 1;
        idx.setEnrolled(newStudentArr,newStudentList);
        idx.setCourseVacancy(newCourseVacancy);
        idx.setIndexVacancy(newIndexVacancy);
		return studentExists;  // returns false is student does not exist, no change to file
    }

    /**
     * Calls Index to add a student to a waitlist and subscribe them for notifications
     * 
     * @param course            name of the course being modified
     * @param index             index of the course being modified
     * @param studentName       name of the student being added to the waitlist, in all caps e.g. SAMUEL ANDREW TAN
     * 
     * @return                  <code>true</code> if the operation was successful
     */
    public static boolean addWaitlist(String course, String index, String studentName) {
        if (!Index.checkIndex(course, index)) {return false;}
        Index idx = new Index(course, index);

        String[] waitlistArr = idx.getWaitlist();
        LinkedList<String> newWaitlistLL = new LinkedList<String>();
        if(waitlistArr[0].equals("")) {
            newWaitlistLL.add(studentName);
            String[] newWaitlistArr = newWaitlistLL.toArray(waitlistArr);
            String newWaitlistList = String.join("",newWaitlistArr);
            idx.setWaitlist(newWaitlistArr, newWaitlistList);
        } 
        else {
            for(int i=0;i<waitlistArr.length;i++) {
                newWaitlistLL.add(waitlistArr[i]);
                if(waitlistArr[i].equals(studentName)) return false; // student alr exists, cannot add again
            }
            newWaitlistLL.add(studentName);
            String[] newWaitlistArr = newWaitlistLL.toArray(waitlistArr);
            String newWaitlistList = String.join(";",newWaitlistArr);
            idx.setWaitlist(newWaitlistArr, newWaitlistList);
        }
       

        return true;
    }

    /**
     * Calls Index to remove a student from a specified position in a waitlist
     * 
     * @param course            name of the course being modified
     * @param index             index of the course being modified
     * @param position          position of the student to be removed in the wait list array
     * 
     * @return                  <code>true</code> if the operation was successful
     */
    public static boolean removeWaitlistStudent(String course, String index, int position) {
        if (!Index.checkIndex(course, index)) {return false;} // index does not exist, cannot remove waitlist

        Index idx = new Index(course, index);

        String[] waitlistArr = idx.getWaitlist();
        ArrayList<String> newWaitlistArray = new ArrayList<String>();
        for(int j=0;j<waitlistArr.length;j++) { // remove the first student
            if (j != position) newWaitlistArray.add(waitlistArr[j]);
        }
        String[] newWaitlistArr = new String[newWaitlistArray.size()];
        for (int k=0; k<newWaitlistArray.size(); k++) {
            newWaitlistArr[k] = newWaitlistArray.get(k);
        }
        String newWaitlistList = String.join(";",newWaitlistArr);
        idx.setWaitlist(newWaitlistArr, newWaitlistList);
        return true;
    }

    /**
     * Calls Index to modify session details
     * 
     * @param course            name of the course being modified
     * @param index             index of the course being modified
     * @param newSessions       name of the student being added to the waitlist, in all caps e.g. SAMUEL ANDREW TAN
     * 
     * @return                  <code>true</code> if the operation was successful
     */
    public static boolean changeSessions(String course, String index, int[] newSessions) {
        if (!Index.checkIndex(course, index)) {return false;} // index does not exist, cannot change sessions

        Index idx = new Index(course, index);
    
        // only has add
		String[] sessionArr = idx.getSessions();
		LinkedList<String> newSessionLL = new LinkedList<String>();
        for(int i=0;i<sessionArr.length;i++) {
            newSessionLL.add(sessionArr[i]);
        }
        for(int i=0;i<newSessions.length; i++) {
            newSessionLL.add(Integer.toString(newSessions[i]));
        }

        String[] newSessionArr = newSessionLL.toArray(sessionArr); 
        String newSessionList = String.join(";",newSessionArr);
        idx.setSessions(newSessionArr, newSessionList);
        return true; 
    }

    /**
     * Calls Index to modify the academic unit weightage of a course
     * 
     * @param course            name of the course being modified
     * @param newAcadUnits      new academic unit weightage of the course, typically between 1-4 inclusive
     * 
     * @return                  <code>true</code> if the operation was successful
     */
    public static boolean changeAcadUnits(String course, int newAcadUnits) {
        if (!Index.checkCourse(course)) {return false;} // course does not exist, cannot change acad units

        Index idx = new Index(course);
        
        idx.setAcadUnits(newAcadUnits);
        return true;
    }

    /**
     * Calls Index to retrieve timetable details of the index
     * 
     * @param course            name of the course
     * @param index             index being retrieved
     * 
     * @return                  matrix in the format {{course,au,index,classtype,day,starttime-endtime,location}, {etc}}
     */
    public static ArrayList<ArrayList<String>> getTimetableDetails(String course, String index) {
        ArrayList<ArrayList<String>> SessionMatrix = new ArrayList<ArrayList<String>>();
        if (!Index.checkIndex(course, index)) {return SessionMatrix;}  // index does not exist

        Index idx = new Index(course, index);
        String[] sessionArr = idx.getSessions();

        for(int i=0;i<sessionArr.length;i++) {
            ArrayList<String> SessionList = new ArrayList<String>();
            SessionList.add(course);
            String au=Integer.toString(idx.getAcadUnits());
            SessionList.add(au);
            SessionList.add(index);

            int id = Integer.parseInt(sessionArr[i]);
            Lesson lsn = new Lesson(id);
            SessionList.add(lsn.getClassType());
            SessionList.add(lsn.getDay());
            String time = String.join("-", lsn.getStartTime(), lsn.getEndTime());
            SessionList.add(time);
            SessionList.add(lsn.getLocation());
            SessionMatrix.add(SessionList);
        }

        return SessionMatrix;
    }

    /**
     * Calls Index to return all session timings associated with course index
     * 
     * @param course                        course code being searched            
     * @param index                         course index being searched
     * 
     * @return                              all session timings in {{Day, Startime, Endtime}, {ETC}} for the index
     */
    public static ArrayList<ArrayList<String>> getDayTime(String course, String index) {
        ArrayList<ArrayList<String>> allDayTime = new ArrayList<ArrayList<String>>();
        if (!Index.checkIndex(course, index)) {return allDayTime;}  // index does not exist

        Index idx = new Index(course, index);
        String [] sessionArr = idx.getSessions();

        for(int i=0;i<sessionArr.length;i++) {
            ArrayList<String> DayTime = new ArrayList<String>();
            Lesson lsn = new Lesson(Integer.parseInt(sessionArr[i]));
            DayTime.add(lsn.getDay());
            DayTime.add(lsn.getStartTime());
            DayTime.add(lsn.getEndTime());
            allDayTime.add(DayTime);
        }

        return allDayTime;
    }
}

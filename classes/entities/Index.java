package classes.entities;

import java.io.*;
import java.util.*;
//import org.apache.commons.lang3;

/**
 * Index is a concrete class for interacting with courses, indexes, and their related attributes.
 * <p>
 * Index contains methods to read, write, and edit indexFlatFile.csv flat file.
 * </p>
 * <p>
 * A Index object encapsulates the information needed for IndexManager operations. This state information includes:
 * </p>
 * <ul>
 * <li>A unique identifier for a course
 * <li>The number of vacancies the course has
 * <li>An index associated with the course
 * <li>The number of vacancies the specific index of the coures has
 * <li>A list of currently enrolled students
 * <li>A list of students on the waitlist
 * <li>A list of the course index's lessons
 * <li>The number of academic units the course is valued at
 * <li>The school that the course is being conducted by
 * </ul>
 */

public class Index {
	//attributes
	/**
	 * The unique identifier of this course
	 */
	private String course;

	/**
	 * The number of vacancies this course has
	 */
	private int courseVacancy;

	/**
	 * The current index associated with the course being operated upon
	 */
	private String index;

	/**
	 * The number of vacancies this index has
	 */
	private int indexVacancy;

	/**
	 * A list of student names currently enrolled in this course
	 */
	private String[] enrolled;

	/**
	 * A list of student names currently on the waitlist
	 */
	private String[] waitlist;

	/**
	 * A list of sessions that this index has
	 */
	private String[] sessions;

	/**
	 * The academic unit weightage of this course
	 */
	private int acadUnits;

	/**
	 * The school conducting this course
	 */
	private String school;

	//static attributes
	/**
	 * The address of the index flat file
	 */
	private static String indexPath = "flatFiles/indexFlatFile.csv";
	
	/**
     * A reader object for reading a flat file
     */
    private static BufferedReader br = null;
	 
	/**
	 * Class Constructor specifying the course of the Index
	 * <p>
	 * Index object is created by reading indexFlatFile.csv to find a the first row corresponding to the input course.
	 * </p>
	 * <p>
	 * Used for changing course codes, course vacancies, and course AU.
	 * </p>
	 */
	public Index(String course) {
		try {
			File indexFile = new File (indexPath);
            BufferedReader br = new BufferedReader(new FileReader(indexFile));
			String cvsSplitBy = ",";
			String line = "";
			while ((line = br.readLine()) != null) {
                String[] row = line.split(cvsSplitBy);
                if (row[0].equals(course)) {
					try{
						// instantiate attributes
						this.course = course;
						this.courseVacancy = Integer.parseInt(row[1]);
						this.index = "0";
						this.indexVacancy = Integer.parseInt(row[3]);
						String[] studentArr = row[4].split(";");
						this.enrolled = studentArr;
						String[] waitlistArr = row[5].split(";");
						this.waitlist = waitlistArr;
						String[] sessionsArr = row[6].split(";");
						this.sessions = sessionsArr;
						this.acadUnits = Integer.parseInt(row[7]);
						this.school = row[8];
						break;
					} catch (ArrayIndexOutOfBoundsException e){
						e.printStackTrace();
					}
                }
			}
			br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	/**
	 * Class Constructor specifying the course and index of the Index
	 * <p>
	 * Index object is created by reading indexFlatFile.csv to find a course:index corresponding to the input course:index.
	 * </p>
	 */
    public Index(String course, String index) {
		try {
			File indexFile = new File (indexPath);
            BufferedReader br = new BufferedReader(new FileReader(indexFile));
			String line = "";
			String cvsSplitBy = ",";
            while ((line = br.readLine()) != null) {
                String[] row = line.split(cvsSplitBy);
                if (row[0].equals(course) && row[2].equals(index)) {
					try{
						// instantiate attributes
						this.course = row[0];
						this.courseVacancy = Integer.parseInt(row[1]);
						this.index = row[2];
						this.indexVacancy = Integer.parseInt(row[3]);
						String[] studentArr = row[4].split(";");
						this.enrolled = studentArr;
						String[] waitlistArr = row[5].split(";");
						this.waitlist = waitlistArr;
						String[] sessionsArr = row[6].split(";");
						this.sessions = sessionsArr;
						this.acadUnits = Integer.parseInt(row[7]);
						this.school = row[8];
					} catch (ArrayIndexOutOfBoundsException e){
						e.printStackTrace();
					}
                }
			}
			br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	// getters
	/**
     * Returns Index's Course Code.
	 * 
	 * @return this index's course code
     */
	public String getCourse() {
		return this.course;
	}

	/**
     * Returns Index's total number of vacancies in the course.
	 * 
	 * @return this index's total number of course vacancies
     */
	public int getCourseVacancy() {
		return this.courseVacancy;
	}

	/**
     * Returns Index's index code.
	 * 
	 * @return this index's index code
     */
	public String getIndex() {
		return this.index;
	}

	/**
     * Returns Index's number of vacancies for the specific index.
	 * 
	 * @return this index's number of vacancies
     */
	public int getIndexVacancy() {
		return this.indexVacancy;
	}

	/**
     * Returns Index's list of currently enrolled students.
	 * 
	 * @return this index's currently enrolled students
     */
	public String[] getEnrolledStudents() {
		return this.enrolled;
	}
	
	/**
     * Returns Index's list of waitlisted students.
	 * 
	 * @return this index's currently waitlisted students
     */
	public String[] getWaitlist() {
		return this.waitlist;
	}

	/**
     * Returns Index's list of lesson sessions.
	 * 
	 * @return this index's current list of lessons
     */
	public String[] getSessions() {
		return this.sessions;
	}

	/**
     * Returns Index's course academic unit weightage.
	 * 
	 * @return this index's AU weightage 
     */
    public int getAcadUnits() {
        return this.acadUnits;
	}

	/**
     * Returns Index's school of conduct.
	 * 
	 * @return this index's school
     */
    public String getSchool() {
        return this.school;
    }

	/**
     * Returns a Hashmap of student courses and indexes
     * 
     * @return              map of course:indexarray key:value pairs
     */
    public static Map<String,ArrayList<String>> getCourseList(){
        Map<String,ArrayList<String>> listCourse = new HashMap<>();
        String cvsSplitBy = ",";
        try {
            BufferedReader br = new BufferedReader(new FileReader(indexPath)); //file containing passwords
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] courseL = line.split(cvsSplitBy);
                // column 1 is username, column 3 is name
				if (courseL[0].equals("courseCode") || courseL[2].equals("index")){
					continue;
				}
				else if (listCourse.containsKey(courseL[0])){
					listCourse.get(courseL[0]).add(courseL[2]);
				}

				else{
					ArrayList<String> index = new ArrayList<>();
					index.add(courseL[2]);
					listCourse.put(courseL[0], index);
				}             
            }

			if (listCourse.containsKey("courseCode")){
				listCourse.remove("courseCode");
			}
			
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listCourse;
    }
	

	//Methods
	/**
	 * Edits the indexFlatFile.csv flat file to change a desired item.
	 * <p>
     * Writes to a temporary file that is subsequently renamed to replace the original file.
     * </p>
     * 
	 * @param newValue			the new value that the field is being changed to
     * @param option            the field being changed
	 * 
	 * @return					<code>true</code> if operation was successful
     */

    ///this editor is to edit the information in the existing index.csv file 
    private boolean editField(String newValue, String option) {
		File tempFile = new File("flatFiles/temp.csv");
		File oldFile = new File (indexPath);
		String line = "";
		String csvSplitBy = ",";
        
        try {
        	br = new BufferedReader(new FileReader(indexPath));
            FileWriter fw = new FileWriter(tempFile);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
			
			
            while ((line = br.readLine()) != null) {
				String[] row = line.split(csvSplitBy);
				if(row[0].equals(this.course)) {
					if(option.equals("modifyCourseCode")) {
						//The 1st element in the row is 'course code', replace it with the new value for course code
						row[0] = newValue;
						// System.out.println("Course code value modified successfully.");
					}else if(option.equals("modifyCourseVacancy")) {
						//The 2nd element in the row is 'course vacancy', replace it with the new value for course vacancy
						row[1] = newValue;
						// System.out.println("Course vacancy value modified successfully.");
					}else if(option.equals("modifySchool")) {
						//The 9th element in the row is 'school', replace it with the new value for school
						row[8] = newValue;
						// System.out.println("School value modified successfully.");
					}
					else if(option.equals("modifyAU")) {
						//The 8th element in the row is 'academic units', replace it with the new value for academic units
						row[7] = newValue;
						// System.out.println("Academic units value modified successfully.");
					}
				} 
				if (row[0].equals(this.course) && row[2].equals(this.index)) {
					//modify the row value according to the option
					switch (option) {
						case "modifyIndex":{
							//The 3rd element in the row is 'index', replace it with the new value for index
							row[2] = newValue;
							//System.out.println("Index value modified successfully.");
							break;
						}
				
						case "modifyIndexVacancy":{
							//The 4th element in the row is 'index vacancy', replace it with the new value for index vacancy 
							row[3] = newValue;		
							//System.out.println("Index vacancy value modified successfully.");
							break;
						}
												
						case "modifyEnrolled":{
							//The 5th element in the row is 'enrolled students' for the current index
							row[4] = newValue;
							//System.out.println("Student enrollment modified successfully.");
							break;
						}
				
						case "modifyWaitlist": {
							//The 6th element in the row is 'waitlist' for the current index
							row[5] = newValue;
							//System.out.println("Student waitlist status modified successfully.");
							break;
						}
				
						case "addNewSessionToIndex":{
							//The 7th element in the row is the sessions to the current index 
							row[6] = newValue;
							//System.out.println("Session added");
							break;
						}
						
						// not used
						case "removeSessionFromIndex":{
							//The 7th element in the row is the sessions to the current index 
							row[6] = newValue;
							//System.out.println("Session removed"); 
							break;
						}
					}
				}
				String myContent = String.join(",", row);
				myContent += "\n";
				pw.write(myContent);  
			}

            pw.flush();
            pw.close();
            br.close();
  
			File dump = new File(indexPath);
			dump.delete();
			tempFile.renameTo(new File(indexPath));

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

	/**
	 * Writes a new course to indexFlatFile.csv.
	 * 
	 * @param course			the unique identifier of the course being added
	 * @param index				an identifier unique within each course being added	
	 * 
	 * @return 					<code>true</code> if the operation was successful
	 */
	public static boolean addCourse(String course, String index) {
		BufferedWriter bw = null;
		
		try {
	        FileWriter pw = new FileWriter(indexPath, true); 
	        pw.append(course+",0,"+index+",0,0,0,0,0,0"); 
	        pw.append("\n");
            // System.out.println("Student has been added successfully");
	        pw.flush();
	        pw.close();
            return true;
    	} catch (IOException e) {
            return false;
    	}
	}

	//setters - both set the current object as well as modify the flat file when called
	/**
     * Edits the Index's course identifier.
     * <p>
     * Writes to indexFlatFile.csv after attributes have been changed.
     * </p>
	 * <p>
	 * Searches flat file to find all entries with matching current course name and updates them to new course name
	 * </p>
     * 
     * @param newCourse         the new identifier for the course
	 * 
     * @return 					<code>true</code> if operation was sucessful
     */
	public boolean setCourse(String newCourse) {
		this.editField(newCourse, "modifyCourseCode");
		this.course = newCourse;
		return true;
	}

	/**
     * Edits the Index's total number of course vacancies.
     * <p>
     * Writes to indexFlatFile.csv after attributes have been changed.
     * </p>
	 * <p>
	 * Searches flat file to find all entries with matching current course name and index and updates them to new value
	 * </p>
     * 
     * @param vacancy         the new total number of vacancies of the course
	 * 
     * @return 					<code>true</code> if operation was sucessful
     */
	public boolean setCourseVacancy(int vacancy) {
		this.courseVacancy = vacancy;
		this.editField(Integer.toString(this.courseVacancy), "modifyCourseVacancy");
		return true;
	}

	/**
     * Edits the Index's index value.
     * <p>
     * Writes to indexFlatFile.csv after attributes have been changed.
     * </p>
	 * <p>
	 * Searches flat file to find all entries with matching current course name and index and updates them to new value
	 * </p>
     * 
     * @param newIndex          the new index value
	 * 
     * @return 					<code>true</code> if operation was sucessful
     */
    public boolean setIndex(String newIndex) {
		this.editField(newIndex, "modifyIndex");
		this.index = newIndex;
		return true;
	}

	/**
     * Edits the Index's total number of index vacancies.
     * <p>
     * Writes to indexFlatFile.csv after attributes have been changed.
     * </p>
	 * <p>
	 * Searches flat file to find all entries with matching current course name and index and updates them to new value
	 * </p>
     * 
     * @param vacancy         	the new total number of vacancies of the index
	 * 
     * @return 					<code>true</code> if operation was sucessful
     */
	public boolean setIndexVacancy(int vacancy) {
		this.indexVacancy = vacancy;
		this.editField(Integer.toString(this.indexVacancy), "modifyIndexVacancy");
		return true;
	}

	/**
     * Edits the Index's currently enrolled list of students
     * <p>
     * Writes to indexFlatFile.csv after attributes have been changed.
     * </p>
	 * <p>
	 * Searches flat file to find all entries with matching current course name and index and updates them to new value
	 * </p>
     * 
     * @param newStudentArr    	the new array of enrolled students
	 * @param newStudentList	the new list of enrolled students in ";" delineated format
	 * 
     * @return 					<code>true</code> if operation was sucessful
     */
	public boolean setEnrolled(String[] newStudentArr, String newStudentList) {	
		this.enrolled = newStudentArr;
		this.editField(newStudentList, "modifyEnrolled");

		return true;
	}
	/**
     * Edits the Index's currently waitlisted list of students
     * <p>
     * Writes to indexFlatFile.csv after attributes have been changed.
     * </p>
	 * <p>
	 * Searches flat file to find all entries with matching current course name and index and updates them to new value
	 * </p>
     * 
     * @param waitlistArr       the new array of waitlisted students
	 * @param waitlistList		the new list of wailisted students in ";" delineated format
	 * 
     * @return 					<code>true</code> if operation was sucessful
     */
	public boolean setWaitlist(String[] waitlistArr, String waitlistList) {
		// method should only be called from Waitlist Manager
		this.waitlist = waitlistArr;
		this.editField(waitlistList, "modifyWaitlist");
		return true;
	}

	/**
     * Edits the Index's current list of lesson sessions
     * <p>
     * Writes to indexFlatFile.csv after attributes have been changed.
     * </p>
	 * <p>
	 * Searches flat file to find all entries with matching current course name and index and updates them to new value
	 * </p>
     * 
     * @param newSessionArr     the new array of lesson sessions
	 * @param newSessionList	the new list of lesson sessions in ";" delineated format
	 * 
     * @return 					<code>true</code> if operation was sucessful
     */
	public boolean setSessions(String[] newSessionArr, String newSessionList) {
		this.sessions = newSessionArr;
		this.editField(newSessionList, "addNewSessionToIndex"); 
		return true;
    } 
	
	/**
     * Edits the Index's course AU weightage
     * <p>
     * Writes to indexFlatFile.csv after attributes have been changed.
     * </p>
	 * <p>
	 * Searches flat file to find all entries with matching current course name and updates them to new value
	 * </p>
     * 
     * @param acadUnits         the new AU weightage
	 * 
     * @return 					<code>true</code> if operation was sucessful
     */
    public boolean setAcadUnits(int acadUnits) { 
        this.acadUnits = acadUnits; 
        this.editField(Integer.toString(acadUnits), "modifyAU"); 
        return true; 
    }

	/**
	 * Edits the Index's course school
	 * <p>
	 * Writes to indexFlatFile.csv after attributes have been changed.
	 * </p>
	 * <p>
	 * Searches flat file to find all entries with matching current course name and updates them to new value
	 * </p>
     * 
     * @param acadUnits         the new school
	 * 
     * @return 					<code>true</code> if operation was sucessful
	 */
	public boolean setSchool(String school) {
		this.school = school;
		this.editField(school, "modifySchool");
		return true;
	}

	/**
	 * Checks if a course code already exists inside the indexFlatFile
	 * 
	 * @param course 			the course code being checked
	 * 
	 * @return 					<code>true</code> if course code exists, false if it does not exist
	 */
    public static boolean checkCourse(String course) {
    	 BufferedReader br = null;
         String delimiter = ",";
         String[] tempArr;
         try {
             br = new BufferedReader(new FileReader(indexPath));
             String newLine;
             while((newLine = br.readLine()) != null) {
                 tempArr = newLine.split(delimiter);
                 // if no index indicated print all about the course
                 if (tempArr[0].equals(course)){
                     return true;
                 }
             }
         } catch (IOException ioe){
             ioe.printStackTrace();
         } finally {
             try{
 				if(br!=null)
 				br.close();
 			}catch(Exception ex){
 				System.out.println("Error in closing the BufferedReader"+ex);
 			}
         }
    	return false;
	}

	/**
	 * Checks if a course code:index pair already exists inside the indexFlatFile
	 * 
	 * @param course 		the course code being checked
	 * @param index			the index being checked
	 * 
	 * @return 				<code>true</code> if course code:index pair exists, false if it does not exist
	 */
	public static boolean checkIndex(String course, String index) {
		BufferedReader br = null;
		String delimiter = ",";
		String[] tempArr;
		try {
			br = new BufferedReader(new FileReader(indexPath));
			String newLine;
			while((newLine = br.readLine()) != null) {
				tempArr = newLine.split(delimiter);
				// if no index indicated print all about the course
				if (tempArr[0].equals(course) && tempArr[2].equals(index)){
					return true;
				}
			}
		} catch (IOException ioe){
			ioe.printStackTrace();
		} finally {
			try{
				if(br!=null)
				br.close();
			}catch(Exception ex){
				System.out.println("Error in closing the BufferedReader"+ex);
			}
		}
		return false;
   }

    /**
	 * Returns a string of students in ";" delineated format that attend a course
	 * 
	 * @param course				name of course being searched
	 * 
	 * @return						a string of students in ";" delineated format that attend the course
	 */
	//get students by course
	public static String getByCourse(String courseCode)throws IOException{
		
		String delimiter = ",";
		String[] tempArr;
		String students = "";

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(indexPath));
            String newLine;
			// System.out.println("Printing list of students...");
            while((newLine = br.readLine()) != null) {
                tempArr = newLine.split(delimiter);
                // if no index indicated print all about the course
                if (tempArr[0].equals(courseCode)){
                    // student is column 4
                    String[] studentArr = tempArr[4].split(";");
                    for (int i = 0; i < studentArr.length; i++){
						if (students.equals("")) {
							students += studentArr[i];
						}
						else {
							students += ";"+studentArr[i];
						}
                    }
                }
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try{
				if(br!=null)
				br.close();
			}catch(Exception ex){
				// System.out.println("Error in closing the BufferedWriter"+ex);
			}
		}
		return students;
	}

	/**
	 * Returns a string of students in ";" delineated format that attend a course index
	 * 
	 * @param course				name of course being searched
	 * @param index					course index being searched
	 * 
	 * @return						a string of students in ";" delineated format that attend the course
	 */
	//get students by course
	public static String getByIndex(String courseCode, String index)throws IOException{
		
		String delimiter = ",";
		String[] tempArr;
		String students = "";

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(indexPath));
            String newLine;
			// System.out.println("Printing list of students...");
            while((newLine = br.readLine()) != null) {
                tempArr = newLine.split(delimiter);
                // if no index indicated print all about the course
                if (tempArr[0].equals(courseCode) && tempArr[2].equals(index)){
                    // student is column 4
                    String[] studentArr = tempArr[4].split(";");
                    for (int i = 0; i < studentArr.length; i++){
						if (students.equals("")) {
							students += studentArr[i];
						}
						else {
							students += ";"+studentArr[i];
						}
                    }
                }
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try{
				if(br!=null)
				br.close();
			}catch(Exception ex){
				// System.out.println("Error in closing the BufferedWriter"+ex);
			}
		}
		return students;
	}

	
	//display methods
   	/**
	* Displays all the unique course code:index pairs in indexFlatFile.csv in "coursecode: index" format
	* @throws IOException
	*/
	public static void displayIndex() throws IOException {
		
		String delimiter = ",";
        String[] tempArr;

        BufferedReader br = new BufferedReader(new FileReader(indexPath));
        String newLine;
        br.readLine();
        while((newLine = br.readLine()) != null) {
            tempArr = newLine.split(delimiter);
            System.out.print(tempArr[0] + ": " + tempArr[2] + "\n");
            System.out.println();
        }
		br.close();
	}
	
	/**
	 * Displays a list of all students enrolled in a selected course in "Name: , Gender: , Nationality: ," format
	 * 
	 * @param courseCode			the selected course
	 */
	public static void displayByCourse(String courseCode) throws IOException{
		
		String delimiter = ",";
        String[] tempArr;

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(indexPath));
            String newLine;
			System.out.println("Printing list of students...");
            while((newLine = br.readLine()) != null) {
                tempArr = newLine.split(delimiter);
                // if no index indicated print all about the course
                if (tempArr[0].equals(courseCode)){
                    // student is column 4
                    String[] studentArr = tempArr[4].split(";");
                    for (int i = 0; i < studentArr.length; i++){
                        Student stud = Student.createByName(studentArr[i].trim());
                        System.out.println("Name: " + studentArr[i].trim() + ", Gender: " +
                                                stud.getGender() + ", Nationality: "
                                                    + stud.getNationality());
                    }
                }
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try{
				if(br!=null)
				br.close();
			}catch(Exception ex){
				System.out.println("Error in closing the BufferedWriter"+ex);
			}
        }
	}

	/**
	 * Displays a list of all students enrolled in a selected course and index in "Name: , Gender: , Nationality: ," format
	 * 
	 * @param courseCode			the selected course
	 * @param index					the selected index
	 */
	public static void displayByIndex(String courseCode, int index){
		String delimiter = ",";
        String[] tempArr;

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(indexPath));
            String newLine;
            // String headers = br.readLine();
            // headerArr = headers.split(delimiter);
			System.out.println("Printing list of students...");
            while((newLine = br.readLine()) != null) {
                tempArr = newLine.split(delimiter);
                // if no index indicated print all about the course
				// 3rd column is index 
                if (tempArr[0].equals(courseCode) && tempArr[2].equals(Integer.toString(index))){
                    // student is column 4
                    String[] studentArr = tempArr[4].split(";");
                    for (int i = 0; i < studentArr.length; i++){
                        Student stud = Student.createByName(studentArr[i].trim());
                        System.out.println("Name: " + stud.getName().trim() + ", Gender: " +
                                                stud.getGender() + ", Nationality: "
                                                    + stud.getNationality());
                    }
                }
            }
        } catch (IOException ioe){
            ioe.printStackTrace();
        } finally {
            try{
				if(br!=null)
				br.close();
			}catch(Exception ex){
				System.out.println("Error in closing the BufferedWriter"+ex);
			}
        }
	}

	/**
	 * Displays information about a selected course in "Course Code: , Course Vacancy: , Index: , indexVacancy: , Sessions: , Acad Units: , School: " format
	 * 
	 * @param courseCode			the selected course
	 */
	public static void displayCourseInfo(String courseCode) throws IOException{
		
		String delimiter = ",";
        String[] tempArr;

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(indexPath));
            String newLine;
			System.out.println("Printing Course Information");
            while((newLine = br.readLine()) != null) {
                tempArr = newLine.split(delimiter);
                // if no index indicated print all about the course
                if (tempArr[0].equals(courseCode)){
                    // student is column 4
					System.out.println("Course Code: " + tempArr[0] + ", Course Vacancy: " + tempArr[1] 
							+ ", Index: " + tempArr[2] + ", indexVacancy: " + tempArr[3] + 
							", Sessions: " + tempArr[6] + ", Acad Units: " + tempArr[7] + ", School: " + tempArr[8]);
                }
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try{
				if(br!=null)
				br.close();
			}catch(Exception ex){
				System.out.println("Error in closing the BufferedWriter"+ex);
			}
        }
	}
	
	/**
	 * Displays information about a selected course and index in "Course Code: , Course Vacancy: , Index: , indexVacancy: , Sessions: , Acad Units: , School: " format
	 * 
	 * @param courseCode			the selected course
	 * @param index					the selected index
	 */
	public static void displayIndexInfo(String courseCode, int index){
		String delimiter = ",";
        String[] tempArr;

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(indexPath));
            String newLine;
            // String headers = br.readLine();
            // headerArr = headers.split(delimiter);
			System.out.println("Printing Index Information");
            while((newLine = br.readLine()) != null) {
                tempArr = newLine.split(delimiter);
                // if no index indicated print all about the course
				// 3rd column is index 
                if (tempArr[0].equals(courseCode) && tempArr[2].equals(Integer.toString(index))){
                    // student is column 4
					System.out.println("Course Code: " + tempArr[0] + ", Course Vacancy: " + tempArr[1] 
							+ ", Index: " + tempArr[2] + ", indexVacancy: " + tempArr[3] + 
							", Sessions: " + tempArr[6] + ", Acad Units: " + tempArr[7] + ", School: " + tempArr[8]);
                }
            }
        } catch (IOException ioe){
            ioe.printStackTrace();
        } finally {
            try{
				if(br!=null)
				br.close();
			}catch(Exception ex){
				System.out.println("Error in closing the BufferedWriter"+ex);
			}
        }
	}

	/**
	 * Displays information about a selected course and/or index in "courseCode/index : vacancies" format
	 * 
	 * @param chosenCourse			the selected course
	 * @param chosenIndex			the selected index
	 * @param choice				selected option, 2 will display for ALL indexes that a course has, 1 will only display for selected index
	 */
	public static String displayVacancy(String chosenCourse, String chosenIndex, int choice) throws IOException { // choice 0: course vacancy, choice 1: index vacancy
		String delimiter = ",";
        String[] tempArr;
        // String[] headerArr;

        BufferedReader br = new BufferedReader(new FileReader(indexPath));
		String newLine;
		// String headers = br.readLine();
        // headerArr = headers.split(delimiter);
        
        if (choice == 2) { // display course AND all index vacancies
            int totalCourseVacancy = 0;
            while((newLine = br.readLine()) != null) {
                tempArr = newLine.split(delimiter);
                if (tempArr[0].equals(chosenCourse)) {
                    System.out.println(chosenCourse + "/" + tempArr[2] + ": " + tempArr[3] + " vacancies");
                    totalCourseVacancy += Integer.parseInt(tempArr[3]);
                }
			}
			br.close();
            return Integer.toString(totalCourseVacancy);
        } else {
            while((newLine = br.readLine()) != null) {
                tempArr = newLine.split(delimiter);
                if (tempArr[0].equals(chosenCourse) && tempArr[2].equals(chosenIndex)) {
                    if (choice == 0) { // only course vacancy
						System.out.print("Number of vacancies for " + tempArr[0] + ": " + tempArr[1] + "\n"); 
						br.close();
						return tempArr[1];
					}
                    else if (choice == 1) { // index vacancy
						System.out.print("Number of vacancies for " + tempArr[0] + "/" + tempArr[2] + ": " + tempArr[3] + "\n"); 
						br.close();
						return tempArr[3];
					}
                }
                System.out.println();
            }
		}
		br.close();
		return null;
    }
}

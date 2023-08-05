    package classes.entities;

import java.io.*;

/**
 * Lesson is a concrete class for interacting with lesson sessions.
 * <p>
 * Lesson contains methods to read, write, and edit lessonFlatFile.csv flat file.
 * </p>
 * <p>
 * A Lesson object encapsulates the information needed for AdminManager operations. This state information includes:
 * </p>
 * <ul>
 * <li>Unique identifier for lesson
 * <li>Location of the lesson
 * <li>Day of the week that lesson is held in three-letter format
 * <li>Starting time of the lesson
 * <li>Ending time of the lesson
 * </ul>
 * 
 */

public class Lesson {
    //attributes
    /**
     * Unique identifier for each lesson
     */
    private int id;

    /**
     * Lesson location
     */
    private String location;

    /**
     * The day that the lesson is being conducted
     */
    private String day;

    /**
     * The starting time of the lesson
     */
    private String startTime;

    /**
     * The ending time of the lesson
     */
    private String endTime;

    /**
     * The class type of the lesson
     */
    private String classType;

    /**
     * The address of the lesson flat file
     */
    private static String lessonFile = "flatFiles/lessonFlatFile.csv";

    /**
     * A reader object for reading the lesson flat file
     */
    private static BufferedReader br = null;

    /**
     * A string object to store each line of the lesson flat file
     */
    private static String line = "";

    //use comma as separator
    private static String cvsSplitBy = ",";

    /**
     * Class Constructor specifying unique identifier of the lesson.
     * <p>
     * Lesson object is created by reading lessonFlatFile.csv to find a lesson corresponding to the input id.
     * </p>
     */
    public Lesson(int id) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(lessonFile));
            while ((line = br.readLine()) != null) {
                String[] row = line.split(cvsSplitBy);
                if (row[0].equals(Integer.toString(id))) {
                    // instantiate attributes
                    this.id = id;
                    this.location = row[1];
                    this.day = row[2];
                    this.startTime = row[3];
                    this.endTime = row[4];
                    this.classType = row[5];
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // getters
    /**
     * Returns Lesson's location.
     * 
     * @return this lesson's location
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * Returns Lesson's day of week of conduct.
     * 
     * @return this lesson's day of conduct
     */
    public String getDay() {
        return this.day;
    }

    /**
     * Returns Lesson's starting time. 
     * 
     * @return this lesson's starting time
     */
    public String getStartTime() {
        return this.startTime;
    }

    /**
     * Returns Lesson's ending time.
     * 
     * @return this lesson's ending time
     */
    public String getEndTime() {
        return this.endTime;
    }


    /**
     * Returns Lesson's class type.
     * 
     * @return this lesson's class type
     */
    public String getClassType() {
        return this.classType;
    }


    // setters
    /**
     * Writes directly to lessonFlatFile.csv to write a new lesson.
     * <p>
     * Provides the new lesson a unique id.
     * </p>
     * 
     * @param location          desired location for the lesson
     * @param newDay            day of the week that lesson is held
     * @param newStartTime      starting time of the lesson
     * @param newEndTime        ending time fo the lesson
     * @param classType         type of the session
     * 
     */
    public static int addLesson(String location, String newDay, String newStartTime, String newEndTime, String classType){
        int count = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(lessonFile)); //file containing passwords
            String line = "";
            //counts how many rows there are and stores as count
            while ((line = br.readLine()) != null) {
                count++;
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // timing clash
        if (checkTime(count + 1, location, newDay, newStartTime, newEndTime) == false) {
            return -1;
        }
        
    	try {
	        FileWriter pw = new FileWriter(lessonFile, true); 
	        pw.append(count+1 + "," + location + "," + newDay + "," + newStartTime + "," + newEndTime + "," + classType);
	        pw.append("\n");
	        pw.flush();
	        pw.close();
            return count+1;
    	} catch (IOException e) {
            return -1;
    	}
    }

    /**
     * Changes the location of the Lesson and edits the lessonFlatFile.csv accordingly.
     * 
     * @param newLocation           the location that the lesson is being changed to
     */
    // not used at the moment, but allows to change location
    public void setLocation(String newLocation) {
        this.location = newLocation;
        this.editField("setLocation");
    }

    /**
     * Determins if there is an overlap in lessons.
     * 
     * @param id                the id of the lesson being checked
     * @param location          the location of the lesson being checked
     * @param newDay            the day of the lesson being check is being held
     * @param newStartTime      the starting time of the lesson being checked
     * @param newEndTime        the ending time of the lesson being checked
     * 
     * @return true if there is no overlap, false if there is overlap
     */
    private static boolean checkTime(int id, String location, String newDay, String newStartTime, String newEndTime) {
        // read file to see if any lesson uses the same location on the same day
        // if same day, check if the lessons overlaps
        // return error code if timing overlaps 
        
        try {
            BufferedReader br = new BufferedReader(new FileReader(lessonFile));
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] row = line.split(cvsSplitBy);
                String existingLocation;
                String existingDay;
                String existingStartTime;
                String existingEndTime;
                
                try {
	                existingLocation = row[1];
	                existingDay = row[2];
	                existingStartTime = row[3];
	                existingEndTime = row[4];
                } catch (ArrayIndexOutOfBoundsException exception) {
                	continue;
                }

                int newStart;
                int newEnd;
                int oldStart;
                int oldEnd;
                
                // check start and end time to see if any overlaps occur if same location and same day
                if (existingLocation.equals(location)){
                    if (existingDay.equals(newDay)){
                        // new end time is after old start time
                        // new start time is after old start time and before old end time 
                        try {
                            newStart = Integer.parseInt(newStartTime);
                            newEnd = Integer.parseInt(newEndTime);
                            oldStart = Integer.parseInt(existingStartTime);
                            oldEnd = Integer.parseInt(existingEndTime);
                        } catch (NumberFormatException e) {
                        	// System.out.println("Exception");
                            return false;
                        }
                        
                        // this lesson was the one occupying it 
                        if (row[0].equals(Integer.toString(id))) {
                        	continue;
                        }
                        
                        //comparisons - not sure exactly how they work ~dk
                        if (newEnd < newStart) {
                        	return false;
                        }
                      
                        if (newStart < oldStart && newEnd > oldStart){
                            return false;
                        }

                        else if (newEnd > oldStart && newStart < oldEnd){
                            return false;
                        }
                        
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    /**
     * Edits the Lesson's day of conduct, start time, and end time.
     * <p>
     * Writes to lessonFlatFile.csv after attributes have been changed.
     * </p>
     * <p>
     * Only performs the edit if the new timning do not cause overlaps with other exisiting lessons.
     * </p>
     * 
     * @param newDay            the day that the lesson is being changed to
     * @param newStartTime      the new starting time of the lesson
     * @param newEndTime        the new ending time of the lesson
     * 
     */
    public void setTime(String newDay, String newStartTime, String newEndTime) {
    	if (checkTime(this.id, this.location, newDay, newStartTime, newEndTime) == true) {
            this.day = newDay;
            this.startTime = newStartTime;
            this.endTime = newEndTime;
    		this.editField("setTime");
    	}
    	else {
    		System.out.println("Location is occupied at this time. Modification failed.");
    	}
    }

    /**
     * Edits the Lesson's location, day of conduct, start time, and end time.
     * <p>
     * Writes to lessonFlatFile.csv after attributes have been changed.
     * </p>
     * <p>
     * Only performs the edit if the new location and timning do not cause overlaps with other exisiting lessons.
     * </p>
     * 
     * @param location          the new location that the lesson is being changed to
     * @param newDay            the day that the lesson is being changed to
     * @param newStartTime      the new starting time of the lesson
     * @param newEndTime        the new ending time of the lesson
     * 
     */
    public void setLocationAndTime(String Location, String newDay, String newStartTime, String newEndTime){
    	this.location = Location;
        if (checkTime(this.id, Location, newDay, newStartTime, newEndTime) == true) {
            this.location = Location;
            this.day = newDay;
            this.startTime = newStartTime;
            this.endTime = newEndTime;
        	this.editField("setLocationAndTime");
        }
        else {
    		System.out.println("Location is occupied at this time. Modification failed.");
    	}
    }

    /**
     * Edits the lessonFlatFile.csv flat file to change a desired item. 
     * <p>
     * Writes to a temporary file that is subsequently renamed to replace the original file.
     * </p>
     * 
     * @param option            the field being changed
     */

    ///this editor is to edit the information in the existing index.csv file 
    private void editField(String option) {
    	//initialization
    	File tempFile = new File("tempLesson.csv");
        File lessonF = new File (lessonFile);
        String line = "";
		String csvSplitBy = ",";
        
        // id, location, day, startTime, endTime

        try {
        	br = new BufferedReader(new FileReader(lessonFile));
            FileWriter fw = new FileWriter(tempFile);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);   
             
            while ((line = br.readLine()) != null) {       	
                String[] row = line.split(csvSplitBy);   
                if (row[0].equals(Integer.toString(this.id))) {
                	 //modify the row value according to the option
                	switch (option) {
	                	case "setLocation":{
	                		// replace location with new location
	                		row[1] = this.location;	
	                		System.out.println("Location modified successfully.");
	                		break;
	                	}
	                	 	                	 
	                	case "setTime":{
	                		// replace day, startTime and endTime
	                		row[2] = this.day;
                            row[3] = this.startTime;
                            row[4] = this.endTime;
	                		System.out.println ("Time modified successfully.");
	                		break;
	                	}
	                	 
	                	case "setLocationAndTime":{
	                		// replace location, day, startTime, endTime
                            row[1] = this.location;	
	                		row[2] = this.day;
                            row[3] = this.startTime;
                            row[4] = this.endTime;
	                		System.out.println ("Location and Time modified successfully.");
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
  
            File dump = new File(lessonFile);
            tempFile.renameTo(dump);
            tempFile.delete();

        } catch (IOException e) {
        	e.printStackTrace();
     	} finally {
        	if (br != null) {
            	try {
                	br.close();
            	} catch (IOException e) {
                	e.printStackTrace();
             	}
         	}
		}
	}
}

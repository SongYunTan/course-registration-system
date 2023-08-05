package classes.controllers;

import java.io.*;
import java.util.*;
import java.lang.reflect.*;

//IMPORTANT: distinguish between different course waitlists


/**
 * NotificationPublisher is a concrete file that manages the notificationList.csv flat file
 * <p>
 * Contains methods to subscribe, unsubscribe, and notify students.
 */

public class NotificationPublisher {
	
	/**
	 * Location of notificationList.csv flat file
	 */
	public static String notificationFile = "flatFiles/notificationFlatFile.csv";

	/**
	 * Adds a student to notificationList.csv flat file
	 * <p>
	 * Should be called when a student is added to waitlist
	 * </p>
	 * 
	 * @param username				username of the student
	 * @param notificationType		reason for the notification e.g. waitlist
	 * @param course				the course that the student is being waitlisted for
	 * @param index					the course index that the student is being waitlisted for
	 */
	// when student is added to waitlist, call Notification Publisher to add student to receive notifications
    public static void subscribe(String username, String notificationType, String course, String index){
        // add students by username and notif type (interested to get notification about waitlist)
        // e.g. chew0403,waitlist,email
        
    	try {
	        FileWriter pw = new FileWriter(notificationFile, true); 
	        pw.append(username);
	        pw.append(",");
	        pw.append(notificationType);
			pw.append(",");
			pw.append(course);
			pw.append(",");
			pw.append(index);
			pw.append(",");
	        // hard coded as email for now since it is the only type available
	        pw.append("email");
	        pw.append("\n");
	        pw.flush();
	        pw.close();
    	} catch (IOException e) {
    		System.out.println("Failed to add student to notification list.");
    	}
    	
    }

	/**
	 * Removes a student from notificationList.csv flat file
	 * <p>
	 * Should be called after a student is removed from waitlist
	 * </p>
	 * 
	 * @param username 				username of the student
	 * @param notificationType		reason for the notification e.g. waitlist
	 * @param course				the course whose waitlist the student is being removed from
	 * @param index					the course index whose waitlist the student is being removed from	
	 */
    // when student is removed from waitlist, call notification publisher to add student to remove student 
    public static void unsubscribe(String username, String notificationType, String course, String index){
        // get unsubscribed from email notification
        // e.g. not on waitlist anymore
        
    	File inputFile = new File(notificationFile);
    	File tempFile = new File("tempNotif.csv");
    	String currentLine;
    	String curUsername;
		String curType;
		String curCourse;
		String curIndex;

    	try {
    		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        	BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
        	while((currentLine = reader.readLine()) != null) {
        	    // trim newline when comparing with lineToRemove
        	    curUsername = currentLine.trim().split(",")[0];
				curType = currentLine.trim().split(",")[1];
				curCourse = currentLine.trim().split(",")[2];
				curIndex = currentLine.trim().split(",")[3];
				if(curUsername.equals(username) && curType.equals(notificationType) 
					&& curCourse.equals(course) && curIndex.equals(index)) continue;
        	    writer.write(currentLine + System.getProperty("line.separator"));
        	}
        	writer.close(); 
        	reader.close(); 
    	} catch (FileNotFoundException e) {
    		System.out.println("Unsubscribe failed!");
    	} catch (IOException f) {
    		System.out.println("Unsubscribe failed!");
    	}
    
    	boolean successful = tempFile.renameTo(inputFile);
    }
	
	/**
	 * Notifies a defined subscriber
	 * <p>
	 * Dependant on STARSSubscriber interface and emailSubscriber class
	 * </p>
	 * 
	 * @param username 				unique name of the student subscriber
	 * @param notificationType		reason for the notification e.g. waitlist
	 * @param message				the content of the message being sent
	 */
    // to be overriden by subclass
    // sends message for each subscriber
    public static void notify(String username, String notificationType, String message) {
    	File inputFile = new File(notificationFile);
    	String currentLine;
    	String curUsername;
    	String notifType;
    	try {
    		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
    		while((currentLine = reader.readLine()) != null) {
    			curUsername = currentLine.trim().split(",")[0];
				notifType = currentLine.trim().split(",")[1];
				
				//check to see if username and notification type match
    			if (curUsername.equals(username) && notifType.equals(notificationType)) {
    				STARSSubscriber st = new emailSubscriber(username);
    				st.sendMessage(notificationType, message);
    			}
    		}
    		reader.close(); 
    	} catch (IOException e) {
    		System.out.println("Failed to notify subscriber!");
    	}
    }


}

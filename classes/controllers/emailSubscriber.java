package classes.controllers;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * emailSubscriber is a concrete class implementing STARSSubscriber according to a strategy pattern.
 * <p>
 * Sends an email to a student notifying them of a change in their waitlist status
 * </p>
 */
public class emailSubscriber implements STARSSubscriber{
	/**
	 * Unique username of the student
	 */
	private String username;
	
	/**
	 * Class Constructor specifying the username of a student
	 */
	public emailSubscriber(String username) {
		this.username = username;
	}


	/**
	 * Sends an email to a student specified by username attribute
	 * 
	 * @param notifType				reason for the notification e.g. waitlist
	 * @param bodyMessage			content of the notification
	 */
    // not static, have to create email subscriber methods
    public void sendMessage(String notifType, String bodyMessage){

        // username and password of admin
        final String username = "starswaitlistnotifer"; // to be added
		final String password = "userpassword";

        Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  }
		  );

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username + "@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(this.username + "@e.ntu.edu.sg")); // to be added an email addr
			message.setSubject("NTU STARS: " + notifType);
			message.setText(bodyMessage);

			Transport.send(message);

			// System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
    }
}

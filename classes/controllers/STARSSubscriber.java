package classes.controllers;

/**
 * STARSSubscriber is an interface class that can be implemented by various types of notification subscriber types.
 */

public interface STARSSubscriber {
    /**
     * Notifies user according to the notification type desired
     * 
     * @param notifType         the reason for the notification e.g. waitlist
     * @param bodyMessage       the content of the message being sent
     */
	public default void sendMessage(String notifType, String bodyMessage) {}
}

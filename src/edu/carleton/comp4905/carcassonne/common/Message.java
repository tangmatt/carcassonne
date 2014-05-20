package edu.carleton.comp4905.carcassonne.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
	private static String DATE_FORMAT = "yyyy-MM-dd";
	private static String TIME_FORMAT = "HH:mm:ss.SSS";
	
	private final String date;
	private final String time;
	private final MessageType type;
	private final String message;
	
	public Message(final MessageType type, final String message) {
		Date date = new Date();
		this.date = new SimpleDateFormat(DATE_FORMAT).format(date);
		this.time = new SimpleDateFormat(TIME_FORMAT).format(date);
		this.type = type;
		this.message = message;
	}
	
	/**
	 * Returns the date when the message was logged.
	 * @return a String
	 */
	public String getDate() {
		return date;
	}
	
	/**
	 * Returns the time when the message was logged.
	 * @return a String
	 */
	public String getTime() {
		return time;
	}
	
	/**
	 * Returns the message type.
	 * @return a MessageType
	 */
	public MessageType getType() {
		return type;
	}
	
	/**
	 * Returns the body of the message.
	 * @return a String
	 */
	public String getMessage() {
		return message;
	}
}

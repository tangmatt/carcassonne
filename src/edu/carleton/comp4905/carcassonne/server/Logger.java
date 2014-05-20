package edu.carleton.comp4905.carcassonne.server;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	private static final String SEPARATOR = ": ";
	
	public static void print(final String message) {
		String timeStamp = new SimpleDateFormat(TIMESTAMP_FORMAT).format(new Date());
		System.out.println(timeStamp + SEPARATOR + message);
	}
}

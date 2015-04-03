package edu.carleton.comp4905.carcassonne.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	private static final String SEPARATOR = ": ";
	
	private Logger() {
		// prevent instantiation
	}
	
	public synchronized static void log(final String message) {
		String prefix = new SimpleDateFormat(DATE_FORMAT).format(new Date());
		System.out.println(prefix + SEPARATOR + message);
	}
}

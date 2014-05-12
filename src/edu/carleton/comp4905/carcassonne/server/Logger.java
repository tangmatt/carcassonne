package edu.carleton.comp4905.carcassonne.server;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	private static String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	private static String SEPARATOR = ": ";
	
	public void print(String message) {
		String timeStamp = new SimpleDateFormat(TIMESTAMP_FORMAT).format(new Date());
		System.out.println(timeStamp + SEPARATOR + message);
	}
}

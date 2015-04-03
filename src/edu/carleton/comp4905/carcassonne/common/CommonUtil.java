package edu.carleton.comp4905.carcassonne.common;

public class CommonUtil {
	protected static int ID = 5000;
	
	private CommonUtil() {
		// prevent instantiation of object
	}
	
	/**
	 * This method converts a passed in string to integer.
	 * @param number the number to convert
	 * @return a String
	 */
	public static String convertIntegerToString(final int number) {
		String num = null;
		try {
			num = Integer.toString(number);
		} catch(Exception e) {
			// do nothing
			e.printStackTrace();
		}
		return num;
	}
	
	/**
	 * This method returns true if the string value is null or empty string.
	 * @param value the string value
	 * @return a Boolean
	 */
	public static boolean isNullOrEmpty(final String value) {
		return value == null || value.isEmpty();
	}
	
	/**
	 * Returns a unique identifier
	 * @return an Integer
	 */
	public static Integer getUniqueId() {
		return ++ID;
	}
}

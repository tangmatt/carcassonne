package edu.carleton.comp4905.carcassonne.common;

public class CommonUtil {
	private CommonUtil() {
		// prevent instantiation of object
	}
	
	/**
	 * This method converts a passed in string to integer.
	 * @param number the number to convert
	 * @return
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
}

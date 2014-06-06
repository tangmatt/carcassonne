package edu.carleton.comp4905.carcassonne.common;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalMessages {
	private LocalMessages() {
		// prevent instantiation
	}
	
	private static final String BUNDLE_NAME = "edu.carleton.comp4905.carcassonne.common.localmessages";
	private static ResourceBundle RES_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	
	public static void setLocale(final Locale locale) {
		RES_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, locale);
	}
	
	public static String getString(String key) {
		return RES_BUNDLE.getString(key);
	}
	
	public static Locale getLocale() {
		return RES_BUNDLE.getLocale();
	}
}

package edu.carleton.comp4905.carcassonne.common;

import javafx.application.Platform;

public class PlatformManager {
	private PlatformManager() {
		// prevent instantiation of object
	}
	
	/**
	 * Makes sure to run JavaFX operations in the JavaFX Application thread.
	 * @param runnable
	 */
	public static void run(final Runnable runnable) {
		if(runnable == null)
			throw new IllegalArgumentException("Runnable is null");
		
		if(Platform.isFxApplicationThread())
			runnable.run();
		else
			runLater(runnable);
	}
	
	public static void runLater(final Runnable runnable) {
		Platform.runLater(runnable);
	}
}

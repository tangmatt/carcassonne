package edu.carleton.comp4905.carcassonne.client;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public abstract class InputController {
	@FXML protected Label messagePrompt;
	@FXML protected TextField usernameField, servAddrField, servPortField;
	
	/**
	 * Displays a message onto the Scene.
	 * @param message a message (String)
	 */
	protected void showMessage(final String message) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				messagePrompt.setText(message);
				FadeTransition fadeTransition = new FadeTransition(Duration.millis(2500), messagePrompt);
				fadeTransition.setFromValue(1.0);
				fadeTransition.setToValue(0.0);
				fadeTransition.play();
			}
		});
	}
	
	/**
	 * Checks whether the user name text field is empty.
	 * @return a boolean
	 */
	protected boolean isNameFieldEmpty() {
		// Check if the username field has any input
		if(usernameField.getText().isEmpty()) {
			// Show the missing field message
			messagePrompt.setText("Enter your username.");
			FadeTransition fadeTransition = new FadeTransition(Duration.millis(2500), messagePrompt);
			fadeTransition.setFromValue(1.0);
			fadeTransition.setToValue(0.0);
			fadeTransition.play();
			return true;
		}
		return false;
	}
	
	/**
	 * Checks whether the server address text field is empty.
	 * @return a boolean
	 */
	protected boolean isAddrFieldEmpty() {
		// Check if the server address field has any input
		if(servAddrField.getText().isEmpty()) {
			// Show the missing field message
			messagePrompt.setText("Enter the server address.");
			FadeTransition fadeTransition = new FadeTransition(Duration.millis(2500), messagePrompt);
			fadeTransition.setFromValue(1.0);
			fadeTransition.setToValue(0.0);
			fadeTransition.play();
			return true;
		}
		return false;
	}
	
	/**
	 * Checks whether the server port field is empty and its correctness.
	 * @return a boolean
	 */
	protected boolean isPortFieldValid() {
		// Check if the server port field has any input
		if(servPortField.getText().isEmpty()) {
			showMessage("Enter the server port.");
			return false;
		}
		
		try {
			// Detect if server port is an integer
			Integer.parseInt(servPortField.getText());
		} catch(NumberFormatException e) {
			showMessage("Invalid server port.");
			return false;
		}
		
		return true;
	}
}

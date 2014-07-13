package edu.carleton.comp4905.carcassonne.client;

import edu.carleton.comp4905.carcassonne.common.LocalMessages;
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
	protected boolean isNameFieldValid() {
		// Check if the username field has any input
		if(usernameField.getText().isEmpty()) {
			// Show the missing field message
			showMessage(LocalMessages.getString("EnterUsername"));
			return false;
		} else if(usernameField.getText().length() > 9) {
			// Username is too long
			showMessage(LocalMessages.getString("LongUsername"));
			return false;
		}
		return true;
	}
	
	/**
	 * Checks whether the server address text field is empty.
	 * @return a boolean
	 */
	protected boolean isAddrFieldEmpty() {
		// Check if the server address field has any input
		if(servAddrField.getText().isEmpty()) {
			// Show the missing field message
			showMessage(LocalMessages.getString("EnterServerAddr"));
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
			showMessage(LocalMessages.getString("EnterServerPort"));
			return false;
		}
		
		try {
			// Detect if server port is an integer
			Integer.parseInt(servPortField.getText());
		} catch(NumberFormatException e) {
			showMessage(LocalMessages.getString("InvalidServerPort"));
			return false;
		}
		
		return true;
	}
}

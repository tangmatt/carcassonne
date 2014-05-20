package edu.carleton.comp4905.carcassonne.client;

import java.net.URL;
import java.util.ResourceBundle;

import edu.carleton.comp4905.carcassonne.common.Address;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class JoinGameController extends InputController implements Initializable, ControlledScreen {
	@FXML private Button hostButton;
	private ScreensController screensController;
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		// do nothing
	}
	
	@FXML
	/**
	 * Handles the submit button when the user chooses to join a game.
	 * @param event an ActionEvent
	 */
	private void handleSubmit(final ActionEvent event) {
		if(isNameFieldEmpty() || isAddrFieldEmpty() || !isPortFieldValid())
			return;
		try {
			Address address = new Address(servAddrField.getText(), Integer.parseInt(servPortField.getText()));
			new GameClient(usernameField.getText(), address).start(new javafx.stage.Stage());
		} catch (Exception e) {
			e.printStackTrace();
			showMessage("Could not connect to server.");
		}
	}
	
	@FXML
	/**
	 * Handles event when Host Game button is pressed from the tool bar.
	 * @param event the event
	 */
	private void handleHostGame(final ActionEvent event) {
		screensController.setScreen(Carcassonne.HOST_SCREEN);
	}

	@Override
	public void setScreenParent(final ScreensController screen) {
		screensController = screen;
	}
}

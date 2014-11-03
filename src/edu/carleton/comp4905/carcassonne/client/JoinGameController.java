package edu.carleton.comp4905.carcassonne.client;

import java.net.URL;
import java.util.ResourceBundle;

import edu.carleton.comp4905.carcassonne.common.Address;
import edu.carleton.comp4905.carcassonne.common.LocalMessages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class JoinGameController extends InputController implements Initializable, ControlledScreen {
	@FXML private Label title, sceneDesc, usernameLabel, servAddrLabel, servPortLabel; 
	@FXML private Button joinButton, hostButton, submitButton;
	private ScreensController screensController;
	private Stage stage;
	
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
		if(!isNameFieldValid() || isAddrFieldEmpty() || !isPortFieldValid())
			return;
		try {
			Address address = new Address(servAddrField.getText(), Integer.parseInt(servPortField.getText()));
			stage.close();
			new GameClient(usernameField.getText(), address).start(new javafx.stage.Stage());
		} catch (Exception e) {
			showMessage(LocalMessages.getString("CouldNotConnect"));
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
	
	/**
	 * Initializes data for this controller.
	 * @param stage a Stage
	 */
	public void initData(final Stage stage) {
		this.stage = stage;
	}

	@Override
	public void setScreenParent(final ScreensController screen) {
		screensController = screen;
	}
}

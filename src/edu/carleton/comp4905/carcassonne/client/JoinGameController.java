package edu.carleton.comp4905.carcassonne.client;

import java.net.URL;
import java.util.ResourceBundle;

import edu.carleton.comp4905.carcassonne.common.Address;
import edu.carleton.comp4905.carcassonne.common.LocalMessages;
import edu.carleton.comp4905.carcassonne.common.PlatformManager;
import edu.carleton.comp4905.carcassonne.common.ResourceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class JoinGameController extends InputController implements Initializable, ControlledScreen {
	@FXML private Label title, sceneDesc, usernameLabel, servAddrLabel, servPortLabel; 
	@FXML private Button joinButton, hostButton, submitButton;
	@FXML private ImageView imageLogo;
	private ScreensController screensController;
	private Stage stage;
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				imageLogo.setImage(ResourceManager.getImageFromResources("carcassonne_logo.png"));
			}
		});
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

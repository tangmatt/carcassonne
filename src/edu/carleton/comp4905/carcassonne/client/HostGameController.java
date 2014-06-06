package edu.carleton.comp4905.carcassonne.client;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import edu.carleton.comp4905.carcassonne.common.LocalMessages;
import edu.carleton.comp4905.carcassonne.server.ServerClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class HostGameController extends InputController implements Initializable, ControlledScreen {
	@FXML private BorderPane borderPane;
	@FXML private Label title, sceneDesc, usernameLabel, servAddrLabel, servPortLabel;
	@FXML private Button joinButton, hostButton, submitButton;
	private ScreensController screensController;
	private Carcassonne client;
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		joinButton.setText(LocalMessages.getString("Join"));
		hostButton.setText(LocalMessages.getString("Host"));
		submitButton.setText(LocalMessages.getString("HostButton"));
		title.setText(LocalMessages.getString("GameTitle"));
		sceneDesc.setText(LocalMessages.getString("HostFormDesc"));
		usernameLabel.setText(LocalMessages.getString("Username"));
		servAddrLabel.setText(LocalMessages.getString("ServerAddr"));
		servPortLabel.setText(LocalMessages.getString("ServerPort"));
		
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			servAddrField.setText(inetAddress.getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	/**
	 * Handles the submit button when the user chooses to host a game.
	 * @param event an ActionEvent
	 */
	private synchronized void handleSubmit(final ActionEvent event) {
		if(!isPortFieldValid())
			return;
		try {
			new ServerClient(Integer.parseInt(servPortField.getText())).start(new javafx.stage.Stage());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	/**
	 * Handles event when Join Game button is pressed from the tool bar.
	 * @param event an ActionEvent
	 */
	private synchronized void handleJoinGame(final ActionEvent event) {
		screensController.setScreen(Carcassonne.JOIN_SCREEN);
	}
	
	/**
	 * Initializes data for this controller.
	 * @param client a Carcassonne
	 */
	public synchronized void initData(final Carcassonne client) {
		this.client = client;
	}

	@Override
	public synchronized void setScreenParent(final ScreensController screen) {
		screensController = screen;
	}
}

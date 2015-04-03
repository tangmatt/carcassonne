package edu.carleton.comp4905.carcassonne.client;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import edu.carleton.comp4905.carcassonne.common.LocalMessages;
import edu.carleton.comp4905.carcassonne.common.Mode;
import edu.carleton.comp4905.carcassonne.common.PlatformManager;
import edu.carleton.comp4905.carcassonne.common.ResourceManager;
import edu.carleton.comp4905.carcassonne.server.ServerClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class HostGameController extends InputController implements Initializable, ControlledScreen {
	@FXML private BorderPane borderPane;
	@FXML private Label title, sceneDesc, usernameLabel, servAddrLabel, servPortLabel;
	@FXML private ToggleGroup toggleGroup;
	@FXML private RadioButton syncButton, asyncButton;
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
				syncButton.setTooltip(new Tooltip(LocalMessages.getString("SyncTip")));
				asyncButton.setTooltip(new Tooltip(LocalMessages.getString("AsyncTip")));
			}
		});
		
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
			stage.close();
			new ServerClient(Integer.parseInt(servPortField.getText()),
					Mode.valueOf(((RadioButton)toggleGroup.getSelectedToggle()).getText()))
			.start(new javafx.stage.Stage());
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
	 * @param stage a Stage
	 */
	public void initData(final Stage stage) {
		this.stage = stage;
	}

	@Override
	public synchronized void setScreenParent(final ScreensController screen) {
		screensController = screen;
	}
}

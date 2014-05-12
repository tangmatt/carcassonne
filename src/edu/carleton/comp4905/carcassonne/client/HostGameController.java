package edu.carleton.comp4905.carcassonne.client;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import edu.carleton.comp4905.carcassonne.server.ServerClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class HostGameController extends InputController implements Initializable, ControlledScreen {
	@FXML private Button hostButton;
		
	private ScreensController screensController;
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					InetAddress inetAddress = InetAddress.getLocalHost();
					servAddrField.setText(inetAddress.getHostAddress());
				} catch (UnknownHostException e) {
					e.printStackTrace();
					System.exit(0);
				}
			}
		});
	}
	
	@FXML
	private void handleSubmit(ActionEvent event) {
		if(!isPortFieldValid())
			return;
		try {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					try {
						new ServerClient(Integer.parseInt(servPortField.getText())).start(new javafx.stage.Stage());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//new Server(Integer.parseInt(servPortField.getText())));
	}
	
	@FXML
	/**
	 * Handles event when Join Game button is pressed from the tool bar.
	 * @param event the event
	 */
	private void handleJoinGame(ActionEvent event) {
		screensController.setScreen(Carcassonne.JOIN_SCREEN);
	}

	@Override
	public void setScreenParent(ScreensController screen) {
		screensController = screen;
	}
}

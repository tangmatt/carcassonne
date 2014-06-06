package edu.carleton.comp4905.carcassonne.common;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class DialogController implements Initializable {
	@FXML private AnchorPane anchorPane;
	@FXML private Label titleLabel, messageLabel;
	@FXML private Button okButton;
	@FXML private ImageView closeButton;
	private Stage stage;
	private double initialX, initialY;
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		closeButton.setImage(ResourceManager.getImageFromResources("close.png"));
		okButton.setText(LocalMessages.getString("Ok"));
		
		anchorPane.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(event.isPrimaryButtonDown()) {
					initialX = event.getSceneX();
					initialY = event.getSceneY();
				}
			}
		});
		
		anchorPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(event.isPrimaryButtonDown()) {
					anchorPane.getScene().getWindow().setX(event.getScreenX() - initialX);
					anchorPane.getScene().getWindow().setY(event.getScreenY() - initialY);
				}
			}
		});
	}
	
	/**
	 * Closes the lobby stage.
	 */
	public void shutdown() {
		PlatformManager.runLater(new Runnable() {
			@Override
			public void run() {
				stage.getOnCloseRequest().handle(null);
			}
		});
	}
	
	@FXML
	/**
	 * Event triggers when mouse is over the "close" button.
	 * @param event a MouseEvent
	 */
	private void handleCloseButtonEntered(final MouseEvent event) {
		closeButton.setOpacity(1f);
	}
	
	@FXML
	/**
	 * Event triggers when mouse leaves the "close" button.
	 * @param event a MouseEvent
	 */
	private void handleCloseButtonExited(final MouseEvent event) {
		closeButton.setOpacity(0.6f);
	}
	
	@FXML
	/**
	 * Event triggers when mouse presses the "close" button.
	 * @param event a MouseEvent
	 */
	private void handleCloseButtonPressed(final MouseEvent event) {
		if(!event.isPrimaryButtonDown())
			return;
		shutdown();
	}
	
	@FXML
	/**
	 * Event triggers when mouse is over the "ok" button.
	 * @param event a MouseEvent
	 */
	private void handleOkButtonEntered(final MouseEvent event) {
		okButton.setEffect(new Glow());
		okButton.setCursor(Cursor.HAND);
	}
	
	@FXML
	/**
	 * Event triggers when mouse leaves the "ok" button.
	 * @param event a MouseEvent
	 */
	private void handleOkButtonExited(final MouseEvent event) {
		okButton.setEffect(null);
		okButton.setCursor(Cursor.DEFAULT);
	}
	
	@FXML
	/**
	 * Event triggers when mouse presses the "ok" button.
	 * @param event a MouseEvent
	 */
	private void handleOkButtonPressed(final MouseEvent event) {
		if(!event.isPrimaryButtonDown())
			return;
		shutdown();
	}
	
	/**
	 * Initializes data for this controller.
	 * @param stage a Stage
	 * @param title a String
	 * @param message a String
	 */
	public void initData(final Stage stage, final String title, final String message) {
		this.stage = stage;
		titleLabel.setText(title);
		messageLabel.setText(message);
	}
}
package edu.carleton.comp4905.carcassonne.client;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventType;
import edu.carleton.comp4905.carcassonne.common.LocalMessages;
import edu.carleton.comp4905.carcassonne.common.PlatformManager;
import edu.carleton.comp4905.carcassonne.common.ResourceManager;
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

public class LobbyController implements Initializable {
	@FXML private AnchorPane anchorPane;
	@FXML private Label title;
	@FXML private Button startButton;
	@FXML private ImageView closeButton;
	@FXML private ImageView player1, player2, player3, player4, player5;
	private List<ImageView> playerIcons;
	private Stage stage;
	private GameClient client;
	private double initialX, initialY;
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		closeButton.setImage(ResourceManager.getImageFromResources("close.png"));
		title.setText(LocalMessages.getString("LobbyTitle"));
		startButton.setText(LocalMessages.getString("Play"));
		
		playerIcons = new ArrayList<>();
		playerIcons.add(player1);
		playerIcons.add(player2);
		playerIcons.add(player3);
		playerIcons.add(player4);
		playerIcons.add(player5);
		
		for(ImageView playerIcon : playerIcons)
			playerIcon.setImage(ResourceManager.getImageFromResources("free.png"));
		
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
	 * Updates the player queue by representing them as icons.
	 * @param numOfPlayers the number of players (integer)
	 */
	public void updatePlayerIcons(final int numOfPlayers) {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				for(int i=0; i<playerIcons.size(); ++i) {
					if(i < numOfPlayers)
						playerIcons.get(i).setImage(ResourceManager.getImageFromResources("joined.png"));
					else
						playerIcons.get(i).setImage(ResourceManager.getImageFromResources("free.png"));
				}
			}
		});
	}
	
	/**
	 * Handles the start button availability.
	 * @param numbOfPlayers
	 */
	public void handleStartAvailability(final int numOfPlayers) {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				if(numOfPlayers < 2) {
					startButton.setOpacity(0.4f);
					startButton.setDisable(true);
				} else {
					startButton.setOpacity(1);
					startButton.setDisable(false);
				}
			}
		});
	}
	
	/**
	 * Closes the lobby stage and game client.
	 */
	public void shutdown() {
		stage.getOnCloseRequest().handle(null);
	}
	
	/**
	 * Closes the lobby stage.
	 */
	public void close() {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				client.getController().blurGame(false);
				stage.close();
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
	 * Event triggers when mouse is over the "start" button.
	 * @param event a MouseEvent
	 */
	private void handleStartButtonEntered(final MouseEvent event) {
		startButton.setEffect(new Glow());
		startButton.setCursor(Cursor.HAND);
	}
	
	@FXML
	/**
	 * Event triggers when mouse leaves the "start" button.
	 * @param event a MouseEvent
	 */
	private void handleStartButtonExited(final MouseEvent event) {
		startButton.setEffect(null);
		startButton.setCursor(Cursor.DEFAULT);
	}
	
	@FXML
	/**
	 * Event triggers when mouse presses the "start" button.
	 * @param event a MouseEvent
	 */
	private void handleStartButtonPressed(final MouseEvent event) {
		if(!event.isPrimaryButtonDown())
			return;
		client.getGame().getConnection().sendEvent(new Event(EventType.START_REQUEST, client.getGame().getPlayerName()));
	}
	
	/**
	 * Initializes data for this controller.
	 * @param stage a Stage
	 * @param client a GameClient
	 */
	public void initData(final Stage stage, final GameClient client) {
		this.stage = stage;
		this.client = client;
	}
}
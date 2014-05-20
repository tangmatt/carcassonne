package edu.carleton.comp4905.carcassonne.client;

import edu.carleton.comp4905.carcassonne.common.Address;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventType;
import edu.carleton.comp4905.carcassonne.common.FXMLManager;
import edu.carleton.comp4905.carcassonne.common.ResourceManager;
import edu.carleton.comp4905.carcassonne.common.StringConstants;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GameClient extends Application {
	private FXMLLoader fxmlLoader;
	private GameController controller;
	private Game game;
	private final String playerName;
	private final Address address;
	private Stage primaryStage;
	
	public GameClient(final String playerName, final Address address) {
		this.playerName = playerName;
		this.address = address;
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		primaryStage.setTitle(StringConstants.GAME_TITLE + StringConstants.SEPARATOR + playerName);
		primaryStage.getIcons().add(ResourceManager.getImageFromResources("icon.png"));
		primaryStage.setResizable(false);
		
		fxmlLoader = FXMLManager.getFXML(getClass(), "GameScene.fxml");
		BorderPane borderPane = fxmlLoader.load();
		controller = fxmlLoader.getController();
		controller.initData(this);
		
		Scene scene = new Scene(borderPane);
		primaryStage.setScene(scene);	
		primaryStage.show();

		// event handler for closing the window
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				try {
					game.getConnection().sendEvent(new Event(EventType.QUIT_REQUEST, game.getPlayerName()));
					Thread.sleep(33);
				} catch(InterruptedException e) {
					e.printStackTrace();
				} catch(NullPointerException e) {
					// do nothing
				}
				game.shutdown();
				primaryStage.close();
			}
		});
		
		// initialize and execute the game
		game = new Game(playerName, address, controller);
		game.getPool().execute(game);
	}
	
	/**
	 * Returns the game object.
	 * @return a Game
	 */
	public Game getGame() {
		return game;
	}
	
	/**
	 * Returns the GameController object.
	 * @return a GameController
	 */
	public GameController getController() {
		return controller;
	}
	
	/**
	 * Returns the primary stage.
	 * @return a Stage
	 */
	public Stage getStage() {
		return primaryStage;
	}
}

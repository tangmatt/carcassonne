package edu.carleton.comp4905.carcassonne.client;

import edu.carleton.comp4905.carcassonne.common.Address;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventType;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GameClient extends Application {
	private FXMLLoader fxmlLoader;
	private GameController controller;
	private Game game;
	private String playerName;
	
	public GameClient(final String playerName, final Address address) {
		this.playerName = playerName;
		game = new Game(playerName, address);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Carcassonne - " + playerName);
		primaryStage.getIcons().add(new Image("/edu/carleton/comp4905/carcassonne/resources/icon.png"));
		primaryStage.setResizable(false);
		
		fxmlLoader = new FXMLLoader(getClass().getResource("/edu/carleton/comp4905/carcassonne/fxml/GameScene.fxml"));
		BorderPane borderPane = fxmlLoader.load();
		controller = fxmlLoader.getController();
		
		Scene scene = new Scene(borderPane);
		
		primaryStage.setScene(scene);	
		primaryStage.show();

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				game.getConnection().sendEvent(new Event(EventType.QUIT_REQUEST, game.getPlayerName()));
				try {
					Thread.sleep(33);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				game.shutdown();
				primaryStage.close();
			}
		});
		
		game.getPool().execute(game);
	}
	
	public GameController getController() {
		return controller;
	}
}

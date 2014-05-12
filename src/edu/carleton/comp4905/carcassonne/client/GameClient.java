package edu.carleton.comp4905.carcassonne.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.carleton.comp4905.carcassonne.common.Address;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventType;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GameClient extends Application {
	private Game game;
	private ExecutorService executors;
	
	public GameClient(final String playerName, final Address address) {
		game = new Game(playerName, address);
		executors = Executors.newCachedThreadPool();
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Carcassonne");
		primaryStage.getIcons().add(new Image("/edu/carleton/comp4905/carcassonne/images/icon.png"));
		primaryStage.setResizable(false);
		
		Scene scene = new Scene(new AnchorPane(), 600, 400);
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				game.getConnection().sendEvent(new Event(EventType.QUIT_REQUEST, game.getPlayerName()));
				game.shutdown();
				executors.shutdown();
				primaryStage.close();
			}
		});
		
		executors.execute(game);
	}
}

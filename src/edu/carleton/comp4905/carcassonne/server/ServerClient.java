package edu.carleton.comp4905.carcassonne.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ServerClient extends Application {
	private FXMLLoader fxmlLoader;
	private ServerController controller;
	private Server server;
	private ExecutorService executors;
	
	public ServerClient() {
		this.server = new Server();
		this.executors = Executors.newCachedThreadPool();
	}
	
	public ServerClient(final int port) {
		this.server = new Server(port);
		this.executors = Executors.newCachedThreadPool();
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Server - Carcassonne");
		primaryStage.getIcons().add(new Image("/edu/carleton/comp4905/carcassonne/images/icon.png"));
		
		fxmlLoader = new FXMLLoader(getClass().getResource("/edu/carleton/comp4905/carcassonne/fxml/ServerLogScene.fxml"));
		AnchorPane anchorPane = fxmlLoader.load();
		controller = fxmlLoader.getController();
		
		primaryStage.setScene(new Scene(anchorPane));
		primaryStage.show();
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				server.shutdown();
				executors.shutdown();
				primaryStage.close();
			}
		});
		
		server.setController(controller);
		executors.execute(server);
	}
	
	public ServerController getController() {
		return controller;
	}
	
	public static void main(String args[]) {
		launch(args);
	}
}

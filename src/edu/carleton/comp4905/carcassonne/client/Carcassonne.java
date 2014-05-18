package edu.carleton.comp4905.carcassonne.client;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Carcassonne extends Application {
	public static final String JOIN_SCREEN = "ServerConnectScene";
	public static final String JOIN_SCREEN_FXML = "ServerConnectScene.fxml";
	public static final String HOST_SCREEN = "ServerHostScene";
	public static final String HOST_SCREEN_FXML = "ServerHostScene.fxml";
	public static final String SERVER_LOG_SCREEN = "ServerLogScene";
	public static final String SERVER_LOG_SCREEN_FXML = "ServerLogScene.fxml";
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Manager - Carcassonne");
		primaryStage.getIcons().add(new Image("edu/carleton/comp4905/carcassonne/resources/icon.png"));
		primaryStage.setResizable(false);
		
		ScreensController mainContainer = new ScreensController();
		mainContainer.loadScreen(Carcassonne.JOIN_SCREEN, Carcassonne.JOIN_SCREEN_FXML);
		mainContainer.loadScreen(Carcassonne.HOST_SCREEN, Carcassonne.HOST_SCREEN_FXML);
		//mainContainer.loadScreen(Carcassonne.SERVER_LOG_SCREEN, Carcassonne.SERVER_LOG_SCREEN_FXML);
		
		mainContainer.setScreen(Carcassonne.JOIN_SCREEN);
		
		Group root = new Group();
		root.getChildren().addAll(mainContainer);
		Scene scene = new Scene(root, 298-10, 281-10);
		//scene.getStylesheets().addAll(getClass().getResource("/styles/bg.css").toExternalForm());
		primaryStage.setScene(scene); // subtracted by 10 because setResizable adds 10 pixels..
		primaryStage.show();
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				primaryStage.close();
			}
		});
	}
	
	public static void main(String args[]) {
		launch(args);
	}
}

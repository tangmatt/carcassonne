package edu.carleton.comp4905.carcassonne.client;

import edu.carleton.comp4905.carcassonne.common.LocalMessages;
import edu.carleton.comp4905.carcassonne.common.ResourceManager;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Carcassonne extends Application {
	public static final String JOIN_SCREEN = "ServerConnectScene";
	public static final String JOIN_SCREEN_FXML = "ServerConnectScene.fxml";
	public static final String HOST_SCREEN = "ServerHostScene";
	public static final String HOST_SCREEN_FXML = "ServerHostScene.fxml";
	public static final String SERVER_LOG_SCREEN = "ServerLogScene";
	public static final String SERVER_LOG_SCREEN_FXML = "ServerLogScene.fxml";
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle(LocalMessages.getString("ManagerTitle") + " - " + LocalMessages.getString("GameTitle"));
		primaryStage.getIcons().add(ResourceManager.getImageFromResources("icon.png"));
		primaryStage.setResizable(false);
		
		// set up multiple screens on one Stage
		ScreensController mainContainer = new ScreensController(primaryStage);
		mainContainer.loadScreen(Carcassonne.JOIN_SCREEN, Carcassonne.JOIN_SCREEN_FXML);
		mainContainer.loadScreen(Carcassonne.HOST_SCREEN, Carcassonne.HOST_SCREEN_FXML);
		mainContainer.setScreen(Carcassonne.JOIN_SCREEN);
		
		Group root = new Group();
		root.getChildren().addAll(mainContainer);
		Scene scene = new Scene(root, 298-10, 440-10);
		primaryStage.setScene(scene); // subtracted by 10 because setResizable adds 10 pixels apparently (on Windows)..
		primaryStage.show();
	}
	
	public static void main(String args[]) {
		launch(args);
	}
}

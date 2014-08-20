package edu.carleton.comp4905.carcassonne.server;

import edu.carleton.comp4905.carcassonne.common.FXMLManager;
import edu.carleton.comp4905.carcassonne.common.LocalMessages;
import edu.carleton.comp4905.carcassonne.common.Mode;
import edu.carleton.comp4905.carcassonne.common.ResourceManager;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ServerClient extends Application {
	private FXMLLoader fxmlLoader;
	private ServerController controller;
	private final Server server;
	private Stage primaryStage;
	
	public ServerClient() {
		this.server = new Server();
	}
	
	public ServerClient(final int port, final Mode mode) {
		this.server = new Server(port, mode);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		primaryStage.setTitle(LocalMessages.getString("ServerTitle") + " - " + LocalMessages.getString("GameTitle"));
		primaryStage.getIcons().add(ResourceManager.getImageFromResources("icon.png"));
		
		fxmlLoader = FXMLManager.getFXML(getClass(), "ServerLogScene.fxml");
		AnchorPane anchorPane = fxmlLoader.load();
		controller = fxmlLoader.getController();
		controller.initData(this);
		
		primaryStage.setScene(new Scene(anchorPane));
		primaryStage.show();

		// handles the event for closing the window
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				server.shutdown();
				primaryStage.close();
			}
		});
		
		// initializes and executes the server
		server.setController(controller);
		server.getPool().execute(server);
	}
	
	/**
	 * Returns the ServerController object.
	 * @return a ServerController
	 */
	public ServerController getController() {
		return controller;
	}
	
	/**
	 * Returns the server.
	 * @return the server
	 */
	public Server getServer() {
		return server;
	}
	
	/**
	 * Returns the primary stage.
	 * @return a Stage
	 */
	public Stage getStage() {
		return primaryStage;
	}
	
	public static void main(String args[]) {
		launch(args);
	}
}

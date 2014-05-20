package edu.carleton.comp4905.carcassonne.client;

import java.io.IOException;

import edu.carleton.comp4905.carcassonne.common.FXMLManager;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

public class LobbyDialog extends Stage {
	private FXMLLoader fxmlLoader;
	private LobbyController controller;
	
	public LobbyDialog(final Window window, final GameClient client) {
		initOwner(window);
		initStyle(StageStyle.UNDECORATED);
		initModality(Modality.WINDOW_MODAL);
		setResizable(false);
		
		try {
			fxmlLoader = FXMLManager.getFXML(getClass(), "LobbyScene.fxml");
			AnchorPane anchorPane = fxmlLoader.load();
			controller = fxmlLoader.getController();
			controller.initData(this, client);
			
			Scene scene = new Scene(anchorPane);
			setScene(scene);	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				client.getController().blurGame(false);
				client.getStage().getOnCloseRequest().handle(null);
				close();
			}
		});
	}
	
	/**
	 * Returns the LobbyController object.
	 * @return a LobbyController
	 */
	public LobbyController getController() {
		return controller;
	}
}

package edu.carleton.comp4905.carcassonne.common;

import java.io.IOException;

import edu.carleton.comp4905.carcassonne.client.GameClient;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class Dialog extends Stage {
	private FXMLLoader fxmlLoader;
	private DialogController controller;
	
	public Dialog(final Window window, final Application client, final String title, final String message) {
		initOwner(window);
		initStyle(StageStyle.UNDECORATED);
		initModality(Modality.WINDOW_MODAL);
		setResizable(false);
		
		try {
			fxmlLoader = FXMLManager.getFXML(getClass(), "DialogScene.fxml");
			AnchorPane anchorPane = fxmlLoader.load();
			controller = fxmlLoader.getController();
			controller.initData(this, title, message);
			
			Scene scene = new Scene(anchorPane);
			setScene(scene);	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the MessageController object.
	 * @return a MessageController
	 */
	public DialogController getController() {
		return controller;
	}
}

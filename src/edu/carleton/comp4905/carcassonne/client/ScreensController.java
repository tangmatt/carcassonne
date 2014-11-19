package edu.carleton.comp4905.carcassonne.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import edu.carleton.comp4905.carcassonne.common.FXMLManager;
import edu.carleton.comp4905.carcassonne.common.LocalMessages;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ScreensController extends StackPane {
	private final Map<String, Node> screens;
	private final Stage stage;
	
	public ScreensController(final Stage stage) {
		this.stage = stage;
		screens = new HashMap<String, Node>();
	}
	
	/**
	 * Adds a new screen.
	 * @param name a name of the screen
	 * @param screen a screen (Node)
	 */
	public void addScreen(final String name, final Node screen) {
		screens.put(name, screen);
	}
	
	/**
	 * Loads the screen.
	 * @param name a name of the screen
	 * @param resource a resource
	 * @return a boolean
	 */
	public boolean loadScreen(final String name, final String resource) {
		try {
			FXMLLoader fxmlLoader = FXMLManager.getFXML(getClass(), resource);
			fxmlLoader.setResources(LocalMessages.RES_BUNDLE);
			Parent loadScreen = (Parent)fxmlLoader.load();
			Object controller = fxmlLoader.getController();
			if(controller instanceof JoinGameController)
				((JoinGameController)controller).initData(stage);
			else if(controller instanceof HostGameController)
				((HostGameController)controller).initData(stage);
			ControlledScreen screenContainer = ((ControlledScreen)fxmlLoader.getController());
			screenContainer.setScreenParent(this);
			addScreen(name, loadScreen);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Sets the screen.
	 * @param name a name of the screen
	 * @return a boolean
	 */
	public boolean setScreen(final String name) {
		if(screens.get(name) == null)
			return false;
		
		if(!getChildren().isEmpty()) {
			getChildren().remove(0);
			getChildren().add(screens.get(name));
		} else {
			getChildren().add(screens.get(name));
		}
		
		return true;
	}
}

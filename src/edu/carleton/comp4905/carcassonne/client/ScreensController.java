package edu.carleton.comp4905.carcassonne.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class ScreensController extends StackPane {
	private Map<String, Node> screens;
	private final String fxmlDir = "edu/carleton/comp4905/carcassonne/fxml";
	
	public ScreensController() {
		screens = new HashMap<String, Node>();
	}
	
	public void addScreen(String name, Node screen) {
		screens.put(name, screen);
	}
	
	public boolean loadScreen(String name, String resource) {
		try {
			String path = "/" + fxmlDir + "/" + resource;
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
			Parent loadScreen = (Parent)fxmlLoader.load();
			ControlledScreen screenContainer = ((ControlledScreen)fxmlLoader.getController());
			screenContainer.setScreenParent(this);
			addScreen(name, loadScreen);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
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

package edu.carleton.comp4905.carcassonne.client;

import javafx.scene.Cursor;

public class TileContainer extends TileBase {
	public static final String defaultStyle = "-fx-border-color: gray;"
		    + "-fx-border-width: 1;"
		    + "-fx-border-style: dashed;";
	
	public TileContainer(GameTile tile) {
		super(tile);
	}

	@Override
	public void setSelected(boolean state) {
		if(state) {
			setCursor(Cursor.HAND);
			setStyle(selectedStyle);
		} else {
			setCursor(Cursor.DEFAULT);
			setStyle(defaultStyle);
		}
	}
}
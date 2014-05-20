package edu.carleton.comp4905.carcassonne.client;

import javafx.scene.Cursor;

public class TileContainer extends AbstractTile {
	public static final String defaultStyle = "-fx-border-color: gray;"
		    + "-fx-border-width: 1;"
		    + "-fx-border-style: dashed;";
	
	public TileContainer(final GameTile tile) {
		super(tile);
	}

	@Override
	public void setSelected(final boolean state) {
		if(state) {
			setCursor(Cursor.HAND);
			setStyle(selectedStyle);
		} else {
			setCursor(Cursor.DEFAULT);
			setStyle(defaultStyle);
		}
	}
}
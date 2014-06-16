package edu.carleton.comp4905.carcassonne.client;


public class TilePreview extends AbstractTile {
	public static final String defaultStyle = "-fx-border-color: #3B3B3B;"
            + "-fx-border-width: 1;"
            + "-fx-border-style: solid;";
	
	public TilePreview(final GameTile tile) {
		super(tile);
	}

	@Override
	public void setSelected(final boolean state) {
		if(state) {
			setOpacity(1.0f);
			setStyle(selectedStyle);
		} else {
			setOpacity(0.2f);
			setStyle(defaultStyle);
		}
	}
}

package edu.carleton.comp4905.carcassonne.client;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class GameController implements Initializable {
	@FXML private GridPane gridPane;
	@FXML private HBox previewPane;
	@FXML private Button endTurnButton;
	private TileManager tileManager;
	private Board board;
	private static MouseEvent mouseEvent;
	
	public GameController() {
		tileManager = TileManager.getInstance();
		board = new Board();
		mouseEvent = new MouseEvent(MouseEvent.MOUSE_PRESSED, 0, 0, 0, 0, 
				MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null);
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					for(int c=0; c<Board.COLS; ++c) {
						for(int r=0; r<Board.ROWS; ++r) {
							TileContainer container;
							if(r == Board.CENTER_ROW && c == Board.CENTER_COL)
								container = new TileContainer(tileManager.getStarterTile());
							else
								container = new TileContainer(tileManager.getEmptyTile());
							addTile(r, c, container);
						}
					}
					setPreviewTiles(tileManager.getTile("tile2.png"));
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Adds a TileContainer object to specified row and column of layout.
	 * @param r a row (integer)
	 * @param c a column (integer)
	 * @param container a TileContainer
	 */
	public void addTile(int r, int c, TileContainer container) {
		board.setTile(r, c, container);
		gridPane.add(container, c, r);
	}
	
	/**
	 * Places tiles onto the tile preview displays.
	 * @param tile a GameTile
	 */
	public void setPreviewTiles(GameTile tile) {
		try {
			board.setRotatedPreviews(tile);
			preview0.addMouseListener(this, new TilePreviewHandler(preview0, this));
			preview90.addMouseListener(this, new TilePreviewHandler(preview90, this));
			preview180.addMouseListener(this, new TilePreviewHandler(preview180, this));
			preview270.addMouseListener(this, new TilePreviewHandler(preview270, this));
			
			Event.fireEvent(preview0, mouseEvent);

			previewPane.getChildren().addAll(preview0, preview90, preview180, preview270);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		//handleHints(container);
	}
	
	/**
	 * Determines whether the chosen tile can be placed adjacent to existing tiles.
	 * @param container a TileBase
	 */
	public void handleHints(TileBase container) {
		for(int c=0; c<Board.COLS; ++c) {
			for(int r=0; r<Board.ROWS; ++r) {
				TileContainer currTile;
				currTile = board.getTile(r, c);
				if(currTile != null) {
					showHint(Side.TOP, r, c, container, currTile.getTopSegment());
					showHint(Side.RIGHT, r, c, container, currTile.getRightSegment());
					showHint(Side.BOTTOM, r, c, container, currTile.getBottomSegment());
					showHint(Side.LEFT, r, c, container, currTile.getLeftSegment());
				}
			}
		}
	}
	
	/**
	 * Manages the empty tile adjacent to an existing tile if there is a match.
	 * @param side a Side
	 * @param r a row (integer)
	 * @param c a column (integer)
	 * @param container a TileBase
	 * @param segment a Segment
	 * @return boolean
	 */
	private boolean showHint(Side side, int r, int c, TileBase container, Segment segment) {
		try {
			TileBase selected = null;
			if(side == Side.TOP && board.getTile(r-1, c).isEmpty() && container.matchesBottomSegment(segment)) {
				selected = board.getTile(r-1, c);
				selected.setSelected(true);
				selected.addMouseListener(this, new TileContainerHandler(selected, this));
			}
			else if(side == Side.RIGHT && board.getTile(r, c+1).isEmpty() && container.matchesLeftSegment(segment)) {
				selected = board.getTile(r, c+1);
				selected.setSelected(true);
				selected.addMouseListener(this, new TileContainerHandler(selected, this));
			}
			else if(side == Side.BOTTOM && board.getTile(r+1, c).isEmpty() && container.matchesTopSegment(segment)) {
				selected = board.getTile(r+1, c);
				selected.setSelected(true);
				selected.addMouseListener(this, new TileContainerHandler(selected, this));
			}
			else if(side == Side.LEFT && board.getTile(r, c-1).isEmpty() && container.matchesRightSegment(segment)) {
				selected = board.getTile(r, c-1);
				selected.setSelected(true);
				selected.addMouseListener(this, new TileContainerHandler(selected, this));
			}
		} catch(ArrayIndexOutOfBoundsException e) {
			return false;
		}
		
		return true;
	}
	
	public void removeMouseHandlers() {
		
	}
	
	public HBox getPreviewPane() {
		return previewPane;
	}
	
	public GridPane getGridPane() {
		return gridPane;
	}
	
	public Board getBoard() {
		return board;
	}
}

package edu.carleton.comp4905.carcassonne.client;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;

import edu.carleton.comp4905.carcassonne.client.handlers.TileContainerHandler;
import edu.carleton.comp4905.carcassonne.client.handlers.TilePreviewHandler;
import edu.carleton.comp4905.carcassonne.common.PlatformManager;
import edu.carleton.comp4905.carcassonne.common.ResourceManager;
import edu.carleton.comp4905.carcassonne.common.StringConstants;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class GameController implements Initializable {
	@FXML private BorderPane rootPane;
	@FXML private GridPane gridPane;
	@FXML private HBox previewPane;
	@FXML private Button endTurnButton;
	@FXML private ImageView player1, player2, player3, player4, player5;
	private TileManager tileManager;
	private Board board;
	private PopOver popOver;
	private MouseEvent mouseEvent;
	private LobbyDialog lobbyDialog;
	private GameClient client;
	private ImageView[] players;
	private Random random;
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		mouseEvent = new MouseEvent(MouseEvent.MOUSE_PRESSED, 0, 0, 0, 0, 
				MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null);
		tileManager = TileManager.getInstance();
		board = new Board();
		players = new ImageView[5];
		random = new Random();
		
		// initialize player tiles and preview tiles
		players[0] = player1;
		players[1] = player2;
		players[2] = player3;
		players[3] = player4;
		players[4] = player5;
		
		player1.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				double x = player1.getX()+player1.getFitWidth()/2;
				double y = player1.getY()+player1.getFitHeight();
				Point2D p = player1.localToScreen(x, y);
				handlePopOver(player1, p.getX(), p.getY());
			}
		});
		
		setPreviewTiles(tileManager.getEmptyTile()); // initializes the preview tiles to be empty
		
		createGameBoard();
		createPopOver();
		createLobby();
	}
	
	/**
	 * Adds a TileContainer object to specified row and column of layout.
	 * @param r a row (integer)
	 * @param c a column (integer)
	 * @param container a TileContainer
	 */
	public void addTile(final int r, final int c, final TileContainer container) {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				board.setTile(r, c, container);
				gridPane.add(container, c, r);
			}
		});
	}
	
	/**
	 * Places tiles onto the tile preview displays.
	 * @param tile a GameTile
	 */
	public void setPreviewTiles(final GameTile tile) {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				setRotatedPreviews(tile);
				if(previewPane.getChildren().isEmpty())
					previewPane.getChildren().addAll(board.getPreviews());
					Event.fireEvent(board.getPreviews()[0], mouseEvent);
			}
		});
	}
	
	/**
	 * Determines whether the chosen tile can be placed adjacent to existing tiles.
	 * @param preview a TilePreview
	 */
	public void handleHints(final TilePreview preview) {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				refresh();
				int numOfHints = 0;
				for(int c=0; c<Board.COLS; ++c) {
					for(int r=0; r<Board.ROWS; ++r) {
						TileContainer currTile;
						currTile = board.getTile(r, c);
						if(currTile != null) {
							// compare existing tile with new tile and indicating if matching
							numOfHints += showHint(Side.TOP, r, c, preview, currTile.getTopSegment());
							numOfHints += showHint(Side.RIGHT, r, c, preview, currTile.getRightSegment());
							numOfHints += showHint(Side.BOTTOM, r, c, preview, currTile.getBottomSegment());
							numOfHints += showHint(Side.LEFT, r, c, preview, currTile.getLeftSegment());
						}
					}
				}
				// if there are no place-able cards, discard current tile and re-draw
				if(numOfHints == 0)
					drawTileFromDeck();
			}
		});
	}
	
	/**
	 * Manages the empty tile adjacent to an existing tile if there is a match.
	 * @param side a Side
	 * @param r a row (integer)
	 * @param c a column (integer)
	 * @param container an AbstractTile
	 * @param segment a Segment
	 * @return Integer
	 */
	private int showHint(final Side side, final int r, final int c, final AbstractTile container, final Segment segment) {
		try {
			AbstractTile selected = null;
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
			return 0;
		}
		
		return 1;
	}
	
	/**
	 * Initializes the start of a game.
	 */
	public void startGame() {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				drawTileFromDeck(); // TODO temporary; for testing
			}
		});
	}
	
	/**
	 * Handles the start of the turn for the current client.
	 */
	public void drawTileFromDeck() {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				// TODO randomize a preview tile based on Carcassonne rules
				int index = random.nextInt(TileManager.NUM_OF_TILES) + 1;
				setPreviewTiles(tileManager.getTile("tile" + index + ".png"));
			}
		});
	}
	
	/**
	 * Handles the end of the turn for current client.
	 */
	public void endTurn() {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				for(TilePreview preview : board.getPreviews())
					preview.setSelected(false);
				for(int c=0; c<Board.COLS; ++c) {
					for(int r=0; r<Board.ROWS; ++r) {
						board.getTile(r, c).setSelected(false);
						board.getTile(r, c).removeMouseListener();
					}
				}
				// TODO send event to server
			}
		});
	}
	
	/**
	 * Sets previews to be given tile and its rotated views and adds mouse listener to it.
	 * @param tile a GameTile
	 */
	private void setRotatedPreviews(final GameTile tile) {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				TilePreview[] previews = board.getPreviews();
				for(int i=0, degrees=0; i<previews.length; ++i, degrees+=90) {
					boolean original = (degrees == 0);
					if(previews[i] == null)
						previews[i] = new TilePreview(tileManager.getTile(original ? tile.getName() : tile.getName()+degrees));
					else
						previews[i].addTile(tileManager.getTile(original ? tile.getName() : tile.getName()+degrees));
					if(!previews[i].getTile().getName().equals(StringConstants.EMPTY_TILE))
						previews[i].addMouseListener(GameController.this, new TilePreviewHandler(previews[i], GameController.this));
				}
			}
		});
	}
	
	/**
	 * Refreshes the preview and game tiles.
	 */
	public void refresh() {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				for(TilePreview preview : board.getPreviews())
					preview.setSelected(false);
				for(int c=0; c<Board.COLS; ++c) {
					for(int r=0; r<Board.ROWS; ++r) {
						board.getTile(r, c).setSelected(false);
						board.getTile(r, c).removeMouseListener();
					}
				}
			}
		});
	}
	
	/**
	 * Handles the triggering of the PopOver.
	 * @param symbol an ImageView
	 * @param x a x-coordinate (Double)
	 * @param y a y-coordinate (Double)
	 */
	public void handlePopOver(final ImageView symbol, final double x, final double y) {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				if(!popOver.isDetached())
					popOver.hide();
				popOver.show(symbol, x, y);
			}
		});
	}
	
	/**
	 * Creates a PopOver component.
	 */
	private void createPopOver() {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				popOver = new PopOver();
				popOver.setArrowSize(10f);
				popOver.setArrowIndent(15f);
				popOver.setCornerRadius(0f);
				popOver.setArrowLocation(ArrowLocation.TOP_CENTER);
				popOver.setDetachable(false);
				popOver.setAutoHide(true);
			}
		});
	}
	
	/**
	 * Creates a dialog for displaying the player queue and pre-game interaction.
	 */
	private void createLobby() {
		PlatformManager.runLater(new Runnable() { // Note: must use runLater otherwise LoadException
			@Override
			public void run() {
				lobbyDialog = new LobbyDialog(gridPane.getScene().getWindow(), client);
			}
		});
	}
	
	/**
	 * Shows the lobby dialog.
	 */
	public void showLobbyDialog() {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				client.getController().blurGame(true);
				lobbyDialog.show();
			}		
		});
	}
	
	/**
	 * Initializes the game board with empty tiles and the starting tile at the center.
	 */
	private void createGameBoard() {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
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
			}
		});
	}
	
	/**
	 * Shows the panel with players information.
	 * @param statuses an array of booleans
	 */
	public void updatePlayerPanel(final boolean[] statuses) {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				for(int i=0; i<statuses.length; ++i) {
					players[i].setImage(ResourceManager.getImageFromResources(statuses[i] ? "joined.png" : "disconnected.png"));
				}
			}
		});
	}
	
	/**
	 * Adds/removes blur from the interface.
	 * @param state
	 */
	public void blurGame(final boolean state) {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				rootPane.setEffect(state ? new GaussianBlur() : null);
			}
		});
	}
	
	/**
	 * Returns the pane containing the preview tiles. 
	 * @return a HBox
	 */
	public HBox getPreviewPane() {
		return previewPane;
	}
	
	/**
	 * Returns the pane containing the game tiles.
	 * @return a GridPane
	 */
	public GridPane getGridPane() {
		return gridPane;
	}
	
	/**
	 * Returns the Board object.
	 * @return a Board
	 */
	public Board getBoard() {
		return board;
	}
	
	/**
	 * Returns the LobbyController object.
	 * @return a LobbyController
	 */
	public LobbyController getLobbyController() {
		return lobbyDialog.getController();
	}
	
	/**
	 * Returns the GameClient object.
	 * @return a GameClient;
	 */
	public GameClient getGameClient() {
		return client;
	}
	
	/**
	 * Initializes data for this controller.
	 * @param client a GameClient
	 */
	public void initData(final GameClient client) {
		this.client = client;
	}
}

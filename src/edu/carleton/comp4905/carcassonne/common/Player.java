package edu.carleton.comp4905.carcassonne.common;

public class Player {
	/**
	 * Playing - player's current turn in SYNC mode (ASYNC mode not applicable)
	 */
	public static enum Status {
		CONNECTED, DISCONNECTED, PLAYING
	}
	
	private final String name;
	private final String address;
	private final String port;
	private int score;
	private final Status status;
	private boolean hasTile;
	
	public Player(final PlayerBuilder builder) {
		name = builder.name;
		address = builder.address;
		port = builder.port;
		status = builder.status;
		score = builder.score;
		hasTile = builder.hasTile;
	}
	
	/**
	 * Returns the player's name.
	 * @return a String
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the player's address.
	 * @return a String
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * Returns the player's port.
	 * @return a String
	 */
	public String getPort() {
		return port;
	}
	
	/**
	 * Returns the name of the status.
	 * @return a String
	 */
	public String getStatus() {
		return status.name();
	}
	
	/**
	 * Adds points to player's score.
	 * @param points the points to be added
	 */
	public void addPoints(final int points) {
		score += points;
	}
	
	/**
	 * Deducts points from player's score.
	 * @param points the points to be deducted
	 */
	public void deductPoints(final int points) {
		score -= points;
	}
	
	/**
	 * Returns true if the player is connected.
	 * @return a boolean
	 */
	public boolean isConnected() {
		return status == Status.CONNECTED;
	}
	
	/**
	 * Sets the flag indicating the player has a tile.
	 * @param hasTile a boolean
	 */
	public void setHasTile(final boolean hasTile) {
		this.hasTile = hasTile;
	}
	
	/**
	 * Returns true if the player has a tile.
	 * @return a boolean
	 */
	public boolean hasTile() {
		return hasTile;
	}
	
	/**
	 * Returns the player's score.
	 * @return an integer
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * Returns true if the player name, address and port is the same as the Player object given associatively.
	 * @param p a Player
	 * @return a boolean
	 */
	public boolean equals(final Player p) {
		return name.equals(p.getName())
				&& address.equals(p.getAddress())
				&& port.equals(p.getPort());
	}
	
	public static class PlayerBuilder implements Builder<Player> {
		private final String name;
		private final String address;
		private final String port;
		private final Status status;
		private int score;
		private final boolean hasTile;
		
		public PlayerBuilder(final String name, final String address, final String port, final Status status, final boolean hasTile) {
			this.name = name;
			this.address = address;
			this.port = port;
			this.status = status;
			this.hasTile = hasTile;
		}
		
		public PlayerBuilder score(final int score) {
			this.score = score;
			return this;
		}
		
		@Override
		public Player build() {
			return new Player(this);
		}
	}
}

package edu.carleton.comp4905.carcassonne.client;

import java.util.HashMap;
import java.util.Map;

public class ScoreData {
	private Map<String, Integer> players;
	public final static int CLOISTER_POINTS = 9;
	
	public ScoreData() {
		players = new HashMap<String, Integer>();
	}
	
	/**
	 * Sets specified score points to the player.
	 * @param name the player name
	 * @param score the score points
	 */
	public void setPlayerScore(final String name, final int score) {
		if(players.get(name) == null)
			players.put(name, 0);
		else
			players.put(name, score);
	}
	
	/**
	 * Returns the player's score.
	 * @param name the player name
	 * @return the score points
	 */
	public int getPlayerScore(final String name) {
		if(players.get(name) == null)
			setPlayerScore(name, 0);
		return players.get(name);
	}
	
	/**
	 * Removes a player from the score data.
	 * @param name the player name
	 */
	public void removePlayer(final String name) {
		players.remove(name);
	}
}

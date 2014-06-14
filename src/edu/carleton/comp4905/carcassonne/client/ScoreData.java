package edu.carleton.comp4905.carcassonne.client;

import java.util.HashMap;
import java.util.Map;

public class ScoreData {
	private Map<String, Integer> players;
	
	public ScoreData() {
		players = new HashMap<String, Integer>();
	}
	
	/**
	 * Adds specified score points to the player.
	 * @param name the player name
	 * @param score the score points
	 */
	public void addPlayerScore(final String name, final int score) {
		if(players.get(name) == null)
			players.put(name, 0);
		else
			players.put(name, players.get(name) + score);
	}
	
	/**
	 * Deducts specified score points from the player.
	 * @param name the player name
	 * @param points the score points
	 */
	public void deductPlayerScore(final String name, final int points) {
		int difference = players.get(name) - points;
		players.put(name, (difference < 0) ? 0 : difference);
	}
	
	/**
	 * Returns the player's score.
	 * @param name the player name
	 * @return the score points
	 */
	public int getPlayerScore(final String name) {
		if(players.get(name) == null)
			addPlayerScore(name, 0);
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

package edu.carleton.comp4905.carcassonne.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck<T> {
	protected final List<T> tiles;
	
	public Deck() {
		tiles = new ArrayList<T>();
	}

	/**
	 * Draws an object from the top of the deck.
	 */
	public T draw() {
		if(isEmpty())
			return null;
		return tiles.remove(tiles.size()-1);
	}
	
	/**
	 * Adds an object to the deck.
	 * @param object the object
	 */
	public void add(T object) {
		tiles.add(object);
	}
	
	/**
	 * Shuffles the deck.
	 */
	public void shuffle() {
		Collections.shuffle(tiles);
	}
	
	/**
	 * Returns the size of the deck.
	 * @return the size of the deck
	 */
	public int size() {
		return tiles.size();
	}
	
	/**
	 * Returns true if deck is empty.
	 * @return true if deck is empty
	 */
	public boolean isEmpty() {
		return tiles.isEmpty();
	}
}

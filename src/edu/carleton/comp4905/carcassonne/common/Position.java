package edu.carleton.comp4905.carcassonne.common;

/**
 * Note: It is crucial to have these positions in the correct order as they have now.
 * Any changes may affect the algorithm to connect the same adjacent segments.
 * 
 * Ordinals:
 * 0 1 2
 * 3 4 5
 * 6 7 8
 */
public enum Position {
	TOP_LEFT, TOP, TOP_RIGHT,
	LEFT, CENTER, RIGHT,
	BOTTOM_LEFT, BOTTOM, BOTTOM_RIGHT
}

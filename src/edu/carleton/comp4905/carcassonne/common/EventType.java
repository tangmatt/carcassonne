package edu.carleton.comp4905.carcassonne.common;

public enum EventType {
	UNKNOWN,
	JOIN_REQUEST,
	JOIN_REPLY,
	QUIT_REQUEST,
	QUIT_REPLY,
	START_REQUEST,
	START_REPLY,
	SEND_TILE_REQUEST,
	SEND_TILE_REPLY,
	PLAYER_UPDATE_REQUEST,
	PLAYER_UPDATE_REPLY,
	END_GAME_REQUEST,
	END_GAME_REPLY,
	START_TURN_REQUEST,
	START_TURN_REPLY,
	END_TURN_REQUEST,
	END_TURN_REPLY,
}

package com.inverted.tech.mission2048.databaseConnection;

import com.inverted.tech.mission2048.Game;

public class highScoreValue {

	// private variables
	int _mode;
	int _tile;
	int _score;

	public final static int MODE_NORMAL = Game.normalMode;
	public final static int MODE_TIMED = Game.timerMode;
	public final static int MODE_LIMITED_MOVES = Game.limitedMoveMode;

	// Empty constructor
	public highScoreValue() {

	}

	public String toString() {
		return "mode : " + _mode + " tile : " + _tile + " score : " + _score;
	}

	// constructor
	public highScoreValue(int mode, int tile, int score) {
		this._mode = mode;
		this._tile = tile;
		this._score = score;
	}

	// constructor
	public highScoreValue(int mode, int tile) {
		this._mode = mode;
		this._tile = tile;
		this._score = 0;
	}

	public int get_mode() {
		return _mode;
	}

	public void set_mode(int _mode) {
		this._mode = _mode;
	}

	public int get_tile() {
		return _tile;
	}

	public void set_tile(int _tile) {
		this._tile = _tile;
	}

	public int get_score() {
		return _score;
	}

	public void set_score(int _score) {
		this._score = _score;
	}

}

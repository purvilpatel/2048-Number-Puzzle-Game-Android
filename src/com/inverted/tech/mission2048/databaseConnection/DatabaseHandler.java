package com.inverted.tech.mission2048.databaseConnection;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.inverted.tech.mission2048.Game;
import com.inverted.tech.mission2048.supportClass.dataStorageClass;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 43;

	// Database Name
	private static final String DATABASE_NAME = "highScoreManager";

	// table name
	private static final String TABLE_HIGHSCORE = "highscore";
	private static final String TABLE_LOAD_SAVE = "loadsave";
	private static final String TABLE_GAME_DATA = "gamedata";

	// highscores Table Columns names
	private static final String HIGHSCORE_KEY_MODE = "mode";
	private static final String HIGHSCORE_KEY_TILE = "tile";
	private static final String HIGHSCORE_KEY_SCORE = "score";

	// save-load Table Columns names
	private static final String SAVE_LOAD_ROW_ID = "id";
	private static final String SAVE_LOAD_MODE = "mode";
	private static final String SAVE_LOAD_SCORE = "score";
	private static final String SAVE_LOAD_NO_OF_TILE = "no_of_tile";
	private static final String SAVE_LOAD_TIME = "time";
	private static final String SAVE_LOAD_GAME_BOARD = "game_board";

	// save-load Table Columns names
	private static final String GAME_DATA_LIKE_US = "like_us";
	private static final String GAME_DATA_RATE_US = "rate_us";
	private static final String GAME_DATA_SCREEN_HEIGHT = "screen_height";
	private static final String GAME_DATA_SCREEN_WIDTH = "screen_width";
	private static final String GAME_DATA_MAX_TILE = "max_tile";
	private static final String GAME_DATA_MUSIC = "music";
	private static final String GAME_DATA_NO_OF_TILE = "no_of_tile";
	private static final String GAME_DATA_GAME_MODE = "game_mode";
	private static final String GAME_DATA_POINTS = "points";
	private static final String GAME_TOTAL_SCORES = "total_score";
	private static final String GAME_TOTAL_WINS = "total_win";
	private static final String GAME_TOTAL_GAME_PLAYED = "total_game";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		System.out.println("creating database");
		String CREATE_HIGHSCORE_TABLE = "CREATE TABLE " + TABLE_HIGHSCORE + "("
				+ HIGHSCORE_KEY_MODE + " INTEGER ," + HIGHSCORE_KEY_TILE
				+ " INTEGER," + HIGHSCORE_KEY_SCORE + " INTEGER" + ")";
		System.out.println(CREATE_HIGHSCORE_TABLE);
		db.execSQL(CREATE_HIGHSCORE_TABLE);

		String CREATE_LOAD_SAVE_TABLE = "CREATE TABLE " + TABLE_LOAD_SAVE + "("
				+ SAVE_LOAD_ROW_ID + " INTEGER PRIMARY KEY," + SAVE_LOAD_MODE
				+ " INTEGER," + SAVE_LOAD_SCORE + " INTEGER,"
				+ SAVE_LOAD_NO_OF_TILE + " INTEGER," + SAVE_LOAD_TIME
				+ " INTEGER," + SAVE_LOAD_GAME_BOARD + " TEXT" + ")";

		System.out.println(CREATE_LOAD_SAVE_TABLE);
		db.execSQL(CREATE_LOAD_SAVE_TABLE);

		String CREATE_GAME_DATA_TABLE = "CREATE TABLE " + TABLE_GAME_DATA + "("
				+ GAME_DATA_LIKE_US + " INTEGER," + GAME_DATA_RATE_US
				+ " INTEGER," + GAME_DATA_SCREEN_HEIGHT + " INTEGER,"
				+ GAME_DATA_SCREEN_WIDTH + " INTEGER," + GAME_DATA_MAX_TILE
				+ " INTEGER," + GAME_DATA_MUSIC + " INTEGER,"
				+ GAME_DATA_NO_OF_TILE + " INTEGER," + GAME_DATA_GAME_MODE
				+ " INTEGER," + GAME_DATA_POINTS + " INTEGER,"
				+ GAME_TOTAL_SCORES + " INTEGER," + GAME_TOTAL_GAME_PLAYED
				+ " INTEGER," + GAME_TOTAL_WINS + " INTEGER" + ")";

		System.out.println(CREATE_GAME_DATA_TABLE);
		db.execSQL(CREATE_GAME_DATA_TABLE);

		ContentValues values = new ContentValues();
		values.put(GAME_DATA_LIKE_US, 0);
		values.put(GAME_DATA_RATE_US, 0);
		values.put(GAME_DATA_SCREEN_HEIGHT, 100);
		values.put(GAME_DATA_SCREEN_WIDTH, 100);
		values.put(GAME_DATA_MAX_TILE, 2048);
		values.put(GAME_DATA_MUSIC, 1);
		values.put(GAME_DATA_NO_OF_TILE, 4);
		values.put(GAME_DATA_GAME_MODE, 1);
		values.put(GAME_DATA_POINTS, 5000);
		values.put(GAME_TOTAL_SCORES, 0);
		values.put(GAME_TOTAL_GAME_PLAYED, 0);
		values.put(GAME_TOTAL_WINS, 0);
		db.insert(TABLE_GAME_DATA, null, values);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		System.out.println("ipgrading database");
		// Drop older table if existed
		// db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIGHSCORE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOAD_SAVE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAME_DATA);
		// Create tables again
		String CREATE_LOAD_SAVE_TABLE = "CREATE TABLE " + TABLE_LOAD_SAVE + "("
				+ SAVE_LOAD_ROW_ID + " INTEGER PRIMARY KEY," + SAVE_LOAD_MODE
				+ " INTEGER," + SAVE_LOAD_SCORE + " INTEGER,"
				+ SAVE_LOAD_NO_OF_TILE + " INTEGER," + SAVE_LOAD_TIME
				+ " INTEGER," + SAVE_LOAD_GAME_BOARD + " TEXT" + ")";

		System.out.println(CREATE_LOAD_SAVE_TABLE);
		db.execSQL(CREATE_LOAD_SAVE_TABLE);

		String CREATE_GAME_DATA_TABLE = "CREATE TABLE " + TABLE_GAME_DATA + "("
				+ GAME_DATA_LIKE_US + " INTEGER," + GAME_DATA_RATE_US
				+ " INTEGER," + GAME_DATA_SCREEN_HEIGHT + " INTEGER,"
				+ GAME_DATA_SCREEN_WIDTH + " INTEGER," + GAME_DATA_MAX_TILE
				+ " INTEGER," + GAME_DATA_MUSIC + " INTEGER,"
				+ GAME_DATA_NO_OF_TILE + " INTEGER," + GAME_DATA_GAME_MODE
				+ " INTEGER," + GAME_DATA_POINTS + " INTEGER,"
				+ GAME_TOTAL_SCORES + " INTEGER," + GAME_TOTAL_GAME_PLAYED
				+ " INTEGER," + GAME_TOTAL_WINS + " INTEGER" + ")";

		System.out.println(CREATE_GAME_DATA_TABLE);
		db.execSQL(CREATE_GAME_DATA_TABLE);

		ContentValues values = new ContentValues();
		values.put(GAME_DATA_LIKE_US, 0);
		values.put(GAME_DATA_RATE_US, 0);
		values.put(GAME_DATA_SCREEN_HEIGHT, 100);
		values.put(GAME_DATA_SCREEN_WIDTH, 100);
		values.put(GAME_DATA_MAX_TILE, 2048);
		values.put(GAME_DATA_MUSIC, 1);
		values.put(GAME_DATA_NO_OF_TILE, 4);
		values.put(GAME_DATA_GAME_MODE, 1);
		values.put(GAME_DATA_POINTS, 5000);
		values.put(GAME_TOTAL_SCORES, 0);
		values.put(GAME_TOTAL_GAME_PLAYED, 0);
		values.put(GAME_TOTAL_WINS, 0);
		db.insert(TABLE_GAME_DATA, null, values);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	public void insertHighScore(int gameMode, int noOfTile, int score[]) {
		SQLiteDatabase db = this.getWritableDatabase();

		String deleteClause = " " + HIGHSCORE_KEY_MODE + "=?" + " and "
				+ HIGHSCORE_KEY_TILE + "=?";
		db.delete(
				TABLE_HIGHSCORE,
				deleteClause,
				new String[] { String.valueOf(gameMode),
						String.valueOf(noOfTile) });

		for (int i = 0; i < score.length; i++) {
			ContentValues values = new ContentValues();
			values.put(HIGHSCORE_KEY_MODE, gameMode);
			values.put(HIGHSCORE_KEY_TILE, noOfTile); // tile
			values.put(HIGHSCORE_KEY_SCORE, score[i]); // score
			// Inserting Row
			db.insert(TABLE_HIGHSCORE, null, values);
		}
		db.close();
	}

	public List<highScoreValue> getAllHighScore(int gameMode, int noOfTile) {
		List<highScoreValue> highscoreList = new ArrayList<highScoreValue>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_HIGHSCORE;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				highScoreValue highscore = new highScoreValue();
				highscore.set_mode(Integer.parseInt(cursor.getString(0)));
				highscore.set_tile(Integer.parseInt(cursor.getString(1)));
				highscore.set_score(Integer.parseInt(cursor.getString(2)));
				// Adding highscore to list
				if (highscore.get_mode() == gameMode
						&& highscore.get_tile() == noOfTile) {
					highscoreList.add(highscore);
				}
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		// return highscore list
		return highscoreList;
	}

	public void setGameDataTotalScores(int score) {
		int totalScore = getGameDataTotalScores() + score;
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(GAME_TOTAL_SCORES, totalScore);
		db.update(TABLE_GAME_DATA, values, null, null);
		db.close();
	}

	public int getGameDataTotalScores() {
		String selectQuery = "SELECT " + GAME_TOTAL_SCORES + " FROM "
				+ TABLE_GAME_DATA;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			int point = cursor.getInt(0);
			cursor.close();
			db.close();
			return point;
		}
		cursor.close();
		db.close();
		return 0;
	}

	public void setGameDataTotalGame() {
		int totalScore = getGameDataTotalGame() + 1;
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(GAME_TOTAL_SCORES, totalScore);
		db.update(TABLE_GAME_DATA, values, null, null);
		db.close();
	}

	public int getGameDataTotalGame() {
		String selectQuery = "SELECT " + GAME_TOTAL_GAME_PLAYED + " FROM "
				+ TABLE_GAME_DATA;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			int point = cursor.getInt(0);
			cursor.close();
			db.close();
			return point;
		}
		cursor.close();
		db.close();
		return 0;
	}

	public void setGameDataTotalWins() {
		int totalScore = getGameDataTotalWins() + 1;
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(GAME_TOTAL_SCORES, totalScore);
		db.update(TABLE_GAME_DATA, values, null, null);
		db.close();
	}

	public int getGameDataTotalWins() {
		String selectQuery = "SELECT " + GAME_TOTAL_WINS + " FROM "
				+ TABLE_GAME_DATA;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			int point = cursor.getInt(0);
			cursor.close();
			db.close();
			return point;
		}
		cursor.close();
		db.close();
		return 0;
	}

	public void setGameDataPoints(int points) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(GAME_DATA_POINTS, points);
		db.update(TABLE_GAME_DATA, values, null, null);
		db.close();
	}

	public int getGameDataPoints() {
		String selectQuery = "SELECT " + GAME_DATA_POINTS + " FROM "
				+ TABLE_GAME_DATA;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			int point = cursor.getInt(0);
			cursor.close();
			db.close();
			return point;
		}
		cursor.close();
		db.close();
		return 0;
	}

	public void setGameDataLikeUs(int flag) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(GAME_DATA_LIKE_US, flag);
		db.update(TABLE_GAME_DATA, values, null, null);
		db.close();
	}

	public boolean getGameDataLikeUs() {
		String selectQuery = "SELECT " + GAME_DATA_LIKE_US + " FROM "
				+ TABLE_GAME_DATA;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			if (cursor.getInt(0) == 1) {
				cursor.close();
				db.close();
				return true;
			}

		}
		cursor.close();
		db.close();
		return false;
	}

	public void setGameDataRateUs(int flag) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(GAME_DATA_RATE_US, flag);
		db.update(TABLE_GAME_DATA, values, null, null);
		db.close();
	}

	public boolean getGameDataRateUs() {
		String selectQuery = "SELECT " + GAME_DATA_RATE_US + " FROM "
				+ TABLE_GAME_DATA;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			if (cursor.getInt(0) == 1) {
				cursor.close();
				db.close();
				return true;
			}
		}
		cursor.close();
		db.close();
		return false;
	}

	public void setScreenDimension(int height, int width) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(GAME_DATA_SCREEN_HEIGHT, height);
		values.put(GAME_DATA_SCREEN_WIDTH, width);
		db.update(TABLE_GAME_DATA, values, null, null);
		db.close();
	}

	public int getGameDataScreenHeight() {
		String selectQuery = "SELECT " + GAME_DATA_SCREEN_HEIGHT + " FROM "
				+ TABLE_GAME_DATA;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query(TABLE_GAME_DATA,
				new String[] { GAME_DATA_SCREEN_HEIGHT }, null, null, null,
				null, null);
		if (cursor.moveToFirst()) {
			int i = cursor.getInt(0);
			cursor.close();
			db.close();
			return i;
		}
		cursor.close();
		db.close();
		return 0;
	}

	public int getGameDataScreenWidth() {
		String selectQuery = "SELECT " + GAME_DATA_SCREEN_WIDTH + " FROM "
				+ TABLE_GAME_DATA;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			int i = cursor.getInt(0);
			cursor.close();
			db.close();
			return i;
		}
		cursor.close();
		db.close();
		return 0;
	}

	public void setGameDataMaxTile(int target) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(GAME_DATA_MAX_TILE, target);
		db.update(TABLE_GAME_DATA, values, null, null);
		db.close();
	}

	public int getGameDataMaxTile() {
		String selectQuery = "SELECT " + GAME_DATA_MAX_TILE + " FROM "
				+ TABLE_GAME_DATA;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			int i = cursor.getInt(0);
			cursor.close();
			db.close();
			return i;
		}
		cursor.close();
		db.close();
		return 2048;
	}

	public void setGameDataMusic(int flag) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(GAME_DATA_MUSIC, flag);
		db.update(TABLE_GAME_DATA, values, null, null);
		db.close();
	}

	public boolean getGameDataMusic() {
		String selectQuery = "SELECT " + GAME_DATA_MUSIC + " FROM "
				+ TABLE_GAME_DATA;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			// readGameData();
			if (cursor.getInt(0) == 1) {
				cursor.close();
				db.close();
				return true;
			}

		}
		cursor.close();
		db.close();
		return false;
	}

	public void setGameDataNoOfTile(int target) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(GAME_DATA_NO_OF_TILE, target);
		db.update(TABLE_GAME_DATA, values, null, null);
		db.close();
	}

	public int getGameDataNoOfTile() {
		String selectQuery = "SELECT " + GAME_DATA_NO_OF_TILE + " FROM "
				+ TABLE_GAME_DATA;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			int i = cursor.getInt(0);
			cursor.close();
			db.close();
			return i;
		}
		cursor.close();
		db.close();
		return 4;
	}

	public void setGameDataGameMode(int target) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(GAME_DATA_GAME_MODE, target);
		db.update(TABLE_GAME_DATA, values, null, null);
		db.close();
	}

	public int getGameDataGameMode() {
		String selectQuery = "SELECT " + GAME_DATA_GAME_MODE + " FROM "
				+ TABLE_GAME_DATA;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			int i = cursor.getInt(0);
			cursor.close();
			db.close();
			return i;
		}
		cursor.close();
		db.close();
		return 4;
	}

	protected void readGameData() {
		String selectQuery = "SELECT * FROM " + TABLE_GAME_DATA;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		for (int i = 0; i < cursor.getColumnCount(); i++) {
			System.out.println("" + cursor.getColumnName(i) + " : ");
		}
		cursor.close();
		db.close();
	}

	// Storing a game State
	public void saveGame(SaveAndLoad state) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(SAVE_LOAD_MODE, state.getMode());
		values.put(SAVE_LOAD_SCORE, state.getScore());
		values.put(SAVE_LOAD_NO_OF_TILE, state.getNoOfTile());
		values.put(SAVE_LOAD_TIME, state.getTime());
		values.put(SAVE_LOAD_GAME_BOARD, state.getGameBoard());

		db.insert(TABLE_LOAD_SAVE, null, values);
		db.close();
	}

	// Adding new highscore
	public void addHighSCore(highScoreValue score) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(HIGHSCORE_KEY_MODE, score.get_mode());
		values.put(HIGHSCORE_KEY_TILE, score.get_tile()); // tile
		values.put(HIGHSCORE_KEY_SCORE, score.get_score()); // score

		// Inserting Row
		db.insert(TABLE_HIGHSCORE, null, values);
		// Closing database connection
		db.close();
	}

	// public void addEmptyHighScore() {
	// for (int i = 3; i <= 5; i++) {
	// for (int idx = 0; idx < 10; idx++) {
	// highScoreValue emptyScore = new highScoreValue(
	// highScoreValue.MODE_NORMAL, i);
	// addHighSCore(emptyScore);
	// // System.out.println(emptyScore.toString());
	// }
	//
	// for (int idx = 0; idx < 10; idx++) {
	// highScoreValue emptyScore = new highScoreValue(
	// highScoreValue.MODE_TIMED, i);
	// addHighSCore(emptyScore);
	// // System.out.println(emptyScore.toString());
	// }
	//
	// for (int idx = 0; idx < 10; idx++) {
	// highScoreValue emptyScore = new highScoreValue(
	// highScoreValue.MODE_LIMITED_MOVES, i);
	// addHighSCore(emptyScore);
	// // System.out.println(emptyScore.toString());
	// }
	// }
	// }

	// Getting single Game State
	public SaveAndLoad loadGame(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_LOAD_SAVE, new String[] {
				SAVE_LOAD_ROW_ID, SAVE_LOAD_MODE, SAVE_LOAD_SCORE,
				SAVE_LOAD_NO_OF_TILE, SAVE_LOAD_TIME, SAVE_LOAD_GAME_BOARD },
				SAVE_LOAD_ROW_ID + "=?", new String[] { String.valueOf(id) },
				null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		SaveAndLoad gameState = new SaveAndLoad(Integer.parseInt(cursor
				.getString(0)), Integer.parseInt(cursor.getString(1)),
				Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor
						.getString(3)), Integer.parseInt(cursor.getString(4)),
				cursor.getString(5));

		// return game state
		cursor.close();
		db.close();
		return gameState;
	}

	// Getting single HighScore
	public highScoreValue getHighScore(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_HIGHSCORE, new String[] {
				HIGHSCORE_KEY_MODE, HIGHSCORE_KEY_TILE, HIGHSCORE_KEY_SCORE },
				HIGHSCORE_KEY_MODE + "=?", new String[] { String.valueOf(id) },
				null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		highScoreValue highscore = new highScoreValue(Integer.parseInt(cursor
				.getString(0)), Integer.parseInt(cursor.getString(1)),
				Integer.parseInt(cursor.getString(2)));
		// return highscore
		cursor.close();
		db.close();
		return highscore;
	}

	// public void deleteGameState(int id) {
	// String deleteQuery = "DELETE FROM " + TABLE_LOAD_SAVE + " where "
	// + SAVE_LOAD_ROW_ID + " = " + String.valueOf(id);
	// System.out.println(deleteQuery);
	//
	// SQLiteDatabase db = this.getWritableDatabase();
	// db.rawQuery(deleteQuery, null);
	// }

	// Getting All Game State
	public List<SaveAndLoad> getAllGameState() {
		List<SaveAndLoad> gameStateList = new ArrayList<SaveAndLoad>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_LOAD_SAVE;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				SaveAndLoad gameState = new SaveAndLoad();

				gameState.setRow_id(Integer.parseInt(cursor.getString(0)));
				gameState.setMode(Integer.parseInt(cursor.getString(1)));
				gameState.setScore(Integer.parseInt(cursor.getString(2)));
				gameState.setNoOfTile(Integer.parseInt(cursor.getString(3)));
				gameState.setTime(Integer.parseInt(cursor.getString(4)));
				gameState.setGameBoard(cursor.getString(5));

				// Adding Game State to list
				gameStateList.add(gameState);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		// return Game State list
		return gameStateList;
	}

	// Getting All HighScore filtered by game Mode
	public List<highScoreValue> getAllHighScore(int gameMode) {
		List<highScoreValue> highscoreList = new ArrayList<highScoreValue>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_HIGHSCORE;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				highScoreValue highscore = new highScoreValue();
				highscore.set_mode(Integer.parseInt(cursor.getString(0)));
				highscore.set_tile(Integer.parseInt(cursor.getString(1)));
				highscore.set_score(Integer.parseInt(cursor.getString(2)));
				// Adding highscore to list
				if (highscore.get_mode() == gameMode)
					highscoreList.add(highscore);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		// return highscore list
		return highscoreList;
	}// Getting All HighScore filtered by game Mode

	// Getting All HighScore
	public List<highScoreValue> getAllHighScore() {
		List<highScoreValue> highscoreList = new ArrayList<highScoreValue>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_HIGHSCORE;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				highScoreValue highscore = new highScoreValue();
				highscore.set_mode(Integer.parseInt(cursor.getString(0)));
				highscore.set_tile(Integer.parseInt(cursor.getString(1)));
				highscore.set_score(Integer.parseInt(cursor.getString(2)));
				// Adding highscore to list
				highscoreList.add(highscore);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		// return highscore list
		return highscoreList;
	}

	public void updateDataBase(dataStorageClass[] data) {
		// System.out.println("updating databas");

		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE " + TABLE_HIGHSCORE);

		String CREATE_HIGHSCORE_TABLE = "CREATE TABLE " + TABLE_HIGHSCORE + "("
				+ HIGHSCORE_KEY_MODE + " INTEGER ," + HIGHSCORE_KEY_TILE
				+ " INTEGER," + HIGHSCORE_KEY_SCORE + " INTEGER" + ")";
		db.execSQL(CREATE_HIGHSCORE_TABLE);

		for (int i = Game.timerMode; i <= Game.limitedMoveMode; i++) {
			for (int idx = 0; idx < 10; idx++) {
				highScoreValue emptyScore = new highScoreValue(
						highScoreValue.MODE_NORMAL, i,
						data[i].normalBestScore[idx]);
				addHighSCore(emptyScore);
				// System.out.println(emptyScore.toString());
			}

			for (int idx = 0; idx < 10; idx++) {
				highScoreValue emptyScore = new highScoreValue(
						highScoreValue.MODE_TIMED, i,
						data[i].timerBestScore[idx]);
				addHighSCore(emptyScore);
				// System.out.println(emptyScore.toString());
			}

			for (int idx = 0; idx < 10; idx++) {
				highScoreValue emptyScore = new highScoreValue(
						highScoreValue.MODE_LIMITED_MOVES, i,
						data[i].limitedBestScore[idx]);
				addHighSCore(emptyScore);
				// System.out.println(emptyScore.toString());
			}
		}
		db.close();
	}

	// deleting Game State
	public void deleteGameState(long id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_LOAD_SAVE, SAVE_LOAD_ROW_ID + " = ?",
				new String[] { String.valueOf(id) });
		db.close();
	}

	// Deleting single HighScore
	public void deletehighscore(highScoreValue highscore) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_HIGHSCORE, HIGHSCORE_KEY_MODE + " = ?",
				new String[] { String.valueOf(highscore.get_mode()) });
		db.close();
	}

	// Getting Game State Count
	public int getGameStateCount() {

		SQLiteDatabase db = this.getWritableDatabase();

		Cursor c = db.rawQuery(
				"SELECT name FROM sqlite_master WHERE type='table'", null);

		if (c.moveToFirst()) {
			while (!c.isAfterLast()) {
				// System.out.println("Table Name=> " + c.getString(0));
				c.moveToNext();
			}
		}

		String countQuery = "SELECT  * FROM " // + DATABASE_NAME + "."
				+ TABLE_LOAD_SAVE;
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();
		db.close();
		// return count
		return count;
	}

	// Getting HighScore Count
	public int getHighScoreCount() {

		SQLiteDatabase db = this.getWritableDatabase();

		Cursor c = db.rawQuery(
				"SELECT name FROM sqlite_master WHERE type='table'", null);

		if (c.moveToFirst()) {
			while (!c.isAfterLast()) {
				// System.out.println("Table Name=> " + c.getString(0));
				c.moveToNext();
			}
		}

		String countQuery = "SELECT  * FROM " // + DATABASE_NAME + "."
				+ TABLE_HIGHSCORE;
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();
		db.close();
		// return count
		return count;
	}

}

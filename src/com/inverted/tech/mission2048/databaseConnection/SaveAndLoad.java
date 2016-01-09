package com.inverted.tech.mission2048.databaseConnection;

public class SaveAndLoad {

	int row_id;
	int mode;
	int score;
	int noOfTile;
	int time;
	String gameBoard;

	public final static int MODE_TIMED = 0;
	public final static int MODE_NORMAL = 1;
	public final static int MODE_LIMITED_MOVES = 2;

	public SaveAndLoad() {
	}

	public SaveAndLoad(int id, int mode, int score, int noOfTile, int time,
			String gameBoard) {
		super();
		this.row_id = id;
		this.mode = mode;
		this.score = score;
		this.noOfTile = noOfTile;
		this.time = time;
		this.gameBoard = gameBoard;
	}

	public void boardToString(int board[][]) {
		String str = "";

		for (int i = 0; i < noOfTile; i++)
			for (int j = 0; j < noOfTile; j++) {
				str += String.valueOf(board[i][j]) + "#";
			}
		this.gameBoard = new String(str);
		System.out.println("board to string : " + str);
	}

	public int[][] stringToBoard() {
		int temp[][] = new int[noOfTile][noOfTile];
		String str[] = gameBoard.split("#");
		int idx = 0;
		for (int i = 0; i < noOfTile; i++)
			for (int j = 0; j < noOfTile; j++) {
				temp[i][j] = Integer.parseInt(str[idx]);
				idx++;
			}
		return temp;
	}

	public String getDisplayvalue() {

		return modeToString() + "\nTile_" + String.valueOf(noOfTile)
				+ " Score_" + String.valueOf(score);
	}

	private String modeToString() {
		switch (mode) {
		case MODE_LIMITED_MOVES:
			return "Limited Mode";

		case MODE_NORMAL:
			return "Normal Mode";

		case MODE_TIMED:
			return "Timed Mode";

		}
		return "";
	}

	public int getRow_id() {
		return row_id;
	}

	public void setRow_id(int row_id) {
		this.row_id = row_id;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getNoOfTile() {
		return noOfTile;
	}

	public void setNoOfTile(int noOfTile) {
		this.noOfTile = noOfTile;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getGameBoard() {
		return gameBoard;
	}

	public void setGameBoard(String gameBoard) {
		this.gameBoard = gameBoard;
	}

}

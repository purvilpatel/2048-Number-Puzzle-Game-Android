package com.inverted.tech.depricatedClass;

import java.util.Arrays;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.inverted.tech.mission2048.Game;
import com.inverted.tech.mission2048.GameDisplay;
import com.inverted.tech.mission2048.HighScore;
import com.inverted.tech.mission2048.R;
import com.inverted.tech.mission2048.Resume_Pause;
import com.inverted.tech.mission2048.R.drawable;
import com.inverted.tech.mission2048.R.id;
import com.inverted.tech.mission2048.R.layout;
import com.inverted.tech.mission2048.R.raw;
import com.inverted.tech.mission2048.databaseConnection.DatabaseHandler;
import com.inverted.tech.mission2048.databaseConnection.SaveAndLoad;
import com.inverted.tech.mission2048.supportClass.OnSwipeTouchListener;

@SuppressLint("DrawAllocation")
public class NormalGame extends View implements Runnable {

	public static int gameBoardSize;
	public static int gameBoard[][];
	private int prevGameBoard[][];
	private Point gameBoardPos[][];

	// point object
	private Point scoreDisplay;
	private Point timeDisplay;
	private Point scoreDisplayCenter;
	private Point timeDisplayCenter;
	private Point tutorialCenter;

	private Point playPauseCenter;
	private Point saveCenter;

	private Context cntxt;

	// button position keepers
	private int tileHeight;
	private int tileWidth;
	private int tileSepration;
	private int gameBoardWidth;
	private int headerButtonWidth;
	private int headerButtonHeight;
	private int controllButtonSize;

	// paint objects
	private Paint timeScoreTextPaint;
	private Paint smallTextPaint;
	private Paint tutorialTextPaint;

	// bitmaps
	private Bitmap bitmapBkg;
	private Bitmap bitmapEmpty;
	private Bitmap bitmapScore;
	private Bitmap bitmapTime;

	private Bitmap bitmapPause;
	private Bitmap bitmapSave;

	private Bitmap bitmapTile_2;
	private Bitmap bitmapTile_4;
	private Bitmap bitmapTile_8;
	private Bitmap bitmapTile_16;
	private Bitmap bitmapTile_32;
	private Bitmap bitmapTile_64;
	private Bitmap bitmapTile_128;
	private Bitmap bitmapTile_256;
	private Bitmap bitmapTile_512;
	private Bitmap bitmapTile_1024;
	private Bitmap bitmapTile_2048;
	private Bitmap bitmapTile_4096;

	private Random rndMove;
	public static int currentScore;
	public static int totalTime = 0;

	/*
	 * for tile new tile generation if current and previous swipe are not equal
	 * than generate new tile
	 */
	private int prevSwipe;
	private int currentSwipe;
	private int SWIPE_LEFT = 1;
	private int SWIPE_RIGHT = 2;
	private int SWIPE_UP = 3;
	private int SWIPE_DOWN = 4;

	private Thread timerThread;
	private boolean timerThreadActive = true;

	public static boolean isPlay = true;
	private boolean isMusic = true;

	// Tutorial messages
	private String tutorialMsg[] = { "Swipe anywhere to move the tiles.",
			"Every Tile movese when you swipe.",
			"Add same numbers by pushing them into walls.", "Great !.",
			"New Numbers appear when you move Tiles.", "You've got it.",
			"Create another 8 to countinue",
			"Your Aim is to Create Tile of 2048.",
			"Use Save button to store current game state.",
			"Explore Menu for more options" };

	private boolean tileMerged = false;
	private int msgNo = 0;

	private MediaPlayer tileGenClickPlayer;
	public static String msgForHighScoreActivity = "";

	private boolean gameEndFlag = false;
	private boolean gameWonFlag = false;
	private boolean threadSyncFlag = false;

	SaveAndLoad gameState = new SaveAndLoad();
	private int screenWidth;
	private int screenHeight;
	private int targetTile;

	public NormalGame(final Context context) {
		super(context);
		this.cntxt = context;

		DatabaseHandler db = new DatabaseHandler(context);
		gameBoardSize = db.getGameDataNoOfTile();
		gameBoard = new int[gameBoardSize][gameBoardSize];
		prevGameBoard = new int[gameBoardSize][gameBoardSize];
		gameBoardPos = new Point[gameBoardSize][gameBoardSize];

		initInfo();

		// register touch gesture listener
		setOnTouchListener(new OnSwipeTouchListener(context) {
			public boolean onTap(MotionEvent me) {

				Point touchPoint = new Point((int) me.getX(), (int) me.getY());

				if (isPlayPauseTouched(touchPoint)) {
					updatePlayPause();
				} else if (isSavetouched(touchPoint)) {
					launchSaveDialog();
				}
				return true;
			}

			public boolean onSwipeTop() {
				if (gameEndFlag || gameWonFlag)
					return true;
				if (Game.tutorialEnabled)
					generateMessage();
				swipeUpNew();
				// display();
				return true;
			}

			public boolean onSwipeRight() {
				if (gameEndFlag || gameWonFlag)
					return true;
				if (Game.tutorialEnabled)
					generateMessage();
				// display();
				swipeRightNew();
				// display();
				return true;
			}

			public boolean onSwipeLeft() {
				if (gameEndFlag || gameWonFlag)
					return true;
				if (Game.tutorialEnabled)
					generateMessage();
				// display();
				swipeLeftNew();
				// display();
				return true;
			}

			public boolean onSwipeBottom() {
				if (gameEndFlag || gameWonFlag)
					return true;
				if (Game.tutorialEnabled)
					generateMessage();
				// display();
				swipeDownNew();
				// display();
				return true;
			}
		});
	}

	/**
	 * save button is touched, show save button dialog
	 */
	protected void launchSaveDialog() {
		gameState.setMode(Game.normalMode);
		gameState.setScore(currentScore);
		gameState.setNoOfTile(gameBoardSize);
		gameState.setTime(totalTime);
		gameState.boardToString(gameBoard);
		System.out.println(gameState.getDisplayvalue() + "\n"
				+ cntxt.toString());

		try {
			final Dialog saveDialog = new Dialog(cntxt);
			saveDialog.setTitle("Save Game");
			saveDialog.setContentView(R.layout.save_confirmation_dialog);

			TextView tv = (TextView) saveDialog
					.findViewById(R.id.save_confirmation_msg);
			tv.setText(gameState.getDisplayvalue());

			Button saveNow = (Button) saveDialog.findViewById(R.id.saveNow);
			saveNow.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					DatabaseHandler db = new DatabaseHandler(cntxt);
					db.saveGame(gameState);
					saveDialog.dismiss();
				}
			});
			Button saveCancel = (Button) saveDialog
					.findViewById(R.id.cancelSave);
			saveCancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					saveDialog.dismiss();
				}
			});
			saveDialog.show();
		} catch (Exception e) {
			System.out.println("Exceptionn : " + e.getMessage());
		}

	}

	/*
	 * save button right control panel
	 */
	private boolean isSavetouched(Point touchPoint) {
		if (touchPoint.x >= saveCenter.x
				&& touchPoint.x <= saveCenter.x + controllButtonSize)
			if (touchPoint.y >= saveCenter.y
					&& touchPoint.y <= saveCenter.y + controllButtonSize)
				return true;
		return false;
	}

	// for pause store current game state
	private void updatePlayPause() {
		if (isPlay) {
			stopTimer();
		} else {
			countinueTimer();
		}

		GameDisplay.gameState = new SaveAndLoad();
		GameDisplay.gameState.setNoOfTile(gameBoardSize);
		GameDisplay.gameState.setMode(Game.gameMode);
		GameDisplay.gameState.setScore(currentScore);
		GameDisplay.gameState.setTime(totalTime);
		GameDisplay.gameState.boardToString(gameBoard);

		Intent intent = new Intent(cntxt, Resume_Pause.class);
		cntxt.startActivity(intent);

		invalidate();
	}

	// check for touch for right control panel
	private boolean isPlayPauseTouched(Point touchPoint) {
		if (touchPoint.x >= playPauseCenter.x
				&& touchPoint.x <= playPauseCenter.x + controllButtonSize)
			if (touchPoint.y >= playPauseCenter.y
					&& touchPoint.y <= playPauseCenter.y + controllButtonSize)
				return true;

		return false;
	}

	// for tutorial generate message
	private void generateMessage() {
		if (msgNo == 5 && !tileMerged)
			return;
		msgNo++;
		invalidate();
	}

	private void initInfo() {
		// reset game board
		for (int i = 0; i < gameBoardSize; i++)
			for (int j = 0; j < gameBoardSize; j++) {
				gameBoard[i][j] = 0;
			}

		// Initialize media player
		tileGenClickPlayer = MediaPlayer.create(cntxt, R.raw.flip_2);
		tileGenClickPlayer.setVolume(100, 100);

		// get game data from database
		DatabaseHandler db = new DatabaseHandler(cntxt);
		screenHeight = db.getGameDataScreenHeight();
		screenWidth = db.getGameDataScreenWidth();
		targetTile = db.getGameDataMaxTile();
		isMusic = db.getGameDataMusic();

		tileWidth = (int) (screenWidth * 0.9) / gameBoardSize;
		tileHeight = tileWidth;
		tileSepration = (int) (screenWidth * 0.1) / (gameBoardSize + 1);

		gameBoardWidth = screenWidth;

		// all game tile position
		int x = tileSepration;
		int y = (int) ((screenHeight - gameBoardWidth) / 2);
		for (int i = 0; i < gameBoardSize; i++) {
			for (int j = 0; j < gameBoardSize; j++) {
				gameBoardPos[i][j] = new Point(x, y);

				x += tileWidth + tileSepration;
			}
			x = tileSepration;
			y += tileHeight + tileSepration;

		}

		// tutorial display center
		int temp = y;
		tutorialCenter = new Point(screenWidth / 2, y + (8 * tileSepration));

		// scoreboard header position
		y = (int) ((screenHeight - gameBoardWidth) / 2);
		headerButtonWidth = screenWidth / 2;
		headerButtonHeight = (int) (y * 0.5);

		timeDisplayCenter = new Point((screenWidth / 2)
				- (headerButtonWidth / 2), (int) (y * 0.6));

		scoreDisplayCenter = new Point(timeDisplayCenter.x + headerButtonWidth,
				timeDisplayCenter.y);

		controllButtonSize = (int) (headerButtonHeight * 0.75);

		// right control panel button position
		playPauseCenter = new Point(screenWidth - tileSepration
				- (int) (controllButtonSize * 1), temp);
		saveCenter = new Point(playPauseCenter.x - tileSepration
				- (int) (controllButtonSize * 1), playPauseCenter.y);

		// design typeface
		Typeface typeFace = Typeface.createFromAsset(getContext().getAssets(),
				"DS_DIGII.TTF");

		timeScoreTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG
				| Paint.LINEAR_TEXT_FLAG);
		timeScoreTextPaint.setStyle(Paint.Style.FILL);
		timeScoreTextPaint.setTextAlign(Paint.Align.CENTER);
		timeScoreTextPaint.setColor(Color.WHITE);
		timeScoreTextPaint.setTypeface(typeFace);
		timeScoreTextPaint.setTextSize((float) (headerButtonHeight * 0.75));

		smallTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG
				| Paint.LINEAR_TEXT_FLAG);
		smallTextPaint.setStyle(Paint.Style.FILL);
		smallTextPaint.setTextAlign(Paint.Align.CENTER);
		smallTextPaint.setColor(Color.DKGRAY);
		smallTextPaint.setTypeface(typeFace);
		smallTextPaint.setTextSize((float) (headerButtonHeight * 0.28));

		typeFace = Typeface.createFromAsset(getContext().getAssets(),
				"Gabriola.ttf");
		tutorialTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG
				| Paint.LINEAR_TEXT_FLAG);
		tutorialTextPaint.setStyle(Paint.Style.FILL);
		tutorialTextPaint.setTextAlign(Paint.Align.CENTER);
		tutorialTextPaint.setColor(Color.DKGRAY);
		tutorialTextPaint.setTypeface(typeFace);
		tutorialTextPaint.setTextSize((float) (headerButtonHeight * 0.32));

		// Initialize game board
		generateBitmaps();
		if (Game.tutorialEnabled) {
			gameBoard[1][2] = 2;
			gameBoard[2][2] = 2;
		} else {
			generateRandomTile();
			generateRandomTile();
		}

		// reset score and time
		currentScore = 0;
		totalTime = 0;

		// start timer thread
		timerThread = new Thread(this);
		timerThread.start();

		// game resumed
		if (GameDisplay.startedAfterPause) {
			resumeGameState();
		}
		// game loaded
		else if (LoadAndSave.loadGame) {
			loadGameState();
			System.out.println("saved game loaded");
		}
	}

	private void resumeGameState() {
		isPlay = true;
		GameDisplay.startedAfterPause = false;
		System.out.println("" + GameDisplay.gameState.getDisplayvalue());
		currentScore = GameDisplay.gameState.getScore();
		totalTime = GameDisplay.gameState.getTime();

		for (int i = 0; i < GameDisplay.gameState.getNoOfTile(); i++) {
			System.arraycopy(GameDisplay.gameState.stringToBoard()[i], 0,
					gameBoard[i], 0, gameBoard[i].length);
			System.out.println("resumed game copied");
		}
	}

	private void loadGameState() {
		LoadAndSave.loadGame = false;
		System.out.println("" + GameDisplay.gameState.getDisplayvalue());
		currentScore = GameDisplay.gameState.getScore();
		totalTime = GameDisplay.gameState.getTime();

		for (int i = 0; i < GameDisplay.gameState.getNoOfTile(); i++) {
			System.arraycopy(GameDisplay.gameState.stringToBoard()[i], 0,
					gameBoard[i], 0, gameBoard[i].length);
			System.out.println("saved game copied");
		}

	}

	private void swipeLeftNew() {
		if (!isPlay)
			return;
		// Toast.makeText(cntxt, "Left", Toast.LENGTH_SHORT).show();
		currentSwipe = SWIPE_LEFT;
		for (int k = 0; k < gameBoardSize - 1; k++) {
			for (int i = 0; i < gameBoardSize; i++) {
				for (int j = 0; j < gameBoardSize - 1; j++) {
					if (gameBoard[i][j] == 0) {
						gameBoard[i][j] = gameBoard[i][j + 1];
						gameBoard[i][j + 1] = 0;
						if (prevSwipe != currentSwipe) {
						}
					}
				}
			}
		}
		for (int i = 0; i < gameBoardSize; i++) {
			for (int j = 0; j < gameBoardSize - 1; j++) {
				if (gameBoard[i][j] == gameBoard[i][j + 1]
						&& gameBoard[i][j] != 0) {
					gameBoard[i][j] += gameBoard[i][j];
					playTileGenClick();
					if (gameBoard[i][j] >= targetTile) {
						gameWonFlag = true;
						gameEndFlag = true;
					}

					currentScore += gameBoard[i][j];
					gameBoard[i][j + 1] = 0;
					tileMerged = true;

					for (int k = j + 1; k < gameBoardSize - 1; k++) {
						gameBoard[i][k] = gameBoard[i][k + 1];
					}
					gameBoard[i][gameBoardSize - 1] = 0;
				}
			}
		}
		if (checkForTileGeneration()) {
			generateRandomTile();
			// playTileGenClick();
		}

		if (isGameEnded() || gameWonFlag) {
			gameEndFlag = true;
		}
		invalidate();
		prevSwipe = currentSwipe;
		updatePrevGameBoard();
	}

	private void swipeRightNew() {
		if (!isPlay)
			return;
		currentSwipe = SWIPE_RIGHT;
		for (int k = 0; k < gameBoardSize - 1; k++) {
			for (int i = 0; i < gameBoardSize; i++) {
				for (int j = gameBoardSize - 1; j > 0; j--) {
					if (gameBoard[i][j] == 0) {
						gameBoard[i][j] = gameBoard[i][j - 1];
						gameBoard[i][j - 1] = 0;
						if (prevSwipe != currentSwipe) {
						}
					}
				}
			}
		}
		for (int i = 0; i < gameBoardSize; i++) {
			for (int j = gameBoardSize - 1; j > 0; j--) {
				if (gameBoard[i][j] == gameBoard[i][j - 1]
						&& gameBoard[i][j] != 0) {
					gameBoard[i][j] += gameBoard[i][j];
					playTileGenClick();
					if (gameBoard[i][j] >= targetTile) {
						gameWonFlag = true;
						gameEndFlag = true;
					}
					currentScore += gameBoard[i][j];
					tileMerged = true;
					gameBoard[i][j - 1] = 0;

					for (int k = j - 1; k > 0; k--) {
						gameBoard[i][k] = gameBoard[i][k - 1];
					}
					gameBoard[i][0] = 0;
				}
			}
		}
		if (checkForTileGeneration()) {
			generateRandomTile();
			// playTileGenClick();
		}

		if (isGameEnded() || gameWonFlag) {
			gameEndFlag = true;
		}
		invalidate();
		prevSwipe = currentSwipe;
		updatePrevGameBoard();
	}

	private void swipeUpNew() {
		if (!isPlay)
			return;
		currentSwipe = SWIPE_UP;
		for (int k = 0; k < gameBoardSize - 1; k++) {
			for (int i = 0; i < gameBoardSize; i++) {
				for (int j = 0; j < gameBoardSize - 1; j++) {
					if (gameBoard[j][i] == 0) {
						gameBoard[j][i] = gameBoard[j + 1][i];
						gameBoard[j + 1][i] = 0;
						if (prevSwipe != currentSwipe) {
						}
					}
				}
			}
		}
		for (int i = 0; i < gameBoardSize; i++) {
			for (int j = 0; j < gameBoardSize - 1; j++) {
				if (gameBoard[j][i] == gameBoard[j + 1][i]
						&& gameBoard[j][i] != 0) {
					gameBoard[j][i] += gameBoard[j][i];
					playTileGenClick();
					if (gameBoard[i][j] >= targetTile) {
						gameWonFlag = true;
						gameEndFlag = true;
					}
					currentScore += gameBoard[j][i];
					tileMerged = true;
					gameBoard[j + 1][i] = 0;
					for (int k = j + 1; k < gameBoardSize - 1; k++) {
						gameBoard[k][i] = gameBoard[k + 1][i];
					}
					gameBoard[gameBoardSize - 1][i] = 0;
				}
			}
		}
		if (checkForTileGeneration()) {
			generateRandomTile();
			// playTileGenClick();
		}

		if (isGameEnded() || gameWonFlag) {
			gameEndFlag = true;
		}
		invalidate();
		prevSwipe = currentSwipe;
		updatePrevGameBoard();
	}

	private void swipeDownNew() {
		if (!isPlay)
			return;
		currentSwipe = SWIPE_DOWN;
		for (int k = 0; k < gameBoardSize - 1; k++) {
			for (int i = 0; i < gameBoardSize; i++) {
				for (int j = gameBoardSize - 1; j > 0; j--) {
					if (gameBoard[j][i] == 0) {
						gameBoard[j][i] = gameBoard[j - 1][i];
						gameBoard[j - 1][i] = 0;
						if (prevSwipe != currentSwipe) {
						}
					}
				}
			}
		}
		for (int i = 0; i < gameBoardSize; i++) {
			for (int j = gameBoardSize - 1; j > 0; j--) {
				if (gameBoard[j][i] == gameBoard[j - 1][i]
						&& gameBoard[j][i] != 0) {
					gameBoard[j][i] += gameBoard[j][i];
					playTileGenClick();
					if (gameBoard[i][j] >= targetTile) {
						gameWonFlag = true;
						gameEndFlag = true;
					}
					currentScore += gameBoard[j][i];
					tileMerged = true;
					gameBoard[j - 1][i] = 0;
					for (int k = j - 1; k > 0; k--) {
						gameBoard[k][i] = gameBoard[k - 1][i];
					}
					gameBoard[0][i] = 0;
				}
			}
		}
		if (checkForTileGeneration()) {
			generateRandomTile();
			// playTileGenClick();
		}

		if (isGameEnded() || gameWonFlag) {
			gameEndFlag = true;
		}
		invalidate();
		prevSwipe = currentSwipe;
		updatePrevGameBoard();
	}

	private void updatePrevGameBoard() {
		for (int i = 0; i < gameBoard.length; i++)
			System.arraycopy(gameBoard[i], 0, prevGameBoard[i], 0,
					gameBoard.length);
	}

	private boolean checkForTileGeneration() {
		for (int i = 0; i < gameBoard.length; i++) {
			for (int j = 0; j < gameBoard.length; j++) {
				if (gameBoard[i][j] != prevGameBoard[i][j])
					return true;
			}
		}
		return false;
	}

	private boolean isGameEnded() {
		// System.out.println("tile count : " + countTile());
		// display();
		if (countTile() == ((int) Math.pow(gameBoardSize, 2)))
			if (!isRowMovePosible() && !isColoumMovePosible()) {
				return true;
			}
		return false;
	}

	private boolean isRowMovePosible() {
		for (int i = 0; i < gameBoardSize; i++)
			for (int j = 0; j < gameBoardSize - 1; j++) {
				if (gameBoard[i][j] == gameBoard[i][j + 1])
					return true;
			}
		return false;
	}

	private boolean isColoumMovePosible() {
		for (int i = 0; i < gameBoardSize; i++)
			for (int j = 0; j < gameBoardSize - 1; j++) {
				if (gameBoard[j][i] == gameBoard[j + 1][i])
					return true;
			}
		return false;
	}

	private boolean generateRandomTile() {

		rndMove = new Random(System.currentTimeMillis());
		int rnd = rndMove.nextInt((int) Math.pow(gameBoardSize, 2));
		while (true) {
			int tile = rndMove.nextFloat() < 0.9f ? 2 : 4;

			if (gameBoard[rnd / gameBoardSize][rnd % gameBoardSize] == 0) {
				gameBoard[rnd / gameBoardSize][rnd % gameBoardSize] = tile;
				return true;
			} else {
				rnd = rndMove.nextInt((int) Math.pow(gameBoardSize, 2));
			}
		}
	}

	private void checkForBestHighScore() {

		for (int i = 0; i < Game.data[gameBoardSize - 3].normalBestScore.length; i++)
			if (Game.data[gameBoardSize - 3].normalBestScore[i] == currentScore)
				return;

		// add current score
		Game.data[gameBoardSize - 3].normalBestScore[10] = currentScore;
		System.out.println("before soriong : ");
		for (int i = 0; i < Game.data[gameBoardSize - 3].normalBestScore.length; i++)
			System.out.println(""
					+ Game.data[gameBoardSize - 3].normalBestScore[i]);
		// sort
		Arrays.sort(Game.data[gameBoardSize - 3].normalBestScore);

		for (int i = 0; i < Game.data[gameBoardSize - 3].normalBestScore.length / 2; i++) {
			int temp = Game.data[gameBoardSize - 3].normalBestScore[i];
			Game.data[gameBoardSize - 3].normalBestScore[i] = Game.data[gameBoardSize - 3].normalBestScore[Game.data[gameBoardSize - 3].normalBestScore.length
					- 1 - i];
			Game.data[gameBoardSize - 3].normalBestScore[Game.data[gameBoardSize - 3].normalBestScore.length
					- 1 - i] = temp;
		}

		System.out.println("after soriong : ");
		for (int i = 0; i < Game.data[gameBoardSize - 3].normalBestScore.length; i++)
			System.out.println(""
					+ Game.data[gameBoardSize - 3].normalBestScore[i]);

		DatabaseHandler db = new DatabaseHandler(cntxt);
		db.updateDataBase(Game.data);
	}

	private int countTile() {
		int count = 0;
		for (int i = 0; i < gameBoardSize; i++) {
			for (int j = 0; j < gameBoardSize; j++) {
				if (gameBoard[i][j] != 0) {
					count++;
				}
			}
		}
		return count;
	}

	// private void display() {
	// for (int i = 0; i < gameBoardSize; i++) {
	// for (int j = 0; j < gameBoardSize; j++) {
	// System.out.print("" + gameBoard[i][j] + "    ");
	// }
	// System.out.println("");
	// }
	// System.out.println("");
	// }

	private void generateBitmaps() {

		bitmapBkg = BitmapFactory
				.decodeResource(getResources(), R.drawable.bkg);
		bitmapBkg = Bitmap.createScaledBitmap(bitmapBkg, screenWidth,
				screenHeight, true);

		bitmapEmpty = BitmapFactory.decodeResource(getResources(),
				R.drawable.empty);
		bitmapEmpty = Bitmap.createScaledBitmap(bitmapEmpty, tileWidth,
				tileHeight, true);

		bitmapScore = BitmapFactory.decodeResource(getResources(),
				R.drawable.time);
		bitmapScore = Bitmap.createScaledBitmap(bitmapScore, headerButtonWidth,
				headerButtonHeight, true);
		scoreDisplay = getLeftTop(bitmapScore, scoreDisplayCenter);

		bitmapTime = BitmapFactory.decodeResource(getResources(),
				R.drawable.time);
		bitmapTime = Bitmap.createScaledBitmap(bitmapTime, headerButtonWidth,
				headerButtonHeight, true);
		timeDisplay = getLeftTop(bitmapTime, timeDisplayCenter);

		bitmapTile_2 = BitmapFactory.decodeResource(getResources(),
				R.drawable.btn_2);
		bitmapTile_2 = Bitmap.createScaledBitmap(bitmapTile_2, tileWidth,
				tileHeight, true);

		bitmapTile_4 = BitmapFactory.decodeResource(getResources(),
				R.drawable.btn_4);
		bitmapTile_4 = Bitmap.createScaledBitmap(bitmapTile_4, tileWidth,
				tileHeight, true);

		bitmapTile_8 = BitmapFactory.decodeResource(getResources(),
				R.drawable.btn_8);
		bitmapTile_8 = Bitmap.createScaledBitmap(bitmapTile_8, tileWidth,
				tileHeight, true);

		bitmapTile_16 = BitmapFactory.decodeResource(getResources(),
				R.drawable.btn_16);
		bitmapTile_16 = Bitmap.createScaledBitmap(bitmapTile_16, tileWidth,
				tileHeight, true);

		bitmapTile_32 = BitmapFactory.decodeResource(getResources(),
				R.drawable.btn_32);
		bitmapTile_32 = Bitmap.createScaledBitmap(bitmapTile_32, tileWidth,
				tileHeight, true);

		bitmapTile_64 = BitmapFactory.decodeResource(getResources(),
				R.drawable.btn_64);
		bitmapTile_64 = Bitmap.createScaledBitmap(bitmapTile_64, tileWidth,
				tileHeight, true);

		bitmapTile_128 = BitmapFactory.decodeResource(getResources(),
				R.drawable.btn_128);
		bitmapTile_128 = Bitmap.createScaledBitmap(bitmapTile_128, tileWidth,
				tileHeight, true);

		bitmapTile_256 = BitmapFactory.decodeResource(getResources(),
				R.drawable.btn_256);
		bitmapTile_256 = Bitmap.createScaledBitmap(bitmapTile_256, tileWidth,
				tileHeight, true);

		bitmapTile_512 = BitmapFactory.decodeResource(getResources(),
				R.drawable.btn_512);
		bitmapTile_512 = Bitmap.createScaledBitmap(bitmapTile_512, tileWidth,
				tileHeight, true);

		bitmapTile_1024 = BitmapFactory.decodeResource(getResources(),
				R.drawable.btn_1024);
		bitmapTile_1024 = Bitmap.createScaledBitmap(bitmapTile_1024, tileWidth,
				tileHeight, true);

		bitmapTile_2048 = BitmapFactory.decodeResource(getResources(),
				R.drawable.btn_2048);
		bitmapTile_2048 = Bitmap.createScaledBitmap(bitmapTile_2048, tileWidth,
				tileHeight, true);

		bitmapTile_4096 = BitmapFactory.decodeResource(getResources(),
				R.drawable.btn_4096);
		bitmapTile_4096 = Bitmap.createScaledBitmap(bitmapTile_4096, tileWidth,
				tileHeight, true);

		bitmapPause = BitmapFactory.decodeResource(getResources(),
				R.drawable.pause);
		bitmapPause = Bitmap.createScaledBitmap(bitmapPause,
				controllButtonSize, controllButtonSize, true);

		bitmapSave = BitmapFactory.decodeResource(getResources(),
				R.drawable.save);
		bitmapSave = Bitmap.createScaledBitmap(bitmapSave, controllButtonSize,
				controllButtonSize, true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawBitmap(bitmapBkg, 0, 0, null);

		canvas.drawBitmap(bitmapScore, scoreDisplay.x, scoreDisplay.y, null);
		canvas.drawBitmap(bitmapTime, timeDisplay.x, timeDisplay.y, null);

		Bitmap bitmap = bitmapEmpty;

		for (int i = 0; i < gameBoardSize; i++)
			for (int j = 0; j < gameBoardSize; j++) {

				bitmap = getBitmap(gameBoard[i][j]);

				canvas.drawBitmap(bitmap, gameBoardPos[i][j].x,
						gameBoardPos[i][j].y, null);
			}

		// Timer Display
		Point pnt = getTextBound(getString(getTime()), timeDisplayCenter,
				timeScoreTextPaint);
		canvas.drawText(getString(getTime()), pnt.x, pnt.y, timeScoreTextPaint);

		pnt = getTextBound("Time", timeDisplayCenter, smallTextPaint);
		canvas.drawText("Time", pnt.x,
				(float) (pnt.y - (headerButtonHeight * 0.6)), smallTextPaint);

		// Current Display
		pnt = getTextBound(String.valueOf(currentScore), scoreDisplayCenter,
				timeScoreTextPaint);
		canvas.drawText(String.valueOf(currentScore), pnt.x, pnt.y,
				timeScoreTextPaint);

		pnt = getTextBound("Score", scoreDisplayCenter, smallTextPaint);
		canvas.drawText("Score", pnt.x,
				(float) (pnt.y - (headerButtonHeight * 0.6)), smallTextPaint);

		if (Game.tutorialEnabled && msgNo < tutorialMsg.length) {
			pnt = getTextBound(tutorialMsg[msgNo], tutorialCenter,
					tutorialTextPaint);
			canvas.drawText(tutorialMsg[msgNo], pnt.x, pnt.y, tutorialTextPaint);
		}

		canvas.drawBitmap(bitmapSave, saveCenter.x, saveCenter.y, null);
		canvas.drawBitmap(bitmapPause, playPauseCenter.x, playPauseCenter.y,
				null);

		if (gameEndFlag || gameWonFlag) {
			if (!gameWonFlag) {
				msgForHighScoreActivity = "No more Move possible";
			} else {
				msgForHighScoreActivity = "You Win!";
			}
			tutorialTextPaint.setTextSize((float) (headerButtonHeight * 0.6));
			pnt = getTextBound(msgForHighScoreActivity, tutorialCenter,
					tutorialTextPaint);
			canvas.drawText(msgForHighScoreActivity, pnt.x, pnt.y,
					tutorialTextPaint);
		}

		if (gameEndFlag || gameWonFlag) {
			stopTimer();
			if (!gameWonFlag) {
				msgForHighScoreActivity = "No more Move possible";
			} else {
				msgForHighScoreActivity = "You Win!";
			}

			pnt = getTextBound(msgForHighScoreActivity, tutorialCenter,
					tutorialTextPaint);
			canvas.drawText(msgForHighScoreActivity, pnt.x, pnt.y,
					tutorialTextPaint);

			if (threadSyncFlag) {
				try {
					Thread.sleep(2500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				checkForBestHighScore();
				Intent intent = new Intent(cntxt, HighScore.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				cntxt.startActivity(intent);
				return;
			}
			threadSyncFlag = true;
			invalidate();
		}
	}

	private Point getTextBound(String string, Point point, Paint paint) {
		Rect bounds = new Rect();
		paint.getTextBounds(string, 0, string.length(), bounds);
		return new Point(point.x, point.y + (bounds.height() / 2));
	}

	private Point getLeftTop(Bitmap bmp, Point center) {
		Point p = new Point();
		p.x = center.x - bmp.getWidth() / 2;
		p.y = center.y - bmp.getHeight() / 2;
		return p;
	}

	private Bitmap getBitmap(int value) {
		Bitmap bitmap;

		switch (value) {

		case 2:
			bitmap = bitmapTile_2;
			break;

		case 4:
			bitmap = bitmapTile_4;
			break;

		case 8:
			bitmap = bitmapTile_8;
			break;

		case 16:
			bitmap = bitmapTile_16;
			break;

		case 32:
			bitmap = bitmapTile_32;
			break;

		case 64:
			bitmap = bitmapTile_64;
			break;

		case 128:
			bitmap = bitmapTile_128;
			break;

		case 256:
			bitmap = bitmapTile_256;
			break;

		case 512:
			bitmap = bitmapTile_512;
			break;

		case 1024:
			bitmap = bitmapTile_1024;
			break;

		case 2048:
			bitmap = bitmapTile_2048;
			break;

		case 4096:
			bitmap = bitmapTile_4096;
			break;

		default:
			bitmap = bitmapEmpty;
			break;

		}
		return bitmap;
	}

	public synchronized int getTime() {
		return totalTime;
	}

	public synchronized void incrementTime() {
		totalTime++;
		postInvalidate();
	}

	@Override
	public void run() {
		while (true) {
			if (!timerThreadActive)
				continue;
			incrementTime();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void stopTimer() {
		timerThreadActive = false;
	}

	public void countinueTimer() {
		timerThreadActive = true;
	}

	public static String getString(int time) {
		String str = "";

		int sec = time % 60;
		int min = time / 60;
		if (min < 10)
			str += "0" + String.valueOf(min) + ":";
		else
			str += String.valueOf(min) + ":";

		if (sec < 10)
			str += "0" + String.valueOf(sec);
		else
			str += String.valueOf(sec);

		return str;
	}

	private void playTileGenClick() {
		if (!isMusic || tileGenClickPlayer.isPlaying())
			return;
		tileGenClickPlayer.start();
	}
}

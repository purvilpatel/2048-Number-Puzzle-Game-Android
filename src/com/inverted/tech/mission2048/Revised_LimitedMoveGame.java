package com.inverted.tech.mission2048;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.inverted.tech.mission2048.R;
import com.inverted.tech.mission2048.R.drawable;
import com.inverted.tech.mission2048.R.id;
import com.inverted.tech.mission2048.R.layout;
import com.inverted.tech.mission2048.databaseConnection.DatabaseHandler;
import com.inverted.tech.mission2048.supportClass.OnSwipeTouchListener;

@SuppressLint("DrawAllocation")
public class Revised_LimitedMoveGame extends View {

	public static int gameBoardSize;
	public static int gameBoard[][];
	private Point gameBoardPos[][];
	private int prevGameBoard[][];

	private Point scoreDisplay;
	private Point timeDisplay;
	private Point scoreDisplayCenter;
	private Point timeDisplayCenter;
	private Point tutorialCenter;

	private int tileHeight;
	private int tileWidth;
	private int tileSepration;
	private int gameBoardWidth;
	private int headerButtonWidth;
	private int headerButtonHeight;

	private Paint timeScoreTextPaint;
	private Paint smallTextPaint;
	private Paint tutorialTextPaint;

	private Context mContext;

	private Bitmap bitmapBkg;
	private Bitmap bitmapEmpty;
	private Bitmap bitmapTimeScoreHeader;

	private Bitmap achievementBitmap;
	private Bitmap nightModeBitmap;
	private Bitmap leaderBoardBitmap;
	private Bitmap resetBitmap;
	private Point achievementpoPoint;
	private Point nightModePoint;
	private Point leaderBoardPoint;
	private Point resetPoint;
	private int rightSidePanelIconSize;

	private Bitmap menuOptionBitmap;
	private Point footerMenuOptionPoint;

	private Point autoMovePoint;
	private Point boostersPoint;
	private Point autoMoveTextPoint;
	private Point boostersTextPoint;
	private Paint footerMenuTextPaint;
	private Point screenCapturePoint;
	private int footerMenuWidth;
	private Bitmap footerMenuBitmap;
	private Bitmap screenCaptureBitmap;

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
	private Bitmap bitmapTile_8192;
	private Bitmap bitmapTile_x2;
	private Bitmap bitmapTile_bomb;

	public static int currentScore;

	private boolean gameEndFlag = false;
	private boolean gameWonFlag = false;
	public static String msgForHighScoreActivity = "";

	private boolean threadSyncFlag = false;
	private boolean gameNightMode = false;
	private int screenWidth;
	private int screenHeight;

	private Random rndMove;
	final private int NO_SWIPE = -1;
	final private int SWIPE_LEFT = 1;
	final private int SWIPE_RIGHT = 2;
	final private int SWIPE_UP = 3;
	final private int SWIPE_DOWN = 4;
	private int prevSwipe = NO_SWIPE;
	private int currentSwipe;

	private int targetTile = 1024;
	private View rootView;
	private Typeface typeFace;

	private static final int MULTIPLY_BY_2_POWERUP = 22;
	private static final int BOMB_POWERUP = -1;
	private int noOfBoosterToProduce = 0;
	private int currentBooster;
	private boolean isNextTileBooster = false;
	private Dialog boosterDialog;
	private int move = 32;
	private boolean outOfMoveFlag;

	public Revised_LimitedMoveGame(final Context context) {
		super(context);
		this.mContext = context;

		DatabaseHandler db = new DatabaseHandler(context);
		gameBoardSize = db.getGameDataNoOfTile();
		gameBoard = new int[gameBoardSize][gameBoardSize];
		prevGameBoard = new int[gameBoardSize][gameBoardSize];
		gameBoardPos = new Point[gameBoardSize][gameBoardSize];

		initInfo();

		setOnTouchListener(new OnSwipeTouchListener(context) {
			public boolean onTap(MotionEvent me) {

				Point touchPoint = new Point((int) me.getX(), (int) me.getY());
				if (isNightModeTouched(touchPoint)) {
					changeNightMode();
					invalidate();
				} else if (isResetTouched(touchPoint)) {
					resetGame();
					invalidate();
				} else if (isAchievementTouched(touchPoint)) {
					showAchievement();
					invalidate();
				} else if (isLeaderBoardTouched(touchPoint)) {
					showLeaderBoard();
					invalidate();
				} else if (isFooterMenuOptionTouched(touchPoint)) {
					showFooterMenuOption();
					invalidate();
				} else if (isAutoMoveTouched(touchPoint)) {
					showAutoMove();
					invalidate();
				} else if (isBoostersTouched(touchPoint)) {
					showBoostersDialod();
					invalidate();
				} else if (isScreenCaptureBitmapMenuTouched(touchPoint)) {
					takeScreenShot();
					invalidate();
				}
				return true;
			}

			public boolean onSwipeTop() {
				if (gameEndFlag || gameWonFlag)
					return true;
				swipeUp();
				invalidate();
				return true;
			}

			public boolean onSwipeRight() {
				if (gameEndFlag || gameWonFlag)
					return true;
				swipeRight();
				invalidate();
				return true;
			}

			public boolean onSwipeLeft() {
				if (gameEndFlag || gameWonFlag)
					return true;
				swipeLeft();
				invalidate();
				return true;
			}

			public boolean onSwipeBottom() {
				if (gameEndFlag || gameWonFlag)
					return true;
				swipeDown();
				invalidate();
				return true;
			}
		});
	}

	private void initInfo() {
		for (int i = 0; i < gameBoardSize; i++)
			for (int j = 0; j < gameBoardSize; j++) {
				gameBoard[i][j] = 0;
			}

		DatabaseHandler db = new DatabaseHandler(mContext);
		screenHeight = db.getGameDataScreenHeight();
		screenWidth = db.getGameDataScreenWidth();
		db.getGameDataMaxTile();
		db.getGameDataMusic();

		tileWidth = (int) (screenWidth * 0.9) / gameBoardSize;
		tileHeight = tileWidth;
		tileSepration = (int) (screenWidth * 0.1) / (gameBoardSize + 1);

		gameBoardWidth = screenWidth;

		int x = tileSepration;
		int y = (int) ((screenHeight - gameBoardWidth) * 0.3);
		for (int i = 0; i < gameBoardSize; i++) {
			for (int j = 0; j < gameBoardSize; j++) {
				gameBoardPos[i][j] = new Point(x, y);
				x += tileWidth + tileSepration;
			}
			x = tileSepration;
			y += tileHeight + tileSepration;
		}

		y = (int) ((screenHeight - gameBoardWidth) / 2);
		headerButtonWidth = screenWidth / 4;
		headerButtonHeight = (int) (y * 0.3);

		timeDisplayCenter = new Point(headerButtonWidth
				- (headerButtonWidth / 2), (int) (y * 0.3));
		scoreDisplayCenter = new Point(
				(timeDisplayCenter.x + headerButtonWidth), timeDisplayCenter.y);

		// recalculate tile separation
		tileSepration = (int) (screenWidth * 0.1) / 5;
		rightSidePanelIconSize = (int) ((screenWidth
				- (scoreDisplayCenter.x + (headerButtonWidth / 2)) - (tileSepration * 3.2)) / 4);

		resetPoint = new Point((int) (scoreDisplayCenter.x
				+ (headerButtonWidth / 2) + (tileSepration * 0.8)),
				scoreDisplayCenter.y - (headerButtonHeight / 2));

		achievementpoPoint = new Point((int) (resetPoint.x
				+ rightSidePanelIconSize + (tileSepration * 0.8)), resetPoint.y);

		leaderBoardPoint = new Point((int) (achievementpoPoint.x
				+ rightSidePanelIconSize + (tileSepration * 0.8)), resetPoint.y);

		nightModePoint = new Point((int) (leaderBoardPoint.x
				+ rightSidePanelIconSize + (tileSepration * 0.8)), resetPoint.y);

		// menu option display point calculation
		footerMenuOptionPoint = new Point(tileSepration,
				(int) (screenHeight * 0.3 + gameBoardWidth * 0.7));
		screenCapturePoint = new Point(screenWidth
				- (rightSidePanelIconSize + tileSepration),
				footerMenuOptionPoint.y);

		// footer menu display point calculation
		// 6 * tileSepration for maintaining distance between display items
		footerMenuWidth = (screenWidth - footerMenuOptionPoint.x
				- (2 * rightSidePanelIconSize) - (4 * tileSepration)) / 2;
		autoMoveTextPoint = new Point(footerMenuOptionPoint.x
				+ rightSidePanelIconSize + tileSepration
				+ (footerMenuWidth / 2), footerMenuOptionPoint.y
				+ (rightSidePanelIconSize / 2));
		boostersTextPoint = new Point(autoMoveTextPoint.x + footerMenuWidth
				+ tileSepration, autoMoveTextPoint.y);

		// tutorial display center calculation
		tutorialCenter = new Point(screenWidth / 2, footerMenuOptionPoint.y
				+ rightSidePanelIconSize + (3 * tileSepration));

		typeFace = Typeface.createFromAsset(getContext().getAssets(),
				"MAIAN.TTF");
		// footer menu paint
		footerMenuTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG
				| Paint.LINEAR_TEXT_FLAG);
		footerMenuTextPaint.setStyle(Paint.Style.FILL);
		footerMenuTextPaint.setTextAlign(Paint.Align.CENTER);
		footerMenuTextPaint.setColor(Color.WHITE);
		footerMenuTextPaint.setTypeface(typeFace);
		footerMenuTextPaint
				.setTextSize((float) (rightSidePanelIconSize * 0.45));

		// header time-score paint
		typeFace = Typeface.createFromAsset(getContext().getAssets(),
				"DS_DIGII.TTF");
		timeScoreTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG
				| Paint.LINEAR_TEXT_FLAG);
		timeScoreTextPaint.setStyle(Paint.Style.FILL);
		timeScoreTextPaint.setTextAlign(Paint.Align.CENTER);
		timeScoreTextPaint.setColor(Color.WHITE);
		timeScoreTextPaint.setTypeface(typeFace);
		timeScoreTextPaint.setTextSize((float) (headerButtonHeight * 0.75));

		// small header title text time-score paint
		typeFace = Typeface.createFromAsset(getContext().getAssets(),
				"MAIAN.TTF");
		smallTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG
				| Paint.LINEAR_TEXT_FLAG);
		smallTextPaint.setStyle(Paint.Style.FILL);
		smallTextPaint.setTextAlign(Paint.Align.CENTER);
		smallTextPaint.setColor(Color.DKGRAY);
		smallTextPaint.setTypeface(typeFace);
		smallTextPaint.setTextSize((float) (headerButtonHeight * 0.28));

		// tutorial instruction and message paint
		typeFace = Typeface.createFromAsset(getContext().getAssets(),
				"MAIAN.TTF");
		tutorialTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG
				| Paint.LINEAR_TEXT_FLAG);
		tutorialTextPaint.setStyle(Paint.Style.FILL);
		tutorialTextPaint.setTextAlign(Paint.Align.CENTER);
		tutorialTextPaint.setColor(Color.DKGRAY);
		tutorialTextPaint.setTypeface(typeFace);
		tutorialTextPaint.setTextSize((float) (headerButtonHeight * 0.32));

		generateBitmaps();
		startGame();

		// game resumed
		if (GameDisplay.startedAfterPause) {
			resumeGameState();
		}
	}

	private void startGame() {
		if (gameBoardSize == 3) {
			move = 32;
			generateRandomTile();
			generateRandomTile();
		} else {
			move = 34;
			generateRandomTile();
			generateRandomTile();
			generateRandomTile();
			generateRandomTile();
		}

		currentScore = 0;
		gameEndFlag = false;
		gameWonFlag = false;
		msgForHighScoreActivity = "";
	}

	private void resumeGameState() {
		GameDisplay.startedAfterPause = false;
		System.out.println("" + GameDisplay.gameState.getDisplayvalue());
		currentScore = GameDisplay.gameState.getScore();
		move = GameDisplay.gameState.getTime();

		for (int i = 0; i < GameDisplay.gameState.getNoOfTile(); i++) {
			System.arraycopy(GameDisplay.gameState.stringToBoard()[i], 0,
					gameBoard[i], 0, gameBoard[i].length);
			System.out.println("resumed game copied");
		}
	}

	private boolean swipeLeft() {
		System.out.println("left called");
		boolean flag = false;
		currentSwipe = SWIPE_LEFT;
		fromStart: for (int k = 0; k < gameBoardSize - 1; k++) {
			for (int i = 0; i < gameBoardSize; i++) {
				for (int j = 0; j < gameBoardSize - 1; j++) {
					if (gameBoard[i][j] == -1) {
						bombFound(i, j, findMaxTile(i, j));
						k = 0;
						continue fromStart;
					}
					if (gameBoard[i][j] == MULTIPLY_BY_2_POWERUP && j != 0) {
						if (gameBoard[i][j - 1] != 0) {
							gameBoard[i][j - 1] *= 2;
							gameBoard[i][j] = 0;
						}
					}
					if (gameBoard[i][j] == 0) {
						gameBoard[i][j] = gameBoard[i][j + 1];
						gameBoard[i][j + 1] = 0;
						if (prevSwipe != currentSwipe) {
						}
					}
				}
			}
		}
		System.out.println("left 1");
		for (int i = 0; i < gameBoardSize; i++) {
			for (int j = 0; j < gameBoardSize - 1; j++) {
				if (gameBoard[i][j] == gameBoard[i][j + 1]
						&& gameBoard[i][j] != 0) {
					gameBoard[i][j] += gameBoard[i][j];
					// playTileGenClick();
					// playTileGenClick();
					if (gameBoard[i][j] >= targetTile) {
						gameWonFlag = true;
						gameEndFlag = true;
					}

					currentScore += gameBoard[i][j];
					gameBoard[i][j + 1] = 0;
					// tileMerged = true;

					for (int k = j + 1; k < gameBoardSize - 1; k++) {
						gameBoard[i][k] = gameBoard[i][k + 1];
					}
					gameBoard[i][gameBoardSize - 1] = 0;
				}
			}
		}
		System.out.println("left 2");
		if (checkForTileGeneration()) {
			generateRandomTile();
			flag = true;
		}
		System.out.println("left 3");

		if (isGameEnded() || gameWonFlag) {
			gameEndFlag = true;
		}
		System.out.println("left 4");

		prevSwipe = currentSwipe;
		updatePrevGameBoard();
		System.out.println("left 5");
		return flag;
	}

	private boolean swipeRight() {
		System.out.println("right called");
		boolean flag = false;
		currentSwipe = SWIPE_RIGHT;
		fromStart: for (int k = 0; k < gameBoardSize - 1; k++) {
			for (int i = 0; i < gameBoardSize; i++) {
				for (int j = gameBoardSize - 1; j > 0; j--) {
					if (gameBoard[i][j] == -1) {
						bombFound(i, j, findMaxTile(i, j));
						k = 0;
						continue fromStart;
					}
					if (gameBoard[i][j] == MULTIPLY_BY_2_POWERUP
							&& j != gameBoardSize - 1) {
						if (gameBoard[i][j + 1] != 0) {
							gameBoard[i][j + 1] += gameBoard[i][j + 1];
							gameBoard[i][j] = 0;
						}
					}
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
					// playTileGenClick();
					// playTileGenClick();
					if (gameBoard[i][j] >= targetTile) {
						gameWonFlag = true;
						gameEndFlag = true;
					}
					currentScore += gameBoard[i][j];
					// tileMerged = true;
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
			flag = true;
		}

		if (isGameEnded() || gameWonFlag) {
			gameEndFlag = true;
		}

		prevSwipe = currentSwipe;
		updatePrevGameBoard();
		return flag;
	}

	private boolean swipeUp() {
		System.out.println("up called");
		boolean flag = false;
		currentSwipe = SWIPE_UP;
		fromStart: for (int k = 0; k < gameBoardSize - 1; k++) {
			for (int i = 0; i < gameBoardSize; i++) {
				for (int j = 0; j < gameBoardSize - 1; j++) {
					if (gameBoard[i][j] == -1) {
						bombFound(i, j, findMaxTile(i, j));
						k = 0;
						continue fromStart;
					}
					if (gameBoard[j][i] == MULTIPLY_BY_2_POWERUP && j != 0) {
						if (gameBoard[j - 1][i] != 0) {
							gameBoard[j - 1][i] += gameBoard[j - 1][i];
							gameBoard[j][i] = 0;
						}
					}
					if (gameBoard[j][i] == 0) {
						gameBoard[j][i] = gameBoard[j + 1][i];
						gameBoard[j + 1][i] = 0;
					}
				}
			}
		}
		for (int i = 0; i < gameBoardSize; i++) {
			for (int j = 0; j < gameBoardSize - 1; j++) {
				if (gameBoard[j][i] == gameBoard[j + 1][i]
						&& gameBoard[j][i] != 0) {
					gameBoard[j][i] += gameBoard[j][i];
					// playTileGenClick();
					// playTileGenClick();
					if (gameBoard[i][j] >= targetTile) {
						gameWonFlag = true;
						gameEndFlag = true;
					}
					currentScore += gameBoard[j][i];
					// tileMerged = true;
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
			flag = true;
		}

		if (isGameEnded() || gameWonFlag) {
			gameEndFlag = true;
		}

		prevSwipe = currentSwipe;
		updatePrevGameBoard();
		return flag;
	}

	private boolean swipeDown() {
		System.out.println("down called");
		boolean flag = false;
		currentSwipe = SWIPE_DOWN;
		fromStart: for (int k = 0; k < gameBoardSize - 1; k++) {
			for (int i = 0; i < gameBoardSize; i++) {
				for (int j = gameBoardSize - 1; j > 0; j--) {
					if (gameBoard[i][j] == -1) {
						bombFound(i, j, findMaxTile(i, j));
						k = 0;
						continue fromStart;
					}
					if (gameBoard[j][i] == MULTIPLY_BY_2_POWERUP
							&& j != gameBoardSize - 1) {
						if (gameBoard[j + 1][i] != 0) {
							gameBoard[j + 1][i] += gameBoard[j + 1][i];
							gameBoard[j][i] = 0;
						}
					}
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
					// playTileGenClick();
					// playTileGenClick();
					if (gameBoard[i][j] >= targetTile) {
						gameWonFlag = true;
						gameEndFlag = true;
					}
					currentScore += gameBoard[j][i];
					// tileMerged = true;
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
			flag = true;
		}

		if (isGameEnded() || gameWonFlag) {
			gameEndFlag = true;
		}

		prevSwipe = currentSwipe;
		updatePrevGameBoard();
		return flag;
	}

	private void incrementMoveCount() {
		move--;
		if (move == 0) {
			gameEndFlag = true;
			outOfMoveFlag = true;
		}
	}

	private void bombFound(int i, int j, int max) {
		if (i > 0 && j > 0) {
			if (gameBoard[i - 1][j - 1] != max)
				gameBoard[i - 1][j - 1] = 0;
		}
		if (i > 0) {
			if (gameBoard[i - 1][j] != max)
				gameBoard[i - 1][j] = 0;
		}
		if (i > 0 && j < gameBoardSize - 1) {
			if (gameBoard[i - 1][j + 1] != max)
				gameBoard[i - 1][j + 1] = 0;
		}

		if (j > 0) {
			if (gameBoard[i][j - 1] != max)
				gameBoard[i][j - 1] = 0;
		}
		gameBoard[i][j] = 0;
		if (j < gameBoardSize - 1) {
			if (gameBoard[i][j + 1] != max)
				gameBoard[i][j + 1] = 0;
		}

		if (i < gameBoardSize - 1 && j > 0) {
			if (gameBoard[i + 1][j - 1] != max)
				gameBoard[i + 1][j - 1] = 0;
		}
		if (i < gameBoardSize - 1) {
			if (gameBoard[i + 1][j] != max)
				gameBoard[i + 1][j] = 0;
		}
		if (i < gameBoardSize - 1 && j < gameBoardSize - 1) {
			if (gameBoard[i + 1][j + 1] != max)
				gameBoard[i + 1][j + 1] = 0;
		}
	}

	private int findMaxTile(int i, int j) {
		int max = gameBoard[0][0];
		if (i > 0 && j > 0) {
			if (gameBoard[i - 1][j - 1] > max)
				max = gameBoard[i - 1][j - 1];
		}
		if (i > 0) {
			if (gameBoard[i - 1][j] > max)
				max = gameBoard[i - 1][j];
		}
		if (i > 0 && j < gameBoardSize - 1) {
			if (gameBoard[i - 1][j + 1] > max)
				max = gameBoard[i - 1][j + 1];
		}

		if (j > 0) {
			if (gameBoard[i][j - 1] > max)
				max = gameBoard[i][j - 1];
		}
		if (j < gameBoardSize - 1) {
			if (gameBoard[i][j + 1] > max)
				max = gameBoard[i][j + 1];
		}

		if (i < gameBoardSize - 1 && j > 0) {
			if (gameBoard[i + 1][j - 1] > max)
				max = gameBoard[i + 1][j - 1];
		}
		if (i < gameBoardSize - 1) {
			if (gameBoard[i + 1][j] > max)
				max = gameBoard[i + 1][j];
		}
		if (i < gameBoardSize - 1 && j < gameBoardSize - 1) {
			if (gameBoard[i + 1][j + 1] > max)
				max = gameBoard[i + 1][j + 1];
		}
		System.out.println(max);
		return max;
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
			int tile;
			if (isNextTileBooster) {
				tile = currentBooster;
			} else
				tile = rndMove.nextFloat() < 0.9f ? 4 : 8;

			if (gameBoard[rnd / gameBoardSize][rnd % gameBoardSize] == 0) {
				gameBoard[rnd / gameBoardSize][rnd % gameBoardSize] = tile;
				if (isNextTileBooster) {
					noOfBoosterToProduce--;
					if (noOfBoosterToProduce == 0) {
						isNextTileBooster = false;
						currentBooster = 0;
					}
				}
				incrementMoveCount();
				return true;
			} else {
				rnd = rndMove.nextInt((int) Math.pow(gameBoardSize, 2));
			}
		}
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

		achievementBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.achievment);
		achievementBitmap = Bitmap.createScaledBitmap(achievementBitmap,
				rightSidePanelIconSize, rightSidePanelIconSize, true);

		nightModeBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.night_mode);
		nightModeBitmap = Bitmap.createScaledBitmap(nightModeBitmap,
				rightSidePanelIconSize, rightSidePanelIconSize, true);

		leaderBoardBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.leaderboard);
		leaderBoardBitmap = Bitmap.createScaledBitmap(leaderBoardBitmap,
				rightSidePanelIconSize, rightSidePanelIconSize, true);

		resetBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.reset);
		resetBitmap = Bitmap.createScaledBitmap(resetBitmap,
				rightSidePanelIconSize, rightSidePanelIconSize, true);

		menuOptionBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.menu);
		menuOptionBitmap = Bitmap.createScaledBitmap(menuOptionBitmap,
				rightSidePanelIconSize, rightSidePanelIconSize, true);

		screenCaptureBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_screencapture);
		screenCaptureBitmap = Bitmap.createScaledBitmap(screenCaptureBitmap,
				rightSidePanelIconSize, rightSidePanelIconSize, true);

		footerMenuBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.footer_menu_bkg);
		footerMenuBitmap = Bitmap.createScaledBitmap(footerMenuBitmap,
				footerMenuWidth, rightSidePanelIconSize, true);
		autoMovePoint = getLeftTop(footerMenuBitmap, autoMoveTextPoint);
		boostersPoint = getLeftTop(footerMenuBitmap, boostersTextPoint);

		bitmapEmpty = BitmapFactory.decodeResource(getResources(),
				R.drawable.empty);
		bitmapEmpty = Bitmap.createScaledBitmap(bitmapEmpty, tileWidth,
				tileHeight, true);

		bitmapTimeScoreHeader = BitmapFactory.decodeResource(getResources(),
				R.drawable.time);
		bitmapTimeScoreHeader = Bitmap.createScaledBitmap(
				bitmapTimeScoreHeader, headerButtonWidth, headerButtonHeight,
				true);
		scoreDisplay = getLeftTop(bitmapTimeScoreHeader, scoreDisplayCenter);
		timeDisplay = getLeftTop(bitmapTimeScoreHeader, timeDisplayCenter);

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

		bitmapTile_8192 = BitmapFactory.decodeResource(getResources(),
				R.drawable.btn_8192);
		bitmapTile_8192 = Bitmap.createScaledBitmap(bitmapTile_8192, tileWidth,
				tileHeight, true);

		bitmapTile_x2 = BitmapFactory.decodeResource(getResources(),
				R.drawable.x2_tile);
		bitmapTile_x2 = Bitmap.createScaledBitmap(bitmapTile_x2, tileWidth,
				tileHeight, true);

		bitmapTile_bomb = BitmapFactory.decodeResource(getResources(),
				R.drawable.bomb_tile);
		bitmapTile_bomb = Bitmap.createScaledBitmap(bitmapTile_bomb, tileWidth,
				tileHeight, true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		rootView = getRootView();

		canvas.drawBitmap(bitmapBkg, 0, 0, null);

		// header display
		canvas.drawBitmap(bitmapTimeScoreHeader, scoreDisplay.x,
				scoreDisplay.y, null);
		canvas.drawBitmap(bitmapTimeScoreHeader, timeDisplay.x, timeDisplay.y,
				null);

		// header icon display
		canvas.drawBitmap(resetBitmap, resetPoint.x, resetPoint.y, null);
		canvas.drawBitmap(achievementBitmap, achievementpoPoint.x,
				achievementpoPoint.y, null);
		canvas.drawBitmap(leaderBoardBitmap, leaderBoardPoint.x,
				leaderBoardPoint.y, null);
		canvas.drawBitmap(nightModeBitmap, nightModePoint.x, nightModePoint.y,
				null);

		// Move Display
		Point pnt = getTextBound("Move", timeDisplayCenter, smallTextPaint);
		canvas.drawText("Move", pnt.x,
				(float) (pnt.y - (headerButtonHeight * 0.6)), smallTextPaint);

		pnt = getTextBound(String.valueOf(move), timeDisplayCenter,
				timeScoreTextPaint);
		canvas.drawText(String.valueOf(move), pnt.x, pnt.y, timeScoreTextPaint);

		// Current Score Display
		pnt = getTextBound("Score", scoreDisplayCenter, smallTextPaint);
		canvas.drawText("Score", pnt.x,
				(float) (pnt.y - (headerButtonHeight * 0.6)), smallTextPaint);

		pnt = getTextBound(String.valueOf(currentScore), scoreDisplayCenter,
				timeScoreTextPaint);
		canvas.drawText(String.valueOf(currentScore), pnt.x, pnt.y,
				timeScoreTextPaint);

		// footer option menu display
		canvas.drawBitmap(menuOptionBitmap, footerMenuOptionPoint.x,
				footerMenuOptionPoint.y, null);
		canvas.drawBitmap(footerMenuBitmap, autoMovePoint.x, autoMovePoint.y,
				null);
		canvas.drawBitmap(footerMenuBitmap, boostersPoint.x, boostersPoint.y,
				null);

		// footer save menu display
		canvas.drawBitmap(screenCaptureBitmap, screenCapturePoint.x,
				screenCapturePoint.y, null);

		// auto move footer menu display
		pnt = getTextBound("AUTO MOVE", autoMoveTextPoint, footerMenuTextPaint);
		canvas.drawText("AUTO MOVE", pnt.x, pnt.y, footerMenuTextPaint);

		// time rush footer menu display
		pnt = getTextBound("POWERUPS", boostersTextPoint, footerMenuTextPaint);
		canvas.drawText("POWERUPS", pnt.x, pnt.y, footerMenuTextPaint);

		Bitmap bitmap = bitmapEmpty;

		for (int i = 0; i < gameBoardSize; i++)
			for (int j = 0; j < gameBoardSize; j++) {

				bitmap = getBitmap(gameBoard[i][j]);

				canvas.drawBitmap(bitmap, gameBoardPos[i][j].x,
						gameBoardPos[i][j].y, null);
			}

		if (gameEndFlag || gameWonFlag) {
			if (!gameWonFlag) {
				msgForHighScoreActivity = "No more Move possible";
			} else {
				msgForHighScoreActivity = "You Win!";
			}
			if (outOfMoveFlag) {
				msgForHighScoreActivity = "Out of Moves!";
			}
			tutorialTextPaint.setTextSize((float) (headerButtonHeight * 0.6));
			pnt = getTextBound(msgForHighScoreActivity, tutorialCenter,
					tutorialTextPaint);
			canvas.drawText(msgForHighScoreActivity, pnt.x, pnt.y,
					tutorialTextPaint);
		}

		if (gameEndFlag || gameWonFlag) {
			if (!gameWonFlag) {
				msgForHighScoreActivity = "No more Move possible";
			} else {
				msgForHighScoreActivity = "You Win!";
			}
			if (outOfMoveFlag) {
				msgForHighScoreActivity = "Out of Moves!";
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

				Intent intent = new Intent(mContext, NewHighscoreActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				intent.putExtra(NewHighscoreActivity.BASE_SCORE, currentScore);
				intent.putExtra(NewHighscoreActivity.HIGH_NUMBER_BONUS,
						calculateHighestTileBonus());
				mContext.startActivity(intent);
				return;
			}
			threadSyncFlag = true;
			invalidate();
		}
	}

	private int calculateHighestTileBonus() {
		int max = gameBoard[0][0];
		for (int i = 0; i < gameBoardSize; i++) {
			for (int j = 0; j < gameBoardSize; j++) {
				if (gameBoard[i][j] > max)
					max = gameBoard[i][j];
			}
		}
		return (int) (max * 12.5);
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

		case 8192:
			bitmap = bitmapTile_8192;
			break;

		case MULTIPLY_BY_2_POWERUP:
			bitmap = bitmapTile_x2;
			break;

		case BOMB_POWERUP:
			bitmap = bitmapTile_bomb;
			break;

		default:
			bitmap = bitmapEmpty;
			break;

		}
		return bitmap;
	}

	private boolean isResetTouched(Point touchPoint) {
		if (touchPoint.x >= resetPoint.x
				&& touchPoint.x <= resetPoint.x + rightSidePanelIconSize)
			if (touchPoint.y >= resetPoint.y
					&& touchPoint.y <= resetPoint.y + rightSidePanelIconSize)
				return true;
		return false;
	}

	private void resetGame() {
		Toast.makeText(mContext, "game reset", Toast.LENGTH_LONG).show();
		for (int i = 0; i < gameBoardSize; i++)
			for (int j = 0; j < gameBoardSize; j++) {
				gameBoard[i][j] = 0;
			}
		startGame();
	}

	private boolean isAchievementTouched(Point touchPoint) {
		if (touchPoint.x >= achievementpoPoint.x
				&& touchPoint.x <= achievementpoPoint.x
						+ rightSidePanelIconSize)
			if (touchPoint.y >= achievementpoPoint.y
					&& touchPoint.y <= achievementpoPoint.y
							+ rightSidePanelIconSize)
				return true;
		return false;
	}

	private void showAchievement() {
		// Toast.makeText(mContext, "showAchievement",
		// Toast.LENGTH_LONG).show();
		// if (NewLayout_For_Testing.mGoogleApiClient.isConnected()) {
		// Games.Achievements.unlock(NewLayout_For_Testing.mGoogleApiClient,
		// getString(R.string.correct_guess_achievement));
		// System.out.println("yahoooo");
		//
		// mContext.startActivity(Games.Achievements
		// .getAchievementsIntent(NewLayout_For_Testing.mGoogleApiClient));
		// System.out.println("User is connected!");
		// } else {
		// NewLayout_For_Testing.mGoogleApiClient.connect();
		// System.out.println("User is not connected!");
		// }
	}

	private boolean isLeaderBoardTouched(Point touchPoint) {
		if (touchPoint.x >= leaderBoardPoint.x
				&& touchPoint.x <= leaderBoardPoint.x + rightSidePanelIconSize)
			if (touchPoint.y >= leaderBoardPoint.y
					&& touchPoint.y <= leaderBoardPoint.y
							+ rightSidePanelIconSize)
				return true;
		return false;
	}

	private void showLeaderBoard() {
		Toast.makeText(mContext, "showLeaderBoard", Toast.LENGTH_LONG).show();
	}

	private boolean isNightModeTouched(Point touchPoint) {
		if (touchPoint.x >= nightModePoint.x
				&& touchPoint.x <= nightModePoint.x + rightSidePanelIconSize)
			if (touchPoint.y >= nightModePoint.y
					&& touchPoint.y <= nightModePoint.y
							+ rightSidePanelIconSize)
				return true;
		return false;
	}

	private void changeNightMode() {
		gameNightMode = !gameNightMode;
		if (gameNightMode) {
			bitmapBkg = BitmapFactory.decodeResource(getResources(),
					R.drawable.bkg_nightmode);
			bitmapBkg = Bitmap.createScaledBitmap(bitmapBkg, screenWidth,
					screenHeight, true);
			smallTextPaint.setColor(Color.WHITE);
			tutorialTextPaint.setColor(Color.WHITE);
			footerMenuTextPaint.setColor(Color.DKGRAY);
			timeScoreTextPaint.setColor(Color.DKGRAY);
		} else {
			bitmapBkg = BitmapFactory.decodeResource(getResources(),
					R.drawable.bkg);
			bitmapBkg = Bitmap.createScaledBitmap(bitmapBkg, screenWidth,
					screenHeight, true);
			smallTextPaint.setColor(Color.DKGRAY);
			tutorialTextPaint.setColor(Color.DKGRAY);
			footerMenuTextPaint.setColor(Color.WHITE);
			timeScoreTextPaint.setColor(Color.WHITE);
		}
		invalidate();
	}

	private boolean isFooterMenuOptionTouched(Point touchPoint) {
		if (touchPoint.x >= footerMenuOptionPoint.x
				&& touchPoint.x <= footerMenuOptionPoint.x
						+ rightSidePanelIconSize)
			if (touchPoint.y >= footerMenuOptionPoint.y
					&& touchPoint.y <= footerMenuOptionPoint.y
							+ rightSidePanelIconSize)
				return true;
		return false;
	}

	private void showFooterMenuOption() {
		Intent intent = new Intent(mContext, NewMenuOption.class);
		// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		mContext.startActivity(intent);
	}

	private boolean isAutoMoveTouched(Point touchPoint) {
		if (touchPoint.x >= autoMovePoint.x
				&& touchPoint.x <= autoMovePoint.x + footerMenuWidth)
			if (touchPoint.y >= autoMovePoint.y
					&& touchPoint.y <= autoMovePoint.y + rightSidePanelIconSize)
				return true;
		return false;
	}

	private void showAutoMove() {
		final AlertDialog alert = new AlertDialog.Builder(mContext,
				AlertDialog.THEME_HOLO_LIGHT).create();
		alert.setTitle("Auto move Alert");
		alert.setCancelable(true);
		alert.setMessage("Auto Move feature is not available in Limited Move Mode.");
		alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						alert.dismiss();
					}

				});
		alert.show();
		return;

	}

	private boolean isBoostersTouched(Point touchPoint) {
		if (touchPoint.x >= boostersPoint.x
				&& touchPoint.x <= boostersPoint.x + footerMenuWidth)
			if (touchPoint.y >= boostersPoint.y
					&& touchPoint.y <= boostersPoint.y + rightSidePanelIconSize)
				return true;
		return false;
	}

	private void showBoostersDialod() {
		if (gameEndFlag || gameWonFlag)
			return;
		if (isNextTileBooster) {
			final AlertDialog alert = new AlertDialog.Builder(mContext,
					AlertDialog.THEME_HOLO_LIGHT).create();
			alert.setTitle("PowerUp Alert");
			alert.setCancelable(true);
			alert.setMessage("Use your current PowerUp completely.Than try your luck with aother PowerUp");
			alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							alert.dismiss();
						}

					});
			alert.show();
			return;
		}
		DatabaseHandler dbhHandler = new DatabaseHandler(mContext);

		boosterDialog = new Dialog(mContext,
				android.R.style.Theme_Holo_Light_Dialog);
		boosterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// boosterDialog.setTitle("Select Powerup");
		boosterDialog.setContentView(R.layout.booster_layout);

		ScrollView sv = (ScrollView) boosterDialog
				.findViewById(R.id.boosterScrollView);
		sv.setMinimumWidth((int) (screenWidth * 0.6));

		// set the custom dialog components - text, image and button
		TextView textView = (TextView) boosterDialog
				.findViewById(R.id.boosterDialogLayout_ShowPoints);
		textView.setText("Points: " + dbhHandler.getGameDataPoints());
		textView.setTypeface(typeFace, Typeface.BOLD);

		textView = (TextView) boosterDialog
				.findViewById(R.id.boosterDialogLayoutTitle);
		textView.setTypeface(typeFace, Typeface.BOLD);

		setOnclickListnerForBoosterDialog(boosterDialog,
				R.id.continuous_5_times_4);
		setOnclickListnerForBoosterDialog(boosterDialog,
				R.id.continuous_3_times_8);
		setOnclickListnerForBoosterDialog(boosterDialog, R.id.booster_16);
		setOnclickListnerForBoosterDialog(boosterDialog, R.id.booster_32);
		setOnclickListnerForBoosterDialog(boosterDialog, R.id.booster_64);
		setOnclickListnerForBoosterDialog(boosterDialog, R.id.booster_128);
		setOnclickListnerForBoosterDialog(boosterDialog, R.id.x2_booster);
		setOnclickListnerForBoosterDialog(boosterDialog, R.id.bombBooster);

		// disabledForBoosterDialog(boosterDialog,
		// R.id.continuous_3_times_8);
		// disabledForBoosterDialog(boosterDialog, R.id.booster_128);
		// disabledForBoosterDialog(boosterDialog, R.id.booster_16);
		// disabledForBoosterDialog(boosterDialog, R.id.booster_32);
		// disabledForBoosterDialog(boosterDialog, R.id.x2_booster);
		// disabledForBoosterDialog(boosterDialog, R.id.bombBooster);

		if (!boosterDialog.isShowing())
			boosterDialog.show();
	}

	@SuppressWarnings("unused")
	private void disabledForBoosterDialog(Dialog boosterDialog, int id) {
		LinearLayout boosterDisplayLayout = (LinearLayout) boosterDialog
				.findViewById(id);
		boosterDisplayLayout.setEnabled(false);

		ImageView img = (ImageView) boosterDisplayLayout.getChildAt(0);
		ColorMatrix matrix = new ColorMatrix();
		matrix.setSaturation((float) 0.125);
		ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
		img.setColorFilter(filter);
	}

	private void setOnclickListnerForBoosterDialog(final Dialog boosterDialog,
			final int id) {
		LinearLayout boosterDisplayLayout = (LinearLayout) boosterDialog
				.findViewById(id);
		boosterDisplayLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				updateBoosterGenerationInformation(id);
				boosterDialog.dismiss();
			}
		});
	}

	private void updateBoosterGenerationInformation(int whichBooster) {
		isNextTileBooster = true;
		noOfBoosterToProduce = 1;
		DatabaseHandler dbhHandler = new DatabaseHandler(mContext);
		int points = dbhHandler.getGameDataPoints();
		switch (whichBooster) {
		case R.id.continuous_5_times_4:
			noOfBoosterToProduce = 5;
			currentBooster = 4;
			if (points < 1500) {
				needExtraPointsAlert(points, 1500);
			} else {
				successfulPurchaseToast("Four Squares", 1500);
			}
			break;
		case R.id.continuous_3_times_8:
			noOfBoosterToProduce = 3;
			currentBooster = 8;
			if (points < 1500) {
				needExtraPointsAlert(points, 1500);
			} else {
				successfulPurchaseToast("Eight Squares", 1500);
			}
			break;
		case R.id.booster_16:
			noOfBoosterToProduce = 3;
			currentBooster = 16;
			if (points < 2500) {
				needExtraPointsAlert(points, 2500);
			} else {
				successfulPurchaseToast("Sixteen Squares", 2500);
			}
			break;
		case R.id.booster_32:
			currentBooster = 32;
			if (points < 2500) {
				needExtraPointsAlert(points, 2500);
			} else {
				successfulPurchaseToast("32 Squares", 2500);
			}
			break;
		case R.id.booster_64:
			currentBooster = 64;
			if (points < 4000) {
				needExtraPointsAlert(points, 4000);
			} else {
				successfulPurchaseToast("64 Squares", 4000);
			}
			break;
		case R.id.booster_128:
			currentBooster = 128;
			if (points < 7000) {
				needExtraPointsAlert(points, 7000);
			} else {
				successfulPurchaseToast("128 Squares", 7000);
			}
			break;
		case R.id.x2_booster:
			currentBooster = 22;
			if (points < 3000) {
				needExtraPointsAlert(points, 3000);
			} else {
				successfulPurchaseToast("2x Multipliers", 3000);
			}
			break;
		case R.id.bombBooster:
			currentBooster = -1;
			if (points < 2000) {
				needExtraPointsAlert(points, 2000);
			} else {
				successfulPurchaseToast("The Destroyer", 2000);
			}
			break;
		default:
			noOfBoosterToProduce = 0;
			currentBooster = 0;
			isNextTileBooster = false;
			break;
		}
	}

	@SuppressWarnings("deprecation")
	private void successfulPurchaseToast(String boosterName,
			final int costPoints) {
		final AlertDialog alertDialog = new AlertDialog.Builder(mContext,
				AlertDialog.THEME_HOLO_LIGHT).create();
		alertDialog.setTitle("Congratulations!");
		alertDialog.setMessage("You are about to purchase PowerUp \""
				+ boosterName + "\".");
		alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();

			}
		});
		alertDialog.setButton("Purchase",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						DatabaseHandler dbhHandler = new DatabaseHandler(
								mContext);
						dbhHandler.setGameDataPoints(dbhHandler
								.getGameDataPoints() - costPoints);
					}
				});
		// Showing Alert Message
		alertDialog.show();

	}

	private void needExtraPointsAlert(int currentPoints, int requiredPoints) {
		AlertDialog alertDialog = new AlertDialog.Builder(mContext,
				AlertDialog.THEME_HOLO_LIGHT).create();
		alertDialog.setTitle("Insufficient points");
		alertDialog.setMessage("You need extra "
				+ (currentPoints - requiredPoints)
				+ " points to ues this PowerUp.");
		// Showing Alert Message
		alertDialog.show();
	}

	private boolean isScreenCaptureBitmapMenuTouched(Point touchPoint) {
		if (touchPoint.x >= screenCapturePoint.x
				&& touchPoint.x <= screenCapturePoint.x
						+ rightSidePanelIconSize)
			if (touchPoint.y >= screenCapturePoint.y
					&& touchPoint.y <= screenCapturePoint.y
							+ rightSidePanelIconSize)
				return true;
		return false;
	}

	public void takeScreenShot() {
		rootView.setDrawingCacheEnabled(true);
		saveBitmap(rootView.getDrawingCache());
	}

	@SuppressWarnings("deprecation")
	public void saveBitmap(Bitmap bitmap) {
		String captureDirPath = Environment.getExternalStorageDirectory()
				+ "/Pictures/2048 Screenshots";

		String partialPath = (new Date(System.currentTimeMillis()))
				.toGMTString().replaceAll("GMT", "").trim().replace(' ', '_')
				.replace(':', '-');

		File imagePath = new File(captureDirPath + "/2048 Puzzle_"
				+ partialPath + ".jpg");

		if (!imagePath.exists()) {
			try {
				new File(captureDirPath).mkdirs();
				if (imagePath.createNewFile()) {
					System.out.println("new file created at "
							+ imagePath.getCanonicalPath());
					FileOutputStream fos;
					try {
						fos = new FileOutputStream(imagePath);
						bitmap.compress(CompressFormat.JPEG, 100, fos);
						fos.flush();
						fos.close();
						Toast.makeText(
								mContext,
								"Screenshot saved at "
										+ imagePath.getCanonicalPath(),
								Toast.LENGTH_SHORT).show();
					} catch (FileNotFoundException e) {
						Log.e("2048! Number Puzzle Game", e.getMessage(), e);
						System.out.println(e.getMessage());
					} catch (IOException e) {
						Log.e("2048! Number Puzzle Game", e.getMessage(), e);
						System.out.println(e.getMessage());
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				Log.e("2048! Number Puzzle Game", e.getMessage(), e);
				System.out.println(e.getMessage());
			}
		}
	}
}

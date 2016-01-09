package com.inverted.tech.mission2048;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.inverted.tech.depricatedClass.LimitedMoveGame;
import com.inverted.tech.depricatedClass.NormalGame;
import com.inverted.tech.mission2048.GameMode.Revised_LimitedMoveGame;
import com.inverted.tech.mission2048.GameMode.Revised_NormalGame;
import com.inverted.tech.mission2048.GameMode.Revised_TimerGame;
import com.inverted.tech.mission2048.SupportActivities.Resume_Pause;
import com.inverted.tech.mission2048.databaseConnection.DatabaseHandler;
import com.inverted.tech.mission2048.databaseConnection.SaveAndLoad;

@SuppressLint("Wakelock")
public class GameDisplay extends Activity {
	Revised_NormalGame normalGame;
	Revised_TimerGame timerGame;
	Revised_LimitedMoveGame limitedMoveGame;
	public static SaveAndLoad gameState;
	public static boolean startedAfterPause = false;
	public static int screenHeight;
	public static int screenWidth;

	PowerManager pm;
	PowerManager.WakeLock wl;
	private boolean isMusic;
	private MediaPlayer tileGenClickPlayer;

	private long prevbackPressed;
	private byte backBtnPressCount = 0;
	private int gameMode;
	public static int timeRushLimit;

	public static final String TIME_RUSH = "time_rush";

	// Google client to interact with Google API
	public static Activity mContext;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		Display display = getWindowManager().getDefaultDisplay();
		screenHeight = display.getHeight();
		screenWidth = display.getWidth();
		initMusicPlayer();

		Bundle bundle = getIntent().getExtras();
		timeRushLimit = bundle.getInt(TIME_RUSH);

		DatabaseHandler databaseHandler = new DatabaseHandler(this);
		databaseHandler.setScreenDimension(screenHeight, screenWidth);
		// databaseHandler.setGameDataNoOfTile(4);
		// databaseHandler.setGameDataGameMode(highScoreValue.MODE_NORMAL);
		databaseHandler.setGameDataPoints(10000000);
		gameMode = databaseHandler.getGameDataGameMode();

		// game resumed
		if (startedAfterPause) {
			DatabaseHandler db = new DatabaseHandler(GameDisplay.this);
			db.setGameDataNoOfTile(gameState.getNoOfTile());
			gameMode = gameState.getMode();
		}

		if (gameMode == Game.normalMode) {
			normalGame = new Revised_NormalGame(GameDisplay.this);
			setContentView(normalGame);
		} else if (gameMode == Game.timerMode) {
			timerGame = new Revised_TimerGame(GameDisplay.this);
			setContentView(timerGame);
		} else {
			limitedMoveGame = new Revised_LimitedMoveGame(GameDisplay.this);
			setContentView(limitedMoveGame);
		}
		mContext = this;
	}

	@Override
	protected void onPause() {
		if (gameMode == Game.normalMode) {
			normalGame.stopTimer();
		} else if (gameMode == Game.timerMode) {
			timerGame.stopTimer();
		}
		wl.release();
		super.onPause();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		if (gameMode == Game.normalMode) {
			normalGame.resumeTimer();
		} else if (gameMode == Game.timerMode) {
			timerGame.resumeTimer();
		}
		pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
				"com.inverted.tech.mission2048");
		wl.acquire();
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		playBtnClickSound();
		// if timer mode no pause is allowed
		if (gameMode == Game.timerMode) {
			if (backBtnPressCount == 0) {
				backBtnPressCount++;
				prevbackPressed = System.currentTimeMillis();
				Toast.makeText(getApplicationContext(),
						"Press again to end this game.", Toast.LENGTH_LONG)
						.show();
				return;
			} else if (System.currentTimeMillis() - prevbackPressed < 3000) {
				super.onBackPressed();
				return;
			} else {
				backBtnPressCount = 0;
				return;
			}
		}
		DatabaseHandler db = new DatabaseHandler(GameDisplay.this);
		// normal game state
		if (gameMode == Game.normalMode) {
			gameState = new SaveAndLoad();
			gameState.setNoOfTile(db.getGameDataNoOfTile());
			gameState.setMode(gameMode);
			gameState.setScore(NormalGame.currentScore);
			gameState.setTime(NormalGame.totalTime);
			gameState.boardToString(Revised_NormalGame.gameBoard);
		}
		// moves game state
		else {
			gameState = new SaveAndLoad();
			gameState.setNoOfTile(db.getGameDataNoOfTile());
			gameState.setMode(gameMode);
			gameState.setScore(LimitedMoveGame.currentScore);
			gameState.setTime(LimitedMoveGame.move);
			gameState.boardToString(Revised_LimitedMoveGame.gameBoard);
		}
		Intent intent = new Intent(GameDisplay.this, Resume_Pause.class);
		startActivity(intent);
	}

	private void playBtnClickSound() {
		if (!isMusic || tileGenClickPlayer.isPlaying())
			return;
		tileGenClickPlayer.start();
	}

	private void initMusicPlayer() {
		tileGenClickPlayer = MediaPlayer.create(GameDisplay.this,
				R.raw.menu_btn_clicked);
		tileGenClickPlayer.setVolume(100, 100);
		DatabaseHandler db = new DatabaseHandler(this);
		isMusic = db.getGameDataMusic();
	}

	protected void onStart() {
		super.onStart();
	}

}
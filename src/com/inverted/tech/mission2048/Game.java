package com.inverted.tech.mission2048;

import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.inverted.tech.depricatedClass.NormalGame;
import com.inverted.tech.depricatedClass.TimerGame;
import com.inverted.tech.mission2048.SupportActivities.AppRater;
import com.inverted.tech.mission2048.SupportActivities.LikeUsActivity;
import com.inverted.tech.mission2048.databaseConnection.DatabaseHandler;
import com.inverted.tech.mission2048.databaseConnection.highScoreValue;
import com.inverted.tech.mission2048.supportClass.dataStorageClass;

@SuppressLint("SetJavaScriptEnabled")
public class Game extends Activity {

	private long prevbackPressed;
	private byte backBtnPressCount = 0;
	public static int screenHeight;
	public static int screenWidth;
	public static boolean isMusic = true;

	public static dataStorageClass data[] = new dataStorageClass[3];

	public final static int timerMode = 0;
	public final static int normalMode = 1;
	public final static int limitedMoveMode = 2;
	public static int gameMode;

	public static boolean tutorialEnabled = false;

	NormalGame normalGame;
	TimerGame timerGame;
	private TextView label;
	private MediaPlayer btnClickPlayer;

	private ImageView normal, timed, moves;

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

		final DatabaseHandler db = new DatabaseHandler(this);
		db.setScreenDimension(screenHeight, screenWidth);
		gameMode = db.getGameDataGameMode();

		for (int i = 0; i < data.length; i++) {
			data[i] = new dataStorageClass();
		}

		setContentView(R.layout.activity_game);
		btnClickPlayer = MediaPlayer.create(Game.this, R.raw.menu_btn_clicked);
		btnClickPlayer.setVolume(100, 100);

		Button btn = (Button) findViewById(R.id.playBtn);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				playBtnClickSound();
				tutorialEnabled = false;

				DatabaseHandler db = new DatabaseHandler(Game.this);
				if (gameMode == limitedMoveMode) {
					if (!db.getGameDataLikeUs()) {
						showLikeUsAlertDialog();
						return;
					}
				} else if (gameMode == timerMode) {
					if (!db.getGameDataRateUs()) {
						showRateUsAlertDialog();
						return;
					}
				}

				Intent newIntent = new Intent(Game.this, GameDisplay.class);
				newIntent.putExtra(GameDisplay.TIME_RUSH, 120);
				startActivity(newIntent);
			}
		});

		btn = (Button) findViewById(R.id.menuOptions);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				playBtnClickSound();
				Intent newIntent = new Intent(Game.this, MenuOptions.class);
				startActivity(newIntent);
			}
		});

		btn = (Button) findViewById(R.id.tutorial);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				playBtnClickSound();
				tutorialEnabled = true;
				DatabaseHandler db = new DatabaseHandler(Game.this);
				db.setGameDataNoOfTile(4);
				gameMode = normalMode;
				Intent newIntent = new Intent(Game.this, GameDisplay.class);
				startActivity(newIntent);
			}
		});

		RelativeLayout rl = (RelativeLayout) findViewById(R.id.relativeLayout1);
		rl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(Game.this, "If you liked game please Rate it.",
						Toast.LENGTH_SHORT).show();
			}
		});

		label = (TextView) findViewById(R.id.modeLabel);

		normal = (ImageView) findViewById(R.id.normalMode);
		normal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				gameMode = normalMode;
				db.setGameDataGameMode(normalMode);
				label.setText("Normal");
				updateViewVisibility(v.getId());
			}
		});

		timed = (ImageView) findViewById(R.id.timerMode);
		timed.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				gameMode = timerMode;
				db.setGameDataGameMode(timerMode);
				label.setText("Timer(90s)");
				updateViewVisibility(v.getId());
			}
		});

		moves = (ImageView) findViewById(R.id.limitedMode);
		moves.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				gameMode = limitedMoveMode;
				db.setGameDataGameMode(limitedMoveMode);
				label.setText("Moves");
				updateViewVisibility(v.getId());
			}
		});

		findViewById(R.id.normalMode).setBackgroundResource(
				R.drawable.normal_disabled);
		findViewById(R.id.timerMode).setBackgroundResource(
				R.drawable.timer_disabled);
		findViewById(R.id.limitedMode).setBackgroundResource(
				R.drawable.limited_disabled);
		switch (gameMode) {
		case normalMode:
			findViewById(R.id.normalMode).setBackgroundResource(
					R.drawable.normal_enabled);
			label.setText("Normal");
			break;
		case timerMode:
			findViewById(R.id.timerMode).setBackgroundResource(
					R.drawable.timer_enabled);
			label.setText("Timer(90s)");
			break;
		case limitedMoveMode:
			findViewById(R.id.limitedMode).setBackgroundResource(
					R.drawable.limited_enabled);
			label.setText("Moves");
			break;
		}

	}

	private void showRateUsAlertDialog() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				Game.this);

		alertDialogBuilder.setTitle("Rate Us");
		alertDialogBuilder
				.setMessage(
						"Rate Us on Google Play Store to unlock this game mode.")
				.setCancelable(false)
				.setPositiveButton("OK,I'll rate it now",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								playBtnClickSound();
								DatabaseHandler db = new DatabaseHandler(
										Game.this);
								db.setGameDataRateUs(1);

								Intent intent = new Intent(Intent.ACTION_VIEW);
								intent.setData(Uri
										.parse("market://details?id=com.inverted.tech.mission2048"));
								startActivity(intent);
								dialog.cancel();
							}
						})
				.setNegativeButton("Not now",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();
	}

	private void showLikeUsAlertDialog() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				Game.this);

		alertDialogBuilder.setTitle("Like Us");
		alertDialogBuilder
				.setMessage("Like Us on Facebook to unlock this game mode.")
				.setCancelable(false)
				.setPositiveButton("Like Us",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent intent = new Intent(Game.this,
										LikeUsActivity.class);
								startActivity(intent);
							}
						})
				.setNegativeButton("Not now",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();
	}

	protected void updateViewVisibility(int id) {
		findViewById(R.id.normalMode).setBackgroundResource(
				R.drawable.normal_disabled);
		findViewById(R.id.timerMode).setBackgroundResource(
				R.drawable.timer_disabled);
		findViewById(R.id.limitedMode).setBackgroundResource(
				R.drawable.limited_disabled);

		switch (id) {
		case R.id.normalMode:
			findViewById(R.id.normalMode).setBackgroundResource(
					R.drawable.normal_enabled);
			break;
		case R.id.timerMode:
			findViewById(R.id.timerMode).setBackgroundResource(
					R.drawable.timer_enabled);
			break;
		case R.id.limitedMode:
			findViewById(R.id.limitedMode).setBackgroundResource(
					R.drawable.limited_enabled);
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		tutorialEnabled = false;
		backBtnPressCount = 0;

		DatabaseHandler db = new DatabaseHandler(this);
		isMusic = db.getGameDataMusic();

		// if (db.getHighScoreCount() == 0) {
		// // System.out.println("Creating empty database");
		// db.addEmptyHighScore();
		// }

		List<highScoreValue> highScoreList = db.getAllHighScore();

		int idx_normal_3 = 0, idx_normal_4 = 0, idx_normal_5 = 0;
		int idx_timed_3 = 0, idx_timed_4 = 0, idx_timed_5 = 0;
		int idx_moves_3 = 0, idx_moves_4 = 0, idx_moves_5 = 0;

		Iterator<highScoreValue> itrator = highScoreList.iterator();
		while (itrator.hasNext()) {
			highScoreValue tempScore = itrator.next();
			// // System.out.println(tempScore.toString());
			// 3 tiles
			if (tempScore.get_tile() == 0) {
				if (tempScore.get_mode() == highScoreValue.MODE_NORMAL) {
					data[0].normalBestScore[idx_normal_3] = tempScore
							.get_score();
					idx_normal_3++;
				} else if (tempScore.get_mode() == highScoreValue.MODE_TIMED) {
					data[0].timerBestScore[idx_timed_3] = tempScore.get_score();
					idx_timed_3++;
				} else {
					data[0].limitedBestScore[idx_moves_3] = tempScore
							.get_score();
					idx_moves_3++;
				}
			}
			// 4 tiles
			else if (tempScore.get_tile() == 1) {
				if (tempScore.get_mode() == highScoreValue.MODE_NORMAL) {
					data[1].normalBestScore[idx_normal_4] = tempScore
							.get_score();
					idx_normal_4++;
				} else if (tempScore.get_mode() == highScoreValue.MODE_TIMED) {
					data[1].timerBestScore[idx_timed_4] = tempScore.get_score();
					idx_timed_4++;
				} else {
					data[1].limitedBestScore[idx_moves_4] = tempScore
							.get_score();
					idx_moves_4++;
				}
			}
			// 5 tiles
			else if (tempScore.get_tile() == 2) {
				if (tempScore.get_mode() == highScoreValue.MODE_NORMAL) {
					data[2].normalBestScore[idx_normal_5] = tempScore
							.get_score();
					idx_normal_5++;
				} else if (tempScore.get_mode() == highScoreValue.MODE_TIMED) {
					data[2].timerBestScore[idx_timed_5] = tempScore.get_score();
					idx_timed_5++;
				} else {
					data[2].limitedBestScore[idx_moves_5] = tempScore
							.get_score();
					idx_moves_5++;
				}
			}
		}
		// dataStatus();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		playBtnClickSound();
		// showRateDialog();
		// super.onBackPressed();
		if (backBtnPressCount == 0) {
			backBtnPressCount++;
			prevbackPressed = System.currentTimeMillis();
			Toast.makeText(getApplicationContext(), "Press again to exit",
					Toast.LENGTH_LONG).show();
		} else if (System.currentTimeMillis() - prevbackPressed < 3000) {
			AppRater.app_launched(getApplicationContext());
			super.onBackPressed();
		} else
			backBtnPressCount = 0;
	}

	private void playBtnClickSound() {
		if (btnClickPlayer.isPlaying() || !isMusic)
			return;
		btnClickPlayer.start();
	}

}
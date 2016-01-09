package com.inverted.tech.mission2048;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.inverted.tech.depricatedClass.LimitedMoveGame;
import com.inverted.tech.depricatedClass.NormalGame;
import com.inverted.tech.depricatedClass.TimerGame;
import com.inverted.tech.mission2048.common.AuthListener;
import com.inverted.tech.mission2048.databaseConnection.DatabaseHandler;
import com.inverted.tech.mission2048.facebook.FacebookFacade;
import com.inverted.tech.mission2048.supportClass.Constants;

public class HighScore extends Activity implements AnimationListener {

	private static String captureDirPath = Environment
			.getExternalStorageDirectory()
			+ "/Android/data/com.inverted.tech.mission2048/Screenshots";
	private static String copyDirPath = Environment
			.getExternalStorageDirectory() + "/Pictures/2048_Screenshots";
	private static String filePath = "/screenShot.jpg";
	MediaPlayer cameraClickPlayer;

	private FacebookFacade facebook;
	private int curScore;
	private String title;
	private Animation animMoveDown;
	private Button scanner;
	private MediaPlayer tileGenClickPlayer;
	private boolean isMusic;
	private int noOfTile;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.highscore);
		initMusicPlayer();

		DatabaseHandler db = new DatabaseHandler(this);
		noOfTile = db.getGameDataNoOfTile();

		animMoveDown = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.move);
		animMoveDown.setAnimationListener(this);

		scanner = (Button) findViewById(R.id.scanner);

		Button playAgain = (Button) findViewById(R.id.playAgain);
		playAgain.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				playBtnClickSound();
				Intent intent = new Intent(HighScore.this, GameDisplay.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});

		Button screenshot = (Button) findViewById(R.id.takeScreenShot);
		screenshot.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				playBtnClickSound();
				scanner.setVisibility(View.VISIBLE);
				scanner.startAnimation(animMoveDown);
			}
		});

		findViewById(R.id.highScoreMainMenu).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						playBtnClickSound();
						Intent intent = new Intent(HighScore.this, Game.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
						startActivity(intent);
						finish();
					}
				});
		TextView tv = (TextView) findViewById(R.id.thisRoundTv);
		float textSize = tv.getTextSize();
		tv = (TextView) findViewById(R.id.title);

		if (Game.gameMode == Game.timerMode)
			title = ("Final Score\nTimed Mode : Tiles " + String
					.valueOf(noOfTile));
		else
			title = ("Final Score\nNormal Mode : Tiles " + String
					.valueOf(noOfTile));
		tv.setText(title);

		tv = (TextView) findViewById(R.id.thisRound);
		tv.setTextSize((float) (textSize * 1.1));

		if (Game.gameMode == Game.timerMode)
			curScore = TimerGame.currentScore;
		else if (Game.gameMode == Game.normalMode)
			curScore = NormalGame.currentScore;
		else
			curScore = LimitedMoveGame.currentScore;

		tv.setText(String.valueOf(curScore));
		tv = (TextView) findViewById(R.id.bestScore);
		tv.setTextSize((float) (textSize * 1.1));
		// tv.setVisibility(View.GONE);
		if (Game.gameMode == Game.timerMode)
			tv.setText(String
					.valueOf(Game.data[noOfTile - 3].timerBestScore[0]));
		else if (Game.gameMode == Game.normalMode)
			tv.setText(String
					.valueOf(Game.data[noOfTile - 3].normalBestScore[0]));
		else
			tv.setText(String
					.valueOf(Game.data[noOfTile - 3].limitedBestScore[0]));
		tv = (TextView) findViewById(R.id.bestScoreTv);

		Button fbshare = (Button) findViewById(R.id.fbShare);
		fbshare.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				playBtnClickSound();

				facebook = new FacebookFacade(HighScore.this,
						Constants.FACEBOOK_APP_ID);
				View view = findViewById(R.id.highScoreActivityCpntainer);
				view = view.getRootView();
				view.setDrawingCacheEnabled(true);
				final Bitmap bitmap = view.getDrawingCache();
				if (facebook.isAuthorized()) {
					publishImage(bitmap);
				} else {
					facebook.authorize(new AuthListener() {
						@Override
						public void onAuthSucceed() {
							publishImage(bitmap);
						}

						@Override
						public void onAuthFail(String error) {
							Toast.makeText(
									HighScore.this,
									"Facebook Authentication Failed" + "\n"
											+ error, Toast.LENGTH_LONG).show();

						}
					});
				}
			}
		});
	}

	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// super.onActivityResult(requestCode, resultCode, data);
	// System.out.println("hello");
	// // switch (requestCode) {
	// // case 1234:
	// System.out.println("data : " + data.toString());
	// if (resultCode == RESULT_OK) {
	// Uri selectedImage = data.getData();
	// System.out.println("URI : " + selectedImage.toString());
	// String[] filePathColumn = { MediaStore.Images.Media.DATA };
	//
	// Cursor cursor = getContentResolver().query(selectedImage,
	// filePathColumn, null, null, null);
	// cursor.moveToFirst();
	//
	// int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	// String filePath = cursor.getString(columnIndex);
	// cursor.close();
	//
	// System.out.println("filepath : " + filePath);
	// Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
	// publishImage(yourSelectedImage);
	// // finish();
	// // }
	// }
	// }

	private void publishImage(Bitmap bitmap) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] bitmapdata = stream.toByteArray();

		String msg = "OMG! I just scored  "
				+ String.valueOf(curScore)
				+ "  in #2048_Number_Puzzel_Game\n"
				+ "https://play.google.com/store/apps/details?id=com.inverted.tech.mission2048";
		Toast.makeText(HighScore.this,
				"Score posted successfully on your Facebook wall",
				Toast.LENGTH_LONG).show();
		facebook.publishImage(bitmapdata, msg);
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(HighScore.this, Game.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

	private void getScreenShot() {
		if (!Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			AlertDialog.Builder alert = new AlertDialog.Builder(HighScore.this);
			alert.setCancelable(true);
			alert.setIcon(R.drawable.storage);
			alert.setTitle("Storage Error");
			alert.setMessage("Memory Card not found!\n\nMake sure the Memory Card is insserted and not connected to your computer.");

			alert.setNegativeButton("Ok",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			AlertDialog alertDialog = alert.create();
			alertDialog.show();
			return;
		}

		View view = findViewById(R.id.highScoreActivityCpntainer);
		view = view.getRootView();
		view.setDrawingCacheEnabled(true);
		Bitmap bitmap = view.getDrawingCache();
		try {
			new File(captureDirPath).mkdirs();
			new File(copyDirPath).mkdirs();
			System.out.println("dir : " + captureDirPath);
			System.out.println("copyDirPath : " + copyDirPath);
			filePath = "/ScreenShot_"
					+ String.valueOf(System.currentTimeMillis()) + ".jpg";

			File file = new File(captureDirPath + filePath);
			System.out.println("filepath : " + filePath);

			file.createNewFile();
			FileOutputStream fos = null;
			fos = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();

			copyFileUsingStream(file, new File(copyDirPath + filePath));
			// MediaStore.Images.Media.insertImage(getContentResolver(),
			// file.getAbsolutePath(), "Screen", "screen");
			// Toast.makeText(HighScore.this,
			// "Screenshot saved at:\n" + file.getAbsolutePath(),
			// Toast.LENGTH_LONG).show();
		} catch (FileNotFoundException e) {
			showErrorAlertDialog(e);
		} catch (Exception e) {
			showErrorAlertDialog(e);
			e.printStackTrace();
		}
	}

	private void copyFileUsingStream(File source, File dest) throws IOException {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(source);
			os = new FileOutputStream(dest);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		} finally {
			is.close();
			os.close();
			Toast.makeText(HighScore.this,
					"Screenshot saved at:\n" + dest.getAbsolutePath(),
					Toast.LENGTH_LONG).show();
		}
	}

	private void showErrorAlertDialog(Exception e) {
		AlertDialog.Builder alert = new AlertDialog.Builder(HighScore.this);
		alert.setCancelable(true);
		System.out.println(e.getMessage());
		alert.setMessage("Error in Reading/Writing Memory Card \n"
				+ e.getMessage());
		alert.setIcon(R.drawable.storage);
		alert.setTitle("Storage");
		alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		AlertDialog alertDialog = alert.create();
		alertDialog.show();
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		scanner.setAnimation(null);
		scanner.setVisibility(View.INVISIBLE);
		getScreenShot();
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
	}

	@Override
	public void onAnimationStart(Animation animation) {
	}

	private void playBtnClickSound() {
		if (!isMusic || tileGenClickPlayer.isPlaying())
			return;
		tileGenClickPlayer.start();
	}

	private void initMusicPlayer() {
		tileGenClickPlayer = MediaPlayer.create(HighScore.this,
				R.raw.menu_btn_clicked);
		tileGenClickPlayer.setVolume(100, 100);
		DatabaseHandler db = new DatabaseHandler(this);
		isMusic = db.getGameDataMusic();
	}

}

package com.inverted.tech.mission2048.SupportActivities;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.inverted.tech.mission2048.Game;
import com.inverted.tech.mission2048.GameDisplay;
import com.inverted.tech.mission2048.R;
import com.inverted.tech.mission2048.databaseConnection.DatabaseHandler;

public class Resume_Pause extends Activity {

	private MediaPlayer tileGenClickPlayer;
	private boolean isMusic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.resume_pause_option_display);
		initMusicPlayer();

		Button btn = (Button) findViewById(R.id.mainMenuResume);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				playBtnClickSound();
				GameDisplay.startedAfterPause = false;
				Intent intent = new Intent(Resume_Pause.this, Game.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);

			}
		});

		btn = (Button) findViewById(R.id.resume);
		btn.findViewById(R.id.resume).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				playBtnClickSound();
				GameDisplay.startedAfterPause = true;

				Intent intent = new Intent(Resume_Pause.this, GameDisplay.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
		});

	}

	private void playBtnClickSound() {
		if (!isMusic || tileGenClickPlayer.isPlaying())
			return;
		tileGenClickPlayer.start();
	}

	private void initMusicPlayer() {
		tileGenClickPlayer = MediaPlayer.create(Resume_Pause.this,
				R.raw.menu_btn_clicked);
		tileGenClickPlayer.setVolume(100, 100);
		DatabaseHandler db = new DatabaseHandler(this);
		isMusic = db.getGameDataMusic();
	}

	@Override
	public void onBackPressed() {
		return;
	}
}

package com.inverted.tech.mission2048;

import com.inverted.tech.mission2048.databaseConnection.DatabaseHandler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class RateUs extends Activity {

	private boolean isMusic;
	private MediaPlayer btnClickPlayer;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		DatabaseHandler db = new DatabaseHandler(this);
		isMusic = db.getGameDataMusic();
		btnClickPlayer = MediaPlayer
				.create(RateUs.this, R.raw.menu_btn_clicked);
		btnClickPlayer.setVolume(100, 100);

		setContentView(R.layout.rate_us);

		Button rateNow = (Button) findViewById(R.id.rateNow);
		rateNow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				playBtnClickSound();
				DatabaseHandler db = new DatabaseHandler(RateUs.this);
				db.setGameDataRateUs(1);

				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setData(Uri
						.parse("market://details?id=com.inverted.tech.mission2048"));

				startActivity(intent);
			}
		});
		Button rateLater = (Button) findViewById(R.id.rateLater);
		rateLater.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				playBtnClickSound();
				finish();
			}
		});
	}

	private void playBtnClickSound() {
		if (btnClickPlayer.isPlaying() || !isMusic)
			return;
		btnClickPlayer.start();
	}

}

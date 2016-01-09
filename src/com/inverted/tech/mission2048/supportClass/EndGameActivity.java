package com.inverted.tech.mission2048.supportClass;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.inverted.tech.mission2048.R;

public class EndGameActivity extends Activity {

	private Animation animZoomIn;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		Display display = getWindowManager().getDefaultDisplay();
		int screenWidth = display.getWidth();
		setContentView(R.layout.activity_end_game);

		Typeface typeFace = Typeface.createFromAsset(getAssets(), "MAIAN.TTF");

		findViewById(R.id.EndGameActivityLayout).setMinimumWidth(
				(int) (screenWidth * 0.75));

		TextView tv = (TextView) findViewById(R.id.EndGameActivityMessage);

		tv.setTypeface(typeFace, Typeface.BOLD);

		animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.zoom_in_out);

		Button btn = (Button) findViewById(R.id.playOn);
		btn.startAnimation(animZoomIn);
		btn.setMinimumWidth(screenWidth / 5);
		btn.setTypeface(typeFace, Typeface.BOLD);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		btn = (Button) findViewById(R.id.EndGame);
		btn.startAnimation(animZoomIn);
		btn.setMinimumWidth(screenWidth / 5);
		btn.setTypeface(typeFace, Typeface.BOLD);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		});
	}

	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}

}

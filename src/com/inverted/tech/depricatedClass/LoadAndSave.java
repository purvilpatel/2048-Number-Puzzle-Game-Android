package com.inverted.tech.depricatedClass;

import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.inverted.tech.mission2048.GameDisplay;
import com.inverted.tech.mission2048.R;
import com.inverted.tech.mission2048.databaseConnection.DatabaseHandler;
import com.inverted.tech.mission2048.databaseConnection.SaveAndLoad;

public class LoadAndSave extends Activity {

	LinearLayout ll;
	LinearLayout.LayoutParams lParams;
	int selectedButton = -1;
	private MediaPlayer tileGenClickPlayer;
	private boolean isMusic;

	public static boolean loadGame = false;
	public static String LOAD_INDEX = "LOAD_INDEX";

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_save_load);
		initMusicPlayer();

		ll = (LinearLayout) findViewById(R.id.layout);
		lParams = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);

		findViewById(R.id.confirmLoad).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						playBtnClickSound();
						if (selectedButton == -1) {
							Toast.makeText(LoadAndSave.this,
									"Select one game state to load",
									Toast.LENGTH_SHORT).show();
							return;
						}
						loadGame = true;
						Intent intent = new Intent(LoadAndSave.this,
								GameDisplay.class);
						intent.putExtra(LOAD_INDEX, selectedButton);

						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
						startActivity(intent);
						finish();
					}
				});
	}

	@Override
	protected void onResume() {
		super.onResume();
		generateInitView();
	}

	private void generateInitView() {
		DatabaseHandler db = new DatabaseHandler(this);
		int stateCount = db.getGameStateCount();
		ll.removeAllViews();

		if (stateCount == 0) {
			Button btn = new Button(this);
			btn.setLayoutParams(lParams);
			btn.setText("No Game Saved");
			btn.setTextColor(Color.WHITE);
			// btn.setTextSize(btn.getTextSize() * 2);
			btn.setPadding(30, 30, 10, 5);
			btn.setBackgroundColor(Color.alpha(0));
			ll.addView(btn);
		} else {
			List<SaveAndLoad> gameStateList = db.getAllGameState();
			Iterator<SaveAndLoad> itr = gameStateList.iterator();

			while (itr.hasNext()) {
				TypedValue typedValue = new TypedValue();
				((Activity) LoadAndSave.this).getTheme().resolveAttribute(
						android.R.attr.textAppearanceLarge, typedValue, true);

				SaveAndLoad loadGame = itr.next();
				Button btn = new Button(this);

				btn.setLayoutParams(lParams);
				btn.setBackgroundResource(R.drawable.save_load_button);
				btn.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

				btn.setTextColor(Color.WHITE);

				// btn.setTextSize(btn.getTextSize() * 2);
				btn.setPadding(30, 5, 10, 5);

				btn.setId(loadGame.getRow_id());

				btn.setText("Game : " + loadGame.getDisplayvalue());
				btn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						for (int i = 0; i < ll.getChildCount(); i++) {
							ll.getChildAt(i).setBackgroundResource(
									R.drawable.save_load_button);
						}
						findViewById(v.getId()).setBackgroundResource(
								R.drawable.save_load_button_selected);
						selectedButton = v.getId();
					}
				});
				// convertIdtoString(s.getType()));
				ll.addView(btn);
			}
		}
	}

	private void playBtnClickSound() {
		if (!isMusic || tileGenClickPlayer.isPlaying())
			return;
		tileGenClickPlayer.start();
	}

	private void initMusicPlayer() {
		tileGenClickPlayer = MediaPlayer.create(LoadAndSave.this,
				R.raw.menu_btn_clicked);
		tileGenClickPlayer.setVolume(100, 100);
		DatabaseHandler db = new DatabaseHandler(this);
		isMusic = db.getGameDataMusic();
	}
}

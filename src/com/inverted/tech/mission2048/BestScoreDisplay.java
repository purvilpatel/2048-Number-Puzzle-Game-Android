package com.inverted.tech.mission2048;

import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.inverted.tech.mission2048.databaseConnection.DatabaseHandler;
import com.inverted.tech.mission2048.databaseConnection.highScoreValue;

public class BestScoreDisplay extends Activity {

	private MediaPlayer tileGenClickPlayer;
	private boolean isMusic;
	public static String GAME_MODE = "game mode";

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.best_score_list);
		initMusicPlayer();

		Bundle bundle = getIntent().getExtras();
		int gameMode = bundle.getInt(GAME_MODE);
		System.out.println(gameMode + "   ~    " + getGameModeString(gameMode));

		TextView tv = (TextView) findViewById(R.id.bestScoreDisplayMessage);
		tv.setText(getGameModeString(gameMode));

		// setUp tabHost view
		TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
		tabHost.setup();

		// get reference to tabs
		TabSpec tab3Spec = tabHost.newTabSpec("Tile 3");
		tab3Spec.setIndicator("Tile  3");

		TabSpec tab4Spec = tabHost.newTabSpec("Tile 4");
		tab4Spec.setIndicator("Tile  4");

		TabSpec tab5Spec = tabHost.newTabSpec("Tile 5");
		tab5Spec.setIndicator("Tile  5");

		TabSpec tab6Spec = tabHost.newTabSpec("Tile 6");
		tab6Spec.setIndicator("Tile  6");

		TabSpec tab7Spec = tabHost.newTabSpec("Tile 7");
		tab7Spec.setIndicator("Tile  7");

		TabSpec tab8Spec = tabHost.newTabSpec("Tile 8");
		tab8Spec.setIndicator("Tile  8");

		LinearLayout tab3Layout = (LinearLayout) findViewById(R.id.tab_3);
		LinearLayout tab4Layout = (LinearLayout) findViewById(R.id.tab_4);
		LinearLayout tab5Layout = (LinearLayout) findViewById(R.id.tab_5);
		LinearLayout tab6Layout = (LinearLayout) findViewById(R.id.tab_6);
		LinearLayout tab7Layout = (LinearLayout) findViewById(R.id.tab_7);
		LinearLayout tab8Layout = (LinearLayout) findViewById(R.id.tab_8);

		// Indexes of 3 Tab Layout
		int idx3, idx4, idx5, idx6, idx7, idx8;
		idx3 = idx4 = idx5 = idx6 = idx7 = idx8 = 0;

		// System.out.println("count" + tabLayout.getChildCount());

		DatabaseHandler db = new DatabaseHandler(BestScoreDisplay.this);

		// get list filtered by game mode
		List<highScoreValue> scoreList = db.getAllHighScore(gameMode);
		Iterator<highScoreValue> itr = scoreList.iterator();
		System.out.println(scoreList.size());

		// Travel through list
		while (itr.hasNext()) {
			highScoreValue currentObj = itr.next();
			if (currentObj.get_score() != 0) {
				// if tile 3 than add to tab_3
				if (currentObj.get_tile() == 3) {
					LinearLayout disParent = (LinearLayout) tab3Layout
							.getChildAt(idx3);

					TextView rank = (TextView) disParent.getChildAt(0);
					rank.setText("   " + getRomanIndex(idx3 + 1));

					TextView score = (TextView) disParent.getChildAt(1);
					score.setText(String.valueOf(currentObj.get_score()));

					System.out.println("idx3 : " + idx3);
					idx3++;
				}
				// if tile 4 than add to tab_4
				else if (currentObj.get_tile() == 4) {
					LinearLayout disParent = (LinearLayout) tab4Layout
							.getChildAt(idx4);

					TextView rank = (TextView) disParent.getChildAt(0);
					rank.setText("   " + getRomanIndex(idx4 + 1));

					TextView score = (TextView) disParent.getChildAt(1);
					score.setText(String.valueOf(currentObj.get_score()));
					System.out.println("idx4 : " + idx4);
					idx4++;
				}
				// if tile 5 than add to tab_5
				else if (currentObj.get_tile() == 5) {
					LinearLayout disParent = (LinearLayout) tab5Layout
							.getChildAt(idx5);

					TextView rank = (TextView) disParent.getChildAt(0);
					rank.setText("   " + getRomanIndex(idx5 + 1));

					TextView score = (TextView) disParent.getChildAt(1);
					score.setText(String.valueOf(currentObj.get_score()));
					System.out.println("idx5 : " + idx5);
					idx5++;
				}
				// if tile 6 than add to tab_6
				else if (currentObj.get_tile() == 6) {
					LinearLayout disParent = (LinearLayout) tab6Layout
							.getChildAt(idx6);

					TextView rank = (TextView) disParent.getChildAt(0);
					rank.setText("   " + getRomanIndex(idx6 + 1));

					TextView score = (TextView) disParent.getChildAt(1);
					score.setText(String.valueOf(currentObj.get_score()));
					System.out.println("idx6 : " + idx6);
					idx6++;
				}
				// if tile 7 than add to tab_7
				else if (currentObj.get_tile() == 7) {
					LinearLayout disParent = (LinearLayout) tab7Layout
							.getChildAt(idx7);

					TextView rank = (TextView) disParent.getChildAt(0);
					rank.setText("   " + getRomanIndex(idx7 + 1));

					TextView score = (TextView) disParent.getChildAt(1);
					score.setText(String.valueOf(currentObj.get_score()));
					System.out.println("idx7 : " + idx7);
					idx7++;
				}
				// if tile 8 than add to tab_8
				else if (currentObj.get_tile() == 8) {
					LinearLayout disParent = (LinearLayout) tab8Layout
							.getChildAt(idx8);

					TextView rank = (TextView) disParent.getChildAt(0);
					rank.setText("   " + getRomanIndex(idx8 + 1));

					TextView score = (TextView) disParent.getChildAt(1);
					score.setText(String.valueOf(currentObj.get_score()));
					System.out.println("idx8 : " + idx8);
					idx8++;
				}
			}
			System.out.println("" + currentObj.get_mode() + " _ "
					+ currentObj.get_tile() + " _ " + currentObj.get_score());
		}

		// set message for empty score board
		@SuppressWarnings("deprecation")
		LinearLayout.LayoutParams noScoreparams = new LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT, 2.0f);
		// set message for empty score board
		if (idx3 == 0) {
			LinearLayout disParent = (LinearLayout) tab3Layout.getChildAt(0);
			TextView rank = (TextView) disParent.getChildAt(0);
			rank.setLayoutParams(noScoreparams);
			rank.setText("No score to display");
			rank.setGravity(Gravity.CENTER_HORIZONTAL);
			disParent.getChildAt(1).setVisibility(View.GONE);
			idx3++;
		}
		// set message for empty score board
		if (idx4 == 0) {
			LinearLayout disParent = (LinearLayout) tab4Layout.getChildAt(0);
			TextView rank = (TextView) disParent.getChildAt(0);
			rank.setLayoutParams(noScoreparams);
			rank.setText("No score to display");
			rank.setGravity(Gravity.CENTER_HORIZONTAL);
			disParent.getChildAt(1).setVisibility(View.GONE);
			idx4++;
		}
		// set message for empty score board
		if (idx5 == 0) {
			LinearLayout disParent = (LinearLayout) tab5Layout.getChildAt(0);
			TextView rank = (TextView) disParent.getChildAt(0);
			rank.setLayoutParams(noScoreparams);
			rank.setText("No score to display");
			rank.setGravity(Gravity.CENTER_HORIZONTAL);
			disParent.getChildAt(1).setVisibility(View.GONE);
			idx5++;
		}
		// set message for empty score board
		if (idx6 == 0) {
			LinearLayout disParent = (LinearLayout) tab6Layout.getChildAt(0);
			TextView rank = (TextView) disParent.getChildAt(0);
			rank.setLayoutParams(noScoreparams);
			rank.setText("No score to display");
			rank.setGravity(Gravity.CENTER_HORIZONTAL);
			disParent.getChildAt(1).setVisibility(View.GONE);
			idx6++;
		}
		// set message for empty score board
		if (idx7 == 0) {
			LinearLayout disParent = (LinearLayout) tab7Layout.getChildAt(0);
			TextView rank = (TextView) disParent.getChildAt(0);
			rank.setLayoutParams(noScoreparams);
			rank.setText("No score to display");
			rank.setGravity(Gravity.CENTER_HORIZONTAL);
			disParent.getChildAt(1).setVisibility(View.GONE);
			idx7++;
		}
		// set message for empty score board
		if (idx8 == 0) {
			LinearLayout disParent = (LinearLayout) tab8Layout.getChildAt(0);
			TextView rank = (TextView) disParent.getChildAt(0);
			rank.setLayoutParams(noScoreparams);
			rank.setText("No score to display");
			rank.setGravity(Gravity.CENTER_HORIZONTAL);
			disParent.getChildAt(1).setVisibility(View.GONE);
			idx8++;
		}

		// set visibility to View.GONE for other LinearLayout
		for (int i = idx3; i < tab3Layout.getChildCount(); i++) {
			tab3Layout.getChildAt(i).setVisibility(View.GONE);
		}

		for (int i = idx4; i < tab4Layout.getChildCount(); i++) {
			tab4Layout.getChildAt(i).setVisibility(View.GONE);
		}

		for (int i = idx5; i < tab5Layout.getChildCount(); i++) {
			tab5Layout.getChildAt(i).setVisibility(View.GONE);
		}

		for (int i = idx6; i < tab6Layout.getChildCount(); i++) {
			tab6Layout.getChildAt(i).setVisibility(View.GONE);
		}

		for (int i = idx7; i < tab7Layout.getChildCount(); i++) {
			tab7Layout.getChildAt(i).setVisibility(View.GONE);
		}

		for (int i = idx8; i < tab8Layout.getChildCount(); i++) {
			tab8Layout.getChildAt(i).setVisibility(View.GONE);
		}

		tab3Spec.setContent(R.id.tabContainer_3);
		tabHost.addTab(tab3Spec);

		tab4Spec.setContent(R.id.tabContainer_4);
		tabHost.addTab(tab4Spec);

		tab5Spec.setContent(R.id.tabContainer_5);
		tabHost.addTab(tab5Spec);

		tab6Spec.setContent(R.id.tabContainer_6);
		tabHost.addTab(tab6Spec);

		tab7Spec.setContent(R.id.tabContainer_7);
		tabHost.addTab(tab7Spec);

		tab8Spec.setContent(R.id.tabContainer_8);
		tabHost.addTab(tab8Spec);

		Button btn = (Button) findViewById(R.id.bestScoreListMainMenu);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				playBtnClickSound();
				finish();
			}
		});
	}

	private String getRomanIndex(int i) {
		switch (i) {
		case 1:
			return "1st";
		case 2:
			return "2nd";
		case 3:
			return "3rd";
		case 4:
			return "4th";
		case 5:
			return "5th";
		case 6:
			return "6th";
		case 7:
			return "7th";
		case 8:
			return "8th";
		case 9:
			return "9th";
		case 10:
			return "10th";
		}
		return "";
	}

	private void playBtnClickSound() {
		if (!isMusic || tileGenClickPlayer.isPlaying())
			return;
		tileGenClickPlayer.start();
	}

	private void initMusicPlayer() {
		tileGenClickPlayer = MediaPlayer.create(BestScoreDisplay.this,
				R.raw.menu_btn_clicked);
		tileGenClickPlayer.setVolume(100, 100);
		DatabaseHandler db = new DatabaseHandler(this);
		isMusic = db.getGameDataMusic();
	}

	private String getGameModeString(int gameMode) {
		String mode = "";
		switch (gameMode) {
		case highScoreValue.MODE_NORMAL:
			mode = "Normal Mode";
			;
			break;
		case highScoreValue.MODE_TIMED:
			mode = "Time Rush";
			break;
		case highScoreValue.MODE_LIMITED_MOVES:
			mode = "Limited Moves Mode";
			break;
		}
		return mode;
	}
}
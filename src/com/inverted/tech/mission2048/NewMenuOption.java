package com.inverted.tech.mission2048;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.inverted.tech.mission2048.SupportActivities.BestScoreDisplay;
import com.inverted.tech.mission2048.databaseConnection.DatabaseHandler;
import com.inverted.tech.mission2048.databaseConnection.highScoreValue;

public class NewMenuOption extends Activity implements OnItemSelectedListener {

	private Animation animZoomIn;
	protected int gameMode;
	private int screenWidth;
	private Typeface typeFace;
	private Spinner gameModeSpinner;
	private Spinner targetTileSpinner;
	private Spinner timeRushSpinner;
	private Spinner tileSpinner;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		Display display = getWindowManager().getDefaultDisplay();
		display.getHeight();
		screenWidth = display.getWidth();

		setContentView(R.layout.activity_new_menu_option);

		typeFace = Typeface.createFromAsset(getAssets(), "MAIAN.TTF");

		findViewById(R.id.newMenuOptionLayout).setMinimumWidth(
				(int) (screenWidth * 0.5));

		TextView tv = (TextView) findViewById(R.id.gameModeMenuOption);
		tv.setTypeface(typeFace, Typeface.BOLD_ITALIC);

		tv = (TextView) findViewById(R.id.targetTileMenuOtion);
		tv.setTypeface(typeFace, Typeface.BOLD_ITALIC);

		tv = (TextView) findViewById(R.id.timeRushMenuOtion);
		tv.setTypeface(typeFace, Typeface.BOLD_ITALIC);

		tv = (TextView) findViewById(R.id.tileMenuOtion);
		tv.setTypeface(typeFace, Typeface.BOLD_ITALIC);

		animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.zoom_in_out);

		Button btn = (Button) findViewById(R.id.newMenuOptionSet);
		btn.startAnimation(animZoomIn);
		btn.setMinimumWidth(screenWidth / 4);
		// btn.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
		// (float) (screenHeight * 0.01));
		btn.setTypeface(typeFace, Typeface.BOLD);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DatabaseHandler dbDatabaseHandler = new DatabaseHandler(
						getApplicationContext());
				setGameMode(dbDatabaseHandler);
				setTargetTile(dbDatabaseHandler);
				setNoTiles(dbDatabaseHandler);

				// dbDatabaseHandler.setGameDataGameMode(Game.normalMode);
				System.out.print("new game");
				Intent intent = new Intent(getApplicationContext(),
						GameDisplay.class);
				// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				intent.putExtra(GameDisplay.TIME_RUSH, setTimeRush());
				startActivity(intent);
				finish();
			}
		});

		btn = (Button) findViewById(R.id.newMenuOptionRateUs);
		btn.setTypeface(typeFace, Typeface.BOLD);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setData(Uri
						.parse("market://details?id=com.inverted.tech.mission2048"));
				startActivity(intent);
				finish();
			}
		});

		btn = (Button) findViewById(R.id.newMenuOptionLikeUs);
		btn.setTypeface(typeFace, Typeface.BOLD);
		btn.setOnClickListener(new OnClickListener() {

			@SuppressLint("SetJavaScriptEnabled")
			@Override
			public void onClick(View v) {
				try {
					@SuppressWarnings("unused")
					ApplicationInfo info = getPackageManager()
							.getApplicationInfo("com.facebook.katana", 0);
					String FbUrl = "fb://page/" + "425926867544590";
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(FbUrl));
					startActivity(intent);
					finish();
				} catch (PackageManager.NameNotFoundException e) {
					e.printStackTrace();
					System.out.println(e.getMessage());
					String url = "https://touch.facebook.com/pages/2048-Number-Puzzle-Game/425926867544590";
					WebView browser = new WebView(NewMenuOption.this);
					browser.getSettings().setLoadsImagesAutomatically(true);
					browser.getSettings().setJavaScriptEnabled(true);
					browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
					browser.loadUrl(url);
					setContentView(browser);
				}
			}
		});

		btn = (Button) findViewById(R.id.newMenuOptionBestScore);
		btn.setTypeface(typeFace, Typeface.BOLD);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NewMenuOption.this,
						BestScoreDisplay.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				gameMode = getGameMode(gameModeSpinner
						.getSelectedItemPosition());
				System.out.println(gameMode);
				intent.putExtra(BestScoreDisplay.GAME_MODE, gameMode);
				startActivity(intent);
				finish();
			}
		});

		addItemsOnGameModeSpinner();
		addItemsTargetTileSpinner();
		addItemsTimeRushSpinner();
		addItemsTileSpinner();
	}

	protected int getGameMode(int selectedItemPosition) {
		switch (selectedItemPosition) {
		case 0:
			return highScoreValue.MODE_NORMAL;
		case 1:
			return highScoreValue.MODE_TIMED;
		case 2:
			return highScoreValue.MODE_LIMITED_MOVES;
		}
		return highScoreValue.MODE_NORMAL;
	}

	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}

	private int setTimeRush() {
		switch (timeRushSpinner.getSelectedItemPosition()) {
		case 0:
			return 60;
		case 1:
			return 120;

		case 2:
			return 240;

		case 3:
			return 360;

		case 4:
			return 600;

		default:
			return 120;
		}
	}

	private void setNoTiles(DatabaseHandler dbDatabaseHandler) {
		switch (tileSpinner.getSelectedItemPosition()) {
		case 0:
			dbDatabaseHandler.setGameDataNoOfTile(3);
			break;
		case 1:
			dbDatabaseHandler.setGameDataNoOfTile(4);
			break;
		case 2:
			dbDatabaseHandler.setGameDataNoOfTile(5);
			break;
		case 3:
			dbDatabaseHandler.setGameDataNoOfTile(6);
			break;
		case 4:
			dbDatabaseHandler.setGameDataNoOfTile(7);
			break;
		case 5:
			dbDatabaseHandler.setGameDataNoOfTile(8);
			break;
		default:
			dbDatabaseHandler.setGameDataNoOfTile(4);
			break;
		}
	}

	private void setTargetTile(DatabaseHandler dbDatabaseHandler) {
		switch (targetTileSpinner.getSelectedItemPosition()) {
		case 0:
			dbDatabaseHandler.setGameDataMaxTile(512);
			break;
		case 1:
			dbDatabaseHandler.setGameDataMaxTile(1024);
			break;
		case 2:
			dbDatabaseHandler.setGameDataMaxTile(2048);
			break;
		case 3:
			dbDatabaseHandler.setGameDataMaxTile(4096);
			break;
		case 4:
			dbDatabaseHandler.setGameDataMaxTile(8192);
			break;
		default:
			dbDatabaseHandler.setGameDataMaxTile(2048);
			break;
		}
	}

	private void setGameMode(DatabaseHandler dbDatabaseHandler) {
		switch (gameModeSpinner.getSelectedItemPosition()) {
		case 0:
			dbDatabaseHandler.setGameDataGameMode(Game.normalMode);
			break;
		case 1:
			dbDatabaseHandler.setGameDataGameMode(Game.timerMode);
			break;
		case 2:
			dbDatabaseHandler.setGameDataGameMode(Game.limitedMoveMode);
			break;
		default:
			dbDatabaseHandler.setGameDataGameMode(Game.normalMode);
			break;
		}
		System.out.print("new game");
	}

	// add items into spinner dynamically
	public void addItemsOnGameModeSpinner() {
		gameModeSpinner = (Spinner) findViewById(R.id.gameModeSpinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.gameMode, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		gameModeSpinner.setAdapter(adapter);
		gameModeSpinner.setSelection(0, true);
		gameModeSpinner.setOnItemSelectedListener(this);
	}

	public void addItemsTargetTileSpinner() {
		targetTileSpinner = (Spinner) findViewById(R.id.targetTileSpinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.targetTile, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		targetTileSpinner.setAdapter(adapter);
		targetTileSpinner.setOnItemSelectedListener(this);
		targetTileSpinner.setSelection(2, true);
	}

	public void addItemsTimeRushSpinner() {
		timeRushSpinner = (Spinner) findViewById(R.id.timeRushSpinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.timeRush, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		timeRushSpinner.setAdapter(adapter);
		timeRushSpinner.setSelection(1, true);
		timeRushSpinner.setOnItemSelectedListener(this);
		timeRushSpinner.setEnabled(false);
	}

	public void addItemsTileSpinner() {
		tileSpinner = (Spinner) findViewById(R.id.tileSpinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.tile, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		tileSpinner.setAdapter(adapter);
		tileSpinner.setSelection(1, true);
		tileSpinner.setOnItemSelectedListener(this);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		if (parent == gameModeSpinner) {
			if (pos == 1)
				timeRushSpinner.setEnabled(true);
			else
				timeRushSpinner.setEnabled(false);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

}

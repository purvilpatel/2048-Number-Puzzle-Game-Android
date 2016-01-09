package com.inverted.tech.mission2048;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RadioGroup;

import com.inverted.tech.depricatedClass.LoadAndSave;
import com.inverted.tech.mission2048.databaseConnection.DatabaseHandler;
import com.inverted.tech.mission2048.databaseConnection.highScoreValue;

public class MenuOptions extends Activity {

	public static int gameMode;
	private MediaPlayer btnClickPlayer;
	private boolean likeUs = false;
	private boolean isMusic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		DatabaseHandler db = new DatabaseHandler(MenuOptions.this);
		isMusic = db.getGameDataMusic();

		btnClickPlayer = MediaPlayer.create(MenuOptions.this,
				R.raw.menu_btn_clicked);
		btnClickPlayer.setVolume(100, 100);

		MainMenuDispay();
	}

	private void MainMenuDispay() {
		setContentView(R.layout.menu);

		Button btn = (Button) findViewById(R.id.likeUs);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				playBtnClickSound();
				likeUs = true;
				likeUsView();
			}
		});

		btn = (Button) findViewById(R.id.rateUsMenu);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				playBtnClickSound();
				Intent newIntent = new Intent(MenuOptions.this, RateUs.class);
				startActivity(newIntent);
			}
		});

		final Button soundBtn = (Button) findViewById(R.id.soundFX);
		final DatabaseHandler db = new DatabaseHandler(MenuOptions.this);
		if (!db.getGameDataMusic()) {
			soundBtn.setBackgroundResource(R.drawable.menu_button__sound);
			soundBtn.setText("OFF");
			isMusic = false;
		}
		soundBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (db.getGameDataMusic()) {
					soundBtn.setBackgroundResource(R.drawable.menu_button__sound);
					soundBtn.setText("OFF");
					db.setGameDataMusic(0);
					isMusic = false;
				} else {
					isMusic = true;
					playBtnClickSound();
					soundBtn.setBackgroundResource(R.drawable.menu_button__4);
					soundBtn.setText("ON");
					db.setGameDataMusic(1);
				}
			}
		});

		btn = (Button) findViewById(R.id.saveLoadMenu);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				playBtnClickSound();
				Intent newIntent = new Intent(MenuOptions.this,
						LoadAndSave.class);
				startActivity(newIntent);
			}
		});

		btn = (Button) findViewById(R.id.highScoreMenu);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				playBtnClickSound();
				final Dialog dlg = new Dialog(MenuOptions.this);
				dlg.setContentView(R.layout.mode_selector);
				dlg.setTitle("Select Mode");
				final RadioGroup radioGrp = (RadioGroup) dlg
						.findViewById(R.id.radioMode);

				radioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						playBtnClickSound();
						switch (checkedId) {
						case R.id.normalMode_Radio:
							gameMode = highScoreValue.MODE_NORMAL;
							break;
						case R.id.timerMode_Radio:
							gameMode = highScoreValue.MODE_TIMED;
							break;
						case R.id.limitedMode_Radio:
							gameMode = highScoreValue.MODE_LIMITED_MOVES;
							break;
						}
					}
				});

				Button btnSet = (Button) dlg.findViewById(R.id.set);

				btnSet.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						playBtnClickSound();
						Intent intent = new Intent(MenuOptions.this,
								BestScoreDisplay.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
						System.out.println(gameMode);
						intent.putExtra(BestScoreDisplay.GAME_MODE, gameMode);
						startActivity(intent);
						dlg.dismiss();
					}
				});
				dlg.setCancelable(true);
				dlg.show();

				switch (gameMode) {
				case highScoreValue.MODE_NORMAL:
					radioGrp.check(R.id.normalMode_Radio);
					break;

				case highScoreValue.MODE_TIMED:
					radioGrp.check(R.id.timerMode_Radio);
					break;

				case highScoreValue.MODE_LIMITED_MOVES:
					radioGrp.check(R.id.limitedMode_Radio);
					break;
				}
			}
		});

		btn = (Button) findViewById(R.id.tileSelectorMenu);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				playBtnClickSound();
				final DatabaseHandler db = new DatabaseHandler(MenuOptions.this);
				final Dialog dlg = new Dialog(MenuOptions.this);
				dlg.setContentView(R.layout.tile_selector_dialog);
				dlg.setTitle("Select Tile");

				final RadioGroup radioTileSelector = (RadioGroup) dlg
						.findViewById(R.id.radioTile);
				radioTileSelector
						.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(RadioGroup group,
									int checkedId) {
								playBtnClickSound();
								switch (checkedId) {
								case R.id.Tile_3:
									db.setGameDataNoOfTile(3);
									break;
								case R.id.Tile_4:
									db.setGameDataNoOfTile(4);
									break;
								case R.id.Tile_5:
									db.setGameDataNoOfTile(5);
									break;
								default:
									db.setGameDataNoOfTile(4);
									break;
								}
							}
						});

				final RadioGroup radiotargetTile = (RadioGroup) dlg
						.findViewById(R.id.radioTile);
				radiotargetTile
						.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(RadioGroup group,
									int checkedId) {
								playBtnClickSound();

								switch (checkedId) {
								case R.id.Tile_1024:
									db.setGameDataMaxTile(1024);
									break;
								case R.id.Tile_2048:
									db.setGameDataMaxTile(2048);
									break;
								case R.id.Tile_4096:
									db.setGameDataMaxTile(4096);
									break;
								default:
									db.setGameDataMaxTile(2048);
									break;
								}
							}
						});

				Button btnSet = (Button) dlg.findViewById(R.id.set);
				btnSet.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						playBtnClickSound();

						// set target tile
						switch (radiotargetTile.getCheckedRadioButtonId()) {
						case R.id.Tile_1024:
							db.setGameDataMaxTile(1024);
							break;
						case R.id.Tile_2048:
							db.setGameDataMaxTile(2048);
							break;
						case R.id.Tile_4096:
							db.setGameDataMaxTile(4096);
							break;
						default:
							db.setGameDataMaxTile(2048);
							break;
						}

						// set grid tile
						switch (radioTileSelector.getCheckedRadioButtonId()) {
						case R.id.Tile_3:
							db.setGameDataNoOfTile(3);
							break;
						case R.id.Tile_4:
							db.setGameDataNoOfTile(4);
							break;
						case R.id.Tile_5:
							db.setGameDataNoOfTile(5);
							break;
						default:
							db.setGameDataNoOfTile(4);
							break;
						}
						dlg.dismiss();
					}
				});

				switch (db.getGameDataNoOfTile()) {
				case 3:
					radioTileSelector.check(R.id.Tile_3);
					break;
				case 4:
					radioTileSelector.check(R.id.Tile_4);
					break;
				case 5:
					radioTileSelector.check(R.id.Tile_5);
					break;
				default:
					radioTileSelector.check(R.id.Tile_4);
					break;
				}

				switch (db.getGameDataMaxTile()) {
				case 1024:
					radiotargetTile.check(R.id.Tile_1024);
					break;
				case 2048:
					radiotargetTile.check(R.id.Tile_2048);
					break;
				case 4096:
					radiotargetTile.check(R.id.Tile_4096);
					break;
				default:
					radiotargetTile.check(R.id.Tile_2048);
					break;
				}
				dlg.setCancelable(true);
				dlg.show();
			}
		});

		btn = (Button) findViewById(R.id.mainMenu);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				playBtnClickSound();
				finish();
			}
		});
	}

	@SuppressLint("SetJavaScriptEnabled")
	protected void likeUsView() {
		// String FbUrl = "fb://page/" + "425926867544590";
		// Intent intent = new Intent(Intent.ACTION_VIEW);
		// intent.setData(Uri.parse(FbUrl));

		DatabaseHandler db = new DatabaseHandler(MenuOptions.this);
		db.setGameDataLikeUs(1);
		String url = "https://touch.facebook.com/pages/2048-Number-Puzzle-Game/425926867544590";
		WebView browser = new WebView(MenuOptions.this);
		browser.getSettings().setLoadsImagesAutomatically(true);
		browser.getSettings().setJavaScriptEnabled(true);
		browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		browser.loadUrl(url);
		setContentView(browser);
	}

	private void playBtnClickSound() {
		if (btnClickPlayer.isPlaying() || !isMusic)
			return;
		btnClickPlayer.start();
	}

	@Override
	public void onBackPressed() {
		if (likeUs) {
			MainMenuDispay();
			return;
		}
	}

}

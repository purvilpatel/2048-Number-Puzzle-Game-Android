package com.inverted.tech.mission2048;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.inverted.tech.mission2048.databaseConnection.DatabaseHandler;
import com.inverted.tech.mission2048.databaseConnection.highScoreValue;

public class NewHighscoreActivity extends Activity {
	public static String BASE_SCORE = "base score";
	public static String HIGH_NUMBER_BONUS = "high number bonus";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_new_highscore);

		Bundle bundle = getIntent().getExtras();
		int baseScore = bundle.getInt(BASE_SCORE);
		int highNumberBonus = bundle.getInt(HIGH_NUMBER_BONUS);
		int total = baseScore + highNumberBonus;
		int highScore = total;
		int gameMode, noOfTile;

		System.out.println(baseScore);
		System.out.println(highNumberBonus);
		System.out.println(total);
		System.out.println(Math.ceil(total * 0.1));

		DatabaseHandler dbHandler = new DatabaseHandler(
				NewHighscoreActivity.this);
		dbHandler.setGameDataPoints((int) (dbHandler.getGameDataPoints() + Math
				.ceil(total * 0.1)));
		gameMode = dbHandler.getGameDataGameMode();
		noOfTile = dbHandler.getGameDataNoOfTile();
		System.out.println(noOfTile);

		TextView tv = (TextView) findViewById(R.id.titleHighScore);
		tv.setText(getGameModeString(gameMode) + "\nTile : " + noOfTile);

		tv = (TextView) findViewById(R.id.baseScoreTV);
		tv.setText(String.valueOf(baseScore));

		tv = (TextView) findViewById(R.id.numberBonusTV);
		tv.setText(String.valueOf(highNumberBonus));

		tv = (TextView) findViewById(R.id.thisRoundTV);
		tv.setText(String.valueOf(total));
		tv.setTextSize((float) (tv.getTextSize() * 1.3));

		tv = (TextView) findViewById(R.id.pointsTV);
		tv.setText(String.valueOf(dbHandler.getGameDataPoints()));

		if (dbHandler.getHighScoreCount() != 0) {
			List<highScoreValue> scores = dbHandler.getAllHighScore(gameMode,
					noOfTile);
			boolean noInsertionFlag = false;
			Iterator<highScoreValue> itr = scores.iterator();
			int highScores[] = new int[11];
			int i = 0;
			while (itr.hasNext()) {
				highScoreValue currScore = itr.next();
				highScores[i] = currScore.get_score();
				System.out.println(highScores[i]);
				if (highScores[i] == total)
					noInsertionFlag = true;
				i++;
			}
			highScores[10] = total;
			Arrays.sort(highScores);

			for (i = 0; i < highScores.length / 2; ++i) {
				int temp = highScores[i];
				highScores[i] = highScores[highScores.length - i - 1];
				highScores[highScores.length - i - 1] = temp;
			}
			highScore = highScores[0];
			highScores = Arrays.copyOf(highScores, 10);
			if (!noInsertionFlag)
				dbHandler.insertHighScore(gameMode, noOfTile, highScores);
		} else {
			System.out.println("scores nulls");
			int dummyScores[] = new int[10];
			dummyScores[0] = total;
			for (int i = 1; i < dummyScores.length; i++) {
				dummyScores[i] = 0;
			}
			dbHandler.insertHighScore(gameMode, noOfTile, dummyScores);
		}

		tv = (TextView) findViewById(R.id.highScoreTV);
		tv.setText(String.valueOf(highScore));
	}

	private String getGameModeString(int gameMode) {
		String mode = "";
		switch (gameMode) {
		case highScoreValue.MODE_NORMAL:
			mode = "Normal Mode";
			;
			break;
		case highScoreValue.MODE_TIMED:
			mode = "Timer Mode";
			break;
		case highScoreValue.MODE_LIMITED_MOVES:
			mode = "Limited Moves Mode";
			break;
		}
		return mode;
	}

}

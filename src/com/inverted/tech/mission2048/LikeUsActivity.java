package com.inverted.tech.mission2048;

import com.inverted.tech.mission2048.databaseConnection.DatabaseHandler;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

public class LikeUsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DatabaseHandler db = new DatabaseHandler(LikeUsActivity.this);
		db.setGameDataLikeUs(1);
		String url = "https://touch.facebook.com/pages/2048-Number-Puzzle-Game/425926867544590";
		WebView browser = new WebView(LikeUsActivity.this);
		browser.getSettings().setLoadsImagesAutomatically(true);
		browser.getSettings().setJavaScriptEnabled(true);
		browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		browser.loadUrl(url);
		setContentView(browser);
	}

}

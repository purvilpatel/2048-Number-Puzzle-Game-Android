package com.inverted.tech.mission2048.supportClass;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import com.inverted.tech.mission2048.R;

public class ScreenShot extends Activity {

	View content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		content = findViewById(R.id.parentView);
		ViewTreeObserver vto = content.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				content.getViewTreeObserver()
						.removeGlobalOnLayoutListener(this);
				getScreen();
			}
		});
	}

	private void getScreen() {
		View view = content;
		View v = view.getRootView();
		v.setDrawingCacheEnabled(true);
		Bitmap b = v.getDrawingCache();
		String extr = Environment.getExternalStorageDirectory().toString();
		File myPath = new File(extr, "test.jpg");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(myPath);
			b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
			MediaStore.Images.Media.insertImage(getContentResolver(), b,
					"Screen", "screen");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finish();
	}
}
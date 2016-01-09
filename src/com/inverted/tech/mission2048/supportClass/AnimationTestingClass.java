package com.inverted.tech.mission2048.supportClass;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.inverted.tech.mission2048.R;

public class AnimationTestingClass extends Activity {

	private Animation animZoomIn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tryand_error);
		// load the animation
		animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.zoom_in_out);

		
		findViewById(R.id.tryErrorButton).startAnimation(animZoomIn);
			}

}

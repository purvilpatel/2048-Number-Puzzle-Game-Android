package com.inverted.tech.mission2048.supportClass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class OnSwipeTouchListener implements OnTouchListener {

	private final GestureDetector gestureDetector;
	public OnSwipeTouchListener(Context ctx) {
		gestureDetector = new GestureDetector(ctx, new GestureListener());
	}

	private final class GestureListener extends SimpleOnGestureListener {

		private static final int SWIPE_THRESHOLD = 100;
		private static final int SWIPE_VELOCITY_THRESHOLD = 100;

		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			return super.onSingleTapConfirmed(e);
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			onTap(e);
			return super.onSingleTapUp(e);
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			boolean result = false;
			try {
				float diffY = e2.getY() - e1.getY();
				float diffX = e2.getX() - e1.getX();
				if (Math.abs(diffX) > Math.abs(diffY)) {
					if (Math.abs(diffX) > SWIPE_THRESHOLD
							&& Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
						if (diffX > 0) {
							onSwipeRight();
							// System.out.println("Right");
						} else {
							onSwipeLeft();
							// System.out.println("Left");
						}
					}
				} else {
					if (Math.abs(diffY) > SWIPE_THRESHOLD
							&& Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
						if (diffY > 0) {
							onSwipeBottom();
							// System.out.println("Bottom");
						} else {
							onSwipeTop();
							// System.out.println("Top");
						}
					}
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
			return result;
		}
	}

	public boolean onSwipeRight() {
		return false;
	}

	public boolean onSwipeLeft() {
		return false;
	}

	public boolean onSwipeTop() {
		return false;
	}

	public boolean onSwipeBottom() {
		return false;
	}

	public boolean onTap(MotionEvent e) {
		return false;
	}

	public boolean onTap() {
		return false;
	}

	@SuppressLint("ClickableViewAccessibility")
	public boolean onTouch(final View v, final MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}
}
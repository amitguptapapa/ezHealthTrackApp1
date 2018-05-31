package com.ezhealthtrack.util;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class OnSwipeTouchListener implements OnTouchListener {

	private final class GestureListener extends SimpleOnGestureListener {

		private static final int SWIPE_THRESHOLD = 100;
		private static final int SWIPE_VELOCITY_THRESHOLD = 100;

		@Override
		public boolean onDown(final MotionEvent e) {
			return true;
		}

		@Override
		public boolean onFling(final MotionEvent e1, final MotionEvent e2,
				final float velocityX, final float velocityY) {
			final boolean result = false;
			try {
				final float diffY = e2.getY() - e1.getY();
				final float diffX = e2.getX() - e1.getX();
				Log.i("", diffX + " " + diffY);
				if (Math.abs(diffX) > Math.abs(diffY)) {
					if ((Math.abs(diffX) > GestureListener.SWIPE_THRESHOLD)
							&& (Math.abs(velocityX) > GestureListener.SWIPE_VELOCITY_THRESHOLD)) {
						if (diffX > 0) {
							onSwipeRight();
						} else {
							onSwipeLeft();
						}
					}
				} else {
					if ((Math.abs(diffY) > GestureListener.SWIPE_THRESHOLD)
							&& (Math.abs(velocityY) > GestureListener.SWIPE_VELOCITY_THRESHOLD)) {
						if (diffY > 0) {
							onSwipeBottom();
						} else {
							onSwipeTop();
						}
					}
				}
			} catch (final Exception exception) {
				exception.printStackTrace();
			}
			return result;
		}
	}

	protected final GestureDetector gestureDetector;

	public OnSwipeTouchListener(final Context ctx) {
		gestureDetector = new GestureDetector(ctx, new GestureListener());
	}

	public void onSwipeBottom() {
	}

	public void onSwipeLeft() {
	}

	public void onSwipeRight() {
	}

	public void onSwipeTop() {
	}

	@Override
	public boolean onTouch(final View v, final MotionEvent event) {
		// TODO Auto-generated method stub
		return true;
	}
}
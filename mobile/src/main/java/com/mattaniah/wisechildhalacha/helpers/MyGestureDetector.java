package com.mattaniah.wisechildhalacha.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.mattaniah.wisechildhalacha.R;

/**
 * Created by Mattaniah on 7/16/2015.
 */
public class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
    private GestureCallbackReciever mCallbacks;
    private Context context;
    private SharedPreferences sharedPref;

    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private int SWIPE_MIN_DISTANCE;

    public interface GestureCallbackReciever {
        public void swipeLeftToRight();

        public void swipeRightToLeft();

        public void doubleTap();

        public void longPress();

        public void zoomIn();

        public void zoomOut();
    }

    public static View.OnTouchListener getOnTouchListener(Context context, GestureCallbackReciever callbacks) {
        final GestureDetector detector = new GestureDetector(context, new MyGestureDetector(context, callbacks));
        final ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener(context, callbacks));

        return new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                scaleGestureDetector.onTouchEvent(event);
                return detector.onTouchEvent(event);
            }
        };
    }

    public MyGestureDetector(Context context, GestureCallbackReciever mCallbacks) {
        this.context = context;
        this.mCallbacks = mCallbacks;
        this.sharedPref= PreferenceManager.getDefaultSharedPreferences(context);
        this.SWIPE_MIN_DISTANCE = (int) context.getResources().getDimension(R.dimen.swipe_min_distance);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)

                return false;
            // right to left swipe
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                mCallbacks.swipeRightToLeft();
                return true;
                //				left to right swipe
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                mCallbacks.swipeLeftToRight();
                return true;
            }
        } catch (Exception e) {
            // nothing
        }
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if (sharedPref.getBoolean(context.getString(R.string.doubleTapKey), true))
            mCallbacks.doubleTap();
        return super.onDoubleTap(e);
    }

    @Override
    public void onLongPress(MotionEvent e) {
        if (sharedPref.getBoolean(context.getString(R.string.longpressKey), true))
            mCallbacks.longPress();
        super.onLongPress(e);
    }

    private static class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        GestureCallbackReciever mCallbacks;
        private static final int zoomScale = 1;
        SharedPreferences sharedPreferences;

        public ScaleListener(Context context, GestureCallbackReciever mCallbacks){
            this.mCallbacks=mCallbacks;
            this.sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();

            if (scaleFactor > zoomScale) {
                if (sharedPreferences.getBoolean("pinch", true))
                    mCallbacks.zoomIn();
            }
            if (scaleFactor < zoomScale) {
                if (sharedPreferences.getBoolean("pinch", true))
                    mCallbacks.zoomOut();
            }
            return true;
        }
    }
}

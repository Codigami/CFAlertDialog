package com.crowdfire.cfalertdialog.utils;

import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;

public class ViewUtil {

    public static void addBounceEffect(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return onButtonTouch(v, event);
            }
        });
    }

    private static boolean onButtonTouch(View button, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            onButtonPressed(button);
        } else if (event.getAction() == MotionEvent.ACTION_UP
                || event.getAction() == MotionEvent.ACTION_CANCEL
                || event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            onButtonReleased(button);
        }
        return false;
    }

    private static void onButtonPressed(View button) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0.9f,
                                                           1, 0.9f,
                                                           Animation.RELATIVE_TO_SELF, 0.5f,
                                                           Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setInterpolator(new DecelerateInterpolator());
        scaleAnimation.setDuration(100);
        scaleAnimation.setFillAfter(true);
        button.startAnimation(scaleAnimation);
    }

    private static void onButtonReleased(View button) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.9f, 1f,
                                                           0.9f, 1f,
                                                           Animation.RELATIVE_TO_SELF, 0.5f,
                                                           Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setInterpolator(new DecelerateInterpolator());
        scaleAnimation.setDuration(100);
        scaleAnimation.setFillAfter(true);
        button.startAnimation(scaleAnimation);
    }
}

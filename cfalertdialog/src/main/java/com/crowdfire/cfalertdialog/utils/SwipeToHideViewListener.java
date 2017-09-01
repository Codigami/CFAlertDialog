package com.crowdfire.cfalertdialog.utils;

import android.animation.Animator;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by rahul on 31/08/17.
 */

public class SwipeToHideViewListener implements View.OnTouchListener {

    private boolean isTouching;
    private float swipeStartX;
    private float deltaX = 0;
    private boolean isSwipingRight = false;

    private View animatingView;
    private boolean shouldDismissView;
    private SwipeToHideCompletionListener listener;

    private static final int SWIPE_TO_DISMISS_THRESHOLD = 200;
    private static final int SWIPE_TO_DISMISS_ANIMATION_DURATION = 100;

    public SwipeToHideViewListener(View animatingView, boolean shouldDismissView, SwipeToHideCompletionListener listener) {
        this.animatingView = animatingView;
        this.shouldDismissView = shouldDismissView;
        this.listener = listener;
    }

    public void setAnimatingView(View animatingView) {
        this.animatingView = animatingView;
    }

    public void setShouldDismissView(boolean shouldDismissView) {
        this.shouldDismissView = shouldDismissView;
    }

    public void setListener(SwipeToHideCompletionListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:

                // Set Touched view as the animatingView if not set
                if (animatingView == null) animatingView = view;

                isTouching = true;
                startSwipe(motionEvent);
                break;

            case MotionEvent.ACTION_MOVE:

                if (!isTouching) break;
                moveSwipe(motionEvent);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_OUTSIDE:

                isTouching = false;
                isSwipingRight = false;
                endSwipe();
                break;
        }

        return isSwipingRight;
    }

    private void startSwipe(MotionEvent event) {

        // Keep the initial swipe action position
        swipeStartX = event.getX();
    }

    private void moveSwipe(MotionEvent event) {

        // Check if the motion is horizontal
        deltaX = event.getRawX() - swipeStartX;
        if (Math.abs(deltaX) > 0) {
            animateCardViewHorizontally(deltaX, 0, null);
            isSwipingRight = true;
        }
    }

    private void endSwipe() {

        if (shouldDismissView && Math.abs(deltaX) > SWIPE_TO_DISMISS_THRESHOLD) {

            // Check whether view should animate left or right
            float endPos = (deltaX > 0) ? animatingView.getWidth() : -animatingView.getWidth();
            animateCardViewHorizontally(endPos, SWIPE_TO_DISMISS_ANIMATION_DURATION, new AnimatorCompletionListener() {
                @Override
                void animationCompleted() {
                    if (listener != null) listener.viewDismissed();
                }
            });
        }
        else {
            animateCardViewHorizontally(0, SWIPE_TO_DISMISS_ANIMATION_DURATION, null);
        }
    }

    private void animateCardViewHorizontally(float dX, int duration, AnimatorCompletionListener listener) {

        animatingView.animate()
                .x(dX)
                .setDuration(duration)
                .setListener(listener)
                .start();
    }

    private abstract class AnimatorCompletionListener implements Animator.AnimatorListener {
        @Override
        public void onAnimationStart(Animator animator) {

        }

        @Override
        public void onAnimationEnd(Animator animator) {
            animationCompleted();
        }

        @Override
        public void onAnimationCancel(Animator animator) {

        }

        @Override
        public void onAnimationRepeat(Animator animator) {

        }

        abstract void animationCompleted();
    }

    public interface SwipeToHideCompletionListener {
        void viewDismissed();
    }

}

package com.crowdfire.cfalertdialog.utils;

import android.animation.Animator;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;

/**
 * Created by rahul on 31/08/17.
 */

public class SwipeToHideViewListener implements View.OnTouchListener {

    private boolean isTouching;
    private float swipeStartX;
    private float swipeStartY;
    private float viewStartX;
    private float deltaX = 0;
    private float deltaY = 0;
    private boolean isSwipingHorizontal = false;

    private View animatingView;
    private boolean shouldDismissView;
    private SwipeToHideCompletionListener listener;

    private static final int SWIPE_TO_DISMISS_THRESHOLD = 150;
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

                view.performClick();
            case MotionEvent.ACTION_OUTSIDE:

                isTouching = false;
                isSwipingHorizontal = false;
                endSwipe();
                break;
        }

        return isSwipingHorizontal;
    }

    private void startSwipe(MotionEvent event) {

        // Keep the initial swipe action position
        swipeStartX = event.getRawX();
        swipeStartY = event.getRawY();

        viewStartX = animatingView.getX();
    }

    private void moveSwipe(MotionEvent event) {

        // Check if the motion is horizontal
        deltaX = event.getRawX() - swipeStartX;
        deltaY = event.getRawY() - swipeStartY;

        // Check Vertical Swipe
        if (Math.abs(deltaY) > 0 && Math.abs(deltaY) > Math.abs(deltaX)) {

            // Is swiping vertically
            return;
        }
        // Check Horizontal swipe
        if (Math.abs(deltaX) > 0) {
            animateViewHorizontally(deltaX, 0, false, null);
            isSwipingHorizontal = true;
        }
    }

    private void endSwipe() {

        if (shouldDismissView && Math.abs(deltaX) > SWIPE_TO_DISMISS_THRESHOLD) {

            // Check whether view should animate left or right
            float endPos = (deltaX > 0) ? animatingView.getWidth() : -animatingView.getWidth();
            animateViewHorizontally(endPos, SWIPE_TO_DISMISS_ANIMATION_DURATION, true, new AnimatorCompletionListener() {
                @Override
                void onAnimationCompleted() {
                    if (listener != null) listener.viewDismissed();
                }
            });
        }
        else {
            animateViewHorizontally(0, SWIPE_TO_DISMISS_ANIMATION_DURATION, false, null);
        }
    }

    private void animateViewHorizontally(float dX, int duration, boolean shouldHide, AnimatorCompletionListener listener) {

        float animatingDistance = viewStartX + dX;

        ViewPropertyAnimator animator = animatingView.animate()
                .x(animatingDistance)
                .setDuration(duration)
                .setListener(listener);

        if (shouldHide) {
            animator.alpha(0);
        }

        animator.start();
    }

    private abstract class AnimatorCompletionListener implements Animator.AnimatorListener {
        @Override
        public void onAnimationStart(Animator animator) {

        }

        @Override
        public void onAnimationEnd(Animator animator) {
            onAnimationCompleted();
        }

        @Override
        public void onAnimationCancel(Animator animator) {

        }

        @Override
        public void onAnimationRepeat(Animator animator) {

        }

        abstract void onAnimationCompleted();
    }

    public interface SwipeToHideCompletionListener {
        void viewDismissed();
    }

}

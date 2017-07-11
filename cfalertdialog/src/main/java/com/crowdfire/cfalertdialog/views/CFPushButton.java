package com.crowdfire.cfalertdialog.views;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

/**
 * Created by rahul on 29/06/17.
 */

public class CFPushButton extends AppCompatButton implements View.OnTouchListener {

    // region Static constants

    public static final String TAG = "CFPushButton";

    public static final long TOUCH_DOWN_ANIMATION_DURATION_DEFAULT = 100;
    public static final long TOUCH_UP_ANIMATION_DURATION_DEFAULT = 100;

    // endregion

    // region Variables

    public float pushTransformScaleFactor = 0.8f;
    public float originalTransformScaleFactor = 1.0f;

    public long touchDownAnimationDuration = TOUCH_DOWN_ANIMATION_DURATION_DEFAULT;
    public long touchUpAnimationDuration = TOUCH_UP_ANIMATION_DURATION_DEFAULT;

    private boolean shouldClick = false;

    private CFPushButtonListener pushButtonListener;

    // endregion

    public CFPushButton(Context context) {
        this(context, null, 0);
    }

    public CFPushButton(Context context, AttributeSet attrs) {
        this(context,attrs, 0);
    }

    public CFPushButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initButton(context, attrs, defStyleAttr);
    }

    void initButton(Context context, AttributeSet attributeSet, int defStyleAttr) {

        // Set on touch listener
        this.setOnTouchListener(this);

        // Set centered text alignment
        setGravity(Gravity.CENTER);
    }

    public void setPushButtonListener(CFPushButtonListener pushButtonListener) {
        this.pushButtonListener = pushButtonListener;
    }

    // region Touch Listeners

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                // Touch down
                handleButtonAction(true, true);

                // Listener callback
                if (pushButtonListener != null) {
                    pushButtonListener.pushButtonTouchDown(this);
                }

                shouldClick = true;

                // True returned so that 'ACTION_UP' event is received.
                return true;

            case MotionEvent.ACTION_UP:

                // Touch up
                handleButtonAction(false, true);

                // Listener callback
                if (pushButtonListener != null) {
                    pushButtonListener.pushButtonTouchUp(this);
                }

                // Perform button click manually here, since we need touch handlers
                if (shouldClick) {
                    performClick();
                }
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_OUTSIDE:
                shouldClick = false;
                break;
        }

        return false;
    }
    // endregion

    // region Helper Methods

    void handleButtonAction(boolean isPushed, boolean animated) {

        // Apply button transformation
        transformButton(isPushed, animated);
    }

    void transformButton(boolean isPushed, boolean animated) {
        float currentScaleX = getScaleX();
        float currentScaleY = getScaleY();

        float targetScaleX = 1.0f;
        float targetScaleY = 1.0f;

        //animated = false;

        long animationDuration = 1;

        if (isPushed) {

            // Set new Scale
            targetScaleX = pushTransformScaleFactor;
            targetScaleY = pushTransformScaleFactor;

            // Store current scale factor to restore after release
            originalTransformScaleFactor = currentScaleX;

            // Set animation duration
            animationDuration = touchDownAnimationDuration;
        }
        else {
            // Set current scale
            currentScaleX = pushTransformScaleFactor;
            currentScaleY = pushTransformScaleFactor;

            // Set new scale
            targetScaleX = originalTransformScaleFactor;
            targetScaleY = originalTransformScaleFactor;

            // Set animation duration
            animationDuration = touchUpAnimationDuration;
        }

        if (animated) {

            // Animate the scaling
            ScaleAnimation scaleAnimation = new ScaleAnimation(currentScaleX, targetScaleX, currentScaleY, targetScaleY, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setDuration(animationDuration);
            scaleAnimation.setFillAfter(true);
            startAnimation(scaleAnimation);
        }
        else {

            // Set the new scale factor without animation.
            setScaleX(targetScaleX);
            setScaleY(targetScaleY);
        }
    }

    // endregion

    public interface CFPushButtonListener {
        void pushButtonTouchDown(CFPushButton button);
        void pushButtonTouchUp(CFPushButton button);
    }
}

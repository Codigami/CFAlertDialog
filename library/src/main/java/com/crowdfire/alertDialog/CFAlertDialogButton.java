package com.crowdfire.alertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

public class CFAlertDialogButton {

    private String buttonText;
    private DialogInterface.OnClickListener onClickListener;
    private ColorStateList textColor;
    private int gravity;
    private Drawable backgroundDrawable;
    private int textColorListId = -1, backgroundDrawableId = -1;

    private CFAlertDialogButton(Builder builder) {
        initDefaultValues();
        this.buttonText = builder.buttonText;

        if (builder.backgroundDrawable != null) {
            this.backgroundDrawable = builder.backgroundDrawable;
            this.backgroundDrawableId = -1;
        } else if (builder.backgroundDrawableId != -1) {
            this.backgroundDrawable = null;
            this.backgroundDrawableId = builder.backgroundDrawableId;
        }

        if (builder.textColor != null) {
            this.textColor = builder.textColor;
            this.textColorListId = -1;
        } else if (builder.textColorStateListId != -1) {
            this.textColorListId = builder.textColorStateListId;
            this.textColor = null;
        }

        this.onClickListener = builder.onClickListener;
        this.gravity = builder.gravity;
    }

    private void initDefaultValues() {
        this.textColorListId = R.color.cfdialog_default_button_text_color;
        this.backgroundDrawableId = R.drawable.cfdialog_default_button_background_drawable;
    }

    /**
     * @param gravity android.view.Gravity
     */
    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public int getGravity() {
        return gravity;
    }

    public String getButtonText() {
        return buttonText;
    }

    public Drawable getBackgroundDrawable() {
        return backgroundDrawable;
    }

    @DrawableRes
    public int getBackgroundDrawableId() {
        return backgroundDrawableId;
    }

    public int getTextColorListId() {
        return textColorListId;
    }

    public ColorStateList getTextColor() {
        return textColor;
    }

    public DialogInterface.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public static class Builder {

        private String buttonText;
        private int backgroundDrawableId = -1, textColorStateListId = -1;
        private DialogInterface.OnClickListener onClickListener;

        private Drawable backgroundDrawable;
        private ColorStateList textColor = null;

        private int gravity = android.view.Gravity.FILL_HORIZONTAL;

        public Builder(String buttonText, DialogInterface.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
            this.buttonText = buttonText;
        }

        public Builder(Context context, @StringRes int buttonTextId, DialogInterface.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
            this.buttonText = context.getString(buttonTextId);
        }

        public Builder backgroundColor(int backgroundColor) {
            this.backgroundDrawable = new ColorDrawable(backgroundColor);
            this.backgroundDrawableId = -1;
            return this;
        }

        public Builder backgroundDrawable(Drawable backgroundDrawable) {
            this.backgroundDrawable = backgroundDrawable;
            this.backgroundDrawableId = -1;
            return this;
        }

        public Builder backgroundDrawable(@DrawableRes int backgroundDrawableId) {
            this.backgroundDrawableId = backgroundDrawableId;
            this.backgroundDrawable = null;
            return this;
        }

        public Builder textColor(int textColor) {
            this.textColor = ColorStateList.valueOf(textColor);
            this.textColorStateListId = -1;
            return this;
        }

        public Builder textColorStateList(ColorStateList colorStateList) {
            this.textColor = colorStateList;
            this.textColorStateListId = -1;
            return this;
        }

        public Builder textColorStateList(@ColorRes int colorStateListId) {
            this.textColorStateListId = colorStateListId;
            this.textColor = null;
            return this;
        }

        /**
         * @param gravity android.view.Gravity
         */
        public Builder gravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public CFAlertDialogButton build() {
            return new CFAlertDialogButton(this);
        }
    }

    public static CFAlertDialogButton getPositiveButton(String buttonText, DialogInterface.OnClickListener onClickListener) {
        return new Builder(buttonText, onClickListener)
                .backgroundDrawable(R.drawable.cfdialog_positive_button_background_drawable)
                .textColorStateList(R.color.cfdialog_button_white_text_color)
                .build();
    }

    public static CFAlertDialogButton getNegativeButton(String buttonText, DialogInterface.OnClickListener onClickListener) {
        return new Builder(buttonText, onClickListener)
                .backgroundDrawable(R.drawable.cfdialog_negative_button_background_drawable)
                .textColorStateList(R.color.cfdialog_button_white_text_color)
                .build();
    }

    public static CFAlertDialogButton getNeutralButton(String buttonText, DialogInterface.OnClickListener onClickListener) {
        return new Builder(buttonText, onClickListener)
                .backgroundDrawable(R.drawable.cfdialog_neutral_button_background_drawable)
                .textColorStateList(R.color.cfdialog_button_white_text_color)
                .build();
    }
}

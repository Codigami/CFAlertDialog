package com.crowdfire.cfalertdialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crowdfire.cfalertdialog.utils.DeviceUtil;
import com.crowdfire.cfalertdialog.views.CFPushButton;

import java.util.ArrayList;
import java.util.List;

public class CFAlertDialog extends AppCompatDialog {

    // region ENUMS
    public enum CFAlertActionStyle{
        DEFAULT,
        NEGATIVE,
        POSITIVE;
    }

    public enum CFAlertActionAlignment {
        START,
        END,
        CENTER,
        JUSTIFIED;
    }

    public enum CFAlertBackgroundStyle {
        PLAIN,
        BLUR;
    }
    // endregion

    private DialogParams params;

    private RelativeLayout cfDialogBackground;
    private LinearLayout cfDialogHeaderLinearLayout, buttonContainerLinearLayout,
            cfDialogFooterLinearLayout, iconTitleContainer, selectableItemsContainer;
    private CardView dialogCardView;
    private TextView dialogTitleTextView, dialogMessageTextView;
    private ImageView cfDialogIconImageView;

    private CFAlertDialog(Context context) {
        super(context, R.style.CFDialog);
    }

    private CFAlertDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the view
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.cfalert_layout, null);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(view);

        // Setup the dialog
        bindSubviews(view);
        populateDialog(params);

        // Adjust the dialog width
        adjustDialogWidth();

        // Set the size to adjust when keyboard shown
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // Disable the view initially
        setEnabled(false);
    }

    public void setCFDialogBackgroundColor(int color){
        cfDialogBackground.setBackgroundColor(color);
    }


    @Override
    public void show() {
        super.show();

        // Perform the present animation
        startPresentAnimation();
    }

    @Override
    public void dismiss() {
        
        // Disable the view when being dismissed
        setEnabled(false);

        // Perform the dismiss animation and after that dismiss the dialog
        Animation dismissAnimation = getDismissAnimation(params.dialogGravity);
        dismissAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        CFAlertDialog.super.dismiss();
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        dialogCardView.startAnimation(dismissAnimation);
    }

    public void setEnabled(boolean enabled) {
        setViewEnabled(cfDialogBackground, enabled);
    }

    private void bindSubviews(View view) {
        cfDialogBackground = ((RelativeLayout) view.findViewById(R.id.cfdialog_background));
        dialogCardView = (CardView) view.findViewById(R.id.cfdialog_cardview);
        cfDialogHeaderLinearLayout = (LinearLayout) view.findViewById(R.id.alert_header_container);
        dialogTitleTextView = (TextView) view.findViewById(R.id.tv_dialog_title);
        iconTitleContainer = (LinearLayout) view.findViewById(R.id.icon_title_container);
        cfDialogIconImageView = (ImageView) view.findViewById(R.id.cfdialog_icon_imageview);
        dialogMessageTextView = (TextView) view.findViewById(R.id.tv_dialog_content_desc);
        buttonContainerLinearLayout = (LinearLayout) view.findViewById(R.id.alert_buttons_container);
        cfDialogFooterLinearLayout = (LinearLayout) view.findViewById(R.id.alert_footer_container);
        selectableItemsContainer = (LinearLayout) view.findViewById(R.id.alert_selection_items_container);
    }

    private void populateDialog(final DialogParams params) {

        // Background
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        if (params.backgroundStyle == CFAlertBackgroundStyle.BLUR) {
            // TODO: Add blur background effect

        }
        else {
            cfDialogBackground.setBackgroundColor(params.backgroundColor);
        }

        cfDialogBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (params.cancelable) {

                    dismiss();
                }
            }
        });

        // Icon
        if (params.iconDrawableId != -1) {
            setIcon(params.iconDrawableId);
        } else if (params.iconDrawable != null) {
            setIcon(params.iconDrawable);
        } else { setIcon(null); }

        // Title
        setTitle(params.title);

        // Message
        setMessage(params.message);

        // Cancel
        setCancelable(params.cancelable);

        // Buttons
        populateButtons(params.context, params.buttons);

        // Dialog position
        setDialogGravity(params.dialogGravity);

        // Text gravity
        setTextGravity(params.textGravity);

        // Image/Header
        if (params.contentImageDrawableId != -1) {
            setContentImageDrawable(params.contentImageDrawableId);
        } else if (params.contentImageDrawable != null) {
            setContentImageDrawable(params.contentImageDrawable);
        } else if (params.headerView != null) {
            setHeaderView(params.headerView);
        } else if (params.headerViewId != -1) {
            setHeaderView(params.headerViewId);
        }

        // Footer
        if (params.footerView != null) {
            setFooterView(params.footerView);
        } else if (params.footerViewId != -1) {
            setFooterView(params.footerViewId);
        }

        // Selection items
        if (params.items != null && params.items.length > 0) {
            setItems(params.items, params.onItemClickListener);
        } else if (params.multiSelectItems != null && params.multiSelectItems.length > 0) {
            setMultiSelectItems(params.multiSelectItems, params.multiSelectedItems, params.onMultiChoiceClickListener);
        } else if (params.singleSelectItems != null && params.singleSelectItems.length > 0) {
            setSingleSelectItems(params.singleSelectItems, params.singleSelectedItem, params.onSingleItemClickListener);
        } else {
            selectableItemsContainer.removeAllViews();
        }
    }

    private void adjustDialogWidth() {
        int margin = (int)getContext().getResources().getDimension(R.dimen.cfdialog_outer_margin);
        int width = DeviceUtil.getScreenWidth(getContext()) - (2 * margin);
        width = Math.min(width, (int) getContext().getResources().getDimension(R.dimen.cfdialog_maxwidth));
        RelativeLayout.LayoutParams cardViewLayoutParams = new RelativeLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        cardViewLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        cardViewLayoutParams.setMargins(margin, margin, margin, margin);
        dialogCardView.setLayoutParams(cardViewLayoutParams);
    }

    private void startPresentAnimation() {
        Animation presentAnimation = getPresentAnimation(params.dialogGravity);
        presentAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                alertPresented();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        dialogCardView.startAnimation(presentAnimation);
    }

    private void alertPresented() {

        setEnabled(true);
    }

    private void setDialogParams(DialogParams params) {
        this.params = params;
    }

    @Override
    public void setTitle(CharSequence title) {
        if (TextUtils.isEmpty(title)) {
            dialogTitleTextView.setVisibility(View.GONE);
            if (cfDialogIconImageView.getVisibility() == View.GONE) {
                iconTitleContainer.setVisibility(View.GONE);
            }
        } else {
            dialogTitleTextView.setText(title);
            dialogTitleTextView.setVisibility(View.VISIBLE);
            iconTitleContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setTitle(int titleId) {
        setTitle(getContext().getString(titleId));
    }

    public void setMessage(CharSequence message) {
        if (TextUtils.isEmpty(message)) {
            dialogMessageTextView.setVisibility(View.GONE);
        } else {
            dialogMessageTextView.setText(message);
            dialogMessageTextView.setVisibility(View.VISIBLE);
        }
    }

    public void setMessage(int messageId) {
        setMessage(getContext().getString(messageId));
    }

    /**
     * @param dialogGravity @see android.view.Gravity
     */
    public void setDialogGravity(int dialogGravity) {
        if (dialogGravity != -1) {
            cfDialogBackground.setGravity(dialogGravity);
        }
    }

    /**
     * @param textGravity @see android.view.Gravity
     */
    public void setTextGravity(int textGravity) {
        ((LinearLayout.LayoutParams) iconTitleContainer.getLayoutParams()).gravity = textGravity;
        dialogMessageTextView.setGravity(textGravity);
    }

    /**
     * @param headerView pass null to remove header
     */
    public void setHeaderView(View headerView) {
        cfDialogHeaderLinearLayout.removeAllViews();
        if (headerView != null) {
            cfDialogHeaderLinearLayout.addView(headerView);
            cfDialogHeaderLinearLayout.setVisibility(View.VISIBLE);
        } else {
            cfDialogHeaderLinearLayout.setVisibility(View.GONE);
        }
        fixDialogPadding();
    }

    public void setHeaderView(@LayoutRes int headerResId) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(headerResId, null);
        setHeaderView(view);
    }

    public void setIcon(@DrawableRes int iconDrawableId) {
        setIcon(ContextCompat.getDrawable(getContext(), iconDrawableId));
    }

    public void setIcon(Drawable iconDrawable) {
        if (iconDrawable == null) {
            cfDialogIconImageView.setVisibility(View.GONE);
            if (dialogTitleTextView.getVisibility() == View.GONE) {
                iconTitleContainer.setVisibility(View.GONE);
            }
        } else {
            cfDialogIconImageView.setVisibility(View.VISIBLE);
            iconTitleContainer.setVisibility(View.VISIBLE);
            cfDialogIconImageView.setImageDrawable(iconDrawable);
        }
    }

    /**
     * @param imageDrawableId value -1 will remove image
     */
    public void setContentImageDrawable(@DrawableRes int imageDrawableId) {
        setContentImageDrawable(ContextCompat.getDrawable(getContext(), imageDrawableId));
    }

    public void setContentImageDrawable(Drawable imageDrawable) {
        if (imageDrawable != null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ImageView imageView = (ImageView) layoutInflater.inflate(R.layout.cfdialog_imageview_header, cfDialogHeaderLinearLayout).findViewById(R.id.cfdialog_imageview_content);
            imageView.setImageDrawable(imageDrawable);
            imageView.setTag(111);
            cfDialogHeaderLinearLayout.setVisibility(View.VISIBLE);
        } else {
            for (int i = 0; i < cfDialogHeaderLinearLayout.getChildCount(); i++) {
                View view = cfDialogHeaderLinearLayout.getChildAt(i);
                if (view instanceof ImageView && (int) view.getTag() == 111) {
                    cfDialogHeaderLinearLayout.removeView(view);
                    cfDialogHeaderLinearLayout.setVisibility(View.GONE);
                    break;
                }
            }
        }
    }

    /**
     * @param footerView pass null to remove header
     */
    public void setFooterView(View footerView) {
        cfDialogFooterLinearLayout.removeAllViews();
        if (footerView != null) {
            cfDialogFooterLinearLayout.addView(footerView);
            cfDialogFooterLinearLayout.setVisibility(View.VISIBLE);
        } else {
            cfDialogFooterLinearLayout.setVisibility(View.GONE);
        }
        fixDialogPadding();
    }

    public void setFooterView(@LayoutRes int footerResId) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(footerResId, null);
        setFooterView(view);
    }

    private void fixDialogPadding() {
        int topPadding, bottomPadding;
        if (cfDialogFooterLinearLayout.getChildCount() == 0) {
            bottomPadding = (int) getContext().getResources().getDimension(R.dimen.cfdialog_footer_top_margin_half);
        } else {
            bottomPadding = 0;
        }
        if (cfDialogHeaderLinearLayout.getChildCount() == 0) {
            topPadding = (int) getContext().getResources().getDimension(R.dimen.cfdialog_header_bottom_margin_half);
        } else {
            topPadding = 0;
        }
        dialogCardView.setContentPadding(0, topPadding, 0, bottomPadding);
    }

    private void setViewEnabled(ViewGroup layout, boolean enabled) {
        layout.setEnabled(enabled);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                setViewEnabled((ViewGroup) child, enabled);
            } else {
                child.setEnabled(enabled);
            }
        }
    }

    private void populateButtons(Context context, List<CFAlertActionButton> buttons) {
        buttonContainerLinearLayout.removeAllViews();
        if (buttons.size() > 0) {
            for (int i = 0; i < buttons.size(); i++) {
                View buttonView = createButton(context, buttons.get(i));
                buttonContainerLinearLayout.addView(buttonView);
            }
            buttonContainerLinearLayout.setVisibility(View.VISIBLE);
        } else {
            buttonContainerLinearLayout.setVisibility(View.GONE);
        }
    }

    private View createButton(Context context, final CFAlertActionButton actionButton) {
        CFPushButton button = new CFPushButton(context, null, R.style.CFDialog_Button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionButton.onClickListener.onClick(CFAlertDialog.this, 0);
            }
        });

        setButtonLayout(button, actionButton);

        button.setText(actionButton.buttonText);

        setButtonColors(button, actionButton);

        return button;
    }

    private void setButtonLayout(View buttonView, CFAlertActionButton actionButton) {
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ;

        switch (actionButton.alignment) {
            case JUSTIFIED:
                buttonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                break;
            case START:
                buttonParams.gravity = Gravity.START;
                break;
            case CENTER:
                buttonParams.gravity = Gravity.CENTER;
                break;
            case END:
                buttonParams.gravity = Gravity.END;
                break;
        }
        int margin = ((int) buttonView.getResources().getDimension(R.dimen.cfdialog_internal_spacing_half));
        buttonParams.setMargins(margin, margin, margin, margin);
        buttonView.setLayoutParams(buttonParams);

        int padding = ((int) buttonView.getResources().getDimension(R.dimen.cfdialog_button_padding));
        buttonView.setPadding(padding, padding, padding, padding);
    }

    private void setButtonColors(TextView button, CFAlertActionButton actionButton) {
        if (actionButton.backgroundDrawable != null) {
            setButtonBackgroundColor(button, actionButton.backgroundDrawable);
        } else if (actionButton.backgroundDrawableId != -1) {
            setButtonBackgroundColor(button, ContextCompat.getDrawable(getContext(), actionButton.backgroundDrawableId));
        }

        if (actionButton.textColor != null) {
            button.setTextColor(actionButton.textColor);
        } else if (actionButton.textColorListId != -1) {
            button.setTextColor(ContextCompat.getColorStateList(getContext(), actionButton.textColorListId));
        }
    }

    private void setButtonBackgroundColor(TextView button, Drawable backgroundDrawable) {
        if (Build.VERSION.SDK_INT > 16) {
            button.setBackground(backgroundDrawable);
        } else {
            button.setBackgroundDrawable(backgroundDrawable);
        }
    }

    public void setItems(String[] items, final OnClickListener onClickListener) {

        if (items != null && items.length > 0) {
            selectableItemsContainer.removeAllViews();
            selectableItemsContainer.setVisibility(View.VISIBLE);
            for (int i = 0; i < items.length; i++) {
                String item = items[i];
                View view = getLayoutInflater().inflate(R.layout.cfdialog_selectable_item_layout, null);
                TextView itemTextView = (TextView) view.findViewById(R.id.cfdialog_selectable_item_textview);
                itemTextView.setText(item);
                final int position = i;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onClickListener != null) {
                            onClickListener.onClick(CFAlertDialog.this, position);
                        }
                    }
                });
                selectableItemsContainer.addView(view);
            }
        } else {
            selectableItemsContainer.setVisibility(View.GONE);
        }
    }

    public void setMultiSelectItems(String[] multiSelectItems, boolean[] selectedItems, final OnMultiChoiceClickListener onMultiChoiceClickListener) {
        if (multiSelectItems != null && multiSelectItems.length > 0) {
            if (selectedItems.length != multiSelectItems.length) {
                throw new IllegalArgumentException("multi select items and boolean array size not equal");
            }
            selectableItemsContainer.removeAllViews();
            selectableItemsContainer.setVisibility(View.VISIBLE);
            for (int i = 0; i < multiSelectItems.length; i++) {
                String item = multiSelectItems[i];
                View view = getLayoutInflater().inflate(R.layout.cfdialog_multi_select_item_layout, null);
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.cfdialog_multi_select_item_checkbox);
                checkBox.setText(item);
                checkBox.setChecked(selectedItems[i]);
                final int position = i;
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (onMultiChoiceClickListener != null) {
                            onMultiChoiceClickListener.onClick(CFAlertDialog.this, position, isChecked);
                        }
                    }
                });
                selectableItemsContainer.addView(view);
            }
        } else {
            selectableItemsContainer.setVisibility(View.GONE);
        }
    }

    public void setSingleSelectItems(String[] singleSelectItems, int selectedItem, final OnClickListener onClickListener) {
        if (singleSelectItems != null && singleSelectItems.length > 0) {
            selectableItemsContainer.removeAllViews();
            selectableItemsContainer.setVisibility(View.VISIBLE);
            RadioGroup radioGroup = (RadioGroup) getLayoutInflater().inflate(R.layout.cfdialog_single_select_item_layout, selectableItemsContainer)
                                                                    .findViewById(R.id.cfstage_single_select_radio_group);
            radioGroup.removeAllViews();
            for (int i = 0; i < singleSelectItems.length; i++) {
                String item = singleSelectItems[i];
                RadioButton radioButton = (RadioButton) getLayoutInflater().inflate(R.layout.cfdialog_single_select_radio_button_layout, null);
                radioButton.setText(item);
                radioButton.setId(i);
                final int position = i;
                if (position == selectedItem) {
                    radioButton.setChecked(true);
                }
                radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked && onClickListener != null) {
                            onClickListener.onClick(CFAlertDialog.this, position);
                        }
                    }
                });
                radioGroup.addView(radioButton);
                ((LinearLayout.LayoutParams) radioButton.getLayoutParams()).leftMargin =
                        ((LinearLayout.LayoutParams) radioButton.getLayoutParams()).rightMargin =
                                (int) getContext().getResources().getDimension(R.dimen.cfdialog_padding);
            }
        } else {
            selectableItemsContainer.setVisibility(View.GONE);
        }
    }

    public void setElevation(float elevation) {
        dialogCardView.setCardElevation(elevation);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //cfDialogFooterLinearLayout.removeAllViews();
        //cfDialogHeaderLinearLayout.removeAllViews();

    }

    // region Animation helper methods

    private Animation getPresentAnimation(int gravity) {
        switch (gravity) {
            case Gravity.TOP:
                return AnimationUtils.loadAnimation(params.context, R.anim.dialog_present_top);
            case Gravity.CENTER:
                return AnimationUtils.loadAnimation(params.context, R.anim.dialog_present_center);
            case Gravity.BOTTOM:
                return AnimationUtils.loadAnimation(params.context, R.anim.dialog_present_bottom);
            default:
                return AnimationUtils.loadAnimation(params.context, R.anim.dialog_present_center);
        }
    }

    private Animation getDismissAnimation(int gravity) {
        switch (gravity) {
            case Gravity.TOP:
                return AnimationUtils.loadAnimation(params.context, R.anim.dialog_dismiss_top);
            case Gravity.CENTER:
                return AnimationUtils.loadAnimation(params.context, R.anim.dialog_dismiss_center);
            case Gravity.BOTTOM:
                return AnimationUtils.loadAnimation(params.context, R.anim.dialog_dismiss_bottom);
            default:
                return AnimationUtils.loadAnimation(params.context, R.anim.dialog_dismiss_center);
        }
    }

    // endregion

    public static class Builder {

        private DialogParams params;

        public Builder(Context context) {
            params = new DialogParams();
            setDefaultParams();
            this.params.context = context;
        }

        public Builder(Context context, @StyleRes int theme) {
            params = new DialogParams();
            setDefaultParams();
            this.params.context = context;
            this.params.theme = theme;
        }

        private void setDefaultParams() {

            this.params.theme = R.style.CFDialog;
            this.params.backgroundStyle = CFAlertBackgroundStyle.PLAIN;
            this.params.backgroundColor = Color.TRANSPARENT;
        }

        public Builder setBackgroundStyle(CFAlertBackgroundStyle backgroundStyle) {
            this.params.backgroundStyle = backgroundStyle;
            return this;
        }

        public Builder setBackgroundResource(@ColorRes int backgroundResource) {
            this.params.backgroundColor = ResourcesCompat.getColor(this.params.context.getResources(), backgroundResource, null);
            return this;
        }

        public Builder setBackgroundColor(@ColorInt int backgroundColor) {
            this.params.backgroundColor = backgroundColor;
            return this;
        }

        public Builder setMessage(CharSequence message) {
            this.params.message = message;
            return this;
        }

        public Builder setTitle(CharSequence title) {
            this.params.title = title;
            return this;
        }

        public Builder setMessage(@StringRes int messageId) {
            this.params.message = params.context.getString(messageId);
            return this;
        }

        public Builder setTitle(@StringRes int titleId) {
            this.params.title = params.context.getString(titleId);
            return this;
        }

        public Builder setContentImageDrawable(@DrawableRes int contentImageDrawableId) {
            this.params.contentImageDrawableId = contentImageDrawableId;
            this.params.contentImageDrawable = null;
            return this;
        }

        public Builder setContentImageDrawable(Drawable contentImageDrawable) {
            this.params.contentImageDrawable = contentImageDrawable;
            this.params.contentImageDrawableId = -1;
            return this;
        }

        public Builder setIcon(@DrawableRes int iconDrawableId) {
            this.params.iconDrawableId = iconDrawableId;
            this.params.iconDrawable = null;
            return this;
        }

        public Builder setIcon(Drawable iconDrawable) {
            this.params.iconDrawable = iconDrawable;
            this.params.iconDrawableId = -1;
            return this;
        }

        public Builder onDismissListener(OnDismissListener onDismissListener) {
            this.params.onDismissListener = onDismissListener;
            return this;
        }

        /**
         * @param dialogGravity @see android.view.Gravity
         */
        public Builder setDialogVerticalGravity(int dialogGravity) {
            this.params.dialogGravity = dialogGravity;
            return this;
        }

        /**
         * @param textGravity @see android.view.Gravity
         */
        public Builder setTextGravity(int textGravity) {
            this.params.textGravity = textGravity;
            return this;
        }

        public Builder addButton(String buttonText, CFAlertActionStyle style, CFAlertActionAlignment alignment, OnClickListener onClickListener) {
            CFAlertActionButton button = new CFAlertActionButton(buttonText, style, alignment, onClickListener);
            this.params.buttons.add(button);
            return this;
        }

        public Builder setItems(String[] items, OnClickListener onItemClickListener) {
            params.items = items;
            params.onItemClickListener = onItemClickListener;
            return this;
        }

        public Builder setMultiChoiceItems(String[] items, boolean[] selectedItems, OnMultiChoiceClickListener onMultiChoiceClickListener) {
            params.multiSelectItems = items;
            params.multiSelectedItems = selectedItems;
            params.onMultiChoiceClickListener = onMultiChoiceClickListener;
            return this;
        }

        public Builder setSingleChoiceItems(String[] items, int selectedItem, OnClickListener onItemClickListener) {
            params.singleSelectItems = items;
            params.singleSelectedItem = selectedItem;
            params.onSingleItemClickListener = onItemClickListener;
            return this;
        }

        public Builder setHeaderView(View headerView) {
            this.params.headerView = headerView;
            this.params.headerViewId = -1;
            return this;
        }

        public Builder setHeaderView(@LayoutRes int headerViewId) {
            this.params.headerViewId = headerViewId;
            this.params.headerView = null;
            return this;
        }

        public Builder setFooterView(View footerView) {
            this.params.footerView = footerView;
            this.params.footerViewId = -1;
            return this;
        }

        public Builder setFooterView(@LayoutRes int footerViewId) {
            this.params.footerViewId = footerViewId;
            this.params.footerView = null;
            return this;
        }

        /**
         * default is true
         *
         * @param cancelable
         */
        public Builder setCancelable(boolean cancelable) {
            this.params.cancelable = cancelable;
            return this;
        }

        public CFAlertDialog create() {
            CFAlertDialog cfAlertDialog;
            if (params.theme == 0) {
                cfAlertDialog = new CFAlertDialog(params.context);
            } else {
                cfAlertDialog = new CFAlertDialog(params.context, params.theme);
            }

            cfAlertDialog.setOnDismissListener(params.onDismissListener);
            cfAlertDialog.setDialogParams(params);
            return cfAlertDialog;
        }

        public CFAlertDialog show() {
            final CFAlertDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }

    private static class DialogParams {

        private Context context;
        private CFAlertBackgroundStyle backgroundStyle = CFAlertBackgroundStyle.PLAIN;
        private @ColorInt int backgroundColor = -1;
        private CharSequence message, title;
        private int theme = R.style.CFDialog,
                dialogGravity = Gravity.CENTER,
                textGravity = Gravity.LEFT,
                iconDrawableId = -1,
                contentImageDrawableId = -1;
        private View headerView, footerView;
        private int headerViewId = -1, footerViewId = -1;
        private Drawable contentImageDrawable, iconDrawable;
        private List<CFAlertActionButton> buttons = new ArrayList<>();
        private OnDismissListener onDismissListener;
        private boolean cancelable = true;
        private String[] multiSelectItems;
        private String[] items;
        private String[] singleSelectItems;
        private boolean[] multiSelectedItems;
        private int singleSelectedItem = -1;
        private OnClickListener onItemClickListener;
        private OnClickListener onSingleItemClickListener;
        private OnMultiChoiceClickListener onMultiChoiceClickListener;
    }

    private static class CFAlertActionButton {
        private String buttonText;
        private DialogInterface.OnClickListener onClickListener;
        private ColorStateList textColor;
        private CFAlertActionStyle style;
        private CFAlertActionAlignment alignment = CFAlertActionAlignment.JUSTIFIED;
        private Drawable backgroundDrawable;
        private int textColorListId = -1, backgroundDrawableId = -1;

        public CFAlertActionButton(String buttonText, CFAlertActionStyle style, CFAlertActionAlignment alignment, OnClickListener onClickListener) {
            this.buttonText = buttonText;
            this.onClickListener = onClickListener;
            this.style = style;
            this.alignment = alignment;
            this.backgroundDrawableId = getBackgroundDrawable(style);
        }

        private @DrawableRes int getBackgroundDrawable(CFAlertActionStyle style) {
            @DrawableRes int backgroundDrawable = 0;
            switch (style) {
                case NEGATIVE:
                    backgroundDrawable = R.drawable.cfdialog_negative_button_background_drawable;
                    break;
                case POSITIVE:
                    backgroundDrawable = R.drawable.cfdialog_positive_button_background_drawable;
                    break;
                case DEFAULT:
                    backgroundDrawable = R.drawable.cfdialog_default_button_background_drawable;
                    break;
            }
            return backgroundDrawable;
        }
    }
}

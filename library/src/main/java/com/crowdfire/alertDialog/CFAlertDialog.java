package com.crowdfire.alertDialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.crowdfire.alertDialog.utils.DeviceUtil;

import java.util.ArrayList;
import java.util.List;

public class CFAlertDialog extends AppCompatDialog {

    private DialogParams params;

    private LinearLayout cfDialogHeaderLinearLayout, buttonContainerLinearLayout,
            cfDialogFooterLinearLayout, iconTitleContainer, selectableItemsContainer;
    private CardView dialogCardView;
    private TextView dialogTitleTextView, dialogMessageTextView;
    private ImageView cfDialogIconImageView;

    private CFAlertDialog(Context context) {
        super(context, R.style.CFDialogStyle);
    }

    private CFAlertDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.crowdfire_dialog_layout, null);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(view);
        bindViews(view);
        initDialog(params);
    }

    private void bindViews(View view) {
        dialogCardView = (CardView) view.findViewById(R.id.cfdialog_cardview);
        cfDialogHeaderLinearLayout = (LinearLayout) view.findViewById(R.id.crowdfire_dialog_header_ll);
        dialogTitleTextView = (TextView) view.findViewById(R.id.tv_dialog_title);
        dialogMessageTextView = (TextView) view.findViewById(R.id.tv_dialog_content_desc);
        buttonContainerLinearLayout = (LinearLayout) view.findViewById(R.id.button_container_ll);
        cfDialogFooterLinearLayout = (LinearLayout) view.findViewById(R.id.crowdfire_dialog_footer_ll);
        iconTitleContainer = (LinearLayout) view.findViewById(R.id.icon_title_container);
        cfDialogIconImageView = (ImageView) view.findViewById(R.id.cfdialog_icon_imageview);
        selectableItemsContainer = (LinearLayout) view.findViewById(R.id.selectable_items_container_ll);
    }

    private void initDialog(DialogParams params) {
        if (params.iconDrawableId != -1) {
            setIcon(params.iconDrawableId);
        } else if (params.iconDrawable != null) {
            setIcon(params.iconDrawable);
        } else { setIcon(null); }
        setTitle(params.title);
        setMessage(params.message);
        setCancelable(params.cancelable);
        populateButtons(params.buttons);
        setDialogGravity(params.dialogGravity);
        setTextGravity(params.textGravity);
        if (params.contentImageDrawableId != -1) {
            setContentImageDrawable(params.contentImageDrawableId);
        } else if (params.contentImageDrawable != null) {
            setContentImageDrawable(params.contentImageDrawable);
        } else if (params.headerView != null) {
            setHeaderView(params.headerView);
        } else if (params.headerViewId != -1) {
            setHeaderView(params.headerViewId);
        }
        if (params.footerView != null) {
            setFooterView(params.footerView);
        } else if (params.footerViewId != -1) {
            setFooterView(params.footerViewId);
        }
        if (params.items != null && params.items.length > 0) {
            setItems(params.items, params.onItemClickListener);
        } else if (params.multiSelectItems != null && params.multiSelectItems.length > 0) {
            setMultiSelectItems(params.multiSelectItems, params.multiSelectedItems, params.onMultiChoiceClickListener);
        } else if (params.singleSelectItems != null && params.singleSelectItems.length > 0) {
            setSingleSelectItems(params.singleSelectItems, params.singleSelectedItem, params.onSingleItemClickListener);
        } else {
            selectableItemsContainer.removeAllViews();
        }
        setMaxWidth();
    }

    private void setMaxWidth() {
        int width = DeviceUtil.getScreenWidth(getContext()) - (int) (2 * getContext().getResources().getDimension(R.dimen.crowdfire_dialog_outer_margin));
        width = Math.min(width, (int) getContext().getResources().getDimension(R.dimen.crowdfire_dialog_maxwidth));
        FrameLayout.LayoutParams cardViewLayoutParams = new FrameLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        cardViewLayoutParams.bottomMargin = cardViewLayoutParams.leftMargin = cardViewLayoutParams.rightMargin = cardViewLayoutParams.topMargin =
                (int) getContext().getResources().getDimension(R.dimen.crowdfire_dialog_outer_margin);
        dialogCardView.setLayoutParams(cardViewLayoutParams);
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
            Window window = getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();

            wlp.gravity = dialogGravity;
            window.setAttributes(wlp);
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
            bottomPadding = (int) getContext().getResources().getDimension(R.dimen.crowdfire_dialog_footer_top_margin_half);
        } else {
            bottomPadding = 0;
        }
        if (cfDialogHeaderLinearLayout.getChildCount() == 0) {
            topPadding = (int) getContext().getResources().getDimension(R.dimen.crowdfire_dialog_header_bottom_margin_half);
        } else {
            topPadding = 0;
        }
        dialogCardView.setContentPadding(0, topPadding, 0, bottomPadding);
    }

    private void populateButtons(List<CFAlertDialogButton> buttons) {
        buttonContainerLinearLayout.removeAllViews();
        if (buttons.size() > 0) {
            for (int i = 0; i < buttons.size(); i++) {
                CFAlertDialogButton CFAlertDialogButton = buttons.get(i);
                View button = initButton(CFAlertDialogButton);
                buttonContainerLinearLayout.addView(button);
            }
            buttonContainerLinearLayout.setVisibility(View.VISIBLE);
        } else {
            buttonContainerLinearLayout.setVisibility(View.GONE);
        }
    }

    private View initButton(final CFAlertDialogButton CFAlertDialogButton) {

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View buttonContainer = layoutInflater.inflate(R.layout.crowdfire_dialog_button, null);
        TextView button = (TextView) buttonContainer.findViewById(R.id.crowdfire_dialog_button_textview);

        setButtonGravity(buttonContainer, CFAlertDialogButton);

        button.setText(CFAlertDialogButton.getButtonText());

        setButtonColors(button, CFAlertDialogButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CFAlertDialogButton.getOnClickListener().onClick(CFAlertDialog.this, 0);
            }
        });

        return buttonContainer;
    }

    private void setButtonGravity(View buttonContainer, CFAlertDialogButton CFAlertDialogButton) {
        LinearLayout.LayoutParams buttonContainerLayoutParams;
        if (CFAlertDialogButton.getGravity() == Gravity.FILL_HORIZONTAL) {
            buttonContainerLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            buttonContainerLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (CFAlertDialogButton.getGravity() == Gravity.LEFT) {
                buttonContainerLayoutParams.gravity = Gravity.LEFT;
            } else if (CFAlertDialogButton.getGravity() == Gravity.CENTER || CFAlertDialogButton.getGravity() == Gravity.CENTER_HORIZONTAL) {
                buttonContainerLayoutParams.gravity = Gravity.CENTER;
            } else {
                buttonContainerLayoutParams.gravity = Gravity.RIGHT;
            }
        }
        buttonContainer.setLayoutParams(buttonContainerLayoutParams);
    }

    private void setButtonColors(TextView button, CFAlertDialogButton CFAlertDialogButton) {
        if (CFAlertDialogButton.getBackgroundDrawable() != null) {
            setButtonBackgroundColor(button, CFAlertDialogButton.getBackgroundDrawable());
        } else if (CFAlertDialogButton.getBackgroundDrawableId() != -1) {
            setButtonBackgroundColor(button, ContextCompat.getDrawable(getContext(), CFAlertDialogButton.getBackgroundDrawableId()));
        }

        if (CFAlertDialogButton.getTextColor() != null) {
            button.setTextColor(CFAlertDialogButton.getTextColor());
        } else if (CFAlertDialogButton.getTextColorListId() != -1) {
            button.setTextColor(ContextCompat.getColorStateList(getContext(), CFAlertDialogButton.getTextColorListId()));
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
                                (int) getContext().getResources().getDimension(R.dimen.crowdfire_dialog_padding);
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
        cfDialogFooterLinearLayout.removeAllViews();
        cfDialogHeaderLinearLayout.removeAllViews();
    }

    public static class Builder {

        private DialogParams params;

        public Builder(Context context) {
            params = new DialogParams();
            this.params.context = context;
        }

        public Builder(Context context, @StyleRes int theme) {
            params = new DialogParams();
            this.params.context = context;
            this.params.theme = theme;
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

        public Builder addButton(CFAlertDialogButton button) {
            this.params.buttons.add(button);
            return this;
        }

        public Builder addPositiveButton(String buttonText, OnClickListener onClickListener) {
            addButton(CFAlertDialogButton.getPositiveButton(buttonText, onClickListener));
            return this;
        }

        public Builder addNegativeButton(String buttonText, OnClickListener onClickListener) {
            addButton(CFAlertDialogButton.getNegativeButton(buttonText, onClickListener));
            return this;
        }

        public Builder addNeutralButton(String buttonText, OnClickListener onClickListener) {
            addButton(CFAlertDialogButton.getNeutralButton(buttonText, onClickListener));
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
        private CharSequence message, title;
        private int theme = R.style.CFDialogStyle,
                dialogGravity = -1,
                textGravity = Gravity.LEFT,
                iconDrawableId = -1,
                contentImageDrawableId = -1;
        private View headerView, footerView;
        private int headerViewId = -1, footerViewId = -1;
        private Drawable contentImageDrawable, iconDrawable;
        private List<CFAlertDialogButton> buttons = new ArrayList<>();
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
}

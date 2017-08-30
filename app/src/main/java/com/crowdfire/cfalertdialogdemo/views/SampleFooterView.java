package com.crowdfire.cfalertdialogdemo.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.crowdfire.cfalertdialog.views.CFPushButton;
import com.crowdfire.cfalertdialogdemo.R;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.crowdfire.cfalertdialogdemo.views.SampleFooterView.ConfigurationState.COLLAPSED;
import static com.crowdfire.cfalertdialogdemo.views.SampleFooterView.ConfigurationState.EXPANDED;
import static com.crowdfire.cfalertdialogdemo.views.SampleFooterView.HeaderState.HIDDEN;
import static com.crowdfire.cfalertdialogdemo.views.SampleFooterView.HeaderState.SHOWN;

/**
 * Created by rahul on 11/07/17.
 */

public class SampleFooterView extends LinearLayout {

    @BindView(R.id.background_color_preview)
    View backgroundColorPreview;
    @BindView(R.id.header_toggle_button)
    CFPushButton headerToggleButton;
    @BindView(R.id.configuration_container)
    LinearLayout configurationContainer;
    @BindView(R.id.configuration_toggle_button)
    CFPushButton configurationToggleButton;
    FooterActionListener listener;

    public SampleFooterView(Context context) {
        this(context, null, 0);
    }

    public SampleFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SampleFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (context instanceof FooterActionListener) {
            listener = (FooterActionListener) context;
        } else {
            throw new IllegalStateException(context + " must implement" + FooterActionListener.class.getSimpleName());
        }
        init();
    }

    @OnClick({R.id.background_color_preview, R.id.header_toggle_button, R.id.configuration_toggle_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.background_color_preview:
                changeBackgroundColor();
                break;
            case R.id.header_toggle_button:
                toggleHeaderState((HeaderState) headerToggleButton.getTag());
                break;
            case R.id.configuration_toggle_button:
                toggleState();
                break;
        }
    }

    private void toggleHeaderState(HeaderState headerState) {
        switch (headerState) {
            case SHOWN:
                hideHeader();
                break;
            case HIDDEN:
                showHeader();
                break;
            default:
                break;
        }
    }

    private void showHeader() {
        headerToggleButton.setTag(SHOWN);
        headerToggleButton.setText("Remove Header");
        ViewCompat.setBackground(headerToggleButton, ContextCompat.getDrawable(getContext(), R.drawable.remove_header_button_background));
        listener.onHeaderAdded();
    }

    private void hideHeader() {
        headerToggleButton.setTag(HIDDEN);
        headerToggleButton.setText("Add Header");
        ViewCompat.setBackground(headerToggleButton, ContextCompat.getDrawable(getContext(), R.drawable.footer_button_drawable));
        listener.onHeaderRemoved();
    }

    private void changeBackgroundColor() {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        ((GradientDrawable) backgroundColorPreview.getBackground()).setColor(color);
        listener.onBackgroundColorChanged(color);
    }

    private void init() {
        inflate(getContext(), R.layout.dialog_footer_layout, this);
        bindViews();
    }

    private void bindViews() {
        ButterKnife.bind(this);
        collapseConfiguration();
        if (listener.isHeaderVisible()) {
            showHeader();
        } else {
            hideHeader();
        }
    }

    private void toggleState() {
        switch ((ConfigurationState) configurationToggleButton.getTag()) {
            case EXPANDED:
                collapseConfiguration();
                break;
            case COLLAPSED:
                expandConfiguration();
                break;
            default:
                break;
        }
    }

    void collapseConfiguration() {
        configurationToggleButton.setTag(COLLAPSED);
        configurationToggleButton.setText("Configurations");
        configurationToggleButton.setTextColor(Color.WHITE);
        ViewCompat.setBackground(configurationToggleButton, ContextCompat.getDrawable(getContext(), R.drawable.footer_button_drawable));
        configurationContainer.setVisibility(GONE);
    }

    void expandConfiguration() {
        configurationToggleButton.setTag(EXPANDED);
        configurationToggleButton.setText("Close");
        configurationToggleButton.setTextColor(Color.BLACK);
        ViewCompat.setBackground(configurationToggleButton, ContextCompat.getDrawable(getContext(), R.drawable.footer_button_disabled_drawable));
        configurationContainer.setVisibility(VISIBLE);
    }

    enum ConfigurationState {
        COLLAPSED, EXPANDED
    }

    public enum HeaderState {
        SHOWN, HIDDEN
    }

    public interface FooterActionListener {

        void onBackgroundColorChanged(int backgroundColor);

        void onHeaderAdded();

        void onHeaderRemoved();

        boolean isHeaderVisible();

    }

}

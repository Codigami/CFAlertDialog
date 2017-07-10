package com.crowdfire.cfalertdialogdemo.views;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.crowdfire.cfalertdialogdemo.R;

/**
 * Created by rahul on 10/07/17.
 */

public class ColorSelectionView extends LinearLayout implements SeekBar.OnSeekBarChangeListener{


    // region Variables
    private static int COLOR_MAX_VALUE = 255;

    private SeekBar seekBarRed, seekBarGreen, seekBarBlue, seekBarAlpha;
    private View selectedColorPreview;

    public int selectedColor;
    // endregion


    public ColorSelectionView(Context context) {
        super(context);
        init();
    }

    public ColorSelectionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorSelectionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.color_selection_view, this);

        bindViews();
    }

    private void bindViews() {

        selectedColorPreview = findViewById(R.id.selected_color_preview);

        seekBarRed = (SeekBar) findViewById(R.id.seekbar_red);
        seekBarRed.setMax(COLOR_MAX_VALUE);
        seekBarRed.setOnSeekBarChangeListener(this);
        seekBarBlue = (SeekBar) findViewById(R.id.seekbar_blue);
        seekBarBlue.setMax(COLOR_MAX_VALUE);
        seekBarBlue.setOnSeekBarChangeListener(this);
        seekBarGreen = (SeekBar) findViewById(R.id.seekbar_green);
        seekBarGreen.setMax(COLOR_MAX_VALUE);
        seekBarGreen.setOnSeekBarChangeListener(this);
        seekBarAlpha = (SeekBar) findViewById(R.id.seekbar_alpha);
        seekBarAlpha.setMax(COLOR_MAX_VALUE);
        seekBarAlpha.setProgress(COLOR_MAX_VALUE);
        seekBarAlpha.setOnSeekBarChangeListener(this);
    }

    // region SeekBar value changed listeners
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        updateSelectedColorPreview();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
    // endregion

    private void updateSelectedColorPreview() {

        // Create color with selected color values
        int redValue = seekBarRed.getProgress();
        int greenValue = seekBarGreen.getProgress();
        int blueValue = seekBarBlue.getProgress();
        int alpha = seekBarAlpha.getProgress();

        selectedColor = Color.argb(alpha, redValue, greenValue, blueValue);

        selectedColorPreview.setBackgroundColor(selectedColor);
    }

    public void setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;

        seekBarAlpha.setProgress(Color.alpha(selectedColor));
        seekBarRed.setProgress(Color.red(selectedColor));
        seekBarGreen.setProgress(Color.green(selectedColor));
        seekBarBlue.setProgress(Color.blue(selectedColor));
    }
}

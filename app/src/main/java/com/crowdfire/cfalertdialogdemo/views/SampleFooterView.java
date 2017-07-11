package com.crowdfire.cfalertdialogdemo.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.crowdfire.cfalertdialogdemo.R;

/**
 * Created by rahul on 11/07/17.
 */

public class SampleFooterView extends LinearLayout {

    public SampleFooterView(Context context) {
        super(context);

        init();
    }

    public SampleFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public SampleFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        inflate(getContext(), R.layout.dialog_footer_layout, this);

        bindViews();
    }

    private void bindViews() {

    }
}

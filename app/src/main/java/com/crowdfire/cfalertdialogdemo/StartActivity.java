package com.crowdfire.cfalertdialogdemo;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.crowdfire.cfalertdialogdemo.views.ColorSelectionView;
import com.crowdfire.cfalertdialogdemo.views.SampleFooterView;

import static com.crowdfire.cfalertdialog.CFAlertDialog.CFAlertActionAlignment;
import static com.crowdfire.cfalertdialog.CFAlertDialog.CFAlertActionStyle;
import static com.crowdfire.cfalertdialog.CFAlertDialog.OnClickListener;

/**
 * Created by rahul on 06/07/17.
 */

public class StartActivity extends AppCompatActivity implements SampleFooterView.FooterActionListener {

    private static final int DEFAULT_BACKGROUND_COLOR = Color.parseColor("#B3000000");

    private EditText titleEditText, messageEditText;
    private CheckBox positiveButtonCheckbox, negativeButtonCheckbox, neutralButtonCheckbox, addHeaderCheckBox, addFooterCheckBox, closesOnBackgroundTapCheckBox;
    private RadioButton itemsRadioButton, multiChoiceRadioButton,
            singleChoiceRadioButton;
    private RadioButton textGravityLeft, textGravityCenter, textGravityRight;
    private RadioButton buttonGravityLeft, buttonGravityRight, buttonGravityCenter, buttonGravityFull;
    private CheckBox showTitleIcon;
    private RadioButton topDialogGravityRadioButton, centerDialogGravityRadioButton, bottomDialogGravityRadioButton;
    private View selectedBackgroundColorView, selectBackgroundColorContainer;
    private FloatingActionButton showDialogFab;
    private CFAlertDialog alertDialog;
    private CFAlertDialog colorSelectionDialog;
    private ColorSelectionView colorSelectionView;
    private boolean headerVisibility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        bindViews();

        showDialogFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCFDialog();
            }
        });

        selectBackgroundColorContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorSelectionAlert();
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void showColorSelectionAlert() {
        if (colorSelectionDialog == null) {

            colorSelectionView = new ColorSelectionView(this);
            colorSelectionView.setSelectedColor(DEFAULT_BACKGROUND_COLOR);
            colorSelectionDialog = new CFAlertDialog.Builder(this)
                    .addButton("Done", -1, -1, CFAlertActionStyle.POSITIVE, CFAlertActionAlignment.JUSTIFIED, new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Update the color preview
                            selectedBackgroundColorView.setBackgroundColor(colorSelectionView.selectedColor);

                            // dismiss the dialog
                            colorSelectionDialog.dismiss();
                        }
                    })
                    .setBackgroundColor(DEFAULT_BACKGROUND_COLOR)
                    .setDialogVerticalGravity(Gravity.BOTTOM)
                    .setHeaderView(colorSelectionView)
                    .onDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {

                            // Update the color preview
                            selectedBackgroundColorView.setBackgroundColor(colorSelectionView.selectedColor);
                        }
                    })
                    .create();
        }
        colorSelectionDialog.show();
    }

    private void showCFDialog() {

        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this, R.style.CFDialog);

        // Vertical position of the dialog
        if (topDialogGravityRadioButton.isChecked()) {
            builder.setDialogVerticalGravity(Gravity.TOP);
        }
        if (centerDialogGravityRadioButton.isChecked()) {
            builder.setDialogVerticalGravity(Gravity.CENTER_VERTICAL);
        }
        if (bottomDialogGravityRadioButton.isChecked()) {
            builder.setDialogVerticalGravity(Gravity.BOTTOM);
        }

        // Background
        if (colorSelectionView != null) {
            builder.setBackgroundColor(colorSelectionView.selectedColor);
        } else {
            builder.setBackgroundColor(DEFAULT_BACKGROUND_COLOR);
        }

        // Title and message
        builder.setTitle(titleEditText.getText());
        builder.setMessage(messageEditText.getText());

        if (textGravityLeft.isChecked()) {
            builder.setTextGravity(Gravity.START);
        } else if (textGravityCenter.isChecked()) {
            builder.setTextGravity(Gravity.CENTER_HORIZONTAL);
        } else if (textGravityRight.isChecked()) {
            builder.setTextGravity(Gravity.END);
        }

        // Title icon
        if (showTitleIcon.isChecked()) {
            builder.setIcon(R.drawable.icon_drawable);
        }

        // Buttons
        if (positiveButtonCheckbox.isChecked()) {

            // Add a sample positive button
            builder.addButton("Positive", -1, -1, CFAlertActionStyle.POSITIVE, getButtonGravity(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(StartActivity.this, "Positive", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            });
        }
        if (negativeButtonCheckbox.isChecked()) {

            // Add a sample negative button
            builder.addButton("Negative", -1, -1, CFAlertActionStyle.NEGATIVE, getButtonGravity(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(StartActivity.this, "Negative", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            });
        }
        if (neutralButtonCheckbox.isChecked()) {

            // Add a sample neutral button
            builder.addButton("Neutral", -1, -1, CFAlertActionStyle.DEFAULT, getButtonGravity(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(StartActivity.this, "Neutral", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            });
        }

        // Add Header
        if (addHeaderCheckBox.isChecked()) {
            builder.setHeaderView(R.layout.dialog_header_layout);
            headerVisibility = true;
        }

        // Add Footer
        if (addFooterCheckBox.isChecked()) {
            builder.setFooterView(new SampleFooterView(this));
        }

        // Selection Items
        if (itemsRadioButton.isChecked()) {

            // List items
            builder.setItems(new String[]{"First", "Second", "Third"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            Toast.makeText(StartActivity.this, "First", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            Toast.makeText(StartActivity.this, "Second", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            Toast.makeText(StartActivity.this, "Third", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
        } else if (singleChoiceRadioButton.isChecked()) {

            // Single choice list items
            builder.setSingleChoiceItems(new String[]{"First", "Second", "Third"}, 1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            Toast.makeText(StartActivity.this, "First", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            Toast.makeText(StartActivity.this, "Second", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            Toast.makeText(StartActivity.this, "Third", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
        } else if (multiChoiceRadioButton.isChecked()) {

            // Multi choice list items
            builder.setMultiChoiceItems(new String[]{"First", "Second", "Third"}, new boolean[]{true, false, false}, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    switch (which) {
                        case 0:
                            Toast.makeText(StartActivity.this, "First " + (isChecked ? "Checked" : "Unchecked"), Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            Toast.makeText(StartActivity.this, "Second " + (isChecked ? "Checked" : "Unchecked"), Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            Toast.makeText(StartActivity.this, "Third " + (isChecked ? "Checked" : "Unchecked"), Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });

        }

        // Cancel on background tap
        builder.setCancelable(closesOnBackgroundTapCheckBox.isChecked());

        alertDialog = builder.show();
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                onDialogDismiss();
            }
        });
        //alertDialog.show();
    }

    private CFAlertActionAlignment getButtonGravity() {

        if (buttonGravityLeft.isChecked()) {
            return CFAlertActionAlignment.START;
        }
        if (buttonGravityCenter.isChecked()) {
            return CFAlertActionAlignment.CENTER;
        }
        if (buttonGravityRight.isChecked()) {
            return CFAlertActionAlignment.END;
        }
        if (buttonGravityFull.isChecked()) {
            return CFAlertActionAlignment.JUSTIFIED;
        }
        return CFAlertActionAlignment.JUSTIFIED;
    }

    private void bindViews() {
        titleEditText = (EditText) findViewById(R.id.title_edittext);
        messageEditText = (EditText) findViewById(R.id.message_edittext);

        textGravityLeft = (RadioButton) findViewById(R.id.text_gravity_left);
        textGravityCenter = (RadioButton) findViewById(R.id.text_gravity_center);
        textGravityRight = (RadioButton) findViewById(R.id.text_gravity_right);

        positiveButtonCheckbox = (CheckBox) findViewById(R.id.positive_button_checkbox);
        negativeButtonCheckbox = (CheckBox) findViewById(R.id.negative_button_checkbox);
        neutralButtonCheckbox = (CheckBox) findViewById(R.id.neutral_button_checkbox);

        addHeaderCheckBox = (CheckBox) findViewById(R.id.add_header_checkbox);
        addFooterCheckBox = (CheckBox) findViewById(R.id.add_footer_checkbox);

        buttonGravityLeft = (RadioButton) findViewById(R.id.button_gravity_left);
        buttonGravityCenter = (RadioButton) findViewById(R.id.button_gravity_center);
        buttonGravityRight = (RadioButton) findViewById(R.id.button_gravity_right);
        buttonGravityFull = (RadioButton) findViewById(R.id.button_gravity_justified);

        itemsRadioButton = (RadioButton) findViewById(R.id.items_radio_button);
        multiChoiceRadioButton = (RadioButton) findViewById(R.id.multi_select_choice_items_radio_button);
        singleChoiceRadioButton = (RadioButton) findViewById(R.id.single_choice_items_radio_button);

        showTitleIcon = (CheckBox) findViewById(R.id.show_title_icon);

        topDialogGravityRadioButton = (RadioButton) findViewById(R.id.top_dialog_gravity_radio_button);
        centerDialogGravityRadioButton = (RadioButton) findViewById(R.id.center_dialog_gravity_radio_button);
        bottomDialogGravityRadioButton = (RadioButton) findViewById(R.id.bottom_dialog_gravity_radio_button);

        closesOnBackgroundTapCheckBox = ((CheckBox) findViewById(R.id.closes_on_background_tap));
        selectedBackgroundColorView = findViewById(R.id.background_color_preview);
        selectedBackgroundColorView.setBackgroundColor(DEFAULT_BACKGROUND_COLOR);
        selectBackgroundColorContainer = findViewById(R.id.background_color_selection_container);

        showDialogFab = (FloatingActionButton) findViewById(R.id.fab);
    }

    @Override
    public void onBackgroundColorChanged(int backgroundColor) {
        alertDialog.setCFDialogBackgroundColor(backgroundColor, true);
    }

    @Override
    public void onHeaderAdded() {
        if (alertDialog != null) { alertDialog.setHeaderView(R.layout.dialog_header_layout); }
        headerVisibility = true;

    }

    @Override
    public void onHeaderRemoved() {
        if (alertDialog != null) { alertDialog.setHeaderView(null); }
        headerVisibility = false;
    }

    private void onDialogDismiss() {
        headerVisibility = false;
    }

    @Override
    public boolean isHeaderVisible() {
        return headerVisibility;
    }

}

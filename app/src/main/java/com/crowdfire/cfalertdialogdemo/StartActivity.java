package com.crowdfire.cfalertdialogdemo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.crowdfire.cfalertdialog.CFAlertDialog;

import static com.crowdfire.cfalertdialog.CFAlertDialog.*;

/**
 * Created by rahul on 06/07/17.
 */

public class StartActivity extends AppCompatActivity {

    private AppCompatSpinner textGravitySpinner, buttonGravitySpinner;
    private EditText titleEditText, messageEditText;
    private CheckBox positiveButtonCheckbox, negativeButtonCheckbox, neutralButtonCheckbox, addHeaderCheckBox, addFooterCheckBox, closesOnBackgroundTapCheckBox;
    private RadioButton itemsRadioButton, multiChoiceRadioButton,
            singleChoiceRadioButton;
    private RadioButton iconShowRadioButton, imageContentShowRadioButton;
    private RadioButton topDialogGravityRadioButton, centerDialogGravityRadioButton, bottomDialogGravityRadioButton;
    private FloatingActionButton showDialogFab;
    private CFAlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        bindViews();

        textGravitySpinner.setSelection(0);
        buttonGravitySpinner.setSelection(2);

        showDialogFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCFDialog();
            }
        });
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
        builder.setBackgroundColor(R.color.sample_bg);

        // Title and message
        builder.setTitle(titleEditText.getText());
        builder.setMessage(messageEditText.getText());
        switch (textGravitySpinner.getSelectedItemPosition()) {
            case 0:
                builder.setTextGravity(Gravity.START);
                break;
            case 1:
                builder.setTextGravity(Gravity.CENTER_HORIZONTAL);
                break;
            case 2:
                builder.setTextGravity(Gravity.END);
                break;
        }

        // Title icon
        if (iconShowRadioButton.isChecked()) {
            builder.setIcon(R.drawable.icon_drawable);
        }

        // Image header
        if (imageContentShowRadioButton.isChecked()) {
            builder.setContentImageDrawable(R.drawable.image_content_drawable);
        }

        // Buttons
        if (positiveButtonCheckbox.isChecked()) {

            // Add a sample positive button
            builder.addButton("Positive", CFAlertActionStyle.POSITIVE, getButtonGravity(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(StartActivity.this, "Positive", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            });
        }
        if (negativeButtonCheckbox.isChecked()) {

            // Add a sample negative button
            builder.addButton("Negative", CFAlertActionStyle.NEGATIVE, getButtonGravity(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(StartActivity.this, "Negative", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            });
        }
        if (neutralButtonCheckbox.isChecked()) {

            // Add a sample neutral button
            builder.addButton("Neutral", CFAlertActionStyle.DEFAULT, getButtonGravity(), new DialogInterface.OnClickListener() {
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
        }

        // Add Footer
        if (addFooterCheckBox.isChecked()) {
            builder.setFooterView(R.layout.dialog_footer_layout);
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

        // TODO:- Add this to the demo UI
        builder.setCancelable(closesOnBackgroundTapCheckBox.isChecked());

        alertDialog = builder.show();
    }

    private CFAlertActionAlignment getButtonGravity() {
        switch (buttonGravitySpinner.getSelectedItemPosition()) {
            case 0:
                return CFAlertActionAlignment.START;
            case 1:
                return CFAlertActionAlignment.CENTER;
            case 2:
                return CFAlertActionAlignment.END;
            case 3:
                return CFAlertActionAlignment.JUSTIFIED;
            default:
                return CFAlertActionAlignment.JUSTIFIED;
        }
    }

    private void bindViews() {
        titleEditText = (EditText) findViewById(R.id.title_edittext);
        messageEditText = (EditText) findViewById(R.id.message_edittext);
        textGravitySpinner = (AppCompatSpinner) findViewById(R.id.text_gravity_selction_spinner);
        positiveButtonCheckbox = (CheckBox) findViewById(R.id.positive_button_checkbox);
        negativeButtonCheckbox = (CheckBox) findViewById(R.id.negative_button_checkbox);
        neutralButtonCheckbox = (CheckBox) findViewById(R.id.neutral_button_checkbox);
        addHeaderCheckBox = (CheckBox) findViewById(R.id.add_header_checkbox);
        addFooterCheckBox = (CheckBox) findViewById(R.id.add_footer_checkbox);
        closesOnBackgroundTapCheckBox = ((CheckBox) findViewById(R.id.closes_on_background_tap));
        buttonGravitySpinner = (AppCompatSpinner) findViewById(R.id.button_gravity_selction_spinner);
        itemsRadioButton = (RadioButton) findViewById(R.id.items_radio_button);
        multiChoiceRadioButton = (RadioButton) findViewById(R.id.multi_select_choice_items_radio_button);
        singleChoiceRadioButton = (RadioButton) findViewById(R.id.single_choice_items_radio_button);
        iconShowRadioButton = (RadioButton) findViewById(R.id.icon_show_radio_button);
        imageContentShowRadioButton = (RadioButton) findViewById(R.id.image_content_show_radio_button);
        topDialogGravityRadioButton = (RadioButton) findViewById(R.id.top_dialog_gravity_radio_button);
        centerDialogGravityRadioButton = (RadioButton) findViewById(R.id.center_dialog_gravity_radio_button);
        bottomDialogGravityRadioButton = (RadioButton) findViewById(R.id.bottom_dialog_gravity_radio_button);
        showDialogFab = (FloatingActionButton) findViewById(R.id.fab);
    }
}

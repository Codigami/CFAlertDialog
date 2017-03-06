package com.android.crowdfire.examples;

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

import com.crowdfire.alertDialog.CFAlertDialog;
import com.crowdfire.alertDialog.CFAlertDialogButton;

public class MainActivity extends AppCompatActivity {

    private AppCompatSpinner textGravitySpinner, buttonGravitySpinner;
    private EditText titleEditText, messageEditText;
    private CheckBox positiveButtonCheckbox, negativeButtonCheckbox, neutralButtonCheckbox,
            defaultButtonCheckbox, addHeaderCheckBox, addFooterCheckBox;
    private RadioButton itemsRadioButton, multiChoiceRadioButton,
            singleChoiceRadioButton, iconShowRadioButton, darkThemeRadioButton,
            imageContentShowRadioButton, bottomDialogGravityRadioButton;
    private FloatingActionButton showDialogFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        CFAlertDialog.Builder builder;
        if (darkThemeRadioButton.isChecked()) {
            builder = new CFAlertDialog.Builder(this, R.style.crowdfire_dialog_style_dark);
        } else {
            builder = new CFAlertDialog.Builder(this, R.style.crowdfire_dialog_style_light);
        }
        if (bottomDialogGravityRadioButton.isChecked()) {
            builder.setDialogVerticalGravity(Gravity.BOTTOM);
        }
        builder.setTitle(titleEditText.getText());
        builder.setMessage(messageEditText.getText());
        switch (textGravitySpinner.getSelectedItemPosition()) {
            case 0:
                builder.setTextGravity(Gravity.LEFT);
                break;
            case 1:
                builder.setTextGravity(Gravity.CENTER_HORIZONTAL);
                break;
            case 2:
                builder.setTextGravity(Gravity.RIGHT);
                break;
        }

        if (iconShowRadioButton.isChecked()) {
            builder.setIcon(R.drawable.icon_drawable);
        }

        if (imageContentShowRadioButton.isChecked()) {
            builder.setContentImageDrawable(R.drawable.image_content_drawable);
        }

        if (positiveButtonCheckbox.isChecked()) {
            CFAlertDialogButton button = CFAlertDialogButton.getPositiveButton("Positive", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "Positive", Toast.LENGTH_SHORT).show();
                }
            });
            button.setGravity(getButtonGravity());
            builder.addButton(button);
        }
        if (negativeButtonCheckbox.isChecked()) {
            CFAlertDialogButton button = CFAlertDialogButton.getNegativeButton("Negative", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "Negative", Toast.LENGTH_SHORT).show();
                }
            });
            button.setGravity(getButtonGravity());
            builder.addButton(button);
        }
        if (neutralButtonCheckbox.isChecked()) {
            CFAlertDialogButton button = CFAlertDialogButton.getNeutralButton("Neutral", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "Neutral", Toast.LENGTH_SHORT).show();
                }
            });
            button.setGravity(getButtonGravity());
            builder.addButton(button);
        }
        if (defaultButtonCheckbox.isChecked()) {
            builder.addButton(new CFAlertDialogButton.Builder("Default", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "Default", Toast.LENGTH_SHORT).show();
                }
            }).gravity(getButtonGravity()).build());
        }

        if (addHeaderCheckBox.isChecked()) {
            builder.setHeaderView(R.layout.dialog_header_layout);
        }

        if (addFooterCheckBox.isChecked()) {
            builder.setFooterView(R.layout.dialog_footer_layout);
        }

        if (itemsRadioButton.isChecked()) {
            builder.setItems(new String[]{"First", "Second", "Third"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            Toast.makeText(MainActivity.this, "First", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            Toast.makeText(MainActivity.this, "Second", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            Toast.makeText(MainActivity.this, "Third", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
        } else if (multiChoiceRadioButton.isChecked()) {
            builder.setMultiChoiceItems(new String[]{"First", "Second", "Third"}, new boolean[]{true, false, false}, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    switch (which) {
                        case 0:
                            Toast.makeText(MainActivity.this, "First " + (isChecked ? "Checked" : "Unchecked"), Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            Toast.makeText(MainActivity.this, "Second " + (isChecked ? "Checked" : "Unchecked"), Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            Toast.makeText(MainActivity.this, "Third " + (isChecked ? "Checked" : "Unchecked"), Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });

        } else if (singleChoiceRadioButton.isChecked()) {
            builder.setSingleChoiceItems(new String[]{"First", "Second", "Third"}, 1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            Toast.makeText(MainActivity.this, "First", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            Toast.makeText(MainActivity.this, "Second", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            Toast.makeText(MainActivity.this, "Third", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
        }

        builder.show();
    }

    private int getButtonGravity() {
        switch (buttonGravitySpinner.getSelectedItemPosition()) {
            case 0:
                return Gravity.LEFT;
            case 1:
                return Gravity.CENTER_HORIZONTAL;
            case 2:
                return Gravity.RIGHT;
            case 3:
                return Gravity.FILL_HORIZONTAL;
            default:
                return Gravity.FILL_HORIZONTAL;
        }
    }

    private void bindViews() {
        titleEditText = (EditText) findViewById(R.id.title_edittext);
        messageEditText = (EditText) findViewById(R.id.message_edittext);
        textGravitySpinner = (AppCompatSpinner) findViewById(R.id.text_gravity_selction_spinner);
        positiveButtonCheckbox = (CheckBox) findViewById(R.id.positive_button_checkbox);
        negativeButtonCheckbox = (CheckBox) findViewById(R.id.negative_button_checkbox);
        neutralButtonCheckbox = (CheckBox) findViewById(R.id.neutral_button_checkbox);
        defaultButtonCheckbox = (CheckBox) findViewById(R.id.default_button_checkbox);
        addHeaderCheckBox = (CheckBox) findViewById(R.id.add_header_checkbox);
        addFooterCheckBox = (CheckBox) findViewById(R.id.add_footer_checkbox);
        buttonGravitySpinner = (AppCompatSpinner) findViewById(R.id.button_gravity_selction_spinner);
        itemsRadioButton = (RadioButton) findViewById(R.id.items_radio_button);
        multiChoiceRadioButton = (RadioButton) findViewById(R.id.multi_select_choice_items_radio_button);
        singleChoiceRadioButton = (RadioButton) findViewById(R.id.single_choice_items_radio_button);
        iconShowRadioButton = (RadioButton) findViewById(R.id.icon_show_radio_button);
        darkThemeRadioButton = (RadioButton) findViewById(R.id.dark_theme_radio_button);
        imageContentShowRadioButton = (RadioButton) findViewById(R.id.image_content_show_radio_button);
        bottomDialogGravityRadioButton = (RadioButton) findViewById(R.id.bottom_dialog_gravity_radio_button);
        showDialogFab = (FloatingActionButton) findViewById(R.id.fab);
    }
}

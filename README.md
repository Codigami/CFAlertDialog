# CFAlertDialog
[![CocoaPods](https://img.shields.io/cocoapods/v/CFAlertViewController.svg)](https://cocoapods.org/pods/CFAlertViewController)
[![CocoaPods](https://img.shields.io/cocoapods/dt/CFAlertViewController.svg)](https://cocoapods.org/pods/CFAlertViewController)
[![license](https://img.shields.io/github/license/codigami/cfalertviewcontroller.svg)](https://github.com/Codigami/CFAlertViewController/blob/master/README.md)
[![Twitter URL](https://img.shields.io/twitter/url/http/shields.io.svg?style=social)](https://twitter.com/home?status=CFAlertViewController%20-%20Now%20display%20and%20customise%20alerts%20and%20action%20sheets%20on%20iOS%20like%20never%20before!%20%23OpenSource%20https%3A//github.com/Codigami/CFAlertViewController)

`CFAlertDialog` is a library that helps you display and customise **alert dialogs** on Android. It offers an adaptive UI support. It’s functionality is almost similar to the native `AlertDialog`.

<img src="https://github.com/Codigami/CFAlertViewController/blob/develop/Images/Demo.gif">

## Requirements :

- CFAlertDialog works on any device with Android API level 14 and above. 

### Install using gradle

```java 
compile 'com.crowdfireapp.cfalertdialog:cfalertdialog:1.0.0'
```


## Usage :  
<p>
    <img src="https://github.com/Codigami/CFAlertViewController/blob/develop/Images/Alert%20%26%20Action%20sheet.png" style="width: 100%" />
</p>

The above shown alert types can easily be implemented using the code snippet given below by some small tweaks
```android
        // Create Alert using Builder
                CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this)
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                        .setTitle("You've hit the limit")
                        .setMessage("Looks like you've hit your usage limit. Upgrade to our paid plan to continue without any limits.")
                        .addButton("UPGRADE", -1, -1, CFAlertActionStyle.POSITIVE, CFAlertActionAlignment.END, (dialog, which) -> {
                            Toast.makeText(BaseActivity.this, "Upgrade tapped", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        })
        
                // Show the alert
                builder.show();
```

## Customisations :

##### Alert Type
The alert type will determine the position and animation style for the dialog. You may set this by calling the `setAlertStyle` method with any of the following values.
```java
CFAlertStyle.NOTIFICATION,
CFAlertStyle.ALERT,
CFAlertStyle.BOTTOM_SHEET
``` 
The default type is `ALERT`.

##### Title and Message
You can set a custom title and message text in the alert, using the `setTitle` method on the builder (pass `null` if you don’t need them).

##### Title Color and Message Color
You can set a custom title and message text color in the alert, using the `setTextColor` on the builder.

##### Text Alignment
You can customise alignment of the title and message. Use the  `setTextGravity` method on the builder with any of the following values: 

```java
Gravity.START,
Gravity.CENTER_HORIZONTAL,
Gravity.END
```

##### Background color
You can change the background (overlay) color of the alert using the method `setBackgroundColor` on the builder with the color of your choice.

##### Dismiss on background tap
This determines whether the Alert dialog is dismissed when user taps on the background. Use the method `setCancelable` with a boolean value. By default it is set to true. 

##### Header / Footer
 You can add header and footer to the dialog. Use the method `setHeaderView` and `setFooterView` with any custom View. You can also pass the layout resource directly into the header/footer. Pass `null` to remove any existing header/footer.
 
 1) Some examples where you can make the use of header in alert (the dollar image is in header)
<p>
    <img src="https://github.com/Codigami/CFAlertViewController/blob/develop/Images/Alert%20With%20Header.png" style="width: 100%" />
</p>

```java
builder.setHeaderView(R.layout.dialog_header_layout);
OR
builder.setHeaderView(headerView);
```

2) Some examples where you can make the use of footer in alert
<p>
    <img src="https://github.com/Codigami/CFAlertViewController/blob/develop/Images/Alert%20With%20Footer.png" style="width: 100%" />
</p>

```java
builder.setFooterView(R.layout.dialog_footer_layout);
OR
builder.setFooterView(footerView);
```

##### Dismiss Callback
You may set a callback when dialog is dismissed with the native `setOnDismissListener` on the alert object.
```java
CFAlertDialog alertDialog = builder.show();
alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
    @Override
    public void onDismiss(DialogInterface dialog) {
        
        // Do something here when dialog dismissed.
    }
});
```

### Action Buttons
You may add as many action buttons with the required styles. Use the `addButton` method on the builder.
```java
builder.addButton("UPGRADE", Color.parseColor("#FFFFFF"), Color.parseColor("#429ef4"), CFAlertActionStyle.POSITIVE, CFAlertActionAlignment.CENTER, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(context, "Upgrade tapped", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
```                           
##### Title
You can set the title of action button to be added.

##### Action Style
Configure the style of the action button that is to be added to alert view. Set `style` property of the above method with one of the following Action style  
```java
 CFAlertActionStyle.DEFAULT,
 CFAlertActionStyle.POSITIVE,
 CFAlertActionStyle.NEGATIVE
```

##### Actions Alignment
Configure the alignment of the action button added to the alert view. Set `alignment` property of  CFAction constructor with one of the following action types
```java
 CFAlertActionAlignment.START,   
 CFAlertActionAlignment.END,
 CFAlertActionAlignment.CENTER,
 CFAlertActionAlignment.JUSTIFIED   // Action Button occupies the full width
```

##### Callback
Pass an `onClickListener` to receive callbacks when the action buttons are tapped. 

## License
This code is distributed under the terms and conditions of the MIT license.

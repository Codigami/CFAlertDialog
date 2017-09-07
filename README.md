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

```gradle 
compile 'com.squareup.retrofit2:retrofit:2.3.0'
```


## Usage :  
<p>
    <img src="https://github.com/Codigami/CFAlertViewController/blob/develop/Images/Alert%20%26%20Action%20sheet.png" style="width: 100%" />
</p>

The above shown alert and actionsheet can easily be implemented using the code snippet given below by some small tweaks
```java
// Create Alert using Builder
CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this);
builder.setDialogVerticalGravity(Gravity.CENTER_VERTICAL);      // Set position (TOP, CENTER_VERTICAL, BOTTOM)
builder.setBackgroundColor(bgColor);        // Set background color
builder.setTitle("You've hit the limit");
builder.setMessage("Looks like you've hit your daily follow/unfollow limit. Upgrade to our paid plan to be able to remove your limits.");
```

## Customisations :

### Alerts

```swift
public convenience init(title: String?,
                        titleColor: UIColor?,
                        message: String?,
                        messageColor: UIColor?,
                        textAlignment: NSTextAlignment,
                        preferredStyle: CFAlertControllerStyle,
                        headerView: UIView?,
                        footerView: UIView?,
                        didDismissAlertHandler dismiss: CFAlertViewControllerDismissBlock?)
```

##### Title and Message
You can set a custom title and message text in the alert (pass `nil` if you don’t need them).

##### Title Color and Message Color
You can set a custom title and message text color in the alert (pass `nil` if you want to use Default color values).

##### Alignment
You can customise alignment of the title and message. Set the `textAlignment` property with one of the following values : 

```swift
NSTextAlignment.left,    
NSTextAlignment.right,    
NSTextAlignment.center
```

##### Alert Style  
Presentation style of the alert can be customised as Alert or Action sheet. Just set the `preferredStyle` property with one of the following values :
```swift
CFAlertControllerStyle.actionSheet,
CFAlertControllerStyle.alert
```

##### Background style
Background (overlay) of alert/action sheet can be blurred (Useful for security reasons in case the background needs to be hidden). Default value is `plain`. You can customize the blur style using `backgroundBlurView` property of type UIVisualEffectView. Update `backgroundStyle` property with one of the following enum values:

```swift
CFAlertControllerBackgroundStyle.plain,
CFAlertControllerBackgroundStyle.blur
```

##### Background color
You can change the background (overlay) color of the alert/actionsheet using the property `backgroundColor`.

##### Dismiss on background tap
By default, the alert gets dismissed after tapping on the background (overlay). Change `shouldDismissOnBackgroundTap` property to `false` to disable it.

##### Header / Footer
 You can add header and footer to the alert. Set properties `headerView` and `footerView` with custom views (subclass of UIView). You can pass nil to this properties to opt them out.  
 
 1) Some examples where you can make the use of header in alert (the dollar image is in header)
<p>
    <img src="https://github.com/Codigami/CFAlertViewController/blob/develop/Images/Alert%20With%20Header.png" style="width: 100%" />
</p>

2) Some examples where you can make the use of footer in alert
<p>
    <img src="https://github.com/Codigami/CFAlertViewController/blob/develop/Images/Alert%20With%20Footer.png" style="width: 100%" />
</p>

##### Callback
A block (of type CFAlertViewControllerDismissBlock) gets called when the Alert / Action Sheet is dismissed. You can use it to handle call back.

### Actions
```swift
public convenience init(title: String?,
                        style: CFAlertActionStyle,
                        alignment: CFAlertActionAlignment,
                        backgroundColor: UIColor?,
                        textColor: UIColor?,
                        handler: CFAlertActionHandlerBlock?) {
```                           
##### Title
You can set the title of action button to be added.  

##### Action Style
Configure the style of the action button that is to be added to alert view. Set `style` property of the above method with one of the following Action style  
```swift
 CFAlertActionStyle.Default,
 CFAlertActionStyle.Cancel,
 CFAlertActionStyle.Destructive
```

##### Actions Alignment
Configure the alignment of the action button added to the alert view. Set `alignment` property of  CFAction constructor with one of the following action types
```swift
 CFAlertActionAlignment.justified,   // Action Button occupies the full width
 CFAlertActionAlignment.right,
 CFAlertActionAlignment.left,
 CFAlertActionAlignment.center
```

##### Callback
A block (of type CFAlertActionHandlerBlock) gets invoked when action is tapped. 

## License
This code is distributed under the terms and conditions of the MIT license.

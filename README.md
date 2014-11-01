## rest2mobile plugin for Android Studio & IntelliJ IDEA

### Stable releases
* [1.1.0](https://github.com/magnetsystems/r2m-plugin-android/releases/1.1.0)
* [1.0.0](https://github.com/magnetsystems/r2m-plugin-android/releases/v1.0.0)

### Latest drop
* [Check releases](https://github.com/magnetsystems/r2m-plugin-android/releases/)

### Prerequisites
The rest2mobile plugin for Android Studio & IntelliJ IDEA has these prerequisites:

* Java JDK 1.6 or later
* Android Studio or IntelliJ IDEA
* Android 'minSDKVersion' value of 15 or later

### Build

Use Intellij 13 or later to create the plugin project from the source:
* create the project from the source directory <code>src</code>
* register the <code>res</code> and <code>l10n</code> directories as resources
* register the <code>libs</code> directory as a library 

Build the plugin zip using the <code>Build->Prepare Plugin Module ... for deployment</code> option.

### Releases

Download the latest release [here](https://github.com/magnetsystems/r2m-plugin-android/releases).

The following picture is a screenshot of the plugin: 

### Setup

The installation instructions are also described [here](http://developer.magnet.com/android).

Go to 
Be sure you include the rest2mobile Android SDK in your app by inserting this in your app's <code>build.gradle</code>.
```groovy
repositories {
    maven {
        url "http://repo.magnet.com:8081/artifactory/public/"
    }
    mavenLocal()
    mavenCentral()
}
dependencies {
    compile("com.magnet:r2m-sdk-android:1.1.0@aar") {
        transitive = true
    }
}
```
Go to the releases page and download the installer:

![rest2mobile plugin main menu](doc/img/R2M-download-release.jpg)
In Android Studio or IntelliJ, go to Preferences -> Plugins, click on install plugin from disk...

![rest2mobile plugin main menu](doc/img/R2M-install-plugin.jpg)

Choose the installer file location
![rest2mobile plugin main menu](doc/img/R2M-installer-file.jpg)

Click Ok in the Preferences window and restart your IDE
![rest2mobile plugin main menu](doc/img/R2M-restart-IDE.jpg)

Once your IDE has restarted, you should see a R2M menu
![rest2mobile plugin main menu](doc/img/R2M-menu.jpg)

Choose "Add new API", you can download an example from our repo:

![rest2mobile plugin download menu](doc/img/R2M-download.jpg)


### Feedback

We are constantly adding features and welcome feedback. 
Please, ask questions or file requests [here](https://github.com/magnetsystems/r2m-plugin-android/issues).

## License

Licensed under the **[Apache License, Version 2.0] [license]** (the "License");
you may not use this software except in compliance with the License.

## Copyright

Copyright © 2014 Magnet Systems, Inc. All rights reserved.

[website]: http://developer.magnet.com
[techdoc]: https://github.com/magnetsystems/rest2mobile/wiki
[r2m-plugin-android]:https://github.com/magnetsystems/r2m-plugin-android/
[r2m-plugin-ios]:https://github.com/magnetsystems/r2m-plugin-ios/
[r2m-cli]:https://github.com/magnetsystems/r2m-cli/
[license]: http://www.apache.org/licenses/LICENSE-2.0
[r2m wiki]:https://github.com/magnetsystems/r2m-cli/wiki

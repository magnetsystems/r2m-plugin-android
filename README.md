## rest2mobile plugin for Android Studio & IntelliJ IDEA

### Prerequisites
The rest2mobile plugin for Android Studio & IntelliJ IDEA has these prerequisites:

* Java JDK 1.6 or later
* Android Studio or IntelliJ IDEA
* Android minSDKVersion value of 16 or later

### Releases

To download the latest release, go to the [Releases](https://github.com/magnetsystems/r2m-plugin-android/releases) page.

### Setup
Once you've downloaded the plugin, follow these instructions to set up your IDE. You need to install the plugin and update your apps to use the rest2mobile SDK library.

**To install the plugin:**

1. Run Android Studio.
2. From the menu bar, select **Android Studio > Preferences**.
3. Under **IDE Settings**, click **Plugins** and then click **Install plugin from disk**.
4. Navigate to the folder where you downloaded the plugin and double-click the <code>.zip</code> file. **Magnet
 rest2mobile** appears in the list of plugins. Click **OK**.
5. Restart Android Studio.
 
To confirm that the plugin is installed, look for a new **Magnet** item in the menu bar. When you click on this, the **Add REST API** menu item appears.

**To update the build file for an app:**

1. Open the <code>app/build.gradle</code> file
2. To specify the library repository, add a <code>maven</code> paragraph to the "repositories" paragraph:
<pre>
repositories {
    maven {
        url "http://repo.magnet.com:8081/artifactory/public/"
    }
    mavenLocal()
    mavenCentral()
}</pre>
3. To specify the library as a dependency, update the "dependencies" paragraph:
<pre>
dependencies {
    compile("com.magnet:r2m-sdk-android:1.0.0@aar") {
        transitive = true
    }
}</pre>

### Rebuild the project

Once you update the build file, rebuild your project by selecting<br>
<b>Build > Rebuild Project</b> from the main menu.
For information on how to use the plugin, see the [Magnet rest2mobile wiki](https://github.com/magnetsystems/rest2mobile/wiki).

The following screenshot is an example of the plugin: 

![rest2mobile plugin for Android Studio](https://github.com/magnetsystems/rest2mobile/blob/master/docimg/r2m-android.jpg)



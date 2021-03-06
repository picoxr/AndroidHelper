# Android Helper

This project incorporates most of our Android repositories into one single package, and categorizes all interfaces into 5 classes:  ``DeviceHelper``, ``StorageHelper``, ``BlueToothHelper``, ``WifiHelper``, ``PowerManagerHelper``.

## Prerequirement
 Knowledge of [Unity Scripting API: AndroidJavaObject](https://docs.unity3d.com/ScriptReference/AndroidJavaObject.html)

## Usage
1. Clone this repo and make project in Android Studio, or download aar file in [release page](https://github.com/picoxr/AndroidHelper/releases).

2. Put ``AndroidHelper_Vx.x.x.aar`` into your Unity project's Assets/Plugins/Android directory;  

3. Refer to the sample code below to call the interface.

   ```c#
   //Initialize an AndroidJavaObject with the class name of the interface, 
   //e.g., DeviceHelper, StorageHelper, BlueToothHelper, WifiHelper, PowerManagerHelper.
   AndroidJavaObject  deviceHelper= new AndroidJavaObject("com.picovr.androidhelper.DeviceHelper");
   //Get current activity
   AndroidJavaObject  activityContext = new AndroidJavaClass("com.unity3d.player.UnityPlayer")
   .GetStatic<AndroidJavaObject>("currentActivity");
   //Must call "init" method before call other interface.
   deviceHelper.Call("init", activityContext );
   //Call the interface.
   string puiVersion = deviceHelper.Call<string>("getPUIVersion");
   ```

## System Signature

The interface marked with star "(*)" requires the system signature.

Refer to the following steps to sign you APK using Pico system signature:

1. Add sharedUserId in ``AndroidManifest.xml`` in Assets/Plugins/Android（If you are using Unity 2019.3.x and above，this directory should be [Unity_PATH]\Editor\Data\PlaybackEngines\AndroidPlayer\Apk\LauncherManifest.xml).

   ```xml
   <manifest ... android:sharedUserId="android.uid.system">
   ...
   </manifest>
   ```

2. Refer to [online instruction](http://static.appstore.picovr.com/docs/KioskMode/chapter_three.html) to sign your apk with system signature.

## Resolve Android support library conflict

The ``installApp`` API use ``AndroidX`` support library which may cause conflict if there is another support libraty in your unity project. To resolve this problem, follow steps below. Note that the ``installApp`` API won't work anymore after doing this.

1. Rename ``AndroidHelper_vx.x.x.aar`` to ``AndroidHelper_vx.x.x.zip``.
2. Unzip all files in ``AndroidHelper_vx.x.x.zip`` to ``AndroidHelper_vx.x.x\``.
3. Open ``AndroidHelper_vx.x.x\`` folder, delete the jar file ``core-1.3.0-rc01.jar`` in libs folder.
4. Open AndroidManifest.xml, delete ``<provider ...>...</provider>``.
3. Open CMD and run ``cd YOUR_JDK_FOLDER_PATH\bin`` to access JDK command.
4. Run ``jar cvf AndroidHelper_Modified.aar -C YOUR_FOLDER_PATH\AndroidHelper_vx.x.x .`` to generate a new AAR.
5. Place ``AndroidHelper_Modified.aar`` into your Unity project.


## Interfaces

**Note: The interface marked with star "(*)" requires the APK has the system signature**

### DeviceHelper  
- [getPUIVersion][getPUIVersion]: get PUI version of device.
- [getDeviceType][getDeviceType]: Get type of device.   
- [getSN][getSN]: Get serial number of device.        
- [silentInstall (*)][silentInstall]: Install the application without user interaction.   
- [silentUninstall (*)][silentUninstall]:  Uninstall the application without user interaction.       
- [killApp (*)][killApp]: Kill the application.     
- [launchBrowser][launchBrowser]: Call specified browser to open the link.    
- [launchBrowserWithLinkInFile][launchBrowserWithLinkInFile]: Call specified browser to open the link in the file.    
- [goToApp][goToApp]: Start an application.       
- [startVRShell][startVRShell]: Launch Android 2D application.        
- [getAppList][getAppList]: Get a name list of installed applications.         
- [registerHomeReceiver][registerHomeReceiver]: Register the receiver of Home event broadcast.        
- [unregisterHomeReceiver][unregisterHomeReceiver]: Unregister the receiver of Home event broadcast.     
- [openRecenterApp][openRecenterApp]: Adjust startup calibration application.       
- [installApp][installApp]: Install the application.    
- [setSystemProp][setSystemProp]: Set specified system property.   
- [getSystemProp][setSystemProp]: Get specific system property.    
### StorageHelper
- [getStorageFreeSize][getStorageFreeSize]: The remaining storage space inside the device.      
- [getStorageTotalSize][getStorageTotalSize]: Total storage space inside the device.         
- [updateFile][updateFile]: Update storaged file.   
- [getSDCardPath][getSDCardPath]: Get SD card path.   
### BlueToothHelper
- [registerBlueToothReceiver][registerBlueToothReceiver]: Register the receiver of bluetooth status broadcast.       
- [unregisterBlueToothReceiver][unregisterBlueToothReceiver]: Unregister the receiver of bluetooth status broadcast.   
- [getContentDevice][getContentDevice]: Get the name of connected bluetooth.     
- [getBlueToothMac (*)][getBlueToothMac]: Get the MAC address of connected bluetooth.      
### WifiHelper
- [registerWifiReceiver][registerWifiReceiver]: Register the receiver of Wi-Fi status broadcast.   
- [unregisterWifiReceiver][unregisterWifiReceiver]:  Unregister the receiver of Wi-Fi status broadcast.  
- [getConnectedWifiSSID][getConnectedWifiSSID]: Get the name of Wi-Fi connection.      
- [getWifiMac][getWifiMac]: Get the MAC address of connected Wi-Fi.   
- [getWifiIpAddress][getWifiIpAddress]: Get Wi-Fi IP address.     
- [connectWifi (*)][connectWifi]: Connect to Wi-Fi.    
- [connectWifiWithStaticIP][connectWifiWithStaticIP]: Set static IP.      
### PowerManagerHelper
- [androidLockScreen][androidLockScreen]: Lock the screen.    
- [androidUnLockScreen][androidUnLockScreen]: Unlock the screen.      
- [acquireWakeLock][acquireWakeLock]: Request a WakeLock.       
- [acquireWakeLock(timeout)][acquireWakeLock(timeout)]: Request a WakeLock, unlock automatically after timeout.          
- [releaseWakeLock][releaseWakeLock]: Deactivate WakeLock.       
- [setPropSleep][setPropSleep]: Sets System Sleep Timeout.           
- [setPropScreenOff][setPropScreenOff]: Sets Screen Off Timeout.      
- [androidShutDown (*)][androidShutDown]: Shutdown the system.      
- [androidReBoot (*)][androidReBoot]: Reboot the system.        

[getPUIVersion]: https://github.com/picoxr/AndroidHelper/wiki/DeviceHelper#string-getpuiversion
[getDeviceType]: https://github.com/picoxr/AndroidHelper/wiki/DeviceHelper#string-getdevicetype
[getSN]: https://github.com/picoxr/AndroidHelper/wiki/DeviceHelper#string-getsn
[silentInstall]: https://github.com/picoxr/AndroidHelper/wiki/DeviceHelper#void-silentinstallstring-apkpath-string-packagename
[silentUninstall]: https://github.com/picoxr/AndroidHelper/wiki/DeviceHelper#void-silentuninstallstring-packagename
[killApp]: https://github.com/picoxr/AndroidHelper/wiki/DeviceHelper#void-killappstring-packagename
[launchBrowser]: https://github.com/picoxr/AndroidHelper/wiki/DeviceHelper#void-launchbrowserint-browser-string-link
[launchBrowserWithLinkInFile]: https://github.com/picoxr/AndroidHelper/wiki/DeviceHelper#void-launchbrowserwithlinkinfileint-browser-string-filepath
[goToApp]: https://github.com/picoxr/AndroidHelper/wiki/DeviceHelper#void-gotoappstring-packagename
[startVRShell]: https://github.com/picoxr/AndroidHelper/wiki/DeviceHelper#void-startvrshellint-way-string-args
[getAppList]: https://github.com/picoxr/AndroidHelper/wiki/DeviceHelper#string-getapplist
[registerHomeReceiver]: https://github.com/picoxr/AndroidHelper/wiki/DeviceHelper#void-registerhomereceiver
[unregisterHomeReceiver]: https://github.com/picoxr/AndroidHelper/wiki/DeviceHelper#void-unregisterhomereceiver
[openRecenterApp]: https://github.com/picoxr/AndroidHelper/wiki/DeviceHelper#void-openrecenterapp
[installApp]: https://github.com/picoxr/AndroidHelper/wiki/DeviceHelper#void-openrecenterapp
[setSystemProp]: https://github.com/picoxr/AndroidHelper/wiki/DeviceHelper#boolean-setsystempropstring-key-string-value
[getSystemProp]: https://github.com/picoxr/AndroidHelper/wiki/DeviceHelper#string-getsystempropstring-key-string-defaultvalue
[getStorageFreeSize]: https://github.com/picoxr/AndroidHelper/wiki/StorageHelper#float-getstoragefreesize
[getStorageTotalSize]: https://github.com/picoxr/AndroidHelper/wiki/StorageHelper#float-getstoragetotalsize
[updateFile]: https://github.com/picoxr/AndroidHelper/wiki/StorageHelper#void-updatefilestring-filepath
[getSDCardPath]: https://github.com/picoxr/AndroidHelper/wiki/StorageHelper#string-getsdcardpath
[registerBlueToothReceiver]: https://github.com/picoxr/AndroidHelper/wiki/BlueToothHelper#void-registerbluetoothreceiver
[unregisterBlueToothReceiver]: https://github.com/picoxr/AndroidHelper/wiki/BlueToothHelper#void-unregisterbluetoothreceiver
[getContentDevice]: https://github.com/picoxr/AndroidHelper/wiki/BlueToothHelper#string-getcontentdevice
[getBlueToothMac]: https://github.com/picoxr/AndroidHelper/wiki/BlueToothHelper#string-getbluetoothmac
[registerWifiReceiver]: https://github.com/picoxr/AndroidHelper/wiki/WifiHelper#void-registerwifireceiver
[unregisterWifiReceiver]: https://github.com/picoxr/AndroidHelper/wiki/WifiHelper#void-unregisterwifireceiver
[getConnectedWifiSSID]: https://github.com/picoxr/AndroidHelper/wiki/WifiHelper#string-getconnectedwifissid
[getWifiMac]: https://github.com/picoxr/AndroidHelper/wiki/WifiHelper#string-getwifimac
[getWifiIpAddress]: https://github.com/picoxr/AndroidHelper/wiki/WifiHelper#string-getwifiipaddress
[connectWifi]: https://github.com/picoxr/AndroidHelper/wiki/WifiHelper#void-connectwifistring-ssidstring-password
[connectWifiWithStaticIP]: https://github.com/picoxr/AndroidHelper/wiki/WifiHelper#void-connectwifiwithstaticipstring-ssidstring-passwordstring-ipstring-gatewaystring-dns
[androidLockScreen]: https://github.com/picoxr/AndroidHelper/wiki/PowerManagerHelper#void-androidlockscreen
[androidUnlockScreen]: https://github.com/picoxr/AndroidHelper/wiki/PowerManagerHelper#void-androidunlockscreen
[acquireWakeLock]: https://github.com/picoxr/AndroidHelper/wiki/PowerManagerHelper#void-acquirewakelock
[acquireWakeLock(timeout)]: https://github.com/picoxr/AndroidHelper/wiki/PowerManagerHelper#void-acquirewakelocklong-timeout
[releaseWakeLock]: https://github.com/picoxr/AndroidHelper/wiki/PowerManagerHelper#void-releasewakelock
[setPropSleep]: https://github.com/picoxr/AndroidHelper/wiki/PowerManagerHelper#void-setpropsleepstring-time
[setPropScreenOff]: https://github.com/picoxr/AndroidHelper/wiki/PowerManagerHelper#void-setpropscreenoffstring-time
[androidShutDown]: https://github.com/picoxr/AndroidHelper/wiki/PowerManagerHelper#void-androidshutdown
[androidReBoot]: https://github.com/picoxr/AndroidHelper/wiki/PowerManagerHelper#void-androidreboot



package com.picovr.example.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.picovr.androidhelper.DeviceHelper;
import com.picovr.example.MethodAdapter;
import com.picovr.example.R;
import com.picovr.example.ResultUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class DeviceHelperActivity extends Activity {
    private static final String TAG = "DeviceHelperActivity";

    List methods = Arrays.asList("getPUIVersion",
            "getDeviceType",
            "getSN",
            "silentInstall",
            "silentUninstall",
            "killApp",
            "launchBrowser",
            "goToApp",
            "startVRShell",
            "getAppList",
            "registerHomeReceiver",
            "unregisterHomeReceiver",
            "openRecenterApp",
            "installApp",
            "setSystemProp",
            "getSystemProp");


    DeviceHelper deviceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_helper);

        deviceHelper = new DeviceHelper();
        deviceHelper.init(this);

        initView();
    }

    private void initView() {
        ArrayAdapter<String> arrayAdapter = new MethodAdapter(DeviceHelperActivity.this, android.R.layout.simple_list_item_1, methods);
        ListView listView = findViewById(R.id.lv_device_helper);
        listView.setAdapter(arrayAdapter);
    }

    private void getPUIVersion() {
        ResultUtil.showResult(this, deviceHelper.getPUIVersion());
    }

    private void getDeviceType() {
        ResultUtil.showResult(this, deviceHelper.getDeviceType());
    }

    private void getSN() {
        ResultUtil.showResult(this, deviceHelper.getSN());
    }

    private void silentInstall() {
        deviceHelper.silentInstall("sdcard/testapp.apk", "com.picovr.example");
    }

    private void silentUninstall() {
        deviceHelper.silentUninstall("com.picovr.testapp");
    }

    private void killApp() {
        deviceHelper.killApp(this, "com.picovr.testapp");
    }

    private void launchBrowser() {
        deviceHelper.launchBrowser(2, "https://www.baidu.com/");
    }

    private void goToApp() {
        deviceHelper.goToApp("com.picovr.testapp");
    }

    private void startVRShell() {
        deviceHelper.startVRShell(0, new String[]{"com.picovr.testapp"});
    }

    private void getAppList() {
        String s = deviceHelper.getAppList();
        ResultUtil.showResult(this, s);

        //Save json to file.
        File file = new File("/sdcard/json.txt");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(s.getBytes());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Decode json.
        JSONTokener jt = new JSONTokener(s);
        try {
            JSONArray jsonArray = ((JSONArray) jt.nextValue());
            for (int i = 0; i < jsonArray.length(); i++) {
                String pkgName = ((JSONObject) jsonArray.get(i)).get("mPackageName").toString();
                int flag = ((int) ((JSONObject) jsonArray.get(i)).get("mSystemFlag"));
                Log.e(TAG, "onCreate: " + flag + pkgName);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void registerHomeReceiver() {
        deviceHelper.registerHomeReceiver();
    }

    private void unregisterHomeReceiver() {
        deviceHelper.unregisterHomeReceiver();
    }

    private void openRecenterApp() {
        deviceHelper.openRecenterApp();
    }

    private void installApp() {
        deviceHelper.installApp("/sdcard/testapp.apk");
    }

    private void setSystemProp() {
        boolean b = deviceHelper.setSystemProp("persist.psensor.sleep.delay", "700");
        ResultUtil.showResult(this, "Set success? " + b);
    }

    private void getSystemProp() {
        String s = deviceHelper.getSystemProp("persist.psensor.sleep.delay", "-1");
        Log.e(TAG, "getSystemProp: " + s);
        ResultUtil.showResult(this, s);
    }
}
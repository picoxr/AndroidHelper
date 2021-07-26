package com.picovr.example.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.picovr.androidhelper.PowerManagerHelper;
import com.picovr.example.MethodAdapter;
import com.picovr.example.R;

import java.util.Arrays;
import java.util.List;

public class PowerManagerHelperActivity extends Activity {
    private static final String TAG = "PowerManagerHelperActiv";

    List<String> methods = Arrays.asList("androidLockScreen",
            "androidUnlockScreen",
            "acquireWakeLock",
            "acquireWakelockWithTimeout",
            "releaseWakeLock",
            "setPropSleep",
            "setPropScreenOff",
            "androidShutDown",
            "androidReBoot");

    PowerManagerHelper powerManagerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_manager_helper);

        powerManagerHelper = new PowerManagerHelper();
        powerManagerHelper.init(this);

        initView();
    }

    private void initView(){
        ListView listView = findViewById(R.id.lv_power_manager_helper);
        ArrayAdapter arrayAdapter = new MethodAdapter(this, android.R.layout.simple_list_item_1, methods);
        listView.setAdapter(arrayAdapter);
    }

    private void androidLockScreen() {
        powerManagerHelper.androidLockScreen();
    }

    private void androidUnlockScreen() {
        powerManagerHelper.androidUnlockScreen();
    }

    private void acquireWakelock() {
        powerManagerHelper.acquireWakeLock();
    }

    private void acquireWakelockWithTimeout() {
        powerManagerHelper.acquireWakeLock(5000);
    }

    private void releaseWakelock() {
        powerManagerHelper.releaseWakeLock();
    }

    private void setPropSleep() {
        powerManagerHelper.setPropSleep("60");
    }

    private void setPropScreenOff() {
        powerManagerHelper.setPropScreenOff("60");
    }

    private void androidShutDown() {
        powerManagerHelper.androidShutDown();
    }

    private void androidReBoot() {
        powerManagerHelper.androidReBoot();
    }
}
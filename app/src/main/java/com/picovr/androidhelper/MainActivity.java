package com.picovr.androidhelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.unity3d.player.UnityPlayerNativeActivityPico;

public class MainActivity extends UnityPlayerNativeActivityPico {
    private static final String TAG = "MainActivity";

    public static Activity currentActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        currentActivity = this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: ");
        if (requestCode == PowerManagerHelper.MY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Log.i(TAG, "onActivityResult.lockNow");
            //Delay lock of screen for 1 second, otherwise, the display will turn black (Screen on but display nothing)
            // after turning on screen manually.
            // Just a workaround.
            new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    PowerManagerHelper.mPolicyManager.lockNow();
                }
            }.start();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

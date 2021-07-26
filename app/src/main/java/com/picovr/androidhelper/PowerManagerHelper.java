package com.picovr.androidhelper;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

public class PowerManagerHelper extends AndroidHelper {
    private static final String TAG = "PowerManagerHelper";

    private static final String SLEEP_TIME = "setprop persist.psensor.sleep.delay ";
    private static final String LOCK_SCREEN = "setprop persist.psensor.screenoff.delay ";
    static final int MY_REQUEST_CODE = 9999;

    static DevicePolicyManager mPolicyManager;
    private ComponentName mComponentName;
    private PowerManager.WakeLock wakeLock;
    private PowerManager mPowerManager;

    private Context mContext;

    @Override
    public void init(Context context) {
        Log.d(TAG, "init: ");
        mContext = context;
        mPowerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mPolicyManager = (DevicePolicyManager) mContext.getSystemService(Context.DEVICE_POLICY_SERVICE);
        mComponentName = new ComponentName(mContext, AdminReceiver.class);
    }

    //System
    public void androidShutDown() {
        Log.d(TAG, "androidShutDown");
        if (Build.VERSION.SDK_INT < 24) {
            try {
                Method method = mPowerManager.getClass().getDeclaredMethod("shutdown", boolean.class, boolean.class);
                method.invoke(mPowerManager, false, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                Method method = mPowerManager.getClass().getDeclaredMethod("shutdown", boolean.class, String.class, boolean.class);
                method.invoke(mPowerManager, false, null, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //System
    public void androidReBoot() {
        Log.d(TAG, "androidReBoot");
        mPowerManager.reboot("");
    }


    public void androidLockScreen() {
        Log.d(TAG, "androidLockScreen");
        if (mPolicyManager.isAdminActive(mComponentName)) {
            Log.i(TAG, "lockNow");
            mPolicyManager.lockNow();
        } else {
            Log.i(TAG, "activeManage");
            //            (MainActivity)mContext.
            activeManager();
        }
    }

    private void activeManager() {
        Log.d(TAG, "activeManager()");
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Lock Screen");
        MainActivity.currentActivity.startActivityForResult(intent, MY_REQUEST_CODE);
    }

    public void androidUnlockScreen() {
        Log.d(TAG, "androidUnlockScreen");
        wakeLock = mPowerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag:");
        wakeLock.acquire();
        wakeLock.release();
    }

    public void acquireWakeLock() {
        Log.d(TAG, "acquireWakeLock: ");
        if (wakeLock == null) {
            wakeLock = mPowerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, this.getClass().getCanonicalName());
            wakeLock.setReferenceCounted(false);
            wakeLock.acquire();
            Log.i(TAG, "acquireWakeLock");
        }
    }

    public void acquireWakeLock(long timeout) {
        Log.d(TAG, "acquireWakeLock: ");
        if (wakeLock == null) {
            wakeLock = mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getCanonicalName());
            wakeLock.setReferenceCounted(false);
            wakeLock.acquire(timeout);
            Log.i(TAG, "acquireWakeLock(long timeout)");
        }
    }

    public void releaseWakeLock() {
        Log.d(TAG, "releaseWakeLock: ");
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
            wakeLock = null;
            Log.i(TAG, "releaseWakeLock");
        }
    }

    public void setPropSleep(String time) {
        Log.d(TAG, "setpropSleep:" + time);
        try {
            Log.e(TAG, "setpropSleep:" + SLEEP_TIME + time);
            execCommand(SLEEP_TIME + time);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPropScreenOff(String time) {
        Log.d(TAG, "setPropLockScreen:" + time);
        try {
            Log.e(TAG, "setPropLockScreen:" + LOCK_SCREEN + time);
            execCommand(LOCK_SCREEN + time);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void execCommand(String command) throws IOException {
        Log.d(TAG, "execCommand: command: " + command);
        Runtime runtime = Runtime.getRuntime();
        Process proc = runtime.exec(command);
        InputStream inputstream = proc.getInputStream();
        InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
        BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
        String line = "";
        StringBuilder sb = new StringBuilder(line);
        while ((line = bufferedreader.readLine()) != null) {
            sb.append(line);
            sb.append("/n");
        }
        Log.e(TAG, sb.toString());

        try {
            if (proc.waitFor() != 0) {
                System.err.println("exit value = " + proc.exitValue());
            }
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }

}

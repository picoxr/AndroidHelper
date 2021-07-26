package com.picovr.androidhelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.KeyEvent;

import com.unity3d.player.UnityPlayer;

public class HomeKeyReceiverClass {
    private static final String TAG = "HomeKeyReceiver";

    private static final String OBJECT_NAME = "AndroidHelper";
    private static final String METHOD_NAME = "HomeKeyReceiverMethod";

    private static final String SINGLE_TAP = "single";
    private static final String DOUBLE_TAP = "double";
    private static final String LONG_PRESS = "long";

    private static BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: " );
            if (intent.getIntExtra("keycode", 0) == KeyEvent.KEYCODE_HOME) {
                switch (intent.getIntExtra("action", 3)) {
                    case 0:
                        Log.d(TAG, "onReceive: " + "single tap");
                        UnityPlayer.UnitySendMessage(OBJECT_NAME, METHOD_NAME, SINGLE_TAP);
                        break;
                    case 1:
                        Log.d(TAG, "onReceive: " + "double tap");
                        UnityPlayer.UnitySendMessage(OBJECT_NAME, METHOD_NAME, DOUBLE_TAP);
                        break;
                    case 2:
                        Log.d(TAG, "onReceive: " + "long press");
                        UnityPlayer.UnitySendMessage(OBJECT_NAME, METHOD_NAME, LONG_PRESS);
                        break;
                    default:
                        break;
                }
            }
        }
    };

    public static void registerHomeReceiver(Context context) {
        Log.d(TAG, "registerHomeReceiver: " );
        IntentFilter filter = new IntentFilter("android.intent.keybroadcast");
        context.registerReceiver(broadcastReceiver, filter);
    }

    public static void unregisterHomeReceiver(Context context) {
        Log.d(TAG, "unregisterHomeReceiver: " );
        if (broadcastReceiver != null) {
            context.unregisterReceiver(broadcastReceiver);
        }
    }
}

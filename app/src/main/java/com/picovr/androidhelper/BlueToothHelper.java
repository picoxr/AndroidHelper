package com.picovr.androidhelper;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.unity3d.player.UnityPlayer;

import java.lang.reflect.Method;
import java.util.Set;

public class BlueToothHelper extends AndroidHelper{

    private static final String TAG = "BlueToothHelper";
    private static final String OBJECT_NAME = "AndroidHelper";

    private static final String MN_SET_BT_Status = "GetBluetoothStatus";
    private static final String MN_SET_BT_CONNECTION_STATUS = "GetBluetoothConnectionStatus";

    private Context mContext;
    @Override
    public void init(Context context) {
        Log.d(TAG, "init: ");
        mContext = context;
    }

    private BroadcastReceiver mBluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: blue " + intent.getAction());
            if (intent.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                Bundle stateBundle = intent.getExtras();
                int message = stateBundle.getInt(BluetoothAdapter.EXTRA_STATE);
                //bluetooth switch value：on oning off offing
                Log.d(TAG, "ACTION_STATE_CHANGED：" + message);
                switch (message) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceive: " + MN_SET_BT_Status + ": " + "bt_status_off");
                        UnityPlayer.UnitySendMessage(OBJECT_NAME, MN_SET_BT_Status, "bt_status_off");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "onReceive: " + MN_SET_BT_Status + ": " + "bt_status_turning_on");
                        UnityPlayer.UnitySendMessage(OBJECT_NAME, MN_SET_BT_Status, "bt_status_turning_on");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "onReceive: " + MN_SET_BT_Status + ": " + "bt_status_on");
                        UnityPlayer.UnitySendMessage(OBJECT_NAME, MN_SET_BT_Status, "bt_status_on");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "onReceive: " + MN_SET_BT_Status + ": " + "bt_status_turning_off");
                        UnityPlayer.UnitySendMessage(OBJECT_NAME, MN_SET_BT_Status, "bt_status_turning_off");
                        break;
                    default:
                        break;
                }
            } else if (intent.getAction().equals(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE,
                        BluetoothProfile.STATE_DISCONNECTED);
                //bluetooth connection value
                Log.d(TAG, "ACTION_CONNECTION_STATE_CHANGED：" + state);
                switch (state) {
                    case 2:
                        Log.d(TAG, "onReceive: " + MN_SET_BT_CONNECTION_STATUS + ": " + "bt_connection_status_connected");
                        UnityPlayer.UnitySendMessage(OBJECT_NAME, MN_SET_BT_CONNECTION_STATUS, "bt_connection_status_connected");
                        break;
                    case 1:
                        Log.d(TAG, "onReceive: " + MN_SET_BT_CONNECTION_STATUS + ": " + "bt_connection_status_connecting");
                        UnityPlayer.UnitySendMessage(OBJECT_NAME, MN_SET_BT_CONNECTION_STATUS, "bt_connection_status_connecting");
                        break;
                    case 0:
                        Log.d(TAG, "onReceive: " + MN_SET_BT_CONNECTION_STATUS + ": " + "bt_connection_status_disconnected");
                        UnityPlayer.UnitySendMessage(OBJECT_NAME, MN_SET_BT_CONNECTION_STATUS, "bt_connection_status_disconnected");
                        break;
                    case 3:
                        Log.d(TAG, "onReceive: " + MN_SET_BT_CONNECTION_STATUS + ": " + "bt_connection_status_disconnecting");
                        UnityPlayer.UnitySendMessage(OBJECT_NAME, MN_SET_BT_CONNECTION_STATUS, "bt_connection_status_disconnecting");
                        break;
                    default:
                        break;
                }
            }
        }
    };

    public void registerBlueToothReceiver() {
        Log.d(TAG, "registerBluetoothReceiver: ");
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        mContext.registerReceiver(mBluetoothReceiver, filter);
    }

    public void unregisterBlueToothReceiver() {
        Log.d(TAG, "unregisterBluetootReceiver: ");
        if (mBluetoothReceiver != null) {
            mContext.unregisterReceiver(mBluetoothReceiver);
        }
    }

    public String getContentDevice() {
        Log.d(TAG, "getContentDevice: ");
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Class<BluetoothAdapter> bluetoothAdapterClass = BluetoothAdapter.class;
        try {
            Method method = bluetoothAdapterClass.getDeclaredMethod("getConnectionState", (Class[]) null);
            method.setAccessible(true);
            int state = (int) method.invoke(bluetoothAdapter, (Object[]) null);
            if (state == BluetoothAdapter.STATE_CONNECTED) {
                Log.i(TAG, "BluetoothAdapter.STATE_CONNECTED");
                Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
                Log.i(TAG, "devices:" + devices.size());
                for (BluetoothDevice device : devices) {
                    Method isConnectedMethod = BluetoothDevice.class.getDeclaredMethod("isConnected", (Class[]) null);
                    method.setAccessible(true);
                    boolean isConnected = (boolean) isConnectedMethod.invoke(device, (Object[]) null);
                    if (isConnected) {
                        Log.i(TAG, "connected:" + device.getAddress() + device.getName());
                        return device.getName();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Require system signature.
     * Without system signature, this method will always return "02:00:00:00:00:00" on the platform that Android API level above 23.
     * */
    public String getBlueToothMac() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String macAddress = bluetoothAdapter.getAddress();
        Log.d(TAG, "bluetoothAdapter.getAddress(); : " + macAddress);
        return macAddress;
    }



}

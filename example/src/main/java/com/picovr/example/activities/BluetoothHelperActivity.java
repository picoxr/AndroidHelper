package com.picovr.example.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.picovr.androidhelper.BlueToothHelper;
import com.picovr.example.MethodAdapter;
import com.picovr.example.R;

import java.util.Arrays;
import java.util.List;

public class BluetoothHelperActivity extends Activity {
    private static final String TAG = "BluetoothHelperActivity";

    List<String> methods = Arrays.asList("registerBluetoothReceiver", "unregisterBluetoothReceiver", "getContentDevices", "getBluetoothMac");

    BlueToothHelper blueToothHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_helper);

        blueToothHelper = new BlueToothHelper();
        blueToothHelper.init(this);

        initView();
    }

    private void initView(){
        ListView listView = findViewById(R.id.lv_bluetooth_helper);
        ArrayAdapter arrayAdapter = new MethodAdapter(this, android.R.layout.simple_list_item_1, methods);
        listView.setAdapter(arrayAdapter);
    }

    private void registerBluetoothReceiver(){
        blueToothHelper.registerBlueToothReceiver();
    }

    private void unregisterBluetoothReceiver(){
        blueToothHelper.unregisterBlueToothReceiver();
    }

    private void getContentDevices(){
        String s = blueToothHelper.getContentDevice();
    }

    private void getBluetoothMac(){
        String s = blueToothHelper.getBlueToothMac();
    }
}
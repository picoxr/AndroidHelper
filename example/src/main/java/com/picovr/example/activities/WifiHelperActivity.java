package com.picovr.example.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.picovr.androidhelper.WifiHelper;
import com.picovr.example.MethodAdapter;
import com.picovr.example.R;
import com.picovr.example.ResultUtil;

import java.util.Arrays;
import java.util.List;

public class WifiHelperActivity extends Activity {
    private static final String TAG = "WifiHelperActivity";

    List<String> methods = Arrays.asList("registerWifiReceiver", "unregisterWifiReceiver", "getConnectWifiSSID", "getWifiMac", "getWifiIpAddress", "connectWifi", "connectWifiWithStaticIP");

    WifiHelper wifiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_helper);

        wifiHelper = new WifiHelper();
        wifiHelper.init(this);

        initView();
    }

    private void initView() {
        ListView listView = findViewById(R.id.lv_wifi_helper);
        ArrayAdapter arrayAdapter = new MethodAdapter(this, android.R.layout.simple_list_item_1, methods);
        listView.setAdapter(arrayAdapter);
    }

    private void registerWifiReceiver() {
        wifiHelper.registerWifiReceiver();
    }

    private void unregisterWifiReceiver() {
        wifiHelper.unregisterWifiReceiver();
    }

    private void getConnectWifiSSID() {
        String s = wifiHelper.getConnectedWifiSSID();
        ResultUtil.showResult(this, s);
    }

    private void getWifiMac() {
        String s = wifiHelper.getWifiMac();
    }

    private void getWifiIpAddress() {
        String s = wifiHelper.getWifiIpAddress();
    }

    private void connectWifi() {
        wifiHelper.connectWifi("support_vpn", "Support@2025");
    }

    private void connectWifiWithStaticIP() {
//        wifiHelper.connectWifiWithStaticIP();
    }
}
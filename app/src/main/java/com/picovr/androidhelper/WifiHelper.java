package com.picovr.androidhelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;

import com.unity3d.player.UnityPlayer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.WIFI_SERVICE;

public class WifiHelper extends AndroidHelper {
    private static final String TAG = "WifiHelper";

    private static final String OBJECT_NAME = "AndroidHelper";
    private static final String MN_SET_WF_Status = "GetWifiStatus";
    private static final String MN_SET_WF_CONNECTION_STATUS = "GetWifiConnectionStatus";
    private static final String MN_SET_RSSI_STATUS = "GetWifiRssiStatus";

    private Context mContext;

    @Override
    public void init(Context context) {
        Log.d(TAG, "init: ");
        mContext = context;
    }

    public void registerWifiReceiver() {
        Log.d(TAG, "registerWifiReceiver: ");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(mWifiReceiver, intentFilter);
    }

    public void unregisterWifiReceiver() {
        Log.d(TAG, "unregisterWifiReceiver: ");
        if (mWifiReceiver != null) {
            mContext.unregisterReceiver(mWifiReceiver);
        }
    }

    public String getConnectedWifiSSID() {
        Log.d(TAG, "getConnectedWifiSSID: ");
        WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(WIFI_SERVICE);
        String ssid = wifiManager.getConnectionInfo().getSSID();
        Log.d(TAG, "getConnectedWifiSSID: ssid: " + ssid);
        return ssid;
    }

    public String getWifiMac() {
        Log.d(TAG, "getWifiMac: ");
        WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(WIFI_SERVICE);
        return wifiManager.getConnectionInfo().getMacAddress();
    }

    public String getWifiIpAddress() {
        Log.d(TAG, "androidGetIpAddress");
        WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(WIFI_SERVICE);
        DhcpInfo wifiInfo = wifiManager.getDhcpInfo();
        Log.d(TAG, "ipAddress = " + intToIp(wifiInfo.ipAddress));
        return intToIp(wifiInfo.ipAddress);

    }

    public String getWifiGateWay() {
        Log.d(TAG, "androidGetGateWay");
        WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(WIFI_SERVICE);
        DhcpInfo wifiInfo = wifiManager.getDhcpInfo();
        Log.d(TAG, "gateway = " + intToIp(wifiInfo.gateway));
        return intToIp(wifiInfo.gateway);

    }

    public String getWifiDNS() {
        Log.d(TAG, "androidGetDNS");
        WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(WIFI_SERVICE);
        DhcpInfo wifiInfo = wifiManager.getDhcpInfo();
        Log.d(TAG, "dns = " + intToIp(wifiInfo.dns1));
        return intToIp(wifiInfo.dns1);
    }

    public void connectWifi(String SSID, String PASSWORD) {
        Log.d(TAG, "connectWifi");
        Log.d(TAG, "SSID = " + SSID);
        Log.d(TAG, "PASSWORD = " + PASSWORD);
        WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiConfiguration wifiConfig = CreateWifiInfo(wifiManager, SSID, PASSWORD, 3);
        int wcgID = wifiManager.addNetwork(wifiConfig);
        boolean b = wifiManager.enableNetwork(wcgID, true);
        Log.d(TAG, "wcgID = " + wcgID + ", b = " + b);
    }

    public WifiConfiguration CreateWifiInfo(WifiManager wifiManager, String SSID, String Password, int Type) {
        Log.d(TAG, "CreateWifiInfo , SSID = " + SSID + ", Password = " + Password + ", Type = " + Type);
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";

        WifiConfiguration tempConfig = this.IsExsits(wifiManager, SSID);
        if (tempConfig != null) {
            wifiManager.removeNetwork(tempConfig.networkId);
        }

        if (Type == 1) // WIFICIPHER_NOPASS
        {
            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (Type == 2) // WIFICIPHER_WEP
        {
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + Password + "\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (Type == 3) // WIFICIPHER_WPA
        {
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }
        return config;
    }

    public void connectWifiWithStaticIP(final String SSID, String PASSWORD, final String ip, final String gateway,
                             final String dns) {
        WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(WIFI_SERVICE);
        Log.d(TAG, "connectWifiWithStaticIP");
        WifiConfiguration tempConfig = IsExsits(wifiManager, SSID);

        if (tempConfig == null) {
            final WifiConfiguration wifiConfig = CreateWifiInfo(wifiManager, SSID, PASSWORD, 3);
            int wcgID = wifiManager.addNetwork(wifiConfig);
            boolean b = wifiManager.enableNetwork(wcgID, true);
            Log.d(TAG, "wcgID = " + wcgID + ", b = " + b);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setStaticIPConfig(wifiConfig, ip, gateway, dns);
                }
            }, 5000);
        } else {
            setStaticIPConfig(tempConfig, ip, gateway, dns);
        }

    }

    //system
    private void setStaticIPConfig(WifiConfiguration wifiConfig, String ip, String gateway, String dns) {
        Log.e(TAG, "setStaticIPConfig");
        WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(WIFI_SERVICE);
        boolean b = wifiManager.enableNetwork(wifiConfig.networkId, true);
        Log.d(TAG, "tempConfig.networkId = " + wifiConfig.networkId + ", b = " + b);
        try {
            setStaticIpConfiguration(wifiManager, wifiConfig, InetAddress.getByName(ip), 24,
                    InetAddress.getByName(gateway), InetAddress.getAllByName(dns));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (wifiManager.disconnect()) {
            boolean c = wifiManager.reconnect();
            Log.d(TAG, " c = " + c);
        }
    }

    @SuppressWarnings("unchecked")
    public static void setStaticIpConfiguration(WifiManager manager, WifiConfiguration config, InetAddress ipAddress,
                                                int prefixLength, InetAddress gateway, InetAddress[] dns)
            throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, NoSuchFieldException, InstantiationException {

        Log.d(TAG, "setStaticIpConfiguration, manager = " + manager.toString() + ", config = " + config
                + ", ipAddress = " + ipAddress + ", prefixLength" + prefixLength + ", gateway = " + gateway);
        // First set up IpAssignment to STATIC.
        Object ipAssignment = getEnumValue("android.net.IpConfiguration$IpAssignment", "STATIC");
        callMethod(config, "setIpAssignment", new String[]{"android.net.IpConfiguration$IpAssignment"},
                new Object[]{ipAssignment});

        // Then set properties in StaticIpConfiguration.
        Object staticIpConfig = newInstance("android.net.StaticIpConfiguration");

        Object linkAddress = newInstance("android.net.LinkAddress", new Class[]{InetAddress.class, int.class},
                new Object[]{ipAddress, prefixLength});
        setField(staticIpConfig, "ipAddress", linkAddress);
        setField(staticIpConfig, "gateway", gateway);
        ArrayList<Object> aa = (ArrayList<Object>) getField(staticIpConfig, "dnsServers");
        aa.clear();
        for (int i = 0; i < dns.length; i++)
            aa.add(dns[i]);
        callMethod(config, "setStaticIpConfiguration", new String[]{"android.net.StaticIpConfiguration"},
                new Object[]{staticIpConfig});
        Log.d(TAG, "conconconm" + config);
        int updateNetwork = manager.updateNetwork(config);
        boolean saveConfiguration = manager.saveConfiguration();
        Log.d(TAG, "updateNetwork" + updateNetwork + saveConfiguration);
    }

    private static void setField(Object object, String fieldName, Object value)
            throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException {
        Log.d(TAG, "setField");
        Field field = object.getClass().getDeclaredField(fieldName);
        field.set(object, value);
    }

    private static Object getField(Object object, String fieldName)
            throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException {
        Log.d(TAG, "getField");
        Field field = object.getClass().getDeclaredField(fieldName);
        Object out = field.get(object);
        return out;
    }

    private static Object newInstance(String className) throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {

        Log.d(TAG, "newInstance, className = " + className);
        return newInstance(className, new Class[0], new Object[0]);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static Object newInstance(String className, Class[] parameterClasses, Object[] parameterValues)
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, ClassNotFoundException {
        Log.d(TAG, "newInstance");
        Class clz = Class.forName(className);
        Constructor constructor = clz.getConstructor(parameterClasses);
        return constructor.newInstance(parameterValues);
    }

    @SuppressWarnings("rawtypes")
    private static void callMethod(Object object, String methodName, String[] parameterTypes, Object[] parameterValues)
            throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException {
        Log.d(TAG, "callMethod, object = " + object.toString() + ", methodName = " + methodName);
        Class[] parameterClasses = new Class[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++)
            parameterClasses[i] = Class.forName(parameterTypes[i]);

        Method method = object.getClass().getDeclaredMethod(methodName, parameterClasses);
        method.invoke(object, parameterValues);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static Object getEnumValue(String enumClassName, String enumValue) throws ClassNotFoundException {
        Log.d(TAG, "getEnumValue");
        Log.d(TAG, "enumClassName = " + enumClassName + " ,enumValue = " + enumValue);
        Class enumClz = (Class) Class.forName(enumClassName);
        return Enum.valueOf(enumClz, enumValue);
    }

    /**
     * wifi receiver
     */
    private BroadcastReceiver mWifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "mWifiReceiver onReceive: ");
            if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                String networkState = info.isConnected() ? "1" : "0";
                Log.e(TAG, "NETWORK_STATE_CHANGED_ACTION：" + networkState);
                if (networkState.equals("1")) {
                    Log.e(TAG, "onReceive: " + MN_SET_WF_Status + ": " + "wf_status_connected");
                    UnityPlayer.UnitySendMessage(OBJECT_NAME, MN_SET_WF_Status, "wf_status_connected");
                } else if (networkState.equals("0")) {
                    Log.e(TAG, "onReceive: " + MN_SET_WF_Status + ": " + "wf_status_disconnected");
                    UnityPlayer.UnitySendMessage(OBJECT_NAME, MN_SET_WF_Status, "wf_status_disconnected");
                }
            }
            if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                        WifiManager.WIFI_STATE_UNKNOWN);
                Log.e(TAG, "WIFI_STATE_CHANGED_ACTION：" + state);
                switch (state) {
                    case WifiManager.WIFI_STATE_ENABLING:
                        Log.e(TAG, "onReceive: " + MN_SET_WF_CONNECTION_STATUS + ": " + "wf_connection_status_enabling");
                        UnityPlayer.UnitySendMessage(OBJECT_NAME, MN_SET_WF_CONNECTION_STATUS, "wf_connection_status_enabling");
                        break;
                    case WifiManager.WIFI_STATE_ENABLED:
                        Log.e(TAG, "onReceive: " + MN_SET_WF_CONNECTION_STATUS + ": " + "wf_connection_status_enabled");
                        UnityPlayer.UnitySendMessage(OBJECT_NAME, MN_SET_WF_CONNECTION_STATUS, "wf_connection_status_enabled");
                        break;
                    case WifiManager.WIFI_STATE_DISABLED:
                        Log.e(TAG, "onReceive: " + MN_SET_WF_CONNECTION_STATUS + ": " + "wf_connection_status_disabled");
                        UnityPlayer.UnitySendMessage(OBJECT_NAME, MN_SET_WF_CONNECTION_STATUS, "wf_connection_status_disabled");
                        break;
                    case WifiManager.WIFI_STATE_DISABLING:
                        Log.e(TAG, "onReceive: " + MN_SET_WF_CONNECTION_STATUS + ": " + "wf_connection_status_disabling");
                        UnityPlayer.UnitySendMessage(OBJECT_NAME, MN_SET_WF_CONNECTION_STATUS, "wf_connection_status_disabling");
                        break;
                    case WifiManager.WIFI_STATE_UNKNOWN:
                        Log.e(TAG, "onReceive: " + MN_SET_WF_CONNECTION_STATUS + ": " + "wf_connection_status_unknown");
                        UnityPlayer.UnitySendMessage(OBJECT_NAME, MN_SET_WF_CONNECTION_STATUS, "wf_connection_status_unknown");
                        break;
                    default:
                        break;
                }
            }
            if (intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)) {
                WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 4);
                Log.e(TAG, MN_SET_RSSI_STATUS + ": " + level);
                UnityPlayer.UnitySendMessage(OBJECT_NAME, MN_SET_RSSI_STATUS, String.valueOf(level));
            }
        }
    };

    private String intToIp(int i) {
        Log.d(TAG, "intToIp: " + i);
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
    }

    private WifiConfiguration IsExsits(WifiManager wifiManager, String SSID) {
        Log.d(TAG, "IsExsits SSID = " + SSID);
        if (mContext.checkSelfPermission("android.permission.ACCESS_FINE_LOCATION") == PackageManager.PERMISSION_GRANTED) {
            List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();
            for (WifiConfiguration existingConfig : existingConfigs) {
                if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                    Log.d(TAG, "existingConfig = " + existingConfig.SSID);
                    return existingConfig;
                }
            }
        }
        return null;
    }

}

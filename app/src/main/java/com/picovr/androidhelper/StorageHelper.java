package com.picovr.androidhelper;

import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.storage.StorageManager;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class StorageHelper extends AndroidHelper {
    private static final String TAG = "StorageHelper";

    private Context mContext;

    @Override
    public void init(Context context) {
        Log.d(TAG, "init: ");
        mContext = context;
    }

    public float getStorageFreeSize() {
        Log.d(TAG, "getStorageFreeSize: ");
        float free = 0f;
        try {
            StorageStatsManager manager = mContext.getSystemService(StorageStatsManager.class);
            free = manager.getFreeBytes(StorageManager.UUID_DEFAULT) / (1000 * 1000 * 1000f);
            float total = manager.getTotalBytes(StorageManager.UUID_DEFAULT) / (1000 * 1000 * 1000f);
            Log.d(TAG, "free : " + free + "  total : " + total + " used " + (total - free));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return free;
    }

    public float getStorageTotalSize() {
        float free, total = 0f;
        try {
            StorageStatsManager manager = mContext.getSystemService(StorageStatsManager.class);
            free = manager.getFreeBytes(StorageManager.UUID_DEFAULT) / (1000 * 1000 * 1000f);
            total = manager.getTotalBytes(StorageManager.UUID_DEFAULT) / (1000 * 1000 * 1000f);
            Log.d(TAG, "free : " + free + "  total : " + total + " used " + (total - free));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    public void updateFile(String filePath) {
        Log.d(TAG, "updateFileStatus: " + "filePath: " + filePath);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(new File(filePath)));
        mContext.sendBroadcast(intent);
    }

    public String getSDCardPath() {
        Log.d(TAG, "getSDCardPath: ");
        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            Log.i(TAG, "getEXStoragePath : " + length);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                Log.i(TAG, i + "path : " + path);
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                if (removable) {
                    return path;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}

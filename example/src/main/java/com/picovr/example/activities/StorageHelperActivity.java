package com.picovr.example.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.picovr.androidhelper.StorageHelper;
import com.picovr.example.MethodAdapter;
import com.picovr.example.R;
import com.picovr.example.ResultUtil;

import java.util.Arrays;
import java.util.List;

public class StorageHelperActivity extends Activity {
    private static final String TAG = "StorageHelperActivity";

    List<String> methods = Arrays.asList("getStorageFreeSize","getStorageTotalSize","updateFile","getSDCardPath");

    StorageHelper storageHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_helper);

        storageHelper = new StorageHelper();
        storageHelper.init(this);

        initView();
    }

    private void initView(){
        ListView listView = findViewById(R.id.lv_storage_helper);
        ArrayAdapter arrayAdapter = new MethodAdapter(this, android.R.layout.simple_list_item_1, methods);
        listView.setAdapter(arrayAdapter);
    }

    private void getStorageFreeSize(){
        float size = storageHelper.getStorageFreeSize();
        ResultUtil.showResult(this, String.valueOf(size));
    }

    private void getStorageTotalSize(){
        float size = storageHelper.getStorageTotalSize();
        ResultUtil.showResult(this, String.valueOf(size));
    }

    private void updateFile(){
        storageHelper.updateFile("/sdcard/test.txt");
    }

    private void getSDCardPath(){
        String path = storageHelper.getSDCardPath();
        ResultUtil.showResult(this, String.valueOf(path));
    }
}
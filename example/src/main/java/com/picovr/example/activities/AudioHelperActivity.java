package com.picovr.example.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.picovr.androidhelper.AudioHelper;
import com.picovr.example.MethodAdapter;
import com.picovr.example.R;

import java.util.Arrays;
import java.util.List;

public class AudioHelperActivity extends Activity {
    private static final String TAG = "AudioHelperActivity";

    List<String> methods = Arrays.asList("startRecordByMIC", "stopRecordByMIC");

    AudioHelper audioHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_helper);

        audioHelper = new AudioHelper();
        audioHelper.init(this);

        initView();
    }

    private void initView() {
        ListView listView = findViewById(R.id.lv_audio_helper);
        ArrayAdapter arrayAdapter = new MethodAdapter(this, android.R.layout.simple_list_item_1, methods);
        listView.setAdapter(arrayAdapter);
    }

    private void startRecordByMIC() {
        audioHelper.startRecordByMIC();
    }

    private void stopRecordByMIC() {
        audioHelper.stopRecordByMIC();
    }
}
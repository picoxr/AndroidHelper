package com.picovr.androidhelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class AudioHelper extends AndroidHelper {
    private static final String TAG = "AudioHelper";
    MediaRecorder recorder;

    public void startRecordByMIC() {
        initMediaRecorder();
        try {
            recorder.prepare();
            recorder.start();
            Log.d(TAG, "startRecordByMIC: ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initMediaRecorder() {
        Log.d(TAG, "initMediaRecorder: ");
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        String timeStr = format.format(new Date());
        String fileName = timeStr + ".aac";
        File destDir = new File(Environment.getExternalStorageDirectory() + "/Record/");
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        String filePath = Environment.getExternalStorageDirectory() + "/Record/" + fileName;
        recorder.setOutputFile(filePath);
        recorder.setAudioChannels(1);
        recorder.setAudioSamplingRate(44100);
        recorder.setAudioEncodingBitRate(192000);
    }

    public void stopRecordByMIC() {
        if (recorder != null) {
            try {
                recorder.stop();
            } catch (IllegalStateException e) {
                recorder = null;
                recorder = new MediaRecorder();
            }
            recorder.release();
            recorder = null;
            Log.d(TAG, "stopRecordByMIC: ");
        }
    }

    @Override
    public void init(Context context) {

    }
}

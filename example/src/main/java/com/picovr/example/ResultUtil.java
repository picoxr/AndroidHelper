package com.picovr.example;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ResultUtil{
    public static void showResult(Context context, String msg){
        new AlertDialog.Builder(context).setMessage(msg).setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }
}

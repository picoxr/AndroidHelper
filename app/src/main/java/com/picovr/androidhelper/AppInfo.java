package com.picovr.androidhelper;

import android.graphics.Bitmap;

public class AppInfo {


    private String mPackageName;
    private String mActivityName;
    private String mAppName;
    private long mInstallTime;
    private int mSystemFlag;// whether its system app 0 not 1 yes
    private Bitmap mIcon;

    public String getPackageName() {
        return mPackageName;
    }

    public void setPackageName(String mPackageName) {
        this.mPackageName = mPackageName;
    }

    public String getActivityName() {
        return mActivityName;
    }

    public void setActivityName(String mActivityName) {
        this.mActivityName = mActivityName;
    }

    public String getAppName() {
        return mAppName;
    }

    public void setAppName(String mAppName) {
        this.mAppName = mAppName;
    }

    public long getInstallTime() {
        return mInstallTime;
    }

    public void setInstallTime(long mInstallTime) {
        this.mInstallTime = mInstallTime;
    }

    public int getSystemFlag() {
        return mSystemFlag;
    }

    public void setSystemFlag(int mSystemFlag) {
        this.mSystemFlag = mSystemFlag;
    }

    public Bitmap getIcon() {
        return mIcon;
    }

    public void setIcon(Bitmap icon) {
        this.mIcon = icon;
    }

    @Override
    public String toString() {
        return "{"
                + "mPackageName:"  + mPackageName
                + ", mActivityName:" + mActivityName
                + ", mAppName:" + mAppName
                + ", mInstallTime:" + mInstallTime
                + ", mSystemFlag:" + mSystemFlag
                + "}";
    }
}

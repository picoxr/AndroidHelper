package com.picovr.androidhelper;

import android.os.Build;
import android.util.Log;

public class SilentInstaller {
	private static final String TAG = "SilentInstaller";

	public static void install(String apkPath, String installerPkgName, ShellCmd.ICmdResultCallback callback) {
		Log.d(TAG, "install: " + "apkPath: " + apkPath + ", installPkgName: " + installerPkgName);
		boolean below7 = Build.VERSION.SDK_INT < 24;
		String[] cmds = null;
		if (below7) {
			cmds = new String[] { "pm", "install", "-r", apkPath };
		} else {
			String[] split = ("pm install -r -i " + installerPkgName + " --user 0 ").split(" ");
			int l = split.length;
			cmds = new String[split.length + 1];
			System.arraycopy(split, 0, cmds, 0, split.length);
			cmds[l] = apkPath;
		}
		ShellCmd.execute(cmds, callback);
	}
}

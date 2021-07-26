package com.picovr.androidhelper;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

class ShellCmd {
	static void execute(String[] cmds, ICmdResultCallback callback) {
		List cmdArgs = Arrays.asList(cmds);
		execute(cmdArgs, callback);
	}

	static void execute(String cmdLine, ICmdResultCallback callback) {
		List cmds = buildArgs(cmdLine);
		execute(cmds, callback);
	}

	static void execute(List<String> cmdArgs, ICmdResultCallback callback) {
		ProcessBuilder builder = new ProcessBuilder(cmdArgs);
		try {
			Process e = builder.start();
			handleProcess(e, callback);
		} catch (IOException e) {
			e.printStackTrace();
			callback.onException(e);
		}
	}

	private static List<String> buildArgs(String cmdLine) {
		String[] args = cmdLine.split(" ");
		return Arrays.asList(args);
	}

	private static void handleProcess(Process process, ICmdResultCallback callback) {
		if (process == null) {
			callback.onError("Process start failed.");
		} else {
			InputStream errorStream = process.getErrorStream();
			InputStream inputStream = process.getInputStream();
			String errorMsg = getErrorMsg(errorStream);
			String normalMsg = getNormalMsg(inputStream);

			if (errorMsg.equals("")){
				callback.onComplete(normalMsg);
			} else {
				callback.onError(errorMsg);
			}
			process.destroy();
		}
	}

	private static String getNormalMsg(InputStream inputStream) {
		return getStringFromStream(inputStream);
	}

	private static String getErrorMsg(InputStream errorStream) {
		return getStringFromStream(errorStream);
	}

	private static String getStringFromStream(InputStream inputStream) {
		ByteArrayOutputStream writer = null;
		try {
			writer = new ByteArrayOutputStream();
			byte[] buffer = new byte[16];
			int len;
			while ((len = inputStream.read(buffer)) != -1) {
				writer.write(buffer, 0, len);
			}
			writer.flush();
			String str = writer.toString();
			return str;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeStream(new Closeable[] { inputStream, writer });
		}
		return null;
	}

	private static void closeStream(Closeable[] obj) {
		try {
			int len = obj.length;

			for (int i = 0; i < len; i++) {
				Closeable o = obj[i];
				if (o != null)
					o.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static abstract interface ICmdResultCallback {
		public abstract void onError(String paramString);

		public abstract void onComplete(String paramString);

		public abstract void onException(Exception paramException);
	}
}

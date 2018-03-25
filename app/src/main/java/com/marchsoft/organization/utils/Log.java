package com.marchsoft.organization.utils;

import android.os.Environment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日志工具类
 *
 * @author amose
 */
public class Log {

    private static final boolean DEBUG = Constant.DEBUG;
    private static final File DIR = new File(
            Environment.getExternalStorageDirectory() + "/organization/log/");

    private static final String FILE_NAME = "log.txt";

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    private static synchronized void save2file(String level, String tag,
                                               String msg) {

        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            if (!DIR.exists()) {
                if (!DIR.mkdirs()) {
                    return;
                }
            }

            FileOutputStream outputStream = null;
            File file = new File(DIR, FILE_NAME);
            if (file.exists() && file.length() > 1024 * 100) {
                file.delete();
            }
            try {
                outputStream = new FileOutputStream(file, true);
                outputStream.write(("[" + FORMAT.format(new Date()) + "] ["
                        + level + "] [" + tag + "] " + msg + "\n").getBytes());
            } catch (FileNotFoundException e) {

            } catch (IOException e) {

            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {

                    }
                }
            }
        }
    }

    public static void v(String tag, String msg) {
        if (DEBUG) {
            android.util.Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (DEBUG) {
            android.util.Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (DEBUG) {
            android.util.Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (DEBUG) {
            android.util.Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (DEBUG) {
            android.util.Log.e(tag, msg);
            save2file("error", tag, msg);
        }
    }
}

package com.baoge.wnotes.util;

import android.text.TextUtils;
import android.util.Log;

public class LogUtil {
    private static String TAG = "wang";
    private static LogUtil INSTANCE;

    private LogUtil() {
    }

    public static LogUtil getInstance() {
        if (INSTANCE == null) {
            synchronized (LogUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LogUtil();
                }
            }
        }

        return INSTANCE;
    }

    public static void i(String content) {
        if (!TextUtils.isEmpty(content)) {
            Log.i(TAG, content);
        }
    }

    public static void d(String content) {
        if (!TextUtils.isEmpty(content)) {
            Log.d(TAG, content);
        }
    }

    public static void e(String content) {
        if (!TextUtils.isEmpty(content)) {
            Log.e(TAG, content);
        }
    }

}

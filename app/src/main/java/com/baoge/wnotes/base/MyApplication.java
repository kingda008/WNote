package com.baoge.wnotes.base;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.baoge.wnotes.Constants;
import com.baoge.wnotes.util.LogUtil;
import com.baoge.wnotes.util.ToastUtil;

import java.io.File;


public class MyApplication extends Application {
    private static MyApplication INSTANCE;
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        ToastUtil.init();
        initFile();
    }

    private void initFile() {
        File dir = new File(Constants.LOG_DIR);
        if (!(dir.exists() && dir.isDirectory())) {
            dir.mkdirs();
        }

        dir = new File(Constants.EXCEL_DIR);
        LogUtil.i(Constants.EXCEL_DIR);
        if (!(dir.exists() && dir.isDirectory())) {
            boolean result = dir.mkdirs();
            LogUtil.i("创建文件夹结果：" + result);
        }
    }

    public static MyApplication getInstance() {
        return INSTANCE;
    }

    public void postRun(Runnable runnable) {
        handler.removeCallbacks(runnable);
        handler.post(runnable);
    }
}

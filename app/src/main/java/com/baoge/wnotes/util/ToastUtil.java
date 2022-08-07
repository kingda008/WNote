package com.baoge.wnotes.util;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baoge.wnotes.R;
import com.baoge.wnotes.base.MyApplication;

public class ToastUtil {
    private static Toast mToast = null;
    private static TextView textView;
    public static void init() {

        mToast = Toast.makeText(MyApplication.getInstance(), null, Toast.LENGTH_SHORT);
        View toastView = LayoutInflater.from(MyApplication.getInstance()).inflate(R.layout.view_toast, null, false);

        textView = toastView.findViewById(R.id.tv);
        mToast.setView(toastView);

        mToast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
    }

    public static void show(final String text) {
        MyApplication.getInstance().postRun(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    init();
                }
//                mToast.setText(text);
                textView.setText(text);
                mToast.show();
            }
        });
    }


}

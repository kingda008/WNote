package com.baoge.wnotes;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.baoge.wnotes.addinfo.AddInfoActivity;
import com.baoge.wnotes.base.BaseActivity;
import com.baoge.wnotes.order.AddOrderActivity;
import com.baoge.wnotes.query.OrderQueryActivity;
import com.baoge.wnotes.util.LogUtil;
import com.baoge.wnotes.util.ToastUtil;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    private MaterialDialog tipsDialog;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        showPassWordDialog();
    }


    private void showPassWordDialog() {


        tipsDialog = new MaterialDialog.Builder(MainActivity.this)
                .title("小王专用")
                .input("请输入密码", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        LogUtil.i("onInput:" + input.toString());
                    }
                })
                .cancelable(false)
                .positiveText("确定")
                .negativeText("取消")
                .autoDismiss(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {


                        if ("wdc0601".equals(dialog.getInputEditText().getText().toString())) {
                            ToastUtil.show("小王真好看");
                            tipsDialog.dismiss();
                        } else {
                            ToastUtil.show("密码错误");

                        }
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        dialog = null;
                        MainActivity.this.finish();

                    }
                })
                .build();


        tipsDialog.show();
    }


    public void init() {
        ((TextView) findViewById(R.id.add_info)).setOnClickListener(this);
        ((TextView) findViewById(R.id.add_record)).setOnClickListener(this);
        ((TextView) findViewById(R.id.query_record)).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_info:
                startActivity(new Intent(MainActivity.this, AddInfoActivity.class));
                break;
            case R.id.add_record:

                startActivity(MainActivity.this, AddOrderActivity.class);
                break;
            case R.id.query_record:
                startActivity(MainActivity.this, OrderQueryActivity.class);
                break;
        }
    }


}

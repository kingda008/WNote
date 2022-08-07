package com.baoge.wnotes.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.baoge.wnotes.MainActivity;
import com.baoge.wnotes.util.LogUtil;
import com.baoge.wnotes.util.ToastUtil;

import java.util.List;

public class BaseActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }


    }


    public void startActivity(Activity activity, Class clazz) {
        startActivity(new Intent(activity, clazz));
    }

    public void print(List<String> data) {
        if (data == null || data.size() == 0) {
            LogUtil.i("无数据");
        } else {
            for (String content : data) {
                LogUtil.i("--" + content);
            }
        }
    }

    public void clearEditText(List<EditText> editTexts) {
        if (editTexts != null && editTexts.size() > 0) {
            for (EditText edt : editTexts) {
                edt.setText("");
            }
        }
    }

    public void clearEditText(EditText editText) {
        if (editText != null) {

            editText.setText("");

        }
    }

    public interface OnDeleteLitener {
        void doDelete();
    }
    public void showDeleteDialog(final OnDeleteLitener deleteLitener) {
        new MaterialDialog.Builder(BaseActivity.this)
                .title("确定删除吗？").content("删除后无法找回！")
                .cancelable(false)
                .positiveText("确定")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if(deleteLitener!=null){
                            deleteLitener.doDelete();
                        }

                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        dialog = null;

                    }
                })
                .build().show();
    }

}

package com.baoge.wnotes.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.baoge.wnotes.base.MyApplication;

public class DataUtil {
    private static SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences("note", Context.MODE_PRIVATE); //私有数据

    private static SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器


    public static int getInstallPrice(String city) {
        return sharedPreferences.getInt(city, 300);
    }

    public static void setInstallPrice(String city, int price) {
        if (!TextUtils.isEmpty(city)) {
            editor.putInt(city, price);
        }
    }
}

package com.baoge.wnotes;

import android.os.Environment;

import androidx.annotation.IntDef;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Constants {
    private static String ROOT_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
    public static String LOG_DIR = ROOT_PATH + File.separator + "log";
    public static String FILE_DIR = ROOT_PATH + File.separator + "excel";

    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.PARAMETER)
    @IntDef(value = {Citys.HangZhou, Citys.HeFei, Citys.NanJing})
    public @interface Citys {
        int HangZhou = 1;
        int HeFei = 2;
        int NanJing = 3;
    }


    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.PARAMETER)
    @IntDef(value = {OrderType.NEW, OrderType.EDIT, OrderType.RETURN_GOODS})
    public @interface OrderType {
        int NEW = 0;//
        int EDIT = 1;
        int RETURN_GOODS = 2;//退货

    }
}

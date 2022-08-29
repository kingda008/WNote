package com.baoge.wnotes.util;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.util.List;

public class CommUtil {
   private static DecimalFormat decimalFormat=new DecimalFormat(".0");
    public static int getPosition(List<String> list,String content){
        if(list==null||list.size()<1|| TextUtils.isEmpty(content)){
            return -1;
        }
        int positon = -1;
        for(int i=0;i<list.size();i++){
            if(TextUtils.equals(list.get(i),content)){
                positon = i;
                return positon;
            }
        }

        return positon;
    }

    public static String parsePrice(double price){
        return decimalFormat.format(price);
    }


}

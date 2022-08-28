package com.baoge.wnotes.util;

import android.text.TextUtils;

import java.util.List;

public class CommUtil {
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
}

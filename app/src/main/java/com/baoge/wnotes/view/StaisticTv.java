package com.baoge.wnotes.view;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.baoge.wnotes.R;

public class StaisticTv extends androidx.appcompat.widget.AppCompatTextView {
    public StaisticTv(Context context) {
        super(context);
        this.setTextAppearance(R.style.Tv_Statics_Style);
    }

    public void setContent(String title, String data) {

        String str = "<font color='#000000'>" + title + ":" + "</font><font color='#1296db'>" + "  "+data + "</font>";
        this.setText(Html.fromHtml(str));

    }

}

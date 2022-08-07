package com.baoge.wnotes.view;

import android.content.Context;
import android.widget.TextView;

import com.baoge.wnotes.R;

public class DividingLine extends androidx.appcompat.widget.AppCompatTextView {
    public DividingLine(Context context) {
        super(context);
        this.setTextAppearance(R.style.Tv_DividingLine_Style);
        setContent("-----------------------------------------");
    }


    public void setContent(String text) {
        setText(text);
    }
}

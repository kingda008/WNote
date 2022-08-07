package com.baoge.wnotes.query;



import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.style.TtsSpan;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.baoge.wnotes.R;
import com.baoge.wnotes.base.BaseActivity;
import com.baoge.wnotes.db.Order;
import com.baoge.wnotes.util.DateFormat;
import com.baoge.wnotes.view.StaisticTv;

public class OrderInfoAcitivity extends BaseActivity {

    private LinearLayout rootGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info_acitivity);


        rootGroup = (LinearLayout) findViewById(R.id.lay_root);

        updateView();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        updateView();
    }

    private void updateView() {
        String orderJson = getIntent().getStringExtra("orderjson");
        if (!TextUtils.isEmpty(orderJson)) {
            Order order = JSON.parseObject(orderJson, Order.class);

            StaisticTv view = new StaisticTv(OrderInfoAcitivity.this);
            view.setContent("城市", order.getCity());
            rootGroup.addView(view);

            view = new StaisticTv(OrderInfoAcitivity.this);
            view.setContent("医院", order.getHospital());
            rootGroup.addView(view);

            view = new StaisticTv(OrderInfoAcitivity.this);
            view.setContent("科室", order.getDepartMent());
            rootGroup.addView(view);

            view = new StaisticTv(OrderInfoAcitivity.this);
            view.setContent("技师", order.getTechnician());
            rootGroup.addView(view);

            view = new StaisticTv(OrderInfoAcitivity.this);
            view.setContent("装机", order.getInstaller());
            rootGroup.addView(view);

            view = new StaisticTv(OrderInfoAcitivity.this);
            view.setContent("设备", order.getDevice());
            rootGroup.addView(view);

            view = new StaisticTv(OrderInfoAcitivity.this);
            view.setContent("日期", DateFormat.getDate(order.getOrderTime(), DateFormat.FORMAT_YYYY_MM_DD));
            rootGroup.addView(view);


            view = new StaisticTv(OrderInfoAcitivity.this);
            view.setContent("金额", order.getTransactionAmount() + "");
            rootGroup.addView(view);


            view = new StaisticTv(OrderInfoAcitivity.this);
            view.setContent("打车", order.getTaxiFare() + "");
            rootGroup.addView(view);

            view = new StaisticTv(OrderInfoAcitivity.this);
            view.setContent("配件", order.getPartPrice() + "");
            rootGroup.addView(view);

            view = new StaisticTv(OrderInfoAcitivity.this);
            view.setContent("支持", order.getSupportPrice() + "");
            rootGroup.addView(view);

            view = new StaisticTv(OrderInfoAcitivity.this);
            String otherContent = order.getOtherContent();
            otherContent = TextUtils.isEmpty(otherContent) ? "无" : otherContent;
            view.setContent("其他描述", otherContent);
            rootGroup.addView(view);

            view = new StaisticTv(OrderInfoAcitivity.this);
            view.setContent("其他金额", order.getOtherPrice() + "");
            rootGroup.addView(view);

        }
    }
}

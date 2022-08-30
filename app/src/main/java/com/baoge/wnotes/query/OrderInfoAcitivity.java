package com.baoge.wnotes.query;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.style.TtsSpan;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.baoge.wnotes.R;
import com.baoge.wnotes.base.BaseActivity;
import com.baoge.wnotes.db.Order;
import com.baoge.wnotes.order.AddOrderActivity;
import com.baoge.wnotes.util.DateFormat;
import com.baoge.wnotes.util.LogUtil;
import com.baoge.wnotes.util.ToastUtil;
import com.baoge.wnotes.view.StaisticTv;

public class OrderInfoAcitivity extends BaseActivity {

    private LinearLayout rootGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.i("oncreate");
        setContentView(R.layout.activity_order_info_acitivity);

        rootGroup = (LinearLayout) findViewById(R.id.lay_root);
        updateView();
    }



    private void updateView() {
        final String orderJson = getIntent().getStringExtra("orderjson");
        LogUtil.i(orderJson);
        View edit = rootGroup.getChildAt(0);
        if (!TextUtils.isEmpty(orderJson)) {
            Order order = JSON.parseObject(orderJson, Order.class);
            rootGroup.removeAllViews();

            StaisticTv view = new StaisticTv(OrderInfoAcitivity.this);
            view.setContent("城市", order.getCity());
            rootGroup.addView(view);

//            view = new StaisticTv(OrderInfoAcitivity.this);
//            view.setContent("医院", order.getHospital());
//            rootGroup.addView(view);
//
//            view = new StaisticTv(OrderInfoAcitivity.this);
//            view.setContent("科室", order.getDepartMent());
//            rootGroup.addView(view);

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
            view.setContent("是否已经支持", order.getIsAleadySupport() ? "是" : "否");
            rootGroup.addView(view);


            view = new StaisticTv(OrderInfoAcitivity.this);
            view.setContent("发票", order.getInvoice() + "");
            rootGroup.addView(view);


            view = new StaisticTv(OrderInfoAcitivity.this);
            String otherContent = order.getOtherContent();
            otherContent = TextUtils.isEmpty(otherContent) ? "无" : otherContent;
            view.setContent("其他描述", otherContent);
            rootGroup.addView(view);

            view = new StaisticTv(OrderInfoAcitivity.this);
            view.setContent("其他金额", order.getOtherPrice() + "");
            rootGroup.addView(view);


            rootGroup.addView(edit);


            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(OrderInfoAcitivity.this, AddOrderActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("order", orderJson);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}

package com.baoge.wnotes.query;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoge.wnotes.Constants;
import com.baoge.wnotes.R;
import com.baoge.wnotes.base.BaseActivity;
import com.baoge.wnotes.db.Order;
import com.baoge.wnotes.manager.DbManager;
import com.baoge.wnotes.util.DataUtil;
import com.baoge.wnotes.util.DateFormat;
import com.baoge.wnotes.util.ExcelUtil;
import com.baoge.wnotes.util.LogUtil;
import com.baoge.wnotes.util.ToastUtil;
import com.baoge.wnotes.util.WeChatUtil;
import com.baoge.wnotes.view.DividingLine;
import com.baoge.wnotes.view.StaisticTv;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class StatisticActivity extends BaseActivity implements View.OnClickListener {
    private String city;
    private long startTime;
    private long endTime;
    private LinearLayout rootLayout;
    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        init();
        city = getIntent().getStringExtra("city");
        startTime = getIntent().getLongExtra("startTime", -1);
        endTime = getIntent().getLongExtra("endTime", -1);
        statistic(city, startTime, endTime);

    }

    private void init() {
        rootLayout = (LinearLayout) findViewById(R.id.lay_statistic);
        findViewById(R.id.btn_share).setOnClickListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        city = getIntent().getStringExtra("city");
        startTime = getIntent().getLongExtra("startTime", -1);
        endTime = getIntent().getLongExtra("endTime", -1);

        statistic(city, startTime, endTime);
    }

    private void statistic(String city, long startTime, long endTime) {
        LogUtil.i("statistic city:" + city);
        LogUtil.i("statistic startTime:" + startTime);
        LogUtil.i("statistic endTime:" + endTime);

        boolean isInvalid = false;
        if (!TextUtils.isEmpty(city) && startTime > 0 && endTime > 0) {
            orderList = DbManager.getInstance().queryOrders(city, startTime, endTime);

            StaisticTv staisticTv = new StaisticTv(StatisticActivity.this);
            staisticTv.setContent("开始时间", DateFormat.getDate(startTime, DateFormat.FORMAT_YYYY_MM_DD_HHMMSS));
            rootLayout.addView(staisticTv);

            staisticTv = new StaisticTv(StatisticActivity.this);
            staisticTv.setContent("截止时间", DateFormat.getDate(endTime, DateFormat.FORMAT_YYYY_MM_DD_HHMMSS));
            rootLayout.addView(staisticTv);


            staisticTv = new StaisticTv(StatisticActivity.this);
            int counts = orderList == null ? 0 : orderList.size();
            staisticTv.setContent("订单总数", counts + "");
            rootLayout.addView(staisticTv);

            if (orderList != null && orderList.size() > 0) {
                int totalMoney = 0;
                int taxiMoney = 0;
                int installMoney = 0;
                int partDeviceMoney = 0;
                int otherMoney = 0;
                int deviceMoney = 0;
                int supportMoney = 0;
                int invoceMoney = 0;
                HashMap<String, Integer> technicianMoneyMap = new HashMap<>();
                HashMap<String, Integer> departMentMoneyMap = new HashMap<>();


                for (Order order : orderList) {
                    totalMoney = totalMoney + order.getTransactionAmount();
                    taxiMoney = taxiMoney + order.getTaxiFare();
                    partDeviceMoney = partDeviceMoney + order.getPartPrice();
                    otherMoney = otherMoney + order.getOtherPrice();
                    supportMoney = supportMoney + order.getSupportPrice();
                    int deviceMoneyDB = DbManager.getInstance().queryDevicePrice(order.getDevice());
                    deviceMoney = deviceMoney + deviceMoneyDB;
                    installMoney = installMoney + order.getInstallPrice();
                    invoceMoney = invoceMoney+order.getInvoice();

                    int technicianMoney = (order.getTransactionAmount() - order.getTaxiFare() - order.getPartPrice() - order.getInstallPrice() - order.getOtherPrice() - order.getSupportPrice() - deviceMoneyDB) / 3;
                    if (technicianMoneyMap.containsKey(order.getTechnician())) {
                        technicianMoneyMap.put(order.getTechnician(), technicianMoneyMap.get(order.getTechnician()) + technicianMoney);
                    } else {
                        technicianMoneyMap.put(order.getTechnician(), technicianMoney);
                    }

                    //科室支持费用
                    String tag = order.getHospital() + "#" + order.getDepartMent();

                    if (departMentMoneyMap.containsKey(tag)) {

                        departMentMoneyMap.put(tag, departMentMoneyMap.get(tag) + order.getSupportPrice());
                    } else {
                        departMentMoneyMap.put(tag, order.getSupportPrice());
                    }


                }
                staisticTv = new StaisticTv(StatisticActivity.this);
                staisticTv.setContent("费用总数", totalMoney + "");
                rootLayout.addView(staisticTv);

                staisticTv = new StaisticTv(StatisticActivity.this);
                staisticTv.setContent("设备成本", deviceMoney + "");
                rootLayout.addView(staisticTv);

                staisticTv = new StaisticTv(StatisticActivity.this);
                staisticTv.setContent("配件成本", partDeviceMoney + "");
                rootLayout.addView(staisticTv);

                staisticTv = new StaisticTv(StatisticActivity.this);
                staisticTv.setContent("装机成本", installMoney + "");
                rootLayout.addView(staisticTv);

                staisticTv = new StaisticTv(StatisticActivity.this);
                staisticTv.setContent("支持成本", supportMoney + "");
                rootLayout.addView(staisticTv);

                staisticTv = new StaisticTv(StatisticActivity.this);
                staisticTv.setContent("发票成本", supportMoney + "");
                rootLayout.addView(staisticTv);


                staisticTv = new StaisticTv(StatisticActivity.this);
                staisticTv.setContent("其他成本", otherMoney + "");
                rootLayout.addView(staisticTv);

                staisticTv = new StaisticTv(StatisticActivity.this);
                double profit = totalMoney - deviceMoney - partDeviceMoney - installMoney - supportMoney - taxiMoney - otherMoney-invoceMoney;
                staisticTv.setContent("利润总数", profit + "");
                rootLayout.addView(staisticTv);

                staisticTv = new StaisticTv(StatisticActivity.this);
                staisticTv.setContent("利润均数", profit / 3 + "");
                rootLayout.addView(staisticTv);

                DividingLine dividingLine = new DividingLine(StatisticActivity.this);
                rootLayout.addView(dividingLine);


                Iterator<String> iterator = technicianMoneyMap.keySet().iterator();
                while (iterator.hasNext()) {
                    String name = iterator.next();
                    double money = technicianMoneyMap.get(name);

                    staisticTv = new StaisticTv(StatisticActivity.this);
                    staisticTv.setContent(name, money + "");
                    rootLayout.addView(staisticTv);
                }

                dividingLine = new DividingLine(StatisticActivity.this);
                rootLayout.addView(dividingLine);


//                iterator = departMentMoneyMap.keySet().iterator();
//                while (iterator.hasNext()) {
//                    String name = iterator.next();
//                    double money = departMentMoneyMap.get(name);
//
//                    staisticTv = new StaisticTv(StatisticActivity.this);
//                    staisticTv.setContent(name, money + "");
//                    rootLayout.addView(staisticTv);
//                }


            }
        } else {
            rootLayout.removeAllViews();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_share:


                String excelFileName = city + "_" + DateFormat.getDate(System.currentTimeMillis(), DateFormat.FORMAT_YYYY_MM_DD) + ".xls";
                String[] title = {"时间", "城市", "医院", "科室", "装机师", "技师", "设备", "交易金额", "打车", "支持", "配件费用", "其他费用", "给我"};
                String sheetName = "明细";

                String filePaths = Constants.EXCEL_DIR + File.separator + excelFileName;
                LogUtil.i(filePaths);
                ExcelUtil.initExcel(filePaths, sheetName, title);
                ExcelUtil.getInstance().writeOrdersListToExcel(orderList, filePaths);
                WeChatUtil.shareWechatFriend(StatisticActivity.this, new File(filePaths));

                break;
        }
    }
}

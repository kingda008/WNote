package com.baoge.wnotes.query;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.baoge.wnotes.Constants;
import com.baoge.wnotes.R;
import com.baoge.wnotes.base.BaseActivity;
import com.baoge.wnotes.db.Order;
import com.baoge.wnotes.manager.DbManager;
import com.baoge.wnotes.util.CommUtil;
import com.baoge.wnotes.util.DateFormat;
import com.baoge.wnotes.util.ExcelUtil;
import com.baoge.wnotes.util.LogUtil;
import com.baoge.wnotes.util.WeChatUtil;
import com.baoge.wnotes.view.DividingLine;
import com.baoge.wnotes.view.StaisticTv;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class StatisticActivity extends BaseActivity implements View.OnClickListener {
    private String city;
    private long startTime;
    private long endTime;
    private LinearLayout rootLayout;
    private List<Order> orderList;

    private StringBuilder stringBuilder;
    private FileOutputStream fileOutputStream;

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
        findViewById(R.id.btn_share_total).setOnClickListener(this);

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
        if (startTime > 0 && endTime > 0) {
            stringBuilder = new StringBuilder();
            if (TextUtils.isEmpty(city)) {
                orderList = DbManager.getInstance().queryOrders(startTime, endTime);
            } else {
                orderList = DbManager.getInstance().queryOrders(city, startTime, endTime);
            }

            StaisticTv staisticTv = new StaisticTv(StatisticActivity.this);
            String cityContent = TextUtils.isEmpty(city) ? "所有城市" : city;
            staisticTv.setContent("城市", cityContent);
            rootLayout.addView(staisticTv);
            stringBuilder.append("城市：").append(cityContent).append("\r\n");

            staisticTv = new StaisticTv(StatisticActivity.this);
            String startTimeContent = DateFormat.getDate(startTime, DateFormat.FORMAT_YYYY_MM_DD_HHMMSS);
            staisticTv.setContent("开始时间：", startTimeContent);
            rootLayout.addView(staisticTv);
            stringBuilder.append("开始时间").append(startTimeContent).append("\r\n");

            staisticTv = new StaisticTv(StatisticActivity.this);
            String endTimeContent = DateFormat.getDate(endTime, DateFormat.FORMAT_YYYY_MM_DD_HHMMSS);
            staisticTv.setContent("截止时间", endTimeContent);
            rootLayout.addView(staisticTv);
            stringBuilder.append("截止时间：").append(endTimeContent).append("\r\n");


            staisticTv = new StaisticTv(StatisticActivity.this);
            int counts = orderList == null ? 0 : orderList.size();
            staisticTv.setContent("订单总数", counts + "");
            rootLayout.addView(staisticTv);
            stringBuilder.append("订单总数：").append(counts).append("\r\n");

            if (orderList != null && orderList.size() > 0) {
                int totalMoney = 0;
                int taxiMoney = 0;
                int installMoney = 0;
                int partDeviceMoney = 0;
                int otherMoney = 0;
                int deviceMoney = 0;
                int supportMoney = 0;
                int invoceMoney = 0;

                int toMe = 0;
                HashMap<String, Double> technicianMoneyMap = new HashMap<>();
                HashMap<String, Integer> departMentMoneyMap = new HashMap<>();


                for (Order order : orderList) {
                    totalMoney = totalMoney + order.getTransactionAmount();
                    taxiMoney = taxiMoney + order.getTaxiFare();
                    partDeviceMoney = partDeviceMoney + order.getPartPrice();
                    otherMoney = otherMoney + order.getOtherPrice();
                    supportMoney = supportMoney + order.getSupportPrice();
                    int deviceMoneyDB = order.getDevicePrice();
                    deviceMoney = deviceMoney + deviceMoneyDB;
                    installMoney = installMoney + order.getInstallPrice();
                    invoceMoney = invoceMoney + order.getInvoice();

                    double technicianMoney = (order.getTransactionAmount() - order.getTaxiFare() - order.getPartPrice() - order.getInstallPrice() - order.getOtherPrice() - order.getSupportPrice() - deviceMoneyDB - order.getInvoice()) / 3.0;
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
                stringBuilder.append("费用总数：").append(totalMoney).append("\r\n");


                staisticTv = new StaisticTv(StatisticActivity.this);
                staisticTv.setContent("设备成本", deviceMoney + "");
                rootLayout.addView(staisticTv);
                stringBuilder.append("设备成本：").append(deviceMoney).append("\r\n");


                staisticTv = new StaisticTv(StatisticActivity.this);
                staisticTv.setContent("配件成本", partDeviceMoney + "");
                rootLayout.addView(staisticTv);
                stringBuilder.append("配件成本：").append(partDeviceMoney).append("\r\n");


                staisticTv = new StaisticTv(StatisticActivity.this);
                staisticTv.setContent("装机成本", installMoney + "");
                rootLayout.addView(staisticTv);
                stringBuilder.append("装机成本：").append(installMoney).append("\r\n");


                staisticTv = new StaisticTv(StatisticActivity.this);
                staisticTv.setContent("支持成本", supportMoney + "");
                rootLayout.addView(staisticTv);
                stringBuilder.append("支持成本：").append(supportMoney).append("\r\n");


                staisticTv = new StaisticTv(StatisticActivity.this);
                staisticTv.setContent("发票成本", invoceMoney + "");
                rootLayout.addView(staisticTv);
                stringBuilder.append("发票成本：").append(invoceMoney).append("\r\n");


                staisticTv = new StaisticTv(StatisticActivity.this);
                staisticTv.setContent("打车成本", taxiMoney + "");
                rootLayout.addView(staisticTv);
                stringBuilder.append("打车成本：").append(taxiMoney).append("\r\n");


                staisticTv = new StaisticTv(StatisticActivity.this);
                staisticTv.setContent("其他成本", otherMoney + "");
                rootLayout.addView(staisticTv);
                stringBuilder.append("其他成本：").append(otherMoney).append("\r\n");


                staisticTv = new StaisticTv(StatisticActivity.this);
                int profit = totalMoney - deviceMoney - partDeviceMoney - installMoney - supportMoney - taxiMoney - otherMoney - invoceMoney;
                staisticTv.setContent("利润总数", profit + "");
                rootLayout.addView(staisticTv);
                stringBuilder.append("利润总数：").append(profit).append("\r\n");


                staisticTv = new StaisticTv(StatisticActivity.this);
                staisticTv.setContent("利润均数", CommUtil.parsePrice(profit / 3.0));
                rootLayout.addView(staisticTv);
                stringBuilder.append("利润均数：").append(CommUtil.parsePrice(profit / 3.0)).append("\r\n");


                staisticTv = new StaisticTv(StatisticActivity.this);
                String toMeContent = CommUtil.parsePrice((profit * 2 / 3.0 + taxiMoney + supportMoney));
                staisticTv.setContent("给我的", toMeContent);
                rootLayout.addView(staisticTv);
                stringBuilder.append("给我的：").append(toMeContent).append("\r\n");


                DividingLine dividingLine = new DividingLine(StatisticActivity.this);
                rootLayout.addView(dividingLine);
                stringBuilder.append("\r\n").append("************").append("\r\n");


                Iterator<String> iterator = technicianMoneyMap.keySet().iterator();
                while (iterator.hasNext()) {
                    String name = iterator.next();
                    double money = technicianMoneyMap.get(name);

                    staisticTv = new StaisticTv(StatisticActivity.this);
                    staisticTv.setContent(name, CommUtil.parsePrice(money));
                    rootLayout.addView(staisticTv);

                    stringBuilder.append(name + "：").append(CommUtil.parsePrice(money)).append("\r\n");

                }

                dividingLine = new DividingLine(StatisticActivity.this);
                rootLayout.addView(dividingLine);
                stringBuilder.append("\r\n").append("************").append("\r\n");

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
            case R.id.btn_share: {


                String excelFileName = (TextUtils.isEmpty(city) ? "所有城市" : city) + "_明细_" + DateFormat.getDate(System.currentTimeMillis(), DateFormat.FORMAT_YYYY_MM_DD) + ".xls";
                String[] title = {"时间", "城市", "装机师", "技师", "技师费用", "设备", "交易金额", "打车", "支持者", "支持", "配件费用", "发票", "其他费用", "给我"};
                String sheetName = "明细";

                String filePaths = Constants.FILE_DIR + File.separator + excelFileName;
                LogUtil.i(filePaths);
                ExcelUtil.initExcel(filePaths, sheetName, title);
                ExcelUtil.getInstance().writeOrdersListToExcel(orderList, filePaths);
                WeChatUtil.shareWechatFriend(StatisticActivity.this, new File(filePaths));
            }
            break;

            case R.id.btn_share_total:
                String fileName = (TextUtils.isEmpty(city) ? "所有城市" : city) + "_总结_" + DateFormat.getDate(System.currentTimeMillis(), DateFormat.FORMAT_YYYY_MM_DD) + ".txt";
                String filePaths = Constants.FILE_DIR + File.separator + fileName;
                File file = new File(filePaths);
                if (file.exists()) {
                    file.delete();
                }
                try {
                    file.createNewFile();
                    fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.getFD().sync();
                    fileOutputStream.write(stringBuilder.toString().getBytes());
                    fileOutputStream.flush();
                    WeChatUtil.shareWechatFriend(StatisticActivity.this, file);


                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        fileOutputStream = null;
                    }
                }

                break;
        }
    }
}

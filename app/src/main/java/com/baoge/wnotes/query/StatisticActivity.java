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
            String cityContent = TextUtils.isEmpty(city) ? "????????????" : city;
            staisticTv.setContent("??????", cityContent);
            rootLayout.addView(staisticTv);
            stringBuilder.append("?????????").append(cityContent).append("\r\n");

            staisticTv = new StaisticTv(StatisticActivity.this);
            String startTimeContent = DateFormat.getDate(startTime, DateFormat.FORMAT_YYYY_MM_DD_HHMMSS);
            staisticTv.setContent("???????????????", startTimeContent);
            rootLayout.addView(staisticTv);
            stringBuilder.append("????????????").append(startTimeContent).append("\r\n");

            staisticTv = new StaisticTv(StatisticActivity.this);
            String endTimeContent = DateFormat.getDate(endTime, DateFormat.FORMAT_YYYY_MM_DD_HHMMSS);
            staisticTv.setContent("????????????", endTimeContent);
            rootLayout.addView(staisticTv);
            stringBuilder.append("???????????????").append(endTimeContent).append("\r\n");


            staisticTv = new StaisticTv(StatisticActivity.this);
            int counts = orderList == null ? 0 : orderList.size();
            staisticTv.setContent("????????????", counts + "");
            rootLayout.addView(staisticTv);
            stringBuilder.append("???????????????").append(counts).append("\r\n");

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
                    deviceMoney = deviceMoney + order.getDevicePrice();
                    installMoney = installMoney + order.getInstallPrice();
                    invoceMoney = invoceMoney + order.getInvoice();

                    double technicianMoney = (order.getTransactionAmount() - order.getTaxiFare() - order.getPartPrice() - order.getInstallPrice() - order.getOtherPrice() - order.getSupportPrice() - order.getDevicePrice() - order.getInvoice()) / 3.0;
                    String techicianTag = order.getCity()+"#"+order.getTechnician();
                    if (technicianMoneyMap.containsKey(techicianTag)) {
                        technicianMoneyMap.put(techicianTag, technicianMoneyMap.get(techicianTag) + technicianMoney);
                    } else {
                        technicianMoneyMap.put(techicianTag, technicianMoney);
                    }

                    //??????????????????
                    String tag = order.getHospital() + "#" + order.getDepartMent();

                    if (departMentMoneyMap.containsKey(tag)) {

                        departMentMoneyMap.put(tag, departMentMoneyMap.get(tag) + order.getSupportPrice());
                    } else {
                        departMentMoneyMap.put(tag, order.getSupportPrice());
                    }


                }
                staisticTv = new StaisticTv(StatisticActivity.this);
                staisticTv.setContent("????????????", totalMoney + "");
                rootLayout.addView(staisticTv);
                stringBuilder.append("???????????????").append(totalMoney).append("\r\n");


                staisticTv = new StaisticTv(StatisticActivity.this);
                staisticTv.setContent("????????????", deviceMoney + "");
                rootLayout.addView(staisticTv);
                stringBuilder.append("???????????????").append(deviceMoney).append("\r\n");


                staisticTv = new StaisticTv(StatisticActivity.this);
                staisticTv.setContent("????????????", partDeviceMoney + "");
                rootLayout.addView(staisticTv);
                stringBuilder.append("???????????????").append(partDeviceMoney).append("\r\n");


                staisticTv = new StaisticTv(StatisticActivity.this);
                staisticTv.setContent("????????????", installMoney + "");
                rootLayout.addView(staisticTv);
                stringBuilder.append("???????????????").append(installMoney).append("\r\n");


                staisticTv = new StaisticTv(StatisticActivity.this);
                staisticTv.setContent("????????????", supportMoney + "");
                rootLayout.addView(staisticTv);
                stringBuilder.append("???????????????").append(supportMoney).append("\r\n");


                staisticTv = new StaisticTv(StatisticActivity.this);
                staisticTv.setContent("????????????", invoceMoney + "");
                rootLayout.addView(staisticTv);
                stringBuilder.append("???????????????").append(invoceMoney).append("\r\n");


                staisticTv = new StaisticTv(StatisticActivity.this);
                staisticTv.setContent("????????????", taxiMoney + "");
                rootLayout.addView(staisticTv);
                stringBuilder.append("???????????????").append(taxiMoney).append("\r\n");


                staisticTv = new StaisticTv(StatisticActivity.this);
                staisticTv.setContent("????????????", otherMoney + "");
                rootLayout.addView(staisticTv);
                stringBuilder.append("???????????????").append(otherMoney).append("\r\n");


                staisticTv = new StaisticTv(StatisticActivity.this);
                int profit = totalMoney - deviceMoney - partDeviceMoney - installMoney - supportMoney - taxiMoney - otherMoney - invoceMoney;
                staisticTv.setContent("????????????", profit + "");
                rootLayout.addView(staisticTv);
                stringBuilder.append("???????????????").append(profit).append("\r\n");


                staisticTv = new StaisticTv(StatisticActivity.this);
                staisticTv.setContent("????????????", CommUtil.parsePrice(profit / 3.0));
                rootLayout.addView(staisticTv);
                stringBuilder.append("???????????????").append(CommUtil.parsePrice(profit / 3.0)).append("\r\n");


                staisticTv = new StaisticTv(StatisticActivity.this);
                String toMeContent = CommUtil.parsePrice((profit * 2 / 3.0 + taxiMoney + supportMoney));
                staisticTv.setContent("?????????", toMeContent);
                rootLayout.addView(staisticTv);
                stringBuilder.append("????????????").append(toMeContent).append("\r\n");


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

                    stringBuilder.append(name + "???").append(CommUtil.parsePrice(money)).append("\r\n");

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


                String excelFileName = (TextUtils.isEmpty(city) ? "????????????" : city) + "_??????_" + DateFormat.getDate(System.currentTimeMillis(), DateFormat.FORMAT_YYYY_MM_DD) + ".xls";
                String[] title = {"??????", "??????", "?????????", "??????", "????????????", "??????", "??????", "??????", "?????????", "??????", "????????????", "??????", "????????????", "??????"};
                String sheetName = "??????";

                String filePaths = Constants.FILE_DIR + File.separator + excelFileName;
                LogUtil.i(filePaths);
                ExcelUtil.initExcel(filePaths, sheetName, title);
                ExcelUtil.getInstance().writeOrdersListToExcel(orderList, filePaths);
                WeChatUtil.shareWechatFriend(StatisticActivity.this, new File(filePaths));
            }
            break;

            case R.id.btn_share_total:
                String fileName = (TextUtils.isEmpty(city) ? "????????????" : city) + "_??????_" + DateFormat.getDate(System.currentTimeMillis(), DateFormat.FORMAT_YYYY_MM_DD) + ".txt";
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

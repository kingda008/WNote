package com.baoge.wnotes.query;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baoge.wnotes.R;
import com.baoge.wnotes.adapter.OrderListAdapter;
import com.baoge.wnotes.base.BaseActivity;
import com.baoge.wnotes.db.Order;
import com.baoge.wnotes.manager.DbManager;
import com.baoge.wnotes.order.AddOrderActivity;
import com.baoge.wnotes.util.CommUtil;
import com.baoge.wnotes.util.DateFormat;
import com.baoge.wnotes.util.LogUtil;
import com.baoge.wnotes.util.ToastUtil;
import com.baoge.wnotes.view.RecycleViewDivider;
import com.baoge.wnotes.view.SpaceItemDecoration;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class OrderQueryActivity extends BaseActivity implements View.OnClickListener {
    private AppCompatSpinner spinner, technicianSpinner;
    private List<String> citys = null;
    private List<String> technicians = null;
    private ArrayAdapter<String> citySpinnerAdapter;
    private ArrayAdapter<String> technicianSpinnerAdapter;
    private int citySpinnerSelectPosition = 0;
    private int technicianSpinnerSelectPosition = -1;
    private RecyclerView recyclerView;
    private TextView mounth;
    private OrderListAdapter orderListAdapter;
    private List<Order> orders;
    private long startTime, endTime;
    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            LogUtil.i("year:" + year);
            LogUtil.i("month:" + month);
            LogUtil.i("dayOfMonth:" + dayOfMonth);
            month++;
            String dayOfMonthStr = dayOfMonth + "";
            dayOfMonthStr = dayOfMonthStr.length() == 1 ? "0" + dayOfMonthStr : dayOfMonthStr;


            String monthStr = month + "";
            monthStr = monthStr.length() == 1 ? "0" + monthStr : monthStr;


            mounth.setText(year + "-" + monthStr);
            updateOrderList();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_query);

        init();


    }

    @Override
    protected void onResume() {
        super.onResume();
        citys = DbManager.getInstance().queryCitysName();
        citys.add(0, "所有城市");

        if (citys != null && citys.size() > 0) {
            for (int i = 0; i < citys.size(); i++) {
                LogUtil.i(citys.get(i));
            }
            mounth.setClickable(true);
            initCityAdapter();
            initTechnicianSpinner("");
        } else {
            mounth.setClickable(false);
        }
    }

    private void init() {

        spinner = (AppCompatSpinner) findViewById(R.id.sp_city);
        technicianSpinner = (AppCompatSpinner) findViewById(R.id.sp_technician);

        recyclerView = (RecyclerView) findViewById(R.id.rcv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ((TextView) findViewById(R.id.tv_order_mounth_content)).setOnClickListener(this);
        findViewById(R.id.btn_statistics).setOnClickListener(this);
        findViewById(R.id.btn_delete_all).setOnClickListener(this);

        mounth = (TextView) findViewById(R.id.tv_order_mounth_content);

        orderListAdapter = new OrderListAdapter();
        recyclerView.setAdapter(orderListAdapter);


        recyclerView.addItemDecoration(new SpaceItemDecoration(2));


        orderListAdapter.setOnItemClickLitener(new OrderListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {


                Intent intent = new Intent(OrderQueryActivity.this, OrderInfoAcitivity.class);
                intent.putExtra("orderjson", JSON.toJSONString(orders.get(position)));
                startActivity(intent);
            }
        });

        orderListAdapter.setOnItemLongClickLitener(new OrderListAdapter.OnItemLongClickLitener() {
            @Override
            public void onItemLongClick(View view, final int position) {
                showDeleteDialog(new OnDeleteLitener() {
                    @Override
                    public void doDelete() {
                        DbManager.getInstance().deleteOrder(orders.get(position));
                        updateOrderList();
                    }
                });
            }
        });
    }

    private void initCityAdapter() {
        citySpinnerSelectPosition = 0;
        citySpinnerAdapter = new ArrayAdapter<>(this, R.layout.item_textview, citys);
        spinner.setAdapter(citySpinnerAdapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LogUtil.d("city:" + citys.get(position));
                citySpinnerSelectPosition = position;
                if (position > 0) {
                    findViewById(R.id.tv_technician_choise).setVisibility(View.VISIBLE);
                    findViewById(R.id.sp_technician).setVisibility(View.VISIBLE);
                } else {
                    if (position > 0) {
                        findViewById(R.id.tv_technician_choise).setVisibility(View.GONE);
                        findViewById(R.id.sp_technician).setVisibility(View.GONE);
                    }
                }

                initTechnicianSpinner(citys.get(position));
                updateOrderList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                LogUtil.d("onNothingSelected");
            }
        });
    }

    private void initTechnicianSpinner(String city) {
        LogUtil.d("initTechnicianSpinner " + city);
        technicianSpinnerSelectPosition = -1;
        if (TextUtils.isEmpty(city)) {
            technicians = DbManager.getInstance().queryTechnicianNames();
        } else {
            technicians = DbManager.getInstance().queryTechnicianNames(city);
        }
        technicians.add(0, "所有技师");
        CommUtil.printLog(technicians);
        technicianSpinnerAdapter = new ArrayAdapter<>(this, R.layout.item_textview, technicians);
        technicianSpinner.setAdapter(technicianSpinnerAdapter);

        if (technicians != null && technicians.size() > 0) {
            technicianSpinnerSelectPosition = 0;
            technicianSpinner.setSelection(0);
            technicianSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    LogUtil.d("onItemSelected technicians:" + technicians.get(position));
                    technicianSpinnerSelectPosition = position;
                    updateOrderList();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    LogUtil.d("onNothingSelected");
                }
            });
        } else {
            technicianSpinnerAdapter.clear();
            technicianSpinnerAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_statistics:
                if (orders == null || orders.size() == 0) {
                    ToastUtil.show("无数据");
                    return;
                }

                Intent intent = new Intent(OrderQueryActivity.this, StatisticActivity.class);
                intent.putExtra("city", citySpinnerSelectPosition == 0 ? "" : citys.get(citySpinnerSelectPosition));
                intent.putExtra("startTime", startTime);
                intent.putExtra("endTime", endTime);
                intent.putExtra("technician", technicians.get(technicianSpinnerSelectPosition));
                startActivity(intent);
                break;
            case R.id.btn_delete_all:
                showDeleteDialog("确定清空所有订单吗？", new OnDeleteLitener() {
                    @Override
                    public void doDelete() {
                        if (orders != null && orders.size() > 0) {
                            for (Order order : orders) {
                                DbManager.getInstance().deleteOrder(order);

                                updateOrderList();
                            }
                        }
                    }
                });
                break;

            case R.id.tv_order_mounth_content:
                Calendar ca = Calendar.getInstance();

                int mYear = ca.get(Calendar.YEAR);
                int mMonth = ca.get(Calendar.MONTH);
                int mDay = ca.get(Calendar.DAY_OF_MONTH);
                LogUtil.i("year:" + mYear);
                LogUtil.i("mMonth:" + mMonth);
                LogUtil.i("mDay:" + mDay);

                DatePickerDialog datePickerDialog = new DatePickerDialog(OrderQueryActivity.this, DatePickerDialog.THEME_HOLO_LIGHT, onDateSetListener, mYear, mMonth, mDay);
                datePickerDialog.show();


                DatePicker dp = findDatePicker((ViewGroup) datePickerDialog.getWindow().getDecorView());
                if (dp != null) {
                    ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0))
                            .getChildAt(2).setVisibility(View.GONE);
                    //如果想隐藏掉年，将getChildAt(2)改为getChildAt(0)
                }
                break;
        }
    }


    private DatePicker findDatePicker(ViewGroup group) {
        if (group != null) {
            for (int i = 0, j = group.getChildCount(); i < j; i++) {
                View child = group.getChildAt(i);
                if (child instanceof DatePicker) {
                    return (DatePicker) child;
                } else if (child instanceof ViewGroup) {
                    DatePicker result = findDatePicker((ViewGroup) child);
                    if (result != null)
                        return result;
                }
            }
        }
        return null;
    }


    private void updateOrderList() {
        String mounthTv = mounth.getText().toString();
        if (!TextUtils.isEmpty(mounthTv) && mounthTv.contains("-")) {
            String[] times = mounthTv.split("-");
            int year = Integer.parseInt(times[0]);
            int mounth = Integer.parseInt(times[1]) - 1;

            Calendar calendar = Calendar.getInstance(Locale.CHINA);
            calendar.clear();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, mounth);
            int firstDay = calendar.getActualMinimum(calendar.DAY_OF_MONTH);
            int lastDay = calendar.getActualMaximum(calendar.DAY_OF_MONTH);
            LogUtil.i("firstDay:" + firstDay);
            LogUtil.i("lastDay:" + lastDay);

            calendar.clear();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, mounth);
            calendar.set(Calendar.DAY_OF_MONTH, firstDay);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            startTime = calendar.getTimeInMillis();

            calendar.clear();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, mounth);
            calendar.set(Calendar.DAY_OF_MONTH, lastDay);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);


            endTime = calendar.getTimeInMillis();

            LogUtil.i("start time:" + startTime + "  , " + DateFormat.getDate(startTime, DateFormat.FORMAT_YYYY_MM_DD_HHMMSS));
            LogUtil.i("end time:" + endTime + "  , " + DateFormat.getDate(endTime, DateFormat.FORMAT_YYYY_MM_DD_HHMMSS));
            LogUtil.i("technicianSpinnerSelectPosition " + technicianSpinnerSelectPosition);
            if (technicianSpinnerSelectPosition == 0) {
                if (citySpinnerSelectPosition == 0) {
                    orders = DbManager.getInstance().queryOrders(startTime, endTime);
                } else {
                    orders = DbManager.getInstance().queryOrders(citys.get(citySpinnerSelectPosition), startTime, endTime);
                }
            } else {

                orders = DbManager.getInstance().queryOrders(citys.get(citySpinnerSelectPosition), startTime, endTime, technicians.get(technicianSpinnerSelectPosition));

            }

            orderListAdapter.setOrders(orders);
            orderListAdapter.notifyDataSetChanged();

        }

    }

}

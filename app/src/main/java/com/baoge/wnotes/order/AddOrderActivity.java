package com.baoge.wnotes.order;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSpinner;

import com.alibaba.fastjson.JSON;
import com.baoge.wnotes.Constants;
import com.baoge.wnotes.R;
import com.baoge.wnotes.base.BaseActivity;
import com.baoge.wnotes.db.Department;
import com.baoge.wnotes.db.Order;
import com.baoge.wnotes.manager.DbManager;
import com.baoge.wnotes.util.CommUtil;
import com.baoge.wnotes.util.DataUtil;
import com.baoge.wnotes.util.DateFormat;
import com.baoge.wnotes.util.LogUtil;
import com.baoge.wnotes.util.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddOrderActivity extends BaseActivity implements View.OnClickListener {
    private AppCompatSpinner citySpinner, technicianSpinner, installerSpinner, deviceSpinner;
    private List<String> citys = null;

    private List<String> technicians = null;
    private List<String> installers = null;
    private List<String> devices = null;

    private ArrayAdapter<String> citySpinnerAdapter;

    private ArrayAdapter<String> technicianSpinnerAdapter;
    private ArrayAdapter<String> installerSpinnerAdapter;
    private ArrayAdapter<String> deviceSpinnerAdapter;


    private int citySpinnerSelectPosition = -1;
    private int technicianSpinnerSelectPosition = -1;
    private int installerSpinnerSelectPosition = -1, deviceSpinnerSelectPosition = -1;


    private TextView orderTime;

    private EditText priceEdt, taxiPriceEdt, partPriceEdt, otherPriceEdt,invoiceEdt,nameEdt;
    private EditText supportEdt, otherTipEdt;

    private final String timeSelectTip = "请选择交易日期";


    private Order editOrder;
    private CheckBox supportCB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);

        init();
        initSpinner();

        switchEditModelIfNecessary(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        switchEditModelIfNecessary(intent);

    }

    /**
     * 编辑模式
     *
     * @param intent
     */
    private void switchEditModelIfNecessary(Intent intent) {
        String editOrderJson = intent.getStringExtra("order");
        if (!TextUtils.isEmpty(editOrderJson)) {
            editOrder = JSON.parseObject(editOrderJson, Order.class);


            ((Button) findViewById(R.id.btn_add)).setText("保存");

            citySpinner.setSelection(CommUtil.getPosition(citys, editOrder.getCity()));
            technicianSpinner.setSelection(CommUtil.getPosition(technicians, editOrder.getTechnician()));
            installerSpinner.setSelection(CommUtil.getPosition(installers, editOrder.getInstaller()));
            deviceSpinner.setSelection(CommUtil.getPosition(devices, editOrder.getDevice()));

            orderTime.setText(DateFormat.getDate(editOrder.getOrderTime(), DateFormat.FORMAT_YYYY_MM_DD));
            priceEdt.setText(editOrder.getTransactionAmount() + "");
            taxiPriceEdt.setText(editOrder.getTaxiFare() + "");
            partPriceEdt.setText(editOrder.getPartPrice() + "");
            supportEdt.setText(editOrder.getSupportPrice() + "");
            invoiceEdt.setText(editOrder.getInvoice() + "");
            nameEdt.setText(editOrder.getName());
            supportCB.setChecked(editOrder.getIsAleadySupport());

            if (!TextUtils.isEmpty(editOrder.getOtherContent())) {
                otherTipEdt.setText(editOrder.getOtherContent());
            }
            if (editOrder.getOtherPrice() > 0) {
                otherPriceEdt.setText(editOrder.getOtherPrice() + "");
            }
        }
    }

    private void init() {
        citySpinner = (AppCompatSpinner) findViewById(R.id.sp_city);
        technicianSpinner = (AppCompatSpinner) findViewById(R.id.sp_technician);
        installerSpinner = (AppCompatSpinner) findViewById(R.id.sp_installer);
        deviceSpinner = (AppCompatSpinner) findViewById(R.id.sp_device);


        orderTime = (TextView) findViewById(R.id.tv_order_time_content);

        priceEdt = (EditText) findViewById(R.id.edt_price);
        taxiPriceEdt = (EditText) findViewById(R.id.edt_taxi_price);
        partPriceEdt = (EditText) findViewById(R.id.edt_part_price);
        supportEdt = (EditText) findViewById(R.id.edt_support);
        otherTipEdt = (EditText) findViewById(R.id.edt_other_tip);
        otherPriceEdt = (EditText) findViewById(R.id.edt_other_price);
        invoiceEdt = (EditText)findViewById(R.id.edt_invoice);
        nameEdt = (EditText)findViewById(R.id.edt_name);

        supportCB = (CheckBox)findViewById(R.id.cb_is_support);
        ((Button) findViewById(R.id.btn_add)).setOnClickListener(this);
        orderTime.setOnClickListener(this);

        orderTime.setText(timeSelectTip);

        supportCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ToastUtil.show("已支持");
                }else {
                    ToastUtil.show("未支持");
                }
            }
        });
    }

    private void initSpinner() {
        citys = DbManager.getInstance().queryCitysName();
        initDeviceSpinner();
        if (citys != null && citys.size() > 0) {

            initCitySpinner();
            initTechnicianSpinner(citys.get(0));
            initStallerpinner(citys.get(0));


        }
    }

    private void initCitySpinner() {
        citySpinnerSelectPosition = 0;
        citySpinnerAdapter = new ArrayAdapter<>(this, R.layout.item_textview, citys);
        citySpinner.setAdapter(citySpinnerAdapter);
        citySpinner.setSelection(0);
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LogUtil.d("city:" + citys.get(position));
                citySpinnerSelectPosition = position;
                //刷新列表


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                LogUtil.d("onNothingSelected");
            }
        });
    }

    private void initTechnicianSpinner(String city) {
        technicianSpinnerSelectPosition = -1;
        technicians = DbManager.getInstance().queryTechnicianNames(city);
        technicianSpinnerAdapter = new ArrayAdapter<>(this, R.layout.item_textview, technicians);
        technicianSpinner.setAdapter(technicianSpinnerAdapter);

        if (technicians != null && technicians.size() > 0) {
            technicianSpinnerSelectPosition = 0;
            technicianSpinner.setSelection(0);
            technicianSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    LogUtil.d("technicians:" + technicians.get(position));
                    technicianSpinnerSelectPosition = position;

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

    private void initStallerpinner(String city) {
        installerSpinnerSelectPosition = -1;
        installers = DbManager.getInstance().queryInstallerNames(city);

        installerSpinnerAdapter = new ArrayAdapter<>(this, R.layout.item_textview, installers);
        installerSpinner.setAdapter(installerSpinnerAdapter);

        if (installers != null && installers.size() > 0) {
            installerSpinnerSelectPosition = 0;
            installerSpinner.setSelection(0);
            installerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    LogUtil.d("installers:" + installers.get(position));
                    installerSpinnerSelectPosition = position;

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    LogUtil.d("onNothingSelected");
                }
            });
        } else {
            installerSpinnerAdapter.clear();
            installerSpinnerAdapter.notifyDataSetChanged();
        }
    }

    private void initDeviceSpinner() {
        deviceSpinnerSelectPosition = -1;
        devices = DbManager.getInstance().queryDeviceNames();
        if (devices != null && devices.size() > 0) {
            deviceSpinnerSelectPosition = 0;
            deviceSpinnerAdapter = new ArrayAdapter<>(this, R.layout.item_textview, devices);
            deviceSpinner.setAdapter(deviceSpinnerAdapter);
            deviceSpinner.setSelection(0);
            deviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    LogUtil.d("devices:" + devices.get(position));
                    deviceSpinnerSelectPosition = position;

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    LogUtil.d("onNothingSelected");
                }
            });
        }
    }


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


            orderTime.setText(year + "-" + monthStr + "-" + dayOfMonthStr);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_order_time_content:
                Calendar ca = Calendar.getInstance();

                int mYear = ca.get(Calendar.YEAR);
                int mMonth = ca.get(Calendar.MONTH);
                int mDay = ca.get(Calendar.DAY_OF_MONTH);
                LogUtil.i("year:" + mYear);
                LogUtil.i("mMonth:" + mMonth);
                LogUtil.i("mDay:" + mDay);
                new DatePickerDialog(AddOrderActivity.this, onDateSetListener, mYear, mMonth, mDay).show();
                break;
            case R.id.btn_add:
                saveOrder();
                break;
        }
    }

    private void saveOrder() {

        if (citySpinnerSelectPosition < 0) {
            ToastUtil.show("请先选择城市");
            return;
        }

        if (technicianSpinnerSelectPosition < 0) {
            ToastUtil.show("请先选择技师");
            return;
        }
        if (installerSpinnerSelectPosition < 0) {
            ToastUtil.show("请先选择装机师");
            return;
        }
        if (deviceSpinnerSelectPosition < 0) {
            ToastUtil.show("请先选择设备");
            return;
        }

        String name = nameEdt.getText().toString();

        if (TextUtils.isEmpty(name)) {
            ToastUtil.show("请先输入姓名");
            return;
        }

        if (TextUtils.isEmpty(priceEdt.getText().toString())) {
            ToastUtil.show("请先输入金额");
            return;
        }

        if (TextUtils.equals(timeSelectTip, orderTime.getText().toString())) {
            ToastUtil.show(timeSelectTip);
            return;
        }
        Order order = null;
        if (editOrder != null) {
            order = editOrder;
        } else {
            order = new Order();
        }

        order.setCity(citys.get(citySpinnerSelectPosition));
        order.setHospital("");
        order.setDepartMent("");
        order.setTechnician(technicians.get(technicianSpinnerSelectPosition));
        order.setInstaller(installers.get(installerSpinnerSelectPosition));
        order.setDevice(devices.get(deviceSpinnerSelectPosition));
        order.setOrderAddTime(System.currentTimeMillis());
        order.setInstallPrice(DataUtil.getInstallPrice(citys.get(citySpinnerSelectPosition)));
        order.setOrderTime(DateFormat.getDate(orderTime.getText().toString(), DateFormat.FORMAT_YYYY_MM_DD));
        order.setTransactionAmount(Integer.parseInt(priceEdt.getText().toString()));
        order.setName(nameEdt.getText().toString());
        order.setIsAleadySupport(supportCB.isChecked());

        int invoice = 0;
        if(!TextUtils.isEmpty(invoiceEdt.getText().toString())){
            invoice = Integer.parseInt(invoiceEdt.getText().toString());
            order.setInvoice(invoice);
        }

        String taxiPrice = taxiPriceEdt.getText().toString();
        if (!TextUtils.isEmpty(taxiPrice)) {
            order.setTaxiFare(Integer.parseInt(taxiPrice));
        }

        String partPrice = partPriceEdt.getText().toString();
        if (!TextUtils.isEmpty(partPrice)) {
            order.setPartPrice(Integer.parseInt(partPrice));
        }

        String supportPrice = supportEdt.getText().toString();
        if (!TextUtils.isEmpty(supportPrice)) {
            order.setSupportPrice(Integer.parseInt(supportPrice));
        }

        String otherTip = otherTipEdt.getText().toString();
        String otherPrice = otherPriceEdt.getText().toString();
        if (!TextUtils.isEmpty(otherTip) && !TextUtils.isEmpty(otherPrice)) {
            order.setOtherContent(otherTip);
            order.setOtherPrice(Integer.parseInt(otherPrice));
        } else if (TextUtils.isEmpty(otherTip) && TextUtils.isEmpty(otherPrice)) {

        } else {
            ToastUtil.show("其他内容需要保持一致，都要填");
            return;
        }

        int profit = Integer.parseInt(priceEdt.getText().toString()) - DbManager.getInstance().queryDevicePrice(devices.get(deviceSpinnerSelectPosition))-invoice;

        if (!TextUtils.isEmpty(taxiPriceEdt.getText().toString())) {
            profit = profit - Integer.parseInt(taxiPriceEdt.getText().toString());
        }

        if (!TextUtils.isEmpty(partPriceEdt.getText().toString())) {
            profit = profit - Integer.parseInt(partPriceEdt.getText().toString());
        }

        if (!TextUtils.isEmpty(supportEdt.getText().toString())) {
            profit = profit - Integer.parseInt(supportEdt.getText().toString());
        }


        if (!TextUtils.isEmpty(otherPriceEdt.getText().toString())) {
            profit = profit - Integer.parseInt(otherPriceEdt.getText().toString());
        }
        if (profit < 0) {
            ToastUtil.show("利润为负数,请检查价格是否正确");
            return;
        }

        List<EditText> editTexts = new ArrayList<>();
        editTexts.add(priceEdt);
        editTexts.add(taxiPriceEdt);
        editTexts.add(partPriceEdt);
        editTexts.add(otherPriceEdt);
        editTexts.add(supportEdt);
        editTexts.add(otherTipEdt);
        clearEditText(editTexts);


        if (editOrder != null) {
            order.setType(Constants.OrderType.EDIT);
        } else {
            order.setType(Constants.OrderType.NEW);
        }
        boolean result = DbManager.getInstance().insertOrder(order);
        if (result) {
            if (editOrder != null) {
                ToastUtil.show("保存成功");
                finish();
            } else {
                ToastUtil.show("添加成功");
            }
        } else {
            if (editOrder != null) {
                ToastUtil.show("保存失败");
            } else {
                ToastUtil.show("添加失败");
            }
        }

    }


}

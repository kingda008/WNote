package com.baoge.wnotes.addinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.baoge.wnotes.R;
import com.baoge.wnotes.adapter.DeviceAdapter;
import com.baoge.wnotes.adapter.HospitalAdapter;
import com.baoge.wnotes.adapter.TextViewAdapter;
import com.baoge.wnotes.base.BaseActivity;
import com.baoge.wnotes.db.Device;
import com.baoge.wnotes.manager.DbManager;
import com.baoge.wnotes.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class AddDevicesActivity extends BaseActivity implements View.OnClickListener {


    private RecyclerView recyclerView;

    private EditText nameEdt, priceEdt;
    private DeviceAdapter adapter;
    private List<Device> devices = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_devices);
        init();

        updateDevices();
    }

    private void init() {
        adapter = new DeviceAdapter();
        priceEdt = (EditText) findViewById(R.id.edt_price);
        nameEdt = (EditText) findViewById(R.id.edt_name);
        recyclerView = (RecyclerView) findViewById(R.id.rcv);

        ((Button) findViewById(R.id.btn_add)).setOnClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        adapter.setOnItemLongClickLitener(new DeviceAdapter.OnItemLongClickLitener() {
            @Override
            public void onItemLongClick(View view, final int position) {
                showDeleteDialog(new OnDeleteLitener() {
                    @Override
                    public void doDelete() {
                        DbManager.getInstance().deleteDevice(devices.get(position));
                        updateDevices();
                    }
                });
            }
        });

    }

    private void updateDevices() {
        devices = DbManager.getInstance().queryDevices();

        adapter.setDatas(devices);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:


                if (TextUtils.isEmpty(nameEdt.getText().toString())) {
                    ToastUtil.show("请先输入设备名称");
                    return;
                }
                if (TextUtils.isEmpty(priceEdt.getText().toString())) {
                    ToastUtil.show("请先输入设备价格");
                    return;
                }

                Device device = new Device();

                device.setName(nameEdt.getText().toString());
                device.setPrice(Integer.parseInt(priceEdt.getText().toString()));


                if (DbManager.getInstance().isDeviceExit(device)) {
                    ToastUtil.show("不要重复添加");
                    return;
                }
                boolean result = DbManager.getInstance().insertDevice(device);
                if (result) {
                    updateDevices();
                    ToastUtil.show("添加成功");
                } else {
                    ToastUtil.show("添加失败");
                }

                clearEditText(nameEdt);
                clearEditText(priceEdt);
                break;
        }
    }
}

package com.baoge.wnotes.addinfo;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.baoge.wnotes.R;
import com.baoge.wnotes.adapter.HospitalAdapter;
import com.baoge.wnotes.adapter.TextViewAdapter;
import com.baoge.wnotes.base.BaseActivity;
import com.baoge.wnotes.db.Hospital;
import com.baoge.wnotes.manager.DbManager;
import com.baoge.wnotes.util.LogUtil;
import com.baoge.wnotes.util.ToastUtil;

import java.util.List;

public class AddHostpitalActivity extends BaseActivity implements View.OnClickListener {
    private AppCompatSpinner spinner;
    private List<String> citys = null;
    private List<String> hospitals = null;

    private ArrayAdapter<String> citySpinnerAdapter;
    private int citySpinnerSelectPosition = -1;
    private RecyclerView recyclerView;
    private HospitalAdapter hospitalAdapter;
    private EditText hospitalEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hostpital);

        init();

        if (citys != null && citys.size() > 0) {
            initCityAdapter();

            updateHospitals(citys.get(0));
        }


    }


    private void init() {
        citys = DbManager.getInstance().queryCitysName();

        spinner = (AppCompatSpinner) findViewById(R.id.sp_city);
        recyclerView = (RecyclerView) findViewById(R.id.rcv_hospital);
        citySpinnerAdapter = new ArrayAdapter<>(this, R.layout.item_textview, citys);


        hospitalEdt = (EditText) findViewById(R.id.edt_city_hospital);

        hospitalAdapter = new HospitalAdapter();
        ((Button) findViewById(R.id.btn_add)).setOnClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        hospitalAdapter.setOnItemLongClickLitener(new TextViewAdapter.OnItemLongClickLitener() {
            @Override
            public void onItemLongClick(View view, final int position) {
                showDeleteDialog(new OnDeleteLitener() {
                    @Override
                    public void doDelete() {
                        DbManager.getInstance().deleteHospital(citys.get(citySpinnerSelectPosition),hospitals.get(position));
                        updateHospitals(citys.get(citySpinnerSelectPosition));
                    }
                });
            }
        });

    }

    private void initCityAdapter() {
        citySpinnerSelectPosition = 0;
        spinner.setAdapter(citySpinnerAdapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LogUtil.d("city:" + citys.get(position));
                citySpinnerSelectPosition = position;

                //刷新列表

                updateHospitals(citys.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                LogUtil.d("onNothingSelected");
            }
        });
    }

    private void updateHospitals(String city) {

        hospitals = DbManager.getInstance().queryHospitalNames(city);
        print(hospitals);
        hospitalAdapter.setDatas(hospitals);
        recyclerView.setAdapter(hospitalAdapter);
        hospitalAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                if (citySpinnerSelectPosition < 0) {
                    ToastUtil.show("请先选择城市");
                    return;
                }

                if (TextUtils.isEmpty(hospitalEdt.getText().toString())) {
                    ToastUtil.show("请先输入医院名称");
                    return;
                }


                Hospital hospital = new Hospital();
                hospital.setCity(citys.get(citySpinnerSelectPosition));
                hospital.setName(hospitalEdt.getText().toString());
                if (DbManager.getInstance().isHospitalExit(hospital)) {
                    ToastUtil.show("不要重复添加");
                    return;
                }
                boolean result = DbManager.getInstance().insertHospital(hospital);
                if (result) {
                    updateHospitals(citys.get(citySpinnerSelectPosition));
                    ToastUtil.show("添加成功");
                } else {
                    ToastUtil.show("添加失败");
                }


                clearEditText(hospitalEdt);
                break;
        }
    }
}

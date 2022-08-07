package com.baoge.wnotes.addinfo;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baoge.wnotes.R;
import com.baoge.wnotes.adapter.DepartMentAdapter;
import com.baoge.wnotes.adapter.TextViewAdapter;
import com.baoge.wnotes.base.BaseActivity;
import com.baoge.wnotes.db.Department;
import com.baoge.wnotes.manager.DbManager;
import com.baoge.wnotes.util.LogUtil;
import com.baoge.wnotes.util.ToastUtil;

import java.util.List;

public class AddDepartMentActivity extends BaseActivity implements View.OnClickListener {
    private AppCompatSpinner citySpinner, hospitalSpinner;
    private List<String> citys = null;
    private List<String> hospitals = null;
    private List<String> departments = null;

    private ArrayAdapter<String> citySpinnerAdapter;
    private ArrayAdapter<String> hospitalSpinnerAdapter;

    private int citySpinnerSelectPosition = -1, hospitalSpinnerSelectPosition = -1;
    private RecyclerView recyclerView;
    private DepartMentAdapter departMentAdapter;
    private EditText departmengEdt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_depart_ment);
        init();

        citys = DbManager.getInstance().queryCitysName();

        if (citys != null && citys.size() > 0) {
            initCitySpinner();
            hospitals = DbManager.getInstance().queryHospitalNames(citys.get(0));


            if (hospitals != null && hospitals.size() > 0) {

                initHospitalSpinner(citys.get(0));
                departments = DbManager.getInstance().queryDepartmentNames(citys.get(0), hospitals.get(0));

                if (departments != null && departments.size() > 0) {
                    updateDepartMent(citys.get(0), hospitals.get(0));
                }
            }

        }
    }

    private void init() {


        citySpinner = (AppCompatSpinner) findViewById(R.id.sp_city);
        hospitalSpinner = (AppCompatSpinner) findViewById(R.id.sp_hospital);
        recyclerView = (RecyclerView) findViewById(R.id.rcv_departmeng);
        citySpinnerAdapter = new ArrayAdapter<>(this, R.layout.item_textview, citys);
        hospitalSpinnerAdapter = new ArrayAdapter<>(this, R.layout.item_textview, hospitals);

        departmengEdt = (EditText) findViewById(R.id.edt_city_department);

        departMentAdapter = new DepartMentAdapter();
        ((Button) findViewById(R.id.btn_add)).setOnClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        departMentAdapter.setOnItemLongClickLitener(new TextViewAdapter.OnItemLongClickLitener() {
            @Override
            public void onItemLongClick(View view, final int position) {
                showDeleteDialog(new OnDeleteLitener() {
                    @Override
                    public void doDelete() {
                        DbManager.getInstance().deleteDepartMent(citys.get(citySpinnerSelectPosition),hospitals.get(hospitalSpinnerSelectPosition),departments.get(position));
                        updateDepartMent(citys.get(citySpinnerSelectPosition),hospitals.get(hospitalSpinnerSelectPosition));
                    }
                });
            }
        });

    }

    private void initCitySpinner() {
        citySpinnerSelectPosition=0;
        citySpinnerAdapter = new ArrayAdapter<>(this, R.layout.item_textview, citys);
        citySpinner.setAdapter(citySpinnerAdapter);
        citySpinner.setSelection(0);
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LogUtil.d("city:" + citys.get(position));
                citySpinnerSelectPosition = position;
                //刷新列表

                initHospitalSpinner(citys.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                LogUtil.d("onNothingSelected");
            }
        });
    }

    private void initHospitalSpinner(String cityName) {
        LogUtil.d("initHospitalSpinner cityName:" + cityName);
        hospitalSpinnerSelectPosition = -1;
        //先更新数据
        hospitals = DbManager.getInstance().queryHospitalNames(cityName);
        if (hospitals != null && hospitals.size() > 0) {
            hospitalSpinnerSelectPosition = 0;
            hospitalSpinnerAdapter = new ArrayAdapter<>(this, R.layout.item_textview, hospitals);

            hospitalSpinner.setAdapter(hospitalSpinnerAdapter);
            hospitalSpinner.setSelection(0);
            hospitalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    LogUtil.d("hospital:" + hospitals.get(position));
                    hospitalSpinnerSelectPosition = position;
                    //刷新列表

                    updateDepartMent(citys.get(citySpinnerSelectPosition), hospitals.get(hospitalSpinnerSelectPosition));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    LogUtil.d("onNothingSelected");
                }
            });
        }else {
            hospitalSpinnerAdapter.clear();
            hospitalSpinnerAdapter.notifyDataSetChanged();
        }
    }


    private void updateDepartMent(String city, String hospital) {
        if (TextUtils.isEmpty(city) || TextUtils.isEmpty(hospital)) {
            return;
        }
        departments = DbManager.getInstance().queryDepartmentNames(city, hospital);
        print(hospitals);
        departMentAdapter.setDatas(departments);
        recyclerView.setAdapter(departMentAdapter);
        departMentAdapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                if (citySpinnerSelectPosition < 0) {
                    ToastUtil.show("请先选择城市");
                    return;
                }
                if (hospitalSpinnerSelectPosition < 0) {
                    ToastUtil.show("请先选择医院");
                    return;
                }


                if (TextUtils.isEmpty(departmengEdt.getText().toString())) {
                    ToastUtil.show("请先输入科室名称");
                    return;
                }


                Department department = new Department();
                department.setCity(citys.get(citySpinnerSelectPosition));
                department.setHospital(hospitals.get(hospitalSpinnerSelectPosition));
                department.setName(departmengEdt.getText().toString());
                if (DbManager.getInstance().isDepartmentExit(department)) {
                    ToastUtil.show("不要重复添加");
                    return;
                }
                boolean result = DbManager.getInstance().insertDepartment(department);
                if (result) {
                    updateDepartMent(citys.get(citySpinnerSelectPosition), hospitals.get(hospitalSpinnerSelectPosition));
                    ToastUtil.show("添加成功");
                } else {
                    ToastUtil.show("添加失败");
                }

                clearEditText(departmengEdt);

                break;
        }
    }
}

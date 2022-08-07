package com.baoge.wnotes.addinfo;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baoge.wnotes.R;
import com.baoge.wnotes.adapter.CitysAdapter;
import com.baoge.wnotes.adapter.TextViewAdapter;
import com.baoge.wnotes.base.BaseActivity;
import com.baoge.wnotes.manager.DbManager;
import com.baoge.wnotes.util.ToastUtil;

import java.util.List;

public class AddCityActivity extends BaseActivity implements View.OnClickListener {
    private List<String> citys = null;
    private RecyclerView recyclerView;
    private CitysAdapter adapter;
    private EditText cityEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);

        init();

        updateCitys();
    }

    private void updateCitys() {

        citys = DbManager.getInstance().queryCitysName();
        adapter.setDatas(citys);
        recyclerView.setAdapter(adapter);
    }

    private void init() {
        adapter = new CitysAdapter();

        recyclerView = (RecyclerView) findViewById(R.id.rcv_city);
        cityEdt = (EditText) findViewById(R.id.edt_city_name);
        ((Button) findViewById(R.id.btn_add_city)).setOnClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter.setOnItemLongClickLitener(new TextViewAdapter.OnItemLongClickLitener() {
            @Override
            public void onItemLongClick(View view, final int position) {
                showDeleteDialog(new OnDeleteLitener() {
                    @Override
                    public void doDelete() {
                        DbManager.getInstance().deleteCitys(citys.get(position));
                        updateCitys();
                    }
                });
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_city:
                if (TextUtils.isEmpty(cityEdt.getText().toString())) {
                    ToastUtil.show("请输入城市名");
                } else {
                    if (DbManager.getInstance().isCityExit(cityEdt.getText().toString())) {
                        ToastUtil.show("不要重复添加");
                    } else {
                        boolean result = DbManager.getInstance().insertCity(cityEdt.getText().toString());
                        if (result) {
                            updateCitys();
                            ToastUtil.show("添加成功");
                        } else {
                            ToastUtil.show("添加失败");
                        }
                    }
                }
                clearEditText(cityEdt);
                break;
        }
    }
}

package com.baoge.wnotes.addinfo;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baoge.wnotes.R;
import com.baoge.wnotes.base.BaseActivity;

public class AddInfoActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);


        init();
    }

    private void init() {
        ((TextView) findViewById(R.id.add_hospital)).setOnClickListener(this);
        ((TextView) findViewById(R.id.add_department)).setOnClickListener(this);
        ((TextView) findViewById(R.id.add_technician)).setOnClickListener(this);
        ((TextView) findViewById(R.id.add_installer)).setOnClickListener(this);
        ((TextView) findViewById(R.id.add_devices)).setOnClickListener(this);
        ((TextView) findViewById(R.id.add_city)).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_city:
                startActivity(AddInfoActivity.this, AddCityActivity.class);
                break;
            case R.id.add_hospital:
                startActivity(AddInfoActivity.this, AddHostpitalActivity.class);
                break;
            case R.id.add_department:
                startActivity(AddInfoActivity.this, AddDepartMentActivity.class);
                break;
            case R.id.add_technician:
                startActivity(AddInfoActivity.this, AddTechnicianActivity.class);
                break;
            case R.id.add_installer:
                startActivity(AddInfoActivity.this, AddInstallerActivity.class);
                break;
            case R.id.add_devices:
                startActivity(AddInfoActivity.this, AddDevicesActivity.class);
                break;

        }
    }
}

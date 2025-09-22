package com.baoge.wnotes.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.baoge.wnotes.dao.DaoMaster;
import com.baoge.wnotes.dao.OrderDao;
import com.baoge.wnotes.util.LogUtil;
import com.github.yuweiguocn.library.greendao.MigrationHelper;

import org.greenrobot.greendao.database.Database;

public class DaoOpenHelper extends DaoMaster.OpenHelper {
    public DaoOpenHelper(Context context, String name) {
        super(context, name);
    }

    public DaoOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        LogUtil.i("onUpgrade Database oldVersion " + oldVersion + " newVersion " + newVersion);
        MigrationHelper.migrate(db,  OrderDao.class);
    }




}

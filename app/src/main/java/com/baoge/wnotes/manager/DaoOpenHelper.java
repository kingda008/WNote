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
//        MigrationHelper.migrate(db,  OrderDao.class);

        if(oldVersion < 21) {
            try {
                LogUtil.i("beginTransaction");
                db.beginTransaction();



                db.execSQL("ALTER TABLE \"order\" ADD COLUMN PATIENT_NAME TEXT");
                db.execSQL("ALTER TABLE \"order\" ADD COLUMN PATIENT_ID TEXT");
                db.execSQL("ALTER TABLE \"order\" ADD COLUMN PATIENT_PHONE TEXT");
                db.setTransactionSuccessful();
                LogUtil.i("setTransactionSuccessful");
            } finally {
                LogUtil.i("endTransaction");
                db.endTransaction();
            }
        }
    }




}

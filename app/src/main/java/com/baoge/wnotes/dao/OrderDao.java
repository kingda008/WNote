package com.baoge.wnotes.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.baoge.wnotes.db.Order;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "order".
*/
public class OrderDao extends AbstractDao<Order, Long> {

    public static final String TABLENAME = "order";

    /**
     * Properties of entity Order.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property OrderAddTime = new Property(1, long.class, "orderAddTime", false, "ORDER_ADD_TIME");
        public final static Property OrderTime = new Property(2, long.class, "orderTime", false, "ORDER_TIME");
        public final static Property City = new Property(3, String.class, "city", false, "CITY");
        public final static Property Hospital = new Property(4, String.class, "hospital", false, "HOSPITAL");
        public final static Property DepartMent = new Property(5, String.class, "departMent", false, "DEPART_MENT");
        public final static Property Technician = new Property(6, String.class, "technician", false, "TECHNICIAN");
        public final static Property Installer = new Property(7, String.class, "installer", false, "INSTALLER");
        public final static Property Device = new Property(8, String.class, "device", false, "DEVICE");
        public final static Property TransactionAmount = new Property(9, int.class, "transactionAmount", false, "TRANSACTION_AMOUNT");
        public final static Property TaxiFare = new Property(10, int.class, "taxiFare", false, "TAXI_FARE");
        public final static Property PartPrice = new Property(11, int.class, "partPrice", false, "PART_PRICE");
        public final static Property InstallPrice = new Property(12, int.class, "installPrice", false, "INSTALL_PRICE");
        public final static Property SupportPrice = new Property(13, int.class, "supportPrice", false, "SUPPORT_PRICE");
        public final static Property OtherContent = new Property(14, String.class, "otherContent", false, "OTHER_CONTENT");
        public final static Property OtherPrice = new Property(15, int.class, "otherPrice", false, "OTHER_PRICE");
        public final static Property Invoice = new Property(16, int.class, "invoice", false, "INVOICE");
        public final static Property Name = new Property(17, String.class, "name", false, "NAME");
        public final static Property IsAleadySupport = new Property(18, boolean.class, "isAleadySupport", false, "IS_ALEADY_SUPPORT");
        public final static Property Type = new Property(19, int.class, "type", false, "TYPE");
    }


    public OrderDao(DaoConfig config) {
        super(config);
    }
    
    public OrderDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"order\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"ORDER_ADD_TIME\" INTEGER NOT NULL ," + // 1: orderAddTime
                "\"ORDER_TIME\" INTEGER NOT NULL ," + // 2: orderTime
                "\"CITY\" TEXT," + // 3: city
                "\"HOSPITAL\" TEXT," + // 4: hospital
                "\"DEPART_MENT\" TEXT," + // 5: departMent
                "\"TECHNICIAN\" TEXT," + // 6: technician
                "\"INSTALLER\" TEXT," + // 7: installer
                "\"DEVICE\" TEXT," + // 8: device
                "\"TRANSACTION_AMOUNT\" INTEGER NOT NULL ," + // 9: transactionAmount
                "\"TAXI_FARE\" INTEGER NOT NULL ," + // 10: taxiFare
                "\"PART_PRICE\" INTEGER NOT NULL ," + // 11: partPrice
                "\"INSTALL_PRICE\" INTEGER NOT NULL ," + // 12: installPrice
                "\"SUPPORT_PRICE\" INTEGER NOT NULL ," + // 13: supportPrice
                "\"OTHER_CONTENT\" TEXT," + // 14: otherContent
                "\"OTHER_PRICE\" INTEGER NOT NULL ," + // 15: otherPrice
                "\"INVOICE\" INTEGER NOT NULL ," + // 16: invoice
                "\"NAME\" TEXT," + // 17: name
                "\"IS_ALEADY_SUPPORT\" INTEGER NOT NULL ," + // 18: isAleadySupport
                "\"TYPE\" INTEGER NOT NULL );"); // 19: type
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"order\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Order entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getOrderAddTime());
        stmt.bindLong(3, entity.getOrderTime());
 
        String city = entity.getCity();
        if (city != null) {
            stmt.bindString(4, city);
        }
 
        String hospital = entity.getHospital();
        if (hospital != null) {
            stmt.bindString(5, hospital);
        }
 
        String departMent = entity.getDepartMent();
        if (departMent != null) {
            stmt.bindString(6, departMent);
        }
 
        String technician = entity.getTechnician();
        if (technician != null) {
            stmt.bindString(7, technician);
        }
 
        String installer = entity.getInstaller();
        if (installer != null) {
            stmt.bindString(8, installer);
        }
 
        String device = entity.getDevice();
        if (device != null) {
            stmt.bindString(9, device);
        }
        stmt.bindLong(10, entity.getTransactionAmount());
        stmt.bindLong(11, entity.getTaxiFare());
        stmt.bindLong(12, entity.getPartPrice());
        stmt.bindLong(13, entity.getInstallPrice());
        stmt.bindLong(14, entity.getSupportPrice());
 
        String otherContent = entity.getOtherContent();
        if (otherContent != null) {
            stmt.bindString(15, otherContent);
        }
        stmt.bindLong(16, entity.getOtherPrice());
        stmt.bindLong(17, entity.getInvoice());
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(18, name);
        }
        stmt.bindLong(19, entity.getIsAleadySupport() ? 1L: 0L);
        stmt.bindLong(20, entity.getType());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Order entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getOrderAddTime());
        stmt.bindLong(3, entity.getOrderTime());
 
        String city = entity.getCity();
        if (city != null) {
            stmt.bindString(4, city);
        }
 
        String hospital = entity.getHospital();
        if (hospital != null) {
            stmt.bindString(5, hospital);
        }
 
        String departMent = entity.getDepartMent();
        if (departMent != null) {
            stmt.bindString(6, departMent);
        }
 
        String technician = entity.getTechnician();
        if (technician != null) {
            stmt.bindString(7, technician);
        }
 
        String installer = entity.getInstaller();
        if (installer != null) {
            stmt.bindString(8, installer);
        }
 
        String device = entity.getDevice();
        if (device != null) {
            stmt.bindString(9, device);
        }
        stmt.bindLong(10, entity.getTransactionAmount());
        stmt.bindLong(11, entity.getTaxiFare());
        stmt.bindLong(12, entity.getPartPrice());
        stmt.bindLong(13, entity.getInstallPrice());
        stmt.bindLong(14, entity.getSupportPrice());
 
        String otherContent = entity.getOtherContent();
        if (otherContent != null) {
            stmt.bindString(15, otherContent);
        }
        stmt.bindLong(16, entity.getOtherPrice());
        stmt.bindLong(17, entity.getInvoice());
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(18, name);
        }
        stmt.bindLong(19, entity.getIsAleadySupport() ? 1L: 0L);
        stmt.bindLong(20, entity.getType());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Order readEntity(Cursor cursor, int offset) {
        Order entity = new Order( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getLong(offset + 1), // orderAddTime
            cursor.getLong(offset + 2), // orderTime
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // city
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // hospital
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // departMent
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // technician
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // installer
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // device
            cursor.getInt(offset + 9), // transactionAmount
            cursor.getInt(offset + 10), // taxiFare
            cursor.getInt(offset + 11), // partPrice
            cursor.getInt(offset + 12), // installPrice
            cursor.getInt(offset + 13), // supportPrice
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // otherContent
            cursor.getInt(offset + 15), // otherPrice
            cursor.getInt(offset + 16), // invoice
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // name
            cursor.getShort(offset + 18) != 0, // isAleadySupport
            cursor.getInt(offset + 19) // type
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Order entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setOrderAddTime(cursor.getLong(offset + 1));
        entity.setOrderTime(cursor.getLong(offset + 2));
        entity.setCity(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setHospital(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setDepartMent(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setTechnician(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setInstaller(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setDevice(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setTransactionAmount(cursor.getInt(offset + 9));
        entity.setTaxiFare(cursor.getInt(offset + 10));
        entity.setPartPrice(cursor.getInt(offset + 11));
        entity.setInstallPrice(cursor.getInt(offset + 12));
        entity.setSupportPrice(cursor.getInt(offset + 13));
        entity.setOtherContent(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setOtherPrice(cursor.getInt(offset + 15));
        entity.setInvoice(cursor.getInt(offset + 16));
        entity.setName(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setIsAleadySupport(cursor.getShort(offset + 18) != 0);
        entity.setType(cursor.getInt(offset + 19));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Order entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Order entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Order entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}

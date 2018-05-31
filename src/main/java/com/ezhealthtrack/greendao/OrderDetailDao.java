package com.ezhealthtrack.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.ezhealthtrack.greendao.OrderDetail;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table ORDER_DETAIL.
*/
public class OrderDetailDao extends AbstractDao<OrderDetail, Long> {

    public static final String TABLENAME = "ORDER_DETAIL";

    /**
     * Properties of entity OrderDetail.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Order_id = new Property(1, Long.class, "order_id", false, "ORDER_ID");
        public final static Property Technician_id = new Property(2, String.class, "technician_id", false, "TECHNICIAN_ID");
        public final static Property Doctor = new Property(3, String.class, "doctor", false, "DOCTOR");
        public final static Property Appointment_id = new Property(4, String.class, "appointment_id", false, "APPOINTMENT_ID");
        public final static Property Episode_id = new Property(5, String.class, "episode_id", false, "EPISODE_ID");
        public final static Property Order_notes = new Property(6, String.class, "order_notes", false, "ORDER_NOTES");
        public final static Property Sampling_notes = new Property(7, String.class, "sampling_notes", false, "SAMPLING_NOTES");
        public final static Property Sampling_on = new Property(8, String.class, "sampling_on", false, "SAMPLING_ON");
        public final static Property Sampling_done_notes = new Property(9, String.class, "sampling_done_notes", false, "SAMPLING_DONE_NOTES");
        public final static Property Sampling_done_on = new Property(10, String.class, "sampling_done_on", false, "SAMPLING_DONE_ON");
        public final static Property Refund_status = new Property(11, String.class, "refund_status", false, "REFUND_STATUS");
        public final static Property Publish_report = new Property(12, String.class, "publish_report", false, "PUBLISH_REPORT");
        public final static Property Cancel_action = new Property(13, String.class, "cancel_action", false, "CANCEL_ACTION");
    };


    public OrderDetailDao(DaoConfig config) {
        super(config);
    }
    
    public OrderDetailDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ORDER_DETAIL' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'ORDER_ID' INTEGER," + // 1: order_id
                "'TECHNICIAN_ID' TEXT," + // 2: technician_id
                "'DOCTOR' TEXT," + // 3: doctor
                "'APPOINTMENT_ID' TEXT," + // 4: appointment_id
                "'EPISODE_ID' TEXT," + // 5: episode_id
                "'ORDER_NOTES' TEXT," + // 6: order_notes
                "'SAMPLING_NOTES' TEXT," + // 7: sampling_notes
                "'SAMPLING_ON' TEXT," + // 8: sampling_on
                "'SAMPLING_DONE_NOTES' TEXT," + // 9: sampling_done_notes
                "'SAMPLING_DONE_ON' TEXT," + // 10: sampling_done_on
                "'REFUND_STATUS' TEXT," + // 11: refund_status
                "'PUBLISH_REPORT' TEXT," + // 12: publish_report
                "'CANCEL_ACTION' TEXT);"); // 13: cancel_action
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ORDER_DETAIL'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, OrderDetail entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long order_id = entity.getOrder_id();
        if (order_id != null) {
            stmt.bindLong(2, order_id);
        }
 
        String technician_id = entity.getTechnician_id();
        if (technician_id != null) {
            stmt.bindString(3, technician_id);
        }
 
        String doctor = entity.getDoctor();
        if (doctor != null) {
            stmt.bindString(4, doctor);
        }
 
        String appointment_id = entity.getAppointment_id();
        if (appointment_id != null) {
            stmt.bindString(5, appointment_id);
        }
 
        String episode_id = entity.getEpisode_id();
        if (episode_id != null) {
            stmt.bindString(6, episode_id);
        }
 
        String order_notes = entity.getOrder_notes();
        if (order_notes != null) {
            stmt.bindString(7, order_notes);
        }
 
        String sampling_notes = entity.getSampling_notes();
        if (sampling_notes != null) {
            stmt.bindString(8, sampling_notes);
        }
 
        String sampling_on = entity.getSampling_on();
        if (sampling_on != null) {
            stmt.bindString(9, sampling_on);
        }
 
        String sampling_done_notes = entity.getSampling_done_notes();
        if (sampling_done_notes != null) {
            stmt.bindString(10, sampling_done_notes);
        }
 
        String sampling_done_on = entity.getSampling_done_on();
        if (sampling_done_on != null) {
            stmt.bindString(11, sampling_done_on);
        }
 
        String refund_status = entity.getRefund_status();
        if (refund_status != null) {
            stmt.bindString(12, refund_status);
        }
 
        String publish_report = entity.getPublish_report();
        if (publish_report != null) {
            stmt.bindString(13, publish_report);
        }
 
        String cancel_action = entity.getCancel_action();
        if (cancel_action != null) {
            stmt.bindString(14, cancel_action);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public OrderDetail readEntity(Cursor cursor, int offset) {
        OrderDetail entity = new OrderDetail( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // order_id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // technician_id
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // doctor
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // appointment_id
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // episode_id
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // order_notes
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // sampling_notes
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // sampling_on
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // sampling_done_notes
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // sampling_done_on
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // refund_status
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // publish_report
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13) // cancel_action
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, OrderDetail entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setOrder_id(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setTechnician_id(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setDoctor(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setAppointment_id(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setEpisode_id(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setOrder_notes(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setSampling_notes(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setSampling_on(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setSampling_done_notes(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setSampling_done_on(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setRefund_status(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setPublish_report(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setCancel_action(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(OrderDetail entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(OrderDetail entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
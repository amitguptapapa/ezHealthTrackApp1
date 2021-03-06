package com.ezhealthtrack.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.ezhealthtrack.greendao.Area;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table AREA.
*/
public class AreaDao extends AbstractDao<Area, Long> {

    public static final String TABLENAME = "AREA";

    /**
     * Properties of entity Area.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property AREA_ID = new Property(1, String.class, "AREA_ID", false, "AREA__ID");
        public final static Property AREA_NAME = new Property(2, String.class, "AREA_NAME", false, "AREA__NAME");
        public final static Property CITY_ID = new Property(3, String.class, "CITY_ID", false, "CITY__ID");
        public final static Property STATE_ID = new Property(4, String.class, "STATE_ID", false, "STATE__ID");
        public final static Property COUNTRY_ID = new Property(5, String.class, "COUNTRY_ID", false, "COUNTRY__ID");
        public final static Property DATE_UPDATED = new Property(6, String.class, "DATE_UPDATED", false, "DATE__UPDATED");
        public final static Property DATE_CREATED = new Property(7, String.class, "DATE_CREATED", false, "DATE__CREATED");
    };


    public AreaDao(DaoConfig config) {
        super(config);
    }
    
    public AreaDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'AREA' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'AREA__ID' TEXT," + // 1: AREA_ID
                "'AREA__NAME' TEXT," + // 2: AREA_NAME
                "'CITY__ID' TEXT," + // 3: CITY_ID
                "'STATE__ID' TEXT," + // 4: STATE_ID
                "'COUNTRY__ID' TEXT," + // 5: COUNTRY_ID
                "'DATE__UPDATED' TEXT," + // 6: DATE_UPDATED
                "'DATE__CREATED' TEXT);"); // 7: DATE_CREATED
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'AREA'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Area entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String AREA_ID = entity.getAREA_ID();
        if (AREA_ID != null) {
            stmt.bindString(2, AREA_ID);
        }
 
        String AREA_NAME = entity.getAREA_NAME();
        if (AREA_NAME != null) {
            stmt.bindString(3, AREA_NAME);
        }
 
        String CITY_ID = entity.getCITY_ID();
        if (CITY_ID != null) {
            stmt.bindString(4, CITY_ID);
        }
 
        String STATE_ID = entity.getSTATE_ID();
        if (STATE_ID != null) {
            stmt.bindString(5, STATE_ID);
        }
 
        String COUNTRY_ID = entity.getCOUNTRY_ID();
        if (COUNTRY_ID != null) {
            stmt.bindString(6, COUNTRY_ID);
        }
 
        String DATE_UPDATED = entity.getDATE_UPDATED();
        if (DATE_UPDATED != null) {
            stmt.bindString(7, DATE_UPDATED);
        }
 
        String DATE_CREATED = entity.getDATE_CREATED();
        if (DATE_CREATED != null) {
            stmt.bindString(8, DATE_CREATED);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Area readEntity(Cursor cursor, int offset) {
        Area entity = new Area( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // AREA_ID
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // AREA_NAME
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // CITY_ID
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // STATE_ID
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // COUNTRY_ID
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // DATE_UPDATED
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7) // DATE_CREATED
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Area entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setAREA_ID(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setAREA_NAME(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setCITY_ID(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setSTATE_ID(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setCOUNTRY_ID(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setDATE_UPDATED(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setDATE_CREATED(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Area entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Area entity) {
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

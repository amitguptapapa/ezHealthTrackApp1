package com.ezhealthtrack.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.ezhealthtrack.greendao.City;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table CITY.
*/
public class CityDao extends AbstractDao<City, Long> {

    public static final String TABLENAME = "CITY";

    /**
     * Properties of entity City.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property CITY_ID = new Property(1, String.class, "CITY_ID", false, "CITY__ID");
        public final static Property CITY_NAME = new Property(2, String.class, "CITY_NAME", false, "CITY__NAME");
        public final static Property STATE_ID = new Property(3, String.class, "STATE_ID", false, "STATE__ID");
        public final static Property COUNTRY_ID = new Property(4, String.class, "COUNTRY_ID", false, "COUNTRY__ID");
        public final static Property DATE_UPDATED = new Property(5, String.class, "DATE_UPDATED", false, "DATE__UPDATED");
        public final static Property DATE_CREATED = new Property(6, String.class, "DATE_CREATED", false, "DATE__CREATED");
    };


    public CityDao(DaoConfig config) {
        super(config);
    }
    
    public CityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'CITY' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'CITY__ID' TEXT," + // 1: CITY_ID
                "'CITY__NAME' TEXT," + // 2: CITY_NAME
                "'STATE__ID' TEXT," + // 3: STATE_ID
                "'COUNTRY__ID' TEXT," + // 4: COUNTRY_ID
                "'DATE__UPDATED' TEXT," + // 5: DATE_UPDATED
                "'DATE__CREATED' TEXT);"); // 6: DATE_CREATED
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'CITY'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, City entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String CITY_ID = entity.getCITY_ID();
        if (CITY_ID != null) {
            stmt.bindString(2, CITY_ID);
        }
 
        String CITY_NAME = entity.getCITY_NAME();
        if (CITY_NAME != null) {
            stmt.bindString(3, CITY_NAME);
        }
 
        String STATE_ID = entity.getSTATE_ID();
        if (STATE_ID != null) {
            stmt.bindString(4, STATE_ID);
        }
 
        String COUNTRY_ID = entity.getCOUNTRY_ID();
        if (COUNTRY_ID != null) {
            stmt.bindString(5, COUNTRY_ID);
        }
 
        String DATE_UPDATED = entity.getDATE_UPDATED();
        if (DATE_UPDATED != null) {
            stmt.bindString(6, DATE_UPDATED);
        }
 
        String DATE_CREATED = entity.getDATE_CREATED();
        if (DATE_CREATED != null) {
            stmt.bindString(7, DATE_CREATED);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public City readEntity(Cursor cursor, int offset) {
        City entity = new City( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // CITY_ID
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // CITY_NAME
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // STATE_ID
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // COUNTRY_ID
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // DATE_UPDATED
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6) // DATE_CREATED
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, City entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCITY_ID(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCITY_NAME(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setSTATE_ID(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setCOUNTRY_ID(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setDATE_UPDATED(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setDATE_CREATED(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(City entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(City entity) {
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
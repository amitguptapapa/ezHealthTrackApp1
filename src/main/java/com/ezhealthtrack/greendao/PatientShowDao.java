package com.ezhealthtrack.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.ezhealthtrack.greendao.PatientShow;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table PATIENT_SHOW.
*/
public class PatientShowDao extends AbstractDao<PatientShow, Long> {

    public static final String TABLENAME = "PATIENT_SHOW";

    /**
     * Properties of entity PatientShow.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property P_id = new Property(1, String.class, "p_id", false, "P_ID");
        public final static Property Pf_id = new Property(2, String.class, "pf_id", false, "PF_ID");
        public final static Property P_type = new Property(3, String.class, "p_type", false, "P_TYPE");
        public final static Property Pfn = new Property(4, String.class, "pfn", false, "PFN");
        public final static Property Pln = new Property(5, String.class, "pln", false, "PLN");
        public final static Property Gender = new Property(6, String.class, "gender", false, "GENDER");
        public final static Property Age = new Property(7, String.class, "age", false, "AGE");
    };


    public PatientShowDao(DaoConfig config) {
        super(config);
    }
    
    public PatientShowDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'PATIENT_SHOW' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'P_ID' TEXT," + // 1: p_id
                "'PF_ID' TEXT," + // 2: pf_id
                "'P_TYPE' TEXT," + // 3: p_type
                "'PFN' TEXT," + // 4: pfn
                "'PLN' TEXT," + // 5: pln
                "'GENDER' TEXT," + // 6: gender
                "'AGE' TEXT);"); // 7: age
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'PATIENT_SHOW'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, PatientShow entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String p_id = entity.getP_id();
        if (p_id != null) {
            stmt.bindString(2, p_id);
        }
 
        String pf_id = entity.getPf_id();
        if (pf_id != null) {
            stmt.bindString(3, pf_id);
        }
 
        String p_type = entity.getP_type();
        if (p_type != null) {
            stmt.bindString(4, p_type);
        }
 
        String pfn = entity.getPfn();
        if (pfn != null) {
            stmt.bindString(5, pfn);
        }
 
        String pln = entity.getPln();
        if (pln != null) {
            stmt.bindString(6, pln);
        }
 
        String gender = entity.getGender();
        if (gender != null) {
            stmt.bindString(7, gender);
        }
 
        String age = entity.getAge();
        if (age != null) {
            stmt.bindString(8, age);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public PatientShow readEntity(Cursor cursor, int offset) {
        PatientShow entity = new PatientShow( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // p_id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // pf_id
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // p_type
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // pfn
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // pln
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // gender
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7) // age
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, PatientShow entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setP_id(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPf_id(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setP_type(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setPfn(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setPln(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setGender(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setAge(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(PatientShow entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(PatientShow entity) {
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

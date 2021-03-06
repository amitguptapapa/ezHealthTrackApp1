package com.ezhealthtrack.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.ezhealthtrack.greendao.Icd10Item;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table ICD10_ITEM.
*/
public class Icd10ItemDao extends AbstractDao<Icd10Item, Long> {

    public static final String TABLENAME = "ICD10_ITEM";

    /**
     * Properties of entity Icd10Item.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Item_id = new Property(1, String.class, "item_id", false, "ITEM_ID");
        public final static Property Chapter_name = new Property(2, String.class, "chapter_name", false, "CHAPTER_NAME");
        public final static Property Chapter_desc = new Property(3, String.class, "chapter_desc", false, "CHAPTER_DESC");
        public final static Property Section_id = new Property(4, String.class, "section_id", false, "SECTION_ID");
        public final static Property Section_desc = new Property(5, String.class, "section_desc", false, "SECTION_DESC");
        public final static Property Diag_parent_name = new Property(6, String.class, "diag_parent_name", false, "DIAG_PARENT_NAME");
        public final static Property Diag_parent_desc = new Property(7, String.class, "diag_parent_desc", false, "DIAG_PARENT_DESC");
        public final static Property Diag_name = new Property(8, String.class, "diag_name", false, "DIAG_NAME");
        public final static Property Diag_desc = new Property(9, String.class, "diag_desc", false, "DIAG_DESC");
        public final static Property Status = new Property(10, String.class, "status", false, "STATUS");
    };


    public Icd10ItemDao(DaoConfig config) {
        super(config);
    }
    
    public Icd10ItemDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ICD10_ITEM' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'ITEM_ID' TEXT," + // 1: item_id
                "'CHAPTER_NAME' TEXT," + // 2: chapter_name
                "'CHAPTER_DESC' TEXT," + // 3: chapter_desc
                "'SECTION_ID' TEXT," + // 4: section_id
                "'SECTION_DESC' TEXT," + // 5: section_desc
                "'DIAG_PARENT_NAME' TEXT," + // 6: diag_parent_name
                "'DIAG_PARENT_DESC' TEXT," + // 7: diag_parent_desc
                "'DIAG_NAME' TEXT," + // 8: diag_name
                "'DIAG_DESC' TEXT," + // 9: diag_desc
                "'STATUS' TEXT);"); // 10: status
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ICD10_ITEM'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Icd10Item entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String item_id = entity.getItem_id();
        if (item_id != null) {
            stmt.bindString(2, item_id);
        }
 
        String chapter_name = entity.getChapter_name();
        if (chapter_name != null) {
            stmt.bindString(3, chapter_name);
        }
 
        String chapter_desc = entity.getChapter_desc();
        if (chapter_desc != null) {
            stmt.bindString(4, chapter_desc);
        }
 
        String section_id = entity.getSection_id();
        if (section_id != null) {
            stmt.bindString(5, section_id);
        }
 
        String section_desc = entity.getSection_desc();
        if (section_desc != null) {
            stmt.bindString(6, section_desc);
        }
 
        String diag_parent_name = entity.getDiag_parent_name();
        if (diag_parent_name != null) {
            stmt.bindString(7, diag_parent_name);
        }
 
        String diag_parent_desc = entity.getDiag_parent_desc();
        if (diag_parent_desc != null) {
            stmt.bindString(8, diag_parent_desc);
        }
 
        String diag_name = entity.getDiag_name();
        if (diag_name != null) {
            stmt.bindString(9, diag_name);
        }
 
        String diag_desc = entity.getDiag_desc();
        if (diag_desc != null) {
            stmt.bindString(10, diag_desc);
        }
 
        String status = entity.getStatus();
        if (status != null) {
            stmt.bindString(11, status);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Icd10Item readEntity(Cursor cursor, int offset) {
        Icd10Item entity = new Icd10Item( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // item_id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // chapter_name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // chapter_desc
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // section_id
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // section_desc
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // diag_parent_name
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // diag_parent_desc
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // diag_name
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // diag_desc
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10) // status
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Icd10Item entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setItem_id(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setChapter_name(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setChapter_desc(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setSection_id(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setSection_desc(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setDiag_parent_name(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setDiag_parent_desc(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setDiag_name(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setDiag_desc(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setStatus(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Icd10Item entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Icd10Item entity) {
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

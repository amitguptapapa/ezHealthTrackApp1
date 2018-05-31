package com.ezhealthtrack.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/**
 * DAO for table LAB_APPOINTMENT.
 */
public class LabAppointmentDao extends AbstractDao<LabAppointment, Long> {

	public static final String TABLENAME = "LAB_APPOINTMENT";

	/**
	 * Properties of entity LabAppointment.<br/>
	 * Can be used for QueryBuilder and for referencing column names.
	 */
	public static class Properties {
		public final static Property Id = new Property(0, Long.class, "id",
				true, "_id");
		public final static Property User_id = new Property(1, String.class,
				"user_id", false, "USER_ID");
		public final static Property Bkid = new Property(2, String.class,
				"bkid", false, "BKID");
		public final static Property Pid = new Property(3, String.class, "pid",
				false, "PID");
		public final static Property Pfid = new Property(4, String.class,
				"pfid", false, "PFID");
		public final static Property Slotid = new Property(5, String.class,
				"slotid", false, "SLOTID");
		public final static Property Flag = new Property(6, String.class,
				"flag", false, "FLAG");
		public final static Property Apflag = new Property(7, String.class,
				"apflag", false, "APFLAG");
		public final static Property Followid = new Property(8, String.class,
				"followid", false, "FOLLOWID");
		public final static Property Aptdate = new Property(9,
				java.util.Date.class, "aptdate", false, "APTDATE");
		public final static Property Reason = new Property(10, String.class,
				"reason", false, "REASON");
		public final static Property Assigned_user_type = new Property(11,
				Integer.class, "assigned_user_type", false,
				"ASSIGNED_USER_TYPE");
		public final static Property Addi_info = new Property(12, String.class,
				"addi_info", false, "ADDI_INFO");
		public final static Property Visit_location = new Property(13,
				String.class, "visit_location", false, "VISIT_LOCATION");
		public final static Property Assigned_user_id = new Property(14,
				String.class, "assigned_user_id", false, "ASSIGNED_USER_ID");
		public final static Property Visit = new Property(15, String.class,
				"visit", false, "VISIT");
		public final static Property Epid = new Property(16, String.class,
				"epid", false, "EPID");
	};

	public LabAppointmentDao(DaoConfig config) {
		super(config);
	}

	public LabAppointmentDao(DaoConfig config, DaoSession daoSession) {
		super(config, daoSession);
	}

	/** Creates the underlying database table. */
	public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
		String constraint = ifNotExists ? "IF NOT EXISTS " : "";
		db.execSQL("CREATE TABLE " + constraint + "'LAB_APPOINTMENT' (" + //
				"'_id' INTEGER PRIMARY KEY ," + // 0: id
				"'USER_ID' TEXT," + // 1: user_id
				"'BKID' TEXT," + // 2: bkid
				"'PID' TEXT," + // 3: pid
				"'PFID' TEXT," + // 4: pfid
				"'SLOTID' TEXT," + // 5: slotid
				"'FLAG' TEXT," + // 6: flag
				"'APFLAG' TEXT," + // 7: apflag
				"'FOLLOWID' TEXT," + // 8: followid
				"'APTDATE' INTEGER," + // 9: aptdate
				"'REASON' TEXT," + // 10: reason
				"'ASSIGNED_USER_TYPE' INTEGER," + // 11: assigned_user_type
				"'ADDI_INFO' TEXT," + // 12: addi_info
				"'VISIT_LOCATION' TEXT," + // 13: visit_location
				"'ASSIGNED_USER_ID' TEXT," + // 14: assigned_user_id
				"'VISIT' TEXT," + // 15: visit
				"'EPID' TEXT)"); // 16: epid
	}

	/** Drops the underlying database table. */
	public static void dropTable(SQLiteDatabase db, boolean ifExists) {
		String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "")
				+ "'LAB_APPOINTMENT'";
		db.execSQL(sql);
	}

	/** @inheritdoc */
	@Override
	protected void bindValues(SQLiteStatement stmt, LabAppointment entity) {
		stmt.clearBindings();

		Long id = entity.getId();
		if (id != null) {
			stmt.bindLong(1, id);
		}

		String user_id = entity.getUser_id();
		if (user_id != null) {
			stmt.bindString(2, user_id);
		}

		String bkid = entity.getBkid();
		if (bkid != null) {
			stmt.bindString(3, bkid);
		}

		String pid = entity.getPid();
		if (pid != null) {
			stmt.bindString(4, pid);
		}

		String pfid = entity.getPfid();
		if (pfid != null) {
			stmt.bindString(5, pfid);
		}

		String slotid = entity.getSlotid();
		if (slotid != null) {
			stmt.bindString(6, slotid);
		}

		String flag = entity.getFlag();
		if (flag != null) {
			stmt.bindString(7, flag);
		}

		String apflag = entity.getApflag();
		if (apflag != null) {
			stmt.bindString(8, apflag);
		}

		String followid = entity.getFollowid();
		if (followid != null) {
			stmt.bindString(9, followid);
		}

		java.util.Date aptdate = entity.getAptdate();
		if (aptdate != null) {
			stmt.bindLong(10, aptdate.getTime());
		}

		String reason = entity.getReason();
		if (reason != null) {
			stmt.bindString(11, reason);
		}

		Integer assigned_user_type = entity.getAssigned_user_type();
		if (assigned_user_type != null) {
			stmt.bindLong(12, assigned_user_type);
		}

		String addi_info = entity.getAddi_info();
		if (addi_info != null) {
			stmt.bindString(13, addi_info);
		}

		String visit_location = entity.getVisit_location();
		if (visit_location != null) {
			stmt.bindString(14, visit_location);
		}

		String assigned_user_id = entity.getAssigned_user_id();
		if (assigned_user_id != null) {
			stmt.bindString(15, assigned_user_id);
		}

		String visit = entity.getVisit();
		if (visit != null) {
			stmt.bindString(16, visit);
		}

		String epid = entity.getEpid();
		if (epid != null) {
			stmt.bindString(17, epid);
		}
	}

	/** @inheritdoc */
	@Override
	public Long readKey(Cursor cursor, int offset) {
		return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
	}

	/** @inheritdoc */
	@Override
	public LabAppointment readEntity(Cursor cursor, int offset) {
		LabAppointment entity = new LabAppointment(
				//
				cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
				cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // user_id
				cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // bkid
				cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // pid
				cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // pfid
				cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // slotid
				cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // flag
				cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // apflag
				cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // followid
				cursor.isNull(offset + 9) ? null : new java.util.Date(cursor
						.getLong(offset + 9)), // aptdate
				cursor.isNull(offset + 10) ? null : cursor
						.getString(offset + 10), // reason
				cursor.isNull(offset + 11) ? null : cursor.getInt(offset + 11), // assigned_user_type
				cursor.isNull(offset + 12) ? null : cursor
						.getString(offset + 12), // addi_info
				cursor.isNull(offset + 13) ? null : cursor
						.getString(offset + 13), // visit_location
				cursor.isNull(offset + 14) ? null : cursor
						.getString(offset + 14), // assigned_user_id
				cursor.isNull(offset + 15) ? null : cursor
						.getString(offset + 15), // visit
				cursor.isNull(offset + 16) ? null : cursor
						.getString(offset + 16)); // epid

		return entity;
	}

	/** @inheritdoc */
	@Override
	public void readEntity(Cursor cursor, LabAppointment entity, int offset) {
		entity.setId(cursor.isNull(offset + 0) ? null : cursor
				.getLong(offset + 0));
		entity.setUser_id(cursor.isNull(offset + 1) ? null : cursor
				.getString(offset + 1));
		entity.setBkid(cursor.isNull(offset + 2) ? null : cursor
				.getString(offset + 2));
		entity.setPid(cursor.isNull(offset + 3) ? null : cursor
				.getString(offset + 3));
		entity.setPfid(cursor.isNull(offset + 4) ? null : cursor
				.getString(offset + 4));
		entity.setSlotid(cursor.isNull(offset + 5) ? null : cursor
				.getString(offset + 5));
		entity.setFlag(cursor.isNull(offset + 6) ? null : cursor
				.getString(offset + 6));
		entity.setApflag(cursor.isNull(offset + 7) ? null : cursor
				.getString(offset + 7));
		entity.setFollowid(cursor.isNull(offset + 8) ? null : cursor
				.getString(offset + 8));
		entity.setAptdate(cursor.isNull(offset + 9) ? null
				: new java.util.Date(cursor.getLong(offset + 9)));
		entity.setReason(cursor.isNull(offset + 10) ? null : cursor
				.getString(offset + 10));
		entity.setAssigned_user_type(cursor.isNull(offset + 11) ? null : cursor
				.getInt(offset + 11));
		entity.setAddi_info(cursor.isNull(offset + 12) ? null : cursor
				.getString(offset + 12));
		entity.setVisit_location(cursor.isNull(offset + 13) ? null : cursor
				.getString(offset + 13));
		entity.setAssigned_user_id(cursor.isNull(offset + 14) ? null : cursor
				.getString(offset + 14));
		entity.setVisit(cursor.isNull(offset + 15) ? null : cursor
				.getString(offset + 15));
		entity.setEpid(cursor.isNull(offset + 16) ? null : cursor
				.getString(offset + 16));
	}

	/** @inheritdoc */
	@Override
	protected Long updateKeyAfterInsert(LabAppointment entity, long rowId) {
		entity.setId(rowId);
		return rowId;
	}

	/** @inheritdoc */
	@Override
	public Long getKey(LabAppointment entity) {
		if (entity != null) {
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
package com.ezhealthtrack.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ezhealthtrack.activity.LoginActivity;
import com.ezhealthtrack.model.Permission;
import com.ezhealthtrack.model.State;
import com.ezhealthtrack.model.ToStep;
import com.ezhealthtrack.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String LOG = "DatabaseHelper";

	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_NAME = "workflow";

	private static final String TABLE_STATE = "state";
	private static final String TABLE_TO_STEP = "to_step";
	private static final String TABLE_PERMISSION = "permission";
	private static final String TABLE_PATIENT = "patient";

	private static final String KEY_ID = "id";
	private static final String KEY_STATE_ID = "state_id";
	private static final String KEY_STATE_NAME = "state_name";
	private static final String KEY_TO_STEP_NAME = "to_step_name";
	private static final String KEY_PERMISSION = "todo_permission";

	private static final String CREATE_TABLE_STATE = "CREATE TABLE "
			+ DatabaseHelper.TABLE_STATE + "(" + DatabaseHelper.KEY_ID
			+ " INTEGER PRIMARY KEY," + DatabaseHelper.KEY_STATE_NAME
			+ " TEXT," + DatabaseHelper.KEY_STATE_ID + " INTEGER )";

	private static final String CREATE_TABLE_TO_STEP = "CREATE TABLE "
			+ DatabaseHelper.TABLE_TO_STEP + "(" + DatabaseHelper.KEY_ID
			+ " INTEGER PRIMARY KEY," + DatabaseHelper.KEY_STATE_ID
			+ " INTEGER," + DatabaseHelper.KEY_TO_STEP_NAME + " TEXT" + ")";

	private static final String CREATE_TABLE_PERMISSION = "CREATE TABLE "
			+ DatabaseHelper.TABLE_PERMISSION + "(" + DatabaseHelper.KEY_ID
			+ " INTEGER PRIMARY KEY," + DatabaseHelper.KEY_STATE_ID
			+ " INTEGER," + DatabaseHelper.KEY_PERMISSION + " TEXT" + ")";

	static public DatabaseHelper db;

	static public void init(final Context context) {
		DatabaseHelper.db = new DatabaseHelper(context);
	}

	private DatabaseHelper(final Context context) {
		super(context, DatabaseHelper.DATABASE_NAME, null,
				DatabaseHelper.DATABASE_VERSION);
	}

	public void clearAllTables() {

		final SQLiteDatabase db = getWritableDatabase();
		db.execSQL("delete from " + DatabaseHelper.TABLE_STATE);
		db.execSQL("delete from " + DatabaseHelper.TABLE_TO_STEP);
		db.execSQL("delete from " + DatabaseHelper.TABLE_PERMISSION);
		// db.execSQL("delete from " + DatabaseHelper.TABLE_PATIENT);
	}

	public void clearPatientTable() {

		final SQLiteDatabase db = getWritableDatabase();
		db.execSQL("delete from " + DatabaseHelper.TABLE_PATIENT);
	}

	public long createPermission(final Permission permission) {
		final SQLiteDatabase db = getWritableDatabase();

		final ContentValues values = new ContentValues();
		values.put(DatabaseHelper.KEY_STATE_ID, permission.getId());
		values.put(DatabaseHelper.KEY_PERMISSION, permission.getName());
		final long key_id = db.insert(DatabaseHelper.TABLE_PERMISSION, null,
				values);
		return key_id;
	}

	public long createState(final State state) {
		final SQLiteDatabase db = getWritableDatabase();

		final ContentValues values = new ContentValues();
		values.put(DatabaseHelper.KEY_STATE_ID, state.getId());
		values.put(DatabaseHelper.KEY_STATE_NAME, state.getName());
		final long state_id = db.insert(DatabaseHelper.TABLE_STATE, null,
				values);
		return state_id;
	}

	public long createToStep(final ToStep toStep) {
		final SQLiteDatabase db = getWritableDatabase();

		final ContentValues values = new ContentValues();
		values.put(DatabaseHelper.KEY_STATE_ID, toStep.getId());
		values.put(DatabaseHelper.KEY_TO_STEP_NAME, toStep.getName());
		final long key_id = db.insert(DatabaseHelper.TABLE_TO_STEP, null,
				values);
		return key_id;
	}

	public void deletePermission(final long state_id) {
		final SQLiteDatabase db = getWritableDatabase();
		db.delete(DatabaseHelper.TABLE_PERMISSION, DatabaseHelper.KEY_STATE_ID
				+ " = ?", new String[] { String.valueOf(state_id) });
	}

	public void deleteState(final long state_id) {
		final SQLiteDatabase db = getWritableDatabase();
		db.delete(DatabaseHelper.TABLE_STATE, DatabaseHelper.KEY_STATE_ID
				+ " = ?", new String[] { String.valueOf(state_id) });
	}

	public void deleteToStep(final long state_id) {
		final SQLiteDatabase db = getWritableDatabase();
		db.delete(DatabaseHelper.TABLE_TO_STEP, DatabaseHelper.KEY_STATE_ID
				+ " = ?", new String[] { String.valueOf(state_id) });
	}

	public List<State> getAllState() {
		final List<State> states = new ArrayList<State>();
		final String selectQuery = "SELECT  * FROM "
				+ DatabaseHelper.TABLE_STATE;

		final SQLiteDatabase db = getReadableDatabase();
		final Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			do {
				final State state = new State();
				state.setId(c.getInt((c
						.getColumnIndex(DatabaseHelper.KEY_STATE_ID))));
				state.setName((c.getString(c
						.getColumnIndex(DatabaseHelper.KEY_STATE_NAME))));
				states.add(state);
			} while (c.moveToNext());
		}

		return states;
	}

	public List<ToStep> getAllToStep() {
		final List<ToStep> toSteps = new ArrayList<ToStep>();
		final String selectQuery = "SELECT  * FROM "
				+ DatabaseHelper.TABLE_TO_STEP;

		final SQLiteDatabase db = getReadableDatabase();
		final Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			do {
				final ToStep toStep = new ToStep();
				toStep.setId(c.getInt((c
						.getColumnIndex(DatabaseHelper.KEY_STATE_ID))));
				toStep.setName((c.getString(c
						.getColumnIndex(DatabaseHelper.KEY_TO_STEP_NAME))));
				toSteps.add(toStep);
			} while (c.moveToNext());
		}

		return toSteps;
	}

	public ArrayList<Permission> getPermission(final long state_id) {
		final SQLiteDatabase db = getReadableDatabase();

		final String selectQuery = "SELECT  * FROM "
				+ DatabaseHelper.TABLE_PERMISSION + " WHERE "
				+ DatabaseHelper.KEY_STATE_ID + " = " + state_id;

		final Cursor c = db.rawQuery(selectQuery, null);

		if (c != null) {
			c.moveToFirst();
		}

		final ArrayList<Permission> permissions = new ArrayList<Permission>();
		do {
			final Permission permission = new Permission();
			permission.setId(c.getInt((c
					.getColumnIndex(DatabaseHelper.KEY_STATE_ID))));
			permission.setName((c.getString(c
					.getColumnIndex(DatabaseHelper.KEY_PERMISSION))));
			permissions.add(permission);
		} while (c.moveToNext());
		return permissions;
	}

	public List<Permission> getPermissions() {
		final List<Permission> permissions = new ArrayList<Permission>();
		final String selectQuery = "SELECT  * FROM "
				+ DatabaseHelper.TABLE_PERMISSION;

		final SQLiteDatabase db = getReadableDatabase();
		final Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			do {
				final Permission permission = new Permission();
				permission.setId(c.getInt((c
						.getColumnIndex(DatabaseHelper.KEY_STATE_ID))));
				permission.setName((c.getString(c
						.getColumnIndex(DatabaseHelper.KEY_PERMISSION))));
				permissions.add(permission);
			} while (c.moveToNext());
		}

		return permissions;
	}

	public State getState(final long state_id) {
		final SQLiteDatabase db = getReadableDatabase();

		final String selectQuery = "SELECT  * FROM "
				+ DatabaseHelper.TABLE_STATE + " WHERE "
				+ DatabaseHelper.KEY_STATE_ID + " = " + state_id;

		Log.e(DatabaseHelper.LOG, selectQuery);

		final Cursor c = db.rawQuery(selectQuery, null);

		if (c != null) {
			c.moveToFirst();
		}

		final State state = new State();
		state.setId(c.getInt(c.getColumnIndex(DatabaseHelper.KEY_STATE_ID)));
		state.setName((c.getString(c
				.getColumnIndex(DatabaseHelper.KEY_STATE_NAME))));

		return state;
	}

	public State getState(final String state_name) {
		final SQLiteDatabase db = getReadableDatabase();

		final String selectQuery = "SELECT  * FROM "
				+ DatabaseHelper.TABLE_STATE + " WHERE "
				+ DatabaseHelper.KEY_STATE_NAME + " = \"" + state_name + "\"";

		Log.e(DatabaseHelper.LOG, selectQuery);

		final Cursor c = db.rawQuery(selectQuery, null);

		if (c != null) {
			c.moveToFirst();
		}

		final State state = new State();
		state.setId(c.getInt(c.getColumnIndex(DatabaseHelper.KEY_STATE_ID)));
		state.setName((c.getString(c
				.getColumnIndex(DatabaseHelper.KEY_STATE_NAME))));

		return state;
	}

	public ArrayList<ToStep> getToStep(final long state_id) {
		final SQLiteDatabase db = getReadableDatabase();

		final String selectQuery = "SELECT  * FROM "
				+ DatabaseHelper.TABLE_TO_STEP + " WHERE "
				+ DatabaseHelper.KEY_STATE_ID + " = " + state_id;

		final Cursor c = db.rawQuery(selectQuery, null);

		if (c != null) {
			c.moveToFirst();
		}

		final ArrayList<ToStep> toSteps = new ArrayList<ToStep>();
		if (c.getCount() > 0) {
			do {
				final ToStep toStep = new ToStep();
				toStep.setId(c.getInt((c
						.getColumnIndex(DatabaseHelper.KEY_STATE_ID))));
				toStep.setName((c.getString(c
						.getColumnIndex(DatabaseHelper.KEY_TO_STEP_NAME))));
				toSteps.add(toStep);
			} while (c.moveToNext());
		}
		return toSteps;
	}

	@Override
	public void onCreate(final SQLiteDatabase db) {
		db.execSQL(DatabaseHelper.CREATE_TABLE_STATE);
		db.execSQL(DatabaseHelper.CREATE_TABLE_TO_STEP);
		db.execSQL(DatabaseHelper.CREATE_TABLE_PERMISSION);
	}

	@Override
	public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
			final int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + DatabaseHelper.TABLE_STATE);
		db.execSQL("DROP TABLE IF EXISTS " + DatabaseHelper.TABLE_TO_STEP);
		db.execSQL("DROP TABLE IF EXISTS " + DatabaseHelper.TABLE_PERMISSION);
		db.execSQL("DROP TABLE IF EXISTS " + DatabaseHelper.TABLE_PATIENT);
		onCreate(db);
	}

	public int updatePermission(final Permission permission) {
		final SQLiteDatabase db = getWritableDatabase();

		final ContentValues values = new ContentValues();
		values.put(DatabaseHelper.KEY_STATE_ID, permission.getId());
		values.put(DatabaseHelper.KEY_PERMISSION, permission.getName());

		return db.update(DatabaseHelper.TABLE_PERMISSION, values,
				DatabaseHelper.KEY_STATE_ID + " = ?",
				new String[] { String.valueOf(permission.getId()) });
	}

	public int updateState(final State state) {
		final SQLiteDatabase db = getWritableDatabase();

		final ContentValues values = new ContentValues();
		values.put(DatabaseHelper.KEY_STATE_ID, state.getId());
		values.put(DatabaseHelper.KEY_STATE_NAME, state.getName());
		return db.update(DatabaseHelper.TABLE_STATE, values,
				DatabaseHelper.KEY_STATE_ID + " = ?",
				new String[] { String.valueOf(state.getId()) });
	}

	public int updateToSteps(final ToStep toStep) {
		final SQLiteDatabase db = getWritableDatabase();

		final ContentValues values = new ContentValues();
		values.put(DatabaseHelper.KEY_STATE_ID, toStep.getId());
		values.put(DatabaseHelper.KEY_TO_STEP_NAME, toStep.getName());

		return db.update(DatabaseHelper.TABLE_TO_STEP, values,
				DatabaseHelper.KEY_STATE_ID + " = ?",
				new String[] { String.valueOf(toStep.getId()) });
	}

}

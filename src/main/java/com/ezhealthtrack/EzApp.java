package com.ezhealthtrack;

import java.io.IOException;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.controller.UserController;
import com.ezhealthtrack.db.DatabaseHelper;
import com.ezhealthtrack.greendao.AllergyDao;
import com.ezhealthtrack.greendao.AppointmentDao;
import com.ezhealthtrack.greendao.AreaDao;
import com.ezhealthtrack.greendao.CityDao;
import com.ezhealthtrack.greendao.CountryDao;
import com.ezhealthtrack.greendao.DaoMaster;
import com.ezhealthtrack.greendao.DaoMaster.DevOpenHelper;
import com.ezhealthtrack.greendao.DaoSession;
import com.ezhealthtrack.greendao.Icd10ItemDao;
import com.ezhealthtrack.greendao.LabAppointmentDao;
import com.ezhealthtrack.greendao.LabTechnicianDao;
import com.ezhealthtrack.greendao.LabWorkFlowDao;
import com.ezhealthtrack.greendao.MedRecAllergyDao;
import com.ezhealthtrack.greendao.MedRecVisitNotesDao;
import com.ezhealthtrack.greendao.MessageModelDao;
import com.ezhealthtrack.greendao.OrderDao;
import com.ezhealthtrack.greendao.OrderDetailDao;
import com.ezhealthtrack.greendao.PatientDao;
import com.ezhealthtrack.greendao.PatientShowDao;
import com.ezhealthtrack.greendao.SoapNoteDao;
import com.ezhealthtrack.greendao.StateDao;
import com.ezhealthtrack.greendao.UserDao;
import com.ezhealthtrack.one.ActivityLifecycleAdapter;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.BitmapLruCache;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.NetworkCallController;
import com.flurry.android.FlurryAgent;

public class EzApp extends Application {
	private static EzApp instance;
	private DaoMaster daoMaster;
	private DaoSession daoSession;
	public static UserController userController = new UserController();
	public static Icd10ItemDao icdDao;
	public static CountryDao countryDao;
	public static StateDao stateDao;
	public static CityDao cityDao;
	public static AreaDao areaDao;
	public static AppointmentDao aptDao;
	public static SoapNoteDao soapNoteDao;
	public static AllergyDao allergyDao;
	public static PatientDao patientDao;
	public static MessageModelDao messageDao;
	public static UserDao userDao;
	public static LabAppointmentDao labAptDao;
	public static PatientShowDao patientShowDao;
	public static OrderDao orderDao;
	public static OrderDetailDao orderDetailDao;
	public static LabTechnicianDao labTechnicianDao;
	public static LabWorkFlowDao labWorkflowDao;
	public static MedRecVisitNotesDao medRecVisitNotesDao;
	public static MedRecAllergyDao medRecAllergyDao;
	private SQLiteDatabase db1;
	public static SharedPreferences sharedPref;
	public static RequestQueue mVolleyQueue;
	private final BitmapLruCache bitmapLruCache = new BitmapLruCache();
	public static final SimpleDateFormat sdfMmddyy = new SimpleDateFormat(
			"MM/dd/yyyy");
	public static final SimpleDateFormat sdfddMmyy = new SimpleDateFormat(
			"dd/MM/yyyy");
	public static final SimpleDateFormat sdfddMmyy1 = new SimpleDateFormat(
			"dd-MM-yyyy");
	public static final SimpleDateFormat sdfyyMmdd = new SimpleDateFormat(
			"yyyy-MM-dd");
	public static final SimpleDateFormat sdfyyyyMmdd = new SimpleDateFormat(
			"yyyy/MM/dd");
	public static final SimpleDateFormat sdfmmm = new SimpleDateFormat(
			"MMM dd, yyyy");
	public static final SimpleDateFormat sdfemmm = new SimpleDateFormat(
			"EEE, MMM dd, yyyy");
	public static final SimpleDateFormat sdfOnmmm = new SimpleDateFormat(
			"' on 'EEE, MMM dd, yyyy");
	public static final SimpleDateFormat sdfyymmddhhmmss = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss");
	public static final SimpleDateFormat sdfddmmyyhhmmss = new SimpleDateFormat(
			"dd/MM/yyyy hh:mm aa");
	public static final SimpleDateFormat sdfddmmyyhhms = new SimpleDateFormat(
			"dd/MM/yyyy hh:mm");
	public static final SimpleDateFormat sdfTime = new SimpleDateFormat(
			"hh:mm a");
	public static final SimpleDateFormat sdfSlot = new SimpleDateFormat("hh:mm");
	public static final SimpleDateFormat sdfeeemmmddyy = new SimpleDateFormat(
			"EEE, MMM dd, yyyy");
	public static final SimpleDateFormat sdfddmmyyyyhhmmss = new SimpleDateFormat(
			"dd/MM/yyyy HH:mm:ss");
	public static final SimpleDateFormat sdfMmyy = new SimpleDateFormat(
			"MM/yyyy");
	public static NetworkCallController networkController = new NetworkCallController();

	public EzApp() {
		instance = this;
	}

	public static EzApp getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// configure Flurry
		FlurryAgent.setLogEnabled(false);

		// init Flurry
		FlurryAgent.init(this, "D2Z8WP9VVBJ6N9YSMZPW");

		// register to be informed of activities starting up
		registerActivityLifecycleCallbacks(new ActivityLifecycleAdapter() {
			@Override
			public void onActivityCreated(Activity activity,
					Bundle savedInstanceState) {
				// activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}
		});
		// init database
		DatabaseHelper.init(this);

		// database initialization
		initDatabaseSetUp();

		if (EzApp.allergyDao.count() == 0) {
			EzApp.userController.allergy(this);
		}
		if (EzApp.icdDao.count() == 0) {
			EzApp.userController.icd10(this);
		} else {
		}

	}

	void initDatabaseSetUp() {

		if (EzUtils.getDeviceSize(this.getApplicationContext()) == EzUtils.EZ_SCREEN_LARGE) {
			DataBaseHelper myDbHelper;
			myDbHelper = new DataBaseHelper(this);

			try {
				myDbHelper.createDataBase();
			} catch (IOException ioe) {
				throw new Error("Unable to create database");
			}

			try {
				myDbHelper.openDataBase();
			} catch (SQLException sqle) {
				throw sqle;
			}
		}

		final DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,
				"areafinal.db", null);
		db1 = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db1);
		daoSession = daoMaster.newSession();
		icdDao = daoSession.getIcd10ItemDao();
		countryDao = daoSession.getCountryDao();
		stateDao = daoSession.getStateDao();
		cityDao = daoSession.getCityDao();
		areaDao = daoSession.getAreaDao();
		aptDao = daoSession.getAppointmentDao();
		soapNoteDao = daoSession.getSoapNoteDao();
		allergyDao = daoSession.getAllergyDao();
		patientDao = daoSession.getPatientDao();
		messageDao = daoSession.getMessageModelDao();
		userDao = daoSession.getUserDao();
		labAptDao = daoSession.getLabAppointmentDao();
		patientShowDao = daoSession.getPatientShowDao();
		orderDao = daoSession.getOrderDao();
		orderDetailDao = daoSession.getOrderDetailDao();
		labTechnicianDao = daoSession.getLabTechnicianDao();
		labWorkflowDao = daoSession.getLabWorkFlowDao();
		medRecVisitNotesDao = daoSession.getMedRecVisitNotesDao();
		medRecAllergyDao = daoSession.getMedRecAllergyDao();
		sharedPref = getApplicationContext().getSharedPreferences(
				Constants.EZ_SHARED_PREF, Context.MODE_PRIVATE);
		if (mVolleyQueue == null) {
			mVolleyQueue = Volley.newRequestQueue(this);
			DashboardActivity.imgLoader = new ImageLoader(mVolleyQueue,
					bitmapLruCache);
		}
	}
}

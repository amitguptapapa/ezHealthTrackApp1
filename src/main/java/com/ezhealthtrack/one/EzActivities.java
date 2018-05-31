package com.ezhealthtrack.one;

import android.content.Context;
import android.content.Intent;

import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.activity.LoginActivity;
import com.ezhealthtrack.labs.activity.LabsDashboardActivity;
import com.ezhealthtrack.model.laborder.LabOrderDetails;
import com.ezhealthtrack.physiciansoap.PhysicianSoapActivityMain;
import com.ezhealthtrack.physiciansoap.PhysicianSoapMiniActivity;
import com.ezhealthtrack.print.PrintAllLabReportsActivity;
import com.ezhealthtrack.print.PrintLabReportActivity;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.Util;

public class EzActivities {

	static public void startPrintAllLabReportActivity(Context context,
			LabOrderDetails orders) {
		// set data
		PrintAllLabReportsActivity.setLabOrderDetails(orders);
		// start activity
		Intent mIntent = new Intent(context, PrintAllLabReportsActivity.class);
		context.startActivity(mIntent);
	}

	static public void startPrintLabReportActivity(Context context,
			LabOrderDetails orders) {
		// set data
		PrintLabReportActivity.setLabOrderDetails(orders);
		// start activity
		Intent mIntent = new Intent(context, PrintLabReportActivity.class);
		context.startActivity(mIntent);
	}

	static public void startPhysicianSoapActivity(Context context) {

		String deviceType = EzUtils.getDeviceSize(null);
		if (deviceType.equals(EzUtils.EZ_SCREEN_SMALL)) {
			Intent intent = new Intent(context, PhysicianSoapMiniActivity.class);
			context.startActivity(intent);
		} else {
			Intent intent = new Intent(context, PhysicianSoapActivityMain.class);
			context.startActivity(intent);
		}

	}

	static public void startLoginActivity(Context context) {
		Log.v("EA::startLoginActivity()", "In...");
		Intent intent = new Intent(context, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		context.startActivity(intent);
	}

	static public void startDashBoardActivity(Context context) {
		Log.v("EA::startDashBoardActivity()", "In...");

		String userType = EzApp.sharedPref.getString(Constants.USER_TYPE, "D");

		if (userType.equals("D")) {
			Intent intent = new Intent(context, DashboardActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			context.startActivity(intent);
		} else if (userType.equals("LT")) {
			Intent intent = new Intent(context, LabsDashboardActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			context.startActivity(intent);
		} else {
			Util.Alertdialog(context,
					"App is only for Doctor and Lab Technician.");
		}
	}

}

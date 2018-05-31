package com.ezhealthtrack.labs.activity;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.print.PrintHelper;
import android.text.Html;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ezhealthrack.api.NetworkCalls;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.AddPrescriptionActivity;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.greendao.LabAppointment;
import com.ezhealthtrack.greendao.Order;
import com.ezhealthtrack.greendao.Patient;
import com.ezhealthtrack.greendao.PatientDao.Properties;
import com.ezhealthtrack.labs.controller.LabsController;
import com.ezhealthtrack.labs.controller.OrderController;
import com.ezhealthtrack.labs.fragments.SideFragment.OnActionSelectedListner;
import com.ezhealthtrack.model.AccountModel;
import com.ezhealthtrack.order.LabOrderReport;
import com.ezhealthtrack.order.OrderProduct;
import com.ezhealthtrack.util.BaseActivity;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.Util;
import com.ezhealthtrack.util.NetworkCallController.OnResponse;
import com.flurry.android.FlurryAgent;

public class LabsOrderSuccessActivity extends BaseActivity implements
		OnActionSelectedListner, NetworkCalls.OnResponse {
	private Button btnBill;
	private Button btnPrintOrder;
	private TextView txtPname;
	private TextView txtBill;
	private TextView txtBillStatus;
	private TextView txtOrderStatus;
	private TextView txtTechnician;
	private TextView txtOrder;
	private TextView txtOrderDate;
	private TextView txtDoctor;
	private TextView txtEmailSMS;
	private TextView txtExternalOrderId;
	private TextView txtApprovalStatus;
	private TextView txtHomeVisit;
	private TextView txtHomeVisitDisplay;
	private LabAppointment apt;
	private LabsOrderCreateActivity labOrderCreate;
	private LinearLayout llOrderSuccess;
	private LayoutInflater inflater;
	private Calendar cal;
	private AccountModel account = new AccountModel();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.labs_activity_order_success);
		getActionBar().setHomeButtonEnabled(true);

		Patient pat1 = new Patient();
		apt = EzApp.labAptDao
				.queryBuilder()
				.where(com.ezhealthtrack.greendao.LabAppointmentDao.Properties.Bkid
						.eq(getIntent().getStringExtra("bkid"))).list().get(0);
		Log.i("", "" + apt.orderView.getLabOrderReports().size());
		try {
			if (EzApp.patientDao
					.queryBuilder()
					.where(Properties.Pid.eq(apt.getPid()),
							Properties.Fid.eq(apt.getPfid())).count() > 0) {
				pat1 = (EzApp.patientDao
						.queryBuilder()
						.where(Properties.Pid.eq(apt.getPid()),
								Properties.Fid.eq(apt.getPfid())).list().get(0));
			}

		} catch (Exception e) {
			Log.e("", e);
		}

		llOrderSuccess = (LinearLayout) findViewById(R.id.ll_test_name_list);
		txtPname = (TextView) findViewById(R.id.txt_name_display);
		txtOrder = (TextView) findViewById(R.id.txt_order_display);
		txtBill = (TextView) findViewById(R.id.txt_bill_status);
		txtBillStatus = (TextView) findViewById(R.id.txt_bill_status_display);
		txtOrderStatus = (TextView) findViewById(R.id.txt_order_status_display);
		txtTechnician = (TextView) findViewById(R.id.txt_technician_display);
		txtOrderDate = (TextView) findViewById(R.id.txt_order_date_display);
		txtDoctor = (TextView) findViewById(R.id.txt_doctor_display);
		txtEmailSMS = (TextView) findViewById(R.id.txt_email_sms);
		txtExternalOrderId = (TextView) findViewById(R.id.txt_external_order_id_display);
		txtApprovalStatus = (TextView) findViewById(R.id.txt_approval_status_display);
		txtHomeVisit = (TextView) findViewById(R.id.txt_home_visit);
		txtHomeVisitDisplay = (TextView) findViewById(R.id.txt_home_visit_display);
		btnBill = (Button) findViewById(R.id.btn_bill);
		btnPrintOrder = (Button) findViewById(R.id.btn_print_order);
		try {
			if (apt.orderView.getOrder().getGenerate_bill().equals("0")) {
				btnBill.setVisibility(View.GONE);
				txtBillStatus.setVisibility(View.GONE);
				txtBill.setVisibility(View.GONE);
			} else {
				btnBill.setVisibility(View.VISIBLE);
				txtBillStatus.setVisibility(View.VISIBLE);
				txtBill.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		txtHomeVisitDisplay.setText(apt.orderView.getPatientLocAppointment()
				.toString());

		final Date date = apt.getAptdate();
		txtOrderDate.setText(EzApp.sdfddMmyy.format(date));
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		txtPname.setText(pat1.getPfn() + " " + pat1.getPln() + ", "
				+ pat1.getPage() + "/" + pat1.getPgender());
		txtBillStatus.setText(Order.getBillStatus(apt.orderView.getOrder()
				.getBill_status_id()));
		txtOrderStatus.setText(Order.getOrderStatus(apt.orderView.getOrder()
				.getOrder_status_id()));
		txtTechnician.setText(LabsController.getLabTechnician(
				apt.orderView.getLabOrderDetail().getTechnicianId()).getName());
		if(!Util.isEmptyString(apt.orderView.getLabOrderDetail().getDoctor())){
		txtDoctor.setText(apt.orderView.getLabOrderDetail().getDoctor());
		}else{
			txtDoctor.setText("--");
		}
		txtOrder.setText(apt.orderView.getOrder().getOrder_display_id());
		if (apt.orderView.getOrder().getOrderFields().size() > 0) {
			txtExternalOrderId.setText(apt.orderView.getOrder()
					.getOrderFields().get(0).getValue());
		}else{
			txtExternalOrderId.setText("--");
		}
		try {
			if (Util.isEmptyString(pat1.getPemail().toString())
					&& Util.isEmptyString(pat1.getPmobnum().toString())) {
				txtEmailSMS
						.setText("Notifications will not be sent because Email and Phone not provided.");

			} else {
				txtEmailSMS
						.setText(Html
								.fromHtml("Notifications will be sent to <b>Email: </b>"
										+ apt.orderView.getContactInfo()
												.getEmail()
										+ " and SMS will be send to <b>Phone: </b>"
										+ apt.orderView.getContactInfo()
												.getPhoneNumber()));
			}
		} catch (Exception e) {

		}

		txtOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				FlurryAgent
						.logEvent("LabsOrderSuccessActivity - Order Details Button Clicked");
				final Dialog loaddialog = Util
						.showLoadDialog(LabsOrderSuccessActivity.this);
				OrderController.getOrderList("1",
						LabsOrderSuccessActivity.this, new OnResponse() {

							@Override
							public void onResponseListner(String response) {
								Intent intent = new Intent(
										LabsOrderSuccessActivity.this,
										LabsOrderDetailsActivity.class);
								intent.putExtra("orderid", apt.orderView
										.getOrder().getOrder_id());
								startActivity(intent);
								loaddialog.dismiss();
							}
						}, null, null, null, -1, -1, "");

			}
		});

		btnBill.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FlurryAgent
						.logEvent("LabsOrderSuccessActivity - Bill Payment Button Clicked");
				Intent intent = new Intent(LabsOrderSuccessActivity.this,
						LabsBillPaymentActivity.class);
				intent.putExtra("orderid", apt.orderView.getOrder()
						.getOrder_id());
				startActivity(intent);
				finish();

			}
		});

		btnPrintOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FlurryAgent
						.logEvent("LabsOrderSuccessActivity - Print Order Button Clicked");
				dialogPrint();

			}
		});
		rowOrderSuccess();
	}

	@Override
	public void onResponseListner(String api) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onActionSelected(int position) {
		// TODO Auto-generated method stub

	}

	private void rowOrderSuccess() {
		for (int i = 1; i <= apt.orderView.getLabOrderReports().size(); i++) {
			LabOrderReport report = apt.orderView.getLabOrderReports().get(
					i - 1);
			final View v = inflater.inflate(R.layout.labs_row_order_success,
					null);
			final Date newDate = new Date();
			try {
				final Date availableTime = EzApp.sdfyymmddhhmmss
						.parse(report.getReportAvailableOn());
				long time = availableTime.getTime() - newDate.getTime();
				Log.i("", "" + time);
				if (time / (1000 * 60 * 60 * 24 * 7) > 0) {
					((TextView) v.findViewById(R.id.txt_report_avail))
							.setText(time
									/ (1000 * 60 * 60 * 24 * 7)
									+ " weeks "
									+ "("
									+ EzApp.sdfddmmyyhhmmss
											.format(availableTime) + ")");
				} else if (time / (1000 * 60 * 60 * 24) > 0) {
					((TextView) v.findViewById(R.id.txt_report_avail))
							.setText(time
									/ (1000 * 60 * 60 * 24)
									+ " days "
									+ "("
									+ EzApp.sdfddmmyyhhmmss
											.format(availableTime) + ")");
				} else if (time / (1000 * 60 * 60) > 0) {
					((TextView) v.findViewById(R.id.txt_report_avail))
							.setText(time
									/ (1000 * 60 * 60)
									+ " hours "
									+ "("
									+ EzApp.sdfddmmyyhhmmss
											.format(availableTime) + ")");
				} else if (time / (1000 * 60) > 0) {
					((TextView) v.findViewById(R.id.txt_report_avail))
							.setText(time
									/ (1000 * 60)
									+ " minutes "
									+ "("
									+ EzApp.sdfddmmyyhhmmss
											.format(availableTime) + ")");
				}

				((TextView) v.findViewById(R.id.txt_sequence)).setText(i + ".");
				((TextView) v.findViewById(R.id.txt_test_name)).setText(report
						.getLabTestName());

				llOrderSuccess.addView(v);

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void dialogPrint() {
		final Dialog dialogPrint = new Dialog(this);
		dialogPrint.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialogPrint.setContentView(R.layout.labs_dialog_print_order);
		dialogPrint.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		LayoutParams params = dialogPrint.getWindow().getAttributes();
		params.width = 2000;
		dialogPrint.getWindow().setAttributes(
				(android.view.WindowManager.LayoutParams) params);
		Patient pat = new Patient();
		apt = EzApp.labAptDao
				.queryBuilder()
				.where(com.ezhealthtrack.greendao.LabAppointmentDao.Properties.Bkid
						.eq(getIntent().getStringExtra("bkid"))).list().get(0);
		Log.i("", "" + apt.orderView.getLabOrderReports().size());
		try {
			if (EzApp.patientDao
					.queryBuilder()
					.where(Properties.Pid.eq(apt.getPid()),
							Properties.Fid.eq(apt.getPfid())).count() > 0) {
				pat = (EzApp.patientDao
						.queryBuilder()
						.where(Properties.Pid.eq(apt.getPid()),
								Properties.Fid.eq(apt.getPfid())).list().get(0));
			}

		} catch (Exception e) {
			Log.e("", e);
		}

		TextView txtLabName = (TextView) dialogPrint
				.findViewById(R.id.txt_lab_name);
		TextView txtLabAddress = (TextView) dialogPrint
				.findViewById(R.id.txt_lab_address);
		TextView txtLabMobnum = (TextView) dialogPrint
				.findViewById(R.id.txt_phone);
		TextView txtLabLandline = (TextView) dialogPrint
				.findViewById(R.id.txt_landline);
		TextView txtPatientName = (TextView) dialogPrint
				.findViewById(R.id.txt_patient_name);
		TextView txtPatientAddress = (TextView) dialogPrint
				.findViewById(R.id.txt_patient_address);
		TextView txtDoctorName = (TextView) dialogPrint
				.findViewById(R.id.txt_doctor_name);
		TextView txtLabOrderId = (TextView) dialogPrint
				.findViewById(R.id.txt_lab_order_id_display);
		TextView txtDate = (TextView) dialogPrint
				.findViewById(R.id.txt_date_display);
		TextView txtPreparedBy = (TextView) dialogPrint
				.findViewById(R.id.txt_prepared_by_display);
		TextView txtTotal = (TextView) dialogPrint
				.findViewById(R.id.txt_total_display);
		TextView txtTechSignature = (TextView) dialogPrint
				.findViewById(R.id.txt_tech_signature_display);
		TextView txtCustomerSignature = (TextView) dialogPrint
				.findViewById(R.id.txt_customer_signature_display);
		TextView txtTax = (TextView) dialogPrint.findViewById(R.id.txt_tax);
		ImageView imgRupeetax = (ImageView) dialogPrint
				.findViewById(R.id.img_rupee_19);
		TextView txtTaxDisplay = (TextView) dialogPrint
				.findViewById(R.id.txt_tax_display);
		LinearLayout llTestName = (LinearLayout) dialogPrint
				.findViewById(R.id.ll_test_name);

		final Date date = apt.getAptdate();
		txtDate.setText(EzApp.sdfddMmyy.format(date));
		txtLabName.setText(EzApp.sharedPref.getString(
				Constants.LAB_NAME, ""));
		txtLabAddress.setText(EzApp.sharedPref.getString(
				Constants.LAB_ADDRESS, ""));
		txtPatientName.setText(pat.getPfn() + " " + pat.getPln() + ", "
				+ pat.getPage() + "/" + pat.getPgender());
		txtPatientAddress.setText(pat.getPadd1() + " " + pat.getPadd2() + ", "
				+ pat.getParea() + ", " + pat.getPcity() + ", "
				+ pat.getPstate() + ", " + pat.getPcountry() + " - "
				+ pat.getPzip());
		txtDoctorName.setText(apt.orderView.getLabOrderDetail().getDoctor());
		txtLabOrderId.setText(apt.orderView.getOrder().getOrder_display_id());
		txtPreparedBy.setText(LabsController.getLabTechnician(
				apt.orderView.getLabOrderDetail().getTechnicianId()).getName());
		txtTechSignature.setText(LabsController.getLabTechnician(
				apt.orderView.getLabOrderDetail().getTechnicianId()).getName());
		txtCustomerSignature.setText(pat.getPfn() + " " + pat.getPln());
		for (int i = 1; i <= apt.orderView.getOrder().getData()
				.getOrderProducts().size(); i++) {
			OrderProduct order = apt.orderView.getOrder().getData()
					.getOrderProducts().get(i - 1);

			final View v1 = inflater.inflate(
					R.layout.labs_row_print_orders_test_list, null);
			((TextView) v1.findViewById(R.id.txt_test_name)).setText(order
					.getName());
			((TextView) v1.findViewById(R.id.txt_type)).setText(order
					.getProductModel());
			((TextView) v1.findViewById(R.id.txt_price)).setText(Util
					.doubleFormat(order.getPrice()));
			if (!apt.orderView.getOrder().getTax_percentage().equals(0)) {
				txtTax.setVisibility(View.VISIBLE);
				txtTaxDisplay.setVisibility(View.VISIBLE);
				imgRupeetax.setVisibility(View.VISIBLE);
			}
			txtTax.setText("Tax @ "
					+ Util.doubleFormat(apt.orderView.getOrder()
							.getTax_percentage()) + " %");
			txtTaxDisplay.setText(Util.doubleFormat(apt.orderView.getOrder()
					.getTax_amount()));
			txtTotal.setText(""
					+ Util.doubleFormat(""
							+ apt.orderView.getOrder().getTotal_amount()));

			llTestName.addView(v1);
		}

		if (!Util.isEmptyString(EzApp.sharedPref.getString(
				Constants.SIGNATURE, "signature"))) {
			Util.getImageFromUrl(EzApp.sharedPref.getString(
					Constants.SIGNATURE, ""), DashboardActivity.imgLoader,
					(ImageView) dialogPrint
							.findViewById(R.id.img_lab_signature));
		}

		dialogPrint.setCancelable(false);
		dialogPrint.findViewById(R.id.txt_close).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						dialogPrint.dismiss();

					}
				});
		dialogPrint.findViewById(R.id.btn_print).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						FlurryAgent
								.logEvent("LabsOrderSuccessActivity - Print Order(Print) Button Clicked");
						PrintHelper photoPrinter = new PrintHelper(
								LabsOrderSuccessActivity.this);

						photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
						View view = (dialogPrint.findViewById(R.id.rl_print));
						view.setDrawingCacheEnabled(true);
						view.buildDrawingCache();
						Bitmap bm = view.getDrawingCache();
						// Bitmap bitmap =
						// BitmapFactory.decodeResource(getResources(),
						// R.drawable.rs_15);

						photoPrinter.printBitmap("Prescription.jpg", bm);
					}
				});

		dialogPrint.show();

	}

	public boolean onOptionsItemSelected(final MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {

		case android.R.id.home:
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

}

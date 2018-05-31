package com.ezhealthtrack.labs.activity;

import java.text.ParseException;
import java.util.Date;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.print.PrintHelper;
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

import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.controller.PatientController;
import com.ezhealthtrack.controller.PatientController.OnResponsePatient;
import com.ezhealthtrack.greendao.LabAppointment;
import com.ezhealthtrack.greendao.Order;
import com.ezhealthtrack.greendao.Patient;
import com.ezhealthtrack.greendao.PatientDao.Properties;
import com.ezhealthtrack.labs.controller.OrderController;
import com.ezhealthtrack.labs.controller.OrderController.OnResponseData;
import com.ezhealthtrack.order.LabOrderReport;
import com.ezhealthtrack.order.OrderAudit;
import com.ezhealthtrack.order.OrderProduct;
import com.ezhealthtrack.order.OrderView;
import com.ezhealthtrack.order.Result;
import com.ezhealthtrack.util.BaseActivity;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.NetworkCallController.OnResponse;
import com.ezhealthtrack.util.Util;
import com.flurry.android.FlurryAgent;

public class LabsBillPaymentHistoryActivity extends BaseActivity {
	private Button btnMakePayment;
	private Button btnPrintBill;
	private Button btnRefund;
	private TextView txtPname;
	private TextView txtOrderDate;
	private TextView txtOrder;
	private TextView txtOrderStatus;
	private TextView txtBillStatus;
	private TextView txtBillId;
	private TextView txtTotalLabTestAmount;
	private TextView txtDiscountView;
	private TextView txtDiscount;
	private TextView txtTaxView;
	private TextView txtTax;
	private TextView txtNetAmount;
	private TextView txtPayableAmount;
	private TextView txtTotalPaid;
	private TextView txtRefundView;
	private TextView txtRefund;
	private TextView txtBalance;
	private LabAppointment apt;
	private LinearLayout llPaymentHistory;
	private LayoutInflater inflater;
	private Patient pat = new Patient();
	private Result res = new Result();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.labs_activity_bill_payment_history);
		getActionBar().setHomeButtonEnabled(true);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		apt = new LabAppointment();
		Log.i("", "" + getIntent().getLongExtra("orderid", 0));
		apt.orderView.getOrder().setOrder_id(
				getIntent().getLongExtra("orderid", 0));
		apt.orderView.getOrder().setId(getIntent().getLongExtra("orderid", 0));
		final Dialog loaddialog = Util
				.showLoadDialog(LabsBillPaymentHistoryActivity.this);
		OrderController.getOrderHistory(this, new OnResponseData() {

			@Override
			public void onResponseListner(Object response) {
				apt.orderView = (OrderView) response;
				details();
				android.util.Log.i("",
						"" + apt.orderView.getOrder().orderAudit.size());
				loaddialog.dismiss();
			}
		}, apt.orderView, "" + apt.orderView.getOrder().getOrder_id());

		txtPname = (TextView) findViewById(R.id.txt_name_display);
		txtOrderDate = (TextView) findViewById(R.id.txt_order_date_display);
		txtOrder = (TextView) findViewById(R.id.txt_order_display);
		txtOrderStatus = (TextView) findViewById(R.id.txt_order_status_display);
		txtBillStatus = (TextView) findViewById(R.id.txt_bill_status_display);
		txtBillId = (TextView) findViewById(R.id.txt_bill_id_display);
		txtTotalLabTestAmount = (TextView) findViewById(R.id.txt_original_amount_display);
		txtDiscount = (TextView) findViewById(R.id.txt_discount);
		txtDiscountView = (TextView) findViewById(R.id.txt_discount_display);
		txtTax = (TextView) findViewById(R.id.txt_tax);
		txtTaxView = (TextView) findViewById(R.id.txt_tax_display);
		txtPayableAmount = (TextView) findViewById(R.id.txt_payable_display);
		txtNetAmount = (TextView) findViewById(R.id.txt_net_display);
		txtTotalPaid = (TextView) findViewById(R.id.txt_total_paid_display);
		txtRefund = (TextView) findViewById(R.id.txt_refund);
		txtRefundView = (TextView) findViewById(R.id.txt_refund_display_2);
		txtBalance = (TextView) findViewById(R.id.txt_balance_display_2);
		btnMakePayment = (Button) findViewById(R.id.btn_make_payment);
		btnPrintBill = (Button) findViewById(R.id.btn_print_bill);
		btnRefund = (Button) findViewById(R.id.btn_refund);
		llPaymentHistory = (LinearLayout) findViewById(R.id.ll_payment_history);

		btnMakePayment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FlurryAgent
						.logEvent("LabsBillPaymentHistoryActivity - Bill Payment Button Clicked");
				final Dialog loaddialog = Util
						.showLoadDialog(LabsBillPaymentHistoryActivity.this);
				Intent intent = new Intent(LabsBillPaymentHistoryActivity.this,
						LabsBillPaymentActivity.class);
				intent.putExtra("orderid", apt.orderView.getOrder()
						.getOrder_id());
				startActivity(intent);
				loaddialog.dismiss();

			}
		});

		btnPrintBill.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FlurryAgent
						.logEvent("LabsBillPaymentHistoryActivity - Print Bill Button Clicked");
				dialogPrintBill();

			}
		});

		btnRefund.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FlurryAgent
						.logEvent("LabsBillPaymentHistoryActivity - Bill Refund Button Clicked");
				final Dialog loaddialog = Util
						.showLoadDialog(LabsBillPaymentHistoryActivity.this);
				Intent intent = new Intent(LabsBillPaymentHistoryActivity.this,
						LabsBillRefundActivity.class);
				intent.putExtra("orderid", apt.orderView.getOrder()
						.getOrder_id());
				startActivity(intent);
				loaddialog.dismiss();

			}
		});
	}

	protected void details() {
		Patient pat1 = new Patient();
		try {
			if (EzApp.patientDao
					.queryBuilder()
					.where(Properties.Pid.eq(apt.orderView.getOrder()
							.getCustomer_id())).count() > 0) {
				pat1 = (EzApp.patientDao
						.queryBuilder()
						.where(Properties.Pid.eq(apt.orderView.getOrder()
								.getCustomer_id())).list().get(0));
			}

		} catch (Exception e) {
			Log.e("", e);
		}
		try {
			PatientController.getPatient(pat1.getPid(), this,
					new OnResponsePatient() {

						@Override
						public void onResponseListner(Patient response) {
							pat = response;

						}
					});
		} catch (Exception e) {

		}

		if (Double.parseDouble(apt.orderView.getOrder().getRefund()) <= 0) {
			txtRefund.setVisibility(View.GONE);
			txtRefundView.setVisibility(View.GONE);
			ImageView imgRupee = (ImageView) findViewById(R.id.img_rupee_8);
			imgRupee.setVisibility(View.GONE);
		} else {
			txtRefund.setVisibility(View.VISIBLE);
			txtRefundView.setVisibility(View.VISIBLE);
			ImageView imgRupee = (ImageView) findViewById(R.id.img_rupee_8);
			imgRupee.setVisibility(View.VISIBLE);
		}

		if (Util.isEmptyString(apt.orderView.getOrder().getRefund_amount())) {
			apt.orderView.getOrder().setRefund_amount("0.0");
		}

		if (Double.parseDouble(apt.orderView.getOrder().getBalance()) > 0
				&& (apt.orderView.getOrder().getBill_status_id()
						.equals(Order.BILL_STATUS_NOT_PAID) || apt.orderView
						.getOrder().getBill_status_id()
						.equals(Order.BILL_STATUS_PARTIALLY_PAID))) {
			btnMakePayment.setVisibility(View.VISIBLE);
			btnPrintBill.setVisibility(View.GONE);
			btnRefund.setVisibility(View.GONE);
		} else if (apt.orderView.getOrder().getBill_status_id()
				.equals(Order.BILL_STATUS_FULLY_PAID)
				|| apt.orderView.getOrder().getBill_status_id()
						.equals(Order.BILL_STATUS_REFUND)) {
			btnMakePayment.setVisibility(View.GONE);
			btnPrintBill.setVisibility(View.VISIBLE);
			btnRefund.setVisibility(View.GONE);
		} else if (Double.parseDouble(apt.orderView.getOrder()
				.getRefund_amount()) == 0
				&& apt.orderView.getOrder().getBill_status_id()
						.equals(Order.BILL_STATUS_CANCEL)) {
			btnMakePayment.setVisibility(View.GONE);
			btnPrintBill.setVisibility(View.GONE);
			btnRefund.setVisibility(View.VISIBLE);
		}

		if (Util.isEmptyString(apt.orderView.getOrder().getTotal())) {
			apt.orderView.getOrder().setTotal("0.0");
		}

		final Date date = apt.orderView.getOrder().getDate_added();
		txtOrderDate.setText(EzApp.sdfddMmyy.format(date));
		txtTotalLabTestAmount.setText(""
				+ Util.doubleFormat(""
						+ apt.orderView.getOrder().getBillSummary()
								.getOriginalAmount()));
		txtDiscount.setText("Discount @ "
				+ apt.orderView.getOrder().getDiscount_percentage() + " % :");
		txtDiscountView.setText(""
				+ Util.doubleFormat(""
						+ apt.orderView.getOrder().getBillSummary()
								.getDiscountAmount()));
		txtTax.setText("Tax @ " + apt.orderView.getOrder().getTax_percentage()
				+ " % :");
		txtTaxView.setText(""
				+ Util.doubleFormat(""
						+ apt.orderView.getOrder().getBillSummary()
								.getTaxAmount()));
		txtNetAmount.setText(""
				+ Util.doubleFormat(""
						+ apt.orderView.getOrder().getBillSummary()
								.getTotalAmount()));
		txtPayableAmount
				.setText(""
						+ Util.doubleFormat(apt.orderView.getOrder()
								.getTotal_amount()));
		txtTotalPaid.setText(""
				+ Util.doubleFormat(apt.orderView.getOrder().getTotalPaid()));
		txtBalance.setText(""
				+ Util.doubleFormat(""
						+ apt.orderView.getOrder().getBalance_amount()));
		txtPname.setText(pat1.getP_detail());
		txtOrder.setText(apt.orderView.getOrder().getOrder_display_id());
		txtBillId.setText(apt.orderView.getOrder().getOrder_display_id()
				+ "-01");
		txtRefundView.setText(Util.doubleFormat(apt.orderView.getOrder()
				.getRefund()));
		txtOrderStatus.setText(Order.getOrderStatus(apt.orderView.getOrder()
				.getOrder_status_id()));
		txtBillStatus.setText(Order.getBillStatus(apt.orderView.getOrder()
				.getBill_status_id()));
		paymentHistoryList();

		txtOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FlurryAgent
						.logEvent("LabsBillPaymentHistoryActivity - Order Details Button Clicked");
				final Dialog loaddialog = Util
						.showLoadDialog(LabsBillPaymentHistoryActivity.this);
				OrderController.getOrderList("1",
						LabsBillPaymentHistoryActivity.this, new OnResponse() {

							@Override
							public void onResponseListner(String response) {
								Intent intent = new Intent(
										LabsBillPaymentHistoryActivity.this,
										LabsOrderDetailsActivity.class);
								intent.putExtra("orderid", apt.orderView
										.getOrder().getOrder_id());
								startActivity(intent);
								loaddialog.dismiss();
							}
						}, null, null, null, -1, -1, "");
			}
		});

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

	private void paymentHistoryList() {
		llPaymentHistory.removeAllViews();
		for (int i = 1; i <= apt.orderView.getOrder().orderAudit.size(); i++) {
			OrderAudit audit = apt.orderView.getOrder().orderAudit.get(i - 1);
			final View v1 = inflater.inflate(R.layout.labs_row_payment_history,
					null);
			final Date date;
			try {
				date = EzApp.sdfyymmddhhmmss.parse(audit
						.getPaidOn());

				((TextView) v1.findViewById(R.id.txt_date))
						.setText(EzApp.sdfddmmyyhhms.format(date));
				((TextView) v1.findViewById(R.id.txt_payment_method))
						.setText(audit.getPaymentMethod());
				Drawable img = getResources().getDrawable(R.drawable.rupee);
				img.setBounds(0, 0, 30, 30);
				if (audit.getPaidAmount().contains("-")) {
					((TextView) v1.findViewById(R.id.txt_refund))
							.setCompoundDrawables(img, null, null, null);
					((TextView) v1.findViewById(R.id.txt_refund)).setText(Util
							.doubleFormat(audit.getPaidAmount()
									.replace("-", "")));
				} else {
					((TextView) v1.findViewById(R.id.txt_amount_paid))
							.setText(Util.doubleFormat(audit.getPaidAmount()));
					((TextView) v1.findViewById(R.id.txt_amount_paid))
							.setCompoundDrawables(img, null, null, null);
				}
				if (!Util.isEmptyString(audit.getComment()))
					((TextView) v1.findViewById(R.id.txt_note)).setText(audit
							.getComment());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			llPaymentHistory.addView(v1);

		}
	}

	private void dialogPrintBill() {
		final Dialog dialogPrint = new Dialog(this);
		dialogPrint.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialogPrint.setContentView(R.layout.labs_dialog_print_bill);
		dialogPrint.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		LayoutParams params = dialogPrint.getWindow().getAttributes();
		params.width = 2000;
		dialogPrint.getWindow().setAttributes(
				(android.view.WindowManager.LayoutParams) params);
		TextView txtLabName = (TextView) dialogPrint
				.findViewById(R.id.txt_lab_name);
		TextView txtLabAddress = (TextView) dialogPrint
				.findViewById(R.id.txt_lab_address);
		TextView txtLabPhone = (TextView) dialogPrint
				.findViewById(R.id.txt_phone);
		TextView txtPatientName = (TextView) dialogPrint
				.findViewById(R.id.txt_patient_name);
		TextView txtPatientAddress = (TextView) dialogPrint
				.findViewById(R.id.txt_patient_address);
		TextView txtPatientPhone = (TextView) dialogPrint
				.findViewById(R.id.txt_patient_phone);
		TextView txtPatientEmail = (TextView) dialogPrint
				.findViewById(R.id.txt_patient_email);
		TextView txtDoctorName = (TextView) dialogPrint
				.findViewById(R.id.txt_doctor_name);
		TextView txtBillId = (TextView) dialogPrint
				.findViewById(R.id.txt_bill_id_display);
		TextView txtDate = (TextView) dialogPrint
				.findViewById(R.id.txt_date_display);
		TextView txtOrderId = (TextView) dialogPrint
				.findViewById(R.id.txt_order_id_display);
		TextView txtOrderStatus = (TextView) dialogPrint
				.findViewById(R.id.txt_order_status_display);
		TextView txtDiscount = (TextView) dialogPrint
				.findViewById(R.id.txt_discount);
		TextView txtDiscountView = (TextView) dialogPrint
				.findViewById(R.id.txt_discount_display);
		TextView txtTax = (TextView) dialogPrint
				.findViewById(R.id.txt_tax_text);
		TextView txtTaxView = (TextView) dialogPrint
				.findViewById(R.id.txt_tax_view);
		TextView txtTotal = (TextView) dialogPrint
				.findViewById(R.id.txt_total_view);

		try {
			final Date date = apt.orderView.getOrder().getDate_added();
			txtDate.setText(EzApp.sdfddMmyy.format(date));
			txtLabName.setText(EzApp.sharedPref.getString(
					Constants.LAB_NAME, ""));
			txtLabAddress.setText(EzApp.sharedPref.getString(
					Constants.LAB_ADDRESS, ""));
			txtPatientName.setText(pat.getP_detail());
			txtPatientAddress.setText(pat.getPadd1() + " " + pat.getPadd2()
					+ ", " + pat.getParea() + ", " + pat.getPcity() + ", "
					+ pat.getPstate() + ", " + pat.getPcountry() + " - "
					+ pat.getPzip());
			txtPatientPhone.setText(pat.getPmobnum());
			txtPatientEmail.setText(pat.getPemail());
			txtDoctorName
					.setText(apt.orderView.getLabOrderDetail().getDoctor());
			txtBillId.setText(apt.orderView.getOrder().getOrder_display_id()
					+ "-01");
			txtOrderId.setText(apt.orderView.getOrder().getOrder_display_id());
			txtOrderStatus.setText(Order.getOrderStatus(apt.orderView
					.getOrder().getOrder_status_id()));
			txtDiscount.setText("Discount @ "
					+ apt.orderView.getOrder().getDiscount_percentage() + " %");
			txtDiscountView.setText(Util.doubleFormat(apt.orderView.getOrder()
					.getDiscount_amount()));
			txtTax.setText("Tax @ "
					+ apt.orderView.getOrder().getTax_percentage() + " %");
			txtTaxView.setText(Util.doubleFormat(apt.orderView.getOrder()
					.getTax_amount()));
			txtTotal.setText(""
					+ Util.doubleFormat(""
							+ apt.orderView.getOrder().getBillSummary()
									.getTotalAmount()));
			
//			for(int i = 1; i<=)

			LinearLayout llOrderDetails = (LinearLayout) dialogPrint
					.findViewById(R.id.ll_order_details);
			for (int i = 1; i <= apt.orderView.getOrder().getData()
					.getOrderProducts().size(); i++) {
				OrderProduct order = apt.orderView.getOrder().getData()
						.getOrderProducts().get(i - 1);
				final View v = inflater.inflate(
						R.layout.labs_row_print_bill_order_list, null);
				((TextView) v.findViewById(R.id.txt_id)).setText(i + ".");
				((TextView) v.findViewById(R.id.txt_test_name)).setText(order
						.getName());
				((TextView) v.findViewById(R.id.txt_price)).setText(Util
						.doubleFormat(order.getPrice()));

				llOrderDetails.addView(v);
			}

			LinearLayout llPaymentHistory = (LinearLayout) dialogPrint
					.findViewById(R.id.ll_test_name);
			llPaymentHistory.removeAllViews();
			for (int i = 1; i <= apt.orderView.getOrder().orderAudit.size(); i++) {
				OrderAudit audit = apt.orderView.getOrder().orderAudit
						.get(i - 1);
				final View v1 = inflater.inflate(
						R.layout.labs_row_print_bill_list, null);
				final Date date1;
				try {
					date1 = EzApp.sdfyymmddhhmmss.parse(audit
							.getPaidOn());

					((TextView) v1.findViewById(R.id.txt_date))
							.setText(EzApp.sdfddmmyyhhms
									.format(date1));
					((TextView) v1.findViewById(R.id.txt_payment_method))
							.setText(audit.getPaymentMethod());
					Drawable img = getResources().getDrawable(R.drawable.rupee);
					img.setBounds(0, 0, 30, 30);

					((TextView) v1.findViewById(R.id.txt_amount)).setText(Util
							.doubleFormat(audit.getPaidAmount()));
					((TextView) v1.findViewById(R.id.txt_amount))
							.setCompoundDrawables(img, null, null, null);

					if (audit.getPaidAmount().contains("-")) {
						((TextView) v1.findViewById(R.id.txt_amount))
								.setTextColor(Color.RED);
					} else {
						((TextView) v1.findViewById(R.id.txt_amount))
								.setTextColor(Color.BLACK);
						;
					}

					((TextView) v1.findViewById(R.id.txt_balance)).setText(""
							+ Util.doubleFormat(audit.getBalanceAmount()));
					((TextView) v1.findViewById(R.id.txt_balance))
							.setCompoundDrawables(img, null, null, null);

					if (!Util.isEmptyString(audit.getComment()))
						((TextView) v1.findViewById(R.id.txt_note))
								.setText(audit.getComment());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				llPaymentHistory.addView(v1);

			}

		} catch (Exception e) {

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
								.logEvent("LabsBillPaymentHistoryActivity - Print Bill(Print) Button Clicked");
						PrintHelper photoPrinter = new PrintHelper(
								LabsBillPaymentHistoryActivity.this);

						photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
						View view = (dialogPrint.findViewById(R.id.rl_print));
						view.setDrawingCacheEnabled(true);
						view.buildDrawingCache();
						Bitmap bm = view.getDrawingCache();
						// Bitmap bitmap =
						// BitmapFactory.decodeResource(getResources(),
						// R.drawable.rs_15);

						photoPrinter.printBitmap("Order "
								+ apt.orderView.getOrder()
										.getOrder_display_id(), bm);
					}
				});

		dialogPrint.show();

	}
}

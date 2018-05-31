package com.ezhealthtrack.labs.activity;

import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.greendao.LabAppointment;
import com.ezhealthtrack.greendao.Order;
import com.ezhealthtrack.greendao.Patient;
import com.ezhealthtrack.greendao.PatientDao.Properties;
import com.ezhealthtrack.labs.controller.OrderController;
import com.ezhealthtrack.labs.controller.OrderController.OnResponseData;
import com.ezhealthtrack.order.OrderView;
import com.ezhealthtrack.util.BaseActivity;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.Util;
import com.ezhealthtrack.util.NetworkCallController.OnResponse;
import com.flurry.android.FlurryAgent;

public class LabsBillRefundActivity extends BaseActivity {

	private LabAppointment apt;
	private TextView txtPaymentHistory;
	private TextView txtPname;
	private TextView txtOrderDate;
	private TextView txtOrder;
	private TextView txtOrderStatus;
	private TextView txtBillStatus;
	private TextView txtBillId;
	private TextView txtPaybleBalanceAmt;
	private TextView txtBalanceAmt;
	private Spinner spinnerPaymentMethod;
	private EditText editRefundAmt;
	private EditText editNote;
	private ArrayList<String> arrPaymentMethod = new ArrayList<String>();
	private Patient pat1 = new Patient();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.labs_activity_bill_refund);
		getActionBar().setHomeButtonEnabled(true);
		apt = new LabAppointment();
		Log.i("", "" + getIntent().getLongExtra("orderid", 0));
		apt.orderView.getOrder().setOrder_id(
				getIntent().getLongExtra("orderid", 0));
		apt.orderView.getOrder().setId(getIntent().getLongExtra("orderid", 0));
		final Dialog loaddialog = Util
				.showLoadDialog(LabsBillRefundActivity.this);
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
		arrPaymentMethod.clear();
		arrPaymentMethod.add("Cash");
		arrPaymentMethod.add("Cheque");
		arrPaymentMethod.add("Credit Card");
		arrPaymentMethod.add("Money Order");
		arrPaymentMethod.add("Adjust");

		txtPname = (TextView) findViewById(R.id.txt_name_display);
		txtOrderDate = (TextView) findViewById(R.id.txt_order_date_display);
		txtOrder = (TextView) findViewById(R.id.txt_order_display);
		txtOrderStatus = (TextView) findViewById(R.id.txt_order_status_display);
		txtBillStatus = (TextView) findViewById(R.id.txt_bill_status_display);
		txtBillId = (TextView) findViewById(R.id.txt_bill_id_display);
		txtPaybleBalanceAmt = (TextView) findViewById(R.id.txt_payable_balance_amount_display);
		txtBalanceAmt = (TextView) findViewById(R.id.txt_balance_amount_display);
		txtPaymentHistory = (TextView) findViewById(R.id.txt_payment_history);
		editNote = (EditText) findViewById(R.id.edit_notes);

		spinnerPaymentMethod = (Spinner) findViewById(R.id.spinner_payment_method);

		final ArrayAdapter<String> adapterPaymentMethod = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, arrPaymentMethod);
		adapterPaymentMethod
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerPaymentMethod.setAdapter(adapterPaymentMethod);
		editRefundAmt = (EditText) findViewById(R.id.edit_refund_amount);
		editRefundAmt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				try {
					double d = Double.parseDouble(txtPaybleBalanceAmt.getText()
							.toString()) + Double.parseDouble(s.toString());
					txtBalanceAmt.setText("" + Util.doubleFormat("" + d));
				} catch (Exception e) {
					txtBalanceAmt.setText(Util.doubleFormat(apt.orderView
							.getOrder().getBalance_amount()));
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		txtPaymentHistory.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FlurryAgent
						.logEvent("LabsBillRefundActivity - Bill Payment History Button Clicked");
				Intent intent = new Intent(LabsBillRefundActivity.this,
						LabsBillPaymentHistoryActivity.class);
				intent.putExtra("orderid", apt.orderView.getOrder()
						.getOrder_id());
				startActivity(intent);

			}
		});

		txtOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FlurryAgent
						.logEvent("LabsBillRefundActivity - Order Details Button Clicked");
				final Dialog loaddialog = Util
						.showLoadDialog(LabsBillRefundActivity.this);
				OrderController.getOrderList("1", LabsBillRefundActivity.this,
						new OnResponse() {

							@Override
							public void onResponseListner(String response) {
								Intent intent = new Intent(
										LabsBillRefundActivity.this,
										LabsOrderDetailsActivity.class);
								intent.putExtra("orderid", apt.orderView
										.getOrder().getOrder_id());
								startActivity(intent);
								loaddialog.dismiss();
							}
						}, null, null, null, -1, -1, "");
			}
		});

		txtBillId.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FlurryAgent
						.logEvent("LabsBillRefundActivity - Bill Payment History Button Clicked");
				Intent intent = new Intent(LabsBillRefundActivity.this,
						LabsBillPaymentHistoryActivity.class);
				intent.putExtra("orderid", apt.orderView.getOrder()
						.getOrder_id());
				startActivity(intent);

			}
		});

		findViewById(R.id.btn_submit).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FlurryAgent
						.logEvent("LabsBillRefundActivity - Submit Button Clicked");
				if (Util.isEmptyString(editRefundAmt.getText().toString())) {
					final Dialog loaddialog = Util
							.showLoadDialog(LabsBillRefundActivity.this);
					OrderController.refundSubmit(LabsBillRefundActivity.this,
							new OnResponseData() {

								@Override
								public void onResponseListner(Object response) {
									Intent intent = new Intent(
											LabsBillRefundActivity.this,
											LabsBillPaymentHistoryActivity.class);
									intent.putExtra("orderid", apt.orderView
											.getOrder().getOrder_id());
									startActivity(intent);
									loaddialog.dismiss();
								}
							}, apt.orderView.getOrder(), getIntent()
									.getStringExtra("reportid"), editNote
									.getText().toString(), editRefundAmt
									.getText().toString(), txtBalanceAmt
									.getText().toString(), spinnerPaymentMethod
									.getSelectedItem().toString());

				} else {
					Util.Alertdialog(LabsBillRefundActivity.this,
							"Only Numeric Value allowed in Refund Amount");
				}
			}
		});

	}

	protected void details() {
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
			final Date date = apt.orderView.getOrder().getDate_added();
			;
			txtOrderDate.setText(EzApp.sdfddMmyy.format(date));
			txtPname.setText(pat1.getP_detail());
			txtOrder.setText(apt.orderView.getOrder().getOrder_display_id());
			txtOrderStatus.setText(Order.getOrderStatus(apt.orderView
					.getOrder().getOrder_status_id()));
			txtBillStatus.setText(Order.getBillStatus(apt.orderView.getOrder()
					.getBill_status_id()));
			txtBillId.setText(apt.orderView.getOrder().getOrder_display_id()
					+ "-01");
			txtPaybleBalanceAmt.setText(Util.doubleFormat(apt.orderView
					.getOrder().getBalance_amount()));
			txtBalanceAmt.setText(Util.doubleFormat(apt.orderView.getOrder()
					.getBalance_amount()));
			((TextView) findViewById(R.id.txt_amount_due_display))
					.setText(Util.doubleFormat(apt.orderView.getOrder()
							.getBalance_amount()));
			((TextView) findViewById(R.id.txt_original_amount_due_display))
					.setText(Util.doubleFormat(apt.orderView.getOrder()
							.getTotal_amount()));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public boolean onOptionsItemSelected(final MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {

		case android.R.id.home:
			finish();
			return true;
		case R.id.action_close:
			final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);

			// set title
			alertDialogBuilder
					.setTitle("Changes will be discarded, Do you want to continue ?");
			// set dialog message
			alertDialogBuilder
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish();

								}

							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(
										final DialogInterface dialog,
										final int id) {
									// if this button is clicked, just close
									// the dialog box and do nothing
									dialog.cancel();
								}
							});

			final AlertDialog alertDialog = alertDialogBuilder.create();

			alertDialog.show();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);

		// set title
		alertDialogBuilder
				.setTitle("Changes will be discarded, Do you want to continue ?");
		// set dialog message
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								finish();

							}

						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog,
							final int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});

		final AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();

	}

}

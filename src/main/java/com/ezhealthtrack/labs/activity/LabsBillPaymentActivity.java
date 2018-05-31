package com.ezhealthtrack.labs.activity;

import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ezhealthrack.api.NetworkCalls;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.controller.PatientController;
import com.ezhealthtrack.controller.PatientController.OnResponsePatient;
import com.ezhealthtrack.greendao.LabAppointment;
import com.ezhealthtrack.greendao.Order;
import com.ezhealthtrack.greendao.Patient;
import com.ezhealthtrack.labs.controller.OrderController;
import com.ezhealthtrack.labs.controller.OrderController.OnResponseData;
import com.ezhealthtrack.labs.fragments.SideFragment.OnActionSelectedListner;
import com.ezhealthtrack.order.OrderAudit;
import com.ezhealthtrack.order.OrderView;
import com.ezhealthtrack.util.BaseActivity;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.NetworkCallController.OnResponse;
import com.ezhealthtrack.util.Util;
import com.flurry.android.FlurryAgent;

public class LabsBillPaymentActivity extends BaseActivity implements
		OnActionSelectedListner, NetworkCalls.OnResponse {
	private TextView txtPaymentHistory;
	private TextView txtPname;
	private TextView txtOrderDate;
	private TextView txtOrder;
	private TextView txtBillStatus;
	private TextView txtOrderStatus;
	private TextView txtBillId;
	private TextView txtAmountDue;
	private TextView txtOriginalAmountDue;
	private TextView txtTotalAmount;
	private EditText txtDiscount;
	private TextView txtTax;
	private TextView txtTaxView;
	private TextView txtPayableAmount;
	private TextView txtBalanceAmount;
	private EditText editPayingAmount;
	private EditText editDiscount;
	private EditText editNotes;
	private Spinner spinnerDiscount;
	private Spinner spinnerPaymentMethod;
	private Button btnSubmit;
	private RelativeLayout rlMain;
	private LabAppointment apt;
	private ArrayList<String> arrPaymentMethod = new ArrayList<String>();
	private ArrayList<String> arrDiscount = new ArrayList<String>();
	private Patient pat1 = new Patient();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.labs_activity_bill_payment);
		getActionBar().setHomeButtonEnabled(true);

		apt = new LabAppointment();
		Log.i("", "" + getIntent().getLongExtra("orderid", 0));
		apt.orderView.getOrder().setOrder_id(
				getIntent().getLongExtra("orderid", 0));
		apt.orderView.getOrder().setId(getIntent().getLongExtra("orderid", 0));
		final Dialog loaddialog = Util
				.showLoadDialog(LabsBillPaymentActivity.this);
		OrderController.getOrderHistory(this, new OnResponseData() {

			@Override
			public void onResponseListner(Object response) {
				apt.orderView = (OrderView) response;
				paymentChanges();
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

		arrDiscount.clear();
		arrDiscount.add("%");
		arrDiscount.add("Rupees");

		spinnerPaymentMethod = (Spinner) findViewById(R.id.spinner_payment_method);
		spinnerDiscount = (Spinner) findViewById(R.id.spinner_discount);
		txtPname = (TextView) findViewById(R.id.txt_name_display);
		txtOrderDate = (TextView) findViewById(R.id.txt_order_date_display);
		txtOrder = (TextView) findViewById(R.id.txt_order_display);
		txtBillStatus = (TextView) findViewById(R.id.txt_bill_status_display);
		txtOrderStatus = (TextView) findViewById(R.id.txt_order_status_display);
		txtBillId = (TextView) findViewById(R.id.txt_bill_id_display);
		txtAmountDue = (TextView) findViewById(R.id.txt_amount_due_display);
		txtOriginalAmountDue = (TextView) findViewById(R.id.txt_original_amount_due_display);
		txtPaymentHistory = (TextView) findViewById(R.id.txt_payment_history);
		txtTotalAmount = (TextView) findViewById(R.id.txt_total_amount_display);
		txtDiscount = (EditText) findViewById(R.id.txt_discount_display);
		txtTaxView = (TextView) findViewById(R.id.txt_tax_display);
		txtTax = (TextView) findViewById(R.id.txt_tax);
		txtPayableAmount = (TextView) findViewById(R.id.txt_payable_amount_display);
		txtBalanceAmount = (TextView) findViewById(R.id.txt_balance_amount_display);
		editPayingAmount = (EditText) findViewById(R.id.edit_paying_amount);
		editDiscount = (EditText) findViewById(R.id.edit_discount);
		editNotes = (EditText) findViewById(R.id.edit_notes);
		btnSubmit = (Button) findViewById(R.id.btn_submit);
		rlMain = (RelativeLayout) findViewById(R.id.rl_scroll);

		final ArrayAdapter<String> adapterPaymentMethod = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, arrPaymentMethod);
		adapterPaymentMethod
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerPaymentMethod.setAdapter(adapterPaymentMethod);

		final ArrayAdapter<String> adapterDiscount = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, arrDiscount);
		adapterDiscount
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerDiscount.setAdapter(adapterDiscount);
		spinnerDiscount.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (spinnerDiscount.getSelectedItemPosition() == 0) {
					editDiscount.setEnabled(true);
					txtDiscount.setEnabled(false);
					txtDiscount.setText("0.00");
					editDiscount
							.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
									3) });
				} else {
					editDiscount.setEnabled(false);
					txtDiscount.setEnabled(true);
					editDiscount
							.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
									5) });
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// editDiscount.setEnabled(true);
				// txtDiscount.setEnabled(false);

			}
		});

		txtDiscount.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				try {
					if (!Util.isEmptyString("" + arg0)) {
						Double dis = Double.parseDouble(Util
								.doubleFormat(txtDiscount.getText().toString()));
						Double dis1 = Double.parseDouble(Util
								.doubleFormat(txtTotalAmount.getText()
										.toString()));
						if (dis <= dis1) {
							Double discount = 100
									* Double.parseDouble(Util
											.doubleFormat(txtDiscount.getText()
													.toString()))
									/ Double.parseDouble(Util
											.doubleFormat(txtTotalAmount
													.getText().toString()));
							if (!editDiscount.isEnabled())
								editDiscount.setText(Util.doubleFormat(""
										+ discount));

						} else {
							Util.Alertdialog(LabsBillPaymentActivity.this,
									"Discount Amount cannot be more than "
											+ dis1);
						}
					} else {
						if (!editDiscount.isEnabled())
							editDiscount.setText("0.00");
					}
				} catch (Exception e) {
					if (!editDiscount.isEnabled())
						editDiscount.setText("0.00");
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});

		editDiscount.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				try {
					Double dis = Double.parseDouble(editDiscount.getText()
							.toString());
					if (dis <= 100) {
						Double discount = Double.parseDouble(Util
								.doubleFormat(txtTotalAmount.getText()
										.toString()))
								* Double.valueOf(Util.doubleFormat(arg0
										.toString())) / 100;
						if (!txtDiscount.isEnabled())
							txtDiscount.setText(""
									+ Util.doubleFormat("" + discount));

						if (Util.isEmptyString(txtDiscount.getText().toString())) {
							double d = Double.parseDouble(Util
									.doubleFormat(txtTotalAmount.getText()
											.toString()))
									* Double.parseDouble(apt.orderView
											.getOrder().getTax_percentage())
									/ 100;
							txtTaxView.setText("" + Util.doubleFormat("" + d));
						} else {
							double d1 = Double.parseDouble(Util
									.doubleFormat(txtTotalAmount.getText()
											.toString()))
									- Double.parseDouble(Util
											.doubleFormat(txtDiscount.getText()
													.toString()));
							double d2 = d1
									* Double.parseDouble(Util
											.doubleFormat(apt.orderView
													.getOrder()
													.getTax_percentage()))
									/ 100;

							txtTaxView.setText("" + Util.doubleFormat("" + d2));
						}
						Double paybleAmount;
						if (!txtDiscount.isEnabled())
							paybleAmount = Double.parseDouble(txtTotalAmount
									.getText().toString())
									- Double.parseDouble("" + discount)
									+ Double.parseDouble(Util
											.doubleFormat(txtTaxView.getText()
													.toString()));
						else
							paybleAmount = Double.parseDouble(txtTotalAmount
									.getText().toString())
									- Double.parseDouble(txtDiscount.getText()
											.toString())
									+ Double.parseDouble(Util
											.doubleFormat(txtTaxView.getText()
													.toString()));

						txtPayableAmount.setText(""
								+ Util.doubleFormat("" + paybleAmount));
						editPayingAmount.setText(""
								+ Util.doubleFormat("" + paybleAmount));
					} else {
						Util.Alertdialog(LabsBillPaymentActivity.this,
								"Discount Percentage cannot be more than 100.");
					}
				} catch (Exception e) {
					if (!txtDiscount.isEnabled())
						txtDiscount.setText("" + Util.doubleFormat("0.00"));
				}

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});

		editDiscount
				.setText(""
						+ Util.doubleFormat(""
								+ apt.orderView.getOrder().getDiscount()));

		editPayingAmount.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				try {
					if (Double.parseDouble(editPayingAmount.getText()
							.toString()) > Double.parseDouble(txtPayableAmount
							.getText().toString())) {
						Util.Alertdialog(LabsBillPaymentActivity.this,
								"Paying Amount cannot be greater than Payable Amount");
					} else if (editDiscount.getVisibility() == View.GONE) {
						double d = Double.parseDouble(txtPayableAmount
								.getText().toString())
								- Double.parseDouble(Util.doubleFormat(s
										.toString()));
						if (d < 0) {
							txtBalanceAmount.setText("0.00");
						} else {
							txtBalanceAmount.setText(""
									+ Util.doubleFormat("" + d));
						}
					} else {
						double d = Double.parseDouble(txtTotalAmount.getText()
								.toString())
								- Double.parseDouble(Util.doubleFormat(s
										.toString()))
								- Double.parseDouble(txtDiscount.getText()
										.toString());
						Double dd = d
								+ Double.parseDouble(txtTaxView.getText()
										.toString());
						if (dd < 0) {
							txtBalanceAmount.setText("0.00");
						} else {
							txtBalanceAmount.setText(""
									+ Util.doubleFormat("" + dd));
						}
					}
				} catch (Exception e) {
					txtBalanceAmount.setText("0.00");
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

		txtBalanceAmount.setText("" + apt.orderView.getOrder().balanceAmount);

		txtOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FlurryAgent
						.logEvent("LabsBillPaymentActivity - Order Details Button Clicked");
				final Dialog loaddialog = Util
						.showLoadDialog(LabsBillPaymentActivity.this);
				OrderController.getOrderList("1", LabsBillPaymentActivity.this,
						new OnResponse() {

							@Override
							public void onResponseListner(String response) {
								Intent intent = new Intent(
										LabsBillPaymentActivity.this,
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
						.logEvent("LabsBillPaymentActivity - Bill Payment History Button Clicked");
				Intent intent = new Intent(LabsBillPaymentActivity.this,
						LabsBillPaymentHistoryActivity.class);
				intent.putExtra("orderid", apt.orderView.getOrder()
						.getOrder_id());
				startActivity(intent);

			}
		});

		txtPaymentHistory.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FlurryAgent
						.logEvent("LabsBillPaymentActivity - Bill Payment History Button Clicked");
				Intent intent = new Intent(LabsBillPaymentActivity.this,
						LabsBillPaymentHistoryActivity.class);
				intent.putExtra("orderid", apt.orderView.getOrder()
						.getOrder_id());
				startActivity(intent);

			}
		});

		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					if (Util.isNumeric(editDiscount.getText().toString())) {
						if (Util.isNumeric(txtDiscount.getText().toString())) {
							if (Util.isNumeric(editPayingAmount.getText()
									.toString())) {
								Double dis = Double.parseDouble(editDiscount
										.getText().toString());
								if (dis <= 100) {
									if (Double.parseDouble(editPayingAmount
											.getText().toString()) > Double
											.parseDouble(txtPayableAmount
													.getText().toString())) {
										Util.Alertdialog(
												LabsBillPaymentActivity.this,
												"Paying Amount cannot be greater than Payable Amount");
									} else {
										FlurryAgent
												.logEvent("LabsBillPaymentActivity - Submit Button Clicked");
										final Dialog loaddialog = Util
												.showLoadDialog(LabsBillPaymentActivity.this);
										OrderAudit audit = new OrderAudit();
										audit.setPaidAmount(editPayingAmount
												.getText().toString());
										audit.setPaymentMethod(spinnerPaymentMethod
												.getSelectedItem().toString());
										audit.setDiscountPercentage(editDiscount
												.getText().toString());
										audit.setTaxPercentage(apt.orderView
												.getOrder().getTax_percentage());
										audit.setComment(editNotes.getText()
												.toString());

										String discountType;
										if (spinnerDiscount
												.getSelectedItemPosition() == 1)
											discountType = "amount";
										else
											discountType = "percentage";
										OrderController.makePayment(
												LabsBillPaymentActivity.this,
												new OnResponseData() {

													@Override
													public void onResponseListner(
															Object response) {
														Intent intent = new Intent(
																LabsBillPaymentActivity.this,
																LabsBillPaymentHistoryActivity.class);
														intent.putExtra(
																"orderid",
																apt.orderView
																		.getOrder()
																		.getOrder_id());
														startActivity(intent);
														loaddialog.dismiss();
													}
												}, apt.orderView.getOrder(),
												audit, discountType,
												txtDiscount.getText()
														.toString());

									}
								} else {
									Util.Alertdialog(
											LabsBillPaymentActivity.this,
											"Discount Percentage cannot be more than 100.");
								}
							} else {
								Util.Alertdialog(LabsBillPaymentActivity.this,
										"Only Numeric Value allowed in Paying Amount");
							}
						} else {
							Util.Alertdialog(LabsBillPaymentActivity.this,
									"Only Numeric Value allowed in Discount Rupees");
						}

					} else {
						Util.Alertdialog(LabsBillPaymentActivity.this,
								"Only Numeric Value allowed in Discount Percent");
					}
				} catch (Exception e) {

				}

			}

		});

	}

	@Override
	public void onResponseListner(String api) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onActionSelected(int position) {
		// TODO Auto-generated method stub

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

	private void details() {
		try {
			PatientController.getPatient(apt.orderView.getOrder()
					.getCustomer_id(), this, new OnResponsePatient() {

				@Override
				public void onResponseListner(Patient response) {
					if (apt.orderView.getOrder().getBill_status_id()
							.contains(Order.BILL_STATUS_FULLY_PAID)) {
						rlMain.setVisibility(View.GONE);
						btnSubmit.setVisibility(View.GONE);
					} else {
						rlMain.setVisibility(View.VISIBLE);
						btnSubmit.setVisibility(View.VISIBLE);
					}
					pat1 = response;
				}
			});
			apt.orderView.getOrder().sum();
			final Date date = apt.orderView.getOrder().getDate_added();
			txtOrderDate.setText(EzApp.sdfddMmyy.format(date));
			// Log.i("", pat1.getP_detail());
			txtPname.setText(pat1.getP_detail());
			txtOrder.setText(apt.orderView.getOrder().getOrder_display_id());
			txtOrderStatus.setText(Order.getOrderStatus(apt.orderView
					.getOrder().getOrder_status_id()));
			txtBillStatus.setText(Order.getBillStatus(apt.orderView.getOrder()
					.getBill_status_id()));
			txtBalanceAmount.setText(apt.orderView.getOrder().getBalance());
			txtAmountDue.setText(""
					+ Util.doubleFormat(apt.orderView.getOrder()
							.getBalance_amount()));
			txtOriginalAmountDue.setText(""
					+ Util.doubleFormat(""
							+ Util.doubleFormat(""
									+ apt.orderView.getOrder()
											.getTotal_amount())));
			txtTotalAmount.setText(""
					+ Util.doubleFormat("" + apt.orderView.getOrder().total1));
			// txtBalanceAmount.setText(""
			// + apt.orderView.getOrder().balanceAmount);
			txtTax.setText("Tax @ "
					+ apt.orderView.getOrder().getTax_percentage() + " % :");
			txtTaxView.setText(Util.doubleFormat(apt.orderView.getOrder()
					.getTax_amount()));
			txtPayableAmount.setText(""
					+ Util.doubleFormat(apt.orderView.getOrder()
							.getBalance_amount()));
			editPayingAmount.setText(""
					+ Util.doubleFormat(apt.orderView.getOrder()
							.getBalance_amount()));
			txtBillId.setText(apt.orderView.getOrder().getOrder_display_id()
					+ "-01");
			if (apt.orderView.getOrder().getBill_status_id() == Order.BILL_STATUS_PARTIALLY_PAID) {

			}

		} catch (Exception e) {
			Log.e("", e);
		}
	}

	private void paymentChanges() {
		if (apt.orderView.getOrder().orderAudit.size() > 0) {
			((TextView) findViewById(R.id.txt_payable_amount))
					.setText("Payable Balance Amount :");
			((TextView) findViewById(R.id.txt_total_amount))
					.setVisibility(View.GONE);
			((TextView) findViewById(R.id.txt_discount))
					.setVisibility(View.GONE);
			((TextView) findViewById(R.id.txt_tax)).setVisibility(View.GONE);
			((TextView) findViewById(R.id.txt_total_amount_display))
					.setVisibility(View.GONE);
			((TextView) findViewById(R.id.txt_percent))
					.setVisibility(View.GONE);
			((TextView) findViewById(R.id.txt_discount_display))
					.setVisibility(View.GONE);
			((TextView) findViewById(R.id.txt_percent_1))
					.setVisibility(View.GONE);
			((TextView) findViewById(R.id.txt_tax_display))
					.setVisibility(View.GONE);
			((ImageView) findViewById(R.id.img_rupee_3))
					.setVisibility(View.GONE);
			((ImageView) findViewById(R.id.img_rupee_2))
					.setVisibility(View.GONE);
			((EditText) findViewById(R.id.edit_discount))
					.setVisibility(View.GONE);
		} else {
			((TextView) findViewById(R.id.txt_payable_amount))
					.setText("Payable Amount :");
			((TextView) findViewById(R.id.txt_total_amount))
					.setVisibility(View.VISIBLE);
			((TextView) findViewById(R.id.txt_discount))
					.setVisibility(View.VISIBLE);
			((TextView) findViewById(R.id.txt_tax)).setVisibility(View.VISIBLE);
			((TextView) findViewById(R.id.txt_total_amount_display))
					.setVisibility(View.VISIBLE);
			((TextView) findViewById(R.id.txt_percent))
					.setVisibility(View.VISIBLE);
			((TextView) findViewById(R.id.txt_discount_display))
					.setVisibility(View.VISIBLE);
			((TextView) findViewById(R.id.txt_percent_1))
					.setVisibility(View.VISIBLE);
			((TextView) findViewById(R.id.txt_tax_display))
					.setVisibility(View.VISIBLE);
			((ImageView) findViewById(R.id.img_rupee_3))
					.setVisibility(View.VISIBLE);
			((ImageView) findViewById(R.id.img_rupee_2))
					.setVisibility(View.VISIBLE);
			((EditText) findViewById(R.id.edit_discount))
					.setVisibility(View.VISIBLE);
		}

	}

}

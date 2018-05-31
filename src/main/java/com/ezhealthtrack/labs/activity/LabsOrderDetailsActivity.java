package com.ezhealthtrack.labs.activity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.print.PrintHelper;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ezhealthrack.api.NetworkCalls;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.greendao.LabAppointment;
import com.ezhealthtrack.greendao.LabTechnician;
import com.ezhealthtrack.greendao.Order;
import com.ezhealthtrack.greendao.OrderDao;
import com.ezhealthtrack.greendao.Patient;
import com.ezhealthtrack.greendao.PatientDao.Properties;
import com.ezhealthtrack.labs.adapter.LabsTechnicianAdapter;
import com.ezhealthtrack.labs.controller.LabWorkFlowController;
import com.ezhealthtrack.labs.controller.LabsController;
import com.ezhealthtrack.labs.controller.OrderController;
import com.ezhealthtrack.labs.controller.OrderController.OnResponseData;
import com.ezhealthtrack.labs.fragments.SideFragment.OnActionSelectedListner;
import com.ezhealthtrack.order.LabOrderReport;
import com.ezhealthtrack.order.OrderProduct;
import com.ezhealthtrack.order.OrderView;
import com.ezhealthtrack.order.Reference;
import com.ezhealthtrack.order.Result;
import com.ezhealthtrack.order.SampleMetum;
import com.ezhealthtrack.order.Step;
import com.ezhealthtrack.order.Transition;
import com.ezhealthtrack.order.WorkFlow;
import com.ezhealthtrack.util.BaseActivity;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.NetworkCallController.OnResponse;
import com.ezhealthtrack.util.Util;
import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;

public class LabsOrderDetailsActivity extends BaseActivity implements
		OnActionSelectedListner, NetworkCalls.OnResponse {
	private Button btnSampling;
	private Button btnSamplingDone;
	private Button btnBill;
	private Button btnPrintLabel;
	private Button btnPrintOrder;
	private Button btnGenerateBill;
	private Button btnPublishReport;
	private Button btnUnpublishReport;
	private Button btnViewAllReport;
	private Button btnUpdateOrder;
	private Spinner spinnerAction;
	private EditText editNotes;
	private EditText editNote;
	private TextView txtPname;
	private TextView txtOrderDate;
	private TextView txtOrder;
	private TextView txtTechnician;
	private TextView txtBill;
	private TextView txtBillStatus;
	private TextView txtOrderStatus;
	private TextView txtDoctor;
	private TextView txtEnterValue;
	private TextView txtReportError;
	private TextView txtErrorReported;
	private TextView txtSlash;
	private TextView txtExternalOrderId;
	private TextView txtApprovalStatus;
	private TextView txtHomeVisitDisplay;
	private LinearLayout llTestList;
	private LayoutInflater inflater;
	private LabAppointment apt;
	private OrderProduct orderProduct;
	private ArrayList<String> arrAction = new ArrayList<String>();
	private Patient pat = new Patient();
	private Result res = new Result();
	private LabOrderReport report;

	@Override
	protected void onResume() {
		fetchData();
		super.onResume();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.labs_activity_order_details);
		getActionBar().setHomeButtonEnabled(true);

		txtPname = (TextView) findViewById(R.id.txt_name_display);
		txtOrderDate = (TextView) findViewById(R.id.txt_order_date_display);
		txtOrder = (TextView) findViewById(R.id.txt_order_display);
		txtTechnician = (TextView) findViewById(R.id.txt_technician_display);
		txtBill = (TextView) findViewById(R.id.txt_bill_status);
		txtBillStatus = (TextView) findViewById(R.id.txt_bill_status_display);
		txtOrderStatus = (TextView) findViewById(R.id.txt_order_status_display);
		txtDoctor = (TextView) findViewById(R.id.txt_doctor_display);
		txtExternalOrderId = (TextView) findViewById(R.id.txt_external_order_id_display);
		txtApprovalStatus = (TextView) findViewById(R.id.txt_approval_status_display);
		txtHomeVisitDisplay = (TextView) findViewById(R.id.txt_home_visit_display);
		btnBill = (Button) findViewById(R.id.btn_bill);
		btnSampling = (Button) findViewById(R.id.btn_sampling);
		btnSamplingDone = (Button) findViewById(R.id.btn_sampling_done);
		btnPrintLabel = (Button) findViewById(R.id.btn_print_labels);
		btnPrintOrder = (Button) findViewById(R.id.btn_print_order);
		btnGenerateBill = (Button) findViewById(R.id.btn_generate_bill);
		btnPublishReport = (Button) findViewById(R.id.btn_publish_report);
		btnUnpublishReport = (Button) findViewById(R.id.btn_unpublish_report);
		btnViewAllReport = (Button) findViewById(R.id.btn_view_all_reports);
		btnUpdateOrder = (Button) findViewById(R.id.btn_update_order);
		llTestList = (LinearLayout) findViewById(R.id.ll_test_list);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		btnBill.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FlurryAgent
						.logEvent("LabsOrderDetailsActivity - Bill Payment History Button Clicked");
				Intent intent = new Intent(LabsOrderDetailsActivity.this,
						LabsBillPaymentHistoryActivity.class);
				// intent.putExtra("bkid", apt.getBkid());
				intent.putExtra("orderid", apt.orderView.getOrder()
						.getOrder_id());
				startActivity(intent);
			}
		});

		btnSampling.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FlurryAgent
						.logEvent("LabsOrderDetailsActivity - Sampling Button Clicked");
				// TODO Auto-generated method stub
				dialogSampling();
			}
		});

		btnSamplingDone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FlurryAgent
						.logEvent("LabsOrderDetailsActivity - Sampling Done Button Clicked");
				// TODO Auto-generated method stub
				dialogSamplingDone();

			}
		});

		btnPrintLabel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FlurryAgent
						.logEvent("LabsOrderDetailsActivity - Print Label Button Clicked");
				final Dialog loaddialog = Util
						.showLoadDialog(LabsOrderDetailsActivity.this);
				OrderController.printLabel(LabsOrderDetailsActivity.this,
						new OnResponseData() {

							@Override
							public void onResponseListner(Object response) {
								dialogPrintLabel("" + response);
								loaddialog.dismiss();
							}
						}, apt.orderView);

			}
		});

		btnPrintOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FlurryAgent
						.logEvent("LabsOrderDetailsActivity - Print Order Button Clicked");
				// TODO Auto-generated method stub
				dialogPrintOrders();
			}
		});

		btnGenerateBill.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				FlurryAgent
						.logEvent("LabsOrderDetailsActivity - Generate Bill Button Clicked");
				final Dialog loaddialog = Util
						.showLoadDialog(LabsOrderDetailsActivity.this);
				OrderController.generateBill(LabsOrderDetailsActivity.this,
						new OnResponseData() {

							@Override
							public void onResponseListner(Object response) {
								Util.Alertdialog(LabsOrderDetailsActivity.this,
										"Bill generated successfully!");
								loaddialog.dismiss();
								btnGenerateBill.setVisibility(View.GONE);
								btnBill.setVisibility(View.VISIBLE);
								txtBill.setVisibility(View.VISIBLE);
								txtBillStatus.setVisibility(View.VISIBLE);
								fetchData();
							}
						}, apt.orderView);

			}
		});

		btnPublishReport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FlurryAgent
						.logEvent("LabsOrderDetailsActivity - Publish Report Button Clicked");
				final Dialog loaddialog = Util
						.showLoadDialog(LabsOrderDetailsActivity.this);
				OrderController.publishReport(LabsOrderDetailsActivity.this,
						new OnResponseData() {

							@Override
							public void onResponseListner(Object response) {
								Util.Alertdialog(LabsOrderDetailsActivity.this,
										"Report Published successfully!");
								loaddialog.dismiss();
								btnPublishReport.setVisibility(View.GONE);
								btnUnpublishReport.setVisibility(View.VISIBLE);

							}
						}, apt.orderView);

			}
		});

		btnUnpublishReport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FlurryAgent
						.logEvent("LabsOrderDetailsActivity - Unpublish Report Button Clicked");
				final Dialog loaddialog = Util
						.showLoadDialog(LabsOrderDetailsActivity.this);
				OrderController.unpublishReport(LabsOrderDetailsActivity.this,
						new OnResponseData() {

							@Override
							public void onResponseListner(Object response) {
								Util.Alertdialog(LabsOrderDetailsActivity.this,
										"Report Unpublished successfully!");
								loaddialog.dismiss();
								btnUnpublishReport.setVisibility(View.GONE);
								btnPublishReport.setVisibility(View.VISIBLE);

							}
						}, apt.orderView);

			}
		});

		btnViewAllReport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				FlurryAgent
						.logEvent("LabsOrderDetailsActivity - View All Reports Button Clicked");
				dialogViewAllReport(report);

			}
		});

		btnUpdateOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				FlurryAgent
						.logEvent("LabsOrderDetailsActivity - Update Order Button Clicked");
				Intent intent = new Intent(LabsOrderDetailsActivity.this,
						LabsOrderUpdateActivity.class);
				intent.putExtra("bkid", apt.getBkid());
				startActivity(intent);

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

	private void orderDetailsList(boolean flag) {
		llTestList.removeAllViews();
		btnPublishReport.setVisibility(View.GONE);
		btnUnpublishReport.setVisibility(View.GONE);
		txtApprovalStatus.setText("Approved");
		int count = 0;
		for (int i = 1; i <= apt.orderView.getLabOrderReports().size(); i++) {
			final LabOrderReport report = apt.orderView.getLabOrderReports()
					.get(i - 1);
			if (!report.getWorkflowLabel().contains("Approved")
					&& !report.getStatus().equals(
							OrderProduct.STATUS_ERROR_REPORT))
				txtApprovalStatus.setText("Pending");
			final View v1 = inflater.inflate(
					R.layout.labs_row_order_details_list, null);
			TextView txtEnterValue = (TextView) v1
					.findViewById(R.id.txt_enter_value);
			TextView txtSlash = (TextView) v1.findViewById(R.id.txt_slash);
			TextView txtReportError = (TextView) v1
					.findViewById(R.id.txt_report_error);
			TextView txtErrorReported = (TextView) v1
					.findViewById(R.id.txt_error_reported);
			TextView txtAction = (TextView) v1
					.findViewById(R.id.txt_action_display);
			TextView txtPrint = (TextView) v1.findViewById(R.id.txt_print);
			TextView txtReport = (TextView) v1.findViewById(R.id.txt_report);
			ImageView imgAction = (ImageView) v1.findViewById(R.id.img_action);
			final Date date;
			try {

				((TextView) v1.findViewById(R.id.txt_test_name)).setText(report
						.getLabTestName());
				if (!Util.isEmptyString(report.getReportPreparedOn())) {
					date = EzApp.sdfyymmddhhmmss.parse(report
							.getReportPreparedOn());
					((TextView) v1.findViewById(R.id.txt_date_display))
							.setText(EzApp.sdfddMmyy.format(date));
				}

				if (!Util.isEmptyString(report.getReportPreparedBy()))
					((TextView) v1.findViewById(R.id.txt_technician_display))
							.setText(""
									+ LabsController.getLabTechnician(
											report.getReportPreparedBy())
											.getName());
				if (!Util.isEmptyString(report.getWorkflowLabel()))
					txtAction.setText(report.getWorkflowLabel());

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			llTestList.addView(v1);

			if (!flag) {
				txtEnterValue.setVisibility(View.GONE);
				txtErrorReported.setVisibility(View.GONE);
				txtReportError.setVisibility(View.GONE);
				txtSlash.setVisibility(View.GONE);
				txtPrint.setVisibility(View.GONE);
				txtReport.setVisibility(View.GONE);
				txtAction.setVisibility(View.GONE);
				txtApprovalStatus.setText("Pending");
			} else {
				if (report.getStatus().equals(OrderProduct.STATUS_ENTER_REPORT)) {

					txtEnterValue.setVisibility(View.VISIBLE);
					txtErrorReported.setVisibility(View.GONE);
					txtReportError.setVisibility(View.VISIBLE);
					txtSlash.setVisibility(View.VISIBLE);
					txtPrint.setVisibility(View.GONE);
					txtReport.setVisibility(View.GONE);
					txtAction.setVisibility(View.GONE);
					txtApprovalStatus.setText("Pending");

				} else if (report.getStatus().equals(
						OrderProduct.STATUS_REPORT_DONE)) {
					count++;
					txtEnterValue.setVisibility(View.GONE);
					txtErrorReported.setVisibility(View.GONE);
					txtReportError.setVisibility(View.GONE);
					txtSlash.setVisibility(View.VISIBLE);
					txtPrint.setVisibility(View.VISIBLE);
					txtReport.setVisibility(View.VISIBLE);
					txtAction.setVisibility(View.VISIBLE);
					if (report.getTransitionPermission()) {
						imgAction.setVisibility(View.VISIBLE);
					} else {
						imgAction.setVisibility(View.GONE);
					}

				} else if (report.getStatus().equals(
						OrderProduct.STATUS_ERROR_REPORT)) {

					txtEnterValue.setVisibility(View.GONE);
					txtErrorReported.setVisibility(View.VISIBLE);
					txtReportError.setVisibility(View.GONE);
					txtSlash.setVisibility(View.GONE);
					txtPrint.setVisibility(View.GONE);
					txtReport.setVisibility(View.GONE);
					txtAction.setVisibility(View.GONE);
				}

			}

			imgAction.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					FlurryAgent
							.logEvent("LabsOrderDetailsActivity - Action Button Clicked");
					dialogAction(report);
				}
			});

			txtEnterValue.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					FlurryAgent
							.logEvent("LabsOrderDetailsActivity - Report Values Button Clicked");
					Intent intent = new Intent(LabsOrderDetailsActivity.this,
							LabsReportValuesActivity.class);
					intent.putExtra("bkid", apt.getBkid());
					intent.putExtra("orderid", apt.orderView.getOrder()
							.getOrder_id());
					intent.putExtra("reportid", report.getId());
					startActivity(intent);

				}
			});

			txtReportError.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					FlurryAgent
							.logEvent("LabsOrderDetailsActivity - Report Error Button Clicked");

					dialogReportErr(apt.orderView.getOrder(), report);

				}
			});
			if (report.getRefundStatus().equals("0")) {
				if (apt.orderView.getOrder().getGenerate_bill().equals("1")) {
					txtErrorReported.setTextColor(Color.parseColor("#9AB8D8"));
					txtErrorReported.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							FlurryAgent
									.logEvent("LabsOrderDetailsActivity - Bill Refund Button Clicked");
							Intent intent = new Intent(
									LabsOrderDetailsActivity.this,
									LabsBillRefundActivity.class);
							// intent.putExtra("bkid", apt.getBkid());
							intent.putExtra("orderid", apt.orderView.getOrder()
									.getOrder_id());
							intent.putExtra("reportid", report.getId());
							startActivity(intent);
							finish();
						}

					});
				} else {
					txtErrorReported.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							FlurryAgent
									.logEvent("LabsOrderDetailsActivity - Error Reported Button Clicked when no bill was generated");
							Util.Alertdialog(LabsOrderDetailsActivity.this,
									"No bill generated for this order!");
						}
					});
				}
			} else {
				txtErrorReported.setTextColor(Color.BLACK);
			}

			txtReport.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// if (!report.getIsEditable()) {
					if (false) {
						FlurryAgent
								.logEvent("LabsOrderDetailsActivity - Report Values view only Button Clicked");
						Intent intent = new Intent(
								LabsOrderDetailsActivity.this,
								LabsReportValuesViewActivity.class);
						intent.putExtra("bkid", apt.getBkid());
						intent.putExtra("orderid", apt.orderView.getOrder()
								.getOrder_id());
						intent.putExtra("reportid", report.getId());
						Log.i("", "" + apt.orderView.getOrder().getOrder_id());
						startActivity(intent);
					} else {
						FlurryAgent
								.logEvent("LabsOrderDetailsActivity - Report Values Button Clicked");
						Intent intent = new Intent(
								LabsOrderDetailsActivity.this,
								LabsReportValuesActivity.class);
						intent.putExtra("bkid", apt.getBkid());
						intent.putExtra("orderid", apt.orderView.getOrder()
								.getOrder_id());
						intent.putExtra("reportid", report.getId());
						Log.i("", "" + apt.orderView.getOrder().getOrder_id());
						startActivity(intent);
					}

				}

			});
			txtPrint.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					FlurryAgent
							.logEvent("LabsOrderDetailsActivity - Print Report Button Clicked");
					if (report.getReportTemplateView().equals("two_column")) {
						dialogPrintReportCol2(report);
					} else {
						dialogPrintReport(report);
					}
				}
			});
			if (report.getWorkflowLabel().equalsIgnoreCase("Approved")) {
				if (apt.orderView.getLabOrderDetail().getPublishReport()
						.equals(1)) {
					btnUnpublishReport.setVisibility(View.VISIBLE);
				} else {
					btnPublishReport.setVisibility(View.VISIBLE);
				}

			}
		}
		if (count > 1) {
			btnViewAllReport.setVisibility(View.VISIBLE);
		} else {
			btnViewAllReport.setVisibility(View.GONE);
		}

		if (apt.orderView.getOrder().getOrder_status_id()
				.equals(Order.ORDER_STATUS_NEW)) {
			btnUpdateOrder.setVisibility(View.VISIBLE);
		} else {
			btnUpdateOrder.setVisibility(View.GONE);
		}
	}

	private void dialogSampling() {
		final Dialog dialogSampling = new Dialog(this);
		dialogSampling.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogSampling.setContentView(R.layout.labs_dialog_order_sampling);
		dialogSampling.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		LayoutParams params = dialogSampling.getWindow().getAttributes();
		params.width = 2000;
		dialogSampling.getWindow().setAttributes(
				(android.view.WindowManager.LayoutParams) params);

		final Spinner spinnerTechnician = (Spinner) dialogSampling
				.findViewById(R.id.spinner_technician_name);
		LabsTechnicianAdapter technicianAdapter = new LabsTechnicianAdapter(
				LabsOrderDetailsActivity.this,
				android.R.layout.simple_spinner_item,
				EzApp.labTechnicianDao.loadAll());
		technicianAdapter.setDropDownViewResource(R.layout.labs_row_technician);
		spinnerTechnician.setAdapter(technicianAdapter);

		if (apt.orderView.getOrder().getGenerate_bill().equals("0")) {

			((TextView) dialogSampling
					.findViewById(R.id.txt_bill_status_display))
					.setVisibility(View.GONE);
			;
			((TextView) dialogSampling.findViewById(R.id.txt_bill_status))
					.setVisibility(View.GONE);
			;

		} else {
			((TextView) dialogSampling
					.findViewById(R.id.txt_bill_status_display))
					.setVisibility(View.VISIBLE);
			;
			((TextView) dialogSampling.findViewById(R.id.txt_bill_status))
					.setVisibility(View.VISIBLE);
			;
		}

		final Date date = apt.orderView.getOrder().getDate_added();
		((TextView) dialogSampling.findViewById(R.id.txt_order_date_display))
				.setText(EzApp.sdfddMmyy.format(date));
		((TextView) dialogSampling.findViewById(R.id.txt_name_display))
				.setText(pat.getP_detail());
		((TextView) dialogSampling.findViewById(R.id.txt_order_display))
				.setText(apt.orderView.getOrder().getOrder_display_id());
		((TextView) dialogSampling.findViewById(R.id.txt_bill_status_display))
				.setText(Order.getBillStatus(apt.orderView.getOrder()
						.getBill_status_id()));
		((TextView) dialogSampling.findViewById(R.id.txt_order_status_display))
				.setText(Order.getOrderStatus(apt.orderView.getOrder()
						.getOrder_status_id()));
		((TextView) dialogSampling.findViewById(R.id.txt_technician_display))
				.setText(LabsController.getLabTechnician(
						apt.orderView.getLabOrderDetail().getTechnicianId())
						.getName());

		((TextView) dialogSampling.findViewById(R.id.txt_order_display))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						FlurryAgent
								.logEvent("LabsOrderDetailsActivity - Order Details Button Clicked");
						final Dialog loaddialog = Util
								.showLoadDialog(LabsOrderDetailsActivity.this);
						OrderController.getOrderList("1",
								LabsOrderDetailsActivity.this,
								new OnResponse() {

									@Override
									public void onResponseListner(
											String response) {
										Intent intent = new Intent(
												LabsOrderDetailsActivity.this,
												LabsOrderDetailsActivity.class);
										intent.putExtra("orderid",
												apt.orderView.getOrder()
														.getOrder_id());
										startActivity(intent);
										loaddialog.dismiss();
									}
								}, null, null, null, -1, -1, "");

					}
				});
		editNotes = (EditText) dialogSampling.findViewById(R.id.edit_note);

		dialogSampling.findViewById(R.id.btn_submit).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						FlurryAgent
								.logEvent("LabsOrderDetailsActivity - Submit Sampling Button Clicked");
						final Dialog loaddialog = Util
								.showLoadDialog(LabsOrderDetailsActivity.this);

						apt.orderView.getLabOrderDetail().setTechnicianId(
								""
										+ ((LabTechnician) spinnerTechnician
												.getSelectedItem()).getId());
						apt.orderView.getLabOrderDetail().setSamplingNotes(
								editNotes.toString());

						OrderController.orderSampling(
								LabsOrderDetailsActivity.this,
								new OnResponseData() {

									@Override
									public void onResponseListner(
											Object response) {
										Util.Alertdialog(
												LabsOrderDetailsActivity.this,
												"Order Status changed to Sampling");
										loaddialog.dismiss();
										apt.orderView
												.getOrder()
												.setOrder_status_id(
														Order.ORDER_STATUS_SAMPLING);
										details();
										fetchData();
										dialogSampling.dismiss();
									}
								}, apt.orderView);

					}
				});

		dialogSampling.setCancelable(false);
		dialogSampling.findViewById(R.id.txt_close).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						dialogSampling.dismiss();

					}
				});
		dialogSampling.show();

	}

	private void dialogSamplingDone() {
		final Dialog dialogSamplingDone = new Dialog(this);
		dialogSamplingDone.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialogSamplingDone
				.setContentView(R.layout.labs_dialog_order_sampling_done);
		dialogSamplingDone.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		LayoutParams params = dialogSamplingDone.getWindow().getAttributes();
		params.width = 2000;
		dialogSamplingDone.getWindow().setAttributes(
				(android.view.WindowManager.LayoutParams) params);

		if (apt.orderView.getOrder().getGenerate_bill().equals("0")) {

			((TextView) dialogSamplingDone
					.findViewById(R.id.txt_bill_status_display))
					.setVisibility(View.GONE);
			;
			((TextView) dialogSamplingDone.findViewById(R.id.txt_bill_status))
					.setVisibility(View.GONE);
			;

		} else {
			((TextView) dialogSamplingDone
					.findViewById(R.id.txt_bill_status_display))
					.setVisibility(View.VISIBLE);
			;
			((TextView) dialogSamplingDone.findViewById(R.id.txt_bill_status))
					.setVisibility(View.VISIBLE);
			;
		}

		final Date date = apt.orderView.getOrder().getDate_added();
		((TextView) dialogSamplingDone
				.findViewById(R.id.txt_order_date_display))
				.setText(EzApp.sdfddMmyy.format(date));
		((TextView) dialogSamplingDone.findViewById(R.id.txt_name_display))
				.setText(pat.getP_detail());
		((TextView) dialogSamplingDone.findViewById(R.id.txt_order_display))
				.setText(apt.orderView.getOrder().getOrder_display_id());
		((TextView) dialogSamplingDone
				.findViewById(R.id.txt_bill_status_display)).setText(Order
				.getBillStatus(apt.orderView.getOrder().getBill_status_id()));
		((TextView) dialogSamplingDone
				.findViewById(R.id.txt_order_status_display)).setText(Order
				.getOrderStatus(apt.orderView.getOrder().getOrder_status_id()));
		((TextView) dialogSamplingDone
				.findViewById(R.id.txt_technician_display))
				.setText(LabsController.getLabTechnician(
						apt.orderView.getLabOrderDetail().getTechnicianId())
						.getName());

		((TextView) dialogSamplingDone.findViewById(R.id.txt_order_display))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						FlurryAgent
								.logEvent("LabsOrderDetailsActivity - Order Details Button Clicked");
						final Dialog loaddialog = Util
								.showLoadDialog(LabsOrderDetailsActivity.this);
						OrderController.getOrderList("1",
								LabsOrderDetailsActivity.this,
								new OnResponse() {

									@Override
									public void onResponseListner(
											String response) {
										Intent intent = new Intent(
												LabsOrderDetailsActivity.this,
												LabsOrderDetailsActivity.class);
										intent.putExtra("orderid",
												apt.orderView.getOrder()
														.getOrder_id());
										startActivity(intent);
										loaddialog.dismiss();
									}
								}, null, null, null, -1, -1, "");

					}
				});
		editNote = (EditText) dialogSamplingDone.findViewById(R.id.edit_note);

		dialogSamplingDone.findViewById(R.id.btn_submit).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						FlurryAgent
								.logEvent("LabsOrderDetailsActivity - Submit Sampling Done Button Clicked");
						final Dialog loaddialog = Util
								.showLoadDialog(LabsOrderDetailsActivity.this);
						apt.orderView.getLabOrderDetail().setSamplingDoneNotes(
								editNote.toString());
						OrderController.orderSamplingDone(
								LabsOrderDetailsActivity.this,
								new OnResponseData() {

									@Override
									public void onResponseListner(
											Object response) {
										Util.Alertdialog(
												LabsOrderDetailsActivity.this,
												"Order Status changed to Waiting");
										loaddialog.dismiss();
										apt.orderView
												.getOrder()
												.setOrder_status_id(
														Order.ORDER_STATUS_WAITING);

										details();
										dialogSamplingDone.dismiss();
										fetchData();

									}
								}, apt.orderView);
					}
				});

		dialogSamplingDone.setCancelable(false);
		dialogSamplingDone.findViewById(R.id.txt_close).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						dialogSamplingDone.dismiss();

					}
				});
		dialogSamplingDone.show();
	}

	private void dialogPrintLabel(String response) {
		final Dialog dialogPrintLabel = new Dialog(this);
		dialogPrintLabel.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogPrintLabel.setContentView(R.layout.labs_dialog_print_labels);
		dialogPrintLabel.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		// LayoutParams params = dialogSamplingDone.getWindow().getAttributes();
		// params.width = 2000;
		// dialogSamplingDone.getWindow().setAttributes(
		// (android.view.WindowManager.LayoutParams) params);
		Util.getImageFromUrl(response, DashboardActivity.imgLoader,
				(ImageView) dialogPrintLabel.findViewById(R.id.img_label));
		((TextView) dialogPrintLabel.findViewById(R.id.txt_patient_name))
				.setText(pat.getP_detail());

		dialogPrintLabel.setCancelable(false);
		dialogPrintLabel.findViewById(R.id.btn_print).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						FlurryAgent
								.logEvent("LabsOrderDetailsActivity - Print Label(Print) Button Clicked");
						PrintHelper photoPrinter = new PrintHelper(
								LabsOrderDetailsActivity.this);

						photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
						ImageView view = (ImageView) (dialogPrintLabel
								.findViewById(R.id.img_label));
						Bitmap bm = ((BitmapDrawable) view.getDrawable())
								.getBitmap();
						// Bitmap bitmap =
						// BitmapFactory.decodeResource(getResources(),
						// R.drawable.rs_15);

						photoPrinter.printBitmap(apt.orderView.getOrder()
								.getOrder_display_id(), bm);

						dialogPrintLabel.dismiss();

					}
				});
		dialogPrintLabel.findViewById(R.id.txt_close).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialogPrintLabel.dismiss();

					}
				});
		dialogPrintLabel.show();
	}

	private void dialogPrintOrders() {
		final Dialog dialogPrint = new Dialog(this);
		dialogPrint.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialogPrint.setContentView(R.layout.labs_dialog_print_order);
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
		TextView txtDiscount = (TextView) dialogPrint
				.findViewById(R.id.txt_discount);
		ImageView imgLab = (ImageView) dialogPrint.findViewById(R.id.img_lab);
		ImageView imgRupee = (ImageView) dialogPrint
				.findViewById(R.id.img_rupee_9);
		TextView txtDiscountDisplay = (TextView) dialogPrint
				.findViewById(R.id.txt_discount_display);
		TextView txtTax = (TextView) dialogPrint.findViewById(R.id.txt_tax);
		ImageView imgRupeetax = (ImageView) dialogPrint
				.findViewById(R.id.img_rupee_19);
		TextView txtTaxDisplay = (TextView) dialogPrint
				.findViewById(R.id.txt_tax_display);
		TextView txtTotal = (TextView) dialogPrint
				.findViewById(R.id.txt_total_display);
		TextView txtTechSignature = (TextView) dialogPrint
				.findViewById(R.id.txt_tech_signature_display);
		TextView txtCustomerSignature = (TextView) dialogPrint
				.findViewById(R.id.txt_customer_signature_display);
		LinearLayout llTestName = (LinearLayout) dialogPrint
				.findViewById(R.id.ll_test_name);
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
			txtDoctorName
					.setText(apt.orderView.getLabOrderDetail().getDoctor());
			txtLabOrderId.setText(apt.orderView.getOrder()
					.getOrder_display_id());
			txtPreparedBy.setText(LabsController.getLabTechnician(
					apt.orderView.getLabOrderDetail().getTechnicianId())
					.getName());
			txtTechSignature.setText(LabsController.getLabTechnician(
					apt.orderView.getLabOrderDetail().getTechnicianId())
					.getName());
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
				llTestName.addView(v1);
			}

			if (!apt.orderView.getOrder().getDiscount_percentage().equals(0)) {
				txtDiscount.setVisibility(View.VISIBLE);
				imgRupee.setVisibility(View.VISIBLE);
				txtDiscountDisplay.setVisibility(View.VISIBLE);
			}
			txtDiscount.setText("Discount @ "
					+ Util.doubleFormat(apt.orderView.getOrder()
							.getDiscount_percentage()) + " %");
			txtDiscountDisplay.setText(Util.doubleFormat(apt.orderView
					.getOrder().getDiscount_amount()));

			if (!apt.orderView.getOrder().getTax_percentage().equals(0.00)) {
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
					+ Util.doubleFormat(apt.orderView.getOrder()
							.getTotal_amount()));

			if (!Util.isEmptyString(EzApp.sharedPref.getString(
					Constants.SIGNATURE, "signature"))) {
				Util.getImageFromUrl(EzApp.sharedPref.getString(
						Constants.SIGNATURE, ""), DashboardActivity.imgLoader,
						(ImageView) dialogPrint
								.findViewById(R.id.img_lab_signature));
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
								.logEvent("LabsOrderDetailsActivity - Print Order(Print) Button Clicked");
						PrintHelper photoPrinter = new PrintHelper(
								LabsOrderDetailsActivity.this);

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

	private void dialogReportErr(final Order order, final LabOrderReport report) {
		final Dialog dialogReportError = new Dialog(this);

		dialogReportError.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogReportError.setContentView(R.layout.labs_dialog_report_error);
		dialogReportError.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		LayoutParams params = dialogReportError.getWindow().getAttributes();
		params.width = 2000;
		dialogReportError.getWindow().setAttributes(
				(android.view.WindowManager.LayoutParams) params);

		dialogReportError.setCancelable(false);
		dialogReportError.findViewById(R.id.txt_close).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						dialogReportError.dismiss();

					}
				});
		dialogReportError.show();

		arrAction.clear();
		arrAction.add("Generate New Order");
		arrAction.add("Refund");

		spinnerAction = (Spinner) dialogReportError
				.findViewById(R.id.spinner_action);
		final ArrayAdapter<String> actionAdapter = new ArrayAdapter<String>(
				LabsOrderDetailsActivity.this,
				android.R.layout.simple_spinner_item, arrAction);
		actionAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerAction.setAdapter(actionAdapter);

		if (apt.orderView.getOrder().getGenerate_bill().equals("0")) {
			((TextView) dialogReportError
					.findViewById(R.id.txt_bill_status_display))
					.setVisibility(View.GONE);
			((TextView) dialogReportError.findViewById(R.id.txt_bill_status))
					.setVisibility(View.GONE);
			((TextView) dialogReportError.findViewById(R.id.txt_action))
					.setVisibility(View.GONE);
			spinnerAction.setVisibility(View.GONE);

		} else {
			((TextView) dialogReportError
					.findViewById(R.id.txt_bill_status_display))
					.setVisibility(View.VISIBLE);
			((TextView) dialogReportError.findViewById(R.id.txt_bill_status))
					.setVisibility(View.VISIBLE);
			((TextView) dialogReportError.findViewById(R.id.txt_action))
					.setVisibility(View.VISIBLE);
			spinnerAction.setVisibility(View.VISIBLE);
		}

		final Date date = apt.orderView.getOrder().getDate_added();
		((TextView) dialogReportError.findViewById(R.id.txt_order_date_display))
				.setText(EzApp.sdfddMmyy.format(date));
		((TextView) dialogReportError.findViewById(R.id.txt_name_display))
				.setText(pat.getP_detail());
		((TextView) dialogReportError.findViewById(R.id.txt_order_display))
				.setText(apt.orderView.getOrder().getOrder_display_id());
		((TextView) dialogReportError
				.findViewById(R.id.txt_bill_status_display)).setText(Order
				.getBillStatus(apt.orderView.getOrder().getBill_status_id()));
		((TextView) dialogReportError
				.findViewById(R.id.txt_order_status_display)).setText(Order
				.getOrderStatus(apt.orderView.getOrder().getOrder_status_id()));
		((TextView) dialogReportError.findViewById(R.id.txt_technician_display))
				.setText(LabsController.getLabTechnician(
						apt.orderView.getLabOrderDetail().getTechnicianId())
						.getName());
		((TextView) dialogReportError.findViewById(R.id.txt_report_display))
				.setText(report.getLabTestName());

		((TextView) dialogReportError.findViewById(R.id.txt_order_display))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						FlurryAgent
								.logEvent("LabsOrderDetailsActivity - Order Details Button Clicked");
						final Dialog loaddialog = Util
								.showLoadDialog(LabsOrderDetailsActivity.this);
						OrderController.getOrderList("1",
								LabsOrderDetailsActivity.this,
								new OnResponse() {

									@Override
									public void onResponseListner(
											String response) {
										Intent intent = new Intent(
												LabsOrderDetailsActivity.this,
												LabsOrderDetailsActivity.class);
										intent.putExtra("orderid",
												apt.orderView.getOrder()
														.getOrder_id());
										startActivity(intent);
										loaddialog.dismiss();
									}
								}, null, null, null, -1, -1, "");

					}
				});

		((Button) dialogReportError.findViewById(R.id.btn_submit))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						FlurryAgent
								.logEvent("LabsOrderDetailsActivity - Submit Report Error Button Clicked");
						// TODO Auto-generated method stub
						if (spinnerAction.getSelectedItemPosition() == 0) {
							final Dialog loaddialog = Util
									.showLoadDialog(LabsOrderDetailsActivity.this);
							OrderController.reportError(
									LabsOrderDetailsActivity.this,
									new OnResponseData() {

										@Override
										public void onResponseListner(
												Object response) {
											Util.Alertdialog(
													LabsOrderDetailsActivity.this,
													"Error reported for the report");
											dialogReportError.dismiss();
											loaddialog.dismiss();
											fetchData();
											txtErrorReported
													.setVisibility(View.VISIBLE);
											txtEnterValue
													.setVisibility(View.GONE);
											txtSlash.setVisibility(View.GONE);
											txtReportError
													.setVisibility(View.GONE);

										}
									}, order, report,
									((EditText) dialogReportError
											.findViewById(R.id.edit_note))
											.getText().toString(), "new order");

						} else {
							final Dialog loaddialog = Util
									.showLoadDialog(LabsOrderDetailsActivity.this);
							OrderController.reportError(
									LabsOrderDetailsActivity.this,
									new OnResponseData() {

										@Override
										public void onResponseListner(
												Object response) {
											Util.Alertdialog(
													LabsOrderDetailsActivity.this,
													"Error reported for the report");
											dialogReportError.dismiss();
											loaddialog.dismiss();
											fetchData();
											Intent intent = new Intent(
													LabsOrderDetailsActivity.this,
													LabsBillRefundActivity.class);
											intent.putExtra("orderid",
													apt.orderView.getOrder()
															.getOrder_id());
											intent.putExtra("reportid",
													report.getId());
											startActivity(intent);

										}
									}, order, report,
									((EditText) dialogReportError
											.findViewById(R.id.edit_note))
											.getText().toString(), "refund");

						}
					}
				});

	}

	public static Bitmap loadBitmapFromView(View v, int width, int height) {
		Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		v.layout(0, 0, v.getWidth(), v.getHeight());
		v.draw(c);
		return b;
	}

	private void dialogPrintReport(final LabOrderReport report) {
		final Dialog dialogPrint = new Dialog(this);
		dialogPrint.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialogPrint.setContentView(R.layout.labs_dialog_print_report);
		dialogPrint.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		LayoutParams params = dialogPrint.getWindow().getAttributes();
		params.width = 2500;
		dialogPrint.getWindow().setAttributes(
				(android.view.WindowManager.LayoutParams) params);

		TextView txtPatientName = (TextView) dialogPrint
				.findViewById(R.id.txt_patient_name);
		TextView txtPatientAddress = (TextView) dialogPrint
				.findViewById(R.id.txt_patient_address);
		TextView txtPhone = (TextView) dialogPrint.findViewById(R.id.txt_phone);
		TextView txtEmail = (TextView) dialogPrint.findViewById(R.id.txt_email);
		TextView txtOrderId = (TextView) dialogPrint
				.findViewById(R.id.txt_order_id_display);
		TextView txtReportingDate = (TextView) dialogPrint
				.findViewById(R.id.txt_date_display);
		TextView txtReportAvailDate = (TextView) dialogPrint
				.findViewById(R.id.txt_report_avai_date_display);
		TextView txtTech = (TextView) dialogPrint
				.findViewById(R.id.txt_tech_name);
		TextView txtPanel = (TextView) dialogPrint.findViewById(R.id.txt_panel);

		try {
			final Date date = EzApp.sdfyymmddhhmmss.parse(report
					.getReportPreparedOn());
			final Date date1 = EzApp.sdfyymmddhhmmss.parse(report
					.getReportAvailableOn());
			txtPatientName.setText(pat.getP_detail());
			txtPatientAddress.setText(pat.getPadd1() + " " + pat.getPadd2()
					+ ", " + pat.getParea() + ", " + pat.getPcity() + ", "
					+ pat.getPstate() + ", " + pat.getPcountry() + " - "
					+ pat.getPzip());
			txtPhone.setText(pat.getPmobnum());
			txtEmail.setText(pat.getPemail());
			txtOrderId.setText(apt.orderView.getOrder().getOrder_display_id());
			txtReportingDate.setText(EzApp.sdfddMmyy
					.format(date1));
			txtReportAvailDate.setText(EzApp.sdfddMmyy
					.format(date));
			txtTech.setText(LabsController.getLabTechnician(
					apt.orderView.getLabOrderDetail().getTechnicianId())
					.getName());
			if (!Util.isEmptyString(EzApp.sharedPref.getString(
					Constants.SIGNATURE, "signature"))) {
				Util.getImageFromUrl(EzApp.sharedPref.getString(
						Constants.SIGNATURE, ""), DashboardActivity.imgLoader,
						(ImageView) dialogPrint
								.findViewById(R.id.img_lab_signature));
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LinearLayout llReportValues = (LinearLayout) dialogPrint
				.findViewById(R.id.ll_test_name_display);
		llReportValues.removeAllViews();

		if (report.getSampleMeta().size() > 1) {
			txtPanel.setVisibility(View.VISIBLE);
			txtPanel.setText(Html.fromHtml("<b>Lab Panel: </b>"
					+ report.getLabTestName()));
		}

		for (final SampleMetum sm1 : report.getSampleMeta()) {
			final SampleMetum sm;
			if (sm1.getResults().size() == 0) {
				sm = sm1.sample_meta.get(0);
			} else {
				sm = sm1;
			}
			final View v = inflater.inflate(
					R.layout.labs_row_print_report_test_list, null);
			final TextView txtTestName = (TextView) v
					.findViewById(R.id.txt_test_name_display);
			final TextView txtMethod = (TextView) v
					.findViewById(R.id.txt_method);
			final TextView txtMethodDisplay = (TextView) v
					.findViewById(R.id.txt_method_display);
			final TextView txtSample1 = (TextView) v
					.findViewById(R.id.txt_sample);
			final TextView txtSampleDisplay = (TextView) v
					.findViewById(R.id.txt_sample_display);

			txtTestName.setText(report.getLabTestName());

			if (!report.getDisplayMethod().equals("0")) {
				if (!Util.isEmptyString(sm.getMethod())) {
					txtMethodDisplay.setText(sm.getMethod());
				}
			} else {
				txtMethod.setVisibility(View.GONE);
				txtMethodDisplay.setVisibility(View.GONE);
			}
			if (!report.getDisplaySample().equals("0")) {
				txtSampleDisplay.setText(sm.getName());
			} else {
				txtSample1.setVisibility(View.GONE);
				txtSampleDisplay.setVisibility(View.GONE);
			}
			WebView txt = (WebView) v.findViewById(R.id.wv_interpret);
			TextView InterpretationText = (TextView) v
					.findViewById(R.id.txt_interpretation_text);

			final List<Result> arrResults;

			if (sm.getdisplay_interpretation() == true) {
				txt.setVisibility(View.VISIBLE);
				InterpretationText.setVisibility(View.VISIBLE);
			} else {
				txt.setVisibility(View.GONE);
				InterpretationText.setVisibility(View.GONE);
			}

			if (sm.getResults().size() > 0) {
				arrResults = sm.getResults();
				txt.loadDataWithBaseURL("file:///android_asset/",
						sm.result_interpretation, "text/html", "UTF-8", null);
			} else {
				arrResults = sm.sample_meta.get(0).getResults();
				txt.loadDataWithBaseURL("file:///android_asset/",
						sm.sample_meta.get(0).result_interpretation,
						"text/html", "UTF-8", null);
			}
			final LinearLayout llResults = (LinearLayout) v
					.findViewById(R.id.ll_test_name_display_list);
			for (final Result res : arrResults) {
				if (!Util.isEmptyString(res.getValue())) {
					final View v1 = inflater
							.inflate(
									R.layout.labs_row_print_report_test_list_list,
									null);
					llResults.addView(v1);
					final TextView txtTestNameMain1 = (TextView) v1
							.findViewById(R.id.txt_test_name_display);
					final TextView txtUnit = (TextView) v1
							.findViewById(R.id.txt_unit_display);
					final TextView txtReferenceRange = (TextView) v1
							.findViewById(R.id.txt_r_range_display);
					final TextView txtNotes = (TextView) v1
							.findViewById(R.id.txt_notes_display);
					final TextView editObservedValue = (TextView) v1
							.findViewById(R.id.txt_observed_value_display);
					final TextView editNotes = (TextView) v1
							.findViewById(R.id.txt_note_display);
					final TextView txtnote = (TextView) v1
							.findViewById(R.id.txt_note);
					final TextView txtGroupName = (TextView) v1
							.findViewById(R.id.txt_group_name);

					txtUnit.setText(res.getUnit());

					if (sm.result_name_bold == true) {
						txtTestNameMain1.setText(Html.fromHtml("<b>"
								+ res.getName() + "</b>"));
					} else {
						txtTestNameMain1.setText(res.getName());
					}

					if (!Util.isEmptyString(res.getValue())) {
						editObservedValue.setText(res.getValue());
						if (res.getStatus() != null
								&& res.getStatus().equals(Result.OUT_RANGE)) {
							editObservedValue.setText(Html.fromHtml("<b>"
									+ res.getValue() + "</b>"));
						}
					}

					if (!Util.isEmptyString(res.getNotes())) {
						txtnote.setVisibility(View.VISIBLE);
						editNotes.setVisibility(View.VISIBLE);
						editNotes.setText(res.getNotes());
					} else {
						txtnote.setVisibility(View.GONE);
						editNotes.setVisibility(View.GONE);
					}

					if (!Util.isEmptyString(res.getGroupName())) {
						txtGroupName.setVisibility(View.VISIBLE);
						txtGroupName.setText(res.getGroupName());
					} else {
						txtGroupName.setText("");
						txtGroupName.setVisibility(View.GONE);
					}

					for (Reference result : res.getReferences()) {
						if (result.getGender().equals(pat.getPgender())
								|| result.getGender().equals("Both")) {
							// Log.e("age", ""+pat.getage());
							if (true) {
								if (!result.getRangeValueMinOption().contains(
										"None")) {
									txtReferenceRange.append(result
											.getRangeValueMinOption());
								}
								if (result.getRangeValueMin().length() > 0) {
									txtReferenceRange.append(" "
											+ result.getRangeValueMin()
											+ " (min)");
								}
								if (!result.getRangeValueMaxOption().contains(
										"None")) {
									txtReferenceRange.append(" "
											+ result.getRangeValueMaxOption());
								}
								if (result.getRangeValueMax().length() > 0) {
									txtReferenceRange.append(" "
											+ result.getRangeValueMax()
											+ " (max)");
								}
								if (!Util.isEmptyString(result.getNotes()))
									txtNotes.append(result.getNotes());
								txtReferenceRange.append("\n");
								txtNotes.append("\n");
							}
						}
					}
				}
			}

			llReportValues.addView(v);
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
								.logEvent("LabsOrderDetailsActivity - Print Report(Print) Button Clicked");
						PrintHelper photoPrinter = new PrintHelper(
								LabsOrderDetailsActivity.this);

						photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
						View view = (dialogPrint.findViewById(R.id.rl_print));
						// view.setDrawingCacheEnabled(true);
						// view.buildDrawingCache();
						Bitmap bm = loadBitmapFromView(view, 1190, 1684);
						// view.destroyDrawingCache();
						// Bitmap bitmap =
						// BitmapFactory.decodeResource(getResources(),
						// R.drawable.rs_15);

						photoPrinter.printBitmap("Report", bm);
					}
				});

		dialogPrint.show();

	}

	private void dialogPrintReportCol2(final LabOrderReport report) {
		final Dialog dialogPrint = new Dialog(this);
		dialogPrint.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialogPrint.setContentView(R.layout.labs_dialog_print_report_col2);
		dialogPrint.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		LayoutParams params = dialogPrint.getWindow().getAttributes();
		params.width = 2500;
		dialogPrint.getWindow().setAttributes(
				(android.view.WindowManager.LayoutParams) params);

		TextView txtPatientName = (TextView) dialogPrint
				.findViewById(R.id.txt_patient_name);
		TextView txtPatientAddress = (TextView) dialogPrint
				.findViewById(R.id.txt_patient_address);
		TextView txtPhone = (TextView) dialogPrint.findViewById(R.id.txt_phone);
		TextView txtEmail = (TextView) dialogPrint.findViewById(R.id.txt_email);
		TextView txtOrderId = (TextView) dialogPrint
				.findViewById(R.id.txt_order_id_display);
		TextView txtReportingDate = (TextView) dialogPrint
				.findViewById(R.id.txt_date_display);
		TextView txtReportAvailDate = (TextView) dialogPrint
				.findViewById(R.id.txt_report_avai_date_display);
		TextView txtTech = (TextView) dialogPrint
				.findViewById(R.id.txt_tech_name);
		TextView txtPanel = (TextView) dialogPrint.findViewById(R.id.txt_panel);
		TextView txtTestName = (TextView) dialogPrint
				.findViewById(R.id.txt_test_name_display);
		TextView txtSampleDisplay = (TextView) dialogPrint
				.findViewById(R.id.txt_sample_display);
		TextView txtSample = (TextView) dialogPrint
				.findViewById(R.id.txt_sample);
		TextView txtMethod = (TextView) dialogPrint
				.findViewById(R.id.txt_method);
		TextView txtMethodDisplay = (TextView) dialogPrint
				.findViewById(R.id.txt_method_display);

		try {
			final Date date = EzApp.sdfyymmddhhmmss.parse(report
					.getReportPreparedOn());
			final Date date1 = EzApp.sdfyymmddhhmmss.parse(report
					.getReportAvailableOn());
			txtPatientName.setText(pat.getP_detail());
			txtPatientAddress.setText(pat.getPadd1() + " " + pat.getPadd2()
					+ ", " + pat.getParea() + ", " + pat.getPcity() + ", "
					+ pat.getPstate() + ", " + pat.getPcountry() + " - "
					+ pat.getPzip());
			txtPhone.setText(pat.getPmobnum());
			txtEmail.setText(pat.getPemail());
			txtOrderId.setText(apt.orderView.getOrder().getOrder_display_id());
			txtReportingDate.setText(EzApp.sdfddMmyy
					.format(date1));
			txtReportAvailDate.setText(EzApp.sdfddMmyy
					.format(date));
			txtTech.setText(LabsController.getLabTechnician(
					apt.orderView.getLabOrderDetail().getTechnicianId())
					.getName());
			if (!Util.isEmptyString(EzApp.sharedPref.getString(
					Constants.SIGNATURE, "signature"))) {
				Util.getImageFromUrl(EzApp.sharedPref.getString(
						Constants.SIGNATURE, ""), DashboardActivity.imgLoader,
						(ImageView) dialogPrint
								.findViewById(R.id.img_lab_signature));
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LinearLayout llReportValues = (LinearLayout) dialogPrint
				.findViewById(R.id.ll_test_name_display);
		llReportValues.removeAllViews();

		if (report.getSampleMeta().size() > 1) {
			txtPanel.setVisibility(View.VISIBLE);
			txtPanel.setText(Html.fromHtml("<b>Lab Panel: </b>"
					+ report.getLabTestName()));
		} else {
			txtTestName.setText(report.getLabTestName());
		}
		for (final SampleMetum sm1 : report.getSampleMeta()) {
			final SampleMetum sm;
			if (sm1.getResults().size() == 0) {
				sm = sm1.sample_meta.get(0);
			} else {
				sm = sm1;
			}
			final View v = inflater.inflate(
					R.layout.labs_row_print_report_test_list_col2, null);

			if (!report.getDisplayMethod().equals("0")) {
				if (!Util.isEmptyString(sm.getMethod())) {
					txtMethodDisplay.setText(sm.getMethod());
				}
			} else {
				txtMethod.setVisibility(View.GONE);
				txtMethodDisplay.setVisibility(View.GONE);
			}
			if (!report.getDisplaySample().equals("0")) {
				txtSampleDisplay.setText(sm.getName());
			} else {
				txtSample.setVisibility(View.GONE);
				txtSampleDisplay.setVisibility(View.GONE);
			}
			WebView txt = (WebView) v.findViewById(R.id.wv_interpret);
			TextView InterpretationText = (TextView) v
					.findViewById(R.id.txt_interpretation_text);

			final List<Result> arrResults;

			if (sm.getdisplay_interpretation() == true) {
				txt.setVisibility(View.VISIBLE);
				InterpretationText.setVisibility(View.VISIBLE);
			} else {
				txt.setVisibility(View.GONE);
				InterpretationText.setVisibility(View.GONE);
			}

			if (sm.getResults().size() > 0) {
				arrResults = sm.getResults();
				txt.loadDataWithBaseURL("file:///android_asset/",
						sm.result_interpretation, "text/html", "UTF-8", null);
			} else {
				arrResults = sm.sample_meta.get(0).getResults();
				txt.loadDataWithBaseURL("file:///android_asset/",
						sm.sample_meta.get(0).result_interpretation,
						"text/html", "UTF-8", null);
			}
			final LinearLayout llResults = (LinearLayout) v
					.findViewById(R.id.ll_test_name_display_list);
			for (final Result res : arrResults) {
				if (!Util.isEmptyString(res.getValue())) {
					final View v1 = inflater.inflate(
							R.layout.labs_row_print_report_test_list_list_col2,
							null);
					llResults.addView(v1);
					final TextView txtTestNameMain1 = (TextView) v1
							.findViewById(R.id.txt_test_name_display);
					final TextView txtUnit = (TextView) v1
							.findViewById(R.id.txt_unit_display);
					final TextView editObservedValue = (TextView) v1
							.findViewById(R.id.txt_observed_value_display);
					final TextView txtGroupName = (TextView) v1
							.findViewById(R.id.txt_group_name);

					txtUnit.setText(res.getUnit());
					if (sm.result_name_bold == true) {
						txtTestNameMain1.setText(Html.fromHtml("<b>"
								+ res.getName() + "</b>"));
					} else {
						txtTestNameMain1.setText(res.getName());
					}

					if (!Util.isEmptyString(res.getValue())) {
						editObservedValue.setText(res.getValue());
						if (res.getStatus() != null
								&& res.getStatus().equals(Result.OUT_RANGE)) {
							editObservedValue.setText(Html.fromHtml("<b>"
									+ res.getValue() + "</b>"));
						}
					}

					if (!Util.isEmptyString(res.getGroupName())) {
						txtGroupName.setVisibility(View.VISIBLE);
						txtGroupName.setText(res.getGroupName());
					} else {
						txtGroupName.setText("");
						txtGroupName.setVisibility(View.GONE);
					}
				}
			}

			llReportValues.addView(v);
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
								.logEvent("LabsOrderDetailsActivity - Print Report(Print) Button Clicked");
						PrintHelper photoPrinter = new PrintHelper(
								LabsOrderDetailsActivity.this);

						photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
						View view = (dialogPrint.findViewById(R.id.rl_print));
						// view.setDrawingCacheEnabled(true);
						// view.buildDrawingCache();
						Bitmap bm = loadBitmapFromView(view, 1190, 1684);
						// view.destroyDrawingCache();
						// Bitmap bitmap =
						// BitmapFactory.decodeResource(getResources(),
						// R.drawable.rs_15);

						photoPrinter.printBitmap("Report", bm);
					}
				});

		dialogPrint.show();

	}

	private void dialogAction(final LabOrderReport report) {
		final Dialog dialogAction = new Dialog(this);
		dialogAction.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogAction.setContentView(R.layout.labs_dialog_action);
		dialogAction.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		LayoutParams params = dialogAction.getWindow().getAttributes();
		params.width = 2000;
		dialogAction.getWindow().setAttributes(
				(android.view.WindowManager.LayoutParams) params);

		final EditText editComment = (EditText) dialogAction
				.findViewById(R.id.edit_comment);
		dialogAction.findViewById(R.id.btn_approve).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						FlurryAgent
								.logEvent("LabsOrderDetailsActivity - Approve Button Clicked");
						final Dialog loaddialog = Util
								.showLoadDialog(LabsOrderDetailsActivity.this);
						OrderController.reportApproval(
								LabsOrderDetailsActivity.this,
								new OnResponseData() {

									@Override
									public void onResponseListner(
											Object response) {
										fetchData();
										Util.Alertdialog(
												LabsOrderDetailsActivity.this,
												"Report approved successfully.");
										dialogAction.dismiss();
										loaddialog.dismiss();
									}
								}, report, "approved", editComment.getText()
										.toString());

					}
				});

		dialogAction.findViewById(R.id.btn_reject).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						FlurryAgent
								.logEvent("LabsOrderDetailsActivity - Reject Button Clicked");
						final Dialog loaddialog = Util
								.showLoadDialog(LabsOrderDetailsActivity.this);
						OrderController.reportApproval(
								LabsOrderDetailsActivity.this,
								new OnResponseData() {

									@Override
									public void onResponseListner(
											Object response) {
										android.util.Log.i("",
												response.toString());
										fetchData();
										Util.Alertdialog(
												LabsOrderDetailsActivity.this,
												"Report rejected successfully.");
										dialogAction.dismiss();
										loaddialog.dismiss();
									}
								}, report, "reject", editComment.getText()
										.toString());

					}
				});
		dialogAction.show();

		dialogAction.setCancelable(false);
		dialogAction.findViewById(R.id.txt_close).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						dialogAction.dismiss();

					}
				});

	}

	private void details() {
		try {
			if (EzApp.patientDao
					.queryBuilder()
					.where(Properties.Pid.eq(apt.orderView.getOrder()
							.getCustomer_id())).count() > 0) {
				pat = (EzApp.patientDao
						.queryBuilder()
						.where(Properties.Pid.eq(apt.orderView.getOrder()
								.getCustomer_id())).list().get(0));
				txtPname.setText(pat.getP_detail());
			}

		} catch (Exception e) {
			Log.e("", e);

		}
		final Date date = apt.orderView.getOrder().getDate_added();
		txtOrderDate.setText(EzApp.sdfddMmyy.format(date));
		txtOrder.setText(apt.orderView.getOrder().getOrder_display_id());
		txtBillStatus.setText(Order.getBillStatus(apt.orderView.getOrder()
				.getBill_status_id()));
		txtOrderStatus.setText(Order.getOrderStatus(apt.orderView.getOrder()
				.getOrder_status_id()));
		txtTechnician.setText(LabsController.getLabTechnician(
				apt.orderView.getLabOrderDetail().getTechnicianId()).getName());
		if (!Util.isEmptyString(apt.orderView.getLabOrderDetail().getDoctor())) {
			txtDoctor.setText(apt.orderView.getLabOrderDetail().getDoctor());
		} else {
			txtDoctor.setText("--");
		}
		txtHomeVisitDisplay.setText(apt.orderView.getPatientLocAppointment()
				.toString());
		// txtAtPatLoc.setText(apt.get);
		if (apt.orderView.getOrder().getOrderFields().size() > 0) {
			txtExternalOrderId.setText(apt.orderView.getOrder()
					.getOrderFields().get(0).getValue());
		} else {
			txtExternalOrderId.setText("--");
		}

		if (apt.orderView.getOrder().getGenerate_bill().equals("0")) {
			btnBill.setVisibility(View.GONE);
			txtBill.setVisibility(View.GONE);
			txtBillStatus.setVisibility(View.GONE);
			btnGenerateBill.setVisibility(View.VISIBLE);
		} else {
			btnBill.setVisibility(View.VISIBLE);
			btnGenerateBill.setVisibility(View.GONE);
		}

		btnSamplingDone.setVisibility(View.GONE);
		btnSampling.setVisibility(View.GONE);

		if (Util.isEmptyString(apt.orderView.getOrder().getOrder_workflow_id()))
			apt.orderView.getOrder().setOrder_workflow_id(
					""
							+ EzApp.sharedPref.getLong(
									LabWorkFlowController.lab_order, 0));
		LabWorkFlowController
				.getWorkFlowById(Long.parseLong(apt.orderView.getOrder()
						.getOrder_workflow_id()), this,
						new LabWorkFlowController.OnResponseWorkFlow() {

							@Override
							public void onResponseWorkFlow(WorkFlow workFlow) {
								Step step = workFlow
										.getStepByName(apt.orderView.getOrder()
												.getOrderStatus());
								Boolean flag = true;
								for (Transition transition : step
										.getTransitions()) {
									if (transition.getLabel().contains(
											"Sampling")) {
										btnSampling.setVisibility(View.VISIBLE);
										flag = false;
									} else if (transition.getLabel().contains(
											"Waiting")) {
										btnSamplingDone
												.setVisibility(View.VISIBLE);
										flag = false;
									}
								}
								orderDetailsList(flag);

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

	private void fetchData() {
		try {
			final Dialog loaddialog = Util
					.showLoadDialog(LabsOrderDetailsActivity.this);
			if (getIntent().hasExtra("bkid")) {
				apt = EzApp.labAptDao
						.queryBuilder()
						.where(com.ezhealthtrack.greendao.LabAppointmentDao.Properties.Bkid
								.eq(getIntent().getStringExtra("bkid"))).list()
						.get(0);
				OrderController.getViewOrder(this, new OnResponseData() {

					@Override
					public void onResponseListner(Object response) {
						apt.orderView = (OrderView) response;
						orderProduct = apt.orderView.getOrder().getData()
								.getOrderProducts()
								.get(getIntent().getIntExtra("pos", 0));
						Log.i("", ""
								+ apt.orderView.getLabOrderReports().size());
						details();
						loaddialog.dismiss();
					}
				}, apt.orderView, getIntent().getStringExtra("bkid"));

			} else {
				apt = new LabAppointment();
				apt.orderView.setOrder(EzApp.orderDao
						.queryBuilder()
						.where(OrderDao.Properties.Id.eq(getIntent()
								.getLongExtra("orderid", 0))).list().get(0));
				OrderController.getViewOrder1(this, new OnResponseData() {

					@Override
					public void onResponseListner(Object response) {
						apt.orderView = (OrderView) response;
						orderProduct = apt.orderView.getOrder().getData()
								.getOrderProducts()
								.get(getIntent().getIntExtra("pos", 0));
						details();
						loaddialog.dismiss();
					}
				}, apt.orderView, "" + apt.orderView.getOrder().getOrder_id());
			}
		} catch (Exception e) {

		}
	}

	private void dialogViewAllReport(final LabOrderReport report) {
		final Dialog dialogPrint = new Dialog(this);
		dialogPrint.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogPrint.setContentView(R.layout.labs_dialog_print_all_report);
		dialogPrint.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		LayoutParams params = dialogPrint.getWindow().getAttributes();
		params.width = 2500;
		dialogPrint.getWindow().setAttributes(
				(android.view.WindowManager.LayoutParams) params);

		TextView txtPatientName = (TextView) dialogPrint
				.findViewById(R.id.txt_patient_name);
		TextView txtPatientAddress = (TextView) dialogPrint
				.findViewById(R.id.txt_patient_address);
		TextView txtPhone = (TextView) dialogPrint.findViewById(R.id.txt_phone);
		TextView txtEmail = (TextView) dialogPrint.findViewById(R.id.txt_email);
		TextView txtOrderId = (TextView) dialogPrint
				.findViewById(R.id.txt_order_id_display);
		TextView txtReportingDate = (TextView) dialogPrint
				.findViewById(R.id.txt_date_display);
		TextView txtReportAvailDate = (TextView) dialogPrint
				.findViewById(R.id.txt_report_avai_date_display);
		TextView txtTech = (TextView) dialogPrint
				.findViewById(R.id.txt_tech_name);
		try {
			final Date date = apt.orderView.getOrder().getDate_added();
			txtPatientName.setText(pat.getP_detail());
			txtPatientAddress.setText(pat.getPadd1() + " " + pat.getPadd2()
					+ ", " + pat.getParea() + ", " + pat.getPcity() + ", "
					+ pat.getPstate() + ", " + pat.getPcountry() + " - "
					+ pat.getPzip());
			txtPhone.setText(pat.getPmobnum());
			txtEmail.setText(pat.getPemail());
			txtOrderId.setText(apt.orderView.getOrder().getOrder_display_id());
			txtReportingDate
					.setText(EzApp.sdfddMmyy.format(date));
			txtReportAvailDate.setVisibility(View.GONE);
			txtTech.setText(LabsController.getLabTechnician(
					apt.orderView.getLabOrderDetail().getTechnicianId())
					.getName());
			if (!Util.isEmptyString(EzApp.sharedPref.getString(
					Constants.SIGNATURE, "signature"))) {
				Util.getImageFromUrl(EzApp.sharedPref.getString(
						Constants.SIGNATURE, ""), DashboardActivity.imgLoader,
						(ImageView) dialogPrint
								.findViewById(R.id.img_lab_signature));
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LinearLayout llReportsCol1 = (LinearLayout) dialogPrint
				.findViewById(R.id.ll_test_name_display_col1);
		llReportsCol1.removeAllViews();

		LinearLayout llReportsCol2 = (LinearLayout) dialogPrint
				.findViewById(R.id.ll_test_name_display_col2);
		llReportsCol2.removeAllViews();

		int oneCol = 0;
		int twoCol = 0;
		for (LabOrderReport report1 : apt.orderView.getLabOrderReports()) {
			if (report1.getReportTemplateView().equals("one_column")) {
				oneCol++;
				int i = 0;
				for (final SampleMetum sm1 : report1.getSampleMeta()) {
					i++;
					final SampleMetum sm;
					if (sm1.getResults().size() == 0) {
						sm = sm1.sample_meta.get(0);
					} else {
						sm = sm1;
					}
					final View v = inflater.inflate(
							R.layout.labs_row_view_all_report_col1, null);
					final TextView txtTestName = (TextView) v
							.findViewById(R.id.txt_test_name_display);
					final TextView txtMethod = (TextView) v
							.findViewById(R.id.txt_method);
					final TextView txtMethodDisplay = (TextView) v
							.findViewById(R.id.txt_method_display);
					final TextView txtSample1 = (TextView) v
							.findViewById(R.id.txt_sample);
					final TextView txtSampleDisplay = (TextView) v
							.findViewById(R.id.txt_sample_display);
					final TextView txtPanel = (TextView) v
							.findViewById(R.id.txt_panel);
					if (report1.getSampleMeta().size() > 1 && i == 1) {
						txtPanel.setVisibility(View.VISIBLE);
						txtPanel.setText(Html.fromHtml("<b>Lab Panel: </b>"
								+ report1.getLabTestName()));
					} else {
						txtTestName.setText(report1.getLabTestName());
					}
					if (!report1.getDisplayMethod().equals("0")) {
						if (!Util.isEmptyString(sm.getMethod())) {
							txtMethodDisplay.setText(sm.getMethod());
						}
					} else {
						txtMethod.setVisibility(View.GONE);
						txtMethodDisplay.setVisibility(View.GONE);
					}
					if (!report1.getDisplaySample().equals("0")) {
						txtSampleDisplay.setText(sm.getName());
					} else {
						txtSample1.setVisibility(View.GONE);
						txtSampleDisplay.setVisibility(View.GONE);
					}
					WebView txt = (WebView) v.findViewById(R.id.wv_interpret);
					TextView InterpretationText = (TextView) v
							.findViewById(R.id.txt_interpretation_text);
					final List<Result> arrResults;

					if (sm.getdisplay_interpretation() == true) {
						txt.setVisibility(View.VISIBLE);
						InterpretationText.setVisibility(View.VISIBLE);
					} else {
						txt.setVisibility(View.GONE);
						InterpretationText.setVisibility(View.GONE);
					}

					if (sm.getResults().size() > 0) {
						arrResults = sm.getResults();
						txt.loadDataWithBaseURL("file:///android_asset/",
								sm.result_interpretation, "text/html", "UTF-8",
								null);
					} else {
						arrResults = sm.sample_meta.get(0).getResults();
						txt.loadDataWithBaseURL("file:///android_asset/",
								sm.sample_meta.get(0).result_interpretation,
								"text/html", "UTF-8", null);
					}

					LinearLayout llResults = (LinearLayout) v
							.findViewById(R.id.ll_test_name_display_list_col1);
					for (final Result res : arrResults) {
						if (!Util.isEmptyString(res.getValue())) {
							final View v1 = inflater
									.inflate(
											R.layout.labs_row_view_all_report_list_col1,
											null);
							llResults.addView(v1);
							final TextView txtTestNameMain1 = (TextView) v1
									.findViewById(R.id.txt_test_name_display);
							final TextView txtUnit = (TextView) v1
									.findViewById(R.id.txt_unit_display);
							final TextView txtReferenceRange = (TextView) v1
									.findViewById(R.id.txt_r_range_display);
							final TextView txtNotes = (TextView) v1
									.findViewById(R.id.txt_notes_display);
							final TextView editObservedValue = (TextView) v1
									.findViewById(R.id.txt_observed_value_display);
							final TextView editNotes = (TextView) v1
									.findViewById(R.id.txt_note_display);
							final TextView txtnote = (TextView) v1
									.findViewById(R.id.txt_note);
							final TextView txtGroupName = (TextView) v1
									.findViewById(R.id.txt_group_name);

							txtUnit.setText(res.getUnit());
							if (sm.result_name_bold == true) {
								txtTestNameMain1.setText(Html.fromHtml("<b>"
										+ res.getName() + "</b>"));
							} else {
								txtTestNameMain1.setText(res.getName());
							}
							if (!Util.isEmptyString(res.getValue())) {
								editObservedValue.setText(res.getValue());
								if (res.getStatus() != null
										&& res.getStatus().equals(
												Result.OUT_RANGE)) {
									editObservedValue.setText(Html
											.fromHtml("<b>" + res.getValue()
													+ "</b>"));
								}
							}

							if (!Util.isEmptyString(res.getNotes())) {
								txtnote.setVisibility(View.VISIBLE);
								editNotes.setVisibility(View.VISIBLE);
								editNotes.setText(res.getNotes());
							} else {
								txtnote.setVisibility(View.GONE);
								editNotes.setVisibility(View.GONE);
							}

							if (!Util.isEmptyString(res.getGroupName())) {
								txtGroupName.setVisibility(View.VISIBLE);
								txtGroupName.setText(res.getGroupName());
							} else {
								txtGroupName.setText("");
								txtGroupName.setVisibility(View.GONE);
							}

							for (Reference result : res.getReferences()) {
								if (result.getGender().equals(pat.getPgender())
										|| result.getGender().equals("Both")) {
									// Log.e("age", ""+pat.getage());
									if (true) {
										if (!result.getRangeValueMinOption()
												.contains("None")) {
											txtReferenceRange.append(result
													.getRangeValueMinOption());
										}
										if (result.getRangeValueMin().length() > 0) {
											txtReferenceRange.append(" "
													+ result.getRangeValueMin()
													+ " (min)");
										}
										if (!result.getRangeValueMaxOption()
												.contains("None")) {
											txtReferenceRange
													.append(" "
															+ result.getRangeValueMaxOption());
										}
										if (result.getRangeValueMax().length() > 0) {
											txtReferenceRange.append(" "
													+ result.getRangeValueMax()
													+ " (max)");
										}
										if (!Util.isEmptyString(result
												.getNotes()))
											txtNotes.append(result.getNotes());
										txtReferenceRange.append("\n");
										txtNotes.append("\n");
									}
								}
							}
						}
					}
					llReportsCol1.addView(v);
				}
			} else if (report1.getReportTemplateView().equals("two_column")) {
				twoCol++;
				int i = 0;
				for (final SampleMetum sm1 : report1.getSampleMeta()) {
					final SampleMetum sm;
					if (sm1.getResults().size() == 0) {
						sm = sm1.sample_meta.get(0);
					} else {
						sm = sm1;
					}
					final View v = inflater.inflate(
							R.layout.labs_row_view_all_report_col2, null);
					final TextView txtTestName = (TextView) v
							.findViewById(R.id.txt_test_name_display);
					final TextView txtMethod = (TextView) v
							.findViewById(R.id.txt_method);
					final TextView txtMethodDisplay = (TextView) v
							.findViewById(R.id.txt_method_display);
					final TextView txtSample1 = (TextView) v
							.findViewById(R.id.txt_sample);
					final TextView txtSampleDisplay = (TextView) v
							.findViewById(R.id.txt_sample_display);
					final TextView txtPanel = (TextView) v
							.findViewById(R.id.txt_panel);
					if (report1.getSampleMeta().size() > 1 && i == 1) {
						txtPanel.setVisibility(View.VISIBLE);
						txtPanel.setText(Html.fromHtml("<b>Lab Panel: </b>"
								+ report1.getLabTestName()));
					} else {
						txtTestName.setText(report1.getLabTestName());
					}
					if (!report1.getDisplayMethod().equals("0")) {
						if (!Util.isEmptyString(sm.getMethod())) {
							txtMethodDisplay.setText(sm.getMethod());
						}
					} else {
						txtMethod.setVisibility(View.GONE);
						txtMethodDisplay.setVisibility(View.GONE);
					}
					if (!report1.getDisplaySample().equals("0")) {
						txtSampleDisplay.setText(sm.getName());
					} else {
						txtSample1.setVisibility(View.GONE);
						txtSampleDisplay.setVisibility(View.GONE);
					}
					WebView txt = (WebView) v.findViewById(R.id.wv_interpret);
					TextView InterpretationText = (TextView) v
							.findViewById(R.id.txt_interpretation_text);
					final List<Result> arrResults;

					if (sm.getdisplay_interpretation() == true) {
						txt.setVisibility(View.VISIBLE);
						InterpretationText.setVisibility(View.VISIBLE);
					} else {
						txt.setVisibility(View.GONE);
						InterpretationText.setVisibility(View.GONE);
					}

					if (sm.getResults().size() > 0) {
						arrResults = sm.getResults();
						txt.loadDataWithBaseURL("file:///android_asset/",
								sm.result_interpretation, "text/html", "UTF-8",
								null);
					} else {
						arrResults = sm.sample_meta.get(0).getResults();
						txt.loadDataWithBaseURL("file:///android_asset/",
								sm.sample_meta.get(0).result_interpretation,
								"text/html", "UTF-8", null);
					}
					final LinearLayout llResults = (LinearLayout) v
							.findViewById(R.id.ll_test_name_display_list_col2);
					for (final Result res : arrResults) {
						if (!Util.isEmptyString(res.getValue())) {
							final View v1 = inflater
									.inflate(
											R.layout.labs_row_view_all_report_list_col2,
											null);
							llResults.addView(v1);
							final TextView txtTestNameMain1 = (TextView) v1
									.findViewById(R.id.txt_test_name_display);
							final TextView txtUnit = (TextView) v1
									.findViewById(R.id.txt_unit_display);
							final TextView editObservedValue = (TextView) v1
									.findViewById(R.id.txt_observed_value_display);
							final TextView txtGroupName = (TextView) v1
									.findViewById(R.id.txt_group_name);

							txtUnit.setText(res.getUnit());

							if (sm.result_name_bold == true) {
								txtTestNameMain1.setText(Html.fromHtml("<b>"
										+ res.getName() + "</b>"));
							} else {
								txtTestNameMain1.setText(res.getName());
							}

							if (!Util.isEmptyString(res.getValue())) {
								editObservedValue.setText(res.getValue());
								if (res.getStatus() != null
										&& res.getStatus().equals(
												Result.OUT_RANGE)) {
									editObservedValue.setText(Html
											.fromHtml("<b>" + res.getValue()
													+ "</b>"));
								}
							}
							// if (!Util.isEmptyString(res.getNotes())) {
							// txtnote.setVisibility(View.VISIBLE);
							// editNotes.setVisibility(View.VISIBLE);
							// editNotes.setText(res.getNotes());
							// } else {
							// txtnote.setVisibility(View.GONE);
							// editNotes.setVisibility(View.GONE);
							// }

							if (!Util.isEmptyString(res.getGroupName())) {
								txtGroupName.setVisibility(View.VISIBLE);
								txtGroupName.setText(res.getGroupName());
							} else {
								txtGroupName.setText("");
								txtGroupName.setVisibility(View.GONE);
							}
						}
					}

					llReportsCol2.addView(v);
				}
			}

		}
		if (oneCol == 0) {
			dialogPrint.findViewById(R.id.rl_test_name)
					.setVisibility(View.GONE);
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
								.logEvent("LabsOrderDetailsActivity - View All Reports(Print) Button Clicked");
						PrintHelper photoPrinter = new PrintHelper(
								LabsOrderDetailsActivity.this);

						photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
						View view = (dialogPrint.findViewById(R.id.rl_print));
						// view.setDrawingCacheEnabled(true);
						// view.buildDrawingCache();
						// int[] location1 = new int[2];
						// view.getLocationInWindow(location1);
						// Bitmap bm = Bitmap.createBitmap(
						// view.getMeasuredWidth(),
						// view.getMeasuredHeight(),
						// Bitmap.Config.ARGB_8888);
						//
						// bm = findViewWithTagRecursively((ViewGroup) view, bm,
						// view);
						// bm = Bitmap.createScaledBitmap(
						// bm,
						// 1024,
						// (int) (view.getMeasuredHeight() * 1.0
						// / view.getMeasuredWidth() * 1024), true);
						// // Bitmap bitmap =
						// // BitmapFactory.decodeResource(getResources(),
						// // R.drawable.rs_15);
						Bitmap bm = loadBitmapFromView(view, 0, 0);

						photoPrinter.printBitmap("Report", bm);
					}
				});

		dialogPrint.show();

	}

	public static Bitmap findViewWithTagRecursively(ViewGroup root,
			Bitmap bitmap, View main) {

		final int childCount = root.getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View childView = root.getChildAt(i);

			if (childView instanceof ViewGroup) {
				bitmap = findViewWithTagRecursively((ViewGroup) childView,
						bitmap, main);
			} else {
				try {
					int[] location = new int[2];
					int[] location1 = new int[2];
					main.getLocationInWindow(location1);
					childView.getLocationInWindow(location);
					childView.setDrawingCacheEnabled(true);
					childView.buildDrawingCache();
					Log.i("", new Gson().toJson(location));
					Canvas canvas = new Canvas(bitmap);
					// crop the screenshot
					Bitmap bitmap1 = Bitmap.createBitmap(childView
							.getDrawingCache());
					// canvas.drawBitmap(bitmap1, new Matrix(), null);
					// , (location[0]), (location[1]),
					// childView.getMeasuredWidth(),
					// childView.getMeasuredHeight()
					canvas.drawBitmap(bitmap1, location[0] - location1[0],
							location[1] - location1[1], null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return bitmap;
	}

}

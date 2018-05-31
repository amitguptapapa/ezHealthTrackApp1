package com.ezhealthtrack.labs.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ezhealthrack.api.LabApis;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.greendao.LabAppointment;
import com.ezhealthtrack.greendao.LabTechnician;
import com.ezhealthtrack.greendao.Order;
import com.ezhealthtrack.greendao.Patient;
import com.ezhealthtrack.greendao.PatientDao.Properties;
import com.ezhealthtrack.labs.activity.LabsBillRefundActivity;
import com.ezhealthtrack.labs.activity.LabsOrderDetailsActivity;
import com.ezhealthtrack.labs.adapter.LabsTechnicianAdapter;
import com.ezhealthtrack.model.PatientAutoSuggest;
import com.ezhealthtrack.order.CreateOrder;
import com.ezhealthtrack.order.Data;
import com.ezhealthtrack.order.LabOrderReport;
import com.ezhealthtrack.order.OrderAudit;
import com.ezhealthtrack.order.OrderView;
import com.ezhealthtrack.order.Result;
import com.ezhealthtrack.order.SampleMetum;
import com.ezhealthtrack.util.NetworkCallController;
import com.ezhealthtrack.util.NetworkCallController.OnResponse;
import com.ezhealthtrack.util.Util;
import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class OrderController {
	public static CreateOrder order = new CreateOrder();

	public static void getOrderList(String pagenum, Context context,
			final OnResponse responsee, Date startDate, Date endDate,
			PatientAutoSuggest selPat, int billStatus, int orderStatus,
			String query) {
		String url = LabApis.ORDERLIST;
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("cli", "api");
		params.put("page_num", pagenum);
		if (startDate != null)
			params.put("from_date",
					EzApp.sdfddMmyy1.format(startDate));
		if (endDate != null)
			params.put("to_date",
					EzApp.sdfddMmyy1.format(endDate));
		if (billStatus > -1)
			params.put("bill_status", "" + billStatus);
		if (orderStatus > -1)
			params.put("order_status", "" + orderStatus);
		if (selPat != null && selPat.getName().equals(query))
			params.put("patient_id", selPat.getId());
		else if (!Util.isEmptyString(query))
			params.put("search", query);
		Log.i(url, params.toString());
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						if (!response
								.equals(NetworkCallController.NOCONNECTION))
							try {
								Gson gson = new GsonBuilder().setDateFormat(
										"yyyy-MM-dd hh:mm:ss").create();
								JSONArray jobj = new JSONObject(response)
										.getJSONObject("orders")
										.getJSONObject("data")
										.getJSONArray("records");
								for (int i = 0; i < jobj.length(); i++) {
									Log.i("", jobj.getJSONObject(i).toString());
									Order order = gson.fromJson(jobj
											.getJSONObject(i).toString(),
											Order.class);
									order.setOrderDetail(order.orderDetail);
									order.setId(jobj.getJSONObject(i).getLong(
											"order_id"));
									if (order.getOrderFields().size() > 0)
										order.setOrder_external_id(order
												.getOrderFields().get(0)
												.getValue());
									EzApp.orderDao
											.insertOrReplace(order);
									if (order.orderDetail != null)
										EzApp.orderDetailDao
												.insertOrReplace(order.orderDetail);

								}

								Log.i("" + EzApp.orderDao.count(),
										""
												+ EzApp.orderDetailDao
														.count());
								responsee.onResponseListner(new JSONObject(
										response).getJSONObject("orders")
										.getJSONObject("data")
										.getString("total"));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								responsee.onResponseListner("");
							}

					}
				});
	}

	public static void getCreateOrder(final String bkid, Context context,
			final OnResponse responsee) {
		String url = LabApis.GETCREATEORDER.replace("{booking_Id}", bkid);
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("cli", "api");
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						JSONObject jobj;
						try {
							Log.i("", response);
							jobj = new JSONObject(response);
							Log.i("fkey",
									new JSONObject(response).getString("fkey"));
							JSONArray jarr = jobj.getJSONArray("lab_tests");
							for (int i = 0; i < jarr.length(); i++) {
								try {
									JSONArray j = new JSONArray(jarr
											.getJSONObject(i).getString(
													"sample_meta"));
									jarr.getJSONObject(i).put("sample_meta", j);
								} catch (Exception e) {

								}
							}
							// jobj.remove("technician");
							order = new Gson().fromJson(jobj.toString(),
									CreateOrder.class);
							if (jobj.has("episode_id")) {
								responsee.onResponseListner(jobj
										.getString("episode_id"));
							} else {
								responsee.onResponseListner("");
								Log.i(bkid, new Gson().toJson(order));
							}

							if (jobj.has("homeCounsultationCharges")) {
								responsee.onResponseListner(jobj
										.getString("homeCounsultationCharges"));
							} else {
								responsee.onResponseListner("");
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	public static void createOrder(final LabAppointment apt,
			final Context context, final OnResponse responsee, String externalID) {
		String url = LabApis.CREATEORDER + apt.getBkid();
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("format", "json");
		params.put("Lab[patient_id]", apt.getPid());
		params.put("Lab[booking_id]", apt.getBkid());
		params.put("Lab[episode_id]", apt.getEpid());
		params.put("Lab[fkey]", apt.orderView.getOrder().getFkey());
		params.put("Lab[technician_id]", apt.orderView.getLabOrderDetail()
				.getTechnicianId());
		params.put("Lab[generate_bill]", apt.orderView.getOrder()
				.getGenerate_bill());
		if (!Util.isEmptyString(externalID)) {
			params.put("Lab[external_order_id]", externalID);
		}
		params.put("Lab[doctor]", apt.orderView.getLabOrderDetail().getDoctor());
		params.put("Lab[order_notes]", apt.orderView.getLabOrderDetail()
				.getOrderNotes());
		params.put("Lab[patient_loc_order]",
				apt.orderView.getPatientLocAppointment());

		for (int i = 0; i < apt.orderView.getOrder().getData()
				.getOrderProducts().size(); i++) {
			params.put("Lab[products][" + i + "][product_name]", apt.orderView
					.getOrder().getData().getOrderProducts().get(i).getName());
			params.put("Lab[products][" + i + "][product_id]", apt.orderView
					.getOrder().getData().getOrderProducts().get(i)
					.getProductId());
			params.put("Lab[products][" + i + "][product_price]", apt.orderView
					.getOrder().getData().getOrderProducts().get(i).getPrice());
			params.put("Lab[products][" + i + "][product_min_price]", "");
			params.put("Lab[products][" + i + "][product_quantity]", "1");
			if (apt.orderView.getOrder().getData().getOrderProducts().get(i).labTest == null) {
				params.put("Lab[products][" + i + "][product_type]",
						"lab_panel");

			} else {
				params.put("Lab[products][" + i + "][product_type]", "lab_test");
				params.put("Lab[products][" + i + "][product_sample]",
						apt.orderView.getOrder().getData().getOrderProducts()
								.get(i).getProductModel());
			}
			if (apt.orderView.getOrder().getData().getOrderProducts().get(i).labTest == null
					&& apt.orderView.getOrder().getData().getOrderProducts()
							.get(i).labPanel == null) {
				params.put("Lab[products][" + i + "][product_type]",
						"lab_charges");
			}
		}

		Log.i(url, params.toString());

		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						JSONObject jobj;
						try {
							Log.i("", response);
							jobj = new JSONObject(response);
							parseViewOrder(jobj, context, new OnResponseData() {

								@Override
								public void onResponseListner(Object response) {
									apt.orderView = (OrderView) response;
									responsee.onResponseListner("");
								}
							}, apt.orderView);

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	public static void getViewOrder(final Context context,
			final OnResponseData responsee, final OrderView orderView,
			String bkid) {
		String url = LabApis.GETCREATEORDER.replace("{booking_Id}", bkid);
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("cli", "api");
		// params.put("id", orderView.getOrder().getOrder_id());
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						JSONObject jobj;
						try {
							Log.i("", response);
							jobj = new JSONObject(response);
							parseViewOrder(jobj, context, responsee, orderView);
							Log.i("", new Gson().toJson(order));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	public static void parseViewOrder(JSONObject jobj, Context context,
			final OnResponseData responsee, final OrderView orderView) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss")
				.create();
		try {
			JSONArray jarr = jobj.getJSONArray("lab_order_reports");
			for (int i = 0; i < jarr.length(); i++) {
				JSONArray j = new JSONArray(jarr.getJSONObject(i).getString(
						"sample_meta"));
				jarr.getJSONObject(i).put("sample_meta", j);
			}
			final OrderView orderView1;
			orderView1 = new Gson().fromJson(jobj.toString(), OrderView.class);
			orderView1.setOrder(gson.fromJson(jobj.getJSONObject("order")
					.getString("data"), Order.class));
			orderView1.getOrder().setData(
					gson.fromJson(
							jobj.getJSONObject("order").getString("data"),
							Data.class));
			responsee.onResponseListner(orderView1);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void dialogSampling(final Context context, final Order apt,
			final OnResponse onresponse) {
		final Dialog dialogSampling = new Dialog(context);
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
				context, android.R.layout.simple_spinner_item,
				EzApp.labTechnicianDao.loadAll());
		technicianAdapter.setDropDownViewResource(R.layout.labs_row_technician);
		spinnerTechnician.setAdapter(technicianAdapter);

		Patient pat1 = new Patient();
		try {
			if (EzApp.patientDao.queryBuilder()
					.where(Properties.Pid.eq(apt.getCustomer_id())).count() > 0) {
				pat1 = (EzApp.patientDao.queryBuilder()
						.where(Properties.Pid.eq(apt.getCustomer_id())).list()
						.get(0));
			}

		} catch (Exception e) {
			Log.e("", e.toString());
		}

		final Date date = apt.getDate_added();
		((TextView) dialogSampling.findViewById(R.id.txt_order_date_display))
				.setText(EzApp.sdfddMmyy.format(date));
		((TextView) dialogSampling.findViewById(R.id.txt_name_display))
				.setText(pat1.getP_detail());
		((TextView) dialogSampling.findViewById(R.id.txt_order_display))
				.setText(apt.getOrder_display_id());
		((TextView) dialogSampling.findViewById(R.id.txt_bill_status_display))
				.setText(Order.getBillStatus(apt.getBill_status_id()));
		((TextView) dialogSampling.findViewById(R.id.txt_order_status_display))
				.setText(Order.getOrderStatus(apt.getOrder_status_id()));
		((TextView) dialogSampling.findViewById(R.id.txt_technician_display))
				.setText(LabsController.getLabTechnician(
						apt.orderDetail.getTechnician_id()).getName());
		final EditText editNotes = (EditText) dialogSampling
				.findViewById(R.id.edit_note);

		dialogSampling.findViewById(R.id.btn_submit).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						FlurryAgent
								.logEvent("OrderController - Submit Sampling Button Clicked");
						final Dialog loaddialog = Util.showLoadDialog(context);
						apt.getOrderDetail().setTechnician_id(
								""
										+ ((LabTechnician) spinnerTechnician
												.getSelectedItem()).getId());
						apt.getOrderDetail().setSampling_notes(
								editNotes.toString());

						OrderController.orderSampling(context,
								new OnResponseData() {

									@Override
									public void onResponseListner(
											Object response) {
										onresponse
												.onResponseListner("Order Status changed to Sampling");
										Util.Alertdialog(context,
												"Order Status changed to Sampling");
										loaddialog.dismiss();
										apt.setOrder_status_id(Order.ORDER_STATUS_SAMPLING);
										dialogSampling.dismiss();
									}
								}, apt);
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

	public static void orderSampling(final Context context,
			final OnResponseData responsee, final OrderView orderView) {
		String url = LabApis.ORDERSAMPLING + orderView.getOrder().getOrder_id();
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("cli", "api");
		params.put("LabOrderDetail[id]", ""
				+ orderView.getLabOrderDetail().getId());
		params.put("LabOrderDetail[technician_id]", orderView
				.getLabOrderDetail().getTechnicianId());
		params.put("LabOrderDetail[sampling_notes]", orderView
				.getLabOrderDetail().getSamplingNotes());
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						JSONObject jobj;
						try {
							Log.i("", response);
							responsee.onResponseListner(response);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	public static void orderSamplingDone(final Context context,
			final OnResponseData responsee, final OrderView orderView) {
		String url = LabApis.ORDERSAMPLINGDONE
				+ orderView.getOrder().getOrder_id();
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("cli", "api");
		params.put("LabOrderDetail[id]", ""
				+ orderView.getLabOrderDetail().getId());
		params.put("LabOrderDetail[sampling_done_notes]", orderView
				.getLabOrderDetail().getSamplingDoneNotes());
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						JSONObject jobj;
						try {
							Log.i("", response);
							responsee.onResponseListner(response);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	public static void generateBill(final Context context,
			final OnResponseData responsee, final OrderView orderView) {
		String url = LabApis.GENERATEBILL;
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("cli", "api");
		params.put("id", orderView.getLabOrderDetail().getOrderId());
		params.put("status", "generate");
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						JSONObject jobj;
						try {
							Log.i("", response);
							responsee.onResponseListner(response);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	public static void printLabel(final Context context,
			final OnResponseData responsee, final OrderView orderView) {
		String url = LabApis.PRINTLABEL.replaceAll("order_display_id",
				orderView.getOrder().getOrder_display_id());
		final HashMap<String, String> params = new HashMap<String, String>();
		// params.put("api", "barcode");
		// params.put("id", orderView.getOrder().getOrder_display_id());
		// params.put("text", orderView.getOrder().getOrder_display_id());
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						try {
							JSONObject jobj = new JSONObject(response);

							Log.i("", response);
							responsee.onResponseListner(jobj.getString("url"));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	public static void reportValues(final Context context,
			final OnResponseData responsee, final LabOrderReport report) {
		String url = LabApis.REPORTVALUES.replace("{reportid}", report.getId())
				.replace("{orderid}", report.getOrderId());
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("cli", "api");
		params.put("Lab[order_id]", report.getOrderId());
		int i = 0;
		for (final SampleMetum sm : report.getSampleMeta()) {
			List<Result> arrResult;
			if (sm.getResults().size() > 0)
				arrResult = sm.getResults();
			else
				arrResult = sm.sample_meta.get(0).getResults();
			for (Result res : arrResult) {
				params.put("Lab[result_value][" + i + "]", res.getValue());
				params.put("Lab[result_notes][" + i + "]", res.getNotes());
				params.put("Lab[result_status][" + i + "]", res.getStatus());
				i++;
			}
		}
		params.put("Lab[notification_check]", "0");
		Log.i("", params.toString());

		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						try {
							Log.i("", response);
							responsee.onResponseListner(response);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

	}

	public static void getViewOrder1(final Context context,
			final OnResponseData responsee, final OrderView orderView,
			String orderid) {
		String url = LabApis.VIEWORDER + orderView.getOrder().getId();
		final HashMap<String, String> params = new HashMap<String, String>();
		Log.i("", params.toString());
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						JSONObject jobj;
						try {
							Log.i("", response);
							jobj = new JSONObject(response);
							OrderController.parseViewOrder(jobj, context,
									responsee, orderView);
							// Log.i("", new Gson().toJson(order));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	public static void getOrderHistory(final Context context,
			final OnResponseData responsee, final OrderView orderView,
			String orderid) {
		String url = LabApis.ORDERPAYMENTHISTORY + orderView.getOrder().getId();
		final HashMap<String, String> params = new HashMap<String, String>();
		Log.i("", params.toString());
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						JSONObject jobj;
						try {
							Log.i("", response);
							jobj = new JSONObject(response);
							OrderController.parseViewOrder(jobj, context,
									responsee, orderView);
							// Log.i("", new Gson().toJson(order));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	public interface OnResponseData {
		public void onResponseListner(Object response);
	}

	public static void makePayment(final Context context,
			final OnResponseData responsee, final Order order,
			OrderAudit audit, String discountType, String discountAmount) {
		String url = LabApis.MAKEPAYMENT + order.getOrder_id();
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("cli", "api");
		params.put("Lab[order_id]", "" + order.getOrder_id());
		params.put("Lab[paid_amount]", audit.getPaidAmount());
		params.put("Lab[payment_method]", audit.getPaymentMethod());
		params.put("Lab[discount_percentage]", audit.getDiscountPercentage());
		params.put("Lab[tax_percentage]", audit.getTaxPercentage());
		params.put("Lab[comment]", audit.getComment());
		params.put("Lab[discount_type]", discountType);
		params.put("Lab[discount_amount]", discountAmount);

		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						JSONObject jobj;
						try {
							Log.i("", response);
							responsee.onResponseListner(response);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

	}

	public static void reportError(final Context context,
			final OnResponseData responsee, final Order order,
			LabOrderReport report, String note, String errorAction) {
		String url = LabApis.REPORTERROR.replace("{order_id}",
				"" + order.getOrder_id()).replace("{lab_order_report_id}",
				"" + report.getId());
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("cli", "api");
		params.put("LabOrderReport[id]", "" + report.getId());
		params.put("LabOrderReport[error_action]", errorAction);
		params.put("LabOrderReport[notes]", note);

		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						JSONObject jobj;
						try {
							Log.i("", response);
							responsee.onResponseListner(response);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

	}

	public static void refundSubmit(final Context context,
			final OnResponseData responsee, final Order order, String reportid,
			String note, String paidAmount, String balanceAmount,
			String paymentMeathod) {
		String url = LabApis.BILLREFUNDSUBMIT.replace("{order_id}",
				"" + order.getOrder_id()).replace("{lab_order_report_id}",
				"" + reportid);
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("cli", "api");
		params.put("Lab[order_id]", "" + order.getOrder_id());
		params.put("Lab[total_amount]", order.getTotal_amount());
		params.put("Lab[paid_amount]", paidAmount);
		params.put("Lab[balance_amount]", balanceAmount);
		params.put("Lab[payment_method]", paymentMeathod);
		params.put("Lab[comment]", note);

		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						JSONObject jobj;
						try {
							Log.i("", response);
							responsee.onResponseListner(response);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

	}

	public static void publishReport(final Context context,
			final OnResponseData responsee, final OrderView orderView) {
		String url = LabApis.PUBLISHREPORT.replace("{order_id}", ""
				+ orderView.getOrder().getOrder_id());
		final HashMap<String, String> params = new HashMap<String, String>();

		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						JSONObject jobj;
						try {
							Log.i("", response);
							responsee.onResponseListner(response);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

	}

	public static void unpublishReport(final Context context,
			final OnResponseData responsee, final OrderView orderView) {
		String url = LabApis.UNPUBLISHREPORT.replace("{order_id}", ""
				+ orderView.getOrder().getOrder_id());
		final HashMap<String, String> params = new HashMap<String, String>();

		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						JSONObject jobj;
						try {
							Log.i("", response);
							responsee.onResponseListner(response);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

	}

	public static void reportApproval(final Context context,
			final OnResponseData responsee, final LabOrderReport report,
			String action, String comment) {
		String url = LabApis.REPORTAPPROVAL.replace("{data_id}",
				"" + report.getId())
				+ action;
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("cli", "api");
		params.put("comment", comment);

		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						JSONObject jobj;
						try {
							Log.i("", response);
							responsee.onResponseListner(response);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

	}

	public static void orderSampling(final Context context,
			final OnResponseData responsee, final Order orderView) {
		String url = LabApis.ORDERSAMPLING + orderView.getId();
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("cli", "api");
		params.put("LabOrderDetail[id]", ""
				+ orderView.getOrderDetail().getId());
		params.put("LabOrderDetail[technician_id]", orderView.getOrderDetail()
				.getTechnician_id());
		params.put("LabOrderDetail[sampling_notes]", orderView.getOrderDetail()
				.getSampling_notes());
		Log.i("", params.toString());
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						JSONObject jobj;
						try {
							Log.i("", response);
							responsee.onResponseListner(response);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	public static void dialogCancel(final Order order, final Context context,
			final Patient pat, final OnResponse response) {
		final Dialog dialogReportError = new Dialog(context);

		dialogReportError.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogReportError.setContentView(R.layout.labs_dialog_order_cancel);
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
		ArrayList<String> arrAction = new ArrayList<String>();
		arrAction.add("Generate New Order");
		arrAction.add("Refund");

		final Spinner spinnerAction = (Spinner) dialogReportError
				.findViewById(R.id.spinner_action);

		final ArrayAdapter<String> actionAdapter = new ArrayAdapter<String>(
				context, android.R.layout.simple_spinner_item, arrAction);
		actionAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerAction.setAdapter(actionAdapter);

		if (order.getGenerate_bill().equals("0")) {
			((TextView) dialogReportError
					.findViewById(R.id.txt_bill_status_display))
					.setVisibility(View.GONE);
			((TextView) dialogReportError.findViewById(R.id.txt_bill_status))
					.setVisibility(View.GONE);

		} else {
			((TextView) dialogReportError
					.findViewById(R.id.txt_bill_status_display))
					.setVisibility(View.VISIBLE);
			((TextView) dialogReportError.findViewById(R.id.txt_bill_status))
					.setVisibility(View.VISIBLE);
		}

		final Date date = order.getDate_added();
		((TextView) dialogReportError.findViewById(R.id.txt_order_date_display))
				.setText(EzApp.sdfddMmyy.format(date));
		((TextView) dialogReportError.findViewById(R.id.txt_name_display))
				.setText(pat.getP_detail());
		((TextView) dialogReportError.findViewById(R.id.txt_order_display))
				.setText(order.getOrder_display_id());
		((TextView) dialogReportError
				.findViewById(R.id.txt_bill_status_display)).setText(Order
				.getBillStatus(order.getBill_status_id()));
		((TextView) dialogReportError
				.findViewById(R.id.txt_order_status_display)).setText(Order
				.getOrderStatus(order.getOrder_status_id()));
		((TextView) dialogReportError.findViewById(R.id.txt_technician_display))
				.setText(LabsController.getLabTechnician(
						order.getOrderDetail().getTechnician_id()).getName());
		// ((TextView) dialogReportError.findViewById(R.id.txt_report_display))
		// .setText(report.getLabTestName());

		((Button) dialogReportError.findViewById(R.id.btn_submit))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						FlurryAgent
								.logEvent("OrderController - Submit Cancel Button Clicked");
						if (spinnerAction.getSelectedItemPosition() == 0) {
							final Dialog loaddialog = Util
									.showLoadDialog(context);
							OrderController.orderCancel(context,
									new OnResponseData() {

										@Override
										public void onResponseListner(
												Object responsee) {
											Util.Alertdialog(context,
													"Order cancelled sucessfully.");
											loaddialog.dismiss();
											dialogReportError.dismiss();
											response.onResponseListner("sucess");

										}
									}, order, ((EditText) dialogReportError
											.findViewById(R.id.edit_note))
											.getText().toString(), "new order");

						} else {
							final Dialog loaddialog = Util
									.showLoadDialog(context);
							OrderController.orderCancel(context,
									new OnResponseData() {

										@Override
										public void onResponseListner(
												Object responsee) {
											Util.Alertdialog(context,
													"Order cancel sucessfully.");
											response.onResponseListner("sucess");
											dialogReportError.dismiss();
											loaddialog.dismiss();
											Intent intent = new Intent(
													context,
													LabsBillRefundActivity.class);
											intent.putExtra("orderid",
													order.getOrder_id());
											context.startActivity(intent);

										}
									}, order, ((EditText) dialogReportError
											.findViewById(R.id.edit_note))
											.getText().toString(), "refund");

						}
					}
				});

	}

	public static void orderCancel(final Context context,
			final OnResponseData responsee, final Order order, String note,
			String errorAction) {
		String url = LabApis.ORDERCANCEL + order.getId();
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("cli", "api");
		params.put("Lab[cancel_action]", errorAction);
		params.put(" Lab[comment]", note);

		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						JSONObject jobj;
						try {
							Log.i("", response);
							responsee.onResponseListner(response);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

	}

	public static void getBillList(String pagenum, Context context,
			final OnResponse responsee, Date startDate, Date endDate,
			PatientAutoSuggest selPat, int billStatus, String query) {
		String url = LabApis.BILLLIST;
		Log.i(url, url);
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("cli", "api");
		params.put("page_num", pagenum);
		if (startDate != null)
			params.put("from_date",
					EzApp.sdfddMmyy1.format(startDate));
		if (endDate != null)
			params.put("to_date",
					EzApp.sdfddMmyy1.format(endDate));
		if (billStatus > -1)
			params.put("bill_status", "" + billStatus);
		if (selPat != null && selPat.getName().equals(query))
			params.put("patient_id", selPat.getId());
		else if (!Util.isEmptyString(query))
			params.put("search", query);
		Log.i(url, params.toString());
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						if (!response
								.equals(NetworkCallController.NOCONNECTION))
							try {
								Gson gson = new GsonBuilder().setDateFormat(
										"yyyy-MM-dd hh:mm:ss").create();
								JSONArray jobj = new JSONObject(response)
										.getJSONObject("orders")
										.getJSONObject("data")
										.getJSONArray("records");
								for (int i = 0; i < jobj.length(); i++) {
									Log.i("", jobj.getJSONObject(i).toString());
									Order order = gson.fromJson(jobj
											.getJSONObject(i).toString(),
											Order.class);
									order.setOrderDetail(order.orderDetail);
									order.setId(jobj.getJSONObject(i).getLong(
											"order_id"));
									order.setOrder_bill_id(order.getId()
											+ "-01");
									if (order.getOrderFields().size() > 0)
										order.setOrder_external_id(order
												.getOrderFields().get(0)
												.getValue());
									EzApp.orderDao
											.insertOrReplace(order);
									if (order.orderDetail != null)
										EzApp.orderDetailDao
												.insertOrReplace(order.orderDetail);

								}

								Log.i("" + EzApp.orderDao.count(),
										""
												+ new JSONObject(response)
														.getJSONObject("orders")
														.getJSONObject("data")
														.getString("total"));
								responsee.onResponseListner(new JSONObject(
										response).getJSONObject("orders")
										.getJSONObject("data")
										.getString("total"));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								responsee.onResponseListner("");
							}

					}
				});
	}

}

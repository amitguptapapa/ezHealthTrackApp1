package com.ezhealthtrack.labs.adapter;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.controller.MessageController;
import com.ezhealthtrack.controller.PatientController;
import com.ezhealthtrack.controller.PatientController.OnResponsePatient;
import com.ezhealthtrack.greendao.Order;
import com.ezhealthtrack.greendao.Patient;
import com.ezhealthtrack.labs.activity.LabsBillPaymentHistoryActivity;
import com.ezhealthtrack.labs.activity.LabsOrderDetailsActivity;
import com.ezhealthtrack.labs.controller.LabWorkFlowController;
import com.ezhealthtrack.labs.controller.LabsController;
import com.ezhealthtrack.labs.controller.OrderController;
import com.ezhealthtrack.order.Step;
import com.ezhealthtrack.order.Transition;
import com.ezhealthtrack.order.WorkFlow;
import com.ezhealthtrack.util.LazyListAdapter;
import com.ezhealthtrack.util.NetworkCallController.OnResponse;
import com.ezhealthtrack.util.Util;
import com.flurry.android.FlurryAgent;

public class LabsOrderAdapter extends LazyListAdapter<Order> {
	/* private view holder class */
	private static class ViewHolder {
		private TextView txtOrders;
		private RelativeLayout rlActions;
		private Button btnViewOrder;
		private Button btnSendMessage;
		private Button btnSampling;
		private Button btnBilling;
		private Button btnCancel;
		private Button btnNoBill;
	}

	Context context;

	public LabsOrderAdapter(final Context context) {
		this.context = context;
	}

	@Override
	public View getView(final int position, final View convertView,
			final ViewGroup parent) {

		final Order rowItem = getItem(position);

		View row = null;
		if (convertView == null) {
			final ViewHolder holder = new ViewHolder();
			final LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.labs_row_orders, null);
			holder.txtOrders = (TextView) row.findViewById(R.id.txt_orders);
			holder.rlActions = (RelativeLayout) row
					.findViewById(R.id.rl_actions);
			holder.btnViewOrder = (Button) row
					.findViewById(R.id.btn_view_orders);
			holder.btnSendMessage = (Button) row
					.findViewById(R.id.btn_send_message);
			holder.btnBilling = (Button) row.findViewById(R.id.btn_billings);
			holder.btnSampling = (Button) row.findViewById(R.id.btn_sampling);
			holder.btnCancel = (Button) row.findViewById(R.id.btn_cancel);
			holder.btnNoBill = (Button) row.findViewById(R.id.btn_no_bill);
			holder.btnNoBill.setEnabled(false);
			row.setTag(holder);
		} else {
			row = convertView;
		}
		final ViewHolder holder = (ViewHolder) row.getTag();
		Date currentDate = Calendar.getInstance().getTime();
		currentDate.setHours(0);
		currentDate.setMinutes(0);
		currentDate.setSeconds(0);
		Date tomorrowDate = new Date(currentDate.getTime() + 24 * 60 * 60000
				- 60000);
		PatientController.getPatient(rowItem.getCustomer_id(), context,
				new OnResponsePatient() {

					@Override
					public void onResponseListner(final Patient pat) {
						if (Util.isEmptyString(rowItem.getOrder_display_id()))
							rowItem.setOrder_display_id("");
						String s = rowItem.getOrder_display_id();
						try {
							if (rowItem.getOrderFields().size() > 0) {
								s = s
										+ ", "
										+ "<b>"
										+ "Ext ID: "
										+ "</b>"
										+ rowItem.getOrderFields().get(0)
												.getValue();
							}
							if (!Util.isEmptyString(rowItem.getOrderStatus())) {
								s = s + ", " + pat.getP_detail() + "<b>"
										+ " Order Date : " + "</b>"
										+ rowItem.getDateAdded() + ", " + "<b>"
										+ "Order Status: " + "</b>"
										+ rowItem.getOrderStatus();
							}

							if (rowItem.getGenerate_bill().equals("1")) {
								s = s + ", " + "<b>" + "Bill Status: " + "</b>"
										+ rowItem.getBillStatus();
							}

							s = s
									+ ", "
									+ "<b>"
									+ "Assign To: "
									+ "</b>"
									+ LabsController.getLabTechnician(
											rowItem.getOrderDetail()
													.getTechnician_id())
											.getName();

							s = s + ", " + "<b>" + " Approval Status : "
									+ "</b>" + rowItem.getApproval_status();

							if (!Util.isEmptyString(rowItem.getApproved_by())) {
								s = s + ", " + "<b>" + " Approved By : "
										+ "</b>" + rowItem.getApproved_by();
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
						holder.txtOrders.setText(Html.fromHtml(s));
						holder.btnCancel
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										FlurryAgent
												.logEvent("LabsOrderAdapter - Cancel Button Clicked");
										OrderController.dialogCancel(rowItem,
												context, pat, new OnResponse() {

													@Override
													public void onResponseListner(
															String response) {
														rowItem.setOrder_status_id(Order.ORDER_STATUS_CANCEL);
														EzApp.orderDao
																.insertOrReplace(rowItem);
														notifyDataSetChanged();

													}
												});

									}
								});

					}
				});
		holder.btnSampling.setVisibility(View.GONE);
		holder.btnBilling.setVisibility(View.GONE);
		holder.btnCancel.setVisibility(View.GONE);
		if (Util.isEmptyString(rowItem.getOrder_workflow_id()))
			rowItem.setOrder_workflow_id(""
					+ EzApp.sharedPref.getLong(
							LabWorkFlowController.lab_order, 0));
		LabWorkFlowController.getWorkFlowById(
				Long.parseLong(rowItem.getOrder_workflow_id()), context,
				new LabWorkFlowController.OnResponseWorkFlow() {

					@Override
					public void onResponseWorkFlow(WorkFlow workFlow) {
						Step step = workFlow.getStepByName(rowItem
								.getOrderStatus());
						for (Transition transition : step.getTransitions()) {
							if (transition.getLabel().equalsIgnoreCase(
									"Sampling"))
								holder.btnSampling.setVisibility(View.VISIBLE);
							if (transition.getLabel()
									.equalsIgnoreCase("Cancel"))
								holder.btnCancel.setVisibility(View.VISIBLE);
						}

					}
				});
		// if (rowItem.getOrder_status_id().equals(Order.ORDER_STATUS_NEW))
		// holder.btnSampling.setVisibility(View.VISIBLE);
		// else
		// holder.btnSampling.setVisibility(View.GONE);

		if (rowItem.getGenerate_bill().equals("1")) {
			holder.btnBilling.setVisibility(View.VISIBLE);
			holder.btnNoBill.setVisibility(View.GONE);
		} else {
			holder.btnBilling.setVisibility(View.GONE);
			holder.btnNoBill.setVisibility(View.VISIBLE);
		}

		// if (rowItem.getOrder_status_id().equals(Order.ORDER_STATUS_NEW)
		// || rowItem.getOrder_status_id().equals(
		// Order.ORDER_STATUS_PENDING)
		// || rowItem.getOrder_status_id().equals(
		// Order.ORDER_STATUS_SAMPLING))
		// holder.btnCancel.setVisibility(View.VISIBLE);
		// else
		// holder.btnCancel.setVisibility(View.GONE);

		holder.txtOrders.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				if (holder.rlActions.getVisibility() == View.GONE) {
					holder.rlActions.setVisibility(View.VISIBLE);
				} else {
					holder.rlActions.setVisibility(View.GONE);
				}

			}
		});

		holder.btnSendMessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				FlurryAgent
						.logEvent("LabsOrderAdapter - Send Message Button Clicked");
				MessageController.showSendMessageDialog(context,
						"" + rowItem.getOrder_id(), rowItem.getCustomer_id(),
						"0", rowItem.getDate_added(), "lab_order");
			}
		});

		holder.btnViewOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				FlurryAgent
						.logEvent("LabsOrderAdapter - Order Details Button Clicked");
				Intent intent;
				intent = new Intent(context, LabsOrderDetailsActivity.class);
				intent.putExtra("orderid", rowItem.getId());
				Log.i("", "" + rowItem.getId());
				context.startActivity(intent);
			}
		});

		holder.btnBilling.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FlurryAgent
						.logEvent("LabsOrderAdapter - Bill Payment History Button Clicked");
				Intent intent;
				intent = new Intent(context,
						LabsBillPaymentHistoryActivity.class);
				intent.putExtra("orderid", rowItem.getId());
				Log.i("", "" + rowItem.getId());
				context.startActivity(intent);

			}
		});
		holder.btnSampling.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FlurryAgent
						.logEvent("LabsOrderAdapter - Sampling Button Clicked");
				OrderController.dialogSampling(context, rowItem,
						new OnResponse() {

							@Override
							public void onResponseListner(String response) {
								rowItem.setOrder_status_id(Order.ORDER_STATUS_SAMPLING);
								EzApp.orderDao
										.insertOrReplace(rowItem);
								notifyDataSetChanged();
							}
						});

			}
		});

		return row;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}
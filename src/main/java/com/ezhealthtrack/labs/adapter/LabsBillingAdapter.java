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

import com.ezhealthtrack.R;
import com.ezhealthtrack.controller.MessageController;
import com.ezhealthtrack.controller.PatientController;
import com.ezhealthtrack.controller.PatientController.OnResponsePatient;
import com.ezhealthtrack.greendao.Order;
import com.ezhealthtrack.greendao.Patient;
import com.ezhealthtrack.labs.activity.LabsBillPaymentActivity;
import com.ezhealthtrack.labs.activity.LabsBillPaymentHistoryActivity;
import com.ezhealthtrack.labs.controller.LabsController;
import com.ezhealthtrack.util.LazyListAdapter;
import com.flurry.android.FlurryAgent;

public class LabsBillingAdapter extends LazyListAdapter<Order> {
	/* private view holder class */
	private static class ViewHolder {
		private TextView txtBilling;
		private RelativeLayout rlActions;
		private Button btnViewBill;
		private Button btnSendMessage;
		private Button btnPayment;

	}

	Context context;

	int type;

	public LabsBillingAdapter(final Context context, int type) {
		this.context = context;
		this.type = type;
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
			row = inflater.inflate(R.layout.labs_row_billing, null);
			holder.txtBilling = (TextView) row.findViewById(R.id.txt_billing);

			holder.btnViewBill = (Button) row.findViewById(R.id.btn_view_bill);
			holder.btnSendMessage = (Button) row
					.findViewById(R.id.btn_send_message);
			holder.btnPayment = (Button) row.findViewById(R.id.btn_payment);
			holder.rlActions = (RelativeLayout) row
					.findViewById(R.id.rl_actions);
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
					public void onResponseListner(Patient pat) {
						try {
							holder.txtBilling.setText(Html.fromHtml(rowItem
									.getOrder_display_id()
									+ ", "
									+ pat.getP_detail()
									+ "<b>"
									+ " Order Date : "
									+ "</b>"
									+ rowItem.getDateAdded()
									+ ", "
									+ "<b>"
									+ "Order Status: "
									+ "</b>"
									+ rowItem.getOrderStatus()
									+ ", "
									+ "<b>"
									+ "Bill Status: "
									+ "</b>"
									+ rowItem.getBillStatus()
									+ ", "
									+ "<b>"
									+ "Assign To: "
									+ "</b>"
									+ LabsController.getLabTechnician(
											rowItem.getOrderDetail()
													.getTechnician_id())
											.getName()));
						} catch (Exception e) {

						}

					}
				});

		holder.txtBilling.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				try {
					if (holder.rlActions.getVisibility() == View.GONE) {
						holder.rlActions.setVisibility(View.VISIBLE);
					} else {
						holder.rlActions.setVisibility(View.GONE);
					}
				} catch (Exception e) {

				}

			}
		});

		holder.btnSendMessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				FlurryAgent
						.logEvent("LabsBillingAdapter - Send Message Button Clicked");
				try {
					MessageController.showSendMessageDialog(context, ""
							+ rowItem.getOrder_id(), rowItem.getCustomer_id(),
							"0", rowItem.getDate_added(), "lab_order");
				} catch (Exception e) {

				}
			}
		});

		holder.btnViewBill.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				FlurryAgent
						.logEvent("LabsBillingAdapter - Bill Payment History Button Clicked");
				Intent intent;
				intent = new Intent(context,
						LabsBillPaymentHistoryActivity.class);
				intent.putExtra("orderid", rowItem.getId());
				Log.i("", "" + rowItem.getId());
				context.startActivity(intent);
			}
		});

		holder.btnPayment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FlurryAgent
						.logEvent("LabsBillingAdapter - Bill Payment Button Clicked");
				Intent intent;
				intent = new Intent(context, LabsBillPaymentActivity.class);
				intent.putExtra("orderid", rowItem.getId());
				context.startActivity(intent);
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
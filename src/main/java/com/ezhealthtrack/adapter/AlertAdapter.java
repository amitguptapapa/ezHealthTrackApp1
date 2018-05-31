package com.ezhealthtrack.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.fragments.AlertsFragment;
import com.ezhealthtrack.greendao.MessageModel;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.Util;

public class AlertAdapter extends ArrayAdapter<MessageModel> {
	/* private view holder class */
	private static class ViewHolder {
		private TextView txtHeader;
		private TextView txtSubject;
		private Button btnDetails;
		private RelativeLayout rlActions;
	}

	private final SharedPreferences sharedPref;

	Context context;
	private static ArrayList<MessageModel> objects = new ArrayList<MessageModel>();

	public AlertAdapter(final Context context, final int resourceId,
			final ArrayList<MessageModel> item) {
		super(context, resourceId, AlertAdapter.objects);
		this.context = context;
		AlertAdapter.objects.clear();
		AlertAdapter.objects.addAll(item);
		sharedPref = context.getSharedPreferences(Constants.EZ_SHARED_PREF,
				Context.MODE_PRIVATE);
	}

	private void dialogDetails(final MessageModel message) {
		final String url = APIs.VIEWALERT();
		final Dialog loaddialog = Util.showLoadDialog(context);
		Log.i("", url);
		final StringRequest logoutRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i("", response);
						try {
							final JSONObject jObj = new JSONObject(response);
							final Dialog dialog = new Dialog(getContext());
							dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
							dialog.setContentView(R.layout.dialog_view_message);
							final JSONObject data = new JSONObject(jObj
									.getJSONObject("notification").getString(
											"data"));
							((TextView) dialog.findViewById(R.id.txt_subject))
									.setText(": " + data.getString("sub"));
							dialog.getWindow()
									.setBackgroundDrawable(
											new ColorDrawable(
													android.graphics.Color.TRANSPARENT));
							final LayoutInflater inflater = (LayoutInflater) context
									.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

							final View v = inflater.inflate(R.layout.item_conv,
									null);
							((LinearLayout) dialog.findViewById(R.id.ll_conv))
									.addView(v);

							((TextView) v.findViewById(R.id.txt_name))
									.setText("ezHealthTrack Team");
							((TextView) v.findViewById(R.id.txt_text))
									.setText(": "
											+ new JSONObject(data
													.getString("data"))
													.getString("text"));
							final SimpleDateFormat dfShow = new SimpleDateFormat(
									"EEE MMM dd ','yyyy");
							final SimpleDateFormat dfOrig = new SimpleDateFormat(
									"yyyy-MM-dd");
							final Date date = message.getDate();
							((TextView) dialog.findViewById(R.id.txt_date))
									.setText("DATE : " + dfShow.format(date));
							dialog.findViewById(R.id.txt_close)
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(final View v) {
											dialog.dismiss();
										}
									});

							dialog.show();
						} catch (final Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
//						Toast.makeText(context,
//								"There is some error while sending message",
//								Toast.LENGTH_SHORT).show();

						Log.e("Error.Response", error);
						loaddialog.dismiss();
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util.getBase64String(sharedPref
						.getString(Constants.USER_TOKEN, "")));
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("format", "json");
				loginParams.put("cli", "api");
				loginParams.put("nid", message.getNid());
				if (message.getContext_type().equalsIgnoreCase("notification")) {
					loginParams.put("context", message.getContext());
				} else {
					loginParams.put("context", message.getNid());
				}
				loginParams.put("to_id", message.getTo_id());
				loginParams.put("to_fam_id", message.getTo_fam_id());
				loginParams.put("to_type", message.getTo_type());
				Log.i("params", loginParams.toString());
				return loginParams;
			}

		};
		logoutRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(logoutRequest);

	}

	@Override
	public View getView(final int position, final View convertView,
			final ViewGroup parent) {

		final MessageModel rowItem = getItem(position);

		View row = null;
		if (convertView == null) {
			final ViewHolder holder = new ViewHolder();
			final LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.row_inbox, null);
			holder.txtHeader = (TextView) row.findViewById(R.id.txt_header_hospital);
			holder.txtSubject = (TextView) row.findViewById(R.id.txt_subject);
			holder.btnDetails = (Button) row.findViewById(R.id.btn_details);
			holder.rlActions = (RelativeLayout) row
					.findViewById(R.id.rl_actions);
			row.setTag(holder);
		} else {
			row = convertView;
		}
		final ViewHolder holder = (ViewHolder) row.getTag();
		try {
			holder.txtHeader.setText(new JSONObject(rowItem.getData())
					.getString("text") + "(" + rowItem.getConv_count() + ")");
			holder.txtSubject.setText(new JSONObject(rowItem.getData())
					.getString("sub"));
		} catch (final JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final View view = row.findViewById(R.id.rl_main);
		if (!rowItem.getNotified().equals("Y")) {
			view.setBackgroundResource(R.drawable.bg_unread);
		} else {
			view.setBackgroundResource(R.drawable.bg_blue_round);
		}
		holder.btnDetails.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				dialogDetails(rowItem);
				view.setBackgroundResource(R.drawable.bg_blue_round);
				if (rowItem.getNotified().equals("N")) {
					rowItem.setNotified("Y");
					--AlertsFragment.unreadCount;
					((TextView) ((Activity) context)
							.findViewById(R.id.txt_count_messages))
							.setText(AlertsFragment.unreadCount
									+ " new messages, "
									+ AlertsFragment.totalCount
									+ " total messages");
				}
			}
		});
		holder.txtHeader.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				if (holder.rlActions.getVisibility() == View.GONE) {
					holder.rlActions.setVisibility(View.VISIBLE);
				} else {
					holder.rlActions.setVisibility(View.GONE);
				}

			}
		});
		return row;
	}
}
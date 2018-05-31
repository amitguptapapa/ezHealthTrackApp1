package com.ezhealthtrack.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.fragments.AssistantSchedulePlanFragment;
import com.ezhealthtrack.model.VacationModel;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.Util;

public class VacationAdapter extends ArrayAdapter<VacationModel> {
	Context context;
	String asstId;

	public VacationAdapter(Context context, int resourceId,
			List<VacationModel> item) {
		super(context, resourceId, item);

		this.context = context;
	}

	public VacationAdapter(Context context, int resourceId,
			List<VacationModel> item, String asstId) {
		super(context, resourceId, item);
		this.asstId = asstId;
		this.context = context;
	}

	/* private view holder class */
	private static class ViewHolder {
		private TextView txtStartDate;
		private TextView txtEndDate;
		private TextView btnDeleteVacation;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final VacationModel rowItem = getItem(position);

		View row = null;
		if (convertView == null) {
			ViewHolder holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.row_vacation, null);
			holder.txtStartDate = (TextView) row
					.findViewById(R.id.txt_start_date);
			holder.txtEndDate = (TextView) row.findViewById(R.id.txt_end_date);
			holder.btnDeleteVacation = (TextView) row
					.findViewById(R.id.btn_delete_vacation);
			row.setTag(holder);
		} else {
			row = convertView;
		}
		final ViewHolder holder = (ViewHolder) row.getTag();
		holder.txtStartDate.setText(rowItem.getStartDate());
		holder.txtEndDate.setText(rowItem.getEndDate());
		holder.btnDeleteVacation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Util.isEmptyString(asstId))
					removeVacation(rowItem);
				else
					removeVacation(rowItem, asstId);
			}
		});

		return row;
	}

	private void removeVacation(final VacationModel vacation) {
		final String url = APIs.ADD_DELETE_VACATIONS();
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						try {
							JSONObject jObj = new JSONObject(response);
							Log.i("get alerts", response);
							DashboardActivity.arrVacations.remove(vacation);
							VacationAdapter.this.notifyDataSetChanged();
						} catch (JSONException e) {
							Util.Alertdialog(context, e.toString());
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(context, error.toString());

						Log.e("Error.Response", error);
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util
						.getBase64String(EzApp.sharedPref
								.getString(Constants.USER_TOKEN, "")));
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> params = new HashMap<String, String>();
				params.put("type", "remove");
				params.put("vacation-id", vacation.getId());
				return params;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);

	}

	private void removeVacation(final VacationModel vacation, final String asstId) {
		final String url = APIs.ADD_DELETE_ASST_VACATIONS();
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						try {
							JSONObject jObj = new JSONObject(response);
							Log.i("get alerts", response);
							AssistantSchedulePlanFragment.arrVacations.remove(vacation);
							VacationAdapter.this.notifyDataSetChanged();
						} catch (JSONException e) {
							Util.Alertdialog(context, e.toString());
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(context, error.toString());

						Log.e("Error.Response", error);
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util
						.getBase64String(EzApp.sharedPref
								.getString(Constants.USER_TOKEN, "")));
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> params = new HashMap<String, String>();
				params.put("type", "remove");
				params.put("vacation-id", vacation.getId());
				params.put("asst_id", asstId);
				return params;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);

	}
}
package com.ezhealthtrack.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.model.DoctorModel;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.Util;

public class DoctorsAutoCompleteAdapter extends ArrayAdapter<DoctorModel> {
	private static class ViewHolder {
		private TextView txt;
	}

	SharedPreferences sharedPref;
	List<DoctorModel> objects;

	String epId;

	public DoctorsAutoCompleteAdapter(final Context context,
			final int resource, final List<DoctorModel> objects,
			final SharedPreferences sp, final String epId) {
		super(context, resource, objects);
		sharedPref = sp;
		this.objects = objects;
		this.epId = epId;
	}

	private void getDocList(final String name) {

		final String url = APIs.DOCTOR_LIST();
		final StringRequest patientListRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						try {
							Log.i("doclist", response);
							final JSONArray jArray = new JSONArray(response);
							objects.clear();
							for (int i = 0; i < jArray.length(); i++) {
								final JSONObject jObj = jArray.getJSONObject(i);
								if(!jObj.getString("id").equals(sharedPref.getString(Constants.ROLE_ID, ""))){
								final DoctorModel dm = new DoctorModel();
								dm.name = jObj.getString("name");
								dm.speciality = jObj.getString("doc_spe");
								dm.id = jObj.getString("id");
								objects.add(dm);
								notifyDataSetChanged();
								}
							}

						} catch (final JSONException e) {
							
//							Toast.makeText(
//									getContext(),
//									"There is some error while fetching data please try again.",
//									Toast.LENGTH_SHORT).show();
							Log.e("", e);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
//						Toast.makeText(
//								getContext(),
//								"There is some error while fetching data please try again",
//								Toast.LENGTH_SHORT).show();

						error.printStackTrace();
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
				loginParams.put("doc_id",
						sharedPref.getString(Constants.USER_ID, ""));
				loginParams.put("ep_id", epId);
				loginParams.put("name", name);
				Log.i("Login Params", loginParams.toString());
				return loginParams;
			}

		};
		patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(patientListRequest);

	}

	@Override
	public Filter getFilter() {
		final Filter filter = new Filter() {
			@Override
			protected FilterResults performFiltering(
					final CharSequence constraint) {
				final FilterResults filterResults = new FilterResults();
				if (constraint != null) {
					getDocList((String) constraint);
				}
				return filterResults;
			}

			@Override
			protected void publishResults(final CharSequence constraint,
					final FilterResults results) {

			}
		};
		return filter;
	}

	@Override
	public View getView(final int position, final View convertView,
			final ViewGroup parent) {
		final DoctorModel rowItem = getItem(position);

		View row = null;
		if (convertView == null) {
			final ViewHolder holder = new ViewHolder();
			final LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(
					android.R.layout.simple_spinner_dropdown_item, null);
			row.setPadding(5, 5, 5, 5);
			holder.txt = (TextView) row.findViewById(android.R.id.text1);
			row.setTag(holder);
		} else {
			row = convertView;
		}
		final ViewHolder holder = (ViewHolder) row.getTag();
		holder.txt.setText(rowItem.name + "," + rowItem.speciality);
		return row;
	}

}

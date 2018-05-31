package com.ezhealthtrack.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.ezhealthtrack.model.AllergyModel.Datum;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Util;

public class AllergiesAllergyAdapter extends ArrayAdapter<Datum> {
	/* private view holder class */
	private static class ViewHolder {
		private TextView txtAllergy;
		private RelativeLayout rlActions;
		private Button btnDelete;
	}

	private final Context context;
	private Dialog dialog;

	public AllergiesAllergyAdapter(final Context context, final int resourceId,
			ArrayList<Datum> arrDatum, Dialog dialog) {
		super(context, resourceId, arrDatum);
		this.context = context;
		this.dialog = dialog;

	}

	@Override
	public View getView(final int position, final View convertView,
			final ViewGroup parent) {
		View row = null;
		final Datum rowItem = getItem(position);
		if (convertView == null) {
			final ViewHolder holder = new ViewHolder();
			final LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.row_mrecords_allergies, null);
			holder.txtAllergy = (TextView) row
					.findViewById(R.id.txt_medrec_allergies);
			holder.rlActions = (RelativeLayout) row
					.findViewById(R.id.rl_actions);
			holder.btnDelete = (Button) row.findViewById(R.id.btn_delete);

			row.setTag(holder);
		} else {
			row = convertView;
		}

		final ViewHolder holder = (ViewHolder) row.getTag();
		if (Util.isEmptyString(rowItem.getAdditionalInfo())) {
			holder.txtAllergy.setText(rowItem.getPatName()
					+ " has allergy with " + rowItem.getMainCat().getNAME()
					+ " --> " + rowItem.getSubCat().getNAME() + " added on "
					+ rowItem.getDateCreated());
		} else {
			holder.txtAllergy.setText(rowItem.getPatName()
					+ " has allergy with " + rowItem.getMainCat().getNAME()
					+ " --> " + rowItem.getSubCat().getNAME() + " added on "
					+ rowItem.getDateCreated() + ", Additional info Provided: "
					+ rowItem.getAdditionalInfo());
		}
		Log.i("", holder.txtAllergy.getText().toString());

		holder.btnDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final String url = APIs.DELETE_ALLERGY();
				final Dialog loaddialog = Util.showLoadDialog(context);
				final StringRequest patientListRequest = new StringRequest(
						Request.Method.POST, url,
						new Response.Listener<String>() {
							@Override
							public void onResponse(final String response) {
								try {
									Log.i("delete document", response);
									final JSONObject jObj = new JSONObject(
											response);
									if (jObj.getString("s").equals("200")) {
										dialog.dismiss();
										Util.Alertdialog(context,
												"Allergy deleted successfully");

									} else {
										Util.Alertdialog(context,
												"There is some error while deleting, please try again.");
									}
								} catch (final JSONException e) {
									Util.Alertdialog(context,
											"There is some error while deleting, please try again.");
								}
								loaddialog.dismiss();
							}
						}, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(final VolleyError error) {
								Util.Alertdialog(context,
										"There is some network error, please try again.");

								error.printStackTrace();
								loaddialog.dismiss();
							}
						}) {

					@Override
					public Map<String, String> getHeaders()
							throws AuthFailureError {
						final HashMap<String, String> loginParams = new HashMap<String, String>();
						loginParams.put("auth-token", Util
								.getBase64String(EzApp.sharedPref
										.getString(Constants.USER_TOKEN, "")));
						return loginParams;
					}

					@Override
					protected Map<String, String> getParams() {
						final HashMap<String, String> loginParams = new HashMap<String, String>();
						final SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd");
						loginParams.put("format", "json");
						loginParams.put("a_id", rowItem.getAllergieId());
						loginParams.put("cli", "api");
						return loginParams;
					}

				};
				patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
						5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
				EzApp.mVolleyQueue.add(patientListRequest);

			}
		});

		// on click temporarily disabled

		// holder.txtAllergy.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(final View v) {
		// if (holder.rlActions.getVisibility() == View.GONE) {
		// holder.rlActions.setVisibility(View.VISIBLE);
		// } else {
		// holder.rlActions.setVisibility(View.GONE);
		// }
		//
		// }
		// });

		return row;
	}
}

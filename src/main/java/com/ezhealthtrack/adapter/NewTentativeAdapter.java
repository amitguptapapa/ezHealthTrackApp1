package com.ezhealthtrack.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ezhealthrack.api.APIs;
import com.ezhealthrack.api.NetworkCalls.OnResponse;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.activity.SheduleActivity;
import com.ezhealthtrack.db.DatabaseHelper;
import com.ezhealthtrack.dialogs.EzDialog;
import com.ezhealthtrack.greendao.Appointment;
import com.ezhealthtrack.model.PatientShow;
import com.ezhealthtrack.model.State;
import com.ezhealthtrack.model.ToStep;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.Util;

public class NewTentativeAdapter extends ArrayAdapter<Appointment> {
	/* private view holder class */
	private static class ViewHolder {
		private TextView txtNewTentative;
		private Button btnView;
		private Button btnReSchedule;
		private Button btnSendMessage;
		private Button btnReject;
		private Button btnConfirm;
		private int mPosition;
		private View mButtonsBar;
	}

	private final Context context;
	private View mSelectedRowView;
	private int mSelectedRowIndex;

	private final SharedPreferences sharedPref;
	private static ArrayList<Appointment> objects = new ArrayList<Appointment>();
	private ArrayList<Appointment> confirmedList = new ArrayList<Appointment>();

	Filter filter = new Filter() {

		@Override
		protected FilterResults performFiltering(final CharSequence constraint) {
			final FilterResults filterResults = new FilterResults();
			final ArrayList<Appointment> tempList = new ArrayList<Appointment>();
			// constraint is the result from text you want to filter against.
			// objects is your data set you will filter from
			if ((constraint != null) && (confirmedList != null)) {
				final int length = confirmedList.size();
				int i = 0;
				while (i < length) {
					final Appointment item = confirmedList.get(i);
					PatientShow pShow = new PatientShow();
					for (PatientShow p : DashboardActivity.arrPatientShow) {
						if (p.getPId().equalsIgnoreCase(item.getPid())
								&& p.getPfId().equalsIgnoreCase(item.getPfId()))
							pShow = p;
					}

					if ((pShow.getPfn().toLowerCase() + " " + pShow.getPln()
							.toLowerCase()).contains(constraint.toString()
							.toLowerCase())) {
						tempList.add(item);
					}

					i++;
				}
				// following two lines is very important
				// as publish result can only take FilterResults objects
				filterResults.values = tempList;
				filterResults.count = tempList.size();
			}
			return filterResults;
		}

		@Override
		protected void publishResults(final CharSequence constraint,
				final FilterResults results) {
			try {
				NewTentativeAdapter.objects.clear();
				NewTentativeAdapter.objects
						.addAll((Collection<? extends Appointment>) results.values);
				notifyDataSetChanged();
				((TextView) ((Activity) context).findViewById(R.id.txt_count))
						.setText(Html.fromHtml("<b>" + "Total Results: "
								+ objects.size() + "</b>"));
			} catch (Exception e) {

			}

		}
	};

	public NewTentativeAdapter(final Context context, final int resourceId,
			final ArrayList<Appointment> item) {
		super(context, resourceId, objects);
		sharedPref = context.getSharedPreferences(Constants.EZ_SHARED_PREF,
				Context.MODE_PRIVATE);
		this.context = context;
		this.confirmedList = item;
		this.mSelectedRowIndex = -1;

		NewTentativeAdapter.objects.clear();
		NewTentativeAdapter.objects.addAll(item);
	}

	private void confirmAppointment(final Appointment rowItem) {
		final String url = APIs.APPOINTMENTCONFIRM().replace("booking_id",
				rowItem.getBkid());
		final Dialog loaddialog = Util.showLoadDialog(context);
		Log.i("", url);
		final StringRequest logoutRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i("", response);
						loaddialog.dismiss();
						try {
							final JSONObject jObj = new JSONObject(response);
							DashboardActivity.arrConfirmedPatients.add(rowItem);
							DashboardActivity.arrNewTentativePatients
									.remove(rowItem);
							((DashboardActivity) context).newTentativeFragment
									.onResume();
							((DashboardActivity) context).calls
									.getNewTentativeList(new OnResponse() {

										@Override
										public void onResponseListner(String api) {
											// TODO Auto-generated method stub

										}
									});
							Util.Alertdialog(context,
									"Appointment has been confirmed successfully.");
							notifyDataSetChanged();

						} catch (final JSONException e) {
							Util.Alertdialog(context,
									"There is some error while appointment confirmation, please try again");
							Log.e("", e);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(context,
								"There is some error while appointment confirmation, please try again");

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
				loginParams.put("tenant_id", EzApp.sharedPref
						.getString(Constants.TENANT_ID, ""));
				loginParams.put("branch_id", EzApp.sharedPref
						.getString(Constants.USER_BRANCH_ID, ""));
				loginParams.put("note", rowItem.getNote());
				return loginParams;
			}

		};
		logoutRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(logoutRequest);

	}

	@Override
	public Filter getFilter() {
		return filter;
	}

	@Override
	public View getView(final int position, View view, final ViewGroup parent) {

		final Appointment rowItem = getItem(position);

		if (view == null) {
			final ViewHolder holder = new ViewHolder();
			final LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.row_new_tentative, parent, false);
			holder.txtNewTentative = (TextView) view
					.findViewById(R.id.txt_new_tentative);
			holder.mButtonsBar = view.findViewById(R.id.id_actions);
			holder.btnView = (Button) view.findViewById(R.id.btn_view);
			holder.btnReSchedule = (Button) view
					.findViewById(R.id.btn_reschedule);
			holder.btnSendMessage = (Button) view
					.findViewById(R.id.btn_sendmessage);
			holder.btnReject = (Button) view.findViewById(R.id.btn_reject);
			holder.btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
			view.setTag(holder);
		}
		final ViewHolder holder = (ViewHolder) view.getTag();
		holder.mPosition = position;

		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View row) {
				if (mSelectedRowView != null) {
					((ViewHolder) mSelectedRowView.getTag()).mButtonsBar
							.setVisibility(View.GONE);
					mSelectedRowView.setBackgroundResource(R.color.white);
				}

				if (((ViewHolder) row.getTag()).mPosition == mSelectedRowIndex) {
					// row deselected
					mSelectedRowView = null;
					mSelectedRowIndex = -1;
				} else {
					// row selected
					mSelectedRowView = row;
					mSelectedRowIndex = position;
					((ViewHolder) mSelectedRowView.getTag()).mButtonsBar
							.setVisibility(View.VISIBLE);
					mSelectedRowView.setBackgroundResource(R.color.lightblue);
				}
			}
		});

		holder.mButtonsBar.setVisibility(View.GONE);
		view.setBackgroundResource(R.color.white);
		if (holder.mPosition == mSelectedRowIndex) {
			mSelectedRowView = view;
			holder.mButtonsBar.setVisibility(View.VISIBLE);
			mSelectedRowView.setBackgroundResource(R.color.lightblue);
		}

		TextView txtIndex = (TextView) view.findViewById(R.id.txt_index);
		txtIndex.setText("" + (position + 1) + ".");

		try {
			final State state = DatabaseHelper.db.getState(rowItem.getWistep());

			holder.btnReSchedule.setVisibility(View.GONE);
			holder.btnReject.setVisibility(View.GONE);
			holder.btnConfirm.setVisibility(View.GONE);
			for (final ToStep toStep : DatabaseHelper.db.getToStep(state
					.getId())) {
				Log.i("Step Name", toStep.getName());
				if (toStep.getName().equals("reschedule")) {
					holder.btnReSchedule.setVisibility(View.VISIBLE);
				}
				if (toStep.getName().equals("cancel")) {
					holder.btnReject.setVisibility(View.VISIBLE);
				}
				if (toStep.getName().equals("confirm")) {
					holder.btnConfirm.setVisibility(View.VISIBLE);
				}
			}
		} catch (Exception e) {

		}
		PatientShow pat1 = new PatientShow();
		for (PatientShow pats : DashboardActivity.arrPatientShow) {
			if (pats.getPfId().equals(rowItem.getPfId())
					&& pats.getPId().equals(rowItem.getPid()))
				pat1 = pats;
		}
		Date currentDate = Calendar.getInstance().getTime();
		currentDate.setHours(0);
		currentDate.setMinutes(0);
		currentDate.setSeconds(0);
		Date tomorrowDate = new Date(currentDate.getTime() + 24 * 60 * 60000
				- 60000);

		if (rowItem.aptDate.compareTo(currentDate) > 0
				&& rowItem.aptDate.compareTo(tomorrowDate) < 0) {
			Log.i(currentDate.toString(), tomorrowDate.toString());
			final SimpleDateFormat df1 = new SimpleDateFormat(
					"' @ 'hh:mm a' Today '");
			for (PatientShow p : DashboardActivity.arrPatientShow) {
				if (p.getPId().equalsIgnoreCase(rowItem.getPid())
						&& p.getPfId().equalsIgnoreCase(rowItem.getPfId()))

					holder.txtNewTentative.setText(Html.fromHtml(p.getPfn()
							+ " " + p.getPln() + ", " + pat1.getAge() + "/"
							+ pat1.getGender() + ", " + pat1.getPType()
							+ " Patient" + df1.format(rowItem.aptDate)
							+ " for " + "<b>" + "'" + rowItem.getReason() + "'"
							+ "</b>"));
			}
		} else {
			final SimpleDateFormat df = new SimpleDateFormat(
					"' @ 'hh:mm a' on 'EEE, MMM dd', 'yyyy");
			for (PatientShow p : DashboardActivity.arrPatientShow) {
				if (p.getPId().equalsIgnoreCase(rowItem.getPid())
						&& p.getPfId().equalsIgnoreCase(rowItem.getPfId()))

					holder.txtNewTentative
							.setText(Html.fromHtml(p.getPfn() + " "
									+ p.getPln() + ", " + pat1.getAge() + "/"
									+ pat1.getGender() + ", " + pat1.getPType()
									+ " Patient" + df.format(rowItem.aptDate)
									+ " for " + "<b>" + "'"
									+ rowItem.getReason() + "'" + "</b>"));
			}
		}

		holder.btnView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				showViewDialog(rowItem);
			}
		});

		holder.btnReSchedule.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				rowItem.setType("reschedule");
				DashboardActivity.arrScheduledPatients.add(rowItem);
				final Intent intent = new Intent(context, SheduleActivity.class);
				intent.putExtra(
						"pos",
						""
								+ DashboardActivity.arrScheduledPatients
										.lastIndexOf(rowItem));
				intent.putExtra("pid", rowItem.getPid());
				intent.putExtra("fid", "0");
				Log.i(rowItem.getPid(), rowItem.getPfId());
				context.startActivity(intent);

			}
		});

		holder.btnSendMessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				showSendMessageDialog(rowItem);
			}
		});

		holder.btnReject.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				showRejectDialog(rowItem);
			}
		});

		holder.btnConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				showConfirmDialog(rowItem);
			}
		});

		return view;
	}

	private void rejectAppointment(final Appointment rowItem) {
		final String url = APIs.APPOINTMENTREJECT().replace("booking_id",
				rowItem.getBkid());
		final Dialog loaddialog = Util.showLoadDialog(context);
		Log.i("", url);
		final StringRequest logoutRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i("", response);
						loaddialog.dismiss();
						try {
							final JSONObject jObj = new JSONObject(response);
							DashboardActivity.arrConfirmedPatients.add(rowItem);
							DashboardActivity.arrNewTentativePatients
									.remove(rowItem);
							((DashboardActivity) context).newTentativeFragment
									.onResume();
							Util.Alertdialog(context,
									"Appointment rejected successfully");
							notifyDataSetChanged();
						} catch (final JSONException e) {
							Util.Alertdialog(context,
									"There is some error, please try again.");
							Log.e("", e);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(context,
								"There is network error, please try again.");

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
				loginParams.put("tenant_id", EzApp.sharedPref
						.getString(Constants.TENANT_ID, ""));
				loginParams.put("branch_id", EzApp.sharedPref
						.getString(Constants.USER_BRANCH_ID, ""));
				loginParams.put("note", rowItem.getNote());
				return loginParams;
			}

		};
		logoutRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(logoutRequest);

	}

	private void showConfirmDialog(final Appointment rowItem) {

		final Dialog dialog = EzDialog.getDialog(context,
				R.layout.dialog_confirm, "Confirmation Details");
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		Date currentDate = Calendar.getInstance().getTime();
		currentDate.setHours(0);
		currentDate.setMinutes(0);
		currentDate.setSeconds(0);
		Date tomorrowDate = new Date(currentDate.getTime() + 24 * 60 * 60000
				- 60000);
		if (rowItem.aptDate.compareTo(currentDate) > 0
				&& rowItem.aptDate.compareTo(tomorrowDate) < 0) {
			final SimpleDateFormat df1 = new SimpleDateFormat(
					"' @ 'hh:mm a' Today '");
			for (PatientShow p : DashboardActivity.arrPatientShow) {
				if (p.getPId().equalsIgnoreCase(rowItem.getPid())
						&& p.getPfId().equalsIgnoreCase(rowItem.getPfId()))
					((TextView) dialog.findViewById(R.id.txt_desc)).setText(p
							.getPfn()
							+ " "
							+ p.getPln()
							+ ", "
							+ p.getAge()
							+ "/"
							+ p.getGender()
							+ ", "
							+ p.getPType()
							+ " Patient"
							+ ", "
							+ df1.format(rowItem.aptDate)
							+ " at "
							+ "' "
							+ EzApp.sharedPref.getString(
									Constants.DR_LOCALITY, "") + "'");
			}

		} else {
			final SimpleDateFormat df = new SimpleDateFormat(
					"' @ 'hh:mm a' on 'EEE, MMM dd', 'yyyy");
			for (PatientShow p : DashboardActivity.arrPatientShow) {
				if (p.getPId().equalsIgnoreCase(rowItem.getPid())
						&& p.getPfId().equalsIgnoreCase(rowItem.getPfId()))
					((TextView) dialog.findViewById(R.id.txt_desc)).setText(p
							.getPfn()
							+ " "
							+ p.getPln()
							+ ", "
							+ p.getAge()
							+ "/"
							+ p.getGender()
							+ ", "
							+ p.getPType()
							+ " Patient"
							+ df.format(rowItem.aptDate)
							+ " at "
							+ "' "
							+ EzApp.sharedPref.getString(
									Constants.DR_LOCALITY, "") + "'");
			}
		}

		dialog.findViewById(R.id.btn_submit).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(final View v) {
						rowItem.setNote(((EditText) dialog
								.findViewById(R.id.edit_note)).getText()
								.toString());
						confirmAppointment(rowItem);
						dialog.dismiss();
						try {
							InputMethodManager imm = (InputMethodManager) context
									.getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									dialog.findViewById(R.id.edit_note)
											.getWindowToken(), 0);
						} catch (Exception e) {

						}

					}
				});
		dialog.setCancelable(false);
		dialog.show();

	}

	private void showRejectDialog(final Appointment rowItem) {
		final Dialog dialog = EzDialog.getDialog(context,
				R.layout.dialog_reject, "Rejection Details");
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));

		Date currentDate = Calendar.getInstance().getTime();
		currentDate.setHours(0);
		currentDate.setMinutes(0);
		currentDate.setSeconds(0);
		Date tomorrowDate = new Date(currentDate.getTime() + 24 * 60 * 60000
				- 60000);
		if (rowItem.aptDate.compareTo(currentDate) > 0
				&& rowItem.aptDate.compareTo(tomorrowDate) < 0) {
			final SimpleDateFormat df1 = new SimpleDateFormat(
					"' @ 'hh:mm a' Today '");
			for (PatientShow p : DashboardActivity.arrPatientShow) {
				if (p.getPId().equalsIgnoreCase(rowItem.getPid())
						&& p.getPfId().equalsIgnoreCase(rowItem.getPfId()))
					((TextView) dialog.findViewById(R.id.txt_desc)).setText(p
							.getPfn()
							+ " "
							+ p.getPln()
							+ ", "
							+ p.getAge()
							+ "/"
							+ p.getGender()
							+ ", "
							+ p.getPType()
							+ " Patient"
							+ ", "
							+ df1.format(rowItem.aptDate)
							+ " at "
							+ "' "
							+ EzApp.sharedPref.getString(
									Constants.DR_LOCALITY, "") + "'");
			}

		} else {
			final SimpleDateFormat df = new SimpleDateFormat(
					"' @ 'hh:mm a' on 'EEE, MMM dd', 'yyyy");
			for (PatientShow p : DashboardActivity.arrPatientShow) {
				if (p.getPId().equalsIgnoreCase(rowItem.getPid())
						&& p.getPfId().equalsIgnoreCase(rowItem.getPfId()))
					((TextView) dialog.findViewById(R.id.txt_desc)).setText(p
							.getPfn()
							+ " "
							+ p.getPln()
							+ ", "
							+ p.getAge()
							+ "/"
							+ p.getGender()
							+ ", "
							+ p.getPType()
							+ " Patient"
							+ df.format(rowItem.aptDate)
							+ " at "
							+ "' "
							+ EzApp.sharedPref.getString(
									Constants.DR_LOCALITY, "") + "'");
			}
		}

		dialog.findViewById(R.id.btn_submit).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(final View v) {
						if (!Util.isEmptyString(((EditText) dialog
								.findViewById(R.id.edit_note)).getText()
								.toString())) {
							rowItem.setNote(((EditText) dialog
									.findViewById(R.id.edit_note)).getText()
									.toString());
							rejectAppointment(rowItem);
							try {
								InputMethodManager imm = (InputMethodManager) context
										.getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.hideSoftInputFromWindow(dialog
										.findViewById(R.id.edit_note)
										.getWindowToken(), 0);
							} catch (Exception e) {

							}
							dialog.dismiss();
						} else {
							Util.Alertdialog(context, "Please enter details");
						}

					}
				});
		dialog.setCancelable(false);
		dialog.show();

	}

	private void showSendMessageDialog(final Appointment rowItem) {
		final Dialog dialog = EzDialog.getDialog(context,
				R.layout.dialog_sendmessage, "Send Message");

		for (PatientShow p : DashboardActivity.arrPatientShow) {
			if (p.getPId().equalsIgnoreCase(rowItem.getPid())
					&& p.getPfId().equalsIgnoreCase(rowItem.getPfId()))

				((TextView) dialog.findViewById(R.id.txt_patient_name_display))
						.setText(p.getPfn() + " " + p.getPln());
		}

		final Button button = (Button) dialog.findViewById(R.id.btn_submit);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				if (Util.isEmptyString(((EditText) dialog
						.findViewById(R.id.edit_subject)).getText().toString())) {
					Util.Alertdialog(context, "Please enter Subject");
				} else if (Util.isEmptyString(((EditText) dialog
						.findViewById(R.id.edit_body)).getText().toString())) {
					Util.Alertdialog(context, "Please enter Body");
				} else {
					final String url = APIs.SENDMESSAGE();
					Log.i("", url);
					final Dialog loaddialog = Util.showLoadDialog(context);
					final StringRequest logoutRequest = new StringRequest(
							Request.Method.POST, url,
							new Response.Listener<String>() {
								@Override
								public void onResponse(final String response) {
									Log.i("", response);
									dialog.dismiss();
									loaddialog.dismiss();
								}

							}, new Response.ErrorListener() {
								@Override
								public void onErrorResponse(
										final VolleyError error) {
									Util.Alertdialog(context,
											"There is some error while sending message");

									Log.e("Error.Response", error);
									loaddialog.dismiss();
								}
							}) {
						@Override
						public Map<String, String> getHeaders()
								throws AuthFailureError {
							final HashMap<String, String> loginParams = new HashMap<String, String>();
							loginParams.put("auth-token", Util
									.getBase64String(sharedPref.getString(
											Constants.USER_TOKEN, "")));
							return loginParams;
						}

						@Override
						protected Map<String, String> getParams() {
							final HashMap<String, String> loginParams = new HashMap<String, String>();
							loginParams.put("format", "json");
							loginParams.put("subject", ((EditText) dialog
									.findViewById(R.id.edit_subject)).getText()
									.toString());
							loginParams.put("body_msg", ((EditText) dialog
									.findViewById(R.id.edit_body)).getText()
									.toString());
							loginParams.put("context", rowItem.getBkid());
							loginParams.put("context_type", "booking");
							loginParams.put("to_id", rowItem.getPid());
							loginParams.put("to_fam_id", rowItem.getPfId());
							loginParams.put("to_type", "P");
							return loginParams;
						}

					};
					logoutRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
							5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
					EzApp.mVolleyQueue.add(logoutRequest);
					// dialog.dismiss();

					try {
						InputMethodManager imm = (InputMethodManager) context
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(
								dialog.findViewById(R.id.edit_body)
										.getWindowToken(), 0);
					} catch (Exception e) {

					}
					// TODO Auto-generated method stub
					dialog.dismiss();
					Util.Alertdialog(context, "Message send successfully");
				}
			}

		});
		dialog.setCancelable(false);
		dialog.show();

	}

	private void showViewDialog(final Appointment rowItem) {
		final Dialog dialog = EzDialog.getDialog(context, R.layout.dialog_view,
				"Appointment Schedule Details");
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		TextView txtTentative = (TextView) dialog.findViewById(R.id.txt_desc);
		Date currentDate = Calendar.getInstance().getTime();
		currentDate.setHours(0);
		currentDate.setMinutes(0);
		currentDate.setSeconds(0);
		Date tomorrowDate = new Date(currentDate.getTime() + 24 * 60 * 60000
				- 60000);
		if (rowItem.aptDate.compareTo(currentDate) > 0
				&& rowItem.aptDate.compareTo(tomorrowDate) < 0) {
			final SimpleDateFormat df1 = new SimpleDateFormat(
					"' @ 'hh:mm a' Today '");
			for (PatientShow p : DashboardActivity.arrPatientShow) {
				if (p.getPId().equalsIgnoreCase(rowItem.getPid())
						&& p.getPfId().equalsIgnoreCase(rowItem.getPfId()))
					txtTentative.setText(Html.fromHtml(p.getPfn() + " "
							+ p.getPln() + ", " + p.getAge() + "/"
							+ p.getGender() + ", " + p.getPType() + " Patient"
							+ df1.format(rowItem.aptDate) + " for " + "<b>"
							+ "'" + rowItem.getReason() + "'" + "</b>"));
			}

		} else {
			final SimpleDateFormat df = new SimpleDateFormat(
					"' @ 'hh:mm a' on 'EEE, MMM dd', 'yyyy");
			for (PatientShow p : DashboardActivity.arrPatientShow) {
				if (p.getPId().equalsIgnoreCase(rowItem.getPid())
						&& p.getPfId().equalsIgnoreCase(rowItem.getPfId()))
					txtTentative.setText(Html.fromHtml(p.getPfn() + " "
							+ p.getPln() + ", " + p.getAge() + "/"
							+ p.getGender() + ", " + p.getPType() + " Patient"
							+ df.format(rowItem.aptDate) + " for " + "<b>"
							+ "'" + rowItem.getReason() + "'" + "</b>"));
			}
		}

		((TextView) dialog.findViewById(R.id.txt_reason_1)).setText(" "
				+ rowItem.getReason());

		dialog.show();
	}
}
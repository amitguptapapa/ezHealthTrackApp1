package com.ezhealthtrack.labs.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ezhealthrack.api.LabApis;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.SheduleActivityNew;
import com.ezhealthtrack.greendao.Appointment;
import com.ezhealthtrack.greendao.LabAppointment;
import com.ezhealthtrack.greendao.LabAppointmentDao.Properties;
import com.ezhealthtrack.greendao.LabTechnician;
import com.ezhealthtrack.greendao.Patient;
import com.ezhealthtrack.greendao.PatientShow;
import com.ezhealthtrack.labs.adapter.LabsTechnicianAdapter;
import com.ezhealthtrack.labs.controller.LabsAppointmentController.OnResponseApt;
import com.ezhealthtrack.labs.fragments.NewOrdersFragment;
import com.ezhealthtrack.model.AccountModel;
import com.ezhealthtrack.model.InRefferalModel;
import com.ezhealthtrack.model.PatientAutoSuggest;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.NetworkCallController.OnResponse;
import com.ezhealthtrack.util.Util;
import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;

import de.greenrobot.dao.query.LazyList;

public class LabsAppointmentController {
	public final static int LOCATION_CLINIC = 1;
	public final static int LOCATION_PATIENT = 2;
	public static AccountModel profile = new AccountModel();

	public interface OnResponseApt {
		public void onResponseListner(String response, String count);
	}

	public static void getAppointmentListApi(int type, final String page_num,
			String cond, Context context, final OnResponseApt responsee,
			PatientAutoSuggest pat, Date fdate, Date tdate, int consult,
			String assigned) {
		final String url = LabApis.CONFIRMED_LIST;
		Log.i("", url);
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("format", "json");
		params.put("page_num", "" + page_num);
		params.put("limit_count", "50");
		if (type == Constants.TYPE_CONFIRMED)
			params.put("status", "confirmed");
		else if (type == Constants.TYPE_HISTORY)
			params.put("status", "history");
		else if (type == Constants.TYPE_NEW_TENTATIVE)
			params.put("status", "new");
		if (!Util.isEmptyString(cond))
			params.put("condval", cond);
		if (pat != null)
			params.put("sel_pat", pat.getId());
		if (fdate != null)
			params.put("fdate", EzApp.sdfddMmyy1.format(fdate));
		if (tdate != null)
			params.put("tdate", EzApp.sdfddMmyy1.format(tdate));
		if (consult > 0) {
			params.put("consultation", "" + consult);
		}
		if (!Util.isEmptyString(assigned))
			params.put("assigned_type", "" + assigned);
		Log.i("", params.toString());
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						Log.i("", response);
						if (response.contains("error")) {
							responsee.onResponseListner("error", "error");
						} else {
							try {
								Log.i("page_num", "" + page_num);
								final JSONObject jObj = new JSONObject(response);
								if (jObj.getString("s").equals("200")) {
									final JSONArray patientList = jObj
											.getJSONArray("data");
									final JSONArray patientList1 = jObj
											.getJSONArray("patients");
									for (int i = 0; i < patientList1.length(); i++) {
										com.ezhealthtrack.greendao.PatientShow pat = new Gson()
												.fromJson(
														patientList1.get(i)
																.toString(),
														com.ezhealthtrack.greendao.PatientShow.class);
										pat.setId((long) (Integer.parseInt(pat
												.getP_id()) + 100000 * Integer
												.parseInt(pat.getPf_id())));
										EzApp.patientShowDao
												.insertOrReplace(pat);

										Log.i("", new Gson().toJson(pat));
									}
									for (int i = 0; i < patientList.length(); i++) {
										final JSONObject patientListData = patientList
												.getJSONObject(i);
										LabAppointment appointment = new LabAppointment();
										appointment.setId(patientListData
												.getLong("bk-id"));
										appointment.setBkid(patientListData
												.getString("bk-id"));
										if (patientListData.has("ep-id"))
											appointment.setEpid(patientListData
													.getString("ep-id"));
										else
											appointment.setEpid("0");
										appointment.setPid(patientListData
												.getString("p-id"));
										appointment.setPfid(patientListData
												.getString("pf-id"));
										appointment.setReason(patientListData
												.getString("reason"));
										appointment.setFlag(patientListData
												.getString("flag"));
										appointment.setApflag(patientListData
												.getString("ap-flag"));
										appointment.setVisit(patientListData
												.getString("visit"));
										appointment.setSlotid(patientListData
												.getString("slot-id"));
										appointment
												.setVisit_location(patientListData
														.getString("visit_location"));
										appointment
												.setAssigned_user_id(patientListData
														.getString("assigned_user_id"));
										final SimpleDateFormat sdf = new SimpleDateFormat(
												"MM/dd/yyyy");
										try {
											final Date date = sdf.parse(patientListData
													.getString("apt-date"));
											final Calendar cal = Calendar
													.getInstance();
											cal.setTime(date);
											cal.set(Calendar.HOUR_OF_DAY, 0);
											cal.set(Calendar.MINUTE, 0);
											cal.add(Calendar.MINUTE,
													(Integer.parseInt(patientListData
															.getString("slot-id")) - 1) * 15);
											// Log.i("",
											// cal.getTime().toString());

											appointment.setAptdate(cal
													.getTime());
										} catch (final Exception e) {
											Log.e("", e);
											appointment.setAptdate(Calendar
													.getInstance().getTime());
										}
										if (patientListData.has("follow-id")) {
											appointment
													.setFollowid(patientListData
															.getString("follow-id"));
										}
										EzApp.labAptDao
												.insertOrReplace(appointment);

									}
									Log.i("", jObj.getString("count"));
									responsee.onResponseListner(
											jObj.getString("cond"),
											jObj.getString("count"));
								}
							} catch (final JSONException e) {
								Log.e("", e);
							}

						}
					}
				});
	}

	public static LazyList<LabAppointment> getAppointmentList(int type) {
		int offsetCurrentTimeZone = TimeZone.getDefault().getRawOffset();
		int offsetIST = 11 * 30 * 60 * 1000;
		Date date = new Date();
		date.setTime(date.getTime() + offsetIST - offsetCurrentTimeZone);
		LazyList<LabAppointment> list;
		if (type == Constants.TYPE_CONFIRMED)
			list = EzApp.labAptDao.queryBuilder()
					.where(Properties.Aptdate.ge(date))
					.orderAsc(Properties.Aptdate).listLazyUncached();
		else
			list = EzApp.labAptDao.queryBuilder()
					.where(Properties.Aptdate.le(date))
					.orderAsc(Properties.Aptdate).listLazyUncached();

		for (LabAppointment apt : list) {
			apt = getPatientShow(apt);
		}
		return list;
	}

	public static LazyList<LabAppointment> getAppointmentList(int type,
			String name, Date startDate, Date endDate, String cType,
			String aType) {
		int offsetCurrentTimeZone = TimeZone.getDefault().getRawOffset();
		int offsetIST = 11 * 30 * 60 * 1000;
		Date date = new Date();
		date.setTime(date.getTime() + offsetIST - offsetCurrentTimeZone);
		LazyList<LabAppointment> list;
		if (type == Constants.TYPE_CONFIRMED)
			list = EzApp.labAptDao.queryBuilder()
					.where(Properties.Aptdate.ge(date))
					.orderAsc(Properties.Aptdate).listLazyUncached();
		else
			list = EzApp.labAptDao.queryBuilder()
					.where(Properties.Aptdate.le(date))
					.orderAsc(Properties.Aptdate).listLazyUncached();
		for (LabAppointment apt : list) {
			apt = getPatientShow(apt);
		}
		return list;
	}

	public static long getAppointmentListCount(int type) {
		int offsetCurrentTimeZone = TimeZone.getDefault().getRawOffset();
		int offsetIST = 11 * 30 * 60 * 1000;
		Date date = new Date();
		date.setTime(date.getTime() + offsetIST - offsetCurrentTimeZone);
		LazyList<LabAppointment> list;
		if (type == Constants.TYPE_CONFIRMED)
			list = EzApp.labAptDao.queryBuilder()
					.where(Properties.Aptdate.ge(date))
					.orderAsc(Properties.Aptdate).listLazyUncached();
		else
			list = EzApp.labAptDao.queryBuilder()
					.where(Properties.Aptdate.le(date))
					.orderAsc(Properties.Aptdate).listLazyUncached();
		return list.size();
	}

	public static LabAppointment getPatientShow(LabAppointment apt) {
		apt.patient = EzApp.patientShowDao
				.queryBuilder()
				.where(com.ezhealthtrack.greendao.PatientShowDao.Properties.P_id
						.eq(apt.getPid()),
						com.ezhealthtrack.greendao.PatientShowDao.Properties.Pf_id
								.eq(apt.getPfid())).list().get(0);
		return apt;
	}

	public static LabAppointment getLabAppointment(String bkid) {
		LabAppointment apt = (LabAppointment) EzApp.labAptDao
				.queryBuilder()
				.where(com.ezhealthtrack.greendao.LabAppointmentDao.Properties.Bkid
						.eq(bkid)).list().get(0);
		apt = getPatientShow(apt);
		return apt;
	}

	public static void showRejectDialog(final LabAppointment rowItem,
			final Context context) {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_reject);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setCancelable(false);
		dialog.findViewById(R.id.txt_close).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							InputMethodManager imm = (InputMethodManager) context
									.getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									dialog.findViewById(R.id.edit_note)
											.getWindowToken(), 0);
						} catch (Exception e) {

						}
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
		Date currentDate = Calendar.getInstance().getTime();
		currentDate.setHours(0);
		currentDate.setMinutes(0);
		currentDate.setSeconds(0);
		Date tomorrowDate = new Date(currentDate.getTime() + 24 * 60 * 60000
				- 60000);
		if (rowItem.getAptdate().compareTo(currentDate) > 0
				&& rowItem.getAptdate().compareTo(tomorrowDate) < 0) {
			final SimpleDateFormat df1 = new SimpleDateFormat(
					"' @ 'hh:mm a' Today '");
			PatientShow p = new PatientShow();
			try {
				if (EzApp.patientShowDao
						.queryBuilder()
						.where(com.ezhealthtrack.greendao.PatientShowDao.Properties.P_id
								.eq(rowItem.getPid()),
								com.ezhealthtrack.greendao.PatientShowDao.Properties.Pf_id
										.eq(rowItem.getPfid())).count() > 0) {
					p = (EzApp.patientShowDao
							.queryBuilder()
							.where(com.ezhealthtrack.greendao.PatientShowDao.Properties.P_id
									.eq(rowItem.getPid()),
									com.ezhealthtrack.greendao.PatientShowDao.Properties.Pf_id
											.eq(rowItem.getPfid())).list()
							.get(0));
				}

			} catch (Exception e) {
				Log.e("", e);
			}
			((TextView) dialog.findViewById(R.id.txt_desc)).setText(p.getPfn()
					+ " " + p.getPln() + ", " + p.getAge() + "/"
					+ p.getGender() + ", " + p.getP_type() + " Patient"
					+ df1.format(rowItem.getAptdate()) + " for " + "'"
					+ rowItem.getReason() + "'");

		} else {
			final SimpleDateFormat df = new SimpleDateFormat(
					"' @ 'hh:mm a' on 'EEE, MMM dd', 'yyyy");
			PatientShow p = new PatientShow();
			try {
				if (EzApp.patientShowDao
						.queryBuilder()
						.where(com.ezhealthtrack.greendao.PatientShowDao.Properties.P_id
								.eq(rowItem.getPid()),
								com.ezhealthtrack.greendao.PatientShowDao.Properties.Pf_id
										.eq(rowItem.getPfid())).count() > 0) {
					p = (EzApp.patientShowDao
							.queryBuilder()
							.where(com.ezhealthtrack.greendao.PatientShowDao.Properties.P_id
									.eq(rowItem.getPid()),
									com.ezhealthtrack.greendao.PatientShowDao.Properties.Pf_id
											.eq(rowItem.getPfid())).list()
							.get(0));
				}

			} catch (Exception e) {
				Log.e("", e);
			}
			((TextView) dialog.findViewById(R.id.txt_desc)).setText(p.getPfn()
					+ " " + p.getPln() + ", " + p.getAge() + "/"
					+ p.getGender() + ", " + p.getP_type() + " Patient"
					+ df.format(rowItem.getAptdate()) + " for " + "'"
					+ rowItem.getReason() + "'");

		}

		dialog.findViewById(R.id.btn_submit).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(final View v) {
						FlurryAgent
								.logEvent("LabsAppointmentController - Submit Reject Button Clicked");
						if (!Util.isEmptyString(((EditText) dialog
								.findViewById(R.id.edit_note)).getText()
								.toString())) {
							rejectAppointment(((EditText) dialog
									.findViewById(R.id.edit_note)).getText()
									.toString(), rowItem, context, dialog);
							try {
								InputMethodManager imm = (InputMethodManager) context
										.getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.hideSoftInputFromWindow(dialog
										.findViewById(R.id.edit_note)
										.getWindowToken(), 0);
							} catch (Exception e) {

							}
							dialog.dismiss();
							// loaddialog.dismiss();

						} else {
							Util.Alertdialog(context, "Please enter note");
						}

					}
				});

		dialog.show();

	}

	public static void rejectAppointment(String note, final LabAppointment apt,
			final Context context, final Dialog dialog) {

		String url = LabApis.REJECTAPPOINTMENT.replace("{booking_Id}",
				apt.getBkid());
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("format", "json");
		params.put("note", note);

		final Dialog loaddialog = Util.showLoadDialog(context);
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						Log.i("", response);
						if (!response.equals("error")) {
							JSONObject jObj;
							try {
								jObj = new JSONObject(response);
								if (jObj.getString("s").contains("200")) {
									Util.Alertdialog(context,
											"Appointment Rejected Sucessfully");
									deleteAppointment(apt);
									dialog.dismiss();
								} else {
									Util.Alertdialog(context,
											"There is some error in rejecting appointment, Please try again.");
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}

						}
						loaddialog.dismiss();

					}
				});
	}

	public static void scheduleAppointment(final Patient pat, final Date date,
			final String slot, final String reason, final int vl,
			String assigned_user_id, InRefferalModel inref,
			final Context context, final Dialog dialog,
			final OnResponse responsee) {

		String url = LabApis.REGISTERAPPOINTMENT;
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("format", "json");
		params.put("pid", pat.getPid());
		params.put("fid", pat.getFid());
		params.put("date", EzApp.sdfyyMmdd.format(date));
		params.put("p-date", EzApp.sdfyyMmdd.format(date));
		params.put("sid", slot);
		params.put("apt-reason", reason);
		params.put("visit_location", "" + vl);
		params.put("assigned_user_type", "LT");
		params.put("ref-id", inref.getRefId());
		params.put("ref-ep", inref.getEpId());
		if (vl == LOCATION_PATIENT) {
			params.put("address", pat.getPadd1());
			params.put("address2", pat.getPadd2());
			params.put("country", pat.getPcountryid());
			params.put("cmbState", pat.getPstateid());
			params.put("cmbCity", pat.getPcityid());
			params.put("cmbArea", pat.getPareaid());
			params.put("cmbArea_text", pat.getParea());
			params.put("zip", pat.getPzip());
			params.put("assigned_user_id", assigned_user_id);
		}

		final Dialog loaddialog = Util.showLoadDialog(context);
		final SimpleDateFormat df = new SimpleDateFormat(
				"EEE, MMM dd', 'yyyy hh:mm aa ' To '");
		final SimpleDateFormat df1 = new SimpleDateFormat("hh:mm aa");
		final String date1 = df1.format(new Date(
				date.getTime() + 15 * 60 * 1000));
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {
					@Override
					public void onResponseListner(String response) {
						Log.i("", response);
						try {
							JSONObject jOBj = new JSONObject(response);
							if (jOBj.getString("s").equals("200")) {
								if (vl == LOCATION_CLINIC) {
									responsee.onResponseListner(jOBj
											.getString("bk_id"));
									Util.AlertdialogWithFinish(
											context,
											"Appointment with "
													+ pat.getPfn()
													+ " "
													+ pat.getPln()
													+ " for "
													+ df.format(date)
													+ date1
													+ " at "
													+ EzApp.sharedPref
															.getString(
																	Constants.LAB_ADDRESS,
																	"")
													+ " created successfully");
								} else {
									responsee.onResponseListner(jOBj
											.getString("bk_id"));
									Util.AlertdialogWithFinish(
											context,
											"Appointment with " + pat.getPfn()
													+ " " + pat.getPln()
													+ " for " + df.format(date)
													+ date1 + " at "
													+ pat.getPadd1() + " "
													+ pat.getPadd2() + ", "
													+ pat.getParea() + ", "
													+ pat.getPcity() + ", "
													+ pat.getPstate() + ", "
													+ pat.getPcountry()
													+ " created successfully");
								}
							} else
								responsee.onResponseListner("error");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							responsee.onResponseListner("error");
						}
						dialog.dismiss();
						loaddialog.dismiss();

					}
				});
	}

	public static void reScheduleAppointment(final Patient pat,
			final Date date, final String slot, final String reason,
			final int vl, String assigned_user_id, InRefferalModel inref,
			final Context context, final Dialog dialog,
			final LabAppointment apt, final OnResponse res) {

		String url = LabApis.RESCHEDULEAPPOINTMENT.replace("{booking_Id}",
				apt.getBkid());
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("format", "json");
		params.put("pid", pat.getPid());
		params.put("fid", pat.getFid());
		params.put("date", EzApp.sdfyyMmdd.format(date));
		params.put("p-date",
				EzApp.sdfyyMmdd.format(apt.getAptdate()));
		params.put("sid", slot);
		params.put("apt-reason", reason);
		params.put("visit_location", "" + vl);
		params.put("assigned_user_type", "LT");
		params.put("ref-id", inref.getRefId());
		params.put("ref-ep", inref.getEpId());
		if (vl == LOCATION_PATIENT) {
			params.put("address", pat.getPadd1());
			params.put("address2", pat.getPadd2());
			params.put("country", pat.getPcountryid());
			params.put("cmbState", pat.getPstateid());
			params.put("cmbCity", pat.getPcityid());
			params.put("cmbArea", pat.getPareaid());
			params.put("cmbArea_text", pat.getParea());
			params.put("zip", pat.getPzip());
			params.put("assigned_user_id", assigned_user_id);
		}

		final Dialog loaddialog = Util.showLoadDialog(context);
		final SimpleDateFormat df = new SimpleDateFormat(
				"EEE, MMM dd', 'yyyy hh:mm aa ' To '");
		final SimpleDateFormat df1 = new SimpleDateFormat("hh:mm aa");
		final String date1 = df1.format(new Date(
				date.getTime() + 15 * 60 * 1000));
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						Log.i("", response);
						try {
							JSONObject jOBj = new JSONObject(response);
							if (jOBj.getString("s").equals("200")) {
								if (vl == LOCATION_CLINIC) {
									res.onResponseListner(jOBj
											.getString("bk_id"));
									Util.AlertdialogWithFinish(
											context,
											"Appointment with "
													+ pat.getPfn()
													+ " "
													+ pat.getPln()
													+ " for "
													+ df.format(date)
													+ date1
													+ " at "
													+ EzApp.sharedPref
															.getString(
																	Constants.LAB_ADDRESS,
																	"")
													+ " created successfully");

								} else {
									res.onResponseListner(jOBj
											.getString("bk_id"));
									Util.AlertdialogWithFinish(
											context,
											"Appointment with " + pat.getPfn()
													+ " " + pat.getPln()
													+ " for " + df.format(date)
													+ date1 + " at "
													+ pat.getPadd1() + " "
													+ pat.getPadd2() + ", "
													+ pat.getParea() + ", "
													+ pat.getPcity() + ", "
													+ pat.getPstate() + ", "
													+ pat.getPcountry()
													+ " created successfully");
								}
							} else
								res.onResponseListner("error");

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							res.onResponseListner("error");
						}
						dialog.dismiss();
						loaddialog.dismiss();

					}
				});
	}

	public static void deleteAppointment(LabAppointment apt) {
		EzApp.labAptDao.delete(apt);
	}

	public static void editAppointment(LabAppointment apt) {
		EzApp.labAptDao.insertOrReplace(apt);
	}

	public static void showReScheduleDialog(final Date date, final String slot,
			final LabAppointment apt, final Context context,
			final Patient patient, final OnResponse responsee) {
		AutoCompleteTextView actvCountry;
		AutoCompleteTextView actvState;
		AutoCompleteTextView actvCity;
		AutoCompleteTextView actvLocality;
		final Spinner spinnerVisitLocation;
		final ArrayList<String> arrVisitLocation = new ArrayList<String>();

		final Dialog dialoglab = new Dialog(context);
		dialoglab.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialoglab.setContentView(R.layout.labs_dialog_reschedule_appointment);
		dialoglab.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		final Appointment pm = new Appointment();
		Date currentDate = Calendar.getInstance().getTime();
		currentDate.setHours(0);
		currentDate.setMinutes(0);
		currentDate.setSeconds(0);
		Date tomorrowDate = new Date(currentDate.getTime() + 24 * 60 * 60000
				- 60000);
		final SimpleDateFormat sd;
		if (date.after(currentDate) && date.before(tomorrowDate)) {
			sd = new SimpleDateFormat("' @ 'hh:mm a' Today '");
		} else {
			sd = new SimpleDateFormat("' @ 'hh:mm a' on 'EEE, MMM dd', 'yyyy");
		}
		if (Util.isEmptyString(patient.getPgender())) {
			patient.setPgender(" ");
		}
		((TextView) dialoglab.findViewById(R.id.txt_shedule_desc))
				.setText(patient.getP_detail()
						+ sd.format(date)
						+ " at '"
						+ EzApp.sharedPref.getString(
								Constants.LAB_LOCALITY, "") + "'.");
		((TextView) dialoglab.findViewById(R.id.txt_patient_address))
				.setText(patient.getPadd1() + " " + patient.getPadd2() + ", "
						+ patient.getParea() + ", " + patient.getPcity() + ", "
						+ patient.getPstate() + ", " + patient.getPcountry()
						+ " - " + patient.getPzip());
		((TextView) dialoglab.findViewById(R.id.txt_lab_address))
				.setText(EzApp.sharedPref.getString(
						Constants.LAB_ADDRESS, ""));

		((TextView) dialoglab.findViewById(R.id.txt_address_1)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

		((TextView) dialoglab.findViewById(R.id.txt_country)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

		((TextView) dialoglab.findViewById(R.id.txt_state)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

		((TextView) dialoglab.findViewById(R.id.txt_locality)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

		((TextView) dialoglab.findViewById(R.id.txt_city)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

		((TextView) dialoglab.findViewById(R.id.txt_pincode)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

		((TextView) dialoglab.findViewById(R.id.txt_reason)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

		((TextView) dialoglab.findViewById(R.id.txt_technician)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

		((EditText) dialoglab.findViewById(R.id.actv_address_1))
				.setText(patient.getPadd1());
		((EditText) dialoglab.findViewById(R.id.actv_address_2))
				.setText(patient.getPadd2());
		((AutoCompleteTextView) dialoglab.findViewById(R.id.actv_country))
				.setText(patient.getPcountry());
		((AutoCompleteTextView) dialoglab.findViewById(R.id.actv_state))
				.setText(patient.getPstate());
		((AutoCompleteTextView) dialoglab.findViewById(R.id.actv_city))
				.setText(patient.getPcity());
		((AutoCompleteTextView) dialoglab.findViewById(R.id.actv_locality))
				.setText(patient.getParea());
		((EditText) dialoglab.findViewById(R.id.actv_pincode)).setText(patient
				.getPzip());
		((TextView) dialoglab.findViewById(R.id.edit_reason)).setText(apt
				.getReason());
		spinnerVisitLocation = (Spinner) dialoglab
				.findViewById(R.id.spinner_visit_location);
		arrVisitLocation.clear();
		arrVisitLocation.add("Clinic");
		arrVisitLocation.add("Patient Location");

		final ArrayAdapter<String> adapterVisitLocation = new ArrayAdapter<String>(
				context, android.R.layout.simple_spinner_item, arrVisitLocation);
		adapterVisitLocation
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerVisitLocation.setAdapter(adapterVisitLocation);
		spinnerVisitLocation
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int pos, long arg3) {
						// TODO Auto-generated method stub
						if (pos == 0) {
							dialoglab.findViewById(R.id.rl_p_details)
									.setVisibility(View.GONE);
							dialoglab.findViewById(R.id.txt_lab_add)
									.setVisibility(View.VISIBLE);
							dialoglab.findViewById(R.id.txt_lab_address)
									.setVisibility(View.VISIBLE);

						} else {
							dialoglab.findViewById(R.id.rl_p_details)
									.setVisibility(View.VISIBLE);
							dialoglab.findViewById(R.id.txt_lab_add)
									.setVisibility(View.GONE);
							dialoglab.findViewById(R.id.txt_lab_address)
									.setVisibility(View.GONE);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});
		final Spinner spinnerTechnician = (Spinner) dialoglab
				.findViewById(R.id.spinner_technician);
		LabsTechnicianAdapter technicianAdapter = new LabsTechnicianAdapter(
				context, android.R.layout.simple_spinner_item,
				EzApp.labTechnicianDao.loadAll());
		technicianAdapter.setDropDownViewResource(R.layout.labs_row_technician);
		spinnerTechnician.setAdapter(technicianAdapter);

		final Button button = (Button) dialoglab
				.findViewById(R.id.btn_reschedule);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				FlurryAgent
						.logEvent("LabsAppointmentController - Submit ReSchedule Button Clicked");
				reScheduleAppointment(
						patient,
						date,
						slot,
						((TextView) dialoglab.findViewById(R.id.edit_reason))
								.getText().toString(),
						spinnerVisitLocation.getSelectedItemPosition() + 1,
						""
								+ ((LabTechnician) (spinnerTechnician
										.getSelectedItem())).getId(),
						new InRefferalModel(), context, dialoglab, apt,
						responsee);
				dialoglab.dismiss();
			}

		});
		dialoglab.setCancelable(false);
		dialoglab.findViewById(R.id.txt_close).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialoglab.dismiss();
					}
				});
		dialoglab.show();

	}

	public static void showScheduleDialog(final Date date, final String slot,
			final Context context, final Patient patient,
			final OnResponse responsee) {
		AutoCompleteTextView actvCountry;
		AutoCompleteTextView actvState;
		AutoCompleteTextView actvCity;
		AutoCompleteTextView actvLocality;
		final Spinner spinnerVisitLocation;
		final ArrayList<String> arrVisitLocation = new ArrayList<String>();

		final Dialog dialoglab = new Dialog(context);
		dialoglab.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialoglab.setContentView(R.layout.labs_dialog_schedule_appointment);
		dialoglab.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		final Appointment pm = new Appointment();
		Date currentDate = Calendar.getInstance().getTime();
		currentDate.setHours(0);
		currentDate.setMinutes(0);
		currentDate.setSeconds(0);
		Date tomorrowDate = new Date(currentDate.getTime() + 24 * 60 * 60000
				- 60000);
		final SimpleDateFormat sd;
		if (date.after(currentDate) && date.before(tomorrowDate)) {
			sd = new SimpleDateFormat("' @ 'hh:mm a' Today '");
		} else {
			sd = new SimpleDateFormat("' @ 'hh:mm a' on 'EEE, MMM dd', 'yyyy");
		}
		if (Util.isEmptyString(patient.getPgender())) {
			patient.setPgender(" ");
		}
		((TextView) dialoglab.findViewById(R.id.txt_shedule_desc))
				.setText(patient.getP_detail()
						+ sd.format(date)
						+ " at '"
						+ EzApp.sharedPref.getString(
								Constants.LAB_LOCALITY, "") + "'.");
		((TextView) dialoglab.findViewById(R.id.txt_patient_address))
				.setText(patient.getPadd1() + " " + patient.getPadd2() + ", "
						+ patient.getParea() + ", " + patient.getPcity() + ", "
						+ patient.getPstate() + ", " + patient.getPcountry()
						+ " - " + patient.getPzip());
		((TextView) dialoglab.findViewById(R.id.txt_lab_address))
				.setText(EzApp.sharedPref.getString(
						Constants.LAB_ADDRESS, ""));

		((TextView) dialoglab.findViewById(R.id.txt_address_1)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

		((TextView) dialoglab.findViewById(R.id.txt_country)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

		((TextView) dialoglab.findViewById(R.id.txt_state)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

		((TextView) dialoglab.findViewById(R.id.txt_locality)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

		((TextView) dialoglab.findViewById(R.id.txt_city)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

		((TextView) dialoglab.findViewById(R.id.txt_pincode)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

		((TextView) dialoglab.findViewById(R.id.txt_reason)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

		((TextView) dialoglab.findViewById(R.id.txt_technician)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

		((EditText) dialoglab.findViewById(R.id.actv_address_1))
				.setText(patient.getPadd1());
		((EditText) dialoglab.findViewById(R.id.actv_address_2))
				.setText(patient.getPadd2());
		((AutoCompleteTextView) dialoglab.findViewById(R.id.actv_country))
				.setText(patient.getPcountry());
		((AutoCompleteTextView) dialoglab.findViewById(R.id.actv_state))
				.setText(patient.getPstate());
		((AutoCompleteTextView) dialoglab.findViewById(R.id.actv_city))
				.setText(patient.getPcity());
		((AutoCompleteTextView) dialoglab.findViewById(R.id.actv_locality))
				.setText(patient.getParea());
		((EditText) dialoglab.findViewById(R.id.actv_pincode)).setText(patient
				.getPzip());
		spinnerVisitLocation = (Spinner) dialoglab
				.findViewById(R.id.spinner_visit_location);

		arrVisitLocation.clear();
		arrVisitLocation.add("Clinic");
		arrVisitLocation.add("Patient Location");

		final ArrayAdapter<String> adapterVisitLocation = new ArrayAdapter<String>(
				context, android.R.layout.simple_spinner_item, arrVisitLocation);
		adapterVisitLocation
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerVisitLocation.setAdapter(adapterVisitLocation);
		spinnerVisitLocation
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int pos, long arg3) {
						// TODO Auto-generated method stub
						if (pos == 0) {

							dialoglab.findViewById(R.id.rl_p_details)
									.setVisibility(View.GONE);
							dialoglab.findViewById(R.id.txt_lab_add)
									.setVisibility(View.VISIBLE);
							dialoglab.findViewById(R.id.txt_lab_address)
									.setVisibility(View.VISIBLE);

						} else {
							dialoglab.findViewById(R.id.rl_p_details)
									.setVisibility(View.VISIBLE);
							dialoglab.findViewById(R.id.txt_lab_add)
									.setVisibility(View.GONE);
							dialoglab.findViewById(R.id.txt_lab_address)
									.setVisibility(View.GONE);

						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});
		final Spinner spinnerTechnician = (Spinner) dialoglab
				.findViewById(R.id.spinner_technician);
		LabsTechnicianAdapter technicianAdapter = new LabsTechnicianAdapter(
				context, android.R.layout.simple_spinner_item,
				EzApp.labTechnicianDao.loadAll());
		technicianAdapter.setDropDownViewResource(R.layout.labs_row_technician);
		spinnerTechnician.setAdapter(technicianAdapter);

		final Button button = (Button) dialoglab
				.findViewById(R.id.btn_schedule);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				FlurryAgent
						.logEvent("LabsAppointmentController - Submit Schedule Button Clicked");
				if (Util.isEmptyString(((EditText) dialoglab
						.findViewById(R.id.edit_reason)).getText().toString())) {
					Util.Alertdialog(context, "Enter reason for visit");
				} else {
					scheduleAppointment(
							patient,
							date,
							slot,
							((EditText) dialoglab
									.findViewById(R.id.edit_reason)).getText()
									.toString(),
							spinnerVisitLocation.getSelectedItemPosition() + 1,
							""
									+ ((LabTechnician) (spinnerTechnician
											.getSelectedItem())).getId(),
							new InRefferalModel(), context, dialoglab,
							responsee);
					dialoglab.dismiss();
				}

			}
		});
		dialoglab.setCancelable(false);
		dialoglab.findViewById(R.id.txt_close).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialoglab.dismiss();
					}
				});
		dialoglab.show();

	}

	public static void showFollowupDialog(final Date date, final String slot,
			final LabAppointment apt, final Context context,
			final Patient patient, final OnResponse responsee) {
		AutoCompleteTextView actvCountry;
		AutoCompleteTextView actvState;
		AutoCompleteTextView actvCity;
		AutoCompleteTextView actvLocality;
		final Spinner spinnerVisitLocation;
		final ArrayList<String> arrVisitLocation = new ArrayList<String>();

		final Dialog dialoglab = new Dialog(context);
		dialoglab.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialoglab.setContentView(R.layout.labs_dialog_followup_appointment);
		dialoglab.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		final Appointment pm = new Appointment();
		Date currentDate = Calendar.getInstance().getTime();
		currentDate.setHours(0);
		currentDate.setMinutes(0);
		currentDate.setSeconds(0);
		Date tomorrowDate = new Date(currentDate.getTime() + 24 * 60 * 60000
				- 60000);
		final SimpleDateFormat sd;
		if (date.after(currentDate) && date.before(tomorrowDate)) {
			sd = new SimpleDateFormat("' @ 'hh:mm a' Today '");
		} else {
			sd = new SimpleDateFormat("' @ 'hh:mm a' on 'EEE, MMM dd', 'yyyy");
		}
		if (Util.isEmptyString(patient.getPgender())) {
			patient.setPgender(" ");
		}
		((TextView) dialoglab.findViewById(R.id.txt_shedule_desc))
				.setText(patient.getP_detail()
						+ sd.format(date)
						+ " at '"
						+ EzApp.sharedPref.getString(
								Constants.LAB_LOCALITY, "") + "'.");
		((TextView) dialoglab.findViewById(R.id.txt_patient_address))
				.setText(patient.getPadd1() + " " + patient.getPadd2() + ", "
						+ patient.getParea() + ", " + patient.getPcity() + ", "
						+ patient.getPstate() + ", " + patient.getPcountry()
						+ " - " + patient.getPzip());
		((TextView) dialoglab.findViewById(R.id.txt_lab_address))
				.setText(EzApp.sharedPref.getString(
						Constants.LAB_ADDRESS, ""));

		((TextView) dialoglab.findViewById(R.id.txt_address_1)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

		((TextView) dialoglab.findViewById(R.id.txt_country)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

		((TextView) dialoglab.findViewById(R.id.txt_state)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

		((TextView) dialoglab.findViewById(R.id.txt_locality)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

		((TextView) dialoglab.findViewById(R.id.txt_city)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

		((TextView) dialoglab.findViewById(R.id.txt_pincode)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

		((TextView) dialoglab.findViewById(R.id.txt_reason)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

		((TextView) dialoglab.findViewById(R.id.txt_technician)).append(Html
				.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));

		((EditText) dialoglab.findViewById(R.id.actv_address_1))
				.setText(patient.getPadd1());
		((EditText) dialoglab.findViewById(R.id.actv_address_2))
				.setText(patient.getPadd2());
		((AutoCompleteTextView) dialoglab.findViewById(R.id.actv_country))
				.setText(patient.getPcountry());
		((AutoCompleteTextView) dialoglab.findViewById(R.id.actv_state))
				.setText(patient.getPstate());
		((AutoCompleteTextView) dialoglab.findViewById(R.id.actv_city))
				.setText(patient.getPcity());
		((AutoCompleteTextView) dialoglab.findViewById(R.id.actv_locality))
				.setText(patient.getParea());
		((EditText) dialoglab.findViewById(R.id.actv_pincode)).setText(patient
				.getPzip());
		((TextView) dialoglab.findViewById(R.id.edit_reason)).setText(apt
				.getReason());
		spinnerVisitLocation = (Spinner) dialoglab
				.findViewById(R.id.spinner_visit_location);
		arrVisitLocation.clear();
		arrVisitLocation.add("Clinic");
		arrVisitLocation.add("Patient Location");

		final ArrayAdapter<String> adapterVisitLocation = new ArrayAdapter<String>(
				context, android.R.layout.simple_spinner_item, arrVisitLocation);
		adapterVisitLocation
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerVisitLocation.setAdapter(adapterVisitLocation);
		spinnerVisitLocation
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int pos, long arg3) {
						// TODO Auto-generated method stub
						if (pos == 0) {

							dialoglab.findViewById(R.id.rl_p_details)
									.setVisibility(View.GONE);
							dialoglab.findViewById(R.id.txt_lab_add)
									.setVisibility(View.VISIBLE);
							dialoglab.findViewById(R.id.txt_lab_address)
									.setVisibility(View.VISIBLE);

						} else {
							dialoglab.findViewById(R.id.rl_p_details)
									.setVisibility(View.VISIBLE);
							dialoglab.findViewById(R.id.txt_lab_add)
									.setVisibility(View.GONE);
							dialoglab.findViewById(R.id.txt_lab_address)
									.setVisibility(View.GONE);

						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});
		final Spinner spinnerTechnician = (Spinner) dialoglab
				.findViewById(R.id.spinner_technician);
		LabsTechnicianAdapter technicianAdapter = new LabsTechnicianAdapter(
				context, android.R.layout.simple_spinner_item,
				EzApp.labTechnicianDao.loadAll());
		technicianAdapter.setDropDownViewResource(R.layout.labs_row_technician);
		spinnerTechnician.setAdapter(technicianAdapter);

		final Button button = (Button) dialoglab
				.findViewById(R.id.btn_followup);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				FlurryAgent
						.logEvent("LabsAppointmentController - Submit Followup Button Clicked");
				followupAppointment(
						patient,
						date,
						slot,
						((TextView) dialoglab.findViewById(R.id.edit_reason))
								.getText().toString(),
						spinnerVisitLocation.getSelectedItemPosition() + 1,
						""
								+ ((LabTechnician) (spinnerTechnician
										.getSelectedItem())).getId(),
						new InRefferalModel(), context, dialoglab, apt,
						responsee);
				dialoglab.dismiss();

			}
		});
		dialoglab.setCancelable(false);
		dialoglab.findViewById(R.id.txt_close).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialoglab.dismiss();
					}
				});
		dialoglab.show();

	}

	public static void followupAppointment(final Patient pat, final Date date,
			final String slot, final String reason, final int vl,
			String assigned_user_id, InRefferalModel inref,
			final Context context, final Dialog dialog, LabAppointment apt,
			final OnResponse res) {

		String url = LabApis.RESCHEDULEAPPOINTMENT.replace("{booking_Id}",
				apt.getBkid());
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("format", "json");
		params.put("pid", pat.getPid());
		params.put("fid", pat.getFid());
		params.put("date", EzApp.sdfyyMmdd.format(date));
		params.put("p-date",
				EzApp.sdfyyMmdd.format(apt.getAptdate()));
		params.put("sid", slot);
		params.put("apt-reason", reason);
		params.put("visit_location", "" + vl);
		params.put("assigned_user_type", "LT");
		params.put("ref-id", inref.getRefId());
		params.put("ref-ep", inref.getEpId());
		if (vl == LOCATION_PATIENT) {
			params.put("address", pat.getPadd1());
			params.put("address2", pat.getPadd2());
			params.put("country", pat.getPcountryid());
			params.put("cmbState", pat.getPstateid());
			params.put("cmbCity", pat.getPcityid());
			params.put("cmbArea", pat.getPareaid());
			params.put("cmbArea_text", pat.getParea());
			params.put("zip", pat.getPzip());
			params.put("assigned_user_id", assigned_user_id);
		}

		final Dialog loaddialog = Util.showLoadDialog(context);
		final SimpleDateFormat df = new SimpleDateFormat(
				"EEE, MMM dd', 'yyyy hh:mm aa ' To '");
		final SimpleDateFormat df1 = new SimpleDateFormat("hh:mm aa");
		final String date1 = df1.format(new Date(
				date.getTime() + 15 * 60 * 1000));
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						Log.i("", response);
						try {
							JSONObject jOBj = new JSONObject(response);
							if (jOBj.getString("s").equals("200")) {
								if (vl == LOCATION_CLINIC) {
									res.onResponseListner(jOBj
											.getString("bk_id"));
									Util.AlertdialogWithFinish(
											context,
											"Appointment with "
													+ pat.getPfn()
													+ " "
													+ pat.getPln()
													+ " for "
													+ df.format(date)
													+ date1
													+ " at "
													+ EzApp.sharedPref
															.getString(
																	Constants.LAB_ADDRESS,
																	"")
													+ " created successfully");
								} else {
									res.onResponseListner(jOBj
											.getString("bk_id"));
									Util.AlertdialogWithFinish(
											context,
											"Appointment with " + pat.getPfn()
													+ " " + pat.getPln()
													+ " for " + df.format(date)
													+ date1 + " at "
													+ pat.getPadd1() + " "
													+ pat.getPadd2() + ", "
													+ pat.getParea() + ", "
													+ pat.getPcity() + ", "
													+ pat.getPstate() + ", "
													+ pat.getPcountry()
													+ " created successfully");
								}
							}

							else
								res.onResponseListner("error");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							res.onResponseListner("error");
						}
						dialog.dismiss();
						loaddialog.dismiss();

					}
				});
	}

	public static void directAppointment(final OnResponse res,
			final Patient pat, final Context context) {
		String url = LabApis.DIRECTAPPOINTMENT;
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("cli", "api");
		params.put("pat_id", pat.getPid());
		params.put("fam_id", pat.getFid());
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						try {
							JSONObject jOBj = new JSONObject(response);
							if (jOBj.getString("s").equals("200")) {
								res.onResponseListner(jOBj.getString("bk_id"));

							} else
								res.onResponseListner("error");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							res.onResponseListner("error");
						}

					}
				});

	}

	public static void getAppointmentListApi(FragmentActivity activity,
			final OnResponseApt responsee, String id) {
		final String url = LabApis.GET_APPOINTMENT + id;
		Log.i("", url);
		final HashMap<String, String> params = new HashMap<String, String>();
		Log.i("", params.toString());
		EzApp.networkController.networkCall(activity, url,
				params, new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						Log.i("", response);
						if (response.contains("error")) {
							responsee.onResponseListner("error", "error");
						} else {
							try {
								final JSONObject jObj = new JSONObject(response);
								if (jObj.getString("s").equals("200")) {
									final JSONArray patientList = jObj
											.getJSONArray("data");
									final JSONArray patientList1 = jObj
											.getJSONArray("patients");
									for (int i = 0; i < patientList1.length(); i++) {
										com.ezhealthtrack.greendao.PatientShow pat = new Gson()
												.fromJson(
														patientList1.get(i)
																.toString(),
														com.ezhealthtrack.greendao.PatientShow.class);
										pat.setId((long) (Integer.parseInt(pat
												.getP_id()) + 100000 * Integer
												.parseInt(pat.getPf_id())));
										EzApp.patientShowDao
												.insertOrReplace(pat);

										Log.i("", new Gson().toJson(pat));
									}
									for (int i = 0; i < patientList.length(); i++) {
										final JSONObject patientListData = patientList
												.getJSONObject(i);
										LabAppointment appointment = new LabAppointment();
										appointment.setId(patientListData
												.getLong("bk-id"));
										appointment.setBkid(patientListData
												.getString("bk-id"));
										if (patientListData.has("ep-id"))
											appointment.setEpid(patientListData
													.getString("ep-id"));
										else
											appointment.setEpid("0");
										appointment.setPid(patientListData
												.getString("p-id"));
										appointment.setPfid(patientListData
												.getString("pf-id"));
										appointment.setReason(patientListData
												.getString("reason"));
										appointment.setFlag(patientListData
												.getString("flag"));
										appointment.setApflag(patientListData
												.getString("ap-flag"));
										appointment.setVisit(patientListData
												.getString("visit"));
										appointment.setSlotid(patientListData
												.getString("slot-id"));
										appointment
												.setVisit_location(patientListData
														.getString("visit_location"));
										appointment
												.setAssigned_user_id(patientListData
														.getString("assigned_user_id"));
										final SimpleDateFormat sdf = new SimpleDateFormat(
												"MM/dd/yyyy");
										try {
											final Date date = sdf.parse(patientListData
													.getString("apt-date"));
											final Calendar cal = Calendar
													.getInstance();
											cal.setTime(date);
											cal.set(Calendar.HOUR_OF_DAY, 0);
											cal.set(Calendar.MINUTE, 0);
											cal.add(Calendar.MINUTE,
													(Integer.parseInt(patientListData
															.getString("slot-id")) - 1) * 15);
											// Log.i("",
											// cal.getTime().toString());

											appointment.setAptdate(cal
													.getTime());
										} catch (final Exception e) {
											Log.e("", e);
											appointment.setAptdate(Calendar
													.getInstance().getTime());
										}
										if (patientListData.has("follow-id")) {
											appointment
													.setFollowid(patientListData
															.getString("follow-id"));
										}
										EzApp.labAptDao
												.insertOrReplace(appointment);

									}
									Log.i("", jObj.getString("count"));
									responsee.onResponseListner(
											jObj.getString("cond"),
											jObj.getString("count"));
								} else {
									responsee.onResponseListner("error",
											"error");
									Log.i("", response);
								}
							} catch (final Exception e) {
								Log.e("", e);
								responsee.onResponseListner("error", "error");
							}

						}
					}
				});
	}

	public static void showAcceptDialog(final LabAppointment rowItem,
			final Context context) {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.labs_dialog_accept);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setCancelable(false);
		dialog.findViewById(R.id.txt_close).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							InputMethodManager imm = (InputMethodManager) context
									.getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									dialog.findViewById(R.id.edit_note)
											.getWindowToken(), 0);
						} catch (Exception e) {

						}
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
		Date currentDate = Calendar.getInstance().getTime();
		currentDate.setHours(0);
		currentDate.setMinutes(0);
		currentDate.setSeconds(0);
		Date tomorrowDate = new Date(currentDate.getTime() + 24 * 60 * 60000
				- 60000);
		if (rowItem.getAptdate().compareTo(currentDate) > 0
				&& rowItem.getAptdate().compareTo(tomorrowDate) < 0) {
			final SimpleDateFormat df1 = new SimpleDateFormat(
					"' @ 'hh:mm a' Today '");
			PatientShow p = new PatientShow();
			try {
				if (EzApp.patientShowDao
						.queryBuilder()
						.where(com.ezhealthtrack.greendao.PatientShowDao.Properties.P_id
								.eq(rowItem.getPid()),
								com.ezhealthtrack.greendao.PatientShowDao.Properties.Pf_id
										.eq(rowItem.getPfid())).count() > 0) {
					p = (EzApp.patientShowDao
							.queryBuilder()
							.where(com.ezhealthtrack.greendao.PatientShowDao.Properties.P_id
									.eq(rowItem.getPid()),
									com.ezhealthtrack.greendao.PatientShowDao.Properties.Pf_id
											.eq(rowItem.getPfid())).list()
							.get(0));
				}

			} catch (Exception e) {
				Log.e("", e);
			}
			((TextView) dialog.findViewById(R.id.txt_desc)).setText(p.getPfn()
					+ " " + p.getPln() + ", " + p.getAge() + "/"
					+ p.getGender() + ", " + p.getP_type() + " Patient"
					+ df1.format(rowItem.getAptdate()) + " for " + "'"
					+ rowItem.getReason() + "'");

		} else {
			final SimpleDateFormat df = new SimpleDateFormat(
					"' @ 'hh:mm a' on 'EEE, MMM dd', 'yyyy");
			PatientShow p = new PatientShow();
			try {
				if (EzApp.patientShowDao
						.queryBuilder()
						.where(com.ezhealthtrack.greendao.PatientShowDao.Properties.P_id
								.eq(rowItem.getPid()),
								com.ezhealthtrack.greendao.PatientShowDao.Properties.Pf_id
										.eq(rowItem.getPfid())).count() > 0) {
					p = (EzApp.patientShowDao
							.queryBuilder()
							.where(com.ezhealthtrack.greendao.PatientShowDao.Properties.P_id
									.eq(rowItem.getPid()),
									com.ezhealthtrack.greendao.PatientShowDao.Properties.Pf_id
											.eq(rowItem.getPfid())).list()
							.get(0));
				}

			} catch (Exception e) {
				Log.e("", e);
			}
			((TextView) dialog.findViewById(R.id.txt_desc)).setText(p.getPfn()
					+ " " + p.getPln() + ", " + p.getAge() + "/"
					+ p.getGender() + ", " + p.getP_type() + " Patient"
					+ df.format(rowItem.getAptdate()) + " for " + "'"
					+ rowItem.getReason() + "'");

		}

		dialog.findViewById(R.id.btn_submit).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(final View v) {
						FlurryAgent
								.logEvent("LabsAppointmentController - Submit Accept Button Clicked");
						if (!Util.isEmptyString(((EditText) dialog
								.findViewById(R.id.edit_note)).getText()
								.toString())) {
							acceptAppointment(((EditText) dialog
									.findViewById(R.id.edit_note)).getText()
									.toString(), rowItem, context, dialog);
							try {
								InputMethodManager imm = (InputMethodManager) context
										.getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.hideSoftInputFromWindow(dialog
										.findViewById(R.id.edit_note)
										.getWindowToken(), 0);
							} catch (Exception e) {

							}
							dialog.dismiss();
							// loaddialog.dismiss();

						} else {
							Util.Alertdialog(context, "Please enter note");
						}

					}
				});

		dialog.show();

	}

	public static void acceptAppointment(String checkNote,
			final LabAppointment apt, final Context context, final Dialog dialog) {

		String url = LabApis.ACCEPTAPPOINTMENT.replace("{booking_Id}",
				apt.getBkid());
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("format", "json");
		params.put("cli", "api");
		params.put("note", checkNote);
		params.put("bk_id", apt.getBkid());
		params.put("assigned_user_id", apt.getAssigned_user_id());

		final Dialog loaddialog = Util.showLoadDialog(context);
		EzApp.networkController.networkCall(context, url, params,
				new OnResponse() {

					@Override
					public void onResponseListner(String response) {
						Log.i("", response);
						if (!response.equals("error")) {
							JSONObject jObj;
							try {
								jObj = new JSONObject(response);
								if (jObj.getString("s").contains("200")) {
									Util.Alertdialog(context,
											"Appointment Accepted Sucessfully");
									deleteAppointment(apt);
									dialog.dismiss();
								} else {
									Util.Alertdialog(context,
											"There is some error in accepting appointment, Please try again.");
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}

						}
						loaddialog.dismiss();

					}
				});
	}
}

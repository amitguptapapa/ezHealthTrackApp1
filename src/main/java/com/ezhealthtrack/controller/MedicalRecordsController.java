package com.ezhealthtrack.controller;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.greendao.MedRecAllergy;
import com.ezhealthtrack.greendao.MedRecVisitNotes;
import com.ezhealthtrack.model.AllergyModel;
import com.ezhealthtrack.model.MedRecClinicalLab;
import com.ezhealthtrack.model.MedRecEKG;
import com.ezhealthtrack.model.MedRecPresModel;
import com.ezhealthtrack.model.MedRecRadiology;
import com.ezhealthtrack.model.PatientAutoSuggest;
import com.ezhealthtrack.model.VitalModel;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.NetworkCallController.OnResponse;
import com.ezhealthtrack.util.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MedicalRecordsController {
    public static void getVisitNotesList(Context context, int pagenum,
                                         String search, Date fDate, Date tDate, final OnResponse onresponse) {
        final String url = APIs.MED_REC_VISIT_NOTES() + pagenum;
        final HashMap<String, String> params = new HashMap<String, String>();
        params.put("format", "json");
        params.put("Booking_page", "" + pagenum);
        if (!Util.isEmptyString(search))
            params.put("search", search);
        if (fDate != null)
            params.put("fdate", EzApp.sdfddMmyy1.format(fDate));
        if (tDate != null)
            params.put("tdate", EzApp.sdfddMmyy1.format(tDate));
        params.put("submit", "");
        Log.i(url, params.toString());
        EzApp.networkController.networkCall(context, url, params,
                new OnResponse() {

                    @Override
                    public void onResponseListner(String response) {
                        Log.i(url, response);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray data = jsonResponse.getJSONArray("data");
                            Gson gson = new GsonBuilder().setDateFormat(
                                    "yyyy-MM-dd").create();
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jNote = data.getJSONObject(i);
                                MedRecVisitNotes note = gson.fromJson(
                                        jNote.toString(),
                                        MedRecVisitNotes.class);
                                note.setId(Long.parseLong(note.getBk_id()));
                                EzApp.medRecVisitNotesDao
                                        .insertOrReplace(note);
                            }
                            onresponse.onResponseListner(jsonResponse
                                    .getString("count"));
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                });
    }

    public static final String ALLERGY_TYPE = "allergies";
    public static final String VITALS_TYPE = "vitals";
    public static final String PRESCRIPTION_TYPE = "prescriptions";
    public static final String RADIOLOGY_TYPE = "radiology";
    public static final String LAB_TYPE = "lab";
    public static final String ECG_TYPE = "ecg";

    public static void getAllergyList(String type, Context context,
                                      int pagenum, String search, final OnResponse onresponse) {
        final String url = APIs.MED_REC_LIST().replace("{type}", type)
                + pagenum;
        final HashMap<String, String> params = new HashMap<String, String>();
        params.put("format", "json");
        params.put("UserPatientRelation_page", "" + pagenum);
        if (!Util.isEmptyString(search))
            params.put("search", search);
        params.put("submit", "");
        EzApp.networkController.networkCall(context, url, params,
                new OnResponse() {

                    @Override
                    public void onResponseListner(String response) {
                        Log.i(url, response);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray data = jsonResponse.getJSONArray("data");
                            Gson gson = new GsonBuilder().setDateFormat(
                                    "yyyy-MM-dd").create();
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jNote = data.getJSONObject(i);
                                MedRecAllergy note = gson.fromJson(
                                        jNote.toString(), MedRecAllergy.class);
                                note.setId(Long.parseLong(note.getPat_id()));
                                EzApp.medRecAllergyDao
                                        .insertOrReplace(note);
                            }
                            onresponse.onResponseListner(jsonResponse
                                    .getString("count"));
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.v("MRC:getAllergyList()",
                                    "Exception: " + e.getMessage());
                            e.printStackTrace();
                            onresponse.onResponseListner("error");
                        }

                    }
                });
    }

    public interface OnResponseObject {
        public void onResponseListner(Object response);
    }

    public static void getMedicalRecordDetail(Context context,
                                              final String medType, String pat_id,
                                              final OnResponseObject onresponse) {
        String url = APIs.MED_REC_DETAIL();
        final HashMap<String, String> params = new HashMap<String, String>();
        params.put("format", "json");
        params.put("pat_id", pat_id);
        params.put("medical_type", medType);
        if (EzApp.sharedPref.getString(Constants.DR_SPECIALITY,
                "").equals("Dentist")) {
            params.put("user_type", "dent");
        } else {
            params.put("user_type", "phys");
        }
        EzApp.networkController.networkCall(context, url, params,
                new OnResponse() {

                    @Override
                    public void onResponseListner(String response) {
                        Log.i(" ", response);
                        if (medType.equals(ALLERGY_TYPE)) {
                            AllergyModel model = new Gson().fromJson(response,
                                    AllergyModel.class);
                            onresponse.onResponseListner(model);
                        } else if (medType.equals(VITALS_TYPE)) {
                            try {
                                JSONObject jobj = Util
                                        .removeEmptyObjects(new JSONObject(
                                                response));
                                Log.i("RESPONSE ----", jobj.toString());
                                VitalModel model = new Gson().fromJson(
                                        response, VitalModel.class);
                                onresponse.onResponseListner(model);
                            } catch (IllegalStateException | JsonSyntaxException | JSONException e) {
                                // TODO Auto-generated catch block
                                Log.e("TAG-ERROR", e.toString());
                                e.printStackTrace();
                            }

                        } else if (medType.equals(PRESCRIPTION_TYPE)) {
                            try {
                                JSONObject jobj = new JSONObject(response);
                                JSONArray jArr = jobj.getJSONArray("data");
                                for (int i = 0; i < jArr.length(); i++) {
                                    if (!Util.isEmptyString(jArr
                                            .getJSONObject(i).get("soap")
                                            .toString())) {
                                        String s = jArr.getJSONObject(i)
                                                .get("soap").toString();
                                        jArr.getJSONObject(i).put("soap", s);
                                    }
                                }
                                MedRecPresModel model = new Gson().fromJson(
                                        jobj.toString(), MedRecPresModel.class);
                                onresponse.onResponseListner(model);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        } else if (medType.equals(RADIOLOGY_TYPE)) {
                            try {
                                JSONObject jobj = new JSONObject(response);
                                JSONArray jArr = jobj.getJSONArray("data");
                                for (int i = 0; i < jArr.length(); i++) {
                                    if (!Util.isEmptyString(jArr
                                            .getJSONObject(i).get("soap")
                                            .toString())) {
                                        String s = jArr.getJSONObject(i)
                                                .getJSONObject("soap")
                                                .toString();
                                        jArr.getJSONObject(i).put("soap", s);
                                    }
                                }
                                MedRecRadiology model = new Gson().fromJson(
                                        jobj.toString(), MedRecRadiology.class);
                                onresponse.onResponseListner(model);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else if (medType.equals(LAB_TYPE)) {
                            try {
                                JSONObject jobj = new JSONObject(response);
                                JSONArray jArr = jobj.getJSONArray("data");
                                for (int i = 0; i < jArr.length(); i++) {
                                    if (!Util.isEmptyString(jArr
                                            .getJSONObject(i).get("soap")
                                            .toString())) {
                                        String s = jArr.getJSONObject(i)
                                                .get("soap").toString();
                                        jArr.getJSONObject(i).put("soap", s);
                                    }
                                }
                                MedRecClinicalLab model = new Gson().fromJson(
                                        jobj.toString(),
                                        MedRecClinicalLab.class);
                                onresponse.onResponseListner(model);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else if (medType.equals(ECG_TYPE)) {
                            try {
                                JSONObject jobj = new JSONObject(response);
                                JSONArray jArr = jobj.getJSONArray("data");
                                for (int i = 0; i < jArr.length(); i++) {
                                    if (!Util.isEmptyString(jArr
                                            .getJSONObject(i).get("soap")
                                            .toString())) {
                                        String s = jArr.getJSONObject(i)
                                                .get("soap").toString();
                                        jArr.getJSONObject(i).put("soap", s);
                                    }
                                }
                                MedRecEKG model = new Gson().fromJson(
                                        jobj.toString(), MedRecEKG.class);
                                onresponse.onResponseListner(model);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                    }
                });

    }

    public static void deleteDocument(final AllergyModel model,
                                      final Context context, final View v) {
        final String url = APIs.DELETE_ALLERGY();
        final Dialog loaddialog = Util.showLoadDialog(context);
        final StringRequest patientListRequest = new StringRequest(
                Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {

                    final JSONObject jObj = new JSONObject(response);
                    if (jObj.getString("s").equals("200")) {

                        // AllergiesFragment.arrAllergy.remove(model);
                        // AllergiesFragment.listAllergies.removeView(v);
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                final HashMap<String, String> loginParams = new HashMap<String, String>();
                loginParams.put("auth-token", Util
                        .getBase64String(EzApp.sharedPref
                                .getString(Constants.USER_TOKEN, "")));
                return loginParams;
            }

            @Override
            protected Map<String, String> getParams() {
                final HashMap<String, String> loginParams = new HashMap<String, String>();
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                loginParams.put("format", "json");
                return loginParams;
            }

        };
        patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        EzApp.mVolleyQueue.add(patientListRequest);

    }

    public static void addAllergy(final String mc, final String sc,
                                  final String ai, final PatientAutoSuggest pat,
                                  final Context context, final Dialog dialogAddAllergies) {
        final String url = APIs.ADD_ALLERGIES();
        final Dialog loaddialog = Util.showLoadDialog(context);
        final StringRequest patientListRequest = new StringRequest(
                Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    Log.i("add allergy", response);
                    final JSONObject jObj = new JSONObject(response);
                    if (jObj.getString("s").equals("200")) {
                        dialogAddAllergies.dismiss();
                        Util.Alertdialog(context,
                                "Allergies added successfully");

                    } else {
                        Util.Alertdialog(context,
                                "Allergy already added");
                    }
                } catch (final JSONException e) {
                    Util.Alertdialog(context,
                            "There is some error in adding allergies, please try again.");

                }
                loaddialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                Util.Alertdialog(context,
                        "Network error,try again later");
                error.printStackTrace();
                loaddialog.dismiss();
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
                params.put("format", "json");
                params.put("main_cat", mc);
                params.put("sub_cat", sc);
                params.put("addi_info", ai);
                params.put("search", pat.getName());
                params.put("pat_id", pat.getId());
                params.put("fam_id", "0");
                params.put("bk_id", "0");
                return params;
            }

        };
        patientListRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        EzApp.mVolleyQueue.add(patientListRequest);
    }

}

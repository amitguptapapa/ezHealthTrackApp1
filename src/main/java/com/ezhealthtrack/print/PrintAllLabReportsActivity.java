package com.ezhealthtrack.print;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.model.laborder.Data;
import com.ezhealthtrack.model.laborder.LabContactNumber;
import com.ezhealthtrack.model.laborder.LabOrderDetails;
import com.ezhealthtrack.model.laborder.Reference;
import com.ezhealthtrack.model.laborder.Result;
import com.ezhealthtrack.model.laborder.SampleMetum;
import com.ezhealthtrack.model.laborder.TestReport;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Util;

import java.util.Date;

public class PrintAllLabReportsActivity extends EzPrint {

    // Lab Info
    private TextView txtLabName;
    private TextView txtLabMotto;
    private TextView txtLabAddress;
    private TextView txtLabPhone;
    private TextView txtTech;
    private ImageView imgLab;

    // Patient Info
    private TextView txtPatientName;
    private TextView txtPatientAddress;
    private TextView txtPatientPhone;
    private TextView txtPatientEmail;

    // Report Info
    private TextView txtOrderId;
    private TextView txtReportingDate;
    private TextView txtReportAvailDate;
    private TextView txtPanel;
    RelativeLayout rlLabInfo;

    static LabOrderDetails mOrders;

    static public void setLabOrderDetails(LabOrderDetails order) {
        mOrders = order;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_all_lab_reports);

        // LabOrderDetails mOrders = (LabOrderDetails) getIntent()
        // .getSerializableExtra("mOrders");
        Data mData = mOrders.getData();

        // Lab get ID's
        txtLabName = (TextView) findViewById(R.id.txt_lab_name);
        txtLabMotto = (TextView) findViewById(R.id.txt_lab_motto);
        txtLabAddress = (TextView) findViewById(R.id.txt_lab_address);
        txtLabPhone = (TextView) findViewById(R.id.txt_phone_lab);
        txtTech = (TextView) findViewById(R.id.txt_tech_name);
        imgLab = (ImageView) findViewById(R.id.img_lab);

        // Patient get ID's
        txtPatientName = (TextView) findViewById(R.id.txt_patient_name);
        txtPatientAddress = (TextView) findViewById(R.id.txt_patient_address);
        txtPatientPhone = (TextView) findViewById(R.id.txt_phone);
        txtPatientEmail = (TextView) findViewById(R.id.txt_email);

        // Reports get ID's
        txtOrderId = (TextView) findViewById(R.id.txt_order_id_display);
        txtReportingDate = (TextView) findViewById(R.id.txt_date_display);
        txtReportAvailDate = (TextView) findViewById(R.id.txt_report_avai_date_display);
        txtPanel = (TextView) findViewById(R.id.txt_panel);
        rlLabInfo = (RelativeLayout) findViewById(R.id.rl_lab_info);

        // Visibility set
        rlLabInfo.setVisibility(View.VISIBLE);
        txtPanel.setVisibility(View.GONE);

        // Set Lab Data
        txtLabName.setText(mData.getLabName());
        txtLabMotto.setText(mData.getLabMoto());
        txtLabAddress.setText(mData.getLabAddress());
        for (LabContactNumber cn : mData.getLabContactNumber()) {
            txtLabPhone.setText(cn.getNum());
        }
        txtTech.setText(mData.getTechnicianName());

        // Get Lab Image/Photo {
        Util.getImageFromUrl(mData.getLabImage(), DashboardActivity.imgLoader,
                imgLab);
        // }

        // Set Patient Data
        txtPatientName.setText(mData.getPatientDetail());
        txtPatientAddress.setText(mData.getPatientAddress());
        txtPatientPhone.setText(mData.getPatientContactNumber());
        txtPatientEmail.setText(mData.getPatientEmail());

        // Set Report Data
        txtOrderId.setText(mData.getDisplayOrderId());

        try {
            Date date;
            String theDate = mData.getTestReports().get(0)
                    .getReportPreparedOn();
            if (!Util.isEmptyString(theDate)) {
                date = EzApp.sdfyymmddhhmmss.parse(theDate);
                txtReportingDate.setText(EzApp.sdfddMmyy
                        .format(date));
            }
            theDate = mData.getTestReports().get(0).getReportAvailableOn();
            if (!Util.isEmptyString(theDate)) {
                date = EzApp.sdfyymmddhhmmss.parse(theDate);
                txtReportAvailDate.setText(EzApp.sdfddMmyy
                        .format(date));
            }

            if (!Util.isEmptyString(EzApp.sharedPref.getString(
                    Constants.SIGNATURE, "signature"))) {
                Util.getImageFromUrl(EzApp.sharedPref.getString(
                        Constants.SIGNATURE, ""), DashboardActivity.imgLoader,
                        (ImageView) findViewById(R.id.img_signature));
            }
        } catch (Exception e) {

        }

        LinearLayout llReportValues = (LinearLayout) findViewById(R.id.ll_test_name_display);
        llReportValues.removeAllViews();

        // if (report.getSampleMeta().size() > 1) {
        // txtPanel.setVisibility(View.VISIBLE);
        // txtPanel.setText(Html.fromHtml("<b>Lab Panel: </b>"
        // + report.getReportName()));
        // }
        for (TestReport report1 : mData.getTestReports()) {
            int i = 0;
            if (report1.getSampleMeta() != null)
                for (SampleMetum sm1 : report1.getSampleMeta()) {
                    i++;
                    final SampleMetum sm;
                    if (sm1.getResults().size() == 0) {
                        sm = sm1.sample_meta.get(0);
                    } else {
                        sm = sm1;
                    }

                    final View v = getLayoutInflater().inflate(
                            R.layout.labs_row_print_report_test_list, null);
                    final TextView txtTestName = (TextView) v
                            .findViewById(R.id.txt_test_name_display);
                    final TextView txtMethod = (TextView) v
                            .findViewById(R.id.txt_method);
                    final TextView txtMethodDisplay = (TextView) v
                            .findViewById(R.id.txt_method_display);
                    final TextView txtSample1 = (TextView) v
                            .findViewById(R.id.txt_sample);
                    final TextView txtSampleDisplay = (TextView) v
                            .findViewById(R.id.txt_sample_display);
                    final TextView InterpretationText = (TextView) v
                            .findViewById(R.id.txt_interpretation_text);
                    txtMethod.setVisibility(View.GONE);
                    txtMethodDisplay.setVisibility(View.GONE);
                    txtSample1.setVisibility(View.GONE);
                    txtSampleDisplay.setVisibility(View.GONE);
                    InterpretationText.setVisibility(View.GONE);

                    if (report1.getSampleMeta().size() > 0 && i == 1) {
                        txtTestName.setText(report1.getReportName());
                    }

                    // if (!report.getSampleMeta().equals("0")) {
                    // if (!Util.isEmptyString(sm.getMethod())) {
                    // txtMethodDisplay.setText(sm.getMethod());
                    // }
                    // } else {
                    // txtMethod.setVisibility(View.GONE);
                    // txtMethodDisplay.setVisibility(View.GONE);
                    // }
                    // if (!report.getSampleMeta().equals("0")) {
                    // txtSampleDisplay.setText(sm.getName());
                    // } else {
                    // txtSample1.setVisibility(View.GONE);
                    // txtSampleDisplay.setVisibility(View.GONE);
                    // }
                    // WebView txt = (WebView)
                    // v.findViewById(R.id.wv_interpret);

                    // final List<Result> arrResults;

                    // if (sm.getdisplay_interpretation() == true) {
                    // txt.setVisibility(View.VISIBLE);
                    // InterpretationText.setVisibility(View.VISIBLE);
                    // } else {
                    // txt.setVisibility(View.GONE);
                    // InterpretationText.setVisibility(View.GONE);
                    // }

                    // if (sm.getResults().size() > 0) {
                    // arrResults = sm.getResults();
                    // // txt.loadDataWithBaseURL("file:///android_asset/",
                    // // sm.result_interpretation, "text/html", "UTF-8",
                    // null);
                    // } else {
                    // arrResults = sm.sample_meta.get(0).getResults();
                    // // txt.loadDataWithBaseURL("file:///android_asset/",
                    // // sm.sample_meta.get(0).result_interpretation,
                    // // "text/html", "UTF-8", null);
                    // }

                    final LinearLayout llResults = (LinearLayout) v
                            .findViewById(R.id.ll_test_name_display_list);
                    for (final Result res : sm.getResults()) {
                        if (!Util.isEmptyString(res.getValue())) {
                            final View v1 = getLayoutInflater().inflate(
                                    R.layout.labs_row_print_report_test_list_list,
                                    null);
                            llResults.addView(v1);
                            final TextView txtTestNameMain1 = (TextView) v1
                                    .findViewById(R.id.txt_test_name_display);
                            final TextView txtUnit = (TextView) v1
                                    .findViewById(R.id.txt_unit_display);
                            final TextView txtReferenceRange = (TextView) v1
                                    .findViewById(R.id.txt_r_range_display);
                            final TextView txtNotes = (TextView) v1
                                    .findViewById(R.id.txt_notes_display);
                            final TextView editObservedValue = (TextView) v1
                                    .findViewById(R.id.txt_observed_value_display);
                            final TextView editNotes = (TextView) v1
                                    .findViewById(R.id.txt_note_display);
                            final TextView txtnote = (TextView) v1
                                    .findViewById(R.id.txt_note);
                            final TextView txtGroupName = (TextView) v1
                                    .findViewById(R.id.txt_group_name);

                            txtUnit.setText(res.getUnit());
                            txtTestNameMain1.setText(res.getName());
                            editObservedValue.setText(res.getValue());

                            txtGroupName.setVisibility(View.GONE);

                            if (!Util.isEmptyString(res.getNotes())) {
                                txtnote.setVisibility(View.VISIBLE);
                                editNotes.setVisibility(View.VISIBLE);
                                editNotes.setText(res.getNotes());
                            } else {
                                txtnote.setVisibility(View.GONE);
                                editNotes.setVisibility(View.GONE);
                            }

                            // if (!Util.isEmptyString(res.getGroupName()))
                            // {
                            // txtGroupName.setVisibility(View.VISIBLE);
                            // txtGroupName.setText(res.getGroupName());
                            // } else {
                            // txtGroupName.setText("");
                            // txtGroupName.setVisibility(View.GONE);
                            // }

                            // Get Reference range
                            for (Reference result : res.getReferences()) {
                                // if (result.getGender().equals(data)
                                // || result.getGender().equals("Both")) {
                                // // Log.e("age", ""+pat.getage());
                                // if (true) {
                                if (!result.getRangeValueMinOption().contains(
                                        "None")) {
                                    txtReferenceRange.append(result
                                            .getRangeValueMinOption());
                                }
                                if (result.getRangeValueMin().length() > 0) {
                                    txtReferenceRange.append(" "
                                            + result.getRangeValueMin() + " (min)");
                                }
                                if (!result.getRangeValueMaxOption().contains(
                                        "None")) {
                                    txtReferenceRange.append(" "
                                            + result.getRangeValueMaxOption());
                                }
                                if (result.getRangeValueMax().length() > 0) {
                                    txtReferenceRange.append(" "
                                            + result.getRangeValueMax() + " (max)");
                                }
                                if (!Util.isEmptyString(result.getNotes()))
                                    txtNotes.append(result.getNotes());
                                txtReferenceRange.append("\n");
                                txtNotes.append("\n");
                            }
                        }
                    }
                    // }
                    // }

                    llReportValues.addView(v);
                }
        }

    }

    // @Override
    // public void onStart() {
    // super.onStart();
    //
    // Log.i("LOD-A", "onStart()");
    // // TODO Auto-generated method stub
    // if (mOrderDetails != null)
    // return;
    //
    // String url = APIs.LAB_ORDER_DETAILS();
    // Map<String, String> params = new HashMap<String, String>();
    //
    // new EzNetwork().POST(url, params, new ResponseHandler() {
    //
    // @Override
    // public void cmdResponseError(Integer code) {
    // // TODO Auto-generated method stub
    // Log.i("LOD-A", "Error: " + code);
    //
    // EzUtils.showLong("Report can't be download. Please retry..");
    // finish();
    // }
    //
    // @Override
    // public void cmdResponse(JSONObject response, String result) {
    // // TODO Auto-generated method stub
    //
    // Log.i("LOD-A:::", "" + response.toString());
    //
    // try {
    // mOrderDetails = new Gson().fromJson(response.toString(),
    // LabOrderDetails.class);
    // } catch (JsonSyntaxException e) {
    // EzUtils.showLong("Report can't be download. Please retry..");
    // EzUtils.showLong("" + e.getMessage());
    // Log.e("LOD-A", "" + e.getMessage());
    //
    // try {
    // JSONObject data = response.getJSONObject("data");
    // JSONArray reports = data.getJSONArray("test_reports");
    // for (int i = 0; i < reports.length(); i++) {
    // String s = reports.getJSONObject(i).getString(
    // "sample_meta");
    // // Log.e("LOD-A", "" + i + " : " + s);
    // }
    // } catch (JSONException e2) {
    // Log.e("LOD-A", "Parse Error:" + e2.getMessage());
    // }
    // return;
    // }
    // }
    // });
    // }

}

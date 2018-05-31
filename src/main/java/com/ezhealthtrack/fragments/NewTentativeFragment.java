package com.ezhealthtrack.fragments;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ezhealthrack.api.NetworkCalls.OnResponse;
import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.adapter.NewTentativeAdapter;
import com.ezhealthtrack.greendao.Appointment;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NewTentativeFragment extends Fragment implements OnClickListener {
    private ListView listPatients;
    private NewTentativeAdapter adapter;
    private TextView txtStartDate;
    private TextView txtEndDate;
    private Date startDate;
    private Date endDate;
    private EditText editFilter;
    private TextView txtCount;
    public static int totalCount = 0;
    private final ArrayList<Appointment> arrAppointment = new ArrayList<Appointment>();

    // private ProgressBar listPb;

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_tentative,
                container, false);

        TextView title = (TextView) view.findViewById(R.id.txt_screen_title);
        title.setText("Appointments - New / Tentative");
        if (EzUtils.getDeviceSize(getActivity()) == EzUtils.EZ_SCREEN_SMALL) {
            title.setVisibility(View.GONE);
        }

        TextView search = (TextView) view.findViewById(R.id.edit_name);
        search.setHint("Patient Name");

        view.findViewById(R.id.edit_name_view).setVisibility(View.VISIBLE);
        view.findViewById(R.id.start_date_view).setVisibility(View.VISIBLE);
        view.findViewById(R.id.end_date_view).setVisibility(View.VISIBLE);
        view.findViewById(R.id.edit_date_view).setVisibility(View.GONE);
        view.findViewById(R.id.btn_search).setVisibility(View.VISIBLE);
        view.findViewById(R.id.line_2).setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        arrAppointment.clear();
        arrAppointment.addAll(DashboardActivity.arrNewTentativePatients);
        adapter = new NewTentativeAdapter(getActivity(),
                R.layout.row_new_tentative, arrAppointment);
        listPatients = (ListView) getActivity()
                .findViewById(R.id.list_patients);
        if (isVisible()) {
            View listFooter = ((LayoutInflater) getActivity().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE)).inflate(
                    R.layout.list_progressbar, null, false);
            listPatients.addFooterView(listFooter);
            // listPb = (ProgressBar) listFooter
            // .findViewById(R.id.list_progressbar);
        }
        listPatients.setAdapter(adapter);
        editFilter = (EditText) getActivity().findViewById(R.id.edit_name);
        editFilter.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(final Editable arg0) {
            }

            @Override
            public void beforeTextChanged(final CharSequence arg0,
                                          final int arg1, final int arg2, final int arg3) {
            }

            @Override
            public void onTextChanged(final CharSequence cs, final int arg1,
                                      final int arg2, final int arg3) {
                // When user changed the Text
                adapter.getFilter().filter(cs);
                Util.filterClear(editFilter,
                        getActivity().findViewById(R.id.img_clear_filter));
            }
        });
        txtStartDate = (TextView) getActivity().findViewById(
                R.id.txt_start_date);
        txtStartDate.setOnClickListener(this);
        Util.filterClear(txtStartDate,
                getActivity().findViewById(R.id.img_start_date_clear_filter));
        txtEndDate = (TextView) getActivity().findViewById(R.id.txt_end_date);
        txtEndDate.setOnClickListener(this);
        Util.filterClear(txtEndDate,
                getActivity().findViewById(R.id.img_end_date_clear_filter));
        getActivity().findViewById(R.id.btn_search).setOnClickListener(this);
        Util.setupUI(getActivity(),
                getActivity().findViewById(R.id.rl_newtentative));
        updateCount();
        // listPb.setVisibility(View.VISIBLE);
        DashboardActivity.calls.getNewTentativeList(new OnResponse() {

            @Override
            public void onResponseListner(String api) {
                onResume();
                // listPb.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.txt_start_date:
                showCalender(txtStartDate, "startDate");
                break;

            case R.id.txt_end_date:
                showCalender(txtEndDate, "endDate");
                break;

            case R.id.btn_search:
                if (Util.isEmptyString(txtStartDate.getText().toString()))
                    startDate = null;
                if (Util.isEmptyString(txtEndDate.getText().toString()))
                    endDate = null;
                if ((startDate != null) && (endDate != null)
                        && startDate.after(endDate)) {
                    Util.Alertdialog(getActivity(),
                            "End date is before start date, please try again.");

                } else {
                    arrAppointment.clear();
                    arrAppointment
                            .addAll(DashboardActivity.arrNewTentativePatients);
                    for (final Appointment apt : DashboardActivity.arrNewTentativePatients) {
                        if ((startDate != null)
                                && apt.aptDate.before(startDate)
                                && !Util.isEmptyString(txtStartDate.getText()
                                .toString())) {
                            arrAppointment.remove(apt);
                        }
                        if ((endDate != null)
                                && apt.aptDate.after(endDate)
                                && !Util.isEmptyString(txtEndDate.getText()
                                .toString())) {
                            arrAppointment.remove(apt);
                        }
                    }
                }

                adapter.notifyDataSetChanged();
                adapter.getFilter().filter(editFilter.getText());
                break;
        }

    }

    @Override
    public void onResume() {
        // Log.i(getTag(), ""+DashboardActivity.arrNewTentativePatients.size());
        arrAppointment.clear();
        arrAppointment.addAll(DashboardActivity.arrNewTentativePatients);
        adapter.getFilter().filter("");
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void updateCount() {
        txtCount = (TextView) getActivity().findViewById(R.id.txt_count);
        txtCount.setText(Html.fromHtml("<b>" + "Total Results: "
                + NewTentativeFragment.totalCount + "</b>"));
    }

    private void showCalender(final TextView txtView, final String date) {
        final Calendar cal = Calendar.getInstance();
        final DatePickerDialog datepicker = new DatePickerDialog(getActivity(),
                new OnDateSetListener() {

                    @Override
                    public void onDateSet(final DatePicker view,
                                          final int year, int monthOfYear,
                                          final int dayOfMonth) {
                        if ((dayOfMonth < 10) && (monthOfYear < 9)) {
                            txtView.setText("0" + dayOfMonth + "/0"
                                    + ++monthOfYear + "/" + year);
                        } else if ((dayOfMonth < 10) && (monthOfYear > 8)) {
                            txtView.setText("0" + dayOfMonth + "/"
                                    + ++monthOfYear + "/" + year);
                        } else if ((dayOfMonth > 9) && (monthOfYear < 9)) {
                            txtView.setText(dayOfMonth + "/0" + ++monthOfYear
                                    + "/" + year);
                        } else {
                            txtView.setText(dayOfMonth + "/" + ++monthOfYear
                                    + "/" + year);
                        }
                        final SimpleDateFormat sdf = new SimpleDateFormat(
                                "dd/MM/yyyy");
                        if (date.equals("startDate")) {
                            try {
                                startDate = sdf.parse(txtView.getText()
                                        .toString());
                            } catch (final ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                endDate = sdf.parse(txtView.getText()
                                        .toString());
                                endDate.setTime(endDate.getTime() + 24 * 60
                                        * 60 * 1000);
                            } catch (final ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        datepicker.show();
    }

    @Override
    public void onStop() {
        if (isRemoving())
            editFilter.setText("");
        super.onStop();
    }
}

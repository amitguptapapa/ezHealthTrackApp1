package com.ezhealthtrack.fragments;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ezhealthrack.api.NetworkCalls.OnResponse;
import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.adapter.HistoryAdapter;
import com.ezhealthtrack.adapter.HistoryAdapter.EzListItemClickListner;
import com.ezhealthtrack.greendao.Appointment;
import com.ezhealthtrack.model.PatientAutoSuggest;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.Util;
import com.ezhealthtrack.util.Util.DateResponse;
import com.flurry.android.FlurryAgent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HistoryFragment extends Fragment implements OnClickListener,
        EzListItemClickListner {
    private ListView listPatients;
    private HistoryAdapter adapter;
    private TextView txtStartDate;
    private TextView txtEndDate;
    private Date startDate;
    private Date endDate;
    private EditText editFilter;
    private TextView txtCount;
    public static int totalCount = 0;
    public static String condVal = "";
    public static String nextUrl;
    public static String prevUrl = "";
    private ProgressBar listPb;
    private PatientAutoSuggest pat;

    private final ArrayList<Appointment> arrAppointment = new ArrayList<Appointment>();

    View mButtonsBar;

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history, container,
                false);
        mButtonsBar = view.findViewById(R.id.id_buttons_bar);
        mButtonsBar.setVisibility(View.GONE);

        TextView title = (TextView) view.findViewById(R.id.txt_screen_title);
        title.setText("Appointments - History");
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

        // 1. View Appointment Details
        Button btnView = (Button) view.findViewById(R.id.btn_view);
        btnView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                EzCommonActions.appointmentHistoryView(getActivity(),
                        adapter.getSelectedAppointment());
            }
        });

        // 2. Reschedule
        Button btnReschedule = (Button) view.findViewById(R.id.btn_reschedule);
        btnReschedule.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                EzCommonActions.appointmentHistoryReschedule(getActivity(),
                        adapter.getSelectedAppointment());
            }
        });

        // 3. Follow up
        Button btnFollowUp = (Button) view.findViewById(R.id.btn_follow);
        btnFollowUp.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                EzCommonActions.appointmentHistoryFollowUp(getActivity(),
                        adapter.getSelectedAppointment());
            }
        });

        // 4. Send Message
        Button btnSendMessage = (Button) view
                .findViewById(R.id.btn_sendmessage);
        btnSendMessage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                EzCommonActions.appointmentHistorySendMessage(getActivity(),
                        adapter.getSelectedAppointment());
            }
        });

        // 5. Prescription
        Button btnPrescription = (Button) view
                .findViewById(R.id.btn_prescription);
        btnPrescription.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                EzCommonActions.appointmentHistoryPrescription(getActivity(),
                        adapter.getSelectedAppointment());
            }
        });

        // 6. Visit Notes
        Button btnVisitNotes = (Button) view.findViewById(R.id.btn_visitnotes);
        btnVisitNotes.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                EzCommonActions.appointmentHistoryVisitNotes(getActivity(),
                        adapter.getSelectedAppointment());
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("", "Size =" + DashboardActivity.arrHistoryPatients.size());
        arrAppointment.clear();
        arrAppointment.addAll(DashboardActivity.arrHistoryPatients);
        adapter = new HistoryAdapter(getActivity(), R.layout.row_history,
                arrAppointment, this);
        listPatients = (ListView) getActivity()
                .findViewById(R.id.list_patients);
        View listFooter = ((LayoutInflater) getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.list_progressbar, null, false);
        listPatients.addFooterView(listFooter);
        listPb = (ProgressBar) listFooter.findViewById(R.id.list_progressbar);
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
        txtStartDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                FlurryAgent
                        .logEvent("ConfirmedFragment - Start Date Field Clicked");
                Util.showCalender(txtStartDate, getActivity(),
                        new DateResponse() {

                            @Override
                            public void dateResponseListner(Date date) {
                                startDate = date;

                            }
                        });
            }
        });
        Util.filterClear(txtStartDate,
                getActivity().findViewById(R.id.img_start_date_clear_filter));
        txtEndDate = (TextView) getActivity().findViewById(R.id.txt_end_date);
        txtEndDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                FlurryAgent
                        .logEvent("ConfirmedFragment - End Date Field Clicked");
                Util.showCalender(txtEndDate, getActivity(),
                        new DateResponse() {

                            @Override
                            public void dateResponseListner(Date date) {
                                endDate = date;
                            }
                        });
            }
        });
        Util.filterClear(txtEndDate,
                getActivity().findViewById(R.id.img_end_date_clear_filter));
        getActivity().findViewById(R.id.btn_search).setOnClickListener(this);
        Util.setupUI(getActivity(), getActivity().findViewById(R.id.rl_history));
        updateCount();
        listPb.setVisibility(View.VISIBLE);
        DashboardActivity.calls.getHistoryList(1, "", new OnResponse() {

            @Override
            public void onResponseListner(String api) {
                updateList();
                listPb.setVisibility(View.GONE);
            }
        }, pat, startDate, endDate);

        listPatients.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                nextUrl = "" + totalItemCount / 50 + 1;
                if (firstVisibleItem % 50 == 15
                        && firstVisibleItem > totalItemCount - 50)
                    if (totalCount > totalItemCount && !prevUrl.equals(nextUrl)) {
                        listPb.setVisibility(View.VISIBLE);
                        DashboardActivity.calls.getHistoryList(
                                totalItemCount / 50 + 1, condVal,
                                new OnResponse() {

                                    @Override
                                    public void onResponseListner(String api) {
                                        updateList();
                                        listPb.setVisibility(View.GONE);
                                    }
                                }, pat, startDate, endDate);
                        prevUrl = "" + totalItemCount / 50 + 1;
                    }

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
                listPb.setVisibility(View.VISIBLE);
                if (Util.isEmptyString(txtStartDate.getText().toString()))
                    startDate = null;
                if (Util.isEmptyString(txtEndDate.getText().toString()))
                    endDate = null;
                if ((startDate != null) && (endDate != null)
                        && startDate.after(endDate)) {
                    Util.Alertdialog(getActivity(),
                            "End date is before start date, please try again.");

                } else {
                    DashboardActivity.calls.getHistoryList(1, "", new OnResponse() {

                        @Override
                        public void onResponseListner(String api) {
                            arrAppointment.clear();
                            arrAppointment
                                    .addAll(DashboardActivity.arrHistoryPatients);
                            updateList();
                            listPb.setVisibility(View.GONE);
                        }
                    }, pat, startDate, endDate);
                }

                break;
        }

    }

    @Override
    public void onResume() {
        adapter.notifyDataSetChanged();
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
        try {
            txtCount = (TextView) getActivity().findViewById(R.id.txt_count);
            txtCount.setText(Html.fromHtml("<b>" + "Total Results: "
                    + HistoryFragment.totalCount + "</b>"));
        } catch (Exception e) {

        }
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

    public void updateList() {
        adapter.notifyItems();
        adapter.notifyDataSetChanged();
        adapter.getFilter().filter(editFilter.getText());
        updateCount();
    }

    @Override
    public void onStop() {
        if (isRemoving())
            editFilter.setText("");
        super.onStop();
    }

    @Override
    public void onListItemClick(boolean selected) {
        // show / hide action buttons
        if (selected == true) {
            mButtonsBar.setVisibility(View.VISIBLE);
        } else {
            mButtonsBar.setVisibility(View.GONE);
        }
    }

}

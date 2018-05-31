package com.ezhealthtrack.fragments;

import android.annotation.SuppressLint;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.adapter.VisitNotesAdapter;
import com.ezhealthtrack.controller.MedicalRecordsController;
import com.ezhealthtrack.greendao.MedRecVisitNotes;
import com.ezhealthtrack.greendao.MedRecVisitNotesDao.Properties;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.EndlessScrollListener;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.NetworkCallController.OnResponse;
import com.ezhealthtrack.util.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.greenrobot.dao.query.QueryBuilder;

public class VisitNotesFragment extends Fragment implements OnClickListener {
    private ListView listVistNotes;
    private VisitNotesAdapter adapter;
    private TextView txtStartDate;
    private TextView txtEndDate;
    private Date startDate;
    private Date endDate;
    private EditText editFilter;
    private TextView txtCount;
    public static int totalCount = 0;
    private ProgressBar listPb;

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_visit_notes, container,
                false);

        TextView title = (TextView) view.findViewById(R.id.txt_screen_title);
        title.setText("Medical Records - Visit Notes");
        if (EzUtils.getDeviceSize(getActivity()) == EzUtils.EZ_SCREEN_SMALL) {
            title.setVisibility(View.GONE);
        }

        TextView search = (TextView) view.findViewById(R.id.edit_name);
        search.setHint("Patient Name | Patient ID");

        view.findViewById(R.id.edit_name_view).setVisibility(View.VISIBLE);
        view.findViewById(R.id.start_date_view).setVisibility(View.VISIBLE);
        view.findViewById(R.id.end_date_view).setVisibility(View.VISIBLE);
        view.findViewById(R.id.edit_date_view).setVisibility(View.GONE);
        view.findViewById(R.id.btn_search).setVisibility(View.VISIBLE);
        view.findViewById(R.id.line_2).setVisibility(View.VISIBLE);
        return view;
    }

    @SuppressLint("InflateParams")
    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new VisitNotesAdapter(getActivity());
        listVistNotes = (ListView) getActivity().findViewById(
                R.id.list_patients);
        View listFooter = ((LayoutInflater) getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.list_progressbar, null);
        listVistNotes.addFooterView(listFooter);
        listPb = (ProgressBar) listFooter.findViewById(R.id.list_progressbar);
        listVistNotes.setAdapter(adapter);

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

            }
        });

        Util.filterClear(editFilter,
                getActivity().findViewById(R.id.img_clear_filter));

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
                getActivity().findViewById(R.id.rl_visit_notes));
        updateCount();

    }

    private void setScrollListner() {
        listVistNotes.setOnScrollListener(new EndlessScrollListener(4, 0) {

            @Override
            public void onLoadMore(final int page, int totalItemsCount) {
                Log.i("", "" + page);
                if (Util.isEmptyString(txtStartDate.getText().toString()))
                    startDate = null;
                if (Util.isEmptyString(txtEndDate.getText().toString()))
                    endDate = null;
                listPb.setVisibility(View.VISIBLE);
                MedicalRecordsController.getVisitNotesList(getActivity(), page,
                        editFilter.getText().toString(), startDate, endDate,
                        new OnResponse() {

                            @Override
                            public void onResponseListner(String response) {
                                totalCount = Integer.parseInt(response);
                                updateCount();
                                adapter.replaceLazyList(getQuery().limit(
                                        15 * page).listLazy());
                                listPb.setVisibility(View.GONE);
                            }
                        });

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
                DashboardActivity.filterText = editFilter.getText().toString();
                if (Util.isEmptyString(txtStartDate.getText().toString()))
                    startDate = null;
                if (Util.isEmptyString(txtEndDate.getText().toString()))
                    endDate = null;

                listPb.setVisibility(View.VISIBLE);
                MedicalRecordsController.getVisitNotesList(getActivity(), 1,
                        editFilter.getText().toString(), startDate, endDate,
                        new OnResponse() {

                            @Override
                            public void onResponseListner(String response) {
                                totalCount = Integer.parseInt(response);
                                updateCount();
                                adapter.replaceLazyList(getQuery().limit(15)
                                        .listLazy());
                                listPb.setVisibility(View.GONE);
                                setScrollListner();
                            }
                        });
                break;
        }

    }

    @Override
    public void onResume() {
        adapter.notifyDataSetChanged();
        editFilter.setText(DashboardActivity.filterText);
        listPb.setVisibility(View.VISIBLE);
        MedicalRecordsController.getVisitNotesList(getActivity(), 1, editFilter
                .getText().toString(), null, null, new OnResponse() {

            @Override
            public void onResponseListner(String response) {
                totalCount = Integer.parseInt(response);
                updateCount();
                adapter.replaceLazyList(getQuery().limit(15).listLazy());
                listPb.setVisibility(View.GONE);
                setScrollListner();
            }
        });
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
                    + VisitNotesFragment.totalCount + "</b>"));
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
                                endDate.setTime(endDate.getTime());
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

    private QueryBuilder<MedRecVisitNotes> getQuery() {
        QueryBuilder<MedRecVisitNotes> qb = EzApp.medRecVisitNotesDao
                .queryBuilder();
        if (startDate != null)
            qb.where(Properties.Apt_date.ge(startDate));
        if (endDate != null)
            qb.where(Properties.Apt_date.le(endDate));
        String s = editFilter.getText().toString().replace("  ", " ");
        qb.whereOr(Properties.Pat_detail.like("%" + s + "%"),
                Properties.Pat_unique_id.like("%" + s + "%"));
        qb.orderDesc(Properties.Last_visit);
        return qb;
    }

}

package com.ezhealthtrack.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.adapter.PatientsListAdapter;
import com.ezhealthtrack.controller.PatientsListController;
import com.ezhealthtrack.greendao.Patient;
import com.ezhealthtrack.greendao.PatientDao.Properties;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.Util;

import org.json.JSONObject;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

public class PatientsListFragment extends EzGridFragment implements
        OnClickListener {

    private TextView mCountView;
    private AutoCompleteTextView mEditFilter;
    public static String nextUrl;

    private OnCallback onCallback;

    public PatientsListFragment() {
        super(false);
    }

    @Override
    public void onCmdResponse(JSONObject response, String result) {
        String count = response.optString("totalItemCount");
        if (count.isEmpty()) {
            mCountView.setVisibility(View.GONE);
            return;
        }
        mCountView
                .setText(Html.fromHtml("<b>Total Results: " + count + "</b>"));

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mController = new PatientsListController(0);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_patients_list,
                container, false);
        super.onCreateListView(view, inflater);

        mGridAdapter = new PatientsListAdapter(mDataList, this, onCallback);
        mGridView.setAdapter(mGridAdapter);

        TextView title = (TextView) view.findViewById(R.id.txt_screen_title);
        title.setText("Patients");
        if (EzUtils.getDeviceSize(getActivity()) == EzUtils.EZ_SCREEN_SMALL) {
            title.setVisibility(View.GONE);
        }

        view.findViewById(R.id.edit_name_view).setVisibility(View.VISIBLE);
        view.findViewById(R.id.start_date_view).setVisibility(View.GONE);
        view.findViewById(R.id.end_date_view).setVisibility(View.GONE);
        view.findViewById(R.id.edit_date_view).setVisibility(View.GONE);
        view.findViewById(R.id.line_2).setVisibility(View.GONE);
        view.findViewById(R.id.btn_search).setVisibility(View.VISIBLE);

        // search button
        view.findViewById(R.id.btn_search).setOnClickListener(this);

        // unread/total item count
        mCountView = (TextView) view.findViewById(R.id.txt_count);

        // filter text
        mEditFilter = (AutoCompleteTextView) view.findViewById(R.id.edit_name);
        mEditFilter
                .setHint("Patient Name | ID | Address | Contact Number | Email");

        mEditFilter.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(final Editable arg0) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(final CharSequence cs, final int arg1,
                                      final int arg2, final int arg3) {
                // When user changed the Text
                Util.filterClear(mEditFilter,
                        getActivity().findViewById(R.id.img_clear_filter));
            }
        });

        // setup UI
        Util.setupUI(getActivity(), view.findViewById(R.id.rl_patients));

        // Get initial data
        mController.getPage(0, null, this);
        return view;
    }

    public interface OnCallback {
        public void onCallback(String id, String name);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        // mGridAdapter.notifyDataSetChanged();
        updateCount();
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        // EzUtils.hideKeyBoard(getActivity());
        super.onStart();
    }

    public void updateCount() {
        try {
            List<Patient> qb = getQuery();
            mCountView = (TextView) getActivity().findViewById(R.id.txt_count);
            mCountView.setText(Html.fromHtml("<b>Total Results: " + qb.size()
                    + "</b>"));
        } catch (Exception e) {

        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        try {
            onCallback = (OnCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnArticleSelectedListener");
        }
        super.onAttach(activity);
    }

    @Override
    public void onStop() {
        Log.i("LOF", "onStop(): Size: " + mDataList.size());
        // save records for later use
        if (isRemoving())
            mEditFilter.setText("");
        Log.i("LOF", "onStop(): Size: " + mDataList.size());
        super.onStop();
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {

            case R.id.btn_search:
                mDataList.clear();
                List<Patient> qb = getQuery();
                mDataList.addAll(qb);
                mGridAdapter.notifyDataSetChanged();
                updateCount();
                break;

        }
    }

    private List<Patient> getQuery() {
        String cs = mEditFilter.getText().toString();
        QueryBuilder<Patient> qb = EzApp.patientDao.queryBuilder();
        qb.whereOr(Properties.P_detail.like("%" + cs + "%"),
                Properties.Padd1.like("%" + cs + "%"),
                Properties.Padd2.like("%" + cs + "%"),
                Properties.Pmobnum.like("%" + cs + "%"),
                Properties.Pcity.like("%" + cs + "%"),
                Properties.Pstate.like("%" + cs + "%"),
                Properties.Pcountry.like("%" + cs + "%"),
                Properties.Parea.like("%" + cs + "%"),
                Properties.Display_id.like("%" + cs + "%"),
                Properties.Pemail.like("%" + cs + "%"))
                .orderDesc(Properties.Id);
        return qb.list();
    }
}

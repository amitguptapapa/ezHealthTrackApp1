package com.ezhealthtrack.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.EzFragment;
import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.controller.AutoSuggestController;
import com.ezhealthtrack.controller.AutoSuggestController.OnAutoSuggest;
import com.ezhealthtrack.controller.MedicalRecordsController;
import com.ezhealthtrack.greendao.MedRecAllergy;
import com.ezhealthtrack.greendao.MedRecAllergyDao.Properties;
import com.ezhealthtrack.model.PatientAutoSuggest;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.EndlessScrollListener;
import com.ezhealthtrack.util.NetworkCallController.OnResponse;
import com.ezhealthtrack.util.Util;
import com.flurry.android.FlurryAgent;

import java.util.ArrayList;

import de.greenrobot.dao.query.QueryBuilder;

@SuppressLint("InflateParams")
public class MrListFragment extends EzFragment {

    protected String mRecordsType;
    protected String mHeader;

    protected int mTotalCount;
    protected TextView mTxtCount;

    protected ListView mListView;
    protected MrListAdapter mListAdapter;
    protected RelativeLayout mProgressBar;

    protected AutoCompleteTextView mTxtFilter;
    protected PatientAutoSuggest mAutoSuggest;
    ArrayAdapter<PatientAutoSuggest> mFilterAdapter;

    public MrListFragment() {
        Log.v("MrLF:MrListFragment()", "CONSTRUCTOR !!!");
    }

    public void setFragmentType(String type) {
        mRecordsType = type;
        if (mRecordsType.equals(MedicalRecordsController.ALLERGY_TYPE))
            mHeader = "Medical Records - Allergies";
        else if (mRecordsType.equals(MedicalRecordsController.VITALS_TYPE))
            mHeader = "Medical Records - Vitals";
        else if (mRecordsType
                .equals(MedicalRecordsController.PRESCRIPTION_TYPE))
            mHeader = "Medical Records - Prescriptions";
        else if (mRecordsType.equals(MedicalRecordsController.RADIOLOGY_TYPE))
            mHeader = "Medical Records - Radiology";
        else if (mRecordsType.equals(MedicalRecordsController.LAB_TYPE))
            mHeader = "Medical Records - Labs";
        else if (mRecordsType.equals(MedicalRecordsController.ECG_TYPE))
            mHeader = "Medical Records - EKG/ECG";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("MrLF:onCreate()", "Type: " + mRecordsType);

        mTotalCount = 0;
        mAutoSuggest = new PatientAutoSuggest();
        mListAdapter = new MrListAdapter(getActivity(), mRecordsType);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mr_list, container,
                false);
        this.setHeader(view);

        mProgressBar = (RelativeLayout) inflater.inflate(
                R.layout.ezlist_progressbar, null);
        mProgressBar.setVisibility(View.GONE);

        mListView = (ListView) view.findViewById(R.id.ezlist);
        mListView.addFooterView(mProgressBar);
        mListView.setAdapter(mListAdapter);

        mTxtCount = (TextView) view.findViewById(R.id.txt_count);
        mFilterAdapter = new ArrayAdapter<PatientAutoSuggest>(getActivity(),
                android.R.layout.simple_dropdown_item_1line);
        mTxtFilter = (AutoCompleteTextView) view.findViewById(R.id.edit_name);
        mTxtFilter.setHint("Patient Name | Patient ID");
        mTxtFilter.setAdapter(mFilterAdapter);

        Log.i(DashboardActivity.filterText, "on activity created called");
        mTxtFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int arg1,
                                          final int arg2, final int arg3) {
                onSearchTextChanged(s.toString());
            }

            @Override
            public void onTextChanged(final CharSequence cs, final int arg1,
                                      final int arg2, final int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        Util.filterClear(mTxtFilter, view.findViewById(R.id.img_clear_filter));
        // getActivity().findViewById(R.id.btn_search).setOnClickListener(this);
        Util.setupUI(getActivity(), view.findViewById(R.id.rl_mr_fragment));

        view.findViewById(R.id.btn_search).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        FlurryAgent.logEvent("EzPatientListFragment - Search");
                        onLoadRecords(1);
                    }
                });

        // start loading records
        if (mTotalCount == 0) {
            onLoadRecords(1);
            setScrollListner();
        }
        return view;
    }

    private void setHeader(View view) {
        TextView title = (TextView) view.findViewById(R.id.txt_screen_title);
        title.setText(mHeader);
        if (EzUtils.getDeviceSize(getActivity()) == EzUtils.EZ_SCREEN_SMALL) {
            title.setVisibility(View.GONE);
        }

        view.findViewById(R.id.edit_name_view).setVisibility(View.VISIBLE);
        view.findViewById(R.id.start_date_view).setVisibility(View.GONE);
        view.findViewById(R.id.end_date_view).setVisibility(View.GONE);
        view.findViewById(R.id.edit_date_view).setVisibility(View.GONE);
        view.findViewById(R.id.btn_search).setVisibility(View.VISIBLE);
        view.findViewById(R.id.line_2).setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    // user is tying search text
    public void onSearchTextChanged(final String cs) {
        AutoSuggestController.autoSuggestPatient(cs.toString(), getActivity(),
                new OnAutoSuggest() {
                    @Override
                    public void OnAutoSuggestListner(Object list) {
                        @SuppressWarnings("unchecked")
                        ArrayList<PatientAutoSuggest> arrPat = (ArrayList<PatientAutoSuggest>) list;
                        mFilterAdapter.clear();
                        mFilterAdapter.addAll(arrPat);
                    }
                });
    }

    private void setScrollListner() {
        mListView.setOnScrollListener(new EndlessScrollListener(4, 0) {

            @Override
            public void onLoadMore(final int page, int totalItemsCount) {
                onLoadRecords(page);
            }
        });
    }

    // load records from database / network
    public void onLoadRecords(final int page) {
        Log.v("MrLF:onLoadRecords()", "Load more..");

        mProgressBar.setVisibility(View.VISIBLE);
        DashboardActivity.filterText = mTxtFilter.getText().toString();
        MedicalRecordsController.getAllergyList(mRecordsType, getActivity(),
                page, mTxtFilter.getText().toString(), new OnResponse() {

                    @Override
                    public void onResponseListner(String response) {
                        if (response.equals("error")) {
                            mProgressBar.setVisibility(View.GONE);
                            EzUtils.showLong("Netwrok problem");
                            return;
                        }
                        mTotalCount = Integer.parseInt(response);
                        updateCount();
                        mListAdapter.replaceLazyList(getQuery()
                                .limit(15 * page).listLazy());
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
    }

    public void updateCount() {
        if (mTxtCount == null)
            return;
        mTxtCount.setText("Total Records: " + mTotalCount);
    }

    @Override
    public void onResume() {
        mTxtFilter.setText(DashboardActivity.filterText);
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private QueryBuilder<MedRecAllergy> getQuery() {
        QueryBuilder<MedRecAllergy> qb = EzApp.medRecAllergyDao
                .queryBuilder();

        String s = mTxtFilter.getText().toString().replace("  ", " ");
        qb.whereOr(Properties.Pat_detail.like("%" + s + "%"),
                Properties.Pat_unique_id.like("%" + s + "%"));
        qb.orderDesc(Properties.Last_visit);
        return qb;
    }

}

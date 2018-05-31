package com.ezhealthtrack.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.LabOrderDetailsActivity;
import com.ezhealthtrack.adapter.LabOrdersAdapter;
import com.ezhealthtrack.controller.AutoSuggestController;
import com.ezhealthtrack.controller.AutoSuggestController.OnAutoSuggest;
import com.ezhealthtrack.controller.LabOrderController;
import com.ezhealthtrack.model.LabOrder;
import com.ezhealthtrack.model.PatientAutoSuggest;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.Util;
import com.ezhealthtrack.util.Util.DateResponse;
import com.flurry.android.FlurryAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class LabOrdersFragment extends EzListFragment implements
        OnClickListener {

    private Spinner spinnerOrderStatus;
    private ArrayList<String> arrOrderStatus = new ArrayList<String>();

    private TextView mtvStartDate;
    private TextView mtvEndDate;
    private Date mStartDate;
    private Date mEndDate;
    private AutoCompleteTextView mEditFilter;
    public static String condVal = "";
    public static String nextUrl;
    public static String prevUrl = "";
    TextView mtvCount;

    private PatientAutoSuggest selectedPatient;

    // protected List<Object> mDataList;
    // protected EzListFragmentAdaptor mListAdapter;
    // private LabOrdersAdapter mListAdapter;
    // private final ArrayList<Appointment> mDataList = new
    // ArrayList<Appointment>();

    public LabOrdersFragment() {
        super(false); // offline not required
    }

    public void onCmdResponse(JSONObject response, String result) {
        // update UI if required

        String count = response.optString("total_count");
        if (count.isEmpty()) {
            mtvCount.setVisibility(View.GONE);
            return;
        }
        mtvCount.setText(Html.fromHtml("<b>Total Results: " + count + "</b>"));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("LOF", "onCreate()");
        mController = new LabOrderController(0);

        arrOrderStatus.clear();
        arrOrderStatus.add("Select Order Status");
        arrOrderStatus.add("Pending");
        arrOrderStatus.add("New");
        arrOrderStatus.add("Cancel");
        arrOrderStatus.add("Sampling");
        arrOrderStatus.add("Waiting");
        arrOrderStatus.add("Partially Complete");
        arrOrderStatus.add("Complete");
        arrOrderStatus.add("Re-Order");
        arrOrderStatus.add("Approved");
        arrOrderStatus.add("Rejected");
        arrOrderStatus.add("Appointment Pending");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {

        Log.i("LOF", "onCreateView()");

        View view = inflater.inflate(R.layout.fragment_lab_orders, container,
                false);
        super.onCreateListView(view, inflater);

        TextView title = (TextView) view.findViewById(R.id.txt_screen_title);
        title.setText("Lab Orders");
        if (EzUtils.getDeviceSize(getActivity()) == EzUtils.EZ_SCREEN_SMALL) {
            title.setVisibility(View.GONE);
        }

        view.findViewById(R.id.id_header_spinner).setVisibility(View.VISIBLE);
        view.findViewById(R.id.edit_name_view).setVisibility(View.VISIBLE);
        view.findViewById(R.id.start_date_view).setVisibility(View.VISIBLE);
        view.findViewById(R.id.end_date_view).setVisibility(View.VISIBLE);
        view.findViewById(R.id.edit_date_view).setVisibility(View.GONE);
        view.findViewById(R.id.btn_search).setVisibility(View.VISIBLE);
        view.findViewById(R.id.line_2).setVisibility(View.VISIBLE);

        spinnerOrderStatus = (Spinner) view
                .findViewById(R.id.id_header_spinner);
        final ArrayAdapter<String> adapterOrderStatus = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item,
                arrOrderStatus);
        adapterOrderStatus
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrderStatus.setAdapter(adapterOrderStatus);

        mListAdapter = new LabOrdersAdapter(mDataList, this);
        mListView.setAdapter(mListAdapter);

        // filter text
        mEditFilter = (AutoCompleteTextView) view.findViewById(R.id.edit_name);
        mEditFilter.setHint("Patient Name");

        final ArrayAdapter<PatientAutoSuggest> adapterPatient = new ArrayAdapter<PatientAutoSuggest>(
                getActivity(), android.R.layout.simple_dropdown_item_1line);
        mEditFilter.setAdapter(adapterPatient);
        mEditFilter.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(final Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                AutoSuggestController.autoSuggestPatient(s.toString(),
                        getActivity(), new OnAutoSuggest() {

                            @Override
                            public void OnAutoSuggestListner(Object list) {
                                ArrayList<PatientAutoSuggest> arrPat = (ArrayList<PatientAutoSuggest>) list;
                                adapterPatient.clear();
                                adapterPatient.addAll(arrPat);
                            }
                        });
            }

            @Override
            public void onTextChanged(final CharSequence cs, final int arg1,
                                      final int arg2, final int arg3) {
                // When user changed the Text
                Util.filterClear(mEditFilter,
                        getActivity().findViewById(R.id.img_clear_filter));
            }
        });
        mEditFilter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                selectedPatient = adapterPatient.getItem(position);
            }
        });

        // start date
        mtvStartDate = (TextView) view.findViewById(R.id.txt_start_date);
        mtvStartDate.setOnClickListener(this);
        Util.filterClear(mtvStartDate,
                view.findViewById(R.id.img_start_date_clear_filter));

        // end date
        mtvEndDate = (TextView) view.findViewById(R.id.txt_end_date);
        mtvEndDate.setOnClickListener(this);
        Util.filterClear(mtvEndDate,
                view.findViewById(R.id.img_end_date_clear_filter));

        // search button
        view.findViewById(R.id.btn_search).setOnClickListener(this);

        // total item count
        mtvCount = ((TextView) view.findViewById(R.id.txt_count));

        // setup UI
        Util.setupUI(getActivity(), view.findViewById(R.id.rl_lab_orders));

        // get initial data
        mController.getPage(1, null, this);
        return view;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("LOF", "onActivityCreated()");
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.txt_start_date:
                FlurryAgent
                        .logEvent("ConfirmedFragment - Start Date Field Clicked");
                Util.showCalender(mtvStartDate, getActivity(), new DateResponse() {

                    @Override
                    public void dateResponseListner(Date date) {
                        mStartDate = date;
                    }
                });
                break;

            case R.id.txt_end_date:
                FlurryAgent.logEvent("ConfirmedFragment - End Date Field Clicked");
                Util.showCalender(mtvEndDate, getActivity(), new DateResponse() {

                    @Override
                    public void dateResponseListner(Date date) {
                        mEndDate = date;
                    }
                });
                break;

            case R.id.btn_search:
                // clear previous results and data
                this.initDataList();
                mListAdapter.resetLastViewedToZero();
                mListAdapter.notifyDataSetChanged();
                this.setInProgressActivity();

                mUrlParams.remove("from_date");
                mUrlParams.remove("to_date");
                mUrlParams.remove("order_status");
                mUrlParams.remove("search");
                mUrlParams.remove("patient_id");

                int status = spinnerOrderStatus.getSelectedItemPosition() - 1;
                switch (status) {
                    case 8:
                        status = 9;
                        break;
                    case 9:
                        status = 10;
                        break;
                    case 10:
                        status = 12;
                        break;
                }
                if (status >= 0) {
                    mUrlParams.put("order_status", Integer.toString(status));
                }

                // Search
                String search = mEditFilter.getText().toString();
                if (selectedPatient != null
                        && selectedPatient.getName().equals(search)) {
                    mUrlParams.put("patient_id", selectedPatient.getId());
                } else if (!Util.isEmptyString(search)) {
                    mUrlParams.put("search", search);
                }

                if (Util.isEmptyString(mtvStartDate.getText().toString()))
                    mStartDate = null;
                if (Util.isEmptyString(mtvEndDate.getText().toString()))
                    mEndDate = null;
                if ((mStartDate != null) && (mEndDate != null)
                        && mStartDate.after(mEndDate)) {
                    Util.Alertdialog(getActivity(),
                            "End date is before start date, please try again.");
                    return;
                }

                // TODO: set parameters and get data
                if (mStartDate != null) {
                    mUrlParams.put("from_date",
                            EzApp.sdfddMmyy1.format(mStartDate));
                } else {
                    mUrlParams.remove("from_date");
                }
                if (mEndDate != null) {
                    mUrlParams.put("to_date",
                            EzApp.sdfddMmyy1.format(mEndDate));
                } else {
                    mUrlParams.remove("to_date");
                }
                mCurrentPage = 1;
                mController.getPage(mCurrentPage, mUrlParams, this);
                this.setInProgressActivity();
                break;
        }
    }

    @Override
    public void onListItemClick(ListView list, View view, int position, long id) {
        super.onListItemClick(list, view, position, id);

        if (position < 0 || position >= mDataList.size())
            return;

        Log.i("onListItemClick", "Position=" + position);
        LabOrder order = (LabOrder) mDataList.get(position);

        Intent intent;
        intent = new Intent(this.getActivity(), LabOrderDetailsActivity.class);
        intent.putExtra("id", order.getId());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        Log.i("LOF", "onResume(): Size: " + mDataList.size());
        mListAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        Log.i("LOF", "onStart(): Size: " + mDataList.size());
        super.onStart();
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

    // public void updateCount() {
    // try {
    // txtCount = (TextView) getActivity().findViewById(
    // R.id.txt_count_history);
    // txtCount.setText(Html.fromHtml("<b>" + "Total Results: "
    // + mListAdapter.getCount() + "</b>"));
    // } catch (Exception e) {
    //
    // }
    // }

}

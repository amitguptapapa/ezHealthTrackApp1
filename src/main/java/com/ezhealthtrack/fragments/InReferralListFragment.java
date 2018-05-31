package com.ezhealthtrack.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezhealthtrack.R;
import com.ezhealthtrack.adapter.InReferralListAdapter;
import com.ezhealthtrack.controller.InReferralListController;
import com.ezhealthtrack.fragments.PatientsListFragment.OnCallback;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.Util;

import org.json.JSONObject;

public class InReferralListFragment extends EzGridFragment implements
        OnClickListener {

    private TextView mCountView;
    private AutoCompleteTextView mEditFilter;
    private OnCallback onCallback;

    public InReferralListFragment() {
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
        mController = new InReferralListController(1);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_inreferral,
                container, false);
        super.onCreateListView(view, inflater);

        mGridAdapter = new InReferralListAdapter(mDataList, this, onCallback);
        mGridView.setAdapter(mGridAdapter);

        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.id_header);
        View line = (View) view.findViewById(R.id.line_1);
        TextView title = (TextView) view.findViewById(R.id.txt_screen_title);
        title.setText("In-Referrals");
        if (EzUtils.getDeviceSize(getActivity()) == EzUtils.EZ_SCREEN_SMALL) {
            relativeLayout.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        }

        view.findViewById(R.id.edit_name_view).setVisibility(View.GONE);
        view.findViewById(R.id.start_date_view).setVisibility(View.GONE);
        view.findViewById(R.id.end_date_view).setVisibility(View.GONE);
        view.findViewById(R.id.edit_date_view).setVisibility(View.GONE);
        view.findViewById(R.id.btn_search).setVisibility(View.GONE);
        view.findViewById(R.id.line_2).setVisibility(View.GONE);

        // unread/total item count
        mCountView = (TextView) view.findViewById(R.id.txt_count);

        // filter text | Temporarily disabled | start{

        mEditFilter = (AutoCompleteTextView) view.findViewById(R.id.edit_name);
        mEditFilter.setHint("Search by Doctor");

        mEditFilter.addTextChangedListener(new TextWatcher() {

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
                // mGridAdapter.getFilter()
                // .filter(editFilter.getText().toString());
                Util.filterClear(mEditFilter,
                        getActivity().findViewById(R.id.img_clear_filter));

            }
        });

        // }end

        // setup UI
        Util.setupUI(getActivity(), view.findViewById(R.id.rl_inreferral));

        // Get initial data
        mController.getPage(1, null, this);

        return view;
    }

    // @Override
    // public void onStart() {
    // super.onStart();
    // }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mGridAdapter.notifyDataSetChanged();
    }

    public void notifyList() {

        // mGridAdapter.getFilter().filter(editFilter.getText().toString());

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

    }
}

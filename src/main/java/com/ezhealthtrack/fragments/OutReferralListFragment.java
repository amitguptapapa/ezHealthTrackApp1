package com.ezhealthtrack.fragments;

import android.database.DataSetObserver;
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
import com.ezhealthtrack.adapter.OutReferralListAdapter;
import com.ezhealthtrack.controller.OutReferralListController;
import com.ezhealthtrack.fragments.PatientsListFragment.OnCallback;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.Util;

import org.json.JSONObject;

public class OutReferralListFragment extends EzGridFragment implements
        OnClickListener {

    private AutoCompleteTextView mEditFilter;
    private TextView mCountView;

    private OnCallback onCallback;

    public OutReferralListFragment() {
        super(false);
        // TODO Auto-generated constructor stub
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
        mController = new OutReferralListController(0);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_outreferral, container,
                false);
        super.onCreateListView(view, inflater);

        mGridAdapter = new OutReferralListAdapter(mDataList, this, onCallback);
        mGridView.setAdapter(mGridAdapter);

        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.id_header);
        View line = (View) view.findViewById(R.id.line_1);
        TextView title = (TextView) view.findViewById(R.id.txt_screen_title);
        title.setText("Out-Referrals");
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
                Util.filterClear(mEditFilter,
                        getActivity().findViewById(R.id.img_clear_filter));
                // updateCount();
            }
        });

        // }end

        Util.setupUI(getActivity(), view.findViewById(R.id.rl_outreferral));

        // Get initial data
        mController.getPage(1, null, this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        // updateCount();
        super.onResume();
    }

    public void updateList() {
        // if (isVisible())
        // mGridAdapter.getFilter().filter(editFilter.getText().toString());
        // updateCount();
    }

    public void updateCount() {
        mGridAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                try {
                    TextView txtCount = (TextView) getActivity().findViewById(
                            R.id.txt_count);
                    txtCount.setText(Html.fromHtml("<b>Total Results: "
                            + mGridAdapter.getCount() + "</b>"));
                    super.onChanged();
                } catch (Exception e) {

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

}

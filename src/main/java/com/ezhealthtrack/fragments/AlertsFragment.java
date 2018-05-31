package com.ezhealthtrack.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.adapter.AlertAdapter;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.Util;

public class AlertsFragment extends Fragment {
    private ListView listPatients;
    private AlertAdapter adapter;
    private EditText editFilter;
    private TextView txtCount;
    public static int totalCount = 0;
    public static int unreadCount = 0;

    public void notifyList() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new AlertAdapter(getActivity(), R.layout.row_inbox,
                DashboardActivity.arrAlerts);
        listPatients = (ListView) getActivity()
                .findViewById(R.id.list_patients);
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
        Util.setupUI(getActivity(), getActivity().findViewById(R.id.rl_alerts));
        updateCount();
    }

    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alerts, container,
                false);

        TextView txtHeader = (TextView) view.findViewById(R.id.txt_header_hospital);

        if (EzUtils.getDeviceSize(getActivity()) == EzUtils.EZ_SCREEN_SMALL) {
            txtHeader.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onResume() {
        updateCount();
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
        txtCount = (TextView) getActivity().findViewById(
                R.id.txt_count_messages);
        txtCount.setText(Html.fromHtml("<b>" + AlertsFragment.unreadCount
                + " New messages, " + AlertsFragment.totalCount
                + " Total messages" + "</b>"));
    }
}

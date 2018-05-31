package com.ezhealthtrack.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.controller.DataController;
import com.ezhealthtrack.model.DashboardModel;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.Log;

public class DashboardFragment extends Fragment implements OnClickListener {
    public static DashboardModel model = new DashboardModel();

    final static private String CARD_TITLE_1 = "Appointments & Messages";
    final static private String CARD_TITLE_2 = "Medical Research";
    final static private String CARD_TITLE_3 = "Prescriptions";
    final static private String CARD_TITLE_4 = "ezHealthTrack Updates";

    private TextView mCard1Details;
    private TextView mCard2Details;
    private TextView mCard3Details;
    private TextView mCard4Details;

    // private TextView txtAptMessage;
    // private TextView txtMedicine;
    // private TextView txtPrescription;
    // private TextView txtInfo;

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // txtAptMessage = (TextView) getActivity().findViewById(
        // R.id.txt_message_apt);
        // txtMedicine = (TextView)
        // getActivity().findViewById(R.id.txt_medicine);
        // txtPrescription = (TextView) getActivity().findViewById(
        // R.id.txt_prescriptions);
        // txtInfo = (TextView) getActivity().findViewById(R.id.txt_info);
        updateData();
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {

        }

    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container,
                false);

        TextView txtHeader = (TextView) view.findViewById(R.id.txt_header);
        View line = (View) view.findViewById(R.id.line);

        if (EzUtils.getDeviceSize(getActivity()) == EzUtils.EZ_SCREEN_SMALL) {
            txtHeader.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        }

        LinearLayout card1View = (LinearLayout) view.findViewById(R.id.card1);
        card1View.removeAllViews();
        LinearLayout card2View = (LinearLayout) view.findViewById(R.id.card2);
        card2View.removeAllViews();
        LinearLayout card3View = (LinearLayout) view.findViewById(R.id.card3);
        card3View.removeAllViews();
        LinearLayout card4View = (LinearLayout) view.findViewById(R.id.card4);
        card4View.removeAllViews();

        // CARD 1

        View card1 = LayoutInflater.from(getActivity()).inflate(
                R.layout.fragment_dashboard_card, null);
        card1View.addView(card1);

        TextView card1Title = (TextView) card1.findViewById(R.id.txt_title);
        card1Title.setText(CARD_TITLE_1);

        mCard1Details = (TextView) card1.findViewById(R.id.txt_details);

        // CARD 2
        View card2 = LayoutInflater.from(getActivity()).inflate(
                R.layout.fragment_dashboard_card, null);
        card2View.addView(card2);

        TextView card2Title = (TextView) card2.findViewById(R.id.txt_title);
        card2Title.setText(CARD_TITLE_2);

        mCard2Details = (TextView) card2.findViewById(R.id.txt_details);

        // CARD 3
        View card3 = LayoutInflater.from(getActivity()).inflate(
                R.layout.fragment_dashboard_card, null);
        card3View.addView(card3);

        TextView card3Title = (TextView) card3.findViewById(R.id.txt_title);
        mCard3Details = (TextView) card3.findViewById(R.id.txt_details);

        card3Title.setText(CARD_TITLE_3);

        // CARD 4
        View card4 = LayoutInflater.from(getActivity()).inflate(
                R.layout.fragment_dashboard_card, null);
        card4View.addView(card4);

        TextView card4Title = (TextView) card4.findViewById(R.id.txt_title);
        mCard4Details = (TextView) card4.findViewById(R.id.txt_details);

        card4Title.setText(CARD_TITLE_4);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v("DF::onStart()", "...");
        DataController.getDashboard((DashboardActivity) getActivity());
    }

    public void updateData() {

        boolean first = false;

        // CARD 1
        mCard1Details.setText("");
        first = true;
        for (final String s : DashboardFragment.model
                .getAppointments_Messages()) {
            if (!first)
                mCard1Details.append("\n");
            first = false;
            mCard1Details.append(s);
        }

        // CARD 2
        mCard2Details.setText("");
        first = true;
        for (final String s : DashboardFragment.model.getMedicine_Research()) {
            if (!first)
                mCard2Details.append("\n");
            first = false;
            mCard2Details.append(s);
        }

        // CARD 3
        mCard3Details.setText("");
        first = true;
        for (final String s : DashboardFragment.model.getPrescriptions()) {
            if (!first)
                mCard3Details.append("\n");
            first = false;
            mCard3Details.append(s);
        }

        // CARD 4
        mCard4Details.setText("");
        first = true;
        for (final String s : DashboardFragment.model
                .getEzHealthTrack_Information()) {
            if (!first)
                mCard4Details.append("\n");
            first = false;
            mCard4Details.append(s);
        }
    }

}

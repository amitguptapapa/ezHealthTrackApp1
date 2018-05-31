package com.ezhealthtrack.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.activity.EditAccountActivity;
import com.ezhealthtrack.util.Constants;

public class SideFragment extends Fragment implements OnClickListener {
	// Container Activity must implement this interface
	public interface OnActionSelectedListner {
		public void onActionSelected(int position);
	}

	OnActionSelectedListner mCallback;
	private TextView txtLabOrders;
	private TextView txtInReferral;
	private TextView txtOutReferral;
	private TextView txtPatients;
	private TextView txtSchedule;
	private TextView txtConfirmed;
	private TextView txtLogout;
	private TextView txtSchedulePlan;
	private TextView txtNewTentative;
	private TextView txtHistory;
	private TextView txtLabPreference;
	private TextView txtRadiPreference;
	private TextView txtInbox;
	private TextView txtAlerts;
	private TextView txtDashboard;
	private TextView txtHospital;
	private TextView txtHospitalSearch;
	private TextView txtAssistant;
	private TextView txtAccount;
	private TextView txtProfile;
	private TextView txtOutbox;
	private TextView txtVisitNotes;
	private TextView txtAllergies;
	private TextView txtVitals;
	private TextView txtPrescriptions;
	private TextView txtRadiology;
	private TextView txtClinicalLab;
	private TextView txtEKG;
	private TextView txtReport;

	private void loadTextColors() {
		txtConfirmed.setTextColor(Color.parseColor("#000000"));
		txtLabOrders.setTextColor(Color.parseColor("#000000"));
		txtInReferral.setTextColor(Color.parseColor("#000000"));
		txtLogout.setTextColor(Color.parseColor("#000000"));
		txtOutReferral.setTextColor(Color.parseColor("#000000"));
		txtPatients.setTextColor(Color.parseColor("#000000"));
		txtSchedule.setTextColor(Color.parseColor("#000000"));
		txtSchedulePlan.setTextColor(Color.parseColor("#000000"));
		txtNewTentative.setTextColor(Color.parseColor("#000000"));
		txtHistory.setTextColor(Color.parseColor("#000000"));
		txtLabPreference.setTextColor(Color.parseColor("#000000"));
		txtRadiPreference.setTextColor(Color.parseColor("#000000"));
		txtInbox.setTextColor(Color.parseColor("#000000"));
		txtDashboard.setTextColor(Color.parseColor("#000000"));
		txtAlerts.setTextColor(Color.parseColor("#000000"));
		txtHospital.setTextColor(Color.parseColor("#000000"));
		txtHospitalSearch.setTextColor(Color.parseColor("#000000"));
		txtAssistant.setTextColor(Color.parseColor("#000000"));
		txtProfile.setTextColor(Color.parseColor("#000000"));
		txtOutbox.setTextColor(Color.parseColor("#000000"));
		txtVisitNotes.setTextColor(Color.parseColor("#000000"));
		txtAllergies.setTextColor(Color.parseColor("#000000"));
		txtVitals.setTextColor(Color.parseColor("#000000"));
		txtPrescriptions.setTextColor(Color.parseColor("#000000"));
		txtRadiology.setTextColor(Color.parseColor("#000000"));
		txtClinicalLab.setTextColor(Color.parseColor("#000000"));
		txtEKG.setTextColor(Color.parseColor("#000000"));
		txtReport.setTextColor(Color.parseColor("#000000"));
		txtAccount.setTextColor(Color.parseColor("#000000"));
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		txtDashboard = (TextView) getActivity()
				.findViewById(R.id.txt_dashboard);
		txtDashboard.setOnClickListener(this);
		txtDashboard.setTextColor(Color.parseColor("#737FE0"));
		txtPatients = (TextView) getActivity()
				.findViewById(R.id.txt_patients_1);
		txtPatients.setOnClickListener(this);
		txtSchedule = (TextView) getActivity().findViewById(R.id.txt_schedule);
		txtSchedule.setOnClickListener(this);
		txtConfirmed = (TextView) getActivity()
				.findViewById(R.id.txt_confirmed);
		txtConfirmed.setOnClickListener(this);
		txtLogout = (TextView) getActivity().findViewById(R.id.txt_logout);
		txtLogout.setOnClickListener(this);
		txtSchedulePlan = (TextView) getActivity().findViewById(
				R.id.txt_schedule_plan);
		txtSchedulePlan.setOnClickListener(this);
		txtLabPreference = (TextView) getActivity().findViewById(
				R.id.txt_lab_preference);
		txtLabPreference.setOnClickListener(this);
		txtRadiPreference = (TextView) getActivity().findViewById(
				R.id.txt_radi_preference);
		txtRadiPreference.setOnClickListener(this);
		txtNewTentative = (TextView) getActivity().findViewById(R.id.txt_new);
		txtNewTentative.setOnClickListener(this);
		txtHistory = (TextView) getActivity().findViewById(R.id.txt_history);
		txtHistory.setOnClickListener(this);
		txtLabOrders = (TextView) getActivity()
				.findViewById(R.id.txt_lab_ordes);
		txtLabOrders.setOnClickListener(this);
		txtInReferral = (TextView) getActivity().findViewById(
				R.id.txt_in_referrals);
		txtInReferral.setOnClickListener(this);
		txtOutReferral = (TextView) getActivity().findViewById(
				R.id.txt_out_referral);
		txtOutReferral.setOnClickListener(this);
		txtInbox = (TextView) getActivity().findViewById(R.id.txt_inbox);
		txtInbox.setOnClickListener(this);
		txtAlerts = (TextView) getActivity().findViewById(R.id.txt_alerts);
		txtAlerts.setOnClickListener(this);
		txtHospital = (TextView) getActivity().findViewById(R.id.txt_hospital);
		txtHospital.setOnClickListener(this);
		txtHospitalSearch = (TextView) getActivity().findViewById(
				R.id.txt_hospital_search);
		txtHospitalSearch.setOnClickListener(this);
		txtAssistant = (TextView) getActivity().findViewById(
				R.id.txt_assistants_1);
		txtAssistant.setOnClickListener(this);
		txtAccount = (TextView) getActivity().findViewById(R.id.txt_account);
		txtAccount.setOnClickListener(this);
		txtProfile = (TextView) getActivity().findViewById(R.id.txt_profile);
		txtProfile.setOnClickListener(this);
		txtOutbox = (TextView) getActivity().findViewById(R.id.txt_outbox);
		txtOutbox.setOnClickListener(this);
		txtVisitNotes = (TextView) getActivity().findViewById(
				R.id.txt_visit_notes);
		txtVisitNotes.setOnClickListener(this);
		txtAllergies = (TextView) getActivity()
				.findViewById(R.id.txt_allergies);
		txtAllergies.setOnClickListener(this);
		txtVitals = (TextView) getActivity().findViewById(R.id.txt_vitals);
		txtVitals.setOnClickListener(this);
		txtPrescriptions = (TextView) getActivity().findViewById(
				R.id.txt_prescriptions_side);
		txtPrescriptions.setOnClickListener(this);
		txtRadiology = (TextView) getActivity()
				.findViewById(R.id.txt_radiology);
		txtRadiology.setOnClickListener(this);
		txtClinicalLab = (TextView) getActivity().findViewById(
				R.id.txt_clinical_lab);
		txtClinicalLab.setOnClickListener(this);
		txtEKG = (TextView) getActivity().findViewById(R.id.txt_ekg);
		txtEKG.setOnClickListener(this);
		txtReport = (TextView) getActivity().findViewById(R.id.txt_report);
		txtReport.setOnClickListener(this);

		final SharedPreferences sharedPref = getActivity()
				.getApplicationContext().getSharedPreferences(
						Constants.EZ_SHARED_PREF, Context.MODE_PRIVATE);
		if (sharedPref.getString(Constants.DR_SPECIALITY, "").equalsIgnoreCase(
				"dentist")) {
			txtRadiPreference.setVisibility(View.GONE);
			txtLabPreference.setVisibility(View.GONE);
		} else {
			txtRadiPreference.setVisibility(View.VISIBLE);
			txtLabPreference.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onAttach(final Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mCallback = (OnActionSelectedListner) activity;
		} catch (final ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}

	@Override
	public void onClick(final View v) {
		switch (v.getId()) {
		case R.id.txt_patients_1:
			mCallback.onActionSelected(0);
			loadTextColors();
			txtPatients.setTextColor(Color.parseColor("#737FE0"));
			break;

		case R.id.txt_schedule:
			loadTextColors();
			txtSchedule.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(1);
			break;

		case R.id.txt_confirmed:
			loadTextColors();
			txtConfirmed.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(2);
			break;

		case R.id.txt_lab_ordes:
			loadTextColors();
			txtLabOrders.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(Constants.UA_LAB_ORDERS);
			break;

		case R.id.txt_in_referrals:
			loadTextColors();
			txtInReferral.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(3);
			break;

		case R.id.txt_out_referral:
			loadTextColors();
			txtOutReferral.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(4);
			break;

		case R.id.txt_logout:
			loadTextColors();
			txtLogout.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(5);
			break;

		case R.id.txt_schedule_plan:
			loadTextColors();
			txtSchedulePlan.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(6);
			break;

		case R.id.txt_new:
			loadTextColors();
			txtNewTentative.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(7);
			break;

		case R.id.txt_history:
			loadTextColors();
			txtHistory.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(8);
			break;

		case R.id.txt_lab_preference:
			loadTextColors();
			txtLabPreference.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(9);
			break;

		case R.id.txt_radi_preference:
			loadTextColors();
			txtRadiPreference.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(10);
			break;

		case R.id.txt_inbox:
			loadTextColors();
			txtInbox.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(11);
			break;

		case R.id.txt_dashboard:
			mCallback.onActionSelected(12);
			loadTextColors();
			txtDashboard.setTextColor(Color.parseColor("#737FE0"));
			break;

		case R.id.txt_alerts:
			loadTextColors();
			txtAlerts.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(13);
			break;

		case R.id.txt_hospital:
			loadTextColors();
			txtHospital.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(14);
			break;

		case R.id.txt_hospital_search:
			loadTextColors();
			txtHospitalSearch.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(15);
			break;

		case R.id.txt_assistants_1:
			mCallback.onActionSelected(16);
			loadTextColors();
			txtAssistant.setTextColor(Color.parseColor("#737FE0"));
			break;

		case R.id.txt_account:
			final Intent intent = new Intent(getActivity(),
					EditAccountActivity.class);
			startActivity(intent);
			break;

		case R.id.txt_profile:
			mCallback.onActionSelected(17);
			loadTextColors();
			txtProfile.setTextColor(Color.parseColor("#737FE0"));
			break;

		case R.id.txt_outbox:
			loadTextColors();
			txtOutbox.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(18);
			break;

		case R.id.txt_visit_notes:
			loadTextColors();
			txtVisitNotes.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(19);
			break;

		case R.id.txt_allergies:
			loadTextColors();
			txtAllergies.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(20);
			break;

		case R.id.txt_vitals:
			loadTextColors();
			txtVitals.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(21);
			break;

		case R.id.txt_prescriptions_side:
			loadTextColors();
			txtPrescriptions.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(22);
			break;

		case R.id.txt_radiology:
			loadTextColors();
			txtRadiology.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(23);
			break;

		case R.id.txt_clinical_lab:
			loadTextColors();
			txtClinicalLab.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(24);
			break;

		case R.id.txt_ekg:
			loadTextColors();
			txtEKG.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(25);
			break;

		case R.id.txt_report:
			loadTextColors();
			txtReport.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(26);
			break;

		}

	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_side, container, false);
	}

	@Override
	public void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	public void updateMessageCount() {
		if (InboxFragment.unreadCount > 0)
			txtInbox.setText("Inbox ( unread " + InboxFragment.unreadCount
					+ ")");
		else
			txtInbox.setText("Inbox");
		// txtOutReferral.setText("Out Referral ("
		// + DashboardActivity.arrRefferedToPatients.size() + ")");

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	public void userNameClicked() {
		mCallback.onActionSelected(17);
		loadTextColors();
		txtProfile.setTextColor(Color.parseColor("#737FE0"));
	}

	public void hospitalClicked() {
		loadTextColors();
		txtHospital.setTextColor(Color.parseColor("#737FE0"));
		mCallback.onActionSelected(14);
	}

	public void medRecClicked() {
		loadTextColors();
		txtVisitNotes.setTextColor(Color.parseColor("#737FE0"));
		// mCallback.onActionSelected(14);

	}
}

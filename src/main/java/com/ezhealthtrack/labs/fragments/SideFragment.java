package com.ezhealthtrack.labs.fragments;

import android.app.Activity;
import android.content.Intent;
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
import com.ezhealthtrack.fragments.InboxFragment;
import com.flurry.android.FlurryAgent;

public class SideFragment extends Fragment implements OnClickListener {
	// Container Activity must implement this interface
	public interface OnActionSelectedListner {
		public void onActionSelected(int position);
	}

	OnActionSelectedListner mCallback;
	private TextView txtConfirmed;
	private TextView txtSchedule;
	private TextView txtNewTentative;
	private TextView txtHistory;
	private TextView txtOrders;
	private TextView txtBillings;
	// private TextView txtNotPaid;
	// private TextView txtPartiallyPaid;
	// private TextView txtFullPaid;
	private TextView txtNewOrders;
	private TextView txtPartialFullfiled;
	private TextView txtCompleted;
	private TextView txtInReferral;
	private TextView txtOutReferral;
	private TextView txtPatients;
	private TextView txtLogout;
	private TextView txtInbox;
	private TextView txtAlerts;
	private TextView txtDashboard;
	private TextView txtAssistant;
	private TextView txtOutbox;
	private TextView txtAccount;
	public final int ACTION_PATIENTS = 0;
	public final int ACTION_INBOX = 4;
	public final int ACTION_DASHBOARD = 5;
	public final int ACTION_ALERT = 6;
	public final int ACTION_OUTBOX = 8;
	public final int ACTION_CONFIRMED = 9;
	public final int ACTION_SCHEDULE = 10;
	public final int ACTION_NEWTENTATIVE = 11;
	public final int ACTION_HISTORY = 12;
	public final int ACTION_NEW = 13;
	public final int ACTION_PARTIAL = 14;
	public final int ACTION_COMPLETE = 15;
	public final int ACTION_ACCOUNT = 16;
	public final int ACTION_ORDERS = 17;
	public final int ACTION_NOTPAID = 18;
	public final int ACTION_PARTIALLYPAID = 19;
	public final int ACTION_FULLPAID = 20;
	public final int ACTION_LOGOUT = 21;
	public final int ACTION_BILLINGS = 22;

	private void loadTextColors() {
		txtConfirmed.setTextColor(Color.parseColor("#000000"));
		txtSchedule.setTextColor(Color.parseColor("#000000"));
		txtNewTentative.setTextColor(Color.parseColor("#000000"));
		txtHistory.setTextColor(Color.parseColor("#000000"));
		txtNewOrders.setTextColor(Color.parseColor("#000000"));
		txtPartialFullfiled.setTextColor(Color.parseColor("#000000"));
		txtCompleted.setTextColor(Color.parseColor("#000000"));
		txtInReferral.setTextColor(Color.parseColor("#000000"));
		txtLogout.setTextColor(Color.parseColor("#000000"));
		txtOutReferral.setTextColor(Color.parseColor("#000000"));
		txtPatients.setTextColor(Color.parseColor("#000000"));
		txtInbox.setTextColor(Color.parseColor("#000000"));
		txtDashboard.setTextColor(Color.parseColor("#000000"));
		txtAlerts.setTextColor(Color.parseColor("#000000"));
		txtAssistant.setTextColor(Color.parseColor("#000000"));
		txtOutbox.setTextColor(Color.parseColor("#000000"));
		txtAccount.setTextColor(Color.parseColor("#000000"));
		txtOrders.setTextColor(Color.parseColor("#000000"));
		txtBillings.setTextColor(Color.parseColor("#000000"));
		// txtNotPaid.setTextColor(Color.parseColor("#000000"));
		// txtFullPaid.setTextColor(Color.parseColor("#000000"));
		// txtPartiallyPaid.setTextColor(Color.parseColor("#000000"));
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
		txtLogout = (TextView) getActivity().findViewById(R.id.txt_logout);
		txtLogout.setOnClickListener(this);
		txtInReferral = (TextView) getActivity().findViewById(
				R.id.txt_in_referrals);
		txtInReferral.setOnClickListener(this);
		txtInReferral.append(" ("
				+ DashboardActivity.arrRefferedByPatients.size() + ")");
		txtOutReferral = (TextView) getActivity().findViewById(
				R.id.txt_out_referral);
		txtOutReferral.setOnClickListener(this);
		txtOutReferral.append(" ("
				+ DashboardActivity.arrRefferedToPatients.size() + ")");
		txtInbox = (TextView) getActivity().findViewById(R.id.txt_inbox);
		txtInbox.setOnClickListener(this);
		txtAlerts = (TextView) getActivity().findViewById(R.id.txt_alerts);
		txtAlerts.setOnClickListener(this);
		txtAssistant = (TextView) getActivity().findViewById(
				R.id.txt_assistants_1);
		txtAssistant.setOnClickListener(this);
		txtOutbox = (TextView) getActivity().findViewById(R.id.txt_outbox);
		txtOutbox.setOnClickListener(this);
		txtConfirmed = (TextView) getActivity()
				.findViewById(R.id.txt_confirmed);
		txtConfirmed.setOnClickListener(this);
		txtSchedule = (TextView) getActivity().findViewById(R.id.txt_schedule);
		txtSchedule.setOnClickListener(this);
		txtNewTentative = (TextView) getActivity().findViewById(
				R.id.txt_new_tentative);
		txtNewTentative.setOnClickListener(this);
		txtHistory = (TextView) getActivity().findViewById(R.id.txt_history);
		txtHistory.setOnClickListener(this);
		txtNewOrders = (TextView) getActivity().findViewById(
				R.id.txt_new_orders);
		txtNewOrders.setOnClickListener(this);
		txtPartialFullfiled = (TextView) getActivity().findViewById(
				R.id.txt_partial_fullfiled);
		txtPartialFullfiled.setOnClickListener(this);
		txtCompleted = (TextView) getActivity()
				.findViewById(R.id.txt_completed);
		txtCompleted.setOnClickListener(this);
		txtAccount = (TextView) getActivity().findViewById(R.id.txt_account);
		txtAccount.setOnClickListener(this);
		txtOrders = (TextView) getActivity().findViewById(R.id.txt_orders_list);
		txtOrders.setOnClickListener(this);
		txtBillings = (TextView) getActivity().findViewById(R.id.txt_billings);
		txtBillings.setOnClickListener(this);
		// txtNotPaid = (TextView)
		// getActivity().findViewById(R.id.txt_not_paid);
		// txtNotPaid.setOnClickListener(this);
		// txtPartiallyPaid = (TextView) getActivity().findViewById(
		// R.id.txt_partially_paid);
		// txtPartiallyPaid.setOnClickListener(this);
		// txtFullPaid = (TextView)
		// getActivity().findViewById(R.id.txt_full_paid);
		// txtFullPaid.setOnClickListener(this);
	}

	@Override
	public void onAttach(final Activity activity) {
		super.onAttach(activity);
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
			FlurryAgent.logEvent("SideFragment - Patients Button Clicked");
			mCallback.onActionSelected(ACTION_PATIENTS);
			loadTextColors();
			txtPatients.setTextColor(Color.parseColor("#737FE0"));
			break;

		case R.id.txt_in_referrals:
			FlurryAgent.logEvent("SideFragment - In Referral Button Clicked");
			loadTextColors();
			txtInReferral.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(1);
			break;

		case R.id.txt_out_referral:
			FlurryAgent.logEvent("SideFragment - Out Referral Button Clicked");
			loadTextColors();
			txtOutReferral.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(2);
			break;

		case R.id.txt_logout:
			FlurryAgent.logEvent("SideFragment - Logout Button Clicked");
			loadTextColors();
			txtLogout.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(ACTION_LOGOUT);
			break;

		case R.id.txt_inbox:
			FlurryAgent.logEvent("SideFragment - Inbox Button Clicked");
			loadTextColors();
			txtInbox.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(ACTION_INBOX);
			break;

		case R.id.txt_dashboard:
			FlurryAgent.logEvent("SideFragment - Dashboard Button Clicked");
			mCallback.onActionSelected(ACTION_DASHBOARD);
			loadTextColors();
			txtDashboard.setTextColor(Color.parseColor("#737FE0"));
			break;

		case R.id.txt_alerts:
			FlurryAgent.logEvent("SideFragment - Alerts Button Clicked");
			loadTextColors();
			txtAlerts.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(ACTION_ALERT);
			break;

		case R.id.txt_assistants_1:
			FlurryAgent.logEvent("SideFragment - Assistant Button Clicked");
			mCallback.onActionSelected(7);
			loadTextColors();
			txtAssistant.setTextColor(Color.parseColor("#737FE0"));
			break;

		case R.id.txt_outbox:
			FlurryAgent.logEvent("SideFragment - Outbox Button Clicked");
			loadTextColors();
			txtOutbox.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(ACTION_OUTBOX);
			break;

		case R.id.txt_confirmed:
			FlurryAgent.logEvent("SideFragment - Confirmed Button Clicked");
			loadTextColors();
			txtConfirmed.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(ACTION_CONFIRMED);
			break;

		case R.id.txt_schedule:
			FlurryAgent.logEvent("SideFragment - Schedule Button Clicked");
			loadTextColors();
			txtSchedule.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(ACTION_SCHEDULE);
			break;

		case R.id.txt_new_tentative:
			FlurryAgent.logEvent("SideFragment - New/Tentative Button Clicked");
			loadTextColors();
			txtNewTentative.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(ACTION_NEWTENTATIVE);
			break;

		case R.id.txt_history:
			FlurryAgent.logEvent("SideFragment - History Button Clicked");
			loadTextColors();
			txtHistory.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(ACTION_HISTORY);
			break;

		case R.id.txt_new_orders:
			FlurryAgent.logEvent("SideFragment - New Orders Button Clicked");
			loadTextColors();
			txtNewOrders.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(ACTION_NEW);
			break;

		case R.id.txt_partial_fullfiled:
			FlurryAgent
					.logEvent("SideFragment - Partially Fullfilled Button Clicked");
			loadTextColors();
			txtPartialFullfiled.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(ACTION_PARTIAL);
			break;

		case R.id.txt_completed:
			FlurryAgent.logEvent("SideFragment - Completed Button Clicked");
			loadTextColors();
			txtCompleted.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(ACTION_COMPLETE);
			break;

		case R.id.txt_orders_list:
			FlurryAgent.logEvent("SideFragment - Orders Button Clicked");
			loadTextColors();
			txtOrders.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(ACTION_ORDERS);
			break;

		case R.id.txt_billings:
			FlurryAgent.logEvent("SideFragment - Billings Button Clicked");
			loadTextColors();
			txtBillings.setTextColor(Color.parseColor("#737FE0"));
			mCallback.onActionSelected(ACTION_BILLINGS);
			break;

		// case R.id.txt_not_paid:
		// FlurryAgent.logEvent("SideFragment - Not Paid Button Clicked");
		// loadTextColors();
		// txtNotPaid.setTextColor(Color.parseColor("#737FE0"));
		// mCallback.onActionSelected(ACTION_NOTPAID);
		// break;
		//
		// case R.id.txt_partially_paid:
		// FlurryAgent
		// .logEvent("SideFragment - Partially Paid Button Clicked");
		// loadTextColors();
		// txtPartiallyPaid.setTextColor(Color.parseColor("#737FE0"));
		// mCallback.onActionSelected(ACTION_PARTIALLYPAID);
		// break;
		//
		// case R.id.txt_full_paid:
		// FlurryAgent.logEvent("SideFragment - Fully Paid Button Clicked");
		// loadTextColors();
		// txtFullPaid.setTextColor(Color.parseColor("#737FE0"));
		// mCallback.onActionSelected(ACTION_FULLPAID);
		// break;

		case R.id.txt_account:
			FlurryAgent.logEvent("SideFragment - Account Button Clicked");
			final Intent intent = new Intent(getActivity(),
					EditAccountActivity.class);
			startActivity(intent);
			break;

		}

	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.labs_fragment_side, container, false);
	}

	@Override
	public void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	public void updateOrderColor() {
		loadTextColors();
		txtOrders.setTextColor(Color.parseColor("#737FE0"));
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
}

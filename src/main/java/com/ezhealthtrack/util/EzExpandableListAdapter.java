package com.ezhealthtrack.util;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ezhealthtrack.R;

public class EzExpandableListAdapter extends BaseExpandableListAdapter {

	private Activity mContext;
	private List<String> mGroups;
	private Map<String, List<String>> mCollection;

	public EzExpandableListAdapter(Activity context, List<String> groups,
			Map<String, List<String>> collection) {
		this.mContext = context;
		this.mCollection = collection;
		this.mGroups = groups;
	}

	public Object getChild(int groupPosition, int childPosition) {
		return mCollection.get(mGroups.get(groupPosition)).get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final String theMember = (String) getChild(groupPosition, childPosition);
		LayoutInflater inflater = mContext.getLayoutInflater();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.drawer_item_row, parent,
					false);
		}
		TextView name = (TextView) convertView.findViewById(R.id.member_name);
		name.setText(theMember);
		ImageView image = (ImageView) convertView.findViewById(R.id.member_img);
		image.setImageResource(this.getImageResId(theMember));
		return convertView;
	}

	private int getImageResId(String name) {

		if (name.equals(EzDrawerActivity.MENU_HOME_Dashboard))
			return R.drawable.ic_action_home;

		if (name.equals(EzDrawerActivity.MENU_APTMNT_Confirmed))
			return R.drawable.ic_action_finger_up;
		if (name.equals(EzDrawerActivity.MENU_APTMNT_Tentative))
			return R.drawable.ic_action_finger_right;
		if (name.equals(EzDrawerActivity.MENU_APTMNT_History))
			return R.drawable.ic_action_finger_down;
		if (name.equals(EzDrawerActivity.MENU_APTMNT_Schedule))
			return R.drawable.ic_action_clock_plus;

		if (name.equals(EzDrawerActivity.MENU_MESG_Inbox))
			return R.drawable.ic_action_inbox;
		if (name.equals(EzDrawerActivity.MENU_MESG_Outbox))
			return R.drawable.ic_action_outbox;
		if (name.equals(EzDrawerActivity.MENU_MESG_Alerts))
			return R.drawable.ic_action_alert;

		if (name.equals(EzDrawerActivity.MENU_PATIENTS_Patients))
			return R.drawable.ic_action_patients;

		if (name.equals(EzDrawerActivity.MENU_MEDREC_VisitNotes))
			return R.drawable.ic_action_reports;
		if (name.equals(EzDrawerActivity.MENU_MEDREC_Allergies))
			return R.drawable.ic_action_reports;
		if (name.equals(EzDrawerActivity.MENU_MEDREC_Vitals))
			return R.drawable.ic_action_reports;
		if (name.equals(EzDrawerActivity.MENU_MEDREC_Prescriptions))
			return R.drawable.ic_action_reports;
		if (name.equals(EzDrawerActivity.MENU_MEDREC_Radiology))
			return R.drawable.ic_action_reports;
		if (name.equals(EzDrawerActivity.MENU_MEDREC_ClinicalLabs))
			return R.drawable.ic_action_reports;
		if (name.equals(EzDrawerActivity.MENU_MEDREC_EkgEcg))
			return R.drawable.ic_action_reports;

		if (name.equals(EzDrawerActivity.MENU_ORDERS_LabOrders))
			return R.drawable.ic_action_bulleted; //

		if (name.equals(EzDrawerActivity.MENU_REFERALLS_In))
			return R.drawable.ic_action_arrow_in; //
		if (name.equals(EzDrawerActivity.MENU_REFERALLS_Out))
			return R.drawable.ic_action_arrow_out; //

		if (name.equals(EzDrawerActivity.MENU_ASSISTENTS_Assistents))
			return R.drawable.ic_action_avatar;

		if (name.equals(EzDrawerActivity.MENU_ACCOUNT_Account))
			return R.drawable.ic_action_lock_open;
		if (name.equals(EzDrawerActivity.MENU_ACCOUNT_SchedulePlan))
			return R.drawable.ic_action_clock_plus;
		if (name.equals(EzDrawerActivity.MENU_ACCOUNT_Profile))
			return R.drawable.ic_action_avatar;
		if (name.equals(EzDrawerActivity.MENU_ACCOUNT_LabPreferences))
			return R.drawable.ic_action_select; //
		if (name.equals(EzDrawerActivity.MENU_ACCOUNT_RadiologyPreferences))
			return R.drawable.ic_action_select; //

		if (name.equals(EzDrawerActivity.MENU_ABOUT))
			return R.drawable.ic_action_about;

		if (name.equals(EzDrawerActivity.MENU_LOGOUT_Logout))
			return R.drawable.ic_action_power; //

		return 0;
	}

	public int getChildrenCount(int groupPosition) {

		if (mCollection == null) {
			Log.v("CGCsF", "mCollection is NULL");
			return 0;
		}
		if (mGroups == null) {
			Log.v("CGCsF", "mGroups is NULL");
			return 0;
		}
		return mCollection.get(mGroups.get(groupPosition)).size();
	}

	public Object getGroup(int groupPosition) {
		return mGroups.get(groupPosition);
	}

	public int getGroupCount() {
		return mGroups.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		String group = (String) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.drawer_title_row,
					parent, false);
		}
		TextView name = (TextView) convertView.findViewById(R.id.header_name);
		name.setText(group.toUpperCase(Locale.ENGLISH));
		return convertView;
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public void onDestroy() {
		this.mContext = null;
		this.mGroups = null;
		this.mCollection = null;
	}

}

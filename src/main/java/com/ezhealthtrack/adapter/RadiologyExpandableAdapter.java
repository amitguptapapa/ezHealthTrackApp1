package com.ezhealthtrack.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;

import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.model.RadiologyModel;

public class RadiologyExpandableAdapter extends BaseExpandableListAdapter {
	private Activity activity;
	private ArrayList<ArrayList<RadiologyModel>> childItems;
	private LayoutInflater inflater;
	private ArrayList<RadiologyModel> parentItems, child;
	ExpandableListView lv;

	// constructor
	public RadiologyExpandableAdapter(ArrayList<RadiologyModel> parents,
			ArrayList<ArrayList<RadiologyModel>> childern, ExpandableListView lv) {
		this.parentItems = parents;
		this.childItems = childern;
		this.lv = lv;
	}

	public void setInflater(LayoutInflater inflater, Activity activity) {
		this.inflater = inflater;
		this.activity = activity;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return parentItems.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return ((ArrayList<RadiologyModel>) childItems.get(groupPosition))
				.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.row_checked_item, null);
		}

		final CheckBox cb = ((CheckBox) convertView
				.findViewById(R.id.checked_item));
		cb.setText(parentItems.get(groupPosition).getTestName());

		cb.setFocusable(false);
		cb.setClickable(false);
		if (DashboardActivity.arrRadiSelected.contains(parentItems.get(
				groupPosition).getTestName())) {
			lv.expandGroup(groupPosition);
			cb.setChecked(true);
		} else {
			lv.collapseGroup(groupPosition);
			cb.setChecked(false);
		}

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!cb.isChecked()) {
					lv.expandGroup(groupPosition);
					cb.setChecked(true);
					DashboardActivity.arrRadiSelected.add(parentItems.get(
							groupPosition).getTestName());
				} else {
					lv.collapseGroup(groupPosition);
					cb.setChecked(false);
					DashboardActivity.arrRadiSelected.remove(parentItems.get(
							groupPosition).getTestName());
				}
			}
		});

		return convertView;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		child = (ArrayList<RadiologyModel>) childItems.get(groupPosition);

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.row_checked_subitem, null);
		}
		final CheckBox cb = ((CheckBox) convertView
				.findViewById(R.id.checked_item));
		cb.setText(childItems.get(groupPosition).get(childPosition).getTestName());

		cb.setFocusable(false);
		//cb.setClickable(false);

		if (DashboardActivity.arrRadiSelected.contains(childItems.get(groupPosition).get(childPosition).getTestName())) {
			cb.setChecked(true);
		} else {
			cb.setChecked(false);
		}

		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (cb.isChecked()) {
					
					DashboardActivity.arrRadiSelected.add(childItems.get(groupPosition).get(childPosition).getTestName());
				} else {
					
					DashboardActivity.arrRadiSelected.remove(childItems.get(groupPosition).get(childPosition).getTestName());
				}
				
			}
		});
	
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

}

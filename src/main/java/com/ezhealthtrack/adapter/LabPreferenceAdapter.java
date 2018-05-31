package com.ezhealthtrack.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import com.ezhealthtrack.R;
import com.ezhealthtrack.model.LabPreference;

public class LabPreferenceAdapter extends ArrayAdapter<LabPreference>{
	private static class ViewHolder {
		private CheckBox cb;
	}

	public LabPreferenceAdapter(Context context, int resource, ArrayList<LabPreference> objects) {
		super(context, resource, objects);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final LabPreference rowItem = getItem(position);
		View row = null;
		if ( convertView == null) {
			final ViewHolder holder = new ViewHolder();
			final LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.row_checked_item, null);
			holder.cb = (CheckBox) row.findViewById(R.id.checked_item);
			row.setTag(holder);
		} else {
			row = convertView;
		}
		final ViewHolder holder = (ViewHolder) row.getTag();
		holder.cb.setText(rowItem.getName());
		holder.cb.setChecked(true);
		holder.cb.setClickable(false);
		holder.cb.setFocusable(false);
		holder.cb.setFocusableInTouchMode(false);
		return row;
	}

}

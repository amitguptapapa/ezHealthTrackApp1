package com.ezhealthtrack.labs.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ezhealthtrack.R;
import com.ezhealthtrack.greendao.LabTechnician;

public class LabsTechnicianAdapter extends ArrayAdapter<LabTechnician> {
	Context context;
	Boolean enabled;
	Activity activity;

	public LabsTechnicianAdapter(Context context, int resourceId,
			List<LabTechnician> item) {
		super(context, resourceId, item);
		this.context = context;
	}

	private static class ViewHolder {
		private TextView txtTechnicain;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final LabTechnician rowItem = getItem(position);

		View row = null;
		if (convertView == null) {
			ViewHolder holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.labs_row_technician, null);
			holder.txtTechnicain = (TextView) row.findViewById(R.id.txt_tech);
			row.setTag(holder);
		} else {
			row = convertView;
		}
		final ViewHolder holder = (ViewHolder) row.getTag();
		holder.txtTechnicain.setText(rowItem.getName());
		return row;

	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		final LabTechnician rowItem = getItem(position);

		View row = null;
		if (convertView == null) {
			ViewHolder holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.labs_row_technician, null);
			holder.txtTechnicain = (TextView) row.findViewById(R.id.txt_tech);
			row.setTag(holder);
		} else {
			row = convertView;
		}
		final ViewHolder holder = (ViewHolder) row.getTag();
		holder.txtTechnicain.setText(rowItem.getName());
		return row;

	}

}

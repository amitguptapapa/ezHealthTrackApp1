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
import com.ezhealthtrack.order.SampleMetum;

public class LabsLabSampleAdapter extends ArrayAdapter<SampleMetum> {
	Context context;
	Boolean enabled;
	Activity activity;

	public LabsLabSampleAdapter(Context context, int resourceId,
			List<SampleMetum> item) {
		super(context, resourceId, item);

		this.context = context;
	}

	/* private view holder class */
	private static class ViewHolder {
		private TextView txtSample;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final SampleMetum rowItem = getItem(position);

		View row = null;
		if (convertView == null) {
			ViewHolder holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.labs_row_lab_tests_sample, null);
			holder.txtSample = (TextView) row.findViewById(R.id.txt_sample);
			row.setTag(holder);
		} else {
			row = convertView;
		}

		final ViewHolder holder = (ViewHolder) row.getTag();
		holder.txtSample.setText(rowItem.getName());
		return row;

	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {

		final SampleMetum rowItem = getItem(position);

		View row = null;
		if (convertView == null) {
			ViewHolder holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.labs_row_lab_tests_sample, null);
			holder.txtSample = (TextView) row.findViewById(R.id.txt_sample);
			row.setTag(holder);
		} else {
			row = convertView;
		}

		final ViewHolder holder = (ViewHolder) row.getTag();
		holder.txtSample.setText(rowItem.getName());
		return row;
	}

}
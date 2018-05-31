package com.ezhealthtrack.labs.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ezhealthtrack.R;
import com.ezhealthtrack.order.LabPanel;

public class LabsLabPanelAdapter extends ArrayAdapter<LabPanel> {
	Context context;
	Activity activity;

	public LabsLabPanelAdapter(Context context, int resourceId,
			List<LabPanel> item) {
		super(context, resourceId, item);

		this.context = context;
	}

	/* private view holder class */
	private static class ViewHolder {
		private TextView txtTestName;
	}

	public void LabsLabTestAdapter(final Context context) {
		activity = (Activity) context;
		this.context = context;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final LabPanel rowItem = getItem(position);

		View row = null;
		if (convertView == null) {
			ViewHolder holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.labs_row_lab_tests, null);
			holder.txtTestName = (TextView) row
					.findViewById(R.id.txt_test_name);
			row.setTag(holder);
		} else {
			row = convertView;
		}

		final ViewHolder holder = (ViewHolder) row.getTag();
		holder.txtTestName.setText(rowItem.getName());
		return row;

	}

}
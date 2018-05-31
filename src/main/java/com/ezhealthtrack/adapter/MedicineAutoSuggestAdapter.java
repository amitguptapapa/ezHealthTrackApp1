package com.ezhealthtrack.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ezhealthtrack.R;
import com.ezhealthtrack.model.MedicineAutoSuggest;

public class MedicineAutoSuggestAdapter extends ArrayAdapter<MedicineAutoSuggest> {
	Context context;
	Boolean enabled;

	public MedicineAutoSuggestAdapter(Context context, int resourceId,
			List<MedicineAutoSuggest> item,boolean enabled) {
		super(context, resourceId, item);
		this.enabled = enabled;
		this.context = context;
	}

	/* private view holder class */
	private static class ViewHolder {
		private TextView txtName;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final MedicineAutoSuggest rowItem = getItem(position);

		View row = null;
		if (convertView == null) {
			ViewHolder holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.row_med_autosuggest, null);
			holder.txtName = (TextView) row.findViewById(R.id.txt_drug_name);
			row.setTag(holder);
		} else {
			row = convertView;
		}
		final ViewHolder holder = (ViewHolder) row.getTag();
		holder.txtName.setText(rowItem.getDisplayName());

		return row;
	}

}
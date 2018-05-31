package com.ezhealthtrack.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ezhealthtrack.R;
import com.ezhealthtrack.model.MedicineModel;

public class MedicineAdapter extends ArrayAdapter<MedicineModel> {
	Context context;
	Boolean enabled;

	public MedicineAdapter(Context context, int resourceId,
			List<MedicineModel> item, boolean enabled) {
		super(context, resourceId, item);
		this.enabled = enabled;
		this.context = context;
	}

	/* private view holder class */
	private static class ViewHolder {
		private TextView txtName;
		private TextView txtStrength;
		private TextView txtUnit;
		private TextView txtRoute;
		private TextView txtFrequency;
		private TextView txttimes;
		private TextView txtQuantity;
		private TextView txtRefillsTimes;
		private TextView txtRefills;
		private TextView btnDeleteMedicine;
		private TextView txtForm;
		private TextView txtNotes;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final MedicineModel rowItem = getItem(position);

		View row = null;
		if (convertView == null) {
			ViewHolder holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.row_medicine, parent, false);
			holder.txtName = (TextView) row.findViewById(R.id.txt_drug_name);
			holder.txtStrength = (TextView) row.findViewById(R.id.txt_strength);
			holder.txtUnit = (TextView) row.findViewById(R.id.txt_unit);
			holder.txtRoute = (TextView) row.findViewById(R.id.txt_route);
			holder.txtFrequency = (TextView) row
					.findViewById(R.id.txt_frequency);
			holder.txtRefills = (TextView) row.findViewById(R.id.txt_refills);
			holder.txttimes = (TextView) row.findViewById(R.id.txt_time);
			holder.txtQuantity = (TextView) row.findViewById(R.id.txt_quantity);
			holder.txtRefillsTimes = (TextView) row
					.findViewById(R.id.txt_refill_time);
			holder.btnDeleteMedicine = (TextView) row
					.findViewById(R.id.btn_del_medicine);
			holder.txtForm = (TextView) row.findViewById(R.id.txt_form);
			holder.txtNotes = (TextView) row.findViewById(R.id.txt_notes);
			row.setTag(holder);
		} else {
			row = convertView;
		}
		final ViewHolder holder = (ViewHolder) row.getTag();
		holder.txtName.setText(rowItem.name);
		holder.txtStrength.setText(rowItem.strength);
		holder.txtUnit.setText(rowItem.unit);
		holder.txtRoute.setText(rowItem.route);
		holder.txtFrequency.setText(rowItem.frequency);
		holder.txtRefills.setText(rowItem.refills);
		holder.txttimes.setText(rowItem.times);
		holder.txtQuantity.setText(rowItem.quantity);
		holder.txtRefillsTimes.setText(rowItem.refillsTime);
		holder.txtForm.setText(rowItem.formulations);
		holder.txtNotes.setText(rowItem.notes);
		if (enabled) {
			holder.btnDeleteMedicine.setVisibility(View.VISIBLE);
		} else {
			holder.btnDeleteMedicine.setVisibility(View.GONE);
		}
		holder.btnDeleteMedicine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				remove(rowItem);
				notifyDataSetChanged();
			}
		});

		return row;
	}

}
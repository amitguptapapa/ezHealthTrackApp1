package com.ezhealthtrack.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezhealthtrack.R;
import com.ezhealthtrack.controller.MedicalRecordsController;
import com.ezhealthtrack.model.AllergyModel;
import com.ezhealthtrack.model.AllergyModel.Datum;
import com.ezhealthtrack.util.Util;

public class AllergiesAllergyDetailAdapter extends ArrayAdapter<Datum> {
	/* private view holder class */
	private static class ViewHolder {
		private TextView txtAllergy;

	}

	private final Context context;

	public AllergiesAllergyDetailAdapter(final Context context,
			List<Datum> arrDatum) {
		super(context, R.layout.row_mrecords_allergies,
				R.id.txt_medrec_allergies, arrDatum);
		this.context = context;

	}

	@Override
	public View getView(final int position, final View convertView,
			final ViewGroup parent) {
		View row = null;
		final Datum rowItem = getItem(position);
		if (convertView == null) {
			final ViewHolder holder = new ViewHolder();
			final LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.row_mrecords_allergies, null);
			holder.txtAllergy = (TextView) row
					.findViewById(R.id.txt_medrec_allergies);
			row.setTag(holder);
		} else {
			row = convertView;
		}

		final ViewHolder holder = (ViewHolder) row.getTag();

		if (Util.isEmptyString(rowItem.getAdditionalInfo())) {
			holder.txtAllergy.setText(rowItem.getPatName()
					+ " has allergy with " + rowItem.getMainCat().getNAME()
					+ " --> " + rowItem.getSubCat().getNAME() + " added on "
					+ rowItem.getDateCreated());
		} else {
			holder.txtAllergy.setText(rowItem.getPatName()
					+ " has allergy with " + rowItem.getMainCat().getNAME()
					+ " --> " + rowItem.getSubCat().getNAME() + " added on "
					+ rowItem.getDateCreated() + ", Additional info Provided: "
					+ rowItem.getAdditionalInfo());
		}

		return row;
	}
}

package com.ezhealthtrack.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ezhealthtrack.R;
import com.ezhealthtrack.controller.MedicalRecordsController;
import com.ezhealthtrack.controller.MedicalRecordsController.OnResponseObject;
import com.ezhealthtrack.dialogs.MedicalRecords;
import com.ezhealthtrack.greendao.MedRecAllergy;
import com.ezhealthtrack.model.AllergyModel;
import com.ezhealthtrack.model.MedRecClinicalLab;
import com.ezhealthtrack.model.MedRecEKG;
import com.ezhealthtrack.model.MedRecPresModel;
import com.ezhealthtrack.model.MedRecRadiology;
import com.ezhealthtrack.model.VitalModel;
import com.ezhealthtrack.util.LazyListAdapter;

public class MrListAdapter extends LazyListAdapter<MedRecAllergy> {

	String type;
	Context context;

	public MrListAdapter(final Context context, String type) {
		this.type = type;
		this.context = context;
	}

	@Override
	public View getView(final int position, View view, final ViewGroup parent) {

		final MedRecAllergy rowItem = getItem(position);
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.row_medical_record, parent, false);
		}
		TextView txtIndex = (TextView) view.findViewById(R.id.txt_index);
		txtIndex.setText("" + (position + 1) + ".");

		TextView txtDetails = (TextView) view.findViewById(R.id.txt_details);
		txtDetails.setText("" + rowItem.getPat_detail() + ", Last Visit: "
				+ rowItem.getLast_visit());

		final View theView = view;
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onListItemClicked(theView, position);
			}
		});
		return view;
	}

	public void onListItemClicked(View view, int position) {
		final MedRecAllergy rowItem = getItem(position);
		MedicalRecordsController.getMedicalRecordDetail(context, type,
				rowItem.getPat_id(), new OnResponseObject() {

					@Override
					public void onResponseListner(Object response) {
						if (type.equals(MedicalRecordsController.ALLERGY_TYPE))
							MedicalRecords.dialogDetailAllergies(context,
									(AllergyModel) response);
						else if (type
								.equals(MedicalRecordsController.VITALS_TYPE))
							MedicalRecords.dialogDetailVitals(context,
									(VitalModel) response);
						else if (type
								.equals(MedicalRecordsController.PRESCRIPTION_TYPE))
							MedicalRecords.dialogDetailPrescription(context,
									(MedRecPresModel) response);
						else if (type
								.equals(MedicalRecordsController.RADIOLOGY_TYPE))
							MedicalRecords.dialogDetailRadiology(context,
									(MedRecRadiology) response);
						else if (type.equals(MedicalRecordsController.LAB_TYPE))
							MedicalRecords.dialogDetailClinicalLab(context,
									(MedRecClinicalLab) response);
						else if (type.equals(MedicalRecordsController.ECG_TYPE))
							MedicalRecords.dialogDetailEkg(context,
									(MedRecEKG) response);
					}
				});
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}
}

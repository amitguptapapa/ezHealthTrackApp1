package com.ezhealthtrack.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
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

public class AllergiesAdapter extends LazyListAdapter<MedRecAllergy> {
	/* private view holder class */
	private static class ViewHolder {
		private TextView txtConfirmed;
		private RelativeLayout rlActions;
		private Button btnEdit;
		private Button btnDetails;
	}

	Context context;
	String type;

	public AllergiesAdapter(final Context context, String type) {
		this.context = context;
		this.type = type;
	}

	@Override
	public View getView(final int position, final View convertView,
			final ViewGroup parent) {
		View row = null;
		final MedRecAllergy rowItem = getItem(position);
		if (convertView == null) {
			final ViewHolder holder = new ViewHolder();
			final LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.row_allergies, null);
			holder.txtConfirmed = (TextView) row
					.findViewById(R.id.txt_confirmed);
			holder.rlActions = (RelativeLayout) row
					.findViewById(R.id.rl_actions);
			holder.btnEdit = (Button) row.findViewById(R.id.btn_edit);
			holder.btnDetails = (Button) row.findViewById(R.id.btn_details);
			row.setTag(holder);
		} else {
			row = convertView;
		}

		final ViewHolder holder = (ViewHolder) row.getTag();
		holder.txtConfirmed.setText(rowItem.getPat_detail() + ", Last Visit: "
				+ rowItem.getLast_visit());
		if (type.equals(MedicalRecordsController.ALLERGY_TYPE)) {
			holder.btnEdit.setVisibility(View.VISIBLE);
		} else {
			holder.btnEdit.setVisibility(View.GONE);
		}
		holder.btnEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MedicalRecordsController.getMedicalRecordDetail(context,
						MedicalRecordsController.ALLERGY_TYPE,
						rowItem.getPat_id(), new OnResponseObject() {

							@Override
							public void onResponseListner(Object response) {
								MedicalRecords.dialogEditAllergies(context,
										(AllergyModel) response);

							}
						});

			}
		});

		holder.btnDetails.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MedicalRecordsController.getMedicalRecordDetail(context, type,
						rowItem.getPat_id(), new OnResponseObject() {

							@Override
							public void onResponseListner(Object response) {
								if (type.equals(MedicalRecordsController.ALLERGY_TYPE))
									MedicalRecords.dialogDetailAllergies(
											context, (AllergyModel) response);
								else if (type
										.equals(MedicalRecordsController.VITALS_TYPE))
									MedicalRecords.dialogDetailVitals(context,
											(VitalModel) response);
								else if (type
										.equals(MedicalRecordsController.PRESCRIPTION_TYPE))
									MedicalRecords
											.dialogDetailPrescription(context,
													(MedRecPresModel) response);
								else if (type
										.equals(MedicalRecordsController.RADIOLOGY_TYPE))
									MedicalRecords
											.dialogDetailRadiology(context,
													(MedRecRadiology) response);
								else if (type
										.equals(MedicalRecordsController.LAB_TYPE))
									MedicalRecords.dialogDetailClinicalLab(
											context,
											(MedRecClinicalLab) response);
								else if (type
										.equals(MedicalRecordsController.ECG_TYPE))
									MedicalRecords.dialogDetailEkg(context,
											(MedRecEKG) response);
							}
						});

			}
		});

		holder.txtConfirmed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				if (holder.rlActions.getVisibility() == View.GONE) {
					holder.rlActions.setVisibility(View.VISIBLE);
				} else {
					holder.rlActions.setVisibility(View.GONE);
				}

			}
		});

		return row;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}

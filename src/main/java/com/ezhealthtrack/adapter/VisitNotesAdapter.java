package com.ezhealthtrack.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.DentistSoap.AddDentistNotesActivity;
import com.ezhealthtrack.controller.SoapNotesController;
import com.ezhealthtrack.greendao.Appointment;
import com.ezhealthtrack.greendao.MedRecVisitNotes;
import com.ezhealthtrack.one.EzActivities;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.physiciansoap.PhysicianSoapActivityMain;
import com.ezhealthtrack.physiciansoap.PhysicianSoapMiniActivity;
import com.ezhealthtrack.templates.physiotherapist.WebPageActivity;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.LazyListAdapter;

public class VisitNotesAdapter extends LazyListAdapter<MedRecVisitNotes> {
	/* private view holder class */
	private static class ViewHolder {
		private TextView txtConfirmed;
		private Button btnEdit;
		private Button btnDetails;
		private int mPosition;
		private View mButtonsBar;
	}

	private final Context context;
	private View mSelectedRowView;
	private int mSelectedRowIndex;

	public VisitNotesAdapter(final Context context) {
		this.context = context;
		this.mSelectedRowIndex = -1;
	}

	@Override
	public View getView(final int position, View view, final ViewGroup parent) {

		try {
			// to calculate screen size
			DisplayMetrics dm = context.getResources().getDisplayMetrics();
			int width = dm.widthPixels;
			int height = dm.heightPixels;
			int dens = dm.densityDpi;
			double wi = (double) width / (double) dens;
			double hi = (double) height / (double) dens;
			double x = Math.pow(wi, 2);
			double y = Math.pow(hi, 2);
			double screenInches = Math.sqrt(x + y);

			final MedRecVisitNotes rowItem = getItem(position);
			if (view == null) {
				final ViewHolder holder = new ViewHolder();
				final LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater
						.inflate(R.layout.row_visit_notes, parent, false);
				holder.txtConfirmed = (TextView) view
						.findViewById(R.id.txt_confirmed);
				holder.mButtonsBar = view.findViewById(R.id.id_actions);
				holder.btnEdit = (Button) view.findViewById(R.id.btn_edit);
				holder.btnDetails = (Button) view
						.findViewById(R.id.btn_details);
				view.setTag(holder);
			}

			final ViewHolder holder = (ViewHolder) view.getTag();
			holder.mPosition = position;

			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View row) {
					if (mSelectedRowView != null) {
						((ViewHolder) mSelectedRowView.getTag()).mButtonsBar
								.setVisibility(View.GONE);
						mSelectedRowView.setBackgroundResource(R.color.white);
					}

					if (((ViewHolder) row.getTag()).mPosition == mSelectedRowIndex) {
						// row deselected
						mSelectedRowView = null;
						mSelectedRowIndex = -1;
					} else {
						// row selected
						mSelectedRowView = row;
						mSelectedRowIndex = position;
						((ViewHolder) mSelectedRowView.getTag()).mButtonsBar
								.setVisibility(View.VISIBLE);
						mSelectedRowView
								.setBackgroundResource(R.color.lightblue);
					}
				}
			});

			holder.mButtonsBar.setVisibility(View.GONE);
			view.setBackgroundResource(R.color.white);
			if (holder.mPosition == mSelectedRowIndex) {
				mSelectedRowView = view;
				holder.mButtonsBar.setVisibility(View.VISIBLE);
				mSelectedRowView.setBackgroundResource(R.color.lightblue);
			}

			TextView txtIndex = (TextView) view.findViewById(R.id.txt_index);
			txtIndex.setText("" + (position + 1) + ".");

			holder.txtConfirmed.setText(rowItem.getPat_detail() + ", "
					+ EzApp.sdfemmm.format(rowItem.getLast_visit()) + ", "
					+ rowItem.getNumber_of_visit() + " visit, \""
					+ rowItem.getReason() + "\"");

			if (screenInches >= 9) {
				holder.btnEdit.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(final View v) {
						Appointment apt = new Appointment();
						apt.aptDate = rowItem.getApt_date();
						apt.setBkid(rowItem.getBk_id());
						apt.setSiid(rowItem.getSoap_id());
						apt.setPid(rowItem.getPat_id());
						apt.setPfId("0");
						apt.setVisit(rowItem.getNumber_of_visit());
						apt.setReason(rowItem.getReason());
						Intent intent;
						if (EzApp.sharedPref.getString(Constants.DR_SPECIALITY,
								"").equalsIgnoreCase("Dentist")) {
							intent = new Intent(context,
									AddDentistNotesActivity.class);
							AddDentistNotesActivity.appointment = apt;
							context.startActivity(intent);
						} else if (EzApp.sharedPref.getString(
								Constants.DR_SPECIALITY, "").equalsIgnoreCase(
								"physiotherapist")) {
							if (EzUtils.isNetworkAvailable(context)) {
								String url = "https://www.google.co.in";
								WebPageActivity.setWebPageURL(url);

								intent = new Intent(context,
										WebPageActivity.class);
								intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
								context.startActivity(intent);
							} else {
								Toast.makeText(context,
										"No Network Connection Available !!!",
										Toast.LENGTH_SHORT).show();
							}
						} else {
							PhysicianSoapActivityMain.Appointment = apt;
							SoapNotesController.setAppointment(apt);
							EzActivities.startPhysicianSoapActivity(context);
						}

					}
				});

				holder.btnDetails.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(final View v) {
						Appointment apt = new Appointment();
						apt.aptDate = rowItem.getApt_date();
						apt.setBkid(rowItem.getBk_id());
						apt.setSiid(rowItem.getSoap_id());
						apt.setPid(rowItem.getPat_id());
						apt.setPfId("0");
						apt.setVisit(rowItem.getNumber_of_visit());
						apt.setReason(rowItem.getReason());
						Intent intent;

						if (EzApp.sharedPref.getString(Constants.DR_SPECIALITY,
								"").equalsIgnoreCase("dentist")) {
							// intent = new Intent(context,
							// ReferDentistNotesActivity.class);
							// ReferDentistNotesActivity.Appointment = apt;
							// context.startActivity(intent);
						} else {
							// For Physician
							intent = new Intent(context,
									PhysicianSoapMiniActivity.class);
							intent.putExtra("bkid", rowItem.getBk_id());
							intent.putExtra("siid", rowItem.getSoap_id());
							intent.putExtra("type", "past");

							PhysicianSoapMiniActivity.Appointment = apt;
							SoapNotesController.setAppointment(apt);
							context.startActivity(intent);
						}

					}
				});
			} else {
				holder.btnDetails.setVisibility(View.GONE);
				holder.btnEdit.setVisibility(View.GONE);

				final View theView = view;
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						onListItemClicked(theView, position);
					}
				});

			}

		} catch (final Exception e) {
			e.printStackTrace();
		}

		return view;
	}

	public void onListItemClicked(View view, int position) {
		final MedRecVisitNotes rowItem = getItem(position);
		Appointment apt = new Appointment();
		apt.aptDate = rowItem.getApt_date();
		apt.setBkid(rowItem.getBk_id());
		apt.setSiid(rowItem.getSoap_id());
		apt.setPid(rowItem.getPat_id());
		apt.setPfId("0");
		apt.setVisit(rowItem.getNumber_of_visit());
		apt.setReason(rowItem.getReason());
		Intent intent;

		if (mSelectedRowView != null) {
			mSelectedRowView.setBackgroundResource(R.color.white);
		}

		if (((ViewHolder) view.getTag()).mPosition == mSelectedRowIndex) {
			// row deselected
			mSelectedRowView = null;
			mSelectedRowIndex = -1;
		} else {
			// row selected
			mSelectedRowView = view;
			mSelectedRowIndex = position;
			mSelectedRowView.setBackgroundResource(R.color.lightblue);
		}

		intent = new Intent(context, PhysicianSoapMiniActivity.class);
		intent.putExtra("bkid", rowItem.getBk_id());
		intent.putExtra("siid", rowItem.getSoap_id());
		intent.putExtra("type", "past");

		PhysicianSoapMiniActivity.Appointment = apt;
		SoapNotesController.setAppointment(apt);
		context.startActivity(intent);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
}

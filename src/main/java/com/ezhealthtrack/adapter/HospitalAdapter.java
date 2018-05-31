package com.ezhealthtrack.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.model.HospitalModel;
import com.ezhealthtrack.util.Util;

public class HospitalAdapter extends ArrayAdapter<HospitalModel> {
	/* private view holder class */
	private static class ViewHolder {
		private ImageView imgHospital;
		private TextView txtName;
		private TextView txtAddress;
		private TextView txtEmail;
		private TextView txtPhone;
	}

	Context context;

	DashboardActivity activity;

	// private static ArrayList<HospitalModel> objects = new
	// ArrayList<HospitalModel>();

	public HospitalAdapter(final Context context, final int resourceId,
			final ArrayList<HospitalModel> item) {
		super(context, resourceId, item);
		activity = (DashboardActivity) context;
		this.context = context;
		// HospitalAdapter.objects = item;
	}

	@Override
	public View getView(final int position, final View convertView,
			final ViewGroup parent) {

		final HospitalModel rowItem = getItem(position);

		View row = null;
		if (convertView == null) {
			final ViewHolder holder = new ViewHolder();
			final LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.row_hospital, null);
			holder.txtName = (TextView) row.findViewById(R.id.txt_name);
			holder.txtAddress = (TextView) row.findViewById(R.id.txt_address);
			holder.txtEmail = (TextView) row.findViewById(R.id.txt_email);
			holder.txtPhone = (TextView) row.findViewById(R.id.txt_phn);
			holder.imgHospital = (ImageView) row.findViewById(R.id.img_patient);
			row.setTag(holder);
		} else {
			row = convertView;
		}
		final ViewHolder holder = (ViewHolder) row.getTag();
		Util.getImageFromUrl(APIs.IMAGE() + rowItem.getPhoto(),
				DashboardActivity.imgLoader, holder.imgHospital);
		holder.txtName.setText(rowItem.getLocation_title());
		holder.txtAddress.setText(rowItem.getAddress());
		holder.txtEmail.setText(rowItem.getEmail());
		holder.txtPhone.setText(rowItem.getMobile());
		return row;
	}
}
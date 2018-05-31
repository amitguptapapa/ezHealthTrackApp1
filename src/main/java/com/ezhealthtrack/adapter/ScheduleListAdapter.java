package com.ezhealthtrack.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.activity.ScheduleActivityDemo_Daily;
import com.ezhealthtrack.activity.SheduleActivityNew;
import com.ezhealthtrack.fragments.EzGridFragment;
import com.ezhealthtrack.fragments.PatientsListFragment.OnCallback;
import com.ezhealthtrack.greendao.Patient;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Util;

public class ScheduleListAdapter extends EzGridFragmentAdaptor {
	/* private view holder class */
	private static class ViewHolder {
		private TextView txtPatientName;
		private TextView txtDisplayID;
		private TextView txtLastVisit;
		private ImageView imgPatient;
	}

	Context context;
	OnCallback onCallback;
	SimpleDateFormat df = new SimpleDateFormat("EEE, MMM dd', 'yyyy");

	public ScheduleListAdapter(List<Object> dataList, EzGridFragment fragment,
			OnCallback onCallback) {
		super(dataList, fragment, onCallback);
		context = fragment.getActivity();
		this.onCallback = onCallback;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView,
			final ViewGroup parent) {
		super.getView(position);

		final Patient pat = (Patient) mDataList.get(position);
		final ViewHolder mHolder;

		if (convertView == null) {
			final LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = inflater.inflate(R.layout.row_schedule_card, parent,
					false);
			mHolder = new ViewHolder();

			mHolder.txtPatientName = (TextView) convertView
					.findViewById(R.id.txt_name);
			mHolder.txtDisplayID = (TextView) convertView
					.findViewById(R.id.txt_display_id);
			mHolder.txtLastVisit = (TextView) convertView
					.findViewById(R.id.txt_lastvisit_1);
			mHolder.imgPatient = (ImageView) convertView
					.findViewById(R.id.img_patient);

			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}

		// Patient photo/image {
		String url = "";
		if (!Util.isEmptyString(pat.getPphoto())) {
			url = APIs.ROOT() + "/documents/show/id/" + pat.getPphoto();
		} else {
			url = APIs.URL() + "/img/patient.jpg";
		}
		Util.getImageFromUrl(url, DashboardActivity.imgLoader,
				mHolder.imgPatient);

		// }

		mHolder.txtPatientName
				.setText(pat.getPfn() + " " + pat.getPmn() + " " + pat.getPln()
						+ " , " + pat.getPage() + "/" + pat.getPgender());
		mHolder.txtDisplayID.setText(pat.getDisplay_id());

		try {
			if (pat.getLastvisit() == null)
				mHolder.txtLastVisit.setText("");
			else
				mHolder.txtLastVisit
						.setText("" + df.format(pat.getLastvisit()));
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				if (EzApp.sharedPref.getString(Constants.USER_TYPE, "")
						.equalsIgnoreCase("D")) {
					// final Intent intent = new Intent(context,
					// SheduleActivity.class);
					// intent.putExtra("pid", pat.getPid());
					// intent.putExtra("fid", pat.getFid());
					final Intent intent = new Intent(context,
							ScheduleActivityDemo_Daily.class);
					context.startActivity(intent);
				} else if (EzApp.sharedPref.getString(Constants.USER_TYPE, "")
						.equalsIgnoreCase("LT")) {
					final Intent intent = new Intent(context,
							SheduleActivityNew.class);
					intent.putExtra("pid", pat.getPid());
					intent.putExtra("fid", pat.getFid());
					intent.putExtra("type", "schedule");
					context.startActivity(intent);
				}
			}
		});
		return convertView;
	}
}
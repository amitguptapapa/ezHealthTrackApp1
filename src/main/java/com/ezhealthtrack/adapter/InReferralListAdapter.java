package com.ezhealthtrack.adapter;

import java.util.ArrayList;
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
import com.ezhealthtrack.activity.SheduleActivity;
import com.ezhealthtrack.controller.PatientController;
import com.ezhealthtrack.fragments.EzGridFragment;
import com.ezhealthtrack.fragments.PatientsListFragment.OnCallback;
import com.ezhealthtrack.greendao.Patient;
import com.ezhealthtrack.greendao.PatientDao.Properties;
import com.ezhealthtrack.model.InRefferalModel;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.Util;

public class InReferralListAdapter extends EzGridFragmentAdaptor {
	/* private view holder class */
	private static class ViewHolder {
		private TextView mPatientName;
		private TextView mReferredBy;
		private TextView mDate;
		private TextView mReason;
		private TextView mEmail;
		private TextView mPhone;
		private TextView mID;
		private ImageView mAvatar;
		private View mIndicator;
	}

	Context context;

	public InReferralListAdapter(List<Object> dataList,
			EzGridFragment fragment, OnCallback onCallback) {
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

		final InRefferalModel rowItem = (InRefferalModel) mDataList
				.get(position);
		final ViewHolder mHolder;

		if (convertView == null) {

			final LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row_referral_in_card,
					parent, false);
			mHolder = new ViewHolder();

			mHolder.mPatientName = (TextView) convertView
					.findViewById(R.id.txt_name);
			mHolder.mReferredBy = (TextView) convertView
					.findViewById(R.id.txt_referredby_1);
			mHolder.mDate = (TextView) convertView
					.findViewById(R.id.txt_date_display);
			mHolder.mReason = (TextView) convertView
					.findViewById(R.id.txt_reason_1);
			mHolder.mEmail = (TextView) convertView
					.findViewById(R.id.txt_email);
			mHolder.mPhone = (TextView) convertView.findViewById(R.id.txt_phn);
			mHolder.mAvatar = (ImageView) convertView
					.findViewById(R.id.img_patient);
			mHolder.mID = (TextView) convertView.findViewById(R.id.txt_id);
			mHolder.mIndicator = (View) convertView
					.findViewById(R.id.img_indicator);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}

		// Get Patient Photo/Image | start{
		String url = "";
		if (!Util.isEmptyString(rowItem.getPhoto())) {
			url = APIs.ROOT() + "/documents/show/id/" + rowItem.getPhoto();
		} else {
			url = APIs.URL() + "/img/patient.jpg";
		}
		// end }

		Util.getImageFromUrl(url, DashboardActivity.imgLoader, mHolder.mAvatar);
		mHolder.mPatientName.setText(rowItem.getPfn() + " " + rowItem.getPln()
				+ ", " + rowItem.getAge() + " / " + rowItem.getGender());
		Log.i("dr name", rowItem.getFromName());
		mHolder.mReferredBy.setText(rowItem.getFromName());
		mHolder.mDate.setText(rowItem.getcdate());
		mHolder.mReason.setText(rowItem.getReferReason());
		mHolder.mEmail.setText(rowItem.getEmail());
		mHolder.mPhone.setText(rowItem.getMobile());

		// Temporarily disabled {
		mHolder.mID.setVisibility(View.GONE);
		// mHolder.mID.setText(rowItem.getpID());
		// }

		if (rowItem.getRefFlag().equals("0")) {
			mHolder.mIndicator.setVisibility(View.VISIBLE);
		} else {
			mHolder.mIndicator.setVisibility(View.GONE);
		}

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				if (!rowItem.getRefFlag().equals("0")) {
					EzUtils.showLong("The patient can not be scheduled");
					return;
				}
				ArrayList<Patient> arrPat = (ArrayList<Patient>) EzApp.patientDao
						.queryBuilder()
						.where(Properties.Pid.eq(rowItem.getPatId()),
								Properties.Fid.eq(rowItem.getFamId())).list();
				if (arrPat.size() == 0) {
					Patient pateint = new Patient();
					pateint.setId((long) (Integer.parseInt(rowItem.getPatId()) + 100000 * Integer
							.parseInt(rowItem.getFamId())));
					pateint.setPfn(rowItem.getPfn());
					pateint.setPln(rowItem.getPln());
					pateint.setPphoto(rowItem.getPhoto());
					pateint.setPid(rowItem.getPatId());
					pateint.setFid(rowItem.getFamId());

					if (Util.isEmptyString(pateint.getPmn()))
						pateint.setP_detail(pateint.getPfn() + " "
								+ pateint.getPln());
					else
						pateint.setP_detail(pateint.getPfn() + " "
								+ pateint.getPmn() + " " + pateint.getPln());

					PatientController.updatePatient(pateint);

				}
				final Intent intent = new Intent(context, SheduleActivity.class);
				intent.putExtra("pid", rowItem.getPatId());
				intent.putExtra("fid", rowItem.getFamId());
				intent.putExtra("type", "in referral");
				intent.putExtra("ref-ep", rowItem.getEpId());
				intent.putExtra("ref-id", rowItem.getRefId());
				context.startActivity(intent);

			}
		});

		return convertView;
	}

	// Search Filter start{

	// private static ArrayList<InRefferalModel> objects = new
	// ArrayList<InRefferalModel>();
	// private ArrayList<InRefferalModel> confirmedList = new
	// ArrayList<InRefferalModel>();

	// Filter filter = new Filter() {
	//
	// @Override
	// protected FilterResults performFiltering(final CharSequence constraint) {
	// final FilterResults filterResults = new FilterResults();
	// final ArrayList<InRefferalModel> tempList = new
	// ArrayList<InRefferalModel>();
	// // constraint is the result from text you want to filter against.
	// // objects is your data set you will filter from
	// if ((constraint != null) && (confirmedList != null)) {
	// final int length = confirmedList.size();
	// int i = 0;
	// while (i < length) {
	// final InRefferalModel item = confirmedList.get(i);
	//
	// if ((item.getFromName().toLowerCase(Locale.US))
	// .contains(constraint.toString().toLowerCase(
	// Locale.US))) {
	// tempList.add(item);
	// }
	//
	// i++;
	// }
	// // following two lines is very important
	// // as publish result can only take FilterResults objects
	// filterResults.values = tempList;
	// filterResults.count = tempList.size();
	// }
	// return filterResults;
	// }
	//
	// @Override
	// protected void publishResults(final CharSequence constraint,
	// final FilterResults results) {
	// try {
	// InReferralListAdapter.objects.clear();
	// InReferralListAdapter.objects
	// .addAll((Collection<? extends InRefferalModel>) results.values);
	// notifyDataSetChanged();
	// ((TextView) ((Activity) context).findViewById(R.id.txt_count))
	// .setText("Total Results: " + getCount());
	// } catch (Exception e) {
	//
	// }
	// }
	// };

	// public Filter getFilter() {
	// return filter;
	// }

	// }end

}
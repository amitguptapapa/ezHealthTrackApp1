package com.ezhealthtrack.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.fragments.EzGridFragment;
import com.ezhealthtrack.fragments.PatientsListFragment.OnCallback;
import com.ezhealthtrack.model.OutReferralModel;
import com.ezhealthtrack.util.Util;

public class OutReferralListAdapter extends EzGridFragmentAdaptor {
	/* private view holder class */
	private static class ViewHolder {
		private TextView mPatientName;
		private TextView mReferred;
		private TextView mDate;
		private TextView mEmail;
		private TextView mPhone;
		private TextView mID;
		private ImageView mAvatar;
	}

	Context context;

	public OutReferralListAdapter(List<Object> dataList,
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
		
		final OutReferralModel rowItem = (OutReferralModel) mDataList
				.get(position);
		final ViewHolder mHolder;

		if (convertView == null) {
			final LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row_referral_out_card,
					parent, false);
			mHolder = new ViewHolder();

			mHolder.mPatientName = (TextView) convertView
					.findViewById(R.id.txt_name);
			mHolder.mReferred = (TextView) convertView
					.findViewById(R.id.txt_referredby_1);
			mHolder.mDate = (TextView) convertView
					.findViewById(R.id.txt_date_display);
			mHolder.mEmail = (TextView) convertView
					.findViewById(R.id.txt_email);
			mHolder.mPhone = (TextView) convertView.findViewById(R.id.txt_phn);
			mHolder.mID = (TextView) convertView.findViewById(R.id.txt_id);
			mHolder.mAvatar = (ImageView) convertView
					.findViewById(R.id.img_patient);
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
		// }end
		
		Util.getImageFromUrl(url, DashboardActivity.imgLoader, mHolder.mAvatar);
		mHolder.mPatientName.setText(rowItem.getPfn() + " " + rowItem.getPln()
				+ ", " + rowItem.getAge() + " / " + rowItem.getGender());
		mHolder.mReferred.setText(rowItem.getTo_name());
		mHolder.mDate.setText(rowItem.getcdate());
		mHolder.mEmail.setText(rowItem.getEmail());
		mHolder.mPhone.setText(rowItem.getMobile());

		// Temporarily disabled {
		mHolder.mID.setVisibility(View.GONE);
		// mHolder.mID.setText(rowItem.getpID());
		// }

		return convertView;
	}

	// Search Filter start{

	// private static ArrayList<Object> objects = new ArrayList<Object>();
	// private ArrayList<OutReferralModel> confirmedList = new
	// ArrayList<OutReferralModel>();
	//
	// Filter filter = new Filter() {
	//
	// @Override
	// protected FilterResults performFiltering(final CharSequence constraint) {
	// final FilterResults filterResults = new FilterResults();
	// final ArrayList<OutReferralModel> tempList = new
	// ArrayList<OutReferralModel>();
	// // constraint is the result from text you want to filter against.
	// // objects is your data set you will filter from
	// if ((constraint != null) && (confirmedList != null)) {
	// final int length = confirmedList.size();
	// int i = 0;
	// while (i < length) {
	// final OutReferralModel item = confirmedList.get(i);
	//
	// if ((item.getTo_name().toLowerCase(Locale.US))
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
	// @SuppressWarnings("unchecked")
	// @Override
	// protected void publishResults(final CharSequence constraint,
	// final FilterResults results) {
	// OutReferralListAdapter.objects.clear();
	// OutReferralListAdapter.objects
	// .addAll((Collection<? extends OutReferralModel>) results.values);
	// notifyDataSetChanged();
	//
	// }
	// };
	//
	// public OutReferralListAdapter(List<Object> dataList,
	// EzGridFragment fragment, OnCallback onCallback) {
	// super(dataList, fragment, onCallback);
	// context = fragment.getActivity();
	// this.onCallback = onCallback;
	// notifyDataSetChanged();
	// }
	//
	// public Filter getFilter() {
	// return filter;
	// }

	// }end
}
package com.ezhealthtrack.adapter;

import java.util.ArrayList;
import java.util.Collection;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.activity.EditAssistantActivity;
import com.ezhealthtrack.fragments.AssistantSchedulePlanFragment;
import com.ezhealthtrack.model.AssistantModel;
import com.ezhealthtrack.util.Util;

public class AssistantAdapter extends ArrayAdapter<AssistantModel> {
	/* private view holder class */
	private static class ViewHolder {
		private ImageView imgAssistant;
		private TextView txtName;
		private TextView txtAddress;
		private TextView txtEmail;
		private TextView txtPhone;
		private Button btnEdit;
		private Button btnSp;
		private ImageView img;
	}

	Context context;

	DashboardActivity activity;

	private static ArrayList<AssistantModel> objects = new ArrayList<AssistantModel>();

	private ArrayList<AssistantModel> confirmedList = new ArrayList<AssistantModel>();

	Filter filter = new Filter() {

		@Override
		protected FilterResults performFiltering(final CharSequence constraint) {
			final FilterResults filterResults = new FilterResults();
			final ArrayList<AssistantModel> tempList = new ArrayList<AssistantModel>();
			// constraint is the result from text you want to filter against.
			// objects is your data set you will filter from
			if ((constraint != null) && (confirmedList != null)) {
				final int length = confirmedList.size();
				int i = 0;
				while (i < length) {
					final AssistantModel item = confirmedList.get(i);

					if ((item.getAssist_name().toLowerCase()).contains(constraint
							.toString().toLowerCase())) {
						tempList.add(item);
					}

					i++;
				}
				// following two lines is very important
				// as publish result can only take FilterResults objects
				filterResults.values = tempList;
				filterResults.count = tempList.size();
			}
			return filterResults;
		}

		@Override
		protected void publishResults(final CharSequence constraint,
				final FilterResults results) {
			objects.clear();
			objects.addAll((Collection<? extends AssistantModel>) results.values);
			notifyDataSetChanged();

		}
	};

	public AssistantAdapter(final Context context, final int resourceId,
			final ArrayList<AssistantModel> item) {
		super(context, resourceId, objects);
		activity = (DashboardActivity) context;
		this.context = context;
		confirmedList = item ;
		objects.clear();
		objects.addAll(item);
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, final View convertView,
			final ViewGroup parent) {

		final AssistantModel rowItem = getItem(position);

		View row = null;
		if (convertView == null) {
			final ViewHolder holder = new ViewHolder();
			final LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.row_assistant, null);
			holder.txtName = (TextView) row.findViewById(R.id.txt_name);
			holder.txtAddress = (TextView) row.findViewById(R.id.txt_address);
			holder.txtEmail = (TextView) row.findViewById(R.id.txt_email);
			holder.txtPhone = (TextView) row.findViewById(R.id.txt_phn);
			holder.imgAssistant = (ImageView) row
					.findViewById(R.id.img_patient);
			holder.btnEdit = (Button) row.findViewById(R.id.btn_edit);
			holder.btnSp = (Button) row.findViewById(R.id.btn_scheduleplan);
			holder.img = (ImageView) row.findViewById(R.id.img_patient);
			row.setTag(holder);
		} else {
			row = convertView;
		}
		final ViewHolder holder = (ViewHolder) row.getTag();
		String url = "";
		if (!Util.isEmptyString(rowItem.getPhoto())) {
			url = APIs.ROOT() + "/documents/show/id/" + rowItem.getPhoto();
		} else {
			url = APIs.URL() + "/img/patient.jpg";
		}
		Util.getImageFromUrl(url, DashboardActivity.imgLoader,
				holder.img);
		holder.txtName.setText(rowItem.getAssist_name() + ","
				+ rowItem.getAge());
		holder.txtAddress.setText(rowItem.getAddress());
		holder.txtEmail.setText(rowItem.getEmail());
		holder.txtPhone.setText(rowItem.getMobile());
		holder.btnEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				final Intent intent = new Intent(context,
						EditAssistantActivity.class);
				intent.putExtra("id", rowItem.getAsst_id());
				context.startActivity(intent);

			}
		});
		holder.btnSp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AssistantSchedulePlanFragment.asstId = rowItem.getAsst_id();
				AssistantSchedulePlanFragment.asst_name = rowItem
						.getAssist_name();
				((DashboardActivity) getContext()).spAssistant();
			}
		});
		return row;
	}
	
	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return filter;
	}
}
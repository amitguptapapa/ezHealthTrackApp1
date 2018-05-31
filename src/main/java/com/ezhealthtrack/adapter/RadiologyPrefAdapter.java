package com.ezhealthtrack.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Filter;
import android.widget.Filterable;

import com.ezhealthtrack.R;
import com.ezhealthtrack.model.RadiologyModel;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.Log;

public class RadiologyPrefAdapter extends ArrayAdapter<RadiologyModel>
		implements Filterable {

	private static class ViewHolder {
		CheckBox cb;
	}

	private final Context context;
	private static ArrayList<RadiologyModel> objects = new ArrayList<RadiologyModel>();

	private List<RadiologyModel> mDataList;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Log.i("RPA:getView()", "Position: " + position);

		final RadiologyModel model = getItem(position);

		View row = null;
		if (convertView == null) {
			final ViewHolder holder = new ViewHolder();
			final LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.row_checked_item, null);
			holder.cb = (CheckBox) row.findViewById(R.id.checked_item);

			row.setTag(holder);
		} else {
			row = convertView;
		}

		final ViewHolder holder = (ViewHolder) row.getTag();

		holder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(final CompoundButton buttonView,
					final boolean isChecked) {
				model.setIsChecked(isChecked);
				EzUtils.showLong("STATE: " + isChecked);
			}
		});

		holder.cb.setText(model.getTestName());
		holder.cb.setChecked(model.getIsChecked());
		return row;
	}

	Filter filter = new Filter() {
		List<RadiologyModel> tempList = new ArrayList<RadiologyModel>();

		@Override
		protected FilterResults performFiltering(final CharSequence constraint) {
			final FilterResults filterResults = new FilterResults();
			final ArrayList<RadiologyModel> tempList = new ArrayList<RadiologyModel>();
			// constraint is the result from text you want to filter against.
			// objects is your data set you will filter from
			if ((constraint != null) && (mDataList != null)) {
				final int length = mDataList.size();
				int i = 0;
				while (i < length) {
					final RadiologyModel item = mDataList.get(i);
					for (RadiologyModel p : mDataList) {
						if (p.getTestName()
								.toLowerCase(Locale.ENGLISH)
								.equalsIgnoreCase(
										constraint.toString().toLowerCase(
												Locale.ENGLISH))
						// && p.getCodcat().equalsIgnoreCase(
						// item.getCodid())
						// && p.getGid().equalsIgnoreCase(item.getGid())
								)
							tempList.add(p);
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
			try {
				RadiologyPrefAdapter.objects.clear();
				RadiologyPrefAdapter.objects
						.addAll((Collection<? extends RadiologyModel>) results.values);
				notifyDataSetChanged();
			} catch (Exception e) {
				Log.e("", e);
			}
		}
	};

	public RadiologyPrefAdapter(Context context, int resource,
			List<RadiologyModel> dataList) {
		super(context, resource, dataList);
		this.context = context;
		this.mDataList = dataList;
	}

}

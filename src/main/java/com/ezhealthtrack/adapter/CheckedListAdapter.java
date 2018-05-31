package com.ezhealthtrack.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.ezhealthtrack.R;

public class CheckedListAdapter extends ArrayAdapter<String> {
	/* private view holder class */
	private static class ViewHolder {
		private CheckBox cb;
	}

	Context context;
	Boolean isEnabled = true;

	ArrayList<String> arrString;

	public CheckedListAdapter(final Context context, final int resourceId,
			final List<String> item, final ArrayList<String> arrString) {
		super(context, resourceId, item);

		this.context = context;
		this.arrString = arrString;
	}

	public CheckedListAdapter(final Context context, final int resourceId,
			final String[] item, final ArrayList<String> arrString) {
		super(context, resourceId, item);

		this.context = context;
		this.arrString = arrString;
	}

	public CheckedListAdapter(
			final Context context, final int resourceId,
			final String[] item, final ArrayList<String> arrString, Boolean isEnabled) {
		super(context, resourceId, item);

		this.context = context;
		this.arrString = arrString;
		this.isEnabled = isEnabled;
	}

	@Override
	public View getView(final int position, final View convertView,
			final ViewGroup parent) {

		final String rowItem = getItem(position);

		View row = null;
		if (true) {// convertView == null) {
			final ViewHolder holder = new ViewHolder();
			final LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.row_checked_item, null);
			holder.cb = (CheckBox) row.findViewById(R.id.checked_item);
			row.setTag(holder);
		} else {
			row = convertView;
		}
		final ViewHolder holder = (ViewHolder) row.getTag();
		holder.cb.setText(rowItem);
		holder.cb.setChecked(arrString.contains(rowItem));
		holder.cb.setEnabled(isEnabled);
		holder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(final CompoundButton buttonView,
					final boolean isChecked) {
				Log.i(arrString.toString(), "" + arrString.size());
				if (isChecked) {
					arrString.add(rowItem);

				} else {
					arrString.remove(rowItem);
				}
				notifyDataSetChanged();

			}
		});
		return row;
	}

}

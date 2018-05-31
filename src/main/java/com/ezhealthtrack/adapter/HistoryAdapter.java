package com.ezhealthtrack.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.db.DatabaseHelper;
import com.ezhealthtrack.greendao.Appointment;
import com.ezhealthtrack.model.PatientShow;
import com.ezhealthtrack.model.State;
import com.ezhealthtrack.model.ToStep;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.Log;

public class HistoryAdapter extends ArrayAdapter<Appointment> implements
		Filterable {
	/* private view holder class */
	public static class ViewHolder {
		public boolean showActions;
		private TextView txtHistory;
		private ImageView imgHistory;
		private int mPosition;
	}

	private final Context mContext;
	private static ArrayList<Appointment> objects = new ArrayList<Appointment>();

	private ArrayList<State> arrState;
	private ArrayList<ToStep> arrToStep;
	private ArrayList<Appointment> historyList = new ArrayList<Appointment>();

	private EzListItemClickListner mListner;
	private int mSelectedRowIndex;
	private View mSelectedRowView;

	public interface EzListItemClickListner {
		public void onListItemClick(boolean selected);
	}

	Filter filter = new Filter() {

		@Override
		protected FilterResults performFiltering(final CharSequence constraint) {
			final FilterResults filterResults = new FilterResults();
			final ArrayList<Appointment> tempList = new ArrayList<Appointment>();
			// constraint is the result from text you want to filter against.
			// objects is your data set you will filter from
			if ((constraint != null) && (historyList != null)) {
				final int length = historyList.size();
				int i = 0;
				while (i < length) {
					final Appointment item = historyList.get(i);
					PatientShow pShow = new PatientShow();
					for (PatientShow p : DashboardActivity.arrPatientShow) {
						if (p.getPId().equalsIgnoreCase(item.getPid())
								&& p.getPfId().equalsIgnoreCase(item.getPfId()))
							pShow = p;
					}

					if ((pShow.getPfn().toLowerCase(Locale.US) + " " + pShow
							.getPln().toLowerCase(Locale.US))
							.contains(constraint.toString().toLowerCase(
									Locale.US))) {
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

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(final CharSequence constraint,
				final FilterResults results) {
			try {
				HistoryAdapter.objects.clear();
				HistoryAdapter.objects
						.addAll((Collection<? extends Appointment>) results.values);
				((TextView) ((Activity) mContext).findViewById(R.id.txt_count))
						.setText(Html.fromHtml("<b>" + "Total Results: "
								+ getCount() + "</b>"));
				notifyDataSetChanged();
			} catch (Exception e) {
				Log.e("publishResults", e);
			}
		}
	};

	public HistoryAdapter(final Context context, final int resourceId,
			final ArrayList<Appointment> item, EzListItemClickListner listner) {
		super(context, resourceId, HistoryAdapter.objects);

		this.mListner = listner;
		this.mContext = context;
		this.mSelectedRowIndex = -1;

		try {
			historyList = item;
			HistoryAdapter.objects.clear();
			HistoryAdapter.objects.addAll(item);
			arrState = (ArrayList<State>) DatabaseHelper.db.getAllState();
			arrToStep = (ArrayList<ToStep>) DatabaseHelper.db.getAllToStep();
		} catch (Exception e) {
			Log.e("HistoryAdapter", e);
		}
	}

	@Override
	public Filter getFilter() {
		return filter;
	}

	@Override
	public View getView(final int position, View view, final ViewGroup parent) {

		final Appointment rowItem = getItem(position);

		if (view == null) {
			final ViewHolder holder = new ViewHolder();
			final LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.row_history, parent, false);
			holder.txtHistory = (TextView) view.findViewById(R.id.txt_history);
			holder.imgHistory = (ImageView) view.findViewById(R.id.img_history);
			view.setTag(holder);
		}
		final ViewHolder holder = (ViewHolder) view.getTag();
		holder.mPosition = position;
		holder.imgHistory.setImageResource(R.drawable.cancel_appointment);

		State state = new State();
		// Log.i("", new Gson().toJson(rowItem));
		for (State s : arrState) {
			if (s.getName().equalsIgnoreCase(rowItem.getWistep()))
				state = s;
		}
		for (final ToStep toStep : arrToStep) {
			try {
				if (toStep.getId() == state.getId()) {
					if (toStep.getName().equals("reschedule")) {
						// holder.btnReSchedule.setVisibility(View.VISIBLE);
						holder.showActions = false;
					}
					if (toStep.getName().equals("followup")) {
						holder.showActions = true;
						holder.imgHistory
								.setImageResource(R.drawable.confirm_appointment);
					}
				}
			} catch (Exception e) {

			}
		}

		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View row) {
				ViewHolder holder = (ViewHolder) row.getTag();
				if (holder.showActions == true) {

					if (mSelectedRowView != null) {
						mSelectedRowView
								.setBackgroundResource(android.R.color.transparent);
						((ViewHolder) mSelectedRowView.getTag()).txtHistory
								.setTextColor(0xFF464646);
					}

					if (((ViewHolder) row.getTag()).mPosition == mSelectedRowIndex) {
						// row deselected
						mSelectedRowView = null;
						mSelectedRowIndex = -1;
						mListner.onListItemClick(false);
					} else {
						// row selected
						mSelectedRowView = row;
						mSelectedRowIndex = position;
						mSelectedRowView
								.setBackgroundResource(R.color.accent_color_light);
						holder.txtHistory.setTextColor(Color.WHITE);
						mListner.onListItemClick(true);
					}
				} else {
					EzUtils.showLong("Patient not checked in");
				}
			}
		});

		view.setBackgroundResource(android.R.color.transparent);
		holder.txtHistory.setTextColor(0xFF464646);
		if (holder.mPosition == mSelectedRowIndex) {
			view.setBackgroundResource(R.color.accent_color_light);
			holder.txtHistory.setTextColor(Color.WHITE);
		}

		PatientShow pat1 = new PatientShow();
		for (PatientShow pats : DashboardActivity.arrPatientShow) {
			if (pats.getPfId().equals(rowItem.getPfId())
					&& pats.getPId().equals(rowItem.getPid()))
				pat1 = pats;
		}
		final SimpleDateFormat df = new SimpleDateFormat(
				"' @ 'hh:mm a' on 'EEE, MMM dd', 'yyyy");
		for (PatientShow p : DashboardActivity.arrPatientShow) {
			if (p.getPId().equalsIgnoreCase(rowItem.getPid())
					&& p.getPfId().equalsIgnoreCase(rowItem.getPfId()))
				holder.txtHistory.setText(Html.fromHtml(p.getPfn() + " "
						+ p.getPln() + ", " + pat1.getAge() + "/"
						+ pat1.getGender() + ", " + pat1.getPType()
						+ " Patient" + df.format(rowItem.aptDate) + " for "
						+ "<b>" + "'" + rowItem.getReason() + "'" + "</b>"));
		}
		if (!rowItem.getVisit().contains("1")) {
			holder.txtHistory.append(" (Followup)");
		}

		return view;
	}

	public void notifyItems() {
		historyList.clear();
		historyList.addAll(DashboardActivity.arrHistoryPatients);
		notifyDataSetChanged();
	}

	public Appointment getSelectedAppointment() {

		return DashboardActivity.arrHistoryPatients.get(mSelectedRowIndex);
	}
}
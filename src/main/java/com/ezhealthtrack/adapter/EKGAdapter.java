package com.ezhealthtrack.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.db.DatabaseHelper;
import com.ezhealthtrack.greendao.Appointment;
import com.ezhealthtrack.greendao.Patient;
import com.ezhealthtrack.model.PatientShow;
import com.ezhealthtrack.model.State;
import com.ezhealthtrack.model.ToStep;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Log;

public class EKGAdapter extends ArrayAdapter<Appointment> implements Filterable {
	/* private view holder class */
	private static class ViewHolder {
		private TextView txtConfirmed;
		private RelativeLayout rlActions;
		private Button btnDetails;
		private Patient patient = new Patient();

	}

	private final Context context;

	private final SharedPreferences sharedPref;
	private static ArrayList<Appointment> objects = new ArrayList<Appointment>();
	private ArrayList<Appointment> confirmedList = new ArrayList<Appointment>();
	private ArrayList<State> arrState = new ArrayList<State>();
	private ArrayList<ToStep> arrToStep = new ArrayList<ToStep>();

	Filter filter = new Filter() {

		@Override
		protected FilterResults performFiltering(final CharSequence constraint) {
			final FilterResults filterResults = new FilterResults();
			final ArrayList<Appointment> tempList = new ArrayList<Appointment>();
			if ((constraint != null) && (confirmedList != null)) {
				final int length = confirmedList.size();
				int i = 0;
				while (i < length) {
					final Appointment item = confirmedList.get(i);
					PatientShow pShow = new PatientShow();
					for (PatientShow p : DashboardActivity.arrPatientShow) {
						if (p.getPId().equalsIgnoreCase(item.getPid())
								&& p.getPfId().equalsIgnoreCase(item.getPfId()))
							pShow = p;
					}

					if ((pShow.getPfn().toLowerCase() + " " + pShow.getPln()
							.toLowerCase()).contains(constraint.toString()
							.toLowerCase())) {
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
			try {
				EKGAdapter.objects.clear();
				EKGAdapter.objects
						.addAll((Collection<? extends Appointment>) results.values);
				notifyDataSetChanged();
				((TextView) ((Activity) context)
						.findViewById(R.id.txt_count_confirmed)).setText(Html
						.fromHtml("<b>" + "Total Results: " + getCount()
								+ "</b>"));
				// Log.i("", ConfirmedAdapter.objects.toString());
			} catch (Exception e) {
				Log.e("", e);
			}

		}
	};

	public EKGAdapter(final Context context, final int resourceId,
			final ArrayList<Appointment> item) {
		super(context, resourceId, EKGAdapter.objects);
		sharedPref = context.getSharedPreferences(Constants.EZ_SHARED_PREF,
				Context.MODE_PRIVATE);
		this.context = context;
		confirmedList = item;
		EKGAdapter.objects.clear();
		EKGAdapter.objects.addAll(item);
		arrToStep = (ArrayList<ToStep>) DatabaseHelper.db.getAllToStep();
		arrState = (ArrayList<State>) DatabaseHelper.db.getAllState();
	}

	@Override
	public Filter getFilter() {
		return filter;
	}

	@Override
	public View getView(final int position, final View convertView,
			final ViewGroup parent) {
		View row = null;
		try {
			final Appointment rowItem = EKGAdapter.objects.get(position);
			if (convertView == null) {
				final ViewHolder holder = new ViewHolder();
				final LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.row_confirmed, null);
				holder.txtConfirmed = (TextView) row
						.findViewById(R.id.txt_confirmed);
				holder.rlActions = (RelativeLayout) row
						.findViewById(R.id.rl_actions);
				holder.btnDetails = (Button) row.findViewById(R.id.btn_details);

				row.setTag(holder);
			} else {
				row = convertView;
			}
			final ViewHolder holder = (ViewHolder) row.getTag();
			State state = new State();
			for (State s : arrState) {
				if (s.getName().equals(rowItem.getWistep()))
					state = s;
			}
			// Log.i("Wi Step", rowItem.getWistep());

			Patient patients = new Patient();
			Date currentDate = Calendar.getInstance().getTime();
			currentDate.setHours(0);
			currentDate.setMinutes(0);
			currentDate.setSeconds(0);
			Date tomorrowDate = new Date(currentDate.getTime() + 24 * 60
					* 60000 - 60000);

			final SimpleDateFormat df = new SimpleDateFormat(
					"EEE, MMM dd', 'yyyy");
			// for (PatientShow p : DashboardActivity.arrPatientShow) {
			// if (p.getPId().equalsIgnoreCase(rowItem.getPid())
			// && p.getPfId().equalsIgnoreCase(rowItem.getPfId()))
			holder.txtConfirmed.setText(Html.fromHtml(patients.getPfn() + " "
					+ patients.getPln() + ", " + patients.getage() + "/"
					+ patients.getPgender() + ", " + "Last Visit : "
					+ df.format(rowItem.aptDate)));

			// on click temporarily disabled

			// holder.txtConfirmed.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(final View v) {
			// if (holder.rlActions.getVisibility() == View.GONE) {
			// holder.rlActions.setVisibility(View.VISIBLE);
			// } else {
			// holder.rlActions.setVisibility(View.GONE);
			// }
			//
			// }
			// });

			holder.btnDetails.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(final View v) {

				}
			});

		} catch (final Exception e) {
			e.printStackTrace();
		}
		return row;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	public void notifyItems() {
		confirmedList.clear();
		confirmedList.addAll(DashboardActivity.arrConfirmedPatients);
		notifyDataSetChanged();
	}
}

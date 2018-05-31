package com.ezhealthtrack.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ezhealthtrack.R;
import com.ezhealthtrack.DentistSoap.ReferDentistNotesActivity;
import com.ezhealthtrack.DentistSoap.Model.PastVisitModel;
import com.ezhealthtrack.physiciansoap.ReferPhysicianSoapActivity;
import com.ezhealthtrack.util.Constants;

public class PastVisitAdapter extends ArrayAdapter<PastVisitModel> {
	/* private view holder class */
	private static class ViewHolder {
		private TextView txtConfirmed;
	}

	private final Context context;

	public PastVisitAdapter(final Context context, final int resourceId,
			final List<PastVisitModel> item) {
		super(context, resourceId, item);
		this.context = context;
	}

	@Override
	public View getView(final int position, final View convertView,
			final ViewGroup parent) {

		final PastVisitModel rowItem = getItem(position);

		View row = null;
		if (convertView == null) {
			final ViewHolder holder = new ViewHolder();
			final LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.row_pastvisits, null);
			holder.txtConfirmed = (TextView) row
					.findViewById(R.id.txt_confirmed);
			row.setTag(holder);
		} else {
			row = convertView;
		}
		final ViewHolder holder = (ViewHolder) row.getTag();
		final SimpleDateFormat df = new SimpleDateFormat(
				"hh:mm a 'on' EEE MMM dd', 'yyyy");
		final SimpleDateFormat df1 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		try {
			holder.txtConfirmed
					.setText(Html.fromHtml("@ "
							+ df.format(df1.parse(rowItem.aptDate + " "
									+ rowItem.time)) + " for \"<b>"
							+ rowItem.reason + "</b>" + "\""));
		} catch (final ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		holder.txtConfirmed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				final Intent intent;
				final SharedPreferences sharedPref = context
						.getApplicationContext().getSharedPreferences(
								Constants.EZ_SHARED_PREF, Context.MODE_PRIVATE);
				if (sharedPref.getString(Constants.DR_SPECIALITY, "")
						.equalsIgnoreCase("dentist")) {
					intent = new Intent(context,
							ReferDentistNotesActivity.class);
					intent.putExtra("bkid", rowItem.bkId);
					intent.putExtra("siid", rowItem.siId);
					intent.putExtra("type", "past");
					context.startActivity(intent);
				} else {
					intent = new Intent(context,
							ReferPhysicianSoapActivity.class);
					intent.putExtra("bkid", rowItem.bkId);
					intent.putExtra("siid", rowItem.siId);
					intent.putExtra("type", "past");
					context.startActivity(intent);
				}

			}
		});
		return row;
	}
}
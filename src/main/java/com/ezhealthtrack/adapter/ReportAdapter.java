package com.ezhealthtrack.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.greendao.MedRecVisitNotes;
import com.ezhealthtrack.util.LazyListAdapter;
import com.ezhealthtrack.util.Util;

public class ReportAdapter extends LazyListAdapter<MedRecVisitNotes> {
	/* private view holder class */
	private static class ViewHolder {
		private TextView txtConfirmed;
		private RelativeLayout rlActions;
		private Button btnDetails;

	}

	private final Context context;

	public ReportAdapter(final Context context) {
		this.context = context;
	}

	@Override
	public View getView(final int position, final View convertView,
			final ViewGroup parent) {
		View row = null;
		try {
			final MedRecVisitNotes rowItem = getItem(position);
			if (convertView == null) {
				final ViewHolder holder = new ViewHolder();
				final LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.row_report, null);
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
			holder.txtConfirmed
					.setText(Html.fromHtml("<b>On</b> "
							+ EzApp.sdfemmm.format(rowItem
									.getApt_date())));
			holder.txtConfirmed.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(final View v) {
					if (holder.rlActions.getVisibility() == View.GONE) {
						holder.rlActions.setVisibility(View.VISIBLE);
					} else {
						holder.rlActions.setVisibility(View.GONE);
					}

				}
			});

			holder.btnDetails.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					dialogDetailReport(context);

				}
			});

		} catch (final Exception e) {
			e.printStackTrace();
		}
		return row;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static void dialogDetailReport(Context context)// ,AllergyModel
															// model)
	{
		final Dialog loaddialog = Util.showLoadDialog(context);
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int width = (int) (metrics.widthPixels);
		int height = (int) (metrics.heightPixels);
		final Dialog dialogDetailReport = new Dialog(context);
		dialogDetailReport.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogDetailReport.setContentView(R.layout.dialog_report_details);
		dialogDetailReport.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		LayoutParams params = dialogDetailReport.getWindow().getAttributes();
		dialogDetailReport.getWindow().setAttributes(
				(android.view.WindowManager.LayoutParams) params);
		ListView listReport = (ListView) dialogDetailReport
				.findViewById(R.id.list_report);
		View listFooter = ((LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.list_progressbar, null, false);
		listReport.addFooterView(listFooter);

		// AllergiesAllergyDetailAdapter adapter;
		// adapter = new AllergiesAllergyDetailAdapter(context,
		// model.getData());
		// listAllergies.setAdapter(adapter);

		dialogDetailReport.setCancelable(false);
		dialogDetailReport.findViewById(R.id.txt_close).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialogDetailReport.dismiss();

					}
				});
		loaddialog.dismiss();
		dialogDetailReport.show();
		dialogDetailReport.getWindow().setLayout((6 * width) / 7,
				(4 * height) / 5);
	}
}

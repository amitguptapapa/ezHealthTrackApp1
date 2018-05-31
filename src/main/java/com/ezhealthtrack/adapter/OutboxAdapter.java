package com.ezhealthtrack.adapter;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ezhealthtrack.R;
import com.ezhealthtrack.greendao.MessageModel;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.LazyListAdapter;

public class OutboxAdapter extends LazyListAdapter<MessageModel> {
	/* private view holder class */
	private static class ViewHolder {
		private TextView txtHeader;
		private TextView txtSubject;
		private TextView txtDate;
	}

	Context context;
	private MessageModel mSelectedMessage;

	public OutboxAdapter(final Context context) {
		this.context = context;
	}

	@Override
	public View getView(final int position, View view, final ViewGroup parent) {

		final MessageModel rowItem = getItem(position);

		if (view == null) {
			final ViewHolder holder = new ViewHolder();
			final LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.row_inbox, parent, false);
			holder.txtHeader = (TextView) view.findViewById(R.id.txt_from);
			holder.txtSubject = (TextView) view.findViewById(R.id.txt_subject);
			holder.txtDate = (TextView) view.findViewById(R.id.txt_date);
			view.setTag(holder);
		}
		final ViewHolder holder = (ViewHolder) view.getTag();

		// on row item click
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				showMessageDetails(rowItem);
			}
		});

		try {
			holder.txtHeader.setText(Html.fromHtml(rowItem.getTo_name()));
			holder.txtDate.setText(EzUtils.getDisplayDateT1(rowItem.getDate()));
			holder.txtSubject.setText(new JSONObject(rowItem.getData())
					.getString("sub"));
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return view;
	}

	public MessageModel getSlectedMessage() {
		return mSelectedMessage;
	}

	@SuppressLint("InflateParams")
	private void showMessageDetails(final MessageModel message) {
		mSelectedMessage = message;

		// final Dialog dialog = Util.showLoadDialog(context);
		try {
			((Activity) context).findViewById(R.id.rl_messages).setVisibility(
					View.GONE);
			((Activity) context).findViewById(R.id.rl_message).setVisibility(
					View.VISIBLE);
			((Activity) context).findViewById(R.id.id_header_buttons)
					.setVisibility(View.VISIBLE);
			((Activity) context).findViewById(R.id.id_compose_view)
					.setVisibility(View.GONE);

			((TextView) ((Activity) context).findViewById(R.id.txt_subjective))
					.setText(new JSONObject(message.getData()).getString("sub"));
			((LinearLayout) ((Activity) context)
					.findViewById(R.id.ll_messages_details)).removeAllViews();

			final LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			final View v = inflater.inflate(R.layout.item_conv, null);
			((LinearLayout) ((Activity) context)
					.findViewById(R.id.ll_messages_details)).addView(v);
			final TextView txt = ((TextView) v.findViewById(R.id.txt_text));
			final TextView txt1 = ((TextView) v.findViewById(R.id.txt_text1));
			final TextView dateView = (TextView) v.findViewById(R.id.txt_date);
			final TextView nameView = (TextView) v.findViewById(R.id.txt_name);
			final View headerView = v.findViewById(R.id.rl_header);

			nameView.setText(message.getTo_name());
			txt.setText(Html.fromHtml(new JSONObject(message.getData())
					.getString("text")));
			txt1.setText(Html.fromHtml(new JSONObject(message.getData())
					.getString("text")));
			dateView.setText(EzUtils.getDisplayDateT1(message.getDate()));

			v.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (txt.getVisibility() == View.VISIBLE) {
						txt.setVisibility(View.GONE);
						headerView
								.setBackgroundResource(R.drawable.bg_selectable_white);
						txt1.setVisibility(View.VISIBLE);
					} else {
						txt.setVisibility(View.VISIBLE);
						headerView
								.setBackgroundResource(R.drawable.bg_selectable_lightblue);
						txt1.setVisibility(View.GONE);
					}
				}
			});

			txt.setVisibility(View.VISIBLE);
			headerView
					.setBackgroundResource(R.drawable.bg_selectable_lightblue);
			txt1.setVisibility(View.GONE);

		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
}
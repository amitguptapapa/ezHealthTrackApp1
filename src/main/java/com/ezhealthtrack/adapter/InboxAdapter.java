package com.ezhealthtrack.adapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.LabOrderDetailsActivity;
import com.ezhealthtrack.fragments.InboxFragment;
import com.ezhealthtrack.fragments.InboxFragment.OnLinkClicked;
import com.ezhealthtrack.greendao.MessageModel;
import com.ezhealthtrack.model.LabOrder;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.NetworkCallController.OnResponse;
import com.ezhealthtrack.util.Util;

public class InboxAdapter extends EzListFragmentAdaptor {
	/* private view holder class */
	private static class ViewHolder {
		private TextView txtHeader;
		private TextView txtSubject;
		private TextView txtDate;
	}

	private String txt;
	Context context;
	OnLinkClicked onlinkClicked;

	public InboxAdapter(List<Object> items, InboxFragment fragment) {
		super(items, fragment);
		context = fragment.getActivity();
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

		final MessageModel rowItem = (MessageModel) mDataList.get(position);
		ViewHolder mHolder;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row_inbox, null);
			mHolder = new ViewHolder();
			mHolder.txtHeader = (TextView) convertView
					.findViewById(R.id.txt_from);
			mHolder.txtSubject = (TextView) convertView
					.findViewById(R.id.txt_subject);
			mHolder.txtDate = (TextView) convertView
					.findViewById(R.id.txt_date);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		try {
			String name = rowItem.getFrom_name();
			if (rowItem.getConv_count() != 1) {
				name = name + "<b>" + " (" + rowItem.getConv_count() + ")"
						+ "</b>";
			}
			mHolder.txtHeader.setText(Html.fromHtml(name));
			mHolder.txtDate
					.setText(EzUtils.getDisplayDateT1(rowItem.getDate()));
			mHolder.txtSubject.setText(new JSONObject(rowItem.getData())
					.getString("sub"));
		} catch (final Exception e) {
			e.printStackTrace();
		}

		// final View view = row.findViewById(R.id.rl_main);
		// if (!rowItem.getNotified().equals("Y")) {
		// view.setBackgroundResource(R.drawable.bg_unread);
		// } else {
		// view.setBackgroundResource(R.drawable.bg_blue_round);
		// }

		return convertView;
	}

	@SuppressWarnings("deprecation")
	public void showMessageDetails(final MessageModel message, String response) {
		Log.i(">> ", response);
		try {
			final JSONObject jObj = new JSONObject(response);
			((Activity) context).findViewById(R.id.rl_messages).setVisibility(
					View.GONE);
			((Activity) context).findViewById(R.id.rl_message).setVisibility(
					View.VISIBLE);
			((Activity) context).findViewById(R.id.id_header_buttons)
					.setVisibility(View.VISIBLE);
			((Activity) context).findViewById(R.id.id_compose_view)
					.setVisibility(View.GONE);

			if (message.getFrom_type().equalsIgnoreCase("A")) {
				((Activity) context).findViewById(R.id.btn_reply)
						.setVisibility(View.GONE);
			} else {
				((Activity) context).findViewById(R.id.btn_reply)
						.setVisibility(View.VISIBLE);
			}
			final JSONObject data = new JSONObject(jObj.getJSONObject(
					"notification").getString("data"));
			final JSONObject noti = jObj.getJSONObject("notification");
			final JSONArray conv = jObj.getJSONArray("conversation");
			((TextView) ((Activity) context).findViewById(R.id.txt_subjective))
					.setText(data.getString("sub"));
			((LinearLayout) ((Activity) context)
					.findViewById(R.id.ll_messages_details)).removeAllViews();
			final LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			if (conv.length() == 0) {

				final View v = inflater.inflate(R.layout.item_conv, null);
				((LinearLayout) ((Activity) context)
						.findViewById(R.id.ll_messages_details)).addView(v);
				final TextView txt = ((TextView) v.findViewById(R.id.txt_text));
				final TextView txt1 = ((TextView) v
						.findViewById(R.id.txt_text1));
				final TextView dateView = (TextView) v
						.findViewById(R.id.txt_date);
				final TextView nameView = (TextView) v
						.findViewById(R.id.txt_name);
				final View headerView = v.findViewById(R.id.rl_header);

				nameView.setText(message.getFrom_name());
				txt.setMovementMethod(LinkMovementMethod.getInstance());
				// txt.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// LabOrder order = (LabOrder) mDataList.get(pos);
				//
				// Intent intent;
				// intent = new Intent(context,
				// LabOrderDetailsActivity.class);
				// intent.putExtra("id", order.getId());
				// context.startActivity(intent);
				//
				// }
				// });
				setTextViewHTML(txt,
						new JSONObject(message.getData()).getString("text"),
						new OnResponse() {

							@Override
							public void onResponseListner(String response) {
								onlinkClicked.onLinkClicked(response);
							}
						});
				Util.setTextViewHTML(txt1,
						new JSONObject(message.getData()).getString("text"),
						new OnResponse() {

							@Override
							public void onResponseListner(String response) {
								onlinkClicked.onLinkClicked(response);
							}
						});

				dateView.setText(EzUtils.getDisplayDateT1(message.getDate()));
				headerView.setOnClickListener(new OnClickListener() {

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

			} else {

				final View v = inflater.inflate(R.layout.item_conv, null);
				((LinearLayout) ((Activity) context)
						.findViewById(R.id.ll_messages_details)).addView(v);
				final TextView txt = ((TextView) v.findViewById(R.id.txt_text));
				final TextView txt1 = ((TextView) v
						.findViewById(R.id.txt_text1));
				final TextView dateView = (TextView) v
						.findViewById(R.id.txt_date);
				final TextView nameView = (TextView) v
						.findViewById(R.id.txt_name);
				final View headerView = v.findViewById(R.id.rl_header);

				if (message.getFrom_id().equals(noti.getString("from_id")))
					nameView.setText(message.getFrom_name());
				else
					nameView.setText(message.getTo_name());
				txt.setText(Html.fromHtml(new JSONObject(noti.getString("data"))
						.getString("text")));
				txt1.setText(Html.fromHtml(new JSONObject(noti
						.getString("data")).getString("text")));

				final SimpleDateFormat dfOrig = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm");

				Date currentDate = Calendar.getInstance().getTime();
				currentDate.setHours(0);
				currentDate.setMinutes(0);
				currentDate.setSeconds(0);
				Date tomorrowDate = new Date(currentDate.getTime() + 24 * 60
						* 60000 - 60000);

				final SimpleDateFormat df = new SimpleDateFormat(
						"MMM dd', 'yyyy");
				final SimpleDateFormat df1 = new SimpleDateFormat("hh:mm");
				final Date date = dfOrig.parse(noti.getString("date"));
				if (date.compareTo(currentDate) > 0
						&& date.compareTo(tomorrowDate) < 0) {

					dateView.setText(df1.format(date));
				} else {
					dateView.setText(df.format(date));
				}
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
			}

			for (int i = 0; i < conv.length(); i++) {

				final View v = inflater.inflate(R.layout.item_conv, null);
				((LinearLayout) ((Activity) context)
						.findViewById(R.id.ll_messages_details)).addView(v);
				final TextView txt = ((TextView) v.findViewById(R.id.txt_text));
				final TextView txt1 = ((TextView) v
						.findViewById(R.id.txt_text1));
				final TextView dateView = (TextView) v
						.findViewById(R.id.txt_date);
				final TextView nameView = (TextView) v
						.findViewById(R.id.txt_name);
				final View headerView = v.findViewById(R.id.rl_header);

				if (message.getFrom_id().equals(
						conv.getJSONObject(i).getString("from_id")))
					nameView.setText(message.getFrom_name());
				else
					nameView.setText(message.getTo_name());
				txt.setText(new JSONObject(conv.getJSONObject(i).getString(
						"data")).getString("text"));
				txt1.setText(new JSONObject(conv.getJSONObject(i).getString(
						"data")).getString("text"));

				final SimpleDateFormat dfOrig = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm");
				final Date date = dfOrig.parse(conv.getJSONObject(i).getString(
						"date"));

				dateView.setText(EzUtils.getDisplayDateT1(date));
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
				if (i == conv.length() - 1) {
					txt.setVisibility(View.VISIBLE);
					headerView
							.setBackgroundResource(R.drawable.bg_selectable_lightblue);
					txt1.setVisibility(View.GONE);
				}
			}

		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void makeLinkClickable(SpannableStringBuilder strBuilder,
			final URLSpan span, final OnResponse onResponse) {
		int start = strBuilder.getSpanStart(span);
		int end = strBuilder.getSpanEnd(span);
		int flags = strBuilder.getSpanFlags(span);
		ClickableSpan clickable = new ClickableSpan() {
			public void onClick(View view) {
				// Do something with span.getURL() to handle the link click...
				handleLinkClick(txt);
				// onResponse.onResponseListner(span.getURL());
			}
		};
		strBuilder.setSpan(clickable, start, end, flags);
		strBuilder.removeSpan(span);
	}

	private void setTextViewHTML(TextView text, String html,
			final OnResponse onResponse) {
		CharSequence sequence = Html.fromHtml(html);
		SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
		URLSpan[] urls = strBuilder.getSpans(0, sequence.length(),
				URLSpan.class);
		for (URLSpan span : urls) {
			makeLinkClickable(strBuilder, span, onResponse);
		}
		text.setText(strBuilder);
	}

	private void handleLinkClick(String value) {
		LabOrder order = new LabOrder();

		Intent intent;
		intent = new Intent(context, LabOrderDetailsActivity.class);
		intent.putExtra("id", order.getId());
		context.startActivity(intent);

	}
}
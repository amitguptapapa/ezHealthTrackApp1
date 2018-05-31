package com.ezhealthtrack.fragments;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.ezhealthtrack.EzFragment;
import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.activity.EditAssistantActivity;
import com.ezhealthtrack.adapter.AssistantAdapter;
import com.ezhealthtrack.util.Util;

public class AssistantFragment extends EzFragment {

	private GridView listPatients;
	private AssistantAdapter adapter;
	public static Boolean isAddAssistant = true;
	private Button btnAdd;
	private EditText editFilter;

	public void notifyList() {
		adapter.getFilter().filter(editFilter.getText().toString());
		Log.i("", "" + AssistantFragment.isAddAssistant);
		if (AssistantFragment.isAddAssistant) {
			btnAdd.setVisibility(View.VISIBLE);
		} else {
			btnAdd.setVisibility(View.GONE);
		}
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		adapter = new AssistantAdapter(getActivity(), R.layout.row_assistant,
				DashboardActivity.arrAssistants);
		listPatients = (GridView) getActivity()
				.findViewById(R.id.list_patients);
		listPatients.setAdapter(adapter);
		btnAdd = (Button) getActivity().findViewById(R.id.btn_add_assistant);
		Log.i("", "" + AssistantFragment.isAddAssistant);
		if (AssistantFragment.isAddAssistant) {
			btnAdd.setVisibility(View.VISIBLE);
		} else {
			btnAdd.setVisibility(View.GONE);
		}
		btnAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				final Intent intent = new Intent(getActivity(),
						EditAssistantActivity.class);
				startActivity(intent);

			}
		});

		updateCount();
		editFilter = (EditText) getActivity().findViewById(R.id.edit_name);
		editFilter.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(final Editable arg0) {
			}

			@Override
			public void beforeTextChanged(final CharSequence arg0,
					final int arg1, final int arg2, final int arg3) {
			}

			@Override
			public void onTextChanged(final CharSequence cs, final int arg1,
					final int arg2, final int arg3) {
				// When user changed the Text
				adapter.getFilter().filter(cs);
				Util.filterClear(editFilter,
						getActivity().findViewById(R.id.img_clear_filter));
				// updateCount();
			}
		});
	}

	private TextView mTxtCount;

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_assistant, container,
				false);
		mTxtCount = (TextView) view.findViewById(R.id.txt_count_asst);
		return view;
	}

	@Override
	public void onResume() {
		adapter.notifyDataSetChanged();
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	public void updateCount() {
		adapter.registerDataSetObserver(new DataSetObserver() {
			@Override
			public void onChanged() {
				mTxtCount.setText(Html.fromHtml("<b>Total Results: "
						+ adapter.getCount() + "</b>"));
				super.onChanged();
			}
		});
	}

}

package com.ezhealthtrack.fragments;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.adapter.AllergiesAdapter;
import com.ezhealthtrack.controller.AutoSuggestController;
import com.ezhealthtrack.controller.AutoSuggestController.OnAutoSuggest;
import com.ezhealthtrack.controller.MedicalRecordsController;
import com.ezhealthtrack.greendao.MedRecAllergy;
import com.ezhealthtrack.greendao.MedRecAllergyDao.Properties;
import com.ezhealthtrack.model.MedRecClinicalLab;
import com.ezhealthtrack.model.PatientAutoSuggest;
import com.ezhealthtrack.model.PatientShow;
import com.ezhealthtrack.util.EndlessScrollListener;
import com.ezhealthtrack.util.NetworkCallController.OnResponse;
import com.ezhealthtrack.util.Util;
import com.flurry.android.FlurryAgent;

import de.greenrobot.dao.query.QueryBuilder;

public class ClinicalLabFragment extends Fragment implements OnClickListener {
	private ListView listClinicalLab;
	public static PatientShow pShow;
	private AllergiesAdapter adapter;;
	private AutoCompleteTextView editFilter;
	public static int totalCount = 0;
	private TextView txtCount;
	private final ArrayList<MedRecClinicalLab> arrClinicalLab = new ArrayList<MedRecClinicalLab>();
	private ProgressBar listPb;
	public boolean isLoad = true;

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		arrClinicalLab.clear();
		arrClinicalLab.addAll(DashboardActivity.arrClinicalLab);

		listClinicalLab = (ListView) getActivity().findViewById(
				R.id.list_patients);
		View listFooter = ((LayoutInflater) getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.list_progressbar, null, false);
		listClinicalLab.addFooterView(listFooter);
		listPb = (ProgressBar) listFooter.findViewById(R.id.list_progressbar);

		adapter = new AllergiesAdapter(getActivity(),
				MedicalRecordsController.LAB_TYPE);
		listClinicalLab.setAdapter(adapter);

		editFilter = (AutoCompleteTextView) getActivity().findViewById(
				R.id.edit_name);
		final ArrayAdapter<PatientAutoSuggest> adapterPatient = new ArrayAdapter<PatientAutoSuggest>(
				getActivity(), android.R.layout.simple_dropdown_item_1line);
		editFilter.setAdapter(adapterPatient);
		editFilter.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(final Editable arg0) {
			}

			@Override
			public void beforeTextChanged(final CharSequence s, final int arg1,
					final int arg2, final int arg3) {
				AutoSuggestController.autoSuggestPatient(s.toString(),
						getActivity(), new OnAutoSuggest() {

							@Override
							public void OnAutoSuggestListner(Object list) {
								ArrayList<PatientAutoSuggest> arrPat = (ArrayList<PatientAutoSuggest>) list;
								adapterPatient.clear();
								adapterPatient.addAll(arrPat);
							}
						});
			}

			@Override
			public void onTextChanged(final CharSequence cs, final int arg1,
					final int arg2, final int arg3) {
				// When user changed the Text
				// adapter.getFilter().filter(cs);

			}
		});

		Util.filterClear(editFilter,
				getActivity().findViewById(R.id.img_clear_filter));

		getActivity().findViewById(R.id.btn_search).setOnClickListener(this);
		Util.setupUI(getActivity(),
				getActivity().findViewById(R.id.rl_confirmed));

		getActivity().findViewById(R.id.btn_search).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// if (startDate.after(endDate)) {
						// Util.Alertdialog(getActivity(),
						// "Please enter correct Dates as End Date cannot be before the Start Date!");
						// } else {
						FlurryAgent
								.logEvent("OrdersFragment - Search Button Clicked");
						DashboardActivity.filterText = editFilter.getText()
								.toString();
						listPb.setVisibility(View.VISIBLE);
						MedicalRecordsController.getAllergyList(
								MedicalRecordsController.LAB_TYPE,
								getActivity(), 1, editFilter.getText()
										.toString(), new OnResponse() {

									@Override
									public void onResponseListner(
											String response) {
										totalCount = Integer.parseInt(response);
										updateCount();
										adapter.replaceLazyList(getQuery()
												.limit(15).listLazy());
										listPb.setVisibility(View.GONE);
										setScrollListner();

									}
								});
						// }
					}
				});
	}

	private void setScrollListner() {
		listClinicalLab.setOnScrollListener(new EndlessScrollListener(4, 0) {

			@Override
			public void onLoadMore(final int page, int totalItemsCount) {

				listPb.setVisibility(View.VISIBLE);
				MedicalRecordsController.getAllergyList(
						MedicalRecordsController.LAB_TYPE, getActivity(), page,
						editFilter.getText().toString(), new OnResponse() {

							@Override
							public void onResponseListner(String response) {

								totalCount = Integer.parseInt(response);
								updateCount();
								adapter.replaceLazyList(getQuery().limit(
										15 * page).listLazy());
								listPb.setVisibility(View.GONE);

							}

						});

			}
		});
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		return inflater
				.inflate(R.layout.fragment_clinicallab, container, false);
	}

	@Override
	public void onResume() {
		listPb.setVisibility(View.VISIBLE);
		editFilter.setText(DashboardActivity.filterText);
		if (isLoad)
			MedicalRecordsController.getAllergyList(
					MedicalRecordsController.LAB_TYPE, getActivity(), 1, "",
					new OnResponse() {

						@Override
						public void onResponseListner(String response) {
							if (isVisible()) {
								totalCount = Integer.parseInt(response);
								updateCount();
								adapter.replaceLazyList(getQuery().limit(15)
										.listLazy());
								listPb.setVisibility(View.GONE);
								setScrollListner();
							}
						}
					});

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
		try {
			txtCount = (TextView) getActivity().findViewById(
					R.id.txt_count_confirmed);
			txtCount.setText(Html.fromHtml("<b>" + "Total Results: "
					+ ClinicalLabFragment.totalCount + "</b>"));
		} catch (Exception e) {

		}
	}

	public void updateList() {
		// adapter.notifyItems();

	}

	@Override
	public void onStop() {
		if (isRemoving())
			editFilter.setText("");
		super.onStop();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	private QueryBuilder<MedRecAllergy> getQuery() {
		QueryBuilder<MedRecAllergy> qb = EzApp.medRecAllergyDao
				.queryBuilder();

		String s = editFilter.getText().toString().replace("  ", " ");
		qb.whereOr(Properties.Pat_detail.like("%" + s + "%"),
				Properties.Pat_unique_id.like("%" + s + "%"));
		qb.orderDesc(Properties.Last_visit);
		return qb;
	}

}

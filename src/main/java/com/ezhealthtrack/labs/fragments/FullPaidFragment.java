package com.ezhealthtrack.labs.fragments;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.controller.AutoSuggestController;
import com.ezhealthtrack.controller.AutoSuggestController.OnAutoSuggest;
import com.ezhealthtrack.greendao.Order;
import com.ezhealthtrack.greendao.OrderDao.Properties;
import com.ezhealthtrack.labs.adapter.LabsBillingAdapter;
import com.ezhealthtrack.labs.controller.OrderController;
import com.ezhealthtrack.model.PatientAutoSuggest;
import com.ezhealthtrack.util.EndlessScrollListener;
import com.ezhealthtrack.util.Util;
import com.ezhealthtrack.util.NetworkCallController.OnResponse;
import com.ezhealthtrack.util.Util.DateResponse;
import com.flurry.android.FlurryAgent;

import de.greenrobot.dao.query.QueryBuilder;

public class FullPaidFragment extends Fragment {
	private Date startDate;
	private Date endDate;
	private AutoCompleteTextView editSearch;
	private PatientAutoSuggest selPat;
	private ProgressBar listPb;

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		editSearch = (AutoCompleteTextView) getActivity().findViewById(
				R.id.edit_name);
		final ArrayAdapter<PatientAutoSuggest> adapterPatient = new ArrayAdapter<PatientAutoSuggest>(
				getActivity(), android.R.layout.simple_dropdown_item_1line);
		editSearch.setAdapter(adapterPatient);
		editSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				selPat = null;

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
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
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		editSearch.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				selPat = adapterPatient.getItem(position);
			}
		});

		Util.filterClear(editSearch,
				getActivity().findViewById(R.id.img_clear_filter));

		ListView listLabOrders = (ListView) getActivity().findViewById(
				R.id.list_full_paid);
		final LabsBillingAdapter adapter = new LabsBillingAdapter(
				getActivity(), 2);
		View listFooter = ((LayoutInflater) getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.list_progressbar, null, false);
		listLabOrders.addFooterView(listFooter);
		listPb = (ProgressBar) listFooter.findViewById(R.id.list_progressbar);
		listLabOrders.setAdapter(adapter);
		listPb.setVisibility(View.VISIBLE);
		OrderController.getOrderList("1", getActivity(), new OnResponse() {

			@Override
			public void onResponseListner(String response) {
				try {
					if (Util.isEmptyString(response)) {
						((TextView) getActivity().findViewById(
								R.id.txt_count_full_paid)).setText(Html
								.fromHtml("<b>Total Results: </b>" + "0"));
						adapter.replaceLazyList(getQuery()
								.orderDesc(Properties.Date_added).limit(15)
								.listLazy());
					} else {
						((TextView) getActivity().findViewById(
								R.id.txt_count_full_paid)).setText(Html
								.fromHtml("<b>Total Results: </b>" + response));
						adapter.replaceLazyList(getQuery()
								.orderDesc(Properties.Date_added).limit(15)
								.listLazy());
						listPb.setVisibility(View.GONE);
					}
				} catch (Exception e) {
				}

			}
		}, null, null, null, 2, -1, "");

		listLabOrders.setOnScrollListener(new EndlessScrollListener() {

			@Override
			public void onLoadMore(final int page, int totalItemsCount) {
				listPb.setVisibility(View.VISIBLE);
				OrderController.getOrderList("" + page, getActivity(),
						new OnResponse() {

							@Override
							public void onResponseListner(String response) {
								try {
									// ((TextView)
									// getActivity().findViewById(R.id.txt_count_full_paid)).setText("Total Results: "+response);
									adapter.replaceLazyList(getQuery()
											.orderDesc(Properties.Date_added)
											.limit(15 * page).listLazy());
									listPb.setVisibility(View.GONE);
								} catch (Exception e) {

								}

							}
						}, startDate, endDate, selPat, 2, -1, editSearch
								.getText().toString());
			}
		});

		final TextView txtStartDate = (TextView) getActivity().findViewById(
				R.id.txt_start_date);
		txtStartDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FlurryAgent
						.logEvent("FullPaidFragment - Start Date Field Clicked");
				Util.showCalender(txtStartDate, getActivity(),
						new DateResponse() {

							@Override
							public void dateResponseListner(Date date) {
								startDate = date;

							}
						});
			}
		});

		Util.filterClear(txtStartDate,
				getActivity().findViewById(R.id.img_start_date_clear_filter));

		final TextView txtEndDate = (TextView) getActivity().findViewById(
				R.id.txt_end_date);
		txtEndDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FlurryAgent
						.logEvent("FullPaidFragment - End Date Field Clicked");
				Util.showCalender(txtEndDate, getActivity(),
						new DateResponse() {

							@Override
							public void dateResponseListner(Date date) {
								endDate = date;
							}
						});
			}
		});

		Util.filterClear(txtEndDate,
				getActivity().findViewById(R.id.img_end_date_clear_filter));

		getActivity().findViewById(R.id.btn_search).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						FlurryAgent
								.logEvent("FullPaidFragment - Search Button Clicked");
						listPb.setVisibility(View.VISIBLE);
						OrderController.getOrderList(
								"1",
								getActivity(),
								new OnResponse() {

									@Override
									public void onResponseListner(
											String response) {
										try {
											if (Util.isEmptyString(response)) {
												((TextView) getActivity()
														.findViewById(
																R.id.txt_count_not_paid))
														.setText(Html
																.fromHtml("<b>Total Results: </b>"
																		+ "0"));
												adapter.replaceLazyList(getQuery()
														.orderDesc(
																Properties.Date_added)
														.limit(15).listLazy());
											} else {
												((TextView) getActivity()
														.findViewById(
																R.id.txt_count_full_paid))
														.setText("Total Results: "
																+ response);
												adapter.replaceLazyList(getQuery()
														.orderDesc(
																Properties.Date_added)
														.limit(15).listLazy());
												listPb.setVisibility(View.GONE);
											}
										} catch (Exception e) {

										}

									}
								}, startDate, endDate, selPat, 2, -1,
								editSearch.getText().toString());

					}
				});

	}

	@Override
	public void onAttach(final Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.labs_fragment_full_paid, container,
				false);
	}

	@Override
	public void onResume() {
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

	private QueryBuilder<Order> getQuery() {
		QueryBuilder<Order> qb = EzApp.orderDao.queryBuilder();
		if (startDate != null)
			qb.where(Properties.Date_added.ge(startDate));
		if (endDate != null)
			qb.where(Properties.Date_added.le(endDate));
		qb.where(Properties.Bill_status_id.eq(2));
		String s = editSearch.getText().toString();
		if (selPat != null && selPat.getName().equals(s))
			qb.where(Properties.Customer_id.eq(selPat.getId()));
		else if (!Util.isEmptyString(s))
			qb.whereOr(Properties.Order_display_id.like("%" + s + "%"),
					Properties.Firstname.like("%" + s + "%"),
					Properties.Lastname.like("%" + s + "%"));
		return qb;
	}

}

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.ezhealthrack.api.NetworkCalls;
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
import com.ezhealthtrack.util.NetworkCallController.OnResponse;
import com.ezhealthtrack.util.Util;
import com.ezhealthtrack.util.Util.DateResponse;
import com.flurry.android.FlurryAgent;

import de.greenrobot.dao.query.QueryBuilder;

public class BillingsFragment extends Fragment {
	private ArrayList<String> arrBillStatus = new ArrayList<String>();
	private Spinner spinnerBillStatus;
	private Date startDate;
	private Date endDate;
	private AutoCompleteTextView editSearch;
	private PatientAutoSuggest selPat;
	private PatientAutoSuggest selPat1;
	private LabsBillingAdapter adapter;
	private ProgressBar listPb;
	public boolean isLoad = true;

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		selPat = null;
		selPat1 = null;
		startDate = null;
		endDate = null;
		arrBillStatus.clear();
		arrBillStatus.add("Select Bill Status");
		arrBillStatus.add("Not Paid");
		arrBillStatus.add("Partially Paid");
		arrBillStatus.add("Fully Paid");
		arrBillStatus.add("Cancel");
		arrBillStatus.add("Refund");

		spinnerBillStatus = (Spinner) getActivity().findViewById(
				R.id.spinner_bill_status);
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
		final ArrayAdapter<String> adapterBillStatus = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				arrBillStatus);
		adapterBillStatus
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerBillStatus.setAdapter(adapterBillStatus);

		ListView listLabOrders = (ListView) getActivity().findViewById(
				R.id.list_orders);
		adapter = new LabsBillingAdapter(getActivity(), 2);
		View listFooter = ((LayoutInflater) getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.list_progressbar, null, false);
		listLabOrders.addFooterView(listFooter);
		listPb = (ProgressBar) listFooter.findViewById(R.id.list_progressbar);
		listLabOrders.setAdapter(adapter);
		listPb.setVisibility(View.VISIBLE);
		if (isLoad)
			OrderController.getBillList("1", getActivity(), new OnResponse() {

				@Override
				public void onResponseListner(String response) {
					if (isVisible()) {
						if (Util.isEmptyString(response)) {
							((TextView) getActivity().findViewById(
									R.id.txt_count_orders)).setText(Html
									.fromHtml("<b>Total Results: 0</b>"));
						} else {
							((TextView) getActivity().findViewById(
									R.id.txt_count_orders)).setText(Html
									.fromHtml("<b>Total Results: " + response
											+ "</b>"));
						}
						adapter.replaceLazyList(getQuery()
								.orderDesc(Properties.Date_added).limit(15)
								.listLazy());
						listPb.setVisibility(View.GONE);
					}
				}
			}, null, null, null, -1, "");

		listLabOrders.setOnScrollListener(new EndlessScrollListener() {

			@Override
			public void onLoadMore(final int page, int totalItemsCount) {
				if (isLoad) {
					listPb.setVisibility(View.VISIBLE);

					OrderController.getBillList(
							"" + page,
							getActivity(),
							new OnResponse() {

								@Override
								public void onResponseListner(String response) {
									try {

										// ((TextView)
										// getActivity().findViewById(R.id.txt_count_orders)).setText("Total Results: "+response);
										adapter.replaceLazyList(getQuery()
												.orderDesc(
														Properties.Date_added)
												.limit(15 * page).listLazy());
										listPb.setVisibility(View.GONE);

									} catch (Exception e) {

									}

								}
							}, startDate, endDate, selPat,
							spinnerBillStatus.getSelectedItemPosition() - 1,
							editSearch.getText().toString());
				}
			}
		});

		final TextView txtStartDate = (TextView) getActivity().findViewById(
				R.id.txt_start_date);
		txtStartDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FlurryAgent
						.logEvent("BillingsFragment - Start Date Field Clicked");
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
						.logEvent("BillingsFragment - End Date Field Clicked");
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
						// if (startDate.after(endDate)) {
						// Util.Alertdialog(getActivity(),
						// "Please enter correct Dates as End Date cannot be before the Start Date!");
						// } else {
						FlurryAgent
								.logEvent("BillingsFragment - Search Button Clicked");
						listPb.setVisibility(View.VISIBLE);
						OrderController.getBillList(
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
																R.id.txt_count_orders)).setText(Html
														.fromHtml("<b>Total Results: 0</b>"));
												adapter.replaceLazyList(getQuery()
														.orderDesc(
																Properties.Date_added)
														.limit(15).listLazy());
											} else {
												((TextView) getActivity()
														.findViewById(
																R.id.txt_count_orders)).setText(Html
														.fromHtml("<b>Total Results: "
																+ response
																+ "</b>"));
											}
											adapter.replaceLazyList(getQuery()
													.orderDesc(
															Properties.Date_added)
													.limit(15).listLazy());
											listPb.setVisibility(View.GONE);
										} catch (Exception e) {

										}

									}
								},
								startDate,
								endDate,
								selPat,
								spinnerBillStatus.getSelectedItemPosition() - 1,
								editSearch.getText().toString());
						// }
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
		return inflater.inflate(R.layout.labs_fragment_billings, container,
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
		qb.where(Properties.Generate_bill.eq("1"));
		if (startDate != null)
			qb.where(Properties.Date_added.ge(startDate));
		if (endDate != null)
			qb.where(Properties.Date_added.le(new Date(endDate.getTime() + 24
					* 60 * 60 * 1000)));
		if (spinnerBillStatus.getSelectedItemPosition() > 0)
			qb.where(Properties.Bill_status_id.eq(spinnerBillStatus
					.getSelectedItemPosition() - 1), Properties.Generate_bill
					.eq("1"));
		String s = editSearch.getText().toString();
		if (selPat != null && selPat.getName().equals(s))
			qb.where(Properties.Customer_id.eq(selPat.getId()));
		else if (!Util.isEmptyString(s)) {
			String[] arr = s.split("-");
			if (arr.length == 4) {
				s = s.replace("-" + arr[3], "");
			}
			qb.whereOr(Properties.Order_display_id.like(s),
					Properties.Firstname.like("%" + s + "%"),
					Properties.Lastname.like("%" + s + "%"),
					Properties.Order_bill_id.like(s));
		}
		return qb;
	}

	public void showPatientOrders(String id, String name) {
		try {
			startDate = null;
			endDate = null;
			spinnerBillStatus.setSelection(0);
			final PatientAutoSuggest nselPat = new PatientAutoSuggest();
			nselPat.setId(id);
			nselPat.setName(name);
			Log.i(id, name);
			EzApp.mVolleyQueue.cancelAll(NetworkCalls.tag);
			editSearch.setText(name);
			listPb.setVisibility(View.VISIBLE);
			OrderController.getBillList("1", getActivity(), new OnResponse() {

				@Override
				public void onResponseListner(String response) {
					isLoad = true;
					selPat = nselPat;
					if (Util.isEmptyString(response)) {
						((TextView) getActivity().findViewById(
								R.id.txt_count_orders)).setText(Html
								.fromHtml("<b>Total Results: 0</b>"));
					} else {
						((TextView) getActivity().findViewById(
								R.id.txt_count_orders)).setText(Html
								.fromHtml("<b>Total Results: " + response
										+ "</b>"));
					}
					adapter.replaceLazyList(getQuery()
							.orderDesc(Properties.Date_added).limit(15)
							.listLazy());

				}
			}, startDate, endDate, nselPat, spinnerBillStatus
					.getSelectedItemPosition() - 1, name);
			listPb.setVisibility(View.GONE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
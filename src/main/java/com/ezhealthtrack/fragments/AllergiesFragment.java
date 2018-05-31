package com.ezhealthtrack.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.adapter.AllergiesAdapter;
import com.ezhealthtrack.controller.AutoSuggestController;
import com.ezhealthtrack.controller.AutoSuggestController.OnAutoSuggest;
import com.ezhealthtrack.controller.MedicalRecordsController;
import com.ezhealthtrack.greendao.Allergy;
import com.ezhealthtrack.greendao.MedRecAllergy;
import com.ezhealthtrack.greendao.MedRecAllergyDao.Properties;
import com.ezhealthtrack.model.AllergyModel;
import com.ezhealthtrack.model.PatientAutoSuggest;
import com.ezhealthtrack.model.PatientShow;
import com.ezhealthtrack.util.EndlessScrollListener;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.NetworkCallController.OnResponse;
import com.ezhealthtrack.util.Util;
import com.flurry.android.FlurryAgent;

import de.greenrobot.dao.query.QueryBuilder;

public class AllergiesFragment extends Fragment implements OnClickListener {
	private ListView listAllergies;
	public static PatientShow pShow;
	private AllergiesAdapter adapter;
	private AutoCompleteTextView editFilter;
	public static int totalCount = 0;
	private TextView txtCount;
	private final ArrayList<AllergyModel> arrAllergy = new ArrayList<AllergyModel>();
	private ProgressBar listPb;
	private Button btnAddAllergy;
	public boolean isLoad = true;
	PatientAutoSuggest pat = new PatientAutoSuggest();
	PatientAutoSuggest pat1 = new PatientAutoSuggest();

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		arrAllergy.clear();
		arrAllergy.addAll(DashboardActivity.arrAllergy);

		listAllergies = (ListView) getActivity().findViewById(
				R.id.list_patients);
		View listFooter = ((LayoutInflater) getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.list_progressbar, null, false);
		listAllergies.addFooterView(listFooter);
		listPb = (ProgressBar) listFooter.findViewById(R.id.list_progressbar);

		adapter = new AllergiesAdapter(getActivity(),
				MedicalRecordsController.ALLERGY_TYPE);
		listAllergies.setAdapter(adapter);

		editFilter = (AutoCompleteTextView) getActivity().findViewById(
				R.id.edit_name);
		final ArrayAdapter<PatientAutoSuggest> adapterPatient = new ArrayAdapter<PatientAutoSuggest>(
				getActivity(), android.R.layout.simple_dropdown_item_1line);
		editFilter.setAdapter(adapterPatient);
		Log.i(DashboardActivity.filterText, "on activity created called");
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

						FlurryAgent
								.logEvent("OrdersFragment - Search Button Clicked");
						listPb.setVisibility(View.VISIBLE);
						DashboardActivity.filterText = editFilter.getText()
								.toString();

						MedicalRecordsController.getAllergyList(
								MedicalRecordsController.ALLERGY_TYPE,
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

		btnAddAllergy = (Button) getActivity().findViewById(
				R.id.btn_add_allergies);
		btnAddAllergy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AutoSuggestController.autoSuggestPatient(editFilter.getText()
						.toString(), getActivity(), new OnAutoSuggest() {

					@Override
					public void OnAutoSuggestListner(Object list) {
						ArrayList<PatientAutoSuggest> arrPat = (ArrayList<PatientAutoSuggest>) list;
						pat1 = null;
						for (PatientAutoSuggest p : arrPat) {
							if (p.getName()
									.replace("  ", " ")
									.equalsIgnoreCase(
											editFilter.getText().toString()
													.replace("  ", " "))) {
								pat1 = p;
							}
						}
						addAlleryDialog();
					}
				});
			}
		});

	}

	private void setScrollListner() {
		listAllergies.setOnScrollListener(new EndlessScrollListener(4, 0) {

			@Override
			public void onLoadMore(final int page, int totalItemsCount) {

				listPb.setVisibility(View.VISIBLE);
				MedicalRecordsController.getAllergyList(
						MedicalRecordsController.ALLERGY_TYPE, getActivity(),
						page, editFilter.getText().toString(),
						new OnResponse() {

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
		return inflater.inflate(R.layout.fragment_allergies, container, false);
	}

	@Override
	public void onResume() {
		editFilter.setText(DashboardActivity.filterText);
		listPb.setVisibility(View.VISIBLE);
		if (isLoad)
			MedicalRecordsController.getAllergyList(
					MedicalRecordsController.ALLERGY_TYPE, getActivity(), 1,
					editFilter.getText().toString(), new OnResponse() {

						@Override
						public void onResponseListner(String response) {
							totalCount = Integer.parseInt(response);
							updateCount();
							adapter.replaceLazyList(getQuery().limit(15)
									.listLazy());

							listPb.setVisibility(View.GONE);
							setScrollListner();

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
					+ AllergiesFragment.totalCount + "</b>"));
		} catch (Exception e) {

		}
	}

	public void updateList() {
		// adapter.notifyItems();

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

	private void addAlleryDialog() {
		try {

			final Dialog dialogAddAllergies = new Dialog(getActivity());
			dialogAddAllergies.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialogAddAllergies
					.setContentView(R.layout.dialog_mrecords_add_allergies);
			dialogAddAllergies.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));

			final AutoCompleteTextView actvPat = (AutoCompleteTextView) dialogAddAllergies
					.findViewById(R.id.actv_pat_name);
			final ArrayAdapter<PatientAutoSuggest> adapterPatient = new ArrayAdapter<PatientAutoSuggest>(
					getActivity(), android.R.layout.simple_dropdown_item_1line);
			actvPat.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					pat1 = adapterPatient.getItem(arg2);

				}
			});

			actvPat.setAdapter(adapterPatient);
			actvPat.addTextChangedListener(new TextWatcher() {

				@Override
				public void afterTextChanged(final Editable arg0) {
					TextView PatName = (TextView) dialogAddAllergies
							.findViewById(R.id.txt_pat_name_display);
					PatName.setText(actvPat.getText().toString());
				}

				@Override
				public void beforeTextChanged(final CharSequence s,
						final int arg1, final int arg2, final int arg3) {
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
				public void onTextChanged(final CharSequence cs,
						final int arg1, final int arg2, final int arg3) {
					// When user changed the Text
					// adapter.getFilter().filter(cs);

				}
			});

			if (pat1 != null) {
				actvPat.setText(pat1.getName());
			}

			final Spinner spinnerMainCategory = (Spinner) dialogAddAllergies
					.findViewById(R.id.spinner_main_catogery);
			final ArrayList<Allergy> arrMainCategory = (ArrayList<Allergy>) EzApp.allergyDao
					.queryBuilder()
					.where(com.ezhealthtrack.greendao.AllergyDao.Properties.ParentID
							.eq("0")).list();
			final ArrayAdapter<Allergy> adapterMainCatogery = new ArrayAdapter<Allergy>(
					getActivity(), android.R.layout.simple_spinner_item,
					arrMainCategory);
			adapterMainCatogery
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerMainCategory.setAdapter(adapterMainCatogery);
			final Spinner spinnerSubCategory = (Spinner) dialogAddAllergies
					.findViewById(R.id.spinner_sub_category);
			final ArrayList<Allergy> arrSubCatogery = (ArrayList<Allergy>) EzApp.allergyDao
					.queryBuilder()
					.where(com.ezhealthtrack.greendao.AllergyDao.Properties.ParentID
							.eq("1")).list();

			final ArrayAdapter<Allergy> adapterSubCatogery = new ArrayAdapter<Allergy>(
					getActivity(), android.R.layout.simple_spinner_item,
					arrSubCatogery);
			adapterSubCatogery
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerSubCategory.setAdapter(adapterSubCatogery);

			dialogAddAllergies.setCancelable(false);
			dialogAddAllergies.findViewById(R.id.txt_close).setOnClickListener(
					new OnClickListener() {

						private Activity context;

						@Override
						public void onClick(View v) {
							try {
								InputMethodManager imm = (InputMethodManager) context
										.getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.hideSoftInputFromWindow(dialogAddAllergies
										.findViewById(R.id.edit_note)
										.getWindowToken(), 0);
							} catch (Exception e) {

							}
							// TODO Auto-generated method stub
							dialogAddAllergies.dismiss();
						}
					}

			);
			spinnerMainCategory
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(final AdapterView<?> arg0,
								final View arg1, final int position,
								final long arg3) {
							// visitModel.dentistPlanModel.allergy.hashState.put(
							// "mc", arrMainCategory.get(position));
							arrSubCatogery.clear();
							final ArrayList<Allergy> arr = (ArrayList<Allergy>) EzApp.allergyDao
									.queryBuilder()
									.where(com.ezhealthtrack.greendao.AllergyDao.Properties.ParentID
											.eq(arrMainCategory.get(position)
													.getID())).list();
							arrSubCatogery.addAll(arr);
							adapterSubCatogery.notifyDataSetChanged();

						}

						@Override
						public void onNothingSelected(final AdapterView<?> arg0) {
							// TODO Auto-generated method stub

						}
					});
			spinnerSubCategory
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(final AdapterView<?> arg0,
								final View arg1, final int position,
								final long arg3) {
							// visitModel.dentistPlanModel.allergy.hashState.put(
							// "sc", arrSubCatogery.get(position));
						}

						@Override
						public void onNothingSelected(final AdapterView<?> arg0) {
							// TODO Auto-generated method stub

						}
					});
			dialogAddAllergies.findViewById(R.id.btn_submit)
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(final View v) {
							if (!Util.isEmptyString(actvPat.getText()
									.toString())
									&& pat1.getName().equalsIgnoreCase(
											actvPat.getText().toString())) {
								arrMainCategory.get(
										spinnerMainCategory
												.getSelectedItemPosition())
										.getID();
								MedicalRecordsController.addAllergy(
										arrMainCategory
												.get(spinnerMainCategory
														.getSelectedItemPosition())
												.getID(),
										arrSubCatogery
												.get(spinnerSubCategory
														.getSelectedItemPosition())
												.getID(),
										((EditText) dialogAddAllergies
												.findViewById(R.id.edit_note))
												.getText().toString(), pat1,
										getActivity(), dialogAddAllergies);
							} else {
								Util.Alertdialog(getActivity(),
										"Please choose Patient before creating an Order");
							}
						}
					});
			dialogAddAllergies.show();

		} catch (Exception e) {

		}

	}
}

package com.ezhealthtrack.labs.activity;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.PopupMenu;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ezhealthrack.api.APIs;
import com.ezhealthrack.api.NetworkCalls;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.AddPatientActivity;
import com.ezhealthtrack.activity.LoginActivity;
import com.ezhealthtrack.controller.MessageController;
import com.ezhealthtrack.controller.ScheduleController;
import com.ezhealthtrack.fragments.AlertsFragment;
import com.ezhealthtrack.fragments.InboxFragment;
import com.ezhealthtrack.fragments.InboxFragment.OnLinkClicked;
import com.ezhealthtrack.fragments.OutboxFragment;
import com.ezhealthtrack.fragments.PatientsListFragment;
import com.ezhealthtrack.fragments.PatientsListFragment.OnCallback;
import com.ezhealthtrack.fragments.ScheduleListFragment;
import com.ezhealthtrack.labs.controller.LabWorkFlowController;
import com.ezhealthtrack.labs.controller.LabsController;
import com.ezhealthtrack.labs.fragments.BillingsFragment;
import com.ezhealthtrack.labs.fragments.CompletedFragment;
import com.ezhealthtrack.labs.fragments.ConfirmedFragment;
import com.ezhealthtrack.labs.fragments.DashboardFragment;
import com.ezhealthtrack.labs.fragments.DetailsFragment;
import com.ezhealthtrack.labs.fragments.HistoryFragment;
import com.ezhealthtrack.labs.fragments.NewOrdersFragment;
import com.ezhealthtrack.labs.fragments.NewTentativeFragment;
import com.ezhealthtrack.labs.fragments.OrdersFragment;
import com.ezhealthtrack.labs.fragments.PartialFullfiledFragment;
import com.ezhealthtrack.labs.fragments.SideFragment;
import com.ezhealthtrack.labs.fragments.SideFragment.OnActionSelectedListner;
import com.ezhealthtrack.model.BranchInfo;
import com.ezhealthtrack.util.BaseActivity;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.NetworkCallController.OnResponse;
import com.ezhealthtrack.util.Util;

public class LabsDashboardActivity extends BaseActivity implements
		OnActionSelectedListner, NetworkCalls.OnResponse, OnCallback,
		OnLinkClicked {
	protected static final String TAG = "LabsDashboardActivity";
	private DashboardFragment dashboardFragment;
	public SideFragment sideFragment;
	public ConfirmedFragment confirmedFragment;
	// private PatientsFragment patientsFragment;
	private PatientsListFragment patientsListFragment;
	private ScheduleListFragment scheduleListFragment;
	private NewTentativeFragment newTentativeFragment;
	private HistoryFragment historyFragment;
	private OrdersFragment ordersFragment;
	// private NotPaidFragment notPaidFragment;
	// private PartiallyPaidFragment partiallyPaidFragment;
	// private FullPaidFragment fullPaidFragment;
	private NewOrdersFragment newOrdersFragment;
	private PartialFullfiledFragment partialFullfiledFragment;
	private CompletedFragment completedFragment;
	private InboxFragment inboxFragment;
	public OutboxFragment outboxFragment;
	private AlertsFragment alertFragment;
	private DetailsFragment detailFragment;
	private BillingsFragment billingsFragment;
	public static NetworkCalls calls;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM dd', 'yyyy");
		String s = "<b>" + sdf.format(Util.getCurrentDate()) + "</b>";
		((TextView) findViewById(R.id.txt_time)).setText(Html.fromHtml(s));
		calls = new NetworkCalls(this);
		sideFragment = new SideFragment();
		patientsListFragment = new PatientsListFragment();
		patientsListFragment.setArguments(getIntent().getExtras());
		detailFragment = new DetailsFragment();
		detailFragment.setArguments(getIntent().getExtras());
		confirmedFragment = new ConfirmedFragment();
		confirmedFragment.setArguments(getIntent().getExtras());
		scheduleListFragment = new ScheduleListFragment();
		scheduleListFragment.setArguments(getIntent().getExtras());
		newTentativeFragment = new NewTentativeFragment();
		newTentativeFragment.setArguments(getIntent().getExtras());
		historyFragment = new HistoryFragment();
		historyFragment.setArguments(getIntent().getExtras());
		newOrdersFragment = new NewOrdersFragment();
		newOrdersFragment.setArguments(getIntent().getExtras());
		partialFullfiledFragment = new PartialFullfiledFragment();
		partialFullfiledFragment.setArguments(getIntent().getExtras());
		completedFragment = new CompletedFragment();
		completedFragment.setArguments(getIntent().getExtras());
		inboxFragment = new InboxFragment();
		inboxFragment.setArguments(getIntent().getExtras());
		outboxFragment = new OutboxFragment();
		outboxFragment.setArguments(getIntent().getExtras());
		alertFragment = new AlertsFragment();
		alertFragment.setArguments(getIntent().getExtras());
		ordersFragment = new OrdersFragment();
		ordersFragment.setArguments(getIntent().getExtras());
		billingsFragment = new BillingsFragment();
		billingsFragment.setArguments(getIntent().getExtras());
		// notPaidFragment = new NotPaidFragment();
		// notPaidFragment.setArguments(getIntent().getExtras());
		// partiallyPaidFragment = new PartiallyPaidFragment();
		// partiallyPaidFragment.setArguments(getIntent().getExtras());
		// fullPaidFragment = new FullPaidFragment();
		// fullPaidFragment.setArguments(getIntent().getExtras());
		dashboardFragment = new DashboardFragment();
		dashboardFragment.setArguments(getIntent().getExtras());
		getSupportFragmentManager().beginTransaction()
				.add(R.id.side_fragment, sideFragment).commit();
		final FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.fragment_container, dashboardFragment);
		// transaction.addToBackStack(null);
		transaction.commit();
		// calls.getScheduleData();
		ScheduleController.getScheduleData();
		final Dialog loaddialog = Util
				.showLoadDialog(LabsDashboardActivity.this);
		LabsController.getDashboard(this, dashboardFragment, new OnResponse() {

			@Override
			public void onResponseListner(String response) {
				Log.i(response, response);
				MessageController.getLabInboxMessages(1,
						LabsDashboardActivity.this, new OnResponse() {

							@Override
							public void onResponseListner(String response) {
								Log.i(response, response);
								sideFragment.updateMessageCount();
								loaddialog.dismiss();
							}
						}, "", "", null);

			}
		});

		LabsController.getTechnicianList(this);
		LabWorkFlowController.getLabWorkFlows(this);

	}

	@Override
	public void onBackPressed() {
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);
		alertDialogBuilder.setTitle("Do you want to Exit ?");
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								finish();

							}

						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog,
							final int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});

		final AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();

	}

	@Override
	public void onActionSelected(int position) {
		if (position == sideFragment.ACTION_PATIENTS) {
			final FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.fragment_container, patientsListFragment);
			// transaction.addToBackStack(null);
			transaction.commit();
			final String url = APIs.PATIENT_LIST() + 0;
			calls.getPatientList(url);
		} else if (position == sideFragment.ACTION_INBOX) {
			final FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.fragment_container, inboxFragment);
			// transaction.addToBackStack(null);
			transaction.commit();
			// inboxFragment.getLabInboxMessages(1);
			// if (inboxFragment.isVisible())
			// inboxFragment.refresh();
		} else if (position == sideFragment.ACTION_OUTBOX) {
			final FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.fragment_container, outboxFragment);
			// transaction.addToBackStack(null);
			transaction.commit();
			if (outboxFragment.isVisible()) {
				outboxFragment.refresh();
			}
			// outboxFragment.getLabOutboxMessages(1);
		} else if (position == sideFragment.ACTION_ALERT) {
			final FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.fragment_container, alertFragment);
			// transaction.addToBackStack(null);
			transaction.commit();
		} else if (position == sideFragment.ACTION_CONFIRMED) {
			final FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.fragment_container, confirmedFragment);
			// transaction.addToBackStack(null);
			transaction.commit();
		} else if (position == sideFragment.ACTION_SCHEDULE) {
			final FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.fragment_container, scheduleListFragment);
			transaction.commit();
			final String url = APIs.PATIENT_LIST() + 0;
			calls.getPatientList(url);
		} else if (position == sideFragment.ACTION_NEWTENTATIVE) {
			final FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.fragment_container, newTentativeFragment);
			transaction.commit();
		} else if (position == sideFragment.ACTION_HISTORY) {
			final FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.fragment_container, historyFragment);
			// transaction.addToBackStack(null);
			transaction.commit();
			// calls.getAppointmentList(1, "", Constants.TYPE_HISTORY);
		} else if (position == sideFragment.ACTION_NEW) {
			final FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.fragment_container, newOrdersFragment);
			transaction.commit();
		} else if (position == sideFragment.ACTION_PARTIAL) {
			final FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.fragment_container,
					partialFullfiledFragment);
			transaction.commit();
		} else if (position == sideFragment.ACTION_COMPLETE) {
			final FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.fragment_container, completedFragment);
			transaction.commit();
		} else if (position == sideFragment.ACTION_ORDERS) {
			final FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.fragment_container, ordersFragment);
			transaction.commit();
		} else if (position == sideFragment.ACTION_BILLINGS) {
			final FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.fragment_container, billingsFragment);
			transaction.commit();
		}
		// else if (position == sideFragment.ACTION_NOTPAID) {
		// final FragmentTransaction transaction = getSupportFragmentManager()
		// .beginTransaction();
		// transaction.replace(R.id.fragment_container, notPaidFragment);
		// transaction.commit();
		// } else if (position == sideFragment.ACTION_PARTIALLYPAID) {
		// final FragmentTransaction transaction = getSupportFragmentManager()
		// .beginTransaction();
		// transaction.replace(R.id.fragment_container, partiallyPaidFragment);
		// transaction.commit();
		// } else if (position == sideFragment.ACTION_FULLPAID) {
		// final FragmentTransaction transaction = getSupportFragmentManager()
		// .beginTransaction();
		// transaction.replace(R.id.fragment_container, fullPaidFragment);
		// transaction.commit(); }
		else if (position == sideFragment.ACTION_LOGOUT) {
			logout();
		} else if (position == sideFragment.ACTION_DASHBOARD) {
			final FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.fragment_container, dashboardFragment);
			// transaction.addToBackStack(null);
			transaction.commit();
			LabsController.getDashboard(this, dashboardFragment,
					new OnResponse() {

						@Override
						public void onResponseListner(String response) {
							// TODO Auto-generated method stub

						}
					});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu items for use in the action bar
		menu.clear();
		final MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.action_bar, menu);
		final MenuItem item = menu.findItem(R.id.id_dr_name);
		item.setTitle(" Welcome "
				+ EzApp.sharedPref.getString(Constants.DR_NAME, ""));
		// MenuItem menuHospital = menu.findItem(R.id.id_branch);
		// menuHospital.setTitle(" "
		// + EzHealthApplication.sharedPref.getString(Constants.LAB_NAME,
		// ""));
		// menuHospital.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT
		// | MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case R.id.id_add_patient:
			Intent intent1 = new Intent(LabsDashboardActivity.this,
					AddPatientActivity.class);
			LabsDashboardActivity.this.startActivity(intent1);
			return true;

		case R.id.id_dr_name:
			try {
				final PopupMenu popup = new PopupMenu(this,
						findViewById(R.id.id_dr_name));

				popup.getMenu().add(Html.fromHtml("<b>My Branch</b>"))
						.setEnabled(false);

				JSONArray branches = BranchInfo.getBranchInfo();
				for (int i = 0; i < branches.length(); ++i) {
					JSONObject branch = branches.getJSONObject(i);
					popup.getMenu().add(1, i, i,
							i + ". " + branch.getString("name"));
				}

				// registering popup with OnMenuItemClickListener
				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(final MenuItem item) {
						int index = item.getItemId();
						if (BranchInfo.setBranchByIndex(index) != true)
							return true;

						// clear top and (re)start DashboardActivity activity
						Intent intent = new Intent(LabsDashboardActivity.this,
								LabsDashboardActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						LabsDashboardActivity.this.startActivity(intent);

						return true;
					}
				});
				popup.show();

			} catch (Exception e) {

			}
			return true;

		case R.id.id_branch:
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void logout() {
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);

		// set title
		alertDialogBuilder.setTitle("Do you really want to logout ?");

		// set dialog message
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(final DialogInterface dialog,
									final int id) {
								EzApp.orderDao.deleteAll();
								EzApp.patientDao.deleteAll();
								EzApp.aptDao.deleteAll();
								EzApp.labAptDao.deleteAll();
								EzApp.labTechnicianDao.deleteAll();
								EzApp.messageDao.deleteAll();
								EzApp.orderDetailDao.deleteAll();
								EzApp.patientShowDao.deleteAll();
								EzApp.soapNoteDao.deleteAll();
								EzApp.userDao.deleteAll();
								final Editor editor = EzApp.sharedPref.edit();
								editor.putString(Constants.USER_NAME, "");
								editor.putString(Constants.USER_PASSWORD, "");
								editor.putString(Constants.USER_TOKEN, "");
								editor.putBoolean(Constants.IS_LOGIN, false);
								final String TAG = null;
								editor.commit();
								final Intent intent = new Intent(
										LabsDashboardActivity.this,
										LoginActivity.class);
								startActivity(intent);
								final String url = APIs.LOGOUT();
								// Log.i(TAG, url);
								final StringRequest logoutRequest = new StringRequest(
										Request.Method.POST, url,
										new Response.Listener<String>() {
											@Override
											public void onResponse(
													final String response) {

												try {
													new JSONObject(response);

												} catch (final JSONException e) {
													Log.e(TAG, e);
												}
												finish();
											}
										}, new Response.ErrorListener() {
											@Override
											public void onErrorResponse(
													final VolleyError error) {
												finish();
												Log.e("Error.Response", error);
											}
										}) {
									@Override
									public Map<String, String> getHeaders()
											throws AuthFailureError {
										final HashMap<String, String> loginParams = new HashMap<String, String>();
										loginParams.put(
												"auth-token",
												Util.getBase64String(EzApp.sharedPref
														.getString(
																Constants.USER_TOKEN,
																"")));
										return loginParams;
									}

									@Override
									protected Map<String, String> getParams() {
										final HashMap<String, String> loginParams = new HashMap<String, String>();
										loginParams.put("format", "json");
										return loginParams;
									}

								};
								logoutRequest
										.setRetryPolicy(new DefaultRetryPolicy(
												20000,
												5,
												DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
								EzApp.mVolleyQueue.add(logoutRequest);
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog,
							final int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});

		final AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();

	}

	@Override
	public void onResponseListner(String api) {
		if (api.equals(APIs.PATIENT_LIST())) {
			if (patientsListFragment.isVisible()) {
				patientsListFragment.updateCount();
			} else if (scheduleListFragment.isVisible()) {
				scheduleListFragment.notifylist();
			}
		} else if (api.equals(APIs.DASHBOARD())) {
			dashboardFragment.updateData();
		}

	}

	@Override
	public void onCallback(final String id, final String name) {
		Log.i(TAG, id);
		ordersFragment.isLoad = false;
		final FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.fragment_container, ordersFragment);
		transaction.commit();
		sideFragment.updateOrderColor();
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				ordersFragment.showPatientOrders(id, name);
			}

		}, 500);

	}

	@Override
	protected void onResume() {
		if (confirmedFragment.isVisible()) {
			confirmedFragment.refresh();
		}
		super.onResume();
	}

	@Override
	public void onLinkClicked(String url) {
		Log.i(TAG, url);
		if (url.contains("/v1/booking/appointmentsv2/id/")) {
			final FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.fragment_container, detailFragment);
			transaction.commit();
			String[] parts = url.split("/v1/booking/appointmentsv2/id/");
			Log.i("", "" + detailFragment);
			detailFragment.showAppointments(parts[parts.length - 1]);
		}

	}

}

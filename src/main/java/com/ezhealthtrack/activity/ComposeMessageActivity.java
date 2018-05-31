package com.ezhealthtrack.activity;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.ezhealthtrack.R;
import com.ezhealthtrack.controller.AutoSuggestController;
import com.ezhealthtrack.controller.AutoSuggestController.OnAutoSuggest;
import com.ezhealthtrack.controller.MessageController;
import com.ezhealthtrack.model.PatientAutoSuggest;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.EzActivity;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.Util;
import com.ezhealthtrack.views.ChipsMultiAutoCompleteTextview;
import com.google.gson.Gson;
import com.tokenautocomplete.TokenCompleteTextView.TokenListener;

public class ComposeMessageActivity extends EzActivity implements TokenListener {

	static public int MTYPE_NONE = 0;
	static public int MTYPE_MESSAGE = 1;
	static public int MTYPE_EMAIL = 2;
	static public int MTYPE_SMS = 3;

	private ChipsMultiAutoCompleteTextview mTo;
	private EditText mSubject;
	private EditText mSMSBody;
	private EditText mBody;
	private TextView mSubjectCount;
	private TextView mSMSCount;
	private TextView mAlert;
	// private Button btnCancel;
	private CheckBox mCheckBoxAllPatient;

	private View mSMSView;
	private View mMailView;

	ArrayList<PatientAutoSuggest> arrPat1 = new ArrayList<PatientAutoSuggest>();
	ArrayAdapter<PatientAutoSuggest> mToAdapter;

	static int mType;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose_message);
		this.setDisplayHomeAsUpEnabled(true);

		// Header
		TextView title = (TextView) findViewById(R.id.txt_screen_title);
		title.setText("New Message");

		findViewById(R.id.id_filters).setVisibility(View.GONE);
		findViewById(R.id.edit_name_view).setVisibility(View.GONE);
		findViewById(R.id.start_date_view).setVisibility(View.GONE);
		findViewById(R.id.end_date_view).setVisibility(View.GONE);
		findViewById(R.id.edit_date_view).setVisibility(View.GONE);
		findViewById(R.id.btn_search).setVisibility(View.GONE);
		findViewById(R.id.line_2).setVisibility(View.VISIBLE);
		findViewById(R.id.txt_count).setVisibility(View.GONE);

		// To:
		mToAdapter = new ArrayAdapter<PatientAutoSuggest>(this,
				android.R.layout.simple_dropdown_item_1line);
		mTo = (ChipsMultiAutoCompleteTextview) findViewById(R.id.actv_to);
		this.configureInputFieldTo();
		mCheckBoxAllPatient = (CheckBox) findViewById(R.id.cb_all_patients);

		// Mail / Message: Subject
		// btnCancel = (Button) findViewById(R.id.btn_cancel);
		mMailView = findViewById(R.id.rl_mail);
		mSubject = (EditText) findViewById(R.id.edit_subject);
		mSubjectCount = (TextView) findViewById(R.id.txt_subject_count_display);
		textRemainingCounter(mSubject, 120, mSubjectCount);

		// Mail / Message: Body
		mBody = (EditText) findViewById(R.id.edit_body);

		// SMS:
		mSMSView = findViewById(R.id.rl_sms);

		mSMSCount = (TextView) findViewById(R.id.txt_sms_count_display);
		mSMSBody = (EditText) findViewById(R.id.edit_sms_body);
		textRemainingCounter(mSMSBody, 120, mSMSCount);
		mAlert = (TextView) findViewById(R.id.txt_sms_alert);

		mCheckBoxAllPatient
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							for (Object object : mTo.getObjects())
								mTo.removeObject(object);
							// actvPatient.setText("");
							mTo.setEnabled(false);
						} else {
							mTo.setEnabled(true);
						}

					}
				});

		if (mType == ComposeMessageActivity.MTYPE_SMS) {
			title.setText("New Message - SMS");
			mSMSView.setVisibility(View.VISIBLE);
			mMailView.setVisibility(View.GONE);
			mAlert.setText(Html
					.fromHtml("<sup><font color='#EE0000'>*</font></sup>"
							+ "You may incur SMS charges."));
		} else if (mType == ComposeMessageActivity.MTYPE_EMAIL) {
			title.setText("New Message - E-mail");
			mSMSView.setVisibility(View.GONE);
			mMailView.setVisibility(View.VISIBLE);
			mAlert.setText(Html
					.fromHtml("<sup><font color='#EE0000'>*</font></sup>"
							+ "You may incur Email charges."));
		} else {
			title.setText("New Message - In App Message");
			mAlert.setVisibility(View.GONE);
			mSMSView.setVisibility(View.GONE);
			mMailView.setVisibility(View.VISIBLE);
		}

		// btnCancel.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// finish();
		// }
		// });
	}

	void configureInputFieldTo() {
		mTo.setAdapter(mToAdapter);
		mTo.allowDuplicates(false);
		mTo.setTokenListener(this);
		mTo.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(final Editable arg0) {
			}

			@Override
			public void beforeTextChanged(final CharSequence s, final int arg1,
					final int arg2, final int arg3) {
				Log.i("selected", new Gson().toJson(mTo.getObjects()));
				String chips = s.toString().replaceAll(", ", "")
						.replaceAll(",", "");
				AutoSuggestController.autoSuggestPatient(chips,
						ComposeMessageActivity.this, new OnAutoSuggest() {

							@Override
							public void OnAutoSuggestListner(Object list) {
								@SuppressWarnings("unchecked")
								ArrayList<PatientAutoSuggest> arrPat = (ArrayList<PatientAutoSuggest>) list;
								for (PatientAutoSuggest pat1 : arrPat) {
									boolean a = false;
									for (Object obj : arrPat1) {
										PatientAutoSuggest pat = (PatientAutoSuggest) obj;
										if (pat.getId().equals(pat1.getId())) {
											a = true;
										}
									}
									if (!a) {
										mToAdapter.add(pat1);
										arrPat1.add(pat1);
									}
								}
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

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = (MenuInflater) getMenuInflater();
		inflater.inflate(R.menu.menu_send, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		for (int i = 0; i < menu.size(); i++)
			menu.getItem(i).setVisible(false);
		final MenuItem item = menu.findItem(R.id.id_send);
		item.setVisible(true);
		return super.onPrepareOptionsMenu(menu);
	}

	public void actionSendMessage(View view) {
		this.sendMessage();
	}

	void sendMessage() {
		String sendTo = mTo.getText().toString();
		String subject = mSubject.getText().toString();
		String body = mBody.getText().toString();
		String smsBody = mSMSBody.getText().toString();

		if (Util.isEmptyString(sendTo) && !mCheckBoxAllPatient.isChecked()) {
			Util.Alertdialog(ComposeMessageActivity.this,
					"Please select atleast one patient");
			return;
		}

		if (mType != ComposeMessageActivity.MTYPE_SMS) {
			if (Util.isEmptyString(body) || Util.isEmptyString(subject)) {
				Util.Alertdialog(ComposeMessageActivity.this,
						"Subject and Message body can't be empty");
				return;
			}
		} else if (Util.isEmptyString(smsBody)) {
			Util.Alertdialog(ComposeMessageActivity.this,
					"SMS Message body can't be empty");
			return;
		}

		MessageController.sendComposeMessage(mSubject.getText().toString(),
				mBody.getText().toString(), ComposeMessageActivity.this, mTo
						.getObjects(), mCheckBoxAllPatient.isChecked(),
				mSMSBody.getText().toString(), mType);

	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {

		case R.id.id_send:
			actionSendMessage(null);
			return true;

		case android.R.id.home:
			this.handleBack();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		this.handleBack();
	}

	@Override
	public void onPause() {
		super.onPause();
		EzUtils.hideKeyBoard(this);
	}

	void handleBack() {
		String sendTo = mTo.getText().toString();
		String subject = mSubject.getText().toString();
		String body = mBody.getText().toString();
		String smsBody = mSMSBody.getText().toString();

		if (Util.isEmptyString(sendTo) && Util.isEmptyString(body)
				&& Util.isEmptyString(subject) && Util.isEmptyString(smsBody)) {
			finish();
			return;
		}

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Changes will be discarded, Do you want to continue ?")
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
						dialog.cancel();
					}
				}).create().show();
	}

	private void textRemainingCounter(final EditText edit, final int count1,
			final TextView txt) {
		edit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				txt.setText("" + (count1 - s.length()));
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	@Override
	public void onTokenAdded(Object arg0) {
		mToAdapter.remove((PatientAutoSuggest) arg0);
		// cbInApp.setEnabled(mTo.getObjects().size() == 1);
	}

	@Override
	public void onTokenRemoved(Object arg0) {
		mToAdapter.add((PatientAutoSuggest) arg0);
		// cbInApp.setEnabled(mTo.getObjects().size() == 1);
	}

}

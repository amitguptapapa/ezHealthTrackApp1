package com.ezhealthtrack.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.controller.LoginController;
import com.ezhealthtrack.dialogs.ForgotPasswordDialog;
import com.ezhealthtrack.gcm.GcmController;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.EzActivity;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.Util;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class LoginActivity extends EzActivity {

    final private String TAG = this.getClass().getSimpleName();

    private EditText mEditEmail;
    private EditText mEditPassword;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ezHealthTrack");
        getSupportActionBar().setSubtitle("Doctor");

        Log.i("LA::onCreate()", "In..");

        LoginController controller = new LoginController(this);
        if (controller.autoLogin() == true) {
            finish();
            return;
        }

        mEditEmail = (EditText) findViewById(R.id.edit_email);
        mEditPassword = (EditText) findViewById(R.id.edit_password);

        mEditEmail.setText(EzApp.sharedPref.getString(Constants.USER_NAME, ""));
        mEditPassword.setText(EzApp.sharedPref.getString(
                Constants.USER_PASSWORD, ""));

        mEditPassword
                .setOnEditorActionListener(new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(final TextView v,
                                                  final int actionId, final KeyEvent event) {
                        if ((actionId == EditorInfo.IME_ACTION_SEARCH)
                                || (actionId == EditorInfo.IME_ACTION_DONE)
                                || ((event.getAction() == KeyEvent.ACTION_DOWN) && (event
                                .getKeyCode() == KeyEvent.KEYCODE_ENTER))) {

                            actionSignIn(null);
                            return true; // consume.
                        }
                        return false; // pass on to other listeners.
                    }
                });

        if (GcmController.checkPlayServices(this)) {
            GcmController.gcm = GoogleCloudMessaging.getInstance(this);
            GcmController.regid = GcmController.getRegistrationId(this);

            if (GcmController.regid.isEmpty()) {
                GcmController.registerInBackground(this);
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }

    }

    // forgot password
    public void actionForgotPassword(View view) {
        new ForgotPasswordDialog(LoginActivity.this);
    }

    // login
    public void actionSignIn(View view) {
        if (Util.isEmptyString(mEditEmail.getText().toString())) {
            Toast.makeText(LoginActivity.this, "Please enter Email !",
                    Toast.LENGTH_SHORT).show();
        } else if (Util.isEmptyString(mEditPassword.getText().toString())) {

            String email = mEditEmail.getText().toString();
            if (email.equals("dev@setezserver")) {
                this.showServerUrlOptions();
                return;
            }
            Toast.makeText(LoginActivity.this, "Please enter Password !",
                    Toast.LENGTH_SHORT).show();
        } else if (!EzUtils.isNetworkAvailable(LoginActivity.this)) {
            Toast.makeText(LoginActivity.this,
                    "No Network Connection Available !!!", Toast.LENGTH_SHORT)
                    .show();
        } else if (Util.isEmptyString(mEditEmail.getText().toString())
                && Util.isEmptyString(mEditPassword.getText().toString())) {
            Toast.makeText(LoginActivity.this,
                    "Please enter Username and Password !", Toast.LENGTH_SHORT)
                    .show();
        } else {

            final Editor editor = EzApp.sharedPref.edit();
            editor.putString(Constants.USER_TOKEN, "");
            editor.commit();

            LoginController controller = new LoginController(this);
            controller.login(mEditEmail.getText().toString(), mEditPassword
                    .getText().toString(), true);
        }
    }

    void showServerUrlOptions() {
        final int DEFAULT_SERVER_ID = 0;
        int serverIndex = EzApp.sharedPref.getInt(Constants.EZ_SERVER_ID,
                DEFAULT_SERVER_ID);
        if (serverIndex < 0)
            serverIndex = DEFAULT_SERVER_ID;

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(true);
        // alert.setIcon(R.drawable.alert_dialog_icon)
        alert.setTitle("Select Server");
        alert.setSingleChoiceItems(APIs.ezServers, serverIndex,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int itemIndex) {
                        // dialog dismissed
                        Toast.makeText(LoginActivity.this,
                                "Server " + itemIndex + " selected",
                                Toast.LENGTH_LONG).show();
                        final Editor editor = EzApp.sharedPref.edit();
                        editor.putInt(Constants.EZ_SERVER_ID, itemIndex);
                        editor.commit();
                        dialog.dismiss();
                    }
                });
        alert.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v("LA::onStart()", "...");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("LA::onDestroy()", "In..");
    }

}

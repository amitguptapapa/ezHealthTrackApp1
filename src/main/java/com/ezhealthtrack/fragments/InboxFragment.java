package com.ezhealthtrack.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.adapter.InboxAdapter;
import com.ezhealthtrack.controller.AutoSuggestController;
import com.ezhealthtrack.controller.AutoSuggestController.OnAutoSuggest;
import com.ezhealthtrack.controller.InboxController;
import com.ezhealthtrack.controller.MessageController;
import com.ezhealthtrack.greendao.MessageModel;
import com.ezhealthtrack.model.PatientAutoSuggest;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.Util;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InboxFragment extends EzListFragment {

    // private InboxAdapter adapter;
    private AutoCompleteTextView editFilter;
    private TextView mCountView;
    public static int totalCount = 0;
    public static int unreadCount = 0;
    private PatientAutoSuggest selPat;

    private View mComposeMessage;
    private MessageModel mSelectedMessage;

    public InboxFragment() {
        super(false);
    }

    @Override
    public void onCmdResponse(JSONObject response, String result) {
        String count = response.optString("totalCount");
        if (count.isEmpty()) {
            mCountView.setVisibility(View.GONE);
            return;
        }
        mCountView
                .setText(Html.fromHtml("<b>Total Results: " + count + "</b>"));
    }

    public interface OnLinkClicked {
        public void onLinkClicked(String url);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("IBF", "onCreate()");
        mController = new InboxController(0);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_inbox_v2,
                container, false);
        super.onCreateListView(view, inflater);

        mListAdapter = new InboxAdapter(mDataList, this);
        mListView.setAdapter(mListAdapter);

        TextView title = (TextView) view.findViewById(R.id.txt_screen_title);
        title.setText("Messages - Inbox");
        if (EzUtils.getDeviceSize(getActivity()) == EzUtils.EZ_SCREEN_SMALL) {
            title.setVisibility(View.GONE);
        }

        view.findViewById(R.id.edit_name_view).setVisibility(View.VISIBLE);
        view.findViewById(R.id.start_date_view).setVisibility(View.GONE);
        view.findViewById(R.id.end_date_view).setVisibility(View.GONE);
        view.findViewById(R.id.edit_date_view).setVisibility(View.GONE);
        view.findViewById(R.id.btn_search).setVisibility(View.VISIBLE);
        view.findViewById(R.id.line_2).setVisibility(View.VISIBLE);

        TextView detailsTitle = (TextView) view
                .findViewById(R.id.txt_screen_title_message);
        detailsTitle.setText("Messages - Inbox");

        // unread/total item count
        mCountView = (TextView) view.findViewById(R.id.txt_count);

        // filter text
        editFilter = (AutoCompleteTextView) view.findViewById(R.id.edit_name);
        editFilter.setHint("Patient Name");
        final ArrayAdapter<PatientAutoSuggest> adapterPatient = new ArrayAdapter<PatientAutoSuggest>(
                getActivity(), android.R.layout.simple_dropdown_item_1line);
        editFilter.setAdapter(adapterPatient);
        editFilter.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                Util.filterClear(editFilter,
                        getActivity().findViewById(R.id.img_clear_filter));

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                AutoSuggestController.autoSuggestPatient(s.toString(),
                        getActivity(), new OnAutoSuggest() {

                            @Override
                            public void OnAutoSuggestListner(Object list) {
                                @SuppressWarnings("unchecked")
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
        editFilter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                selPat = adapterPatient.getItem(position);
            }
        });

        // on send message
        final TextView mesgView = (EditText) view.findViewById(R.id.edit_reply);
        view.findViewById(R.id.btn_send).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        try {
                            InputMethodManager imm = (InputMethodManager) getActivity()
                                    .getSystemService(
                                            Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(
                                    mesgView.getWindowToken(), 0);
                        } catch (Exception e) {

                        }

                        String body = mesgView.getText().toString();

                        MessageController.sendMessage(mSelectedMessage, body,
                                getActivity(),
                                new MessageController.SendMessageResponse() {

                                    @Override
                                    public void onSendMessageResponse(
                                            boolean success) {
                                        if (success != true)
                                            return;
                                        mesgView.setText("");
                                        view.findViewById(R.id.id_compose_view)
                                                .setVisibility(View.GONE);
                                        view.findViewById(
                                                R.id.id_header_buttons)
                                                .setVisibility(View.VISIBLE);

                                    }
                                });
                    }
                });

        // hide compose view
        mComposeMessage = view.findViewById(R.id.id_compose_view);
        mComposeMessage.setVisibility(View.GONE);

        // compose / reply to a message
        view.findViewById(R.id.btn_reply).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(final View v) {
                        view.findViewById(R.id.id_compose_view).setVisibility(
                                View.VISIBLE);
                        view.findViewById(R.id.id_header_buttons)
                                .setVisibility(View.GONE);
                    }
                });

        // discard new message
        view.findViewById(R.id.btn_cancel).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(final View v) {
                        view.findViewById(R.id.id_compose_view).setVisibility(
                                View.GONE);
                        view.findViewById(R.id.id_header_buttons)
                                .setVisibility(View.VISIBLE);
                    }
                });

        // on back from message details
        view.findViewById(R.id.btn_back).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        view.findViewById(R.id.rl_messages).setVisibility(
                                View.VISIBLE);
                        view.findViewById(R.id.rl_message).setVisibility(
                                View.GONE);
                    }
                });

        // search button
        // view.findViewById(R.id.btn_search).setOnClickListener(this);

        Util.setupUI(getActivity(), view.findViewById(R.id.rl_inbox));

        // get initial data
        mController.getPage(1, null, this);
        return view;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_messages, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        for (int i = 0; i < menu.size(); i++)
            menu.getItem(i).setVisible(false);
        final MenuItem item = menu.findItem(R.id.id_compose);
        item.setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onResume() {
        // updateCount();
        mListAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        EzUtils.hideKeyBoard(getActivity());
        super.onStart();
    }

    // private void updateCount() {
    // if (mCountView != null)
    // mCountView.setText("" + totalCount + " messages");
    // }

    // public void refresh() {
    // getActivity().findViewById(R.id.rl_messages)
    // .setVisibility(View.VISIBLE);
    // getActivity().findViewById(R.id.rl_message).setVisibility(View.GONE);
    //
    // }

    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                // clear previous results and data
                this.initDataList();
                mListAdapter.resetLastViewedToZero();
                mListAdapter.notifyDataSetChanged();
                this.setInProgressActivity();

                mUrlParams.remove("search");
                mUrlParams.remove("patient_id");

                // Search
                String search = editFilter.getText().toString();
                if (selPat != null && selPat.getName().equals(search)) {
                    mUrlParams.put("patient_id", selPat.getId());
                } else if (!Util.isEmptyString(search)) {
                    mUrlParams.put("search", search);
                }

                mCurrentPage = 1;
                mController.getPage(mCurrentPage, mUrlParams, this);
                this.setInProgressActivity();
                break;
        }
    }

    @Override
    public void onListItemClick(ListView list, View view, final int position,
                                long id) {
        super.onListItemClick(list, view, position, id);

        if (position < 0 || position >= mDataList.size())
            return;

        Log.i("onListItemClick", "Position=" + position);
        mSelectedMessage = (MessageModel) mDataList.get(position);

        // hide compose view
        mComposeMessage.setVisibility(View.GONE);

        // view.setBackgroundResource(R.drawable.bg_blue_round);
        if (mSelectedMessage.getNotified().equals("N")) {
            mSelectedMessage.setNotified("Y");
            if (InboxFragment.unreadCount > 0)
                InboxFragment.unreadCount--;
            mCountView.setText("" + InboxFragment.totalCount + " messages");
        }

        final String url = APIs.VIEWMESSAGE();
        // final Dialog dialog = Util.showLoadDialog(context);
        Log.i("", url);
        final StringRequest logoutRequest = new StringRequest(
                Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                ((InboxAdapter) mListAdapter).showMessageDetails(
                        mSelectedMessage, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                Util.Alertdialog(getActivity(),
                        "There is some error while fetching conversation.");
                // dialog.dismiss();
                Log.e("Error.Response", error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final HashMap<String, String> loginParams = new HashMap<String, String>();
                loginParams.put("auth-token", Util
                        .getBase64String(EzApp.sharedPref.getString(
                                Constants.USER_TOKEN, "")));
                return loginParams;
            }

            @Override
            protected Map<String, String> getParams() {
                final HashMap<String, String> loginParams = new HashMap<String, String>();
                loginParams.put("format", "json");
                loginParams.put("cli", "api");
                loginParams.put("nid", mSelectedMessage.getNid());
                if (mSelectedMessage.getContext_type().equalsIgnoreCase(
                        "notification")) {
                    loginParams.put("context", mSelectedMessage.getContext());
                } else {
                    loginParams.put("context", mSelectedMessage.getNid());
                }
                loginParams.put("to_id", mSelectedMessage.getTo_id());
                loginParams.put("to_fam_id", mSelectedMessage.getTo_fam_id());
                loginParams.put("to_type", mSelectedMessage.getTo_type());
                loginParams.put("tenant_id",
                        EzApp.sharedPref.getString(Constants.TENANT_ID, ""));
                loginParams.put("branch_id", EzApp.sharedPref.getString(
                        Constants.USER_BRANCH_ID, ""));
                Log.i("params", loginParams.toString());
                return loginParams;
            }

        };
        logoutRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        EzApp.mVolleyQueue.add(logoutRequest);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}

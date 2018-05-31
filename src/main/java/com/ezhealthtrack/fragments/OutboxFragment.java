package com.ezhealthtrack.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ezhealthrack.api.LabApis;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.adapter.OutboxAdapter;
import com.ezhealthtrack.controller.MessageController;
import com.ezhealthtrack.greendao.MessageModel;
import com.ezhealthtrack.greendao.MessageModelDao.Properties;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.NetworkCallController.OnResponse;
import com.ezhealthtrack.util.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class OutboxFragment extends Fragment implements OnClickListener {
    private ListView listPatients;
    private OutboxAdapter adapter;
    private EditText editFilter;
    private TextView mCountView;
    public static int totalCount = 0;
    public static int unreadCount = 0;
    private int tCount = 0;
    private TextView txtStartDate;
    private TextView txtEndDate;
    private Date startDate;
    private Date endDate;
    public static String nextUrl;
    public static String prevUrl = "";
    private ProgressBar listPb;

    private MessageModel mSelectedMessage;

    public void notifyList() {
        // adapter.replaceLazyList(EzHealthApplication.messageDao
        // .queryBuilder()
        // .where(Properties.Message_type.eq(Constants.OUTBOX),
        // Properties.To_name.like("%" + editFilter.getText().toString() +
        // "%")).orderDesc(Properties.Date)
        // .listLazy());
        // tCount = adapter.getCount();
        // updateCount();
        // adapter.notifyDataSetChanged();
        dateFilters();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("OBF", "onCreate()");
        setHasOptionsMenu(true);
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_inbox_v2,
                container, false);

        TextView title = (TextView) view.findViewById(R.id.txt_screen_title);
        title.setText("Messages - Outbox");
        if (EzUtils.getDeviceSize(getActivity()) == EzUtils.EZ_SCREEN_SMALL) {
            title.setVisibility(View.GONE);
        }

        view.findViewById(R.id.edit_name_view).setVisibility(View.VISIBLE);
        view.findViewById(R.id.start_date_view).setVisibility(View.VISIBLE);
        view.findViewById(R.id.end_date_view).setVisibility(View.VISIBLE);
        view.findViewById(R.id.edit_date_view).setVisibility(View.GONE);
        view.findViewById(R.id.btn_search).setVisibility(View.VISIBLE);
        view.findViewById(R.id.line_2).setVisibility(View.VISIBLE);

        TextView detailsTitle = (TextView) view
                .findViewById(R.id.txt_screen_title_message);
        detailsTitle.setText("Messages - Outbox");

        mCountView = (TextView) view.findViewById(R.id.txt_count);

        adapter = new OutboxAdapter(getActivity());
        adapter.replaceLazyList(EzApp.messageDao.queryBuilder()
                .where(Properties.Message_type.eq(Constants.OUTBOX))
                .orderDesc(Properties.Date).listLazy());
        listPatients = (ListView) view.findViewById(android.R.id.list);
        View listFooter = ((LayoutInflater) getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.list_progressbar, null);
        listPatients.addFooterView(listFooter);
        listPb = (ProgressBar) listFooter.findViewById(R.id.list_progressbar);
        listPatients.setAdapter(adapter);

        tCount = totalCount;

        editFilter = (EditText) view.findViewById(R.id.edit_name);
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
                startDate = null;
                endDate = null;
                adapter.replaceLazyList(EzApp.messageDao
                        .queryBuilder()
                        .where(Properties.Message_type.eq(Constants.OUTBOX),
                                Properties.To_name.like("%" + cs + "%"))
                        .orderDesc(Properties.Date).listLazy());
                tCount = adapter.getCount();
                Util.filterClear(editFilter,
                        view.findViewById(R.id.img_clear_filter));
                updateCount();
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

                        mSelectedMessage = adapter.getSlectedMessage();
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
        view.findViewById(R.id.id_compose_view).setVisibility(View.GONE);

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

        Util.setupUI(getActivity(), view.findViewById(R.id.rl_inbox));
        updateCount();
        listPb.setVisibility(View.VISIBLE);
        getLabOutboxMessages(1);
        txtStartDate = (TextView) view.findViewById(R.id.txt_start_date);
        txtStartDate.setOnClickListener(this);
        Util.filterClear(txtStartDate,
                view.findViewById(R.id.img_start_date_clear_filter));
        txtEndDate = (TextView) view.findViewById(R.id.txt_end_date);
        txtEndDate.setOnClickListener(this);
        Util.filterClear(txtEndDate,
                view.findViewById(R.id.img_end_date_clear_filter));
        view.findViewById(R.id.btn_search).setOnClickListener(this);
        listPatients.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                nextUrl = "" + totalItemCount / 15 + 1;
                Log.i("", "" + firstVisibleItem);
                if (firstVisibleItem % 15 == 2
                        && firstVisibleItem > totalItemCount - 15)
                    if (totalCount > totalItemCount && !prevUrl.equals(nextUrl)) {
                        listPb.setVisibility(View.VISIBLE);
                        getLabOutboxMessages(totalItemCount / 15 + 1);
                        prevUrl = "" + totalItemCount / 15 + 1;
                    }
            }
        });

        return view; // inflater.inflate(R.layout.fragment_outbox, container,
        // false);
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLabOutboxMessages(1);
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
        updateCount();
        adapter.notifyDataSetChanged();
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

    public void updateCount() {
        if (mCountView != null)
            mCountView.setText("" + tCount + " messages");
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.txt_start_date:
                showCalender(txtStartDate, "startDate");
                break;

            case R.id.txt_end_date:
                showCalender(txtEndDate, "endDate");
                break;

            case R.id.btn_search:
                dateFilters();
                break;
        }

    }

    private void showCalender(final TextView txtView, final String date) {
        final Calendar cal = Calendar.getInstance();
        final DatePickerDialog datepicker = new DatePickerDialog(getActivity(),
                new OnDateSetListener() {

                    @Override
                    public void onDateSet(final DatePicker view,
                                          final int year, int monthOfYear,
                                          final int dayOfMonth) {
                        if ((dayOfMonth < 10) && (monthOfYear < 9)) {
                            txtView.setText("0" + dayOfMonth + "/0"
                                    + ++monthOfYear + "/" + year);
                        } else if ((dayOfMonth < 10) && (monthOfYear > 8)) {
                            txtView.setText("0" + dayOfMonth + "/"
                                    + ++monthOfYear + "/" + year);
                        } else if ((dayOfMonth > 9) && (monthOfYear < 9)) {
                            txtView.setText(dayOfMonth + "/0" + ++monthOfYear
                                    + "/" + year);
                        } else {
                            txtView.setText(dayOfMonth + "/" + ++monthOfYear
                                    + "/" + year);
                        }
                        final SimpleDateFormat sdf = new SimpleDateFormat(
                                "dd/MM/yyyy");
                        if (date.equals("startDate")) {
                            try {
                                startDate = sdf.parse(txtView.getText()
                                        .toString());
                            } catch (final ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                endDate = sdf.parse(txtView.getText()
                                        .toString());
                                endDate.setTime(endDate.getTime() + 24 * 60
                                        * 60 * 1000);
                            } catch (final ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        datepicker.show();
    }

    private void dateFilters() {
        if ((startDate != null) && (endDate != null)) {
            adapter.replaceLazyList(EzApp.messageDao
                    .queryBuilder()
                    .where(Properties.Message_type.eq(Constants.OUTBOX),
                            Properties.To_name.like("%"
                                    + editFilter.getText().toString() + "%"),
                            Properties.Date.between(startDate, endDate))
                    .orderDesc(Properties.Date).listLazy());
            tCount = adapter.getCount();
            updateCount();
        } else if (startDate != null) {
            adapter.replaceLazyList(EzApp.messageDao
                    .queryBuilder()
                    .where(Properties.Message_type.eq(Constants.OUTBOX),
                            Properties.To_name.like("%"
                                    + editFilter.getText().toString() + "%"),
                            Properties.Date.gt(startDate))
                    .orderDesc(Properties.Date).listLazy());
            tCount = adapter.getCount();
            updateCount();
        } else if (endDate != null) {
            adapter.replaceLazyList(EzApp.messageDao
                    .queryBuilder()
                    .where(Properties.Message_type.eq(Constants.OUTBOX),
                            Properties.To_name.like("%"
                                    + editFilter.getText().toString() + "%"),
                            Properties.Date.lt(endDate))
                    .orderDesc(Properties.Date).listLazy());
            tCount = adapter.getCount();
            updateCount();
        } else {
            adapter.replaceLazyList(EzApp.messageDao
                    .queryBuilder()
                    .where(Properties.Message_type.eq(Constants.OUTBOX),
                            Properties.To_name.like("%"
                                    + editFilter.getText().toString() + "%"))
                    .orderDesc(Properties.Date).listLazy());
            tCount = adapter.getCount();
            updateCount();
        }
        txtStartDate.setText("");
        txtEndDate.setText("");
    }

    public void refresh() {
        getActivity().findViewById(R.id.rl_messages)
                .setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.rl_message).setVisibility(View.GONE);

    }

    public void getLabOutboxMessages(final int pageno) {
        String url = LabApis.OUTBOX;
        final HashMap<String, String> params = new HashMap<String, String>();
        params.put("cli", "api");
        params.put("page_num", "" + pageno);
        params.put("condval", "");
        EzApp.networkController.networkCall(getActivity(), url,
                params, new OnResponse() {

                    @Override
                    public void onResponseListner(String response) {
                        Log.i("", response);
                        listPb.setVisibility(View.GONE);
                        try {
                            if (pageno == 1) {
                                EzApp.messageDao
                                        .queryBuilder()
                                        .where(com.ezhealthtrack.greendao.MessageModelDao.Properties.Message_type
                                                .eq(Constants.OUTBOX))
                                        .buildDelete()
                                        .executeDeleteWithoutDetachingEntities();
                                // EzHealthApplication.messageDao.deleteAll();
                            }
                            final JSONObject jObj = new JSONObject(response);
                            final JSONArray data = jObj.getJSONArray("data");
                            OutboxFragment.totalCount = jObj.getInt("count");
                            Gson gson = new GsonBuilder().setDateFormat(
                                    "yyyy-MM-dd hh:mm").create();
                            for (int i = 0; i < data.length(); i++) {

                                final MessageModel model = gson.fromJson(data
                                                .getJSONObject(i).toString(),
                                        MessageModel.class);
                                model.setId((long) Integer.parseInt(model
                                        .getNid()));
                                model.setMessage_type(Constants.OUTBOX);
                                EzApp.messageDao
                                        .insertOrReplace(model);
                            }
                            notifyList();
                        } catch (final JSONException e) {
                            Log.e("", e.toString());
                        }

                    }
                });
    }
}

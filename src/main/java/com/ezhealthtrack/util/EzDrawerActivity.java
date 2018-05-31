package com.ezhealthtrack.util;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.ezhealthtrack.R;
import com.ezhealthtrack.one.EzUtils;
import com.flurry.android.FlurryAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract public class EzDrawerActivity extends EzActivity {

    final static public String MENU_HOME_Dashboard = "Dashboard";

    final static public String MENU_APTMNT_Confirmed = "Confirmed";
    final static public String MENU_APTMNT_Tentative = "Tentative";
    final static public String MENU_APTMNT_Schedule = "Schedule";
    final static public String MENU_APTMNT_History = "History";

    final static public String MENU_MESG_Inbox = "Inbox";
    final static public String MENU_MESG_Outbox = "Outbox";
    final static public String MENU_MESG_Alerts = "Alerts";
    final static public String MENU_MESG_Reports = "Reports";

    final static public String MENU_PATIENTS_Patients = "Patients";

    final static public String MENU_MEDREC_VisitNotes = "Visit Notes";
    final static public String MENU_MEDREC_Allergies = "Allergies";
    final static public String MENU_MEDREC_Vitals = "Vitals";
    final static public String MENU_MEDREC_Prescriptions = "Prescriptions";
    final static public String MENU_MEDREC_Radiology = "Radiology";
    final static public String MENU_MEDREC_ClinicalLabs = "Clinical Labs";
    final static public String MENU_MEDREC_EkgEcg = "EKG/ECG";

    final static public String MENU_ORDERS_LabOrders = "Lab Orders";

    final static public String MENU_REFERALLS_In = "In Referrals";
    final static public String MENU_REFERALLS_Out = "Out Referrals";

    final static public String MENU_ASSISTENTS_Assistents = "Assistants";

    final static public String MENU_ACCOUNT_Account = "Account";
    final static public String MENU_ACCOUNT_SchedulePlan = "Schedule Plan";
    final static public String MENU_ACCOUNT_Profile = "Profile";
    final static public String MENU_ACCOUNT_LabPreferences = "Lab Preferences";
    final static public String MENU_ACCOUNT_RadiologyPreferences = "Radiology Preferences";

    final static public String MENU_ABOUT = "About";

    final static public String MENU_LOGOUT_Logout = "Logout";

    protected String mTitle;
    protected String mSubTitle;

    protected DrawerLayout mDrawerLayout;
    protected ActionBarDrawerToggle mDrawerToggle;
    private boolean isDrawerLocked = false;

    protected List<String> mGroups;
    protected Map<String, List<String>> mCollection;

    protected ExpandableListView mListView;
    protected EzExpandableListAdapter mListAdapter;

    protected Fragment mActiveFragment;
    protected String mActiveFragmentTag;

    // to be implemented by derived class
    abstract protected void setFragmentByTag(String item);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.restoreInstanceState(savedInstanceState);
        setDrawerItems();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this, "D2Z8WP9VVBJ6N9YSMZPW");
    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }

    // to be called by derived class after setContentView()
    protected void setHomeAsDrawerEnabled(Bundle savedInstanceState) {

        // enable ActionBar App Icon
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setHomeButtonEnabled(true);

        if (mTitle == null || mTitle.length() < 1)
            mTitle = getTitle().toString();

        if (mSubTitle == null || mSubTitle.length() < 1)
            mSubTitle = getTitle().toString();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mListView = (ExpandableListView) findViewById(R.id.left_drawer);

        // custom shadow that overlays main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);
        // mListView.setDivider(new ColorDrawable(Color.parseColor("#d0d0d0")));

        if ((int) getResources().getDimension(R.dimen.drawer_content_padding) != 0) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN,
                    mListView);
            mDrawerLayout.setScrimColor(Color.TRANSPARENT);
            isDrawerLocked = true;
            Log.v("EDA:setHomeAsDrawerEnabled", "OPEN - LOCKED");
        } else {
            Log.v("EDA:setHomeAsDrawerEnabled", "NORMAL - UNLOCKED");
        }

        // setting the nav drawer list adapter
        mListAdapter = new EzExpandableListAdapter(this, mGroups, mCollection);
        mListView.setAdapter(mListAdapter);

        final Activity activity = this;
        mListView.setOnChildClickListener(new OnChildClickListener() {

            public boolean onChildClick(ExpandableListView parent, View v,
                                        int gPos, int cPos, long id) {

                // update selected item and title, then close the drawer
                mListView.setItemChecked(getHighlightedRowIndex(), true);
                if (!isDrawerLocked) {
                    mDrawerLayout.closeDrawer(mListView);
                }
                String item = (String) mListAdapter.getChild(gPos, cPos);
                setFragmentByTag(item);
                // EzUtils.showLong("Show (" + gPos + ":" + cPos + ") " + item);
                return true;
            }
        });
        mListView.expandGroup(0);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                if (EzUtils.getDeviceSize(getApplicationContext()) == EzUtils.EZ_SCREEN_SMALL) {
                    setActionBarTitle("" + mTitle);
                    getSupportActionBar().setSubtitle(mSubTitle);
                }else {
                    setActionBarTitle("ezHealthTrack");
                }
                ActivityCompat.invalidateOptionsMenu(activity);
                mListAdapter.notifyDataSetChanged();
            }

            public void onDrawerOpened(View drawerView) {
                //setActionBarTitle("ezHealthTrack");
                ActivityCompat.invalidateOptionsMenu(activity);
                mListAdapter.notifyDataSetChanged();
            }
        };

        // if not locked
        if (!isDrawerLocked) {
            mDrawerLayout.setDrawerListener(mDrawerToggle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(false);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            //getSupportActionBar().setIcon(R.drawable.ic_launcher);
        }

        // if (savedInstanceState == null) {
        // this.showFragment(null, null);
        // }
    }

    protected void showFragment(Fragment fragment) {
        showFragment(fragment, null);
    }

    protected void showFragment(Fragment fragment, String tag) {

        Log.e("EDA:showFragment()", "Fragment= " + fragment + ", Tag=" + tag);
        mActiveFragment = fragment;
        mActiveFragmentTag = tag;

        if (mActiveFragment == null) {
            Log.e("EDA::showFragment()", "Error: Active Fragment is NULL");
            return;
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (mActiveFragmentTag == null) {
            ft.replace(R.id.fragment_container, mActiveFragment).commit();
        } else {
            ft.replace(R.id.fragment_container, mActiveFragment,
                    mActiveFragmentTag).commit();
        }
        // update selected item and title, then close the drawer
        mListView.setItemChecked(getHighlightedRowIndex(), true);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!isDrawerLocked) {
            // If drawer is open, hide menu items
            boolean drawerOpen = mDrawerLayout.isDrawerOpen(mListView);
            for (int i = 0; i < menu.size(); i++)
                menu.getItem(i).setVisible(!drawerOpen);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout.isDrawerOpen(mListView);
    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(mListView);
    }

    private int getHighlightedRowIndex() {
        return 1;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isDrawerLocked) {
            mDrawerLayout.closeDrawer(mListView);
        }
    }

    @Override
    protected void onPause() {
        if (!isDrawerLocked) {
            mDrawerLayout.closeDrawer(mListView);
        }
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save custom values into the bundle
        savedInstanceState.putString("DAFragmentTag", mActiveFragmentTag);
        Log.e("EDA:onSaveInstanceState()", "Active Fragment Tag"
                + mActiveFragmentTag);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void restoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null)
            return;
        // Restore state members from saved instance
        mActiveFragmentTag = savedInstanceState.getString("DAFragmentTag");
        Log.e("EDA:onRestoreInstanceState()", "Active Fragment Tag"
                + mActiveFragmentTag);
    }

    void setDrawerItems() {
        String parent;
        List<String> children;

        if (mGroups == null) {
            mGroups = new ArrayList<String>();
            mCollection = new HashMap<String, List<String>>();
        }

        // add Group (Dashboard) and it's children
        parent = "Home";
        children = new ArrayList<String>();
        children.add(MENU_HOME_Dashboard);
        mGroups.add(parent);
        mCollection.put(parent, children);

        // add Group (Appointments) and it's children
        parent = "Appointments";
        children = new ArrayList<String>();
        children.add(MENU_APTMNT_Confirmed);
        children.add(MENU_APTMNT_Tentative);
        // children.add(MENU_APTMNT_Schedule);
        children.add(MENU_APTMNT_History);
        mGroups.add(parent);
        mCollection.put(parent, children);

        // add Group (Messages) and it's children
        parent = "Messages";
        children = new ArrayList<String>();
        children.add(MENU_MESG_Inbox);
        children.add(MENU_MESG_Outbox);
        children.add(MENU_MESG_Alerts);
        // children.add(MENU_MESG_Reports);
        mGroups.add(parent);
        mCollection.put(parent, children);

        // add Group (Patients) and it's children
        parent = "Patients";
        children = new ArrayList<String>();
        children.add(MENU_PATIENTS_Patients);
        mGroups.add(parent);
        mCollection.put(parent, children);

        // add Group (Medical Records) and it's children
        parent = "Medical Records";
        children = new ArrayList<String>();
        children.add(MENU_MEDREC_VisitNotes);
        children.add(MENU_MEDREC_Allergies);
        children.add(MENU_MEDREC_Vitals);
        children.add(MENU_MEDREC_Prescriptions);
        children.add(MENU_MEDREC_Radiology);
        children.add(MENU_MEDREC_ClinicalLabs);
        children.add(MENU_MEDREC_EkgEcg);
        mGroups.add(parent);
        mCollection.put(parent, children);

        parent = "Patient Orders";
        children = new ArrayList<String>();
        children.add(MENU_ORDERS_LabOrders);
        mGroups.add(parent);
        mCollection.put(parent, children);

        parent = "Referrals";
        children = new ArrayList<String>();
        children.add(MENU_REFERALLS_In);
        children.add(MENU_REFERALLS_Out);
        mGroups.add(parent);
        mCollection.put(parent, children);

        // EzUtils.showLong("ScreenSize " + EzUtils.getDeviceSize(this));
        if (EzUtils.getDeviceSize(this).equals(EzUtils.EZ_SCREEN_LARGE)) {

            parent = "Assistants";
            children = new ArrayList<String>();
            children.add(MENU_ASSISTENTS_Assistents);
            mGroups.add(parent);
            mCollection.put(parent, children);

            parent = "My Account";
            children = new ArrayList<String>();
            children.add(MENU_ACCOUNT_Account);
            // children.add(MENU_ACCOUNT_SchedulePlan);
            children.add(MENU_ACCOUNT_Profile);
            children.add(MENU_ACCOUNT_LabPreferences);
            children.add(MENU_ACCOUNT_RadiologyPreferences);
            mGroups.add(parent);
            mCollection.put(parent, children);
        }

        // ABOUT option temporarily disabled
        // parent = "About";
        // children = new ArrayList<String>();
        // children.add(MENU_ABOUT);
        // mGroups.add(parent);
        // mCollection.put(parent, children);

        parent = "Logout";
        children = new ArrayList<String>();
        children.add(MENU_LOGOUT_Logout);
        mGroups.add(parent);
        mCollection.put(parent, children);
    }

}

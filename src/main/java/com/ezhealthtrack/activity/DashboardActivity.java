package com.ezhealthtrack.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.PopupMenu;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.ezhealthrack.api.APIs;
import com.ezhealthrack.api.NetworkCalls;
import com.ezhealthrack.api.NetworkCalls.OnResponse;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.about.AboutFragment;
import com.ezhealthtrack.controller.DataController;
import com.ezhealthtrack.controller.DoctorController;
import com.ezhealthtrack.controller.LoginController;
import com.ezhealthtrack.controller.MedicalRecordsController;
import com.ezhealthtrack.controller.ScheduleController;
import com.ezhealthtrack.fragments.AlertsFragment;
import com.ezhealthtrack.fragments.AssistantFragment;
import com.ezhealthtrack.fragments.AssistantSchedulePlanFragment;
import com.ezhealthtrack.fragments.ConfirmedFragment;
import com.ezhealthtrack.fragments.DashboardFragment;
import com.ezhealthtrack.fragments.HistoryFragment;
import com.ezhealthtrack.fragments.HospitalFragment;
import com.ezhealthtrack.fragments.HospitalSearchFragment;
import com.ezhealthtrack.fragments.InReferralListFragment;
import com.ezhealthtrack.fragments.InboxFragment;
import com.ezhealthtrack.fragments.InboxFragment.OnLinkClicked;
import com.ezhealthtrack.fragments.LabFragment;
import com.ezhealthtrack.fragments.LabOrdersFragment;
import com.ezhealthtrack.fragments.NewTentativeFragment;
import com.ezhealthtrack.fragments.OutReferralListFragment;
import com.ezhealthtrack.fragments.OutboxFragment;
import com.ezhealthtrack.fragments.PatientsListFragment;
import com.ezhealthtrack.fragments.PatientsListFragment.OnCallback;
import com.ezhealthtrack.fragments.ProfileFragment;
import com.ezhealthtrack.fragments.RadiologyFragment;
import com.ezhealthtrack.fragments.ReportFragment;
import com.ezhealthtrack.fragments.ScheduleListFragment;
import com.ezhealthtrack.fragments.SchedulePlanFragment;
import com.ezhealthtrack.fragments.SideFragment;
import com.ezhealthtrack.fragments.VisitNotesFragment;
import com.ezhealthtrack.greendao.Appointment;
import com.ezhealthtrack.greendao.MessageModel;
import com.ezhealthtrack.model.AllergyModel;
import com.ezhealthtrack.model.AssistantModel;
import com.ezhealthtrack.model.BranchInfo;
import com.ezhealthtrack.model.DayPlanModel;
import com.ezhealthtrack.model.HospitalModel;
import com.ezhealthtrack.model.InRefferalModel;
import com.ezhealthtrack.model.LabPreference;
import com.ezhealthtrack.model.MedRecClinicalLab;
import com.ezhealthtrack.model.MedRecEKG;
import com.ezhealthtrack.model.MedRecRadiology;
import com.ezhealthtrack.model.OutReferralModel;
import com.ezhealthtrack.model.PatientAutoSuggest;
import com.ezhealthtrack.model.PatientShow;
import com.ezhealthtrack.model.ProfileModel;
import com.ezhealthtrack.model.RadiologyModel;
import com.ezhealthtrack.model.VacationModel;
import com.ezhealthtrack.model.VitalModel;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.ui.MrListFragment;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.EzDrawerActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DashboardActivity extends EzDrawerActivity implements OnResponse,
        OnCallback, OnLinkClicked {
    final static public int MAX_PLAN_DAYS = 7;

    public static NetworkCalls calls;
    public static ImageLoader imgLoader;
    // public static ArrayList<Patient> arrPatients = new ArrayList<Patient>();
    public static ArrayList<Appointment> arrScheduledPatients = new ArrayList<Appointment>();
    public static ArrayList<Appointment> arrConfirmedPatients = new ArrayList<Appointment>();
    public static ArrayList<Appointment> arrHistoryPatients = new ArrayList<Appointment>();
    public static ArrayList<Appointment> arrNewTentativePatients = new ArrayList<Appointment>();
    // public static List<Object> arrLabOrders = new ArrayList<Object>();
    public static ArrayList<InRefferalModel> arrRefferedByPatients = new ArrayList<InRefferalModel>();
    public static ArrayList<OutReferralModel> arrRefferedToPatients = new ArrayList<OutReferralModel>();
    public static ArrayList<PatientShow> arrPatientShow = new ArrayList<PatientShow>();
    public static ArrayList<LabPreference> arrLabPreferences = new ArrayList<LabPreference>();
    public static ArrayList<LabPreference> arrLabSelected = new ArrayList<LabPreference>();
    public static ArrayList<RadiologyModel> arrRadiPreferences = new ArrayList<RadiologyModel>();
    public static ArrayList<String> arrRadiSelected = new ArrayList<String>();
    public static ArrayList<MessageModel> arrAlerts = new ArrayList<MessageModel>();
    public static ArrayList<HospitalModel> arrHospital = new ArrayList<HospitalModel>();
    public static DayPlanModel[] dayPlan = new DayPlanModel[MAX_PLAN_DAYS];
    public static ArrayList<VacationModel> arrVacations = new ArrayList<VacationModel>();
    public static ArrayList<AssistantModel> arrAssistants = new ArrayList<AssistantModel>();
    public static ArrayList<AssistantModel> arrCurrentAssistants = new ArrayList<AssistantModel>();
    public static ArrayList<AllergyModel> arrAllergy = new ArrayList<AllergyModel>();
    public static ArrayList<VitalModel> arrVital = new ArrayList<VitalModel>();
    public static ArrayList<MedRecRadiology> arrRadiology = new ArrayList<MedRecRadiology>();
    public static ArrayList<MedRecClinicalLab> arrClinicalLab = new ArrayList<MedRecClinicalLab>();
    public static ArrayList<MedRecEKG> arrEKG = new ArrayList<MedRecEKG>();
    public static ArrayList<PatientAutoSuggest> arrPatAutoSugg = new ArrayList<PatientAutoSuggest>();
    public static ProfileModel profile = new ProfileModel();

    public static SideFragment sideFragment;
    private static DashboardFragment dashboardFragment;

    private static PatientsListFragment patientsFragment;
    private static ScheduleListFragment scheduleFragment;
    public static ConfirmedFragment confirmedFragment;
    private static InReferralListFragment inReferralFragment;
    private static OutReferralListFragment outReferralFragment;
    private static SchedulePlanFragment schedulePlanFragment;
    public static NewTentativeFragment newTentativeFragment;
    private static HistoryFragment historyFragment;
    private static LabOrdersFragment labOrdersFragment;
    private static RadiologyFragment radiologyFragment;
    private static LabFragment labFragment;
    private static InboxFragment inboxFragment;
    private static OutboxFragment outboxFragment;
    private static AlertsFragment alertFragment;
    private static HospitalFragment hospitalFragment;
    private static HospitalSearchFragment hospitalSearchFragment;
    private static AssistantFragment assistantFragment;
    private static ProfileFragment profileFragment;
    private static AssistantSchedulePlanFragment assSpFragment;
    private static ReportFragment reportFragment;
    private static AboutFragment aboutFragment;

    private static VisitNotesFragment visitNotesFragment;
    // private static AllergiesFragment allergiesFragment;
    // private static VitalsFragment vitalsFragment;
    // private static PrescriptionsFragment prescriptionFragment;
    // private static RadiologyMRecordsFragment radiologyMRecordFragment;
    // private static ClinicalLabFragment clinicalLabFragment;
    // private static EKGFragment ekgFragment;

    private static MrListFragment allergiesFragment;
    private static MrListFragment vitalsFragment;
    private static MrListFragment prescriptionFragment;
    private static MrListFragment radiologyMRecordFragment;
    private static MrListFragment clinicalLabFragment;
    private static MrListFragment ekgFragment;

    private final HashMap<String, String> hashLabPref = new HashMap<String, String>();
    public HashMap<String, String> hashRadiPref = new HashMap<String, String>();
    private MenuItem menuHospital;
    public static String filterText;

    //private String mTitle;

    @Override
    protected void onResume() {
        // if (confirmedFragment.isVisible())
        // calls.getConfirmedList(1, "");
        super.onResume();
    }

    @Override
    public void onBackPressed() {

        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)
                && EzUtils.getDeviceSize(this.getApplicationContext()) == EzUtils.EZ_SCREEN_LARGE
                && EzUtils.getDeviceSize(this.getApplicationContext()) == EzUtils.EZ_SCREEN_MEDIUM) {
            this.mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }

        if (!mActiveFragmentTag.equals(EzDrawerActivity.MENU_HOME_Dashboard)) {
            this.setFragmentByTag(EzDrawerActivity.MENU_HOME_Dashboard);
            if (EzUtils.getDeviceSize(this.getApplicationContext()) == EzUtils.EZ_SCREEN_SMALL) {
                getSupportActionBar().setTitle("Dashboard");
                getSupportActionBar().setSubtitle("");
            }
            return;
        }

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder.setTitle("Do you wish to Exit ?");
        alertDialogBuilder
                .setCancelable(true)
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

    static boolean doInit;

    static public void resetOnLogout() {
        doInit = false;
    }

    private void doInit() {
        if (doInit == true)
            return;
        doInit = true;

        // SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM dd', 'yyyy");
        // String s = "<b>" + sdf.format(new Date()) + "</b>";
        // ((TextView) findViewById(R.id.txt_time)).setText(Html.fromHtml(s));

        calls = new NetworkCalls(this);

        // DashboardActivity.arrPatients.clear();
        DashboardActivity.arrScheduledPatients.clear();
        DashboardActivity.arrConfirmedPatients.clear();
        DashboardActivity.arrNewTentativePatients.clear();

        if (findViewById(R.id.fragment_container) == null)
            return;

        // sideFragment = new SideFragment();
        // getSupportFragmentManager().beginTransaction()
        // .add(R.id.side_fragment, sideFragment).commit();
        dashboardFragment = new DashboardFragment();
        // dashboardFragment.setArguments(getIntent().getExtras());
        // getSupportFragmentManager().beginTransaction()
        // .add(R.id.fragment_container, dashboardFragment).commit();
        mActiveFragment = dashboardFragment;

        patientsFragment = new PatientsListFragment();
        patientsFragment.setArguments(getIntent().getExtras());
        scheduleFragment = new ScheduleListFragment();
        scheduleFragment.setArguments(getIntent().getExtras());
        confirmedFragment = new ConfirmedFragment();
        confirmedFragment.setArguments(getIntent().getExtras());
        inReferralFragment = new InReferralListFragment();
        inReferralFragment.setArguments(getIntent().getExtras());
        outReferralFragment = new OutReferralListFragment();
        outReferralFragment.setArguments(getIntent().getExtras());
        schedulePlanFragment = new SchedulePlanFragment();
        schedulePlanFragment.setArguments(getIntent().getExtras());
        newTentativeFragment = new NewTentativeFragment();
        newTentativeFragment.setArguments(getIntent().getExtras());
        historyFragment = new HistoryFragment();
        historyFragment.setArguments(getIntent().getExtras());
        labOrdersFragment = new LabOrdersFragment();
        labOrdersFragment.setArguments(getIntent().getExtras());
        radiologyFragment = new RadiologyFragment();
        radiologyFragment.setArguments(getIntent().getExtras());
        labFragment = new LabFragment();
        labFragment.setArguments(getIntent().getExtras());
        inboxFragment = new InboxFragment();
        inboxFragment.setArguments(getIntent().getExtras());
        outboxFragment = new OutboxFragment();
        outboxFragment.setArguments(getIntent().getExtras());
        alertFragment = new AlertsFragment();
        alertFragment.setArguments(getIntent().getExtras());
        hospitalFragment = new HospitalFragment();
        hospitalFragment.setArguments(getIntent().getExtras());
        hospitalSearchFragment = new HospitalSearchFragment();
        hospitalSearchFragment.setArguments(getIntent().getExtras());
        assistantFragment = new AssistantFragment();
        assistantFragment.setArguments(getIntent().getExtras());
        profileFragment = new ProfileFragment();
        profileFragment.setArguments(getIntent().getExtras());
        assSpFragment = new AssistantSchedulePlanFragment();
        assSpFragment.setArguments(getIntent().getExtras());
        reportFragment = new ReportFragment();
        reportFragment.setArguments(getIntent().getExtras());
        aboutFragment = new AboutFragment();
        aboutFragment.setArguments(getIntent().getExtras());
        visitNotesFragment = new VisitNotesFragment();
        visitNotesFragment.setArguments(getIntent().getExtras());

        // allergiesFragment = new AllergiesFragment();
        // allergiesFragment.setArguments(getIntent().getExtras());
        // vitalsFragment = new VitalsFragment();
        // vitalsFragment.setArguments(getIntent().getExtras());
        // prescriptionFragment = new PrescriptionsFragment();
        // prescriptionFragment.setArguments(getIntent().getExtras());
        // radiologyMRecordFragment = new RadiologyMRecordsFragment();
        // radiologyMRecordFragment.setArguments(getIntent().getExtras());
        // clinicalLabFragment = new ClinicalLabFragment();
        // clinicalLabFragment.setArguments(getIntent().getExtras());
        // ekgFragment = new EKGFragment();
        // ekgFragment.setArguments(getIntent().getExtras());

        allergiesFragment = new MrListFragment();
        allergiesFragment
                .setFragmentType(MedicalRecordsController.ALLERGY_TYPE);
        allergiesFragment.setArguments(getIntent().getExtras());
        vitalsFragment = new MrListFragment();
        vitalsFragment.setFragmentType(MedicalRecordsController.VITALS_TYPE);
        vitalsFragment.setArguments(getIntent().getExtras());
        prescriptionFragment = new MrListFragment();
        prescriptionFragment
                .setFragmentType(MedicalRecordsController.PRESCRIPTION_TYPE);
        prescriptionFragment.setArguments(getIntent().getExtras());
        radiologyMRecordFragment = new MrListFragment();
        radiologyMRecordFragment
                .setFragmentType(MedicalRecordsController.RADIOLOGY_TYPE);
        radiologyMRecordFragment.setArguments(getIntent().getExtras());
        clinicalLabFragment = new MrListFragment();
        clinicalLabFragment.setFragmentType(MedicalRecordsController.LAB_TYPE);
        clinicalLabFragment.setArguments(getIntent().getExtras());
        ekgFragment = new MrListFragment();
        ekgFragment.setFragmentType(MedicalRecordsController.ECG_TYPE);
        ekgFragment.setArguments(getIntent().getExtras());

        // calls.getSchedulePlan();
        // calls.getScheduleData();

        for (int i = 0; i < 7; i++) {
            DashboardActivity.dayPlan[i] = new DayPlanModel();
        }
        if (!EzApp.sharedPref.getString(Constants.DR_SPECIALITY, "").equals(
                "Dentist")) {
            DoctorController.getLabPreference(hashLabPref,
                    DashboardActivity.this);
            calls.getRadiPreference(hashRadiPref);
        }
        calls.getAccount();
        // final String url = APIs.PATIENT_LIST() + 0;
        // calls.getPatientList(url);
        // calls.getAssistants(1, "");
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_fragment);
        Log.v("DA:onCreate()", "IN... ");
        doInit();
        super.setHomeAsDrawerEnabled(savedInstanceState);

        if (EzUtils.getDeviceSize(this.getApplicationContext()) == EzUtils.EZ_SCREEN_SMALL) {
            getSupportActionBar().setTitle("Dashboard");
            getSupportActionBar().setSubtitle("");
        }
    }

    @Override
    protected void setFragmentByTag(String item) {

        Log.v("DA:setFragmentByTag()", "Tag : "
                + (item == null ? "NULL" : item));

        // handle new activities first
        if (item != null && item.equals(EzDrawerActivity.MENU_ACCOUNT_Account)) {
            final Intent intent = new Intent(this, EditAccountActivity.class);
            startActivity(intent);
            return;
        }

        EzUtils.hideKeyBoard(this);

        // try {
        // InputMethodManager imm = (InputMethodManager)
        // getSystemService(Context.INPUT_METHOD_SERVICE);
        // imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        // } catch (Exception e) {
        // }

        if (item == null || item.equals(EzDrawerActivity.MENU_HOME_Dashboard)) {
            showFragment(dashboardFragment,
                    EzDrawerActivity.MENU_HOME_Dashboard);
            filterText = "";
            setTitle("Dashboard", "");
        } else if (item.equals(EzDrawerActivity.MENU_APTMNT_Confirmed)) {
            showFragment(confirmedFragment,
                    EzDrawerActivity.MENU_APTMNT_Confirmed);
            filterText = "";
            setTitle("Appointments", EzDrawerActivity.MENU_APTMNT_Confirmed);
        } else if (item.equals(EzDrawerActivity.MENU_APTMNT_Tentative)) {
            showFragment(mActiveFragment = newTentativeFragment,
                    EzDrawerActivity.MENU_APTMNT_Tentative);
            filterText = "";
            setTitle("Appointments", EzDrawerActivity.MENU_APTMNT_Tentative);
        } else if (item.equals(EzDrawerActivity.MENU_APTMNT_Schedule)) {
            showFragment(scheduleFragment,
                    EzDrawerActivity.MENU_APTMNT_Schedule);
            // EzHealthApplication.patientDao.deleteAll();
            // final String url = APIs.PATIENT_LIST() + 0;
            // calls.getPatientList(url);
            filterText = "";
            setTitle("Appointments", EzDrawerActivity.MENU_APTMNT_Schedule);
        } else if (item.equals(EzDrawerActivity.MENU_APTMNT_History)) {
            showFragment(historyFragment, EzDrawerActivity.MENU_APTMNT_History);
            filterText = "";
            setTitle("Appointments", EzDrawerActivity.MENU_APTMNT_History);
        } else if (item.equals(EzDrawerActivity.MENU_MESG_Inbox)) {
            showFragment(inboxFragment, EzDrawerActivity.MENU_MESG_Inbox);
            calls.getInboxMessages(1);
            // if (inboxFragment.isVisible())
            // inboxFragment.refresh();
            filterText = "";
            setTitle("Messages", EzDrawerActivity.MENU_MESG_Inbox);
        } else if (item.equals(EzDrawerActivity.MENU_MESG_Outbox)) {
            showFragment(outboxFragment, EzDrawerActivity.MENU_MESG_Outbox);
            if (outboxFragment.isVisible()) {
                outboxFragment.refresh();
            }
            filterText = "";
            setTitle("Messages", EzDrawerActivity.MENU_MESG_Outbox);
        } else if (item.equals(EzDrawerActivity.MENU_MESG_Alerts)) {
            showFragment(alertFragment, EzDrawerActivity.MENU_MESG_Alerts);
            calls.getAlerts(1);
            filterText = "";
            setTitle("Messages", EzDrawerActivity.MENU_MESG_Alerts);
        } else if (item.equals(EzDrawerActivity.MENU_MESG_Reports)) {
            showFragment(reportFragment, EzDrawerActivity.MENU_MESG_Reports);
        } else if (item.equals(EzDrawerActivity.MENU_PATIENTS_Patients)) {
            showFragment(patientsFragment,
                    EzDrawerActivity.MENU_PATIENTS_Patients);
            // final String url = APIs.PATIENT_LIST() + 0;
            // calls.getPatientList(url);
            filterText = "";
            setTitle("Patients", "");
        } else if (item.equals(EzDrawerActivity.MENU_MEDREC_VisitNotes)) {
            showFragment(visitNotesFragment,
                    EzDrawerActivity.MENU_MEDREC_VisitNotes);
            setTitle("Medical Records", EzDrawerActivity.MENU_MEDREC_VisitNotes);
        } else if (item.equals(EzDrawerActivity.MENU_MEDREC_Allergies)) {
            showFragment(allergiesFragment,
                    EzDrawerActivity.MENU_MEDREC_Allergies);
            setTitle("Medical Records", EzDrawerActivity.MENU_MEDREC_Allergies);
        } else if (item.equals(EzDrawerActivity.MENU_MEDREC_Vitals)) {
            showFragment(vitalsFragment, EzDrawerActivity.MENU_MEDREC_Vitals);
            setTitle("Medical Records", EzDrawerActivity.MENU_MEDREC_Vitals);
        } else if (item.equals(EzDrawerActivity.MENU_MEDREC_Prescriptions)) {
            showFragment(prescriptionFragment,
                    EzDrawerActivity.MENU_MEDREC_Prescriptions);
            setTitle("Medical Records", EzDrawerActivity.MENU_MEDREC_Prescriptions);
        } else if (item.equals(EzDrawerActivity.MENU_MEDREC_Radiology)) {
            showFragment(radiologyMRecordFragment,
                    EzDrawerActivity.MENU_MEDREC_Radiology);
            setTitle("Medical Records", EzDrawerActivity.MENU_MEDREC_Radiology);
        } else if (item.equals(EzDrawerActivity.MENU_MEDREC_ClinicalLabs)) {
            showFragment(clinicalLabFragment,
                    EzDrawerActivity.MENU_MEDREC_ClinicalLabs);
            setTitle("Medical Records", EzDrawerActivity.MENU_MEDREC_ClinicalLabs);
        } else if (item.equals(EzDrawerActivity.MENU_MEDREC_EkgEcg)) {
            showFragment(ekgFragment, EzDrawerActivity.MENU_MEDREC_EkgEcg);
            setTitle("Medical Records", EzDrawerActivity.MENU_MEDREC_EkgEcg);
        } else if (item.equals(EzDrawerActivity.MENU_ORDERS_LabOrders)) {
            showFragment(labOrdersFragment,
                    EzDrawerActivity.MENU_ORDERS_LabOrders);
            filterText = "";
            setTitle("Lab Orders", "");
        } else if (item.equals(EzDrawerActivity.MENU_REFERALLS_In)) {
            showFragment(inReferralFragment, EzDrawerActivity.MENU_REFERALLS_In);
            // calls.getInReferralList(1);
            filterText = "";
            setTitle("Referrals", EzDrawerActivity.MENU_REFERALLS_In);
        } else if (item.equals(EzDrawerActivity.MENU_REFERALLS_Out)) {
            showFragment(outReferralFragment,
                    EzDrawerActivity.MENU_REFERALLS_Out);
            // calls.getOutReferralList(1);
            filterText = "";
            setTitle("Referrals", EzDrawerActivity.MENU_REFERALLS_Out);
        } else if (item.equals(EzDrawerActivity.MENU_ASSISTENTS_Assistents)) {
            Log.v("DA:setFragment", "Before showFragment()");
            showFragment(assistantFragment,
                    EzDrawerActivity.MENU_ASSISTENTS_Assistents);
            Log.v("DA:setFragment", "After showFragment()");
            calls.getAssistants(1, "");
            Log.v("DA:setFragment", "After getAssistants()");
            filterText = "";
            setTitle("Assistants", "");
        } else if (item.equals(EzDrawerActivity.MENU_ACCOUNT_Profile)) {
            showFragment(profileFragment, EzDrawerActivity.MENU_ACCOUNT_Profile);
            calls.getProfile();
            filterText = "";
            setTitle("My Account", EzDrawerActivity.MENU_ACCOUNT_Profile);
        } else if (item.equals(EzDrawerActivity.MENU_ACCOUNT_SchedulePlan)) {
            showFragment(schedulePlanFragment,
                    EzDrawerActivity.MENU_ACCOUNT_SchedulePlan);
            // calls.getSchedulePlan();
            ScheduleController.getSchedulePlan();
            ScheduleController.getScheduleData();
            filterText = "";
        } else if (item.equals(EzDrawerActivity.MENU_ACCOUNT_LabPreferences)) {
            showFragment(labFragment,
                    EzDrawerActivity.MENU_ACCOUNT_LabPreferences);
            // calls.getLabPreference(hashLabPref);
            DoctorController.getLabPreference(hashLabPref,
                    DashboardActivity.this);
            filterText = "";
            setTitle("My Account", EzDrawerActivity.MENU_ACCOUNT_LabPreferences);
        } else if (item
                .equals(EzDrawerActivity.MENU_ACCOUNT_RadiologyPreferences)) {
            showFragment(radiologyFragment,
                    EzDrawerActivity.MENU_ACCOUNT_RadiologyPreferences);
            calls.getRadiPreference(hashRadiPref);
            filterText = "";
            setTitle("My Account", EzDrawerActivity.MENU_ACCOUNT_RadiologyPreferences);
        } else if (/* position == 14 */item.equals("Hospital?")) {
            final FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            transaction.replace(R.id.fragment_container, hospitalFragment);
            // transaction.addToBackStack(null);
            transaction.commit();
            DataController.getDashboard(this);
            filterText = "";
        } else if (/* position == 15 */item.equals("HospitalSearch?")) {
            final FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            transaction
                    .replace(R.id.fragment_container, hospitalSearchFragment);
            // transaction.addToBackStack(null);
            transaction.commit();
            filterText = "";
        } else if (item.equals(EzDrawerActivity.MENU_ABOUT)) {
            showFragment(aboutFragment, EzDrawerActivity.MENU_ABOUT);
        } else if (item.equals(EzDrawerActivity.MENU_LOGOUT_Logout)) {
            // calls.logout();
            LoginController controller = new LoginController(this);
            controller.onLogout();
        } else {
            EzUtils.showLong(this, "Unknown Action: " + item);
        }
    }

    public void setTitle(String title, String subTitle) {
        if (EzUtils.getDeviceSize(this.getApplicationContext()) == EzUtils.EZ_SCREEN_SMALL) {
            mTitle = title;
            mSubTitle = subTitle;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu items for use in the action bar
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        final MenuItem item = menu.findItem(R.id.id_dr_name);
        item.setTitle("  " + EzApp.sharedPref.getString(Constants.DR_NAME, ""));
        DashboardActivity.imgLoader.get(
                EzApp.sharedPref.getString(Constants.DR_IMAGE, ""),
                new ImageListener() {

                    @Override
                    public void onErrorResponse(final VolleyError arg0) {
                    }

                    @Override
                    public void onResponse(final ImageContainer ic,
                                           final boolean arg1) {
                        item.setIcon(new BitmapDrawable(getResources(), ic
                                .getBitmap()));

                    }
                });
        menuHospital = menu.findItem(R.id.id_branch);
        menuHospital.setTitle("Branch");
        if (!EzUtils.getDeviceSize(this).equals(EzUtils.EZ_SCREEN_LARGE)) {
            menu.removeItem(R.id.id_add_patient);
            menu.removeItem(R.id.id_branch);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.id_add_patient:
                intent = new Intent(DashboardActivity.this,
                        AddPatientActivity.class);
                startActivity(intent);
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
                        popup.getMenu().add(1, i, i, branch.getString("name"));
                    }

                    // registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(final MenuItem item) {
                            int index = item.getItemId();
                            if (BranchInfo.setBranchByIndex(index) != true)
                                return true;

                            // clear top and (re)start DashboardActivity activity
                            Intent intent = new Intent(DashboardActivity.this,
                                    DashboardActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            DashboardActivity.this.startActivity(intent);
                            return true;
                        }
                    });
                    popup.show();

                } catch (Exception e) {

                }
                return true;

            case R.id.id_branch:
                // ANIL - sideFragment.hospitalClicked();
                return true;

            case R.id.id_compose:

                final PopupMenu popup = new PopupMenu(this,
                        findViewById(R.id.id_compose));

                popup.getMenu().add(1, 1, 1, "New InApp Message");
                popup.getMenu().add(1, 2, 2, "New Email");
                popup.getMenu().add(1, 3, 3, "New SMS");

                // registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(final MenuItem item) {
                        int index = item.getItemId();
                        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        ComposeMessageActivity.mType = ComposeMessageActivity.MTYPE_NONE;
                        if (index == 1) {
                            ComposeMessageActivity.mType = ComposeMessageActivity.MTYPE_MESSAGE;
                        } else if (index == 2) {
                            ComposeMessageActivity.mType = ComposeMessageActivity.MTYPE_EMAIL;
                        } else if (index == 3) {
                            ComposeMessageActivity.mType = ComposeMessageActivity.MTYPE_SMS;
                        }
                        Intent intent = new Intent(DashboardActivity.this,
                                ComposeMessageActivity.class);
                        startActivity(intent);
                        return true;
                    }
                });
                popup.show();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void spAssistant() {
        final FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.fragment_container, assSpFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void onResponseListner(String api) {
        if (api.equals(APIs.PATIENT_LIST())) {

            // Temporarily disabled {
            // if (patientsFragment.isVisible()) {
            // patientsFragment.notifylist();
            // } else if (scheduleFragment.isVisible()) {
            // scheduleFragment.notifylist();
            // }
            // }

            // if (scheduleFragment.isVisible())
            // scheduleFragment.notifylist();

        } else if (api.equals(APIs.OUTBOX_MESSAGES())) {
            if (outboxFragment.isVisible()) {
                outboxFragment.notifyList();
            }
        } else if (api.equals(APIs.ALERTS())) {
            if (alertFragment.isVisible()) {
                alertFragment.notifyList();
            }
        } else if (api.equals(APIs.ASSISTANTS())) {
            if (assistantFragment.isVisible()) {
                assistantFragment.notifyList();
            }
        } else if (api.equals(APIs.CONFIRMED_LIST())) {
            if (confirmedFragment.isVisible()) {
                confirmedFragment.updateList();
            }
        } else if (api.equals(APIs.HISTORY_LIST())) {
            // if (historyFragment.isVisible()) {
            // historyFragment.updateList();
            // }
        } else if (api.equals(APIs.LAB_ORDERS_LIST())) {
            if (labOrdersFragment.isVisible()) {
                EzUtils.showLong(this, "Lab Order - New Impl");
                // labOrdersFragment.updateList();
            }
        } else if (api.equals(APIs.DASHBOARD())) {
            dashboardFragment.updateData();
        } else if (api.equals(APIs.HOSPITAL())) {
            try {
                menuHospital.setTitle(" "
                        + arrHospital.get(0).getLocation_title());
                // String url = arrHospital.get(0).getPhoto();
                // if (url != null) {
                // Log.v("DA:onResponseListner()", "Branch Image URL : "
                // + APIs.IMAGE() + url);
                // DashboardActivity.imgLoader.get(APIs.IMAGE()
                // + arrHospital.get(0).getPhoto(),
                // new ImageListener() {
                //
                // @Override
                // public void onErrorResponse(
                // final VolleyError arg0) {
                // }
                //
                // @Override
                // public void onResponse(final ImageContainer ic,
                // final boolean arg1) {
                // menuHospital.setIcon(new BitmapDrawable(
                // getResources(), ic.getBitmap()));
                //
                // }
                // });
                // }
                if (hospitalFragment.isVisible()) {
                    hospitalFragment.notifyList();
                }
            } catch (Exception e) {

            }
        } else if (api.equals(APIs.IN_REFFERRAL_LIST())) {
            // if (inReferralFragment.isVisible())
            // inReferralFragment.notifyList();
        } else if (api.equals(APIs.INBOX_MESSAGES())) {
            // if (inboxFragment.isVisible()) {
            // inboxFragment.notifyList();
            // }
            // ANIL - sideFragment.updateMessageCount();
        } else if (api.equals(APIs.OUT_REFFERRAL_LIST())) {
            // sideFragment.updateRefCount();
            // outReferralFragment.updateList();
        } else if (api.equals(APIs.NEW_PATIENT_LIST())) {
            if (newTentativeFragment.isVisible())
                newTentativeFragment.onResume();
        } else if (api.equals(APIs.HOSPITAL())) {
            if (hospitalFragment.isVisible())
                hospitalFragment.updateData();
        } else if (api.equals(APIs.PROFILE())) {
            if (profileFragment.profFragment.isVisible()) {
                profileFragment.profFragment.updateData();
            }
        }

    }

    public void postRadiologyPref() {
        calls.postRadiologyPref(hashRadiPref);
    }

    public void postLabPref() {
        calls.postLabPref();
    }

    @Override
    public void onCallback(String id, String name) {
        // ANIL - sideFragment.medRecClicked();
        filterText = name;
        final FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.fragment_container, visitNotesFragment);
        // transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void onLinkClicked(String url) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("" + requestCode, "" + resultCode);

        if ((resultCode == RESULT_OK)) {
            String fileSelected = data
                    .getStringExtra(com.orleonsoft.android.simplefilechooser.Constants.KEY_FILE_SELECTED);
            if (requestCode == profileFragment.insFragment.FILE_CHOOSER) {
                Log.i("", fileSelected);
                profileFragment.insFragment.intentReceived(fileSelected);
            } else if (requestCode == profileFragment.pubFragment.FILE_CHOOSER) {
                profileFragment.pubFragment.intentReceived(fileSelected);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.setFragmentByTag(mActiveFragmentTag);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private AlertDialog mNewAppDialog;

    // not required - will use Google Play
    void showNewAppAlert() {

        String mesg = "Please download and install new App.";
        mesg += "\nCurrent App version: ";
        mesg += "\nNew App version: ";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New App Available")
                .setMessage(mesg)
                .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // download file
                                EzUtils.downloadFile(
                                        DashboardActivity.this,
                                        "http://dev.oyepages.com/data/mZones.json",
                                        "EzHealthTrack", "Downloading app",
                                        "ezhealthtrack.apk");
                            }
                        })
                .setNegativeButton(android.R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // do nothing
                            }
                        }).setIcon(android.R.drawable.ic_dialog_alert);
        mNewAppDialog = builder.create();
        mNewAppDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("DA::onDestroy()", "In..");
    }

}

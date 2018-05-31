package com.ezhealthtrack.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.print.PrintHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.activity.SheduleActivity;
import com.ezhealthtrack.activity.SheduleActivityNew;
import com.ezhealthtrack.activity.UpdatePatientActivity;
import com.ezhealthtrack.controller.DoctorController;
import com.ezhealthtrack.controller.PatientController;
import com.ezhealthtrack.dialogs.EzDialog;
import com.ezhealthtrack.fragments.EzGridFragment;
import com.ezhealthtrack.fragments.PatientsListFragment.OnCallback;
import com.ezhealthtrack.greendao.Patient;
import com.ezhealthtrack.labs.activity.LabsOrderCreateActivity;
import com.ezhealthtrack.labs.controller.LabsAppointmentController;
import com.ezhealthtrack.labs.controller.LabsAppointmentController.OnResponseApt;
import com.ezhealthtrack.labs.controller.OrderController.OnResponseData;
import com.ezhealthtrack.model.DoctorModel;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.NetworkCallController.OnResponse;
import com.ezhealthtrack.util.Util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PatientsListAdapter extends EzGridFragmentAdaptor {
    private static class ViewHolder {
        // Patient Image
        private ImageView imgPatient;

        // Patient Details
        private TextView txtPatientName;
        private TextView txtPatientAddress;
        private TextView txtEmail;
        private TextView txtPhone;
        private TextView txtDisplayId;

        // Patient Action Buttons
        private Button btnRefer;
        private Button btnShedule;
        private Button btnUpdate;
        private Button btnOrders;
        private Button btnNewOrder;
        private Button btnCheckin;
        private Button btnMedicalRecords;

        //Views/Lines
        private View vUpdate;
        private View vRefer;
        private View vSchedule;
        private View vCheckin;
        private View vOrders;
        private View vNewOrder;
    }

    Context context;
    OnCallback onCallback;
    PatientController patController = new PatientController();

    public PatientsListAdapter(List<Object> dataList, EzGridFragment fragment,
                               OnCallback onCallback) {
        super(dataList, fragment, onCallback);
        context = fragment.getActivity();
        this.onCallback = onCallback;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView,
                        final ViewGroup parent) {
        super.getView(position);

        final Patient pat = (Patient) mDataList.get(position);

        final ViewHolder mHolder;

        if (convertView == null) {
            final LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.row_patients_card, parent,
                    false);
            mHolder = new ViewHolder();

            // Set ID's
            mHolder.txtPatientName = (TextView) convertView
                    .findViewById(R.id.txt_name);
            mHolder.txtPatientAddress = (TextView) convertView
                    .findViewById(R.id.txt_address);
            mHolder.txtEmail = (TextView) convertView
                    .findViewById(R.id.txt_email);
            mHolder.txtPhone = (TextView) convertView
                    .findViewById(R.id.txt_phn);
            mHolder.txtDisplayId = (TextView) convertView
                    .findViewById(R.id.txt_display_id);
            mHolder.btnRefer = (Button) convertView
                    .findViewById(R.id.btn_refer);
            mHolder.btnShedule = (Button) convertView
                    .findViewById(R.id.btn_schedule);
            mHolder.btnUpdate = (Button) convertView
                    .findViewById(R.id.btn_update);
            mHolder.imgPatient = (ImageView) convertView
                    .findViewById(R.id.img_patient);
            mHolder.btnOrders = (Button) convertView
                    .findViewById(R.id.btn_orders);
            mHolder.btnNewOrder = (Button) convertView
                    .findViewById(R.id.btn_new_order);
            mHolder.btnCheckin = (Button) convertView
                    .findViewById(R.id.btn_checkin);
            mHolder.btnMedicalRecords = (Button) convertView
                    .findViewById(R.id.btn_medical_records);
            mHolder.vUpdate = (View) convertView.findViewById(R.id.view_update);
            mHolder.vRefer = (View) convertView.findViewById(R.id.view_refer);
            mHolder.vSchedule = (View) convertView.findViewById(R.id.view_schedule);
            mHolder.vCheckin = (View) convertView.findViewById(R.id.view_checkin);
            mHolder.vOrders = (View) convertView.findViewById(R.id.view_orders);
            mHolder.vNewOrder = (View) convertView.findViewById(R.id.view_new_order);

            // hide if non-Large screen device
            if (!EzUtils.getDeviceSize(null).equals(EzUtils.EZ_SCREEN_LARGE)) {
                mHolder.btnUpdate.setEnabled(false);
                mHolder.btnUpdate.setVisibility(View.GONE);
                mHolder.btnCheckin.setEnabled(false);
                mHolder.btnCheckin.setVisibility(View.GONE);
                mHolder.vUpdate.setVisibility(View.GONE);
                mHolder.vCheckin.setVisibility(View.GONE);
            }

            if (EzApp.sharedPref.getString(Constants.USER_TYPE, "")
                    .equalsIgnoreCase("D")) {
                mHolder.btnOrders.setVisibility(View.GONE);
                mHolder.btnNewOrder.setVisibility(View.GONE);
                mHolder.vOrders.setVisibility(View.GONE);
                mHolder.vNewOrder.setVisibility(View.GONE);
            } else if (EzApp.sharedPref.getString(Constants.USER_TYPE, "")
                    .equalsIgnoreCase("LT")) {
                mHolder.btnRefer.setVisibility(View.GONE);
                mHolder.btnCheckin.setVisibility(View.GONE);
                mHolder.btnMedicalRecords.setVisibility(View.GONE);
            }

            convertView.setTag(mHolder);

        } else {
            mHolder = (ViewHolder) convertView.getTag();
            // mHolder.imgPatient.setImageDrawable(null);
            mHolder.imgPatient.setImageResource(0);
        }

        // Get Patient Photo/Image | start{
        String url = "";
        url = APIs.VIEW() + pat.getPphoto();
        Util.getImageFromUrl(url, DashboardActivity.imgLoader,
                mHolder.imgPatient);
        // } end

        //Disabled for later use
        /*if (APIs.VIEW() + pat.getPphoto() != null) {
            Picasso.with(context).load(APIs.VIEW() + pat.getPphoto()).into(mHolder.imgPatient);
        } else {
            Picasso.with(context).load(APIs.NO_IMAGE()).into(mHolder.imgPatient);
        }*/

        // SET Patient Data
        mHolder.txtPatientName.setText(pat.getPfn() + " " + pat.getPmn() + " "
                + pat.getPln() + ", " + pat.getPage() + " / "
                + pat.getPgender());
        mHolder.txtPatientAddress.setText(pat.getPadd1() + " " + pat.getPadd2()
                + " " + pat.getParea() + " " + pat.getPcity() + " "
                + pat.getPstate() + " " + pat.getPcountry() + "-"
                + pat.getPzip());

        if (!Util.isEmptyString(pat.getPemail())) {
            mHolder.txtEmail.setText(pat.getPemail());
        } else {
            mHolder.txtEmail.setText(" -");
        }

        if (!Util.isEmptyString(pat.getPmobnum())) {
            mHolder.txtPhone.setText(pat.getPmobnum());
        } else {
            mHolder.txtPhone.setText(" -");
        }

        mHolder.txtDisplayId.setText(pat.getDisplay_id());

        // Action Buttons implementation
        mHolder.imgPatient.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Dialog imgdialog = new Dialog(context);
                if (EzUtils.getDeviceSize(null).equals(EzUtils.EZ_SCREEN_SMALL)) {
                    imgdialog = EzDialog.getDialog(context,
                            R.layout.dialog_patient_details_mobile,
                            "Patient Details");

                } else {
                    imgdialog = EzDialog.getDialog(context,
                            R.layout.dialog_patient_details, "Patient Details");
                }
                imgdialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));

                // Get Patient Image/Photo {
                Util.getImageFromUrl(APIs.VIEW() + pat.getPphoto(),
                        DashboardActivity.imgLoader,
                        (ImageView) imgdialog.findViewById(R.id.img_user));
                // }

                ((TextView) imgdialog.findViewById(R.id.txt_phone_display))
                        .setText(pat.getPmobnum());
                ((TextView) imgdialog.findViewById(R.id.txt_email_display))
                        .setText(pat.getPemail());
                ((TextView) imgdialog.findViewById(R.id.txt_name)).setText(pat
                        .getPfn()
                        + " "
                        + pat.getPmn()
                        + " "
                        + pat.getPln()
                        + "\n" + pat.getDisplay_id());
                ((TextView) imgdialog.findViewById(R.id.txt_gender_display))
                        .setText(pat.getPgender());
                ((TextView) imgdialog.findViewById(R.id.txt_dob_display))
                        .setText(pat.getPdob());
                ((TextView) imgdialog.findViewById(R.id.txt_address_display))
                        .setText(pat.getPadd1() + " " + pat.getPadd2());
                ((TextView) imgdialog.findViewById(R.id.txt_country_display))
                        .setText(pat.getPcountry());
                ((TextView) imgdialog.findViewById(R.id.txt_state_display))
                        .setText(pat.getPstate());
                ((TextView) imgdialog.findViewById(R.id.txt_city_display))
                        .setText(pat.getPcity());
                ((TextView) imgdialog.findViewById(R.id.txt_locality_display))
                        .setText(pat.getParea());
                ((TextView) imgdialog.findViewById(R.id.txt_zipcode_display))
                        .setText(pat.getPzip());
                ((TextView) imgdialog.findViewById(R.id.txt_id_display))
                        .setText(pat.getUid());
                ((TextView) imgdialog
                        .findViewById(R.id.txt_visible_mark_display))
                        .setText(pat.getVisiblemark());
                ((Button) imgdialog.findViewById(R.id.btn_print_card))
                        .setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                final Dialog dialogPrintCard = new Dialog(
                                        context);
                                dialogPrintCard
                                        .requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialogPrintCard
                                        .setContentView(R.layout.dialog_print_card);
                                dialogPrintCard
                                        .getWindow()
                                        .setBackgroundDrawable(
                                                new ColorDrawable(
                                                        android.graphics.Color.TRANSPARENT));

                                try {
                                    Date date = EzApp.sdfddMmyy.parse(pat
                                            .getPdob());
                                    ((TextView) dialogPrintCard
                                            .findViewById(R.id.txt_birthdate_display))
                                            .setText(EzApp.sdfMmyy.format(date));
                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                ((TextView) dialogPrintCard
                                        .findViewById(R.id.txt_pat_name))
                                        .setText(pat.getPfn() + " "
                                                + pat.getPln());

                                ((TextView) dialogPrintCard
                                        .findViewById(R.id.txt_gender_display))
                                        .setText(pat.getPgender());
                                ((TextView) dialogPrintCard
                                        .findViewById(R.id.txt_pat_contact))
                                        .setText(pat.getPmobnum());

                                // Get Patient Image/Photo {
                                Util.getImageFromUrl(
                                        APIs.VIEW() + pat.getPphoto(),
                                        DashboardActivity.imgLoader,
                                        (ImageView) dialogPrintCard
                                                .findViewById(R.id.img_pat));
                                // }

                                PatientController.patientBarcode(context,
                                        new OnResponseData() {

                                            @Override
                                            public void onResponseListner(
                                                    Object response) {

                                                Util.getImageFromUrl(
                                                        response.toString(),
                                                        DashboardActivity.imgLoader,
                                                        (ImageView) dialogPrintCard
                                                                .findViewById(R.id.img_barcode));

                                            }
                                        }, pat);

                                dialogPrintCard.findViewById(R.id.btn_print)
                                        .setOnClickListener(
                                                new OnClickListener() {

                                                    @Override
                                                    public void onClick(View v) {

                                                        PrintHelper photoPrinter = new PrintHelper(
                                                                context);

                                                        photoPrinter
                                                                .setScaleMode(PrintHelper.SCALE_MODE_FIT);
                                                        View view = (dialogPrintCard
                                                                .findViewById(R.id.rl_top));
                                                        view.setDrawingCacheEnabled(true);
                                                        view.buildDrawingCache();
                                                        Bitmap bm = view
                                                                .getDrawingCache();
                                                        // Bitmap bitmap =
                                                        // BitmapFactory.decodeResource(getResources(),
                                                        // R.drawable.rs_15);

                                                        photoPrinter
                                                                .printBitmap(
                                                                        "Card.jpg",
                                                                        bm);

                                                        // dialogPrintLabordered.dismiss();

                                                    }
                                                });
                                dialogPrintCard.show();

                            }
                        });

                if (pat.getUid_type().equalsIgnoreCase("Select Id")) {
                    ((TextView) imgdialog.findViewById(R.id.txt_type_display))
                            .setText("");
                } else {
                    ((TextView) imgdialog.findViewById(R.id.txt_type_display))
                            .setText(pat.getUid_type());
                }

                if (pat.getHaircolor().equalsIgnoreCase("Select Hair Color")) {
                    ((TextView) imgdialog
                            .findViewById(R.id.txt_hair_color_display))
                            .setText("");
                } else {
                    ((TextView) imgdialog
                            .findViewById(R.id.txt_hair_color_display))
                            .setText(pat.getHaircolor());
                }

                if (pat.getHeight().equalsIgnoreCase("Select Height")
                        || pat.getHeight().equalsIgnoreCase("SelectHeight")) {
                    ((TextView) imgdialog.findViewById(R.id.txt_height_display))
                            .setText("");
                } else {
                    ((TextView) imgdialog.findViewById(R.id.txt_height_display))
                            .setText(pat.getHeight());
                }

                if (pat.getEyecolor().equalsIgnoreCase("Select Eye Color")) {
                    ((TextView) imgdialog
                            .findViewById(R.id.txt_eye_color_display))
                            .setText("");
                } else {
                    ((TextView) imgdialog
                            .findViewById(R.id.txt_eye_color_display))
                            .setText(pat.getEyecolor());
                }

                imgdialog.show();
            }

        });
        mHolder.btnRefer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {
                final Dialog dialog = EzDialog.getDialog(context,
                        R.layout.dialog_refer, "Refer - Patient");
                dialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                ((TextView) dialog.findViewById(R.id.txt_patient)).setText(pat
                        .getPfn() + " " + pat.getPln());
                final AutoCompleteTextView actvDoctor = (AutoCompleteTextView) dialog
                        .findViewById(R.id.actv_doctor);
                final ArrayList<String> arrHospital = new ArrayList<String>();
                arrHospital.add("SELECT HOSPITAL");
                arrHospital.add("Appolo Hospital");
                arrHospital.add("Fortis Hospital");
                arrHospital.add("Agarsen Hospital");
                final ArrayList<DoctorModel> arrDoctor = new ArrayList<DoctorModel>();
                final DoctorsAutoCompleteAdapter adapterDoctor = new DoctorsAutoCompleteAdapter(
                        context, R.layout.support_simple_spinner_dropdown_item,
                        arrDoctor, EzApp.sharedPref, "");
                adapterDoctor
                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                final ArrayAdapter<String> adapterHospital = new ArrayAdapter<String>(
                        context, android.R.layout.simple_spinner_item,
                        arrHospital);
                adapterHospital
                        .setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                actvDoctor.setAdapter(adapterDoctor);
                actvDoctor.setThreshold(2);

                final DoctorModel selectedDoctor = new DoctorModel();
                actvDoctor.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(final AdapterView<?> arg0,
                                            final View arg1, final int arg2, final long arg3) {

                        selectedDoctor.name = arrDoctor.get(arg2).name;
                        selectedDoctor.id = arrDoctor.get(arg2).id;
                        selectedDoctor.speciality = arrDoctor.get(arg2).speciality;
                        actvDoctor.setText(selectedDoctor.name + ","
                                + selectedDoctor.speciality);

                    }
                });

                final RadioGroup rgRefer = (RadioGroup) dialog
                        .findViewById(R.id.rg_refer);

                rgRefer.check(1);
                rgRefer.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(final RadioGroup group,
                                                 final int checkedId) {
                        if (checkedId == group.getChildAt(0).getId()) {
                            actvDoctor.setAdapter(adapterDoctor);
                        }
                        // } else {
                        // spinnerDoctor.setAdapter(adapterHospital);
                        // }

                    }
                });
                final RadioButton rb = (RadioButton) dialog
                        .findViewById(R.id.rb_doc);
                rb.setChecked(true);
                final Button button = (Button) dialog
                        .findViewById(R.id.btn_submit);
                button.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(final View v) {

                        if (!Util
                                .isEmptyString(actvDoctor.getText().toString())) {
                            if (actvDoctor
                                    .getText()
                                    .toString()
                                    .equals(selectedDoctor.name + ","
                                            + selectedDoctor.speciality)) {
                                // referPatient(
                                // selectedDoctor.id,
                                // ((EditText) dialog
                                // .findViewById(R.id.edit_reason))
                                // .getText().toString(), pat,
                                // dialog);
                                patController
                                        .referPatient(
                                                selectedDoctor.id,
                                                ((EditText) dialog
                                                        .findViewById(R.id.edit_reason))
                                                        .getText().toString(),
                                                pat, dialog, context);
                            } else {
                                Util.Alertdialog(context,
                                        "Please select doctor");
                            }

                            try {
                                InputMethodManager imm = (InputMethodManager) context
                                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(dialog
                                        .findViewById(R.id.edit_reason)
                                        .getWindowToken(), 0);
                            } catch (Exception e) {

                            }
                        } else {
                            Util.Alertdialog(context, "Please select doctor");

                        }

                    }

                });
                dialog.setCancelable(false);
                dialog.show();

            }
        });
        mHolder.btnShedule.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {

                if (EzApp.sharedPref.getString(Constants.USER_TYPE, "")
                        .equalsIgnoreCase("D")) {
                    final Intent intent = new Intent(context,
                            SheduleActivity.class);
                    intent.putExtra("pid", pat.getPid());
                    intent.putExtra("fid", pat.getFid());
                    context.startActivity(intent);
                } else if (EzApp.sharedPref.getString(Constants.USER_TYPE, "")
                        .equalsIgnoreCase("LT")) {
                    final Intent intent = new Intent(context,
                            SheduleActivityNew.class);
                    intent.putExtra("pid", pat.getPid());
                    intent.putExtra("fid", pat.getFid());
                    intent.putExtra("type", "schedule");
                    context.startActivity(intent);
                }

            }
        });
        mHolder.btnUpdate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(context,
                        UpdatePatientActivity.class);
                intent.putExtra("pid", pat.getPid());
                intent.putExtra("fid", pat.getFid());
                context.startActivity(intent);
            }
        });

        mHolder.btnOrders.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Util.isEmptyString(pat.getPmn()))
                    onCallback.onCallback(pat.getPid(), pat.getPfn() + " "
                            + pat.getPln());
                else
                    onCallback.onCallback(pat.getPid(), pat.getPfn() + " "
                            + pat.getPmn() + " " + pat.getPln());
            }
        });

        mHolder.btnNewOrder.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                LabsAppointmentController.directAppointment(new OnResponse() {

                    @Override
                    public void onResponseListner(final String responsee) {
                        LabsAppointmentController.getAppointmentListApi(
                                Constants.TYPE_CONFIRMED, "1", "", context,
                                new OnResponseApt() {

                                    @Override
                                    public void onResponseListner(
                                            String response, String count) {
                                        if (!response.equals("error")) {
                                            Intent intent;

                                            intent = new Intent(
                                                    context,
                                                    LabsOrderCreateActivity.class);

                                            intent.putExtra("bkid", responsee);
                                            context.startActivity(intent);
                                        }

                                    }
                                }, null, null, null, 0, null);

                    }
                }, pat, context);

            }
        });

        mHolder.btnCheckin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                DoctorController.directCheckin(pat, context);

            }
        });

        mHolder.btnMedicalRecords.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Util.isEmptyString(pat.getPmn()))
                    onCallback.onCallback(pat.getPid(), pat.getPfn() + " "
                            + pat.getPln());
                else
                    onCallback.onCallback(pat.getPid(), pat.getPfn() + " "
                            + pat.getPmn() + " " + pat.getPln());

            }
        });
        return convertView;
    }
}

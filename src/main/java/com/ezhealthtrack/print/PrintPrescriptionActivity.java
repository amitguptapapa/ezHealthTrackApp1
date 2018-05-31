package com.ezhealthtrack.print;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.activity.EditAccountActivity;
import com.ezhealthtrack.controller.PatientController;
import com.ezhealthtrack.greendao.Patient;
import com.ezhealthtrack.labs.controller.OrderController.OnResponseData;
import com.ezhealthtrack.one.EzCommonViews;
import com.ezhealthtrack.physiciansoap.PhysicianSoapActivityMain;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Util;

public class PrintPrescriptionActivity extends EzPrint {

	Patient pat;
	private TextView txtDoctorName;
	private TextView txtDoctorAddress;
	private TextView txtPatientName;
	private TextView txtPatientAge;
	private TextView txtPatientGender;
	private TextView txtPatientAddress;
	private TextView txtDate;
	private TextView txtPrescription;
	private TextView txtSpecialInstructions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_print_prescription);

		txtDoctorName = (TextView) findViewById(R.id.txt_drname);
		txtDoctorAddress = (TextView) findViewById(R.id.txt_doc_address);
		txtPatientName = (TextView) findViewById(R.id.txt_patient_name);
		txtPatientAge = (TextView) findViewById(R.id.txt_patient_age);
		txtPatientGender = (TextView) findViewById(R.id.txt_patient_gender);
		txtPatientAddress = (TextView) findViewById(R.id.txt_ptaddress);
		txtDate = (TextView) findViewById(R.id.txt_date);
		txtPrescription = (TextView) findViewById(R.id.txt_prescription);
		txtSpecialInstructions = (TextView) findViewById(R.id.txt_special_notes);

		// Date formatting
		try {
			final SimpleDateFormat df = new SimpleDateFormat(
					" EEE, MMM dd, yyyy");
			final Date date;
			date = PhysicianSoapActivityMain.Appointment.aptDate;

			// Fetching Doctor Name & Address
			String drName = EzApp.sharedPref.getString(
					Constants.DR_NAME, "");
			String drAddress = EzApp.sharedPref.getString(
					Constants.DR_ADDRESS, "");

			// Setting Text
			txtDoctorName.setText(drName);
			txtDoctorAddress.setText(drAddress + " - "
					+ EditAccountActivity.profile.getZip()); // + getting doctor
			// zip
			// from account
			if (!Util.isEmptyString(PhysicianSoapActivityMain.patientModel
					.getPmn())) {
				txtPatientName.setText(Html.fromHtml("<b>Patient :</b> "
						+ PhysicianSoapActivityMain.patientModel.getPfn() + " "
						+ PhysicianSoapActivityMain.patientModel.getPmn() + " "
						+ PhysicianSoapActivityMain.patientModel.getPln()));
			} else {
				txtPatientName.setText(Html.fromHtml("<b>Patient :</b> "
						+ PhysicianSoapActivityMain.patientModel.getPfn() + " "
						+ PhysicianSoapActivityMain.patientModel.getPln()));
			}
			txtPatientAge.setText(Html.fromHtml("<b>Age :</b> "
					+ PhysicianSoapActivityMain.patientModel.getPage()));
			txtPatientGender.setText(Html.fromHtml("<b>Gender :</b> "
					+ PhysicianSoapActivityMain.patientModel.getPgender()));
			txtPatientAddress
					.setText(Html.fromHtml("<b>Address :</b> "
							+ PhysicianSoapActivityMain.patientModel.getPadd1()
							+ " "
							+ PhysicianSoapActivityMain.patientModel.getPadd2()
							+ ", "
							+ PhysicianSoapActivityMain.patientModel.getParea()
							+ ", "
							+ PhysicianSoapActivityMain.patientModel.getPcity()
							+ ", "
							+ PhysicianSoapActivityMain.patientModel
									.getPstate()
							+ ", "
							+ PhysicianSoapActivityMain.patientModel
									.getPcountry() + " - "
							+ PhysicianSoapActivityMain.patientModel.getPzip()));
			txtDate.setText(Html.fromHtml("<b>Date : </b>" + df.format(date)));

			txtPrescription.setText(Html.fromHtml("<b>Prescription :</b> "));

			txtSpecialInstructions
					.setText(Html
							.fromHtml("<b>Special Instructions :</b> "
									+ PhysicianSoapActivityMain.physicianVisitNotes.physicianPlanModel.prescription.si
											.get("si")));

			// Prescription Table
			EzCommonViews.prescriptionTable(this);
			// }

			PatientController.patientBarcode(PrintPrescriptionActivity.this,
					new OnResponseData() {

						@Override
						public void onResponseListner(Object response) {

							Util.getImageFromUrl(response.toString(),
									DashboardActivity.imgLoader,
									(ImageView) findViewById(R.id.img_barcode));
						}
					}, PhysicianSoapActivityMain.patientModel);

			if (!Util.isEmptyString(EzApp.sharedPref.getString(
					Constants.SIGNATURE, "signature"))) {
				Util.getImageFromUrl(EzApp.sharedPref.getString(
						Constants.SIGNATURE, ""), DashboardActivity.imgLoader,
						(ImageView) findViewById(R.id.img_signature));
			}
		} catch (Exception e) {

		}

	}

}

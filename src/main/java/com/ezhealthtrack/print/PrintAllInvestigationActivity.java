package com.ezhealthtrack.print;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map.Entry;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.activity.EditAccountActivity;
import com.ezhealthtrack.controller.PatientController;
import com.ezhealthtrack.labs.controller.OrderController.OnResponseData;
import com.ezhealthtrack.one.EzCommonViews;
import com.ezhealthtrack.physiciansoap.PhysicianSoapActivityMain;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Util;

public class PrintAllInvestigationActivity extends EzPrint {

	private TextView txtDoctorName;
	private TextView txtDoctorAddress;
	private TextView txtPatientName;
	private TextView txtPatientAge;
	private TextView txtPatientGender;
	private TextView txtPatientAddress;
	private TextView txtDate;
	private TextView txtPrescription;
	private TextView txtSpecialInstructions;
	private TextView txtLabOrdered;
	private TextView txtRadiologyOrdered;
	private TextView txtVitals;
	private TextView txtFinalDiagnosis;

	// private TextView txtFollowup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_print_all_investigations);

		txtDoctorName = (TextView) findViewById(R.id.txt_drname);
		txtDoctorAddress = (TextView) findViewById(R.id.txt_doc_address);
		txtPatientName = (TextView) findViewById(R.id.txt_patient_name);
		txtPatientAge = (TextView) findViewById(R.id.txt_patient_age);
		txtPatientGender = (TextView) findViewById(R.id.txt_patient_gender);
		txtPatientAddress = (TextView) findViewById(R.id.txt_ptaddress);
		txtDate = (TextView) findViewById(R.id.txt_date);
		txtPrescription = (TextView) findViewById(R.id.txt_prescription);
		txtSpecialInstructions = (TextView) findViewById(R.id.txt_special_instructions);
		txtLabOrdered = (TextView) findViewById(R.id.txt_lab_ordered);
		txtRadiologyOrdered = (TextView) findViewById(R.id.txt_radiology_orderd);
		txtVitals = (TextView) findViewById(R.id.txt_vitals);
		txtFinalDiagnosis = (TextView) findViewById(R.id.txt_final_diagnosis);
		// txtFollowup = (TextView) findViewById(R.id.txt_followup);

		// Date formatting
		try {
			final SimpleDateFormat df = new SimpleDateFormat(
					" EEE, MMM dd, yyyy");
			final Date date;
			date = PhysicianSoapActivityMain.Appointment.aptDate;

			// Fetching Doctor Name & Address
			String drName = EzApp.sharedPref.getString(Constants.DR_NAME, "");
			String drAddress = EzApp.sharedPref.getString(Constants.DR_ADDRESS,
					"");

			// Setting Text
			txtDoctorName.setText(drName);
			txtDoctorAddress.setText(drAddress + " - "
					+ EditAccountActivity.profile.getZip()); // + getting doctor
			// zip from account

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

			// Prescription
			txtPrescription.setText(Html.fromHtml("<b>Prescription :</b> "));
			txtSpecialInstructions
					.setText(Html
							.fromHtml("<b>Special Instructions :</b> "
									+ PhysicianSoapActivityMain.physicianVisitNotes.physicianPlanModel.prescription.si
											.get("si")));

			// Lab Ordered
			txtLabOrdered.setText(Html.fromHtml("<b>Lab Ordered : </b>"));
			for (Entry<String, String> entry : PhysicianSoapActivityMain.physicianVisitNotes.physicianPlanModel.lab.hashLab
					.entrySet()) {
				if (entry.getValue().equals("on")) {
					String[] result = entry.getKey().split("_");
					txtLabOrdered.append("\n"
							+ entry.getKey().replace(
									"_" + result[result.length - 1], ""));
				}
			}

			// Radiology Ordered
			txtRadiologyOrdered.setText(Html
					.fromHtml("<b>Radiology Ordered : </b>"));

			for (Entry<String, String> entry : PhysicianSoapActivityMain.physicianVisitNotes.physicianPlanModel.radiology.hashRadiology
					.entrySet()) {
				if (entry.getValue().equals("on")) {
					txtRadiologyOrdered.append("\n" + entry.getKey());
				}
			}

			// Final Diagnosis
			txtFinalDiagnosis
					.setText(Html
							.fromHtml("<b>Diagnosis:</b> "
									+ PhysicianSoapActivityMain.physicianVisitNotes.diagnosisModel.fd));

			// Vitals
			if (!Util
					.isEmptyString(PhysicianSoapActivityMain.physicianVisitNotes.examinationModel.hashVitals
							.get("low"))
					|| !Util.isEmptyString(PhysicianSoapActivityMain.physicianVisitNotes.examinationModel.hashVitals
							.get("high"))
					|| !Util.isEmptyString(PhysicianSoapActivityMain.physicianVisitNotes.examinationModel.hashVitals
							.get("temp"))
					|| !Util.isEmptyString(PhysicianSoapActivityMain.physicianVisitNotes.examinationModel.hashVitals
							.get("rr"))
					|| !Util.isEmptyString(PhysicianSoapActivityMain.physicianVisitNotes.examinationModel.hashVitals
							.get("low"))
					|| !Util.isEmptyString(PhysicianSoapActivityMain.physicianVisitNotes.examinationModel.hashVitals
							.get("puls"))
					|| !Util.isEmptyString(PhysicianSoapActivityMain.physicianVisitNotes.examinationModel.hashVitals
							.get("weig"))
					|| !Util.isEmptyString(PhysicianSoapActivityMain.physicianVisitNotes.examinationModel.hashVitals
							.get("heig"))
					|| !Util.isEmptyString(PhysicianSoapActivityMain.physicianVisitNotes.examinationModel.hashVitals
							.get("waist"))
					|| !Util.isEmptyString(PhysicianSoapActivityMain.physicianVisitNotes.examinationModel.hashVitals
							.get("bmi"))) {
				txtVitals.setText(Html.fromHtml("<b>Vitals : </b>"));
			} else {
				txtVitals.setVisibility(View.GONE);
			}

			if (!Util
					.isEmptyString(PhysicianSoapActivityMain.physicianVisitNotes.examinationModel.hashVitals
							.get("low"))
					|| !Util.isEmptyString(PhysicianSoapActivityMain.physicianVisitNotes.examinationModel.hashVitals
							.get("high"))) {
				txtVitals
						.append(Html.fromHtml("<b>Blood Pressure</b> = "
								+ PhysicianSoapActivityMain.physicianVisitNotes.examinationModel.hashVitals
										.get("high")
								+ "/"
								+ PhysicianSoapActivityMain.physicianVisitNotes.examinationModel.hashVitals
										.get("low") + " mm of Hg"));
			}
			if (!Util
					.isEmptyString(PhysicianSoapActivityMain.physicianVisitNotes.examinationModel.hashVitals
							.get("temp"))) {
				txtVitals
						.append(Html
								.fromHtml(", <b>Body Temperature</b> = "
										+ PhysicianSoapActivityMain.physicianVisitNotes.examinationModel.hashVitals
												.get("temp") + " &#176; F"));
			}
			if (!Util
					.isEmptyString(PhysicianSoapActivityMain.physicianVisitNotes.examinationModel.hashVitals
							.get("rr"))) {
				txtVitals
						.append(Html
								.fromHtml(", <b>Respiratory Rate</b> = "
										+ PhysicianSoapActivityMain.physicianVisitNotes.examinationModel.hashVitals
												.get("rr") + " breathes/minute"));
			}
			if (!Util
					.isEmptyString(PhysicianSoapActivityMain.physicianVisitNotes.examinationModel.hashVitals
							.get("puls"))) {
				txtVitals
						.append(Html
								.fromHtml(", <b>Pulse</b> = "
										+ PhysicianSoapActivityMain.physicianVisitNotes.examinationModel.hashVitals
												.get("puls") + " beats/minute"));
			}
			if (!Util
					.isEmptyString(PhysicianSoapActivityMain.physicianVisitNotes.examinationModel.hashVitals
							.get("weig"))) {
				txtVitals
						.append(Html
								.fromHtml(", <b>Weight</b> = "
										+ PhysicianSoapActivityMain.physicianVisitNotes.examinationModel.hashVitals
												.get("weig") + " kg"));
			}

			if (!Util
					.isEmptyString(PhysicianSoapActivityMain.physicianVisitNotes.examinationModel.hashVitals
							.get("heig"))) {
				txtVitals
						.append(Html
								.fromHtml(", <b>Height</b> = "
										+ PhysicianSoapActivityMain.physicianVisitNotes.examinationModel.hashVitals
												.get("heig") + " cm"));
			}

			if (!Util
					.isEmptyString(PhysicianSoapActivityMain.physicianVisitNotes.examinationModel.hashVitals
							.get("waist"))) {
				txtVitals
						.append(Html
								.fromHtml(", <b>Waist</b> = "
										+ PhysicianSoapActivityMain.physicianVisitNotes.examinationModel.hashVitals
												.get("waist") + " cm"));
			}

			if (!Util
					.isEmptyString(PhysicianSoapActivityMain.physicianVisitNotes.examinationModel.hashVitals
							.get("bmi"))) {
				txtVitals
						.append(Html
								.fromHtml(", <b>BMI</b> = "
										+ PhysicianSoapActivityMain.physicianVisitNotes.examinationModel.hashVitals
												.get("bmi")
										+ " kg/m<sup>2</sup>"));
			}

		} catch (Exception e) {

		}

		// Prescription Table
		EzCommonViews.prescriptionTable(this);
		// }

		PatientController.patientBarcode(PrintAllInvestigationActivity.this,
				new OnResponseData() {

					@Override
					public void onResponseListner(Object response) {

						Util.getImageFromUrl(response.toString(),
								DashboardActivity.imgLoader,
								(ImageView) findViewById(R.id.img_barcode));
					}
				}, PhysicianSoapActivityMain.patientModel);

		if (!Util.isEmptyString(EzApp.sharedPref.getString(Constants.SIGNATURE,
				"signature"))) {
			Util.getImageFromUrl(
					EzApp.sharedPref.getString(Constants.SIGNATURE, ""),
					DashboardActivity.imgLoader,
					(ImageView) findViewById(R.id.img_signature));
		}

	}

}

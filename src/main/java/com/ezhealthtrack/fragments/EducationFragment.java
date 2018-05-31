package com.ezhealthtrack.fragments;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract.Data;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.ezhealthtrack.activity.DashboardActivity;
import com.ezhealthtrack.activity.EditAccountActivity;
import com.ezhealthtrack.model.Certification;
import com.ezhealthtrack.model.Education;
import com.ezhealthtrack.model.VacationModel;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Log;
import com.ezhealthtrack.util.Util;

public class EducationFragment extends Fragment {
	private ArrayList<String> arrDegrees = new ArrayList<String>();
	private ArrayList<String> arrUniversity = new ArrayList<String>();
	private LinearLayout llEducation;
	private LinearLayout llCertification;
	private AutoCompleteTextView spinnerDegree;
	private AutoCompleteTextView spinnerUniversity;
	private EditText editPassed;
	private EditText editIntern;
	private LayoutInflater inflater;
	private EditText editCertification;
	private EditText editInstitute;
	private EditText editDateIssue;
	private EditText editDateExpire;
	private TextView txtDegree;
	private TextView txtUnivName;
	private TextView txtDatePassed;
	private TextView txtInternship;
	private TextView txtCertification;
	private TextView txtInstitute;
	private TextView txtDateIssued;
	private TextView txtDateExpiry;
	private CheckBox cbCme;
	private ArrayAdapter<String> adapterUniversity;

	private void getUniversityNames(final String s) {
		final String url = APIs.UNIVERSITY() + s;
		final StringRequest schedulePlanRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						// Log.i("", response);
						try {
							final JSONArray jArr = new JSONArray(response);
							for (int i = 0; i < jArr.length(); i++) {
								final JSONObject jObj = jArr.getJSONObject(i);
								if (!arrUniversity.contains(jObj.getString(
										"name").toLowerCase())) {
									adapterUniversity.add(jObj
											.getString("name").toLowerCase());
									arrUniversity.add(jObj.getString("name")
											.toLowerCase());
								}

							}
							Log.i("", arrUniversity.toString());
						} catch (final Exception e) {
							Log.e("", e);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						// Toast.makeText(
						// DashboardActivity.this,
						// "There is some error while fetching schedule plan please try again",
						// Toast.LENGTH_SHORT).show();
						//
						// Log.e("Error.Response", error);
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				// loginParams.put("auth-token", Util.getBase64String(sharedPref
				// .getString(Constants.USER_TOKEN, "")));
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("format", "json");
				return loginParams;
			}

		};
		schedulePlanRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(schedulePlanRequest);

	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		try {
			arrDegrees.clear();
			// JSONArray degrees = (new JSONObject(Util.readJsonFromAssets(
			// "Degrees.txt", getActivity()))).getJSONArray("option");
			// for (int i = 0; i < degrees.length(); i++) {
			// arrDegrees.add(degrees.getJSONObject(i).getString("name"));
			// }
			arrDegrees.add("Anaes. & Critical Care Med.");
			arrDegrees.add("D.P.B (Pathology) ");
			arrDegrees.add("DGO -PREVENTIRE & SOCIAL MEDICINE ");
			arrDegrees.add("DGO ");
			arrDegrees.add("Dip. in Path.& Bact. ");
			arrDegrees.add("Diplom -Diploma V & D ");
			arrDegrees.add("Diploma - Aviation Medicine ");
			arrDegrees.add("Diploma - Diploma - OLO - Rhino-Laryngology ");
			arrDegrees.add("Diploma - Diploma in Medical Radio-Diagnosis ");
			arrDegrees.add("Diploma - Diploma in Pathology & Bacteriology ");
			arrDegrees.add("Diploma (Marine Medicine) ");
			arrDegrees.add("Diploma in Allergy & Clinical Immunology ");
			arrDegrees.add("Diploma in Anesthesia ");
			arrDegrees.add("Diploma in Bacteriology ");
			arrDegrees.add("Diploma in Basic Medical Sciences (Anatomy) ");
			arrDegrees.add("Diploma in Basic Medical Sciences (Pharmacology) ");
			arrDegrees.add("Diploma in Basic Medical Sciences (Physiology) ");
			arrDegrees.add("Diploma in Cardiology ");
			arrDegrees.add("Diploma in Child Health ");
			arrDegrees.add("Diploma in Clinical Pathology ");
			arrDegrees.add("Diploma in Community Medicine ");
			arrDegrees.add("Diploma in Dermatology ");
			arrDegrees.add("Diploma in Dermatology, Venereology and Leprosy ");
			arrDegrees.add("Diploma in Diabetology ");
			arrDegrees.add("Diploma in ENT ");
			arrDegrees.add("Diploma in Forensic Medicine ");
			arrDegrees.add("Diploma in Health Administration ");
			arrDegrees.add("Diploma in Health Education ");
			arrDegrees.add("Diploma in Hospital Administration ");
			arrDegrees
					.add("Diploma in Immuno-Haematology and Blood Transfusion ");
			arrDegrees.add("Diploma in Industrial Health ");
			arrDegrees.add("Diploma in Industrial Hygiene ");
			arrDegrees.add("Diploma in Leprosy ");
			arrDegrees.add("Diploma in Maternity & Child Welfare ");
			arrDegrees.add("Diploma in Medical Radio Electrology ");
			arrDegrees.add("Diploma in Medical Radioligy & Electrology ");
			arrDegrees.add("Diploma in Medical Virology ");
			arrDegrees.add("Diploma in Microbiology ");
			arrDegrees.add("Diploma in Neuro-pathology ");
			arrDegrees.add("Diploma in Nutrition ");
			arrDegrees.add("Diploma in Obstetrics & Gynaecology ");
			arrDegrees.add("Diploma in Occupational Health ");
			arrDegrees.add("Diploma in Ophthalmology ");
			arrDegrees.add("Diploma in Orthopaedics ");
			arrDegrees.add("Diploma in Oto-Rhino-Laryngology ");
			arrDegrees.add("Diploma in Paediatrics ");
			arrDegrees.add("Diploma in Physical Medicine & Rehabilitation ");
			arrDegrees.add("Diploma in Psychiatry ");
			arrDegrees.add("Diploma in Psychological Medicine ");
			arrDegrees.add("Diploma in Public Health ");
			arrDegrees.add("Diploma in Radiation Medicine ");
			arrDegrees.add("Diploma in Radio Therapy ");
			arrDegrees.add("Diploma in Radio-Diagnosis ");
			arrDegrees.add("Diploma in Radiological Physics ");
			arrDegrees.add("Diploma in Sports Medicine ");
			arrDegrees.add("Diploma in Tropical Medicine Health ");
			arrDegrees.add("Diploma in Tuberculosis & Chest Diseases ");
			arrDegrees.add("Diploma in Venereology ");
			arrDegrees.add("Diploma- Plastic Surgery ");
			arrDegrees.add("Diploma- Urology ");
			arrDegrees.add("Diploma-Diplomate N.B.(Gen.Surg.) ");
			arrDegrees.add("DM - Cardiac-Anaes. ");
			arrDegrees.add("DM - Cardiology ");
			arrDegrees.add("DM - Child & Adolescent Psychiatry ");
			arrDegrees.add("DM - Clinical Haematology ");
			arrDegrees.add("DM - Clinical Immunology ");
			arrDegrees.add("DM - Clinical Pharmacology ");
			arrDegrees.add("DM - Critical Care Medicine ");
			arrDegrees.add("DM - Endocrinology ");
			arrDegrees.add("DM - Gastroenterology ");
			arrDegrees.add("DM - Geriatric Mental Health ");
			arrDegrees.add("DM - Haematology Pathology/Hematopthology ");
			arrDegrees.add("DM - Hepatology ");
			arrDegrees.add("DM - Immunology ");
			arrDegrees.add("DM - Infectious Disease ");
			arrDegrees.add("DM - Infectious Diseases ");
			arrDegrees.add("DM - Medical Genetics ");
			arrDegrees.add("DM - Neonatology ");
			arrDegrees.add("DM - Nephrology ");
			arrDegrees.add("DM - Neuro Anaesthesia ");
			arrDegrees.add("DM - Neuro Radiology ");
			arrDegrees.add("DM - Neurology ");
			arrDegrees.add("DM - Oncology ");
			arrDegrees
					.add("DM - Organ Transplant Anaesthesia & Critical Care ");
			arrDegrees.add("DM - Paediatric Anaesthesia ");
			arrDegrees.add("DM - Paediatric Hepatology ");
			arrDegrees.add("DM - Paediatric Nephrology ");
			arrDegrees.add("DM - Paediatric Oncology ");
			arrDegrees.add("DM - Pediatrics Cardiology ");
			arrDegrees.add("DM - Pediatrics Gastroenterology ");
			arrDegrees.add("DM - Pul. Med. & Critical Care Med. ");
			arrDegrees.add("DM - Pulmonary Medicine ");
			arrDegrees.add("DM - Reproductive Medicine ");
			arrDegrees.add("DM - Rheumatology ");
			arrDegrees.add("DM - Virology ");
			arrDegrees.add("Doctor of Medicine ");
			arrDegrees.add("F.C.P.S.(Dermatology, Venereology & Leprosy) ");
			arrDegrees.add("F.C.P.S.(Medicine) ");
			arrDegrees.add("F.C.P.S.(Mid. & Gynae) ");
			arrDegrees.add("F.C.P.S.(Ophthalmology) ");
			arrDegrees.add("F.C.P.S.(Pathology) ");
			arrDegrees.add("F.C.P.S.(Surgery) ");
			arrDegrees.add("FMT ");
			arrDegrees.add("M C P S ");
			arrDegrees.add("M.B.B.S. ");
			arrDegrees.add("M.Ch - Cardio Thoracic and Vascular Surgery ");
			arrDegrees.add("M.Ch - Cardio Thoracic Surgery ");
			arrDegrees.add("M.Ch - Endocrine Surgery ");
			arrDegrees.add("M.Ch - Gynaecological Oncology ");
			arrDegrees.add("M.Ch - Hepato Pancreato Biliary Surgery ");
			arrDegrees.add("M.Ch - Neuro Surgery ");
			arrDegrees.add("M.Ch - Oncology ");
			arrDegrees.add("M.Ch - Paediatric Surgery ");
			arrDegrees
					.add("M.Ch - Pediatric Cardio-Thoracic Vascular Surgery ");
			arrDegrees.add("M.Ch - Plastic & Reconstructive Surgery ");
			arrDegrees.add("M.Ch - Plastic Surgery ");
			arrDegrees.add("M.Ch - Surgical Gastroenterology/G.I. Surgery ");
			arrDegrees.add("M.Ch - Surgical Oncology ");
			arrDegrees.add("M.Ch - Thoracic Surgery ");
			arrDegrees.add("M.Ch - Urology ");
			arrDegrees.add("M.Ch - Urology/Genito-Urinary Surgery ");
			arrDegrees.add("M.Ch - Vascular Surgery ");
			arrDegrees.add("M.Ch. - Hand & Micro Surgery ");
			arrDegrees.add("M.Ch. - Hand Surgery ");
			arrDegrees.add("M.Ch.(Burns & Plastic Surgery) ");
			arrDegrees.add("M.D. ");
			arrDegrees.add("M.Sc - Anatomy ");
			arrDegrees.add("M.Sc - Bio-Physics ");
			arrDegrees.add("M.Sc - Medical Anatomy ");
			arrDegrees.add("M.Sc - Medical Bacteriology ");
			arrDegrees.add("M.Sc - Medical Bio-chemistry ");
			arrDegrees.add("M.Sc - Medical Pathology ");
			arrDegrees.add("M.Sc - Medical Pharmocology ");
			arrDegrees.add("M.Sc - Microbiology ");
			arrDegrees.add("M.Sc - Pathology ");
			arrDegrees.add("M.Sc - Physiology ");
			arrDegrees.add("M.Sc. - M.Sc. - Statistics ");
			arrDegrees.add("Master of Family Medicine ");
			arrDegrees.add("Master of Hospital Administration ");
			arrDegrees.add("Master of Public Health (Epidemiology) ");
			arrDegrees.add("Master's of Physician ");
			arrDegrees.add("MD - Anaesthesiology ");
			arrDegrees.add("MD - Anatomy ");
			arrDegrees.add("MD - Aviation Medicine ");
			arrDegrees.add("MD - Aviation Medicine/Aerospace Medicine ");
			arrDegrees.add("MD - Bio-Chemistry ");
			arrDegrees.add("MD - Bio-Physics ");
			arrDegrees
					.add("MD - Blood Banking & Immuno. Haem./Imm. Haem. & Blood Trans. ");
			arrDegrees.add("MD - CCM ");
			arrDegrees.add("MD - Community Health Administration ");
			arrDegrees.add("MD - Community Medicine ");
			arrDegrees.add("MD - Dermatology , Venereology & Leprosy ");
			arrDegrees.add("MD - Dermatology ");
			arrDegrees.add("MD - Emergency Medicine ");
			arrDegrees.add("MD - Family Medicine ");
			arrDegrees
					.add("MD - Forensic Medicine/Forensic Medicine & Toxicology ");
			arrDegrees.add("MD - General Medicine ");
			arrDegrees.add("MD - Geriatrics ");
			arrDegrees.add("MD - Health Administration ");
			arrDegrees.add("MD - Hospital Administration ");
			arrDegrees.add("MD - Immuno Haematology & Blood Transfusion ");
			arrDegrees.add("MD - Lab Medicine ");
			arrDegrees.add("MD - Maternity & Child Health ");
			arrDegrees.add("MD - MD- Skin & VD ");
			arrDegrees.add("MD - Medical Genetics ");
			arrDegrees.add("MD - Medicine ");
			arrDegrees.add("MD - Microbiology ");
			arrDegrees.add("MD - Nuclear Medicine ");
			arrDegrees.add("MD - Obstetrtics & Gynaecology ");
			arrDegrees.add("MD - Ophthalmology ");
			arrDegrees.add("MD - P.S.M ");
			arrDegrees.add("MD - Paediatrics ");
			arrDegrees.add("MD - Palliative Medicine ");
			arrDegrees.add("MD - Pathology & Microbiology ");
			arrDegrees.add("MD - Pathology ");
			arrDegrees.add("MD - Pharmacology and Therapeutics ");
			arrDegrees.add("MD - Pharmacology ");
			arrDegrees.add("MD - Physical Medicine & Rehabilitation ");
			arrDegrees.add("MD - Physiology ");
			arrDegrees.add("MD - Psychiatry ");
			arrDegrees.add("MD - Pulmonary Medicine ");
			arrDegrees.add("MD - R & D ");
			arrDegrees.add("MD - Radio Diagnosis ");
			arrDegrees.add("MD - Radio Diagnosis/Radiology ");
			arrDegrees.add("MD - Radiology ");
			arrDegrees.add("MD - Radiotherapy ");
			arrDegrees.add("MD - Rheumatology ");
			arrDegrees.add("MD - Skin & VD & Lepxsy ");
			arrDegrees
					.add("MD - Social & Preventive Medicine / Community Medicine ");
			arrDegrees.add("MD - Sports Medicine ");
			arrDegrees.add("MD - TB & Chest ");
			arrDegrees.add("MD - Thoracic Medicine ");
			arrDegrees.add("MD - Tropical Medicine ");
			arrDegrees
					.add("MD - Tuberculosis & Respiratory Diseases / Pulmonary Medicine ");
			arrDegrees
					.add("MD - Tuberculosis & Respiratory Diseases/Medicine ");
			arrDegrees.add("MD - Venereology ");
			arrDegrees.add("MD/MS - Anatomy ");
			arrDegrees.add("MD/MS - Obstetrtics & Gynaecology ");
			arrDegrees.add("MD/MS - Ophthalmology ");
			arrDegrees.add("MD-Transfusion Medicine ");
			arrDegrees.add("MS - Anaesthesia ");
			arrDegrees.add("MS - Anatomy ");
			arrDegrees.add("MS - ENT ");
			arrDegrees.add("MS - General Surgery ");
			arrDegrees.add("MS - Neuro Surgery ");
			arrDegrees.add("MS - Obstetrics and Gynaechology ");
			arrDegrees.add("MS - Ophthalmology ");
			arrDegrees.add("MS - Orthopaedics ");
			arrDegrees.add("MS - Traumatology and Surgery ");
			arrDegrees.add("MS. - MS. Medicine ");
			arrDegrees.add("Ph. D - Anaesthesia ");
			arrDegrees.add("Ph. D - Anatomy ");
			arrDegrees.add("Ph. D - Bio- Chemistry ");
			arrDegrees.add("Ph. D - Bio-Statistics ");
			arrDegrees.add("Ph. D - Bio-Technology ");
			arrDegrees.add("Ph. D - Cardio Thoracic & Vascular Surgery ");
			arrDegrees.add("Ph. D - Cardiology ");
			arrDegrees.add("Ph. D - Community Medicine ");
			arrDegrees.add("Ph. D - Dermatology & Venereology ");
			arrDegrees.add("Ph. D - Doctor of Phylosophy ");
			arrDegrees.add("Ph. D - Endocrinology & Metabolism ");
			arrDegrees.add("Ph. D - ENT ");
			arrDegrees.add("Ph. D - Forensic Medicine ");
			arrDegrees.add("Ph. D - Gastro & Human Nutrition Unit ");
			arrDegrees.add("Ph. D - Gastrointestinal Surgery ");
			arrDegrees.add("Ph. D - Haematology ");
			arrDegrees.add("Ph. D - Histo Compatibility & Immunogenetics ");
			arrDegrees.add("Ph. D - Hospital Administration ");
			arrDegrees.add("Ph. D - Lab Medicine ");
			arrDegrees.add("Ph. D - Medical Biochemistry ");
			arrDegrees.add("Ph. D - Medical Oncology ");
			arrDegrees.add("Ph. D - Medical Physics ");
			arrDegrees.add("Ph. D - Medicine ");
			arrDegrees.add("Ph. D - Microbiology ");
			arrDegrees.add("Ph. D - Nephrology ");
			arrDegrees.add("Ph. D - Neuro Magnetic Resonance ");
			arrDegrees.add("Ph. D - Neuro Surgery ");
			arrDegrees.add("Ph. D - Neurology ");
			arrDegrees.add("Ph. D - Nuclear Medicine ");
			arrDegrees.add("Ph. D - Obst. & Gynae ");
			arrDegrees.add("Ph. D - Ocular Bio Chemistry ");
			arrDegrees.add("Ph. D - Ocular Microbiology ");
			arrDegrees.add("Ph. D - Ocular Phramacology ");
			arrDegrees.add("Ph. D - Orthopaedics ");
			arrDegrees.add("Ph. D - Paediatric Surgery ");
			arrDegrees.add("Ph. D - Paediatric ");
			arrDegrees.add("Ph. D - Pathology ");
			arrDegrees.add("Ph. D - Physical Medicine & Rehabilitation ");
			arrDegrees.add("Ph. D - Physiology ");
			arrDegrees.add("Ph. D - Psychiatry ");
			arrDegrees.add("Ph. D - Radio Diagnosis ");
			arrDegrees.add("Ph. D - Radiotherapy ");
			arrDegrees.add("Ph. D - Surgery ");
			arrDegrees.add("Ph. D - Urology ");
			arrDegrees.add("Ph. D-Pharmacology ");
			arrDegrees.add("PSM ");
			arrDegrees.add("BDS");
			arrDegrees.add("Diploma-Dental Hygienists");
			arrDegrees.add("Diploma-Dental Mechanics");
			arrDegrees.add("Diploma-Dental Operating Room Assistant");
			arrDegrees.add("MDS-Conservative Dentistry");
			arrDegrees.add("MDS-Oral Medicine");
			arrDegrees.add("MDS-Oral Pathology");
			arrDegrees.add("MDS-Oral Surgery");
			arrDegrees.add("MDS-Orthodonitics");
			arrDegrees.add("MDS-Pedodontics");
			arrDegrees.add("MDS-Periodontics");
			arrDegrees.add("MDS-Prosthodontics");
			arrDegrees.add("MDS-Public Health Dentistry");
			arrDegrees.add("PG Diploma-Conservative Dentistry");
			arrDegrees.add("PG Diploma-Oral Surgery");
			arrDegrees.add("PG Diploma-Orthodontics");
			arrDegrees.add("PG Diploma-Pedodontics");
			arrDegrees.add("PG Diploma-Periodontics");
			arrDegrees.add("PG Diploma-Prosthodontics");

			arrUniversity.clear();
			JSONArray university = (new JSONObject(Util.readJsonFromAssets(
					"University.txt", getActivity()))).getJSONArray("option");
			for (int i = 0; i < university.length(); i++) {
				arrUniversity
						.add(university.getJSONObject(i).getString("name"));
			}

			llEducation = (LinearLayout) getActivity().findViewById(
					R.id.ll_education);
			llCertification = (LinearLayout) getActivity().findViewById(
					R.id.ll_certification);
			inflater = (LayoutInflater) getActivity().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);

			for (final Education education : DashboardActivity.profile
					.getEducation()) {
				addEducationRow(education);

			}

			for (final Certification certification : DashboardActivity.profile
					.getCertifications()) {
				addCertificationRow(certification);

			}

			spinnerDegree = (AutoCompleteTextView) getActivity().findViewById(
					R.id.spinner_degree);
			final ArrayAdapter<String> adapterDegree = new ArrayAdapter<String>(
					getActivity(), android.R.layout.simple_spinner_item,
					arrDegrees);
			adapterDegree
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerDegree.setAdapter(adapterDegree);

			spinnerUniversity = (AutoCompleteTextView) getActivity()
					.findViewById(R.id.spinner_university);
			adapterUniversity = new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_spinner_item, arrUniversity);
			adapterUniversity
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerUniversity.setAdapter(adapterUniversity);

			editIntern = (EditText) getActivity().findViewById(
					R.id.edit_internship);
			editPassed = (EditText) getActivity().findViewById(
					R.id.edit_date_passed);

			getActivity().findViewById(R.id.btn_add_education)
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							if (!Util.isEmptyString(spinnerDegree.getText()
									.toString())) {
								if (!Util.isEmptyString(spinnerUniversity
										.getText().toString())) {
									if (!Util.isEmptyString(editPassed
											.getText().toString())) {
										if (!Util.isEmptyString(editIntern
												.getText().toString())) {
											educationValidation(true);
										} else {
											Util.Alertdialog(getActivity(),
													"Please enter Internship/Residency");
										}
									} else {
										Util.Alertdialog(getActivity(),
												"Please enter Passed Date");
									}
								} else {
									Util.Alertdialog(getActivity(),
											"Please select University");
								}
							} else {
								Util.Alertdialog(getActivity(),
										"Please select Degree");
							}

						}
					});
			spinnerUniversity.addTextChangedListener(new TextWatcher() {

				@Override
				public void afterTextChanged(final Editable s) {

				}

				@Override
				public void beforeTextChanged(final CharSequence s,
						final int start, final int count, final int after) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onTextChanged(final CharSequence s,
						final int start, final int before, final int count) {
					if (s.length() > 1)
						getUniversityNames(s.toString());

				}
			});

			Util.datePicker(editPassed, getActivity());

			editCertification = (EditText) getActivity().findViewById(
					R.id.edit_certification);
			editInstitute = (EditText) getActivity().findViewById(
					R.id.edit_institute);
			editDateIssue = (EditText) getActivity().findViewById(
					R.id.edit_date_issued_edu);
			editDateExpire = (EditText) getActivity().findViewById(
					R.id.edit_date_expiry_edu);
			txtDegree = (TextView) getActivity().findViewById(R.id.txt_degree);
			((TextView) getActivity().findViewById(R.id.txt_degree))
					.append(Html
							.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));
			txtUnivName = (TextView) getActivity().findViewById(
					R.id.txt_university);
			((TextView) getActivity().findViewById(R.id.txt_university))
					.append(Html
							.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));
			txtDatePassed = (TextView) getActivity().findViewById(
					R.id.txt_date_passed);
			((TextView) getActivity().findViewById(R.id.txt_date_passed))
					.append(Html
							.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));
			txtInternship = (TextView) getActivity().findViewById(
					R.id.txt_internship);
			((TextView) getActivity().findViewById(R.id.txt_internship))
					.append(Html
							.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));
			txtCertification = (TextView) getActivity().findViewById(
					R.id.txt_certification_1);
			((TextView) getActivity().findViewById(R.id.txt_certification_1))
					.append(Html
							.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));
			txtInstitute = (TextView) getActivity().findViewById(
					R.id.txt_institute);
			((TextView) getActivity().findViewById(R.id.txt_institute))
					.append(Html
							.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));
			txtDateIssued = (TextView) getActivity().findViewById(
					R.id.txt_date_issued_edu);
			((TextView) getActivity().findViewById(R.id.txt_date_issued_edu))
					.append(Html
							.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));
			txtDateExpiry = (TextView) getActivity().findViewById(
					R.id.txt_date_expiry_edu);
			((TextView) getActivity().findViewById(R.id.txt_date_expiry_edu))
					.append(Html
							.fromHtml("<sup><font color='#EE0000'>*</font></sup>"));
			cbCme = (CheckBox) getActivity().findViewById(R.id.cb_cme);
			Util.datePicker(editDateExpire, getActivity());
			Util.datePicker(editDateIssue, getActivity());

			getActivity().findViewById(R.id.btn_add_certification)
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							if (!Util.isEmptyString(editCertification.getText()
									.toString())) {
								if (!Util.isEmptyString(editInstitute.getText()
										.toString())) {
									if (!Util.isEmptyString(editDateIssue
											.getText().toString())) {
										if (!Util.isEmptyString(editDateExpire
												.getText().toString())) {
											certificationValidation(true);
										} else {
											Util.Alertdialog(getActivity(),
													"Please enter Expiry Date");
										}
									} else {
										Util.Alertdialog(getActivity(),
												"Please enter Issued Date");
									}
								} else {
									Util.Alertdialog(getActivity(),
											"Please enter Institute Name");
								}
							} else {
								Util.Alertdialog(getActivity(),
										"Please enter Certification");
							}

						}
					});

			getActivity().findViewById(R.id.button).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							Util.Alertdialog(getActivity(),
									"Profile Updated successfully");

						}
					});

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void onClick(final View v) {
		switch (v.getId()) {

		}

	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_education, container, false);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	private void showCalender(final TextView txtView) {
		Calendar cal = Calendar.getInstance();
		DatePickerDialog datepicker = new DatePickerDialog(getActivity(),
				new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						if (dayOfMonth < 10 && monthOfYear < 9) {
							txtView.setText("0" + ++monthOfYear + "/0"
									+ dayOfMonth + "/" + year);
						} else if (dayOfMonth < 10 && monthOfYear > 8) {
							txtView.setText(++monthOfYear + "/0" + dayOfMonth
									+ "/" + year);
						} else if (dayOfMonth > 9 && monthOfYear < 9) {
							txtView.setText("0" + ++monthOfYear + "/"
									+ dayOfMonth + "/" + year);
						} else {
							txtView.setText(++monthOfYear + "/" + dayOfMonth
									+ "/" + year);
						}

					}
				}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH));
		datepicker.show();
	}

	private void deleteEducation(final Education education, final View v) {
		final String url = APIs.EDUCATION_ADD_DELETE();
		final Dialog loaddialog = Util.showLoadDialog(getActivity());
		final StringRequest schedulePlanRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i("", response);
						JSONObject jObj;
						try {
							jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								DashboardActivity.profile.getEducation()
										.remove(education);
								llEducation.removeView(v);
							} else {
								Util.Alertdialog(getActivity(),
										jObj.getString("m"));
							}
						} catch (Exception e) {
							Util.Alertdialog(getActivity(), e.toString());
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						// Util.Alertdialog(getActivity(), error.toString());
						loaddialog.dismiss();
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util
						.getBase64String(EzApp.sharedPref
								.getString(Constants.USER_TOKEN, "")));
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("eid", education.getEid());
				loginParams.put("type", "remove");
				return loginParams;
			}

		};
		schedulePlanRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(schedulePlanRequest);
	}

	private void addEducation() {
		final String url = APIs.EDUCATION_ADD_DELETE();
		final Dialog loaddialog = Util.showLoadDialog(getActivity());
		final StringRequest schedulePlanRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i("", response);
						JSONObject jObj;
						try {
							jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								Education education = new Education();
								education.setDatepass(editPassed.getText()
										.toString());
								education.setDegree(spinnerDegree.getText()
										.toString());
								education.setEid(jObj.getString("eid"));
								education.setIntern(editIntern.getText()
										.toString());
								education.setUniversity(spinnerUniversity
										.getText().toString());
								DashboardActivity.profile.getEducation().add(
										education);
								addEducationRow(education);
								editIntern.setText("");
								editPassed.setText("");
								spinnerDegree.setText("");
								spinnerUniversity.setText("");
							} else {
								Util.Alertdialog(getActivity(),
										jObj.getString("m"));
							}
						} catch (Exception e) {
							Util.Alertdialog(getActivity(), e.toString());
						}
						loaddialog.dismiss();

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						Util.Alertdialog(getActivity(), error.toString());
						loaddialog.dismiss();
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util
						.getBase64String(EzApp.sharedPref
								.getString(Constants.USER_TOKEN, "")));
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("type", "add");
				loginParams.put("degree", spinnerDegree.getText().toString());
				loginParams.put("university", spinnerUniversity.getText()
						.toString());
				loginParams.put("datepass", editPassed.getText().toString());
				loginParams.put("intern", editIntern.getText().toString());

				return loginParams;
			}

		};
		schedulePlanRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(schedulePlanRequest);
	}

	private void addEducationRow(final Education education) {
		final View v = inflater.inflate(R.layout.row_education, null);
		llEducation.addView(v);
		((TextView) v.findViewById(R.id.txt_degree)).setText(education
				.getDegree());
		((TextView) v.findViewById(R.id.txt_university)).setText(education
				.getUniversity());
		try {
			((TextView) v.findViewById(R.id.txt_date_passed))
					.setText(EzApp.sdfMmddyy
							.format(EzApp.sdfyyMmdd
									.parse(education.getDatepass())));
		} catch (ParseException e) {
			((TextView) v.findViewById(R.id.txt_date_passed)).setText(education
					.getDatepass());
		}
		((TextView) v.findViewById(R.id.txt_internship)).setText(education
				.getIntern());
		v.findViewById(R.id.btn_del_education).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View a) {
						deleteEducation(education, v);

					}
				});
	}

	private void addCertificationRow(final Certification certification) {
		try {
			final View v = inflater.inflate(R.layout.row_certification, null);
			llCertification.addView(v);
			((TextView) v.findViewById(R.id.txt_certification))
					.setText(certification.getCertification());
			((TextView) v.findViewById(R.id.txt_institute))
					.setText(certification.getInstitute());

			try {
				((TextView) v.findViewById(R.id.txt_date_issued))
						.setText(EzApp.sdfMmddyy
								.format(EzApp.sdfyyMmdd
										.parse(certification.getDatei())));
			} catch (ParseException e) {
				((TextView) v.findViewById(R.id.txt_date_issued))
						.setText(certification.getDatei());
			}

			try {
				((TextView) v.findViewById(R.id.txt_date_expiry))
						.setText(EzApp.sdfMmddyy
								.format(EzApp.sdfyyMmdd
										.parse(certification.getDatee())));
			} catch (ParseException e) {
				((TextView) v.findViewById(R.id.txt_date_expiry))
						.setText(certification.getDatee());
			}
			((CheckBox) v.findViewById(R.id.cb_cme)).setChecked(certification
					.getCme().equalsIgnoreCase("true"));
			((CheckBox) v.findViewById(R.id.cb_cme)).setEnabled(false);
			v.findViewById(R.id.btn_rem_certification).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View a) {
							deleteCertification(certification, v);

						}
					});
		} catch (Exception e) {
			Log.e("", e.toString());
		}
	}

	private void deleteCertification(final Certification certification,
			final View v) {
		final String url = APIs.CERTIFICATION_ADD_DELETE();
		final Dialog loaddialog = Util.showLoadDialog(getActivity());
		final StringRequest schedulePlanRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i("", response);
						JSONObject jObj;
						try {
							jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								DashboardActivity.profile.getCertifications()
										.remove(certification);
								llCertification.removeView(v);
							} else {
								// Util.Alertdialog(getActivity(),
								// jObj.getString("m"));
							}
						} catch (Exception e) {
							Util.Alertdialog(getActivity(), e.toString());
						}
						loaddialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						// Util.Alertdialog(getActivity(), error.toString());
						loaddialog.dismiss();
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util
						.getBase64String(EzApp.sharedPref
								.getString(Constants.USER_TOKEN, "")));
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("cid", certification.getCid());
				loginParams.put("type", "remove");
				return loginParams;
			}

		};
		schedulePlanRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(schedulePlanRequest);
	}

	private void addCertification() {
		final String url = APIs.CERTIFICATION_ADD_DELETE();
		final Dialog loaddialog = Util.showLoadDialog(getActivity());
		final StringRequest schedulePlanRequest = new StringRequest(
				Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						Log.i("", response);
						JSONObject jObj;
						try {
							jObj = new JSONObject(response);
							if (jObj.getString("s").equals("200")) {
								Certification certification = new Certification();
								certification
										.setCertification(editCertification
												.getText().toString());
								certification.setCid(jObj.getString("cid"));
								certification.setCme("" + cbCme.isChecked());
								certification.setDatee(editDateExpire.getText()
										.toString());
								certification.setDatei(editDateIssue.getText()
										.toString());
								certification.setInstitute(editInstitute
										.getText().toString());
								DashboardActivity.profile.getCertifications()
										.add(certification);
								addCertificationRow(certification);
								editCertification.setText("");
								editInstitute.setText("");
								editDateExpire.setText("");
								editDateIssue.setText("");
								cbCme.setChecked(false);

							} else {
								// Util.Alertdialog(getActivity(),
								// jObj.getString("m"));
							}
						} catch (Exception e) {
							Util.Alertdialog(getActivity(), e.toString());
						}
						loaddialog.dismiss();

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						// Util.Alertdialog(getActivity(), error.toString());
						loaddialog.dismiss();
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				loginParams.put("auth-token", Util
						.getBase64String(EzApp.sharedPref
								.getString(Constants.USER_TOKEN, "")));
				return loginParams;
			}

			@Override
			protected Map<String, String> getParams() {
				final HashMap<String, String> loginParams = new HashMap<String, String>();
				// certification, institute, datei (date issued) , datee (date
				// expired) , cme
				loginParams.put("type", "add");
				loginParams.put("certification", editCertification.getText()
						.toString());
				loginParams
						.put("institute", editInstitute.getText().toString());
				loginParams.put("datei", editDateIssue.getText().toString());
				loginParams.put("datee", editDateExpire.getText().toString());
				if (cbCme.isChecked())
					loginParams.put("cme", "1");
				else
					loginParams.put("cme", "0");

				return loginParams;
			}

		};
		schedulePlanRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		EzApp.mVolleyQueue.add(schedulePlanRequest);
	}

	private void certificationValidation(boolean check) {
		Date date = new Date();
		Date issueDate = null;
		Date expiryDate = null;

		try {
			issueDate = EzApp.sdfMmddyy.parse(editDateIssue
					.getText().toString());
			expiryDate = EzApp.sdfMmddyy.parse(editDateExpire
					.getText().toString());

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Log.i(passedDate.toString(), date.toString());
		if (!Util.isEmptyString(editDateIssue.getText().toString())
				&& !Util.isEmptyString(editDateExpire.getText().toString())) {
			if (issueDate.before(date) || issueDate.equals(date)) {
				if (issueDate.before(expiryDate)) {
					if (expiryDate.after(date)) {
						Education education = new Education();
						education.setIssueDate(editDateIssue.getText()
								.toString());
						education.setExpiryDate(editDateExpire.getText()
								.toString());
						addCertification();
					} else if (check) {
						Util.Alertdialog(getActivity(),
								"Please enter correct dates as Expiry Date can be in future only");
					}
				} else if (check) {
					Util.Alertdialog(
							getActivity(),
							"Please enter correct dates as Expiry Date cannot be before or same as Date Issued");
				}
			} else if (check) {
				Util.Alertdialog(getActivity(),
						"Please enter correct Issued Date as it can be in past only");
			}
		} else if (check) {
			Util.Alertdialog(getActivity(), "Please enter dates");
		}

	}

	private void educationValidation(boolean check) {
		Date date = new Date();
		Date passedDate = null;

		try {
			passedDate = EzApp.sdfMmddyy.parse(editPassed
					.getText().toString());

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Log.i(passedDate.toString(), date.toString());
		if (!Util.isEmptyString(editPassed.getText().toString())) {
			if (passedDate.before(date)) {
				Education education = new Education();
				education.setDatepass(editPassed.getText().toString());

				addEducation();

			} else if (check) {
				Util.Alertdialog(getActivity(),
						"Please enter correct Passed Date as Passed date cannot be in future");
			}
		} else if (check) {
			Util.Alertdialog(getActivity(), "Please enter start and end dates");
		}

	}

}

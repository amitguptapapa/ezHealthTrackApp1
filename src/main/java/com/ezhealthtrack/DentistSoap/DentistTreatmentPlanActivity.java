package com.ezhealthtrack.DentistSoap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.app.Dialog;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.R;
import com.ezhealthtrack.DentistSoap.Model.Task;
import com.ezhealthtrack.DentistSoap.Model.TeethModel;
import com.ezhealthtrack.DentistSoap.Model.TreatmentPlanModel;
import com.ezhealthtrack.adapter.CheckedListAdapter;
import com.ezhealthtrack.util.BaseActivity;
import com.ezhealthtrack.util.Util;
import com.ezhealthtrack.views.EditUtils;
import com.ezhealthtrack.views.TeethView;

public class DentistTreatmentPlanActivity extends BaseActivity implements
		OnCheckedChangeListener,
		android.widget.RadioGroup.OnCheckedChangeListener,OnClickListener {
	private TreatmentPlanModel treatmentPlanModel;
	private Button btnSubmit;
	private CheckBox cbRestoration;
	private CheckBox cbScaling;
	private CheckBox cbCrown;
	private CheckBox cbOralMaxilloSurgery;
	private CheckBox cbPeridontal;
	private ArrayList<TeethModel> arrTeeth;
	private TeethModel selectedTeeth;
	private RadioGroup rgScale1;
	private RadioGroup rgScale2;
	private RadioGroup rgCrown;
	private RadioGroup rgRemLesion;
	private LinearLayout llTeeth;
	//private RadioGroup rgType;
	private ListView listCompleteDenture;
	private final String[] arrCompDenture = new String[] {
			"Primary impression", "Border moulding", "Secondary impression",
			"Jaw relation", "Try-in", "Denture delivered" };
	private ArrayList<String> arrCompDentureSelected;
	private CheckedListAdapter adapterCompDenture;
	private CheckBox cbCd;
	private CheckBox cbTw;
	private CheckBox cbScal;
	private CheckBox cbOthe;

	private void alternateTooth(final TextView txtTooth,
			final RadioGroup rgToothSelection, final ImageView img,
			final TeethModel teeth, final int no) {
		txtTooth.setText("");
		final RadioButton rb1 = (RadioButton) rgToothSelection
				.getChildAt(0);
		final RadioButton rb2 = (RadioButton) rgToothSelection
				.getChildAt(1);
		if (AddDentistNotesActivity.visitModel.dentistExaminationModel.oralExamination.hardTissue.tp
				.contains("Permanent")) {
			rgToothSelection.check(rb1.getId());
			selectedTeeth = teeth;
		} else {
			rgToothSelection.check(rb2.getId());
			selectedTeeth = arrTeeth.get(no);
		}
		if(teeth.arrTeethState.size()==0||arrTeeth.get(no).arrTeethState.size()==0){
			if(teeth.arrTeethState.size()==0)
				selectedTeeth = arrTeeth.get(no);
			else
				selectedTeeth = teeth;
			rgToothSelection.setVisibility(View.GONE);
			txtTooth.setText(selectedTeeth.name.replace("tooth", "Tooth"));
				
		}else{
			rgToothSelection.setVisibility(View.VISIBLE);
			txtTooth.setText("");
		}
		function2((ViewGroup) findViewById(R.id.ll_dentist_treatment_plan));
		((TextView) findViewById(R.id.txt_tooth_state)).setText(" "
				+ selectedTeeth.arrTeethState);
		((EditText) findViewById(R.id.edit_restoration_other))
				.setText(selectedTeeth.restOther);
		((EditText) findViewById(R.id.edit_peridontal))
				.setText(selectedTeeth.periOther);
		((EditText) findViewById(R.id.edit_other)).setText(selectedTeeth.other);
		function4();

		findViewById(R.id.ll_teeth_plan).setVisibility(View.VISIBLE);
		findViewById(R.id.ll_common_plan).setVisibility(View.VISIBLE);
		try {
			Log.i("on checked change", teeth.scaling1Tag + "");
			for (int i = 0; i < rgScale1.getChildCount(); i++) {
				if (((RadioButton) rgScale1.getChildAt(i)).getText().toString()
						.contains(selectedTeeth.scaling1)) {
					((RadioButton) rgScale1.getChildAt(i)).setChecked(true);
				}
			}
			for (int i = 0; i < rgScale2.getChildCount(); i++) {
				if (((RadioButton) rgScale2.getChildAt(i)).getText().toString()
						.contains(selectedTeeth.scaling2)) {
					((RadioButton) rgScale2.getChildAt(i)).setChecked(true);
				}
			}
			for (int i = 0; i < rgCrown.getChildCount(); i++) {
				if (((RadioButton) rgCrown.getChildAt(i)).getText().toString()
						.contains(selectedTeeth.crown)) {
					((RadioButton) rgCrown.getChildAt(i)).setChecked(true);
				}
			}
			for (int i = 0; i < rgRemLesion.getChildCount(); i++) {
				if (((RadioButton) rgRemLesion.getChildAt(i)).getText()
						.toString().contains(selectedTeeth.remLesion)) {
					((RadioButton) rgRemLesion.getChildAt(i)).setChecked(true);
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
		rgToothSelection
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(final RadioGroup group,
							final int checkedId) {
						final RadioButton rb = (RadioButton) group
								.findViewById(checkedId);
						if (rb.getText().toString()
								.equalsIgnoreCase(img.getTag().toString())) {
							if (selectedTeeth != null) {
								selectedTeeth.other = ((EditText) findViewById(R.id.edit_other))
										.getText().toString();
								selectedTeeth.restOther = ((EditText) findViewById(R.id.edit_restoration_other))
										.getText().toString();
								selectedTeeth.periOther = ((EditText) findViewById(R.id.edit_peridontal))
										.getText().toString();
							}
							selectedTeeth = teeth;
							function2((ViewGroup) findViewById(R.id.ll_dentist_treatment_plan));
							function4();
							((EditText) findViewById(R.id.edit_other))
									.setText(selectedTeeth.other);
							((EditText) findViewById(R.id.edit_restoration_other))
									.setText(selectedTeeth.restOther);
							((EditText) findViewById(R.id.edit_peridontal))
									.setText(selectedTeeth.periOther);
						} else {
							if (selectedTeeth != null) {
								selectedTeeth.other = ((EditText) findViewById(R.id.edit_other))
										.getText().toString();
								selectedTeeth.restOther = ((EditText) findViewById(R.id.edit_restoration_other))
										.getText().toString();
								selectedTeeth.periOther = ((EditText) findViewById(R.id.edit_peridontal))
										.getText().toString();
							}
							selectedTeeth = arrTeeth.get(no);
							function2((ViewGroup) findViewById(R.id.ll_dentist_treatment_plan));
							function4();
							((EditText) findViewById(R.id.edit_other))
									.setText(selectedTeeth.other);
							((EditText) findViewById(R.id.edit_restoration_other))
									.setText(selectedTeeth.restOther);
							((EditText) findViewById(R.id.edit_peridontal))
									.setText(selectedTeeth.periOther);
						}
						((TextView) findViewById(R.id.txt_tooth_state))
								.setText(" " + selectedTeeth.arrTeethState);
						findViewById(R.id.ll_teeth_plan).setVisibility(
								View.VISIBLE);
						findViewById(R.id.ll_common_plan).setVisibility(
								View.VISIBLE);
						try {
							Log.i("on checked change", teeth.scaling1Tag + "");
							for (int i = 0; i < rgScale1.getChildCount(); i++) {
								if (((RadioButton) rgScale1.getChildAt(i))
										.getText().toString()
										.contains(selectedTeeth.scaling1)) {
									((RadioButton) rgScale1.getChildAt(i))
											.setChecked(true);
								}
							}
							for (int i = 0; i < rgScale2.getChildCount(); i++) {
								if (((RadioButton) rgScale2.getChildAt(i))
										.getText().toString()
										.contains(selectedTeeth.scaling2)) {
									((RadioButton) rgScale2.getChildAt(i))
											.setChecked(true);
								}
							}
							for (int i = 0; i < rgCrown.getChildCount(); i++) {
								if (((RadioButton) rgCrown.getChildAt(i))
										.getText().toString()
										.contains(selectedTeeth.crown)) {
									((RadioButton) rgCrown.getChildAt(i))
											.setChecked(true);
								}
							}
							for (int i = 0; i < rgRemLesion.getChildCount(); i++) {
								if (((RadioButton) rgRemLesion.getChildAt(i))
										.getText().toString()
										.contains(selectedTeeth.remLesion)) {
									((RadioButton) rgRemLesion.getChildAt(i))
											.setChecked(true);
								}
							}
						} catch (final Exception e) {
							e.printStackTrace();
						}
					}
				});
	}

	private void AutoSave() {
		// Log.i("note", masterPlanModel.note.get("note"));
		EditUtils.autoSaveEditText(
				(EditText) findViewById(R.id.edit_other_type),
				treatmentPlanModel.othe);
		EditUtils.autoSaveEditText((EditText) findViewById(R.id.edit_note),
				treatmentPlanModel.note);
		final HashMap<String, String> hm = treatmentPlanModel.cd;
		final EditText editText = ((EditText) findViewById(R.id.edit_footer_other));
		if (hm.containsKey(editText.getTag().toString())) {
			editText.setText(hm.get(editText.getTag().toString()));
		}
		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(final Editable s) {
				// TODO Auto-generated method stub
				hm.put(editText.getTag().toString(), s.toString());
				createTableCompDenture((TableLayout) findViewById(R.id.table_comp_denture));
			}

			@Override
			public void beforeTextChanged(final CharSequence s,
					final int start, final int count, final int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(final CharSequence s, final int start,
					final int before, final int count) {
			}
		});
		final CheckBox cb = (CheckBox) findViewById(R.id.cb_mark_others_task);
		if (treatmentPlanModel.hashOthe.get("othe").status.equals("on")) {
			cb.setText("done");
			cb.setChecked(true);
		}else{
			cb.setText("pending");
			cb.setChecked(false);
		}
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					treatmentPlanModel.hashOthe.get("othe").status= "on";
					cb.setText("done");
				}else{
					treatmentPlanModel.hashOthe.get("othe").status= "off";
					cb.setText("pending");
				}
			}
		});
	}

	private void createTable(final TableLayout table) {
		final String[] arrStr = new String[] { "Tooth", "Selected Option",
				"Status", "Choose Task" };
		final int[] arrWeight = new int[] { 1, 2, 1, 1 };

		final TableRow trHeader = new TableRow(this);
		for (int i = 0; i < 4; i++) {
			final TextView timeHeader1 = new TextView(this);
			timeHeader1.setBackgroundColor(Color.parseColor("#999999"));
			timeHeader1.setGravity(Gravity.CENTER);
			timeHeader1.setPadding(5, 10, 5, 10);
			timeHeader1.setTextColor(Color.WHITE);
			final LayoutParams params = new LayoutParams(0,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			params.setMargins(1, 1, 1, 1);
			params.weight = arrWeight[i];
			timeHeader1.setLayoutParams(params);
			timeHeader1.setText(arrStr[i]);
			timeHeader1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
			trHeader.addView(timeHeader1);
		}
		table.addView(trHeader);

		for (final TeethModel teeth : arrTeeth) {
			if (teeth.arrTeethTreatmentPlan.size() > 0) {

				for (int i = 0; i <= teeth.arrTeethTreatmentPlan.size(); i++) {
					final String[] arrStr1;
					if (i == teeth.arrTeethTreatmentPlan.size()) {
						arrStr1 = new String[] { teeth.name, teeth.other,
								"Pending" };
					} else {
						arrStr1 = new String[] { teeth.name,
								teeth.arrTeethTreatmentPlan.get(i), "Pending" };
					}
					final TableRow tr = new TableRow(this);
					tr.setTag("" + (i + 1));
					for (int j = 0; j < 3; j++) {
						final TextView c1 = new TextView(this);
						c1.setTag("c" + j);
						c1.setGravity(Gravity.CENTER);
						c1.setBackgroundColor(Color.parseColor("#FFFFFF"));
						c1.setPadding(10, 10, 10, 10);
						c1.setTextColor(Color.BLACK);
						if ((i == 0) || (j != 0)) {
							if (j == 1) {
								String s;
								final String str = arrStr1[j];
								if (str.contains("scaling")) {
									s = str + " (" + teeth.scaling1 + ","
											+ teeth.scaling2 + ")";
								} else if (str.contains("crown")) {
									s = str + " (" + teeth.crown + ")";
								} else if (str
										.contains("oral and maxillofacial surgery")) {
									s = str + " for removal of lesion ("
											+ teeth.remLesion + ")";
								} else if (str.contains("gingivoplasty")) {
									s = "periodontal surgery - " + str;
								} else if (str.contains("gingivectomy")) {
									s = "peridoontal surgery - " + str;
								} else if (str.contains("osteoplasty")) {
									s = "periodontal surgery - " + str;
								} else if (str.contains("ostectomy")) {
									s = "periodontal surgery - " + str;
								} else if (str.contains("modified widman flap")) {
									s = "periodontal surgery - " + str;
								} else if (str
										.contains("apically displaced flap")) {
									s = "periodontal surgery - " + str;
								} else if (str.contains("palatal flap")) {
									s = "periodontal surgery - " + str;
								} else if (str.contains("undisplaced flap")) {
									s = "periodontal surgery - " + str;
								} else if (str
										.contains("papilla preservation flap")) {
									s = "periodontal surgery - " + str;
								} else if (str.contains("double papilla flap")) {
									s = "periodontal surgery - " + str;
								} else if (str.contains("distal molar surgery")) {
									s = "periodontal surgery - " + str;
								} else if (str.contains("glass ionomer cement")) {
									s = "restoration - " + str;
								} else if (str.contains("composite")) {
									s = "restoration - " + str;
								} else if (str.contains("periodontal surgery")) {
									s = "periodontal surgery - "
											+ teeth.periOther;
								} else if (str.contains("restoration")) {
									s = "restoration - " + teeth.restOther;
								} else {
									s = str;
								}
								c1.setText(s);
							} else {
								c1.setText(arrStr1[j]);
							}
						}
						final LayoutParams params = new LayoutParams(0, 60);
						params.setMargins(1, 1, 1, 1);
						params.weight = arrWeight[j];
						c1.setLayoutParams(params);
						c1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
						tr.addView(c1);
					}
					final CheckBox c1 = new CheckBox(this);
					c1.setTag("cb" + i);
					c1.setGravity(Gravity.CENTER);
					c1.setBackgroundColor(Color.parseColor("#FFFFFF"));
					c1.setPadding(10, 10, 10, 10);
					c1.setTextColor(Color.BLACK);
					for (final TeethModel teeth1 : AddDentistNotesActivity.visitNotes.dentistExaminationModel.oralExamination.hardTissue.arrTeeth) {
						if ((teeth1.name).equals(teeth.name)) {
							if (teeth1.arrTeethTreatmentPlan
									.contains(arrStr1[1])) {
								if (!Util.isEmptyString(teeth1.hashStatus
										.get(arrStr1[1]))) {
									((TextView) tr.findViewWithTag("c2"))
											.setText(teeth1.hashStatus
													.get(arrStr1[1]));
								} else {
									((TextView) tr.findViewWithTag("c2"))
											.setText("pending");
								}
							} else if (i == teeth.arrTeethTreatmentPlan.size()) {
								if (teeth1.hashStatus.containsKey(arrStr1[1])) {
									if (!Util.isEmptyString(teeth1.hashStatus
											.get(arrStr1[1]))) {
										((TextView) tr.findViewWithTag("c2"))
												.setText(teeth1.hashStatus
														.get(arrStr1[1]));
									} else {
										((TextView) tr.findViewWithTag("c2"))
												.setText("pending");
									}
								}
							} else {
								c1.setChecked(false);
								((TextView) tr.findViewWithTag("c2"))
										.setText("pending");
							}
						}
					}
					final LayoutParams params = new LayoutParams(0, 60);
					params.setMargins(1, 1, 1, 1);
					params.weight = arrWeight[3];
					c1.setLayoutParams(params);
					c1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
					c1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(
								final CompoundButton buttonView,
								final boolean isChecked) {
							String s;
							final String str = arrStr1[1];
							if (str.contains("scaling")) {
								s = "scaling";
							} else if (str.contains("crown")) {
								s = "crown";
							} else if (str
									.contains("oral and maxillofacial surgery")) {
								s = "oral and maxillofacial surgery";
							} else if (str.contains("gingivoplasty")) {
								s = "periodontal surgery - " + str;
							} else if (str.contains("gingivectomy")) {
								s = "peridoontal surgery - " + str;
							} else if (str.contains("osteoplasty")) {
								s = "periodontal surgery - " + str;
							} else if (str.contains("ostectomy")) {
								s = "periodontal surgery - " + str;
							} else if (str.contains("modified widman flap")) {
								s = "periodontal surgery - " + str;
							} else if (str
									.contains("apically displaced flap")) {
								s = "periodontal surgery - " + str;
							} else if (str.contains("palatal flap")) {
								s = "periodontal surgery - " + str;
							} else if (str.contains("undisplaced flap")) {
								s = "periodontal surgery - " + str;
							} else if (str
									.contains("papilla preservation flap")) {
								s = "periodontal surgery - " + str;
							} else if (str.contains("double papilla flap")) {
								s = "periodontal surgery - " + str;
							} else if (str.contains("distal molar surgery")) {
								s = "periodontal surgery - " + str;
							} else if (str.contains("glass ionomer cement")) {
								s = "restoration - " + str;
							} else if (str.contains("composite")) {
								s = "restoration - " + str;
							} else if (str.contains("periodontal surgery")) {
								s = "periodontal surgery - "
										+ teeth.periOther;
							} else if (str.contains("restoration")) {
								s = "restoration - " + teeth.restOther;
							} else {
								s = str;
							}
							for (final TeethModel teeth1 : AddDentistNotesActivity.visitNotes.dentistExaminationModel.oralExamination.hardTissue.arrTeeth) {
								if ((teeth1.name).equals(teeth.name)) {
									Task task = new Task();
									task.setDate(EzApp.sdfyyMmdd
											.format(Calendar.getInstance()
													.getTime()));
									try{
									if (isChecked) {
										teeth1.hashStatus.put(arrStr1[1],
												"done");
										((TextView) tr.findViewWithTag("c2"))
												.setText("done");
										task.setStatus("done");
										if(AddDentistNotesActivity.masterPlan.arrTeeth.get(arrTeeth.indexOf(teeth1)).hashTask.containsKey(s))
										AddDentistNotesActivity.masterPlan.arrTeeth.get(arrTeeth.indexOf(teeth1)).hashTask.get(s).setStatus("done");
										else if(s.contains(teeth1.periOther)&&AddDentistNotesActivity.masterPlan.arrTeeth.get(arrTeeth.indexOf(teeth1)).hashTask.containsKey("periodontal surgery - othe"))
											AddDentistNotesActivity.masterPlan.arrTeeth.get(arrTeeth.indexOf(teeth1)).hashTask.get("periodontal surgery - othe").setStatus("done");
										else if(s.contains(teeth1.restOther)&&AddDentistNotesActivity.masterPlan.arrTeeth.get(arrTeeth.indexOf(teeth1)).hashTask.containsKey("restoration - othe"))
											AddDentistNotesActivity.masterPlan.arrTeeth.get(arrTeeth.indexOf(teeth1)).hashTask.get("restoration - othe").setStatus("done");
										else if(s.contains(teeth1.other)&&AddDentistNotesActivity.masterPlan.arrTeeth.get(arrTeeth.indexOf(teeth1)).hashTask.containsKey("othe"))
											AddDentistNotesActivity.masterPlan.arrTeeth.get(arrTeeth.indexOf(teeth1)).hashTask.get("othe").setStatus("done");
									} else {
										teeth1.hashStatus.remove(arrStr1[1]);
										((TextView) tr.findViewWithTag("c2"))
												.setText("pending");
										task.setStatus("in progress");
										if(AddDentistNotesActivity.masterPlan.arrTeeth.get(arrTeeth.indexOf(teeth1)).hashTask.containsKey(s))
										AddDentistNotesActivity.masterPlan.arrTeeth.get(arrTeeth.indexOf(teeth1)).hashTask.get(s).setStatus("in progress");
										else if(s.contains(teeth1.periOther)&&AddDentistNotesActivity.masterPlan.arrTeeth.get(arrTeeth.indexOf(teeth1)).hashTask.containsKey("periodontal surgery - othe"))
											AddDentistNotesActivity.masterPlan.arrTeeth.get(arrTeeth.indexOf(teeth1)).hashTask.get("periodontal surgery - othe").setStatus("in progress");
										else if(s.contains(teeth1.restOther)&&AddDentistNotesActivity.masterPlan.arrTeeth.get(arrTeeth.indexOf(teeth1)).hashTask.containsKey("restoration - othe"))
											AddDentistNotesActivity.masterPlan.arrTeeth.get(arrTeeth.indexOf(teeth1)).hashTask.get("restoration - othe").setStatus("in progress");
										else if(s.contains(teeth1.other)&&AddDentistNotesActivity.masterPlan.arrTeeth.get(arrTeeth.indexOf(teeth1)).hashTask.containsKey("othe"))
											AddDentistNotesActivity.masterPlan.arrTeeth.get(arrTeeth.indexOf(teeth1)).hashTask.get("othe").setStatus("in progress");
									}
									}catch(Exception e){
										e.printStackTrace();
									}
									
									teeth1.crown = teeth.crown;
									teeth1.remLesion = teeth.remLesion;
									teeth1.scaling1 = teeth.scaling1;
									teeth1.scaling2 = teeth.scaling2;
								}
							}

						}
					});
					tr.addView(c1);
					if (i == teeth.arrTeethTreatmentPlan.size()) {
						if (!Util.isEmptyString(teeth.other))
							table.addView(tr);
					} else if (teeth.arrTeethTreatmentPlan.get(i).contains(
							"restoration")) {
						if (!Util.isEmptyString(teeth.restOther))
							table.addView(tr);
					} else if (teeth.arrTeethTreatmentPlan.get(i).contains(
							"periodontal surgery")) {
						if (!Util.isEmptyString(teeth.periOther))
							table.addView(tr);
					} else {
						table.addView(tr);
					}
				}
			}
		}

	}

	private void createTableCompDenture(final TableLayout table) {
		table.setVisibility(View.VISIBLE);
		table.removeAllViews();
		final String[] arrStr = new String[] { "Selected Option",
				"Mark as done", "Status" };
		final int[] arrWeight = new int[] { 1, 1, 1 };

		final TableRow trHeader = new TableRow(this);
		for (int i = 0; i < 3; i++) {
			final TextView timeHeader1 = new TextView(this);
			timeHeader1.setBackgroundColor(Color.parseColor("#999999"));
			timeHeader1.setGravity(Gravity.CENTER);
			timeHeader1.setPadding(5, 10, 5, 10);
			timeHeader1.setTextColor(Color.WHITE);
			final LayoutParams params = new LayoutParams(0,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			params.setMargins(1, 1, 1, 1);
			params.weight = arrWeight[i];
			timeHeader1.setLayoutParams(params);
			timeHeader1.setText(arrStr[i]);
			timeHeader1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
			trHeader.addView(timeHeader1);
		}
		table.addView(trHeader);
		for (final String s : treatmentPlanModel.arrCompDenture) {
			final TableRow tr = new TableRow(this);
			final TextView c1 = new TextView(this);
			c1.setTag("c" + 0);
			c1.setGravity(Gravity.CENTER);
			c1.setBackgroundColor(Color.parseColor("#FFFFFF"));
			c1.setPadding(10, 10, 10, 10);
			c1.setTextColor(Color.BLACK);
			c1.setText(s);
			final LayoutParams params = new LayoutParams(0, 60);
			params.setMargins(1, 1, 1, 1);
			params.weight = arrWeight[1];
			c1.setLayoutParams(params);
			c1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			tr.addView(c1);

			final CheckBox cb1 = new CheckBox(this);
			cb1.setTag("cb" + 0);
			cb1.setGravity(Gravity.CENTER);
			cb1.setBackgroundColor(Color.parseColor("#FFFFFF"));
			cb1.setPadding(10, 10, 10, 10);
			cb1.setTextColor(Color.BLACK);
			params.setMargins(1, 1, 1, 1);
			params.weight = arrWeight[1];
			cb1.setLayoutParams(params);
			cb1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			if (!Util.isEmptyString(treatmentPlanModel.cd.get(s))) {
				cb1.setChecked(!treatmentPlanModel.cd.get(s).equals("pending"));
			} else {
				treatmentPlanModel.cd.put(s, "pending");
			}
			tr.addView(cb1);

			final TextView c2 = new TextView(this);
			c2.setTag("c" + 1);
			c2.setGravity(Gravity.CENTER);
			c2.setBackgroundColor(Color.parseColor("#FFFFFF"));
			c2.setPadding(10, 10, 10, 10);
			c2.setTextColor(Color.BLACK);
			c2.setText(treatmentPlanModel.cd.get(s));
			params.setMargins(1, 1, 1, 1);
			params.weight = arrWeight[1];
			c2.setLayoutParams(params);
			c2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			tr.addView(c2);
			cb1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(final CompoundButton buttonView,
						final boolean isChecked) {
					if (isChecked) {
						treatmentPlanModel.cd.put(s, "done");
						if(!Util.isEmptyString(AddDentistNotesActivity.masterPlan.masterPlanType))
						AddDentistNotesActivity.masterPlan.cd.put(s, "done");
						c2.setText("done");
					} else {
						treatmentPlanModel.cd.put(s, "pending");
						if(!Util.isEmptyString(AddDentistNotesActivity.masterPlan.masterPlanType))
						AddDentistNotesActivity.masterPlan.cd.put(s, "in progress");
					}
				}
			});

			table.addView(tr);
		}
		if (!Util.isEmptyString(treatmentPlanModel.cd.get("othe"))) {
			final TableRow tr = new TableRow(this);
			final TextView c1 = new TextView(this);
			c1.setTag("c" + 0);
			c1.setGravity(Gravity.CENTER);
			c1.setBackgroundColor(Color.parseColor("#FFFFFF"));
			c1.setPadding(10, 10, 10, 10);
			c1.setTextColor(Color.BLACK);
			treatmentPlanModel.hashCd.get("othe").value = treatmentPlanModel.cd
					.get("othe");
			c1.setText(treatmentPlanModel.cd.get("othe"));

			final LayoutParams params = new LayoutParams(0, 60);
			params.setMargins(1, 1, 1, 1);
			params.weight = arrWeight[1];
			c1.setLayoutParams(params);
			c1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			tr.addView(c1);

			final CheckBox cb1 = new CheckBox(this);
			cb1.setTag("cb" + 0);
			cb1.setGravity(Gravity.CENTER);
			cb1.setBackgroundColor(Color.parseColor("#FFFFFF"));
			cb1.setPadding(10, 10, 10, 10);
			cb1.setTextColor(Color.BLACK);
			params.setMargins(1, 1, 1, 1);
			params.weight = arrWeight[1];
			cb1.setLayoutParams(params);
			cb1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			if (!Util.isEmptyString(treatmentPlanModel.hashCd.get("othe").status)) {
				cb1.setChecked(!treatmentPlanModel.hashCd.get("othe").status
						.equals("pending"));
			} else {
				treatmentPlanModel.hashCd.get("othe").status = "pending";
			}
			tr.addView(cb1);

			final TextView c2 = new TextView(this);
			c2.setTag("c" + 1);
			c2.setGravity(Gravity.CENTER);
			c2.setBackgroundColor(Color.parseColor("#FFFFFF"));
			c2.setPadding(10, 10, 10, 10);
			c2.setTextColor(Color.BLACK);
			c2.setText(treatmentPlanModel.hashCd.get("othe").status);
			params.setMargins(1, 1, 1, 1);
			params.weight = arrWeight[1];
			c2.setLayoutParams(params);
			c2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			tr.addView(c2);
			cb1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(final CompoundButton buttonView,
						final boolean isChecked) {
					if (isChecked) {
						treatmentPlanModel.hashCd.get("othe").status = "done";
						if(!Util.isEmptyString(AddDentistNotesActivity.masterPlan.masterPlanType))
							AddDentistNotesActivity.masterPlan.hashCd.get("othe").status = "done";
						c2.setText("done");
					} else {
						treatmentPlanModel.hashCd.get("othe").status = "pending";
						c2.setText("pending");
						if(!Util.isEmptyString(AddDentistNotesActivity.masterPlan.masterPlanType))
							AddDentistNotesActivity.masterPlan.hashCd.get("othe").status = "in progress";
					}
				}
			});

			table.addView(tr);
		}
	}

	private void createTableScaling(final TableLayout table) {
		table.removeAllViews();
		final String[] arrStr = new String[] { "Selected Option",
				"Mark as done", "Status" };
		final int[] arrWeight = new int[] { 1, 1, 1 };

		final TableRow trHeader = new TableRow(this);
		for (int i = 0; i < 3; i++) {
			final TextView timeHeader1 = new TextView(this);
			timeHeader1.setBackgroundColor(Color.parseColor("#999999"));
			timeHeader1.setGravity(Gravity.CENTER);
			timeHeader1.setPadding(5, 10, 5, 10);
			timeHeader1.setTextColor(Color.WHITE);
			final LayoutParams params = new LayoutParams(0,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			params.setMargins(1, 1, 1, 1);
			params.weight = arrWeight[i];
			timeHeader1.setLayoutParams(params);
			timeHeader1.setText(arrStr[i]);
			timeHeader1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
			trHeader.addView(timeHeader1);
		}
		table.addView(trHeader);
		final TableRow tr = new TableRow(this);
		final TextView c1 = new TextView(this);
		c1.setTag("c" + 0);
		c1.setGravity(Gravity.CENTER);
		c1.setBackgroundColor(Color.parseColor("#FFFFFF"));
		c1.setPadding(10, 10, 10, 10);
		c1.setTextColor(Color.BLACK);
		c1.setText(treatmentPlanModel.scal.get("value"));
		final LayoutParams params = new LayoutParams(0, 60);
		params.setMargins(1, 1, 1, 1);
		params.weight = arrWeight[1];
		c1.setLayoutParams(params);
		c1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		tr.addView(c1);

		final CheckBox cb1 = new CheckBox(this);
		cb1.setTag("cb" + 0);
		cb1.setGravity(Gravity.CENTER);
		cb1.setBackgroundColor(Color.parseColor("#FFFFFF"));
		cb1.setPadding(10, 10, 10, 10);
		cb1.setTextColor(Color.BLACK);
		params.setMargins(1, 1, 1, 1);
		params.weight = arrWeight[1];
		cb1.setLayoutParams(params);
		cb1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		cb1.setChecked(!treatmentPlanModel.scal.get("status").equals("pending"));
		cb1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(final CompoundButton buttonView,
					final boolean isChecked) {
				if (isChecked) {
					treatmentPlanModel.scal.put("status", "done");
					if(!Util.isEmptyString(AddDentistNotesActivity.masterPlan.masterPlanType))
						AddDentistNotesActivity.masterPlan.scal.put("status","done");
				} else {
					treatmentPlanModel.scal.put("status", "pending");
					if(!Util.isEmptyString(AddDentistNotesActivity.masterPlan.masterPlanType))
						AddDentistNotesActivity.masterPlan.scal.put("status","in progress");
				}
				createTableScaling(table);
			}
		});
		tr.addView(cb1);

		final TextView c2 = new TextView(this);
		c2.setTag("c" + 1);
		c2.setGravity(Gravity.CENTER);
		c2.setBackgroundColor(Color.parseColor("#FFFFFF"));
		c2.setPadding(10, 10, 10, 10);
		c2.setTextColor(Color.BLACK);
		c2.setText(treatmentPlanModel.scal.get("status"));
		params.setMargins(1, 1, 1, 1);
		params.weight = arrWeight[1];
		c2.setLayoutParams(params);
		c2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		tr.addView(c2);

		table.addView(tr);
	}

	private void function1(final ViewGroup v) {
		for (int i = 0; i < v.getChildCount(); i++) {
			if (v.getChildAt(i) instanceof CheckBox) {
				((CheckBox) v.getChildAt(i))
						.setOnCheckedChangeListener(new OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(
									final CompoundButton buttonView,
									final boolean isChecked) {
								if (isChecked) {
									if (!selectedTeeth.arrTeethTreatmentPlan
											.contains(buttonView.getText())) {
										selectedTeeth.arrTeethTreatmentPlan
												.add((String) buttonView
														.getText());
									}
								} else {
									selectedTeeth.arrTeethTreatmentPlan
											.remove(buttonView.getText());
								}

							}
						});
			} else if (v.getChildAt(i) instanceof ViewGroup) {
				function1((ViewGroup) v.getChildAt(i));
			}
		}

	}

	private void function2(final ViewGroup v) {
		Log.i("", "function2 called");
		for (int i = 0; i < v.getChildCount(); i++) {
			if (v.getChildAt(i) instanceof CheckBox) {
				final CheckBox cb = (CheckBox) v.getChildAt(i);
				if (selectedTeeth.arrTeethTreatmentPlan.contains(cb.getText())) {
					cb.setChecked(true);
				} else {
					cb.setChecked(false);
				}
			} else if (v.getChildAt(i) instanceof ViewGroup) {
				function2((ViewGroup) v.getChildAt(i));
			}
		}
	}

	private void function3(final TeethModel teeth, final ImageView img) {
		final RadioGroup rgToothSelection = (RadioGroup) findViewById(R.id.rg_teeth_selection);
		final RadioButton rb1 = (RadioButton) rgToothSelection.getChildAt(0);
		final RadioButton rb2 = (RadioButton) rgToothSelection.getChildAt(1);
		rgToothSelection.check(rb1.getId());
		final TextView txtTooth = ((TextView) findViewById(R.id.txt_tooth_name));
		if (teeth.name.equals("tooth11")) {
			alternateTooth(txtTooth, rgToothSelection, img, teeth, 32);
			rb1.setText("Tooth11");
			rb2.setText("Tooth51");
		} else if (teeth.name.equals("tooth12")) {
			alternateTooth(txtTooth, rgToothSelection, img, teeth, 33);
			rb1.setText("Tooth12");
			rb2.setText("Tooth52");
		} else if (teeth.name.equals("tooth13")) {
			alternateTooth(txtTooth, rgToothSelection, img, teeth, 34);
			rb1.setText("Tooth13");
			rb2.setText("Tooth53");
		} else if (teeth.name.equals("tooth16")) {
			alternateTooth(txtTooth, rgToothSelection, img, teeth, 35);
			rb1.setText("Tooth16");
			rb2.setText("Tooth54");
		} else if (teeth.name.equals("tooth17")) {
			alternateTooth(txtTooth, rgToothSelection, img, teeth, 36);
			rb1.setText("Tooth17");
			rb2.setText("Tooth55");
		} else if (teeth.name.equals("tooth21")) {
			alternateTooth(txtTooth, rgToothSelection, img, teeth, 37);
			rb1.setText("Tooth21");
			rb2.setText("Tooth61");
		} else if (teeth.name.equals("tooth22")) {
			alternateTooth(txtTooth, rgToothSelection, img, teeth, 38);
			rb1.setText("Tooth22");
			rb2.setText("Tooth62");
		} else if (teeth.name.equals("tooth23")) {
			alternateTooth(txtTooth, rgToothSelection, img, teeth, 39);
			rb1.setText("Tooth23");
			rb2.setText("Tooth63");
		} else if (teeth.name.equals("tooth26")) {
			alternateTooth(txtTooth, rgToothSelection, img, teeth, 40);
			rb1.setText("Tooth26");
			rb2.setText("Tooth64");
		} else if (teeth.name.equals("tooth27")) {
			alternateTooth(txtTooth, rgToothSelection, img, teeth, 41);
			rb1.setText("Tooth27");
			rb2.setText("Tooth65");
		} else if (teeth.name.equals("tooth31")) {
			alternateTooth(txtTooth, rgToothSelection, img, teeth, 42);
			rb1.setText("Tooth31");
			rb2.setText("Tooth71");
		} else if (teeth.name.equals("tooth32")) {
			alternateTooth(txtTooth, rgToothSelection, img, teeth, 43);
			rb1.setText("Tooth32");
			rb2.setText("Tooth72");
		} else if (teeth.name.equals("tooth33")) {
			alternateTooth(txtTooth, rgToothSelection, img, teeth, 44);
			rb1.setText("Tooth33");
			rb2.setText("Tooth73");
		} else if (teeth.name.equals("tooth36")) {
			alternateTooth(txtTooth, rgToothSelection, img, teeth, 45);
			rb1.setText("Tooth36");
			rb2.setText("Tooth74");
		} else if (teeth.name.equals("tooth37")) {
			alternateTooth(txtTooth, rgToothSelection, img, teeth, 46);
			rb1.setText("Tooth37");
			rb2.setText("Tooth75");
		} else if (teeth.name.equals("tooth41")) {
			alternateTooth(txtTooth, rgToothSelection, img, teeth, 47);
			rb1.setText("Tooth41");
			rb2.setText("Tooth81");
		} else if (teeth.name.equals("tooth42")) {
			alternateTooth(txtTooth, rgToothSelection, img, teeth, 48);
			rb1.setText("Tooth42");
			rb2.setText("Tooth82");
		} else if (teeth.name.equals("tooth43")) {
			alternateTooth(txtTooth, rgToothSelection, img, teeth, 49);
			rb1.setText("Tooth43");
			rb2.setText("Tooth83");
		} else if (teeth.name.equals("tooth46")) {
			alternateTooth(txtTooth, rgToothSelection, img, teeth, 50);
			rb1.setText("Tooth46");
			rb2.setText("Tooth84");
		} else if (teeth.name.equals("tooth47")) {
			alternateTooth(txtTooth, rgToothSelection, img, teeth, 51);
			rb1.setText("Tooth47");
			rb2.setText("Tooth85");
		} else {
			txtTooth.setText(teeth.name.replace("tooth", "Tooth"));
			rgToothSelection.setVisibility(View.GONE);
			selectedTeeth = teeth;
			function2((ViewGroup) findViewById(R.id.ll_dentist_treatment_plan));
			((TextView) findViewById(R.id.txt_tooth_state)).setText(" "
					+ selectedTeeth.arrTeethState);
			((EditText) findViewById(R.id.edit_other))
					.setText(selectedTeeth.other);
			((EditText) findViewById(R.id.edit_restoration_other))
					.setText(selectedTeeth.restOther);
			((EditText) findViewById(R.id.edit_peridontal))
					.setText(selectedTeeth.periOther);
			function4();
			findViewById(R.id.ll_teeth_plan).setVisibility(View.VISIBLE);
			findViewById(R.id.ll_common_plan).setVisibility(View.VISIBLE);
			try {
				Log.i("on checked change", teeth.scaling1Tag + "");
				for (int i = 0; i < rgScale1.getChildCount(); i++) {
					if (((RadioButton) rgScale1.getChildAt(i)).getText()
							.toString().contains(selectedTeeth.scaling1)) {
						((RadioButton) rgScale1.getChildAt(i)).setChecked(true);
					}
				}
				for (int i = 0; i < rgScale2.getChildCount(); i++) {
					if (((RadioButton) rgScale2.getChildAt(i)).getText()
							.toString().contains(selectedTeeth.scaling2)) {
						((RadioButton) rgScale2.getChildAt(i)).setChecked(true);
					}
				}
				for (int i = 0; i < rgCrown.getChildCount(); i++) {
					if (((RadioButton) rgCrown.getChildAt(i)).getText()
							.toString().contains(selectedTeeth.crown)) {
						((RadioButton) rgCrown.getChildAt(i)).setChecked(true);
					}
				}
				for (int i = 0; i < rgRemLesion.getChildCount(); i++) {
					if (((RadioButton) rgRemLesion.getChildAt(i)).getText()
							.toString().contains(selectedTeeth.remLesion)) {
						((RadioButton) rgRemLesion.getChildAt(i))
								.setChecked(true);
					}
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void function4() {
		if (selectedTeeth.arrTeethTreatmentPlan.contains("restoration")) {
			findViewById(R.id.ll_restoration).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.ll_restoration).setVisibility(View.GONE);
		}
		if (selectedTeeth.arrTeethTreatmentPlan.contains("scaling")) {
			findViewById(R.id.ll_scaling).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.ll_scaling).setVisibility(View.GONE);
		}
		if (selectedTeeth.arrTeethTreatmentPlan.contains("crown")) {
			findViewById(R.id.rg_crown).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.rg_crown).setVisibility(View.GONE);
		}
		if (selectedTeeth.arrTeethTreatmentPlan
				.contains("oral and maxillofacial surgery")) {
			findViewById(R.id.ll_oral_maxillo_surgery).setVisibility(
					View.VISIBLE);
		} else {
			findViewById(R.id.ll_oral_maxillo_surgery).setVisibility(View.GONE);
		}
		if (selectedTeeth.arrTeethTreatmentPlan.contains("periodontal surgery")) {
			findViewById(R.id.ll_peridontal).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.ll_peridontal).setVisibility(View.GONE);
		}
	}

	private int getAlternateToothPos(final TeethModel teeth) {
		if (teeth.name.equals("tooth11")) {
			return 32;
		} else if (teeth.name.equals("tooth12")) {
			return 33;
		} else if (teeth.name.equals("tooth13")) {
			return 34;
		} else if (teeth.name.equals("tooth16")) {
			return 35;
		} else if (teeth.name.equals("tooth17")) {
			return 36;
		} else if (teeth.name.equals("tooth21")) {
			return 37;
		} else if (teeth.name.equals("tooth22")) {
			return 38;
		} else if (teeth.name.equals("tooth23")) {
			return 39;
		} else if (teeth.name.equals("tooth26")) {
			return 40;
		} else if (teeth.name.equals("tooth27")) {
			return 41;
		} else if (teeth.name.equals("tooth31")) {
			return 42;
		} else if (teeth.name.equals("tooth32")) {
			return 43;
		} else if (teeth.name.equals("tooth33")) {
			return 44;
		} else if (teeth.name.equals("tooth36")) {
			return 45;
		} else if (teeth.name.equals("tooth37")) {
			return 46;
		} else if (teeth.name.equals("tooth41")) {
			return 47;
		} else if (teeth.name.equals("tooth42")) {
			return 48;
		} else if (teeth.name.equals("tooth43")) {
			return 49;
		} else if (teeth.name.equals("tooth46")) {
			return 50;
		} else if (teeth.name.equals("tooth47")) {
			return 51;
		} else {
			return arrTeeth.indexOf(teeth);
		}
	}

	@Override
	public void onCheckedChanged(final CompoundButton buttonView,
			final boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.cb_mark_others_task:
			break;
		case R.id.cb_restoration:
			Util.showHideView(findViewById(R.id.ll_restoration), isChecked,
					selectedTeeth.arrTeethTreatmentPlan,
					(String) buttonView.getText());
			break;

		case R.id.cb_scaling:
			Util.showHideView(findViewById(R.id.ll_scaling), isChecked,
					selectedTeeth.arrTeethTreatmentPlan,
					(String) buttonView.getText());
			break;

		case R.id.cb_crown:
			Util.showHideView(findViewById(R.id.rg_crown), isChecked,
					selectedTeeth.arrTeethTreatmentPlan,
					(String) buttonView.getText());
			break;

		case R.id.cb_oral_maxillo_surgery:
			Util.showHideView(findViewById(R.id.ll_oral_maxillo_surgery),
					isChecked, selectedTeeth.arrTeethTreatmentPlan,
					(String) buttonView.getText());
			break;

		case R.id.cb_periodontal_surgery:
			Util.showHideView(findViewById(R.id.ll_peridontal), isChecked,
					selectedTeeth.arrTeethTreatmentPlan,
					(String) buttonView.getText());
			break;
			
		case R.id.cb_cd:

			hideAllViews();
			if (isChecked) {
				treatmentPlanModel.masterPlanType = "Complete Denture";
				findViewById(R.id.ll_comp_denture).setVisibility(View.VISIBLE);
				treatmentPlanModel.hashVal.put("cd", "on");
			} else {
				treatmentPlanModel.hashVal.put("cd", "off");
			}
			break;

		case R.id.cb_tw:
			hideAllViews();
			if (isChecked) {
				treatmentPlanModel.hashVal.put("tw", "on");
				treatmentPlanModel.masterPlanType = "Tooth Wise";
				findViewById(R.id.rl_complete_denture).setVisibility(
						View.VISIBLE);
				findViewById(R.id.toothimage).setVisibility(View.VISIBLE);
			} else {
				treatmentPlanModel.hashVal.put("tw", "off");
			}
			break;

		case R.id.cb_scal:
			hideAllViews();
			if (isChecked) {
				findViewById(R.id.ll_scaling_type).setVisibility(View.VISIBLE);
				treatmentPlanModel.masterPlanType = "Scaling";
				treatmentPlanModel.hashVal.put("scal", "on");
			} else {
				treatmentPlanModel.hashVal.put("scal", "off");
			}
			break;

		case R.id.cb_othe:
			hideAllViews();
			if (isChecked) {
				findViewById(R.id.ll_other_type).setVisibility(View.VISIBLE);
				treatmentPlanModel.masterPlanType = "Other";
				treatmentPlanModel.hashVal.put("othe", "on");
			} else {
				treatmentPlanModel.hashVal.put("othe", "off");
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void onCheckedChanged(final RadioGroup group, final int checkedId) {
		final RadioButton cb = (RadioButton) group.findViewById(checkedId);
		if (cb != null) {
			switch (group.getId()) {
			case R.id.rg_scaling_1:
				Log.i("on checked change" + group.getId(), R.id.rg_scaling_1
						+ "");
				selectedTeeth.scaling1 = (String) cb.getText();
				selectedTeeth.scaling1Tag = checkedId;
				break;

			case R.id.rg_scaling_2:
				selectedTeeth.scaling2 = (String) cb.getText();
				selectedTeeth.scaling2Tag = checkedId;
				break;

			case R.id.rg_crown:
				selectedTeeth.crown = (String) cb.getText();
				selectedTeeth.crownTag = checkedId;
				break;

			case R.id.rg_oral_rem_lesion:
				selectedTeeth.remLesion = (String) cb.getText();
				selectedTeeth.remLesionTag = checkedId;
				break;

			case R.id.rg_masterplan_type:
				if (cb.getText().equals("Complete Denture")) {
					findViewById(R.id.ll_comp_denture).setVisibility(
							View.VISIBLE);
					treatmentPlanModel.masterPlanType = "Complete Denture";
				} else {
					findViewById(R.id.ll_comp_denture).setVisibility(View.GONE);
				}
				if (cb.getText().equals("Tooth Wise")) {
					treatmentPlanModel.masterPlanType = "Tooth Wise";
					findViewById(R.id.rl_complete_denture).setVisibility(
							View.VISIBLE);
					findViewById(R.id.toothimage).setVisibility(View.VISIBLE);
				} else {
					findViewById(R.id.rl_complete_denture).setVisibility(
							View.GONE);
					findViewById(R.id.toothimage).setVisibility(View.GONE);
				}
				if (cb.getText().equals("Scaling")) {
					findViewById(R.id.ll_scaling_type).setVisibility(
							View.VISIBLE);
					treatmentPlanModel.masterPlanType = "Scaling";
				} else {
					findViewById(R.id.ll_scaling_type).setVisibility(View.GONE);
				}

				if (cb.getText().equals("Other")) {
					findViewById(R.id.ll_other_type)
							.setVisibility(View.VISIBLE);
					treatmentPlanModel.masterPlanType = "Other";
				} else {
					findViewById(R.id.ll_other_type).setVisibility(View.GONE);
				}

				break;

			default:
				break;
			}
		}

	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dentist_treatmentplan);
		cbCd = ((CheckBox) findViewById(R.id.cb_cd));
		cbCd.setOnCheckedChangeListener(this);
		cbTw = ((CheckBox) findViewById(R.id.cb_tw));
		cbTw.setOnCheckedChangeListener(this);
		cbScal = ((CheckBox) findViewById(R.id.cb_scal));
		cbScal.setOnCheckedChangeListener(this);
		cbOthe = ((CheckBox) findViewById(R.id.cb_othe));
		cbOthe.setOnCheckedChangeListener(this);
		treatmentPlanModel = AddDentistNotesActivity.visitNotes.dentistPlanModel.treatmentPlan;
		if (treatmentPlanModel.hashVal.containsKey("cd"))
			cbCd.setChecked(treatmentPlanModel.hashVal.get("cd").equals("on"));
		if (treatmentPlanModel.hashVal.containsKey("tw"))
			cbTw.setChecked(treatmentPlanModel.hashVal.get("tw").equals("on"));
		if (treatmentPlanModel.hashVal.containsKey("scal"))
			cbScal.setChecked(treatmentPlanModel.hashVal.get("scal").equals("on"));
		if (treatmentPlanModel.hashVal.containsKey("othe"))
			cbOthe.setChecked(treatmentPlanModel.hashVal.get("othe").equals("on"));
		Boolean isEnabled = Util.isEmptyString(AddDentistNotesActivity.masterPlan.masterPlanType);
		if(!isEnabled){
			treatmentPlanModel.masterPlanType = AddDentistNotesActivity.masterPlan.masterPlanType;
			treatmentPlanModel.hashVal = AddDentistNotesActivity.masterPlan.hashVal;
			findViewById(R.id.cb_cd).setEnabled(false);
			findViewById(R.id.cb_tw).setEnabled(false);
			findViewById(R.id.cb_scal).setEnabled(false);
			findViewById(R.id.cb_othe).setEnabled(false);
			if(AddDentistNotesActivity.masterPlan.hashVal.containsKey("cd")&&AddDentistNotesActivity.masterPlan.hashVal.get("cd").equals("on")){
				findViewById(R.id.cb_cd).setClickable(true);
				findViewById(R.id.txt_cd).setOnClickListener(this);
			}else
				findViewById(R.id.cb_cd).setClickable(false);
			
			if(AddDentistNotesActivity.masterPlan.hashVal.containsKey("tw")&&AddDentistNotesActivity.masterPlan.hashVal.get("tw").equals("on")){
				findViewById(R.id.cb_tw).setClickable(true);
				findViewById(R.id.txt_tw).setOnClickListener(this);
			}else
				findViewById(R.id.cb_tw).setClickable(false);
			
			if(AddDentistNotesActivity.masterPlan.hashVal.containsKey("scal")&&AddDentistNotesActivity.masterPlan.hashVal.get("scal").equals("on")){
				findViewById(R.id.cb_scal).setClickable(true);
				findViewById(R.id.txt_scal).setOnClickListener(this);
			}else
				findViewById(R.id.cb_scal).setClickable(false);
			
			if(AddDentistNotesActivity.masterPlan.hashVal.containsKey("othe")&&AddDentistNotesActivity.masterPlan.hashVal.get("othe").equals("on")){
				findViewById(R.id.cb_othe).setClickable(true);
				findViewById(R.id.txt_othe).setOnClickListener(this);
			}else
				findViewById(R.id.cb_othe).setClickable(false);
		}else{
			findViewById(R.id.txt_cd).setOnClickListener(this);
			findViewById(R.id.txt_tw).setOnClickListener(this);
			findViewById(R.id.txt_scal).setOnClickListener(this);
			findViewById(R.id.txt_othe).setOnClickListener(this);
		}
		Util.enableDisableView(findViewById(R.id.rl_complete_denture),isEnabled);
		findViewById(R.id.edit_other_type).setEnabled(isEnabled);
		findViewById(R.id.edit_note).setEnabled(true);
		Util.enableDisableView(findViewById(R.id.rg_teeth_selection),true);
		Util.enableDisableView(findViewById(R.id.rg_scaling),isEnabled);
		llTeeth = (LinearLayout) findViewById(R.id.ll_tooth);
		rgScale1 = (RadioGroup) findViewById(R.id.rg_scaling_1);
		rgScale1.setOnCheckedChangeListener(this);
		rgScale2 = (RadioGroup) findViewById(R.id.rg_scaling_2);
		rgScale2.setOnCheckedChangeListener(this);
		rgCrown = (RadioGroup) findViewById(R.id.rg_crown);
		rgCrown.setOnCheckedChangeListener(this);
		rgRemLesion = (RadioGroup) findViewById(R.id.rg_oral_rem_lesion);
		rgRemLesion.setOnCheckedChangeListener(this);
//		rgType = (RadioGroup) findViewById(R.id.rg_masterplan_type);
//		rgType.setOnCheckedChangeListener(this);
		
		arrCompDentureSelected = treatmentPlanModel.arrCompDenture;
		listCompleteDenture = (ListView) findViewById(R.id.list_comp_denture);
		listCompleteDenture.addFooterView(getLayoutInflater().inflate(
				R.layout.footer_other, null));
		findViewById(R.id.edit_footer_other).setEnabled(isEnabled);
		adapterCompDenture = new CheckedListAdapter(this,
				R.layout.row_checked_item, arrCompDenture,
				arrCompDentureSelected,isEnabled);
		adapterCompDenture.registerDataSetObserver(new DataSetObserver() {
			@Override
			public void onChanged() {
				createTableCompDenture((TableLayout) findViewById(R.id.table_comp_denture));
				super.onChanged();
			}
		});
		listCompleteDenture.setAdapter(adapterCompDenture);
		arrTeeth = AddDentistNotesActivity.visitNotes.dentistExaminationModel.oralExamination.hardTissue.arrTeeth;
		// selectedTeeth = arrTeeth.get(0);
		cbRestoration = (CheckBox) findViewById(R.id.cb_restoration);
		cbScaling = (CheckBox) findViewById(R.id.cb_scaling);
		cbCrown = (CheckBox) findViewById(R.id.cb_crown);
		cbPeridontal = (CheckBox) findViewById(R.id.cb_periodontal_surgery);
		cbOralMaxilloSurgery = (CheckBox) findViewById(R.id.cb_oral_maxillo_surgery);
		((CheckBox) findViewById(R.id.cb_mark_others_task))
				.setOnCheckedChangeListener(this);
		btnSubmit = (Button) findViewById(R.id.btn_submit);
		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				finish();
			}
		});
		final TeethView view = (TeethView) findViewById(R.id.toothimage);
		for (int i = 1; i < 5; i++) {
			for (int j = 1; j < 9; j++) {
				final String s = "tooth" + i + "" + j + "_a";
				final TeethModel teeth = arrTeeth.get((j - 1) + ((i - 1) * 8));
				teeth.arrTeethState = AddDentistNotesActivity.visitNotes.dentistExaminationModel.oralExamination.hardTissue.arrTeeth
						.get((j - 1) + ((i - 1) * 8)).arrTeethState;
				final ImageView img = (ImageView) view.findViewWithTag("tooth"
						+ i + "" + j);
				TeethModel teeth1;
				if (teeth.name.equals("tooth11")) {
					teeth1 = arrTeeth.get(32);
				} else if (teeth.name.equals("tooth12")) {
					teeth1 = arrTeeth.get(33);
				} else if (teeth.name.equals("tooth13")) {
					teeth1 = arrTeeth.get(34);
				} else if (teeth.name.equals("tooth16")) {
					teeth1 = arrTeeth.get(35);
				} else if (teeth.name.equals("tooth17")) {
					teeth1 = arrTeeth.get(36);
				} else if (teeth.name.equals("tooth21")) {
					teeth1 = arrTeeth.get(37);
				} else if (teeth.name.equals("tooth22")) {
					teeth1 = arrTeeth.get(38);
				} else if (teeth.name.equals("tooth23")) {
					teeth1 = arrTeeth.get(39);
				} else if (teeth.name.equals("tooth26")) {
					teeth1 = arrTeeth.get(40);
				} else if (teeth.name.equals("tooth27")) {
					teeth1 = arrTeeth.get(41);
				} else if (teeth.name.equals("tooth31")) {
					teeth1 = arrTeeth.get(42);
				} else if (teeth.name.equals("tooth32")) {
					teeth1 = arrTeeth.get(43);
				} else if (teeth.name.equals("tooth33")) {
					teeth1 = arrTeeth.get(44);
				} else if (teeth.name.equals("tooth36")) {
					teeth1 = arrTeeth.get(45);
				} else if (teeth.name.equals("tooth37")) {
					teeth1 = arrTeeth.get(46);
				} else if (teeth.name.equals("tooth41")) {
					teeth1 = arrTeeth.get(47);
				} else if (teeth.name.equals("tooth42")) {
					teeth1 = arrTeeth.get(48);
				} else if (teeth.name.equals("tooth43")) {
					teeth1 = arrTeeth.get(49);
				} else if (teeth.name.equals("tooth46")) {
					teeth1 = arrTeeth.get(50);
				} else if (teeth.name.equals("tooth47")) {
					teeth1 = arrTeeth.get(51);
				} else {
					teeth1 = teeth;
				}
				if (teeth.arrTeethState.contains("Missing")
						|| teeth1.arrTeethState.contains("Missing")) {
					img.setImageResource(getResources().getIdentifier(
							"m_" + i + "" + j, "drawable", getPackageName()));
				} else if (teeth.arrTeethState.contains("Root Stump")
						|| teeth1.arrTeethState.contains("Root Stump")) {
					img.setImageResource(getResources().getIdentifier(
							"rs_" + i + "" + j, "drawable", getPackageName()));
				} else if (teeth.arrTeethState.contains("Restoration")
						|| teeth.arrTeethState.contains("Restoration")) {
					img.setImageResource(getResources().getIdentifier(s,
							"drawable", getPackageName()));
				}
				if ((teeth.arrTeethState.size() > 0)
						|| (arrTeeth.get(getAlternateToothPos(teeth)).arrTeethState
								.size() > 0)) {
					img.setBackgroundColor(Color.parseColor("#77EA2B7F"));

					img.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(final View v) {
							if (selectedTeeth != null) {
								selectedTeeth.other = ((EditText) findViewById(R.id.edit_other))
										.getText().toString();
								selectedTeeth.restOther = ((EditText) findViewById(R.id.edit_restoration_other))
										.getText().toString();
								selectedTeeth.periOther = ((EditText) findViewById(R.id.edit_peridontal))
										.getText().toString();
							}
							// tableTooth();
							function3(teeth, img);
							for (int i = 1; i < 5; i++) {
								for (int j = 1; j < 9; j++) {
									final TeethModel teeth1 = arrTeeth
											.get((j - 1) + ((i - 1) * 8));
									final ImageView img1 = (ImageView) view
											.findViewWithTag("tooth" + i + ""
													+ j);
									img1.setBackgroundColor(Color
											.parseColor("#ffffff"));
									// if (teeth1.arrTeethTreatmentPlan.size() >
									// 0)
									// {
									// img1.setAlpha(1f);
									// } else {
									// img1.setAlpha(0.7f);
									// }
									if ((teeth1.arrTeethState.size() > 0)) {
										img1.setBackgroundColor(Color
												.parseColor("#77EA2B7F"));
									} else {
										img1.setBackgroundColor(Color
												.parseColor("#FFFFFF"));
									}
								}
							}
							for (int i = 5; i < 9; i++) {
								for (int j = 1; j < 6; j++) {
									final TeethModel teeth1 = arrTeeth
											.get(((32 + j) - 1) + ((i - 5) * 5));
									teeth1.arrTeethState = AddDentistNotesActivity.visitNotes.dentistExaminationModel.oralExamination.hardTissue.arrTeeth
											.get(((32 + j) - 1) + ((i - 5) * 5)).arrTeethState;
									ImageView img1;
									if (j < 4) {
										img1 = (ImageView) view
												.findViewWithTag("tooth"
														+ (i - 4) + "" + j);
									} else {
										img1 = (ImageView) view
												.findViewWithTag("tooth"
														+ (i - 4) + ""
														+ (j + 2));
									}
									// img1.setBackgroundColor(Color
									// .parseColor("#ffffff"));
									if ((teeth1.arrTeethState.size() > 0)) {
										img1.setBackgroundColor(Color
												.parseColor("#77EA2B7F"));
									}
								}
							}
							img.setBackgroundColor(Color
									.parseColor("#77aabbcc"));

						}
					});
				} else {
					img.setBackgroundColor(Color.parseColor("#FFFFFF"));
				}
			}
		}

		for (int i = 5; i < 9; i++) {
			for (int j = 1; j < 6; j++) {
				final TeethModel teeth1 = arrTeeth.get(((32 + j) - 1)
						+ ((i - 5) * 5));
				ImageView img1;
				if (j < 4) {
					img1 = (ImageView) view.findViewWithTag("tooth" + (i - 4)
							+ "" + j);
				} else {
					img1 = (ImageView) view.findViewWithTag("tooth" + (i - 4)
							+ "" + (j + 2));
				}
				// img1.setBackgroundColor(Color.parseColor("#ffffff"));
				if ((teeth1.arrTeethState.size() > 0)) {
					img1.setBackgroundColor(Color.parseColor("#77EA2B7F"));

				}
			}
		}
		if(Util.isEmptyString(treatmentPlanModel.masterPlanType))
		treatmentPlanModel.masterPlanType = "Tooth Wise";
		if (Util.isEmptyString(treatmentPlanModel.masterPlanType)) {
			showPlanSelectDialog();
		} else {
			if (treatmentPlanModel.masterPlanType.equals("Complete Denture")) {
				findViewById(R.id.ll_comp_denture).setVisibility(View.VISIBLE);
			} else {
				findViewById(R.id.ll_comp_denture).setVisibility(View.GONE);
			}
			if (treatmentPlanModel.masterPlanType.equals("Tooth Wise")) {
				findViewById(R.id.rl_complete_denture).setVisibility(View.VISIBLE);
				findViewById(R.id.toothimage).setVisibility(View.VISIBLE);
			} else {
				findViewById(R.id.rl_complete_denture).setVisibility(View.GONE);
				findViewById(R.id.toothimage).setVisibility(View.GONE);
			}
			if (treatmentPlanModel.masterPlanType.equals("Scaling")) {
				findViewById(R.id.ll_scaling_type).setVisibility(View.VISIBLE);
				treatmentPlanModel.masterPlanType = "Scaling";
			} else {
				findViewById(R.id.ll_scaling_type).setVisibility(View.GONE);
			}
			if (treatmentPlanModel.masterPlanType.equals("Other")) {
				findViewById(R.id.ll_other_type).setVisibility(View.VISIBLE);
				treatmentPlanModel.masterPlanType = "Other";
			} else {
				findViewById(R.id.ll_other_type).setVisibility(View.GONE);
			}

		}
		function1((ViewGroup) findViewById(R.id.ll_dentist_treatment_plan));
		// function2((ViewGroup) findViewById(R.id.ll_dentist_treatment_plan));
		cbRestoration.setOnCheckedChangeListener(this);
		cbScaling.setOnCheckedChangeListener(this);
		cbCrown.setOnCheckedChangeListener(this);
		cbPeridontal.setOnCheckedChangeListener(this);
		cbOralMaxilloSurgery.setOnCheckedChangeListener(this);
		tableTooth();
		AutoSave();
		scaleType();
		listCompleteDenture.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(final View v, final MotionEvent event) {
				createTableCompDenture((TableLayout) findViewById(R.id.table_comp_denture));
				return false;
			}
		});
		if (treatmentPlanModel.arrCompDenture.size() > 0) {
			createTableCompDenture((TableLayout) findViewById(R.id.table_comp_denture));
		}
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {

		case android.R.id.home:
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onPause() {
		if (selectedTeeth != null) {
			selectedTeeth.other = ((EditText) findViewById(R.id.edit_other))
					.getText().toString();
			selectedTeeth.restOther = ((EditText) findViewById(R.id.edit_restoration_other))
					.getText().toString();
			selectedTeeth.periOther = ((EditText) findViewById(R.id.edit_peridontal))
					.getText().toString();
		}
		super.onPause();
	}

	private void scaleType() {
		final RadioGroup rg = (RadioGroup) findViewById(R.id.rg_scaling);
		if (!Util.isEmptyString(treatmentPlanModel.scal.get("value"))) {
			((RadioButton) rg.findViewWithTag(treatmentPlanModel.scal.get("value")
					.replace(" Scaling", ""))).setChecked(true);
			findViewById(R.id.table_scale_type).setVisibility(View.VISIBLE);
			findViewById(R.id.txt_scaling_type).setVisibility(View.VISIBLE);
			createTableScaling((TableLayout) findViewById(R.id.table_scale_type));
		}
		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(final RadioGroup group,
					final int checkedId) {
				findViewById(R.id.table_scale_type).setVisibility(View.VISIBLE);
				findViewById(R.id.txt_scaling_type).setVisibility(View.VISIBLE);
				final RadioButton cb = (RadioButton) group
						.findViewById(checkedId);
				treatmentPlanModel.scal.put("value", cb.getTag() + " Scaling");
				treatmentPlanModel.scal.put("status", "pending");
				createTableScaling((TableLayout) findViewById(R.id.table_scale_type));
			}
		});
	}

	private void selectTaskDialog() {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_select_task);
		final DisplayMetrics metrics = getResources().getDisplayMetrics();
		final int width = metrics.widthPixels;
		dialog.getWindow().setLayout((6 * width) / 7,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		createTable((TableLayout) dialog.findViewById(R.id.table));
		dialog.show();
	}

	private void showPlanSelectDialog() {
		final Dialog dialogSelect = new Dialog(this);
		dialogSelect.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogSelect.setContentView(R.layout.dialog_master_plan_type);
		dialogSelect.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		final RadioGroup rgTypeDialog = (RadioGroup) dialogSelect
				.findViewById(R.id.rg_masterplan_type);
		rgTypeDialog
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(final RadioGroup group,
							final int checkedId) {
						final RadioButton cb = (RadioButton) group
								.findViewById(checkedId);
//						if (cb.getText().equals("Complete Denture")) {
//							rgType.check(rgType.getChildAt(0).getId());
//							findViewById(R.id.ll_comp_denture).setVisibility(
//									View.VISIBLE);
//							treatmentPlanModel.masterPlanType = "Complete Denture";
//						} else {
//							findViewById(R.id.ll_comp_denture).setVisibility(
//									View.GONE);
//						}
//						if (cb.getText().equals("Tooth Wise")) {
//							rgType.check(rgType.getChildAt(1).getId());
//							findViewById(R.id.rl_complete_denture)
//									.setVisibility(View.VISIBLE);
//							findViewById(R.id.toothimage).setVisibility(
//									View.VISIBLE);
//							treatmentPlanModel.masterPlanType = "Tooth Wise";
//						} else {
//							findViewById(R.id.rl_complete_denture)
//									.setVisibility(View.GONE);
//							findViewById(R.id.toothimage).setVisibility(
//									View.GONE);
//						}
//						if (cb.getText().equals("Scaling")) {
//							rgType.check(rgType.getChildAt(2).getId());
//							findViewById(R.id.ll_scaling_type).setVisibility(
//									View.VISIBLE);
//							treatmentPlanModel.masterPlanType = "Scaling";
//						} else {
//							findViewById(R.id.ll_scaling_type).setVisibility(
//									View.GONE);
//						}
//						if (cb.getText().equals("Other")) {
//							rgType.check(rgType.getChildAt(3).getId());
//							findViewById(R.id.ll_other_type).setVisibility(
//									View.VISIBLE);
//							treatmentPlanModel.masterPlanType = "Other";
//						} else {
//							findViewById(R.id.ll_other_type).setVisibility(
//									View.GONE);
//						}
//						dialogSelect.dismiss();
					}
				});

		dialogSelect.show();
	}

	public void tableTooth() {
		final TextView tv = new TextView(this);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
		tv.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		tv.setBackgroundResource(R.drawable.btn_login);
		tv.setTextColor(Color.WHITE);
		tv.setGravity(Gravity.CENTER);
		tv.setPadding(10, 10, 10, 10);
		tv.setText("Select Task for this appointment");
		llTeeth.addView(tv);
		tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				selectTaskDialog();
			}
		});
	}
	
	private void hideAllViews() {
		findViewById(R.id.ll_comp_denture).setVisibility(View.GONE);
		findViewById(R.id.rl_complete_denture).setVisibility(View.GONE);
		findViewById(R.id.toothimage).setVisibility(View.GONE);
		findViewById(R.id.ll_scaling_type).setVisibility(View.GONE);
		findViewById(R.id.ll_other_type).setVisibility(View.GONE);
	}
	
	@Override
	public void onClick(View v) {
		hideAllViews();
		switch (v.getId()) {
		case R.id.txt_cd:
			cbCd.setChecked(true);
			findViewById(R.id.ll_comp_denture).setVisibility(View.VISIBLE);
			treatmentPlanModel.masterPlanType = "Complete Denture";
			treatmentPlanModel.hashVal.put("cd", "on");
			break;

		case R.id.txt_tw:
			cbTw.setChecked(true);
			treatmentPlanModel.masterPlanType = "Tooth Wise";
			findViewById(R.id.rl_complete_denture).setVisibility(View.VISIBLE);
			findViewById(R.id.toothimage).setVisibility(View.VISIBLE);
			treatmentPlanModel.hashVal.put("tw", "on");
			break;

		case R.id.txt_scal:
			cbScal.setChecked(true);
			findViewById(R.id.ll_scaling_type).setVisibility(View.VISIBLE);
			treatmentPlanModel.masterPlanType = "Scaling";
			treatmentPlanModel.hashVal.put("scal", "on");
			break;

		case R.id.txt_othe:
			cbOthe.setChecked(true);
			findViewById(R.id.ll_other_type).setVisibility(View.VISIBLE);
			treatmentPlanModel.masterPlanType = "Other";
			treatmentPlanModel.hashVal.put("othe", "on");
			break;

		default:
			break;
		}

	}
}

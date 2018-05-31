package com.ezhealthtrack.DentistSoap.Model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.DentistSoap.AddDentistNotesActivity;
import com.ezhealthtrack.DentistSoap.Model.TreatmentPlanModel.ObjValStat;
import com.ezhealthtrack.util.Constants;
import com.ezhealthtrack.util.Util;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

//import android.util.Log;

public class MasterPlanModel {

	public JSONObject jsonData = new JSONObject();

	public ArrayList<TeethModel> arrTeeth = new ArrayList<TeethModel>();
	public ArrayList<String> arrCompDenture = new ArrayList<String>();
	public String masterPlanType = "";
	public HashMap<String, String> othe = new HashMap<String, String>();
	public HashMap<String, String> scal = new HashMap<String, String>();
	public HashMap<String, String> cd = new HashMap<String, String>();
	public HashMap<String, String> note = new HashMap<String, String>();

	public String[] arrayKey = new String[] { "othe", "is", "impl", "debr",
			"crow", "aspi", "sutu", "rct", "rpd", "extr", "oms", "brid",
			"rest", "fcd", "rcd", "scal", "ps", "gic", "comp", "ostec", "uf",
			"dms", "adf", "pf", "oste", "gingi", "mwf", "dpf", "ging", "ppf",
			"pi", "bm", "si", "jr", "ti", "dd" };
	public String[] arrayValue = new String[] { "othe", "impaction surgery",
			"implants", "debridement", "crown", "aspiration", "suturing",
			"root canal treatment", "removable partial denture", "extraction",
			"oral and maxillofacial surgery", "bridge", "restoration",
			"fixed complete denture", "removable complete denture", "scaling",
			"periodontal surgery", "glass ionomer cement", "composite",
			"ostectomy", "undisplaced flap", "distal molar surgery",
			"apically displaced flap", "palatal flap", "osteoplasty",
			"gingivectomy", "modified widman flap", "double papilla flap",
			"gingivoplasty", "papilla preservation flap", "Primary impression",
			"Border moulding", "Secondary impression", "Jaw relation",
			"Try-in", "Denture delivered" };
	private final HashMap<String, String> keyValue = new HashMap<String, String>();
	private final HashMap<String, String> valueKey = new HashMap<String, String>();
	public HashMap<String, String> hashVal = new HashMap<String, String>();
	public HashMap<String, ObjValStat> hashCd = new HashMap<String, ObjValStat>();
	public String mp_ep_id = "0";

	public MasterPlanModel() {
		if (arrTeeth.size() == 0) {
			for (int i = 1; i < 5; i++) {
				for (int j = 1; j < 9; j++) {
					final TeethModel teeth = new TeethModel();
					teeth.name = "tooth" + i + "" + j;
					arrTeeth.add(teeth);
				}
			}
			for (int i = 5; i < 9; i++) {
				for (int j = 1; j < 6; j++) {
					final TeethModel teeth = new TeethModel();
					teeth.name = "tooth" + i + "" + j;
					arrTeeth.add(teeth);
				}
			}
		}

		for (int i = 0; i < arrayKey.length; i++) {
			keyValue.put(arrayKey[i], arrayValue[i]);
			valueKey.put(arrayValue[i], arrayKey[i]);
		}
	}

	private JSONObject getBlankJson(final Context context) {
		JSONObject jObj;
		try {
			InputStream fis;
			fis = context.getAssets().open("mastdatablank.txt");
			final StringBuffer fileContent = new StringBuffer("");
			final byte[] buffer = new byte[1024];
			while (fis.read(buffer) != -1) {
				fileContent.append(new String(buffer));
			}
			final String s = String.valueOf(fileContent);
			jObj = new JSONObject(s);
			// Log.i("", appointmentModel.epId);
			return jObj.getJSONObject("mast");
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void JsonParse(final JSONObject tp) {
		try {
			if (tp.get("type") instanceof JSONObject) {
				if (tp.has("type")) {
					if (tp.getJSONObject("type").has("val")) {
						@SuppressWarnings("unchecked")
						final Iterator<String> iterVal = tp
								.getJSONObject("type").getJSONObject("val")
								.keys();
						while (iterVal.hasNext()) {
							final String key = iterVal.next();
							try {
								String value = "";
								if (tp.getJSONObject("type").get("val") instanceof JSONObject)
									value = tp.getJSONObject("type")
											.getJSONObject("val")
											.getString(key);
								if (value.equals("on")) {
									masterPlanType = "Tooth Wise";
								}
								hashVal.put(key, value);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					if (tp.getJSONObject("type").has("cd")
							&& tp.getJSONObject("type").get("cd") instanceof JSONObject) {
						@SuppressWarnings("unchecked")
						final Iterator<String> iterCd = tp
								.getJSONObject("type").getJSONObject("cd")
								.keys();
						arrCompDenture.clear();
						while (iterCd.hasNext()) {
							final String key = iterCd.next();
							try {
								if (key.equals("task")) {
								} else {
									if (tp.get("type") instanceof JSONObject
											&& tp.getJSONObject("type")
													.getJSONObject("cd")
													.getString(key)
													.equals("on")) {
										arrCompDenture.add(keyValue.get(key));
									}
									cd.put(key, tp.getJSONObject("type")
											.getJSONObject("cd").getString(key));
									try {
										if (tp.getJSONObject("type")
												.getJSONObject("cd")
												.getJSONObject("task")
												.getJSONObject("cd-" + key)
												.getString("status")
												.equals("start")) {
											if (!key.equals("othe")) {
												cd.put(keyValue.get(key),
														"in progress");
											} else {
												final ObjValStat objOthe = new ObjValStat();
												objOthe.value = cd.get("othe");
												objOthe.status = "in progress";
											}
										} else {
											if (!key.equals("othe")) {
												cd.put(keyValue.get(key),
														tp.getJSONObject("type")
																.getJSONObject(
																		"cd")
																.getJSONObject(
																		"task")
																.getJSONObject(
																		"cd-"
																				+ key)
																.getString(
																		"status"));
											} else {
												final ObjValStat objOthe = new ObjValStat();
												objOthe.value = cd.get("othe");
												objOthe.status = tp
														.getJSONObject("type")
														.getJSONObject("cd")
														.getJSONObject("task")
														.getJSONObject(
																"cd-othe" + key)
														.getString("status");
												hashCd.put("othe", objOthe);

											}

										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					}
					final ObjValStat objOthe = new ObjValStat();
					if (tp.getJSONObject("type").getJSONObject("othe")
							.has("value"))
						objOthe.value = tp.getJSONObject("type")
								.getJSONObject("othe").getString("value");
					else
						objOthe.value = "";
					othe.put("othe", objOthe.value);
					// Log.i("othe", othe.toString());

					final ObjValStat objScal = new ObjValStat();
					if (tp.getJSONObject("type").has("scal"))
						objScal.value = tp.getJSONObject("type")
								.getJSONObject("scal").getString("value");
					else
						objScal.value = "";
					if (objScal.value.equals("comp")) {
						scal.put("value", "Complete Scaling");
					} else {
						scal.put("value", "Partial Scaling");
					}
					try {
						String s = tp.getJSONObject("type")
								.getJSONObject("scal").getJSONObject("task")
								.getString("status");
						if (s.equals("start"))
							s = "in progress";
						scal.put("status", s);
					} catch (Exception e) {
						e.printStackTrace();
					}
					try{
						note.put("note",
								tp.getJSONObject("type").getJSONObject("tw")
										.getJSONObject("note").getString("value"));
					}catch(Exception e){
						
					}
					if (tp.getJSONObject("type").has("tw")
							&& tp.getJSONObject("type").getJSONObject("tw")
									.has("tooth"))
						jsonParseTeeth(tp.getJSONObject("type")
								.getJSONObject("tw").getJSONObject("tooth"));
					if (tp.getJSONObject("type").has("tw")
							&& tp.getJSONObject("type").getJSONObject("tw")
									.has("task"))
						jsonParseTaskTeeth(tp.getJSONObject("type")
								.getJSONObject("tw").getJSONObject("task"));
				}
			}

		} catch (final JSONException e1) {
			e1.printStackTrace();
		}
	}

	private void jsonParseTeeth(final JSONObject jsonTooth) {
		Iterator<String> iter;
		try {
			iter = jsonTooth.keys();
			while (iter.hasNext()) {
				final String key = iter.next();
				try {
					for (final TeethModel teeth1 : arrTeeth) {
						if (teeth1.name.equalsIgnoreCase("tooth" + key)) {
							teeth1.arrTeethTreatmentPlan.clear();
							final Iterator<String> iter1 = jsonTooth
									.getJSONObject(key).keys();
							while (iter1.hasNext()) {
								final String key1 = iter1.next();
								String value1;
								if (key1.equals("othe")) {
									value1 = jsonTooth.getJSONObject(key)
											.getString(key1);
									teeth1.other = value1;
									if (!Util.isEmptyString(value1)) {
										teeth1.arrTeethTreatmentPlan
												.add(value1);
									}
								} else {
									value1 = jsonTooth.getJSONObject(key)
											.getString(key1);
									if (value1.equals("on")) {
										if (!Util.isEmptyString(keyValue
												.get(key1))) {
											teeth1.arrTeethTreatmentPlan
													.add(keyValue.get(key1));
										}
									}
								}

								if (key1.equals("crow")) {
									String t = "";
									if (jsonTooth.getJSONObject(key)
											.getJSONObject(key1).has("t"))
										t = jsonTooth.getJSONObject(key)
												.getJSONObject(key1)
												.getJSONObject("t")
												.getString("value");
									if (t.equals("am")) {
										teeth1.crown = "all metal";
									} else if (t.equals("mc")) {
										teeth1.crown = "metal-ceramic";
									} else if (t.equals("ac")) {
										teeth1.crown = "all ceramic";
									} else if (t.equals("othe")) {
										teeth1.crown = "others";
									}
									if (jsonTooth.getJSONObject(key)
											.getJSONObject(key1).has("cb")
											&& jsonTooth.getJSONObject(key)
													.getJSONObject(key1)
													.getString("cb")
													.equals("on")) {
										teeth1.arrTeethTreatmentPlan
												.add(keyValue.get(key1));
									}
								}

								if (key1.equals("scal")) {
									String t1 = "";
									if (jsonTooth.getJSONObject(key)
											.getJSONObject(key1).has("t1"))
										t1 = jsonTooth.getJSONObject(key)
												.getJSONObject(key1)
												.getJSONObject("t1")
												.getString("value");
									String t2 = "";
									if (jsonTooth.getJSONObject(key)
											.getJSONObject(key1).has("t2"))
										t2 = jsonTooth.getJSONObject(key)
												.getJSONObject(key1)
												.getJSONObject("t2")
												.getString("value");
									if (t1.equals("part")) {
										teeth1.scaling1 = "partial";
									} else if (t1.equals("comp")) {
										teeth1.scaling1 = "complete";
									}
									if (t2.equals("loca")) {
										teeth1.scaling2 = "localized";
									} else {
										teeth1.scaling2 = "generalized";
									}
									if (jsonTooth.getJSONObject(key)
											.getJSONObject(key1).has("cb")
											&& jsonTooth.getJSONObject(key)
													.getJSONObject(key1)
													.getString("cb")
													.equals("on")) {
										teeth1.arrTeethTreatmentPlan
												.add(keyValue.get(key1));
									}
								}

								if (key1.equals("oms")) {
									String rl = "";
									if (jsonTooth.getJSONObject(key)
											.getJSONObject(key1).has("rl")
											&& jsonTooth.getJSONObject(key)
													.getJSONObject(key1)
													.getJSONObject("rl")
													.has("value"))
										rl = jsonTooth.getJSONObject(key)
												.getJSONObject(key1)
												.getJSONObject("rl")
												.getString("value");
									if (rl.equals("part")) {
										teeth1.remLesion = "partial";
									} else if (rl.equals("comp")) {
										teeth1.remLesion = "complete";
									}
									if (jsonTooth.getJSONObject(key)
											.getJSONObject(key1).has("cb")
											&& jsonTooth.getJSONObject(key)
													.getJSONObject(key1)
													.getString("cb")
													.equals("on")) {
										teeth1.arrTeethTreatmentPlan
												.add(keyValue.get(key1));
									}
								}

								if (key1.equals("rest")) {
									String gic = "";
									if (jsonTooth.getJSONObject(key)
											.getJSONObject(key1).has("gic"))
										gic = jsonTooth.getJSONObject(key)
												.getJSONObject(key1)
												.getString("gic");
									if (gic.equals("on")) {
										teeth1.arrTeethTreatmentPlan
												.add(keyValue.get("gic"));
									}

									String comp = "";
									if (jsonTooth.getJSONObject(key)
											.getJSONObject(key1).has("comp"))
										comp = jsonTooth.getJSONObject(key)
												.getJSONObject(key1)
												.getString("comp");
									if (comp.equals("on")) {
										teeth1.arrTeethTreatmentPlan
												.add(keyValue.get("comp"));
									}

									teeth1.restOther = jsonTooth
											.getJSONObject(key)
											.getJSONObject(key1)
											.getString("othe");
									if (jsonTooth.getJSONObject(key)
											.getJSONObject(key1).has("cb")
											&& jsonTooth.getJSONObject(key)
													.getJSONObject(key1)
													.getString("cb")
													.equals("on")) {
										teeth1.arrTeethTreatmentPlan
												.add(keyValue.get(key1));
									}

								}

								if (key1.equals("ps")) {
									final Iterator<String> iterPs = jsonTooth
											.getJSONObject(key)
											.getJSONObject(key1).keys();
									while (iterPs.hasNext()) {
										final String key2 = iterPs.next();
										if (!key2.equals("cb")
												&& !key2.equals("status")
												&& !key2.equals("othe")) {
											String value2;
											value2 = jsonTooth
													.getJSONObject(key)
													.getJSONObject(key1)
													.getString(key2);
											if (value2.equals("on")) {
												if (!Util
														.isEmptyString(keyValue
																.get(key2))) {
													teeth1.arrTeethTreatmentPlan
															.add(keyValue
																	.get(key2));
												}
											}
										}
									}
									teeth1.periOther = jsonTooth
											.getJSONObject(key)
											.getJSONObject(key1)
											.getString("othe");
									if (jsonTooth.getJSONObject(key)
											.getJSONObject(key1).has("cb")
											&& jsonTooth.getJSONObject(key)
													.getJSONObject(key1)
													.getString("cb")
													.equals("on")) {
										teeth1.arrTeethTreatmentPlan
												.add(keyValue.get(key1));
									}

								}

							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (final Exception e1) {
			e1.printStackTrace();
		}

	}

	private void jsonParseTaskTeeth(final JSONObject jsonTask) {
		Iterator<String> iter;
		try {
			iter = jsonTask.keys();
			while (iter.hasNext()) {
				final String key = iter.next();
				for (final TeethModel teeth1 : arrTeeth) {
					if (teeth1.name.equalsIgnoreCase("tooth" + key)) {
						teeth1.hashTask.clear();
						final Iterator<String> iter1 = jsonTask.getJSONObject(
								key).keys();
						while (iter1.hasNext()) {
							final String key1 = iter1.next();
							Task task = new Gson().fromJson(jsonTask
									.getJSONObject(key).getString(key1),
									Task.class);
							if (keyValue.containsKey(key1)
									&& !key1.contains("othe")) {
								teeth1.hashTask.put(keyValue.get(key1), task);

							} else if (key1.contains("ps")) {
								if (key1.contains("othe"))
									teeth1.hashTask.put(
											"periodontal surgery - " + "othe",
											task);
								else
									teeth1.hashTask
											.put("periodontal surgery - "
													+ keyValue
															.get(key1.replace(
																	"ps-", "")),
													task);
							} else if (key1.contains("oms")) {
								teeth1.hashTask.put(
										"oral and maxillofacial surgery - "
												+ keyValue.get(key1.replace(
														"oms-", "")), task);
							} else if (key1.contains("rest")) {
								if (key1.contains("othe"))
									teeth1.hashTask.put("restoration - "
											+ "othe", task);
								else
									teeth1.hashTask.put(
											"restoration - "
													+ keyValue.get(key1
															.replace("rest-",
																	"")), task);
							}else {
								teeth1.hashTask.put(key1, task);
							}
						}
					}
					Log.i("taskteeth", new Gson().toJson(teeth1.hashTask.toString()));
				}
			}
		} catch (final JSONException e1) {
			e1.printStackTrace();
		}
	}

	private void jsonUpdateTeeth(final JSONObject jsonTooth) {
		Iterator<String> iter;
		try {
			iter = jsonTooth.keys();
			while (iter.hasNext()) {
				final String key = iter.next();
				Log.i(key, "ss");
				for (final TeethModel teeth1 : arrTeeth) {
					if (teeth1.name.equalsIgnoreCase("tooth" + key)) {
						for (final String s : teeth1.arrTeethTreatmentPlan) {
							if (jsonTooth.getJSONObject(key).has(
									valueKey.get(s))) {
								if (!s.equals(keyValue.get("rest"))
										&& !s.equals("periodontal surgery")
										&& !s.equals(keyValue.get("scal"))
										&& !s.equals(keyValue.get("crow"))
										&& !s.equals(keyValue.get("oms"))) {
									jsonTooth.getJSONObject(key).put(
											valueKey.get(s), "on");
								} else {
									jsonTooth.getJSONObject(key)
											.getJSONObject(valueKey.get(s))
											.put("cb", "on");

								}
							}
							if (jsonTooth.getJSONObject(key)
									.getJSONObject("ps").has(valueKey.get(s))) {
								jsonTooth.getJSONObject(key)
										.getJSONObject("ps")
										.put(valueKey.get(s), "on");

							}

							if (jsonTooth.getJSONObject(key)
									.getJSONObject("rest").has(valueKey.get(s))) {
								jsonTooth.getJSONObject(key)
										.getJSONObject("rest")
										.put(valueKey.get(s), "on");
							}

							if (s.equals(keyValue.get("crow"))) {
								if (teeth1.crown.equals("all metal")) {
									jsonTooth.getJSONObject(key)
											.getJSONObject("crow")
											.getJSONObject("t")
											.put("value", "am");
								} else if (teeth1.crown.equals("metal-ceramic")) {
									jsonTooth.getJSONObject(key)
											.getJSONObject("crow")
											.getJSONObject("t")
											.put("value", "mc");
								} else if (teeth1.crown.equals("all ceramic")) {
									jsonTooth.getJSONObject(key)
											.getJSONObject("crow")
											.getJSONObject("t")
											.put("value", "ac");
								} else if (teeth1.crown.equals("others")) {
									jsonTooth.getJSONObject(key)
											.getJSONObject("crow")
											.getJSONObject("t")
											.put("value", "othe");
								}
							}

							if (s.equals(keyValue.get("scal"))) {
								if (teeth1.scaling1.equals("partial")) {
									jsonTooth.getJSONObject(key)
											.getJSONObject("scal")
											.getJSONObject("t1")
											.put("value", "part");
								} else if (teeth1.scaling1.equals("complete")) {
									jsonTooth.getJSONObject(key)
											.getJSONObject("scal")
											.getJSONObject("t1")
											.put("value", "comp");
								}
								if (teeth1.scaling2.equals("localized")) {
									jsonTooth.getJSONObject(key)
											.getJSONObject("scal")
											.getJSONObject("t2")
											.put("value", "loca");
								} else if (teeth1.scaling2
										.equals("generalized")) {
									jsonTooth.getJSONObject(key)
											.getJSONObject("scal")
											.getJSONObject("t2")
											.put("value", "gene");
								}
							}

							if (s.equals(keyValue.get("oms"))) {
								if (teeth1.scaling1.equals("partial")) {
									jsonTooth.getJSONObject(key)
											.getJSONObject("oms")
											.getJSONObject("rl")
											.put("value", "part");
								} else if (teeth1.scaling1.equals("complete")) {
									jsonTooth.getJSONObject(key)
											.getJSONObject("oms")
											.getJSONObject("rl")
											.put("value", "comp");
								}
							}
						}
						jsonTooth.getJSONObject(key).getJSONObject("ps")
								.put("othe", teeth1.periOther);
						jsonTooth.getJSONObject(key).getJSONObject("rest")
								.put("othe", teeth1.restOther);
						jsonTooth.getJSONObject(key).put("othe", teeth1.other);
					}
					// Log.i("masterplan",
					// teeth1.arrTeethTreatmentPlan.toString());
				}
			}
		} catch (final JSONException e1) {
			e1.printStackTrace();
		}

	}

	public void updateJson(final JSONObject json, final Context context) {
		try {
			final JSONObject mast = getBlankJson(context);
			json.put("context_id", "" + mp_ep_id);
			json.put(
					"created_by",
					""
							+ EzApp.sharedPref.getString(
									Constants.USER_ID, ""));
			json.put("content_type", "episode");
			final JSONObject jsonValue = mast.getJSONObject("type")
					.getJSONObject("val");
			for (final Entry entry : hashVal.entrySet()) {
				jsonValue.put((String) entry.getKey(), entry.getValue());
			}

			final JSONObject jsonCd = mast.getJSONObject("type").getJSONObject(
					"cd");
			final JSONObject jsonCdTask = new JSONObject();
			for (final Entry entry : cd.entrySet()) {
				try{
				if (keyValue.containsKey(entry.getKey())) {
					if (arrCompDenture.contains(keyValue.get(entry.getKey())))
						jsonCd.put((String) entry.getKey(), "on");
					else if (!entry.getKey().equals("othe"))
						jsonCd.put((String) entry.getKey(), "off");
					else if (entry.getKey().equals("othe"))
						jsonCd.put((String) entry.getKey(), entry.getValue());
				} else if (valueKey.containsKey(entry.getKey())) {
					JSONObject jobj = new JSONObject();
					if (entry.getValue().equals("in progress"))
						jobj.put("status", "start");
					else
						jobj.put("status", entry.getValue());
					jobj.put("date",
							EzApp.sdfemmm.format(new Date()));
					jsonCdTask.put("cd-" + valueKey.get(entry.getKey()), jobj);
				}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			JSONObject jobj = new JSONObject();
			if (hashCd.containsKey("othe")) {
				if (hashCd.get("othe").status.equals("in progress"))
					jobj.put("status", "start");
				else
					jobj.put("status", hashCd.get("othe").status);
			}
			jobj.put("date", EzApp.sdfemmm.format(new Date()));
			jsonCdTask.put("cd-othe", jobj);
			jsonCd.put("task", jsonCdTask);
			Log.i("Cd", new Gson().toJson(jsonCd));

			final JSONObject jsonScal = mast.getJSONObject("type")
					.getJSONObject("scal");
			final JSONObject taskJson = new JSONObject();
			if (scal.containsKey("value")) {
				final String valStat = scal.get("value");
				if (valStat.contains("part")) {
					jsonScal.put("value", "part");
				} else {
					jsonScal.put("value", "comp");
				}
			}
			try {
				if (scal.get("status").equals("in progress"))
					taskJson.put("status", "start");
				else
					taskJson.put("status", scal.get("status"));
			} catch (Exception e) {
				Log.e("", e.toString());
			}
			taskJson.put("date", EzApp.sdfemmm.format(new Date()));
			jsonScal.put("task", taskJson);

			final JSONObject jsonOthe = mast.getJSONObject("type")
					.getJSONObject("othe");
			final String valStat1 = othe.get("othe");
			jsonOthe.put("value", valStat1);
			jsonUpdateTeeth(mast.getJSONObject("type").getJSONObject("tw")
					.getJSONObject("tooth"));
			jsonUpdateTaskTeeth(mast.getJSONObject("type").getJSONObject("tw"));
			mast.getJSONObject("type").getJSONObject("tw")
			.getJSONObject("note").put("value", note.get("note"));
			json.put("mast", mast);
			jsonData = json;

		} catch (final JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void jsonUpdateTaskTeeth(JSONObject task) {
		JSONObject jsonTask = new JSONObject();

		for (final TeethModel teeth1 : arrTeeth) {
			JSONObject jsonTeeth = new JSONObject();
			for (Entry<String, Task> entry : teeth1.hashTask.entrySet()) {
				try {
					JSONObject ent = new JSONObject();
					ent.put("status", entry.getValue().getStatus());
					ent.put("date", entry.getValue().getDate());
					if (valueKey.containsKey(entry.getKey()))
						jsonTeeth.put(valueKey.get(entry.getKey()), ent);
					else if (entry.getKey().contains("periodontal surgery - ")) {
						if (valueKey.containsKey(entry.getKey().replace(
								"periodontal surgery - ", "")))
							jsonTeeth
									.put("ps-"
											+ valueKey
													.get(entry
															.getKey()
															.replace(
																	"periodontal surgery - ",
																	"")), ent);
						else
							jsonTeeth.put("ps-othe", ent);
					} else if (entry.getKey().contains("restoration - ")) {
						if (valueKey.containsKey(entry.getKey().replace(
								"restoration - ", "")))
							jsonTeeth.put(
									"rest-"
											+ valueKey.get(entry.getKey()
													.replace("restoration - ",
															"")), ent);
						else
							jsonTeeth.put("rest-othe", ent);
					} else if (entry.getKey().contains(
							"oral and maxillofacial surgery - ")) {
						if (valueKey.containsKey(entry.getKey().replace(
								"oral and maxillofacial surgery - ", "")))
							jsonTeeth
									.put("oms-"
											+ valueKey
													.get(entry
															.getKey()
															.replace(
																	"oral and maxillofacial surgery - ",
																	"")), ent);
						else
							jsonTeeth.put("oms-othe", ent);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					jsonTask.put(teeth1.name.replace("tooth", ""), jsonTeeth);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		Log.i("task", jsonTask.toString());
		try {
			task.put("task", jsonTask);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("tw", task.toString());
	}

}
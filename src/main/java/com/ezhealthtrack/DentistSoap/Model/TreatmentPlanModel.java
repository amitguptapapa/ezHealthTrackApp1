package com.ezhealthtrack.DentistSoap.Model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.ezhealthtrack.R;
import com.ezhealthtrack.util.Util;
import com.google.gson.Gson;

//import android.util.Log;

public class TreatmentPlanModel {
	public static class ObjValStat {
		public String value = "";
		public String status = "";
	}

	public String[] arrayKey = new String[] { "othe", "is", "impl", "debr",
			"crow", "aspi", "sutu", "rct", "rpd", "extr", "oms", "brid",
			"rest", "fcd", "rcd", "scal", "ps", "gic", "comp", "ostec", "uf",
			"dms", "adf", "pf", "oste", "gingi", "mwf", "dpf", "ging", "ppf",
			"pi", "bm", "si", "jr", "ti", "dd" };
	public String[] arrayValue = new String[] { "", "impaction surgery",
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
	public HashMap<String, ObjValStat> hashOthe = new HashMap<String, ObjValStat>();
	public HashMap<String, ObjValStat> hashScal = new HashMap<String, ObjValStat>();
	public HashMap<String, String> hashStatus = new HashMap<String, String>();
	public ArrayList<String> arrCompDenture = new ArrayList<String>();
	public String masterPlanType = "";
	public HashMap<String, String> othe = new HashMap<String, String>();
	public HashMap<String, String> scal = new HashMap<String, String>();
	public HashMap<String, String> cd = new HashMap<String, String>();
	public HashMap<String, String> note = new HashMap<String, String>();

	public TreatmentPlanModel() {
		for (int i = 0; i < arrayKey.length; i++) {
			keyValue.put(arrayKey[i], arrayValue[i]);
			valueKey.put(arrayValue[i], arrayKey[i]);
		}
	}

	private JSONObject getBlankJson(final Context context) {
		JSONObject jObj;
		try {
			InputStream fis;
			fis = context.getAssets().open("dentistTemplateJson.txt");
			final StringBuffer fileContent = new StringBuffer("");
			final byte[] buffer = new byte[1024];
			while (fis.read(buffer) != -1) {
				fileContent.append(new String(buffer));
			}
			final String s = String.valueOf(fileContent);
			jObj = new JSONObject(s);
			// Log.i("", appointmentModel.epId);
			return jObj.getJSONObject("Soap").getJSONObject("plan")
					.getJSONObject("tp");
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void JsonParse(final JSONObject tp,
			final ArrayList<TeethModel> arrTeeth) {
		try {
			if (tp.has("type")) {
				if (tp.getJSONObject("type").has("val")) {
					final Iterator<String> iterVal = tp.getJSONObject("type")
							.getJSONObject("val").keys();
					while (iterVal.hasNext()) {
						final String key = iterVal.next();
						String value = "";
						value = tp.getJSONObject("type").getJSONObject("val")
								.getString(key);
						hashVal.put(key, value);
					}
				}
				try {
					if (tp.getJSONObject("type").has("cd")) {
						final Iterator<String> iterCd = tp
								.getJSONObject("type").getJSONObject("cd")
								.keys();
						arrCompDenture.clear();
						while (iterCd.hasNext()) {
							final ObjValStat obj = new ObjValStat();
							final String key = iterCd.next();
							// Log.i("key", key);
							if (key.equals("othe")) {
								if (tp.getJSONObject("type")
										.getJSONObject("cd").getJSONObject(key)
										.has("value"))
									obj.value = tp.getJSONObject("type")
											.getJSONObject("cd")
											.getJSONObject(key)
											.getString("value");
								cd.put("othe", obj.value);
								if (tp.getJSONObject("type")
										.getJSONObject("cd").getJSONObject(key)
										.has("status"))
									if (tp.getJSONObject("type")
											.getJSONObject("cd")
											.getJSONObject(key)
											.getString("status").equals("on"))
										obj.status = "done";
									else
										obj.status = "pending";
								hashCd.put(key, obj);
							} else {
								if (tp.getJSONObject("type")
										.getJSONObject("cd").getJSONObject(key)
										.has("cb"))
									obj.value = tp.getJSONObject("type")
											.getJSONObject("cd")
											.getJSONObject(key).getString("cb");

								if (tp.getJSONObject("type")
										.getJSONObject("cd").getJSONObject(key)
										.has("status"))
									if (tp.getJSONObject("type")
											.getJSONObject("cd")
											.getJSONObject(key)
											.getString("status").equals("on"))
										obj.status = "done";
									else
										obj.status = "pending";
								cd.put(keyValue.get(key), obj.status);
								hashCd.put(key, obj);
								if (obj.value.equals("on")) {
									arrCompDenture.add(keyValue.get(key));
								}
							}
						}
					}
				} catch (Exception e) {

				}
				try {
					if (tp.getJSONObject("type").has("othe")) {
						final ObjValStat objOthe = new ObjValStat();
						if (tp.getJSONObject("type").getJSONObject("othe")
								.has("value"))
							objOthe.value = tp.getJSONObject("type")
									.getJSONObject("othe").getString("value");
						if (tp.getJSONObject("type").getJSONObject("othe")
								.has("status"))
							objOthe.status = tp.getJSONObject("type")
									.getJSONObject("othe").getString("status");
						hashOthe.put("othe", objOthe);
						othe.put("othe", objOthe.value);
					}
				} catch (Exception e) {

				}
				try {
					if (tp.getJSONObject("type").has("scal")) {
						final ObjValStat objScal = new ObjValStat();
						if (tp.getJSONObject("type").getJSONObject("scal")
								.has("value"))
							objScal.value = tp.getJSONObject("type")
									.getJSONObject("scal").getString("value");
						if (tp.getJSONObject("type").getJSONObject("scal")
								.has("status"))
							objScal.status = tp.getJSONObject("type")
									.getJSONObject("scal").getString("status");
						hashScal.put("scal", objScal);
						if (objScal.value.equals("comp")) {
							scal.put("value", "Complete Scaling");
						} else if (objScal.value.equals("part")) {
							scal.put("value", "Partial Scaling");
						}
						if (objScal.status.equals("on")) {
							scal.put("status", "done");
						} else {
							scal.put("status", "pending");
						}
					}
				} catch (Exception e) {

				}
				// Log.i("note", tp.getJSONObject("type").getJSONObject("tw")
				// .getJSONObject("note").toString());
				try{
					note.put("note",
							tp.getJSONObject("type").getJSONObject("tw")
									.getJSONObject("note").getString("value"));
				}catch(Exception e){
					
				}
				if (tp.getJSONObject("type").has("tw")
						&& tp.getJSONObject("type").getJSONObject("tw")
								.has("tooth"))
					jsonParseTeeth(tp.getJSONObject("type").getJSONObject("tw")
							.getJSONObject("tooth"), arrTeeth);
			}

		} catch (final JSONException e1) {
			e1.printStackTrace();
		}
	}

	private void jsonParseTeeth(final JSONObject jsonTooth,
			final ArrayList<TeethModel> arrTeeth) {
		// Log.i("", "jsonParse teeth called");
		Iterator<String> iter;
		try {
			iter = jsonTooth.keys();
			while (iter.hasNext()) {
				final String key = iter.next();
				for (final TeethModel teeth1 : arrTeeth) {
					if (teeth1.name.equalsIgnoreCase("tooth" + key)) {
						teeth1.arrTeethTreatmentPlan.clear();
						final Iterator<String> iter1 = jsonTooth.getJSONObject(
								key).keys();
						while (iter1.hasNext()) {
							final String key1 = iter1.next();
							String value1 = "";
							if (key1.equals("othe")) {
								if (jsonTooth.getJSONObject(key)
										.getJSONObject(key1).has("value")) {
									value1 = jsonTooth.getJSONObject(key)
											.getJSONObject(key1)
											.getString("value");
								}
								if (jsonTooth.getJSONObject(key)
										.getJSONObject(key1).has("status")) {
									if (jsonTooth.getJSONObject(key)
											.getJSONObject(key1)
											.getString("status").equals("on"))
										teeth1.hashStatus.put(
												jsonTooth.getJSONObject(key)
														.getJSONObject(key1)
														.getString("value"),
												"done");
									else
										teeth1.hashStatus.put(
												jsonTooth.getJSONObject(key)
														.getJSONObject(key1)
														.getString("value"),
												"pending");
								}

								teeth1.other = value1;
							} else {
								if (jsonTooth.getJSONObject(key)
										.getJSONObject(key1).has("cb"))
									value1 = jsonTooth.getJSONObject(key)
											.getJSONObject(key1)
											.getString("cb");

								if (value1.equals("on")) {
									if (!Util.isEmptyString(keyValue.get(key1))) {
										teeth1.arrTeethTreatmentPlan
												.add(keyValue.get(key1));
									}
									if (jsonTooth.getJSONObject(key)
											.getJSONObject(key1).has("status")
											&& jsonTooth.getJSONObject(key)
													.getJSONObject(key1)
													.getString("status")
													.equals("on")) {
										teeth1.hashStatus.put(
												keyValue.get(key1), "done");
									} else {
										teeth1.hashStatus.put(
												keyValue.get(key1), "pending");
									}
								}
							}

							if (key1.equals("crow")) {
								String t = "";
								if (jsonTooth.getJSONObject(key)
										.getJSONObject(key1).has("t")
										&& jsonTooth.getJSONObject(key)
												.getJSONObject(key1)
												.getJSONObject("t")
												.has("value"))
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
							}

							if (key1.equals("scal")) {
								String t1 = "";
								if (jsonTooth.getJSONObject(key)
										.getJSONObject(key1).has("t1")
										&& jsonTooth.getJSONObject(key)
												.getJSONObject(key1)
												.getJSONObject("t1")
												.has("value"))
									t1 = jsonTooth.getJSONObject(key)
											.getJSONObject(key1)
											.getJSONObject("t1")
											.getString("value");
								String t2 = "";
								if (jsonTooth.getJSONObject(key)
										.getJSONObject(key1).has("t2")
										&& jsonTooth.getJSONObject(key)
												.getJSONObject(key1)
												.getJSONObject("t2")
												.has("value"))
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
							}

							if (key1.equals("rest")) {
								String gic = "";
								if (jsonTooth.getJSONObject(key)
										.getJSONObject(key1).has("gic")
										&& jsonTooth.getJSONObject(key)
												.getJSONObject(key1)
												.getJSONObject("gic").has("cb"))
									gic = jsonTooth.getJSONObject(key)
											.getJSONObject(key1)
											.getJSONObject("gic")
											.getString("cb");
								String status = "";
								if (jsonTooth.getJSONObject(key)
										.getJSONObject(key1).has("gic")
										&& jsonTooth.getJSONObject(key)
												.getJSONObject(key1)
												.getJSONObject("gic")
												.has("status"))
									status = jsonTooth.getJSONObject(key)
											.getJSONObject(key1)
											.getJSONObject("gic")
											.getString("status");
								hashStatus.put("gic", status);
								if (gic.equals("on")) {
									teeth1.arrTeethTreatmentPlan.add(keyValue
											.get("gic"));

									if (status.equals("on")) {
										teeth1.hashStatus.put(
												keyValue.get("gic"), "done");
									} else {
										teeth1.hashStatus.put(
												keyValue.get("gic"), "pending");
									}
								}

								String comp = "";
								if (jsonTooth.getJSONObject(key)
										.getJSONObject(key1).has("comp")
										&& jsonTooth.getJSONObject(key)
												.getJSONObject(key1)
												.getJSONObject("comp")
												.has("cb"))
									comp = jsonTooth.getJSONObject(key)
											.getJSONObject(key1)
											.getJSONObject("comp")
											.getString("cb");
								String status1 = "";
								if (jsonTooth.getJSONObject(key)
										.getJSONObject(key1).has("comp")
										&& jsonTooth.getJSONObject(key)
												.getJSONObject(key1)
												.getJSONObject("comp")
												.has("status"))
									status1 = jsonTooth.getJSONObject(key)
											.getJSONObject(key1)
											.getJSONObject("comp")
											.getString("status");
								hashStatus.put("comp", status1);
								if (comp.equals("on")) {
									teeth1.arrTeethTreatmentPlan.add(keyValue
											.get("comp"));
									if (jsonTooth.getJSONObject(key)
											.getJSONObject(key1)
											.getJSONObject("comp")
											.has("status")
											&& jsonTooth.getJSONObject(key)
													.getJSONObject(key1)
													.getJSONObject("comp")
													.getString("status")
													.equals("on")) {
										teeth1.hashStatus.put(
												keyValue.get("comp"), "done");
									} else {
										teeth1.hashStatus
												.put(keyValue.get("comp"),
														"pending");
									}
								}
								if (jsonTooth.getJSONObject(key)
										.getJSONObject(key1).has("othe")
										&& jsonTooth.getJSONObject(key)
												.getJSONObject(key1)
												.getJSONObject("othe")
												.has("value"))
									teeth1.restOther = jsonTooth
											.getJSONObject(key)
											.getJSONObject(key1)
											.getJSONObject("othe")
											.getString("value");
								if (jsonTooth.getJSONObject(key)
										.getJSONObject(key1)
										.getJSONObject("othe").has("status")
										&& jsonTooth.getJSONObject(key)
												.getJSONObject(key1)
												.getJSONObject("othe")
												.getString("status")
												.equals("on"))
									teeth1.hashStatus
											.put("restoration", "done");
								else
									teeth1.hashStatus.put("restoration",
											"pending");
							}

							if (key1.equals("ps")) {
								final Iterator<String> iterPs = jsonTooth
										.getJSONObject(key).getJSONObject(key1)
										.keys();
								while (iterPs.hasNext()) {
									final String key2 = iterPs.next();
									if (!key2.equals("cb")
											&& !key2.equals("status")
											&& !key2.equals("othe")) {
										String value2 = "";
										if (jsonTooth.getJSONObject(key)
												.getJSONObject(key1)
												.getJSONObject(key2).has("cb"))
											value2 = jsonTooth
													.getJSONObject(key)
													.getJSONObject(key1)
													.getJSONObject(key2)
													.getString("cb");
										if (jsonTooth.getJSONObject(key)
												.getJSONObject(key1)
												.getJSONObject(key2)
												.has("status"))
											hashStatus.put(key2, jsonTooth
													.getJSONObject(key)
													.getJSONObject(key1)
													.getJSONObject(key2)
													.getString("status"));
										if (value2.equals("on")) {
											if (!Util.isEmptyString(keyValue
													.get(key2))) {
												teeth1.arrTeethTreatmentPlan
														.add(keyValue.get(key2));
												if (jsonTooth
														.getJSONObject(key)
														.getJSONObject(key1)
														.getJSONObject(key2)
														.has("status")
														&& jsonTooth
																.getJSONObject(
																		key)
																.getJSONObject(
																		key1)
																.getJSONObject(
																		key2)
																.getString(
																		"status")
																.equals("on")) {
													teeth1.hashStatus.put(
															keyValue.get(key2),
															"done");
												} else {
													teeth1.hashStatus.put(
															keyValue.get(key2),
															"pending");
												}
											}
										}
									} else if (key2.equals("othe")) {
										if (jsonTooth.getJSONObject(key)
												.getJSONObject(key1)
												.getJSONObject(key2)
												.has("value"))
											teeth1.periOther = jsonTooth
													.getJSONObject(key)
													.getJSONObject(key1)
													.getJSONObject(key2)
													.getString("value");
										if (jsonTooth.getJSONObject(key)
												.getJSONObject(key1)
												.getJSONObject(key2)
												.has("status")
												&& jsonTooth.getJSONObject(key)
														.getJSONObject(key1)
														.getJSONObject(key2)
														.getString("status")
														.equals("on"))
											teeth1.hashStatus.put(
													"periodontal surgery",
													"done");
										else
											teeth1.hashStatus.put(
													"periodontal surgery",
													"pending");

									}
								}

							}

						}
					}
				}
			}
		} catch (final JSONException e1) {
			e1.printStackTrace();
		}

	}

	private void jsonUpdateTeeth(final JSONObject jsonTooth,
			final ArrayList<TeethModel> arrTeeth) {
		Iterator<String> iter;
		try {
			iter = jsonTooth.keys();
			while (iter.hasNext()) {
				final String key = iter.next();
				for (final TeethModel teeth1 : arrTeeth) {
					if (teeth1.name.equalsIgnoreCase("tooth" + key)) {

						for (final String s : teeth1.arrTeethTreatmentPlan) {
							if (jsonTooth.getJSONObject(key).has(
									valueKey.get(s))) {
								jsonTooth.getJSONObject(key)
										.getJSONObject(valueKey.get(s))
										.put("cb", "on");
								if (teeth1.hashStatus.containsKey(s)) {
									if (teeth1.hashStatus.get(s).equals("done")) {
										jsonTooth.getJSONObject(key)
												.getJSONObject(valueKey.get(s))
												.put("status", "on");
									} else {
										jsonTooth.getJSONObject(key)
												.getJSONObject(valueKey.get(s))
												.put("status", "off");
									}
								}
							}
							if (jsonTooth.getJSONObject(key)
									.getJSONObject("ps").has(valueKey.get(s))) {
								jsonTooth.getJSONObject(key)
										.getJSONObject("ps")
										.getJSONObject(valueKey.get(s))
										.put("cb", "on");
								if (teeth1.hashStatus.containsKey(s)) {
									if (teeth1.hashStatus.get(s).equals("done")) {
										jsonTooth.getJSONObject(key)
												.getJSONObject("ps")
												.getJSONObject(valueKey.get(s))
												.put("status", "on");
									} else {
										jsonTooth.getJSONObject(key)
												.getJSONObject("ps")
												.getJSONObject(valueKey.get(s))
												.put("status", "off");
									}
								}
							}

							if (jsonTooth.getJSONObject(key)
									.getJSONObject("rest").has(valueKey.get(s))) {
								jsonTooth.getJSONObject(key)
										.getJSONObject("rest")
										.getJSONObject(valueKey.get(s))
										.put("cb", "on");
								if (teeth1.hashStatus.containsKey(s)) {
									if (teeth1.hashStatus.get(s).equals("done")) {
										jsonTooth.getJSONObject(key)
												.getJSONObject("rest")
												.getJSONObject(valueKey.get(s))
												.put("status", "on");
									} else {
										jsonTooth.getJSONObject(key)
												.getJSONObject("rest")
												.getJSONObject(valueKey.get(s))
												.put("status", "off");
									}
								}
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
						jsonTooth.getJSONObject(key).getJSONObject("othe")
								.put("value", teeth1.other);
						if (!Util.isEmptyString(teeth1.hashStatus.get(teeth1.other))
								&& teeth1.hashStatus.get(teeth1.other).contains("done"))
							jsonTooth.getJSONObject(key).getJSONObject("othe")
									.put("status", "on");
						else
							jsonTooth.getJSONObject(key).getJSONObject("othe")
									.put("status", "off");

						jsonTooth.getJSONObject(key).getJSONObject("rest")
								.getJSONObject("othe")
								.put("value", teeth1.restOther);

						if (!Util.isEmptyString(teeth1.hashStatus.get("restoration"))
								&& teeth1.hashStatus.get("restoration")
										.contains("done"))
							jsonTooth.getJSONObject(key).getJSONObject("rest")
									.getJSONObject("othe").put("status", "on");
						else
							jsonTooth.getJSONObject(key).getJSONObject("rest")
									.getJSONObject("othe").put("status", "off");

						jsonTooth.getJSONObject(key).getJSONObject("ps")
								.getJSONObject("othe")
								.put("value", teeth1.periOther);
						if (!Util.isEmptyString(teeth1.hashStatus
								.get("periodontal surgery"))
								&& teeth1.hashStatus.get("periodontal surgery")
										.contains("done"))
							jsonTooth.getJSONObject(key).getJSONObject("ps")
									.getJSONObject("othe").put("status", "on");
						else
							jsonTooth.getJSONObject(key).getJSONObject("ps")
									.getJSONObject("othe").put("status", "off");

					}
				}
			}
		} catch (final JSONException e1) {
			e1.printStackTrace();
		}

	}

	public void updateJson(final JSONObject plan,
			final ArrayList<TeethModel> arrTeeth, final Context context) {
		try {
			plan.put("tp", getBlankJson(context));
			final JSONObject jsonValue = plan.getJSONObject("tp")
					.getJSONObject("type").getJSONObject("val");
			for (final Entry entry : hashVal.entrySet()) {
				jsonValue.put((String) entry.getKey(), entry.getValue());
			}

			final JSONObject jsonCd = plan.getJSONObject("tp")
					.getJSONObject("type").getJSONObject("cd");
			for (final Entry entry : hashCd.entrySet()) {
				final JSONObject obj = new JSONObject();
				final ObjValStat valStat = (ObjValStat) entry.getValue();
				if (entry.getKey().equals("othe")) {
					obj.put("value", cd.get("othe"));
					// Log.i("", hashCd.get("othe").status);
					if (hashCd.get("othe").status.equals("done"))
						obj.put("status", "on");
					else
						obj.put("status", "off");
				} else {
					if (arrCompDenture.contains(keyValue.get(entry.getKey()))) {
						obj.put("cb", "on");
					} else {
						obj.put("cb", "off");
					}
					if (cd.containsKey(keyValue.get(entry.getKey()))
							&& cd.get(keyValue.get(entry.getKey())).equals(
									"done"))
						obj.put("status", "on");
					else
						obj.put("status", "off");
				}

				jsonCd.put((String) entry.getKey(), obj);
				// Log.i("", jsonCd.toString());
			}

			final JSONObject jsonScal = plan.getJSONObject("tp")
					.getJSONObject("type").getJSONObject("scal");
			final ObjValStat valStat = hashScal.get("scal");
			if (scal.containsKey("value")) {
				if (scal.get("value").toString()
						.equalsIgnoreCase("partial scaling")) {
					jsonScal.put("value", "part");
				} else if (scal.get("value").toString()
						.equalsIgnoreCase("Complete Scaling")) {
					jsonScal.put("value", "comp");
				}
				if (!Util.isEmptyString(scal.get("value"))) {
					if (scal.get("status").contains("done")) {
						jsonScal.put("status", "on");
					} else {
						jsonScal.put("status", "off");
					}
				}
				// jsonScal.put("status", valStat.status);
			}

			// Log.i("scal value",jsonScal.toString());

			final JSONObject jsonOthe = plan.getJSONObject("tp")
					.getJSONObject("type").getJSONObject("othe");
			final ObjValStat valStat1 = hashOthe.get("othe");
			if (hashOthe.containsKey("othe")) {
				jsonOthe.put("value", othe.get("othe"));
				jsonOthe.put("status", valStat1.status);
			}
			plan.getJSONObject("tp").getJSONObject("type").getJSONObject("tw")
					.getJSONObject("note").put("value", note.get("note"));
			jsonUpdateTeeth(plan.getJSONObject("tp").getJSONObject("type")
					.getJSONObject("tw").getJSONObject("tooth"), arrTeeth);

		} catch (final JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public CharSequence getTpString(ArrayList<TeethModel> arrTeeth) {
		String s = "Treatment Plan :  ";
		try {
			for (final TeethModel teeth : arrTeeth) {
				if ((teeth.arrTeethState.size() > 0)
						&& (teeth.arrTeethTreatmentPlan.size() > 0)) {
					s = s + teeth.getTreatmentPlanString();
				}
			}
			if (hashCd.containsValue("othe"))
				hashCd.get("othe").value = cd.get("othe");
			if (arrCompDenture.size() > 0
					|| !Util.isEmptyString(cd.get("othe"))) {
				s = s + "\n        Complete Denture : ";
				for (Entry ent : hashCd.entrySet()) {
					ObjValStat obj = (ObjValStat) ent.getValue();
					if (!ent.getKey().equals("othe")) {
						if (arrCompDenture.contains(keyValue.get(ent.getKey()))) {
							obj.value = "on";
							obj.status = cd.get(keyValue.get(ent.getKey()));
						} else {
							obj.value = "off";
							obj.status = "pending";
						}
					}
					if (obj.value.equals("on")) {
						if (ent.getKey().equals("pi"))
							s = s + "\n           - Primary impression("
									+ obj.status + ")";
						else if (ent.getKey().equals("bm"))
							s = s + "\n           - Border Moulding("
									+ obj.status + ")";
						else if (ent.getKey().equals("si"))
							s = s + "\n           - Secondary impression("
									+ obj.status + ")";
						else if (ent.getKey().equals("jr"))
							s = s + "\n           - Jaw Relation(" + obj.status
									+ ")";
						else if (ent.getKey().equals("ti"))
							s = s + "\n           - Try-in(" + obj.status + ")";
						else if (ent.getKey().equals("dd"))
							s = s + "\n           - Denture Delivered("
									+ obj.status + ")";

					} else if (ent.getKey().equals("othe")
							&& !Util.isEmptyString(obj.value)) {
						s = s + "\n           - " + obj.value + "("
								+ obj.status + ")";
					}
				}
			}
			if (!Util.isEmptyString(scal.get("value")))
				s = s + "\n        Scaling : \n           - "
						+ scal.get("value") + "(" + scal.get("status") + ")";
			if (!Util.isEmptyString(othe.get("othe")))
				if (hashOthe.get("othe").status.equals("on"))
					s = s + "\n        Other treatment plan : \n           - "
							+ othe.get("othe") + "(done)";
				else
					s = s + "\n        Other treatment plan : \n           - "
							+ othe.get("othe") + "(pending)";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
	
	public CharSequence getTpStringPast(ArrayList<TeethModel> arrTeeth) {
		String s = "Treatment Plan :  ";
		try {
			for (final TeethModel teeth : arrTeeth) {
				if ((teeth.arrTeethTreatmentPlan.size() > 0)) {
					s = s + teeth.getTreatmentPlanString();
				}
			}
			if (hashCd.containsValue("othe"))
				hashCd.get("othe").value = cd.get("othe");
			if (arrCompDenture.size() > 0
					|| !Util.isEmptyString(cd.get("othe"))) {
				s = s + "\n        Complete Denture : ";
				for (Entry ent : hashCd.entrySet()) {
					ObjValStat obj = (ObjValStat) ent.getValue();
					if (!ent.getKey().equals("othe")) {
						if (arrCompDenture.contains(keyValue.get(ent.getKey()))) {
							obj.value = "on";
							obj.status = cd.get(keyValue.get(ent.getKey()));
						} else {
							obj.value = "off";
							obj.status = "pending";
						}
					}
					if (obj.value.equals("on")) {
						if (ent.getKey().equals("pi"))
							s = s + "\n           - Primary impression("
									+ obj.status + ")";
						else if (ent.getKey().equals("bm"))
							s = s + "\n           - Border Moulding("
									+ obj.status + ")";
						else if (ent.getKey().equals("si"))
							s = s + "\n           - Secondary impression("
									+ obj.status + ")";
						else if (ent.getKey().equals("jr"))
							s = s + "\n           - Jaw Relation(" + obj.status
									+ ")";
						else if (ent.getKey().equals("ti"))
							s = s + "\n           - Try-in(" + obj.status + ")";
						else if (ent.getKey().equals("dd"))
							s = s + "\n           - Denture Delivered("
									+ obj.status + ")";

					} else if (ent.getKey().equals("othe")
							&& !Util.isEmptyString(obj.value)) {
						s = s + "\n           - " + obj.value + "("
								+ obj.status + ")";
					}
				}
			}
			if (!Util.isEmptyString(scal.get("value")))
				s = s + "\n        Scaling : \n           - "
						+ scal.get("value") + "(" + scal.get("status") + ")";
			if (!Util.isEmptyString(othe.get("othe")))
				if (hashOthe.get("othe").status.equals("on"))
					s = s + "\n        Other treatment plan : \n           - "
							+ othe.get("othe") + "(done)";
				else
					s = s + "\n        Other treatment plan : \n           - "
							+ othe.get("othe") + "(pending)";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

}

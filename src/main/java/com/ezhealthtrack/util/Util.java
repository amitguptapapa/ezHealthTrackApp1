package com.ezhealthtrack.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.ezhealthtrack.R;
import com.ezhealthtrack.util.NetworkCallController.OnResponse;
import com.todddavies.components.progressbar.ProgressWheel;

public class Util {
	private static final String TAG = "Util";

	public static void Alertdialog(final Context con, final String s,
			final OnResponse onresponse) {
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				con);

		// set title
		// alertDialogBuilder.setTitle(s);

		// set dialog message
		alertDialogBuilder
				.setMessage(s)
				.setCancelable(false)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog,
							final int id) {
						onresponse.onResponseListner("Ok");

					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

							}
						});

		// create alert dialog
		final AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

	}

	public static void Alertdialog(final Context con, final String s) {
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				con);

		// set title
		// alertDialogBuilder.setTitle(s);

		// set dialog message
		alertDialogBuilder.setMessage(s).setCancelable(false)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog,
							final int id) {
						// if this button is clicked, close
						// current activity

					}
				});

		// create alert dialog
		final AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

	}

	public static void AlertdialogWithFinish(final Context con, final String s) {
		try {
			final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					con);

			// set title
			// alertDialogBuilder.setTitle(s);

			// set dialog message
			alertDialogBuilder
					.setMessage(s)
					.setCancelable(false)
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(
										final DialogInterface dialog,
										final int id) {
									((Activity) con).finish();

								}
							});

			// create alert dialog
			final AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.setCancelable(false);
			// show it
			alertDialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String getBase64String(final String string) {
		byte[] data;
		String base64string = "";
		try {
			data = string.getBytes("UTF-8");
			base64string = Base64.encodeToString(data, Base64.DEFAULT);
		} catch (final UnsupportedEncodingException e) {
			Log.e(Util.TAG, e);
		}
		return base64string;
	}

	public static void getImageFromUrl(final String url,
			final ImageLoader imgLoader, final ImageView view) {
		imgLoader.get(url, new ImageListener() {

			@Override
			public void onErrorResponse(final VolleyError arg0) {
				view.setImageResource(R.drawable.noimage);
			}

			@Override
			public void onResponse(final ImageContainer ic, final boolean arg1) {
				view.setImageBitmap(ic.getBitmap());

			}
		});
	}

	public static boolean isEmptyString(final String str) {
		if ((str == null) || str.equalsIgnoreCase("null")
				|| str.equalsIgnoreCase("") || (str.length() < 1)) {
			return true;
		}
		return false;
	}

	public static String readJsonFromAssets(final String path,
			final Context context) {
		String json = null;
		try {

			final InputStream is = context.getAssets().open(path);

			final int size = is.available();

			final byte[] buffer = new byte[size];

			is.read(buffer);

			is.close();

			json = new String(buffer, "UTF-8");

		} catch (final IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return json;
	}

	public static void showHideView(final View layout, final Boolean visiblity,
			final ArrayList<String> arrString, final String string) {
		if (visiblity) {
			layout.setVisibility(View.VISIBLE);
			if (!arrString.contains(string)) {
				arrString.add(string);
			}
		} else {
			layout.setVisibility(View.GONE);
			arrString.remove(string);
		}
	}

	public static void showHideView(final View layout, final ImageView img) {
		if (layout.getVisibility() == View.GONE) {
			img.setImageResource(R.drawable.minus);
			layout.setVisibility(View.VISIBLE);
		} else {
			img.setImageResource(R.drawable.plus);
			layout.setVisibility(View.GONE);
		}
	}

	public static void showHideView(final View layout, final TextView txt) {
		String s = (String) txt.getText();
		if (layout.getVisibility() == View.GONE) {
			s = s.replace("+", "-");
			layout.setVisibility(View.VISIBLE);
		} else {
			layout.setVisibility(View.GONE);
			s = s.replace("-", "+");
		}
		txt.setText(s);

	}

	public static void showHideView(final View layout, final TextView txt,
			final TextView txtHide) {
		String s = (String) txt.getText();
		if (layout.getVisibility() == View.GONE) {
			s = s.replace("+", "-");
			layout.setVisibility(View.VISIBLE);
			txtHide.setVisibility(View.GONE);
		} else {
			layout.setVisibility(View.GONE);
			s = s.replace("-", "+");
			txtHide.setVisibility(View.VISIBLE);
		}
		txt.setText(s);

	}

	public interface DateResponse {
		public void dateResponseListner(Date date);
	}

	public static void showCalender(final TextView txtView, Context context,
			final DateResponse response) {
		final Calendar cal = Calendar.getInstance();
		final DatePickerDialog datepicker = new DatePickerDialog(context,
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
						try {
							Date date = sdf.parse(txtView.getText().toString());
							response.dateResponseListner(date);

						} catch (final ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal
						.get(Calendar.DAY_OF_MONTH));
		datepicker.show();
	}

	public static void datePicker(final EditText edit, final Context context) {
		edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				final Calendar cal = Calendar.getInstance();
				final DatePickerDialog datepicker = new DatePickerDialog(
						context, new OnDateSetListener() {

							@Override
							public void onDateSet(final DatePicker view,
									final int year, int monthOfYear,
									final int dayOfMonth) {
								if (dayOfMonth < 10 && monthOfYear < 9) {
									edit.setText("0" + ++monthOfYear + "/0"
											+ dayOfMonth + "/" + year);
								} else if (dayOfMonth < 10 && monthOfYear > 8) {
									edit.setText(++monthOfYear + "/0"
											+ dayOfMonth + "/" + year);
								} else if (dayOfMonth > 9 && monthOfYear < 9) {
									edit.setText("0" + ++monthOfYear + "/"
											+ dayOfMonth + "/" + year);
								} else {
									edit.setText(++monthOfYear + "/"
											+ dayOfMonth + "/" + year);
								}

							}
						}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal
								.get(Calendar.DAY_OF_MONTH));
				datepicker.show();

			}
		});
	}

	public static int getSlotId(String time) {
		int i = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
		try {
			Date date = sdf.parse(time);
			Date dateRef = sdf.parse("00:00 AM");
			long mills = date.getTime() - dateRef.getTime();
			i = (int) mills / (1000 * 60 * 15);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i + 1;
	}

	public static void hideSoftKeyboard(Activity activity) {
		try {
			InputMethodManager inputMethodManager = (InputMethodManager) activity
					.getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(activity
					.getCurrentFocus().getWindowToken(), 0);
		} catch (Exception e) {

		}
	}

	public static void setupUI(final Activity activity, View view) {

		// Set up touch listener for non-text box views to hide keyboard.
		if (!(view instanceof EditText)) {

			view.setOnTouchListener(new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {
					hideSoftKeyboard(activity);
					return false;
				}

			});
		}

		// If a layout container, iterate over children and seed recursion.
		if (view instanceof ViewGroup) {

			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

				View innerView = ((ViewGroup) view).getChildAt(i);

				setupUI(activity, innerView);
			}
		}
	}

	public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		Bitmap resizedBitmap = null;
		if (bm.getWidth() > newWidth) {
			try {
				int width = bm.getWidth();

				int height = bm.getHeight();

				float scaleWidth = ((float) newWidth) / width;

				float scaleHeight = ((float) newHeight) / height;

				// CREATE A MATRIX FOR THE MANIPULATION

				Matrix matrix = new Matrix();

				// RESIZE THE BIT MAP

				matrix.postScale(scaleWidth, scaleWidth);

				// RECREATE THE NEW BITMAP

				resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
						matrix, false);
			} catch (Exception e) {
				Log.e("Util", e);
			} catch (Error e) {
				Log.e("Util", e);
			}
		} else {
			resizedBitmap = bm;
		}
		return resizedBitmap;
	}

	public static Bitmap getGalleryBitmap(Activity actvity, Uri uri,
			int orientation) {
		Log.e("", "uri---> " + uri);
		Bitmap bitmap = null;
		Cursor cursor;
		bitmap = null;
		String[] projection = { MediaStore.Images.Media.DATA };
		cursor = actvity.getContentResolver().query(uri, projection, null,
				null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String filePath = cursor.getString(column_index);
		cursor.close();

		bitmap = decodeSampledBitmapFromFile(filePath, 1152, 864, orientation);

		return bitmap;
	}

	public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth,
			int reqHeight, int orientation) {
		try {
			// First decode with inJustDecodeBounds=true to check dimensions
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);

			// Calculate inSampleSize
			options.inSampleSize = calculateInSampleSize(options, reqWidth,
					reqHeight);

			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			if (orientation > 0) {
				Matrix matrix = new Matrix();
				matrix.postRotate(orientation);
				Bitmap srcBitmap = BitmapFactory.decodeFile(path, options);

				return Bitmap.createBitmap(srcBitmap, 0, 0,
						srcBitmap.getWidth(), srcBitmap.getHeight(), matrix,
						true);
			} else {
				return BitmapFactory.decodeFile(path, options);
			}
		} catch (Exception e) {
			Log.e("Util::", e);
			return null;
		}

	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width

			options.inSampleSize = 2;

			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	public static void changeAlertDialog(final Activity context) {
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

		// set title
		alertDialogBuilder
				.setTitle("Changes will be discarded, Do you want to continue ?");
		// set dialog message
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								context.finish();

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

	public static void filterClear(final TextView txtFilter, final View view) {
		txtFilter.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (Util.isEmptyString(s.toString()))
					view.setVisibility(View.GONE);
				else
					view.setVisibility(View.VISIBLE);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				txtFilter.setText("");

			}
		});
	}

	public static Dialog showLoadDialog(Context context) {
		final Dialog dialog = new Dialog(context);
		try {
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.dialog_load);
			final ProgressWheel pw = (ProgressWheel) dialog
					.findViewById(R.id.pw_spinner);
			pw.setSpinSpeed(10);
			pw.spin();
			dialog.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			dialog.setCancelable(false);
			dialog.show();
		} catch (Exception e) {
			Log.e(TAG, e);
		}
		return dialog;
	}

	public static void enableDisableView(View view, boolean enabled) {
		view.setEnabled(enabled);

		if (view instanceof ViewGroup) {
			ViewGroup group = (ViewGroup) view;

			for (int idx = 0; idx < group.getChildCount(); idx++) {
				enableDisableView(group.getChildAt(idx), enabled);
			}
		}
	}

	public static Date getCurrentDate() {
		int offsetCurrentTimeZone = TimeZone.getDefault().getRawOffset();
		int offsetIST = 11 * 30 * 60 * 1000;
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(cal.getTime().getTime() - offsetCurrentTimeZone
				+ offsetIST));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Log.i("" + offsetCurrentTimeZone, "" + cal.getTime().toString());
		return cal.getTime();
	}

	public static String doubleFormat(String s) {
		DecimalFormat df = new DecimalFormat("##0.00");
		return df.format(Double.parseDouble(s));
	}

	public static void makeLinkClickable(SpannableStringBuilder strBuilder,
			final URLSpan span, final OnResponse onResponse) {
		int start = strBuilder.getSpanStart(span);
		int end = strBuilder.getSpanEnd(span);
		int flags = strBuilder.getSpanFlags(span);
		ClickableSpan clickable = new ClickableSpan() {
			public void onClick(View view) {
				// Do something with span.getURL() to handle the link click...
				onResponse.onResponseListner(span.getURL());
			}
		};
		strBuilder.setSpan(clickable, start, end, flags);
		strBuilder.removeSpan(span);
	}

	public static void setTextViewHTML(TextView text, String html,
			final OnResponse onResponse) {
		CharSequence sequence = Html.fromHtml(html);
		SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
		URLSpan[] urls = strBuilder.getSpans(0, sequence.length(),
				URLSpan.class);
		for (URLSpan span : urls) {
			makeLinkClickable(strBuilder, span, onResponse);
		}
		text.setText(strBuilder);
	}

	public static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?"); // match a number with optional
												// '-' and decimal.
	}

	public static JSONObject removeEmptyObjects(JSONObject jobj) {
		JSONObject jobj1 = new JSONObject();
		final Iterator<String> iter;
		iter = jobj.keys();
		while (iter.hasNext()) {
			final String key = iter.next();
			try {
				if (jobj.get(key) instanceof JSONArray) {
					Log.i(key, jobj.get(key).toString());
					if (jobj.getJSONArray(key).length() == 0) {
						// jobj.remove(key);
					} else {
						JSONArray jArr = new JSONArray();
						for (int i = 0; i < jobj.getJSONArray(key).length(); i++) {
							jArr.put(i,
									removeEmptyObjects(jobj.getJSONArray(key)
											.getJSONObject(i)));
						}
						jobj1.put(key, jArr);
					}
				} else if (jobj.get(key) instanceof JSONObject) {
					jobj1.put(key, removeEmptyObjects(jobj.getJSONObject(key)));
				} else {
					jobj1.put(key, jobj.get(key));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return jobj1;
	}

}

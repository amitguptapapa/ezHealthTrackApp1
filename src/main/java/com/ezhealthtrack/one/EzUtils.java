package com.ezhealthtrack.one;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.print.PrintHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.gallery.GConstants;
import com.ezhealthtrack.util.Util;

public class EzUtils {
	final static public String BRANCH_INFO_FILENAME = "branch_info.txt";

	final static public String EZ_SCREEN_LARGE = "ez-large";
	final static public String EZ_SCREEN_MEDIUM = "ez-medium";
	final static public String EZ_SCREEN_SMALL = "ez-small";

	final static public int PRINT_PAGE_WIDTH = 1058;
	final static public int PRINT_PAGE_HEIGHT = 1496;
	static Context context;

	// remove an item from JSONArray
	public static JSONArray removeInt(JSONArray inArray, int value) {

		JSONArray outArray = new JSONArray();
		int len = inArray.length();
		for (int i = 0; i < len; i++) {
			if (inArray.optInt(i) != value)
				outArray.put(inArray.optInt(i));
		}
		return outArray;
	}

	// Write JSONArray into a file
	static public void writeToFile(String filename, JSONArray array) {
		if (array == null)
			array = new JSONArray();
		JSONObject object = new JSONObject();
		try {
			object.put("data", array);
		} catch (JSONException e) {

		}
		EzUtils.writeToFile(filename, object);
	}

	// Read string from a file
	static public JSONArray readJSONArryFromFile(String filename) {
		JSONObject object = EzUtils.readFromFile(filename);
		JSONArray array = object.optJSONArray("data");
		if (array == null)
			array = new JSONArray();
		return array;
	}

	// Write string into a file
	static public void writeToFile(String filename, JSONObject object) {

		if (object == null)
			object = new JSONObject();

		String data = object.toString();
		try {
			Context appContext = EzApp.getInstance().getApplicationContext();
			FileOutputStream fos = appContext.openFileOutput(filename,
					Context.MODE_PRIVATE);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);
			outputStreamWriter.write(data);
			outputStreamWriter.close();
		} catch (IOException e) {
			Log.e("OU:writeToFile: ", "Write failed: " + e.toString());
		}
	}

	// Read string from a file
	static public JSONObject readFromFile(String filename) {

		if (filename == null)
			filename = "oye_null.txt";

		// String contents = null;
		JSONObject object = new JSONObject();
		try {
			Context appContext = EzApp.getInstance().getApplicationContext();
			FileInputStream inputStream = appContext.openFileInput(filename);

			if (inputStream != null) {
				InputStreamReader inputStreamReader = new InputStreamReader(
						inputStream);
				BufferedReader bufferedReader = new BufferedReader(
						inputStreamReader);
				String receiveString = "";
				StringBuilder stringBuilder = new StringBuilder();

				while ((receiveString = bufferedReader.readLine()) != null) {
					stringBuilder.append(receiveString);
				}

				inputStream.close();
				String contents = stringBuilder.toString();
				if (contents != null && contents.length() > 0) {
					try {
						object = new JSONObject(contents);
					} catch (JSONException e) {
						Log.e("OU:readFromFile: " + filename,
								"JSONObject conversion failed: " + e.toString());
					}
				}
				Log.w("OU:readFromFile: " + filename, "Read Length: "
						+ contents.length());
			}
		} catch (FileNotFoundException e) {
			Log.w("OU:readFromFile: " + filename,
					"File not found: " + e.toString());
		} catch (IOException e) {
			Log.w("OU:readFromFile: " + filename,
					"Can not read file: " + e.toString());
		}

		return object;
	}

	static public void setSharedPreference(String key, String value) {
		final Editor editor = EzApp.sharedPref.edit();
		editor.putString(key, value);
		editor.commit();
	}

	static public void showLong(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	static public void showShort(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	static public void showLong(String message) {
		showLong(EzApp.getInstance().getApplicationContext(), message);
	}

	// Save Image to Ez Album
	static public boolean savePhotoToAlbum(Context context, Bitmap bmImage,
			String imageId) {

		/* Checks if external storage is available for read and write */
		if (EzUtils.isExternalStorageWritable() != true) {
			Toast.makeText(context, "External storage not found.",
					Toast.LENGTH_LONG).show();
			EzUtils.savePhotoToOtherAlbums(context, bmImage);
			return false;
		}

		File externalDir = Environment.getExternalStorageDirectory();
		File dir = new File(externalDir + GConstants.PHOTO_ALBUM);

		if (!dir.exists()) {
			dir.mkdirs();
			if (!dir.exists()) {
				Toast.makeText(context, "Can not create Ez Album.",
						Toast.LENGTH_LONG).show();
				EzUtils.savePhotoToOtherAlbums(context, bmImage);
				return false;
			}
		}

		File file = null;
		try {
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			bmImage.compress(Bitmap.CompressFormat.JPEG, 80, bytes);

			// create a new file name
			String imageFileName = "IMG_" + imageId;
			file = new File(dir + File.separator + imageFileName + ".jpg");

			// Save image into app's album gallery
			FileOutputStream fo = new FileOutputStream(file);
			fo.write(bytes.toByteArray());
			fo.close();

		} catch (FileNotFoundException e) {
			EzUtils.savePhotoToOtherAlbums(context, bmImage);
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			EzUtils.savePhotoToOtherAlbums(context, bmImage);
			e.printStackTrace();
			return false;
		}

		// Refresh Android gallery after storing/deleting or updating a photo
		// sendBroadcast(new Intent(
		// Intent.ACTION_MEDIA_MOUNTED,
		// Uri.parse("file://" + Environment.getExternalStorageDirectory())));

		MediaScannerConnection.scanFile(context,
				new String[] { file.toString() }, null,
				new MediaScannerConnection.OnScanCompletedListener() {
					public void onScanCompleted(String path, Uri uri) {
						Log.v("ExternalStorage", "Scanned " + path + ":");
						Log.v("ExternalStorage", "-> uri=" + uri);
					}
				});
		Toast.makeText(context, "Photo Saved", Toast.LENGTH_LONG).show();
		return true;
	}

	static private void savePhotoToOtherAlbums(Context context, Bitmap bmImage) {
		try {
			if (MediaStore.Images.Media.insertImage(
					context.getContentResolver(), bmImage, "Ez", "Ez") != null) {
				Toast.makeText(context, "Phtoto added to Photo Album.",
						Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Toast.makeText(context, "Ops! Phtoto could not be saved.",
					Toast.LENGTH_LONG).show();
		}
	}

	/* Checks if external storage is available for read and write */
	static public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	static public Uri getNewCameraImageUri() {

		/* Checks if external storage is available for read and write */
		if (EzUtils.isExternalStorageWritable() != true) {
			EzUtils.showLong("External storage not found / writable.");
			return null;
		}

		File externalDir = Environment.getExternalStorageDirectory();
		File dir = new File(externalDir + GConstants.PHOTO_ALBUM);
		if (!dir.exists()) {
			dir.mkdirs();
			if (!dir.exists()) {
				EzUtils.showLong("Can not create Ez Album.");
				return null;
			}
		}

		// Determine Uri of camera image to save.
		final String fname = getUniqueImageFilename();
		final File filepath = new File(dir, fname);
		return Uri.fromFile(filepath);
	}

	static private String getUniqueImageFilename() {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		return "JPEG_" + timeStamp + ".jpg";
	}

	static private String mEzScreenSize = null;

	static public String getDeviceSize(Context activity) {
		if (mEzScreenSize != null)
			return mEzScreenSize;

		DisplayMetrics dm = new DisplayMetrics();

		WindowManager windowManager = (WindowManager) activity
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(dm);

		// activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		int dens = dm.densityDpi;
		double wi = (double) width / (double) dens;
		double hi = (double) height / (double) dens;
		double x = Math.pow(wi, 2);
		double y = Math.pow(hi, 2);
		double screenInches = Math.sqrt(x + y);

		if (screenInches >= 9) {
			mEzScreenSize = EZ_SCREEN_LARGE;
		} else if (screenInches >= 7) {
			mEzScreenSize = EZ_SCREEN_MEDIUM;
		} else {
			mEzScreenSize = EZ_SCREEN_SMALL;
		}
		return mEzScreenSize;
	}

	@SuppressWarnings("deprecation")
	static public String getDisplayDateT1(Date date) {
		Date currentDate = Calendar.getInstance().getTime();
		currentDate.setHours(0);
		currentDate.setMinutes(0);
		currentDate.setSeconds(0);
		Date tomorrowDate = new Date(currentDate.getTime() + 24 * 60 * 60000
				- 60000);

		if (date.compareTo(currentDate) > 0 && date.compareTo(tomorrowDate) < 0) {
			final SimpleDateFormat df = new SimpleDateFormat("hh:mm");
			return df.format(date);
		} else {
			final SimpleDateFormat df = new SimpleDateFormat("MMM dd', 'yyyy");
			return df.format(date);
		}
	}

	// get printable date & time from server response
	static public Date getDisplayDateTime(String date, int minutes) {

		final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		final Calendar cal = Calendar.getInstance();
		try {
			final Date theDate = sdf.parse(date);
			cal.setTime(theDate);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.add(Calendar.MINUTE, minutes);
		} catch (final Exception e) {
			Log.e("EU::getDisplayDateTime()", "" + e.getMessage());
		}
		return cal.getTime();
	}

	public static void hideKeyBoard(Activity activity) {
		// Hide Key board
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		View focus = activity.getCurrentFocus();
		if (focus != null) {
			imm.hideSoftInputFromWindow(focus.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	static public void printView(View view, String job, Context context) {

		if (!PrintHelper.systemSupportsPrint()) {
			Util.Alertdialog(
					context,
					"Device does not support printing. You need to install and enable a print service plugin from the Play Store");
			return;
		}

		PrintHelper printer = new PrintHelper(context);
		printer.setScaleMode(PrintHelper.SCALE_MODE_FIT);
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bm = view.getDrawingCache();
		printer.printBitmap(job, bm);
	}

	public static Bitmap loadBitmapFromView(View view) {
		Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bitmap);
		view.layout(0, 0, view.getWidth(), view.getHeight());
		view.draw(c);
		return bitmap;
	}

	static public void downloadFile(Context c, String url, String title,
			String description, String file) {
		DownloadManager.Request request = new DownloadManager.Request(
				Uri.parse(url));
		request.setTitle(title);
		request.setDescription(description);
		request.allowScanningByMediaScanner();
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		request.setDestinationInExternalPublicDir(
				Environment.DIRECTORY_DOWNLOADS, file);

		// get download service and enqueue file
		DownloadManager manager = (DownloadManager) c
				.getSystemService(Context.DOWNLOAD_SERVICE);
		manager.enqueue(request);
	}

	// added to check the network availability
	static public boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}
}

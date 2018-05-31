package com.ezhealthtrack.gallery;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

public class GUtils {
	private Context _context;
	private int _numColumns;

	// constructor
	public GUtils(Context context) {
		this._context = context;
		_numColumns = GConstants.NUM_OF_COLUMNS_DEFAULT;
	}

	// Reading file paths from SDCard
	public ArrayList<String> getFilePaths() {
		ArrayList<String> filePaths = new ArrayList<String>();

		File directory = new File(
				android.os.Environment.getExternalStorageDirectory()
						+ File.separator + GConstants.PHOTO_ALBUM);

		// check for directory
		if (directory.isDirectory()) {
			// getting list of file paths
			File[] listFiles = directory.listFiles();

			// Check for count
			if (listFiles.length > 0) {

				// loop through all files
				for (int i = 0; i < listFiles.length; i++) {

					// get file path
					String filePath = listFiles[i].getAbsolutePath();

					// check for supported file extension
					if (IsSupportedFile(filePath)) {
						// Add image path to array list
						filePaths.add(filePath);
					}
				}
			} else {
				// image directory is empty
				Toast.makeText(
						_context,
						GConstants.PHOTO_ALBUM
								+ " is empty. Please load some images in it !",
						Toast.LENGTH_LONG).show();
			}

		} else {
			AlertDialog.Builder alert = new AlertDialog.Builder(_context);
			alert.setTitle("Error!");
			alert.setMessage(GConstants.PHOTO_ALBUM
					+ " directory path is not valid! Please set the image directory name GConstants.java class");
			alert.setPositiveButton("OK", null);
			alert.show();
		}

		return filePaths;
	}

	// Check supported file extensions
	private boolean IsSupportedFile(String filePath) {
		String ext = filePath.substring((filePath.lastIndexOf(".") + 1),
				filePath.length());

		if (GConstants.FILE_EXTN.contains(ext.toLowerCase(Locale.getDefault())))
			return true;
		else
			return false;

	}

	/*
	 * getting screen width
	 */
	@SuppressWarnings("deprecation")
	public Point getScreenSize() {
		WindowManager wm = (WindowManager) _context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		final Point point = new Point();
		if (android.os.Build.VERSION.SDK_INT >= 13) {
			display.getSize(point);
		} else {
			point.x = display.getWidth();
			point.y = display.getHeight();
		}
		return point;
	}

	public int getColumnWidth() {
		Resources r = _context.getResources();
		float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				GConstants.GRID_PADDING, r.getDisplayMetrics());
		int columnWidth = (int) ((getScreenSize().x - ((_numColumns + 1) * padding)) / _numColumns);
		return columnWidth;
	}

	public int getColumnCount() {
		return _numColumns;
	}

	public void setColumnCount(int numColumns) {
		_numColumns = numColumns;
	}

}

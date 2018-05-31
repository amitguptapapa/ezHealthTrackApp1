package com.ezhealthtrack.gallery;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.ezhealthtrack.one.EzUtils;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

public class EzImageUploader {
	private Bitmap bitmap;

	public int mImageInitialSize;
	public int mCompressionRatio;

	final int MAX_WIDTH_LIMIT = 1200;
	final int MAX_HEIGHT_LIMIT = 900;

	public EzImageUploader(Context context) {
	}

	public boolean isBitmapAvailable() {
		if (bitmap != null)
			return true;
		return false;
	}

	Bitmap getImageBitmap() {
		return bitmap;
	}

	public String getImageAsBase64String() {
		if (bitmap == null) {
			return null;
		}

		// resize & convert bitmap to string for image upload
		double width = bitmap.getWidth();
		double height = bitmap.getHeight();
		if (width > MAX_WIDTH_LIMIT || height > MAX_HEIGHT_LIMIT) {
			if (width > MAX_WIDTH_LIMIT) {
				height = height * (MAX_WIDTH_LIMIT / width);
				width = MAX_WIDTH_LIMIT;
			}
			if (height > MAX_HEIGHT_LIMIT) {
				width = width * (MAX_HEIGHT_LIMIT / height);
				height = MAX_HEIGHT_LIMIT;
			}
			Bitmap bmap = Bitmap.createScaledBitmap(bitmap, (int) width,
					(int) height, true);
			clearImageBitmap();
			bitmap = bmap;
		}

		/*
		 * double width = bitmap.getWidth(); double height = bitmap.getHeight();
		 * double ratio = 300 / width; int newheight = (int) (ratio * height);
		 * bitmap = Bitmap.createScaledBitmap(bitmap, 300, newheight, true);
		 */

		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		mCompressionRatio = 90;
		bitmap.compress(Bitmap.CompressFormat.JPEG, mCompressionRatio, bao);
		byte[] ba = bao.toByteArray();
		mImageInitialSize = ba.length;

		if (mImageInitialSize > 250000) {
			mCompressionRatio = 40;
		} else if (mImageInitialSize > 225000) {
			mCompressionRatio = 44;
		} else if (mImageInitialSize > 200000) {
			mCompressionRatio = 50;
		} else if (mImageInitialSize > 175000) {
			mCompressionRatio = 57;
		} else if (mImageInitialSize > 150000) {
			mCompressionRatio = 67;
		} else if (mImageInitialSize > 125000) {
			mCompressionRatio = 80;
		} else {
		}

		Log.v("Uploader", "Initial Size of JPEG Image is: " + mImageInitialSize);
		Log.v("Uploader", "Compression Ratio is: " + mCompressionRatio);

		if (mCompressionRatio < 90) {
			ByteArrayOutputStream baoSmall = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, mCompressionRatio,
					baoSmall);
			byte[] baSmall = baoSmall.toByteArray();
			mImageInitialSize = baSmall.length;
			return Base64.encodeToString(baSmall, Base64.DEFAULT);
		}
		return Base64.encodeToString(ba, Base64.DEFAULT);
	}

	public Bitmap getScaledBitmapForWidth(int theWidth) {
		if (bitmap == null) {
			return null;
		}

		// resize & convert bitmap to string for image upload
		double width = bitmap.getWidth();
		double height = bitmap.getHeight();
		double ratio = theWidth / width;
		int newheight = (int) (ratio * height);
		Bitmap bitmapIcon = Bitmap.createScaledBitmap(bitmap, theWidth,
				newheight, true);
		return bitmapIcon;
	}

	public Bitmap loadImageFromUri(Uri imageURI, Context context) {
		try {
			bitmap = this.decodeBitmapFromUri(imageURI, context, 1200, 900);
		} catch (FileNotFoundException e) {
			Log.e("EIU:loadImageFromUri()", "ImageURI: " + imageURI);
			Log.e(e.getClass().getName(), e.getMessage(), e);
			this.loadImageFromUriByFile(imageURI, context);
		} catch (Exception e) {
			EzUtils.showLong("File not found");
			Log.e(e.getClass().getName(), e.getMessage(), e);
		}
		return bitmap;
	}

	public Bitmap decodeBitmapFromUri(Uri imageURI, Context context,
			int reqWidth, int reqHeight) throws FileNotFoundException,
			IOException {

		Log.e("decodeBitmapFromUri", "imageURI: " + imageURI.toString());
		InputStream input = context.getContentResolver().openInputStream(
				imageURI);

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(input, null, options);
		input.close();
		if ((options.outWidth == -1) || (options.outHeight == -1)) {
			return null;
		}

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		input = context.getContentResolver().openInputStream(imageURI);
		Bitmap bitmap = BitmapFactory.decodeStream(input, null, options);
		input.close();
		return bitmap;
	}

	public Bitmap loadImageFromUriByFile(Uri imageURI, Context context) {
		this.clearImageBitmap();

		try {
			// MEDIA GALLERY
			String filePath = this.getPath(imageURI, context);

			if (filePath == null) {
				filePath = imageURI.getPath();
			}

			if (filePath == null) {
				EzUtils.showLong("Unknown path");
				Log.e("Bitmap", "Unknown path");
				return null;
			}
			// set Bitmap
			bitmap = EzImageUploader.decodeBitmapFromFile(filePath, 1200, 900);

		} catch (Exception e) {
			EzUtils.showLong("File not found");
			Log.e(e.getClass().getName(), e.getMessage(), e);
		}
		return bitmap;
	}

	public static Bitmap decodeBitmapFromFile(String filePath, int reqWidth,
			int reqHeight) {

		Log.v("EIU:decodeBitmapFromFile()", "File: " + filePath);

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
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

	public String getPath(Uri uri, Context context) {
		String[] projection = { MediaStore.Images.Media.DATA };

		Cursor cursor = context.getContentResolver().query(uri, projection,
				null, null, null);
		if (cursor == null)
			return null;

		// HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
		// THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	public void clearImageBitmap() {
		if (bitmap == null)
			return;
		bitmap.recycle();
		bitmap = null;
		System.gc();
	}

	public void onDestroy() {
		clearImageBitmap();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	protected int sizeOf(Bitmap data) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
			return data.getRowBytes() * data.getHeight();
		} else {
			return data.getByteCount();
		}
	}

	public boolean RotateImage() {
		if (bitmap == null)
			return false;

		Matrix matrix = new Matrix();
		matrix.postRotate(90);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return true;
	}
}

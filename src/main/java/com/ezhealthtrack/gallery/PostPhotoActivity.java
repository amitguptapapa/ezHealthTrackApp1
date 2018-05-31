package com.ezhealthtrack.gallery;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ezhealthrack.api.APIs;
import com.ezhealthtrack.R;
import com.ezhealthtrack.controller.EzNetwork;
import com.ezhealthtrack.controller.EzNetwork.ResponseHandler;
import com.ezhealthtrack.controller.SoapNotesController;
import com.ezhealthtrack.model.gallery.SOAPPhoto;
import com.ezhealthtrack.one.EzUtils;
import com.ezhealthtrack.util.EzActivity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * IMPORTANT - ShareDataActivity.java provides similar functionality
 * Any change in this file may require update in that file.
 */

public class PostPhotoActivity extends EzActivity {

    final static int TAKE_PHOTO_REQUEST_CODE = 1221;

    private LinearLayout mProgressBar;

    private ImageView mImageView;
    private LinearLayout mImageLL;
    private EditText mPhotoTitle;
    private EditText mPhotoDescription;
    EzImageUploader mImageUploader;
    LinearLayout mViewContainer;
    private long mLastClickTime = 0;

    static public String mEditId;
    static public String mEditTitle;
    static public String mEditDescription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_gallery_image);
        mScreenRotation = ActivityInfo.SCREEN_ORIENTATION_SENSOR;

        Log.v("PPA", "onCreate");
        // this.setHomeAsDisabled(savedInstanceState);

        mImageUploader = new EzImageUploader(this);

        mProgressBar = (LinearLayout) findViewById(R.id.id_progressbar_ll);
        mPhotoTitle = (EditText) findViewById(R.id.photo_title);
        mPhotoDescription = (EditText) findViewById(R.id.photo_description);
        mImageView = (ImageView) findViewById(R.id.id_image);
        mImageLL = (LinearLayout) findViewById(R.id.id_image_ll);

        mViewContainer = (LinearLayout) findViewById(R.id.container);

        // Edit post ?
        if (mEditId != null) {
            mViewContainer.setVisibility(View.VISIBLE);
            if (mEditTitle == null)
                mEditTitle = "";
            if (mEditDescription == null)
                mEditDescription = "";
            mPhotoTitle.setText(mEditTitle);
            mPhotoDescription.setText(mEditDescription);
            mImageLL.setVisibility(View.GONE);
        } else {
            loadImageOrTakePhoto();
            mViewContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_upload_gallery_image, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
            return true;
        } else if (itemId == R.id.menu_send_icon) {
            actionPostStatus(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Rotate Image
    public void actionRotateImage(View view) {
        mImageUploader.RotateImage();
        mImageView.setImageBitmap(mImageUploader.getScaledBitmapForWidth(100));
        mImageView.invalidate();
        return;
    }

    // User clicks on Post Status
    public void actionPostStatus(View view) {

        // Avoid repeat call by double tap
        long timeSeconds = SystemClock.elapsedRealtime();
        if ((timeSeconds - mLastClickTime) < 3000) {
            Log.e("PPA", "Double tap");
            return;
        }
        mLastClickTime = timeSeconds;

        String title = mPhotoTitle.getText().toString().trim();
        String description = mPhotoDescription.getText().toString().trim();
        if (title == null || title.equals("")) {
            EzUtils.showLong("Please update photo title");
            return;
        }

        // Create List
        Map<String, String> params = new HashMap<String, String>();
        if (mEditId != null) {
            // edit photo
            params.put("name", title);
            params.put("description", description);
            params.put("id", mEditId);
        } else {
            // new photo
            params.put("name", title);
            params.put("description", description);
            params.put("context_id", SoapNotesController.getAppointment()
                    .getBkid());
            params.put("context_type", "booking");

            // get photo
            String bitmapBase64 = mImageUploader.getImageAsBase64String();
            if (bitmapBase64 == null) {
                EzUtils.showLong("Image can not be uploaded");
                return;
            }
            params.put("image_base64", bitmapBase64);
        }

        EzNetwork network = new EzNetwork();
        mProgressBar.setVisibility(View.VISIBLE);
        network.POST(
                (mEditId == null ? APIs.POST_GALLERY_PHOTO() : APIs
                        .EDIT_GALLERY_PHOTO()), params, new ResponseHandler() {

                    @Override
                    public void cmdResponseError(Integer code) {
                        mProgressBar.setVisibility(View.GONE);
                        if (mEditId == null) {
                            EzUtils.showLong("Image can not be uploaded");
                        } else {
                            EzUtils.showLong("Can not update info");
                        }
                    }

                    @Override
                    public void cmdResponse(JSONObject response, String result) {
                        // update parent data
                        mProgressBar.setVisibility(View.GONE);
                        try {
                            // response is same for edit and upload
                            JSONObject jPhoto = response.getJSONObject("photo");
                            SOAPPhoto photo = new Gson().fromJson(
                                    jPhoto.toString(), SOAPPhoto.class);
                            if (mEditId == null) {
                                // new upload
                                SoapNotesController
                                        .updateSOAPGalleryPhoto(photo);
                                String galleryId = response
                                        .getString("gallery_id");
                                SoapNotesController.setSOAPGalleryId(galleryId);
                            } else {
                                GalleryViewActivity.onPhotoUpdated(photo);
                            }
                            if (mEditId == null) {
                                EzUtils.showLong("Image has been uploaded");
                            } else {
                                EzUtils.showLong("Information has been updated");
                            }
                            finish();
                        } catch (JSONException e) {

                        }
                    }
                });

        // Avoid repeat call by double tap
        mLastClickTime = SystemClock.elapsedRealtime();
    }

    /**
     * Called when the user clicks on Back icon
     **/
    public void onBackPressed() {
        String message = "Are you sure you want to discard it ?";
        if (mEditId != null) {
            String title = mPhotoTitle.getText().toString().trim();
            String description = mPhotoDescription.getText().toString().trim();
            if (title.equals(mEditTitle)
                    && description.equals(mEditDescription))
                finish();
            message = "Are you sure you want to discard changes ?";
        } else {

        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Discard",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause(); // Always call the superclass method first
        Log.v("PPA: ", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop(); // Always call the superclass method first
        Log.v("PPA: ", "onStop");
    }

    @Override
    protected void onDestroy() {
        Log.v("PPA: ", "onDestroy");
        super.onDestroy();
    }

    private Uri mOutputFileUri;

    private void loadImageOrTakePhoto() {

        // Determine Uri of camera image to save.
        mOutputFileUri = EzUtils.getNewCameraImageUri();

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(
                captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName,
                    res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutputFileUri);
            cameraIntents.add(intent);
        }

        // File system.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Chooser of file system options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent,
                "Select Source");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        startActivityForResult(chooserIntent, TAKE_PHOTO_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK || requestCode != TAKE_PHOTO_REQUEST_CODE) {
            Log.v("PPA", "onActivityResult - ResC: " + resultCode + ", ReqC: "
                    + requestCode);
            finish();
            return;
        }
        final boolean isCamera;
        if (data == null) {
            isCamera = true;
        } else {
            final String action = data.getAction();
            if (action == null) {
                isCamera = false;
            } else {
                isCamera = action
                        .equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            }
        }

        Uri imageUri;
        if (isCamera) {
            imageUri = mOutputFileUri;
        } else {
            imageUri = (data == null ? null : data.getData());
        }

        mImageUploader.loadImageFromUri(imageUri, this);
        if (mImageUploader.isBitmapAvailable() == false) {
            Log.v("PPA", "onActivityResult - bitmap is NULL");
            EzUtils.showLong("Image file not found");
            finish();
            return;
        }
        mImageView.setImageBitmap(mImageUploader.getScaledBitmapForWidth(200));
        mImageView.setVisibility(View.VISIBLE);
        mViewContainer.setVisibility(View.VISIBLE);
    }
}

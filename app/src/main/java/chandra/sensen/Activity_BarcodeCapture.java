/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This file and all BarcodeXXX and CameraXXX files in this project edited by
 * Daniell Algar (included due to copyright reason)
 */
package chandra.sensen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public final class Activity_BarcodeCapture extends AppCompatActivity implements BarcodeTracker.BarcodeGraphicTrackerCallback {

    //INIT
    private static final String TAG = "Barcode-reader";
    private static final int RC_HANDLE_GMS = 9001; //Intent request code to handle updating play services if needed.
    private static final int RC_HANDLE_CAMERA_PERM = 2; //Permission request codes need to be < 256
    public static final String BarcodeObject = "Barcode"; //Constants used to pass extra data in the intent
    private CameraSource mCameraSource;
    private CameraSourcePreview mPreview;

    /**
     * Initializes the UI and creates the detector pipeline.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.barcode_capture);
        //INIT
        mPreview = (CameraSourcePreview) findViewById(R.id.qr_preview);
        boolean autoFocus = true;
        boolean useFlash = false;
        //CEK PERMISSION KAMERA
        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        //KALO ADA PERMISSION -> TAMPILIN DISPLAY KAMERA
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource(autoFocus, useFlash);
        }
        //KALO GAADA PERMISSION, REQUEST PERMISSION
        else {
            requestCameraPermission();
        }
    }

    @Override
    public void onDetectedQrCode(Barcode barcode) {
        //KALO NGE-DETECT BARCODE
        if (barcode != null) {
            Intent intent = new Intent();
            intent.putExtra(BarcodeObject, barcode);
            setResult(CommonStatusCodes.SUCCESS, intent);
            finish();
        }
    }

    //HANDLER BUAT REQUEST PERMISSION KAMERA
    private void requestCameraPermission() {
        //INIT
        final String[] permissions = new String[]{Manifest.permission.CAMERA};
        //KALO BELOM ADA PERMISSION -> REQUEST PERMISSION
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
        }
    }

    @SuppressLint("InlinedApi") //CEK MINIMUM VERSION SEBELUM JALANIN FUNGSI
    private void createCameraSource(boolean autoFocus, boolean useFlash) {
        //INIT
        Context context = getApplicationContext();
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(context).setBarcodeFormats(Barcode.QR_CODE).build(); //BUAT OBJEK UNTUK TRACKING BARCODE
        BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory(this); //UNTUK BUAT INSTANCE UNTUK SETIAP BARCODE YANG DIDETEKSI
        barcodeDetector.setProcessor(new MultiProcessor.Builder<>(barcodeFactory).build()); //SET MULTI PROCESSOR UNTUK GUNAIN BARCODE FACTORY
        //KALO BARCODE DETECTORNYA UDAH JALAN (SYARAT: LIBRARY UNTUK DETECTION UDAH DIDONLOT)
        if (!barcodeDetector.isOperational()) {
            //CEK KONDISI STORAGE SEKARANG (LOW APA KAGA)
            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;
            //KALO KONDISI STORAGE LOW
            if (hasLowStorage) {
                Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show();
            }
        }
        //MULAI KAMERA
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        CameraSource.Builder builder = new CameraSource.Builder(getApplicationContext(), barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(metrics.widthPixels, metrics.heightPixels)
                .setRequestedFps(24.0f);
        //KALO VERI ANDROID > I -> GUNAIN AUTO FOCUS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            builder = builder.setFocusMode(autoFocus ? Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE : null);
        }
        mCameraSource = builder.setFlashMode(useFlash ? Camera.Parameters.FLASH_MODE_TORCH : null).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startCameraSource();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPreview != null) {
            mPreview.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPreview != null) {
            mPreview.release();
        }
    }

    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on {@link #requestPermissions(String[], int)}.
     * <p>
     * <strong>Note:</strong> It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     * </p>
     *
     * @param requestCode  The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either {@link PackageManager#PERMISSION_GRANTED}
     *                     or {@link PackageManager#PERMISSION_DENIED}. Never null.
     * @see #requestPermissions(String[], int)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }
        //KALO UDAH ADA PERMISSION
        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //INIT UNTUK KAMERA
            boolean autoFocus = true;
            boolean useFlash = false;
            //TAMPILIN DISPLAY KAMERA
            createCameraSource(autoFocus, useFlash);
            return;
        }
        //CLICK LISTENER
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        };
        //ALERT DIALOG
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pemindai Kode QR").setMessage(R.string.no_camera_permission).setPositiveButton(R.string.ok, listener).show();
    }

    private void startCameraSource() throws SecurityException {
        //INIT
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getApplicationContext());
        //KALO GOOGLE PLAY SERVICE AVAILABLE
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg = GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }
        //KALO KAMERA BELUM DIMULAI
        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource);
            } catch (IOException e) {
                Log.e(TAG, "Tidak dapat menampilkan kamera.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }
}

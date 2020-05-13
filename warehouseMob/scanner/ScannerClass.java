package com.example.warehouse.scanner;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.warehouse.R;
import com.example.warehouse.apiCalls.ApiCallFunctions;
import com.example.warehouse.forms.ItemCreate;
import com.example.warehouse.forms.ShowItemInformation;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.IOException;
import java.util.List;

public class ScannerClass extends AppCompatActivity implements Detector.Processor {
    private TextView textView;
    private SurfaceView surfaceView;
    private CustomBarcodeDetector barcodeDetector;
    private CameraSource cameraSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner_activity);
        surfaceView = (SurfaceView) this.findViewById(R.id.surfaceView);

        barcodeDetector = new CustomBarcodeDetector(this);
        barcodeDetector.setProcessor(this);

        cameraSource = new CameraSource.Builder(getApplicationContext(), barcodeDetector).
                setRequestedPreviewSize(1024, 1024).setAutoFocusEnabled(true)
                .build();

        final Activity activity = this;

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 1024);
                        return;
                    }
                    cameraSource.start(surfaceView.getHolder());
                } catch (IOException ie) {
                    Log.e("Camera start problem", ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
    }

    @Override
    public void release() {

    }

    @Override
    public void receiveDetections(Detector.Detections detections) {
        final SparseArray<Barcode> barcodes = detections.getDetectedItems();
        if (barcodes.size() != 0) {
            surfaceView.post(new Runnable() {
                @Override
                public void run() {
                    cameraSource.release();
                }
            });
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < barcodes.size(); ++i) {
                sb.append(barcodes.valueAt(i).rawValue).append("\n");
            }

            ApiCallFunctions apiCallFunctions = new ApiCallFunctions();
            apiCallFunctions.getItemsByBarcode(new ApiCallFunctions.OnItemsByBarcodeListReceivedCallback() {
                @Override
                public void OnItemsByBarcodeListReceived(List<com.example.warehouse.objects.Barcode> list) {
                    if(list == null || list.isEmpty()){
                        sendToCreateItem(sb.toString());
                    } else {
                        sendToShowItem(list.get(0));
                    }
                }
            }, sb.toString());
        }
    }

    private void sendToCreateItem(String barcode){
        Intent intent = new Intent(this, ItemCreate.class);
        intent.putExtra("barcodeValue", barcode);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void sendToShowItem(com.example.warehouse.objects.Barcode barcode){
        Intent intent = new Intent(this, ItemCreate.class);
        intent.putExtra("barcodeWithItem", barcode);
        startActivity(intent);
        finish();
    }
}

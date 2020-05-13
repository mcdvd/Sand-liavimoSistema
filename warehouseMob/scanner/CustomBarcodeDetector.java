package com.example.warehouse.scanner;

import android.content.Context;
import android.util.SparseArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public class CustomBarcodeDetector extends Detector<Barcode> {

    private BarcodeDetector internalDetector;

    public CustomBarcodeDetector(Context context) {
        this.internalDetector = new BarcodeDetector.Builder(context)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();
    }

    @Override
    public SparseArray<Barcode> detect(Frame frame) {

        SparseArray<Barcode> detected = this.internalDetector.detect(frame);

        if (detected == null) {
            // Nothing detected, kick out an empty array
            return new SparseArray<>();
        }

        if (detected.size() == 0) {
            // Detected size = 0 , kick out an empty array
            return new SparseArray<>();
        }

        if (detected.size() > 1) {
            // Detected more than one barcode, kick out an empty array
            return new SparseArray<>();
        }

        // If we're here, there is only one barcode, return the array
        return detected;
    }
}
package com.google.zxing.extra;

import android.content.Context;
import android.content.Intent;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.CaptureActivity;

public class ScanCapture {

    public static void open(Context context, ScanCaptureResultDelegate delegate) {

        Intent intent = new Intent(context, ScanCaptureActivity.class);
        intent.putExtra(CaptureActivity.KEY_DECODE, new int[]{BarcodeFormat.QR_CODE.ordinal(),BarcodeFormat.EAN_8.ordinal(),BarcodeFormat.EAN_13.ordinal(),BarcodeFormat.CODE_128.ordinal()});
        ScanCaptureResult.setDelagate(delegate);
        context.startActivity(intent);
    }
}

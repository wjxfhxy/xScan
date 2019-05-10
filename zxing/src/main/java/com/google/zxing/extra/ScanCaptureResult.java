package com.google.zxing.extra;

public class ScanCaptureResult  {

    private static ScanCaptureResultDelegate delegate;

    public ScanCaptureResult() {

    }

    public static void setDelagate(ScanCaptureResultDelegate delegate) {

        ScanCaptureResult.delegate = delegate;
    }

    static ScanCaptureResultDelegate getDelegate() {

        return ScanCaptureResult.delegate;
    }

    static void cleanDelegate() {

        ScanCaptureResult.delegate = null;
    }
}

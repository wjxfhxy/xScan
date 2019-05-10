package com.google.zxing.extra;

public interface ScanCaptureResultDelegate {

    void scanResult(String result);

    boolean isContinueScan();
}

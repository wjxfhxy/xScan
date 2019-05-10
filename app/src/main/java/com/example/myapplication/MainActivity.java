package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//import com.skateboard.zxinglib.CaptureActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.extra.ScanCapture;
import com.google.zxing.extra.ScanCaptureActivity;
import com.google.zxing.CaptureActivity;
import com.google.zxing.extra.ScanCaptureResult;
import com.google.zxing.extra.ScanCaptureResultDelegate;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startBtn = findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ScanCapture.open(MainActivity.this, new ScanCaptureResultDelegate() {

                    @Override
                    public void scanResult(String content) {

                        Toast.makeText(MainActivity.this, "qrcode result is " + content, 3).show();
                    }

                    @Override
                    public boolean isContinueScan() {

                        return true;
                    }
                });

           //     startActivityForResult(intent, 1001);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            String result = data.getStringExtra(CaptureActivity.KEY_DATA);
            Toast.makeText(this, "qrcode result is " + result, Toast.LENGTH_SHORT).show();
        }
    }
}

package com.google.zxing.extra;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.util.Hashtable;

public class ScanImage {

    public static void decodeAsync(final String picPath, final ScanImageDecodeResult result) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                final String r = ScanImage.decode(picPath);
                result.decodeResult(r);
            }
        }).start();
    }

    public static String decode(String picPath) {

        Bitmap bitmap = QRCodeUtil.getDecodeAbleBitmap(picPath);

        if(bitmap != null) {

//            int[] data = new int[bitmap.getWidth() * bitmap.getHeight()];
//            bitmap.getPixels(data, 0, bitmap.getWidth(), 0,0,bitmap.getWidth(), bitmap.getHeight());
//
//            RGBLuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(),bitmap.getHeight(),data);
//
//            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
//            QRCodeReader reader = new QRCodeReader();
//
//            Result result = null;
//            try {
//                Hashtable<DecodeHintType, String> hints = new Hashtable<>();
//                hints.put(DecodeHintType.CHARACTER_SET, "UTF8");
//                result = reader.decode(binaryBitmap, hints);
//            }
//            catch (NotFoundException e) {
//            }
//            catch (ChecksumException e) {
//            }
//            catch (FormatException e) {
//
//            }

            String result = QRCodeDecoder.syncDecodeQRCode(bitmap);

            return result;
        }

        return null;
    }
}

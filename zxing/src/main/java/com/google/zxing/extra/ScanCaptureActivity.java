/*
 * Copyright (C) 2008 ZXing authors
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
 */

package com.google.zxing.extra;

import android.app.ActionBar;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.zxing.CaptureActivity;
import com.google.zxing.R;

public final class ScanCaptureActivity extends CaptureActivity  {

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onNavigateUp() {

        ScanCaptureResult.cleanDelegate();
        finish();
        return super.onNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_1) {

            scanAlbumImage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static final int CODE_GALLERY_REQUEST = 233;
    public void scanAlbumImage() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") == 0 &&
                ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") == 0) {
            Intent intentFromGallery = new Intent();
            intentFromGallery.setType("image/*");
            intentFromGallery.setAction("android.intent.action.PICK");
            this.startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
        } else {
            Toast.makeText(this, "未能获取读取相册权限", 0).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            switch(requestCode) {
                case CODE_GALLERY_REQUEST:
                    Uri uri = data.getData();
                    String[] filePathColumn = new String[]{"_data"};
                    if (null == filePathColumn) {
                        return;
                    }

                    Cursor cursor = this.getContentResolver().query(this.getFileUri(uri), (String[])null, (String)null, (String[])null, (String)null);
                    cursor.moveToFirst();
                    String photoPath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
                    cursor.close();

                    ScanImage.decodeAsync(photoPath, new ScanImageDecodeResult() {
                        @Override
                        public void decodeResult(final String result) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    handleResult(result);
                                }
                            });
                        }
                    });
            }
        }
    }

    private Uri getFileUri(Uri uri) {
        try {
            if (uri.getScheme().equals("file")) {
                String path = uri.getEncodedPath();
                if (path != null) {
                    path = Uri.decode(path);
                    ContentResolver cr = this.getContentResolver();
                    StringBuffer buff = new StringBuffer();
                    buff.append("(").append("_data").append("=").append("'" + path + "'").append(")");
                    Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_id"}, buff.toString(), (String[])null, (String)null);
                    int index = 0;
                    cur.moveToFirst();

                    while(!cur.isAfterLast()) {
                        index = cur.getColumnIndex("_id");
                        index = cur.getInt(index);
                        cur.moveToNext();
                    }

                    if (index != 0) {
                        Uri uri_temp = Uri.parse("content://media/external/images/media/" + index);
                        if (uri_temp != null) {
                            uri = uri_temp;
                        }
                    }
                }
            }
        } catch (Exception var8) {
            ;
        }

        return uri;
    }

    @Override
    public void handleResult(String result) {

        ScanCaptureResult.getDelegate().scanResult(result);

        if(ScanCaptureResult.getDelegate().isContinueScan()) {

            restartPreviewAfterDelay(1000L);
            return;
        }

        ScanCaptureResult.cleanDelegate();
        finish();
    }
}

package com.lrchao.cropimageviewsample;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private static final int REQUEST_CODE_ALBUM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_album).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_album:
                startActivityForResult(createJumpIntoSystemAblumIntent(), REQUEST_CODE_ALBUM);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_ALBUM:
                String filePath = getFilePathFromIntent(data);
                Intent startIntent = new Intent(this, CropImageViewActivity.class);
                startIntent.putExtra("filePath", filePath);
                startActivity(startIntent);
                break;

            default:
                break;
        }
    }

    /**
     * 返回跳转到系统相册页的 intent
     */
    public static Intent createJumpIntoSystemAblumIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");

        return intent;
    }

    private String getFilePathFromIntent(Intent intent) {
        Uri uri = intent.getData();
        String imgPath = null;
        if (uri == null) {
            return "";
        }
        String[] proj = {MediaStore.Images.Media.DATA};
        if ("content".equals(uri.getScheme())) {
            @SuppressWarnings("deprecation") Cursor cursor = managedQuery(uri, proj, null,
                    null, null);
            if (cursor != null) {
                int actual_image_column_index = cursor.getColumnIndexOrThrow(proj[0]);
                cursor.moveToFirst();
                imgPath = cursor.getString(actual_image_column_index);
            }
        } else if ("file".equals(uri.getScheme())) {
            imgPath = uri.getPath();
        }

        if (TextUtils.isEmpty(imgPath)) {
            imgPath = null;
        }
        return imgPath;
    }
}

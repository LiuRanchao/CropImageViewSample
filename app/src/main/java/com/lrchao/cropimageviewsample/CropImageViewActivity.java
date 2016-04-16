package com.lrchao.cropimageviewsample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.lrchao.cropimageview.CropImageView;

/**
 * Description: 裁剪
 *
 * @author liuranchao
 * @date 16/4/16 下午3:25
 */
public class CropImageViewActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CropImageViewActivity";

    private CropImageView mCropImageView;

    private ImageView mIvShow;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);
        mIvShow = (ImageView) findViewById(R.id.iv_show);
        findViewById(R.id.btn_crop).setOnClickListener(this);
        String filePath = getIntent().getStringExtra("filePath");
        Log.e(TAG, "File Path=" + filePath);

        mCropImageView = (CropImageView) findViewById(R.id.crop_image_view);

        Bitmap bitmap = decodeSampledBitmapFromFile(filePath, getDeviceWidthPixels(this), getDeviceHeightPixels(this));
        mCropImageView.setImageBitmap(bitmap);
    }

    private Bitmap decodeSampledBitmapFromFile(String filePath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
            final float totalPixels = width * height;
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;
            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    /**
     * 获取设备的宽度像素点
     *
     * @param context
     * @return
     */
    public static int getDeviceWidthPixels(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取设备的高度像素点
     *
     * @param context
     * @return
     */
    public static int getDeviceHeightPixels(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_crop:
                Bitmap bitmap = mCropImageView.getCroppedImage();
                mIvShow.setImageBitmap(bitmap);
                break;
            default:
                break;
        }
    }
}

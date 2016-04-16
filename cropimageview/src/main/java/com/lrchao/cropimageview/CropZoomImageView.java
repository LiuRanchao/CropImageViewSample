package com.lrchao.cropimageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Description: 截取图片缩放的View.
 *
 * @author liuranchao
 * @date 16/4/16 下午4:06
 */
public class CropZoomImageView extends ImageView {

    private static final String TAG = "CropZoomImageView";

    /**
     * 放大最大倍数
     */
    //private static final float MAX_SCALE_VALUE = 2f;

    /**
     * 缩小最大倍数
     */
    //private static final float MIN_SCALE_VALUE = 1f;

    private int mWidth;
    private int mHeight;

    private boolean mIsMeasured;

    /**
     * 缩放比
     */
    private float scaleWidth;

    public CropZoomImageView(Context context) {
        super(context);
        init();
    }

    public CropZoomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 初始化View
     */
    public void init() {
        this.setImageMatrix(matrix);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        mHeight = View.MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(mWidth, mHeight);

        if (!mIsMeasured && mWidth > 0 && mHeight > 0) {
            setBitmap(mSourceBitmap);
            mIsMeasured = true;
        }
    }

    private Bitmap mSourceBitmap;

    /**
     * Description: 设置Bitmap，并初始化位置
     *
     * @param bmp Bitmap
     */
    public void setBitmap(Bitmap bmp) {
        mSourceBitmap = bmp;
        if (bmp == null || mWidth <= 0) {
            return;
        }
        setImageBitmap(bmp);

        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();

        // 获取图片缩放比
        scaleWidth = (float) mWidth / 2 / bmpWidth;

        // 将图片缩放到屏幕的宽度的一半
        matrix.postScale(scaleWidth, scaleWidth);

        // 获取缩放后的图片的X坐标
        float translateX = (float) mWidth / 2 - (float) bmpWidth * scaleWidth / 2;
        // 获取缩放后的图片的Y坐标
        float translateY = (float) mHeight / 2 - (float) bmpHeight * scaleWidth / 2;

        // 将图片设置到中心位置
        matrix.postTranslate(translateX, translateY);

        this.setImageMatrix(matrix);
        invalidate();
    }

    /**
     * 获取矩形区域内的截图
     * @param rect Rect
     * @return Bitmap
     */
    public Bitmap getBitmap(Rect rect) {
        if (rect == null || rect.width() <= 0 || rect.height() <= 0) {
            return null;
        }

        Log.e(TAG, "rect=" + rect.toString());
        this.setDrawingCacheEnabled(true);
        this.buildDrawingCache();
        Bitmap screenShoot = this.getDrawingCache();
        int width = rect.width();
        Bitmap finalBitmap = null;
        try {
            finalBitmap = Bitmap.createBitmap(screenShoot, rect.left, rect.top, width, width);

        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.setDrawingCacheEnabled(false);
        return finalBitmap;
    }

    // These matrices will be used to move and zoom image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;

    /* 这里实现了多点触摸放大缩小，和单点移动图片的功能，参考了论坛的代码 */
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                // 設置初始點位置
                start.set(event.getX(), event.getY());
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                Log.d(TAG, "oldDist=" + oldDist);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    // ...
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
                } else if (mode == ZOOM) {
                    // 两点间变化距离
                    float newDist = spacing(event);

                    if (newDist > 10f) {
                        // 此次缩放比
                        float scale = newDist / oldDist;
                        matrix.set(savedMatrix);
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }
        this.setImageMatrix(matrix);
        return true; // indicate event was handled
    }


    /**
     * Determine the space between the first two fingers
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }
}

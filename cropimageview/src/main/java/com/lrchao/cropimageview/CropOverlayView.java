package com.lrchao.cropimageview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;

import com.lrchao.cropimageview.utils.AspectRatioUtils;
import com.lrchao.cropimageview.utils.Edge;
import com.lrchao.cropimageview.utils.PaintUtils;

/**
 * Description: 选取头像的覆盖层.
 *
 * @author liuranchao
 * @date 16/4/16 下午3:58
 */
public class CropOverlayView extends View {

    /**
     * 截取的类型
     */
    protected static final int CLIP_IMAGE_TYPE_CIRCLE = 1; // 圆形
    protected static final int CLIP_IMAGE_TYPE_RECTANGLE = 2; // 矩形

    //private static final String TAG = "CropOverlayView";
    // Member Variables ////////////////////////////////////////////////////////

    // The Paint used to draw the white rectangle around the crop area.
    private Paint mBorderPaint;

    // The Paint used to darken the surrounding areas outside the crop area.
    private Paint mBackgroundPaint;

    // The bounding box around the Bitmap that we are cropping.
    private Rect mBitmapRect;

    // Flag indicating if the crop area should always be a certain aspect ratio
    // (indicated by mTargetAspectRatio).
    private boolean mFixAspectRatio = CropImageView.DEFAULT_FIXED_ASPECT_RATIO;

    // Floats to save the current aspect ratio of the image
    private int mAspectRatioX = CropImageView.DEFAULT_ASPECT_RATIO_X;
    private int mAspectRatioY = CropImageView.DEFAULT_ASPECT_RATIO_Y;

    // The aspect ratio that the crop area should maintain; this variable is
    // only used when mMaintainAspectRatio is true.
    private float mTargetAspectRatio = ((float) mAspectRatioX) / mAspectRatioY;

    // Whether the Crop View has been initialized for the first time
    private boolean initializedCropWindow = false;

    // 画完监听器
    private OnDrawCropOverlayViewFinishListener mOnDrawCropOverlayViewFinishListener;

    /**
     * 显示类型
     */
    private int mType = CLIP_IMAGE_TYPE_CIRCLE;

    public void setOnDrawCropOverlayViewFinishListener(
            OnDrawCropOverlayViewFinishListener onDrawCropOverlayViewFinishListener) {
        mOnDrawCropOverlayViewFinishListener = onDrawCropOverlayViewFinishListener;
    }

    // Constructors ////////////////////////////////////////////////////////////

    public CropOverlayView(Context context) {
        super(context);
        init(context);
    }

    public CropOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    // View Methods ////////////////////////////////////////////////////////////

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        // Initialize the crop window here because we need the size of the view
        // to have been determined.
        initCropWindow(mBitmapRect);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        // Draw translucent background for the cropped area.
        drawBackground(canvas);

        // Draw the circular border
        float cx = (Edge.LEFT.getCoordinate() + Edge.RIGHT.getCoordinate()) / 2;
        float cy = (Edge.TOP.getCoordinate() + Edge.BOTTOM.getCoordinate()) / 2;
        float radius = (Edge.RIGHT.getCoordinate() - Edge.LEFT.getCoordinate()) / 2 / 2;

        // KevinLau 画圆的边框
        canvas.drawCircle(cx, cy, radius, mBorderPaint);
    }

    // Public Methods //////////////////////////////////////////////////////////

    /**
     * Informs the CropOverlayView of the image's position relative to the
     * ImageView. This is necessary to call in order to draw the crop window.
     *
     * @param bitmapRect the image's bounding box
     */
    public void setBitmapRect(Rect bitmapRect) {
        if (bitmapRect == null) {
            return;
        }
        mBitmapRect = bitmapRect;
        initCropWindow(mBitmapRect);
    }

    /**
     * 设置覆盖的类型
     *
     * @param type 显示类型 圆形或者正方形
     */
    public void setOverlayViewType(int type) {
        mType = type;
    }

    /**
     * Resets the crop overlay view.
     * <p/>
     * bitmap the Bitmap to set
     */
    public void resetCropOverlayView() {

        if (initializedCropWindow) {
            initCropWindow(mBitmapRect);
            invalidate();
        }
    }

    /**
     * Sets whether the aspect ratio is fixed or not; true fixes the aspect
     * ratio, while false allows it to be changed.
     *
     * @param fixAspectRatio Boolean that signals whether the aspect ratio
     *                       should be maintained.
     */
/*    public void setFixedAspectRatio(boolean fixAspectRatio)
    {
        mFixAspectRatio = fixAspectRatio;

        if (initializedCropWindow)
        {
            initCropWindow(mBitmapRect);
            invalidate();
        }
    }*/

    /**
     * Sets the X value of the aspect ratio; is defaulted to 1.
     *
     * @param aspectRatioX int that specifies the new X value of the aspect
     *                     ratio
     */
/*    public void setAspectRatioX(int aspectRatioX)
    {
        if (aspectRatioX <= 0)
        {
            throw new IllegalArgumentException(
                    "Cannot set aspect ratio value to a number less than or equal to 0.");
        }
        else
        {
            mAspectRatioX = aspectRatioX;
            mTargetAspectRatio = ((float) mAspectRatioX) / mAspectRatioY;

            if (initializedCropWindow)
            {
                initCropWindow(mBitmapRect);
                invalidate();
            }
        }
    }*/

    /**
     * Sets the Y value of the aspect ratio; is defaulted to 1.
     *
     * @param aspectRatioY int that specifies the new Y value of the aspect
     *                     ratio
     */
/*    public void setAspectRatioY(int aspectRatioY)
    {
        if (aspectRatioY <= 0)
        {
            throw new IllegalArgumentException(
                    "Cannot set aspect ratio value to a number less than or equal to 0.");
        }
        else
        {
            mAspectRatioY = aspectRatioY;
            mTargetAspectRatio = ((float) mAspectRatioX) / mAspectRatioY;

            if (initializedCropWindow)
            {
                initCropWindow(mBitmapRect);
                invalidate();
            }
        }
    }*/

    /**
     * Sets all initial values, but does not call initCropWindow to reset the
     * views. Used once at the very start to initialize the attributes.
     *
     * @param fixAspectRatio Boolean that signals whether the aspect ratio
     *                       should be maintained.
     * @param aspectRatioX   float that specifies the new X value of the aspect
     *                       ratio
     * @param aspectRatioY   float that specifies the new Y value of the aspect
     *                       ratio
     */
    public void setInitialAttributeValues(boolean fixAspectRatio, int aspectRatioX,
                                          int aspectRatioY) {
        mFixAspectRatio = fixAspectRatio;

        if (aspectRatioX <= 0) {
            throw new IllegalArgumentException(
                    "Cannot set aspect ratio value to a number less than or equal to 0.");
        } else {
            mAspectRatioX = aspectRatioX;
            mTargetAspectRatio = ((float) mAspectRatioX) / mAspectRatioY;
        }

        if (aspectRatioY <= 0) {
            throw new IllegalArgumentException(
                    "Cannot set aspect ratio value to a number less than or equal to 0.");
        } else {
            mAspectRatioY = aspectRatioY;
            mTargetAspectRatio = ((float) mAspectRatioX) / mAspectRatioY;
        }

    }

    // Private Methods /////////////////////////////////////////////////////////

    private void init(Context context) {
        mBorderPaint = PaintUtils.newBorderPaint(context);
        mBackgroundPaint = PaintUtils.newBackgroundPaint();
    }

    /**
     * Set the initial crop window size and position. This is dependent on the
     * size and position of the image being cropped.
     *
     * @param bitmapRect the bounding box around the image being cropped
     */
    private void initCropWindow(Rect bitmapRect) {

        // Tells the attribute functions the crop window has already been
        // initialized
        if (initializedCropWindow == false) {
            initializedCropWindow = true;
        }

        if (mFixAspectRatio) {

            // If the image aspect ratio is wider than the crop aspect ratio,
            // then the image height is the determining initial length. Else,
            // vice-versa.
            if (AspectRatioUtils.calculateAspectRatio(bitmapRect) > mTargetAspectRatio) {

                Edge.TOP.setCoordinate(bitmapRect.top);

                Edge.BOTTOM.setCoordinate(bitmapRect.bottom);

                final float centerX = getWidth() / 2f;

                // Limits the aspect ratio to no less than 40 wide or 40 tall
                final float cropWidth = Math.max(Edge.MIN_CROP_LENGTH_PX,
                        AspectRatioUtils.calculateWidth(
                                Edge.TOP.getCoordinate(),
                                Edge.BOTTOM.getCoordinate(),
                                mTargetAspectRatio));

                // Create new TargetAspectRatio if the original one does not fit
                // the screen
                if (cropWidth == Edge.MIN_CROP_LENGTH_PX) {
                    mTargetAspectRatio = (Edge.MIN_CROP_LENGTH_PX) / (Edge.BOTTOM.getCoordinate() - Edge.TOP.getCoordinate());
                }

                final float halfCropWidth = cropWidth / 2f;
                Edge.LEFT.setCoordinate(centerX - halfCropWidth);
                Edge.RIGHT.setCoordinate(centerX + halfCropWidth);

            } else {

                Edge.LEFT.setCoordinate(bitmapRect.left);
                Edge.RIGHT.setCoordinate(bitmapRect.right);

                final float centerY = getHeight() / 2f;

                // Limits the aspect ratio to no less than 40 wide or 40 tall
                final float cropHeight = Math.max(Edge.MIN_CROP_LENGTH_PX,
                        AspectRatioUtils.calculateHeight(
                                Edge.LEFT.getCoordinate(),
                                Edge.RIGHT.getCoordinate(),
                                mTargetAspectRatio));

                // Create new TargetAspectRatio if the original one does not fit
                // the screen
                if (cropHeight == Edge.MIN_CROP_LENGTH_PX) {
                    mTargetAspectRatio = (Edge.RIGHT.getCoordinate() - Edge.LEFT.getCoordinate()) / Edge.MIN_CROP_LENGTH_PX;
                }

                final float halfCropHeight = cropHeight / 2f;
                Edge.TOP.setCoordinate(centerY - halfCropHeight);
                Edge.BOTTOM.setCoordinate(centerY + halfCropHeight);
            }

        } else { // ... do not fix aspect ratio...

            // Initialize crop window to have 10% padding w/ respect to image.
            final float horizontalPadding = 0.1f * bitmapRect.width();
            final float verticalPadding = 0.1f * bitmapRect.height();

            Edge.LEFT.setCoordinate(bitmapRect.left + horizontalPadding);
            Edge.TOP.setCoordinate(bitmapRect.top + verticalPadding);
            Edge.RIGHT.setCoordinate(bitmapRect.right - horizontalPadding);
            Edge.BOTTOM.setCoordinate(bitmapRect.bottom - verticalPadding);
        }
    }

    /**
     * 圆心x
     */
    private float mCx;

    /**
     * 圆心y
     */
    private float mCy;

    /**
     * 圆半径
     */
    private float mRadius;

    /**
     * 获取选中的矩形区域
     */
    private Rect getCircleRect() {
        int left = (int) (mCx - mRadius);

        int top = (int) (mCy - mRadius);

        int right = (int) (mCx + mRadius);

        int bottom = (int) (mCy + mRadius);
        Rect rect = new Rect(left, top, right, bottom);
        return rect;
    }

    /**
     * 获取正方形的选择区域
     */
    private Rect getSquareRect() {
        return new Rect((int) mRectLeft, (int) mRectTop, (int) mRectRight, (int) mRectBottom);
    }

    /**
     * 获取选中区域
     */
    public Rect getCropRect() {
        Rect rect;
        if (mType == CLIP_IMAGE_TYPE_RECTANGLE) {
            rect = getSquareRect();
        } else if (mType == CLIP_IMAGE_TYPE_CIRCLE) {
            rect = getCircleRect();
        } else {
            rect = getCircleRect();
        }
        return rect;
    }

    /**
     * 画背景
     *
     * @param canvas 画布
     */
    private void drawBackground(Canvas canvas) {

        Path fullCanvasPath = new Path();

        // 画整个画布区域
        fullCanvasPath.addRect(0, 0, getWidth(), getHeight(), Path.Direction.CW);

        Path selectionPath;
        if (mType == CLIP_IMAGE_TYPE_RECTANGLE) {
            selectionPath = drawRectPath();
        } else if (mType == CLIP_IMAGE_TYPE_CIRCLE) {
            selectionPath = drawCircle();
        } else {
            selectionPath = drawCircle();
        }

        canvas.clipPath(fullCanvasPath);
        canvas.clipPath(selectionPath, Region.Op.DIFFERENCE);

        //Draw semi-transparent background
        // 画阴影部分
        canvas.drawRect(0, 0, getWidth(), getHeight(), mBackgroundPaint);

        if (mOnDrawCropOverlayViewFinishListener != null) {
            mOnDrawCropOverlayViewFinishListener.onDrawCropOverlayViewFinish(mRadius);
        }
    }

    /**
     * 矩形的left
     */
    private float mRectLeft;

    /**
     * 矩形的top
     */
    private float mRectTop;

    /**
     * 矩形的bottom
     */
    private float mRectBottom;

    /**
     * 矩形的right
     */
    private float mRectRight;


    /**
     * 画矩形
     */
    private Path drawRectPath() {
        // 即是 矩形的内圆半径
        mRectTop = getHeight() / 2 / 2;
        mRectBottom = mRectTop + mRectTop * 2;
        mRectLeft = getWidth() / 2 - mRectTop;
        mRectRight = mRectLeft + mRectTop * 2;

        Path rectSelectionPath = new Path();
        // 画矩形的选取区域
        rectSelectionPath.addRect(mRectLeft, mRectTop, mRectRight, mRectBottom, Path.Direction.CCW);
        return rectSelectionPath;
    }

    /**
     * 画圆的显示区域
     */
    private Path drawCircle() {
        mCx = getWidth() / 2;
        mCy = getHeight() / 2;
        mRadius = getWidth() / 2 / 2;

        Path circleSelectionPath = new Path();
        // 画圆的选取区域
        circleSelectionPath.addCircle(mCx, mCy, mRadius, Path.Direction.CCW);

        return circleSelectionPath;
    }

    /**
     * 画完覆盖的回调
     */
    public interface OnDrawCropOverlayViewFinishListener {
        void onDrawCropOverlayViewFinish(float radius);
    }
}

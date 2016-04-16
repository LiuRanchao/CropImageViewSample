package com.lrchao.cropimageview.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.TypedValue;

/**
 * Description: 画笔的工具类
 *
 * @author liuranchao
 */
public final class PaintUtils {
    private PaintUtils() {
    }

    // Private Constants ///////////////////////////////////////////////////////

    private static final String SEMI_TRANSPARENT = "#AAFFFFFF";
    private static final String DEFAULT_BACKGROUND_COLOR_ID = "#B0000000";
    private static final float DEFAULT_LINE_THICKNESS_DP = 2;

    // Public Methods //////////////////////////////////////////////////////////

    /**
     * Creates the Paint object for drawing the crop window border.
     *
     * @param context the Context
     * @return new Paint object
     */
    public static Paint newBorderPaint(Context context) {

        // Set the line thickness for the crop window border.
        final float lineThicknessPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                DEFAULT_LINE_THICKNESS_DP,
                context.getResources().getDisplayMetrics());

        final Paint borderPaint = new Paint();
        borderPaint.setColor(Color.parseColor(SEMI_TRANSPARENT));
        borderPaint.setStrokeWidth(lineThicknessPx);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setAntiAlias(true);

        return borderPaint;
    }

    /**
     * Creates the Paint object for drawing the translucent overlay outside the
     * crop window.
     *
     * @return the new Paint object
     */
    public static Paint newBackgroundPaint() {

        final Paint paint = new Paint();
        paint.setColor(Color.parseColor(DEFAULT_BACKGROUND_COLOR_ID));

        return paint;
    }
}

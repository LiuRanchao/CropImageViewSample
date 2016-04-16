package com.lrchao.cropimageview.utils;

import android.graphics.Bitmap;

/**
 * Description: 有关Bitmap的工具方法集合.
 *
 * @author liuranchao
 */
public final class BitmapUtils {

    private BitmapUtils() {
    }

    /**
     * bitmap可不可用
     *
     * @param b Bitmap
     * @return 是否可用
     */
    public static boolean isAvailableBitmap(Bitmap b) {
        return (b != null && !b.isRecycled());
    }

    /**
     * 释放Bitmap
     *
     * @param b bitmap
     */
    public static void recycleBitmap(Bitmap b) {
        if (isAvailableBitmap(b)) {
            b.recycle();
        }
    }
}

package com.lrchao.cropimageview.utils;

import android.graphics.Bitmap;

/**
 * Description: 有关Bitmap的工具方法集合.
 *
 * @author liuranchao
 * @date 16/4/16 下午4:09
 */
public final class BitmapUtils {

    private BitmapUtils() {
    }

    /**
     * bitmap可不可用
     *
     * @param b Bitmap
     */
    public static boolean isAvailableBitmap(Bitmap b) {
        return (b != null && !b.isRecycled());
    }

    /**
     * 释放Bitmap
     */
    public static void recycleBitmap(Bitmap b) {
        if (isAvailableBitmap(b)) {
            b.recycle();
        }
    }
}

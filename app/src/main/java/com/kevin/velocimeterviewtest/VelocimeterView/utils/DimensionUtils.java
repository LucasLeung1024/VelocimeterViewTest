package com.kevin.velocimeterviewtest.VelocimeterView.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * @author Adrián García Lomas
 */
public class DimensionUtils {

    private DimensionUtils() {
        //No instances allowed
    }

    /**
     * dp转px
     * @param dp
     * @param context
     * @return
     */
    public static int getSizeInPixels(float dp, Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float pixels = metrics.density * dp;
        return (int) (pixels + 0.5f);
    }

    /**
     * px 转dp
     * @param context
     * @param sp
     * @return
     */
    public static float pixelsToSp(Context context, float sp) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * scaledDensity;
    }
}

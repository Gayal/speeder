/*
 * Created:         7/29/2016
 * Last Modified:   7/29/2016
 * User:            Chameera V. Murutthettuwegama
 *
 * Copyright (c) WAD International (Pvt)Ltd. All Rights Reserved.
 *
 */

package test.siaxe.speedeo;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;

/**
 * Class to check availability of Network and GPS
 *
 * @author Chameera V. Murutthettuwegama
 * @version 1.0 21st July 2016
 */
public class UISetup {

    public static int deviceHeight;
    public static int deviceWidth;
    static int PSDHEIGHT = 1920;
    static int PSDWIDTH = 1080;

    /**
     * Resize UI element according to device HEIGHT
     *
     * @param objSize object size according to PSD
     * @return new object size
     */
    public static int sizeBaseOnHeight(int objSize) {
        int newSize = (objSize * deviceHeight) / PSDHEIGHT;
        if (newSize < 1)
            newSize = 1;
        return newSize;
    }

    // customize for get widht and height of the device

    public static int getHeight() {
        return deviceHeight;
    }

    public static void setHeight(int height){
        deviceHeight = height;
    }

    public static int getWidth() {
        return deviceWidth;
    }

    public static void setWidth(int width){
        deviceWidth = width;
    }

    /**
     * Resize UI element according to device WIDTH
     *
     * @param objSize object size according to PSD
     * @return new object size
     */
    public static int sizeBaseOnWidth(int objSize) {
        int newSize = (objSize * deviceWidth) / PSDWIDTH;
        if (newSize < 1)
            newSize = 1;
        return newSize;
    }

    /**
     * Identify whether device is TABLET or NOT
     *
     * @param context Application Context
     * @return TRUE / FALSE
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * Bitmap Image
     *
     * @param bt image bitmap
     * @return resized bitmap
     */
    public static Bitmap resize(Bitmap bt) {
        Bitmap bitmapResized = Bitmap.createScaledBitmap(bt,
                deviceWidth, deviceHeight, false);
        return bitmapResized;
    }

}

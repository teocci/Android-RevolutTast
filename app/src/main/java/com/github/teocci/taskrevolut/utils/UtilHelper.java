package com.github.teocci.taskrevolut.utils;

import android.os.Build;

/**
 * Created by Teocci.
 *
 * @author teocci@yandex.com on 2019-Aug-03
 */
public class UtilHelper
{
    public static boolean minAPI18()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    public static boolean minAPI19()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean minAPI21()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean minAPI28()
    {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.P;
    }
}

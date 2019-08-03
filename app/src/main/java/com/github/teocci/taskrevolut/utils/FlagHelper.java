package com.github.teocci.taskrevolut.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;

/**
 * Created by Teocci.
 *
 * @author teocci@yandex.com on 2019-Aug-02
 */
public class FlagHelper
{
    private final static String TAG = LogHelper.makeLogTag(FlagHelper.class);

    private final static String RESOURCE_TYPE = "drawable";
    private final static String RESOURCE_PREFIX = "flag_";

    public static Drawable getDrawableByFlag(Context context, String flagName)
    {
        Resources resources = context.getResources();
        String flagId = RESOURCE_PREFIX + flagName.toLowerCase();
        int resourceId = resources.getIdentifier(flagId, RESOURCE_TYPE, context.getPackageName());

        LogHelper.e(TAG, "flagId: " + flagId + " | resourceId:  " + resourceId);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return resources.getDrawable(resourceId, context.getTheme());
        } else {
            return resources.getDrawable(resourceId);
        }
    }
}

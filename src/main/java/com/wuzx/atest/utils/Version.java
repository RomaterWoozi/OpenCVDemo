package com.wuzx.atest.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

public class Version {
    private static final String TAG = Version.class.getSimpleName();

    private static final String UNKNOWN = "unknown";

    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    static PackageInfo getPackageInfo(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            return packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get find package information for "
                    + context.getPackageName());
        }
    }

    static String getMetadata(Context context, String name) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            String value = applicationInfo.metaData.getString(name);
            if (value != null) {
                return value;
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Version.getMetadata: " + name, e);
        }
        return UNKNOWN;
    }
}

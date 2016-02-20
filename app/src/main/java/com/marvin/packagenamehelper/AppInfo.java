package com.marvin.packagenamehelper;

import android.graphics.drawable.Drawable;

/**
 * Created by MarvinR2D2 on 2016/2/19.
 */
public class AppInfo {
    private Drawable icon;
    private String appName;
    private String packageName;

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}

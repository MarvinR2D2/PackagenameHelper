package com.marvin.packagenamehelper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.util.LruCache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marvin on 2016/2/19.
 */
public class AppInfoProvider {
    private PackageManager pm;
    private ArrayList<ApplicationInfo> mApplications;
    private static final int CACHE_SIZE = 2 * 1024 * 1024;
    private final LruCache<String, Bitmap> mBitmapCache = new LruCache<String, Bitmap>(
            CACHE_SIZE) {
        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes() * value.getHeight();
        }

        @Override
        protected void entryRemoved(boolean evicted, String key,
                                    Bitmap oldValue, Bitmap newValue) {
            super.entryRemoved(evicted, key, oldValue, newValue);
            System.out.println(key + "");
        }
    };

    public AppInfoProvider(Context context) {
        // 拿到一个包管理器
        pm = context.getPackageManager();
    }

    public List<AppInfo> getAllApps() {
        List<AppInfo> list = new ArrayList<AppInfo>();
        Intent mainIntent = new Intent();
        mainIntent.setAction(Intent.ACTION_MAIN);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = pm.queryIntentActivities(
                mainIntent, 0);
        if (apps != null) {
            final int count = apps.size();
            list.clear();
            for (int i = 0; i < count; i++) {
                // get application's icon and packagename
                AppInfo application = new AppInfo();
                ResolveInfo info = apps.get(i);
                String p = info.activityInfo.applicationInfo.packageName;
                // we filter message,email and contacts.Delete the following
                // two lines to get all application
                application.setPackageName(p);
                String className = info.activityInfo.name;
                String key = p + "/" + className;
                Bitmap bitmap = mBitmapCache.get(key);
                if (bitmap == null) {
                    bitmap = drawableToBitmap(info.activityInfo.loadIcon(pm));
                    mBitmapCache.put(key, bitmap);
                }
                @SuppressWarnings("deprecation")
                BitmapDrawable bd = new BitmapDrawable(bitmap);
                application.setIcon(bd);
                // get application's name
                String appname = info.loadLabel(pm).toString();
                application.setAppName(appname);

                    list.add(application);
            }
        }

        return list;
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                           drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

}

package com.mattaniah.wisechildhalacha;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.mattaniah.wisechildhalacha.bookmarking.BookmarkManager;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Mattaniah on 1/17/2016.
 */
public class HalachaApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
         BookmarkManager.getInstance().initialize(this);

    }
}

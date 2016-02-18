package com.mattaniah.wisechildhalacha;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

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

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "qnXfnKMZCz6Xv9m6cQGD6a6AskFfMK5cdS1ACRgt", "1EZ5ZkPSajN0Q5ZtTOI4VzxwDJeiiH6DDeWuw9Hz");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParseUser.enableAutomaticUser();
        ParseUser.getCurrentUser().increment("RunCount");
        ParseUser.getCurrentUser().saveInBackground();
    }
}

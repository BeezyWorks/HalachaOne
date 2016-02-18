package com.mattaniah.wisechildhalacha.helpers;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by Mattaniah on 1/24/2016.
 */
public class CommonIntents {
    Activity context;

    public CommonIntents(Activity context) {
        this.context = context;
    }

    public Intent contactUsIntent() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"mattaniahbeezy@gmail.com"});

        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(
                    context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        i.putExtra(Intent.EXTRA_SUBJECT, "Halacha v" + info.versionName);
        return i;
    }

    public void safeLaunchIntent(Intent intent, String purpose){
        if (intent.resolveActivity(context.getPackageManager()) != null)
            context.startActivity(intent);
        else
            context.startActivity(Intent.createChooser(intent, purpose));
    }
}

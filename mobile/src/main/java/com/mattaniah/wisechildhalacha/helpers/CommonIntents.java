package com.mattaniah.wisechildhalacha.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.mattaniah.wisechildhalacha.R;
import com.mattaniah.wisechildhalacha.goaltracking.TimeTracker;

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
            i.putExtra(Intent.EXTRA_SUBJECT, "Halacha v" + info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            i.putExtra(Intent.EXTRA_SUBJECT, "Halacha"+"version error");
        }


        return i;
    }

    public Intent shareStatsIntent(TimeTracker timeTracker) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        }

        final String url ="http://tinyurl.com/hkbocdc";
        intent.putExtra(Intent.EXTRA_SUBJECT, String.format("%s %s", context.getString(R.string.iveLearnedString), timeTracker.getTotalTimeAsString()));
        intent.putExtra(Intent.EXTRA_TEXT, String.format("%s %s %s %s. %s %s", context.getString(R.string.iveLearnedString), timeTracker.getTotalTimeAsString(), context.getString(R.string.withString), context.getString(R.string.app_name), context.getString(R.string.checkItOutAt), url));

        return intent;
    }

    public static void safeLaunchIntent(Context context, Intent intent, String purpose){
        if (intent.resolveActivity(context.getPackageManager()) != null)
            context.startActivity(intent);
        else
            context.startActivity(Intent.createChooser(intent, purpose));
    }
}

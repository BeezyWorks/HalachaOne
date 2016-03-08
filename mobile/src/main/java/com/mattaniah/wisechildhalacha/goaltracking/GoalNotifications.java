package com.mattaniah.wisechildhalacha.goaltracking;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;

import com.mattaniah.wisechildhalacha.R;
import com.mattaniah.wisechildhalacha.activities.MainActivity;
import com.mattaniah.wisechildhalacha.activities.StatsActivity;
import com.mattaniah.wisechildhalacha.helpers.SettingsUtil;

/**
 * Created by Mattaniah on 1/24/2016.
 */
public class GoalNotifications {
    Context context;
    NotificationManagerCompat notificationManager;
    public static final int goalNotiId = 22;

//    private final String goalMetKey = "goalMet";

    public GoalNotifications(Context context) {
        this.context = context;
        notificationManager = NotificationManagerCompat.from(context);
    }

    public void setRelaventNotification() {
        if (!new SettingsUtil(context).isGoalNotificaitonEnabled())
            return;

        TimeTracker timeTracker = new TimeTracker(context);

        float timeSoFarToday = timeTracker.getTimeSoFarToday();
        float goalSetTime = new SettingsUtil(context).getGoalTime() * 60;
        float percent = (timeSoFarToday * 100.0f) / goalSetTime;
        int timeLeft = (int) (goalSetTime - timeSoFarToday) / 60;
        NotificationCompat.Builder notificationBuilder = getBasicNotification();

        String todayKey = TimeTracker.getTodayKey();
        String key = null;

        if (percent <= 50) {
            key = todayKey + "chizuk";
            notificationBuilder.setContentTitle(context.getString(R.string.app_name));
            notificationBuilder.setContentText(context.getString(R.string.chizukText));
        }
        if (percent >= 80) {
            key = todayKey + "almostThere";
            notificationBuilder.setContentTitle("Almost There!");
            if (timeLeft == 0)
                timeLeft = 1;
            notificationBuilder.setContentText(String.format(context.getString(R.string.almostThereText), timeLeft));
        }

        if (percent >= 100) {
            key = todayKey + "goalMet";
            notificationBuilder.setContentTitle("Shkoyach!");
            notificationBuilder.setContentText(context.getString(R.string.goalMetText));
        }

        if (percent >= 150) {
            key = todayKey + "goalExceeded";
            notificationBuilder.setContentTitle("Whoa!");
            notificationBuilder.setContentText(String.format(context.getString(R.string.goalExceededText), Math.abs(timeLeft)));
        }

        if (percent >= 200) {
            key = todayKey + "farExceeded";
            notificationBuilder.setContentTitle("Masmid!");
            notificationBuilder.setContentText(String.format(context.getString(R.string.farExceededText), +Math.abs(timeLeft)));
        }

        if (key == null)
            return;

        if (!isMessagePostedToday(key)) {
            notificationManager.notify(goalNotiId, notificationBuilder.build());
            setMessagePostedToday(key);
        }
    }

    private boolean isMessagePostedToday(String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, false);
    }

    private void setMessagePostedToday(String key) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(key, true).apply();
    }

    private NotificationCompat.Builder getBasicNotification() {
        Intent launchIntent = new Intent(context, MainActivity.class);

        Intent adjustGoalIntent = new Intent(context, StatsActivity.class);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setSmallIcon(R.drawable.ic_launcher_notification)
                .setContentIntent(PendingIntent.getActivity(context, 0, launchIntent, 0))
                .setColor(ContextCompat.getColor(context, R.color.primary))
                .addAction(new NotificationCompat.Action.Builder(
                        R.drawable.ic_adjust,
                        "Change Goal",
                        PendingIntent.getActivity(context, 0, adjustGoalIntent, 0))
                        .build());
        return builder;

    }
}

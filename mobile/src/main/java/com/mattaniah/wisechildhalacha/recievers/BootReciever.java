package com.mattaniah.wisechildhalacha.recievers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mattaniah.wisechildhalacha.services.GoalNotificationService;

import java.util.Calendar;

/**
 * Created by Mattaniah on 1/25/2016.
 */
public class BootReciever extends BroadcastReceiver {

    int[] timesOfDay = {9,11,14,20};
    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();


        Intent serviceIntent = new Intent(context, GoalNotificationService.class);

        for (int i: timesOfDay){
            calendar.set(Calendar.HOUR_OF_DAY, i);
            alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, PendingIntent.getService(context, i, serviceIntent, 0));
        }
    }
}

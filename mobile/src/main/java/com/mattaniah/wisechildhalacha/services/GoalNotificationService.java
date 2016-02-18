package com.mattaniah.wisechildhalacha.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.mattaniah.wisechildhalacha.goaltracking.GoalNotifications;

/**
 * Created by Mattaniah on 1/25/2016.
 */
public class GoalNotificationService extends Service {
    public static final int KillNotifications=1;
    public static final int RestoreNotifications=2;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        GoalNotifications goalNotifications = new GoalNotifications(this);
        goalNotifications.setRelaventNotification();
        return null;
    }


}

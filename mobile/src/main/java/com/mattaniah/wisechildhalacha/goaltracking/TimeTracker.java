package com.mattaniah.wisechildhalacha.goaltracking;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.mattaniah.wisechildhalacha.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Mattaniah on 1/19/2016.
 */
public class TimeTracker {
    Context context;
    public static String TotalTimeKey = "key_timeinapp";
    public static String DaysKey = "key_days";

    public TimeTracker(Context context) {
        this.context = context;
    }

    public void addTime(long timeOpened) {
        ParseUser user = ParseUser.getCurrentUser();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPreferences.contains(TotalTimeKey)) {
            user.put(TotalTimeKey, sharedPreferences.getLong(TotalTimeKey, 0));
            sharedPreferences.edit().remove(TotalTimeKey).apply();
        }

        long savedTime = user.getLong(TotalTimeKey);
        savedTime += (Calendar.getInstance().getTimeInMillis() - timeOpened);
        user.put(TotalTimeKey, savedTime);

        Map<String, Object> daysMap = getTimeForDaysMap();
        int timeSoFarToday = getTimeSoFarToday();
        timeSoFarToday += (Calendar.getInstance().getTimeInMillis() - timeOpened) / 1000;
        daysMap.put(getTodayKey(), timeSoFarToday);

        user.put(DaysKey, daysMap);

        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("Save Time", "success");
                    notifiyWidgetUpdate();
                } else
                    Log.d("Save Time", e.toString());
            }
        });
    }

    public boolean goalMetToday(){
        return ParseUser.getCurrentUser().getInt(context.getString(R.string.goalTimeKey))<=getTimeSoFarToday();
    }

    public int getTimeSoFarToday() {
        Map<String, Object> dayTimeMap = getTimeForDaysMap();
        String todayKey = getTodayKey();
        Integer mInt= (Integer) dayTimeMap.get(todayKey);
        return mInt==null? 0: mInt;
    }

    protected static String getTodayKey() {
        Calendar calendar = Calendar.getInstance();
        return new SimpleDateFormat("dd_MM_yy").format(calendar.getTime());
    }

    public void notifiyWidgetUpdate() {
        Intent intent = new Intent(context.getApplicationContext(), GoalWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        int[] ids = widgetManager.getAppWidgetIds(new ComponentName(context, GoalWidgetProvider.class));
        widgetManager.notifyAppWidgetViewDataChanged(ids, android.R.id.list);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(intent);
    }

    public long getMilisLearned() {
        return ParseUser.getCurrentUser().getLong(TotalTimeKey);
    }


    private Map<String, Object> getTimeForDaysMap() {
       ParseUser user = ParseUser.getCurrentUser();
        Map retMapt = user.getMap(DaysKey);
        return retMapt==null? new HashMap<>():retMapt;
    }

    private Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<>();

        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);
            map.put(key, value);
        }
        return map;
    }


    public String getTotalTimeAsString() {
        long timeInMilliSeconds = getMilisLearned();

        long seconds = timeInMilliSeconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        String dayString = days > 0 ? String.valueOf(days) : null;
        String hourString = hours > 0 ? String.valueOf(hours % 24) : null;
        String minutesString = minutes > 0 ? String.valueOf(minutes % 60) : null;
        String secondsString = String.valueOf(seconds % 60);

        StringBuilder builder = new StringBuilder();
        if (dayString != null)
            builder.append(dayString).append(" days ");
        if (hourString != null)
            builder.append(hourString).append(" hours ");
        if (minutesString != null && dayString == null)
            builder.append(minutesString).append(" minutes");
        if (builder.length() == 0)
            builder.append(secondsString).append(" seconds");
        return builder.toString();
    }



}
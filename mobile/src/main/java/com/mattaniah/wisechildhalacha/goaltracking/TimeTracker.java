package com.mattaniah.wisechildhalacha.goaltracking;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mattaniah.wisechildhalacha.helpers.SettingsUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Mattaniah on 1/19/2016.
 */
public class TimeTracker {
    Context context;
    SharedPreferences sharedPreferences;
    public static String TotalTimeKey = "key_timeinapp";
    public static String DaysKey = "key_days";

    public TimeTracker(Context context) {
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public long getTotalTime() {
        return sharedPreferences.getLong(TotalTimeKey, 0);
    }

    @SuppressLint("CommitPrefEdits")
    public void putTotalTime(long totalTime) {
        sharedPreferences.edit().putLong(TotalTimeKey, totalTime).commit();
    }

    @SuppressLint("CommitPrefEdits")
    public void addTime(long timeOpened) {
        long savedTime = getTotalTime();
        savedTime += (Calendar.getInstance().getTimeInMillis() - timeOpened);
        putTotalTime(savedTime);

        Map<String, Integer> daysMap = getTimeForDaysMap();
        int timeSoFarToday = getTimeSoFarToday();
        timeSoFarToday += (Calendar.getInstance().getTimeInMillis() - timeOpened) / 1000;
        daysMap.put(getTodayKey(), timeSoFarToday);

        sharedPreferences.edit().putString(DaysKey, new Gson().toJson(daysMap)).commit();


        notifiyWidgetUpdate();
    }

    public boolean goalMetToday() {
        return new SettingsUtil(context).getGoalTime() <= getTimeSoFarToday();
    }

    public int getTimeSoFarToday() {
        Map<String, Integer> dayTimeMap = getTimeForDaysMap();
        String todayKey = getTodayKey();
        Integer mInt =  dayTimeMap.get(todayKey);
        return mInt == null ? 0 : mInt;
    }

    protected static String getTodayKey() {
        Calendar calendar = Calendar.getInstance();
        return new SimpleDateFormat("dd_MM_yy", Locale.US).format(calendar.getTime());
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

    private Map<String, Integer> getTimeForDaysMap() {
        Map<String, Integer> retMap = new HashMap<>();
        String jsonString = sharedPreferences.getString(DaysKey, null);
        if (jsonString == null)
            jsonString = new Gson().toJson(retMap);
        retMap = new Gson().fromJson(jsonString, new TypeToken<HashMap<String, Integer>>() {
        }.getType());
        return retMap == null ? new HashMap<String, Integer>() : retMap;
    }


    public String getTotalTimeAsString() {
        long timeInMilliSeconds = getTotalTime();

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
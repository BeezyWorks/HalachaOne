package com.mattaniah.wisechildhalacha.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mattaniah.wisechildhalacha.R;

/**
 * Created by Mattaniah on 7/16/2015.
 */
public class SettingsUtil {
    SharedPreferences sharedPreferences;
    private Context context;

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public SettingsUtil(Context context) {
        this.context = context;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public Sections getSavedSection() {
        String saved = sharedPreferences.getString(Sections.key, Sections.defaultSection.getName());
        for (Sections section : Sections.values())
            if (section.getName().equals(saved))
                return section;
        return Sections.defaultSection;
    }

    public void saveDefaultSection(Sections sections) {
        sharedPreferences.edit().putString(Sections.key, sections.getName()).apply();
    }

    public boolean isGoalNotificaitonEnabled() {
        return sharedPreferences.getBoolean(context.getString(R.string.show_goal_notifications_key), true);
    }

    public void setGoalNotificationEnabled(boolean enabled) {
        sharedPreferences.edit().putBoolean(context.getString(R.string.show_goal_notifications_key), enabled).apply();
    }

    public int getGoalTime() {
        int goalTime = sharedPreferences.getInt(context.getString(R.string.goalTimeKey), 15);
        if (goalTime < 1)
            setGoalTime(15);
        else
            return goalTime;
        return getGoalTime();
    }

    @SuppressLint("CommitPrefEdits")
    public void setGoalTime(int goalTime) {
        sharedPreferences.edit().putInt(context.getString(R.string.goalTimeKey), goalTime).commit();
    }

}

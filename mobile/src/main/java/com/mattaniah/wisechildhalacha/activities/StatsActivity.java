package com.mattaniah.wisechildhalacha.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mattaniah.wisechildhalacha.R;
import com.mattaniah.wisechildhalacha.goaltracking.GoalNotifications;
import com.mattaniah.wisechildhalacha.goaltracking.GoalView;
import com.mattaniah.wisechildhalacha.goaltracking.TimeTracker;
import com.mattaniah.wisechildhalacha.helpers.SettingsUtil;
import com.parse.ParseUser;


public class StatsActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener {

    GoalView goalView;
    ParseUser user;
    TimeTracker timeTracker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats_layout);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.cancel(GoalNotifications.goalNotiId);

        timeTracker = new TimeTracker(this);

        user = ParseUser.getCurrentUser();
        if (!user.has(getString(R.string.goalTimeKey)))
            user.put(getString(R.string.goalTimeKey), 20);

        goalView = new GoalView(this, user.getInt(getString(R.string.goalTimeKey)), timeTracker.getTimeSoFarToday());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.stats_menu);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setTitle(getString(R.string.app_name) + " " + getString(R.string.statsString));
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView statBox = (TextView) findViewById(R.id.statBox);
        statBox.setText(timeTracker.getTotalTimeAsString());

        FrameLayout goalCircleFrame = (FrameLayout) findViewById(R.id.goalCircleFrame);
        goalCircleFrame.addView(goalView);
        goalCircleFrame.setOnClickListener(this);

        View fab = findViewById(R.id.shareButton);
        fab.setOnClickListener(this);

        final SettingsUtil settingsUtil = new SettingsUtil(this);
        SwitchCompat switchCompat = (SwitchCompat) findViewById(R.id.notificationSwitch);
        final TextView switchText = (TextView) findViewById(R.id.notificationText);
        switchCompat.setChecked(settingsUtil.isGoalNotificaitonEnabled());
        switchText.setText(settingsUtil.isGoalNotificaitonEnabled() ? R.string.notificationSwitchTextEnabled : R.string.notificationSwitchTextDisabled);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settingsUtil.setGoalNotificationEnabled(isChecked);
                switchText.setText(isChecked? R.string.notificationSwitchTextEnabled: R.string.notificationSwitchTextDisabled);
            }
        });
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.myGoal:
                setGoal();
                return true;
        }
        return false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shareButton:

                break;
            case R.id.goalCircleFrame:
                setGoal();
                break;
        }
    }

    private void setGoal() {
        View layoutView = getLayoutInflater().inflate(R.layout.number_picker, null);
        TextInputLayout inputLayout = (TextInputLayout) layoutView.findViewById(R.id.inputLayout);
        final EditText numberPicker = (EditText) inputLayout.findViewById(R.id.editText);
        inputLayout.setHint(getString(R.string.myGoalString));
        numberPicker.setText(String.valueOf(user.getInt(getString(R.string.goalTimeKey))));

        new AlertDialog.Builder(this)
                .setView(layoutView)
                .setPositiveButton(R.string.saveString, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int newNumber = Integer.valueOf(numberPicker.getText().toString());
                        if (newNumber < 1)
                            newNumber = 1;
                        user.put(getString(R.string.goalTimeKey), newNumber);
                        user.saveInBackground(null);
                        goalView.setGoalMinutes(newNumber);
                        timeTracker.notifiyWidgetUpdate();
                    }
                })
                .create()
                .show();
    }
}

package com.mattaniah.wisechildhalacha.activities;

import android.annotation.SuppressLint;
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
import com.mattaniah.wisechildhalacha.helpers.CommonIntents;
import com.mattaniah.wisechildhalacha.helpers.SettingsUtil;


public class StatsActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener {

    GoalView goalView;
    TimeTracker timeTracker;
    SettingsUtil settingsUtil;

    @SuppressLint("PrivateResource")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats_layout);

        NotificationManagerCompat.from(this).cancel(GoalNotifications.goalNotiId);

        timeTracker = new TimeTracker(this);
        settingsUtil = new SettingsUtil(this);

        goalView = new GoalView(this, settingsUtil.getGoalTime(), timeTracker.getTimeSoFarToday());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar!=null;
        toolbar.inflateMenu(R.menu.stats_menu);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setTitle(getString(R.string.app_name) + " " + getString(R.string.statsString));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView statBox = (TextView) findViewById(R.id.statBox);
        assert statBox!=null;
        statBox.setText(timeTracker.getTotalTimeAsString());

        FrameLayout goalCircleFrame = (FrameLayout) findViewById(R.id.goalCircleFrame);
        assert goalCircleFrame!=null;
        goalCircleFrame.addView(goalView);
        goalCircleFrame.setOnClickListener(this);

        View fab = findViewById(R.id.shareButton);
        assert fab!=null;
        fab.setOnClickListener(this);

        final SettingsUtil settingsUtil = new SettingsUtil(this);
        SwitchCompat switchCompat = (SwitchCompat) findViewById(R.id.notificationSwitch);
        final TextView switchText = (TextView) findViewById(R.id.notificationText);
        assert switchText!=null;
        assert switchCompat!=null;
        switchCompat.setChecked(settingsUtil.isGoalNotificaitonEnabled());
        switchText.setText(settingsUtil.isGoalNotificaitonEnabled() ? R.string.notificationSwitchTextEnabled : R.string.notificationSwitchTextDisabled);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settingsUtil.setGoalNotificationEnabled(isChecked);
                switchText.setText(isChecked ? R.string.notificationSwitchTextEnabled : R.string.notificationSwitchTextDisabled);
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
                CommonIntents.safeLaunchIntent(this, new CommonIntents(this).shareStatsIntent(timeTracker), getString(R.string.selectShareApp));
                break;
            case R.id.goalCircleFrame:
                setGoal();
                break;
        }
    }

    private void setGoal() {
        FrameLayout parent = new FrameLayout(this);
        View layoutView = getLayoutInflater().inflate(R.layout.number_picker, parent, false);
        TextInputLayout inputLayout = (TextInputLayout) layoutView.findViewById(R.id.inputLayout);
        final EditText numberPicker = (EditText) inputLayout.findViewById(R.id.editText);
        inputLayout.setHint(getString(R.string.myGoalString));
        numberPicker.setText(String.valueOf(settingsUtil.getGoalTime()));

        new AlertDialog.Builder(this)
                .setView(layoutView)
                .setPositiveButton(R.string.saveString, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int newNumber = Integer.valueOf(numberPicker.getText().toString());
                        if (newNumber < 1)
                            newNumber = 1;
                        settingsUtil.setGoalTime(newNumber);
                        goalView.setGoalMinutes(newNumber);
                        timeTracker.notifiyWidgetUpdate();
                    }
                })
                .create()
                .show();
    }
}

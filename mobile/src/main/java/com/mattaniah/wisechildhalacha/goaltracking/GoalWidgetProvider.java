package com.mattaniah.wisechildhalacha.goaltracking;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.widget.RemoteViews;

import com.mattaniah.wisechildhalacha.R;
import com.mattaniah.wisechildhalacha.activities.StatsActivity;
import com.mattaniah.wisechildhalacha.helpers.SettingsUtil;

/**
 * Created by Mattaniah on 1/21/2016.
 */
public class GoalWidgetProvider extends AppWidgetProvider {

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        GoalView myView = new GoalView(context, new SettingsUtil(context).getGoalTime(), new TimeTracker(context).getTimeSoFarToday());
        myView.setWidgetPaint();
        myView.measure(500, 500);
        myView.layout(0, 0, 500, 500);
        Bitmap bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
        myView.draw(new Canvas(bitmap));

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, StatsActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.goal_widget_layout);
            views.setImageViewBitmap(R.id.widgetImage, bitmap);
            views.setOnClickPendingIntent(R.id.widgetImage, pendingIntent);


            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
package com.mattaniah.wisechildhalacha.goaltracking;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.mattaniah.wisechildhalacha.R;

/**
 * Created by Mattaniah on 1/21/2016.
 */
public class GoalView extends View {
    private Context context;
    private int goalMinutes;
    private int timeToday;


    Paint mPaint = new Paint();
    Paint troughPaint;

    Paint timeLearnedPaint = new Paint();
    Paint yourGoalPaint;

    Path path = new Path();
    RectF oval = new RectF();


    private float radius;
    private float width;
    private float height;
    private float offset;
    private float center_x;
    private float center_y;

    public GoalView(Context context, int goalMinutes, int timeToday) {
        super(context);
        this.context = context;
        this.goalMinutes = goalMinutes;
        if(this.goalMinutes==0)
            this.goalMinutes=15;

        this.timeToday = timeToday/60;

        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(context.getResources().getDimension(R.dimen.activity_horizontal_margin));
        mPaint.setAntiAlias(true);
        mPaint.setColor(ContextCompat.getColor(context, R.color.primary));

        troughPaint = new Paint(mPaint);
        troughPaint.setColor(Color.BLACK);
        troughPaint.setAlpha(30);

        timeLearnedPaint.setColor(ContextCompat.getColor(context, android.R.color.primary_text_light));
        timeLearnedPaint.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        timeLearnedPaint.setAntiAlias(true);

        yourGoalPaint = new Paint(timeLearnedPaint);
        yourGoalPaint.setColor(ContextCompat.getColor(context, android.R.color.secondary_text_light));
    }

    public void setTimeToday(int timeToday) {
        this.timeToday = timeToday;
    }

    public void setGoalMinutes(int goalMinutes) {
        this.goalMinutes = goalMinutes;
        invalidate();
    }

    public void setWidgetPaint(){
        timeLearnedPaint.setColor(Color.WHITE);
        yourGoalPaint.setColor(Color.WHITE);

        timeLearnedPaint.setShadowLayer(4, 0, 0, Color.BLACK);
        yourGoalPaint.setShadowLayer(3,0,0,Color.BLACK);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        offset = context.getResources().getDimension(R.dimen.activity_horizontal_margin);
        height = canvas.getHeight();
        width = canvas.getWidth();
        radius = (width / 2) - offset;
        center_x = width / 2;
        center_y = height / 2;

        drawArcs(canvas);
        drawTexts(canvas);

    }

    private void drawArcs(Canvas canvas) {
        //			Degrees per metric
        int degrees = 360 / goalMinutes;

        //			JewishMonth Circle
        path.addCircle(width / 2, height / 2, radius, Path.Direction.CW);

        int achievedAngle = (timeToday * degrees);
        oval.set(center_x - radius, center_y - radius, center_x + radius, center_y + radius);

        canvas.drawCircle(center_x, center_y, radius, troughPaint);
        canvas.drawArc(oval, 270, achievedAngle, false, mPaint);
    }

    private void drawTexts(Canvas canvas) {
        String timeLearnedText = String.valueOf(timeToday) + getContext().getString(R.string.minString);
        String yourGoalText = getContext().getString(R.string.dailyGoalString) + String.valueOf(goalMinutes) + getContext().getString(R.string.minString);

        double hypot = Math.hypot(radius, radius)-offset;

        float desiredWidth = (float) hypot;

        drawText(canvas, timeLearnedPaint, timeLearnedText, desiredWidth, (float) (center_y - (hypot / 5)));
        drawText(canvas, yourGoalPaint, yourGoalText, desiredWidth, (float) (center_y + (hypot / 8)));
    }

    private void drawText(Canvas canvas, Paint paint, String text, float width, float yOffset){
        Rect titleBounds = new Rect();
        setTextSizeForWidth(paint, width, text);
        paint.getTextBounds(text, 0, text.length(), titleBounds);
        canvas.drawText(text, center_x - (paint.measureText(text) / 2), (yOffset + titleBounds.height()), paint);
    }

    private void setTextSizeForWidth(Paint paint, float desiredWidth, String text) {
        final float testTextSize = 48f;

        // Get the bounds of the text, using our testTextSize.
        paint.setTextSize(testTextSize);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        // Calculate the desired size as a proportion of our testTextSize.
        float desiredTextSize = testTextSize * desiredWidth / bounds.width();

        // Set the paint for that size.
        paint.setTextSize(desiredTextSize);
    }

    public void setCircleColor(int color){
        mPaint.setColor(color);
    }
}

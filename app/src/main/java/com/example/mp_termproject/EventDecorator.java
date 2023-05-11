package com.example.mp_termproject;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

/**
 * Decorate several days with a dot
 */
public class EventDecorator implements DayViewDecorator {

    private Drawable drawable;
    private int color;
    private HashSet<CalendarDay> dates;

    public EventDecorator(int color, Collection<CalendarDay> dates,Context context) {
        this.color = color;
        this.dates = new HashSet<>(dates);
        drawable = ContextCompat.getDrawable(context, R.drawable.bar);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(drawable);
    }

    public Drawable getDrawable() {
        return drawable;
    }
}


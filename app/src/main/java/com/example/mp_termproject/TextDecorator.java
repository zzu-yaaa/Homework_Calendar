package com.example.mp_termproject;

import android.content.Context;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.HashSet;

public class TextDecorator implements DayViewDecorator {
    private final HashSet<CalendarDay> dates;
    private final Context context;
    private final String priceDay;
    private final int color;

    private final int order;


    public TextDecorator(Context context, HashSet<CalendarDay> dates, String priceText, int color, int order) {
        this.context = context;
        this.dates = dates;
        this.priceDay = priceText;
        this.color = color;
        this.order = order;
    }

    //Use to select dates based on specific conditions and to apply decorations corresponding to those dates
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    //Apply decoration
    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new AddTextToDates(priceDay,color,order));
        //view.setDaysDisabled(true);
    }

}

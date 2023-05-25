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

    //특정 조건에 따라 날짜를 선택하고, 그 날짜에 해당하는 장식을 적용하고자 할 때 사용
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    //장식 적용
    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new AddTextToDates(priceDay,color,order));
        //view.setDaysDisabled(true);
    }

}

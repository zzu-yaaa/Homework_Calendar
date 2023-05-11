package com.example.mp_termproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    private MaterialCalendarView materialCalendarView;
    TextView dateTextView;
    private int currentTileWidth;
    private int currentTileHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AndroidThreeTen.init(this);
        materialCalendarView = findViewById(R.id.materialCalendarView);
        dateTextView = findViewById(R.id.dateTextView);

        //지정 날짜에 bar 표시하기
        HashSet<CalendarDay> dates = new HashSet<>();
        dates.add(CalendarDay.from(2023, 5, 13));
        dates.add(CalendarDay.from(2023, 5, 14));
        dates.add(CalendarDay.from(2023, 5, 15));

        EventDecorator customDecorator = new EventDecorator(Color.RED, dates,this);
        materialCalendarView.addDecorator(customDecorator);

        //선택한 날짜 보여주기
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int Year = date.getYear();
                int Month = date.getMonth();
                int Day = date.getDay();

                String shot_Day = Year + "년 " + Month + "월 " + Day+"일";

                Log.i("shot_Day test", shot_Day + "");
                dateTextView.setText(shot_Day);
            }
        });

    }
}
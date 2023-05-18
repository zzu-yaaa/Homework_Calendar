package com.example.mp_termproject;

import static java.sql.Types.NULL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.HashMap;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    NavigationView navigationView;
    private MaterialCalendarView materialCalendarView;
    private TextView dateTextView;
    private int currentTileWidth;
    private int currentTileHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //toolbar를 MainActivity의 AppBar로 지정
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open
                ,R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);

        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

/*
        materialCalendarView = findViewById(R.id.materialCalendarView);
        dateTextView = findViewById(R.id.dateTextView);

        //날짜 지정
        HashSet<CalendarDay> dates = new HashSet<>();
        dates.add(CalendarDay.from(2023, 5, 13));
        dates.add(CalendarDay.from(2023, 5, 14));
        dates.add(CalendarDay.from(2023, 5, 15));

        HashSet<CalendarDay> dates2 = new HashSet<>();
        dates2.add(CalendarDay.from(2023, 5, 15));
        dates2.add(CalendarDay.from(2023, 5, 16));

        //지정된 날짜에 textview & rect 표시

        TextDecorator decorator = new TextDecorator(this, dates,"text", Color.RED);
        materialCalendarView.addDecorator(decorator);

        TextDecorator decorator2 = new TextDecorator(this, dates2,"", Color.GREEN);
        materialCalendarView.addDecorator(decorator2);

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
        });*/

        /*
        //버튼 누르면 색 바꾸기(테스트용)
        Button btn_color = findViewById(R.id.btn_color);
        btn_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventDecorator customDecorator = new EventDecorator(Color.RED, dates,MainActivity.this);
                materialCalendarView.addDecorator(customDecorator);
            }
        });
        */
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
        switch(menuItem.getItemId()){
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;
            case R.id.myInfo:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new InfoFragment()).commit();
                break;
            case R.id.color:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ColorFragment()).commit();
                break;
            case R.id.subject:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SubjectFragment()).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
package com.example.mp_termproject;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.HashSet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private MaterialCalendarView materialCalendarView;
    TextView dateTextView;
    TextView textView;
    TextView textView2;
    private int currentTileWidth;
    private int currentTileHeight;
    Context ct;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        //activity에서의 activity.this 대체체
        ct = container.getContext();
        textView = v.findViewById(R.id.textView);
        textView2 = v.findViewById(R.id.textView2);

        AndroidThreeTen.init(ct);
        materialCalendarView = v.findViewById(R.id.materialCalendarView);
        dateTextView = v.findViewById(R.id.dateTextView);

        //지정 날짜에 bar 표시하기
        HashSet<CalendarDay> dates = new HashSet<>();
        dates.add(CalendarDay.from(2023, 5, 13));
        dates.add(CalendarDay.from(2023, 5, 14));
        dates.add(CalendarDay.from(2023, 5, 15));

        HashSet<CalendarDay> dates2 = new HashSet<>();
        dates2.add(CalendarDay.from(2023, 5, 15));
        dates2.add(CalendarDay.from(2023, 5, 16));


        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator()
        );
        //지정된 날짜에 textview & rect 표시
        TextDecorator decorator = new TextDecorator(ct, dates,"text",  Color.RED);
        materialCalendarView.addDecorator(decorator);

        TextDecorator decorator2 = new TextDecorator(ct, dates2,"text",  Color.GREEN);
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
        });

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

        if(getArguments() != null){

            textView.setText(getArguments().getInt("unSelected"));
            textView2.setText(getArguments().getInt("undone_color"));
        }

        return v;
    }
}
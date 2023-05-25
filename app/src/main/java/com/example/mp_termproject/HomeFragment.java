package com.example.mp_termproject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
    TextView textView3;
    Context ct;

    //로그인 관련 변수---------------------------
    Button loginTest;
    CourseDBHelper courseHelper;
    AsgDBHelper AsgHelper;
    ColorDBHelper colorHelper;
    SQLiteDatabase courseDb, asgDb, colorDb;
    Cursor cursor;

    String[][] courseArray ; //과목 10개에 강의명과 교수님으로 초기화



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
        textView3 = v.findViewById(R.id.textView3);

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

        HashSet<CalendarDay> dates3 = new HashSet<>();
        dates3.add(CalendarDay.from(2023, 5, 16));
        dates3.add(CalendarDay.from(2023, 5, 17));
        dates3.add(CalendarDay.from(2023, 5, 18));

        materialCalendarView.setTileHeightDp(70);

        //지정된 날짜에 textview & rect 표시
        TextDecorator decorator = new TextDecorator(ct, dates,"text",  Color.RED, 1);
        materialCalendarView.addDecorator(decorator);

        TextDecorator decorator2 = new TextDecorator(ct, dates2,"text",  Color.GREEN, 2);
        materialCalendarView.addDecorator(decorator2);

        TextDecorator decorator3 = new TextDecorator(ct, dates3,"text",  Color.BLUE, 3);
        materialCalendarView.addDecorator(decorator3);

        TextDecorator decorator4 = new TextDecorator(ct, dates3,"text",  Color.YELLOW, 4);
        materialCalendarView.addDecorator(decorator4);

        TextDecorator decorator5 = new TextDecorator(ct, dates3,"text",  Color.MAGENTA, 5);
        materialCalendarView.addDecorator(decorator5);

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

        Bundle bundle = getArguments();


        if(bundle != null){
            Set<String> keys = bundle.keySet(); // 번들에 저장된 모든 키 값 가져오기

            for (String key : keys) {
                // key 값을 활용하여 원하는 동작 수행
                Log.d("Key", key);
                if(key.equals("SelectedSubject")){
                    ArrayList<Integer> list = getArguments().getIntegerArrayList(key);
                    String msg = "선택된 과목 : ";
                    for(int i=0;i<list.size();i++){
                        msg += list.get(i);
                    }
                    textView.setText(msg);
                }
                else if(key.equals("checked_color")){
                    ArrayList<Integer> list = getArguments().getIntegerArrayList(key);
                    String msg = "선택된 색상 : ";
                    for(int i=0;i<list.size();i++){
                        msg += list.get(i);
                    }
                    textView2.setText(msg);
                }
            }
        }

        //로그인/DB 관련 함수--------------------
        loginTest = (Button) v.findViewById(R.id.loginBtn);
        loginTest.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ct.getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });

        Log.d("check", "home 다시 돌아오다");  //확인용 로그캣

        //저장된 db 불러오기
        courseHelper = new CourseDBHelper(ct);
        AsgHelper = new AsgDBHelper(ct);

        //course table 정보 전부 가져오기
        courseArray = courseHelper.selectCourse();
        int cNum = courseHelper.courseNum();
        for(int i=0; i < cNum; i++) {
            Log.d("course", Arrays.toString(courseArray[i]));  //확인용 로그캣
        }

        //원하는 과목의 assignment index 가져오기
        ArrayList<Integer> index = AsgHelper.selectAssignment("소프트웨어공학");  //괄호 안에 원하는 과목명을 입력하면 해당 과목의 과제 index를 return 해준다
        SQLiteDatabase db = AsgHelper.getReadableDatabase();
        Cursor c = db.query("Assignment", null,null,null,null,null, null);

        for(int i : index){
            c.moveToPosition(i);    //해당 index의 row로 이동

            //해당 과제 내용을 변수에 저장하지 않고 로그캣으로 출력해 확인한 형태
            Log.d("assignment", c.getString(1) + " " + c.getString(2) + " " + c.getString(3) + " "
                    + c.getString(4) + " " + c.getString(5) + " " + c.getString(6) + " " + c.getString(7) + " ");
        }

        return v;
    }
}
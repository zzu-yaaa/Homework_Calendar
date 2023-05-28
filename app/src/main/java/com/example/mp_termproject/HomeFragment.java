package com.example.mp_termproject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    int doneColor=Color.GRAY;
    int undoneColor=Color.YELLOW;
    private MaterialCalendarView materialCalendarView;
    TextView dateTextView;
    TextView textView;
    TextView textView2;
    TextView textView3;
    ListView listView;
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

        listView = v.findViewById(R.id.listView);

        //저장된 db 불러오기
        courseHelper = new CourseDBHelper(ct);
        AsgHelper = new AsgDBHelper(ct);

        /*
        //지정 날짜에 bar 표시하기
        HashSet<CalendarDay> dates = new HashSet<>();
        dates.add(CalendarDay.from(2023, 05, 13));
        dates.add(CalendarDay.from(2023, 05, 14));
        dates.add(CalendarDay.from(2023, 05, 15));

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

         */

        //선택한 날짜 보여주기 & 해당 날짜에 제출해야 하는 과제 보여주기
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int Year = date.getYear();
                int Month = date.getMonth();
                int Day = date.getDay();

                String shot_Day = Year + "년 " + Month + "월 " + Day+"일";
                //날짜 찾기 위해 비교할 문자열 만들어줌
                String compareStr = "";
                if(Month<10){
                    if (Day<10){
                        compareStr = Year+ "-0" + Month + "-0" + Day;
                    }
                    else{
                        compareStr = Year+ "-0" + Month + "-" + Day;
                    }
                }
                else{
                    if (Day<10){
                        compareStr = Year+ "-" + Month + "-0" + Day;
                    }
                    else{
                        compareStr = Year+ "-" + Month + "-" + Day;
                    }
                }

                Log.i("shot_Day test", compareStr + "");
                dateTextView.setText(shot_Day);

                ArrayList<Integer> index = AsgHelper.selectAssignmentDate(compareStr);  //괄호 안에 원하는 날짜 입력하면 해당 날짜 과제 index를 return 해준다
                Log.i("index", String.valueOf(index.size()));
                SQLiteDatabase db = AsgHelper.getReadableDatabase();
                Cursor c = db.query("Assignment", null,null,null,null,null, null);

                ArrayList<Assignment> list = new ArrayList<>();
                //Cursor courseCursor = courseHelper.readAllData();
                for(int i : index){
                    c.moveToPosition(i);    //해당 index의 row로 이동
                    //해당 과목이 선택된 경우에만 과제 보여줌
                    Cursor courseCursor = courseHelper.readAllData();
                    while(courseCursor.moveToNext()) {
                        if(c.getString(1).equals(courseCursor.getString(1))){
                            if(courseCursor.getString(3).equals("check")){
                                //해당 과제 내용을 변수에 저장하지 않고 로그캣으로 출력해 확인한 형태
                                Log.d("assignmentDate", c.getString(1) + " " + c.getString(2) + " " + c.getString(3) + " "
                                        + c.getString(4) + " " + c.getString(5) + " ");

                                list.add(new Assignment(c.getString(1),c.getString(2),c.getString(5)));
                            }
                            else{
                                continue;
                            }
                        }
                    }


                }

                //해당 날짜에 해당하는 과제 보여주는 listview 보여주기 위한 adapter
                ArrayAdapter<Assignment> asgAdapter = new ArrayAdapter<Assignment>(ct, android.R.layout.simple_spinner_item, list){
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        TextView textView = (TextView) super.getView(position, convertView, parent);
                        Assignment asg = getItem(position);
                        if (asg != null) {
                            textView.setText(asg.getSubjectName()+"\n"+asg.getAsgName());
                        }
                        return textView;
                    }
                };
                listView.setAdapter(asgAdapter);

                //listview 항목 클릭하면 과제 페이지로 연결
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Assignment asg = (Assignment) adapterView.getItemAtPosition(position);
                        String link = asg.getLink();

                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                        startActivity(intent);
                    }
                });
            }
        });

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
                    //ArrayList<Integer> list = getArguments().getIntegerArrayList(key);
                    ArrayList<String> list = getArguments().getStringArrayList(key);
                    String msg = "선택된 색상 : ";
                    for(int i=0;i<list.size();i++){
                        msg += list.get(i);
                    }

                    doneColor= selectColor(list.get(0));
                    undoneColor= selectColor(list.get(1));

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

        //course table 정보 전부 가져오기
        courseArray = courseHelper.selectCourse();
        int cNum = courseHelper.courseNum();
        for(int i=0; i < cNum; i++) {
            Log.d("course", Arrays.toString(courseArray[i]));  //확인용 로그캣
        }

        Cursor courseCursor = courseHelper.readAllData();
        int courseOrder = 1;
        int isPlusOrder = 0;
        while(courseCursor.moveToNext()){
            isPlusOrder = 0;
            if(courseCursor.getString(3).equals("check")){
                Log.d("cursorCheck", courseCursor.getString(1)+courseCursor.getString(3));  //확인용 로그캣
                ArrayList<Integer> index = AsgHelper.selectAssignment(courseCursor.getString(1));  //괄호 안에 원하는 과목명을 입력하면 해당 과목의 과제 index를 return 해준다
                SQLiteDatabase db = AsgHelper.getReadableDatabase();
                Cursor c = db.query("Assignment", null,null,null,null,null, null);

                if(index.size()>0){
                    isPlusOrder = 1;
                    for(int j : index){
                        c.moveToPosition(j);    //해당 index의 row로 이동

                        //해당 과제 내용을 변수에 저장하지 않고 로그캣으로 출력해 확인한 형태
                        Log.d("assignment", c.getString(1) + " " + c.getString(2) + " " + c.getString(3) + " "
                                + c.getString(4) + " " + c.getString(5) + " ");

                        //날짜 전처리
                        String date = c.getString(3);

                        // "-"를 구분자로 문자열을 분리하여 배열에 저장
                        String[] parts = date.split("-");

                        // 각각의 요소를 추출하여 변수에 저장
                        String year = parts[0];   // "2023"
                        String month = parts[1];  // "05"
                        String day = parts[2];    // "26"

                        HashSet<CalendarDay> dueDate = new HashSet<>();
                        dueDate.add(CalendarDay.from(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day)));

                        //과제 제출 여부에 따라 색 달라짐
                        if(c.getString(4).equals("제출 완료") || c.getString(4).equals("Submitted for grading")){
                            TextDecorator deco = new TextDecorator(ct, dueDate,c.getString(1), doneColor, courseOrder);
                            materialCalendarView.addDecorator(deco);
                        }
                        else{
                            TextDecorator deco = new TextDecorator(ct, dueDate,c.getString(1), undoneColor, courseOrder);
                            materialCalendarView.addDecorator(deco);
                        }
                }

                }

            }
            if(isPlusOrder==1){
                courseOrder+=1;
            }
        }

        /*

        //원하는 과목의 assignment index 가져오기
            ArrayList<Integer> index = AsgHelper.selectAssignment("소프트웨어공학");  //괄호 안에 원하는 과목명을 입력하면 해당 과목의 과제 index를 return 해준다
            SQLiteDatabase db = AsgHelper.getReadableDatabase();
            Cursor c = db.query("Assignment", null,null,null,null,null, null);

            for(int j : index){
                c.moveToPosition(j);    //해당 index의 row로 이동

                //해당 과제 내용을 변수에 저장하지 않고 로그캣으로 출력해 확인한 형태
                Log.d("assignment", c.getString(1) + " " + c.getString(2) + " " + c.getString(3) + " "
                        + c.getString(4) + " " + c.getString(5) + " ");

                //날짜 전처리
                String date = c.getString(3);

                // "-"를 구분자로 문자열을 분리하여 배열에 저장
                String[] parts = date.split("-");

                // 각각의 요소를 추출하여 변수에 저장
                String year = parts[0];   // "2023"
                String month = parts[1];  // "05"
                String day = parts[2];    // "26"

                HashSet<CalendarDay> dueDate = new HashSet<>();
                dueDate.add(CalendarDay.from(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day)));

                //과제 제출 여부에 따라 색 달라짐
                if(c.getString(4).equals("제출 완료")){
                    TextDecorator deco = new TextDecorator(ct, dueDate,c.getString(1), doneColor, 1);
                    materialCalendarView.addDecorator(deco);
                }
                else{
                    TextDecorator deco = new TextDecorator(ct, dueDate,c.getString(1), undoneColor, 1);
                    materialCalendarView.addDecorator(deco);
                }
            }

         */


        return v;
    }

    public int selectColor(String temp){
        int result;

        switch(temp){
            case "red":
                result = Color.parseColor("#ff0000");
                break;
            case "green":
                result = Color.parseColor("#00ff00");
                break;
            case "blue":
                result = Color.parseColor("#0000ff");
                break;
            case "pink":
                result = Color.parseColor("#ff00ff");
                break;
            default:
                result = Color.parseColor("#000000");
                break;
        }
        return result;
    };
}
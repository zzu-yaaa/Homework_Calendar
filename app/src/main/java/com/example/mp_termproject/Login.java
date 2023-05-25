package com.example.mp_termproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private String url = "https://cyber.gachon.ac.kr/login.php";
    TextView showResult;
    EditText name, password;
    Button loginBtn;
    Button returnBtn;
    String loginId, loginPw, content, color1, color2;
    Message msg;    //data 전달 class
    ValueHandler handler = new ValueHandler();

    //db
    CourseDBHelper courseHelper;
    AsgDBHelper AsgHelper;
    ColorDBHelper colorHelper;
    SQLiteDatabase courseDb, asgDb, colorDb;
    Cursor cursor;

    //array for save data
    String[][] courseArray = new String[10][2]; //과목 10개에 강의명과 교수님으로 초기화
    String[] colorArray = new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        setDatabase();  //set database table

        /* db test하는 코드 */
        courseHelper.insertCourse("소프트웨어공학", "정옥란");
        courseHelper.insertCourse("사물인터넷개론", "최재혁");
        courseHelper.insertCourse("모바일프로그래밍", "오영민");
        courseHelper.insertCourse("소프트웨어산업세미나", "조정찬 / 최재영 / 민홍");

        AsgHelper.insertAsg("모바일프로그래밍", "lab2", "2023-03-23 00:00:00", "2023-03-24 23:30:00",
                "Submitted for grading", "20.00", "https://cyber.gachon.ac.kr/mod/assign/view.php?id=653001");
        AsgHelper.insertAsg("사물인터넷개론", "[과제1] Paper reading (~5/11)", "2023-04-27 00:00:00", "2023-05-11 23:59:00",
                "Submitted for grading", null, "https://cyber.gachon.ac.kr/mod/assign/view.php?id=671292");
        AsgHelper.insertAsg("모바일프로그래밍", "lab3", "2023-03-29 00:00:00", "2023-03-31 23:30:00",
                "Not sumbmitted", null, "https://cyber.gachon.ac.kr/mod/assign/view.php?id=656475");
        AsgHelper.insertAsg("소프트웨어공학", "Summary for chapter 1 & 2", "2023-03-29 00:00:00", "2023-03-31 23:30:00",
                "Not sumbmitted", null, "https://cyber.gachon.ac.kr/mod/assign/view.php?id=656475");

        colorHelper.insertColor("pink", "green");
        //colorHelper.updateColor("blue", "skyblue");
        /* db test하는 코드 */

        //course table 정보 전부 가져오기
        courseArray = courseHelper.selectCourse();
        int cNum = courseHelper.courseNum();
        for(int i=0; i < cNum; i++) {
            Log.d("course", Arrays.toString(courseArray[i]));  //확인용 로그캣
        }

        //color table 정보 전부 가져오기
        colorArray = colorHelper.selectColor();
        Log.d("color", "color1 : " + colorArray[0] + ", color2 : " + colorArray[1]);  //확인용 로그캣

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



        Bundle bundle = new Bundle();
        name = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);
        showResult = (TextView) findViewById(R.id.showText);

        /*
        1. login
        2. crowling data
        3. save in database
        */
        loginBtn = (Button) findViewById(R.id.lgnBtn);
        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //입력한 아이디, 비번 가져오기
                loginId = name.getText().toString();
                loginPw = password.getText().toString();
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            // #1. 전송할 폼 데이터
                            Map<String, String> data = new HashMap<>();
                            data.put("userid", loginId);
                            data.put("password", loginPw);
                            data.put("redirect", "/");

                            // #2. 로그인(POST)
                            Connection.Response response = Jsoup.connect(url)
                                    .data("username", loginId, "password", loginPw)
                                    .method(Connection.Method.POST)
                                    .execute();

                            // 쿠키값 가져오기
                            Map<String, String> loginCookie = response.cookies();
                            Log.d("Tag", "쿠기정보 : " + loginCookie); //확인용 로그캣 출력 --> 최종에서는 빼도 된다

                            // #3. 쿠키 이용해 로그인 후 필요한 정보 빼오기
                            Document doc = (Document) Jsoup.connect("https://cyber.gachon.ac.kr/")
                                    .timeout(3000000)
                                    .cookies(loginCookie)
                                    .method(Connection.Method.POST)
                                    .get();

                            // #3-1 강의명 & 교수님
                            Elements Data = doc.select(".course-title"); //끌어온 html에서 태그 class가 "course-title"인 값만 선택해서 빼오기
                            boolean isEmpty = Data.isEmpty(); //빼온 값 null체크
                            Log.d("Tag", "isNull? : " + isEmpty); //빼온 값의 null 여부 로그캣 출력

                            if (isEmpty==true){
                                Login.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() { //로그인 실패시 띄우는 메세지
                                        Toast.makeText(Login.this, "아이디 또는 패스워드가 잘못 입력되었습니다", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            //null값이 아니면 크롤링 실행
                            if (isEmpty == false) {
                                content = Data.get(0).text(); //크롤링 값 가져오기

                                //결과값 Bundle 통해 전달해 화면에 출력하기
                                bundle.putString("course", content); //bundle에 뽑아낸 결과값 담기
                                bundle.putBoolean("login", isEmpty);
                                msg = handler.obtainMessage(); //handler에 결과값 추가
                                msg.setData(bundle);
                                handler.sendMessage(msg);  //msg를 message queue에 전달
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();  // thread end
            }
        }); // OnClickListener end

        returnBtn = findViewById(R.id.returnBtn);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onDestroy() {
        courseHelper.close();
        AsgHelper.close();
        colorHelper.close();
        colorDb.close();
        courseDb.close();
        asgDb.close();
        super.onDestroy();
    }

    //화면 출력용 함수
    class ValueHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            //key값을 통해 원하는 정보를 가져와 textView에 출력하는 부분
            String value = msg.getData().getString("course");
            Boolean loginResult = msg.getData().getBoolean("login");

            showResult.append("\n[강의] \n" + value + "\n");    //화면에 출력
        }
    }

    public void setDatabase(){
        //course db
        courseHelper = new CourseDBHelper(this);
        courseDb = courseHelper.getWritableDatabase();
        courseDb.execSQL("DROP TABLE IF EXISTS Course");  //delete existing table
        courseHelper.onCreate(courseDb);

        //assignment db
        AsgHelper = new AsgDBHelper(this);
        asgDb = AsgHelper.getWritableDatabase();
        asgDb.execSQL("DROP TABLE IF EXISTS Assignment");  //delete existing table
        AsgHelper.onCreate(asgDb);

        //color db
        colorHelper = new ColorDBHelper(this);
        colorDb = colorHelper.getWritableDatabase();
//        colorDb.execSQL("DROP TABLE IF EXISTS Color");  //delete existing table
        colorHelper.onCreate(colorDb);
    }


}
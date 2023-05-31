package com.example.mp_termproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mp_termproject.AsgDBHelper;
import com.example.mp_termproject.ColorDBHelper;
import com.example.mp_termproject.CourseDBHelper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private Loading loading;
    private String url = "https://cyber.gachon.ac.kr/login.php";
    TextView showResult;
    EditText name, password;
    Button loginBtn;
    String loginId, loginPw;
    String[] courseContent;
    String profContent;
    String titleContent, asgTitleContent, firstContent, submitContent, dueContent;
    Message msg;    //data 전달 class
    //ValueHandler handler = new ValueHandler();

    //db
    CourseDBHelper courseHelper;
    AsgDBHelper AsgHelper;
    ColorDBHelper colorHelper;
    UserDBHelper userHelper;
    SQLiteDatabase courseDb, asgDb, colorDb, userDb;

    Cursor cursor;

    //array for save data
    String[][] courseArray = new String[10][3]; //과목 10개에 강의명과 교수님, 체크 여부로 초기화
    String[] content, colorArray = new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        loading = new Loading(this);

        setDatabase();  //set database table


        courseHelper.insertCourse("소프트웨어공학", "정옥란", "check");
        courseHelper.insertCourse("사물인터넷개론", "최재혁", "check");
        courseHelper.insertCourse("모바일프로그래밍", "오영민", "check");
        courseHelper.insertCourse("소프트웨어산업세미나", "조정찬 / 최재영 / 민홍", "check");

        AsgHelper.insertAsg("모바일프로그래밍", "lab2",  "2023-03-24",
                "Submitted for grading",  "https://cyber.gachon.ac.kr/mod/assign/view.php?id=653001");
        AsgHelper.insertAsg("사물인터넷개론", "[과제1] Paper reading (~5/11)", "2023-05-11",
                "Submitted for grading", "https://cyber.gachon.ac.kr/mod/assign/view.php?id=671292");
        AsgHelper.insertAsg("모바일프로그래밍", "lab3", "2023-03-31",
                "Not sumbmitted", "https://cyber.gachon.ac.kr/mod/assign/view.php?id=656475");
        AsgHelper.insertAsg("소프트웨어공학", "Summary for chapter 1 & 2", "2023-03-31",
                "제출 완료", "https://cyber.gachon.ac.kr/mod/assign/view.php?id=656475");
        AsgHelper.insertAsg("소프트웨어공학", "Final", "2023-05-30",
                "Not sumbmitted", "https://cyber.gachon.ac.kr/mod/assign/view.php?id=656475");



        colorHelper.updateColor("#BEDBFC","#FFB531");

        //Bundle bundle = new Bundle();
        name = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);

        /*
        1. login
        2. crowling data
        3. save in database
        */
        loginBtn = (Button) findViewById(R.id.lgnBtn);
        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //로딩화면시작
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.show();
                    }
                });

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

                            //강의 페이지
                            Elements course = doc.getElementsByClass("course_link");
                            boolean linkIsEmpty = course.isEmpty();
                            Log.d("Tag", "LinkisNull? : " + linkIsEmpty); //빼온 값의 null 여부 로그캣 출력*/

                            Elements info = doc.getElementsByClass("items");
                            boolean InfoIsEmpty = info.isEmpty();
                            Log.d("Tag", "InfoIsNull? : " + InfoIsEmpty);

                            //로그인 확인
                            if (isEmpty==true){
                                Login.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Login.this, "아이디 또는 패스워드가 잘못 입력되었습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            //내 정보
                            if (InfoIsEmpty==false){
                                String InfoLink = info.select("a").attr("href");

                                Document InfoDocument = Jsoup.connect(InfoLink)
                                        .timeout(3000000)
                                        .cookies(loginCookie)
                                        .method(Connection.Method.POST)
                                        .get();

                                String name = InfoDocument.select("h2").text();
                                String[] numNmajor = InfoDocument.getElementsByClass("felement fstatic").text().split(" ");
                                String num=numNmajor[0];
                                String major=numNmajor[1];

                                userHelper.insertUser(name, major, num);
                            }

                            //null값이 아니면 크롤링 실행
                            if (isEmpty == false) {
                                for(int i=0;i<Data.size();i++) {
                                    courseContent = Data.get(i).select("h3").text().split(" \\("); //크롤링 값 가져오기
                                    //content String[]으로 변경
                                    //content[0]=강의명
                                    profContent=Data.get(i).getElementsByClass("prof").text();

                                    courseHelper.insertCourse(courseContent[0], profContent, "check"); //courseDB 업데이트

                                }
                            }

                            //강의 링크 존재하면 크롤링 실행
                            if (linkIsEmpty==false){

                                //강의 링크만 긁어오기
                                for (Element element : course) {
                                    String courseHref = element.attr("abs:href");

                                    Log.d("Tag", "cousreLink? : " + courseHref); //빼온 값의 null 여부 로그캣 출력

                                    //강의 링크 접속
                                    Document courseDocument = Jsoup.connect(courseHref)
                                            .timeout(3000000)
                                            .cookies(loginCookie)
                                            .method(Connection.Method.POST)
                                            .get();

                                    //assignment링크만 긁어오기
                                    Elements asg = courseDocument
                                            .getElementsByClass("activity assign modtype_assign ") //assign 뒤에 스페이스 지우면 안됨
                                            .select(".activityinstance")
                                            .select("a");

                                    boolean asgIsEmpty= asg.isEmpty();
                                    //Log.d("Tag", "asgLinkisNull? : " + asgIsEmpty); //빼온 값의 null 여부 로그캣 출력

                                    //과제 링크 존재하면 크롤링
                                    if(asgIsEmpty==false){
                                        for (Element asglink : asg) {
                                            String asgHref = asglink.attr("abs:href");

                                            //과제 링크 접속
                                            Document asgDocument = Jsoup.connect(asgHref)
                                                    .timeout(3000000)
                                                    .cookies(loginCookie)
                                                    .method(Connection.Method.POST)
                                                    .get();

                                            //과목 이름 & 과제 이름
                                            Elements title = asgDocument
                                                    .getElementsByClass("breadcrumb");
                                            boolean titleIsEmpty = title.isEmpty(); //빼온 값 null체크

                                            if (titleIsEmpty == false) {
                                                for (Element courseTitle : title) {
                                                    titleContent = String.valueOf(courseTitle.select("a").get(1).text()); //크롤링 값 가져오기
                                                    Log.d("Tag", "title? : " + titleContent);

                                                    for (Element asgTitleElement : title) {
                                                        asgTitleContent = String.valueOf(asgTitleElement.select("a").get(2).text()); //크롤링 값 가져오기
                                                        Log.d("Tag", "asgtitle? : " + asgTitleContent);
                                                    }

                                                }
                                            }

                                            //제출여부, 마감일
                                            Elements table = asgDocument.select("tbody > tr > td");
                                            boolean dueIsEmpty = table.isEmpty(); //빼온 값 null체크

                                            if (dueIsEmpty == false) {
                                                firstContent=String.valueOf(table.get(0).text());
                                                if(firstContent.contains("팀")){
                                                    submitContent = String.valueOf(table.get(5).text());
                                                    dueContent = table.get(9).text().split(" ")[0];
                                                    //dueContent[0]=날짜, [1]=시간
                                                }
                                                else if(firstContent.contains("제출 여부")){
                                                    submitContent = String.valueOf(table.get(1).text());
                                                    dueContent = table.get(5).text().split(" ")[0];
                                                }
                                                else {
                                                    submitContent = String.valueOf(table.get(3).text());
                                                    dueContent = table.get(7).text().split(" ")[0];//크롤링 값 가져오기
                                                }
                                                Log.d("Tag", "table? : " +submitContent+" "+dueContent);
                                                Log.d("Tag", "Href? : " +asgHref);
                                            }

                                            AsgHelper.insertAsg(titleContent, asgTitleContent, dueContent, submitContent, asgHref);
                                            //(String course, String title, String end, String submission, String link)

                                        }
                                    }
                                }
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //로딩화면 끝
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loading.dismiss();
                            }

                        });

                    }
                }.start();  // thread end
            }

        }); // OnClickListener end

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

//    //화면 출력용 함수
//    class ValueHandler extends Handler {
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
//
//            //key값을 통해 원하는 정보를 가져와 textView에 출력하는 부분
//            String value = msg.getData().getString("course");
//            Boolean loginResult = msg.getData().getBoolean("login");
//
//            showResult.append("\n[강의] \n" + value + "\n");    //화면에 출력
//        }
//    }

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

        //user db
        userHelper = new UserDBHelper(Login.this);
        userDb = userHelper.getWritableDatabase();
        userDb.execSQL("DROP TABLE IF EXISTS User");
        userHelper.onCreate(userDb);

        //color db
        colorHelper = new ColorDBHelper(this);
        colorDb = colorHelper.getWritableDatabase();
//        colorDb.execSQL("DROP TABLE IF EXISTS Color");  //delete existing table
        colorHelper.onCreate(colorDb);
    }
}
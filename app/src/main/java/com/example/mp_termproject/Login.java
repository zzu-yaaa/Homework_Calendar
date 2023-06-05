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
    EditText name, password;
    Button loginBtn;
    String loginId, loginPw;
    String[] courseContent;
    String profContent;
    String titleContent, asgTitleContent, firstContent, submitContent, dueContent;

    //db
    CourseDBHelper courseHelper;
    AsgDBHelper AsgHelper;
    ColorDBHelper colorHelper;
    UserDBHelper userHelper;
    SQLiteDatabase courseDb, asgDb, colorDb, userDb;

    //array for save data
    String[][] courseArray = new String[10][3]; //Initialize 10 subjects with lecture name, professor, and check status
    String[] content, colorArray = new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        loading = new Loading(this);

        setDatabase();  //set database table

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
                //Start loading screen
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.show();
                    }
                });

                //Get the ID and pw entered
                loginId = name.getText().toString();
                loginPw = password.getText().toString();
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            // #1. Form data to be transferred
                            Map<String, String> data = new HashMap<>();
                            data.put("userid", loginId);
                            data.put("password", loginPw);
                            data.put("redirect", "/");

                            // #2. Login(POST)
                            Connection.Response response = Jsoup.connect(url)
                                    .data("username", loginId, "password", loginPw)
                                    .method(Connection.Method.POST)
                                    .execute();

                            // Get Cookie Value
                            Map<String, String> loginCookie = response.cookies();

                            // #3. Use cookies to log in and get the information you need
                            Document doc = (Document) Jsoup.connect("https://cyber.gachon.ac.kr/")
                                    .timeout(3000000)
                                    .cookies(loginCookie)
                                    .method(Connection.Method.POST)
                                    .get();

                            // #3-1 Lecture name & professor
                            Elements Data = doc.select(".course-title"); //Select and extract only values with tag class "course-title" from dragged html
                            boolean isEmpty = Data.isEmpty(); //Check null

                            //lecture page
                            Elements course = doc.getElementsByClass("course_link");
                            boolean linkIsEmpty = course.isEmpty();

                            Elements info = doc.getElementsByClass("items");
                            boolean InfoIsEmpty = info.isEmpty();

                            //Check login
                            if (isEmpty==true){
                                Login.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Login.this, "아이디 또는 패스워드가 잘못 입력되었습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            //User Info
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

                            //If it is not a null value, performance
                            if (isEmpty == false) {
                                for(int i=0;i<Data.size();i++) {
                                    courseContent = Data.get(i).select("h3").text().split(" \\("); //Get Crawling Value
                                    //Change to content String[]
                                    //content[0]=Lecture name
                                    profContent=Data.get(i).getElementsByClass("prof").text();

                                    courseHelper.insertCourse(courseContent[0], profContent, "check"); //courseDB update

                                }
                            }

                            //Run crawl if lecture link exists
                            if (linkIsEmpty==false){

                                //Import lecture links only
                                for (Element element : course) {
                                    String courseHref = element.attr("abs:href");

                                    //Link to the lecture
                                    Document courseDocument = Jsoup.connect(courseHref)
                                            .timeout(3000000)
                                            .cookies(loginCookie)
                                            .method(Connection.Method.POST)
                                            .get();

                                    //Get only analysis links
                                    Elements asg = courseDocument
                                            .getElementsByClass("activity assign modtype_assign ") //Do not clear the space behind the assignment
                                            .select(".activityinstance")
                                            .select("a");

                                    boolean asgIsEmpty= asg.isEmpty();

                                    //Task link crawls if present
                                    if(asgIsEmpty==false){
                                        for (Element asglink : asg) {
                                            String asgHref = asglink.attr("abs:href");

                                            //Connecting Task Links
                                            Document asgDocument = Jsoup.connect(asgHref)
                                                    .timeout(3000000)
                                                    .cookies(loginCookie)
                                                    .method(Connection.Method.POST)
                                                    .get();

                                            //Subject Name & Task Name
                                            Elements title = asgDocument
                                                    .getElementsByClass("breadcrumb");
                                            boolean titleIsEmpty = title.isEmpty();

                                            if (titleIsEmpty == false) {
                                                for (Element courseTitle : title) {
                                                    titleContent = String.valueOf(courseTitle.select("a").get(1).text());

                                                    for (Element asgTitleElement : title) {
                                                        asgTitleContent = String.valueOf(asgTitleElement.select("a").get(2).text());
                                                    }

                                                }
                                            }

                                            //Submission status, deadline
                                            Elements table = asgDocument.select("tbody > tr > td");
                                            boolean dueIsEmpty = table.isEmpty(); //Check null

                                            if (dueIsEmpty == false) {
                                                firstContent=String.valueOf(table.get(0).text());
                                                if(firstContent.contains("팀")){
                                                    submitContent = String.valueOf(table.get(5).text());
                                                    dueContent = table.get(9).text().split(" ")[0];
                                                    //dueContent[0]=date, [1]=time
                                                }
                                                else if(firstContent.contains("제출 여부")){
                                                    submitContent = String.valueOf(table.get(1).text());
                                                    dueContent = table.get(5).text().split(" ")[0];
                                                }
                                                else {
                                                    submitContent = String.valueOf(table.get(3).text());
                                                    dueContent = table.get(7).text().split(" ")[0];
                                                }
                                            }

                                            //Check Duplicate Assignment
                                            if (!AsgHelper.checkDuplicateAsg(asgHref)){
                                                AsgHelper.insertAsg(titleContent, asgTitleContent, dueContent, submitContent, asgHref);
                                            }
                                        }
                                    }
                                }
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //The end of the loading screen
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
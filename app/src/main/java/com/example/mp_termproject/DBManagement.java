package com.example.mp_termproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

/*
db와 관련된 것만 한 번에 볼 수 있게 따로 빼놓은 예시 class
*/
public class DBManagement  extends AppCompatActivity {

    String color1, color2;
    //db
    CourseDBHelper courseHelper;
    AsgDBHelper AsgHelper;
    ColorDBHelper colorHelper;
    SQLiteDatabase courseDb, asgDb, colorDb;
    Cursor cursor;

    //array for save data from db
    String[][] courseArray = new String[10][2]; //과목 10개에 강의명과 교수님으로 초기화
    String[] colorArray = new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setDatabase();  //set database table

        /* db test하는 코드 */
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
                "Not sumbmitted", "https://cyber.gachon.ac.kr/mod/assign/view.php?id=656475");

        colorHelper.insertColor("pink", "green");
        //colorHelper.updateColor("blue", "skyblue");
        /* db test하는 코드 */

        //course table 정보 전부 가져오기
        courseArray = courseHelper.selectCourse();
        int cNum = courseHelper.courseNum();
        for (int i = 0; i < cNum; i++) {
            Log.d("course", Arrays.toString(courseArray[i]));  //확인용 로그캣
        }

        //color table 정보 전부 가져오기
        colorArray = colorHelper.selectColor();
        Log.d("color", "color1 : " + colorArray[0] + ", color2 : " + colorArray[1]);  //확인용 로그캣

        //원하는 과목의 assignment index 가져오기
        ArrayList<Integer> index = AsgHelper.selectAssignment("소프트웨어공학");  //괄호 안에 원하는 과목명을 입력하면 해당 과목의 과제 index를 return 해준다
        SQLiteDatabase db = AsgHelper.getReadableDatabase();
        Cursor c = db.query("Assignment", null, null, null, null, null, null);

        for (int i : index) {
            c.moveToPosition(i);    //해당 index의 row로 이동

            //해당 과제 내용을 변수에 저장하지 않고 로그캣으로 출력해 확인한 형태
            Log.d("assignment", c.getString(1) + " " + c.getString(2) + " " + c.getString(3) + " "
                    + c.getString(4) + " " + c.getString(5) + " " + c.getString(6) + " " + c.getString(7) + " ");
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
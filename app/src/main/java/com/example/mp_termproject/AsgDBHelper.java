package com.example.mp_termproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/*
Database helper for Assignment table
Assignment columns(String) : course name, assignment title, start date, due date, submission state, grade
 */
public class AsgDBHelper extends SQLiteOpenHelper {
    //database information
    static final String DB_NAME = "Assignment.db";
    static final int DB_VERSION = 1;
    static final String TABLE_NAME = "Assignment";

    //database columns
    static final String KEY_INFOID = "_id";
    static final String KEY_COURSE = "course_name";
    static final String KEY_TITLE = "title";
    static final String KEY_STARTDATE = "start_date";
    static final String KEY_DUEDATE = "due_date";
    static final String KEY_SUBMISSION = "submission";
    static final String KEY_GRADE = "grade";
    static final String KEY_LINK = "link";

    static final String DB_CREATE =
            "CREATE TABLE " + TABLE_NAME + " ("
                    + KEY_INFOID + " integer primary key autoincrement, "
                    + KEY_COURSE + " text not null, "
                    + KEY_TITLE + " text not null, "
                    + KEY_STARTDATE + " text not null, "
                    + KEY_DUEDATE + " text not null, "
                    + KEY_SUBMISSION + " text not null, "
                    + KEY_GRADE + " text, "
                    + KEY_LINK + " text not null)";

    Cursor c;

    //specify DB name in the constructor
    public AsgDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //필요한 DB가 존재하지 않을 때만 실행되어 DB를 생성
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(DB_CREATE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //DB version up
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Simply discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db); //create new database
    }

    // insert data in course table
    void insertAsg (String course, String title, String start, String end, String submission, String grade, String link) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_COURSE, course);
        values.put(KEY_TITLE, title);
        values.put(KEY_STARTDATE, start);
        values.put(KEY_DUEDATE, end);
        values.put(KEY_SUBMISSION, submission);
        values.put(KEY_GRADE, grade);
        values.put(KEY_LINK, link);

        long result = db.insert(TABLE_NAME, null, values);
        if (result == -1) {
            Log.d("database", "Assignment db 데이터 추가 실패");
        } else {
            Log.d("database", "Assignment db 데이터 추가 성공");
        }
    }

    //해당 강의의 과제들 index 전송
    public ArrayList selectAssignment(String course){
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.query(TABLE_NAME, null,null,null,null,null, null);
        ArrayList<Integer> result = new ArrayList<>();

        while(c.moveToNext()) {
            if(c.getString(1).equals(course)){
                result.add(c.getPosition());   //row의 index 추가
            }
        }
        return result;
    }
}

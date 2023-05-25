package com.example.mp_termproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/*
Database helper for Course table
Course columns(String) : course name, professor name, color
 */
public class CourseDBHelper extends SQLiteOpenHelper {
    //database information
    static final String DB_NAME = "Course.db";
    static final int DB_VERSION = 1;
    static final String TABLE_NAME = "Course";

    //database columns
    static final String KEY_INFOID = "_id";
    static final String KEY_COURSE = "course_name";
    static final String KEY_PROF = "professor";

    //statement to create new DB
    static final String DB_CREATE =
            "CREATE TABLE " + TABLE_NAME + " ("
                    + KEY_INFOID + " integer primary key autoincrement, "
                    + KEY_COURSE + " text not null, "
                    + KEY_PROF + " text not null)";
    Cursor c;

    //specify DB name in the constructor
    public CourseDBHelper(Context context) {
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
    public void insertCourse(String course, String professor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_COURSE, course);
        values.put(KEY_PROF, professor);

        long result = db.insert(TABLE_NAME, null, values);
        if (result == -1) {
            Log.d("database", "course db 데이터 추가 실패");
        } else {
            Log.d("database", "course db 데이터 추가 성공");
        }
    }

    //get all table data
    public String[][] selectCourse(){
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.query(TABLE_NAME, null,null,null,null,null, null);
        String[][] result = new String[10][2];
        int index1 = 0, index2 = 0;

        while(c.moveToNext()){
            String course = c.getString(1);
            String name = c.getString(2);

            result[index1][index2] = course;
            result[index1][index2+1] = name;
            index1++;
            index2 = 0;
            Log.d("course select", "강의 " + index1 + " 개 추가");
        }
        return result;
    }

    public int courseNum (){
        SQLiteDatabase db = this.getReadableDatabase();
        int num = c.getCount();
        return num;
    }
}
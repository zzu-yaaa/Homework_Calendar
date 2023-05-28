package com.example.mp_termproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserDBHelper extends SQLiteOpenHelper  {
    static final String DB_NAME = "User.db";
    static final int DB_VERSION = 1;
    static final String TABLE_NAME = "User";

    //database columns
    static final String KEY_INFOID = "_id";
    static final String KEY_NAME = "name";
    static final String KEY_MAJOR = "major";
    static final String KEY_NUM = "num";

    //statement to create new DB
    static final String DB_CREATE =
            "CREATE TABLE " + TABLE_NAME + " ("
                    + KEY_INFOID + " integer primary key autoincrement, "
                    + KEY_NAME + " text not null, "
                    + KEY_MAJOR + " text not null, "
                    + KEY_NUM + " text not null)";

    //specify DB name in the constructor
    public UserDBHelper(Context context) {
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
    public void insertUser(String name, String major, String num) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_NAME, name);
        values.put(KEY_MAJOR, major);
        values.put(KEY_NUM, num);

        long result = db.insert(TABLE_NAME, null, values);
        if (result == -1) {
            Log.d("database", "user db 데이터 추가 실패");
        } else {
            Log.d("database", "user db 데이터 추가 성공");
        }
    }
}

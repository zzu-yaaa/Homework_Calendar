package com.example.mp_termproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/*
Database helper for Color table
Course columns(String) : color1 for Not submitted, color2 for submitted
The two color values go into the first row only, and use modified
 */
public class ColorDBHelper extends SQLiteOpenHelper {
    SQLiteDatabase db;
    static final String DB_NAME = "Color.db";
    static final int DB_VERSION = 1;
    static final String TABLE_NAME = "Color";

    //database columns
    static final String KEY_INFOID = "_id";
    static final String KEY_COLOR1 = "color1";
    static final String KEY_COLOR2 = "color2";

    //statement to create new DB
    static final String DB_CREATE =
            "CREATE TABLE " + TABLE_NAME + " ("
                    + KEY_INFOID + " integer primary key autoincrement, "
                    + KEY_COLOR1 + " text not null, "
                    + KEY_COLOR2 + " text not null)";
    Cursor c;

    //specify DB name in the constructor
    public ColorDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //Runs only when the required DB does not exist to create a DB
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
    public void insertColor (String color1, String color2) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_COLOR1, color1);
        values.put(KEY_COLOR2, color2);

        long result = db.insert(TABLE_NAME, null, values);
        if (result == -1) {
            Log.d("database", "color db 데이터 추가 실패");
        } else {
            Log.d("database", "color db 데이터 추가 성공");
        }
    }
    //delete all data from assignment table
    public static void deleteAllColor(SQLiteDatabase db) {
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

    //Method called to change color
    public void updateColor (String color1, String color2){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        deleteAllColor(db); //delete value
        insertColor(color1, color2);    //insert value
    }
    //get all table data
    public String[] selectColor(){
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.query(TABLE_NAME, null,null,null,null,null, null);
        String[] result = new String[2];

        c.moveToFirst();   //get data from first row
        String color1 = c.getString(1);
        String color2 = c.getString(2);

        result[0] = color1;
        result[1] = color2;
        Log.d("color select", "색 전달 완료");

        return result;
    }
}

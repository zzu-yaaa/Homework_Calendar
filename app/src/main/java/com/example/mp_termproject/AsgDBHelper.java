package com.example.mp_termproject;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Database helper for Assignment table
 * Assignment columns(String) : course name, assignment title, start date, due date, submission state, grade
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
    static final String KEY_DUEDATE = "due_date";
    static final String KEY_SUBMISSION = "submission";
    static final String KEY_LINK = "link";

    static final String DB_CREATE =
            "CREATE TABLE " + TABLE_NAME + " ("
                    + KEY_INFOID + " integer primary key autoincrement, "
                    + KEY_COURSE + " text not null, "
                    + KEY_TITLE + " text not null, "
                    + KEY_DUEDATE + " text not null, "
                    + KEY_SUBMISSION + " text not null, "
                    + KEY_LINK + " text not null)";

    Cursor c;

    //specify DB name in the constructor
    public AsgDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //Create a DB only when the required DB does not exist
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
    public void insertAsg (String course, String title, String end, String submission, String link) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_COURSE, course);
        values.put(KEY_TITLE, title);
        values.put(KEY_DUEDATE, end);
        values.put(KEY_SUBMISSION, submission);
        values.put(KEY_LINK, link);

        long result = db.insert(TABLE_NAME, null, values);
        if (result == -1) {
            Log.d("database", "Assignment db 데이터 추가 실패");
        } else {
            Log.d("database", "Assignment db 데이터 추가 성공");
        }
    }

    public ArrayList selectAssignment(String course){
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.query(TABLE_NAME, null,null,null,null,null, null);
        ArrayList<Integer> result = new ArrayList<>();

        while(c.moveToNext()) {
            if(c.getString(1).equals(course)){
                result.add(c.getPosition());   //Add index of row
            }
        }
        return result;
    }

    public ArrayList selectAssignmentDate(String end){
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.query(TABLE_NAME, null,null,null,null,null, null);
        ArrayList<Integer> result = new ArrayList<>();

        while(c.moveToNext()) {
            if(c.getString(3).equals(end)){
                result.add(c.getPosition());   //Add index of row
            }
        }
        return result;
    }

    // Return assignments for a specific date
    public ArrayList<String> getAssignmentByDate(String endDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> assignments = new ArrayList<>();

        String[] columns = {KEY_COURSE, KEY_TITLE, KEY_SUBMISSION};
        String selection = KEY_DUEDATE + "=?";
        String[] selectionArgs = {endDate};

        Cursor c = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int courseIndex = c.getColumnIndex(KEY_COURSE);
        int titleIndex = c.getColumnIndex(KEY_TITLE);
        int submissionIndex = c.getColumnIndex(KEY_SUBMISSION);

        while (c.moveToNext()) {
            String course = c.getString(courseIndex);
            String title = c.getString(titleIndex);
            String submission = c.getString(submissionIndex);

            String assignmentInfo = "과목 - " + course + "\n"
                    + "과제명 - " + title + "\n"
                    + "제출여부 - " + submission + "\n";

            assignments.add(assignmentInfo);
        }

        c.close();
        return assignments;
    }

    //get Asg by submission
    public ArrayList<String> getAssignmentsBySubmission() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> assignments = new ArrayList<>();

        String[] columns = {KEY_COURSE, KEY_TITLE};
        String selection = KEY_SUBMISSION + "=? OR " + KEY_SUBMISSION + "=?";
        String[] selectionArgs = {"미제출", "No attempt"};

        Cursor c = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        int courseIndex = c.getColumnIndex(KEY_COURSE);
        int titleIndex = c.getColumnIndex(KEY_TITLE);

        while (c.moveToNext()) {
            String course = c.getString(courseIndex);
            String title = c.getString(titleIndex);

            String assignmentInfo = "과목 - " + course + "\n"
                    + "과제명 - " + title + "\n";
            Log.d("adgDB", "제출 안 한 과제 : " + assignmentInfo);
            assignments.add(assignmentInfo);
        }

        c.close();
        return assignments;
    }

    //Remove Duplicate Asg
    public boolean checkDuplicateAsg(String asgHref) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = { "link" };
        String selection = "link = ?";
        String[] selectionArgs = { asgHref };
        Cursor cursor = db.query("Assignment", projection, selection, selectionArgs, null, null, null);
        boolean isDuplicate = cursor.getCount() > 0;
        cursor.close();
        return isDuplicate;
    }

}

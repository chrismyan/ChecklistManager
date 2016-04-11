package edu.westga.checklistmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Chris Yan on 4/11/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "checklistDB.db";
    public static final String TABLE_TASKS = "tasks";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TASKNAME = "taskname";
    public static final String COLUMN_CATEGORY = "category";



    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factor, int version) {
        super(context, DATABASE_NAME, factor, DATABASE_VERSION);
        Log.d("Database 0perations", "Database created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASKS_TABLE = "CREATE TABLE " +
                TABLE_TASKS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_TASKNAME + " TEXT,"
                + COLUMN_CATEGORY + "INTEGER" + ")";
        db.execSQL(CREATE_TASKS_TABLE);
        Log.d("Database 0perations", "Table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

//    public void addTask(Tasks task) {
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_TASKNAME, task.getTaskName());
//        values.put(COLUMN_CATEGORY, task.getCategoryName());
//
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        db.insert(TABLE_TASKS, null, values);
//        db.close();
//    }
}

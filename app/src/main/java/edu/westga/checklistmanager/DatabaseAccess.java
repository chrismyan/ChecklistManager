package edu.westga.checklistmanager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import edu.westga.checklistmanager.DatabaseOpenHelper;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;
    private int category;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public void setCategory(int category) {
        this.category = category;
    }

    /**
     * Read all quotes from the database.
     *
     * @return a List of quotes
     */
    public List<String> getTaskItems(int category) {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT _taskname FROM tasks WHERE _category =" + Integer.toString( this.category), null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
}
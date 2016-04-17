package edu.westga.checklistmanager.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;
    private int event;

    /**
     * Private constructor to avoid object creation from outside classes.
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

    public void setEvent(int category) {
        this.event = category;
    }

    /**
     * Read all quotes from the database.
     *
     * @return a List of quotes
     */
    public List<String> getTaskItems(int category) {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT _taskItemName, _id FROM taskitems WHERE _eventID =" + Integer.toString(this.event), null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getEvents() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _eventName, _id FROM events" , null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public void addEvent(String eventName) {
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.COLUMN_EVENTNAME, eventName);

        database.insert(DatabaseOpenHelper.TABLE_EVENTS, null, values);
        database.close();
    }

    public boolean deleteEvent(String eventName) {
        boolean result = false;
        String query = "Select * FROM " + DatabaseOpenHelper.TABLE_EVENTS + " WHERE " + DatabaseOpenHelper.COLUMN_EVENTNAME + " =  \"" + eventName + "\"";
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
//        int eventID = Integer.parseInt(eventIDString);

        if(cursor.moveToFirst()){
            String eventIDString = cursor.getString(0);
            db.delete(DatabaseOpenHelper.TABLE_EVENTS, DatabaseOpenHelper.COLUMN_EVENT_ID + " = ?", new String[]{eventIDString});
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    public boolean isItemCompleted(String taskItem) {
        boolean completed = false;
        String query = "Select * FROM " + DatabaseOpenHelper.TABLE_TASK_ITEMS + " WHERE " + DatabaseOpenHelper.COLUMN_TASKITEM_NAME + " =  \"" + taskItem + "\"";
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            String itemIDString = cursor.getString(0);
            db.delete(DatabaseOpenHelper.TABLE_EVENTS, DatabaseOpenHelper.COLUMN_EVENT_ID + " = ?", new String[]{itemIDString});
            cursor.close();
            completed = true;
        }
        db.close();
        return completed;
    }

    public boolean itemCompleted(String taskItemName) {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.COLUMN_COMPLETED, 1);
        db.update(DatabaseOpenHelper.TABLE_TASK_ITEMS, values, "_taskItemName =?", new String[]{taskItemName});
        db.close();
        return true;
    }

    public boolean itemNotCompleted(String taskItemName) {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.COLUMN_COMPLETED, 0);
        db.update(DatabaseOpenHelper.TABLE_TASK_ITEMS, values, "_taskItemName = ?", new String[]{taskItemName});
        db.close();
        return true;
    }
}
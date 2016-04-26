package edu.westga.checklistmanager.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

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

    public Cursor getTaskItemsCursor(int category) {
        database = openHelper.getReadableDatabase();
        return database.rawQuery("SELECT _taskItemName, _id, _completed FROM taskitems WHERE _eventID =" + Integer.toString(category), null);
    }

    public Cursor getAllEventCursor() {
        List<Events> list = new ArrayList<>();
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db.rawQuery("SELECT _eventName, _id FROM events" , null);
    }

    public void addEvent(Events newEvent) {
        SQLiteDatabase database = openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.COLUMN_EVENTNAME, newEvent.getName());

        database.insert(DatabaseOpenHelper.TABLE_EVENTS, null, values);
        database.close();
    }

    public boolean deleteEventCursor(Events deleteEvent) {
        boolean result = false;
        try {
            SQLiteDatabase db = openHelper.getReadableDatabase();
            db.delete(DatabaseOpenHelper.TABLE_EVENTS, DatabaseOpenHelper.COLUMN_EVENT_ID + " = ?", new String[]{Integer.toString(deleteEvent.getId())});
            db.close();
            result = true;
        } catch (Exception ex) {
            Log.d("Check database", ex.getMessage());
        }

        return result;
    }

    public void addTaskItem(TaskItems newItem) {
        SQLiteDatabase database = openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.COLUMN_TASKITEM_NAME, newItem.getName());
        values.put(DatabaseOpenHelper.COLUMN_TASK_ITEM_EVENT_ID, newItem.getEventId());
        values.put(DatabaseOpenHelper.COLUMN_COMPLETED, 0);

        database.insert(DatabaseOpenHelper.TABLE_TASK_ITEMS, null, values);
        database.close();
    }

    public void deleteTaskItem(TaskItems item) {
        try {
            SQLiteDatabase db = openHelper.getReadableDatabase();
            db.delete(DatabaseOpenHelper.TABLE_TASK_ITEMS, DatabaseOpenHelper.COLUMN_ITEM_ID + " = ?", new String[]{Integer.toString(item.getId())});
            db.close();
        } catch (Exception ex) {
            Log.d("Check database", ex.getMessage());
        }
    }

    public int getTaskItemId(TaskItems item) {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id FROM taskitems WHERE _taskItemName = '" + item.getName() + "'" + "AND _eventID = '"
                + item.getEventId() + "'", null);
        cursor.moveToFirst();
        int taskId = cursor.getInt(0);
        db.close();
        return taskId;
    }

    public void isTaskItemCompleted(int id, int completed) {
        SQLiteDatabase db = this.openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.COLUMN_COMPLETED, completed);
        db.update(DatabaseOpenHelper.TABLE_TASK_ITEMS, values, "_id = ?", new String[]{Integer.toString(id)});
    }
}
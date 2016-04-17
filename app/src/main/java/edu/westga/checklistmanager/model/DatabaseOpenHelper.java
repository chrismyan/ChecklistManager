package edu.westga.checklistmanager.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseOpenHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "checklistmanager.sqlite";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_EVENTS = "events";
    public static final String COLUMN_EVENT_ID = "_id";
    public static final String COLUMN_EVENTNAME = "_eventName";
    public static final String TABLE_TASK_ITEMS = "taskitems";
    public static final String COLUMN_ITEM_ID = "_id";
    public static final String COLUMN_TASKITEM_NAME = "_taskItemName";
    public static final String COLUMN_TASK_ITEM_EVENT_ID = "_eventID";
    public static final String COLUMN_COMPLETED = "_completed";

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
}
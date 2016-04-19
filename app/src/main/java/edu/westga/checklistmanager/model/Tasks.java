package edu.westga.checklistmanager.model;

import android.provider.BaseColumns;

/**
 * Created by Chris Dunmeyer and Chris Yan on 4/11/2016.
 */
public class Tasks {
    private int _id;
    private String _taskname;
    private int _category;

    public Tasks(String taskname, int category) {
        this._taskname = taskname;
        this._category = category;
    }

    public void set_category(int _category) {
        this._category = _category;
    }

    public void set_taskname(String _taskname) {
        this._taskname = _taskname;
    }

    public int get_id() {
        return _id;
    }

    public int get_category() {
        return _category;
    }

    public String get_taskname() {
        return _taskname;
    }

    public static abstract class ChecklistInfo implements BaseColumns {
        public static final String TASK_NAME = "task_name";
        public static final String CHECKLIST_ID = "checklist_id";
        public static final String DATABASE_NAME = "checklist_info";
        public static final String TASK_TABLE_NAME = "task_info";
    }
}

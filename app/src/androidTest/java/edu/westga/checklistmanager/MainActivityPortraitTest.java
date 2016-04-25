package edu.westga.checklistmanager;

import android.database.Cursor;
import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;
import java.util.List;

import edu.westga.checklistmanager.controller.MainActivity;
import edu.westga.checklistmanager.model.DatabaseAccess;
import edu.westga.checklistmanager.model.Events;
import edu.westga.checklistmanager.model.TaskItems;

/**
 * Created by Chris Dunmeyer and Chris Yan on 4/24/2016.
 */
public class MainActivityPortraitTest extends ActivityInstrumentationTestCase2<MainActivity> {
    MainActivity main;

    public MainActivityPortraitTest() {
        super(MainActivity.class);
    }

    public void testIfEventDelete() {
        DatabaseAccess db = DatabaseAccess.getInstance(this.main);
        Cursor cursorBefore = db.getAllEventCursor();
        List<Events> listBefore = new ArrayList<>();
        cursorBefore.moveToFirst();
        while (!cursorBefore.isAfterLast()) {
            Events newEvent = new Events();
            newEvent.setName(cursorBefore.getString(0));
            listBefore.add(newEvent);
            cursorBefore.moveToNext();
        }
        // Delete event
        Events newEvent = new Events();
        newEvent.setId(listBefore.size());
        db.deleteEventCursor(newEvent);
        Cursor cursorAfter = db.getAllEventCursor();
        List<Events> listAfter = new ArrayList<>();
        cursorBefore.moveToFirst();
        while (!cursorAfter.isAfterLast()) {
            Events event = new Events();
            newEvent.setName(cursorBefore.getString(0));
            listAfter.add(event);
            cursorAfter.moveToNext();
        }
        assertTrue(listBefore.size() < listAfter.size());
    }

        public void testIfEventAdd() {
        DatabaseAccess db = DatabaseAccess.getInstance(this.main);
        Cursor cursorBefore = db.getAllEventCursor();
        List<Events> listBefore = new ArrayList<>();
        cursorBefore.moveToFirst();
        while (!cursorBefore.isAfterLast()) {
            Events newEvent = new Events();
            newEvent.setName(cursorBefore.getString(0));
            listBefore.add(newEvent);
            cursorBefore.moveToNext();
        }
        // Add event
        Events newEvent = new Events();
        newEvent.setName("New Event");
        db.addEvent(newEvent);
        Cursor cursorAfter = db.getAllEventCursor();
        List<Events> listAfter = new ArrayList<>();
        cursorBefore.moveToFirst();
        while (!cursorAfter.isAfterLast()) {
            Events event = new Events();
            newEvent.setName(cursorBefore.getString(0));
            listAfter.add(event);
            cursorAfter.moveToNext();
        }
        assertTrue(listBefore.size() < listAfter.size());
    }

    public void testIfTaskItemAdd() {
        DatabaseAccess db = DatabaseAccess.getInstance(this.main);
        Cursor cursorBefore = db.getTaskItemsCursor(1);
        List<TaskItems> listBefore = new ArrayList<>();
        cursorBefore.moveToFirst();
        while (!cursorBefore.isAfterLast()) {
            TaskItems item = new TaskItems();
            item.setName(cursorBefore.getString(0));
            item.setEventId(cursorBefore.getInt(1));
            listBefore.add(item);
            cursorBefore.moveToNext();
        }
        // Add task item
        TaskItems newItem = new TaskItems();
        newItem.setName("New Event");
        newItem.setEventId(1);
        db.addTaskItem(newItem);
        Cursor cursorAfter = db.getTaskItemsCursor(1);
        List<TaskItems> listAfter = new ArrayList<>();
        cursorBefore.moveToFirst();
        while (!cursorAfter.isAfterLast()) {
            TaskItems afterItems = new TaskItems();
            afterItems.setName(cursorBefore.getString(0));
            listAfter.add(afterItems);
            cursorAfter.moveToNext();
        }
        assertTrue(listBefore.size() < listAfter.size());
    }

    @Override
    protected void setUp() {
        this.main = getActivity();
    }
}

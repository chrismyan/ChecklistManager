package edu.westga.checklistmanager;

import android.app.Fragment;
import android.database.Cursor;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import edu.westga.checklistmanager.controller.AddFragment;
import edu.westga.checklistmanager.controller.MainActivity;
import edu.westga.checklistmanager.model.DatabaseAccess;
import edu.westga.checklistmanager.model.Events;
import edu.westga.checklistmanager.model.TaskItems;

/**
 * Created by Chris Dunmeyer and Chris Yan on 4/24/2016.
 */
public class MainActivityPortraitTest extends ActivityInstrumentationTestCase2<MainActivity> {
    MainActivity main;
    DatabaseAccess db;

    public MainActivityPortraitTest() {
        super(MainActivity.class);
    }

    public void testIfEventAdd() {
        Cursor cursorBefore = this.db.getAllEventCursor();
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
        this.db.addEvent(newEvent);
        getInstrumentation().waitForIdleSync();

        Cursor cursorAfter = this.db.getAllEventCursor();
        List<Events> listAfter = new ArrayList<>();
        cursorBefore.moveToFirst();
        while (!cursorAfter.isAfterLast()) {
            Events event = new Events();
            newEvent.setName(cursorBefore.getString(0));
            listAfter.add(event);
            cursorAfter.moveToNext();
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(listBefore.size() < listAfter.size());
    }

    public void testWhenEventDelete() {

        Cursor cursorBefore = this.db.getAllEventCursor();
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
        this.db.deleteEventCursor(newEvent);
        getInstrumentation().waitForIdleSync();

        Cursor cursorAfter = this.db.getAllEventCursor();
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
        Cursor cursorBefore = this.db.getTaskItemsCursor(1);
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
        newItem.setName("New TaskItem");
        newItem.setEventId(1);
        this.db.addTaskItem(newItem);
        getInstrumentation().waitForIdleSync();

        Cursor cursorAfter = this.db.getTaskItemsCursor(1);
        List<TaskItems> listAfter = new ArrayList<>();
        cursorBefore.moveToFirst();
        while (!cursorAfter.isAfterLast()) {
            TaskItems afterItems = new TaskItems();
            afterItems.setName(cursorBefore.getString(0));
            listAfter.add(afterItems);
            cursorAfter.moveToNext();
        }
        Log.d("*List After Adding", Integer.toString(listAfter.size()));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(listBefore.size() < listAfter.size());
    }

    public void testWhenTaskItemDelete() {

        Cursor cursorBefore = this.db.getTaskItemsCursor(1);
        List<TaskItems> listBefore = new ArrayList<>();
        cursorBefore.moveToFirst();
        while (!cursorBefore.isAfterLast()) {
            TaskItems item = new TaskItems();
            item.setName(cursorBefore.getString(0));
            item.setEventId(cursorBefore.getInt(1));
            listBefore.add(item);
            cursorBefore.moveToNext();
        }

        Log.d("*List Before Delete", Integer.toString(listBefore.size()));
        // delete task item
        TaskItems newItem = new TaskItems();
        newItem.setName("New TaskItem");
        newItem.setEventId(1);
        int taskId = this.db.getTaskItemId(newItem);
        newItem.setId(taskId);
        this.db.deleteTaskItem(newItem);
        getInstrumentation().waitForIdleSync();
        Cursor cursorAfter = this.db.getTaskItemsCursor(1);
        List<TaskItems> listAfter = new ArrayList<>();
        cursorAfter.moveToFirst();
        while (!cursorAfter.isAfterLast()) {
            TaskItems afterItems = new TaskItems();
            afterItems.setName(cursorAfter.getString(0));
            afterItems.setEventId(cursorAfter.getInt(1));
            listAfter.add(afterItems);
            cursorAfter.moveToNext();
        }
        android.util.Log.d("*List after delete", Integer.toString(listAfter.size()));
        assertTrue(listBefore.size() > listAfter.size());
    }

    public void testWhenFirstItemClickedNameCorrect() {

        final ListView list = (ListView) this.main.findViewById(R.id.listView);
        assertNotNull("The list was not loaded", list);
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                int position = 0;
                list.performItemClick(list.getAdapter().getView(position, null, null),
                        position, list.getAdapter().getItemId(position));
            }

        });

        String eventName = this.main.getEventNameToPass();
        assertEquals("Gym", eventName);
    }

    @Override
    protected void setUp() {
        this.main = getActivity();
        this.db = DatabaseAccess.getInstance(this.main);
    }
}

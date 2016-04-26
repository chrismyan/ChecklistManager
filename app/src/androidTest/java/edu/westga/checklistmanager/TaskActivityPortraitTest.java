package edu.westga.checklistmanager;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;

import java.util.List;

import edu.westga.checklistmanager.controller.TaskActivity;
import edu.westga.checklistmanager.model.DatabaseAccess;

/**
 * Created by Chris Dunmeyer and Chris Yan on 4/25/2016.
 */
public class TaskActivityPortraitTest extends ActivityInstrumentationTestCase2<TaskActivity> {
    TaskActivity taskActivity;
    ListView taskListView;

    public TaskActivityPortraitTest() {
        super(TaskActivity.class);
    }

    public void testIfTaskActivityNotNull() {
        this.taskActivity = getActivity();
        assertNotNull(this.taskActivity);
    }


    public void testIfClickFirstItemWaterCorrect() {
        this.taskActivity = getActivity();
        this.taskListView = (ListView) this.taskActivity.findViewById(R.id.taskListView);
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                int position = 0;
                taskListView.performItemClick(taskListView.getAdapter().getView(position, null, null),
                        position, taskListView.getAdapter().getItemId(position));
            }

        });
        String clickedItem = this.taskActivity.getClickedItemName();
        assertEquals("Water", clickedItem);
    }


    @Override
    protected void setUp() {
        Intent taskIntent = new Intent(getInstrumentation().getTargetContext(), TaskActivity.class);
        taskIntent.putExtra("event",1);
        setActivityIntent(taskIntent);
//        this.db = DatabaseAccess.getInstance(this.main);
    }
}

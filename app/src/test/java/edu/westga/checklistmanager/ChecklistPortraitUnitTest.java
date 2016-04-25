package edu.westga.checklistmanager;

import android.database.Cursor;
import android.provider.ContactsContract;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import dalvik.annotation.TestTargetClass;

import static org.junit.Assert.*;

import edu.westga.checklistmanager.controller.MainActivity;
import edu.westga.checklistmanager.model.DatabaseAccess;
import edu.westga.checklistmanager.model.DatabaseOpenHelper;
import edu.westga.checklistmanager.model.Events;
import edu.westga.checklistmanager.model.TaskItems;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ChecklistPortraitUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testIfGetEventNameCorrect() {
        Events newEvent = new Events();
        newEvent.setName("Honeymoon");
        assertEquals("Honeymoon", newEvent.getName());
    }

    @Test
    public void testIfGetTaskItemNameCorrect() {
        TaskItems newItem = new TaskItems();
        newItem.setName("Get Dinner");
        assertEquals("Get Dinner", newItem.getName());
    }
}

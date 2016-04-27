package edu.westga.checklistmanager.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import edu.westga.checklistmanager.model.DatabaseAccess;
import edu.westga.checklistmanager.R;
import edu.westga.checklistmanager.model.DatabaseOpenHelper;
import edu.westga.checklistmanager.model.TaskItems;

public class TaskActivity extends AppCompatActivity implements AddFragment.AddEventListener{
    private ListView taskListView;
    private int event;
    private DatabaseAccess db;
    private String itemName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Retrieve text from main activity
        Bundle taskData = getIntent().getExtras();
        if(taskData == null) {
            return;
        }
        String eventName = taskData.getString("eventName");
        TextView taskText = (TextView) findViewById(R.id.checklistText);
        taskText.setText(eventName);

        this.event = taskData.getInt("event");

        // find listview
        this.taskListView = (ListView) findViewById(R.id.taskListView);
        this.taskListView.setLongClickable(true);
        this.db = DatabaseAccess.getInstance(this);
        this.db.setEvent(this.event);
        this.db.open();
        populateListView();

        this.registerClickCallBack();
        this.registerLongClickCallBack();
        deleteMessage();
    }

    private void populateListView() {
        // Get Cursor object from database
        this.db.open();
        Cursor todoCursor = db.getTaskItemsCursor(this.event);
        if(todoCursor.getCount() <= 0) {
            String[] emptyMessage = {"Please add a task item above"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,emptyMessage);
            this.taskListView.setAdapter(adapter);

        } else {
            TodoCursorAdapter adapter = new TodoCursorAdapter(this, todoCursor);

            // Configure List View
            this.taskListView.setAdapter(adapter);
        }

    }

    public class TodoCursorAdapter extends CursorAdapter {
        public TodoCursorAdapter(Context context, Cursor cursor) {
            super(context, cursor, 0);
        }

        // The newView method is used to inflate a new view and return it,
        // you don't bind any data to the view at this point.
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_checked, parent, false);
        }

        // The bindView method is used to bind all data to a given view
        // such as setting the text on a TextView.
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            String displayTask = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.COLUMN_TASKITEM_NAME));
            TextView taskItem = (TextView) view.findViewById(android.R.id.text1);
            taskItem.setText(displayTask);

            if (cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseOpenHelper.COLUMN_COMPLETED)) == 1) {
                TaskActivity.this.taskListView.setItemChecked(cursor.getPosition(), true);
            } else {
                TaskActivity.this.taskListView.setItemChecked(cursor.getPosition(), false);
            }
        }
    }


    private void registerClickCallBack() {
        this.taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                TaskActivity.this.setClickItemName(textView.getText().toString());
                CheckedTextView testChecked = (CheckedTextView) view;
                if(testChecked.isChecked()) {
                    TaskItems item = new TaskItems();
                    item.setId((int) id);
                    item.setCompleted(1);
                    isTaskItemCompleted(item);
                } else {
                    TaskItems item = new TaskItems();
                    item.setId((int) id);
                    item.setCompleted(0);
                    isTaskItemCompleted(item);
                }
            }
        });
    }

    public void setClickItemName(String name) {
        this.itemName = name;
    }

    public String getClickedItemName() {
        return this.itemName;
    }

    private void isTaskItemCompleted(TaskItems item) {
        this.db.isTaskItemCompleted(item);
        populateListView();
    }

    private void registerLongClickCallBack() {
        this.taskListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int arg2, long id) {
                Toast.makeText(TaskActivity.this, String.valueOf(id),
                        Toast.LENGTH_LONG).show();
                TextView textView = (TextView) arg1;
                String taskItemName = textView.getText().toString();
                showDeleteDialogCursor((int) id, taskItemName);
                return true;
            }
        });
    }

    public void showDeleteDialogCursor(final int taskItemId, String taskItemName) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Delete task item")
                .setMessage("Are you sure you want to delete " + taskItemName + "? ")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("DELETE", "Task to delete: ");
                        deleteTaskItemCursor(taskItemId);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    @Override
    public void onAddItem(String taskItemName) {
        TaskItems item = new TaskItems();
        item.setName(taskItemName);
        item.setEventId(this.event);
        this.db.addTaskItem(item);
        populateListView();
        deleteMessage();
    }

    public void deleteMessage() {
        Toast.makeText(this,"To delete press and HOLD an item", Toast.LENGTH_LONG).show();
    }

    public void deleteTaskItemCursor(int taskItemId) {
        TaskItems item = new TaskItems();
        item.setId(taskItemId);
        this.db.deleteTaskItem(item);
        populateListView();
    }
}

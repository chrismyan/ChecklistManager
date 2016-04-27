package edu.westga.checklistmanager.controller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import edu.westga.checklistmanager.R;
import edu.westga.checklistmanager.model.DatabaseAccess;
import edu.westga.checklistmanager.model.Events;

public class MainActivity extends AppCompatActivity implements AddFragment.AddEventListener{
    ListView myTaskListView;
    DatabaseAccess db;
    TodoCursorAdapter todoAdapter;
    int clickedEvent;
    String eventName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.myTaskListView = (ListView) findViewById(R.id.listView);
        this.myTaskListView.setLongClickable(true);

        // Access database
        this.db = DatabaseAccess.getInstance(this);

        populateListView();
        registerClickCallBack();
        registerLongClickCallBack();
        deleteMessage();
    }

    public void populateListView() {
        // Get cursor object from database
        Cursor todoCursor = db.getAllEventCursor();
        if(todoCursor.getCount() <= 0) {
            String[] emptyMessage = {"Please add an event above"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, emptyMessage);
            this.myTaskListView.setAdapter(adapter);
        } else {
            this.todoAdapter = new TodoCursorAdapter(this, todoCursor);

            // Configure List View
            this.myTaskListView.setAdapter(this.todoAdapter);
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
            return LayoutInflater.from(context).inflate(R.layout.task_item, parent, false);
        }

        // The bindView method is used to bind all data to a given view
        // such as setting the text on a TextView.
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // Find fields to populate in inflated template
            TextView txtEvent = (TextView) view.findViewById(R.id.listItem);

            // Extract properties from cursor
            String eventName = cursor.getString(cursor.getColumnIndexOrThrow("_eventName"));

            // Populate fields with extracted properties
            txtEvent.setText(eventName);
        }
    }



    private void registerClickCallBack() {
        this.myTaskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View clickedView, int position, long clickedId) {
                TextView textView = (TextView) clickedView;

                Intent checklistIntent = new Intent(MainActivity.this, TaskActivity.class);
                setEventNameToPass(textView.getText().toString());
                checklistIntent.putExtra("event", (int) clickedId);
                checklistIntent.putExtra("eventName", MainActivity.this.eventName);
                startActivity(checklistIntent);
            }
        });
    }

    public void setEventNameToPass(String eventName) {
        this.eventName = eventName;
    }

    public String getEventNameToPass() {
        return this.eventName;
    }

    private void registerLongClickCallBack() {
        this.myTaskListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View view,
                                           final int position, long id) {
                MainActivity.this.clickedEvent = (int) id;
                TextView textView = (TextView) view;
                String event = textView.getText().toString();
                int eventId = (int) id;
                showDeleteDialogCursor(eventId, event);
//                showDeleteDialog(event);
                return true;

            }
        });
    }

    private void showDeleteDialogCursor(final int eventId, String event) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Delete event")
                .setMessage("Are you sure you want to delete " + event + "? ")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("DELETE", "Task to delete: ");
                        deleteEventCursor(eventId);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    public void deleteEventCursor(int eventID) {
        Events deleteEvent = new Events();
        deleteEvent.setId(eventID);
        this.db.deleteEventCursor(deleteEvent);
        populateListView();
    }

    public void deleteMessage() {
        Toast.makeText(this,"To delete press and HOLD an item", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onAddItem(String eventName) {
        Events newEvent = new Events();
        newEvent.setName(eventName);
        this.db.addEvent(newEvent);
        populateListView();
        deleteMessage();
    }
}

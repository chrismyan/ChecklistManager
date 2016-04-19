package edu.westga.checklistmanager.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import edu.westga.checklistmanager.R;
import edu.westga.checklistmanager.model.DatabaseAccess;

public class MainActivity extends AppCompatActivity implements AddFragment.AddEventListener{
    ListView myTaskListView;
    DatabaseAccess db;
    ArrayAdapter<String> adapter;
    List<String> myEventsFromDB;

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
    }

    public void populateListView() {
//        // Create list of items
        this.myEventsFromDB = db.getEvents();

        // Configure adapter
        this.adapter = new ArrayAdapter<String>(
                this,                   // Sets up Context for activity
                R.layout.task_item,     // Layout to use (create)
                myEventsFromDB);               // Items to be displayed

        // Configure List View
        this.myTaskListView.setAdapter(adapter);
    }

    private void registerClickCallBack() {
        this.myTaskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View clickedView, int position, long id) {
                TextView textView = (TextView) clickedView;
                String message = "You clicked " + position + " which is string " +
                        textView.getText();

                Intent checklistIntent = new Intent(MainActivity.this, TaskActivity.class);

                checklistIntent.putExtra("category", (int) position + 1);
                startActivity(checklistIntent);
            }
        });
    }

    private void registerLongClickCallBack() {
        this.myTaskListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int arg2, long arg3) {
                Toast.makeText(MainActivity.this, "Main Activity: this is my Toast message!!! =)",
                        Toast.LENGTH_LONG).show();
                TextView textView = (TextView) arg1;
                String event = textView.getText().toString();
                showDeleteDialog(event);
                return true;

            }
        });
    }

    public void showDeleteDialog(final String event) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Delete event")
                .setMessage("Are you sure you want to delete " + event + "? ")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("DELETE", "Task to delete: ");
                                deleteEvent(event);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
        dialog.show();
    }

//    public void addEvent(String eventName) {
//        if(!eventName.equals("")) {
//            db.addEvent(eventName);
//        } else {
//            return;
//        }
//    }

    public void deleteEvent(String eventName) {
        if(!eventName.equals("")) {
            db.deleteEvent(eventName);
        } else {
            return;
        }
    }

    @Override
    public void onAddItem(String eventName) {
        this.db.addEvent(eventName);
        populateListView();
    }
}

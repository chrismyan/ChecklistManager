package edu.westga.checklistmanager.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import edu.westga.checklistmanager.model.DatabaseAccess;
import edu.westga.checklistmanager.R;

public class TaskActivity extends AppCompatActivity implements AddFragment.AddEventListener{
    private ListView taskListView;
    private int category;
    private DatabaseAccess db;

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
        String event = taskData.getString("event");
        TextView taskText = (TextView) findViewById(R.id.checklistText);
        taskText.setText(event);

        this.category = taskData.getInt("category");

        // find listview
        this.taskListView = (ListView) findViewById(R.id.taskListView);
        this.taskListView.setLongClickable(true);
        this.db = DatabaseAccess.getInstance(this);
        this.db.setEvent(this.category);
        this.db.open();
        populateListView();

        this.registerClickCallBack();
        this.registerLongClickCallBack();
    }

    private void populateListView() {
        List<String> tasks = this.db.getTaskItems(this.category);
        this.db.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, tasks);
        this.taskListView.setAdapter(adapter);

        // Check which tasks are completed
        View v;
        TextView tv;
        for(int counter = 0; counter < this.taskListView.getCount(); counter++) {
            v = this.taskListView.getAdapter().getView(counter, null, null);
            tv = (TextView) v.findViewById(counter);
            String taskItem = adapter.getItem(counter).toString();
            Log.d("Item CHOOSEN", taskItem);

            if(this.db.isItemCompleted(taskItem)) {
                this.taskListView.setItemChecked(counter, true);
            } else {
                this.taskListView.setItemChecked(counter, false);
            }
        }
    }

    private void registerClickCallBack() {
        this.taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                String message = "You clicked " + position + " which is string " +
                        textView.getText();
                CheckedTextView testChecked = (CheckedTextView) view;
                if(testChecked.isChecked()) {
                    Log.d("BOOLEAN", "TRUE " + textView.getText());
                } else {
                    Log.d("BOOLEAN", "FALSE " +  textView.getText());
                }

            }
        });
    }


    private void registerLongClickCallBack() {
        this.taskListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int arg2, long arg3) {
                Toast.makeText(TaskActivity.this, "Task Activity: this is my Toast message!!! =)",
                        Toast.LENGTH_LONG).show();
                TextView textView = (TextView) arg1;
                String taskItem = textView.getText().toString();
                showDeleteDialog(taskItem);
                return true;
            }
        });
    }

    public void showDeleteDialog(String taskItem) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Delete task item")
                .setMessage("Are you sure you want to delete " + taskItem + "? ")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("DELETE", "Task to add: ");
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    @Override
    public void onAddItem(String eventName) {
        this.db.AddTaskItem(eventName,this.category);
    }
}

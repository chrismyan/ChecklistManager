package edu.westga.checklistmanager.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import edu.westga.checklistmanager.R;

public class MainActivity extends AppCompatActivity {
    ListView myTaskListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        this.myTaskListView = (ListView) findViewById(R.id.listView);

        populateListView();
        registerClickCallBack();
    }



    public void populateListView() {
        // Create list of items
        String[] myTasks ={"Euro Trip", "Hiking", "Camping"};

        // Configure adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,                   // Sets up Context for activity
                R.layout.task_item,     // Layout to use (create)
                myTasks);               // Items to be displayed

        // Configure List View
//        ListView myTaskListView = (ListView) findViewById(R.id.listView);
        this.myTaskListView.setAdapter(adapter);
    }

    private void registerClickCallBack() {
//    ListView myTasksListView = (ListView) findViewById(R.id.listView);
        this.myTaskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View clickedView, int position, long id) {
                TextView textView = (TextView) clickedView;
                String message = "You clicked " + position + " which is string " +
                        textView.getText();
//                Toast.makeText(MainActivity.this,message, Toast.LENGTH_SHORT).show();

                Intent checklistIntent = new Intent(MainActivity.this, TaskActivity.class);

                checklistIntent.putExtra("taskMessage",message);
                checklistIntent.putExtra("category", (int) position + 1);
                startActivity(checklistIntent);
            }
        });
    }
}

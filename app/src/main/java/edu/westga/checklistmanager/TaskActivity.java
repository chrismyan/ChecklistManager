package edu.westga.checklistmanager;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class TaskActivity extends AppCompatActivity {
    private ListView taskListView;
    private int category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Retrieve text from main activity
        Bundle taskData = getIntent().getExtras();
        if(taskData == null) {
            return;
        }
        String taskMessage = taskData.getString("taskMessage");
        TextView taskText = (TextView) findViewById(R.id.checklistText);
        taskText.setText(taskMessage );

        this.category = taskData.getInt("category");

        // find listview
        this.taskListView = (ListView) findViewById(R.id.taskListView);
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.setCategory(this.category);
        databaseAccess.open();
        List<String> tasks = databaseAccess.getTaskItems(this.category);
        databaseAccess.close();

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, tasks);
        this.taskListView.setAdapter(adapter);

        // Check which tasks are completed
        View v;
        TextView tv;
        for(int counter = 0; counter < this.taskListView.getCount(); counter++) {
            v = this.taskListView.getAdapter().getView(counter, null, null);
            tv = (TextView) v.findViewById(counter);
            String str = adapter.getItem(counter).toString();
            Log.d("Item CHOOSEN", str);
//            tv.setPaintFlags(tv.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        registerClickCallBack();
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

}

package edu.westga.checklistmanager.controller;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import edu.westga.checklistmanager.R;
import edu.westga.checklistmanager.model.DatabaseAccess;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment {
    EditText addEvent;
    ImageButton addBtn;
    String newEventString;
    AddEventListener listener;
    DatabaseAccess db;

    public interface AddEventListener{
        void onAddItem(String eventName);
    }

    public AddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View theView = inflater.inflate(R.layout.fragment_add, container, false);

        this.db = DatabaseAccess.getInstance(getContext());
        this.addEvent = (EditText) theView.findViewById(R.id.editTxtAdd);
        this.addBtn = (ImageButton) theView.findViewById(R.id.btnAdd);
        this.addBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    setEventString();
                    addEvent();
                } catch(Exception ex) {
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return theView;
    }

    public void addEvent() {
        this.listener.onAddItem(this.newEventString);
        this.addEvent.setText("");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.listener = (AddEventListener) context;
    }

    private void setEventString() throws Exception {
        if (this.addEvent.getText().toString().equals("")) {
            throw new Exception("This field is required");
        } else {
            this.newEventString = this.addEvent.getText().toString();
        }
    }
}

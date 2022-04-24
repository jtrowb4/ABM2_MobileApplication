package com.c196.abm2_mobileapplication.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.c196.abm2_mobileapplication.R;
import com.c196.abm2_mobileapplication.database.Repository;
import com.c196.abm2_mobileapplication.model.Assessment;

import java.util.ArrayList;
import java.util.List;


public class AssessmentDetail extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Repository repository;

    EditText editTitle;
    EditText editStartDate;
    EditText editEndDate;
    Spinner editTypeSpinner;

    int assessmentID;
    String title;
    String startDate;
    String endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_editor_assessment);

        repository = new Repository(getApplication());

        List<String> types = new ArrayList<>();
        types.add(0, "Performance");
        types.add(1,"Objective");

        editTitle = findViewById(R.id.assessmentTitleText);
        //editTypeSpinner = findViewById(R.id.assessmentSpinner);
        editTypeSpinner.setOnItemSelectedListener(this);
        editStartDate = findViewById(R.id.termStartText);
        editEndDate = findViewById(R.id.termEndText);

        assessmentID = getIntent().getIntExtra("id", -1);
        types = getIntent().getStringArrayListExtra("type");
        title = getIntent().getStringExtra("title");
        startDate = getIntent().getStringExtra("start date");
        endDate = getIntent().getStringExtra("end date");

        editTitle.setText(title);
        editStartDate.setText(startDate);
        editEndDate.setText(endDate);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editTypeSpinner.setAdapter(dataAdapter);
    }

    /*public void onSave(View view){
        Assessment assessment;
        if(assessmentID == -1){
            int newID = 0;
            assessment = new Assessment(newID, editTitle.getText().toString(), editTypeSpinner.getAdapter().toString(), editStartDate.getText().toString(), editEndDate.getText().toString());
            repository.insertAssessment(assessment);
        }
        else{
            assessment = new Assessment (assessmentID, editTitle.getText().toString(), editTypeSpinner.getAdapter().toString(), editStartDate.getText().toString(), editEndDate.getText().toString());
            repository.updateAssessment(assessment);
        }
        closeContextMenu();

    }*/

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        // On selecting a spinner item
        String item = adapterView.getItemAtPosition(position).toString();
        // Showing selected spinner item
        Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

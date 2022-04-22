package com.c196.abm2_mobileapplication.controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.c196.abm2_mobileapplication.R;
import com.c196.abm2_mobileapplication.database.Repository;
import com.c196.abm2_mobileapplication.model.Assessment;
import com.c196.abm2_mobileapplication.model.Term;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AssessmentScreenActivity extends AppCompatActivity {
    //FAB items
    boolean isShowing = false;
    FloatingActionButton addAssessment;
    //DialogBox items
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog alertDialog;
    EditText editTitle;
    EditText editStartDate;
    EditText editEndDate;
    //Spinner editTypeSpinner;
    RadioGroup assessmentGroup;
    RadioButton paButton;
    RadioButton oaButton;
    Button saveButton;
    Button cancelButton;

    //for saving assessment to repo
    Repository repository;
    int assessmentID;
    String assessmentTitle;
    String assessmentType;
    String startDate;
    String endDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_screen);
        RecyclerView recyclerView = findViewById(R.id.assessmentRecyclerView);
        Repository repo = new Repository(getApplication());
        List<Assessment> assessments = repo.getAllAssessments();
        AssessmentAdapter adapter = new AssessmentAdapter(getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter.setAssessments(assessments);

        addAssessment = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        addAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isShowing) {
                    addAssessment.hide();
                    newAssessmentDialog();
                    isShowing = true;
                }
                else{
                    isShowing = false;
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate((R.menu.appbar_menu_term), menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void newAssessmentDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        View newAssessmentPopup = getLayoutInflater().inflate(R.layout.popup_new_assesment, null);
        editTitle = (EditText) newAssessmentPopup.findViewById(R.id.assessmentTitleText);
        //editTypeSpinner = (Spinner) newAssessmentPopup.findViewById(R.id.assessmentTypeSpinner);
        editStartDate = (EditText) newAssessmentPopup.findViewById(R.id.assessmentStart);
        editEndDate = (EditText) newAssessmentPopup.findViewById(R.id.assessmentEnd);

        saveButton = (Button) newAssessmentPopup.findViewById(R.id.saveButton);
        cancelButton = (Button) newAssessmentPopup.findViewById(R.id.cancelButton);

        /*List<String> types = new ArrayList<>();
        types.add(0, "Performance");
        types.add(1,"Objective");*/

        //ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //editTypeSpinner.setAdapter(dataAdapter);

        dialogBuilder.setView(newAssessmentPopup);
        alertDialog = dialogBuilder.create();
        alertDialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //Save Assessment

            assessmentTitle = editTitle.getText().toString();
            startDate = editStartDate.getText().toString();
            endDate = editEndDate.getText().toString();
            Assessment assessment;
            if(assessmentID == -1){
                int newID = 0;
                assessment = new Assessment(newID, assessmentTitle, assessmentType, startDate, endDate);
                repository.insertAssessment(assessment);
            }
/*            else{
                term = new Term (termID, editTitle.getText().toString(), editStartDate.getText().toString(), editEndDate.getText().toString());
                repository.updateTerm(term);
            }*/

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    public void onRadioButtonClicked(View view){
        switch (view.getId()){
            case R.id.paButton:
                assessmentType = paButton.getText().toString();
                break;
            case R.id.oaButton:
                assessmentType = oaButton.getText().toString();
                break;
        }

    }


}
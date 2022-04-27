package com.c196.abm2_mobileapplication.controller;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.RecyclerView;

import com.c196.abm2_mobileapplication.R;
import com.c196.abm2_mobileapplication.database.Repository;
import com.c196.abm2_mobileapplication.model.Assessment;
import com.c196.abm2_mobileapplication.model.CourseNotes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class AssessmentDetail extends AppCompatActivity{

    Repository repository;

    EditText editTitle;
    EditText editStartDate;
    EditText editEndDate;
    Spinner editTypeSpinner;

    int assessmentID;
    String title;
    String startDate;
    String endDate;

    private Assessment currentAssessment;
    private EditText tempText;
    private String assessmentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        repository = new Repository(getApplication());
        List<Assessment> assessments = repository.getAllAssessments();
        for (Assessment assessment : assessments) {
            int i = getIntent().getIntExtra("id", -1);
            if (assessment.getAssessmentID() == i) {
                setTitle(assessment.getAssessmentTitle());
                currentAssessment = assessment;
                break;
            } else {
                setTitle("A Title");
            }
        }
        setLabels();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.appbar_menu_detail, menu);
        menu.setGroupVisible(R.id.overFlowItems, false);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch(menuItem.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_edit:
                editNote();
                return true;
            case R.id.action_delete:
                deleteAssessment();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void deleteAssessment() {
        this.finish();
        repository.deleteAssessment(currentAssessment);
        recreate();
    }

    public void editNote(){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View editAssessmentPopup = getLayoutInflater().inflate(R.layout.popup_new_assessment, null);
        editTitle = (EditText) editAssessmentPopup.findViewById(R.id.assessmentTitleText);
        editStartDate = (EditText) editAssessmentPopup.findViewById(R.id.assessmentStart);
        editEndDate = (EditText) editAssessmentPopup.findViewById(R.id.assessmentEnd);
        RadioGroup assessmentGroup = (RadioGroup) editAssessmentPopup.findViewById(R.id.assessmentRadio);
        RadioButton paButton = (RadioButton) editAssessmentPopup.findViewById(R.id.paButton);
        RadioButton oaButton = (RadioButton) editAssessmentPopup.findViewById(R.id.oaButton);

        editTitle.setText(getIntent().getStringExtra("title"));
        editStartDate.setText(getIntent().getStringExtra("start date"));
        editEndDate.setText(getIntent().getStringExtra("end date"));

        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel(tempText);
            }

            private void updateLabel(EditText et){
                String dateFormat = "MM-dd-yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
                et.setText(simpleDateFormat.format(myCalendar.getTime()));
            }
        };
        editStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempText = editStartDate;
                new DatePickerDialog(AssessmentDetail.this, date,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                editStartDate = tempText;
            }
        });
        editEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempText = editEndDate;
                new DatePickerDialog(AssessmentDetail.this, date,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                editEndDate = tempText;
            }
        });

        String selection = getIntent().getStringExtra("type");
        if (selection.equals("Performance Assessment")){
            paButton.setSelected(true);
            assessmentType = paButton.getText().toString();
        }
        else{
            oaButton.setSelected(true);
            assessmentType = oaButton.getText().toString();
        }

        Button saveButton = (Button) editAssessmentPopup.findViewById(R.id.saveButton);
        Button cancelButton = (Button) editAssessmentPopup.findViewById(R.id.cancelButton);

        dialogBuilder.setView(editAssessmentPopup);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Save Assessment
                assessmentID = currentAssessment.getAssessmentID();
                String assessmentTitle = editTitle.getText().toString();
                startDate = editStartDate.getText().toString();
                endDate = editEndDate.getText().toString();
                int courseID = currentAssessment.getCourseID();

                Assessment assessment;
                assessment = new Assessment(assessmentID, assessmentTitle, assessmentType, startDate, endDate, courseID);
                repository.updateAssessment(assessment);
                recreate();
                alertDialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    public void setLabels(){

        TextView startDate = findViewById(R.id.assessmentStartDate);
        TextView endDate = findViewById(R.id.assessmentEndDate);
        TextView type = findViewById(R.id.assessmentType);

        startDate.setText("Assessment Start: " + currentAssessment.getStartDate());
        endDate.setText("Assessment End: " + currentAssessment.getEndDate());
        type.setText("Assessment Type: " + currentAssessment.getAssessmentType());

    }
}

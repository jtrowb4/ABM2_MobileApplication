package com.c196.abm2_mobileapplication.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.c196.abm2_mobileapplication.R;
import com.c196.abm2_mobileapplication.database.Repository;
import com.c196.abm2_mobileapplication.model.Assessment;
import com.c196.abm2_mobileapplication.model.Course;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CourseDetail extends AppCompatActivity {

    //Contains information on Course Selected
    //Contains list of Assessments

    //FAB items
    boolean isShowing = false;
    FloatingActionButton addAssessment;
    //DialogBox items
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog alertDialog;
    EditText editTitle;
    EditText editStartDate;
    EditText editEndDate;
    EditText tempText;
    RadioGroup assessmentGroup;
    RadioButton paButton;
    RadioButton oaButton;
    Button saveButton;
    Button cancelButton;


    //for saving assessment to repo
    Repository repo;
    int assessmentID;
    String assessmentTitle;
    String assessmentType;
    String startDate;
    String endDate;

    Course currentCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_screen);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        repo = new Repository(getApplication());
        List<Course> courses = repo.getAllCourse();
        for (Course course : courses) {
            int i = getIntent().getIntExtra("id", -1);
            if (course.getTermID() == i && course.getTermID() != -1) {
                setTitle(course.getCourseTitle());
                currentCourse = course;
                break;
            } else {
                setTitle("A Title");
            }
        }
        ArrayList<Assessment> associatedAssessments = new ArrayList<>();
        List<Assessment> assessments = repo.getAllAssessments();
        for (Assessment assessment : assessments) {

            if (assessment.getCourseID() == currentCourse.getCourseID()) {
                associatedAssessments.add(assessment);
            }
        }

        AssessmentAdapter adapter = new AssessmentAdapter(getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter.setAssessments(associatedAssessments);

        addAssessment = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        addAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isShowing) {
                    addAssessment.hide();
                    newCourseDialog();
                    isShowing = true;
                } else {
                    isShowing = false;
                }
            }
        });

        final Assessment[] deletedAssessment = {null};
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                if (direction == ItemTouchHelper.LEFT) {
                    Assessment assessmentSelected;
                    assessmentSelected = adapter.getAssessmentPosition(viewHolder.getAdapterPosition());
                    deletedAssessment[0] = assessmentSelected;
                    assessments.remove(assessmentSelected);
                    associatedAssessments.remove(assessmentSelected);
                    repo.deleteAssessment(assessmentSelected);
                    Snackbar.make(recyclerView, "Assessment Deleted", Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    assessments.add(deletedAssessment[0]);
                                    associatedAssessments.add(deletedAssessment[0]);
                                    repo.insertAssessment(deletedAssessment[0]);
                                    recreate();
                                }
                            }).show();
                    //case ItemTouchHelper.RIGHT:
                    //Modify
                }
            }

        }).attachToRecyclerView(recyclerView);
    }

    public void newCourseDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        View newAssessmentPopup = getLayoutInflater().inflate(R.layout.popup_new_assessment, null);
        editTitle = (EditText) newAssessmentPopup.findViewById(R.id.assessmentTitleText);
        editStartDate = (EditText) newAssessmentPopup.findViewById(R.id.assessmentStart);
        editEndDate = (EditText) newAssessmentPopup.findViewById(R.id.assessmentEnd);
        assessmentGroup = (RadioGroup) newAssessmentPopup.findViewById(R.id.assessmentRadio);
        paButton = (RadioButton) newAssessmentPopup.findViewById(R.id.paButton);
        oaButton = (RadioButton) newAssessmentPopup.findViewById(R.id.oaButton);

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
                new DatePickerDialog(CourseDetail.this, date,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                editStartDate = tempText;
            }
        });
        editEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempText = editEndDate;
                new DatePickerDialog(CourseDetail.this, date,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                editEndDate = tempText;
            }
        });

        assessmentGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRadioButtonClicked(view);
            }
        });


        saveButton = (Button) newAssessmentPopup.findViewById(R.id.saveButton);
        cancelButton = (Button) newAssessmentPopup.findViewById(R.id.cancelButton);

        dialogBuilder.setView(newAssessmentPopup);
        alertDialog = dialogBuilder.create();
        alertDialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Save Assessment
                assessmentID = 0;
                assessmentTitle = editTitle.getText().toString();
                startDate = editStartDate.getText().toString();
                endDate = editEndDate.getText().toString();
                int courseID = currentCourse.getCourseID();

                Assessment assessment;
                assessment = new Assessment(assessmentID, assessmentTitle, startDate, endDate, assessmentType, courseID);
                repo.insertAssessment(assessment);
                System.out.println(currentCourse.getCourseTitle() + "added: " + assessment.getAssessmentTitle());
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

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_share_notify, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch(menuItem.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Text from field");
                sendIntent.putExtra(Intent.EXTRA_TITLE, "Title");
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                return true;
            case R.id.notify:
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
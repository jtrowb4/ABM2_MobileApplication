package com.c196.abm2_mobileapplication.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.c196.abm2_mobileapplication.R;
import com.c196.abm2_mobileapplication.database.Repository;
import com.c196.abm2_mobileapplication.main.MainActivity;
import com.c196.abm2_mobileapplication.model.Assessment;
import com.c196.abm2_mobileapplication.model.Course;
import com.c196.abm2_mobileapplication.model.CourseNotes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    public static Course currentCourse;
    private EditText editCourseStart;
    private EditText editCourseEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        repo = new Repository(getApplication());
        List<Course> courses = repo.getAllCourse();
        for (Course course : courses) {
            int i = getIntent().getIntExtra("course id", -1);
            if (course.getCourseID() == i) {
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

        setLabels();

        addAssessment = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        addAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isShowing) {
                    addAssessment.hide();
                    newAssessmentDialog();
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

    public void newAssessmentDialog(){
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
                assessment = new Assessment(assessmentID, assessmentTitle, assessmentType, startDate, endDate, courseID);
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

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.appbar_menu_detail, menu);
        MenuItem item = menu.findItem(R.id.share);
        if (item != null){
            item.setVisible(false);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch(menuItem.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.notify:
                String dateFromScreenStart = currentCourse.getStartDate();
                String alertStart = "Your Course: " + currentCourse.getCourseTitle() + " Starts Today";
                setNotification(CourseDetail.this,dateFromScreenStart, alertStart);
                Toast.makeText(CourseDetail.this, "Notification Alert set for Course Start", Toast.LENGTH_LONG);
                String dateFromScreenEnd = currentCourse.getEndDate();
                String alertEnd = "Your Course: " + currentCourse.getCourseTitle() + " Ends Today";
                setNotification(CourseDetail.this, dateFromScreenEnd, alertEnd);
                Toast.makeText(CourseDetail.this, "Notification Alert set for Course Start", Toast.LENGTH_LONG);
                return true;
            case R.id.action_edit:
                editCourse();
                return true;
            case R.id.action_delete:
                deleteCourse();
                return true;
            case R.id.noted:
                addNote();
                return true;
            case R.id.viewNote:
            Intent notes = new Intent(CourseDetail.this, NotesListActivity.class);
            startActivity(notes);
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void deleteCourse(){
        //this.finish();
        List<Assessment> assessments = repo.getAllAssessments();
        for(Assessment assessment: assessments){
            if (assessment.getCourseID()==currentCourse.getCourseID()){
                repo.deleteAssessment(assessment);
            }
        }
        repo.deleteCourse(currentCourse);
        recreate();
    }

    public void editCourse(){
        dialogBuilder = new AlertDialog.Builder(this);
        View editCoursePopup = getLayoutInflater().inflate(R.layout.popup_new_course, null);
        EditText editCourseTitle = (EditText) editCoursePopup.findViewById(R.id.courseTitleText);
        editCourseStart = (EditText) editCoursePopup.findViewById(R.id.courseStartText);
        editCourseEnd = (EditText) editCoursePopup.findViewById(R.id.courseEndText);
        Spinner editStatus = (Spinner) editCoursePopup.findViewById(R.id.statusSpinner);
        EditText editName = (EditText) editCoursePopup.findViewById(R.id.instructorName);
        EditText editPhone = (EditText) editCoursePopup.findViewById(R.id.instructorPhone);
        EditText editEmail = (EditText) editCoursePopup.findViewById(R.id.instructorEmail);

        String statusString = getIntent().getStringExtra("status");
        String[] statusArray = getResources().getStringArray(R.array.status_array);

        for (int i = 0; i < statusArray.length; i++) {
            if(statusArray[i].equals(statusString)){
                editStatus.setSelection(i);
                break;
            }
        }

        editCourseTitle.setText(getIntent().getStringExtra("title"));
        editCourseStart.setText(getIntent().getStringExtra("start date"));
        editCourseEnd.setText(getIntent().getStringExtra("end date"));
        editName.setText(getIntent().getStringExtra("instructor name")) ;
        editPhone.setText(getIntent().getStringExtra("phone"));
        editEmail.setText(getIntent().getStringExtra("email"));


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
        editCourseStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempText = editCourseStart;
                new DatePickerDialog(CourseDetail.this, date,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                editCourseStart = tempText;
            }
        });
        editCourseEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempText = editCourseEnd;
                new DatePickerDialog(CourseDetail.this, date,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                editCourseEnd = tempText;
            }
        });

        saveButton = (Button) editCoursePopup.findViewById(R.id.saveButton);
        cancelButton = (Button) editCoursePopup.findViewById(R.id.cancelButton);

        dialogBuilder.setView(editCoursePopup);
        alertDialog = dialogBuilder.create();
        alertDialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Save Course
                int courseID = getIntent().getIntExtra("course id", -1);
                String courseTitle = editCourseTitle.getText().toString();
                startDate = editCourseStart.getText().toString();
                endDate = editCourseEnd.getText().toString();
                String status = editStatus.getSelectedItem().toString();
                String instructorName = editName.getText().toString();
                String instructorPhone = editPhone.getText().toString();
                String instructorEmail = editEmail.getText().toString();
                int termID = getIntent().getIntExtra("term id", -1);

                Course course;
                course = new Course(courseID, courseTitle, startDate, endDate, status, instructorName, instructorPhone, instructorEmail, termID);
                repo.updateCourse(course);
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

    public void addNote(){
        dialogBuilder = new AlertDialog.Builder(this);
        View newNotePopup = getLayoutInflater().inflate(R.layout.popup_new_note, null);
        EditText editSubject = (EditText) newNotePopup.findViewById(R.id.noteTitleText);
        EditText editBody = (EditText) newNotePopup.findViewById(R.id.noteBodyText);

        Button saveButton = (Button) newNotePopup.findViewById(R.id.saveButton);
        Button cancelButton = (Button) newNotePopup.findViewById(R.id.cancelButton);

        dialogBuilder.setView(newNotePopup);
        alertDialog = dialogBuilder.create();
        alertDialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Save Course
                int noteID = 0;
                String noteTitle = editSubject.getText().toString();
                String noteBody = editBody.getText().toString();
                int courseID = currentCourse.getCourseID();

                CourseNotes note;
                note = new CourseNotes(noteID, noteTitle, noteBody, courseID);
                repo.insertNote(note);
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

        TextView startDate = findViewById(R.id.courseStart);
        TextView endDate = findViewById(R.id.courseEnd);
        TextView instructor = findViewById(R.id.instructor);
        TextView status = findViewById(R.id.status);
        TextView email = findViewById(R.id.email);
        TextView phone = findViewById(R.id.phone);

        startDate.setText("Course Start: " + currentCourse.getStartDate());
        endDate.setText("Course End: " + currentCourse.getEndDate());
        instructor.setText("Instructor: " + currentCourse.getInstructorName());
        status.setText("Status: " + currentCourse.getStatus());
        email.setText("Email: " + currentCourse.getInstructorEmail());
        phone.setText("Phone: " + currentCourse.getInstructorPhone());

    }

    public void setNotification(Context context, String date, String notification) {

        String dateFromScreen = date;

        Date notifyDate=null;

        String dateFormat = "MM-dd-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);

        try{
            notifyDate = simpleDateFormat.parse(dateFromScreen);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long trigger = notifyDate.getTime();
        Intent intent=new Intent(context, NotificationReceiver.class);
        intent.putExtra("key",notification);
        PendingIntent sender = PendingIntent.getBroadcast(context, MainActivity.numAlert++, intent ,0);
        AlarmManager alarmManager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,trigger,sender);
    }
}
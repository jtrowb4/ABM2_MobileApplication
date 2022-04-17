package com.c196.abm2_mobileapplication.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.c196.abm2_mobileapplication.R;
import com.c196.abm2_mobileapplication.database.Repository;
import com.c196.abm2_mobileapplication.model.Course;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CourseDetail extends AppCompatActivity {

    Repository repository;
    //EditText editCourseID;
    EditText editTitle;
    EditText editStartDate;
    EditText editEndDate;
    EditText editStatus;
    EditText editInstructorName;
    EditText editInstructorPhone;
    EditText editInstructorEmail;

    int courseID;
    String title;
    DatePickerDialog.OnDateSetListener startDate;
    DatePickerDialog.OnDateSetListener endDate;
    String status;
    String instructorName;
    String instructorPhone;
    String instructorEmail;

    final Calendar myCalendarStart = Calendar.getInstance();
    final Calendar myCalendarEnd = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        repository = new Repository(getApplication());

        editTitle = findViewById(R.id.courseTitleText);
        editStartDate = findViewById(R.id.courseStartText);
        String myFormat = "MM-dd-yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        editStartDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Date date;
                String dateString = editStartDate.getText().toString();
                if(dateString.equals("")){
                    dateString = "01-01-2022";
                }
                try{
                    myCalendarStart.setTime(dateFormat.parse(dateString));
                }catch(ParseException e){
                    e.printStackTrace();
                }
                new DatePickerDialog(CourseDetail.this, startDate, myCalendarStart.get(Calendar.YEAR),
                        myCalendarStart.get(Calendar.MONTH), myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        editEndDate = findViewById(R.id.courseEndText);
        editStartDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Date date;
                String dateString = editStartDate.getText().toString();
                if(dateString.equals("")){
                    dateString = "01-01-2022";
                }
                try{
                    myCalendarStart.setTime(dateFormat.parse(dateString));
                }catch(ParseException e){
                    e.printStackTrace();
                }
                new DatePickerDialog(CourseDetail.this, endDate, myCalendarEnd.get(Calendar.YEAR),
                        myCalendarEnd.get(Calendar.MONTH), myCalendarEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        editStatus = findViewById(R.id.courseStatusText);
        editInstructorName = findViewById(R.id.instructorNameText);
        editInstructorPhone = findViewById(R.id.instructorPhoneText);
        editInstructorEmail = findViewById(R.id.instructorEmailText);

        courseID = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        startDate = getIntent().getParcelableExtra("start date");
        endDate = getIntent().getParcelableExtra("end date");
        status = getIntent().getStringExtra("status");
        instructorName = getIntent().getStringExtra("instructor name");
        instructorPhone = getIntent().getStringExtra("phone");
        instructorEmail = getIntent().getStringExtra("email");

        editTitle.setText(title);
        editStartDate.setText(;
//        editEndDate.setText(endDate.toString());
        editStatus.setText(status);
        editInstructorName.setText(instructorName);
        editInstructorPhone.setText(instructorPhone);
        editInstructorEmail.setText(instructorEmail);
    }

    public void onSave(View view){
        Course course;
        if(courseID == -1){
            int newID = 0;
            course = new Course(newID, editTitle.getText().toString(), editStartDate.getText().toString(),
                    editEndDate.getText().toString(), editStatus.getText().toString(), editInstructorName.getText().toString(),
                    editInstructorPhone.getText().toString(), editInstructorEmail.getText().toString());
            repository.insertCourse(course);
        }
        else{
            course = new Course(courseID, editTitle.getText().toString(), editStartDate.getText().toString(),
                    editEndDate.getText().toString(), editStatus.getText().toString(), editInstructorName.getText().toString(),
                    editInstructorPhone.getText().toString(), editInstructorEmail.getText().toString());
            repository.updateCourse(course);
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
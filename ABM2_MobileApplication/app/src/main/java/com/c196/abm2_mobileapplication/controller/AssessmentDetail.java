package com.c196.abm2_mobileapplication.controller;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.c196.abm2_mobileapplication.R;
import com.c196.abm2_mobileapplication.database.Repository;
import com.c196.abm2_mobileapplication.main.MainActivity;
import com.c196.abm2_mobileapplication.model.Assessment;
import com.google.android.material.snackbar.Snackbar;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class AssessmentDetail extends AppCompatActivity{

    Repository repository;

    EditText editTitle;
    EditText editStartDate;
    EditText editEndDate;

    int assessmentID;
    String startDate;
    String endDate;

    private Assessment currentAssessment;
    private EditText tempText;
    private String assessmentType;
    private RadioButton paButton;
    private RadioButton oaButton;
    private RadioGroup assessmentGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);
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
        MenuItem share = menu.findItem(R.id.share);
        if (share != null){
            share.setVisible(false);
        }
        MenuItem add = menu.findItem(R.id.noted);
        if (add != null){
            add.setVisible(false);
        }
        MenuItem view = menu.findItem(R.id.viewNote);
        if (view != null){
            view.setVisible(false);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch(menuItem.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.notify:
                String dateFromScreenStart = currentAssessment.getStartDate();
                String alertStart = "Your Assessment: " + currentAssessment.getAssessmentTitle() + " Starts Today";
                setNotification(AssessmentDetail.this, dateFromScreenStart, alertStart);
                Toast.makeText(AssessmentDetail.this, "Notification Alert set for Assessment Start", Toast.LENGTH_LONG);
                String dateFromScreenEnd = currentAssessment.getEndDate();
                String alertEnd = "Your Assessment: " + currentAssessment.getAssessmentTitle() + " Ends Today";
                setNotification(AssessmentDetail.this, dateFromScreenEnd, alertEnd);
                Toast.makeText(AssessmentDetail.this, "Notification Alert set for Assessment End", Toast.LENGTH_LONG);
                return true;
            case R.id.action_edit:
                editAssessment();
                return true;
            case R.id.action_delete:
                deleteAssessment();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void deleteAssessment() {

        repository.deleteAssessment(currentAssessment);
        this.finish();
    }

    public void editAssessment(){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View editAssessmentPopup = getLayoutInflater().inflate(R.layout.popup_new_assessment, null);
        editTitle = (EditText) editAssessmentPopup.findViewById(R.id.assessmentTitleText);
        editStartDate = (EditText) editAssessmentPopup.findViewById(R.id.assessmentStart);
        editEndDate = (EditText) editAssessmentPopup.findViewById(R.id.assessmentEnd);
        assessmentGroup = (RadioGroup) editAssessmentPopup.findViewById(R.id.assessmentRadio);
        paButton = (RadioButton) editAssessmentPopup.findViewById(R.id.paButton);
        oaButton = (RadioButton) editAssessmentPopup.findViewById(R.id.oaButton);

        editTitle.setText(getIntent().getStringExtra("title"));
        editStartDate.setText(getIntent().getStringExtra("start date"));
        editEndDate.setText(getIntent().getStringExtra("end date"));

        String type = currentAssessment.getAssessmentType();
        if (type.equals(paButton.getText().toString())){
            paButton.setChecked(true);
            assessmentType = paButton.getText().toString();
        }
        else if (type.equals(oaButton.getText().toString()) ){
            oaButton.setChecked(true);
            assessmentType = oaButton.getText().toString();
        }
        else{
            assessmentType = "";
            paButton.setChecked(false);
            oaButton.setChecked(false);
        }

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

        assessmentGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRadioButtonClicked(view);
            }
        });


        Button saveButton = (Button) editAssessmentPopup.findViewById(R.id.saveButton);
        Button cancelButton = (Button) editAssessmentPopup.findViewById(R.id.cancelButton);

        dialogBuilder.setView(editAssessmentPopup);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Save Assessment
                try {
                    //check for empty values
                    if ((editTitle.getText().toString().equals("")) || (editStartDate.getText().toString()).equals("") ||
                            (editEndDate.getText().toString().equals("")))
                    {
                        throw new Exception("All Fields and Selections are Required");
                    }

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

                catch (Exception e){
                    RecyclerView recyclerView = findViewById(R.id.recyclerView);
                    Snackbar.make(recyclerView, Objects.requireNonNull(e.getMessage()), Snackbar.LENGTH_LONG).show();
                }
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

    public void setLabels(){

        TextView startDate = findViewById(R.id.assessmentStartDate);
        TextView endDate = findViewById(R.id.assessmentEndDate);
        TextView type = findViewById(R.id.assessmentType);

        startDate.setText("Assessment Start: " + currentAssessment.getStartDate());
        endDate.setText("Assessment End: " + currentAssessment.getEndDate());
        type.setText("Assessment Type: " + currentAssessment.getAssessmentType());

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
        PendingIntent sender = PendingIntent.getBroadcast(context, MainActivity.numAlert++, intent ,PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,trigger,sender);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }

}

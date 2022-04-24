package com.c196.abm2_mobileapplication.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.c196.abm2_mobileapplication.R;
import com.c196.abm2_mobileapplication.database.Repository;
import com.c196.abm2_mobileapplication.model.Course;
import com.c196.abm2_mobileapplication.model.Term;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TermDetail extends AppCompatActivity {
    //Contains information on Term Selected
    //Contains list of Course

    //FAB items
    boolean isShowing = false;
    FloatingActionButton addCourse;
    //DialogBox items
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog alertDialog;
    EditText editTitle;
    EditText editStartDate;
    EditText editEndDate;
    Spinner editStatus;
    EditText editName;
    EditText editPhone;
    EditText editEmail;

    EditText tempText;
    Button saveButton;
    Button cancelButton;

    //for saving course to repo
    Repository repo;
    int courseID;
    String courseTitle;
    String startDate;
    String endDate;
    String status;
    String instructorName;
    String instructorPhone;
    String instructorEmail;

    Term currentTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_screen);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        repo = new Repository(getApplication());
        List<Term> terms = repo.getAllTerms();
        for (Term term : terms) {
            int i = getIntent().getIntExtra("id", -1);
            if (term.getTermID() == i && term.getTermID() != -1) {
                setTitle(term.getTermTitle());
                currentTerm = term;
                break;
            } else {
                setTitle("A Title");
            }
        }
        ArrayList<Course> associatedCourses = new ArrayList<>();
        List<Course> courses = repo.getAllCourse();
        for (Course course: courses) {
            //int i = getIntent().getIntExtra("id", -1);
            if (course.getTermID() == currentTerm.getTermID()) {
                associatedCourses.add(course);
            }
        }

        CourseAdapter adapter = new CourseAdapter(getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter.setCourses(associatedCourses);

        addCourse = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isShowing) {
                    addCourse.hide();
                    newCourseDialog();
                    isShowing = true;
                } else {
                    isShowing = false;
                }
            }
        });

        final Course[] deletedCourse = {null};
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                if (direction == ItemTouchHelper.LEFT) {
                    Course courseSelected;
                    courseSelected = adapter.getCoursePosition(viewHolder.getAdapterPosition());
                    deletedCourse[0] = courseSelected;
                    courses.remove(courseSelected);
                    associatedCourses.remove(courseSelected);
                    repo.deleteCourse(courseSelected);
                    Snackbar.make(recyclerView, "Course Deleted", Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    courses.add(deletedCourse[0]);
                                    associatedCourses.add(deletedCourse[0]);
                                    repo.insertCourse(deletedCourse[0]);
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
        View newCoursePopup = getLayoutInflater().inflate(R.layout.popup_new_course, null);
        editTitle = (EditText) newCoursePopup.findViewById(R.id.courseTitleText);
        editStartDate = (EditText) newCoursePopup.findViewById(R.id.courseStartText);
        editEndDate = (EditText) newCoursePopup.findViewById(R.id.courseEndText);
        editStatus = (Spinner) newCoursePopup.findViewById(R.id.statusSpinner);
        editName = (EditText) newCoursePopup.findViewById(R.id.instructorName);
        editPhone = (EditText) newCoursePopup.findViewById(R.id.instructorPhone);
        editEmail = (EditText) newCoursePopup.findViewById(R.id.instructorEmail);


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
                new DatePickerDialog(TermDetail.this, date,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                editStartDate = tempText;
            }
        });
        editEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempText = editEndDate;
                new DatePickerDialog(TermDetail.this, date,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                editEndDate = tempText;
            }
        });

        saveButton = (Button) newCoursePopup.findViewById(R.id.saveButton);
        cancelButton = (Button) newCoursePopup.findViewById(R.id.cancelButton);

        dialogBuilder.setView(newCoursePopup);
        alertDialog = dialogBuilder.create();
        alertDialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Save Assessment
                courseID = 0;
                courseTitle = editTitle.getText().toString();
                startDate = editStartDate.getText().toString();
                endDate = editEndDate.getText().toString();
                status = editStatus.getAdapter().toString();
                instructorName = editName.getText().toString();
                instructorPhone = editPhone.getText().toString();
                instructorEmail = editEmail.getText().toString();
                int termID = currentTerm.getTermID();

                Course course;
                course = new Course(courseID, courseTitle, startDate, endDate, status, instructorName, instructorPhone, instructorEmail, termID);
                repo.insertCourse(course);
                System.out.println(currentTerm.getTermTitle() + "added: " + course.getCourseTitle());
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


}

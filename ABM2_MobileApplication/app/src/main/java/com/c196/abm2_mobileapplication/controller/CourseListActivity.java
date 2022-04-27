package com.c196.abm2_mobileapplication.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.c196.abm2_mobileapplication.R;
import com.c196.abm2_mobileapplication.database.Repository;
import com.c196.abm2_mobileapplication.model.Course;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class CourseListActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_screen);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setVisibility(findViewById(R.id.floatingActionButton).GONE);
        repo = new Repository(getApplication());
        List<Course> courses = repo.getAllCourse();
        CourseAdapter adapter = new CourseAdapter(getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter.setCourses(courses);

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
                    repo.deleteCourse(courseSelected);
                    Snackbar.make(recyclerView, "Course Deleted", Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    courses.add(deletedCourse[0]);
                                    repo.insertCourse(deletedCourse[0]);
                                    recreate();
                                }
                            }).show();
                }
            }

        }).attachToRecyclerView(recyclerView);
    }

}


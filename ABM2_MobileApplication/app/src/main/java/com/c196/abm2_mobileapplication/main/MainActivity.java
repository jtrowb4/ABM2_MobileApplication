package com.c196.abm2_mobileapplication.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.c196.abm2_mobileapplication.R;
import com.c196.abm2_mobileapplication.controller.HomeScreenActivity;
import com.c196.abm2_mobileapplication.database.Repository;
import com.c196.abm2_mobileapplication.model.Course;
import com.c196.abm2_mobileapplication.model.Term;

import java.time.LocalDateTime;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void enterApplication(View view) {
        Intent homeScreenIntent = new Intent(MainActivity.this, HomeScreenActivity.class);
        startActivity(homeScreenIntent);
        /*Testing code
        Repository repo = new Repository(getApplication());
        Term term = new Term(0,"Spring", "01-01-2022", "06-30-2022");
        repo.insertTerm(term);
        term = new Term(0, "Fall", "07-01-2022", "12-31-2022");
        repo.insertTerm(term);
        Course course = new Course(0,"Mobile Dev", "04-01-2022", "04-22-2022", "In Progress", "Carolyn Sher", "123-456-7890", "@wgu.edu");
        repo.insertCourse(course);
        course = new Course(0,"Data Management", "04-25-2022", "05-25-2022", "Not Started", "Unknown", "123-456-7890", "@wgu.edu");
        repo.insertCourse(course);
        delete later */
    }

    public void exitApplication(View view){
        finish();
        System.exit(0);
    }
}
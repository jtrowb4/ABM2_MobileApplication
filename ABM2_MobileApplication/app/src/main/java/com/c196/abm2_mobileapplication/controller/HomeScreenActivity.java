package com.c196.abm2_mobileapplication.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.c196.abm2_mobileapplication.R;

public class HomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
    }

    public void toTermScreen(View view) {
        Intent termScreenIntent = new Intent(HomeScreenActivity.this, TermScreenActivity.class);
        startActivity(termScreenIntent);
    }

    public void toCourseScreen(View view) {
        Intent termScreenIntent = new Intent(HomeScreenActivity.this, CourseScreenActivity.class);
        startActivity(termScreenIntent);
    }
}
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

    public static int numAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void enterApplication(View view) {
        Intent homeScreenIntent = new Intent(MainActivity.this, HomeScreenActivity.class);
        startActivity(homeScreenIntent);
    }

    public void exitApplication(View view){
        finish();
        System.exit(0);
    }
}
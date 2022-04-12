package com.c196.abm2_mobileapplication.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.c196.abm2_mobileapplication.R;
import com.c196.abm2_mobileapplication.controller.HomeScreenActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void enterApplication(View view) {
        Intent homeScreenIntent = new Intent(MainActivity.this, HomeScreenActivity.class);
        startActivity(homeScreenIntent);
    }
}
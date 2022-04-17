package com.c196.abm2_mobileapplication.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.c196.abm2_mobileapplication.R;
import com.c196.abm2_mobileapplication.database.Repository;
import com.c196.abm2_mobileapplication.model.Term;

public class TermDetail extends AppCompatActivity {

    Repository repository;
    //EditText editTermID;
    EditText editTitle;
    EditText editStartDate;
    EditText editEndDate;

    int termID;
    String title;
    String startDate;
    String endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);

        repository = new Repository(getApplication());

        editTitle = findViewById(R.id.termTitleText);
        editStartDate = findViewById(R.id.termStartText);
        editEndDate = findViewById(R.id.termEndText);

        termID = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        startDate = getIntent().getStringExtra("start date");
        endDate = getIntent().getStringExtra("end date");

        editTitle.setText(title);
        editStartDate.setText(startDate);
        editEndDate.setText(endDate);
    }

    public void onSave(View view){
        Term term;
        if(termID == -1){
            int newID = 0;
            term = new Term(newID, editTitle.getText().toString(), editStartDate.getText().toString(), editEndDate.getText().toString());
            repository.insertTerm(term);
        }
        else{
            term = new Term (termID, editTitle.getText().toString(), editStartDate.getText().toString(), editEndDate.getText().toString());
            repository.updateTerm(term);
        }

    }
}
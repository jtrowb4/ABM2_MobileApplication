package com.c196.abm2_mobileapplication.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.c196.abm2_mobileapplication.R;
import com.c196.abm2_mobileapplication.database.Repository;
import com.c196.abm2_mobileapplication.model.Course;
import com.c196.abm2_mobileapplication.model.Term;

import java.util.List;

public class TermDetail extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_screen);
        Repository repository = new Repository(getApplication());
        List<Term> terms = repository.getAllTerms();
        for (Term term: terms)
        {
            int i = getIntent().getIntExtra("id", -1);
            if (term.getTermID() == i && term.getTermID() != -1){
                setTitle(term.getTermTitle());
                break;
            }
            else
            {
                setTitle("A Title");
            }
        }
    }

   /* public void onSave(View view){
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
     }*/

}

package com.c196.abm2_mobileapplication.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.c196.abm2_mobileapplication.R;
import com.c196.abm2_mobileapplication.database.Repository;
import com.c196.abm2_mobileapplication.model.Assessment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class AssessmentListActivity extends AppCompatActivity {
    //Contains list of Assessments


    //for saving course to repo
    Repository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_screen);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setVisibility(findViewById(R.id.floatingActionButton).GONE);
        repo = new Repository(getApplication());
        List<Assessment> assessments = repo.getAllAssessments();
        AssessmentAdapter adapter = new AssessmentAdapter(getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter.setAssessments(assessments);

        final Assessment[] deletedAssessment = {null};
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                if (direction == ItemTouchHelper.LEFT) {
                    Assessment assessmentSelected;
                    assessmentSelected = adapter.getAssessmentPosition(viewHolder.getAdapterPosition());
                    deletedAssessment[0] = assessmentSelected;
                    assessments.remove(assessmentSelected);
                    repo.deleteAssessment(assessmentSelected);
                    Snackbar.make(recyclerView, "Course Deleted", Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    assessments.add(deletedAssessment[0]);
                                    repo.insertAssessment(deletedAssessment[0]);
                                    recreate();
                                }
                            }).show();
                }
            }

        }).attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }

}


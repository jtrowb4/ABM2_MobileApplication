package com.c196.abm2_mobileapplication.controller;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.c196.abm2_mobileapplication.model.Course;
import com.c196.abm2_mobileapplication.model.CourseNotes;
import com.c196.abm2_mobileapplication.model.Term;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class NotesListActivity extends AppCompatActivity {
    //Contains list of Notes

    //for saving course to repo
    Repository repo;
    private Course currentCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_screen);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setVisibility(findViewById(R.id.floatingActionButton).GONE);
        repo = new Repository(getApplication());
        currentCourse = CourseDetail.currentCourse;
        List<CourseNotes> courseNotes = repo.getAllNotes();
        ArrayList<CourseNotes> associatedNotes = new ArrayList<>();
        for(CourseNotes note : courseNotes){
            if (currentCourse.getCourseID() == note.getCourseID()){
                associatedNotes.add(note);
            }
        }

        NoteAdapter adapter = new NoteAdapter(getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter.setNotes(associatedNotes);

        final CourseNotes[] deletedNote = {null};
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                if (direction == ItemTouchHelper.LEFT) {
                    CourseNotes noteSelected;
                    noteSelected = adapter.getNotePosition(viewHolder.getAdapterPosition());
                    deletedNote[0] = noteSelected;
                    courseNotes.remove(noteSelected);
                    repo.deleteNote(noteSelected);
                    Snackbar.make(recyclerView, "Note Deleted", Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    courseNotes.add(deletedNote[0]);
                                    repo.insertNote(deletedNote[0]);
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

package com.c196.abm2_mobileapplication.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.c196.abm2_mobileapplication.R;
import com.c196.abm2_mobileapplication.database.Repository;
import com.c196.abm2_mobileapplication.model.CourseNotes;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Objects;


public class NoteDetail extends AppCompatActivity{

    private CourseNotes currentNote;
    TextView textTitle;
    TextView textBody;

    Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        textTitle = findViewById(R.id.noteTitle);
        textBody = findViewById(R.id.noteBody);
        repository = new Repository(getApplication());
        List<CourseNotes> notes = repository.getAllNotes();
        for (CourseNotes note : notes) {
            int i = getIntent().getIntExtra("key", -1);
            if (note.getNoteKey() == i) {
                currentNote = note;
                setTitle("Note Detail");
                textTitle.setText(note.getNoteTitle());
                textBody.setText(note.getNoteBody());
                break;
            } else {
                setTitle("A Title");
            }
        }

    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.appbar_menu_detail, menu);
        MenuItem item = menu.findItem(R.id.noted);
        if (item != null){
            item.setVisible(false);
        }

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch(menuItem.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, textBody.getText());
                sendIntent.putExtra(Intent.EXTRA_TITLE, textTitle.getText());
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                return true;
            case R.id.action_edit:
                editNote();
                return true;
            case R.id.action_delete:
                deleteNote();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
    public void deleteNote(){

        repository.deleteNote(currentNote);
        this.finish();
    }

    public void editNote(){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View editNotePopup = getLayoutInflater().inflate(R.layout.popup_new_note, null);
        EditText editTitle = (EditText) editNotePopup.findViewById(R.id.noteTitleText);
        EditText editBody = (EditText) editNotePopup.findViewById(R.id.noteBodyText);

        editTitle.setText(currentNote.getNoteTitle());
        editBody.setText(currentNote.getNoteBody());

        Button saveButton = (Button) editNotePopup.findViewById(R.id.saveButton);
        Button cancelButton = (Button) editNotePopup.findViewById(R.id.cancelButton);

        dialogBuilder.setView(editNotePopup);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Save Note
                try {
                    //check for empty values
                    if ((editTitle.getText().toString().equals("")) || (editBody.getText().toString()).equals(""))
                    {
                        throw new Exception("All Fields and Selections are Required");
                    }
                int noteID = currentNote.getNoteKey();
                String noteTitle = editTitle.getText().toString();
                String noteBody = editBody.getText().toString();
                int courseID = currentNote.getCourseID();

                CourseNotes note;
                note = new CourseNotes(noteID, noteTitle, noteBody, courseID);
                repository.updateNote(note);
                recreate();
                alertDialog.dismiss();
            }
            catch (Exception e) {
                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                Snackbar.make(recyclerView, Objects.requireNonNull(e.getMessage()), Snackbar.LENGTH_LONG).show();
            }
        }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }

}

package com.c196.abm2_mobileapplication.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.c196.abm2_mobileapplication.R;
import com.c196.abm2_mobileapplication.database.Repository;
import com.c196.abm2_mobileapplication.model.Term;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TermScreenActivity extends AppCompatActivity {

    //FAB items
    boolean isShowing = false;
    FloatingActionButton addTerm;
    //DialogBox items
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog alertDialog;
    EditText editTitle;
    EditText editStartDate;
    EditText editEndDate;
    EditText tempText;
    Button saveButton;
    Button cancelButton;

    //for saving assessment to repo
    Repository repo;
    int termID;
    String termTitle;
    String startDate;
    String endDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_screen);
        RecyclerView recyclerView = findViewById(R.id.termRecyclerView);
        repo = new Repository(getApplication());
        List<Term> terms = repo.getAllTerms();
        TermAdapter adapter = new TermAdapter(getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter.setTerms(terms);

        addTerm = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        addTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isShowing) {
                    addTerm.hide();
                    newTermDialog();
                    isShowing = true;
                }
                else{
                    isShowing = false;
                }
            }
        });

        final Term[] deletedTerm = {null};
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                switch (direction){
                    case ItemTouchHelper.LEFT:
                        Term termSelected;
                        termSelected = adapter.getTermPostion(viewHolder.getAdapterPosition());
                        deletedTerm[0] = termSelected;
                        terms.remove(termSelected);
                        repo.deleteTerm(termSelected);
                        Snackbar.make(recyclerView, "Term Deleted", Snackbar.LENGTH_LONG)
                                .setAction("Undo", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        terms.add(deletedTerm[0]);
                                        repo.insertTerm(deletedTerm[0]);
                                        recreate();
                                    }
                                }).show();
                        break;
                    case ItemTouchHelper.RIGHT:
                        //Modify
                }
            }

        }).attachToRecyclerView(recyclerView);
    }

    public void newTermDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        View newTermPopup = getLayoutInflater().inflate(R.layout.popup_new_term, null);
        editTitle = (EditText) newTermPopup.findViewById(R.id.termTitleText);
        editStartDate = (EditText) newTermPopup.findViewById(R.id.termStartText);
        editEndDate = (EditText) newTermPopup.findViewById(R.id.termEndText);

        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel(tempText);
            }

            private void updateLabel(EditText et){
                String dateFormat = "MM-dd-yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
                et.setText(simpleDateFormat.format(myCalendar.getTime()));
            }
        };
        editStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempText = editStartDate;
                new DatePickerDialog(TermScreenActivity.this, date,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                editStartDate = tempText;
            }
        });
        editEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempText = editEndDate;
                new DatePickerDialog(TermScreenActivity.this, date,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                editEndDate = tempText;
            }
        });

        saveButton = (Button) newTermPopup.findViewById(R.id.saveButton);
        cancelButton = (Button) newTermPopup.findViewById(R.id.cancelButton);

        dialogBuilder.setView(newTermPopup);
        alertDialog = dialogBuilder.create();
        alertDialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Save Assessment
                termID = 0;
                termTitle = editTitle.getText().toString();
                startDate = editStartDate.getText().toString();
                endDate = editEndDate.getText().toString();
                Term term;
                term = new Term(termID, termTitle, startDate, endDate);
                repo.insertTerm(term);
                recreate();
                alertDialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate((R.menu.appbar_menu_term), menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
   }

    public void toTermEditor(View view) {
        Intent editorScreenIntent = new Intent(TermScreenActivity.this, TermDetail.class);
        startActivity(editorScreenIntent);
    }

}

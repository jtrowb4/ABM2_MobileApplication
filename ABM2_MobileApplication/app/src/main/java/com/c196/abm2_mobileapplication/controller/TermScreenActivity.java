package com.c196.abm2_mobileapplication.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.c196.abm2_mobileapplication.R;
import com.c196.abm2_mobileapplication.database.Repository;
import com.c196.abm2_mobileapplication.model.Term;

import java.util.List;

public class TermScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_screen);
        RecyclerView recyclerView = findViewById(R.id.termRecyclerView);
        Repository repo = new Repository(getApplication());
        List<Term> terms = repo.getAllTerms();
        TermAdapter adapter = new TermAdapter(getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter.setTerms(terms);
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

   /* public void goToCourses(View view){
        Intent courseScreenIntent = new Intent(TermScreenActivity.this, CourseScreenActivity.class);
        startActivity(courseScreenIntent);
    }
    */
}

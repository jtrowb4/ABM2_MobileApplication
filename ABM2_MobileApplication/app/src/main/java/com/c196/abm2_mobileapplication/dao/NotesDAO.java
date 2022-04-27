package com.c196.abm2_mobileapplication.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import androidx.room.Query;
import androidx.room.Update;

import com.c196.abm2_mobileapplication.model.CourseNotes;

import java.util.List;


@Dao
public interface NotesDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertNote(CourseNotes notes);

    @Update
    void updateNote(CourseNotes notes);

    @Delete
    void deleteNote(CourseNotes notes);

    @Query("SELECT * FROM notes ORDER BY noteKey ASC")
    List<CourseNotes> getAllNotes();

}
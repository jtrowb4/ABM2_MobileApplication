package com.c196.abm2_mobileapplication.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class CourseNotes {
    @PrimaryKey(autoGenerate = true)
    private int noteKey;
    private String noteTitle;
    private String noteBody;
    private int courseID;

    public CourseNotes(int noteKey, String noteTitle, String noteBody, int courseID) {
        this.noteKey = noteKey;
        this.noteTitle = noteTitle;
        this.noteBody = noteBody;
        this.courseID = courseID;
    }

    public int getNoteKey() {
        return noteKey;
    }

    public void setNoteKey(int noteKey) {
        this.noteKey = noteKey;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteBody() {
        return noteBody;
    }

    public void setNoteBody(String noteBody) {
        this.noteBody = noteBody;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }
}

package com.c196.abm2_mobileapplication.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.c196.abm2_mobileapplication.dao.AssessmentDAO;
import com.c196.abm2_mobileapplication.dao.CourseDAO;
import com.c196.abm2_mobileapplication.dao.TermDAO;

import com.c196.abm2_mobileapplication.model.Assessment;
import com.c196.abm2_mobileapplication.model.Course;
import com.c196.abm2_mobileapplication.model.Term;

//if you change any of the class you must increment the version number
@Database(entities={Term.class, Course.class, Assessment.class}, version = 1, exportSchema = false)
public abstract class DatabaseBuilder extends RoomDatabase {

    public abstract TermDAO termDAO();
    public abstract CourseDAO courseDAO();
    public abstract AssessmentDAO assessmentDAO();

    private static volatile DatabaseBuilder INSTANCE;

    static DatabaseBuilder getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (DatabaseBuilder.class) {
                if(INSTANCE == null)
                {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DatabaseBuilder.class, "c196DataBase.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

package com.c196.abm2_mobileapplication.database;

import android.app.Application;
import android.text.style.ReplacementSpan;

import com.c196.abm2_mobileapplication.dao.AssessmentDAO;
import com.c196.abm2_mobileapplication.dao.CourseDAO;
import com.c196.abm2_mobileapplication.dao.NotesDAO;
import com.c196.abm2_mobileapplication.dao.TermDAO;
import com.c196.abm2_mobileapplication.model.Assessment;
import com.c196.abm2_mobileapplication.model.Course;
import com.c196.abm2_mobileapplication.model.CourseNotes;
import com.c196.abm2_mobileapplication.model.Term;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private TermDAO mTermDAO;
    private CourseDAO mCourseDAO;
    private NotesDAO mNotesDAO;
    private AssessmentDAO mAssessmentDAO;
    private List<Term> mAllTerms;
    private List<Course> mAllCourses;
    private List<CourseNotes> mAllNotes;
    private List<Assessment> mAllAssessments;

    private static int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application){
        DatabaseBuilder databaseBuilder = DatabaseBuilder.getInstance(application);
        mTermDAO = databaseBuilder.termDAO();
        mCourseDAO = databaseBuilder.courseDAO();
        mNotesDAO = databaseBuilder.notesDAO();
        mAssessmentDAO = databaseBuilder.assessmentDAO();
    }

//Terms
    public void insertTerm(Term term){
        databaseExecutor.execute(()->{
            mTermDAO.insertTerm(term);
        });
        try{
            Thread.sleep(1000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    public void updateTerm(Term term){
        databaseExecutor.execute(()->{
            mTermDAO.updateTerm(term);
        });
        try{
            Thread.sleep(1000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    public void deleteTerm(Term term){
        databaseExecutor.execute(()->{
            mTermDAO.deleteTerm(term);
        });
        try{
            Thread.sleep(1000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    public List<Term> getAllTerms(){
        databaseExecutor.execute(()->{
            mAllTerms = mTermDAO.getAllTerms();
        });
        try{
            Thread.sleep(1000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        return mAllTerms;
    }

//Courses
    public void insertCourse(Course course){
        databaseExecutor.execute(()->{
            mCourseDAO.insertCourse(course);
        });
        try{
            Thread.sleep(1000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    public void updateCourse(Course course){
        databaseExecutor.execute(()->{
            mCourseDAO.updateCourse(course);
        });
        try{
            Thread.sleep(1000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    public void deleteCourse(Course course){
        databaseExecutor.execute(()->{
            mCourseDAO.deleteCourse(course);
        });
        try{
            Thread.sleep(1000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    public List<Course> getAllCourse(){
        databaseExecutor.execute(()->{
            mAllCourses = mCourseDAO.getAllCourses();
        });
        try{
            Thread.sleep(1000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        return mAllCourses;
    }

//CourseNotes
public void insertNote(CourseNotes notes){
    databaseExecutor.execute(()->{
        mNotesDAO.insertNote(notes);
    });
    try{
        Thread.sleep(1000);
    }
    catch (InterruptedException e){
        e.printStackTrace();
    }
}
public void updateNote(CourseNotes courseNotes){
    databaseExecutor.execute(()->{
        mNotesDAO.updateNote(courseNotes);
    });
    try{
        Thread.sleep(1000);
    }
    catch (InterruptedException e){
        e.printStackTrace();
    }
}
public void deleteNote(CourseNotes courseNotes){
    databaseExecutor.execute(()->{
        mNotesDAO.deleteNote(courseNotes);
    });
    try{
        Thread.sleep(1000);
    }
    catch (InterruptedException e){
        e.printStackTrace();
    }
}
public List<CourseNotes> getAllNotes(){
    databaseExecutor.execute(()->{
        mAllNotes = mNotesDAO.getAllNotes();
    });
    try{
        Thread.sleep(1000);
    }
    catch (InterruptedException e){
        e.printStackTrace();
    }
    return mAllNotes;
}

//Assessments
    public void insertAssessment(Assessment assessment){
        databaseExecutor.execute(()->{
            mAssessmentDAO.insertAssessment(assessment);
        });
        try{
            Thread.sleep(1000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    public void updateAssessment(Assessment assessment){
        databaseExecutor.execute(()->{
            mAssessmentDAO.updateAssessment(assessment);
        });
        try{
            Thread.sleep(1000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    public void deleteAssessment(Assessment assessment){
        databaseExecutor.execute(()-> {
            mAssessmentDAO.deleteAssessment(assessment);
        });
        try{
            Thread.sleep(1000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public List<Assessment> getAllAssessments(){
        databaseExecutor.execute(()->{
            mAllAssessments = mAssessmentDAO.getAllAssessments();
        });
        try{
            Thread.sleep(1000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        return mAllAssessments;
    }

}

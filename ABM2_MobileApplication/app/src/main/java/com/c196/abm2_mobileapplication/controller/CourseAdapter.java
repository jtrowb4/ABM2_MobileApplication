package com.c196.abm2_mobileapplication.controller;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.c196.abm2_mobileapplication.R;
import com.c196.abm2_mobileapplication.model.Course;

import java.util.List;

public class CourseAdapter  extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    class CourseViewHolder extends RecyclerView.ViewHolder {
        private final TextView courseItemView;

        private CourseViewHolder(View view) {
            super(view);
            courseItemView = view.findViewById(R.id.displayItemText);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Course currentCourse = mCourses.get(position);
                    Intent intent = new Intent(context, CourseDetail.class);
                    intent.putExtra("course id", currentCourse.getCourseID());
                    intent.putExtra("title", currentCourse.getCourseTitle());
                    intent.putExtra("start date", currentCourse.getStartDate());
                    intent.putExtra("end date", currentCourse.getEndDate());
                    intent.putExtra("status", currentCourse.getStatus());
                    intent.putExtra("instructor name", currentCourse.getInstructorName());
                    intent.putExtra("phone", currentCourse.getInstructorPhone());
                    intent.putExtra("email", currentCourse.getInstructorEmail());
                    intent.putExtra("term id", currentCourse.getTermID());

                    context.startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });
        }
    }
    private List<Course> mCourses;
    private final Context context;
    private final LayoutInflater mInflator;

    public CourseAdapter(Context context) {
        mInflator = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public CourseAdapter.CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflator.inflate(R.layout.list_item_display, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.CourseViewHolder holder, int position) {
        if(mCourses != null){
            Course currentCourse = mCourses.get(position);
            String courseTitle = currentCourse.getCourseTitle();
            holder.courseItemView.setText(courseTitle);
        }
        else{
            holder.courseItemView.setText("No Course Title");
        }
    }

    public void setCourses(List<Course> courses){
        mCourses = courses;
        notifyDataSetChanged();
    }

    public Course getCoursePosition(int position){
        return mCourses.get(position);
    }

    @Override
    public int getItemCount() {
        if(mCourses != null)
        {
            return mCourses.size();
        }
        else
        {
            return 0;
        }
    }
}
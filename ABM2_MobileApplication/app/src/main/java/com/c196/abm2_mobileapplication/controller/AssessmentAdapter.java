package com.c196.abm2_mobileapplication.controller;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.c196.abm2_mobileapplication.R;
import com.c196.abm2_mobileapplication.model.Assessment;
import com.c196.abm2_mobileapplication.model.Course;

import java.util.List;

public class AssessmentAdapter  extends RecyclerView.Adapter<AssessmentAdapter.AssessmentViewHolder> {

    class AssessmentViewHolder extends RecyclerView.ViewHolder {
        private final TextView assessmentItemView;

        private AssessmentViewHolder(View view) {
            super(view);
            assessmentItemView = view.findViewById(R.id.displayItemText);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Assessment currentAssessment = mAssessments.get(position);
                    Intent intent = new Intent(context, AssessmentDetail.class);
                    intent.putExtra("id", currentAssessment.getAssessmentID());
                    intent.putExtra("title", currentAssessment.getAssessmentTitle());
                    intent.putExtra("type", currentAssessment.getAssessmentType());
                    intent.putExtra("start date", currentAssessment.getStartDate());
                    intent.putExtra("end date", currentAssessment.getEndDate());
                    context.startActivity(intent);
                }
            });
        }
    }
    private List<Assessment> mAssessments;
    private final Context context;
    private final LayoutInflater mInflator;

    public AssessmentAdapter(Context context) {
        mInflator = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public AssessmentAdapter.AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflator.inflate(R.layout.list_item_display, parent, false);
        return new AssessmentAdapter.AssessmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentAdapter.AssessmentViewHolder holder, int position) {
        if(mAssessments != null){
            Assessment currentAssessment = mAssessments.get(position);
            String assessmentTitle = currentAssessment.getAssessmentTitle();
            holder.assessmentItemView.setText(assessmentTitle);
        }
        else{
            holder.assessmentItemView.setText("No Assessment Title");
        }
    }

    public void setAssessments(List<Assessment> assessments){
        mAssessments = assessments;
        notifyDataSetChanged();
    }

    public Assessment getAssessmentPosition (int postion){
        return mAssessments.get(postion);
    }

    @Override
    public int getItemCount() {
        if(mAssessments != null)
        {
            return mAssessments.size();
        }
        else
        {
            return 0;
        }
    }
}
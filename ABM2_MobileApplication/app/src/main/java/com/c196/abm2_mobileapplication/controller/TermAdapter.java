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
import com.c196.abm2_mobileapplication.model.Term;

import java.util.List;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermViewHolder> {

    class TermViewHolder extends RecyclerView.ViewHolder{
        private final TextView termItemView;

        private TermViewHolder(View view){
            super(view);
            termItemView = view.findViewById(R.id.displayItemText);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Term currentTerm = mTerms.get(position);
                    Intent intent = new Intent(context,TermDetail.class); // load on modify
                    intent.putExtra("id", currentTerm.getTermID());
                    intent.putExtra("title", currentTerm.getTermTitle());
                    intent.putExtra("start date", currentTerm.getStartDate());
                    intent.putExtra("end date", currentTerm.getEndDate());
                    context.startActivity(intent);
                }
            });
        }
    }

    private List<Term> mTerms;
    private final Context context;
    private final LayoutInflater mInflator;

    public TermAdapter(Context context){
        mInflator = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public TermAdapter.TermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflator.inflate(R.layout.list_item_display, parent, false);
        return new TermViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TermAdapter.TermViewHolder holder, int position) {
        if(mTerms != null){
            Term currentTerm = mTerms.get(position);
            String termTitle = currentTerm.getTermTitle();
            holder.termItemView.setText(termTitle);
        }
        else{
            holder.termItemView.setText("No Term Title");
        }
    }

    public void setTerms(List<Term> terms){
        mTerms = terms;
        notifyDataSetChanged();
    }

    public Term getTermPosition(int position){
        return mTerms.get(position);
    }

    @Override
    public int getItemCount() {
        if(mTerms != null)
        {
            return mTerms.size();
        }
        else
        {
            return 0;
        }
    }
}
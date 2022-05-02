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
import com.c196.abm2_mobileapplication.model.CourseNotes;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    class NoteViewHolder extends RecyclerView.ViewHolder {
        private final TextView noteItemView;

        private NoteViewHolder(View view) {
            super(view);
            noteItemView = view.findViewById(R.id.displayItemText);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final CourseNotes currentNote = mNotes.get(position);
                    Intent intent = new Intent(context, NoteDetail.class);
                    intent.putExtra("key", currentNote.getNoteKey());
                    intent.putExtra("subject", currentNote.getNoteTitle());
                    intent.putExtra("body", currentNote.getNoteBody());
                    intent.putExtra("course id", currentNote.getCourseID());
                    context.startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });
        }
    }
    private List<CourseNotes> mNotes;
    private final Context context;
    private final LayoutInflater mInflator;

    public NoteAdapter(Context context) {
        mInflator = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public NoteAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflator.inflate(R.layout.list_item_display, parent, false);
        return new NoteAdapter.NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.NoteViewHolder holder, int position) {
        if(mNotes != null){
            CourseNotes currentNote = mNotes.get(position);
            String noteTitle = currentNote.getNoteTitle();
            holder.noteItemView.setText(noteTitle);
        }
        else{
            holder.noteItemView.setText("No Note");
        }
    }

    public void setNotes(List<CourseNotes> notes){
        mNotes = notes;
        notifyDataSetChanged();
    }

    public CourseNotes getNotePosition (int position){
        return mNotes.get(position);
    }

    @Override
    public int getItemCount() {
        if(mNotes != null)
        {
            return mNotes.size();
        }
        else
        {
            return 0;
        }
    }
}

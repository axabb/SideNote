package com.example.sidenote;

import static com.example.sidenote.AllNotes.UPDATE_REQUEST_CODE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewsHolder>{

    private Activity activity;
    private List<NotesModel> notesModelsList;



    public NotesAdapter(Activity activity) {
        this.activity = activity;
        notesModelsList = new ArrayList<>();
    }


    public void add(NotesModel notesModel){
        notesModelsList.add(notesModel);
        notifyDataSetChanged();
    }

    public void clear(){
        notesModelsList.clear();
        notifyDataSetChanged();
    }

    public void filterList(List<NotesModel> newList){
        notesModelsList = newList;
        notifyDataSetChanged();
    }
    public List<NotesModel> getList(){
        return notesModelsList;
    }


    @NonNull
    @Override
    public ViewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_tow,parent,false);
        return new  ViewsHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewsHolder holder, int position) {
        NotesModel notesModel = notesModelsList.get(position);
        holder.c_title.setText(notesModel.getTitle());
        holder.c_desc.setText(notesModel.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updateIntent = new Intent(activity, UpdateActivity.class);
                updateIntent.putExtra("id", notesModel.getId());
                updateIntent.putExtra("title", notesModel.getTitle());
                updateIntent.putExtra("description", notesModel.getDescription());
                activity.startActivityForResult(updateIntent, UPDATE_REQUEST_CODE);

            }
        });

    }

    @Override
    public int getItemCount() {
        return notesModelsList.size();
    }

    public class ViewsHolder extends RecyclerView.ViewHolder{
         TextView c_title, c_desc;
        public ViewsHolder(@NonNull View itemView) {
            super(itemView);
            c_title = itemView.findViewById(R.id.card_title);
            c_desc = itemView.findViewById(R.id.card_description);


        }
    }
}

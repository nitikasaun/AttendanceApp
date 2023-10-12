package com.example.attendanceapp;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {
ArrayList<ClassItem> classItems;
Context context;

private OnItemClickListener onItemClickListener;
public interface  OnItemClickListener{
    void onClick(int position);
}

public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
}

public ClassAdapter(Context context, ArrayList<ClassItem> classItems) {
    this.classItems = classItems;
    this.context = context;
}


public static class ClassViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView className;
        TextView subjectName;
        Button deleteButton;
    public ClassViewHolder(@NonNull View itemView,OnItemClickListener onItemClickListener) {
        super(itemView);
        className = itemView.findViewById(R.id.class_tv);
        subjectName = itemView.findViewById(R.id.subject_tv);
        deleteButton = itemView.findViewById(R.id.deleteButton); // Initialize the delete button
        deleteButton.setOnClickListener(view -> {
        itemView.setOnClickListener(v->onItemClickListener.onClick(getAdapterPosition()));
        itemView.setOnCreateContextMenuListener(this);
    });
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
         contextMenu.add(getAdapterPosition(),0,0,"EDIT");
         contextMenu.add(getAdapterPosition(),1,0,"DELETE");
    }
}

@NonNull
@Override
public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item,parent,false);
    return new ClassViewHolder(itemView,onItemClickListener);
}

@Override
public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
    holder.className.setText(classItems.get(position).getClassName());
    holder.subjectName.setText(classItems.get(position).getSubjectName());

    holder.itemView.setOnClickListener(v -> {
        // Handle click on the entire class item (not delete button)
        if (onItemClickListener != null) {
            onItemClickListener.onClick(position);
        }
    });
    holder.deleteButton.setOnClickListener(v -> {
        int clickedPosition = holder.getAdapterPosition();
        if (clickedPosition != RecyclerView.NO_POSITION) {
            classItems.remove(clickedPosition);
            notifyItemRemoved(clickedPosition);
        }
    });
}

@Override
public int getItemCount() {
    return classItems.size();
}
}

package com.example.attendanceapp;

import android.content.Context;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    ArrayList<StudentItem> studentItems;
    Context context;
    private DbHelper dbHelper;

    int clickedPosition = -1;

    private OnItemClickListener onItemClickListener;
    public interface  OnItemClickListener{
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public StudentAdapter(Context context, ArrayList<StudentItem> studentItems) {
        this.studentItems = studentItems;
        this.context = context;
    }


    public static class StudentViewHolder extends RecyclerView.ViewHolder{
        TextView roll;
        TextView name;
        TextView status;
        CardView cardView;
        Button deleteButton;


        public StudentViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            roll = itemView.findViewById(R.id.roll);
            name = itemView.findViewById(R.id.name);
            status = itemView.findViewById(R.id.status);
            cardView = itemView.findViewById(R.id.cardview);
            deleteButton = itemView.findViewById(R.id.deleteButton); // Initialize the delete button
            itemView.setOnClickListener(v -> onItemClickListener.onClick(getAdapterPosition()));
        }
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item, parent, false);
        itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.add(0, R.id.edit, 0, "Edit");
                contextMenu.add(0, R.id.delete, 0, "Delete");
            }
        });
        return new StudentViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        holder.roll.setText(studentItems.get(position).getRoll() + "");
        holder.name.setText(studentItems.get(position).getName());
        holder.status.setText(studentItems.get(position).getStatus());
        holder.cardView.setCardBackgroundColor(getColor(position));

        // Set an OnClickListener for the delete button
        holder.deleteButton.setOnClickListener(v -> {
            // Implement the delete logic here
            int clickedPosition = holder.getAdapterPosition();
            if (clickedPosition != RecyclerView.NO_POSITION) {
                studentItems.remove(clickedPosition);
                deleteStudent(clickedPosition);
                notifyItemRemoved(clickedPosition);
            }
        });
    }
    private void deleteStudent(int position) {
        dbHelper.deleteStudent(studentItems.get(position).getSid());
        studentItems.remove(position);
    }

    private int getColor(int position) {
        String status = studentItems.get(position).getStatus();
        if(status.equals("P"))
            return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context,R.color.present)));
        else if (status.equals("A"))
            return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context,R.color.absent)));


        return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context,R.color.normal)));
    }

    @Override
    public int getItemCount() {
        return studentItems.size();
    }
    public void onClick(int position) {
        clickedPosition = position;
        // Notify that the item has been clicked.
        if (onItemClickListener != null) {
            onItemClickListener.onClick(position);
        }
    }
    public int getClickedPosition() {
        return clickedPosition;
    }

}

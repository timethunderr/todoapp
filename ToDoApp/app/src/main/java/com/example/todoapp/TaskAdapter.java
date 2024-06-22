package com.example.todoapp;



import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;

public class TaskAdapter extends FirestoreRecyclerAdapter<Task, TaskAdapter.TaskViewHolder>{

    Context context;


    public TaskAdapter(@NonNull FirestoreRecyclerOptions<Task> options, Context context) {
        super(options);
        this.context = context;

    }

    @Override
    protected void onBindViewHolder(@NonNull TaskViewHolder taskViewHolder, @SuppressLint("RecyclerView") int position, @NonNull Task task) {
        taskViewHolder.titleTextView.setText(task.title);
        taskViewHolder.contentTextView.setText(task.content);
        taskViewHolder.timestampTextView.setText(Utility.timestampToString(task.timestamp));

        taskViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TaskDetailsActivity.class);
                intent.putExtra("title", task.title);
                intent.putExtra("content", task.content);
                String docId = getSnapshots().getSnapshot(position).getId();
                intent.putExtra("docId", docId);
                context.startActivity(intent);
            }
        });


    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_task_item, parent, false);
        return new TaskViewHolder(view);
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView, contentTextView, timestampTextView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.task_title_text_view);
            contentTextView = itemView.findViewById(R.id.task_content_text_view);
            timestampTextView = itemView.findViewById(R.id.task_timestamp_text_view);



        }
    }
}

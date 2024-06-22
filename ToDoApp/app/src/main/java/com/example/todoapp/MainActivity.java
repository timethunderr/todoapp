package com.example.todoapp;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    ImageButton addTaskBtn;
    RecyclerView recyclerView;
    ImageButton menuBtn;
    TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addTaskBtn = findViewById(R.id.add_image_btn);
        recyclerView = findViewById(R.id.recycler_view);
        menuBtn = findViewById(R.id.menu_btn);



        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TaskDetailsActivity.class));
            }
        });

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showMenu();
            }
        });

        setupRecyclerView();

    }

    void showMenu(){
        //todo display menu

    }

    void setupRecyclerView() {
        Query query = Utility.getCollectionReferenceForTasks().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Task> options = new FirestoreRecyclerOptions.Builder<Task>()
                .setQuery(query, Task.class).build();
        recyclerView.setLayoutManager((new LinearLayoutManager(MainActivity.this)));
        taskAdapter = new TaskAdapter(options, this);
        recyclerView.setAdapter(taskAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        taskAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        taskAdapter.stopListening();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        taskAdapter.notifyDataSetChanged();
    }



}

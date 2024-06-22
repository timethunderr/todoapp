package com.example.todoapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.Timestamp;

import org.w3c.dom.Text;

public class TaskDetailsActivity extends AppCompatActivity {

    EditText titleEditText, contentEditText;
    ImageButton saveTaskBtn;

    TextView pageTitleTextView;
    String title, content, docId;
    boolean isEditMode = false;

    TextView deleteTaskTextViewBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_task_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        titleEditText = findViewById(R.id.task_title_text);
        contentEditText = findViewById(R.id.task_content_text);
        saveTaskBtn = findViewById(R.id.save_task_btn);
        pageTitleTextView = findViewById(R.id.title_page);
        deleteTaskTextViewBtn = findViewById(R.id.delete_task_text_view_btn);

        //receive data
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        if(docId != null && !docId.isEmpty()){
            isEditMode =true;
        }

        titleEditText.setText(title);
        contentEditText.setText(content);

        if(isEditMode){
            pageTitleTextView.setText("Edit your task");

            deleteTaskTextViewBtn.setVisibility(View.VISIBLE);
        }

        saveTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });

        deleteTaskTextViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTaskFromFirebase();
            }
        });

    }

    void saveTask(){
        String taskTitle = titleEditText.getText().toString();
        String taskContent = contentEditText.getText().toString();

        if(taskTitle == null || taskTitle.isEmpty()){
            titleEditText.setError("Title is required");
            return;
        }

        Task task = new Task();
        task.setTitle(taskTitle);
        task.setContent(taskContent);
        task.setTimestamp(Timestamp.now());

        saveTaskToFirebase(task);

    }

    void saveTaskToFirebase(Task task){



        DocumentReference documentReference;

        if(isEditMode){
            //update the task
            documentReference = Utility.getCollectionReferenceForTasks().document(docId);
        } else {
            //create new task
            documentReference = Utility.getCollectionReferenceForTasks().document();
        }



        documentReference.set(task).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                if(task.isSuccessful()){
                    //task is added
                    Toast.makeText(TaskDetailsActivity.this, "Task added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(TaskDetailsActivity.this, "Failed while adding task", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void deleteTaskFromFirebase(){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForTasks().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(TaskDetailsActivity.this, "Task deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(TaskDetailsActivity.this, "Failed while deleting task", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
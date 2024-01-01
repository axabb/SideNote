package com.example.sidenote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class UpdateActivity extends AppCompatActivity {
    Button save, delete;
    String id, title, description;

    TextView titletxt, contenttxt;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    NotesAdapter notesAdapter;
    RecyclerView notesRecycler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        titletxt = (TextView) findViewById(R.id.note_title);
        contenttxt = (TextView) findViewById(R.id.note_content);
        save = (Button) findViewById(R.id.save_btn);
        delete = (Button) findViewById(R.id.del_btn);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        title = intent.getStringExtra("title");
        description = intent.getStringExtra("description");

        titletxt.setText(title);
        contenttxt.setText(description);

        notesAdapter = new NotesAdapter(this);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNote();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(v.getContext());
                progressDialog.setTitle("Deleting");
                FirebaseFirestore.getInstance()
                        .collection("notes")
                        .document(id)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.dismiss();
                                Toast.makeText(UpdateActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
                                notesAdapter.notifyDataSetChanged();
                                setResult(RESULT_OK);
                                finish();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(UpdateActivity.this, "Error Deleting note", Toast.LENGTH_SHORT).show();

                            }
                        });

            }
        });
    }

    private void updateNote() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating");
        progressDialog.setMessage("your note");
        progressDialog.show();
        NotesModel notesModel = new NotesModel (id, title, description, mAuth.getUid());

        firebaseFirestore.collection("Notes")
                .document(id)
                .set(notesModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UpdateActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                        setResult(RESULT_OK);
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("UpdateActivity", "Error updating note: " + e.getMessage());
                        Toast.makeText(UpdateActivity.this, "Error updating note", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });

    }
}
package com.example.sidenote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class NewNote extends AppCompatActivity {

    EditText title, description;
    Button save, b_to_s;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        title = (EditText) findViewById(R.id.note_title);
        description = (EditText) findViewById(R.id.note_content);
        save = (Button) findViewById(R.id.save_btn);

        b_to_s = (Button) findViewById(R.id.back_btn);

        b_to_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obbjjj = new Intent(NewNote.this, AllNotes.class);
                startActivity(obbjjj);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });
    }

    private void saveNote() {
        FirebaseFirestore  firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Saving");
        progressDialog.setMessage("your note");
        progressDialog.show();

        String noteID = UUID.randomUUID().toString();
        String noteTitle = title.getText().toString();
        String noteDescription = description.getText().toString();

        if (noteTitle.isEmpty() || noteDescription.isEmpty()) {
            Toast.makeText(this, "Title and Description cannot be empty", Toast.LENGTH_SHORT).show();
            progressDialog.cancel();
            return;
        }

        NotesModel notesModel = new NotesModel (noteID, noteTitle, noteDescription, mAuth.getUid());

        firebaseFirestore.collection("Notes")
                .document(noteID)
                .set(notesModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(NewNote.this, "Note Saved", Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NewNote.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();

                    }
                });

    }
}
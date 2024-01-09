package com.example.sidenote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllNotes extends AppCompatActivity {
    Button add;
    FirebaseAuth mAuth;
    MenuInflater inflater;
    NotesAdapter notesAdapter;
    RecyclerView notesRecycler;
    EditText search;
    static final int UPDATE_REQUEST_CODE = 123;
    List<NotesModel> notesModelList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notes);

        Toolbar tbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tbar);

        mAuth = FirebaseAuth.getInstance();

        notesModelList = new ArrayList<>();
        notesAdapter = new NotesAdapter(this);

        notesRecycler = (RecyclerView) findViewById(R.id.notes_recycler);

        notesRecycler.setLayoutManager(new LinearLayoutManager(this));

        if (notesRecycler != null) {
            notesRecycler.setAdapter(notesAdapter);
        }


        add = (Button) findViewById(R.id.add_note);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obj7;
                obj7 = new Intent(AllNotes.this, NewNote.class);
                startActivity(obj7);
            }
        });


        search = (EditText) findViewById(R.id.searchbar);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (text.length()>0){
                    filter(text);
                }else {
                    notesAdapter.clear();
                    notesAdapter.filterList(notesModelList);
                }

            }
        });
        getData();

    }

    private void filter(String text) {
        List<NotesModel> adapterList = notesAdapter.getList();
        List<NotesModel> notesModelsList = new ArrayList<>();
        for (int i=0;i<adapterList.size();i++){
            NotesModel notesModel = adapterList.get(i);
            if (notesModel.getTitle().toLowerCase().contains(text.toLowerCase()) ||
                    notesModel.getDescription().toLowerCase().contains(text.toLowerCase())){
                notesModelsList.add(notesModel);
            }
        }
        notesAdapter.filterList(notesModelsList);

    }

    @Override
    protected void onResume() {
        super.onResume();

        getData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_REQUEST_CODE && resultCode == RESULT_OK) {
            refreshData();
        }
    }

    private void refreshData() {
        notesAdapter.clear();
        getData();
    }


    private void getData(){
        FirebaseFirestore.getInstance()
                .collection("Notes")
                .whereEqualTo("uid", mAuth.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        notesAdapter.clear();
                        List<DocumentSnapshot> dsList = queryDocumentSnapshots.getDocuments();
                        for (int i=0;i<dsList.size(); i++){
                            DocumentSnapshot documentSnapshot = dsList.get(i);
                            NotesModel notesModel = documentSnapshot.toObject(NotesModel.class);
                            notesModelList.add(notesModel);
                            notesAdapter.add(notesModel);
                        }
                        notesAdapter.notifyDataSetChanged();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AllNotes.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("Menu", "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            //redirect the user to Login and signup page after logging out
            Intent obj8 = new Intent(AllNotes.this, HomeActivity.class);
            startActivity(obj8);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




}
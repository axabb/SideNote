package com.example.sidenote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {
    Button btn1, btn2;
    FirebaseAuth mAuth;



    @Override
    public void onStart() {
        super.onStart();
        // Redirect user to All Notes page if the user is already logged in
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent obj5 = new Intent(HomeActivity.this, AllNotes.class);
            startActivity(obj5);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btn1 = (Button) findViewById(R.id.home_to_sign);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obj1 = new Intent(HomeActivity.this, SignUp.class);
                startActivity(obj1);
            }
        });
        btn2 = (Button) findViewById(R.id.home_to_log);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obj2 = new Intent(HomeActivity.this, Login.class);
                startActivity(obj2);
            }
        });

    }
}
package com.example.sidenote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    EditText l_email, l_pass;
    Button l_to_s, login_btn, b_to_s;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        l_email = (EditText) findViewById(R.id.log_email);
        l_pass = (EditText) findViewById(R.id.log_password);
        login_btn = (Button) findViewById(R.id.log_btn);
        l_to_s = (Button) findViewById(R.id.log_to_sign);
        b_to_s = (Button) findViewById(R.id.back_to_start);

        l_to_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objj = new Intent(Login.this, SignUp.class);
                startActivity(objj);
            }
        });

        b_to_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obbjj = new Intent(Login.this, HomeActivity.class);
                startActivity(obbjj);
            }
        });

        mAuth = FirebaseAuth.getInstance();

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = String.valueOf(l_email.getText());
                password = String.valueOf(l_pass.getText());


                if (TextUtils.isEmpty(email))
                {
                    Toast.makeText(Login.this, "Enter valid Email Address", Toast.LENGTH_SHORT).show();
                    return;

                }
                if (TextUtils.isEmpty(password))
                {
                    Toast.makeText(Login.this, "Enter valid Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                            Intent obj6 = new Intent(Login.this, AllNotes.class);
                            startActivity(obj6);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "Incorrect Email or Password",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
}

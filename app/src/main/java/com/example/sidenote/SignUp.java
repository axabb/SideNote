package com.example.sidenote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

public class SignUp extends AppCompatActivity {
    EditText s_name, s_email, s_pass;
    Button s_to_l, sign_btn, b_to_s;
    FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        s_name = (EditText) findViewById(R.id.sign_username);
        s_email = (EditText) findViewById(R.id.sign_email);
        s_pass = (EditText) findViewById(R.id.sign_password);
        sign_btn = (Button) findViewById(R.id.signup_btn);
        s_to_l = (Button) findViewById(R.id.sign_to_log);
        b_to_s = (Button) findViewById(R.id.back_to_start);

        s_to_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objj = new Intent(SignUp.this, Login.class);
                startActivity(objj);
            }
        });

        b_to_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obbjj = new Intent(SignUp.this, HomeActivity.class);
                startActivity(obbjj);
            }
        });

        mAuth = FirebaseAuth.getInstance();

        sign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, email, password;
                name = String.valueOf(s_name.getText());
                email = String.valueOf(s_email.getText());
                password = String.valueOf(s_pass.getText());

                if (TextUtils.isEmpty(name))
                {
                    Toast.makeText(SignUp.this, "Enter valid Username", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(email))
                {
                    Toast.makeText(SignUp.this, "Enter valid Email Address", Toast.LENGTH_SHORT).show();
                    return;

                }
                if (TextUtils.isEmpty(password))
                {
                    Toast.makeText(SignUp.this, "Enter valid Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(SignUp.this, "Account Successfully Created", Toast.LENGTH_SHORT).show();
                            Intent obj4 = new Intent(SignUp.this, Login.class);
                            startActivity(obj4);
                            finish();

                        } else {
                            Toast.makeText(SignUp.this, "Authentication Failed", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });



    }
}

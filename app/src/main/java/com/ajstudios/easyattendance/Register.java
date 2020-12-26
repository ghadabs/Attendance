package com.ajstudios.easyattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ajstudios.easyattendance.Model.UserDetails;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    EditText  username, address, password,university;
    Button btnSignUp;
    TextView tvSignIn;

    FirebaseAuth mFirebaseAuth;
    FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        address = findViewById(R.id.register_name);
        password = findViewById(R.id.register_password);
        university= findViewById(R.id.register_university);
        btnSignUp = findViewById(R.id.register);
        tvSignIn = findViewById(R.id.login_register);


        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String usern = username.getText().toString();
                final String add = address.getText().toString();
                final String pwd = password.getText().toString();
                final String univ = university.getText().toString();

               if (usern.isEmpty()) {
                   username.setError("Please provide your username");
                   username.requestFocus();
                } else if (add.isEmpty()) {
                    address.setError("Please provide your email");
                   address.requestFocus();
                } else if (univ.isEmpty()) {
                    university.setError("Please provide your university");
                    university.requestFocus();
                } else if (pwd.isEmpty()) {
                    password.setError("Please provide password");
                    password.requestFocus();
                } else if (!(add.isEmpty() && pwd.isEmpty())) {
                    mFirebaseAuth.createUserWithEmailAndPassword(add, pwd)
                            .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (!task.isSuccessful()) {
                                        Toast.makeText(Register.this, "SignUp Unsuccessful, Plaese Try Again!", Toast.LENGTH_SHORT).show();

                                    } else {
                                        UserDetails userDetail = new UserDetails(usern,add,univ);
                                        String uid = task.getResult().getUser().getUid();
                                        firebaseDatabase.getReference(uid).setValue(userDetail)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Intent intent = new Intent(Register.this,
                                                                Login.class);
                                                      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        intent.putExtra("name", usern );
                                                        startActivity(intent);
                                                    }
                                                });
                                    }

                                }
                            });
                } else {
                   Toast.makeText(Register.this, "Registred", Toast.LENGTH_SHORT).show();
                }
            }
        });

       tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Register.this, Login.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
    }
}
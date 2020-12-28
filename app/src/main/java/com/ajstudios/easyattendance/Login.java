package com.ajstudios.easyattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ajstudios.easyattendance.realm.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.realm.Realm;
import io.realm.RealmResults;

public class Login extends AppCompatActivity {


    private static final String TAG = "Home";
    EditText emailId, password;
    Button btnSignIn;
    TextView tvSignUp;
    private Realm realm;
    private UserDetails myRealmObject;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        emailId = findViewById(R.id.login_name);
        password = findViewById(R.id.Login_password);
        btnSignIn = findViewById(R.id.button_login);
        tvSignUp = findViewById(R.id.textViewSignUp);



        realm = Realm.getDefaultInstance();


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=emailId.getText().toString();
                String pass=password.getText().toString();
                if (email.length() == 0) {
                    Toast.makeText(Login.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    emailId.requestFocus();
                } else if (pass.length() == 0) {
                    Toast.makeText(Login.this, "Enter password", Toast.LENGTH_SHORT).show();
                    password.requestFocus();
                }else if (checkUser(email,pass)){
                    myRealmObject = realm.where(UserDetails.class).equalTo("address", email).findFirst();
                    Intent i = new Intent(Login.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.putExtra("person_id", myRealmObject.getId());
                    startActivity(i);
                }else {
                    Toast.makeText(Login.this, "Invalid Email or Password!", Toast.LENGTH_SHORT).show();
                }

            }
            private boolean checkUser(String email,String password) {
                RealmResults<UserDetails> realmObjects = realm.where(UserDetails.class).findAll();
                for (UserDetails myRealmObject : realmObjects) {
                    if (email.equals(myRealmObject.getAddress()) && password.equals(myRealmObject.getPassword())) {
                        return true;
                    }
                }
                return false;
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intSignUp = new Intent(Login.this, Register.class);
                intSignUp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intSignUp);

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    /*private void moveToHomeActivity(FirebaseUser mFirebaseUser) {

        firebaseDatabase.getReference().child(mFirebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserDetails userDetail = snapshot.getValue(UserDetails.class);
                        String name = userDetail.getUsername() ;
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.putExtra("name", name);
                        startActivity(i);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {


                    }
                });
    }*/
}
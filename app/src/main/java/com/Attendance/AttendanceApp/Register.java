package com.Attendance.AttendanceApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Attendance.AttendanceApp.realm.UserDetails;

import java.util.UUID;

import io.realm.Realm;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class Register extends AppCompatActivity {
    private static final String TAG = "Register";
    EditText  username, address, password,university;
    Button btnSignUp;
    TextView tvSignIn;
    Realm realm;
    private UserDetails myRealmObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        username = findViewById(R.id.username);
        address = findViewById(R.id.register_name);
        password = findViewById(R.id.register_password);
        university= findViewById(R.id.register_university);
        btnSignUp = findViewById(R.id.register);
        tvSignIn = findViewById(R.id.login_register);




        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (username.length() == 0) {
                    Toast.makeText(Register.this, "Enter Username", Toast.LENGTH_SHORT).show();;
                    username.requestFocus();
                } else if (university.length() == 0) {
                    Toast.makeText(Register.this, "Enter University", Toast.LENGTH_SHORT).show();
                    university.requestFocus();
                } else if (address.length() ==0) {
                    Toast.makeText(Register.this, "Enter a valid email", Toast.LENGTH_SHORT).show();;
                    address.requestFocus();
                }else if (password.length() ==0) {
                    Toast.makeText(Register.this, "Enter a valid password", Toast.LENGTH_SHORT).show();;
                    password.requestFocus();
                } else {

                    try{

                        realm.beginTransaction();
                        myRealmObject = realm.createObject(UserDetails.class, UUID.randomUUID().toString());
                        myRealmObject.setUsername(username.getText().toString());
                        myRealmObject.setUniversity(university.getText().toString());
                        myRealmObject.setAddress(address.getText().toString());
                        myRealmObject.setPassword(password.getText().toString());
                        realm.commitTransaction();
                        Toast.makeText(Register.this, "Save success", Toast.LENGTH_SHORT).show();

                    } catch (RealmPrimaryKeyConstraintException e){
                        e.printStackTrace();
                        Toast.makeText(Register.this, "User found on db.", Toast.LENGTH_SHORT).show
                                ();
                    }
                    Intent i = new Intent(Register.this, MainActivity.class);
                    i.putExtra("person_id", myRealmObject.getId());
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);


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
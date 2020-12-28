package com.ajstudios.easyattendance;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.ajstudios.easyattendance.Adapter.ClassListAdapter;
import com.ajstudios.easyattendance.realm.Class_Names;
import com.ajstudios.easyattendance.realm.UserDetails;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    BottomAppBar bottomAppBar;
    FloatingActionButton fab_main;
    RecyclerView recyclerView;
    TextView sample;
    ClassListAdapter mAdapter;
    Realm realm;
    TextView btnLogout;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(this);

        getWindow().setEnterTransition(null);
        btnLogout = findViewById(R.id.out);
        Intent i=this.getIntent();
        final String personId = i.getStringExtra("person_id");

        bottomAppBar = findViewById(R.id.bottomAppBar);
        fab_main = findViewById(R.id.fab_main);
        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Insert_class_Activity.class);
                intent.putExtra("person_id", personId);
                startActivity(intent);
            }
        });

        realm = Realm.getDefaultInstance();
        UserDetails person = realm.where(UserDetails.class).equalTo("id", personId).findFirst();
        RealmResults<Class_Names> results;

        results = realm.where(Class_Names.class)
               .equalTo("userId", person.getId())
                .findAll();


        sample = findViewById(R.id.classes_sample);
        recyclerView = findViewById(R.id.recyclerView_main);

        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        mAdapter = new ClassListAdapter( results,MainActivity.this);
        recyclerView.setAdapter(mAdapter);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });



    }

    @Override
    protected void onResume() {
        realm.refresh();
        realm.setAutoRefresh(true);
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
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
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.ajstudios.easyattendance.Adapter.ClassListAdapter;
import com.ajstudios.easyattendance.Adapter.StudentsListAdapter;
import com.ajstudios.easyattendance.realm.Attendance_Reports;
import com.ajstudios.easyattendance.realm.Class_Names;
import com.ajstudios.easyattendance.realm.Students_List;
import com.ajstudios.easyattendance.realm.UserDetails;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity {

    BottomAppBar bottomAppBar;
    FloatingActionButton fab_main;
    RecyclerView recyclerView;
    TextView sample;
    ClassListAdapter mAdapter;
    Realm realm;
    TextView btnLogout,place_holder;
    RealmChangeListener realmChangeListener;
    private Handler handler = new Handler();

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
        place_holder = findViewById(R.id.placeholder_detail1);
        place_holder.setVisibility(View.GONE);
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
        final UserDetails person = realm.where(UserDetails.class).equalTo("id", personId).findFirst();
        RealmResults<Class_Names> results;

        results = realm.where(Class_Names.class)
               .equalTo("userId", person.getId()).findAll();


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

        Runnable r = new Runnable() {
            @Override
            public void run() {
                RealmInit();
            }
        };
        handler.postDelayed(r, 500);

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
    public void RealmInit(){
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        Intent i=this.getIntent();
        final String personId = i.getStringExtra("person_id");
        final UserDetails person = realm.where(UserDetails.class).equalTo("id", personId).findFirst();
        realmChangeListener = new RealmChangeListener() {
            @Override
            public void onChange(Object o) {
                long count = realm.where(Class_Names.class)
                        .equalTo("userId", personId)
                        .count();
                    if (!(count==0)){
                        place_holder.setVisibility(View.GONE);
                    }else if (count==0) {
                        place_holder.setVisibility(View.VISIBLE);
                    }

            }
        };
        realm.addChangeListener(realmChangeListener);

        long count = realm.where(Class_Names.class)
                .equalTo("userId", personId)
                .count();

            if (!(count==0)){
                place_holder.setVisibility(View.GONE);
            }else if (count==0){
                place_holder.setVisibility(View.VISIBLE);
            }

    }
}
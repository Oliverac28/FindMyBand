package com.example.findmyband;


import android.content.Intent;
import androidx.annotation.NonNull;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;


import android.net.Uri;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;


import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;


import java.util.HashMap;
import java.util.Map;



public class account extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;

    private Button mLogout;
    private Button saveBtn;

    //saving user profile

    private String user_id;


    private EditText mLocation, mPrimaryInstrument, mSecondaryInstrument, mGenreOne, mGenreTwo, mGenreThree, mBio;
    StorageReference storageReference;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mLogout = findViewById(R.id.logout_button);

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.logout_button) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent_MainActivity = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent_MainActivity);

                }
            }
        });

        //saving user profile
        {
            firebaseAuth = FirebaseAuth.getInstance();
            user_id = firebaseAuth.getCurrentUser().getUid();

            firebaseFirestore = FirebaseFirestore.getInstance();
            storageReference = FirebaseStorage.getInstance().getReference();




            mLocation = findViewById(R.id.location);
            mPrimaryInstrument = findViewById(R.id.primary_instrument);
            mSecondaryInstrument = findViewById(R.id.secondary_instrument);
            mGenreOne = findViewById(R.id.genre_one);
            mGenreTwo = findViewById(R.id.genre_two);
            mGenreThree = findViewById(R.id.genre_three);
            mBio = findViewById(R.id.bio);

            saveBtn = findViewById(R.id.save_profile_button);


            saveBtn.setEnabled(false);

            firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {

                        if (task.getResult().exists()) {

                            String location = task.getResult().getString("location");
                            String primaryInstrument = task.getResult().getString("primaryInstrument");
                            String secondaryInstrument = task.getResult().getString("secondaryInstrument");
                            String genreOne = task.getResult().getString("genreOne");
                            String genreTwo = task.getResult().getString("genreTwo");
                            String genreThree = task.getResult().getString("genreThree");
                            String bio = task.getResult().getString("bio");


                            mLocation.setText(location);
                            mPrimaryInstrument.setText(primaryInstrument);
                            mSecondaryInstrument.setText(secondaryInstrument);
                            mGenreOne.setText(genreOne);
                            mGenreTwo.setText(genreTwo);
                            mGenreThree.setText(genreThree);
                            mBio.setText(bio);
                        }

                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(account.this, "(FIRESTORE Retrieve Error2) : " + error, Toast.LENGTH_LONG).show();
                    }

                    saveBtn.setEnabled(true);
                }
            });

            //Crashes on attempt to overwrite value
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String location = mLocation.getText().toString();
                    final String primary_instrument = mPrimaryInstrument.getText().toString();
                    final String secondary_instrument = mSecondaryInstrument.getText().toString();
                    final String genre_one = mGenreOne.getText().toString();
                    final String genre_two = mGenreTwo.getText().toString();
                    final String genre_three = mGenreThree.getText().toString();
                    final String bio = mBio.getText().toString();

                    user_id = firebaseAuth.getCurrentUser().getUid();
                    try {
                        Task<UploadTask.TaskSnapshot> task = null;
                        storeFirestore(task, location, primary_instrument, secondary_instrument, genre_one, genre_two, genre_three, bio);

                        //storeFirestore(null, _location, primary_instrument, secondary_instrument, genre_one, genre_two, genre_three, _bio);
                    }
                    catch (Exception task){
                        Toast.makeText(account.this, "(Cannot Overwrite)", Toast.LENGTH_SHORT).show();
                    }
                }

            });
        }


        {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            drawer = findViewById(R.id.drawer_layout);
            //*****
            NavigationView navigationView = findViewById(R.id.nav_view);//listener for navigation method
            navigationView.setNavigationItemSelectedListener(this);
            //*****

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                    R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            navigationView.setCheckedItem(R.id.nav_account);
        }


    }

    //methods for saving user profile
    private void storeFirestore(@NonNull Task<UploadTask.TaskSnapshot> task, String location, String primary_instrument
            , String secondary_instrument, String genre_one, String genre_two, String genre_three, String bio) {

        db.collection("Users").document(user_id)
                .update(
                        "location", location,
                        "primaryInstrument", primary_instrument,
                        "secondaryInstrument", secondary_instrument,
                        "genreOne", genre_one,
                        "genreTwo", genre_two,
                        "genreThree", genre_three,
                        "bio", bio
                       );

        System.out.println("Hello");


        Toast.makeText(account.this, "The user Settings are updated.", Toast.LENGTH_LONG).show();
        Intent mainIntent = new Intent(account.this, discovery.class);
        startActivity(mainIntent);
        finish();


    }


    //navigation method
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.nav_discover:
                Intent intent_discover = new Intent(getApplicationContext(), discovery.class);
                startActivity(intent_discover);
                break;

            case R.id.nav_message:
                Intent intent_message = new Intent(getApplicationContext(), message.class);
                startActivity(intent_message);
                break;

            case R.id.nav_search:
                Intent intent_search = new Intent(getApplicationContext(), search.class);
                startActivity(intent_search);
                break;

            case R.id.nav_account:
                Intent intent_account = new Intent(getApplicationContext(), account.class);
                startActivity(intent_account);
                break;
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }
}

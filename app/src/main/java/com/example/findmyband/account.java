package com.example.findmyband;

import android.Manifest;
import android.content.Intent;
import androidx.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class account extends AppCompatActivity implements /*AdapterView.OnItemSelectedListener,*/ NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    //private Spinner primary_instrument;
    //private Spinner secondary_instrument;

    private Button mLogout;

    //saving user profile
    /////////////////////////////private CircleImageView setupImage;
    private Uri mainImageURI = null;

    private String user_id;

    private boolean isChanged = false;

    private EditText mLocation, mPrimaryInstrument, mSecondaryInstrument, mGenreOne, mGenreTwo, mGenreThree, mBio;
    private Button saveBtn;

    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private Bitmap compressedImageFile;
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


           /////////////////////////////////// setupImage = findViewById(R.id.setup_image);

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

                    if(task.isSuccessful()){

                        if(task.getResult().exists()){

                            String location = task.getResult().getString("location");
                            String primaryInstrument = task.getResult().getString("primaryInstrument");
                            String secondaryInstrument = task.getResult().getString("secondaryInstrument");
                            String genreOne = task.getResult().getString("genreOne");
                            String genreTwo = task.getResult().getString("genreTwo");
                            String genreThree = task.getResult().getString("genreThree");
                            String bio = task.getResult().getString("bio");

                            String image = task.getResult().getString("image");

                            mainImageURI = Uri.parse(image);

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


            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String _location = mLocation.getText().toString();
                    final String primary_instrument = mPrimaryInstrument.getText().toString();
                    final String secondary_instrument = mSecondaryInstrument.getText().toString();
                    final String genre_one = mGenreOne.getText().toString();
                    final String genre_two = mGenreTwo.getText().toString();
                    final String genre_three = mGenreThree.getText().toString();
                    final String _bio = mBio.getText().toString();

                    if (!TextUtils.isEmpty(_location) && mainImageURI != null) {



                        if (isChanged) {

                            user_id = firebaseAuth.getCurrentUser().getUid();

                            File newImageFile = new File(mainImageURI.getPath());
                            try {

                                compressedImageFile = new Compressor(account.this)
                                        .setMaxHeight(125)
                                        .setMaxWidth(125)
                                        .setQuality(50)
                                        .compressToBitmap(newImageFile);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] thumbData = baos.toByteArray();

                            UploadTask image_path = storageReference.child("profile_images").child(user_id + ".jpg").putBytes(thumbData);

                            image_path.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                    if (task.isSuccessful()) {
                                        storeFirestore(task, _location, primary_instrument, secondary_instrument, genre_one, genre_two, genre_three, _bio);

                                    } else {

                                        String error = task.getException().getMessage();
                                        //Toast.makeText(setup.this, "(IMAGE Error) : " + error, Toast.LENGTH_LONG).show();



                                    }
                                }
                            });

                        } else {

                            storeFirestore(null, _location, primary_instrument, secondary_instrument, genre_one, genre_two, genre_three, _bio);

                        }

                    }

                }

            });
/*
            setupImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                        if(ContextCompat.checkSelfPermission(account.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                            //Toast.makeText(setup.this, "Permission Denied", Toast.LENGTH_LONG).show();
                            ActivityCompat.requestPermissions(account.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                        } else {

                            BringImagePicker();

                        }

                    } else {

                        BringImagePicker();

                    }

                }

            });*/
        }
        //

        /*{
            primary_instrument = findViewById(R.id.primary_instrument);
            secondary_instrument = findViewById(R.id.secondary_instrument);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.instrument_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            primary_instrument.setAdapter(adapter);
            secondary_instrument.setAdapter(adapter);
        }*/

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
    private void storeFirestore(@NonNull Task<UploadTask.TaskSnapshot> task, String _location, String primary_instrument
            , String secondary_instrument, String genre_one, String genre_two, String genre_three, String _bio) {

        Uri download_uri;

        if(task != null) {

            download_uri = task.getResult().getUploadSessionUri();

        } else {

            download_uri = mainImageURI;

        }

        Map<String, String> userMap = new HashMap<>();
        userMap.put("location", _location);
        userMap.put("primaryInstrument", primary_instrument);
        userMap.put("secondaryInstrument", secondary_instrument);
        userMap.put("genreOne", genre_one);
        userMap.put("genreTwo", genre_two);
        userMap.put("genreThree", genre_three);
        userMap.put("bio", _bio);

        userMap.put("image", download_uri.toString());

        firebaseFirestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    Toast.makeText(account.this, "The user Settings are updated.", Toast.LENGTH_LONG).show();
                    Intent mainIntent = new Intent(account.this, discovery.class);
                    startActivity(mainIntent);
                    finish();

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(account.this, "(FIRESTORE Error1) : " + error, Toast.LENGTH_LONG).show();

                }


            }
        });


    }

    private void BringImagePicker() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(account.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mainImageURI = result.getUri();
             ////////////////////   setupImage.setImageURI(mainImageURI);

                isChanged = true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }

    }
    //
    /*

    //Methods for spinners
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
    */
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
//*************

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

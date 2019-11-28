package com.example.findmyband;

import android.content.Intent;
import androidx.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class account extends AppCompatActivity implements AdapterView.OnItemSelectedListener, NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    private Spinner primary_instrument;
    private Spinner secondary_instrument;

    private Button mLogout;

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

        {
            primary_instrument = findViewById(R.id.primary_instrument);
            secondary_instrument = findViewById(R.id.secondary_instrument);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.instrument_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            primary_instrument.setAdapter(adapter);
            secondary_instrument.setAdapter(adapter);
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
    //Methods for spinners
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
    //
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

package com.example.findmyband;

import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class message extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;

    private ListView listView;
    private String[] dummy_users = {"user1", "user2", "user3", "user4", "user5", "user6", "user7", "user8", "user9", "user10"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        {
            listView = findViewById(R.id.friends);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dummy_users);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(openChat);
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

            navigationView.setCheckedItem(R.id.nav_message);
        }
    }

    private AdapterView.OnItemClickListener openChat = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            Intent intent_chat = new Intent(getApplicationContext(), chat.class);
            startActivity(intent_chat);
        }
    };

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

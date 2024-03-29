package com.example.findmyband;

import android.content.Intent;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class discovery extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    private DrawerLayout drawer;

    //Swipe cards//

    private cards cards_data[];
    private arrayAdapter arrayAdapter;
    private int i;
    //

    private final String COLLECTION_KEY = "Users";
    private String user_id;
    private String currentLocation;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference currentUser;


    List<cards> rowItems;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);

        rowItems = new ArrayList<>();

        //Navigation

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

        navigationView.setCheckedItem(R.id.nav_discover);
        //

        //Get user id
        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();

        currentUser = db.document("Users/"+user_id);

        generateCurrentLocation();

        //Swipe cards

        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems );

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);


        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Toast.makeText(discovery.this, "left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Toast.makeText(discovery.this, "right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                //rowItems.add("XML ".concat(String.valueOf(i)));
                arrayAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
                i++;
            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(discovery.this, "click", Toast.LENGTH_SHORT).show();
            }
        });

        //Swipe Cards

        generateStack();

    }

    public void generateCurrentLocation() {

        System.out.println("Test");
        currentUser.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess (DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            currentLocation = documentSnapshot.getString("location");
                            System.out.println(currentLocation);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure (@NonNull Exception e) {
                Toast.makeText(discovery.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                ;
            }
        });
    }

   //Searches the users by location and adds them to the stack
   public void generateStack(){
       System.out.println("Generate Stack begin");
       // Create a reference to the Users collection
       CollectionReference users = db.collection(COLLECTION_KEY);

       // Create a query against the collection.
       // final Query query = users.whereEqualTo("location", currentLocation);


       ArrayList<String> data = new ArrayList<>();

       db.collection(COLLECTION_KEY)
               .whereEqualTo("location", "kamloops")
               .get()
               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete (@NonNull Task<QuerySnapshot> task) {
                       if (task.isSuccessful()) {
                           for (QueryDocumentSnapshot document : task.getResult()) {

                               //cards item = document.toObject(cards.class);

                               //Log.d("testing",(String)document.getData().get("name") + "");


                               cards item = new cards(
                                       document.getData().get("name") + "",
                                       document.getData().get("lName") + "",
                                       document.getData().get("image") + "",
                                       document.getData().get("location") + "",
                                       document.getData().get("primaryInstrument") + "",
                                       document.getData().get("secondaryInstrument") + "",
                                       document.getData().get("genreOne") + "",
                                       document.getData().get("genreTwo") + "",
                                       document.getData().get("genreThree") + "",
                                       document.getData().get("bio") + ""
                               );
                               rowItems.add(item);
                               System.out.println(rowItems.toString());


                               arrayAdapter.notifyDataSetChanged();


                           }
                       } else {

                       }
                   }
               });

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
//comment
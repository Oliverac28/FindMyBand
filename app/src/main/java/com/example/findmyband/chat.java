package com.example.findmyband;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.r0adkll.slidr.Slidr;

public class chat extends AppCompatActivity {

    private ListView listView;
    private String[] dummy_messages = {"message1", "message2", "message3", "message4", "message5", "message6"
            , "message7", "message8", "message9", "message10"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Slidr.attach(this);

        listView = findViewById(R.id.friends);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dummy_messages);
        listView.setAdapter(adapter);
    }
}

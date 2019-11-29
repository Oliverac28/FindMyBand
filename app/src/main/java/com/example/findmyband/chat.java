package com.example.findmyband;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class chat extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private String[] dummy_messages = {"message1", "message2", "message3", "message4", "message5"
            , "message6", "message7", "message8", "message9", "message10"};
    private EditText editText;
    private List< String > ListElementsArrayList = new ArrayList< String > (Arrays.asList(dummy_messages));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Slidr.attach(this);

        listView = findViewById(R.id.display_messages);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ListElementsArrayList);
        listView.setAdapter(adapter);

        editText = findViewById(R.id.message_text);
    }

    public void sendMessage(View view){
        ListElementsArrayList.add(editText.getText().toString());
        adapter.notifyDataSetChanged();
        editText.setText("");
    }
}

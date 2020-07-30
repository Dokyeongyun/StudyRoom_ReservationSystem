package com.example.studyroomreservationsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ChatTest extends AppCompatActivity {
    ArrayList<String> list = new ArrayList<>();
    EditText edt;
    Button button5;
    ArrayAdapter<String> adapter;
    String name="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_test);

        Intent intent = getIntent();
        String userNo = intent.getStringExtra("userNo");

        String result="";
        try {
            result = new Task().execute("Get_UserInfo",userNo).get();
            String[] resultSplit = result.split(" ");

            name = resultSplit[1];

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }


        ListView listview = findViewById(R.id.listview);
        edt = findViewById(R.id.editText);
        button5 = findViewById(R.id.button5);

        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.simplelist, list);
        listview.setAdapter(adapter);


        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                list.add(name+" : "+edt.getText().toString()); //버튼을 클릭하면 array에 추가
                adapter.notifyDataSetChanged(); //어댑터 새로고침
                edt.setText("");
            }
        });
    }

}

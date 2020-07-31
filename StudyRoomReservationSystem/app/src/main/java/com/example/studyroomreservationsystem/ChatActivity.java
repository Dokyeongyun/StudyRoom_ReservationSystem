package com.example.studyroomreservationsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class ChatActivity extends AppCompatActivity {
    ArrayList<ChatVO> list = new ArrayList<>();
    ListView listview;
    EditText edt;
    ImageButton button5;
    String name = "";
    int[] imageID = {R.drawable.account_circle_48dp, R.drawable.account_circle_48dp, R.drawable.account_circle_48dp};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        String userNo = intent.getStringExtra("userNo");

        //ArrayAdapter<ChatVO> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.simplelist, list);
        //listview.setAdapter(adapter);

        listview = findViewById(R.id.listview);
        edt = findViewById(R.id.editText);
        button5 = findViewById(R.id.button5);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("message");

        String result = "";
        try {
            result = new Task().execute("Get_UserInfo", userNo).get();
            String[] resultSplit = result.split(" ");

            name = resultSplit[1];

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        final ChatAdapter adapter = new ChatAdapter(getApplicationContext(), R.layout.simplelist, list, name);
        ((ListView) findViewById(R.id.listview)).setAdapter(adapter);


        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "내용을 입력하세요.", Toast.LENGTH_LONG).show();
                } else {
                    Date today = new Date();
                    SimpleDateFormat timeNow = new SimpleDateFormat("a K:mm");

                    StringBuffer sb = new StringBuffer(edt.getText().toString());
                    int lineWord = 45;
                    if (sb.length() >= lineWord) {
                        for (int i = 1; i <= sb.length() / lineWord; i++) {
                            sb.insert(lineWord * i, "\n");
                        }
                    }

//list.add(new ChatVO(R.drawable.profile1, id, sb.toString(), timeNow.format(today)));
//adapter.notifyDataSetChanged();

                    // 채팅내역 업로드
                    //myRef.setValue(new ChatVO(R.drawable.account_circle_48dp, name, sb.toString(), timeNow.format(today)));
                    // 채팅내역 덮어쓰기 방지
                    myRef.push().setValue(new ChatVO(R.drawable.account_circle_48dp, name, sb.toString(), timeNow.format(today)));
                    myRef.push().setValue(new ChatVO(R.drawable.account_circle_48dp, "테스트상대", sb.toString(), timeNow.format(today)));
                    myRef.push().setValue(new ChatVO(R.drawable.ic_adb_black_24dp, "봇로는하말로꾸거", sb.reverse().toString(), timeNow.format(today)));
                    edt.setText("");

                }
            }
        });


        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatVO value = dataSnapshot.getValue(ChatVO.class); // 괄호 안 : 꺼낼 자료 형태
                list.add(value);
                adapter.notifyDataSetChanged();
                listview.setSelection(list.size()-1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

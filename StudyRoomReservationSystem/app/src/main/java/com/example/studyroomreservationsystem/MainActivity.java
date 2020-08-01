package com.example.studyroomreservationsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView id_tv, pw_tv;
    EditText id_et, pw_et;
    Button join_bt, login_bt;
    String id, pwd;

    String userNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Setting();
    }

    public void Setting() {
        id_et = findViewById(R.id.id_et);
        pw_et = findViewById(R.id.pw_et);

        join_bt = findViewById(R.id.join_bt);
        login_bt = findViewById(R.id.login_bt);
    }

    public void Join(View view) {
        Intent intent = new Intent(MainActivity.this, Register.class);
        startActivity(intent);
    }

    public void Login(View view) {
        id = id_et.getText().toString();
        pwd = pw_et.getText().toString();
        try {
            String result2 = new Task().execute("Login", id, pwd).get();
            String[] result = result2.split(" ");
            if (result[0].equals("true")) {
                userNo = result[1];
                Intent intent = new Intent(MainActivity.this, MenuPage.class);
                intent.putExtra("userNo", userNo);
                startActivity(intent);
                overridePendingTransition(0, 0);
            } else if (result[0].equals("false")) {
                Toast.makeText(MainActivity.this, "아이디 또는 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
            } else if (result[0].equals("noId")) {
                Toast.makeText(MainActivity.this, "아이디가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
        }
    }
}

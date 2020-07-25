package com.example.studyroomreservationsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView id_tv, pw_tv, idStatus_tv;
    EditText id_et, pw_et;
    Button join_bt, login_bt;
    String id, pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Setting();
    }
    public void Setting(){
        id_tv = findViewById(R.id.id_tv);
        pw_tv = findViewById(R.id.pw_tv);
        idStatus_tv = findViewById(R.id.idStatus_tv);

        id_et = findViewById(R.id.id_et);
        pw_et = findViewById(R.id.pw_et);

        join_bt = findViewById(R.id.join_bt);
        login_bt = findViewById(R.id.login_bt);

        id = id_et.getText().toString();
        pwd = pw_et.getText().toString();
    }

    public void Join(View view) {
        Intent intent = new Intent(MainActivity.this, Register.class);
        startActivity(intent);
    }

    public void Login(View view) {
        id = id_et.getText().toString();
        pwd = pw_et.getText().toString();
        try {
            String result = new Task().execute("Login", id, pwd).get();
            if (result.equals("true")) {
                Intent intent = new Intent(MainActivity.this, MenuPage.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            } else if (result.equals("false")) {
                Toast.makeText(MainActivity.this, "아이디 또는 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
            } else if (result.equals("noId")) {
                Toast.makeText(MainActivity.this, "아이디가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
        }
    }

}

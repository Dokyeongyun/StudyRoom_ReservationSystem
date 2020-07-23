package com.example.studyroomreservationsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView id_tv, pw_tv, pwchk_tv, textView;
    EditText id_et, pw_et, pwchk_et;
    Button join_bt, login_bt;
    String id, pw, pwchk;
/*

    String users[] = new String[100];
    StringBuffer sb;
    int version = 1;
    int count = 0;
    MyDBOpenHelper helper;
    SQLiteDatabase database;

    String sql;
    Cursor cursor;
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/*        Task task = new Task();
        task.execute("DKY", "1234");*/


        textView = findViewById(R.id.textView);

        id_tv = findViewById(R.id.id_tv);
        pw_tv = findViewById(R.id.pw_tv);
        pwchk_tv = findViewById(R.id.pwchk_tv);

        id_et = findViewById(R.id.id_et);
        pw_et = findViewById(R.id.pw_et);
        pwchk_et = findViewById(R.id.pwchk_et);

        join_bt = findViewById(R.id.join_bt);
        login_bt = findViewById(R.id.login_bt);



/*
        helper = new MyDBOpenHelper(this, MyDBOpenHelper.tableName,null, version);
        database = helper.getWritableDatabase();
        sb = new StringBuffer();
*/
    }

    public void Join(View view) {
        id = id_et.getText().toString();
        pw = pw_et.getText().toString();
        pwchk = pwchk_et.getText().toString();
/*

        try{
            String result;
            Task task = new Task();
            result = task.execute("DKY","1234").get();
            Log.i("리턴 값",result);
        }catch(Exception e){

        }
*/

        try {
            String result  = new Task().execute(id,pw,"join").get();
            if(result.equals("id")) {
                Toast.makeText(MainActivity.this,"이미 존재하는 아이디입니다.",Toast.LENGTH_SHORT).show();
/*                id_et.setText("");
                pw_et.setText("");*/
            } else if(result.equals("OK")) {
/*                id_et.setText("");
                pw_et.setText("");*/
                Toast.makeText(MainActivity.this,"회원가입을 축하합니다.",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e) {}


/*
        if (id != null && pw != null && pwchk != null) {
            if (pw.equals(pwchk)) {
                // DB에 추가
            } else {
                // 패스워드 불일치 메시지
            }
        } else {
            // 정보 입력 확인 메시지
        }*/
    }

    public void Login(View view) {
        id = id_et.getText().toString();
        pw = pw_et.getText().toString();
        try {
            String result  = new Task().execute(id,pw,"login").get();
            if(result.equals("true")) {
                Toast.makeText(MainActivity.this,"로그인",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                startActivity(intent);
                finish();
            } else if(result.equals("false")) {
                Toast.makeText(MainActivity.this,"아이디 또는 비밀번호가 틀렸음",Toast.LENGTH_SHORT).show();
/*                id_et.setText("");
                pw_et.setText("");*/
            } else if(result.equals("noId")) {
                Toast.makeText(MainActivity.this,"존재하지 않는 아이디",Toast.LENGTH_SHORT).show();
/*                id_et.setText("");
                pw_et.setText("");*/
            }
        }catch (Exception e) {}
    }
/*
    public void Add(View view){
        Toast.makeText(this, "Add Button", Toast.LENGTH_SHORT).show();
        sb.setLength(0);
        helper.insertName(database,(id_et.getText().toString()));
        echoText();
    }

    public void echoText(){
        sql = "select * from " + helper.tableName;
        cursor = database.rawQuery(sql,null);
        if(cursor !=null){
            count = cursor.getCount();
            for(int i=0; i<count; i++){
                cursor.moveToNext();
                String user = cursor.getString(0);
                users[i] = user;
                sb.append(users[i]+" ");
            }
            textView.setText(" "+sb);
            cursor.close();
        }
    }*/
}

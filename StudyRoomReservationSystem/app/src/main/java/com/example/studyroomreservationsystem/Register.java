package com.example.studyroomreservationsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class Register extends AppCompatActivity {

    EditText name_et, id_et, pw_et, pwchk_et, stuno_et, email_et, phone_et, grade_et;
    String name, id, pw, pwchk, stuno, email, phone, grade;
    String result;
    boolean id_isPossible = false, email_isPossible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setting();
    }

    public void setting() {
        name_et = findViewById(R.id.name_et);
        id_et = findViewById(R.id.id_et);
        pw_et = findViewById(R.id.pw_et);
        pwchk_et = findViewById(R.id.pwchk_et);
        stuno_et = findViewById(R.id.stuno_et);
        email_et = findViewById(R.id.email_et);
        phone_et = findViewById(R.id.phone_et);
        grade_et = findViewById(R.id.grade_et);
    }

    public void Register_Click(View view) throws ExecutionException, InterruptedException {
        switch (view.getId()) {
            case R.id.register_bt:
                if (id_isPossible && email_isPossible) {
                    // 아이디와 이메일 중복검사를 완료,
                    name = name_et.getText().toString();
                    id = id_et.getText().toString();
                    pw = pw_et.getText().toString();
                    pwchk = pwchk_et.getText().toString();
                    stuno = stuno_et.getText().toString();
                    email = email_et.getText().toString();
                    phone = phone_et.getText().toString();
                    grade = grade_et.getText().toString();
                    if (!pw.equals(pwchk)) {
                        // 비밀번호와 비밀번호확인이 일치하지 않으면,
                        Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        // 비밀번호와 비밀번호확인이 일치 => DB에 데이터 삽입 => 회원가입 완료 메시지 출력 및 액티비티 종료
                        result = new Task().execute("Register", null, name, id, pw, stuno, email, phone, grade).get();
                        Toast.makeText(this, "처리중입니다. 잠시만 기다려주세요.", Toast.LENGTH_SHORT).show();
                        if (result.equals("Register_OK")) {
                            Toast.makeText(this, "축하합니다. 회원가입이 완료되었습니다!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                } else {
                    if (!id_isPossible && email_isPossible) {
                        Toast.makeText(this, "아이디 중복검사를 실행해주세요.", Toast.LENGTH_SHORT).show();
                    } else if (!email_isPossible && id_isPossible) {
                        Toast.makeText(this, "이메일 중복검사를 실행해주세요.", Toast.LENGTH_SHORT).show();
                    } else if (!id_isPossible && !email_isPossible) {
                        Toast.makeText(this, "아이디와 이메일 중복검사를 실행해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.id_bt:
                // 아이디 중복검사
                id = id_et.getText().toString();
                result = new Task().execute("ID_doubleCheck", id).get();

                if (result.equals("ID_double")) {
                    Toast.makeText(this, "중복된 아이디입니다!", Toast.LENGTH_SHORT).show();
                    id_isPossible = false;
                } else if (result.equals("ID_OK")) {
                    Toast.makeText(this, "사용가능한 아이디입니다!", Toast.LENGTH_SHORT).show();
                    id_isPossible = true;
                }
                break;
            case R.id.email_bt:
                // 이메일 중복검사
                email = email_et.getText().toString();
                result = new Task().execute("EMAIL_doubleCheck", email).get();

                if (result.equals("EMAIL_double")) {
                    Toast.makeText(this, "중복된 이메일입니다!", Toast.LENGTH_SHORT).show();
                    email_isPossible = false;
                } else if (result.equals("EMAIL_OK")) {
                    Toast.makeText(this, "사용가능한 이메일입니다!", Toast.LENGTH_SHORT).show();
                    email_isPossible = true;
                }
                break;
        }
    }
}

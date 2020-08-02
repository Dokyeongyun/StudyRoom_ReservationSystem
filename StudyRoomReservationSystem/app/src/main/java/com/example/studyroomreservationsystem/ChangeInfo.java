package com.example.studyroomreservationsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

import static com.example.studyroomreservationsystem.MenuPage.userNo;

public class ChangeInfo extends AppCompatActivity {
    TextView title_tv;
    LinearLayout changePw_LL, changeEmail_LL, changePhone_LL, changeGrade_LL;
    EditText currentPw_et, changePw_et, changePwChk_et;
    EditText changeEmail_et, authCode_et;
    EditText changePhone_et, changeGrade_et;


    String title = "";
    String authCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);

        Intent intent = getIntent();
        title = intent.getStringExtra("Title");

        setting();
        // 인터넷 사용을 위한 권한 허용 ( 이메일 인증 기능 구현 위해 추가 2020-08-02 )
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        // 타이틀 텍스트 설정
        if (title.equals("비밀번호")) {
            changePw_LL.setVisibility(View.VISIBLE);
        } else if (title.equals("이메일")) {
            changeEmail_LL.setVisibility(View.VISIBLE);
        } else if (title.equals("휴대전화")) {
            changePhone_LL.setVisibility(View.VISIBLE);
        } else if (title.equals("학년")) {
            changeGrade_LL.setVisibility(View.VISIBLE);
        }


    }

    public void setting() {
        title_tv = findViewById(R.id.title_tv);
        changePw_LL = findViewById(R.id.changePw_LL);
        changeEmail_LL = findViewById(R.id.changeEmail_LL);
        changePhone_LL = findViewById(R.id.changePhone_LL);
        changeGrade_LL = findViewById(R.id.changeGrade_LL);

        currentPw_et = findViewById(R.id.currentPw_et);
        changePw_et = findViewById(R.id.changePw_et);
        changePwChk_et = findViewById(R.id.changePwChk_et);
        changePhone_et = findViewById(R.id.changePhone_et);
        changeGrade_et = findViewById(R.id.changeGrade_et);

        changeEmail_et = findViewById(R.id.changeEmail_et);
        authCode_et = findViewById(R.id.authCode_et);

        title_tv.setText(title + " 수정");


    }

    public void onConfirmBT(View view) throws ExecutionException, InterruptedException {
        switch (view.getId()) {
            case R.id.changeInfo_back_bt:
                finish();
                break;
            case R.id.changePw_bt:
                try {
                    String result = new Task().execute("Get_Password", userNo).get();
                    if (!result.equals("Get_Password_FAIL")) {
                        if (currentPw_et.getText().toString().equals(result)) {
                            if (changePw_et.getText().toString().equals(changePwChk_et.getText().toString())) {
                                String result2 = new Task().execute("Update_Password", userNo, changePw_et.getText().toString()).get();
                                if (result2.equals("Update_Password_OK")) {
                                    Toast.makeText(this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            } else {
                                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "현재 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.changeEmail_bt:
                if (authCode_et.getText().toString().equals(authCode)) {
                    String result = new Task().execute("Update_Email", userNo, changeEmail_et.getText().toString()).get();
                    if (result.equals("Update_Email_OK")) {
                        Toast.makeText(this, "이메일 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        setResult(1);
                        finish();
                    }
                } else {
                    Toast.makeText(this, "인증번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.authEmail_bt:
                try {
                    GMailSender gMailSender = new GMailSender("aservmz@gmail.com", "ehruddbs4$");
                    //GMailSender.sendMail(제목, 본문내용, 받는사람);
                    authCode = gMailSender.getEmailCode();
                    gMailSender.sendMail("이메일 인증요청", "인증번호 : " + authCode, changeEmail_et.getText().toString());
                    Toast.makeText(getApplicationContext(), "이메일을 성공적으로 보냈습니다.", Toast.LENGTH_SHORT).show();
                } catch (SendFailedException e) {
                    Toast.makeText(getApplicationContext(), "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                } catch (MessagingException e) {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.changePhone_bt:
                String result = new Task().execute("Update_Phone", userNo, changePhone_et.getText().toString()).get();
                if(result.equals("Update_Phone_OK")){
                    Toast.makeText(this, "휴대폰번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    setResult(1);
                    finish();
                }
                break;
            case R.id.changeGrade_bt:
                String result2 = new Task().execute("Update_Grade", userNo, changeGrade_et.getText().toString()).get();
                if(result2.equals("Update_Grade_OK")){
                    Toast.makeText(this, "학년이 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    setResult(1);
                    finish();
                }
                break;
        }
    }
}

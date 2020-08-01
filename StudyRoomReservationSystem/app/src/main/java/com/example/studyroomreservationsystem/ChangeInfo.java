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
    LinearLayout changePw_LL;
    LinearLayout changeEmail_LL;
    LinearLayout changePhone_LL;
    LinearLayout changeGrade_LL;
    EditText currentPw_et, changePw_et, changePwChk_et;
    EditText currentEmail_et, authNum_et;

    String title = "";

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

        if (title.equals("비밀번호")) {
            changePw_LL.setVisibility(View.VISIBLE);
        }else if(title.equals("이메일")){
            changeEmail_LL.setVisibility(View.VISIBLE);
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

        currentEmail_et = findViewById(R.id.currentEmail_et);
        authNum_et = findViewById(R.id.authNum_et);

        title_tv.setText(title + " 수정");



    }

    public void onConfirmBT(View view) {
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

                break;
            case R.id.authEmail_bt:
                try {
                    GMailSender gMailSender = new GMailSender("aservmz@gmail.com", "ehruddbs4$");
                    //GMailSender.sendMail(제목, 본문내용, 받는사람);
                    gMailSender.sendMail("이메일 인증요청", "인증번호 : ", currentEmail_et.getText().toString());
                    Toast.makeText(getApplicationContext(), "이메일을 성공적으로 보냈습니다.", Toast.LENGTH_SHORT).show();
                } catch (SendFailedException e) {
                    Toast.makeText(getApplicationContext(), "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                } catch (MessagingException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}

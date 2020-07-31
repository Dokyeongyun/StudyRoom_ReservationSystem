package com.example.studyroomreservationsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class ReservationStatus_PopUp extends Activity {

    String userNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_reservation_status__pop_up);

        TextView timeInfo_tv = findViewById(R.id.timeInfo_tv);

        Intent intent = getIntent();
        userNo = intent.getStringExtra("userNo");

        try {
            String result = new Task().execute("Get_UserInfo", userNo).get();
            // 이름, 학번, 이메일, 휴대폰번호, 학년 순서

            if (!result.equals("Get_UserInfo_FAIL")) {
                String[] resultSplit = result.split(" ");

                String text = " · 이용자명: " + resultSplit[1] + "\n"
                        + " · 학번: " + resultSplit[2] + "\n"
                        + " · 이메일: " + resultSplit[3] + "\n"
                        + " · 전화번호: " + resultSplit[4] + "\n"
                        + " · 학년: " + resultSplit[5] + "\n";
                timeInfo_tv.setText(text);
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onClose(View view) {
        finish();
    }

    public void onChatBT(View view) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("userNo",userNo);
        startActivity(intent);
    }
}

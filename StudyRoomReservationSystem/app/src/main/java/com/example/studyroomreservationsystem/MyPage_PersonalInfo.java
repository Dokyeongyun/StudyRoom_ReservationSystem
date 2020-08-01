package com.example.studyroomreservationsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class MyPage_PersonalInfo extends AppCompatActivity {

    public static int NUMBER_OF_INFO = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page__personal_info);

        String name = "", stuNo = "", email = "", phone = "", grade = "", id = "";
        // 개인정보 변경 액티비티
        String[] resultSplit = new String[8];
        Intent intent = getIntent();
        String userNo = intent.getStringExtra("userNo");
        try {
            String result = new Task().execute("Get_UserInfo", userNo).get();

            if (!result.equals("Get_UserInfo_FAIL")) {
                resultSplit = result.split(" ");
                id = resultSplit[0];
                name = resultSplit[1];
                stuNo = resultSplit[2];
                email = resultSplit[3];
                phone = resultSplit[4];
                grade = resultSplit[5];
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        LinearLayout personalInfo_LL = findViewById(R.id.personalInfo_LL);

        String[] infoTitleArr = new String[]{"회원번호", "ID", "이름", "비밀번호", "학번", "이메일", "휴대전화", "학년"};
        String[] infoValueArr = new String[]{userNo, id, name, "**********", stuNo, email, phone, grade};
        for (int i = 0; i < NUMBER_OF_INFO; i++) {

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 60);
            params.setMargins(0, 50, 0, 10);

            LinearLayout infoTitle_LL = new LinearLayout(this);
            infoTitle_LL.setLayoutParams(params);

            TextView infoTitle_tv = new TextView(this);
            infoTitle_tv.setText(infoTitleArr[i]);
            infoTitle_tv.setTextAppearance(R.style.personalInfo_title);
            infoTitle_tv.setPadding(30, 0, 0, 0);
            infoTitle_LL.addView(infoTitle_tv);


            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 130);
            params2.gravity = Gravity.CENTER;

            LinearLayout infoValue_LL = new LinearLayout(this);
            infoValue_LL.setBackgroundResource(R.drawable.border);
            infoValue_LL.setLayoutParams(params2);

            TextView infoValue_tv = new TextView(this);
            infoValue_tv.setLayoutParams(new LinearLayout.LayoutParams(300, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            infoValue_tv.setText(infoValueArr[i]);
            infoValue_tv.setPadding(50, 0, 0, 0);
            infoValue_tv.setTextAppearance(R.style.personalInfo_value);
            infoValue_tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            infoValue_LL.addView(infoValue_tv);

            if (i == 3 || i == 5 || i == 6 || i == 7) {
                Button infoChange_bt = new Button(this);
                infoChange_bt.setText("수정");
                infoChange_bt.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                infoChange_bt.setPadding(0, 0, 20, 0);
                int finalI = i;
                infoChange_bt.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), ChangeInfo.class);
                        intent.putExtra("Title", infoTitleArr[finalI]);
                        startActivity(intent);
                    }
                });
                infoValue_LL.addView(infoChange_bt);
            }
            personalInfo_LL.addView(infoTitle_LL);
            personalInfo_LL.addView(infoValue_LL);
        }
    }
}

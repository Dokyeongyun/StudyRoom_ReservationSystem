package com.example.studyroomreservationsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;


public class MyPage_PopUp extends Activity {
    public static String resNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_page__pop_up);

        Intent intent = getIntent();

        resNo = intent.getStringExtra("resNo");
        String userNo = intent.getStringExtra("userNo");
        String resDate = intent.getStringExtra("resDate");
        String resRoom = intent.getStringExtra("resRoom");
        String resStartTime = intent.getStringExtra("resStartTime");
        String resEndTime = intent.getStringExtra("resEndTime");

        int hour = (int) ((Double.parseDouble(resStartTime) * 60) / 60);
        int minute = (int) ((Double.parseDouble(resStartTime) * 60) % 60);
        int hour2 = (int) ((Double.parseDouble(resEndTime) * 60) / 60);
        int minute2 = (int) ((Double.parseDouble(resEndTime) * 60) % 60);


        String detail = " · 예약번호: " + resNo + "\n"
                + " · 예약자명: " + userNo + "\n"
                + " · 이용예정일: " + resDate + "\n"
                + " · 이용구역: 4-332" + resRoom + "\n"
                + " · 이용시간: " + hour + "시 " + minute + "분" + " ~ " + hour2 + "시 " + minute2 + "분" + "\n"
                + " · 이용상태: 이용대기" + "\n";

        TextView detailInfo_tv = findViewById(R.id.detailInfo_tv);
        detailInfo_tv.setText(detail);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 바깥레이어 클릭시 안닫히게 하는 코드
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

    public void onClose(View view) {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 기능이 수행되는 컨텍스트상의 모든 요소를 다시그리는거야
    }

    public void cancelBT(View view) throws ExecutionException, InterruptedException {
        // DB 에서 해당 예약 데이터 삭제 후 finish()
        // 해당 예약의 키 값 => resNo
        String result = new Task().execute("Delete_Reservation", resNo).get();
        if (result.equals("Delete_OK")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("예약을 정말 취소하시겠습니까?");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 확인버튼 클릭 시 실행 메소드
                    Toast.makeText(MyPage_PopUp.this, "예약이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                    setResult(1);
                    finish();
                }
            });
            builder.setNegativeButton("취소", null);
            builder.create().show();

        } else {
            Toast.makeText(this, "예약을 취소하지 못하였습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
        }
    }
}

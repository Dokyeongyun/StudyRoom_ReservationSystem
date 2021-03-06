package com.example.studyroomreservationsystem;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;


public class Reservation_Frag extends Fragment {
    public Reservation_Frag() {
    }

    static Reservation_Frag newInstance() {
        return new Reservation_Frag();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public static String userNo;
    private Button date_bt2, startTime_bt, endTime_bt, roomB_bt, roomC_bt, reservation_bt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reservation_, container, false);

        // MenuPage 에서 전송한 Bundle 객체 수신
        Bundle bundle = getArguments();
        userNo = bundle.getString("userNo");

        // findViewById
        setting(v);

        AtomicBoolean roomB_selected = new AtomicBoolean(false);
        AtomicBoolean roomC_selected = new AtomicBoolean(false);

        Calendar Today = Calendar.getInstance();
        date_bt2.setText(Today.get(Calendar.YEAR) + "-" + (Today.get(Calendar.MONTH) + 1) + "-" + Today.get(Calendar.DAY_OF_MONTH));


        View.OnClickListener mClickListener = v1 -> {
            switch (v1.getId()) {
                case R.id.reservation_bt:
                    String resRoom = "", resStartTime = "", resEndTime = "";

                    // roomB인지 roomC인지 Check
                    if (roomB_selected.get()) {
                        resRoom = "B";
                    } else if (roomC_selected.get()) {
                        resRoom = "C";
                    }

                    // double형 숫자를 시, 분 형태로 표현
                    String[] tempTime = startTime_bt.getText().toString().split("시 ");
                    if (tempTime.length == 2) {
                        if (tempTime[1].equals("0분")) {
                            resStartTime = tempTime[0];
                        } else if (tempTime[1].equals("30분")) {
                            resStartTime = String.valueOf((Integer.parseInt(tempTime[0]) + 0.5));
                        }
                    }

                    String[] tempTime2 = endTime_bt.getText().toString().split("시 ");
                    if (tempTime2.length == 2) {
                        if (tempTime2[1].equals("0분")) {
                            resEndTime = tempTime2[0];
                        } else if (tempTime2[1].equals("30분")) {
                            resEndTime = String.valueOf((Integer.parseInt(tempTime2[0]) + 0.5));
                        }
                    }

                    // 날짜
                    String resDate = date_bt2.getText().toString();

                    if (!resDate.equals("") && !resRoom.equals("") && !resStartTime.equals("") && !resEndTime.equals("")) {
                        String[] resInfo = new String[]{resDate, resStartTime, resEndTime, resRoom};
                        confirm_info(resInfo);
                    } else {
                        Toast.makeText(getActivity(), "예약 정보를 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.startTime_bt:
                    showTime(startTime_bt);
                    break;
                case R.id.endTime_bt:
                    showTime(endTime_bt);
                    break;
                case R.id.roomB_bt:
                    roomB_bt.setTextColor(Color.parseColor("#4DCD8F"));
                    roomB_selected.set(true);
                    roomC_bt.setTextColor(Color.BLACK);
                    roomC_selected.set(false);
                    break;
                case R.id.roomC_bt:
                    roomC_bt.setTextColor(Color.parseColor("#4DCD8F"));
                    roomC_selected.set(true);
                    roomB_bt.setTextColor(Color.BLACK);
                    roomB_selected.set(false);
                    break;
            }
        };

        reservation_bt.setOnClickListener(mClickListener);
        startTime_bt.setOnClickListener(mClickListener);
        endTime_bt.setOnClickListener(mClickListener);
        roomB_bt.setOnClickListener(mClickListener);
        roomC_bt.setOnClickListener(mClickListener);

        return v;
    }

    private void setting(View v) {
        date_bt2 = v.findViewById(R.id.date_bt2);
        startTime_bt = v.findViewById(R.id.startTime_bt);
        endTime_bt = v.findViewById(R.id.endTime_bt);
        roomB_bt = v.findViewById(R.id.roomB_bt);
        roomC_bt = v.findViewById(R.id.roomC_bt);
        reservation_bt = v.findViewById(R.id.reservation_bt);
    }

    private void showTime(Button b) {
        long now = System.currentTimeMillis();
        Date cDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("hh");
        SimpleDateFormat simpleDate2 = new SimpleDateFormat("mm");
        int cHour = Integer.parseInt(simpleDate.format(cDate));
        int cMinute = Integer.parseInt(simpleDate2.format(cDate));

        CustomTimePicker timePicker = new CustomTimePicker(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                b.setText(hourOfDay + "시 " + minute + "분");
            }
        }, cHour, cMinute, false);
        timePicker.setMessage("시간입력");
        timePicker.show();
    }

    private void confirm_info(String[] message) {

        String resInfo_message = "예약일: " + message[0] + "\n"
                + "사용시작시간: " + message[1] + "\n"
                + "사용종료시간: " + message[2] + "\n"
                + "스터디룸 " + message[3] + "\n";

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(" 예약내용을 확인해주세요.");
        builder.setMessage(resInfo_message);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 확인버튼 클릭 시 실행 메소드
                try {
                    String result = new Task().execute("Insert_Reservation", message[0], message[1], message[2], message[3], userNo, null).get();

                    if (result.equals("Reservation_OK")) {
                        Toast.makeText(getActivity(), "예약이 완료되었습니다. 감사합니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "예약에 실패하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                ((MenuPage) getActivity()).replaceFragment(Mypage_Frag.newInstance());
            }
        });
        builder.setNegativeButton("취소", null);
        builder.create().show();
    }
}

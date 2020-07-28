package com.example.studyroomreservationsystem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;


public class Mypage_Frag extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Mypage_Frag() {
    }

    public static Mypage_Frag newInstance(String param1, String param2) {
        Mypage_Frag fragment = new Mypage_Frag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static Mypage_Frag newInstance() {
        return new Mypage_Frag();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private static String userNo;
    static final int REQUEST_DELETE = 101;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mypage_, container, false);
        LinearLayout myStatus_LL = v.findViewById(R.id.myStatus_LL);

/*
        나의 예약현황에 들어갈 정보
        1. 예약일 2. 이용시간 3. 룸번호 4. 이용상태(이용중, 이용대기, 이용완료 등)
        select   resDate, resStartTime, resEndTime, resRoom
        from     reservation
        where    userNo = ?
        order by resDate
        이 때, 오늘 날짜 이후의 예약만 출력하도록 함
*/



        Bundle bundle = getArguments();
        userNo = bundle.getString("userNo");

        // 오늘 날짜 계산
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        String today_s = simpleDate.format(new Date(System.currentTimeMillis()));

        try {
            String result = new Task().execute("Request_myStatus", userNo).get();

            // 예약이 없다면 "NOTHING" 을 리턴받고, 예약이 없다는 TextView 와 ImageView 출력 + 예약하러가기 Button 출력
            // 예약이 있다면 resNo, resDate, resStartTime, resEndTime, resRoom 순서로 데이터를 받아온 후 분리하여 저장
            if (result.equals("NOTHING")) {
                ImageView nothing_image = new ImageView(getActivity());
                nothing_image.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
                nothing_image.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
                nothing_image.setPadding(0, 20, 0, 30);

                TextView nothing = new TextView(getActivity());
                nothing.setText("예약 기록이 없습니다.");
                nothing.setTextAppearance(R.style.myStatus_nothing);
                nothing.setGravity(Gravity.CENTER);
                nothing.setPadding(0, 0, 0, 40);

                Button add_bt = new Button(getActivity());
                add_bt.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150));
                add_bt.setText("예약하러가기");
                add_bt.setTextAppearance(R.style.myStatus_nothing);
                add_bt.setBackgroundColor(Color.parseColor("#4DCD8F"));
                add_bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MenuPage) getActivity()).replaceFragment(Reservation_Frag.newInstance());
                    }
                });

                myStatus_LL.addView(nothing_image);
                myStatus_LL.addView(nothing);
                myStatus_LL.addView(add_bt);
            } else {
                String[] resultSplit = result.split(" ");

                int dataNum = 5;
                String[] resNoArr = new String[resultSplit.length / dataNum];
                String[] resDateArr = new String[resultSplit.length / dataNum];
                String[] resStartTimeArr = new String[resultSplit.length / dataNum];
                String[] resEndTimeArr = new String[resultSplit.length / dataNum];
                String[] resRoomArr = new String[resultSplit.length / dataNum];

                // 각 데이터 배열에 값 저장
                for (int i = 0; i < resultSplit.length; i++) {
                    if(i%dataNum == 0){
                        resNoArr[i/dataNum] = resultSplit[i];
                    }else if (i % dataNum == 1) {
                        resDateArr[i / dataNum] = resultSplit[i];
                    } else if (i % dataNum == 2) {
                        resStartTimeArr[i / dataNum] = resultSplit[i];
                    } else if (i % dataNum == 3) {
                        resEndTimeArr[i / dataNum] = resultSplit[i];
                    } else if (i % dataNum == 4) {
                        resRoomArr[i / dataNum] = resultSplit[i];
                    }
                }

                // 나의 예약 수만큼 반복해서 예약정보 출력하기
                for (int index = 0; index < resDateArr.length; index++) {
                    // 나의 예약 - 날짜 출력 코드 [ 날짜별로 한번만 출력하게 하는 코드 + 오늘 날짜는 "TODAY" 로 출력]
                    LinearLayout myStatus_dateLL = new LinearLayout(getActivity());
                    if (index == 0 || !resDateArr[index - 1].equals(resDateArr[index])) {
                        TextView border = new TextView(getActivity());
                        border.setBackgroundColor(Color.parseColor("#FF999999"));
                        border.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5));
                        myStatus_LL.addView(border);

                        TextView myStatus_dateTV = new TextView(getActivity());
                        String[] tempDate = resDateArr[index].split("-");
                        if (resDateArr[index].equals(today_s)) {
                            myStatus_dateTV.setText("Today");
                        } else {
                            myStatus_dateTV.setText(tempDate[1] + "월 " + tempDate[2] + "일");

                        }
                        myStatus_dateTV.setPadding(0, 20, 0, 0);
                        myStatus_dateTV.setTextAppearance(R.style.myStatus_dateStyle);
                        myStatus_dateLL.addView(myStatus_dateTV);
                    }
                    // ------------------------------------------------------------------------------------

                    // 나의 예약 - LinearLayout 안에 스터디룸 번호 출력하는 TextView 삽입 코드 (upperLL)
                    LinearLayout myStatus_upperLL = new LinearLayout(getActivity());
                    myStatus_upperLL.setPadding(10, 20, 0, 0);
                    TextView myStatus_resRoomTV = new TextView(getActivity());
                    myStatus_resRoomTV.setText("4-332" + resRoomArr[index]);
                    myStatus_resRoomTV.setTextAppearance(R.style.myStatus_roomTVStyle);
                    myStatus_resRoomTV.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                    myStatus_upperLL.addView(myStatus_resRoomTV);
                    // ------------------------------------------------------------------------------------

                    // 나의 예약 - LinearLayout 안에 이용상태, 이용시간 등 사용정보 출력하는 TextView 삽입 코드 (lowerLL)
                    LinearLayout myStatus_lowerLL = new LinearLayout(getActivity());
                    myStatus_lowerLL.setOrientation(LinearLayout.VERTICAL);
                    TextView myStatus_usingStatusTV = new TextView(getActivity());
                    myStatus_usingStatusTV.setText(" · 이용상태: 이용대기");
                    myStatus_usingStatusTV.setTextAppearance(R.style.myStatus_usingStatusStyle);
                    myStatus_usingStatusTV.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    myStatus_lowerLL.addView(myStatus_usingStatusTV);

                    TextView myStatus_resTimeTV = new TextView(getActivity());
                    // DB에서 가져온 Double 형태의 값을 시간, 분 형태로 표현하기 위한 코드
                    int hour = (int) ((Double.parseDouble(resStartTimeArr[index]) * 60) / 60);
                    int minute = (int) ((Double.parseDouble(resStartTimeArr[index]) * 60) % 60);
                    int hour2 = (int) ((Double.parseDouble(resEndTimeArr[index]) * 60) / 60);
                    int minute2 = (int) ((Double.parseDouble(resEndTimeArr[index]) * 60) % 60);
                    myStatus_resTimeTV.setText(" · 이용시간: " + hour + "시 " + minute + "분" + " ~ " + hour2 + "시 " + minute2 + "분");
                    myStatus_resTimeTV.setTextAppearance(R.style.myStatus_usingStatusStyle);
                    myStatus_resTimeTV.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    myStatus_lowerLL.addView(myStatus_resTimeTV);
                    // ------------------------------------------------------------------------------------

                    LinearLayout myStatus_resInfoLL = new LinearLayout(getActivity());
                    myStatus_resInfoLL.setOrientation(LinearLayout.VERTICAL);
                    int finalIndex = index;
                    Intent intent = new Intent(getActivity(), MyPage_PopUp.class);
                    myStatus_resInfoLL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 해당 예약에 대한 상세정보 + 예약취소 + 예약상태 변경 등
                            intent.putExtra("userNo", userNo);
                            intent.putExtra("resNo", resNoArr[finalIndex]);
                            intent.putExtra("resDate", resDateArr[finalIndex]);
                            intent.putExtra("resRoom", resRoomArr[finalIndex]);
                            intent.putExtra("resStartTime", resStartTimeArr[finalIndex]);
                            intent.putExtra("resEndTime", resEndTimeArr[finalIndex]);

                            startActivityForResult(intent, REQUEST_DELETE);
                        }
                    });

                    myStatus_resInfoLL.addView(myStatus_upperLL);
                    myStatus_resInfoLL.addView(myStatus_lowerLL);

                    myStatus_LL.addView(myStatus_dateLL);
                    myStatus_LL.addView(myStatus_resInfoLL);
                }
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_DELETE) {
            if (resultCode == 1) {
                // 삭제된 예약을 반영하기 위해 프래그먼트 다시 불러오기
                ((MenuPage) getActivity()).replaceFragment(Mypage_Frag.newInstance());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}

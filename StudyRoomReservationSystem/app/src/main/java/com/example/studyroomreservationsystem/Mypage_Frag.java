package com.example.studyroomreservationsystem;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

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

        try {
            String result = new Task().execute("Request_myStatus", userNo).get();

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

                String[] resDateArr = new String[resultSplit.length / 4];
                String[] resStartTimeArr = new String[resultSplit.length / 4];
                String[] resEndTimeArr = new String[resultSplit.length / 4];
                String[] resRoomArr = new String[resultSplit.length / 4];

                for (int i = 0; i < resultSplit.length; i++) {
                    if (i % 4 == 0) {
                        resDateArr[i / 4] = resultSplit[i];
                    } else if (i % 4 == 1) {
                        resStartTimeArr[i / 4] = resultSplit[i];
                    } else if (i % 4 == 2) {
                        resEndTimeArr[i / 4] = resultSplit[i];
                    } else {
                        resRoomArr[i / 4] = resultSplit[i];
                    }

                }

                long now = System.currentTimeMillis();
                Date Today = new Date(now);
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
                String today_s = simpleDate.format(Today);

                for (int index = 0; index < resDateArr.length; index++) {
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

                    LinearLayout myStatus_upperLL = new LinearLayout(getActivity());
                    myStatus_upperLL.setPadding(10, 20, 0, 0);

                    TextView myStatus_resRoomTV = new TextView(getActivity());
                    myStatus_resRoomTV.setText("4-332" + resRoomArr[index]);
                    myStatus_resRoomTV.setTextAppearance(R.style.myStatus_roomTVStyle);
                    myStatus_resRoomTV.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

                    myStatus_upperLL.addView(myStatus_resRoomTV);

                    LinearLayout myStatus_lowerLL = new LinearLayout(getActivity());
                    myStatus_lowerLL.setOrientation(LinearLayout.VERTICAL);

                    TextView myStatus_usingStatusTV = new TextView(getActivity());
                    myStatus_usingStatusTV.setText(" · 이용상태: 이용대기");
                    myStatus_usingStatusTV.setTextAppearance(R.style.myStatus_usingStatusStyle);
                    myStatus_usingStatusTV.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    myStatus_lowerLL.addView(myStatus_usingStatusTV);

                    TextView myStatus_resTimeTV = new TextView(getActivity());
                    int hour = (int) ((Double.parseDouble(resStartTimeArr[index]) * 60) / 60);
                    int minute = (int) ((Double.parseDouble(resStartTimeArr[index]) * 60) % 60);

                    int hour2 = (int) ((Double.parseDouble(resEndTimeArr[index]) * 60) / 60);
                    int minute2 = (int) ((Double.parseDouble(resEndTimeArr[index]) * 60) % 60);
                    myStatus_resTimeTV.setText(" · 이용시간: " + hour + "시 " + minute + "분" + " ~ " + hour2 + "시 " + minute2 + "분");
                    myStatus_resTimeTV.setTextAppearance(R.style.myStatus_usingStatusStyle);
                    myStatus_resTimeTV.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    myStatus_lowerLL.addView(myStatus_resTimeTV);

                    myStatus_LL.addView(myStatus_dateLL);
                    myStatus_LL.addView(myStatus_upperLL);
                    myStatus_LL.addView(myStatus_lowerLL);
                }
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return v;
    }
}

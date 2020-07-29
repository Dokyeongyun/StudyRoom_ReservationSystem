package com.example.studyroomreservationsystem;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class ReservationStatus_Frag extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ReservationStatus_Frag() {
    }

    public static ReservationStatus_Frag newInstance(String param1, String param2) {
        ReservationStatus_Frag fragment = new ReservationStatus_Frag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static ReservationStatus_Frag newInstance() {
        return new ReservationStatus_Frag();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    public static final int REQUEST_TIMEINFO = 102;
    private String tempDate = "";
    private TextView b, c;
    private LinearLayout roomBStatus_LL, roomCStatus_LL, refresh_bt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reservation_status_, container, false);

        Button date_bt2 = v.findViewById(R.id.date_bt2);
        ImageButton search_bt = v.findViewById(R.id.search_bt);
        roomBStatus_LL = v.findViewById(R.id.roomBStatus_LL);
        roomCStatus_LL = v.findViewById(R.id.roomCStatus_LL);
        refresh_bt = v.findViewById(R.id.refresh_bt);
        b = v.findViewById(R.id.textView12);
        c = v.findViewById(R.id.textView13);
        b.setVisibility(View.INVISIBLE);
        c.setVisibility(View.INVISIBLE);
        refresh_bt.setVisibility(View.INVISIBLE);
        LinearLayout showStatusLL = v.findViewById(R.id.showStatusLL);

        View.OnClickListener mClickListener = v1 -> {
            switch (v1.getId()) {
                case R.id.date_bt2:
                    showDate(date_bt2);
                    break;
                case R.id.search_bt:
                    // DB 에서 date_bt2 의 날짜의 예약현황을 검색하여 출력,
                    String searchDate = date_bt2.getText().toString();
                    tempDate = searchDate;
                    getResStatus(searchDate);

                    Animation animation = new AlphaAnimation(0, 1);
                    animation.setDuration(700);
                    showStatusLL.setVisibility(View.VISIBLE);
                    showStatusLL.setAnimation(animation);
                    break;
                case R.id.refresh_bt:
                    getResStatus(tempDate);
                    Toast.makeText(getActivity(), "새로고침 되었습니다!", Toast.LENGTH_SHORT).show();
                    break;
            }
        };

        date_bt2.setOnClickListener(mClickListener);
        search_bt.setOnClickListener(mClickListener);
        refresh_bt.setOnClickListener(mClickListener);

        Calendar Today = Calendar.getInstance();
        date_bt2.setText(Today.get(Calendar.YEAR) + "-" + (Today.get(Calendar.MONTH) + 1) + "-" + Today.get(Calendar.DAY_OF_MONTH));
        //search_bt.callOnClick();

        return v;
    }

    private void showDate(Button b) {
        Calendar SelectDate = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (view, year, month, dayOfMonth) -> {
            b.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
        }, SelectDate.get(Calendar.YEAR), SelectDate.get(Calendar.MONTH), SelectDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setMessage("날짜입력");

        // 선택가능 날짜 제한, 최소 = 오늘 / 최대 = 3일 후
        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();
        minDate.set(SelectDate.get(Calendar.YEAR), SelectDate.get(Calendar.MONTH), SelectDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(minDate.getTime().getTime());
        maxDate.set(SelectDate.get(Calendar.YEAR), SelectDate.get(Calendar.MONTH), SelectDate.get(Calendar.DAY_OF_MONTH) + 3);
        datePickerDialog.getDatePicker().setMaxDate(maxDate.getTime().getTime());

        datePickerDialog.show();
    }

    public void showStatus(LinearLayout statusTableLL, double[] startTimeArr, int[] userNoArr) {

        LinearLayout AMRow = new LinearLayout(getActivity());
        LinearLayout PMRow = new LinearLayout(getActivity());

        Button[] btn = new Button[24];
        Button[] btn2 = new Button[24];

        AMRow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        PMRow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView AM_text = new TextView(getActivity());
        TextView PM_text = new TextView(getActivity());
        AM_text.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100, 1));
        PM_text.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100, 1));
        AM_text.setText("AM");
        PM_text.setText("PM");
        AM_text.setTextColor(Color.RED);
        PM_text.setTextColor(Color.BLUE);
        AM_text.setTextSize(6);
        PM_text.setTextSize(6);

        AMRow.addView(AM_text);
        PMRow.addView(PM_text);

        for (int j = 0; j < 24; j++) {
            btn[j] = new Button(getActivity());
            btn[j].setId(j);
            btn[j].setText("" + (j / 2));
            if (startTimeArr[j] == 1.0) {
                btn[j].setBackgroundColor(Color.parseColor("#8CFF262D"));
            } else {
                btn[j].setBackgroundColor(Color.parseColor("#00FFFFFF"));
            }
            btn[j].setTextSize(4);
            btn[j].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100, 1));
            AMRow.addView(btn[j]);

            btn2[j] = new Button(getActivity());
            btn2[j].setId(j);
            btn2[j].setText("" + ((j / 2) + 12));
            if (startTimeArr[j + 24] == 1.0) {
                btn2[j].setBackgroundColor(Color.parseColor("#8CFF262D"));
            } else {
                btn2[j].setBackgroundColor(Color.parseColor("#00FFFFFF"));
            }
            btn2[j].setTextSize(4);
            btn2[j].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100, 1));
            PMRow.addView(btn2[j]);

            int finalJ = j;
            btn[j].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userNoArr[finalJ] != 0) {
                        Intent intent = new Intent(getActivity(), ReservationStatus_PopUp.class);
                        intent.putExtra("userNo", String.valueOf(userNoArr[finalJ]));
                        startActivityForResult(intent, REQUEST_TIMEINFO);
                    }
                }
            });
            btn2[j].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userNoArr[finalJ + 24] != 0) {
                        Intent intent = new Intent(getActivity(), ReservationStatus_PopUp.class);
                        intent.putExtra("userNo", String.valueOf(userNoArr[finalJ + 24]));
                        startActivityForResult(intent, REQUEST_TIMEINFO);
                    }
                }
            });
        }
        statusTableLL.addView(AMRow);
        statusTableLL.addView(PMRow);
    }

    private void getResStatus(String searchDate) {
        if (!searchDate.equals("")) {
            roomBStatus_LL.removeAllViews();
            roomCStatus_LL.removeAllViews();

            double[] startTimeArr_B = new double[48];
            double[] startTimeArr_C = new double[48];
            int[] userNoArr_B = new int[48];
            int[] userNoArr_C = new int[48];

            try {
                String result = new Task().execute("SearchDate", searchDate).get();
                // result 에는
                // 스터디룸 번호, 시작시간, 종료시간, 사용자번호 순으로 저장되어있음.
                // 이를 구분하여 따로 데이터화.

                String[] resultSplit = result.split(" ");
                for (int i = 0; i < resultSplit.length; i++) {
                    if (i % 4 == 0) {
                        if (resultSplit[i].equals("B")) {
                            int s = (int) (Double.parseDouble(resultSplit[i + 1]) / 0.5);
                            int e = (int) (Double.parseDouble(resultSplit[i + 2]) / 0.5);
                            for (int k = s; k < e; k++) {
                                startTimeArr_B[k] = 1;
                                userNoArr_B[k] = Integer.parseInt(resultSplit[i + 3]);
                            }
                        } else if (resultSplit[i].equals("C")) {
                            int s = (int) (Double.parseDouble(resultSplit[i + 1]) / 0.5);
                            int e = (int) (Double.parseDouble(resultSplit[i + 2]) / 0.5);
                            for (int k = s; k < e; k++) {
                                startTimeArr_C[k] = 1;
                                userNoArr_C[k] = Integer.parseInt(resultSplit[i + 3]);
                            }
                        }
                    }
                }

                roomBStatus_LL.addView(b);
                b.setVisibility(View.VISIBLE);
                showStatus(roomBStatus_LL, startTimeArr_B, userNoArr_B);

                roomCStatus_LL.addView(c);
                c.setVisibility(View.VISIBLE);
                showStatus(roomCStatus_LL, startTimeArr_C, userNoArr_C);

                refresh_bt.setVisibility(View.VISIBLE);

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(getActivity(), "날짜를 선택해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

}

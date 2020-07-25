package com.example.studyroomreservationsystem;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

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

    double[] startTimeArr_B = new double[48];
    double[] endTimeArr_B = new double[48];

    double[] startTimeArr_C = new double[48];
    double[] endTimeArr_C = new double[48];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reservation_status_, container, false);

        Button date_bt2 = v.findViewById(R.id.date_bt2);
        ImageButton search_bt = v.findViewById(R.id.search_bt);

        TextView show = v.findViewById(R.id.show);
        View.OnClickListener mClickListener = v1 -> {
            switch (v1.getId()) {
                case R.id.date_bt2:
                    showDate(date_bt2);
                    break;
                case R.id.search_bt:
                    // DB 에서 date_bt2 의 날짜의 예약현황을 검색하여 출력,
                    String searchDate = date_bt2.getText().toString();

                    try {
                        String result = new Task().execute("SearchDate", searchDate).get();
                        // result 에는
                        // 시작시간, 종료시간, 스터디룸 번호 순으로 저장되어있음.
                        // 이를 구분하여 따로 데이터화.

                        String[] resultSplit = result.split(" ");
                        String showText = "";
                        for (int i = 0; i < resultSplit.length; i++) {
                            if (i % 3 == 0) {
                                if (resultSplit[i].equals("B")) {
                                    startTimeArr_B[(int) (Double.parseDouble(resultSplit[i + 1]) / 0.5)] = 1;
                                    endTimeArr_B[(int) (Double.parseDouble(resultSplit[i + 2]) / 0.5)] = 1;
                                } else if (resultSplit[i].equals("C")) {
                                    startTimeArr_C[(int) (Double.parseDouble(resultSplit[i + 1]) / 0.5)] = 1;
                                    endTimeArr_C[(int) (Double.parseDouble(resultSplit[i + 2]) / 0.5)] = 1;
                                }
                            }
                        }

                        for (int i = 0; i < startTimeArr_B.length; i++) {
                            showText += startTimeArr_B[i] + " ";
                        }
                        showText += "Room B 시작시간 배열 \n";

                        for (int i = 0; i < startTimeArr_C.length; i++) {
                            showText += startTimeArr_C[i] + " ";
                        }
                        showText += "Room C 시작시간 배열 \n";

                        show.setText(showText);

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        };

        date_bt2.setOnClickListener(mClickListener);
        search_bt.setOnClickListener(mClickListener);


        int numOfRoom = 2;
        LinearLayout statusTableLL = v.findViewById(R.id.statusTableLL);

        TableRow[] AMRow = new TableRow[numOfRoom];
        TableRow[] PMRow = new TableRow[numOfRoom];

        Button[] btn = new Button[24];
        Button[] btn2 = new Button[24];
        for (int i = 0; i < numOfRoom; i++) {
            AMRow[i] = new TableRow(getActivity());
            PMRow[i] = new TableRow(getActivity());
            AMRow[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 30));
            PMRow[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 30));
            for (int j = 0; j < 24; j++) {
                btn[j] = new Button(getActivity());
                btn[j].setId(j);
                btn[j].setText(""+j);
                btn[j].setBackgroundColor(Color.parseColor("#00FFFFFF"));
                btn[j].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                AMRow[i].addView(btn[j]);

                btn2[j] = new Button(getActivity());
                btn2[j].setId(j);
                btn2[j].setText(""+j);
                btn2[j].setBackgroundColor(Color.parseColor("#00FFFFFF"));
                btn2[j].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                PMRow[i].addView(btn2[j]);
            }
            statusTableLL.addView(AMRow[i]);
            statusTableLL.addView(PMRow[i]);
        }

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

}

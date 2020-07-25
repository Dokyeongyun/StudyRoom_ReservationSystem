package com.example.studyroomreservationsystem;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;


public class Reservation_Frag extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Reservation_Frag() {
    }

    public static Reservation_Frag newInstance(String param1, String param2) {
        Reservation_Frag fragment = new Reservation_Frag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static Reservation_Frag newInstance() {
        return new Reservation_Frag();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reservation_, container, false);

        Button date_bt = v.findViewById(R.id.date_bt);
        Button date_bt2 = v.findViewById(R.id.date_bt2);
        Button startTime_bt = v.findViewById(R.id.startTime_bt);
        Button endTime_bt = v.findViewById(R.id.endTime_bt);
        Button roomB_bt = v.findViewById(R.id.roomB_bt);
        Button roomC_bt = v.findViewById(R.id.roomC_bt);
        Button reservation_bt = v.findViewById(R.id.reservation_bt);
        LinearLayout Status_LL = v.findViewById(R.id.Status_LL);

        AtomicBoolean roomB_selected = new AtomicBoolean(false);
        AtomicBoolean roomC_selected = new AtomicBoolean(false);

        View.OnClickListener mClickListener = v1 -> {
            switch (v1.getId()) {
                case R.id.date_bt:
                    showDate(date_bt);
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

                    Status_LL.setVisibility(View.VISIBLE);
                    break;
                case R.id.roomC_bt:
                    roomC_bt.setTextColor(Color.parseColor("#4DCD8F"));
                    roomC_selected.set(true);
                    roomB_bt.setTextColor(Color.BLACK);
                    roomB_selected.set(false);

                    Status_LL.setVisibility(View.VISIBLE);
                    break;
                case R.id.reservation_bt:
                    break;

            }
        };

        date_bt.setOnClickListener(mClickListener);
        startTime_bt.setOnClickListener(mClickListener);
        endTime_bt.setOnClickListener(mClickListener);
        roomB_bt.setOnClickListener(mClickListener);
        roomC_bt.setOnClickListener(mClickListener);
        reservation_bt.setOnClickListener(mClickListener);

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

    private void showTime(Button v) {
        long now = System.currentTimeMillis();
        Date cDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("hh");
        SimpleDateFormat simpleDate2 = new SimpleDateFormat("mm");
        int cHour = Integer.parseInt(simpleDate.format(cDate));
        int cMinute = Integer.parseInt(simpleDate2.format(cDate));

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), (view, hourOfDay, minute) -> {
            v.setText(hourOfDay + "시 " + minute + "분");
        }, cHour, cMinute, false);
        timePickerDialog.setMessage("시간입력");
        timePickerDialog.show();
    }
}

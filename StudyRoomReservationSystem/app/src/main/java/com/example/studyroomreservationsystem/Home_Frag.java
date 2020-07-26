package com.example.studyroomreservationsystem;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import static com.example.studyroomreservationsystem.MenuPage.userNo;

public class Home_Frag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Home_Frag() {
    }

    // TODO: Rename and change types and number of parameters
    public static Home_Frag newInstance(String param1, String param2) {
        Home_Frag fragment = new Home_Frag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static Home_Frag newInstance() {
        return new Home_Frag();
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
        View v = inflater.inflate(R.layout.fragment_home_, container, false);

        Button resFrag_bt = v.findViewById(R.id.resFrag_bt);
        Button statusFrag_bt = v.findViewById(R.id.statusFrag_bt);
        Button myPageFrag_bt = v.findViewById(R.id.myPageFrag_bt);
        Button precautionFrag_bt = v.findViewById(R.id.precautionFrag_bt);

        View.OnClickListener mClickListener = v1 -> {
            switch (v1.getId()) {
                case R.id.resFrag_bt:
                    ((MenuPage) getActivity()).replaceFragment(Reservation_Frag.newInstance());
                    break;
                case R.id.statusFrag_bt:
                    ((MenuPage) getActivity()).replaceFragment(ReservationStatus_Frag.newInstance());
                    break;
                case R.id.myPageFrag_bt:
                    ((MenuPage) getActivity()).replaceFragment(Mypage_Frag.newInstance());
                    break;
                case R.id.precautionFrag_bt:
                    ((MenuPage) getActivity()).replaceFragment(Precautions_Frag.newInstance());
                    break;
            }
        };

        resFrag_bt.setOnClickListener(mClickListener);
        statusFrag_bt.setOnClickListener(mClickListener);
        myPageFrag_bt.setOnClickListener(mClickListener);
        precautionFrag_bt.setOnClickListener(mClickListener);
        return v;
    }

}

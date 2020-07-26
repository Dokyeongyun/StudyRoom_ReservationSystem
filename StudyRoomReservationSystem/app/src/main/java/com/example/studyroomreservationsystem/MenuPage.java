package com.example.studyroomreservationsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuPage extends AppCompatActivity {

    TextView menu_title_tv;
    public static String userNo;

    private FragmentManager fm = getSupportFragmentManager();
    private FragmentTransaction ft = fm.beginTransaction();
    private Reservation_Frag reservation_frag = new Reservation_Frag();
    private ReservationStatus_Frag reservationStatus_frag = new ReservationStatus_Frag();
    private Mypage_Frag mypage_frag = new Mypage_Frag();
    private Precautions_Frag precautions_frag = new Precautions_Frag();
    private Home_Frag home_frag = new Home_Frag();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menupage);

        Intent intent = getIntent();
        userNo = intent.getStringExtra("userNo");

        BottomNavigationView BNV = findViewById(R.id.bottomNavigationView);

        // 첫 화면
        //FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frameLayout, home_frag).commitAllowingStateLoss();
        menu_title_tv = findViewById(R.id.menu_title_tv);
        menu_title_tv.setText("공간정보 스터디룸 예약시스템");

        // Bottom Navigation View 의 메뉴가 선택될 때 화면 전환하는 리스너 정의
        BNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                FragmentTransaction ft = fm.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navigation_menu1:
                        ft.replace(R.id.frameLayout, reservation_frag).commitAllowingStateLoss();
                        menu_title_tv.setText("예약하기");
                        sendBundle(reservation_frag);
                        break;
                    case R.id.navigation_menu2:
                        ft.replace(R.id.frameLayout, reservationStatus_frag).commitAllowingStateLoss();
                        menu_title_tv.setText("예약현황");
                        break;
                    case R.id.navigation_menu3:
                        ft.replace(R.id.frameLayout, home_frag).commitAllowingStateLoss();
                        menu_title_tv.setText("공간정보 스터디룸 예약시스템");
                        break;
                    case R.id.navigation_menu4:
                        ft.replace(R.id.frameLayout, mypage_frag).commitAllowingStateLoss();
                        menu_title_tv.setText("마이페이지");
                        break;
                    case R.id.navigation_menu5:
                        ft.replace(R.id.frameLayout, precautions_frag).commitAllowingStateLoss();
                        menu_title_tv.setText("이용방법");
                        break;
                }
                return true;
            }
        });

    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment).commitAllowingStateLoss();

        sendBundle(fragment);
    }

    public void sendBundle(Fragment fragment) {

        // 프래그먼트로 전송할 번들 객체 생성 및 데이터 삽입
        Bundle bundle = new Bundle();
        bundle.putString("userNo", userNo);

        // fragment 로 bundle 전송
        fragment.setArguments(bundle);
    }


}

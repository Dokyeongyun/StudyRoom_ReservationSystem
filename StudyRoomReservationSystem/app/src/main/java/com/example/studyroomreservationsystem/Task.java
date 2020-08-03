package com.example.studyroomreservationsystem;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Task extends AsyncTask<String, Void, String> {
    //public static String ip = "172.22.229.37"; //자신의 IP번호
    String sendMsg, receiveMsg;
    // String serverip = "http://"+ip+"/ex/list.jsp"; // 연결할 jsp주소
    private String serverip = "http://192.168.43.128:8080/Project2/NewFile.jsp";

    Task(String sendmsg) {
        this.sendMsg = sendmsg;
    }

    public Task() {

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(String... strings) {
        try {
            String str;
            URL url = new URL(serverip);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST"); // 데이터를 POST방식으로 전송한다.
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

            if (strings[0].equals("ID_doubleCheck")) {
                sendMsg = "type=" + strings[0] + "&id=" + strings[1];
            } else if (strings[0].equals("EMAIL_doubleCheck")) {
                sendMsg = "type=" + strings[0] + "&email=" + strings[1];
            } else if (strings[0].equals("Register")) {
                sendMsg = "type=" + strings[0] + "&userNo=" + strings[1] + "&name=" + strings[2] + "&id=" + strings[3] + "&pwd=" + strings[4]
                        + "&stuno=" + strings[5] + "&email=" + strings[6] + "&phone=" + strings[7] + "&grade=" + strings[8];
            } else if (strings[0].equals("Login")) {
                sendMsg = "type=" + strings[0] + "&id=" + strings[1] + "&pwd=" + strings[2];
            } else if (strings[0].equals("SearchDate")) {
                sendMsg = "type=" + strings[0] + "&resDate=" + strings[1];
            } else if (strings[0].equals("Insert_Reservation")) {
                sendMsg = "type=" + strings[0] + "&resDate=" + strings[1] + "&resStartTime=" + strings[2] + "&resEndTime=" + strings[3]
                        + "&resRoom=" + strings[4] + "&userNo=" + strings[5] + "&resNo=" + strings[6];
            } else if (strings[0].equals("Request_myStatus")) {
                sendMsg = "type=" + strings[0] + "&userNo=" + strings[1];
            } else if (strings[0].equals("Delete_Reservation")) {
                sendMsg = "type=" + strings[0] + "&resNo=" + strings[1];
            } else if (strings[0].equals("GetUserInfo")) {
                sendMsg = "type=" + strings[0] + "&userNo=" + strings[1];
            } else if (strings[0].equals("Get_UserInfo")) {
                sendMsg = "type=" + strings[0] + "&userNo=" + strings[1];
            } else if (strings[0].equals("Get_Password")) {
                sendMsg = "type=" + strings[0] + "&userNo=" + strings[1];
            } else if (strings[0].equals("Update_Password")) {
                sendMsg = "type=" + strings[0] + "&userNo=" + strings[1] + "&pwd=" + strings[2];
            }else if(strings[0].equals("Update_Email")){
                sendMsg = "type=" + strings[0] + "&userNo=" + strings[1] + "&email=" + strings[2];
            }else if(strings[0].equals("Update_Phone")){
                sendMsg = "type=" + strings[0] + "&userNo=" + strings[1] + "&phone=" + strings[2];
            }else if(strings[0].equals("Update_Grade")){
                sendMsg = "type=" + strings[0] + "&userNo=" + strings[1] + "&grade=" + strings[2];
            }else if(strings[0].equals("Update_ProfilePhoto")){
                sendMsg = "type=" + strings[0] + "&userNo=" + strings[1] + "&profilePhoto=" + strings[2];
            }else if(strings[0].equals("Get_ProfilePhoto")){
                sendMsg = "type=" + strings[0] + "&userNo=" + strings[1];
            }


            // 보낼 데이터가 여러 개일 경우 &로 구분하여 작성

            osw.write(sendMsg);
            osw.flush();

            // jsp와의 통신이 정상적으로 이루어졌다면,
            if (conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();

                // jsp에서 보낸 값들을 수신
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
            } else {
                Log.i("통신 결과", conn.getResponseCode() + "에러");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return receiveMsg;
    }
}

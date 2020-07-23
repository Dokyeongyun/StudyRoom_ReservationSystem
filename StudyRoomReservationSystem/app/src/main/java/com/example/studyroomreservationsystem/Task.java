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
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Task extends AsyncTask<String, Void, String> {
    public static String ip ="172.22.229.37"; //자신의 IP번호
    String sendMsg, receiveMsg;
    // String serverip = "http://"+ip+"/ex/list.jsp"; // 연결할 jsp주소
    String serverip = "http://192.168.43.128:8080/Project2/NewFile.jsp";

    Task(String sendmsg){
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

            // 보낼 데이터가 여러 개일 경우 &로 구분하여 작성
         //   if(sendMsg.equals("id")){
                sendMsg = "id="+strings[0]+"&pwd="+strings[1]+"&type="+strings[2];
         //   }else if(sendMsg.equals("id")){
         //       sendMsg = "&pwd="+strings[0];
         //   }

            osw.write(sendMsg);
            osw.flush();

            // jsp와의 통신이 정상적으로 이루어졌다면,
            if(conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();

                // jsp에서 보낸 값들을 수신
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
            } else {
                Log.i("통신 결과", conn.getResponseCode()+"에러");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return receiveMsg;
    }
}

package com.example.studyroomreservationsystem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import static android.provider.MediaStore.Images;

import static android.provider.MediaStore.Images.Media;
import static com.example.studyroomreservationsystem.MenuPage.userNo;

public class MyPage_PersonalInfo extends AppCompatActivity {

    public static int NUMBER_OF_INFO = 8;
    static final int MODIFY_USERINFO = 102;
    static final int GET_GALLERY_IMAGE = 103;

    ImageButton profilePicture_bt;
    Bitmap profilePhoto;
    LinearLayout personalInfo_LL;
    String temp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page__personal_info);
        loadingActivity();
        try {
            loadingPhoto();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void loadingActivity() {
        profilePicture_bt = findViewById(R.id.profilePicture_bt);

        String name = "", stuNo = "", email = "", phone = "", grade = "", id = "";
        // 개인정보 변경 액티비티
        String[] resultSplit = new String[8];
        Intent intent = getIntent();
        String userNo = intent.getStringExtra("userNo");
        try {
            String result = new Task().execute("Get_UserInfo", userNo).get();

            if (!result.equals("Get_UserInfo_FAIL")) {
                resultSplit = result.split(" ");
                id = resultSplit[0];
                name = resultSplit[1];
                stuNo = resultSplit[2];
                email = resultSplit[3];
                phone = resultSplit[4];
                grade = resultSplit[5];
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        personalInfo_LL = findViewById(R.id.personalInfo_LL);
        personalInfo_LL.removeAllViews();

        String[] infoTitleArr = new String[]{"회원번호", "ID", "이름", "비밀번호", "학번", "이메일", "휴대전화", "학년"};
        String[] infoValueArr = new String[]{userNo, id, name, "**********", stuNo, email, phone, grade};
        for (int i = 0; i < NUMBER_OF_INFO; i++) {

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 60);
            params.setMargins(0, 50, 0, 10);

            LinearLayout infoTitle_LL = new LinearLayout(this);
            infoTitle_LL.setLayoutParams(params);

            TextView infoTitle_tv = new TextView(this);
            infoTitle_tv.setText(infoTitleArr[i]);
            infoTitle_tv.setTextAppearance(R.style.personalInfo_title);
            infoTitle_tv.setPadding(30, 0, 0, 0);
            infoTitle_LL.addView(infoTitle_tv);


            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 130);
            params2.gravity = Gravity.CENTER;

            LinearLayout infoValue_LL = new LinearLayout(this);
            infoValue_LL.setBackgroundResource(R.drawable.border);
            infoValue_LL.setLayoutParams(params2);

            TextView infoValue_tv = new TextView(this);
            infoValue_tv.setLayoutParams(new LinearLayout.LayoutParams(300, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            infoValue_tv.setText(infoValueArr[i]);
            infoValue_tv.setPadding(50, 0, 0, 0);
            infoValue_tv.setTextAppearance(R.style.personalInfo_value);
            infoValue_tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            infoValue_LL.addView(infoValue_tv);

            if (i == 3 || i == 5 || i == 6 || i == 7) {
                Button infoChange_bt = new Button(this);
                infoChange_bt.setText("수정");
                infoChange_bt.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                infoChange_bt.setPadding(0, 0, 20, 0);
                int finalI = i;
                infoChange_bt.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), ChangeInfo.class);
                        intent.putExtra("Title", infoTitleArr[finalI]);
                        startActivityForResult(intent, MODIFY_USERINFO);
                    }
                });
                infoValue_LL.addView(infoChange_bt);
            }
            personalInfo_LL.addView(infoTitle_LL);
            personalInfo_LL.addView(infoValue_LL);
        }
    }

    public void loadingPhoto() throws ExecutionException, InterruptedException {
        String result = new Task().execute("Get_ProfilePhoto", userNo).get();
        if(result.contains("Get_ProfilePhoto_FAIL")){
            Toast.makeText(this, "이미지를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
        }else{
            Bitmap getBitmap = StringToBitMap(result);
            profilePicture_bt.setImageBitmap(getBitmap);
            profilePicture_bt.setBackground(new ShapeDrawable(new OvalShape()));
            profilePicture_bt.setClipToOutline(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadingActivity();
    }

    public void ChangePicture_bt(View view) {

        Intent intent = new Intent(Intent.ACTION_PICK);
        /*
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        */
        intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, GET_GALLERY_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri selectedImageUri = data.getData();
            profilePicture_bt.setImageURI(selectedImageUri);
            profilePicture_bt.setBackground(new ShapeDrawable(new OvalShape()));
            profilePicture_bt.setClipToOutline(true);

            try {
                profilePhoto = Media.getBitmap(getContentResolver(), data.getData());
                profilePhoto = resize(profilePhoto);
/*                //    pd = new ProgressDialog(this);
                //  pd.setMessage("이미지를 저장중입니다. 잠시만 기다려주세요");
                // pd.show();*/
                BitMapToString(profilePhoto);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);    //bitmap compress
        byte[] arr = baos.toByteArray();
        String image = Base64.encodeToString(arr, Base64.DEFAULT);

        try {
            temp = URLEncoder.encode(image, "utf-8");
            String result = new Task().execute("Update_ProfilePhoto", userNo, temp).get();
            if (result.equals("Update_ProfilePhoto_OK")) {
                Toast.makeText(this, "이미지 저장이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "이미지 저장에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
        }
    }

    public static Bitmap StringToBitMap(String image){
        try{
            byte [] encodeByte=Base64.decode(image,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    private Bitmap resize(Bitmap bm) {

        Configuration config = getResources().getConfiguration();
		/*if(config.smallestScreenWidthDp>=800)
			bm = Bitmap.createScaledBitmap(bm, 400, 240, true);//이미지 크기로 인해 용량초과
		else */
        if (config.smallestScreenWidthDp >= 600)
            bm = Bitmap.createScaledBitmap(bm, 300, 300, true);
        else if (config.smallestScreenWidthDp >= 400)
            bm = Bitmap.createScaledBitmap(bm, 200, 200, true);
        else if (config.smallestScreenWidthDp >= 360)
            bm = Bitmap.createScaledBitmap(bm, 180, 180, true);
        else
            bm = Bitmap.createScaledBitmap(bm, 160, 160,true);

        return bm;

    }
}

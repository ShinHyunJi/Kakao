package com.example.a405_25.kakao.util;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

public class Phone {

    private Context ctx;            //server에서 사용
    private AppCompatActivity activity;//phone과는 연관관계
    private String phoneNum;

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Phone(Context ctx, AppCompatActivity activity) {
        this.ctx = ctx;
        this.activity = activity;
    }
    public void dial(){
                                                                //prefix
        Intent intent= new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phoneNum));                //(ctx)의 경우 activity내부에서만 움직일 수 있기 때문에 전화걸때는 외부!
    }

    public void directCall() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));

        //                                      |                          |manifests참고
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, 2);
            return;
        }
        ctx.startActivity(intent);

    }
}

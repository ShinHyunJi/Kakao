package com.example.a405_25.kakao.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

public class Email {

    private Context ctx;
    private AppCompatActivity activity;
    private String phoneNum;

    public Email(Context ctx, AppCompatActivity activity) {
        this.ctx = ctx;
        this.activity = activity;
    }




    public void sendEmail(String email){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"+email));
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL,email);
        intent.putExtra(Intent.EXTRA_SUBJECT,"Hello");
        intent.putExtra(Intent.EXTRA_TEXT,"안녕");//본문

        //ctx.startActivity(intent.createChooser(intetnt,"example"));



    }



}

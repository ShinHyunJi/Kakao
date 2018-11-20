package com.example.a405_25.kakao;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class mbr_list extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mbr_list);

        final Context ctx = mbr_list.this;
        ListView mbr_list = findViewById(R.id.mbr_list);

        findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ctx,mbr_detail.class));
            }
        });


    }
}

package com.example.a405_25.kakao;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class mbr_detail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mbr_detail);
        final Context ctx = mbr_detail.this;


        findViewById(R.id.btnList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ctx,mbr_list.class));
            }
        });

        findViewById(R.id.btnUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ctx,mbr_update.class));
            }
        });


        }


    private class DetailQuery extends Main.QueryFactory{

        SQLiteOpenHelper helper;

        public DetailQuery(Context ctx) {
            super(ctx);
            helper = new Main.SQLiteHelper(ctx);

        }
        @Override
        public SQLiteDatabase getDateabase() {
            return null;
        }

        private class ItemDetail extends DetailQuery{
            public ItemDetail(Context ctx) {
                super(ctx);
            }


        }
    }



}

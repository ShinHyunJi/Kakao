package com.example.a405_25.kakao;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class mbr_detail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mbr_detail);
        final Context ctx = mbr_detail.this;

        //들어오자마자 누른 회원의 상세목록 출력
        Intent intent = this.getIntent();
        String seq = intent.getExtras().getString("seq");


        final ItemDetail query = new ItemDetail(ctx);      //new intent가 아닌, 보낸 정보 그대로 현위치에
        query.seq = seq;

        Member m = (Member) new Main.ObjectService() {
            @Override
            public Object perfome() {
                return query.execute();
            }
        }.perfome();

        // 선택한 멤버 정보를 로그로 출력하기
        // Log.d("선택한 멤버 정보", m.toString());      //왜 error???

        final String spec = m.seq + "/" + m.addr + "/" + m.email + "/" + m.name + "/" + m.pass + "/" + m.photo + "/" + m.phone;

        //updateBtn
        findViewById(R.id.updateBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, mbr_update.class);
                intent.putExtra("spec", spec);
                startActivity(intent);
            }
        });
    }       //onCreate End

    //DB영역
    private class DetailQuery extends Main.QueryFactory {
        Main.SQLiteHelper helper;

        public DetailQuery(Context ctx) {
            super(ctx);
            helper = new Main.SQLiteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDateabase() {
            return helper.getReadableDatabase();
        }

    }

    private class ItemDetail extends DetailQuery {       //가져온DB로 Query
        String seq;

        public ItemDetail(Context ctx) {
            super(ctx);
        }

        public Member execute() {
            Log.d("execute", " 내부");
            //Member m = new Member();
            String sql = String.format(" SELECT * FROM %s " +
                            " WHERE %s LIKE '%s'", DBInfo.MBR_TABLE,
                    DBInfo.MBR_SEQ, seq);
            Log.d("sql", sql);
            Member m = null;
            Cursor c = this.getDateabase().rawQuery(sql, null);

            if (c != null) {
                if (c.moveToNext()) {       //cursor 한번긁기 한명이니까 한번만 !
                    m = new Member();
                    m.setSeq(Integer.parseInt(c.getString(c.getColumnIndex(DBInfo.MBR_SEQ))));
                    m.setName(c.getString(c.getColumnIndex(DBInfo.MBR_NAME)));
                    m.setAddr(c.getString(c.getColumnIndex(DBInfo.MBR_ADDR)));
                    m.setEmail(c.getString(c.getColumnIndex(DBInfo.MBR_EMAIL)));
                    m.setPass(c.getString(c.getColumnIndex(DBInfo.MBR_PASS)));
                    m.setPhone(c.getString(c.getColumnIndex(DBInfo.MBR_PHONE)));
                    m.setPhoto(c.getString(c.getColumnIndex(DBInfo.MBR_PHOTO)));
                    Log.d("검색된 회원은", m.getName());
                }
            } else {
                Log.d("검색된 회원은", "없음");
            }
            return m;

        }
    }




}
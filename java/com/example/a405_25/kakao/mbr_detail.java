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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class mbr_detail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mbr_detail);
        final Context ctx = mbr_detail.this;


        //들어오자마자 누른 회원의 상세목록 출력

        final Intent intent = this.getIntent();       //new intent가 아닌, 보낸 정보 그대로 현위치에
        String seq = intent.getExtras().getString("seq");

        final ItemDetail query = new ItemDetail(ctx);

        query.seq = seq;
        Member m = (Member)new Main.ObjectService(){
            @Override
            public Object perfome() {
                return query.execute();
            }
        }.perfome();

        //선택한 멤버 정보를 로그로 출력하기
//       Log.d("선택한 멤버 정보",m.toString());     //왜 error???

//        findViewById(R.id.btnDetail).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//
        final String spec = m.seq+"/"+m.addr+"/"+m.email+"/"+m.name+"/"+m.pass+"/"+m.photo+"/"+m.phone;

        //넘어간 후에 hint!
//        String[] arr = spec.split("/");
        //updateBtn
        findViewById(R.id.updateBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx,mbr_update.class);
                intent.putExtra("spec",spec);
                startActivity(intent);

            }
        });





        }       //onCreate End

    //DB
    private class DetailQuery extends Main.QueryFactory {

        SQLiteOpenHelper helper;

        public DetailQuery(Context ctx) {
            super(ctx);
            helper = new Main.SQLiteHelper(ctx);

        }

        @Override
        public SQLiteDatabase getDateabase() {

            return helper.getReadableDatabase();
        }
    }

    private class ItemDetail extends DetailQuery{       //가져온DB로 Query
        String seq;
        public ItemDetail(Context ctx) {
            super(ctx);
            }
        public Member execute(){
            //Member m = new Member();
            Member m = null;
            Cursor c = this.getDateabase().rawQuery(String.format(" SELECT * FROM %s " +
                    " WHERE %s LIKE '%s '", DBInfo.MBR_TABLE,
                    DBInfo.MBR_SEQ,seq),null);

            if(c!=null) {
                if (c.moveToNext()) {       //cursor 한번긁기 한명이니까 한번만 !
                    m = new Member();
                    m.setSeq(Integer.parseInt(c.getString(c.getColumnIndex(DBInfo.MBR_SEQ))));
                    m.setName(c.getString(c.getColumnIndex(DBInfo.MBR_NAME)));
                    m.setAddr(c.getString(c.getColumnIndex(DBInfo.MBR_ADDR)));
                    m.setEmail(c.getString(c.getColumnIndex(DBInfo.MBR_EMAIL)));
                    m.setPass(c.getString(c.getColumnIndex(DBInfo.MBR_PASS)));
                    m.setPhone(c.getString(c.getColumnIndex(DBInfo.MBR_PHONE)));
                    m.setPhoto(c.getString(c.getColumnIndex(DBInfo.MBR_PHOTO)));
                    Log.d("검색된 회원은",m.getName());
                }
            }else{
                Log.d("검색된 회원은","없음");
            }
            return m;

        }
    }


    private class MemberAdapter extends BaseAdapter {
        //DB데려오기
        Member mb;
        Context ctx;
        LayoutInflater inflater;

        public MemberAdapter(Context ctx, Member mb) {
            this.ctx = ctx;
            this.mb = mb;
            this.inflater = LayoutInflater.from(ctx);
        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View v, ViewGroup g) {
          ViewHolder holder;

          if(v == null){
              v = inflater.inflate(R.layout.mbr_detail,null);
              holder = new ViewHolder();
              //holder.photo = v.findViewById(R.id.photo);
              holder.name = v.findViewById(R.id.name);
              holder.email = v.findViewById(R.id.email);
              holder.phone = v.findViewById(R.id.phone);
              holder.addr = v.findViewById(R.id.addr);
              v.setTag(holder);

          }else {
              holder = (ViewHolder)v.getTag();
          }




            return v;
        }


    }

    static class ViewHolder{
        ImageView photo;
        TextView name, email,phone,addr;

    }

}

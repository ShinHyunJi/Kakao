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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class mbr_list extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mbr_list);

        final Context ctx = mbr_list.this;
        ListView mbr_list = findViewById(R.id.btnList);


        final ItemList query = new ItemList(ctx);

        //Main.ListService s = new Main.ListService() {

       //Main.ListService s = mbr_list.setAdapter(new MemberAdapter(ctx,s));  //ListView를 adapter에 붙이는 작업

        /*
        ArrayList<Member> s =(ArrayList<Member>)new Main.ListService() {
            // List<Member> s = (ArrayList<Member>)new Main.ListService() {

            @Override
            public List<?> perfome() {
                return query.execute();
            }
        }.perfome();
        */
       // Log.d("친구목록",s.toString());

        //mbr_list.setAdapter(new MemberAdapter(ctx,s));

         mbr_list.setAdapter(new MemberAdapter(ctx, (ArrayList<Member>)new Main.ListService(){
            public List<?> perfome() {
                return query.execute();
            }
         }.perfome()));

        findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ctx,mbr_detail.class));
            }
        });

    }   // onCreate End지점!

    //친구추가 버튼 누르기 전 리스트(DB)
    private class ListQuery extends Main.QueryFactory{
        SQLiteOpenHelper helper;                // SQLiteOpenHelper = connection 역할

        public ListQuery(Context ctx) {
            super(ctx);
            helper = new Main.SQLiteHelper(ctx);    //초기화 => main db를 현재 위치로 데려옴
        }

        public SQLiteDatabase getDateabase(){           //db가져오기
            return helper.getReadableDatabase();
        }
    }

    private class ItemList extends ListQuery{
        public ItemList(Context ctx) {      //부모에게서 위치값 받아서 넣기
            super(ctx);
        }

        public ArrayList<Member> execute(){
            ArrayList<Member> ls = new ArrayList<>();
            Cursor c = this.getDateabase().rawQuery(        //Cursor ctr+C상태
                    "SELECT * FROM MEMBER ",null
            );

            Member m =null;
            //Member m = new Member();    <=badcode

            if(c!=null) {
                while (c.moveToNext()) {     //문장 끝
                    m = new Member();       //한개만 담고 바로 list에 넘겨주고 그 위치 다시 재활용

                    //String s = c.getString(c.getColumnIndex(DBInfo.MBR_SEQ));       //s = sequence   DB는 전부 STRING으로 통일
                    //m.setSeq(Integer.parseInt(s));
                    m.setSeq(Integer.parseInt(getString(c.getColumnIndex(DBInfo.MBR_SEQ))));
                    m.setName(c.getString(c.getColumnIndex(DBInfo.MBR_NAME)));
                    m.setAddr(c.getString(c.getColumnIndex(DBInfo.MBR_ADDR)));
                    m.setEmail(c.getString(c.getColumnIndex(DBInfo.MBR_EMAIL)));
                    m.setPass(c.getString(c.getColumnIndex(DBInfo.MBR_PASS)));
                    m.setPhone(c.getString(c.getColumnIndex(DBInfo.MBR_PHONE)));
                    m.setPhoto(c.getString(c.getColumnIndex(DBInfo.MBR_PHOTO)));

                    ls.add(m);
                    Log.d("등록된 회원수는",ls.size()+"");

                }
            }else{
                Log.d("등록된 회원은","없음");
            }
            return ls;
        }

    }

    // Item 관련 Part




    //Adapter Pattern
    private class MemberAdapter extends BaseAdapter{        //AS내부에 있   ==>error? 1)const 2)imple..

        //Data 데려오기
        ArrayList<Member> ls ;
        Context ctx;
        LayoutInflater inflater;    //inflater?data압축(ListView공간차지줄임)

        public MemberAdapter(Context ctx,ArrayList<Member> ls) {
            this.ctx = ctx;
            this.ls = ls;
            this.inflater = LayoutInflater.from(ctx);       //data 압축 풀어야 할 장소에서 압축풀기?



        }

        @Override
        public int getCount() {
            return ls.size();
        }

        @Override
        public Object getItem(int i) {
            return ls.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }


        //http://ismydream.tistory.com/150
        @Override
        public View getView(int i, View v, ViewGroup g) {       //

            ViewHolder holder;

            if(v == null ){
                v = inflater.inflate(R.layout.mbr_item,null);
                holder = new ViewHolder();
                //holder.photo = v.findViewById(R.id.photo);
                holder.name = v.findViewById(R.id.name);
                holder.phone = v.findViewById(R.id.phone);
                v.setTag(holder);

            }else{
                holder = (ViewHolder)v.getTag();
            }

            holder.name.setText(ls.get(i).getName());
            holder.phone.setText(ls.get(i).getPhone());
            return v;
        }
    }


    static class ViewHolder{
        ImageView photo;
        TextView name, phone;

    }


    private class PhotoQuery extends Main.QueryFactory{     //Photo 불러오는게 db가 아니라
                                                                //select photo from id like sequence
        @Override
        public SQLiteDatabase getDateabase() {
            return null;
        }

        public PhotoQuery(Context ctx) {
            super(ctx);
        }
    }


}



















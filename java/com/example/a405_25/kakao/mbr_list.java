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
import android.widget.AdapterView;
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
        final ListView mbr_list = findViewById(R.id.btnList);


        final ItemList query = new ItemList(ctx);

        //Main.ListService s = new Main.ListService() {

       //Main.ListService s = mbr_list.setAdapter(new MemberAdapter(ctx,s));  //ListView를 adapter에 붙이는 작업


        final ArrayList<Member> mlist =(ArrayList<Member>)new Main.ListService() {              // 변수를 할당해야 하는지또는 하지 말고 바로 실행시킬지는변수의 사용횟수에 따름
            @Override
            public List<?> perfome() {
                return query.execute();
            }
        }.perfome();


       // Log.d("친구목록",s.toString());

        //mbr_list.setAdapter(new MemberAdapter(ctx,s));

         mbr_list.setAdapter(new MemberAdapter(ctx, mlist));

        //친구추가 버튼
        findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ctx,mbr_detail.class));
            }
        });

        //Detail처리(상세정보(=짐)까지출력)
        mbr_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> p, View v, int i, long l) {
                Member m = (Member)mbr_list.getItemAtPosition(i);
                Log.d("선택한 ID",m.seq+"");
                Intent intent = new Intent(ctx,mbr_detail.class);
                intent.putExtra("seq", m.seq+"" );  //map구조 key = seq           put <=> get
                startActivity(intent);

            }
        });

        //삭제처리





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
                    m.setSeq(Integer.parseInt(c.getString(c.getColumnIndex(DBInfo.MBR_SEQ))));
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

        //http://ismydream.tistory.com/150    http://theeye.pe.kr/archives/1253
        @Override
        public View getView(int i, View v, ViewGroup g) {

            ViewHolder holder;      //ViewHolder란, 이름 그대로 뷰들을 홀더에 꼽아놓듯이 보관하는 객체
                                   //각각의 Row를 그려낼 때 그 안의 위젯들의 속성을 변경하기 위해 findViewById를 호출하는데 이것의 비용이 큰것을 줄이기 위해 사용

            if(v == null ){        // 캐시된 뷰가 없을 경우 새로 생성하고 뷰홀더를 생성한다
                v = inflater.inflate(R.layout.mbr_item,null);
                holder = new ViewHolder();
                holder.photo = v.findViewById(R.id.photo);
                holder.name = v.findViewById(R.id.name);
                holder.phone = v.findViewById(R.id.phone);
                v.setTag(holder);

            }else{               // 캐시된 뷰가 있을 경우 저장된 뷰홀더를 사용한다
                holder = (ViewHolder)v.getTag();
            }

            holder.name.setText(ls.get(i).getName());
            holder.phone.setText(ls.get(i).getPhone());

            //photo 불러오는 코드
            final ItemPhoto query = new ItemPhoto(ctx);               //객체 위치 설정

            query.seq = ls.get(i).seq+"";

            String s = ((String)new Main.ObjectService() {   //s =file name
                @Override
                public Object perfome() {
                    return query.execute();
                }
            }.perfome()).toLowerCase();                 //table에는 PHOTO로 되어있어서
            Log.d("파일명:",s);

            //쓸데없이 외우지 말것.. 구조이해가 먼저!
            holder.photo
                    .setImageDrawable(getResources().getDrawable(           //사진은 drawable안에 file명만 있으니까
                            getResources().getIdentifier(//parame 3개
                                    ctx.getPackageName()+":drawable/"+s,null,null),
                            ctx.getTheme() ));

            return v;
        }
    }

    static class ViewHolder{
        ImageView photo;
        TextView name, phone;

    }

    private class PhotoQuery extends Main.QueryFactory{     //Photo 불러오는게 db가 아니라
                                                                //select photo from id like sequence

        Main.SQLiteHelper helper;

        public PhotoQuery(Context ctx) {
            super(ctx);
            helper =new Main. SQLiteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDateabase() {
            return helper.getReadableDatabase();
        }
    }
    private class ItemPhoto extends PhotoQuery{
        String seq;
        public ItemPhoto(Context ctx) {
            super(ctx);
        }
        public String execute(){
            Cursor c= getDateabase()
                    .rawQuery(String.format(
                            " SELECT %s FROM %s WHERE %s LIKE '%s' ",
                            DBInfo.MBR_PHOTO,
                            DBInfo.MBR_TABLE,
                            DBInfo.MBR_SEQ,
                            seq
                    ),null);
            String result = "";
            if(c!= null){
                if(c.moveToNext()){
                    result = c.getString(c.getColumnIndex(DBInfo.MBR_PHOTO));
                }
            }
            return result;
        }



    }
}



















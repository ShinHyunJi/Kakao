package com.example.a405_25.kakao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class mbr_update extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mbr_update);

        final Context ctx = mbr_update.this;


        //2
        //Intent intent = this.getIntent().getStringExtra("spec");
        //String s = getIntent().getStringExtra("spec");
        //String[] arr = s.split("/");


        final String[] arr = getIntent().getStringExtra("spec").split("/");
        // m.seq+"/"+m.addr+"/"+m.email+"/"+m.name+"/"+m.pass+"/"+m.photo+"/"+m.phone

        //1
        final EditText name = findViewById(R.id.name);
        name.setHint(arr[3]);
        final EditText email = findViewById(R.id.email);
        email.setHint(arr[2]);
        final EditText phone = findViewById(R.id.phone);
        phone.setHint(arr[6]);
        final EditText addr = findViewById(R.id.addr);
        addr.setHint(arr[1]);
        final EditText pass = findViewById(R.id.pass);
        pass.setHint(arr[4]);


        //Image View
        ImageView photo = findViewById(R.id.photo);
        Log.d("프로필사진 :: ",arr[5].toLowerCase());
        photo.setImageDrawable(
                getResources()
                        .getDrawable(
                                getResources()
                                        .getIdentifier(this.getPackageName()+":drawable/"+arr[5].toLowerCase(),null,null), ctx.getTheme()
                        )
        );


        //수정 이후

        findViewById(R.id.confirmBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Query에 던져줄 거
                Member member = new Member();
                //cancel 눌렀을 경우를 생각하면 반드시 내부에
                final ItemUpdate query = new ItemUpdate(ctx);
                //member에 값을 넣는데, 만약 EditText가  NULL이라면 배열(arr[])에 있는 값이라도 member에 할당해야함
                //삼항연산자 사용할것!(android에서는 if안씀)

                //but 느리기때문에 cpu건들이지 않고 메모리가 할 수 있도록 삼항연산자로!
//                String name2 ="";
//                if(name.getText().equals("")){          //null= 주소가 없음! 메모리 주소지가 없음!
//                    name2 = arr[3];              //값이 없다면 원래 데이터 그대로 넣기
//                }else{
//                    name2 = name.getText().toString();
//                }
//                member.setName(name2);  //수정된 데이터 member에
//
//                query.member = member;

                //삼항연산자 꼭 익숙해질것!
                //String name2 =
                //(name.getText().equals(""))? arr[3]:name.getText().toString();
                //member.setName(name2);
                // query.member = member;

                member.setName((name.getText().equals(""))? arr[3]:name.getText().toString());
                query.member = member;

                //email
                //name... 다 만들것!!



                //반환값이 void니까 삭제
                //Main.ExcuteService s = new Main.ExcuteService() {
                new Main.ExcuteService() {
                    @Override
                    public void perfome() {
                        query.execute();
                    }
                }.perfome();


            }
        });



        findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private class UpdateQuery extends Main.QueryFactory{
        SQLiteOpenHelper helper;

        public UpdateQuery(Context ctx) {
            super(ctx);
            this.helper = new Main.SQLiteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDateabase() {
            return helper.getWritableDatabase();
        }
    }

    private class ItemUpdate extends UpdateQuery{
        Member member;                  //Memory에 저장!
                                        // Member member = new Member();     안돼!

        public ItemUpdate(Context ctx) {
            super(ctx);
            member = new Member();
            //instance 변수는 반드시 생성자 내부에서 초기화 함
            //로직은 반드시 에어리어 내부에서 이루어 짐
            // 에어리어 내부는 CPU를 뜻함
            //필드는 RAM 영역을 뜻함

        }
        public void execute(){
            String sql = String.format(
                    " UPDATE %s SET "+
                            " %s = '%s' ," +
                            " %s = '%s' ,"+
                            " %s = '%s' ," +
                            " %s = '%s' ," +
                            " %s = '%s' ," +
                            " %s = '%s' " +
                            " WHERE %s LIKE '%s' ",

                    DBInfo.MBR_TABLE,
                    DBInfo.MBR_ADDR, member.addr,
                    DBInfo.MBR_EMAIL, member.email,
                    DBInfo.MBR_NAME, member.name,
                    DBInfo.MBR_PASS, member.pass,
                    DBInfo.MBR_PHOTO,member.photo,
                    DBInfo.MBR_PHONE, member.phone,
                    DBInfo.MBR_SEQ, member.seq


                    // m.seq+"/"+m.addr+"/"+m.email+"/"+m.name+"/"+m.pass+"/"+m.photo+"/"+m.phone
            );
            Log.d(" SQL :::", sql);
            getDateabase().execSQL(sql);
        }
    }

}


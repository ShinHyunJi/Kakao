package com.example.a405_25.kakao;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

import static android.provider.Telephony.Mms.Part.TEXT;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final Context ctx = Main.this;

        //MOVE
        findViewById(R.id.moveLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SqliteHelper helper = new SqliteHelper(ctx);
                //Helper라는 객체를 만드는 것은 곧, SQLite DB를 만드는 것!


                /*
                Intent intent = new Intent(ctx,Login.class);        //java=>compile=>class
                                        //frome     to 즉,현재페이지에서...
                startActivity(intent);
                */
                startActivity(new Intent(ctx, Login.class));

            }
        });
        }
    static interface ExcuteService {
        public void perfome();

    }

    static interface ListService{
        public List<?> perfome();
    }

    static interface ObjectService{
        public Object perfome();
    }

    static abstract class QueryFactory{     //query생성clas
        Context ctx;

        public QueryFactory(Context ctx) {      //alt+insert
            this.ctx = ctx;
        }

        public abstract SQLiteDatabase getDateabase();

    }

    static class SqliteHelper extends SQLiteOpenHelper{     //sqlitedb열어줌
            //alt+insert > implement +

        public SqliteHelper(Context context) {
            super(context, DBInfo.DBNAME, null,1);  //커스터마이즈
            this.getWritableDatabase();         //data insert
        }

        public void onCreate(SQLiteDatabase db){
            String sql = String.format(
              " CREATE TABLE IF NOT EXISTS %s " +
              " ( %s INTEGER PRIMARY KEY AUTOINCREMENT," +   //자동1씩 증가
              "   %s TEXT, "+
              "   %s TEXT, "+
              "   %s TEXT, "+
              "   %s TEXT, "+
              "   %s TEXT, " +
              "   %s TEXT " +
              ") ",

                    DBInfo.MBR_TABLE,
                    DBInfo.MBR_SEQ,
                    DBInfo.MBR_NAME,
                    DBInfo.MBR_EMAIL,
                    DBInfo.MBR_PASS,
                    DBInfo.MBR_ADDR,
                    DBInfo.MBR_PHONE,
                    DBInfo.MBR_PHOTO
            );

            Log.d("실행할 쿼리 ::",sql);
            db.execSQL(sql);

            Log.d("===========================================","쿼리실행");

            String[] names = {"강동원","윤아","임수정","박보검","이효리"};
            String[] emails = {"aaa@naber.com","bbb@naber.com","ccc@gmail.com","ddd@daum.net","eeee@yahoo.com"};
            String[] addr = {"강동구","관악구","송파구","강서구","강남구"};


            for(int i=0;i<names.length;i++){
                Log.d("입력하는 이름 ::" ,names[i]);

                db.execSQL(String.format(                   //writable   vs readable ?
                        " INSERT INTO %s  " +
                        " ( %s ," +
                        "  %s ," +
                        "  %s ," +
                        "  %s ," +
                        "  %s ," +
                        "  %s " +
                        ")VALUES("+
                        "'%s', " +
                        "'%s' ," +
                        "'%s' ," +
                        "'%s' ," +
                        "'%s' ," +
                        "'%s' " +
                        " ) " ,
                        DBInfo.MBR_TABLE, DBInfo.MBR_NAME, DBInfo.MBR_EMAIL, DBInfo.MBR_PASS,
                        DBInfo.MBR_ADDR, DBInfo.MBR_PHONE, DBInfo.MBR_PHOTO,
                        names[i],emails[i],'1',addr[i],"010-1234-567"+i,"PHOTO_"+(i+1)
                ));
                Log.d("*************************","친구등록완료!");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+DBInfo.MBR_TABLE);
            onCreate(db);


        }
    }
}
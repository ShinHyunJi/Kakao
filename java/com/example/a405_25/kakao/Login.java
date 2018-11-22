package com.example.a405_25.kakao;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.Collections;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        final Context ctx = Login.this;
        final EditText etID = findViewById(R.id.etID);
        final EditText etPass = findViewById(R.id.etPass);

        //Button => btLogin
        findViewById(R.id.btLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Validation 유효성체크
                if(etID.getText().length()!=0 && etPass.getText().length()!=0){
                    String id = etID.getText().toString();
                    String pass = etPass.getText().toString();
                    final ItemExist query = new ItemExist(ctx);
                    query.id = id;
                    query.pw = pass;
                    new Main.ExcuteService() {
                        @Override
                        public void perfome() {
                            if(query.execute()){
                                startActivity(new Intent(ctx, mbr_list.class));
                            }else{
                                startActivity(new Intent(ctx, Login.class));
                            }
                        }
                    }.perfome();
                }else{
                    startActivity(new Intent(ctx, Login.class));
                }

            }
        });

        //Button  => btCancel
        findViewById(R.id.btCancel).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
            }
        });
    }           //onCreate END

    private class LoginQuery extends Main.QueryFactory{
        SQLiteOpenHelper helper;
        public LoginQuery(Context ctx) {
            super(ctx);
            helper = new Main.SQLiteHelper(ctx);
        }
        public SQLiteDatabase getDateabase() {
            return helper.getReadableDatabase();

        }
    } // LoginQuery End

    private class ItemExist extends LoginQuery{
        String id, pw;

        public ItemExist(Context ctx) {
            super(ctx);
        }
        public boolean execute(){
            return super
                    .getDateabase()
                    .rawQuery(String.format(
                            " SELECT * FROM %s "+
                                    " WHERE %s LIKE '%s' AND %s LIKE '%s'",
                            DBInfo.MBR_TABLE,DBInfo.MBR_SEQ, id,
                            DBInfo.MBR_PASS, pw),null)
                    .moveToNext()
                    ;
        }
    }
}

package com.test.tongil.base64test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.test.tongil.base64test.util.BCrypt;
import com.test.tongil.base64test.util.Preferences;

public class MainActivity extends Activity {
    private static String TAG = "TongIl";
    private Button enrollKey;
    private Button callKey;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private StringBuilder builder;
    private StringBuilder builder2;
    private LinearLayout linearLayout1;
    private LinearLayout linearLayout2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_two);
        Log.i(TAG,"INIT");
        FindObject();
        SetObjectListener();


    }
    private void FindObject(){
        enrollKey = (Button) findViewById(R.id.enroll);
        callKey = (Button) findViewById(R.id.call);
        textView1 = (TextView) findViewById(R.id.txtView);
        textView2 = (TextView) findViewById(R.id.txtView2);
        textView3 = (TextView) findViewById(R.id.txtView3);
        linearLayout1 = (LinearLayout) findViewById(R.id.Layer1);
        linearLayout2 = (LinearLayout) findViewById(R.id.Layer2);
    }

    private void SetObjectListener(){
        enrollKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnrollKey();
                Preferences.setSettingsParam("Key","asdf");
            }
        });

        callKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallKey();
                Log.i(TAG,Preferences.getSettingsParam("Key"));
            }
        });
        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linearLayout2.getVisibility()==View.GONE){
                    linearLayout2.setVisibility(View.VISIBLE);
                }else if(linearLayout2.getVisibility()==View.VISIBLE){
                    linearLayout2.setVisibility(View.GONE);
                }
            }
        });
    }
    private void EnrollKey(){
        String keyId = "TongIl-Test-Key-"+ Base64.encodeToString(BCrypt.gensalt().getBytes(), Base64.NO_WRAP);


        //keyId = Base64.encodeToString(keyId.getBytes(), Base64.NO_WRAP);
        // NO_WARP => 개행문자 X   ,  URL_SAFE => 네트워크 단에서도 사용가능한 형태(개행문자 O)
        keyId = Base64.encodeToString(keyId.getBytes(), Base64.URL_SAFE);
        StringBuilder sb2 = new StringBuilder();
        for(byte b : keyId.getBytes()) sb2.append(b+ " ");
        Log.i(TAG,"키아이디 : "+keyId);
        Log.i(TAG,"스트링 빌더 : " + sb2.toString());
        Preferences.setSettingsParam("keyId", keyId);
        Preferences.setSettingsParamLong("LengKey", keyId.length());
        Log.i(TAG,"등록완료 =>\n키 : " + Preferences.getSettingsParam("keyId") + "\n길이 : " + (int)Preferences.getSettingsParamLong("LengKey"));

        //base64 형태가 아닌  일반 문자열 + 개행문자 \n
        Preferences.setSettingsParam("test","abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ\n");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ShowToast("등록 완료");
                setText(textView1,"KEY : " + Preferences.getSettingsParam("keyId"));
                setText(textView2,"LENGTH : " + Preferences.getSettingsParamLong("LengKey"));
                setText(textView3,"등록 완료");
            }
        });
    }

    private void CallKey(){
        String keyId = Preferences.getSettingsParam("keyId");
        byte[] value = keyId.getBytes();

        Log.i(TAG,"인증 key 길이 :  " + Preferences.getSettingsParamLong("LengKey"));
        Log.i(TAG,"인증 key 1 :  " + keyId);
         builder = new StringBuilder();
        for(byte b : value) builder.append(b + " ");
        Log.i(TAG,"스트링 빌더 1 : "+ builder.toString());

        String keyId2 = Preferences.getSettingsParam("keyId");
        keyId2 = keyId2.substring(0,(int)Preferences.getSettingsParamLong("LengKey"));
        byte[] value2 = keyId2.getBytes();

		Log.i(TAG,"인증 key 2 :  " + keyId2);
		 builder2 = new StringBuilder();
		for(byte b : value2) builder2.append(b + " ");
		Log.i(TAG,"스트링 빌더 2 : "+ builder2.toString());

        String test = Preferences.getSettingsParam("test");
		Log.i(TAG,"test 길이 : " + test.length());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ShowToast("호출 완료");
                setText(textView1,"오리지널 10 진법 형태 : "+ builder.toString());
                setText(textView2,"인덱스 제한 10 진법 형태 :" + builder2.toString());
                if(builder.toString().equals(builder2.toString())){
                    setText(textView3,"일치"+builder.toString().length()+" "+builder2.toString().length());
                }else{
                    setText(textView3,"불일치"+builder.toString().length()+" "+builder2.toString().length());
                }
            }
        });
    }
    private void ShowToast(String str){
        Toast.makeText(this,str,Toast.LENGTH_LONG).show();
    }

    private void setText(TextView textView,String str){
        textView.setText(str);
    }
}

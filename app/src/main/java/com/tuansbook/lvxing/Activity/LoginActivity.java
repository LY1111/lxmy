package com.tuansbook.lvxing.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tuansbook.lvxing.OKHttp.MyOKHttp;
import com.tuansbook.lvxing.OKHttp.OKHttpCallBack;
import com.tuansbook.lvxing.R;

/**
 * Created by Administrator on 2017/2/28.
 * 登录
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private CardView login;
    private EditText username,psd;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        context = this;

        TextView register = (TextView) findViewById(R.id.login_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // 用户名
        username = (EditText) findViewById(R.id.login_username);
        // 密码
        psd = (EditText) findViewById(R.id.login_psd);

        // 登录
        login = (CardView) findViewById(R.id.login_login);
        login.setOnClickListener(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.login_login: // 登录
                MyOKHttp myOKHttp = MyOKHttp.getInstance(context);
//                String encypted = AESUtils.encrypt("username=" + username.getText().toString() + "&password=" + psd.getText().toString());
//
//                myOKHttp.requestAsyn("login", "api/user/login/", null, encypted, new OKHttpCallBack<String>() {
//                    @Override
//                    public void onSucess(String result) {
//                        String afterDecrypt = AESUtils.decrypt(result);
//                        Log.i("解密后的结果：",afterDecrypt);
//                    }
//
//                    @Override
//                    public void onFailure(String msg) {
//                        Log.i("登录报错：",msg);
//                    }
//                });

                myOKHttp.requestAsyn("login", "http://120.27.112.152/cgapp/action/Web/indexPort/", null, null, new OKHttpCallBack<String>() {
                    @Override
                    public void onSucess(String result) {
                        Log.i("解密后的结果：",result);
                    }

                    @Override
                    public void onFailure(String msg) {
                        Log.i("登录报错：",msg);
                    }
                });
                break;
            default:
                break;
        }
    }
}

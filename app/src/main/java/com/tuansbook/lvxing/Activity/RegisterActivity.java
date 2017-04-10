package com.tuansbook.lvxing.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.tuansbook.lvxing.OKHttp.MyOKHttp;
import com.tuansbook.lvxing.OKHttp.OKHttpCallBack;
import com.tuansbook.lvxing.R;
import com.tuansbook.lvxing.Util.AESUtils;

/**
 * Created by Administrator on 2017/2/28.
 * 注册
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private EditText phone,verify,psd;
    private ImageView get_verify,can_see;
    private CardView register;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        context = this;

        // 返回
        ImageView back = (ImageView) findViewById(R.id.register_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 登录
        TextView login = (TextView) findViewById(R.id.register_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,LoginActivity.class);
                startActivity(intent);
            }
        });

        // 手机号
        phone = (EditText) findViewById(R.id.register_phone);
        // 验证码
        verify = (EditText) findViewById(R.id.register_verify);
        // 密码
        psd = (EditText) findViewById(R.id.register_psd);
        // 获取验证码
        get_verify = (ImageView) findViewById(R.id.register_get_verify);
        get_verify.setOnClickListener(this);
        // 密码是否可见
        can_see = (ImageView) findViewById(R.id.register_cansee);
        can_see.setOnClickListener(this);
        // 注册按钮
        register = (CardView) findViewById(R.id.register_register);
        register.setOnClickListener(this);
        // 快捷登录_QQ
        ImageView qq = (ImageView) findViewById(R.id.fast_login_qq);
        qq.setOnClickListener(this);
        // 快捷登录_微信
        ImageView wechat = (ImageView) findViewById(R.id.fast_login_wechat);
        wechat.setOnClickListener(this);
        // 快捷登录_新浪
        ImageView sina = (ImageView) findViewById(R.id.fast_login_sina);
        sina.setOnClickListener(this);
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

            // 获取验证码
            case R.id.register_get_verify:
                MyOKHttp myOKHttp = MyOKHttp.getInstance(context);
                String encypted = AESUtils.encrypt("userName=" + phone.getText().toString() + "&type=regist");
                myOKHttp.requestAsyn("register", "api/LoginAndRegist/Register/", null, encypted, new OKHttpCallBack<String>() {
                    @Override
                    public void onSucess(String result) {
                        Log.i("返回值：",result);
                        String afterDecrypt = AESUtils.decrypt(result);
                        Log.i("解密后的结果：",afterDecrypt);
                    }
                    @Override
                    public void onFailure(String msg) {
                        Log.i("注册报错：",msg);
                    }
                });
                break;
            // 密码是否可见
            case R.id.register_cansee:
                break;
            // 注册
            case R.id.register_register:
                break;
            // QQ登录
            case R.id.fast_login_qq:
                break;
            // 微信登录
            case R.id.fast_login_wechat:
                break;
            // 新浪登录
            case R.id.fast_login_sina:
                break;
            default:
                break;
        }
    }
}

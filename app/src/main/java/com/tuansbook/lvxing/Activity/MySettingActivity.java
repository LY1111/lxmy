package com.tuansbook.lvxing.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.tuansbook.lvxing.R;

/**
 * 我的设置
 * Created by Administrator on 2017/3/9
 */
public class MySettingActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_setting);
        context = this;

        // 意见反馈
        LinearLayout feedback = (LinearLayout) findViewById(R.id.setting_feedback);
        feedback.setOnClickListener(this);
        // 修改密码
        LinearLayout chgPsd = (LinearLayout) findViewById(R.id.setting_changepsd);
        chgPsd.setOnClickListener(this);
        // 修改手机号
        LinearLayout phone = (LinearLayout) findViewById(R.id.setting_phone);
        phone.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        Intent intent;
        switch (v.getId()){

            case R.id.setting_feedback: // 意见反馈
                intent = new Intent(context,FeedbackActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_changepsd: // 修改密码
            case R.id.setting_phone: // 验证手机号
                intent = new Intent(context,PhoneVerifyActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}

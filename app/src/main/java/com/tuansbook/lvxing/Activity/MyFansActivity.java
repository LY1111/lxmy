package com.tuansbook.lvxing.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tuansbook.lvxing.R;

/**
 * Created by Administrator on 2017/3/9
 * 我的粉丝
 */
public class MyFansActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        context=this;

    }
}

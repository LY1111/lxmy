package com.tuansbook.lvxing.Activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tuansbook.lvxing.R;

/**
 * Created by Administrator on 2017/3/7.
 * 会员及开通会员
 */
public class KaiTongHYActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.huiyuan);
        context = this;

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toobar_toolbar);
        toolbar.setBackgroundColor(Color.BLACK);
        // 返回
        ImageView back = (ImageView) findViewById(R.id.toobar_cancel);
        back.setVisibility(View.VISIBLE);
        back.setBackgroundResource(R.mipmap.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageView right = (ImageView) findViewById(R.id.toolbar_search);
        right.setVisibility(View.GONE);
        // 标题
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("会员中心");

        ImageView imvip = (ImageView) findViewById(R.id.imvip);
        Glide.with(context).load(R.mipmap.imvip).crossFade().error(R.mipmap.ic_launcher).into(imvip);

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
}

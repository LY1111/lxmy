package com.tuansbook.lvxing.Activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuansbook.lvxing.R;

/**
 * Created by Administrator on 2017/3/10.
 * 申请开圈
 */
public class CreateQuanZiActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_quanzi);
        context = this;

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toobar_toolbar);
        toolbar.setBackgroundColor(Color.BLACK);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("申请开圈");

        ImageView search = (ImageView) findViewById(R.id.toolbar_search);
        search.setVisibility(View.GONE);

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

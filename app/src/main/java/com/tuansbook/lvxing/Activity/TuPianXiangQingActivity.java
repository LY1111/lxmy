package com.tuansbook.lvxing.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tuansbook.lvxing.Adapter.PingLunRecyclerHeaderAdapter;
import com.tuansbook.lvxing.Model.PingLun;
import com.tuansbook.lvxing.R;
import com.tuansbook.lvxing.Util.MyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/8.
 * 会员-图片详情页
 */
public class TuPianXiangQingActivity extends AppCompatActivity {

    private Context context;
    private RecyclerView recyclerView;
    private PingLunRecyclerHeaderAdapter adapter;
    private List<PingLun> pingLunList = new ArrayList<>(); // 评论列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_xiangqing);
        context = this;

        RelativeLayout tab = (RelativeLayout) findViewById(R.id.video_tab_below);
        tab.setVisibility(View.GONE);
        LinearLayout tab1 = (LinearLayout) findViewById(R.id.pic_tab_below);
        tab1.setVisibility(View.VISIBLE);

        recyclerView = (RecyclerView) findViewById(R.id.video_recycler);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        adapter = new PingLunRecyclerHeaderAdapter(context,pingLunList);

        View header = LayoutInflater.from(context).inflate(R.layout.pic_recycler_header,recyclerView,false); // 头部布局
        adapter.addHeader(header);
        recyclerView.setAdapter(adapter);

        // 主图片
        ImageView pic = (ImageView) header.findViewById(R.id.pic_recycler_header_pic);

        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        Log.i("屏幕宽度:",String.valueOf(displayMetrics.widthPixels) + "," + String.valueOf(point.x));
        Log.i("屏幕高度:",String.valueOf(displayMetrics.heightPixels) + "," + String.valueOf(point.y));

        // 设置高度为满屏
        pic.setMinimumHeight(point.y);

        // 转到评论列表页面
        RelativeLayout toall = (RelativeLayout) findViewById(R.id.pic_to_all_pinglun);
        toall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pinglun = new Intent(context,AllPingLunActivity.class);
                startActivity(pinglun);
            }
        });

        // 分享
        ImageView share = (ImageView) findViewById(R.id.pic_xiangqing_fenxiang);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtil.showSharePopUp(context,recyclerView);
            }
        });

        // 获取数据
        getData();
    }

    /**
     * 获取数据
     */
    private void getData() {

        pingLunList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            PingLun pinglun = new PingLun();
            pinglun.setPic("http://pic.baike.soso.com/p/20131221/20131221101135-1355890959.jpg");
            pingLunList.add(pinglun);
        }
        adapter.refresh(context,pingLunList);
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

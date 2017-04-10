package com.tuansbook.lvxing.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tuansbook.lvxing.Adapter.PingLunRecyclerHeaderAdapter;
import com.tuansbook.lvxing.Model.PingLun;
import com.tuansbook.lvxing.R;
import com.tuansbook.lvxing.Util.MyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/10.
 * 帖子详情
 */
public class TieZiXiangQingActivity extends AppCompatActivity {

    private Context context;
    private RecyclerView pinglunRecycler;
    private List<PingLun> pingLunList = new ArrayList<>(); // 评论
    private PingLunRecyclerHeaderAdapter adapter;
    private NestedScrollView nestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tiezi_xiangqing);
        context = this;

        nestedScrollView = (NestedScrollView) findViewById(R.id.tiezi_scroll);
        pinglunRecycler = (RecyclerView) findViewById(R.id.tiezi_xiangqing_recycler);
        pinglunRecycler.setItemAnimator(new DefaultItemAnimator());

        //        // 去掉RecyclerView的滑动属性 重写LinearLayoutManager的canScrollVertically方法 解决ScrollView和Recycler的滑动冲突问题
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        pinglunRecycler.setLayoutManager(linearLayoutManager);

        adapter = new PingLunRecyclerHeaderAdapter(context,pingLunList);
        pinglunRecycler.setAdapter(adapter);

        // 转到评论列表页面
        RelativeLayout toall = (RelativeLayout) findViewById(R.id.tiezi_to_all_pinglun);
        toall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pinglun = new Intent(context,AllPingLunActivity.class);
                startActivity(pinglun);
            }
        });

        // 分享
        ImageView share = (ImageView) findViewById(R.id.tiezi_xiangqing_fenxiang);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtil.showSharePopUp(context,pinglunRecycler);
            }
        });

        // 获取数据
        getData();


    }

    /**
     * 获取数据
     */
    private void getData(){

        pingLunList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            PingLun pinglun = new PingLun();
            pinglun.setPic("http://pic.baike.soso.com/p/20131221/20131221101135-1355890959.jpg");
            pingLunList.add(pinglun);
        }
        adapter.refresh(context,pingLunList);
        nestedScrollView.smoothScrollTo(0,0);

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

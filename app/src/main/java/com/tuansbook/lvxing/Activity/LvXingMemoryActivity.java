package com.tuansbook.lvxing.Activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuansbook.lvxing.Adapter.MemoryRecyclerAdapter;
import com.tuansbook.lvxing.Model.Video;
import com.tuansbook.lvxing.R;
import com.tuansbook.lvxing.Util.MyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/2.
 * 侣行记忆
 */
public class LvXingMemoryActivity extends AppCompatActivity {

    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private MemoryRecyclerAdapter adapter;
    private List<Video> videoList = new ArrayList<>(); // 视频

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coordinator_recycler);
        context = this;

        // 标题
        final TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("侣行记忆");

        // 隐藏搜索按钮
        ImageView search = (ImageView) findViewById(R.id.toolbar_search);
        search.setVisibility(View.GONE);

        // 返回
        ImageView back = (ImageView) findViewById(R.id.toobar_cancel);
        back.setBackgroundResource(R.mipmap.back);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toobar_toolbar);
        toolbar.setBackgroundColor(Color.BLACK);

        recyclerView = (RecyclerView) findViewById(R.id.coordinator_recycler);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.coordinator_swipe_refresh);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new MemoryRecyclerAdapter(videoList,context);
        recyclerView.setAdapter(adapter);

        // 下拉刷新
        swipeRefreshLayout.setColorSchemeColors(MyUtil.getColor(context,R.color.colorPrimary));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getData();
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                getData();
            }
        });
    }

    /**
     * 获取数据
     */
    private void getData() {

        // 测试数据
        videoList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Video video = new Video();
            video.setPic("http://pic.baike.soso.com/p/20131221/20131221101135-1355890959.jpg");
            videoList.add(video);
        }

        // 正常加载
        adapter.refresh(videoList,context);

        if(swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
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

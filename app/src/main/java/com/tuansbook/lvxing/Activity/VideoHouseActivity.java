package com.tuansbook.lvxing.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tuansbook.lvxing.Adapter.VideoHouseRecyclerAdapter;
import com.tuansbook.lvxing.Adapter.ViewPagerAdapter;
import com.tuansbook.lvxing.Model.Video;
import com.tuansbook.lvxing.R;
import com.tuansbook.lvxing.Util.MyUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/3/1.
 * 视频库
 */
public class VideoHouseActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Context context;
    private VideoHouseRecyclerAdapter adapter;
    private List<Video> videoList = new ArrayList<>(); // 视频
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private LinearLayout dot_linear; // 小圆点
    private List<String> pics = new ArrayList<>(); // 轮播图片
    private Timer timer = new Timer();
    private int position = 0; // 当前轮播图片位置
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean isloading = false; // 是否正在上拉刷新

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.road);
        context = this;

        recyclerView = (RecyclerView) findViewById(R.id.road_recycler);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new VideoHouseRecyclerAdapter(videoList,context);

        // 添加Header
        View header = LayoutInflater.from(context).inflate(R.layout.recycler_header_viewpager,null);
        adapter.addHeader(header);
        recyclerView.setAdapter(adapter);

        // 视频库各个入口
        LinearLayout entrance = (LinearLayout) header.findViewById(R.id.video_house_entrance);
        entrance.setVisibility(View.VISIBLE);

        // 新鲜在线
        TextView xinxian = (TextView) header.findViewById(R.id.xinxian_entrance);
        xinxian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent xin = new Intent(context,XinXianActivity.class);
                startActivity(xin);
            }
        });

        // 张梁记 张梁记和疯狂小片共用一个Activity
        TextView zhl = (TextView) header.findViewById(R.id.zhangliang_entrance);
        zhl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent zh = new Intent(context,XiaoPianActivity.class);
                zh.putExtra("title","张梁记");
                startActivity(zh);
            }
        });

        // 侣行记忆
        TextView memory = (TextView) header.findViewById(R.id.memory_entrance);
        memory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jiyi = new Intent(context,LvXingMemoryActivity.class);
                startActivity(jiyi);
            }
        });

        // 疯狂小片
        TextView xiaopian = (TextView) header.findViewById(R.id.xiaopian_enter);
        xiaopian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pian = new Intent(context,XiaoPianActivity.class);
                pian.putExtra("title","疯狂小片");
                startActivity(pian);
            }
        });

        // ViewPager
        viewPager = (ViewPager) header.findViewById(R.id.header_view_pager);
        dot_linear = (LinearLayout) header.findViewById(R.id.header_dot);

        viewPagerAdapter = new ViewPagerAdapter(pics,context);
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int pos) {

                viewPager.setCurrentItem(pos,true);
                position = pos; // 改变轮播起始位置
                if(context != null){
                    for (int i = 0; i < dot_linear.getChildCount(); i++) {
                        Glide.with(context).load(R.mipmap.ic_launcher).crossFade().into((ImageView) dot_linear.getChildAt(i));
                    }
                    Glide.with(context).load(R.mipmap.publish).crossFade().into((ImageView) dot_linear.getChildAt(position));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // 滚动到底部 下拉刷新
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                // findLastCompletelyVisibleItemPosition
                int last_visiable_position = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                // 底部
                if(last_visiable_position + 1 == adapter.getItemCount()){

                    // 上拉刷新
                    if(!isloading){
                        isloading = true;
                        // 更新数据
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getData();
                            }
                        },400);
                    }
                }

            }
        });

        // SwipeRefreshLayout
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.road_swipe_refresh);
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
    private void getData(){

        pics = new ArrayList<>();
        // 轮播图片
        for (int i = 0; i < 4; i++) {
            pics.add("http://tse4.mm.bing.net/th?id=OIP.UXjDIe2iDAqm149LTX-B0wEsDg&pid=15.1");
        }

        // 小圆点
        dot_linear.removeAllViews();
        for (int i = 0; i < pics.size(); i++) {

            ImageView tp = new ImageView(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(18,18);
            layoutParams.setMargins(10,0,0,10);
            tp.setLayoutParams(layoutParams);
            Glide.with(context).load(R.mipmap.ic_launcher).crossFade().into(tp);
            dot_linear.addView(tp);
        }
        Glide.with(context).load(R.mipmap.publish).crossFade().into((ImageView) dot_linear.getChildAt(0));

        viewPagerAdapter.refresh(pics,context);

        if(timer != null){
            timer.cancel();
        }
        timer = new Timer();

        // 开始图片轮播
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    // 避免使用Toast时报Can't create handler inside thread that has not called Looper.prepare()错误
                    // 可以在TimerTask中使用runOnUiThread让程序运行在主线程，Handler就不需要用了
                    Message msg = new Message();
                    msg.what = 0;
                    if(position < dot_linear.getChildCount() - 1){
                        position += 1;
                    }else {
                        position = 0;
                    }
                    msg.obj = position;
                    handler.sendMessage(msg);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 4000); // 图片轮播

        // 测试数据
        videoList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            Video video = new Video();
            video.setPic("http://pic.baike.soso.com/p/20131221/20131221101135-1355890959.jpg");
            videoList.add(video);
        }

        // 上拉加载完成
        if(isloading){
            // 去掉Footer Footer既可以是上拉加载的动画 也可以是Footer布局
            // 注意 运用了notifyItemRemoved方法后就不能用notifyDataSetChanged方法了 不然把改变又还原了
            adapter.notifyItemRemoved(adapter.getItemCount());
            isloading = false;
        }else {
            adapter.refresh(videoList,context);
        }

        if(swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * Handler
     */
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);

            switch (msg.what){

                case 0:
                    // 图片轮播
                    if(context != null){
                        viewPager.setCurrentItem(position,true);
                        for (int i = 0; i < dot_linear.getChildCount(); i++) {
                            Glide.with(context).load(R.mipmap.ic_launcher).crossFade().into((ImageView) dot_linear.getChildAt(i));
                        }
                        Glide.with(context).load(R.mipmap.publish).crossFade().into((ImageView) dot_linear.getChildAt(position));
                    }
                    break;
                default:
                    break;
            }
        }
    };

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

        if(timer != null){
            timer.cancel();
        }
    }
}

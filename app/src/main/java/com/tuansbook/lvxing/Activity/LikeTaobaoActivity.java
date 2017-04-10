package com.tuansbook.lvxing.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tuansbook.lvxing.Adapter.LvDianAdapter;
import com.tuansbook.lvxing.Adapter.ViewPagerAdapter;
import com.tuansbook.lvxing.Model.Prodruct;
import com.tuansbook.lvxing.R;
import com.tuansbook.lvxing.Util.MyUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/3/11.
 * 侣店
 */
public class LikeTaobaoActivity extends AppCompatActivity {

    private Context context;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Prodruct> prodructList = new ArrayList<>(); // 商品
    private LvDianAdapter adapter;
    private boolean isloading = false; // 是否正在上拉刷新
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private LinearLayout dot_linear; // 小圆点
    private List<String> pics = new ArrayList<>(); // 轮播图片
    private Timer timer = new Timer();
    private int position = 0; // 当前轮播图片位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.road);
        context = this;

        // 标题
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("侣店");

        // 隐藏搜索按钮
        ImageView search = (ImageView) findViewById(R.id.toolbar_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,SearchActivity.class);
                startActivity(intent);
            }
        });

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

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.road_swipe_refresh);
        recyclerView = (RecyclerView) findViewById(R.id.road_recycler);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new LvDianAdapter(context,prodructList);
        recyclerView.setAdapter(adapter);

        // Header
        View header = LayoutInflater.from(context).inflate(R.layout.recycler_header_viewpager,recyclerView,false);
        adapter.addHeader(header);

        // Footer
        View footer = LayoutInflater.from(context).inflate(R.layout.recycler_footer_refresh,recyclerView,false);
        adapter.addFooter(footer);

        // 上拉刷新
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int position = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                if(position + 1 == adapter.getItemCount()){
                    // 上拉刷新
                    if(!isloading){
                        isloading = true;
                        // 更新数据
                        getData();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

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

        // 轮播图片
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
    }

    /**
     * 获取数据
     */
    private void getData() {

        pics = new ArrayList<>();
        // 轮播图片
        for (int i = 0; i < 3; i++) {
            pics.add("https://www.cosme.com/en/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/1/1/11650_1.jpg");
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

        timer.cancel();
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
        prodructList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Prodruct prodruct = new Prodruct();
            prodruct.setPic("http://pic.baike.soso.com/p/20131221/20131221101135-1355890959.jpg");
            prodructList.add(prodruct);
        }

        // 上拉加载完成
        if(isloading){
            // 去掉Footer Footer既可以是上拉加载的动画 也可以是Footer布局
            // 注意 运用了notifyItemRemoved方法后就不能用notifyDataSetChanged方法了 不然把改变又还原了
            adapter.notifyItemRemoved(adapter.getItemCount());
            isloading = false;
        }else {
            adapter.refreshList(prodructList);
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

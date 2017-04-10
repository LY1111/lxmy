package com.tuansbook.lvxing.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.tuansbook.lvxing.Activity.LoginActivity;
import com.tuansbook.lvxing.Adapter.TieziAdapter;
import com.tuansbook.lvxing.Adapter.ViewPagerAdapter;
import com.tuansbook.lvxing.Model.TieZi;
import com.tuansbook.lvxing.R;
import com.tuansbook.lvxing.Util.MyUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/2/20.
 * 在路上
 */
public class RoadFragment extends Fragment {

    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private LinearLayout dot_linear; // 小圆点
    private List<String> pics = new ArrayList<>(); // 轮播图片
    private Timer timer = new Timer();
    private int position = 0; // 当前轮播图片位置
    private RecyclerView recycler;
    private List<TieZi> tieziList = new ArrayList<>(); // 资讯
    private TieziAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean isloading = false; // 是否正在上拉刷新

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState){
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.road,container,false);
        Log.i("onCreareView","onCreareView");

        // 搜索
        ImageView search = (ImageView) view.findViewById(R.id.toolbar_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        // 资讯，视频及帖子 前两个官方帖子及视频动态添加 只保留一个RecyclerView
        recycler = (RecyclerView) view.findViewById(R.id.road_recycler);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setLayoutManager(linearLayoutManager);

        adapter = new TieziAdapter(getActivity(),tieziList);
        recycler.setAdapter(adapter);

        // 为RecyclerView添加HeaderView
        View header = inflater.from(getActivity()).inflate(R.layout.recycler_header_viewpager,container,false);
        adapter.addHeader(header);

        View footer = inflater.from(getActivity()).inflate(R.layout.recycler_footer_refresh,container,false);
        adapter.addFooter(footer);

        viewPager = (ViewPager) header.findViewById(R.id.header_view_pager);
        dot_linear = (LinearLayout) header.findViewById(R.id.header_dot);

        viewPagerAdapter = new ViewPagerAdapter(pics,getActivity());
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int pos) {

                viewPager.setCurrentItem(pos,true);
                position = pos; // 改变轮播起始位置
                if(getActivity() != null && !getActivity().isDestroyed()){
                    for (int i = 0; i < dot_linear.getChildCount(); i++) {
                        Glide.with(getActivity()).load(R.mipmap.ic_launcher).crossFade().into((ImageView) dot_linear.getChildAt(i));
                    }
                    Glide.with(getActivity()).load(R.mipmap.publish).crossFade().into((ImageView) dot_linear.getChildAt(position));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // 滚动到底部 下拉刷新
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

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
                        },300);
                    }
                }

            }
        });

//        // 去掉RecyclerView的滑动属性 重写LinearLayoutManager的canScrollVertically方法 解决ScrollView和Recycler的滑动冲突问题
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()){
//            @Override
//            public boolean canScrollVertically() {
//                return false;
//            }
//        };

        // SwipeRefreshLayout
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.road_swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(MyUtil.getColor(getActivity(),R.color.colorPrimary));

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

        // 悬浮按钮
        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.road_float_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"float",Snackbar.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    /**
     * 获取数据
     */
    private void getData(){

        pics = new ArrayList<>();
        // 轮播图片
        for (int i = 0; i < 4; i++) {
            pics.add("https://www.cosme.com/en/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/1/1/11650_1.jpg");
        }

        // 小圆点
        dot_linear.removeAllViews();
        for (int i = 0; i < pics.size(); i++) {

            ImageView tp = new ImageView(getActivity());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(18,18);
            layoutParams.setMargins(10,0,0,10);
            tp.setLayoutParams(layoutParams);
            Glide.with(getActivity()).load(R.mipmap.ic_launcher).crossFade().into(tp);
            dot_linear.addView(tp);
        }
        Glide.with(getActivity()).load(R.mipmap.publish).crossFade().into((ImageView) dot_linear.getChildAt(0));

        viewPagerAdapter.refresh(pics,getActivity());

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
        tieziList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            TieZi tiezi = new TieZi();
            tiezi.setPic("https://www.cosme.com/en/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/1/1/11650_1.jpg");
            tieziList.add(tiezi);
        }

        // 上拉加载完成
        if(isloading){
            // 去掉Footer Footer既可以是上拉加载的动画 也可以是Footer布局
            // 注意 运用了notifyItemRemoved方法后就不能用notifyDataSetChanged方法了 不然把改变又还原了
            adapter.notifyItemRemoved(adapter.getItemCount());
            isloading = false;
        }else {
            adapter.refreshList(tieziList);
        }

        if(swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.i("onStart","onStart");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.i("onResume","onResume");
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
                    if(getActivity() != null && !getActivity().isDestroyed()){
                        viewPager.setCurrentItem(position,true);
                        for (int i = 0; i < dot_linear.getChildCount(); i++) {
                            Glide.with(getActivity()).load(R.mipmap.ic_launcher).crossFade().into((ImageView) dot_linear.getChildAt(i));
                        }
                        Glide.with(getActivity()).load(R.mipmap.publish).crossFade().into((ImageView) dot_linear.getChildAt(position));
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onPause(){
        super.onPause();
        Log.i("onPause","onPause");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i("onDestroy","onDestroy");
    }
}

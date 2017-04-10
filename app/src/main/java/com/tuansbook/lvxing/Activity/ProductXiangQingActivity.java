package com.tuansbook.lvxing.Activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.tuansbook.lvxing.Adapter.MoreProductAdapter;
import com.tuansbook.lvxing.Model.Prodruct;
import com.tuansbook.lvxing.R;
import com.tuansbook.lvxing.Util.MyUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/11.
 * 商品详情
 */
public class ProductXiangQingActivity extends AppCompatActivity {

    private Context context;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Prodruct> prodructList = new ArrayList<>(); // 商品
    private MoreProductAdapter adapter;
    private boolean isloading = false; // 是否正在上拉刷新

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.road);
        context = this;

        // 标题
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("详情");

        // 收藏
        ImageView collect = (ImageView) findViewById(R.id.toolbar_search);
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new MoreProductAdapter(context,prodructList);
        recyclerView.setAdapter(adapter);

        // Header
        View header = LayoutInflater.from(context).inflate(R.layout.lvdian_xiangqing_header,recyclerView,false);
        adapter.addHeader(header);

        // Footer
        View footer = LayoutInflater.from(context).inflate(R.layout.recycler_footer_refresh,recyclerView,false);
        adapter.addFooter(footer);

        // 上拉刷新
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int position = gridLayoutManager.findLastVisibleItemPosition();

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
    }

    /**
     * 获取数据
     */
    private void getData() {

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

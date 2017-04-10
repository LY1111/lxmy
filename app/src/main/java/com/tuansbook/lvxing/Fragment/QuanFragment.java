package com.tuansbook.lvxing.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuansbook.lvxing.Activity.AllQuanZiActivity;
import com.tuansbook.lvxing.Activity.CreateQuanZiActivity;
import com.tuansbook.lvxing.Activity.SearchActivity;
import com.tuansbook.lvxing.Adapter.OfficialMyQuanAdapter;
import com.tuansbook.lvxing.Adapter.TieziAdapter;
import com.tuansbook.lvxing.Model.TieZi;
import com.tuansbook.lvxing.R;
import com.tuansbook.lvxing.Util.MyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/20.
 * 侣友圈
 */
public class QuanFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView,official_recy,my_recy;
    private TieziAdapter adapter;
    private List<TieZi> tieZiList = new ArrayList<>();
    private boolean isloading = false; // 是否正在上拉刷新

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.road,container,false);

        // Toolbar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toobar_toolbar);
        toolbar.setBackgroundColor(Color.BLACK);

        // 标题
        TextView title = (TextView) view.findViewById(R.id.toolbar_title);
        title.setText("侣友圈");

        // 搜索
        ImageView search = (ImageView) view.findViewById(R.id.toolbar_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.road_recycler);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new TieziAdapter(getActivity(),tieZiList);
        recyclerView.setAdapter(adapter);

        // 为RecyclerView添加HeaderView
        View header = inflater.from(getActivity()).inflate(R.layout.quan_recycler_header,container,false);
        adapter.addHeader(header);

        View footer = inflater.from(getActivity()).inflate(R.layout.recycler_footer_refresh,container,false);
        adapter.addFooter(footer);

        // 全部圈子
        TextView allq = (TextView) header.findViewById(R.id.more_quan);
        allq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent allquan = new Intent(getActivity(), AllQuanZiActivity.class);
                startActivity(allquan);
            }
        });

        // 申请开圈
        TextView open = (TextView) header.findViewById(R.id.open_quan);
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openquan = new Intent(getActivity(), CreateQuanZiActivity.class);
                startActivity(openquan);
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        LinearLayoutManager manager1 = new LinearLayoutManager(getContext());
        manager1.setOrientation(LinearLayoutManager.HORIZONTAL);

        // 官方圈子
        official_recy = (RecyclerView) header.findViewById(R.id.quan_recycler_official);
        official_recy.setLayoutManager(manager);
        official_recy.setItemAnimator(new DefaultItemAnimator());

        // 我的圈子
        my_recy = (RecyclerView) header.findViewById(R.id.quan_recycler_my);
        my_recy.setLayoutManager(manager1);
        my_recy.setItemAnimator(new DefaultItemAnimator());

        List<TieZi> tiezis = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            TieZi tie = new TieZi();
            tie.setPic("https://www.cosme.com/en/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/1/1/11650_1.jpg");
            tie.setTitle(String.valueOf(i));
            tiezis.add(tie);
        }

        OfficialMyQuanAdapter official = new OfficialMyQuanAdapter(getContext(),tiezis);
        official_recy.setAdapter(official);

        List<TieZi> tiezis1 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            TieZi tie = new TieZi();
            tie.setPic("https://www.cosme.com/en/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/1/1/11650_1.jpg");
            tie.setTitle(String.valueOf(i));
            tiezis1.add(tie);
        }

        OfficialMyQuanAdapter my = new OfficialMyQuanAdapter(getContext(),tiezis1);
        my_recy.setAdapter(my);


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
                int position = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                // 底部
                if(position + 1 == adapter.getItemCount()){

                    // 上拉刷新
                    if(!isloading){
                        isloading = true;
                        // 更新数据
                        getData();
                    }
                }
            }
        });

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

        Snackbar.make(view,"侣友圈",Snackbar.LENGTH_SHORT).show();
        return view;
    }

    /**
     * 获取数据
     */
    private void getData() {

        // 测试数据
        tieZiList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            TieZi tiezi = new TieZi();
            tiezi.setPic("https://www.cosme.com/en/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/1/1/11650_1.jpg");
            tieZiList.add(tiezi);
        }

        // 上拉加载完成
        if(isloading){
            // 去掉Footer Footer既可以是上拉加载的动画 也可以是Footer布局
            // 注意 运用了notifyItemRemoved方法后就不能用notifyDataSetChanged方法了 不然把改变又还原了
            adapter.notifyItemRemoved(adapter.getItemCount());
            isloading = false;
        }else {
            adapter.refreshList(tieZiList);
        }

        if(swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}

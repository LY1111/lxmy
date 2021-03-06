package com.tuansbook.lvxing.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tuansbook.lvxing.Adapter.QuanZiAdapter;
import com.tuansbook.lvxing.Model.TieZi;
import com.tuansbook.lvxing.R;
import com.tuansbook.lvxing.Util.MyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/9.
 * 推荐圈子
 */
public class TuiJianQuanFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private QuanZiAdapter adapter;
    private List<TieZi> tieZiList = new ArrayList<>();
    private boolean isloading = false; // 是否正在上拉刷新

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.content_road,container,false);

        RecyclerView rec = (RecyclerView) view.findViewById(R.id.road_recycler);
        rec.setVisibility(View.GONE);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.road_swipe);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) view.findViewById(R.id.road_swipe_recycler);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new QuanZiAdapter(getActivity(),tieZiList);
        recyclerView.setAdapter(adapter);

        View footer = LayoutInflater.from(getActivity()).inflate(R.layout.recycler_footer_refresh,recyclerView,false);
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

        return view;
    }

    /**
     * 获取数据
     */
    private void getData() {

        // 测试数据
        tieZiList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            TieZi tieZi = new TieZi();
            tieZi.setPic("http://pic.baike.soso.com/p/20131221/20131221101135-1355890959.jpg");
            tieZiList.add(tieZi);
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

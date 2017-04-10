package com.tuansbook.lvxing.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tuansbook.lvxing.Adapter.PicStaggeredGridAdapter;
import com.tuansbook.lvxing.Model.Pic;
import com.tuansbook.lvxing.R;
import com.tuansbook.lvxing.Util.MyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/6.
 * 会员-精选图片
 */
public class JingXuanPicFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private PicStaggeredGridAdapter adapter;
    private List<Pic> picList = new ArrayList<>(); // 图片
    private boolean isloading = false; // 是否正在上拉刷新

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.content_road, container, false);

        RecyclerView rec = (RecyclerView) view.findViewById(R.id.road_recycler);
        rec.setVisibility(View.GONE);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.road_swipe);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) view.findViewById(R.id.road_swipe_recycler);

        final StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        // 解决刷新的时候item互换位置的问题
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        // 设置默认动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new PicStaggeredGridAdapter(getActivity(),picList);
        recyclerView.setAdapter(adapter);

        // 上拉刷新
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                staggeredGridLayoutManager.invalidateSpanAssignments();

                int[] poo = new int[staggeredGridLayoutManager.getSpanCount()];

                int[] pp = staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(poo);

                if(findMax(pp) + 1 == adapter.getItemCount()){
                    // 上拉刷新
                    if(!isloading){
                        isloading = true;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // 更新数据
                                getData();
                            }
                        },400);
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
        picList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            Pic pic = new Pic();
            pic.setPic("http://pic.baike.soso.com/p/20131221/20131221101135-1355890959.jpg");
            picList.add(pic);
        }

        // 上拉加载完成
        if(isloading){
            // 去掉Footer Footer既可以是上拉加载的动画 也可以是Footer布局
            // 注意 运用了notifyItemRemoved方法后就不能用notifyDataSetChanged方法了 不然把改变又还原了
            adapter.notifyItemRemoved(adapter.getItemCount());
            isloading = false;
        }else {
            adapter.refresh(getActivity(),picList);
        }

        if(swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * 找最大值
     */
    private int findMax(final int[] pos){
        int max = pos[0];
        for (int i = 0; i < pos.length; i++) {
            if(max < pos[i]){
                max = pos[i];
            }
        }
        return max;
    }

    /**
     * Handler
     */
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
        }
    };

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

package com.tuansbook.lvxing.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tuansbook.lvxing.Activity.VideoHouseActivity;
import com.tuansbook.lvxing.Model.TieZi;
import com.tuansbook.lvxing.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/24.
 * 带HeaderView和FooterView的RecyclerAdapter
 */
public class HeaderFooterRecyclerAdapter extends RecyclerView.Adapter<HeaderFooterRecyclerAdapter.Tie>{

    private Context context;
    private List<TieZi> tieZiList = new ArrayList<>(); // 咨询，视频，帖子

    private View header;
    private int RECYCLER_HEADER = 0; // 头部
    private int RECYCLER_BODY = 1; // 资讯区
    private int RECYCLER_FOOTER = 2; // 底部

    /**
     * 添加头部 header的逻辑在Activity或者Fragment里写
     * @param header
     */
    public void addHeader(View header){
        this.header = header;
        notifyItemInserted(0); // 插入第0个位置
    }

    /**
     * 获取view的类型 是否是header footer 或 body
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position){

        if(header == null){ // 头部

            if(position + 1 == getItemCount()){
                return RECYCLER_FOOTER;
            }else {
                return RECYCLER_BODY;
            }
        }

        if(header != null){

            if(position == 0){
                return RECYCLER_HEADER;
            }

            if(position + 1 == getItemCount()){
                return RECYCLER_FOOTER;
            }else {
                return RECYCLER_BODY;
            }
        }
        return RECYCLER_BODY;
    }

    /**
     * 初始化
     * @param tieZiList
     * @param context
     */
    public HeaderFooterRecyclerAdapter(List<TieZi> tieZiList,Context context){
        this.tieZiList = tieZiList;
        this.context = context;
    }

    /**
     * 刷新
     */
    public void refresh(List<TieZi> tieZiList,Context context){
        this.context = context;
        this.tieZiList = tieZiList;
        this.notifyDataSetChanged();
    }

    @Override
    public Tie onCreateViewHolder(ViewGroup parent, int viewType) {

        // 根据viewType引入不同的布局
        // Header
        if(header != null && viewType == RECYCLER_HEADER){
            return new Tie(header);
        }

        // Footer Footer作为展示 并不负责上拉加载 上拉加载逻辑在Activity或Fragment里面
        if(viewType == RECYCLER_FOOTER){

            View view = LayoutInflater.from(context).inflate(R.layout.recycler_footer_refresh,parent,false);
            return new Tie(view);
        }

        // Body
        View view = LayoutInflater.from(context).inflate(R.layout.road_recycler_item,parent,false);
        Tie tie = new Tie(view);
        return tie;
    }

    @Override
    public void onBindViewHolder(final Tie holder, final int position) {

        if(getItemViewType(position) == RECYCLER_HEADER){
            return;
        }else if(getItemViewType(position) == RECYCLER_BODY){

            int pos;

            if(header != null){
                pos = position -1; // 正确的item位置 此时要区分是否有header
            }else {
                pos = position; // 正确的item位置 此时要区分是否有header
            }

            // 资讯，视频，帖子
            TieZi data = tieZiList.get(pos);
            Glide.with(context).load(data.getPic()).crossFade().centerCrop().error(R.mipmap.ic_launcher).into(holder.pic);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(position == 3){
                        // 跳到视频库
                        Intent intent = new Intent(context, VideoHouseActivity.class);
                        context.startActivity(intent);
                    }
                }
            });
        }else {
            return;
        }
    }

    @Override
    public int getItemCount() {
        // 要加上Header Footer
        return tieZiList.size() == 0?0:(header == null?tieZiList.size() + 1:tieZiList.size() + 2);
    }

    /**
     * 正常body的viewHolder
     */
    public class Tie extends RecyclerView.ViewHolder {

        private ImageView pic;

        public Tie(View itemView) {
            super(itemView);

            pic = (ImageView) itemView.findViewById(R.id.tiezi_pic); // 图片
        }
    }
}

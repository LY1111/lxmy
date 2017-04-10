package com.tuansbook.lvxing.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tuansbook.lvxing.Activity.VideoXiangQingActivity;
import com.tuansbook.lvxing.Model.Video;
import com.tuansbook.lvxing.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/24.
 * 视频库(新鲜在线、张梁记、疯狂小片)
 */
public class VideoHouseRecyclerAdapter extends RecyclerView.Adapter<VideoHouseRecyclerAdapter.Vio>{

    private Context context;
    private List<Video> videoList = new ArrayList<>(); // 视频

    private View header;
    private int RECYCLER_HEADER = 0; // 头部
    private int RECYCLER_BODY = 1; // 视频
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
     * @param videoList
     * @param context
     */
    public VideoHouseRecyclerAdapter(List<Video> videoList,Context context){
        this.videoList = videoList;
        this.context = context;
    }

    /**
     * 刷新
     */
    public void refresh(List<Video> videoList,Context context){
        this.context = context;
        this.videoList = videoList;
        this.notifyDataSetChanged();
    }

    @Override
    public Vio onCreateViewHolder(ViewGroup parent, int viewType) {

        // 根据viewType引入不同的布局
        // Header
        if(header != null && viewType == RECYCLER_HEADER){
            return new Vio(header);
        }

        // Footer Footer作为展示 并不负责上拉加载 上拉加载逻辑在Activity或Fragment里面
        if(viewType == RECYCLER_FOOTER){

            View view = LayoutInflater.from(context).inflate(R.layout.recycler_footer_refresh,parent,false);
            return new Vio(view);
        }

        // Body
        View view = LayoutInflater.from(context).inflate(R.layout.xiaopian_inflate,parent,false);
        Vio vio = new Vio(view);
        return vio;
    }

    @Override
    public void onBindViewHolder(final Vio holder, final int position) {

        if(getItemViewType(position) == RECYCLER_HEADER){
            return;
        }else if(getItemViewType(position) == RECYCLER_BODY){

            int pos;

            if(header != null){
                pos = position -1; // 正确的item位置 此时要区分是否有header
            }else {
                pos = position; // 正确的item位置 此时要区分是否有header
            }

            // 视频
            Video data = videoList.get(pos);
            Glide.with(context).load(data.getPic()).crossFade().centerCrop().error(R.mipmap.ic_launcher).into(holder.pic);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // 跳转到详情页面
                    Intent intent = new Intent(context, VideoXiangQingActivity.class);
                    context.startActivity(intent);
                }
            });
        }else {
            return;
        }
    }

    @Override
    public int getItemCount() {
        // 要加上Header Footer
        return videoList.size() == 0?0:(header == null?videoList.size() + 1:videoList.size() + 2);
    }

    /**
     * 正常body的viewHolder
     */
    public class Vio extends RecyclerView.ViewHolder {

        private ImageView pic;

        public Vio(View itemView) {
            super(itemView);

            pic = (ImageView) itemView.findViewById(R.id.xiaopian_pic); // 图片
        }
    }
}
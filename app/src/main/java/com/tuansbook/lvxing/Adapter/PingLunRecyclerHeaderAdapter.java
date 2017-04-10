package com.tuansbook.lvxing.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tuansbook.lvxing.Model.PingLun;
import com.tuansbook.lvxing.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/3.
 * 评论列表
 */
public class PingLunRecyclerHeaderAdapter extends RecyclerView.Adapter<PingLunRecyclerHeaderAdapter.Ping>{

    private Context context;
    private List<PingLun> pingLunList = new ArrayList<>(); // 评论列表

    private View header;
    private int RECYCLER_HEADER = 0; // 头部
    private int RECYCLER_BODY = 1;

    public PingLunRecyclerHeaderAdapter(Context context,List<PingLun> pingLunList){
        this.context = context;
        this.pingLunList = pingLunList;
    }

    /**
     * 刷新
     * @param context
     * @param pingLunList
     */
    public void refresh(Context context,List<PingLun> pingLunList){
        this.context = context;
        this.pingLunList = pingLunList;
        this.notifyDataSetChanged();
    }

    /**
     * 添加头部
     * @param header
     */
    public void addHeader(View header){
        this.header = header;
        notifyItemInserted(0); // 第一条插入header
    }

    /**
     * 获取View的类型
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position){

        if(header == null){
            return RECYCLER_BODY;
        }else {
            if (position == 0){
                return RECYCLER_HEADER;
            }else {
                return RECYCLER_BODY;
            }
        }
    }

    @Override
    public Ping onCreateViewHolder(ViewGroup parent, int viewType) {

        if(header !=null && viewType == RECYCLER_HEADER){
            return new Ping(header);
        }else {
            View body = LayoutInflater.from(context).inflate(R.layout.pinglun_inflate,parent,false);
            return new Ping(body);
        }
    }

    @Override
    public void onBindViewHolder(Ping holder, int position) {

        if(getItemViewType(position) == RECYCLER_HEADER){
            return;
        }else {

            // youheader的情况下 真是的position要变化
            PingLun data = null;
            if(header != null){
                // 评论列表
                data = pingLunList.get(position - 1);
            }else {
                data = pingLunList.get(position);
            }

            Glide.with(context).load(data.getPic()).centerCrop().crossFade().error(R.mipmap.ic_launcher).into(holder.icon); // 头像
        }
    }

    @Override
    public int getItemCount() {
        return header == null? pingLunList.size() : pingLunList.size() + 1;
    }

    /**
     * 评论
     */
    public class Ping extends RecyclerView.ViewHolder {

        private ImageView icon;

        public Ping(View itemView) {
            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.pinglun_icon); // 头像
        }
    }
}

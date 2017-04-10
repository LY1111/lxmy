package com.tuansbook.lvxing.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tuansbook.lvxing.Model.Video;
import com.tuansbook.lvxing.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/2.
 * 侣行记忆
 */
public class MemoryRecyclerAdapter extends RecyclerView.Adapter<MemoryRecyclerAdapter.Memory> {

    private List<Video> videoList = new ArrayList<>();
    private Context context;

    public MemoryRecyclerAdapter(List<Video> videoList,Context context){
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
    public Memory onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(context).inflate(R.layout.memory_inflate,parent,false);
        Memory memory = new Memory(view);
        return memory;
    }

    @Override
    public void onBindViewHolder(Memory holder, int position) {

        Video data = videoList.get(position);

        Glide.with(context).load(data.getPic()).centerCrop().crossFade().error(R.mipmap.ic_launcher).into(holder.pic);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class Memory extends RecyclerView.ViewHolder {

        private ImageView pic;

        public Memory(View itemView) {
            super(itemView);

            pic = (ImageView) itemView.findViewById(R.id.memory_pic); // 图片
        }
    }
}

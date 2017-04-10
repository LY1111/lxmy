package com.tuansbook.lvxing.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tuansbook.lvxing.Activity.TuPianXiangQingActivity;
import com.tuansbook.lvxing.Model.Pic;
import com.tuansbook.lvxing.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/7.
 * 会员-精选图片
 */
public class PicStaggeredGridAdapter extends RecyclerView.Adapter<PicStaggeredGridAdapter.MyPic>{

    private List<Pic> picList = new ArrayList<>();
    private Context context;
    private int TYPE_FOOTER = 0; // 底部
    private int TYPE_BODY = 1; // BODY

    /**
     * 获取view的类型 是否是header footer 或 body
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position){

        if(position + 1 == getItemCount()){
            return TYPE_FOOTER;
        }else {
            return TYPE_BODY;
        }
    }

    public PicStaggeredGridAdapter(Context context,List<Pic> picList){
        this.context = context;
        this.picList = picList;
    }

    /**
     * 刷新
     */
    public void refresh(Context context,List<Pic> picList){
        this.context = context;
        this.picList = picList;
        this.notifyDataSetChanged();
    }

    @Override
    public MyPic onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view;

        if(viewType == TYPE_FOOTER){
            view = inflater.inflate(R.layout.recycler_footer_refresh,parent,false);
        }else {
            view = inflater.inflate(R.layout.huiyuan_pic_inflate,parent,false);
        }
        return new MyPic(view);
    }

    @Override
    public void onBindViewHolder(MyPic holder, int position) {

        if(getItemViewType(position) == TYPE_FOOTER){

            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
            return;
        }else {
            ViewGroup.LayoutParams layoutParams = holder.pic.getLayoutParams();
            int height = (int) (600 + Math.random()*200);
            layoutParams.height = height;
            holder.pic.setLayoutParams(layoutParams);

            Pic data = picList.get(position);
            Glide.with(context).load(data.getPic()).centerCrop().crossFade().into(holder.pic);
            Glide.with(context).load(R.mipmap.publish).crossFade().into(holder.top);
            holder.top.bringToFront(); // 在最顶层显示

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 进入图片详情页
                    Intent intent = new Intent(context, TuPianXiangQingActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return picList.size() + 1;
    }

    public class MyPic extends RecyclerView.ViewHolder {

        private ImageView pic,top;

        public MyPic(View itemView) {
            super(itemView);

            pic = (ImageView) itemView.findViewById(R.id.jingxuan_pic_pic); // 精选图片
            top = (ImageView) itemView.findViewById(R.id.jingxuan_left_top); // 最左上角图片
        }
    }
}

package com.tuansbook.lvxing.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tuansbook.lvxing.Activity.TieZiXiangQingActivity;
import com.tuansbook.lvxing.Model.TieZi;
import com.tuansbook.lvxing.R;

import java.util.List;

/**
 * Created by Administrator on 2017/3/9.
 * 帖子Adapter 首页
 */
public class TieziAdapter extends BaseAdapter<TieZi>{

    private Context context;

    public TieziAdapter(Context context, List<TieZi> tieZiList){
        this.context = context;
        addData(tieZiList);
    }

    @Override
    public RecyclerView.ViewHolder createHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tiezi_inflate,parent,false);
        THolder tHolder = new THolder(view);
        return tHolder;
    }

    @Override
    public void bindHolder(RecyclerView.ViewHolder holder, int position, TieZi data) {

        if(holder instanceof THolder){
            Glide.with(context).load(data.getPic()).centerCrop().crossFade().error(R.mipmap.ic_launcher).into(((THolder) holder).pic);

            // 点击跳到帖子详情页
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent tiezi = new Intent(context, TieZiXiangQingActivity.class);
                    context.startActivity(tiezi);
                }
            });
        }
    }

    public class THolder extends BaseAdapter.MyHolder{

        private ImageView pic;

        public THolder(View itemView) {
            super(itemView);

            pic = (ImageView) itemView.findViewById(R.id.tiezi_inflate_pic); // 图片
        }
    }
}

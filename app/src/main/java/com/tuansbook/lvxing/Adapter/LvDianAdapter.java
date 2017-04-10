package com.tuansbook.lvxing.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tuansbook.lvxing.Activity.ProductXiangQingActivity;
import com.tuansbook.lvxing.Model.Prodruct;
import com.tuansbook.lvxing.R;

import java.util.List;

/**
 * Created by Administrator on 2017/3/11.
 * 侣店Adapter
 */
public class LvDianAdapter extends BaseAdapter<Prodruct> {

    private Context context;

    public LvDianAdapter(Context context, List<Prodruct> prodructList){
        this.context = context;
        addData(prodructList);
    }

    @Override
    public RecyclerView.ViewHolder createHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.lvdian_inflate,parent,false);
        PHolder pHolder = new PHolder(view);
        return pHolder;
    }

    @Override
    public void bindHolder(RecyclerView.ViewHolder holder, int position, Prodruct data) {

        if(holder instanceof PHolder){
            Glide.with(context).load(data.getPic()).centerCrop().crossFade(200).error(R.mipmap.ic_launcher).into(((PHolder) holder).pic);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProductXiangQingActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }

    /**
     * ViewHolder
     */
    public class PHolder extends BaseAdapter.MyHolder{

        private ImageView pic;

        public PHolder(View itemView) {
            super(itemView);

            pic = (ImageView) itemView.findViewById(R.id.lvdian_inflate_pic); // 图片
        }
    }
}

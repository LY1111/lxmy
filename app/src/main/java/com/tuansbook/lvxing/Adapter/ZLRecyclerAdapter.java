package com.tuansbook.lvxing.Adapter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tuansbook.lvxing.Model.TieZi;
import com.tuansbook.lvxing.R;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Administrator on 2017/3/8.
 * 会员-张梁记
 */
public class ZLRecyclerAdapter extends BaseAdapter<TieZi>{

    private Context context;

    public ZLRecyclerAdapter(Context context, List<TieZi> tieZiList){
        this.context = context;
        addData(tieZiList);
    }

    @Override
    public RecyclerView.ViewHolder createHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.zhangliang_recycler_inflate,parent,false);
        return new ZL(view);
    }

    @Override
    public void bindHolder(final RecyclerView.ViewHolder holder, final int position, TieZi data) {

        if(holder instanceof ZL){
            Glide.with(context).load(data.getPic()).crossFade().centerCrop().error(R.mipmap.ic_launcher)
                    .bitmapTransform(new CropCircleTransformation(context)).into(((ZL) holder).icon);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(holder.itemView,String.valueOf(position),Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * ViewHolder
     */
    public class ZL extends BaseAdapter.MyHolder {

        private ImageView icon;

        public ZL(View itemView) {
            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.zl_inflate_icon); // 头像
        }
    }
}

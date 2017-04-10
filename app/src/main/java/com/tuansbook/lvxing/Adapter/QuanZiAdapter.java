package com.tuansbook.lvxing.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tuansbook.lvxing.Model.TieZi;
import com.tuansbook.lvxing.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/9.
 * 侣友圈-全部圈子
 */
public class QuanZiAdapter extends BaseAdapter<TieZi>{

    private Context context;
    private List<TieZi> tieZiList = new ArrayList<>();
    private int clkp = -1; // 记录点击的位置

    public QuanZiAdapter(Context context, List<TieZi> tieZiList){
        this.context = context;
        this.tieZiList = tieZiList;
    }

    /**
     * 刷新
     * @param tieZiList
     * @param clkp
     */
    public void refreshList(List<TieZi> tieZiList,int clkp){
        this.clkp = clkp;
        this.tieZiList = tieZiList;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder createHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.quan_quan_inflate,parent,false);
        return new MTHolder(view);
    }

    @Override
    public void bindHolder(final RecyclerView.ViewHolder holder, final int position, TieZi data) {

        if(holder instanceof MTHolder){
            Glide.with(context).load(data.getPic()).crossFade().centerCrop().error(R.mipmap.ic_launcher).into(((MTHolder) holder).icon);

            ((MTHolder) holder).guanzhu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MTHolder) holder).guanzhu.setBackgroundResource(R.mipmap.publish);
                    refreshList(tieZiList,position);
                }
            });

            if(clkp >= 0 && clkp == position){
                // 已关注
                ((MTHolder) holder).guanzhu.setBackgroundResource(R.mipmap.publish);
            }else {
                // 未关注
                ((MTHolder) holder).guanzhu.setBackgroundResource(R.drawable.road_a);
            }
        }
    }

    /**
     * ViewHolder
     */
    public class MTHolder extends BaseAdapter.MyHolder {

        private ImageView icon,guanzhu;

        public MTHolder(View itemView) {
            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.quan_quan_pic); // 圈子图标
            guanzhu = (ImageView) itemView.findViewById(R.id.quan_quan_guanzhu); // 关注
        }
    }
}

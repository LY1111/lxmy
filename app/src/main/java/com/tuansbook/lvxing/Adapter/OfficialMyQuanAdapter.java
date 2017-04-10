package com.tuansbook.lvxing.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tuansbook.lvxing.Model.TieZi;
import com.tuansbook.lvxing.R;

import java.util.List;

/**
 * Created by Administrator on 2017/3/9.
 * 侣友圈-官方圈子-我的圈子
 */
public class OfficialMyQuanAdapter extends BaseAdapter<TieZi>{

    private Context context;

    public OfficialMyQuanAdapter(Context context, List<TieZi> tieZiList){
        this.context = context;
        addData(tieZiList);
    }

    @Override
    public RecyclerView.ViewHolder createHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.xiaoquan_inflate,parent,false);
        THolder tHolder = new THolder(view);
        return tHolder;
    }

    @Override
    public void bindHolder(RecyclerView.ViewHolder holder, int position, TieZi data) {

        if(holder instanceof THolder){
            Glide.with(context).load(data.getPic()).centerCrop().crossFade().error(R.mipmap.ic_launcher).into(((THolder) holder).pic);
            ((THolder) holder).name.setText(data.getTitle());
        }
    }

    public class THolder extends BaseAdapter.MyHolder{

        private ImageView pic;
        private TextView name;

        public THolder(View itemView) {
            super(itemView);

            pic = (ImageView) itemView.findViewById(R.id.small_quan_pic); // 图片
            name = (TextView) itemView.findViewById(R.id.small_quan_name); // 名字
        }
    }
}

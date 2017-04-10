package com.tuansbook.lvxing.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tuansbook.lvxing.R;

import java.util.ArrayList;
import java.util.List;

/**
 * PagerAdapter 用于图片滑动
 */
public class ViewPagerAdapter extends PagerAdapter {

    private List<String> imageList = new ArrayList<>();
    private Context context;

    public ViewPagerAdapter(List<String> imageList, Context context){
        this.imageList = imageList;
        this.context = context;
    }

    /**
     * 刷新
     */
    public void refresh(List<String> imageList, Context context){
        this.imageList = imageList;
        this.context = context;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ImageView imageView = new ImageView(context);
        Glide.with(context).load(imageList.get(position)).centerCrop().crossFade().error(R.mipmap.ic_launcher).into(imageView); // 图片不能充分显示 TODO
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
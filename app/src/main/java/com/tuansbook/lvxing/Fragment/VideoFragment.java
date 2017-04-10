package com.tuansbook.lvxing.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tuansbook.lvxing.Activity.KaiTongHYActivity;
import com.tuansbook.lvxing.Adapter.TabViewPagerAdapter;
import com.tuansbook.lvxing.R;
import com.tuansbook.lvxing.Util.MyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/20.
 * 会员
 */
public class VideoFragment extends Fragment {

    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.tab_viewpager,container,false);

        // Toolbar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toobar_toolbar);
        toolbar.setBackgroundColor(Color.BLACK);

        // 会员或开通会员
        LinearLayout left = (LinearLayout) view.findViewById(R.id.toobar_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), KaiTongHYActivity.class);
                startActivity(intent);
            }
        });

        // 头像
        ImageView icon = (ImageView) view.findViewById(R.id.toobar_cancel);
        icon.setVisibility(View.VISIBLE);
        icon.setBackgroundResource(R.mipmap.ic_launcher);

        // 昵称
        TextView nicheng = (TextView) view.findViewById(R.id.toobar_left_text);
        nicheng.setVisibility(View.VISIBLE);
        nicheng.setText("开通会员畅享超值服务!");
        nicheng.setTextColor(MyUtil.getColor(getActivity(),R.color.tao_hong));

        TextView title = (TextView) view.findViewById(R.id.toolbar_title);
        title.setVisibility(View.GONE);

        // 会员
        ImageView huiyuan = (ImageView) view.findViewById(R.id.toolbar_search);
        huiyuan.setBackgroundResource(R.mipmap.ic_launcher);

        // TabLayout
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tablayout_tab);
        tabLayout.setBackgroundColor(Color.WHITE);
        tabLayout.setVisibility(View.VISIBLE);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.tab_viewpager_viewpager);

        tabLayout.setTabTextColors(Color.GRAY,MyUtil.getColor(getActivity(),R.color.small_blue));
        tabLayout.setSelectedTabIndicatorColor(MyUtil.getColor(getActivity(), R.color.small_blue));

        List<String> titles = new ArrayList<>();
        titles.add("精选图片");
        titles.add("专享视频");
        titles.add("张梁屋");

        // 添加Tab
        for (int i = 0; i < titles.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(titles.get(i)));
        }

        fragmentList = new ArrayList<>();
        fragmentList.add(new JingXuanPicFragment());
        fragmentList.add(new ZhuanXiangVideoFragment());
        fragmentList.add(new ZhangLiangFragment());

        TabViewPagerAdapter adapter = new TabViewPagerAdapter(getChildFragmentManager(),fragmentList,titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}

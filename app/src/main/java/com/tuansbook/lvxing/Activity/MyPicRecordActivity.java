package com.tuansbook.lvxing.Activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tuansbook.lvxing.Adapter.TabViewPagerAdapter;
import com.tuansbook.lvxing.Fragment.PicDownFinishFragment;
import com.tuansbook.lvxing.Fragment.PicDownloadingFragment;
import com.tuansbook.lvxing.R;
import com.tuansbook.lvxing.Util.MyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的-图片下载记录
 * Created by Administrator on 2017/3/9
 */
public class MyPicRecordActivity extends AppCompatActivity {

    private Context context;
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_viewpager);
        context = this;

        // 显示底部按钮
        LinearLayout tab_below = (LinearLayout) findViewById(R.id.tab_viewpager_bottom);
        tab_below.setVisibility(View.VISIBLE);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toobar_toolbar);
        toolbar.setBackgroundColor(Color.BLACK);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("图片下载记录");

        ImageView search = (ImageView) findViewById(R.id.toolbar_search);
        search.setVisibility(View.GONE);

        // 返回
        ImageView back = (ImageView) findViewById(R.id.toobar_cancel);
        back.setVisibility(View.VISIBLE);
        back.setBackgroundResource(R.mipmap.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // TabLayout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout_tab);
        tabLayout.setBackgroundColor(Color.WHITE);
        tabLayout.setVisibility(View.VISIBLE);
        ViewPager viewPager = (ViewPager) findViewById(R.id.tab_viewpager_viewpager);

        tabLayout.setTabTextColors(Color.GRAY,MyUtil.getColor(context,R.color.small_blue));
        tabLayout.setSelectedTabIndicatorColor(MyUtil.getColor(context, R.color.small_blue));

        List<String> titles = new ArrayList<>();
        titles.add("正在下载");
        titles.add("下载完成");

        // 添加Tab
        for (int i = 0; i < titles.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(titles.get(i)));
        }

        fragmentList = new ArrayList<>();
        fragmentList.add(new PicDownloadingFragment());
        fragmentList.add(new PicDownFinishFragment());

        TabViewPagerAdapter adapter = new TabViewPagerAdapter(getSupportFragmentManager(),fragmentList,titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}

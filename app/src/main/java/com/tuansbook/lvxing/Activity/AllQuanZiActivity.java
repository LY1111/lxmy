package com.tuansbook.lvxing.Activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuansbook.lvxing.Adapter.TabViewPagerAdapter;
import com.tuansbook.lvxing.Fragment.AllQuanFragment;
import com.tuansbook.lvxing.Fragment.ShenQingingQuanFragment;
import com.tuansbook.lvxing.Fragment.TuiJianQuanFragment;
import com.tuansbook.lvxing.R;
import com.tuansbook.lvxing.Util.MyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/9.
 * 全部圈子
 */
public class AllQuanZiActivity extends AppCompatActivity{

    private Context context;
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_viewpager);
        context = this;

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toobar_toolbar);
        toolbar.setBackgroundColor(Color.BLACK);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("全部圈子");

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

        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabTextColors(Color.GRAY,MyUtil.getColor(context,R.color.small_blue));
        tabLayout.setSelectedTabIndicatorColor(MyUtil.getColor(context, R.color.small_blue));

        List<String> titles = new ArrayList<>();
        titles.add("全部圈子");
        titles.add("推荐圈子");
        titles.add("申请中的圈子");

        // 添加Tab
        for (int i = 0; i < titles.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(titles.get(i)));
        }

        fragmentList = new ArrayList<>();
        fragmentList.add(new AllQuanFragment());
        fragmentList.add(new TuiJianQuanFragment());
        fragmentList.add(new ShenQingingQuanFragment());

        TabViewPagerAdapter adapter = new TabViewPagerAdapter(getSupportFragmentManager(),fragmentList,titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
}

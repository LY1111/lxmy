package com.tuansbook.lvxing.Activity;

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
import com.tuansbook.lvxing.Fragment.XinXianNetFragment;
import com.tuansbook.lvxing.Fragment.XinXianTVFragment;
import com.tuansbook.lvxing.R;
import com.tuansbook.lvxing.Util.MyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/2.
 * 新鲜在线
 */
public class XinXianActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_viewpager);

        // 标题
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("新鲜在线");

        // 隐藏搜索按钮
        ImageView search = (ImageView) findViewById(R.id.toolbar_search);
        search.setVisibility(View.GONE);

        // 返回
        ImageView back = (ImageView) findViewById(R.id.toobar_cancel);
        back.setBackgroundResource(R.mipmap.back);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toobar_toolbar);
        toolbar.setBackgroundColor(Color.BLACK);

        // TabLayout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout_tab);
        ViewPager viewPager = (ViewPager) findViewById(R.id.tab_viewpager_viewpager);

        tabLayout.setVisibility(View.VISIBLE);
        tabLayout.setBackgroundColor(Color.WHITE);

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new XinXianNetFragment());
        fragmentList.add(new XinXianTVFragment());

        List<String> titles = new ArrayList<>(); // 标题
        titles.add("网络版");
        titles.add("TV版");

        tabLayout.addTab(tabLayout.newTab().setText("网络版"));
        tabLayout.addTab(tabLayout.newTab().setText("TV版"));

        tabLayout.setTabTextColors(Color.GRAY, MyUtil.getColor(XinXianActivity.this,R.color.small_blue));
        tabLayout.setSelectedTabIndicatorColor(MyUtil.getColor(XinXianActivity.this,R.color.small_blue));

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

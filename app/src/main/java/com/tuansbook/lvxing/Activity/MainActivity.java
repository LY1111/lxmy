package com.tuansbook.lvxing.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.tuansbook.lvxing.Adapter.TabViewPagerAdapter;
import com.tuansbook.lvxing.Fragment.MyFragment;
import com.tuansbook.lvxing.Fragment.PublishFragment;
import com.tuansbook.lvxing.Fragment.QuanFragment;
import com.tuansbook.lvxing.Fragment.RoadFragment;
import com.tuansbook.lvxing.Fragment.VideoFragment;
import com.tuansbook.lvxing.Model.CustomViewPager;
import com.tuansbook.lvxing.R;
import com.tuansbook.lvxing.Util.MyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页面
 */
public class MainActivity extends AppCompatActivity {

    private Context context;
    private FloatingActionMenu floatingActionMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        // 按钮菜单
        ImageView btn = new ImageView(context);

        // https://github.com/oguzbilgener/CircularFloatingActionMenu
        FloatingActionButton.LayoutParams layoutParams = new FloatingActionButton.LayoutParams(FloatingActionButton.LayoutParams.WRAP_CONTENT,FloatingActionButton.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,0,0,40);

        final FloatingActionButton floatingActionButton = new FloatingActionButton.Builder(MainActivity.this)
                .setContentView(btn).setPosition(5).setLayoutParams(layoutParams).setBackgroundDrawable(R.mipmap.publish).build();
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(MainActivity.this);

        SubActionButton.LayoutParams layoutParams1 = new SubActionButton.LayoutParams(SubActionButton.LayoutParams.WRAP_CONTENT, SubActionButton.LayoutParams.WRAP_CONTENT);

        // 签到
        ImageView qian = new ImageView(context);
        qian.setImageResource(R.mipmap.publish);
        SubActionButton qiandao = itemBuilder.setContentView(qian).build();
        qiandao.setBackground(null);
        qiandao.setLayoutParams(layoutParams1);

        qiandao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                floatingActionMenu.close(true);

                Intent intent = new Intent(context, SignActivity.class);
                startActivity(intent);
            }
        });

        // 发布
        ImageView fa = new ImageView(context);
        fa.setImageResource(R.mipmap.publish);
        SubActionButton fabu = itemBuilder.setContentView(fa).build();
        fabu.setBackground(null);
        fabu.setLayoutParams(layoutParams1);

        fabu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionMenu.close(true);

                Intent intent = new Intent(context, PublishActivity.class);
                startActivity(intent);
            }
        });

        // 商城
        ImageView mai = new ImageView(context);
        mai.setImageResource(R.mipmap.publish);
        SubActionButton maia = itemBuilder.setContentView(mai).build();
        maia.setBackground(null);
        maia.setLayoutParams(layoutParams1);
        maia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                floatingActionMenu.close(true);

                Intent mai = new Intent(context,LikeTaobaoActivity.class);
                startActivity(mai);
            }
        });

        // 可设置初始、结束角度、半径等 位置在上面设定
        floatingActionMenu = new FloatingActionMenu.Builder(MainActivity.this)
                .addSubActionView(qiandao).addSubActionView(fabu).addSubActionView(maia).attachTo(floatingActionButton).setStartAngle(220).setEndAngle(320).build();

        final List<String> titles = new ArrayList<>(); // Fragment标题
        List<Fragment> fragmentList = new ArrayList<>(); // FragmentList

        titles.clear();
        titles.add("在路上");
        titles.add("会员");
        titles.add(""); // 发布只有图标
        titles.add("侣友圈");
        titles.add("我的");

        // Tablayout图标
        int[] icons = new int[5];
        icons[0] = R.drawable.road_icon_selector;
        icons[1] = R.drawable.video_icon_selector;
        icons[3] = R.drawable.quan_icon_selector;
        icons[4] = R.drawable.my_icon_selector;

        fragmentList.clear();
        fragmentList.add(new RoadFragment());
        fragmentList.add(new VideoFragment());
        fragmentList.add(new PublishFragment());
        fragmentList.add(new QuanFragment());
        fragmentList.add(new MyFragment());

        CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.main_view_pager);
        viewPager.setAdapter(new TabViewPagerAdapter(getSupportFragmentManager(),fragmentList,titles));

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tablayout);
        for (int i = 0; i < titles.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(titles.get(i)));
        }

        // 未选中和选中颜色
        tabLayout.setTabTextColors(Color.WHITE, MyUtil.getColor(this,R.color.tab_selected));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(final TabLayout.Tab tab) {

                if(tab.getText().toString().equals(titles.get(0))){ // 在路上
                }else if(tab.getText().toString().equals(titles.get(1))){ // 会员
                }else if(tab.getText().toString().equals(titles.get(3))){ // 侣友圈
                }else if(tab.getText().toString().equals(titles.get(4))){ // 我的
                }else{ // 发布
                }
                Log.i("OnTabSelectedListener：",tab.getText().toString());

                if(floatingActionMenu.isOpen()){
                    floatingActionMenu.close(true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        tabLayout.setupWithViewPager(viewPager);

        // 限制发布TabLayout的点击
        TabLayout.Tab tab2 = tabLayout.getTabAt(2);
        if(tab2 != null){
            tab2.setCustomView(R.layout.tab_not_use);
            View view = (View) tab2.getCustomView().getParent();
            view.setClickable(false);
        }

        // setupWithViewPager目前不支持图标 必须在setupWithViewPager之后设置图标才可以 或者不用setupWithViewPager 采用另一种写法
        // http://m.blog.csdn.net/article/details?id=53943762
        // 设置Tablayout图标
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if(i != 2){
                tab.setIcon(icons[i]);
            }
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();

        if(floatingActionMenu != null && floatingActionMenu.isOpen()){
            floatingActionMenu.close(true);
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
}

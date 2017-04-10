package com.tuansbook.lvxing.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.tuansbook.lvxing.Activity.ModifyInformationActivity;
import com.tuansbook.lvxing.Activity.MyCollectionActivity;
import com.tuansbook.lvxing.Activity.MyFansActivity;
import com.tuansbook.lvxing.Activity.MyFocusActivity;
import com.tuansbook.lvxing.Activity.MyNoticeActivity;
import com.tuansbook.lvxing.Activity.MyPicRecordActivity;
import com.tuansbook.lvxing.Activity.MySettingActivity;
import com.tuansbook.lvxing.Activity.MyTieZiActivity;
import com.tuansbook.lvxing.R;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Administrator on 2017/2/20.
 * 我的
 */
public class MyFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.content_my,container,false);

        ImageView background = (ImageView) view.findViewById(R.id.my_bac); // 背景图片
        ImageView icon = (ImageView) view.findViewById(R.id.my_icon); // 头像

        Glide.with(getActivity()).load("http://pic.baike.soso.com/p/20131221/20131221101135-1355890959.jpg").centerCrop().crossFade()
                .bitmapTransform(new BlurTransformation(getActivity(),14)).into(background); // 模糊效果
        Glide.with(getActivity()).load("http://pic.baike.soso.com/p/20131221/20131221101135-1355890959.jpg").centerCrop().crossFade()
                .bitmapTransform(new CropCircleTransformation(getActivity())).crossFade().into(icon);

        LinearLayout tiezi = (LinearLayout) view.findViewById(R.id.my_tiezi); // 帖子
        LinearLayout focus = (LinearLayout) view.findViewById(R.id.my_focus); // 关注
        LinearLayout fensi = (LinearLayout) view.findViewById(R.id.my_fans); // 粉丝
        LinearLayout news = (LinearLayout) view.findViewById(R.id.my_news); // 通知
        LinearLayout collection = (LinearLayout) view.findViewById(R.id.my_collection); // 收藏
        LinearLayout setting = (LinearLayout) view.findViewById(R.id.my_setting); // 设置
        LinearLayout pic_download = (LinearLayout) view.findViewById(R.id.my_pic_record); // 图片下载记录
        LinearLayout info = (LinearLayout) view.findViewById(R.id.my_info); // 修改个人信息

        tiezi.setOnClickListener(this);
        focus.setOnClickListener(this);
        fensi.setOnClickListener(this);
        news.setOnClickListener(this);
        collection.setOnClickListener(this);
        setting.setOnClickListener(this);
        pic_download.setOnClickListener(this);
        info.setOnClickListener(this);

        return view;
    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {

        Intent intent;
        switch (v.getId()){

            case R.id.my_tiezi: // 我的帖子
                intent = new Intent(getActivity(),MyTieZiActivity.class);
                startActivity(intent);
                break;
            case R.id.my_focus: // 我的关注
                intent = new Intent(getActivity(),MyFocusActivity.class);
                startActivity(intent);
                break;
            case R.id.my_fans: // 我的粉丝
                intent = new Intent(getActivity(),MyFansActivity.class);
                startActivity(intent);
                break;
            case R.id.my_news: // 我的通知
                intent = new Intent(getActivity(),MyNoticeActivity.class);
                startActivity(intent);
                break;
            case R.id.my_collection: // 我的收藏
                intent = new Intent(getActivity(),MyCollectionActivity.class);
                startActivity(intent);
                break;
            case R.id.my_setting: // 我的设置
                intent = new Intent(getActivity(),MySettingActivity.class);
                startActivity(intent);
                break;
            case R.id.my_pic_record: // 图片下载记录
                intent = new Intent(getActivity(),MyPicRecordActivity.class);
                startActivity(intent);
                break;
            case R.id.my_info: // 修改个人信息
                intent = new Intent(getActivity(),ModifyInformationActivity.class);
                startActivity(intent);
            default:
                break;
        }
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


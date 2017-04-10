package com.tuansbook.lvxing.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.tuansbook.lvxing.R;
import com.tuansbook.lvxing.Util.AESUtils;
import com.tuansbook.lvxing.Util.PhotoUtils;

import static com.tuansbook.lvxing.R.id.pic1;

/**
 * Created by Administrator on 2017/3/13.
 * 签到
 */
public class SignActivity extends AppCompatActivity {

    private Context context;
    private MediaRecorder mediaRecorder = new MediaRecorder(); // 录音
    private SimpleExoPlayer simpleExoPlayer;
    private boolean isPlaying = false;
    private DataSource.Factory dataSourceFactory;
    private ExtractorsFactory extractorsFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign);
        context = this;

        // 1：网络图片下载到本地并且压缩存储。
        // 2: 图片按照比例重绘。
        // 3: 加水印，水印自动换行。
        // 4：腾讯视频、Bilibili、语音。
        // 5：类似微信那种录制视频和语音的方案。
        ImageView pic = (ImageView) findViewById(pic1);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // 按指定宽高缩放图片(附带压缩)
        Bitmap tmp = PhotoUtils.shrinkRatable(context,metrics.widthPixels,pic.getLayoutParams().height,BitmapFactory.decodeResource(getResources(),R.mipmap.qs));
        // 保存到本地
        String uuu = PhotoUtils.compressBitmap(tmp);
        Glide.with(context).load(uuu).centerCrop().crossFade().error(R.mipmap.ic_launcher).into(pic);

        final ImageView pic2 = (ImageView) findViewById(R.id.pic2);

        TextView waterMark = (TextView) findViewById(R.id.shui);
        waterMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 添加水印
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap waterMark = PhotoUtils.createWatermark(context,BitmapFactory.decodeResource(getResources(),R.mipmap.login_bg),
                                "小包子,白又白,两只耳朵竖起来,爱吃萝卜爱吃菜.小包子,白又白,两只耳朵竖起来,爱吃萝卜爱吃菜.");
                        pic2.setImageBitmap(PhotoUtils.shrinkRatable(context,pic2.getWidth(),pic2.getHeight(),waterMark));
                    }
                });
            }
        });

        // 录音
        Button luyin = (Button) findViewById(R.id.sign_luyin);
        luyin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 声音来源为麦克风
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            }
        });

        // 播放
        Button play = (Button) findViewById(R.id.sign_bofang);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TrackSelector
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        MediaSource source = new ExtractorMediaSource(Uri.parse("http://139.129.193.65:812/1.mp3"), dataSourceFactory, extractorsFactory, null, null);
                        simpleExoPlayer.prepare(source);

                        if(isPlaying){
                            simpleExoPlayer.setPlayWhenReady(false);
                            isPlaying = false;
                        }else {
                            simpleExoPlayer.setPlayWhenReady(true);
                            isPlaying = true;
                        }
                    }
                });

            }
        });

        // 测试加密解密
        String encrypt = AESUtils.encrypt("12345");
        String decrypt = AESUtils.decrypt(encrypt);

    }

    /**
     * Handler
     */
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onResume(){
        super.onResume();

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory factory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(factory);
        LoadControl loadControl = new DefaultLoadControl();
        // SimpleExoPlayer
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context,trackSelector,loadControl);

        dataSourceFactory = new DefaultDataSourceFactory(context,"DataSource");
        extractorsFactory = new DefaultExtractorsFactory();
    }

    @Override
    protected void onPause(){
        super.onPause();

        // 释放SimpleExoPlayer
        if(simpleExoPlayer != null){
            simpleExoPlayer.release();
            isPlaying = false;
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
}

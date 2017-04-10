package com.tuansbook.lvxing.Activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuansbook.lvxing.Adapter.BaseAdapter;
import com.tuansbook.lvxing.Model.TieZi;
import com.tuansbook.lvxing.R;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMoveListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/3/13.
 * 发布 Recycler可拖动
 */
public class PublishActivity extends AppCompatActivity {

    private Context context;
    private SwipeMenuRecyclerView recyclerView;
    private List<TieZi> tieZiList = new ArrayList<>();
    private PublishAdapter adapter;
    private NestedScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish);
        context = this;

        final TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("编辑帖子");

        // 隐藏搜索按钮
        ImageView search = (ImageView) findViewById(R.id.toolbar_search);
        search.setVisibility(View.GONE);

        TextView publish = (TextView) findViewById(R.id.right_text);
        publish.setVisibility(View.VISIBLE);
        publish.setText("发布");

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

        scrollView = (NestedScrollView) findViewById(R.id.publish_nested);

        // 去掉RecyclerView的滑动属性 重写LinearLayoutManager的canScrollVertically方法 解决ScrollView和Recycler的滑动冲突问题
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        recyclerView = (SwipeMenuRecyclerView) findViewById(R.id.publish_swipe_menu_recycler);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setLongPressDragEnabled(true);

        adapter = new PublishAdapter();
        recyclerView.setAdapter(adapter);

        // 拖拽
        recyclerView.setOnItemMoveListener(new OnItemMoveListener() {
            @Override
            public boolean onItemMove(int fromPosition, int toPosition) {
                Collections.swap(tieZiList,adapter.getRealPosition(fromPosition),adapter.getRealPosition(toPosition));
                adapter.notifyItemMoved(adapter.getRealPosition(fromPosition),adapter.getRealPosition(toPosition));
                return true;
            }

            @Override
            public void onItemDismiss(int position) {
            }
        });

        // 初始化
        TieZi tieZi = new TieZi();
        tieZi.setTitle("");
        tieZi.setType(3); // 初始化的时候文字、图片、视频全部显示
        tieZiList.add(tieZi);
        adapter.refreshList(tieZiList);

        // 添加文字
        final TextView addText = (TextView) findViewById(R.id.new_text_publish);
        addText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TieZi tieZi = new TieZi();
                tieZi.setTitle("");
                tieZi.setType(0); // 文字：0 图片：1 视频：2
                tieZiList.add(tieZi);
                adapter.refreshList(tieZiList);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(NestedScrollView.FOCUS_DOWN); // 滚动到底部 有可能View还没刷新scrollView就滚动到底部了 所以设个延时
                    }
                },200);
            }
        });

        // 添加图片
        final TextView addPic = (TextView) findViewById(R.id.new_pic_publish);
        addPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TieZi tieZi = new TieZi();
                tieZi.setTitle("");
                tieZi.setType(1); // 文字：0 图片：1 视频：2
                tieZiList.add(tieZi);
                adapter.refreshList(tieZiList);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(NestedScrollView.FOCUS_DOWN); // 滚动到底部 有可能View还没刷新scrollView就滚动到底部了 所以设个延时
                    }
                },200);
            }
        });

        // 添加视频
        final TextView addVideo = (TextView) findViewById(R.id.new_video_publish);
        addVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TieZi tieZi = new TieZi();
                tieZi.setTitle("");
                tieZi.setType(2); // 文字：0 图片：1 视频：2
                tieZiList.add(tieZi);
                adapter.refreshList(tieZiList);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(NestedScrollView.FOCUS_DOWN); // 滚动到底部 有可能View还没刷新scrollView就滚动到底部了 所以设个延时
                    }
                },200);
            }
        });

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

    /**
     * Adapter
     */
    private class PublishAdapter extends BaseAdapter<TieZi> {

        @Override
        public RecyclerView.ViewHolder createHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(context).inflate(R.layout.publish_inflate,parent,false);
            Pub pub = new Pub(view);
            return pub;
        }

        @Override
        public void bindHolder(final RecyclerView.ViewHolder holder, final int position, TieZi data) {

            if(holder instanceof Pub){

                // 区分添加的是什么 文字：0 图片：1 视频：2
                if(data.getType() == 0){
                    ((Pub) holder).content.setVisibility(View.VISIBLE);
                    ((Pub) holder).add_pic.setVisibility(View.GONE);
                    ((Pub) holder).add_video.setVisibility(View.GONE);
                }else if(data.getType() == 1){
                    ((Pub) holder).content.setVisibility(View.GONE);
                    ((Pub) holder).add_pic.setVisibility(View.VISIBLE);
                    ((Pub) holder).add_video.setVisibility(View.GONE);
                }else if(data.getType() == 2){
                    ((Pub) holder).content.setVisibility(View.GONE);
                    ((Pub) holder).add_pic.setVisibility(View.GONE);
                    ((Pub) holder).add_video.setVisibility(View.VISIBLE);
                }else if(data.getType() == 3){
                    ((Pub) holder).content.setVisibility(View.VISIBLE);
                    ((Pub) holder).add_pic.setVisibility(View.VISIBLE);
                    ((Pub) holder).add_video.setVisibility(View.VISIBLE);
                }

                // 删除
                ((Pub) holder).delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tieZiList.remove(position);
                        refreshList(tieZiList);
                    }
                });

            }
        }

        /**
         * ViewHolder
         */
        private class Pub extends BaseAdapter.MyHolder {

            private CardView delete,pic_desc,video_desc;
            private ImageView pic,video;
            private EditText content,pic_con,video_con;
            private FrameLayout add_pic,add_video;

            public Pub(View itemView) {
                super(itemView);

                delete = (CardView) itemView.findViewById(R.id.publish_inflate_delete); // 删除
                pic_desc = (CardView) itemView.findViewById(R.id.publish_inflate_picc); // 添加图片描述
                video_desc = (CardView) itemView.findViewById(R.id.publish_inflate_videoc); // 添加视频描述
                pic = (ImageView) itemView.findViewById(R.id.publish_inflate_pic); // 图片
                video = (ImageView) itemView.findViewById(R.id.publish_inflate_video); // 视频缩略图
                content = (EditText) itemView.findViewById(R.id.publish_inflate_content); // 正文
                pic_con = (EditText) itemView.findViewById(R.id.publish_inflate_picd); // 图片注释
                video_con = (EditText) itemView.findViewById(R.id.publish_inflate_videod); // 视频注释
                add_pic = (FrameLayout) itemView.findViewById(R.id.publish_kind_pic); // 添加图片
                add_video = (FrameLayout) itemView.findViewById(R.id.publish_kind_video); // 添加视频
            }
        }
    }

    /**
     * handler
     */
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
        }
    };

}

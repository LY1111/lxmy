package com.tuansbook.lvxing.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tuansbook.lvxing.Common.MyApplication;
import com.tuansbook.lvxing.Model.Photo;
import com.tuansbook.lvxing.Model.PhotoFolder;
import com.tuansbook.lvxing.R;
import com.tuansbook.lvxing.Util.MyUtil;
import com.tuansbook.lvxing.Util.PhotoUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/10.
 * 选择照片_照相
 */
public class Select_or_Picture_Activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayout back,below;
    private TextView yulan,publish,xiangce;
    private List<Photo> fileList = new ArrayList<>(); // 相册图片
    private XiangCeAdapter recyclerAdapter;
    private String path = "";
    private Uri uri = null;
    private Map<String,PhotoFolder> photoFolderMap = new HashMap<>();
    private List<Photo> selected = new ArrayList<>();
    private int chosen = 0; // 已选中的数量
    private PopupWindow popupWindow;
    private String current_xiangce = "所有图片"; // 当前相册地址
    private Context context;

    @Override
    protected void onCreate(Bundle savesInstanceState){
        super.onCreate(savesInstanceState);
        setContentView(R.layout.select_or_picture);
        context = this;

        // 获取读写权限
        AndPermission.with(this).requestCode(101).permission(Manifest.permission.READ_EXTERNAL_STORAGE
                ,Manifest.permission.WRITE_EXTERNAL_STORAGE).send();

        recyclerView = (RecyclerView) findViewById(R.id.select_or_picture_recycler); // RecyclerView
        back = (LinearLayout) findViewById(R.id.select_or_picture_back); // 返回
        publish = (TextView) findViewById(R.id.select_or_picture_publish); // 发布
        xiangce = (TextView) findViewById(R.id.select_or_picture_xiangce); // 相册
        yulan = (TextView) findViewById(R.id.select_or_picture_yulan); // 预览
        yulan.setVisibility(View.GONE);
        below = (LinearLayout) findViewById(R.id.xiangce_below);

        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // 返回
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 预览
        yulan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 全屏 暂时没做
            }
        });

        xiangce.setText("所有图片");
        // 相册选择
        xiangce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(context);
                View view = inflater.inflate(R.layout.pop,null);

                RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.pop_inflate_recycler);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                List<PhotoFolder> folders = new ArrayList<>();
                List<PhotoFolder> newFolders = new ArrayList<>();

                for(String key:photoFolderMap.keySet()){


                    if(photoFolderMap.get(key).getName().equals("所有图片")){
                        newFolders.add(photoFolderMap.get(key));
                    }else {
                        folders.add(photoFolderMap.get(key));
                    }
                }

                Collections.sort(folders, new Comparator<PhotoFolder>() {
                    @Override
                    public int compare(PhotoFolder lhs, PhotoFolder rhs) {

                        if(lhs.getName().compareTo(rhs.getName())>0){
                            return 1;
                        }else {
                            return -1;
                        }
                    }
                });
                newFolders.addAll(folders);

                XiangceSelectorAdapter Xiangce = new XiangceSelectorAdapter(newFolders);
                recyclerView.setAdapter(Xiangce);

                popupWindow = new PopupWindow(view,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                popupWindow.setOutsideTouchable(true);
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
                    popupWindow.setBackgroundDrawable(new BitmapDrawable());
                }
                popupWindow.setHeight(1460);
                popupWindow.setFocusable(true);
                popupWindow.update();
                popupWindow.showAtLocation(xiangce, Gravity.LEFT|Gravity.BOTTOM,0,below.getHeight());
            }
        });

        // 图片选择完成
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selected.size() == 0){
                    Snackbar.make(publish,"请选择图片", Snackbar.LENGTH_SHORT).show();
                }else {
                    MyApplication.deliver.put("selected_pic_wechat",selected);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){

        switch (requestCode){
            case 0: // 选择图片
                if(data!=null){
                    uri = data.getData();
                    if(uri !=null){
                        path = uri.getPath();
                        Log.i("选择图片",path);
                        path = MyUtil.getPath(context,uri);
                        Log.i("选择图片_处理Uri为String",path);
                    }else {
                        Bundle bundle = data.getExtras();
                        if(bundle !=null){
                            Bitmap bitmap = bundle.getParcelable("data");
                            if(bitmap !=null){
                                try {
                                    // 压缩后保存本地
                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG,20,byteArrayOutputStream);
                                    byteArrayOutputStream.flush();

                                    if(byteArrayOutputStream !=null){
                                        byteArrayOutputStream.close();
                                    }

                                    // 再压缩？
                                    path = PhotoUtils.compressBitmap(bitmap);
                                    Log.i("压缩后路径",path);

                                    // Do Something Else

                                } catch (Exception e) {
                                    if(!bitmap.isRecycled()){
                                        bitmap.recycle();
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    Photo photo = new Photo();
                    photo.setCamera(true);
                    photo.setPath(path);
                    selected.add(photo);
                    MyApplication.deliver.put("selected_pic_wechat",selected);
                    finish();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 获取图片
     */
    private class getPic extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            fileList.clear();
            selected.clear();
            chosen = 0;
            Photo photo = new Photo(); // 相机拍照
            fileList.add(photo);

            photoFolderMap = PhotoUtils.getPics(getApplicationContext());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // 默认加载所有图片
            if(photoFolderMap != null){
                fileList.addAll(photoFolderMap.get("所有图片").getPhotoList());
            }
            recyclerAdapter = new XiangCeAdapter();
            recyclerView.setAdapter(recyclerAdapter);
        }
    }

    /**
     * 获取权限回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] results){
        AndPermission.onRequestPermissionsResult(requestCode, permissions, results, new PermissionListener() {
            @Override
            public void onSucceed(int requestCode, List<String> grantPermissions) {
                new getPic().execute(); // 获取图片
            }

            @Override
            public void onFailed(int requestCode, List<String> deniedPermissions) {
                Snackbar.make(publish,"权限申请失败", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    /**
     * 相册Adapter
     */
    private class XiangCeAdapter extends RecyclerView.Adapter<XiangCeAdapter.XiangCe>{

        public XiangCeAdapter(){
        }

        public void refresh(){
            this.notifyDataSetChanged();
        }

        @Override
        public XiangCe onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.select_or_picture_inflate,parent,false);
            XiangCe xiangCe = new XiangCe(view);
            return xiangCe;
        }

        @Override
        public void onBindViewHolder(final XiangCe holder, final int position) {

            // Pic
            if(fileList.get(position).getPath() == null || fileList.get(position).getPath().equals("")){
                Glide.with(context).load(R.mipmap.ic_launcher).centerCrop().crossFade().into(holder.pic);
                holder.checkBox.setVisibility(View.GONE);
            }else {
                holder.checkBox.setVisibility(View.VISIBLE);
                Glide.with(context).load(new File(fileList.get(position).getPath())).centerCrop().crossFade().into(holder.pic);
            }

            holder.pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 拍照
                    if(position == 0){
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent,0,null);
                        // 更新选中状态
                    }else {
                        if(selected.contains(fileList.get(position))){
                            selected.remove(fileList.get(position));
                            chosen -= 1;
                        }else {
                            chosen += 1;
                            selected.add(fileList.get(position));
                        }

                        if(chosen > MyApplication.pic_no){
                            chosen -= 1;
                            selected.remove(fileList.get(position));
                            Snackbar.make(publish,"最多选择" + String.valueOf(MyApplication.pic_no) + "张图片", Snackbar.LENGTH_SHORT).show();
                        }else {
                            if(chosen ==0){
                                publish.setText("确定");
                            }else {
                                publish.setText("确定(" + chosen + "/" + String.valueOf(MyApplication.pic_no) + ")");
                            }
                            refresh();
                        }
                    }
                }
            });

            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selected.contains(fileList.get(position))){
                        selected.remove(fileList.get(position));
                        chosen -= 1;
                    }else {
                        chosen += 1;
                        selected.add(fileList.get(position));
                    }

                    if(chosen > MyApplication.pic_no){
                        chosen -= 1;
                        selected.remove(fileList.get(position));
                        Snackbar.make(publish,"最多选择" + String.valueOf(MyApplication.pic_no) + "张图片", Snackbar.LENGTH_SHORT).show();
                    }else {
                        if(chosen ==0){
                            publish.setText("确定");
                        }else {
                            publish.setText("确定(" + chosen + "/" + String.valueOf(MyApplication.pic_no) + ")");
                        }
                        refresh();
                    }
                }
            });

            if(selected.size() >0){ // 刷新选中状态
                if(selected.contains(fileList.get(position))){
                    holder.checkBox.setChecked(true);
                }else {
                    holder.checkBox.setChecked(false);
                }
            }
        }

        @Override
        public int getItemCount() {
            return fileList!=null && fileList.size()>0?fileList.size():0;
        }

        public class XiangCe extends RecyclerView.ViewHolder {

            ImageView pic;
            CheckBox checkBox;
            public XiangCe(View itemView) {
                super(itemView);

                pic = (ImageView) itemView.findViewById(R.id.s_or_p_pic); // 图片
                checkBox = (CheckBox) itemView.findViewById(R.id.s_or_p_checkbox); // CheckBox
            }
        }
    }

    /**
     * 选择相册PopupWindow
     */
    private class XiangceSelectorAdapter extends RecyclerView.Adapter<XiangceSelectorAdapter.CC>{

        private List<PhotoFolder> photoFolders = new ArrayList<>(); // 相册名称

        public XiangceSelectorAdapter(List<PhotoFolder> photoFolders){
            this.photoFolders = photoFolders;
        }

        @Override
        public CC onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.xiangce_pop_inflate,parent,false);
            CC CC = new CC(view);
            return CC;
        }

        @Override
        public void onBindViewHolder(final CC holder, final int position) {

            List<Photo> photos = photoFolders.get(position).getPhotoList();
            holder.num.setText(String.valueOf(photos!=null && photos.size()>0?photos.size():0) + "张");
            holder.title.setText(photoFolders.get(position).getName());
            Glide.with(context).load(photos!=null && photos.size()>0?photos.get(0).getPath():"").centerCrop().crossFade().into(holder.pic);

            // 点击相册进入相应相册选择页面
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.checkBox.setChecked(true);
                    fileList.clear();
                    selected.clear();
                    chosen = 0;
                    Photo photo = new Photo(); // 相机拍照
                    fileList.add(photo);
                    xiangce.setText(holder.title.getText().toString());

                    // TODO
                    fileList.addAll(photoFolders.get(position).getPhotoList());
                    current_xiangce = photoFolders.get(position).getDirPath();
                    recyclerAdapter.refresh();
                    popupWindow.dismiss();
                }
            });

            // 当前选择的相册
            if(photoFolders.get(position).getDirPath().equals(current_xiangce)){
                holder.checkBox.setChecked(true);
            }else {
                holder.checkBox.setChecked(false);
            }
        }

        @Override
        public int getItemCount() {
            return photoFolders!=null && photoFolders.size()>0?photoFolders.size():0;
        }

        public class CC extends RecyclerView.ViewHolder {

            TextView title,num;
            CheckBox checkBox;
            ImageView pic;

            public CC(View itemView) {
                super(itemView);

                title = (TextView) itemView.findViewById(R.id.xiangce_pop_title); // 相册名称
                pic = (ImageView) itemView.findViewById(R.id.xiangce_pop_background); // 相册图片
                num = (TextView) itemView.findViewById(R.id.xiangce_pop_num); // 图片数目
                checkBox = (CheckBox) itemView.findViewById(R.id.xiangce_pop_checkbox); // 是否选中
            }
        }
    }

}
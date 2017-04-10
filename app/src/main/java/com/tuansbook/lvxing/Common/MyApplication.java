package com.tuansbook.lvxing.Common;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/2/22.
 */
public class MyApplication extends Application {

    public static HashMap<String,Object> deliver = new HashMap<>(); // 数据传递
    public static int pic_no = 9; // 图片最大选择数量
    public static final int IMAGE_SIZE = 60; // 如果图片大于此KB，则压缩
    public static String PIC_PATH = ""; // 发送图片-图片压缩后存放位置
    private Context context;

    @Override
    public void onCreate(){
        super.onCreate();
        context = this;

        try {

            // 创建压缩图片存放文件夹
            if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()){
                 // /storage/emulated/0/Android/data/com.tuansbook.lvxing/img
//                PIC_PATH = context.getExternalCacheDir().getAbsolutePath();
                PIC_PATH = Environment.getExternalStorageDirectory().getPath() + "/lvxing/img";
                Log.i("缓存:",PIC_PATH);
            }else {
                // /data/user/0/com.tuansbook.lvxing/img
                PIC_PATH = context.getCacheDir().getAbsolutePath();
            }

            File file = new File(PIC_PATH);
            if(!file.exists()){
                file.mkdirs();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

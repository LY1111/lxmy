package com.tuansbook.lvxing.Util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.tuansbook.lvxing.Common.MyApplication;
import com.tuansbook.lvxing.Model.Photo;
import com.tuansbook.lvxing.Model.PhotoFolder;
import com.tuansbook.lvxing.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/13.
 * 图片工具类(压缩、按比例重绘、保存到本地、加水印、获取本地所有相册及所有图片)
 */
public class PhotoUtils {

    /**
     * 获取所有图片及各个相册的图片列表
     * @param context
     * @return
     */
    public static Map<String,PhotoFolder> getPics(Context context) {

        Map<String,PhotoFolder> picsMap = new HashMap<>();

        // 所有图片的PhotoFolder
        PhotoFolder photoFolder = new PhotoFolder();
        String allphoto = "所有图片";
        photoFolder.setName(allphoto);
        photoFolder.setDirPath(allphoto);
        photoFolder.setPhotoList(new ArrayList<Photo>());

        picsMap.put(allphoto,photoFolder); // 所有图片

        // 图片Uri
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI; // SD卡上的图片文件
        ContentResolver contentResolver = context.getContentResolver();

        Cursor cursor = contentResolver.query(uri,null, MediaStore.Images.Media.MIME_TYPE + " in(?,?)",new String[]{"image/jpeg","image/png"}, MediaStore.Images.Media.DATE_MODIFIED + " desc");

        int pathIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

        // 获取数据
        if (cursor.moveToFirst()){
            do{
                String path = cursor.getString(pathIndex);

                // 图片的父路径
                File parentFile = new File(path).getParentFile();
                if(parentFile == null){
                    continue;
                }

                String parentDir = parentFile.getAbsolutePath();

                if(picsMap.containsKey(parentDir)){

                    Photo photo = new Photo();
                    photo.setPath(path);

                    // 把照片加入PhotoFolder
                    picsMap.get(parentDir).getPhotoList().add(photo);
                    // 所有照片中增加
                    picsMap.get(allphoto).getPhotoList().add(photo);
                }else {
                    // 新增PhotoFolder
                    PhotoFolder newPhotoFolder = new PhotoFolder();
                    newPhotoFolder.setPhotoList(new ArrayList<Photo>());
                    Photo photo = new Photo();
                    photo.setPath(path);
                    // 增加照片
                    newPhotoFolder.getPhotoList().add(photo);
                    newPhotoFolder.setDirPath(parentDir);
                    newPhotoFolder.setName(parentDir.substring(parentDir.lastIndexOf(File.separator) + 1));
                    // 所有照片中增加
                    picsMap.get(allphoto).getPhotoList().add(photo);
                    picsMap.put(parentDir,newPhotoFolder);

                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        return picsMap;
    }

    /**
     * 为图片添加文字水印
     * @param context
     * @param bitmap
     * @param markText
     * @return Bitmap
     */
    public static Bitmap createWatermark(Context context, Bitmap bitmap, String markText) {

        // 获取图片的宽高
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        // 创建一个和图片一样大的背景图
        Bitmap bmp = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        // 画背景图
        canvas.drawBitmap(bitmap, 0, 0, null);

        //-------------开始绘制文字-------------------------------

        if (!TextUtils.isEmpty(markText)) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            int screenWidth = displayMetrics.widthPixels;
            float textSize = MyUtil.dip2px(context,19) * bitmapWidth / screenWidth;

            // 创建画笔
            TextPaint mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            // 文字矩阵区域
            Rect textBounds = new Rect();
            mPaint.setFilterBitmap(true);
            mPaint.setDither(true);
            // 水印的字体大小
            mPaint.setTextSize(textSize);
            // 水印的区域
            mPaint.getTextBounds(markText, 0, markText.length(), textBounds);
            // 水印的颜色
            mPaint.setColor(MyUtil.getColor(context, R.color.colorAccent));

            // StaticLayout
            StaticLayout layout = new StaticLayout(markText, 0, markText.length(), mPaint, (int) (bitmapWidth - textSize),
                    Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.5F, true);

            // 文字开始的坐标
            float textX = MyUtil.dip2px(context,10) * bitmapWidth / screenWidth;
            float textY = bitmapHeight * 2 / 3;//图片的中间

            // 画文字
            canvas.translate(textX, textY);
            layout.draw(canvas);
        }

        //保存所有元素
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        return bmp;
    }

    /**
     * 压缩图片保存到本地
     */
    public static String compressBitmap(Bitmap bitmap){

        String res = "";

        if(bitmap!=null){
            try{
                Bitmap afterCompress;
                // 通过压缩质量方式压缩
                afterCompress = compressByQuality(bitmap);
                long time = System.currentTimeMillis();
                res = MyApplication.PIC_PATH + "/" + time+".jpg";

                FileOutputStream out = new FileOutputStream(res);
                System.out.println("压缩后图片存放位置:"+res);
                afterCompress.compress(Bitmap.CompressFormat.JPEG, 50, out);
                out.flush();
                out.close();
            }catch(Exception e){
                e.printStackTrace();
                System.out.println("压缩图像失败:"+e.getMessage());
            }
        }else{
            System.out.println("无法获取Bitmap图像");
        }

        return res;
    }

    /**
     * 按照控件大小等比例缩小图片
     * @param targetWidth
     * @param targetHeight
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap shrinkRatable(Context context,int targetWidth,int targetHeight,Bitmap bitmap) {

        // 屏幕宽高
        // bitmap写入到byteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);

        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 不去真的解析图片，只是获取图片的头部信息，包含宽高等
        newOpts.inJustDecodeBounds = true;

        bitmap = BitmapFactory.decodeByteArray(byteArrayOutputStream.toByteArray(),0,byteArrayOutputStream.toByteArray().length,newOpts);

        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        Log.i("原图片宽高:",String.valueOf(w) + "," + String.valueOf(h));

        float ww = targetWidth;
        float hh = targetHeight;

        Log.i("目标图片宽高:",String.valueOf(ww) + "," + String.valueOf(hh));

        int widthBili = 0;
        int heightBili = 0;
        int widtargetbili = 0;
        // 分别计算图片宽度、高度与目标宽度、高度的比例；取大于该比例的最小整数
        widthBili = (int) Math.ceil(newOpts.outWidth / ww);
        heightBili = (int) Math.ceil(newOpts.outHeight / hh);

        if(targetWidth > targetHeight){
            widtargetbili = (int) Math.ceil(targetWidth / (float)targetHeight);
        }else {
            widtargetbili = (int) Math.ceil(targetHeight / (float)targetWidth);
        }

        Log.i("宽高比:",String.valueOf(newOpts.outWidth / ww) + "," + String.valueOf(newOpts.outHeight / hh));
        Log.i("宽高比取整:",String.valueOf(widthBili) + "," + String.valueOf(heightBili));
        Log.i("目标图片宽高比:",String.valueOf(widtargetbili));
        if(widthBili > 1 || heightBili > 1){
            if(widthBili > heightBili){
                // 设置缩放比例
                if(widthBili > widtargetbili){
                    newOpts.inSampleSize = widtargetbili;
                }else {
                    newOpts.inSampleSize = widtargetbili;
                }
            }else {
                if(heightBili > widtargetbili){
                    newOpts.inSampleSize = widtargetbili;
                }else {
                    newOpts.inSampleSize = widtargetbili;
                }
            }
        }
        Log.i("缩放倍数:",String.valueOf(newOpts.inSampleSize));

        newOpts.inJustDecodeBounds = false;
        Bitmap result = BitmapFactory.decodeByteArray(byteArrayOutputStream.toByteArray(),0,byteArrayOutputStream.toByteArray().length,newOpts);

        Log.i("结果图片宽高:",String.valueOf(newOpts.outWidth) + "," + String.valueOf(newOpts.outHeight));
        // Bitmap回收
        recycleBitmap(bitmap);

        return compressByQuality(result);
    }

    /**
     * 通过压缩质量方式压缩
     * @param image
     * @return
     */
    public static Bitmap compressByQuality(Bitmap image) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        boolean canCompress = false;

        // 循环判断如果压缩后图片是否大于n kb,大于继续压缩
        while (byteArrayOutputStream.toByteArray().length / 1024 > MyApplication.IMAGE_SIZE) {

            canCompress = true;
            options -= 5;
            if(options <= 0){
                break;
            }

            // 重置baos
            byteArrayOutputStream.reset();
            // 这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, byteArrayOutputStream);

        }

        if(canCompress){
            // 把压缩后的数据baos存放到ByteArrayInputStream中
            ByteArrayInputStream isBm = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            // 把ByteArrayInputStream数据生成图片
            Bitmap bitmap = BitmapFactory.decodeStream(isBm);
            recycleBitmap(image);
            return bitmap;
        }else {
            return image;
        }
    }

    /**
     * Bitmap回收
     */
    public static void recycleBitmap(Bitmap bitmap){

        if(bitmap != null && !bitmap.isRecycled()){
            bitmap.recycle();
            bitmap = null;
        }
    }

}

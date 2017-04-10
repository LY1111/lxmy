package com.tuansbook.lvxing.Util;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.tuansbook.lvxing.Adapter.OfficialMyQuanAdapter;
import com.tuansbook.lvxing.Model.TieZi;
import com.tuansbook.lvxing.R;

import java.util.ArrayList;
import java.util.List;

import static com.tuansbook.lvxing.R.mipmap.wechat_friends;
import static com.tuansbook.lvxing.R.mipmap.wechat_quan;

/**
 * Created by Administrator on 2017/2/21.
 */
public class MyUtil {

    /**
     * 从Resource获取颜色
     */
    public static int getColor(Context context, int resource){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            return context.getResources().getColor(resource,null);
        }else {
            return context.getResources().getColor(resource);
        }
    }

    /**
     * 根据手机的分辨率从 dp 单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * Get a file path from a Uri. This will get the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     * 获取Storage Access Framework中文件的真实路径
     * 图片选择，文件选择
     * @param context The context.
     * @param uri The Uri to query.
     */
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        // SAF从Android4.4开始引入
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * Pop-分享
     */
    public static void showSharePopUp(Context context, ViewGroup group){

        try {
            // Popup
            View view = LayoutInflater.from(context).inflate(R.layout.fenxiang_inflate,group,false);
            PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.update();

            popupWindow.showAtLocation(group, Gravity.LEFT|Gravity.BOTTOM,0,0);

            // 各种分享及复制链接
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.pop_share_recycler);

            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setLayoutManager(new GridLayoutManager(context,3)); // 每行显示三个

            List<TieZi> tieZiList = new ArrayList<>();

            TieZi qq_friends = new TieZi();
            qq_friends.setTitle("QQ好友");
            qq_friends.setId(String.valueOf(R.mipmap.qq_friends));
            tieZiList.add(qq_friends);

            TieZi qq_square = new TieZi();
            qq_square.setTitle("QQ空间");
            qq_square.setId(String.valueOf(R.mipmap.qq_square));
            tieZiList.add(qq_square);

            TieZi weChat_friends = new TieZi();
            weChat_friends.setTitle("微信好友");
            weChat_friends.setId(String.valueOf(wechat_friends));
            tieZiList.add(weChat_friends);

            TieZi weChat_quan = new TieZi();
            weChat_quan.setTitle("朋友圈");
            weChat_quan.setId(String.valueOf(wechat_quan));
            tieZiList.add(weChat_quan);

            TieZi sina_square = new TieZi();
            sina_square.setTitle("新浪微博");
            sina_square.setId(String.valueOf(R.mipmap.sina_square));
            tieZiList.add(sina_square);

            TieZi copy = new TieZi();
            copy.setTitle("复制链接");
            copy.setId(String.valueOf(R.mipmap.copy_link));
            tieZiList.add(copy);

            OfficialMyQuanAdapter adapter = new OfficialMyQuanAdapter(context,tieZiList);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

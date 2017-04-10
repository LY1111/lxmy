package com.tuansbook.lvxing.OKHttp;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * Created by Administrator on 2017/2/21.
 * volley是一个简单的异步http库，仅此而已。缺点是不支持同步，这点会限制开发模式；不能post大数据，所以不适合用来上传文件
 * okhttp是高性能的http库，支持同步、异步，而且实现了spdy、http2、websocket协议，api很简洁易用，和volley一样实现了http协议的缓存。
 * 利用拦截器对请求主体进行GZip压缩TODO
 */
public class MyOKHttp {

    public static final String BASE_URL = "http://139.129.193.65:812/"; // 根地址
    private static final MediaType MEDIA_TYPE_STR = MediaType.parse("application/text;charset=utf-8"); // String
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8"); // JSON
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown;charset=utf-8"); // markdown 文档
    private static final MediaType MEDIA_TYPE_OBJECT = MediaType.parse("application/octet-stream"); // 上传文件时用到

    private OkHttpClient okHttpClient;
    private Handler okHttpHandler; // 全局处理子线程和主线程通信
    // volatile : 修饰的成员变量在每次被线程访问时，都强迫从共享内存中重读该成员变量的值。而且，当成员变量发生变化时，强迫线程将变化值回写到共享内存。
    // static :   在程序中任何变量或者代码都是在编译时由系统自动分配内存来存储的，而所谓静态就是指在编译后所分配的内存会一直存在，直到程序退出内存才会释放这个空间，也就是只要程序在运行，那么这块内存就会一直存在
    private static volatile MyOKHttp myOKHttp;

    private MyOKHttp(Context context){

        try {
            final File cache;
            if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
                cache = context.getExternalCacheDir(); // /storage/emulated/0/Android/data/com.tuansbook.lvxing/cache
                Log.i("getExternalCacheDir：",cache.getAbsolutePath());


            }else {
                cache = context.getCacheDir(); // // /data/user/0/com.tuansbook.lvxing/cache
            }

            if(!cache.exists()){
                cache.mkdirs();
            }

            // 初始化OkHttpClient和Handler 缓存(Okhttp仅支持get请求缓存) Cookie sslSocketFactory(Https) FormBody MultipartBody newWebSocket TODO
            okHttpClient = new OkHttpClient().newBuilder()
                    .addInterceptor(new LOGInterceptor()) // 自动打印URL和请求结果
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(20,TimeUnit.SECONDS) // 写入无超时
                    .retryOnConnectionFailure(true) // 失败自动重连
                    .cache(new Cache(cache,10*1024*1024)) // 10M 缓存
                    .build();

            okHttpHandler = new Handler(context.getMainLooper());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 单例模式
     */
    public static MyOKHttp getInstance(Context context){

        MyOKHttp ins = myOKHttp;

        if(ins == null){
            synchronized (MyOKHttp.class){
                ins = myOKHttp;
                if(ins == null){
                    ins = new MyOKHttp(context.getApplicationContext());
                    myOKHttp = ins;
                }
            }
        }
        return myOKHttp;
    }

    /**
     *
     * 异步处理请求_POST_FormBody_键值对
     */
    public <T> void requestAsyn(String tag,String url, Map<String,String> map, String body, final OKHttpCallBack<T> callBack){

        FormBody.Builder form_body_builder = new FormBody.Builder();
        RequestBody requestBody = null;

        // 加入参数
        if(map != null){
            for(String key:map.keySet()){
                form_body_builder.add(key,map.get(key));
            }
        }

        requestBody = form_body_builder.build();

        if(body != null){
            requestBody = RequestBody.create(MEDIA_TYPE_STR,body);
        }

        Request request = new Request.Builder().url(url).post(requestBody).tag(tag).build(); // POST

        // 每个Call对象只能执行一次请求 通过newCall可复制相同配置的Call对象 做到重复请求
        okHttpClient.newCall(request).enqueue(new Callback() { // 异步 enqueue
            @Override
            public void onFailure(Call call, IOException e) {
                FailBack("请求失败",callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if(response.isSuccessful()){
                    // 对于超过1MB的响应body，应使用流的方式来处理body byteStream或charStream TODO
                    // http://www.jianshu.com/p/a71a42f4634b
                    SucessBack((T)response.body().toString(),callBack);

                    // 关闭ResponseBody
                    response.body().close();
                }else {
                    FailBack("服务器错误",callBack);
                }
            }
        });
    }

    /**
     * 上传文件
     */
    public <T> void uploadFile(String tag, String url, String filepath, final OKHttpCallBack<T> callBack){

        File file = new File(filepath);
        if(file.exists()){

            RequestBody requestBody = RequestBody.create(MEDIA_TYPE_OBJECT,file);
            final Request request = new Request.Builder().url(BASE_URL + url).post(requestBody).tag(tag).build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    FailBack("上传失败",callBack);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    if(response.isSuccessful()){
                        SucessBack((T)response.body().toString(),callBack);

                        // 关闭ResponseBody
                        response.body().close();
                    }else {
                        FailBack("服务器错误",callBack);
                    }
                }
            });
        }
    }

    /**
     * 不带进度条的文件下载
     */
    public <T> void downloadFile(String tag, String url, String save_path, final OKHttpCallBack<T> callBack){

        String filename = ""; // TODO

        final File file = new File(save_path,filename);

        if(file.exists()){
            SucessBack((T)file,callBack);
            return;
        }

        Request request = new Request.Builder().url(BASE_URL + url).tag(tag).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                FailBack("下载失败",callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                InputStream inputStream = null;
                FileOutputStream fileOutputStream = null;

                try {
                    byte[] buf = new byte[1024];
                    int read;

                    inputStream = response.body().byteStream();
                    fileOutputStream = new FileOutputStream(file);

                    while ((read = inputStream.read(buf)) != -1){
                        fileOutputStream.write(buf,0,read);
                    }

                    fileOutputStream.flush();
                    SucessBack((T)file,callBack);
                    response.body().close();

                } catch (Exception e) {
                    e.printStackTrace();
                    FailBack("下载失败",callBack);
                    response.body().close();
                }finally {

                    if(inputStream != null){
                        inputStream.close();
                    }

                    if(fileOutputStream != null){
                        fileOutputStream.close();
                    }
                }
            }
        });
    }

    /**
     * 带进度条的文件下载
     */
    public <T> void downloadFileWithProgress(final String tag, String url, String save_path, final ProgressOKHttpCallBack<T> callBack){

        String fileName = ""; // TODO

        final File file = new File(save_path,fileName);

        if(file.exists()){
            SucessBack((T)file,callBack);
            return;
        }

        Request request = new Request.Builder().url(BASE_URL + url).tag(tag).build(); // POST?
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                FailBack("下载失败",callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                InputStream inputStream = null; // 文件读写 TODO
                FileOutputStream fileOutputStream = null;

                try {

                    byte[] buf = new byte[1024];

                    long current = 0;
                    int read;

                    inputStream = response.body().byteStream();
                    fileOutputStream = new FileOutputStream(file);

                    while ((read = inputStream.read(buf)) != -1){
                        current += read;
                        fileOutputStream.write(buf,0,read);
                        ProgressBack(response.body().contentLength(),current,callBack);
                    }

                    fileOutputStream.flush();
                    SucessBack((T)file,callBack);
                    response.body().close();

                } catch (Exception e) {
                    e.printStackTrace();
                    FailBack("下载失败",callBack);
                    response.body().close();
                }finally {

                    if(inputStream != null){
                        inputStream.close();
                    }

                    if(fileOutputStream != null){
                        fileOutputStream.close();
                    }
                }
            }
        });
    }

    /**
     * 请求成功后将结果返回到OKHttpCallBack的onSucess
     * okHttpHandler
     */
    private <T> void SucessBack(final T result, final OKHttpCallBack<T> callBack){

        okHttpHandler.post(new Runnable() {
            @Override
            public void run() {
                if(callBack != null){
                    callBack.onSucess(result);
                }
            }
        });
    }

    /**
     * 请求失败后将结果返回到OKHttpCallBack的onFailure
     * okHttpHandler
     */
    private <T> void FailBack(final String msg, final OKHttpCallBack<T> callBack){

        okHttpHandler.post(new Runnable() {
            @Override
            public void run() {

                if(callBack != null){
                    callBack.onFailure(msg);
                }
            }
        });
    }

    /**
     * 进度回调
     * @param total
     * @param current
     * @param callBack
     * @param <T>
     */
    private <T> void ProgressBack(final long total, final long current, final ProgressOKHttpCallBack<T> callBack){

        okHttpHandler.post(new Runnable() {
            @Override
            public void run() {

                if(callBack != null){
                    callBack.onProgress(total,current);
                }
            }
        });
    }

    /**
     * 创建带进度条的RequestBody
     * @param mediaType
     * @param file
     * @param callBack
     * @param <T>
     * @return RequestBody
     */
    private <T> RequestBody createProgressRequestBody(final MediaType mediaType, final File file, final ProgressOKHttpCallBack<T> callBack){

        RequestBody requestBody = new RequestBody() {
            @Override
            public MediaType contentType() {
                return mediaType;
            }

            @Override
            public long contentLength(){
                return file.length();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {

                Source source;
                source = Okio.source(file);

                long current = 0;
                long remaining = contentLength();

                long read;
                Buffer buffer = new Buffer();

                while ((read = source.read(buffer,1024)) != -1){
                    sink.write(buffer,read);
                    current += read;
                    ProgressBack(remaining,current,callBack);
                }
            }
        };
        return requestBody;
    }
}

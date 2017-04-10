package com.tuansbook.lvxing.OKHttp;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/22.
 * OKHttp应用拦截器，用于在请求发送前打印URL和接收到响应后打印请求结果
 * http://blog.csdn.net/qq_19431333/article/details/53053367
 *
 * 拦截器分为应用拦截器和网络拦截器。应用拦截器是在发送请求之前和获取到响应之后进行操作的，网络拦截器是在进行网络获取前进行操作的。(addInterceptor addNetworkInterceptor).
 */
public class LOGInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Log.i("请求地址：",request.toString());

        Response response = chain.proceed(request);
        Log.i("请求结果：",response.toString());

        return response;
    }
}

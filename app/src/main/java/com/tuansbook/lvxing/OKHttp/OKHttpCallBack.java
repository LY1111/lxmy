package com.tuansbook.lvxing.OKHttp;

/**
 * Created by Administrator on 2017/2/22.
 * OKHttp请求结果
 */
public interface OKHttpCallBack<T> {

    /**
     * 请求成功
     */
    void onSucess(T result);

    /**
     * 请求失败
     */
    void onFailure(String msg);
}

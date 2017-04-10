package com.tuansbook.lvxing.OKHttp;

/**
 * Created by Administrator on 2017/2/22.
 */
public interface ProgressOKHttpCallBack<T> extends OKHttpCallBack<T> {

    /**
     * 请求进度
     * @param total
     * @param current
     */
    void onProgress(long total,long current);
}

package com.acmenhe.mylibrary.http;



/**
 * author: HePeng
 * Date: 2021/4/15 11:01
 * e-mail: rkyyn@qq.com
 * description：
 */
public interface IBaseView {

    void onResultSuccess(String url, String json);
    /**
     * 失败
     * @param url
     * @param json
     */
    void onResultFailed(String url, Object obj, String json);
    /**
     * 异常回调方法
     *
     * @param e 异常
     */
    void onExceptions(Throwable e);
}

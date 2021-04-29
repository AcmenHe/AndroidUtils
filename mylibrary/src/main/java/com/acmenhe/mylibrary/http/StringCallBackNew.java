package com.acmenhe.mylibrary.http;

import android.util.Log;

import com.acmenhe.mylibrary.utils.GsonUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * author: HePeng
 * Date: 2021/4/15 11:01
 * e-mail: rkyyn@qq.com
 * description：
 */
public class StringCallBackNew<T extends IBaseResult> implements Observer<ResponseBody> {


    private IBaseView baseView;
    private Disposable disposable;
    private String url;
    private Class<T> t;

    public StringCallBackNew(IBaseView baseView, String url) {
        this.baseView = baseView;
        this.url = url;
    }

    public StringCallBackNew(IBaseView baseView, String url, Class<T> clazz) {
        this.baseView = baseView;
        this.url = url;
        t = clazz;
    }


    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }

    @Override
    public void onNext(ResponseBody stringResult) {
        try {
            String data = stringResult.string();
            Log.e("=====数据=====", data);
            IBaseResult baseBean = GsonUtils.getInstance().fromJson(data, t);
            if (baseBean.isSuccess()) {
                baseView.onResultSuccess(url, data);
                Log.e("=====成功=====", data);
            } else {
                baseView.onResultFailed(url, baseBean, data);
                Log.e("=====失败=====", data);
            }
        } catch (Exception e) {
            Log.e("=====失败=====", e.getMessage());
            e.printStackTrace();
            baseView.onExceptions(e);
        }
    }

    @Override
    public void onError(Throwable e) {
        if(e!=null){
            e.printStackTrace();
        }
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        baseView.onExceptions(e);
    }

    @Override
    public void onComplete() {
        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();
    }
}

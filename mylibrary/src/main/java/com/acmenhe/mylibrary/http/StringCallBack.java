package com.acmenhe.mylibrary.http;

import android.util.Log;

import com.acmenhe.mylibrary.bean.BaseBean;
import com.acmenhe.mylibrary.utils.GsonUtils;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * author: HePeng
 * Date: 2021/4/15 11:01
 * e-mail: rkyyn@qq.com
 * description：
 */
public class StringCallBack implements Observer<ResponseBody> {


    private IBaseView baseView;
    private Disposable disposable;
    private  String url;

    public StringCallBack(IBaseView baseView, String url) {
        this.baseView = baseView;
        this.url=url;
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
            IBaseResult baseBean = GsonUtils.getInstance().fromJson(data, BaseBean.class);
        if (baseBean.isSuccess()) {
            baseView.onResultSuccess(url,data);
            Log.e("=====成功=====", data);
        } else {
            baseView.onResultFailed(url,baseBean,data);
            Log.e("=====失败=====", data);
         }
        } catch (IOException e) {
            Log.e("=====失败=====", e.getMessage());
            e.printStackTrace();
            baseView.onExceptions(e);
        }
    }

    @Override
    public void onError(Throwable e) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        Log.e("onError:" , e.getMessage() + "=====失败=====");
        baseView.onExceptions(e);
    }

    @Override
    public void onComplete() {
        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();
    }
}

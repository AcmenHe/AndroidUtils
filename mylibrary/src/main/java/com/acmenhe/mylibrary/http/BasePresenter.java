package com.acmenhe.mylibrary.http;



import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;


public class BasePresenter {

    protected IBaseView baseView;

    private ApiManager apiManager;

    /***
     *
     * @param url
     * @param baseView
     */
    public BasePresenter(String url, IBaseView baseView) {
        this.baseView = baseView;
         apiManager = ApiManager.getApiManager(url);
    }
    public BasePresenter(String url, IBaseView baseView, Interceptor interceptor) {
        this.baseView = baseView;
         apiManager = ApiManager.getApiManager(url,interceptor);
    }

    protected <T> T getService(Class<T> service) {
        return apiManager.create(service);
    }

    protected <T> ObservableTransformer<T, T> compose() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

}

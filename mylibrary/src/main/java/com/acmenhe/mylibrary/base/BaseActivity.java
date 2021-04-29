package com.acmenhe.mylibrary.base;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.acmenhe.mylibrary.R;
import com.acmenhe.mylibrary.bean.BaseBean;
import com.acmenhe.mylibrary.http.IBaseView;
import com.acmenhe.mylibrary.utils.ActivityCollector;
import com.acmenhe.mylibrary.utils.DialogLoading;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * author: HePeng
 * Date: 2021/4/28 11:15
 * e-mail: rkyyn@qq.com
 * description：activity基类
 */
public abstract class BaseActivity extends RxAppCompatActivity
//        implements LifecycleProvider<ActivityEvent>
implements IBaseView {
    protected abstract int getLayoutId();

    protected abstract void initView();

    protected DialogLoading mLoadingView;
    private BaseActivity baseActivity;
    protected ImmersionBar mImmersionBar;
    protected RxPermissions mRxPermission;
    protected Toast mToast = null;

    @Override
    @CallSuper
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        baseActivity = this;
        ActivityCollector.addActivity(this);

        Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Long>bindToLifecycle())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {

                    }
                });
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        ActivityCollector.removeActivity(this);
        super.onDestroy();
    }

    public void showLoading() {
        if (mLoadingView == null) {
            mLoadingView = new DialogLoading(baseActivity);
        }
        mLoadingView.show();
    }

    public void hideLoading() {
        if (mLoadingView != null) {
            mLoadingView.dismiss();
        }
    }

    /**
     * }
     * /**
     * 显示一个Toast信息
     *
     * @param content
     */
    public void showToast(String content) {
        try {
            if (mToast == null) {
                mToast = Toast.makeText(this, content, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(content);
            }
            mToast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置标题栏透明 重叠布局
     * Init immersion bar.
     */
    protected void initImmersionBars() {
        if (null != mImmersionBar) {
            mImmersionBar.reset();
        }
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.transparentStatusBar();
        mImmersionBar.statusBarDarkFont(true);
        mImmersionBar.transparentNavigationBar();
        mImmersionBar.fullScreen(true);
        mImmersionBar.statusBarDarkFont(true);
        mImmersionBar.keyboardEnable(true);
        mImmersionBar.navigationBarColor(android.R.color.transparent);
        mImmersionBar.hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR);
        mImmersionBar.init();
    }

    /**
     * 设置标题栏透明 重叠布局
     * Init immersion bar.
     */
    protected void initImmersionBarsWhite() {
        if (null != mImmersionBar) {
            mImmersionBar.reset();
        }
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarColor(android.R.color.transparent);
        mImmersionBar.transparentStatusBar();
        mImmersionBar.transparentNavigationBar();
        mImmersionBar.keyboardEnable(true);
        mImmersionBar.flymeOSStatusBarFontColor(R.color.white);
        mImmersionBar.navigationBarColor(android.R.color.transparent);
        mImmersionBar.hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR);
        mImmersionBar.init();
    }

    /**
     * 检查权限
     * permission 获取方式： Manifest.permission.CAMERA
     */
    public void checkAppPermission(Consumer<Boolean> onNext, String... permission) {
        getRxPermission()
                .request(permission)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Boolean>bindToLifecycle())
                .subscribe(onNext, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showToast(throwable.getMessage());
                    }
                });
    }

    /**
     * 检查权限
     * permission 获取方式： Manifest.permission.CAMERA
     */
    protected void checkAppPermissions(String permission, Consumer<Permission> onNext) {
        getRxPermission()
                .requestEach(permission)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Permission>bindToLifecycle())
                .subscribe(onNext,
                new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showToast(throwable.getMessage());
                    }
                });
    }

    public RxPermissions getRxPermission() {
        if (mRxPermission == null)
            mRxPermission = new RxPermissions(this);
        return mRxPermission;
    }

    @Override
    public void onExceptions(Throwable e) {
        if(null!=e){
            if(!TextUtils.isEmpty(e.getMessage())){
                showToast(e.getMessage());
            }
        }
    }

    @Override
    public void onResultSuccess(String url, String json) {

    }

    @Override
    public void onResultFailed(String url,Object object,String json) {
        if(object instanceof BaseBean){
            BaseBean mBean=(BaseBean)object;
            showToast(mBean.getMsg()+"");
        }

    }
}

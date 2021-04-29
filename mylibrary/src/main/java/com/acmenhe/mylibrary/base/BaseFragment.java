package com.acmenhe.mylibrary.base;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.acmenhe.mylibrary.bean.BaseBean;
import com.acmenhe.mylibrary.http.IBaseView;
import com.acmenhe.mylibrary.utils.DialogLoading;
import com.acmenhe.mylibrary.utils.GsonUtils;
import com.trello.rxlifecycle2.components.support.RxFragment;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * author: HePeng
 * Date: 2021/4/28 14:20
 * e-mail: rkyyn@qq.com
 * description：
 */
public abstract class BaseFragment extends RxFragment implements IBaseView {

    protected abstract int getLayoutId();
    protected abstract void initView(View view);

    protected DialogLoading mLoadingView;
    protected View mRootView;
    private boolean firstLoad = true;
    protected BaseActivity baseActivity;
    protected Toast mToast = null;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            baseActivity = (BaseActivity) context;
        }
    }
    public BaseActivity getBaseActivity() {
        if(null == baseActivity){
            if (getActivity() instanceof BaseActivity) {
                baseActivity = (BaseActivity) this.getActivity();
            }
        }
        return baseActivity;
    }
    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (firstLoad) {
            inflaterView(inflater, container);
            initView(mRootView);
            firstLoad = false;
        }
        return mRootView;
    }

    /**
     * 设置View
     *
     * @param inflater
     * @param container
     */
    private void inflaterView(LayoutInflater inflater, @Nullable ViewGroup container) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutId(), container, false);
        }
    }
    /**
     * 显示一个Toast信息
     *
     * @param content
     */
    public void showToast(String content) {
        if (mToast == null) {
            mToast = Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(content);
        }
        mToast.show();
    }
    public void showLoading() {
        try {
            if (mLoadingView == null) {
                mLoadingView = new DialogLoading(getActivity());
            }
            if (mLoadingView != null) {
                mLoadingView.show();
            }
        } catch (Exception e) {
            Log.e("",e.getMessage());
        }
    }

    public void hideLoading() {
        if (mLoadingView != null) {
            mLoadingView.dismiss();
        }
    }

    @Override
    public void onExceptions(Throwable e) {
        if(!TextUtils.isEmpty(e.getMessage())){
            showToast(e.getMessage());
        }
    }
    protected void onBackPressed() {
        getBaseActivity().onBackPressed();
    }
    @Override
    public void onResultSuccess(String url, String obj) {

    }

    @Override
    public void onResultFailed(String url,Object object,String json){
        BaseBean baseBean = GsonUtils.getInstance().fromJson(json, BaseBean.class);
        if(baseBean instanceof BaseBean){
            BaseBean mBean=baseBean;
            showToast(mBean.getMsg()+"");
        }
    }

}

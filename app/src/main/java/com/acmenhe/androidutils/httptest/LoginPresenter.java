package com.acmenhe.androidutils.httptest;


import com.acmenhe.androidutils.httptest.Api;
import com.acmenhe.androidutils.httptest.ApiUrl;
import com.acmenhe.androidutils.httptest.Config;
import com.acmenhe.mylibrary.http.BasePresenter;
import com.acmenhe.mylibrary.http.IBaseView;
import com.acmenhe.mylibrary.http.StringCallBack;
import okhttp3.ResponseBody;

/**
 * java版
 */
public class LoginPresenter extends BasePresenter {

    public LoginPresenter(IBaseView baseView) {
        super(Config.ENDPOINT, baseView);
    }

    /**用户密码登陆*/
    public void  userLogin(String mobile , String userPwd,String smsCode,String loginType){
        getService(Api.class)
                .userLogin(mobile,userPwd,smsCode,loginType)
                .compose(this.<ResponseBody>compose())
                .subscribe(new StringCallBack(baseView, ApiUrl.USER_LOGIN));
    }

}

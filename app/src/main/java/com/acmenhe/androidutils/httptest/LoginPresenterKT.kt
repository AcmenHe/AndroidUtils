package com.acmenhe.androidutils.httptest

import com.acmenhe.mylibrary.http.BasePresenter
import com.acmenhe.mylibrary.http.IBaseView
import com.acmenhe.mylibrary.http.StringCallBack
import okhttp3.ResponseBody

/**
 * kotlin版
 */
class LoginPresenterKT(baseView: IBaseView) : BasePresenter(Config.ENDPOINT,baseView) {

    private var mPresenter = getService(Api::class.java)
    /**用户密码登陆*/
    fun userLogin(mobile : String , userPwd : String, smsCode : String, loginType : String){
        mPresenter.userLogin(mobile,userPwd,smsCode,loginType)
                .compose<ResponseBody>(compose<ResponseBody>())
                .subscribe(StringCallBack(baseView, ApiUrl.USER_LOGIN))
    }

}

package com.acmenhe.mylibrary.bean;

import android.text.TextUtils;

import com.acmenhe.mylibrary.http.IBaseResult;

import java.io.Serializable;

public class BaseBean implements Serializable, IBaseResult {
    private final static int SUCCESS_CODE=1;
    private  String status="";
    private  String msg="";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public boolean isSuccess() {
        return TextUtils.equals(status,""+SUCCESS_CODE);
    }
}


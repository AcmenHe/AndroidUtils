package com.acmenhe.mylibrary.base;

import android.app.Application;
import android.content.Context;

import com.acmenhe.mylibrary.R;

/**
 * author: HePeng
 * Date: 2021/4/15 11:01
 * e-mail: rkyyn@qq.com
 * description：
 */
public abstract class BaseApplication extends Application {
   
   private static BaseApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application=this;
    }
    public static BaseApplication getApplication() {
         return application;
    }
    public static Context  getAppContext() {
        return application.getApplicationContext();
    }

}

package com.acmenhe.httplib;

import android.util.Log;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CustomInterceptor implements Interceptor {
    //设缓存有效期为1天
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 1;
    //查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    public static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;

    private String sAuthorization = "";

    public CustomInterceptor() {
        sAuthorization = "";
    }

    public CustomInterceptor(String authorization) {
        sAuthorization = authorization;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        try {
            String url = original.url().toString();
            Log.e("请求", url);
        } catch (Exception e) {
        }
        String Authorization = "";
        if(sAuthorization!=null && !"".equals(sAuthorization)){
            Authorization = sAuthorization;
        }
        Request.Builder requestBuilder = original.newBuilder()
                .header("Authorization", Authorization)
                .header("Accept", "*/*")
                .method(original.method(), original.body());
        Request request = requestBuilder.build();
        Response response = chain.proceed(request);
        int maxAge = 20;
        return response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control", "public, max-age=" + maxAge)// 有网情况下最大缓存60s
                .build();
    }
}

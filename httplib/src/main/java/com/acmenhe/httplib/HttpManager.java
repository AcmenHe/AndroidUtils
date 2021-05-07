package com.acmenhe.httplib;

import android.util.Base64;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 *  网络访问管理
 **/
public class HttpManager {
    /** 接口链接 */
    private String sServerUrl = "";
    /** 超时时间 */
    private static final int TIMEOUT = 20;
    /** 时间单位 */
    private static final TimeUnit UNIT = TimeUnit.SECONDS;
    private Retrofit retrofit;
    private OkHttpClient okHttpClient;
    private Interceptor interceptor = null;
    private String sAuthorization ="";
    /** 默认地址 */
    private static String defaultUrl = "";
    /** 默认的用户名密码 */
    private static String defaultAuthorization = "";
    /** 默认拦截器 */
    private static Interceptor defaultInterceptor = null;
    /**
     * 初始化默认值
     * @param url
     */
    public static void setDefaultUrl(String url){
        defaultUrl = url;
    }
    public static void setDefaultInterceptor(Interceptor interceptor ){
        defaultInterceptor = interceptor;
    }
    public static void setDefaultAuth(String sAuthorization){
        defaultAuthorization = sAuthorization;
    }
    public static void setDefaultAuthBase64(String sUserName,String sPassword){
        defaultAuthorization = getAuthBase64(sUserName,sPassword);
    }


    public static HttpManager getInstance() {
        return new HttpManager(defaultUrl);
    }
    public static HttpManager getInstance(String url) {
        return new HttpManager(url);
    }

    private HttpManager(String url) {
        if (url == null || "".equals(url)) {
            throw new RuntimeException("baseUrl is null!");
        }
        sServerUrl = url;
//        getOkHttpClient();
    }
    /**
     * 自定义过滤器
     * @param interceptor 过滤器
     * @return
     */
    public HttpManager setInterceptor(Interceptor interceptor){
        this.interceptor = interceptor;
        return this;
    }

    /**
     * 设置访问链接的用户名密码
     * @param sAuthorization (已转码的字符串)
     * @return
     */
    public HttpManager setAuth(String sAuthorization){
        this.sAuthorization = sAuthorization;
        return this;
    }

    /**
     * 设置访问链接的用户名密码（Base64）
     * @param sUserName 用户名
     * @param sPassword 密码 可使用Blankj的utilcode EncryptUtils进行转码后传入
     * @return
     */
    public HttpManager setAuthBase64(String sUserName, String sPassword){
        this.sAuthorization = getAuthBase64(sUserName,sPassword);
        return this;
    }

    public static String getAuthBase64(String sUserName,String sPassword){
        //        String credentials = sUserName + ":" + EncryptUtils.encryptSHA256ToString(sPassword).toLowerCase();
        String credentials = sUserName + ":" + sPassword;
        String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        return basic;
    }

    /**
     * 主机名称校验
     *
     * @param hostUrls hostUrls
     * @return HostnameVerifier
     */
    private HostnameVerifier getHostnameVerifier(final String[] hostUrls) {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                for (String host : hostUrls) {
                    if (host.contains(hostname) || hostname.contains(host) || host.equalsIgnoreCase(hostname) || hostname.equalsIgnoreCase(host)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }
    /**
     * OkHttpClient
     */
    private void getOkHttpClient() {
        String[] hosts = {sServerUrl};
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (interceptor == null && defaultInterceptor !=null) {
            interceptor = defaultInterceptor;
        }
        if ((sAuthorization == null ||"".equals(sAuthorization) )&& (defaultAuthorization !=null && !"".equals(defaultAuthorization))) {
            sAuthorization = defaultAuthorization;
        }
        if (interceptor != null) {
            builder.addInterceptor(interceptor);
        }
//        Cache cache = new Cache(new File(mContext.getCacheDir(), "HttpCache"), 1024 * 1024 * 100);
        okHttpClient = builder
//                .cache(cache)
                .retryOnConnectionFailure(true)
                .addNetworkInterceptor(new LogInterceptor())
                .addInterceptor(new CustomInterceptor(sAuthorization))
                .hostnameVerifier(getHostnameVerifier(hosts))
                .connectTimeout(TIMEOUT, UNIT)
                .readTimeout(TIMEOUT, UNIT)
                .writeTimeout(TIMEOUT, UNIT)
                .build();
        //每次使用后调用清除传入的
        this.interceptor = null;
        retrofit = new Retrofit.Builder()
                .addConverterFactory(JsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(sServerUrl)
                .client(okHttpClient)
                .build();
    }
    /**
     * 获取 ApiService
     *
     * @param tClass 类型
     * @param <T>    类型
     * @return T
     */
    public <T> T create(Class<T> tClass) {
        getOkHttpClient();
        if (tClass == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(tClass);
    }

}

package com.acmenhe.mylibrary.http;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * author: HePeng
 * Date: 2021/4/28 15:31
 * e-mail: rkyyn@qq.com
 * description：
 */
public class ApiManager {
    /**
     * 接口链接
     */
    public static String ENDPOINT = "";
    /**
     * 超时时间
     */
    private static final int TIMEOUT = 20;
    /**
     * 时间单
     */
    private static final TimeUnit UNIT = TimeUnit.SECONDS;
    /**
     * OkHttp日志Tag
     */
    private static final String NET_TAG = "=====Net=====";

    private Retrofit retrofit;
    private OkHttpClient okHttpClient;

    private static ApiManager apiManager;
    private Interceptor interceptor = null;

    private ApiManager() {
        okHttpClient();
        getRetrofit();
    }

    private ApiManager(Interceptor interceptor) {
        this.interceptor = interceptor;
        okHttpClient();
        getRetrofit();
    }

    public static ApiManager getApiManager(String url) {
        ENDPOINT = url;
        return apiManager = new ApiManager();
    }

    public static ApiManager getApiManager(String url, Interceptor interceptor) {
        ENDPOINT = url;
        return apiManager = new ApiManager(interceptor);
    }

    public static ApiManager getApiManager() {
        return apiManager = new ApiManager();
    }

    /**
     * OkHttpClient
     */
    private void okHttpClient() {
        String[] hosts = {ENDPOINT};
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (interceptor != null) {
            builder.addInterceptor(interceptor);
        }
        okHttpClient = builder
                .retryOnConnectionFailure(true)
//                .cache(cache)
                .addNetworkInterceptor(new AppLoggerInterceptor(NET_TAG))
                .addInterceptor(new CacheInterceptor(false))
                .addNetworkInterceptor(new CacheInterceptor(false))
                .hostnameVerifier(getHostnameVerifier(hosts))// 校验主机名
                .connectTimeout(TIMEOUT, UNIT)
                .readTimeout(TIMEOUT, UNIT)
                .writeTimeout(TIMEOUT, UNIT)
//                .cookieJar(new PlayCookieJar())
                .build();
    }


    /**
     * 获取Retrofit
     */
    private void getRetrofit() {
        retrofit = new Retrofit.Builder()
//                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(JsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(ENDPOINT)
                .client(okHttpClient)
                .build();
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
     * 获取 ApiService
     *
     * @param tClass 类型
     * @param <T>    类型
     * @return T
     */
    public <T> T create(Class<T> tClass) {
        if (tClass == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(tClass);
    }
}

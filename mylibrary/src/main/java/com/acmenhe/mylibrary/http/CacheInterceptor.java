package com.acmenhe.mylibrary.http;

import android.util.Log;

import androidx.annotation.NonNull;

import com.acmenhe.mylibrary.utils.NetworkUtil;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CacheInterceptor implements Interceptor {
	private boolean printLog;

	public CacheInterceptor(boolean printLog) {
		this.printLog = printLog;
	}

	@Override
	public Response intercept(@NonNull Chain chain) throws IOException {
		Request request = chain.request();
		if (NetworkUtil.isNetworkAvailable()) {// 有效连接
			Response response = chain.proceed(request);
			// 有网情况下最大缓存60s read from cache for 60 s
			int maxAge = 10;
			String cacheControl = request.cacheControl().toString();
			if (printLog)
				Log.e("=====有网缓存=====", "10s load cache" + cacheControl);
			return response.newBuilder()
					.removeHeader("Pragma")
					.removeHeader("Cache-Control")
					.header("Cache-Control", "public, max-age=" + maxAge)// 有网情况下最大缓存60s
					.build();
		} else {
			if (printLog)
				Log.e("=====无网缓存=====", " no network load cache 3days");
			request = request.newBuilder()
					.cacheControl(CacheControl.FORCE_CACHE)
					.build();
			Response response = chain.proceed(request);
			//没网状态下缓存3天 set cache times is 3 days
			int maxStale = 60 * 60 * 24 * 3;
			return response.newBuilder()
					.removeHeader("Pragma")
					.removeHeader("Cache-Control")
					.header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
					.build();
		}
	}
}

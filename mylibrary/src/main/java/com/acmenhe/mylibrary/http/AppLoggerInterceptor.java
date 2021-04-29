package com.acmenhe.mylibrary.http;


import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import java.io.IOException;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 *
 * 日志拦截
 **/
public class AppLoggerInterceptor implements Interceptor {
    private static final String TAG = "=====OkHttpLogger=====";
    private String tag;
    /**
     * 是否打印响应日志，默认不打印
     */
    private boolean showResponse;

    private AppLoggerInterceptor(String tag, boolean showResponse) {
        if (TextUtils.isEmpty(tag)) {
            tag = TAG;
        }
        this.showResponse = showResponse;
        this.tag = tag;
    }

    public AppLoggerInterceptor(String tag) {
        this(tag, false);
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        logForRequest(request);// 请求日志
        Response response = chain.proceed(request);
        return showResponse ? logForResponse(response) : response;
    }

    /**
     * 打印请求日志
     *
     * @param request Request
     */
    private void logForRequest(Request request) {
        try {
            String lastUrl = request.url().toString();
            Headers headers = request.headers();

            Log.e(tag, "请求日志==========begin");
            Log.e(tag, "method : " + request.method());
            Log.e(tag, "url : " + lastUrl);

            if (headers != null && headers.size() > 0) {
                Log.e(tag, "headers : " + headers.toString());
            }

            RequestBody requestBody = request.body();
            if (requestBody != null) {
                MediaType mediaType = requestBody.contentType();
                if (mediaType != null) {
                    Log.e(tag, "requestBody's contentType : " + mediaType.toString());
                    if (isText(mediaType)) {
                        Log.e(tag, "requestBody's content : " + bodyToString(request));
                    } else {
                        Log.e(tag, "requestBody's content : " + bodyToString(request));
                    }
                }
            }
            Log.e(tag, "请求日志==========end");
        } catch (Exception e) {
            Log.e(tag, "请求日志=====" + e.getMessage() + "==========end");
        }
    }

    /**
     * 打印响应日志
     *
     * @param response Response
     * @return Response
     */
    private Response logForResponse(Response response) {
        try {
            Log.e(tag, "响应日志==========begin");
            Response.Builder builder = response.newBuilder();
            Response clone = builder.build();
            Log.e(tag, "url : " + clone.request().url());
            Log.e(tag, "code : " + clone.code());
            Log.e(tag, "protocol : " + clone.protocol());
            if (!TextUtils.isEmpty(clone.message()))
                Log.e(tag, "message : " + clone.message());
            ResponseBody body = clone.body();
            if (body != null) {
                MediaType mediaType = body.contentType();
                if (mediaType != null) {
                    Log.e(tag, "responseBody's contentType : " + mediaType.toString());
                    if (isText(mediaType)) {
                        String resp = body.string();
                        Log.e(tag, "responseBody's content : " + resp);

                        body = ResponseBody.create(mediaType, resp);
                        return response.newBuilder().body(body).build();
                    } else {
                        Log.e(tag, "responseBody's content : " + " maybe [file part] , too large too print , ignored!");
                    }
                }
            }
            Log.e(tag, "响应日志==========end");
        } catch (Exception e) {
            Log.e(tag, "响应日志=====" + e.getMessage() + "==========end");
        }

        return response;
    }

    private boolean isText(MediaType mediaType) {
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        if (mediaType.subtype() != null) {
            return mediaType.subtype().equals("json") || mediaType.subtype().equals("xml") ||
                    mediaType.subtype().equals("html") || mediaType.subtype().equals("webviewhtml");
        }
        return false;
    }

    private String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            if (copy.body() != null) {
                copy.body().writeTo(buffer);
            }
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "something error when show requestBody.";
        }
    }
}

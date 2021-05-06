package com.acmenhe.androidutils.httptest;

import org.json.JSONObject;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * author: HePeng
 * Date: 2021/4/28 16:24
 * e-mail: rkyyn@qq.com
 * description：
 */
public interface Api {

    /**用户密码登陆*/
    @POST(ApiUrl.USER_LOGIN)
    Observable<ResponseBody> userLogin(@Query("mobile")String mobile , @Query("userPwd")String userPwd ,
                  @Query("smsCode")String smsCode , @Query("loginType")String loginType);  /**用户密码登陆*/
    /**用户密码登陆*/
    @GET("api/task/taskinstance/getTaskStepInfo?client=2&runLogId=lwzccq20210322151906246179139&client=2&taskId=lwzccq20210320163525603861506")
    Observable<JSONObject> getTaskStepInfo();  /**用户密码登陆*/

}

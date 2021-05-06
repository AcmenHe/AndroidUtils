# AndroidUtils
## 使用方式

### 1. 网络库：implementation 'com.github.AcmenHe.AndroidUtils:httplib:v1.0.1'
```
                      HttpManager.getInstance(Config.serverUrl)
//                        .setAuthBase64(edt_name.getText().toString(),
//                                EncryptUtils.encryptSHA256ToString(
//                                        edt_password.getText().toString()
//                                ).toLowerCase())
                        .create(Api.class)
                        .getTaskList()
                        .compose(RxSchedulers.<JSONObject>applySchedulers())
                        .subscribe(new Consumer<JSONObject>() {
                            @Override
                            public void accept(JSONObject jsonObject) throws Exception {
                                tv_result.setText(jsonObject.toString());
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e("Throwable",throwable.getMessage());
                            }
                        });
```
Api接口
```
public interface Api {

    @GET("api/task/getTaskList")
    Observable<JSONObject> getTaskList();

}
```
### 2. mvp+网络库：implementation 'com.github.AcmenHe.AndroidUtils:mylibrary:v1.0.1'
Activity
```
   public class HttpTestActivity extends BaseActivity {

    private LoginPresenterKT mPresenter = null;
    private EditText edt_name;
    private EditText edt_password;
    private TextView tv_result;
    private Button btn_login;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_http_test;
    }

    @Override
    protected void initView() {
        edt_name = findViewById(R.id.edt_name);
        edt_password = findViewById(R.id.edt_password);
        btn_login = findViewById(R.id.btn_login);
        tv_result = findViewById(R.id.tv_result);
        mPresenter = new LoginPresenterKT(this);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.userLogin(
                        edt_name.getText().toString(),
                        edt_password.getText().toString(),
                        "",
                        "2"
                );
            }
        });
    }

    @Override
    public void onResultSuccess(String url, String json) {
        super.onResultSuccess(url, json);
        tv_result.setText(json);
    }

    @Override
    public void onResultFailed(String url, Object object, String json) {
        super.onResultFailed(url, object, json);
        tv_result.setText(json);
    }

    @Override
    public void onExceptions(Throwable e) {
        super.onExceptions(e);
        tv_result.setText(e.getMessage());
    }
}
```
Presenter
```
public class LoginPresenter extends BasePresenter {

    public LoginPresenter(IBaseView baseView) {
        super(Config.serverUrl, baseView);
    }
    
    public void  userLogin(String mobile , String userPwd,String smsCode,String loginType){
        getService(Api.class)
                .userLogin(mobile,userPwd,smsCode,loginType)
                .compose(this.<ResponseBody>compose())
                .subscribe(new StringCallBack(baseView, ApiUrl.USER_LOGIN));
    }

}
```
Api接口
```
public interface Api {

    /**用户密码登陆*/
    @POST(ApiUrl.USER_LOGIN)
    Observable<ResponseBody> userLogin(@Query("mobile")String mobile , @Query("userPwd")String userPwd ,
                  @Query("smsCode")String smsCode , @Query("loginType")String loginType);

    @GET("api/task/taskinstance/getTaskStepInfo?client=2&runLogId=lwzccq20210322151906246179139&client=2&taskId=lwzccq20210320163525603861506")
    Observable<JSONObject> getTaskStepInfo();

}
```
接口地址
```
public class ApiUrl {
    /**用户密码登陆*/
    public static final String USER_LOGIN = "user/login";
}
```
服务器地址
```
public class Config {
    public static  String serverUrl = "http://127.0.0.1:8080/test/";
}
```


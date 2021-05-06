package com.acmenhe.androidutils.httptest;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.acmenhe.androidutils.R;
import com.acmenhe.httplib.HttpManager;
import com.acmenhe.httplib.RxSchedulers;
import com.acmenhe.mylibrary.base.BaseActivity;

import org.json.JSONObject;

import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

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
//                mPresenter.userLogin(
//                        edt_name.getText().toString(),
//                        edt_password.getText().toString(),
//                        "",
//                        "2"
//                );
                HttpManager.getInstance(Config.serverUrl)
                        .setAuth("Basic MDQwMDg6ZWY3OTdjODExOGYwMmRmYjY0OTYwN2RkNWQzZjhjNzYyMzA0OGM5YzA2M2Q1MzJjYzk1YzVlZDdhODk4YTY0Zg==")
                        .create(Api.class)
                        .getTaskStepInfo()
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
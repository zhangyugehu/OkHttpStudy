package com.thssh.okhttpstudy;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.thssh.okhttpstudy.bean.ResponseBean;
import com.thssh.okhttpstudy.client.CommonOkHttpClient;
import com.thssh.okhttpstudy.listener.DisposeDataHandler;
import com.thssh.okhttpstudy.listener.DisposeDataListener;
import com.thssh.okhttpstudy.request.CommonRequest;
import com.thssh.okhttpstudy.request.RequestParms;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.btn_get)Button btnGet;
    @Bind(R.id.btn_post)Button btnPost;
    @Bind(R.id.tv_content)TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_get)
    public void getRequest(){
//        regularGet();
        coolGet();
    }

    @OnClick(R.id.btn_post)
    public void postRequest(){
//        regularPost();
        coolPost();
    }

    private void coolGet() {
        CommonOkHttpClient.get("http://www.baidu.com", null, new DisposeDataHandler(new DisposeDataListener() {
            @Override
            public void onSuccess(Object response) {
                tvContent.setText(response.toString());
            }

            @Override
            public void onFailure(Object error) {
                tvContent.setText(error.toString());
            }
        }));

    }

    private void coolPost() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userName", "hutianhang@docmail.cn");
        paramMap.put("password", "a123456");
        paramMap.put("clienttype", "mobile");
        RequestParms params = new RequestParms(paramMap);
        CommonOkHttpClient.post("http://mail.docmail.cn/auth/login", params, new DisposeDataHandler(new DisposeDataListener() {
            @Override
            public void onSuccess(Object response) {
                tvContent.setText(response.toString());
            }

            @Override
            public void onFailure(Object error) {
                tvContent.setText(error.toString());
            }
        }, ResponseBean.class));
    }

//    ********** old ************
    private void regularGet() {
        Request request = new Request.Builder()
                .url("http://www.baidu.com").build();
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                setText(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                setText(response.body().string());
            }
        });
    }

    private void regularPost() {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder form = new FormBody.Builder();
        form.add("userName", "hutianhang@docmail.cn");
        form.add("password", "a123456");
        form.add("clienttype", "mobile");
        Request req = new Request.Builder()
                .url("http://mail.docmail.cn/auth/login")
                .post(form.build())
                .build();
        Call call = client.newCall(req);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                setText(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                setText(response.body().string());
            }
        });
    }

    private void setText(final String text){
        tvContent.post(new Runnable() {
            @Override
            public void run() {
                tvContent.setText(text);
            }
        });
    }
}

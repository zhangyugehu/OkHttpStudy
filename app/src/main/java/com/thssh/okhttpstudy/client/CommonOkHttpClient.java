package com.thssh.okhttpstudy.client;

import android.os.Handler;
import android.os.Looper;

import com.thssh.okhttpstudy.https.HttpsUtils;
import com.thssh.okhttpstudy.listener.DisposeDataHandler;
import com.thssh.okhttpstudy.request.CommonRequest;
import com.thssh.okhttpstudy.request.RequestParms;
import com.thssh.okhttpstudy.response.CommonJsonCallBack;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by zhang on 2016/11/14.
 */

public class CommonOkHttpClient {
    private static final int TIME_OUT = 5;
    private static OkHttpClient okHttpClient;

    private static Handler mDelieverHandler;

    static {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        okHttpBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });
        okHttpBuilder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpBuilder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpBuilder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpBuilder.followRedirects(true); // 允许自定项
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        okHttpBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager); // 信任所有ssl证书

        okHttpClient = okHttpBuilder.build();
        mDelieverHandler = new Handler(Looper.getMainLooper());
    }

    public static void post(String url, RequestParms params, final DisposeDataHandler handler){
        try{
            Request request = CommonRequest.createPostRequest(url, params);
            Call call = okHttpClient.newCall(request);
            call.enqueue(new CommonJsonCallBack(handler));
        }catch (final Exception e){
            mDelieverHandler.post(new Runnable() {
                @Override
                public void run() {
                    handler.listener.onFailure(e);
                }
            });
        }
    }

    public static void get(String url, RequestParms params, final DisposeDataHandler handler){
        try {
            Request request = CommonRequest.createGetRequest(url, params);
            Call call = okHttpClient.newCall(request);
            call.enqueue(new CommonJsonCallBack(handler));
        }catch (final Exception e){
            mDelieverHandler.post(new Runnable() {
                @Override
                public void run() {
                    handler.listener.onFailure(e);
                }
            });
        }
    }
}

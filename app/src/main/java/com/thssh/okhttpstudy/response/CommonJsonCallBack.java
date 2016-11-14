package com.thssh.okhttpstudy.response;


import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.thssh.okhttpstudy.exception.OkHttpException;
import com.thssh.okhttpstudy.listener.DisposeDataHandler;
import com.thssh.okhttpstudy.listener.DisposeDataListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.logging.LogRecord;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by zhang on 2016/11/14.
 */

public class CommonJsonCallBack implements Callback {
    protected final String RESULT_CODE = "status";
    protected final int RESULT_CODE_DUCCESS = 1;
    protected final String ERROR_MSG = "message";
    protected final String EMPTY_MSG = "";

    protected final int NETWORK_ERROR = -1;
    protected final int JSON_ERROR = -2;
    protected final int OTHER_ERROR = -3;

    private DisposeDataListener listener;
    private Class<?> clazz;
    private Handler mDelieverHandler;

    public CommonJsonCallBack(DisposeDataHandler handler) {
        this.listener = handler.listener;
        this.clazz = handler.clazz;
        mDelieverHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        notifyFailure(e);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        final String result = response.body().string();
        handlerResponse(result);
    }

    private void handlerResponse(String result) {
        if(TextUtils.isEmpty(result)){
            notifyFailure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
            return;
        }
        try {
            JSONObject obj = new JSONObject(result);
            if(obj.has(RESULT_CODE)){
                if(obj.optInt(RESULT_CODE) == RESULT_CODE_DUCCESS){
                    if(clazz == null){
                        notifySuccess(obj);
                    }else{
                        Object clazzObj = null;
                        try {
                            clazzObj = JSON.parseObject(result, clazz);
                            if(clazzObj == null) {
                                notifyFailure(new OkHttpException(JSON_ERROR, EMPTY_MSG));
                            }else{
                                // 所有逻辑正常，返回实体对象
                                notifySuccess(clazzObj);
                            }
                        }catch (Exception e){
                            notifyFailure(new OkHttpException(JSON_ERROR, EMPTY_MSG));
                        }
                    }
                }else{
                    notifyFailure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
                }
            }else{
                notifyFailure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
            }
        }catch (Exception e){
            if(e instanceof JSONException){
                notifySuccess(result);
            }else {
                notifyFailure(new OkHttpException(OTHER_ERROR, EMPTY_MSG));
            }
        }
    }

    /**
     * 主线程回调成功信息
     * @param obj
     */
    private void notifySuccess(final Object obj){
        mDelieverHandler.post(new Runnable() {
            @Override
            public void run() {
                listener.onSuccess(obj);
            }
        });
    }

    /**
     * 主线程回调失败信息
     * @param obj
     */
    private void notifyFailure(final Object obj){
        mDelieverHandler.post(new Runnable() {
            @Override
            public void run() {
                listener.onFailure(obj);
            }
        });
    }
}

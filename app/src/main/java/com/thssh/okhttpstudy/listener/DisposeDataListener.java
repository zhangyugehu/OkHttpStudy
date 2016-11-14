package com.thssh.okhttpstudy.listener;

/**
 * Created by zhang on 2016/11/14.
 */

public interface DisposeDataListener {
    public void onSuccess(Object response);

    public void onFailure(Object error);
}
